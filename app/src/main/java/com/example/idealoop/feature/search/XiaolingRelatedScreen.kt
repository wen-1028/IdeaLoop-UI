package com.example.idealoop.feature.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.R
import com.example.idealoop.feature.capture.ChatComposer
import com.example.idealoop.feature.capture.ChatMessageContent
import com.example.idealoop.feature.capture.ChatMode
import com.example.idealoop.feature.capture.parseLocalMessage
import com.example.idealoop.feature.memory.MemoryItem
import com.example.idealoop.feature.memory.MemoryListCard
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite

@Composable
fun XiaolingRelatedScreen(
    query: String,
    onBack: () -> Unit,
    onMemory: (MemoryItem) -> Unit,
    modifier: Modifier = Modifier,
    initialMode: ChatMode = ChatMode.Chat,
    onModeChanged: (ChatMode) -> Unit = {},
    onRecordMessage: (ChatMessageContent) -> Unit = {},
    onOpenAlbum: (ChatMode) -> Unit = {},
    onVoiceMessage: (ChatMode) -> Unit = {},
) {
    var expanded by rememberSaveable { mutableStateOf(true) }
    var mode by rememberSaveable(initialMode) { mutableStateOf(initialMode) }
    var draft by rememberSaveable { mutableStateOf("") }
    var voiceInput by rememberSaveable { mutableStateOf(false) }
    var followUpMessages by remember { mutableStateOf(emptyList<String>()) }
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    fun sendDraft() {
        val content = parseLocalMessage(draft) ?: return
        draft = ""
        if (mode == ChatMode.Record) {
            onRecordMessage(content)
        } else {
            followUpMessages = followUpMessages + when (content) {
                is ChatMessageContent.Text -> content.text
                is ChatMessageContent.Link -> content.url
                is ChatMessageContent.Photos -> "发送了 ${content.ids.size} 张照片"
                is ChatMessageContent.Voice -> content.transcript
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to IdeaLoopBackgroundTop,
                        0.40f to IdeaLoopBackgroundMiddle,
                        1f to IdeaLoopBackgroundBottom,
                    ),
                ),
            ),
    ) {
        Scaffold(
            modifier = Modifier.imePadding(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                IdeaLoopTopBar(
                    title = "小灵",
                    onBack = onBack,
                )
            },
            bottomBar = {
                ChatComposer(
                    mode = mode,
                    imeVisible = imeVisible,
                    voiceInput = voiceInput,
                    draft = draft,
                    onDraftChange = { draft = it },
                    onModeChange = { selected ->
                        mode = selected
                        onModeChanged(selected)
                    },
                    onAttachment = { onOpenAlbum(mode) },
                    onVoice = { voiceInput = true },
                    onKeyboard = { voiceInput = false },
                    onVoiceSend = { onVoiceMessage(mode) },
                    onSend = ::sendDraft,
                )
            },
        ) { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 16.dp,
                    end = 20.dp,
                    bottom = 24.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                item(key = "user_message") {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ChatBubble(text = query, assistant = false)
                        UserAvatar()
                    }
                }
                item(key = "assistant_message") {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        AssistantAvatar()
                        ChatBubble(
                            text = "找到 ${SearchSampleData.xiaolingRelated.size} 条相关记忆，已按相关度排序。",
                            assistant = true,
                        )
                    }
                }
                item(key = "related") {
                    RelatedMemoriesPanel(
                        expanded = expanded,
                        onToggle = { expanded = !expanded },
                        onMemory = onMemory,
                        modifier = Modifier.padding(horizontal = 44.dp),
                    )
                }
                followUpMessages.forEachIndexed { index, message ->
                    item(key = "follow_up_$index") {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            ChatBubble(text = message, assistant = false)
                            UserAvatar()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserAvatar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(36.dp)
            .shadow(2.dp, CircleShape)
            .clip(CircleShape)
            .background(IdeaLoopWhite.copy(alpha = 0.90f))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.95f), CircleShape),
    ) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = "Wen",
            tint = Color(0xFF334155),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun AssistantAvatar() {
    Image(
        painter = painterResource(R.drawable.xiaoling_avatar),
        contentDescription = "小灵",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(36.dp)
            .shadow(2.dp, CircleShape)
            .clip(CircleShape)
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), CircleShape),
    )
}

@Composable
private fun ChatBubble(
    text: String,
    assistant: Boolean,
) {
    val shape = RoundedCornerShape(16.dp)
    Text(
        text = text,
        color = Color(0xFF31518F),
        fontSize = 13.sp,
        lineHeight = 20.sp,
        modifier = Modifier
            .widthIn(max = 252.dp)
            .shadow(
                elevation = if (assistant) 4.dp else 3.dp,
                shape = shape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.08f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.08f),
            )
            .clip(shape)
            .background(
                Brush.linearGradient(
                    if (assistant) {
                        listOf(IdeaLoopWhite.copy(alpha = 0.95f), Color(0xFFEEF4FF).copy(alpha = 0.88f))
                    } else {
                        listOf(IdeaLoopWhite.copy(alpha = 0.92f), Color(0xFFEEF2FF).copy(alpha = 0.85f))
                    },
                ),
            )
            .border(
                1.dp,
                if (assistant) IdeaLoopWhite.copy(alpha = 0.90f) else Color(0xFFE0E7FF).copy(alpha = 0.80f),
                shape,
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
    )
}

@Composable
private fun RelatedMemoriesPanel(
    expanded: Boolean,
    onToggle: () -> Unit,
    onMemory: (MemoryItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.07f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.07f),
            )
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        IdeaLoopWhite.copy(alpha = 0.92f),
                        Color(0xFFF4F7FF).copy(alpha = 0.82f),
                    ),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .padding(horizontal = 12.dp, vertical = if (expanded) 12.dp else 6.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = if (expanded) "收起相关记忆" else "展开相关记忆"
                }
                .clickable(onClick = onToggle)
                .padding(horizontal = 4.dp, vertical = 2.dp),
        ) {
            Text(
                text = "相关记忆",
                color = IdeaLoopBlue900,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.25.sp,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "按相关度排序",
                    color = IdeaLoopSlate500,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                )
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    contentDescription = null,
                    tint = IdeaLoopSlate500,
                    modifier = Modifier
                        .size(11.dp)
                        .rotate(if (expanded) 0f else -90f),
                )
            }
        }
        if (expanded) {
            Spacer(Modifier.size(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SearchSampleData.xiaolingRelated.forEach { memory ->
                    MemoryListCard(
                        memory = memory,
                        onClick = { onMemory(memory) },
                    )
                }
            }
        }
    }
}

@Preview(
    name = "P35 · 小灵相关记忆 · 360",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Preview(
    name = "P35 · 小灵相关记忆 · 412",
    device = "spec:width=412dp,height=915dp,dpi=440",
    showBackground = true,
)
@Composable
private fun XiaolingRelatedPreview() {
    IdeaLoopTheme {
        XiaolingRelatedScreen(
            query = "帮我找一下广州周末旅行相关的记忆",
            onBack = {},
            onMemory = {},
        )
    }
}
