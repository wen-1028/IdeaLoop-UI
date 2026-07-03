package com.example.idealoop.feature.memory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.ui.components.CircularIconButton
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopSlate600
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite

@Composable
fun MemoryDetailScreen(
    detail: MemoryDetail = MemorySampleData.detail("guangzhou"),
    onBack: () -> Unit,
    onShare: () -> Unit = {},
    onRelatedMemory: (MemoryItem) -> Unit = {},
    modifier: Modifier = Modifier,
) {
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
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                IdeaLoopTopBar(
                    title = "记忆详情",
                    onBack = onBack,
                    rightContent = {
                        CircularIconButton(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "分享",
                            onClick = onShare,
                        )
                    },
                )
            },
        ) { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 8.dp,
                    end = 20.dp,
                    bottom = 32.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("memory_detail_list"),
            ) {
                item(key = "title") { MemoryHero(detail) }
                item(key = "summary") {
                    DetailSection(title = "摘要") {
                        Text(
                            text = detail.item.summary,
                            color = IdeaLoopSlate600,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                        )
                    }
                }
                item(key = "content") {
                    DetailSection(title = "内容") {
                        ScreenshotPreview()
                        Spacer(Modifier.size(12.dp))
                        Text(
                            text = detail.content,
                            color = IdeaLoopSlate600,
                            fontSize = 12.5.sp,
                            lineHeight = 19.sp,
                        )
                    }
                }
                item(key = "tags") {
                    DetailSection(title = "标签") {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            detail.detailTags.forEach { tag -> MemoryTag(tag) }
                        }
                    }
                }
                item(key = "related_title") {
                    DetailSection(title = "关联记忆") {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            detail.related.forEach { memory ->
                                MemoryListCard(
                                    memory = memory,
                                    onClick = { onRelatedMemory(memory) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MemoryHero(detail: MemoryDetail) {
    DetailSurface {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF06B6D4), Color(0xFF6366F1)),
                        ),
                    ),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = null,
                    tint = IdeaLoopWhite,
                    modifier = Modifier.size(18.dp),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = detail.item.title,
                    color = IdeaLoopBlue900,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.18.sp,
                )
                Spacer(Modifier.size(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Metadata(
                        icon = Icons.Outlined.AccessTime,
                        text = detail.savedTime,
                    )
                    detail.item.place?.let { place ->
                        Metadata(
                            icon = Icons.Outlined.LocationOn,
                            text = place,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Metadata(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = IdeaLoopSlate500,
            modifier = Modifier.size(11.dp),
        )
        Text(
            text = text,
            color = IdeaLoopSlate500,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: @Composable () -> Unit,
) {
    DetailSurface {
        Column {
            Text(
                text = title,
                color = IdeaLoopBlue900,
                fontSize = 13.5.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.size(if (title == "摘要") 8.dp else 12.dp))
            content()
        }
    }
}

@Composable
private fun DetailSurface(content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
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
                        IdeaLoopWhite.copy(alpha = 0.95f),
                        Color(0xFFF4F7FF).copy(alpha = 0.85f),
                    ),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .padding(16.dp),
    ) {
        content()
    }
}

@Composable
private fun ScreenshotPreview() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(5f / 3f)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFC7D2FE).copy(alpha = 0.55f),
                        Color(0xFFD8B4FE).copy(alpha = 0.45f),
                    ),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.70f), RoundedCornerShape(12.dp)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = null,
                tint = IdeaLoopBlue900,
                modifier = Modifier.size(22.dp),
            )
            Text(
                text = "原始截图预览",
                color = IdeaLoopBlue900,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Preview(
    name = "P12 · 记忆详情 · 360",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Preview(
    name = "P12 · 记忆详情 · 412",
    device = "spec:width=412dp,height=915dp,dpi=440",
    showBackground = true,
)
@Composable
private fun MemoryDetailPreview() {
    IdeaLoopTheme {
        MemoryDetailScreen(onBack = {})
    }
}
