package com.example.idealoop.feature.memory

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.idealoop.ui.components.IdeaLoopBottomItem
import com.example.idealoop.ui.components.IdeaLoopBottomNavigation
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopSlate400
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite

data class MemoryLibraryCallbacks(
    val onSearch: () -> Unit = {},
    val onMemory: (MemoryItem) -> Unit = {},
    val onBottomItem: (IdeaLoopBottomItem) -> Unit = {},
    val onAdd: () -> Unit = {},
)

@Composable
fun MemoryLibraryScreen(
    memories: List<MemoryItem> = MemorySampleData.library,
    callbacks: MemoryLibraryCallbacks = MemoryLibraryCallbacks(),
    modifier: Modifier = Modifier,
) {
    var activeFilter by rememberSaveable { mutableStateOf(MemoryFilter.All) }
    val visibleMemories = activeFilter.source?.let { source ->
        memories.filter { it.source == source }
    } ?: memories

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
                    title = "记忆",
                    modifier = Modifier.testTag("memory_top_bar"),
                    rightContent = {
                        CircularIconButton(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "搜索",
                            onClick = callbacks.onSearch,
                        )
                    },
                )
            },
            bottomBar = {
                IdeaLoopBottomNavigation(
                    activeItem = IdeaLoopBottomItem.Memory,
                    onItemSelected = callbacks.onBottomItem,
                    onAdd = callbacks.onAdd,
                    modifier = Modifier.testTag("bottom_navigation"),
                )
            },
        ) { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 20.dp,
                    end = 20.dp,
                    bottom = 32.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .testTag("memory_list"),
            ) {
                item(key = "filters") {
                    MemoryFilters(
                        active = activeFilter,
                        onSelect = { activeFilter = it },
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                }

                if (visibleMemories.isEmpty()) {
                    item(key = "empty") {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                        ) {
                            Text(
                                text = "该分类下暂无记忆",
                                color = IdeaLoopSlate400,
                                fontSize = 12.sp,
                            )
                        }
                    }
                } else {
                    items(visibleMemories, key = { it.id }) { memory ->
                        MemoryListCard(
                            memory = memory,
                            onClick = { callbacks.onMemory(memory) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MemoryFilters(
    active: MemoryFilter,
    onSelect: (MemoryFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        MemoryFilter.entries.forEach { filter ->
            val selected = filter == active
            val shape = CircleShape
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
                    .shadow(
                        elevation = if (selected) 4.dp else 0.dp,
                        shape = shape,
                        ambientColor = IdeaLoopBlue900.copy(alpha = 0.22f),
                        spotColor = IdeaLoopBlue900.copy(alpha = 0.22f),
                    )
                    .clip(shape)
                    .background(if (selected) IdeaLoopBlue900 else IdeaLoopWhite.copy(alpha = 0.70f))
                    .border(
                        width = 1.dp,
                        color = if (selected) IdeaLoopWhite.copy(alpha = 0.40f) else Color(0xFFBAD2FF).copy(alpha = 0.55f),
                        shape = shape,
                    )
                    .clickable { onSelect(filter) },
            ) {
                Text(
                    text = filter.label,
                    color = if (selected) IdeaLoopWhite else IdeaLoopBlue900,
                    fontSize = 12.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                )
            }
        }
    }
}

@Preview(
    name = "P11 · 记忆库正常态 · 360",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Preview(
    name = "P11 · 记忆库正常态 · 412",
    device = "spec:width=412dp,height=915dp,dpi=440",
    showBackground = true,
)
@Composable
private fun MemoryLibraryPreview() {
    IdeaLoopTheme { MemoryLibraryScreen() }
}

@Preview(
    name = "P11 · 空记忆库 · 360",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Composable
private fun EmptyMemoryLibraryPreview() {
    IdeaLoopTheme { MemoryLibraryScreen(memories = emptyList()) }
}
