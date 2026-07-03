package com.example.idealoop.feature.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.feature.memory.MemoryItem
import com.example.idealoop.feature.memory.MemoryListCard
import com.example.idealoop.feature.memory.MemorySource
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite

private val albumPhotoIds = (1..21).map { "p$it" }

private val relatedMemories = listOf(
    MemoryItem(
        id = "guangzhou",
        title = "广州周末旅行攻略",
        summary = "两日游路线：沙面、永庆坊、珠江夜游，适合轻松散步和拍照。",
        tags = listOf("旅行攻略", "周末"),
        source = MemorySource.Image,
        time = "5 月 28 日",
        place = "广州",
    ),
    MemoryItem(
        id = "taotaoju",
        title = "陶陶居早茶备忘",
        summary = "推荐虾饺、凤爪和奶黄包，上午 10 点前到店排队更短。",
        tags = listOf("早茶", "美食"),
        source = MemorySource.Text,
        time = "5 月 21 日",
        place = "广州",
    ),
    MemoryItem(
        id = "zhujiang",
        title = "珠江夜游订票链接",
        summary = "大沙头码头 20:00 航班视野更好，记得提前 30 分钟取票。",
        tags = listOf("行程", "夜景"),
        source = MemorySource.Link,
        time = "4 月 16 日",
    ),
)

@Composable
fun AlbumPickerScreen(
    onBack: () -> Unit,
    onSend: (List<String>) -> Unit,
) {
    val selected = remember { mutableStateListOf<String>() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(captureBackgroundBrush()),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            IdeaLoopTopBar(title = "相册", onBack = onBack)
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                itemsIndexed(albumPhotoIds, key = { _, id -> id }) { index, id ->
                    AlbumPhotoTile(
                        id = id,
                        index = index,
                        selected = id in selected,
                        onClick = {
                            if (id in selected) selected.remove(id) else selected.add(id)
                        },
                    )
                }
            }
            AlbumSendBar(
                count = selected.size,
                onSend = { if (selected.isNotEmpty()) onSend(selected.toList()) },
            )
        }
    }
}

@Composable
private fun AlbumPhotoTile(
    id: String,
    index: Int,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(2.dp))
            .background(photoBrush(index))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.85f), RoundedCornerShape(2.dp))
            .semantics {
                contentDescription = if (selected) "取消选择照片 $id" else "选择照片 $id"
            }
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    if (selected) Color(0x61101A36) else Color.Transparent,
                ),
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .size(20.dp)
                .clip(CircleShape)
                .background(
                    if (selected) {
                        Brush.linearGradient(listOf(Color(0xFF818CF8), Color(0xFF6366F1)))
                    } else {
                        Brush.linearGradient(listOf(IdeaLoopWhite.copy(alpha = 0.62f), IdeaLoopWhite.copy(alpha = 0.52f)))
                    },
                )
                .border(1.dp, IdeaLoopWhite.copy(alpha = 0.75f), CircleShape),
        ) {
            if (selected) {
                Icon(Icons.Outlined.Check, null, tint = IdeaLoopWhite, modifier = Modifier.size(12.dp))
            }
        }
    }
}

@Composable
private fun AlbumSendBar(count: Int, onSend: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(Color(0xE0F7F9FF), Color(0xEFF4F7FF))),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.85f))
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(40.dp)
                .clip(CircleShape)
                .background(
                    if (count > 0) {
                        Brush.linearGradient(listOf(IdeaLoopBlue900, Color(0xFF3B82F6)))
                    } else {
                        Brush.linearGradient(listOf(Color(0xFF94A3B8), Color(0xFFCBD5E1)))
                    },
                )
                .clickable(enabled = count > 0, onClick = onSend)
                .padding(horizontal = 16.dp),
        ) {
            Icon(Icons.AutoMirrored.Outlined.Send, null, tint = IdeaLoopWhite, modifier = Modifier.size(14.dp))
            Text(
                text = if (count > 0) "发送 · $count" else "发送",
                color = IdeaLoopWhite,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
fun ChatPhotosResultScreen(
    session: CaptureSession,
    onBack: () -> Unit,
    onMemory: (MemoryItem) -> Unit,
    onOpenAlbum: (ChatMode) -> Unit,
    onVoiceMessage: (ChatMode) -> Unit,
    onRecordMessage: (ChatMessageContent) -> Unit,
) {
    val photoIds = session.selectedPhotoIds
    val messages = remember(photoIds) {
        listOf(
            ChatMessage(0, ChatAuthor.Assistant, ChatMessageContent.Text(ChatMode.Chat.greeting)),
            ChatMessage(1, ChatAuthor.User, ChatMessageContent.Photos(photoIds)),
            ChatMessage(
                2,
                ChatAuthor.Assistant,
                ChatMessageContent.Text("收到 ${photoIds.size} 张照片，已为你找到 ${relatedMemories.size} 条相关记忆。"),
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
        extraContent = {
            RelatedMemoriesCard(onMemory = onMemory)
        },
    )
}

@Composable
private fun RelatedMemoriesCard(onMemory: (MemoryItem) -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 44.dp)
            .shadow(6.dp, shape)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(IdeaLoopWhite.copy(alpha = 0.92f), Color(0xFFF4F7FF).copy(alpha = 0.82f)),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .padding(12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("相关记忆", color = IdeaLoopBlue900, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
            Text("按相关度排序", color = IdeaLoopSlate500, fontSize = 10.5.sp)
        }
        relatedMemories.forEach { memory ->
            MemoryListCard(memory = memory, onClick = { onMemory(memory) })
        }
    }
}

@Preview(name = "P38 · 相册", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun AlbumPickerPreview() {
    IdeaLoopTheme { AlbumPickerScreen({}, {}) }
}

@Preview(name = "P39 · Chat 照片", device = "spec:width=412dp,height=915dp,dpi=440")
@Composable
private fun ChatPhotosPreview() {
    IdeaLoopTheme { ChatPhotosResultScreen(CaptureSession(), {}, {}, {}, {}, {}) }
}
