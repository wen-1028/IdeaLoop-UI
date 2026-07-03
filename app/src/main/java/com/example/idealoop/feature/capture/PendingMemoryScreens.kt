package com.example.idealoop.feature.capture

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite

@Composable
fun PendingMemoryScreen(
    session: CaptureSession,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onReanalyze: () -> Unit,
) {
    val initial = remember(session.pendingContent) { pendingMemoryDraft(session.pendingContent) }
    var title by rememberSaveable(session.pendingContent) { mutableStateOf(initial.title) }
    var summary by rememberSaveable(session.pendingContent) { mutableStateOf(initial.summary) }
    var tags by rememberSaveable(session.pendingContent) { mutableStateOf(initial.tags.joinToString("、")) }
    var contentType by rememberSaveable(session.pendingContent) { mutableStateOf(initial.contentType) }
    var editing by rememberSaveable { mutableStateOf(false) }
    var composerDraft by rememberSaveable { mutableStateOf("") }
    var voiceInput by rememberSaveable { mutableStateOf(false) }
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)

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
                contentPadding = PaddingValues(20.dp, 32.dp, 20.dp, 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        AssistantAvatar()
                        MessageBubble(
                            message = ChatMessage(
                                id = 0,
                                author = ChatAuthor.Assistant,
                                content = ChatMessageContent.Text(
                                    if (editing) "点击各字段直接编辑，完成后点「完成」。" else "已生成记忆卡片，请确认后保存。",
                                ),
                            ),
                            assistant = true,
                        )
                    }
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top,
                    ) {
                        AssistantAvatar()
                        PendingMemoryCard(
                            title = title,
                            summary = summary,
                            tags = tags,
                            contentType = contentType,
                            editing = editing,
                            onTitleChange = { title = it },
                            onSummaryChange = { summary = it },
                            onTagsChange = { tags = it },
                            onContentTypeChange = { contentType = it },
                            onPrimary = {
                                if (editing) editing = false else onSave()
                            },
                            onModify = { editing = !editing },
                            onReanalyze = onReanalyze,
                        )
                    }
                }
            }
            ChatComposer(
                mode = ChatMode.Record,
                imeVisible = imeBottom > 0,
                voiceInput = voiceInput,
                draft = composerDraft,
                onDraftChange = { composerDraft = it },
                onModeChange = { session.mode = it },
                onAttachment = {},
                onVoice = { voiceInput = true },
                onKeyboard = { voiceInput = false },
                onVoiceSend = {},
                onSend = { composerDraft = "" },
            )
        }
    }
}

@Composable
private fun PendingMemoryCard(
    title: String,
    summary: String,
    tags: String,
    contentType: String,
    editing: Boolean,
    onTitleChange: (String) -> Unit,
    onSummaryChange: (String) -> Unit,
    onTagsChange: (String) -> Unit,
    onContentTypeChange: (String) -> Unit,
    onPrimary: () -> Unit,
    onModify: () -> Unit,
    onReanalyze: () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    Column(
        modifier = Modifier
            .widthIn(max = 280.dp)
            .shadow(6.dp, shape)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(IdeaLoopWhite.copy(alpha = 0.95f), Color(0xFFE8F0FF).copy(alpha = 0.88f)),
                ),
            )
            .border(1.dp, Color(0xFFC7D2FE).copy(alpha = 0.60f), shape),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
        ) {
            Icon(Icons.Outlined.AutoAwesome, null, tint = Color(0xFF6366F1), modifier = Modifier.size(14.dp))
            Text("待保存记忆卡片", color = Color(0xFF31518F), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            if (editing) {
                Spacer(Modifier.weight(1f))
                Text(
                    "编辑中",
                    color = Color(0xFF4338CA),
                    fontSize = 10.5.sp,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFE0E7FF).copy(alpha = 0.85f))
                        .padding(horizontal = 7.dp, vertical = 2.dp),
                )
            }
        }
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)) {
            MemoryField(Icons.Outlined.Description, "标题", title, editing, onTitleChange, "pending_title")
            MemoryField(Icons.Outlined.Layers, "摘要", summary, editing, onSummaryChange, "pending_summary")
            MemoryField(Icons.AutoMirrored.Outlined.Label, "标签", tags, editing, onTagsChange, "pending_tags")
            MemoryField(Icons.Outlined.PhotoCamera, "内容类型", contentType, editing, onContentTypeChange, "pending_type")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color(0xFFC7D2FE).copy(alpha = 0.30f))
                .background(Color(0xFFF4F7FF).copy(alpha = 0.50f))
                .padding(horizontal = 12.dp, vertical = 10.dp),
        ) {
            MemoryAction(text = if (editing) "完成" else "保存", primary = true, onClick = onPrimary, modifier = Modifier.weight(1f))
            MemoryAction(text = if (editing) "取消" else "修改", onClick = onModify, modifier = Modifier.weight(1f))
            MemoryAction(text = "重新分析", onClick = onReanalyze, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun MemoryField(
    icon: ImageVector,
    label: String,
    value: String,
    editing: Boolean,
    onValueChange: (String) -> Unit,
    testTag: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 9.dp),
    ) {
        Icon(icon, null, tint = Color(0xFF6366F1), modifier = Modifier.size(13.dp))
        Text(label, color = Color(0xFF31518F), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        if (editing) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                textStyle = TextStyle(color = Color(0xFF31518F), fontSize = 11.5.sp),
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(IdeaLoopWhite.copy(alpha = 0.85f))
                    .border(1.dp, Color(0xFFA5B4FC).copy(alpha = 0.70f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 5.dp)
                    .testTag(testTag),
            )
        } else {
            Text(
                value,
                color = Color(0xFF3F5688),
                fontSize = 11.5.sp,
                lineHeight = 16.sp,
                modifier = Modifier.weight(1f),
            )
            Icon(Icons.Outlined.ChevronRight, null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(12.dp))
        }
    }
}

@Composable
private fun MemoryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primary: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(36.dp)
            .clip(CircleShape)
            .background(
                if (primary) {
                    Brush.linearGradient(listOf(Color(0xFF818CF8), Color(0xFF6366F1), Color(0xFFA78BFA)))
                } else {
                    Brush.verticalGradient(listOf(IdeaLoopWhite, Color(0xFFE8F0FF)))
                },
            )
            .border(1.dp, if (primary) IdeaLoopWhite.copy(alpha = 0.40f) else Color(0xFFBAD2FF).copy(alpha = 0.60f), CircleShape)
            .clickable(onClick = onClick),
    ) {
        Text(text, color = if (primary) IdeaLoopWhite else Color(0xFF31518F), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun SaveSuccessDialogContent(onClose: () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .widthIn(max = 280.dp)
            .shadow(14.dp, shape)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(IdeaLoopWhite.copy(alpha = 0.96f), Color(0xFFF4F7FF).copy(alpha = 0.94f)),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .padding(20.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(IdeaLoopWhite, Color(0xFFEEF2FF))))
                .border(1.dp, IdeaLoopWhite.copy(alpha = 0.95f), CircleShape),
        ) {
            Icon(Icons.Outlined.Check, contentDescription = "完成", tint = IdeaLoopBlue900, modifier = Modifier.size(26.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text("已保存到你的记忆库", color = IdeaLoopBlue900, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(18.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(38.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(listOf(Color(0xFF818CF8), Color(0xFF6366F1))))
                .clickable(onClick = onClose),
        ) {
            Text("关闭", color = IdeaLoopWhite, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(name = "P09 · 待保存", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun PendingMemoryPreview() {
    IdeaLoopTheme {
        PendingMemoryScreen(CaptureSession().apply { mode = ChatMode.Record }, {}, {}, {})
    }
}

@Preview(name = "P10 · 保存成功", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun SaveSuccessPreview() {
    IdeaLoopTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x2E1E3A8A))
                .navigationBarsPadding()
                .padding(32.dp),
        ) {
            SaveSuccessDialogContent(onClose = {})
        }
    }
}
