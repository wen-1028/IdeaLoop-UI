package com.example.idealoop.feature.review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.feature.memory.MemoryItem
import com.example.idealoop.feature.memory.MemoryListCard
import com.example.idealoop.ui.components.IdeaLoopBottomItem
import com.example.idealoop.ui.components.IdeaLoopBottomNavigation
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopIndigo500
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopWhite
import com.example.idealoop.ui.theme.IdeaLoopTheme

@Composable
fun WeeklyReviewScreen(
    onAllSuggestions: () -> Unit,
    onSuggestionMemories: (ReviewSuggestion) -> Unit,
    onAllThemes: () -> Unit,
    onTheme: (ReviewTheme) -> Unit,
    onBottomItem: (IdeaLoopBottomItem) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val weeks = ReviewSampleData.weeks
    var weekIndex by remember { mutableIntStateOf(0) }
    var suggestionCollections by remember {
        mutableStateOf(weeks.map { SuggestionCollection(it.suggestions) })
    }
    val week = weeks[weekIndex]
    val suggestions = suggestionCollections[weekIndex].items

    ReviewBackground {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                ReviewOverviewTopBar(
                    weekLabel = week.label,
                    previousEnabled = weekIndex < weeks.lastIndex,
                    nextEnabled = weekIndex > 0,
                    onPrevious = { weekIndex = (weekIndex + 1).coerceAtMost(weeks.lastIndex) },
                    onNext = { weekIndex = (weekIndex - 1).coerceAtLeast(0) },
                )
            },
            bottomBar = {
                ReviewBottomBar(onBottomItem = onBottomItem, onAdd = onAdd)
            },
        ) { innerPadding ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 32.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                item(key = "overview-${week.label}") {
                    ReviewSectionCard {
                        Text(
                            text = "本周记忆总览",
                            color = IdeaLoopBlue900,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth()) {
                            ReviewStatBlock(week.total, "条", "本周记忆", Modifier.weight(1f))
                            ReviewStatBlock(week.themeCount, "个", "主题", Modifier.weight(1f))
                            ReviewStatBlock(week.actionCount, "条", "行动建议", Modifier.weight(1f))
                        }
                        Spacer(Modifier.height(12.dp))
                        ActivityTrendChart(week.daily)
                    }
                }
                item(key = "themes-${week.label}") {
                    ReviewSectionCard {
                        ReviewSectionHeader(
                            title = "主题分布",
                            onAll = onAllThemes,
                            allDescription = "查看全部主题",
                        )
                        Spacer(Modifier.height(12.dp))
                        ThemeCarousel(onTheme = onTheme)
                    }
                }
                item(key = "suggestions-${week.label}") {
                    ReviewSectionCard {
                        ReviewSectionHeader(
                            title = "行动建议",
                            showSparkle = true,
                            onAll = onAllSuggestions,
                            allDescription = "查看全部行动建议",
                        )
                        Spacer(Modifier.height(10.dp))
                        if (suggestions.isEmpty()) {
                            Text(
                                text = "已无待处理建议",
                                color = IdeaLoopSlate500,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(vertical = 24.dp),
                            )
                        } else {
                            suggestions.forEachIndexed { index, suggestion ->
                                if (index > 0) Spacer(Modifier.height(10.dp))
                                SuggestionCard(
                                    suggestion = suggestion,
                                    onRelated = { onSuggestionMemories(suggestion) },
                                    onHandled = {
                                        suggestionCollections = suggestionCollections.toMutableList().also { collections ->
                                            collections[weekIndex] = collections[weekIndex].apply(
                                                suggestion.id,
                                                SuggestionAction.Handled,
                                            )
                                        }
                                    },
                                    onIgnored = {
                                        suggestionCollections = suggestionCollections.toMutableList().also { collections ->
                                            collections[weekIndex] = collections[weekIndex].apply(
                                                suggestion.id,
                                                SuggestionAction.IgnoreThisWeek,
                                            )
                                        }
                                    },
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
fun ActionSuggestionsScreen(
    onBack: () -> Unit,
    onSuggestionMemories: (ReviewSuggestion) -> Unit,
    onBottomItem: (IdeaLoopBottomItem) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var collection by remember { mutableStateOf(SuggestionCollection(ReviewSampleData.currentSuggestions)) }
    ReviewListScaffold(
        title = "行动建议",
        onBack = onBack,
        onBottomItem = onBottomItem,
        onAdd = onAdd,
        modifier = modifier,
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 32.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                ReviewSummaryCard(title = "${collection.items.size} 条建议待处理")
                Spacer(Modifier.height(6.dp))
            }
            items(collection.items, key = { it.id }) { suggestion ->
                SuggestionCard(
                    suggestion = suggestion,
                    onRelated = { onSuggestionMemories(suggestion) },
                    onHandled = {
                        collection = collection.apply(suggestion.id, SuggestionAction.Handled)
                    },
                    onIgnored = {
                        collection = collection.apply(suggestion.id, SuggestionAction.IgnoreThisWeek)
                    },
                )
            }
        }
    }
}

@Composable
fun SuggestionMemoriesScreen(
    suggestionId: String,
    onBack: () -> Unit,
    onMemory: (MemoryItem) -> Unit,
    onBottomItem: (IdeaLoopBottomItem) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val suggestion = ReviewSampleData.suggestion(suggestionId)
    val memories = ReviewSampleData.memoriesForSuggestion(suggestionId)
    ReviewListScaffold("相关记忆", onBack, onBottomItem, onAdd, modifier) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 32.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                ReviewSummaryCard(
                    title = suggestion.title,
                    subtitle = "共关联 ${memories.size} 条记忆",
                )
                Spacer(Modifier.height(6.dp))
            }
            items(memories, key = { it.id }) { memory ->
                MemoryListCard(memory = memory, onClick = { onMemory(memory) })
            }
        }
    }
}

@Composable
fun ReviewThemesScreen(
    onBack: () -> Unit,
    onTheme: (ReviewTheme) -> Unit,
    onBottomItem: (IdeaLoopBottomItem) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val themes = ReviewSampleData.themes
    ReviewListScaffold("主题分布", onBack, onBottomItem, onAdd, modifier) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 32.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                ReviewSummaryCard(title = "${themes.size} 个主题 · 共 ${themes.sumOf { it.count }} 条记忆")
                Spacer(Modifier.height(6.dp))
            }
            items(themes, key = { it.id }) { theme ->
                ThemeListCard(theme = theme, onClick = { onTheme(theme) })
            }
        }
    }
}

@Composable
fun ThemeMemoriesScreen(
    themeId: String,
    onBack: () -> Unit,
    onMemory: (MemoryItem) -> Unit,
    onBottomItem: (IdeaLoopBottomItem) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val theme = ReviewSampleData.theme(themeId)
    val memories = ReviewSampleData.memoriesForTheme(theme.id)
    ReviewListScaffold(theme.name, onBack, onBottomItem, onAdd, modifier) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 32.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                ReviewSummaryCard(title = theme.name, subtitle = "共 ${memories.size} 条记忆")
                Spacer(Modifier.height(6.dp))
            }
            items(memories, key = { it.id }) { memory ->
                MemoryListCard(memory = memory, onClick = { onMemory(memory) })
            }
        }
    }
}

@Composable
private fun ReviewOverviewTopBar(
    weekLabel: String,
    previousEnabled: Boolean,
    nextEnabled: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xEBF4F7FF), Color(0xD9F7F9FF), Color(0xB3F7F9FF)),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.60f))
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = 9.dp, bottom = 10.dp),
    ) {
        Text(
            text = "复盘",
            color = IdeaLoopBlue900,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.3.sp,
        )
        Spacer(Modifier.height(2.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WeekArrowButton(
                icon = Icons.Outlined.ChevronLeft,
                description = "上一周",
                enabled = previousEnabled,
                onClick = onPrevious,
            )
            Text(
                text = weekLabel,
                color = IdeaLoopBlue900.copy(alpha = 0.60f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp,
            )
            WeekArrowButton(
                icon = Icons.Outlined.ChevronRight,
                description = "下一周",
                enabled = nextEnabled,
                onClick = onNext,
            )
        }
    }
}

@Composable
private fun WeekArrowButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(20.dp)
            .clip(CircleShape)
            .background(IdeaLoopWhite.copy(alpha = 0.70f))
            .border(1.dp, Color(0xFFBAD2FF).copy(alpha = 0.50f), CircleShape)
            .clickable(enabled = enabled, onClick = onClick),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = IdeaLoopBlue900.copy(alpha = if (enabled) 1f else 0.35f),
            modifier = Modifier.size(10.dp),
        )
    }
}

@Composable
private fun ThemeCarousel(onTheme: (ReviewTheme) -> Unit) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val itemWidth = (maxWidth - 16.dp) / 3
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(0.dp),
        ) {
            items(ReviewSampleData.themes, key = { it.id }) { theme ->
                ThemeTile(theme = theme, onClick = { onTheme(theme) }, modifier = Modifier.width(itemWidth))
            }
        }
    }
}

@Composable
private fun ThemeTile(
    theme: ReviewTheme,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accent = Color(theme.accent)
    val shape = RoundedCornerShape(16.dp)
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .heightIn(min = 104.dp)
            .semantics { contentDescription = "主题 ${theme.id}" }
            .clip(shape)
            .background(Brush.linearGradient(listOf(IdeaLoopWhite.copy(alpha = 0.92f), Color(0xFFF4F7FF).copy(alpha = 0.82f))))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .clickable(onClick = onClick)
            .padding(start = 12.dp, top = 8.dp, end = 12.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(IdeaLoopWhite.copy(alpha = 0.95f)),
        ) {
            Icon(reviewThemeIcon(theme.icon), null, tint = accent, modifier = Modifier.size(15.dp))
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = theme.name,
            color = IdeaLoopBlue900,
            fontSize = 12.5.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(text = "${theme.count} 条", color = IdeaLoopSlate500, fontSize = 10.5.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(14.dp))
    }
}

@Composable
private fun ThemeListCard(theme: ReviewTheme, onClick: () -> Unit) {
    val accent = Color(theme.accent)
    val shape = RoundedCornerShape(18.dp)
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(68.dp)
            .semantics { contentDescription = "主题 ${theme.id}" }
            .clip(shape)
            .background(Brush.linearGradient(listOf(IdeaLoopWhite.copy(alpha = 0.92f), Color(0xFFF4F7FF).copy(alpha = 0.82f))))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(IdeaLoopWhite.copy(alpha = 0.95f)),
        ) {
            Icon(reviewThemeIcon(theme.icon), null, tint = accent, modifier = Modifier.size(18.dp))
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = theme.name,
                color = IdeaLoopBlue900,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false),
            )
            Text(text = "${theme.count} 条", color = IdeaLoopSlate500, fontSize = 11.sp)
        }
        Icon(Icons.Outlined.ChevronRight, null, tint = IdeaLoopIndigo500.copy(alpha = 0.70f), modifier = Modifier.size(16.dp))
    }
}

@Composable
private fun ReviewListScaffold(
    title: String,
    onBack: () -> Unit,
    onBottomItem: (IdeaLoopBottomItem) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    ReviewBackground {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = { IdeaLoopTopBar(title = title, onBack = onBack) },
            bottomBar = { ReviewBottomBar(onBottomItem, onAdd) },
            content = content,
        )
    }
}

@Composable
private fun ReviewBottomBar(
    onBottomItem: (IdeaLoopBottomItem) -> Unit,
    onAdd: () -> Unit,
) {
    IdeaLoopBottomNavigation(
        activeItem = IdeaLoopBottomItem.Review,
        onItemSelected = onBottomItem,
        onAdd = onAdd,
    )
}

@Preview(name = "P19 · 每周复盘 · 360", device = "spec:width=360dp,height=800dp,dpi=440")
@Preview(name = "P19 · 每周复盘 · 412", device = "spec:width=412dp,height=915dp,dpi=440")
@Composable
private fun WeeklyReviewPreview() {
    IdeaLoopTheme { WeeklyReviewScreen({}, {}, {}, {}, {}, {}) }
}

@Preview(name = "P31 · 行动建议", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun SuggestionsPreview() {
    IdeaLoopTheme { ActionSuggestionsScreen({}, {}, {}, {}) }
}

@Preview(name = "P32 · 相关记忆", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun SuggestionMemoriesPreview() {
    IdeaLoopTheme { SuggestionMemoriesScreen("aigc", {}, {}, {}, {}) }
}

@Preview(name = "P33 · 主题分布", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun ThemesPreview() {
    IdeaLoopTheme { ReviewThemesScreen({}, {}, {}, {}) }
}

@Preview(name = "P34 · 主题记忆", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun ThemeMemoriesPreview() {
    IdeaLoopTheme { ThemeMemoriesScreen("guangzhou", {}, {}, {}, {}) }
}
