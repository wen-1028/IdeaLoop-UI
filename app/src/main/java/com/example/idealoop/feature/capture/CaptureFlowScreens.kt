package com.example.idealoop.feature.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopTheme
import kotlinx.coroutines.delay

@Composable
fun CaptureAnalysisScreen(
    session: CaptureSession,
    onBack: () -> Unit,
    onFinished: () -> Unit,
) {
    val messages = remember(session.pendingContent) {
        listOf(
            ChatMessage(0, ChatAuthor.Assistant, ChatMessageContent.Text(ChatMode.Record.greeting)),
            ChatMessage(1, ChatAuthor.User, session.pendingContent),
        )
    }
    LaunchedEffect(session.pendingContent) {
        delay(1_200)
        onFinished()
    }
    FlowChatPage(
        session = session,
        messages = messages,
        onBack = onBack,
        analyzing = true,
    )
}

@Composable
fun ChatVoiceResultScreen(
    session: CaptureSession,
    onBack: () -> Unit,
    onOpenAlbum: (ChatMode) -> Unit,
    onVoiceMessage: (ChatMode) -> Unit,
    onRecordMessage: (ChatMessageContent) -> Unit,
) {
    val messages = remember {
        listOf(
            ChatMessage(0, ChatAuthor.Assistant, ChatMessageContent.Text(ChatMode.Chat.greeting)),
            ChatMessage(
                1,
                ChatAuthor.User,
                ChatMessageContent.Voice("8\"", "帮我找一下广州周末旅行相关的记忆"),
            ),
            ChatMessage(
                2,
                ChatAuthor.Assistant,
                ChatMessageContent.Text("听到啦，正在为你检索相关记忆…"),
            ),
        )
    }
    FlowChatPage(
        session = session,
        messages = messages,
        onBack = onBack,
        onOpenAlbum = onOpenAlbum,
        onVoiceMessage = onVoiceMessage,
        onRecordMessage = onRecordMessage,
    )
}

@Composable
fun RecordVoiceAnalysisScreen(
    session: CaptureSession,
    onBack: () -> Unit,
    onFinished: () -> Unit,
) {
    LaunchedEffect(Unit) {
        session.prepare(
            ChatMessageContent.Voice(
                duration = "14\"",
                transcript = "这周末打算去广州，重点想去陶陶居吃早茶，再去珠江夜游",
            ),
        )
        delay(1_500)
        onFinished()
    }
    val messages = remember {
        listOf(
            ChatMessage(0, ChatAuthor.Assistant, ChatMessageContent.Text(ChatMode.Record.greeting)),
            ChatMessage(
                1,
                ChatAuthor.User,
                ChatMessageContent.Voice(
                    duration = "14\"",
                    transcript = "这周末打算去广州，重点想去陶陶居吃早茶，再去珠江夜游",
                ),
            ),
        )
    }
    FlowChatPage(
        session = session,
        messages = messages,
        onBack = onBack,
        analyzing = true,
    )
}

@Composable
internal fun FlowChatPage(
    session: CaptureSession,
    messages: List<ChatMessage>,
    onBack: () -> Unit,
    analyzing: Boolean = false,
    onOpenAlbum: (ChatMode) -> Unit = {},
    onVoiceMessage: (ChatMode) -> Unit = {},
    onRecordMessage: (ChatMessageContent) -> Unit = {},
    extraContent: (@Composable () -> Unit)? = null,
) {
    var draft by rememberSaveable { mutableStateOf("") }
    var voiceInput by rememberSaveable { mutableStateOf(false) }
    var nextId by rememberSaveable { mutableLongStateOf(100L) }
    val localMessages = remember { mutableStateListOf<ChatMessage>() }
    val listState = rememberLazyListState()
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val focusManager = LocalFocusManager.current

    fun sendDraft() {
        val content = parseLocalMessage(draft) ?: return
        draft = ""
        if (session.mode == ChatMode.Record) {
            focusManager.clearFocus()
            session.prepare(content)
            onRecordMessage(content)
        } else {
            localMessages += ChatMessage(nextId++, ChatAuthor.User, content)
        }
    }

    LaunchedEffect(localMessages.size, imeBottom) {
        val totalItems = messages.size +
            (if (analyzing) 1 else 0) +
            (if (extraContent != null) 1 else 0) +
            localMessages.size
        if ((localMessages.isNotEmpty() || imeBottom > 0) && totalItems > 0) {
            listState.animateScrollToItem(totalItems - 1)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(captureBackgroundBrush()),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
        ) {
            IdeaLoopTopBar(title = "小灵", onBack = onBack)
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(20.dp, 32.dp, 20.dp, 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(messages, key = { it.id }) { ChatMessageRow(it) }
                if (analyzing) {
                    item(key = "analyzing") { AnalyzingMessage() }
                }
                extraContent?.let { content -> item(key = "extra") { content() } }
                items(localMessages, key = { it.id }) { ChatMessageRow(it) }
            }
            ChatComposer(
                mode = session.mode,
                imeVisible = imeBottom > 0,
                voiceInput = voiceInput,
                draft = draft,
                onDraftChange = { draft = it },
                onModeChange = { session.mode = it },
                onAttachment = { onOpenAlbum(session.mode) },
                onVoice = { voiceInput = true },
                onKeyboard = { voiceInput = false },
                onVoiceSend = {
                    voiceInput = false
                    onVoiceMessage(session.mode)
                },
                onSend = ::sendDraft,
            )
        }
    }
}

@Composable
private fun AnalyzingMessage() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth(),
    ) {
        AssistantAvatar()
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFFFDFEFF), Color(0xFFEEF4FF)),
                    ),
                )
                .border(
                    1.dp,
                    Color.White.copy(alpha = 0.90f),
                    androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                )
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .semantics { contentDescription = "分析中" },
        ) {
            androidx.compose.material3.Text(
                text = "正在整理成记忆卡片",
                color = Color(0xFF31518F),
                fontSize = 12.5.sp,
            )
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (index == 1) Color(0xFF818CF8) else Color(0xFFA5B4FC)),
                )
            }
        }
    }
}

internal fun captureBackgroundBrush(): Brush = Brush.verticalGradient(
    colorStops = arrayOf(
        0f to IdeaLoopBackgroundTop,
        0.40f to IdeaLoopBackgroundMiddle,
        1f to IdeaLoopBackgroundBottom,
    ),
)

@Preview(name = "P07 · 分析中", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun CaptureAnalysisPreview() {
    IdeaLoopTheme {
        CaptureAnalysisScreen(CaptureSession().apply { mode = ChatMode.Record }, {}, {})
    }
}

@Preview(name = "P36 · Chat 语音", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun ChatVoicePreview() {
    IdeaLoopTheme { ChatVoiceResultScreen(CaptureSession(), {}, {}, {}, {}) }
}

@Preview(name = "P37 · Record 语音", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun RecordVoicePreview() {
    IdeaLoopTheme {
        RecordVoiceAnalysisScreen(CaptureSession().apply { mode = ChatMode.Record }, {}, {})
    }
}
