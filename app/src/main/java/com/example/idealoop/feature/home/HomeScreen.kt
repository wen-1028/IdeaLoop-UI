package com.example.idealoop.feature.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
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
import com.example.idealoop.feature.memory.MemoryListCard
import com.example.idealoop.ui.components.IdeaLoopBottomItem
import com.example.idealoop.ui.components.IdeaLoopBottomNavigation
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopIndigo300
import com.example.idealoop.ui.theme.IdeaLoopIndigo400
import com.example.idealoop.ui.theme.IdeaLoopIndigo500
import com.example.idealoop.ui.theme.IdeaLoopPurple400
import com.example.idealoop.ui.theme.IdeaLoopPurple500
import com.example.idealoop.ui.theme.IdeaLoopSlate400
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopSlate600
import com.example.idealoop.ui.theme.IdeaLoopSlate800
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite

data class HomeCallbacks(
    val onSearch: () -> Unit = {},
    val onActionSuggestions: () -> Unit = {},
    val onAwakeAll: () -> Unit = {},
    val onAwakeCard: (AwakeCard) -> Unit = {},
    val onRecentMemory: (RecentMemory) -> Unit = {},
    val onBottomItem: (IdeaLoopBottomItem) -> Unit = {},
    val onAdd: () -> Unit = {},
)

@Composable
fun HomeScreen(
    state: HomeUiState = HomeSampleData.normal,
    callbacks: HomeCallbacks = HomeCallbacks(),
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
        HomeBackgroundGlow()

        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = { IdeaLoopTopBar(title = HomeContract.title) },
            bottomBar = {
                IdeaLoopBottomNavigation(
                    activeItem = IdeaLoopBottomItem.Home,
                    onItemSelected = callbacks.onBottomItem,
                    onAdd = callbacks.onAdd,
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 32.dp),
            ) {
                SearchEntry(onClick = callbacks.onSearch)
                SummaryCards(
                    captureCount = state.captureCount,
                    suggestionCount = state.suggestionCount,
                    onActionSuggestions = callbacks.onActionSuggestions,
                )
                AwakeSection(
                    cards = state.awakeCards,
                    onAll = callbacks.onAwakeAll,
                    onCard = callbacks.onAwakeCard,
                )
                RecentMemoriesSection(
                    memories = state.recentMemories,
                    onMemory = callbacks.onRecentMemory,
                )
            }
        }
    }
}

@Composable
private fun HomeBackgroundGlow() {
    Canvas(Modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    IdeaLoopPurple400.copy(alpha = 0.25f),
                    IdeaLoopPurple400.copy(alpha = 0f),
                ),
                center = Offset(size.width * 0.20f, 0f),
                radius = size.maxDimension * 0.50f,
            ),
        )
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    IdeaLoopIndigo400.copy(alpha = 0.25f),
                    IdeaLoopIndigo400.copy(alpha = 0f),
                ),
                center = Offset(size.width * 0.90f, size.height),
                radius = size.maxDimension * 0.50f,
            ),
        )
    }
}

@Composable
private fun SearchEntry(onClick: () -> Unit) {
    val shape = CircleShape
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp)
            .height(46.dp)
            .shadow(
                elevation = 4.dp,
                shape = shape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.06f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.06f),
            )
            .clip(shape)
            .background(IdeaLoopWhite.copy(alpha = 0.70f))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.80f), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = IdeaLoopIndigo400,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = HomeContract.searchHint,
            color = IdeaLoopSlate400.copy(alpha = 0.90f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun SummaryCards(
    captureCount: Int,
    suggestionCount: Int,
    onActionSuggestions: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 20.dp),
    ) {
        SummaryCard(
            title = "本周捕捉",
            label = "已记录",
            value = captureCount.toString(),
            suffix = "条",
            icon = Icons.AutoMirrored.Outlined.TrendingUp,
        )
        SummaryCard(
            title = "行动建议",
            label = "生成",
            value = suggestionCount.toString(),
            suffix = "条建议",
            icon = Icons.Outlined.AutoAwesome,
            onClick = onActionSuggestions,
        )
    }
}

@Composable
private fun RowScope.SummaryCard(
    title: String,
    label: String,
    value: String,
    suffix: String,
    icon: ImageVector,
    onClick: (() -> Unit)? = null,
) {
    val shape = RoundedCornerShape(18.dp)
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .weight(1f)
            .height(100.dp)
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.08f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.08f),
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
            .then(
                if (onClick == null) Modifier else Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
            )
            .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 14.dp),
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = IdeaLoopIndigo500.copy(alpha = 0.80f),
                    modifier = Modifier.size(13.dp),
                )
                Text(
                    text = title,
                    color = IdeaLoopBlue900,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = label,
                color = IdeaLoopSlate500.copy(alpha = 0.90f),
                fontSize = 10.5.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.42.sp,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = value,
                    color = IdeaLoopBlue900,
                    fontSize = 30.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp,
                )
                Text(
                    text = suffix,
                    color = IdeaLoopSlate600,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 2.dp),
                )
            }
        }
    }
}

@Composable
private fun AwakeSection(
    cards: List<AwakeCard>,
    onAll: () -> Unit,
    onCard: (AwakeCard) -> Unit,
) {
    val sectionShape = RoundedCornerShape(24.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp)
            .shadow(
                elevation = 4.dp,
                shape = sectionShape,
                ambientColor = Color(0xFF6E64DC).copy(alpha = 0.08f),
                spotColor = Color(0xFF6E64DC).copy(alpha = 0.08f),
            )
            .clip(sectionShape)
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFF3EFFF).copy(alpha = 0.85f),
                        Color(0xFFE8ECFF).copy(alpha = 0.75f),
                    ),
                ),
            )
            .border(1.dp, Color(0xFFC4B5FD).copy(alpha = 0.40f), sectionShape)
            .padding(top = 14.dp, bottom = 12.dp),
    ) {
        Column {
            SectionHeader(
                title = "今日唤醒",
                icon = Icons.Outlined.AutoAwesome,
                color = Color(0xFF581C87).copy(alpha = 0.85f),
                iconColor = IdeaLoopPurple500,
                actionLabel = "全部",
                onAction = onAll,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(Modifier.height(10.dp))
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val cardWidth = maxWidth - 32.dp
                val listState = rememberLazyListState()
                LazyRow(
                    state = listState,
                    flingBehavior = rememberSnapFlingBehavior(listState),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { contentDescription = "今日唤醒轮播" },
                ) {
                    items(cards, key = { it.id }) { card ->
                        AwakeCardItem(
                            card = card,
                            onClick = { onCard(card) },
                            modifier = Modifier.width(cardWidth),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AwakeCardItem(
    card: AwakeCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)
    val purpleGradient = Brush.linearGradient(listOf(IdeaLoopPurple500, IdeaLoopIndigo500))
    Column(
        modifier = modifier
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
                    listOf(
                        IdeaLoopWhite.copy(alpha = 0.92f),
                        Color(0xFFF4F7FF).copy(alpha = 0.82f),
                    ),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .clickable(onClick = onClick)
            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 14.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(24.dp)
                    .shadow(3.dp, CircleShape, ambientColor = IdeaLoopPurple500.copy(alpha = 0.35f))
                    .clip(CircleShape)
                    .background(purpleGradient),
            ) {
                Icon(
                    imageVector = triggerIcon(card.triggerType),
                    contentDescription = null,
                    tint = IdeaLoopWhite,
                    modifier = Modifier.size(11.dp),
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = card.triggerLabel,
                color = IdeaLoopSlate400,
                fontSize = 10.5.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            text = card.title,
            color = IdeaLoopSlate800,
            fontSize = 14.5.sp,
            lineHeight = 19.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = card.content,
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
            SmallTag(
                text = triggerTypeLabel(card.triggerType),
                contentColor = Color(0xFF7E22CE).copy(alpha = 0.85f),
                backgroundColor = Color(0xFFF3E8FF).copy(alpha = 0.85f),
                borderColor = Color(0xFFD8B4FE).copy(alpha = 0.60f),
            )
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
private fun RecentMemoriesSection(
    memories: List<RecentMemory>,
    onMemory: (RecentMemory) -> Unit,
) {
    val shape = RoundedCornerShape(22.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp)
            .shadow(
                elevation = 4.dp,
                shape = shape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.07f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.07f),
            )
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFECF2FF).copy(alpha = 0.78f),
                        Color(0xFFF4F0FF).copy(alpha = 0.70f),
                    ),
                ),
            )
            .border(1.dp, Color(0xFFBAC8FF).copy(alpha = 0.40f), shape)
            .padding(top = 10.dp, bottom = 8.dp),
    ) {
        Column {
            SectionHeader(
                title = "最近记忆",
                icon = Icons.Outlined.History,
                color = IdeaLoopBlue900.copy(alpha = 0.85f),
                iconColor = IdeaLoopIndigo500,
                modifier = Modifier.padding(horizontal = 12.dp),
            )
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                contentPadding = PaddingValues(start = 10.dp, end = 10.dp, bottom = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(304.dp),
            ) {
                items(memories, key = { it.id }) { memory ->
                    MemoryListCard(
                        memory = memory,
                        onClick = { onMemory(memory) },
                        modifier = Modifier.height(154.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector,
    color: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = title,
                color = color,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        if (actionLabel != null && onAction != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onAction),
            ) {
                Text(
                    text = actionLabel,
                    color = Color(0xFF9333EA).copy(alpha = 0.90f),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                )
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = Color(0xFF9333EA).copy(alpha = 0.90f),
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@Composable
private fun SmallTag(
    text: String,
    contentColor: Color,
    backgroundColor: Color,
    borderColor: Color,
) {
    Text(
        text = text,
        color = contentColor,
        fontSize = 10.sp,
        lineHeight = 13.sp,
        fontWeight = FontWeight.Medium,
        maxLines = 1,
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 1.dp),
    )
}

private fun triggerIcon(type: AwakeTriggerType): ImageVector = when (type) {
    AwakeTriggerType.Location -> Icons.Outlined.LocationOn
    AwakeTriggerType.Time -> Icons.Outlined.Timer
    AwakeTriggerType.Relation -> Icons.Outlined.Link
}

private fun triggerTypeLabel(type: AwakeTriggerType): String = when (type) {
    AwakeTriggerType.Location -> "地点唤醒"
    AwakeTriggerType.Time -> "时间唤醒"
    AwakeTriggerType.Relation -> "关联唤醒"
}

@Preview(
    name = "P04 · 首页正常态 · 360",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Preview(
    name = "P04 · 首页正常态 · 412",
    device = "spec:width=412dp,height=915dp,dpi=440",
    showBackground = true,
)
@Composable
private fun HomeScreenPreview() {
    IdeaLoopTheme {
        HomeScreen()
    }
}
