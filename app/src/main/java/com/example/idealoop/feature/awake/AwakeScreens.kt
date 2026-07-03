package com.example.idealoop.feature.awake

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.feature.memory.MemoryItem
import com.example.idealoop.feature.memory.MemoryListCard
import com.example.idealoop.ui.components.GlassCard
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopIndigo500
import com.example.idealoop.ui.theme.IdeaLoopPurple400
import com.example.idealoop.ui.theme.IdeaLoopPurple500
import com.example.idealoop.ui.theme.IdeaLoopSlate400
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopSlate600
import com.example.idealoop.ui.theme.IdeaLoopSlate700
import com.example.idealoop.ui.theme.IdeaLoopSlate800
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite

@Composable
fun AwakeListScreen(
    onBack: () -> Unit,
    onItem: (AwakeItem) -> Unit,
    items: List<AwakeItem> = AwakeSampleData.items,
) {
    AwakeBackground {
        Column(Modifier.fillMaxSize()) {
            IdeaLoopTopBar(title = "今日唤醒", onBack = onBack)
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(items, key = { it.id }) { item ->
                    AwakeListCard(item = item, onClick = { onItem(item) })
                }
            }
        }
    }
}

@Composable
private fun AwakeListCard(item: AwakeItem, onClick: () -> Unit) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(154.dp)
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.08f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.08f),
            )
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(IdeaLoopWhite.copy(alpha = 0.92f), Color(0xFFF4F7FF).copy(alpha = 0.82f)),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .semantics { contentDescription = "唤醒卡片 ${item.id}" }
            .clickable(onClick = onClick)
            .padding(12.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            TriggerCircle(item.trigger)
            Text(
                text = item.meta,
                color = IdeaLoopSlate400,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = item.title,
            color = IdeaLoopSlate800,
            fontSize = 14.5.sp,
            lineHeight = 19.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = item.description,
            color = IdeaLoopSlate500,
            fontSize = 12.sp,
            lineHeight = 18.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            TriggerTag(item.trigger.label)
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = IdeaLoopPurple400,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
fun LocationAwakeDetailScreen(
    onBack: () -> Unit,
    onMemory: (MemoryItem) -> Unit,
) {
    AwakeDetailScreen(
        title = "位置唤醒",
        trigger = AwakeTrigger.Location,
        detail = AwakeSampleData.locationDetail,
        onBack = onBack,
        onMemory = onMemory,
    )
}

@Composable
fun TimeAwakeDetailScreen(
    onBack: () -> Unit,
    onMemory: (MemoryItem) -> Unit,
) {
    AwakeDetailScreen(
        title = "时间唤醒",
        trigger = AwakeTrigger.Time,
        detail = AwakeSampleData.timeDetail,
        onBack = onBack,
        onMemory = onMemory,
    )
}

@Composable
private fun AwakeDetailScreen(
    title: String,
    trigger: AwakeTrigger,
    detail: AwakeDetail,
    onBack: () -> Unit,
    onMemory: (MemoryItem) -> Unit,
) {
    AwakeBackground {
        Column(Modifier.fillMaxSize()) {
            IdeaLoopTopBar(title = title, onBack = onBack)
            LazyColumn(
                contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                item {
                    GlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = triggerIcon(trigger),
                                    contentDescription = null,
                                    tint = IdeaLoopIndigo500,
                                    modifier = Modifier.size(12.dp),
                                )
                                Text(
                                    text = detail.triggerLine,
                                    color = IdeaLoopIndigo500,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "唤醒原因",
                                color = IdeaLoopSlate800,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = detail.reason,
                                color = IdeaLoopSlate600,
                                fontSize = 13.sp,
                                lineHeight = 20.sp,
                            )
                        }
                    }
                }
                item { DetailSectionTitle("原始记忆") }
                item {
                    MemoryListCard(
                        memory = detail.originalMemory,
                        onClick = { onMemory(detail.originalMemory) },
                    )
                }
                item { DetailSectionTitle("相关记忆") }
                items(detail.relatedMemories, key = { it.id }) { memory ->
                    MemoryListCard(memory = memory, onClick = { onMemory(memory) })
                }
            }
        }
    }
}

@Composable
private fun DetailSectionTitle(text: String) {
    Text(
        text = text,
        color = IdeaLoopSlate700,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun TriggerCircle(trigger: AwakeTrigger) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(24.dp)
            .shadow(3.dp, CircleShape, ambientColor = IdeaLoopPurple500.copy(alpha = 0.35f))
            .clip(CircleShape)
            .background(Brush.linearGradient(listOf(IdeaLoopPurple500, IdeaLoopIndigo500))),
    ) {
        Icon(
            imageVector = triggerIcon(trigger),
            contentDescription = null,
            tint = IdeaLoopWhite,
            modifier = Modifier.size(11.dp),
        )
    }
}

@Composable
private fun TriggerTag(text: String) {
    Text(
        text = text,
        color = Color(0xFF7E22CE).copy(alpha = 0.85f),
        fontSize = 10.sp,
        lineHeight = 13.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFF3E8FF).copy(alpha = 0.85f))
            .border(1.dp, Color(0xFFD8B4FE).copy(alpha = 0.60f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 1.dp),
    )
}

private fun triggerIcon(trigger: AwakeTrigger): ImageVector = when (trigger) {
    AwakeTrigger.Location -> Icons.Outlined.LocationOn
    AwakeTrigger.Time -> Icons.Outlined.Timer
    AwakeTrigger.Relation -> Icons.Outlined.Link
}

@Composable
private fun AwakeBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
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
        content()
    }
}

@Preview(name = "P16 · 今日唤醒 · 360", device = "spec:width=360dp,height=800dp,dpi=440")
@Preview(name = "P16 · 今日唤醒 · 412", device = "spec:width=412dp,height=915dp,dpi=440")
@Composable
private fun AwakeListPreview() {
    IdeaLoopTheme { AwakeListScreen({}, {}) }
}

@Preview(name = "P17 · 位置唤醒", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun LocationAwakePreview() {
    IdeaLoopTheme { LocationAwakeDetailScreen({}, {}) }
}

@Preview(name = "P18 · 时间唤醒", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun TimeAwakePreview() {
    IdeaLoopTheme { TimeAwakeDetailScreen({}, {}) }
}
