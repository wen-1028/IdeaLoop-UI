package com.example.idealoop.feature.capture

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object CaptureContract {
    const val route = "P06"
    const val analysisRoute = "P07"
    const val pendingMemoryRoute = "P09"
    const val savedRoute = "P10"
    const val chatVoiceRoute = "P36"
    const val recordVoiceRoute = "P37"
    const val albumRoute = "P38"
    const val chatPhotosRoute = "P39"
}

enum class CaptureInputKind {
    Text,
    Link,
    Voice,
    Photos,
}

fun captureDestination(mode: ChatMode, inputKind: CaptureInputKind): String? = when (mode) {
    ChatMode.Chat -> when (inputKind) {
        CaptureInputKind.Text,
        CaptureInputKind.Link,
        -> null
        CaptureInputKind.Voice -> CaptureContract.chatVoiceRoute
        CaptureInputKind.Photos -> CaptureContract.chatPhotosRoute
    }
    ChatMode.Record -> when (inputKind) {
        CaptureInputKind.Voice -> CaptureContract.recordVoiceRoute
        CaptureInputKind.Text,
        CaptureInputKind.Link,
        CaptureInputKind.Photos,
        -> CaptureContract.analysisRoute
    }
}

enum class ChatMode(
    val label: String,
    val placeholder: String,
    val greeting: String,
) {
    Chat(
        label = "Chat",
        placeholder = "向小灵提问…",
        greeting = "嗨 Wen，有什么想问的，或者让我帮你搜索记忆？",
    ),
    Record(
        label = "Record",
        placeholder = "输入记忆内容…",
        greeting = "嗨 Wen，把截图、文字、语音或链接发给我，我会帮你整理成记忆卡片。",
    ),
}

enum class ChatAuthor {
    Assistant,
    User,
}

sealed interface ChatMessageContent {
    data class Text(val text: String) : ChatMessageContent
    data class Link(val url: String) : ChatMessageContent
    data class Voice(val duration: String, val transcript: String) : ChatMessageContent
    data class Photos(val ids: List<String>) : ChatMessageContent
}

data class ChatMessage(
    val id: Long,
    val author: ChatAuthor,
    val content: ChatMessageContent,
)

class CaptureSession {
    var mode by mutableStateOf(ChatMode.Chat)
    var pendingContent by mutableStateOf<ChatMessageContent>(
        ChatMessageContent.Text("帮我整理一下这个广州攻略"),
    )
    var selectedPhotoIds by mutableStateOf(listOf("p1", "p3", "p5"))

    fun prepare(content: ChatMessageContent) {
        pendingContent = content
    }

    fun preparePhotos(ids: List<String>) {
        selectedPhotoIds = ids
        pendingContent = ChatMessageContent.Photos(ids)
    }
}

data class PendingMemoryDraft(
    val title: String,
    val summary: String,
    val tags: List<String>,
    val contentType: String,
)

fun pendingMemoryDraft(content: ChatMessageContent): PendingMemoryDraft = PendingMemoryDraft(
    title = "广州周末旅行攻略",
    summary = "适合周末两日游，包含景点、餐厅和路线建议",
    tags = listOf("旅行攻略", "广州", "周末"),
    contentType = when (content) {
        is ChatMessageContent.Text -> "文字"
        is ChatMessageContent.Link -> "链接"
        is ChatMessageContent.Voice -> "语音"
        is ChatMessageContent.Photos -> "图片"
    },
)

fun initialChatMessages(mode: ChatMode): List<ChatMessage> = listOf(
    ChatMessage(
        id = 0L,
        author = ChatAuthor.Assistant,
        content = ChatMessageContent.Text(mode.greeting),
    ),
)

fun parseLocalMessage(draft: String): ChatMessageContent? {
    val text = draft.trim()
    if (text.isEmpty()) return null
    return if (text.matches(Regex("https?://\\S+", RegexOption.IGNORE_CASE))) {
        ChatMessageContent.Link(text)
    } else {
        ChatMessageContent.Text(text)
    }
}
