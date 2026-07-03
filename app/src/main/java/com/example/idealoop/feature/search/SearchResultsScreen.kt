package com.example.idealoop.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.feature.memory.MemoryFilter
import com.example.idealoop.feature.memory.MemoryItem
import com.example.idealoop.feature.memory.MemoryListCard
import com.example.idealoop.feature.memory.MemorySource
import com.example.idealoop.ui.components.IdeaLoopBottomItem
import com.example.idealoop.ui.components.IdeaLoopBottomNavigation
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopSlate800
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite

data class SearchResultsCallbacks(
    val onBack: () -> Unit = {},
    val onMemory: (MemoryItem) -> Unit = {},
    val onBottomItem: (IdeaLoopBottomItem) -> Unit = {},
    val onAdd: () -> Unit = {},
)

@Composable
fun SearchResultsScreen(
    query: String,
    callbacks: SearchResultsCallbacks = SearchResultsCallbacks(),
    modifier: Modifier = Modifier,
) {
    var filter by rememberSaveable { mutableStateOf(MemoryFilter.All) }
    var submittedQuery by remember(query) { mutableStateOf(query) }
    var queryValue by remember(query) {
        mutableStateOf(TextFieldValue(query, selection = TextRange(query.length)))
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val submitQuery = {
        val normalized = queryValue.text.trim()
        if (normalized.isNotEmpty()) {
            submittedQuery = normalized
            queryValue = TextFieldValue(normalized, selection = TextRange(normalized.length))
            filter = MemoryFilter.All
            focusManager.clearFocus(force = true)
            keyboardController?.hide()
        }
    }
    val queryResults = SearchSampleData.search(submittedQuery)
    val filteredResults = queryResults.filter { memory ->
        when (filter) {
            MemoryFilter.All -> true
            MemoryFilter.Image -> memory.source == MemorySource.Image || memory.source == MemorySource.Shot
            else -> memory.source == filter.source
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
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                IdeaLoopTopBar(
                    title = "搜索结果",
                    onBack = callbacks.onBack,
                )
            },
            bottomBar = {
                IdeaLoopBottomNavigation(
                    activeItem = IdeaLoopBottomItem.Memory,
                    onItemSelected = callbacks.onBottomItem,
                    onAdd = callbacks.onAdd,
                )
            },
        ) { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 32.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                item(key = "query") {
                    QueryEditor(
                        value = queryValue,
                        onValueChange = { queryValue = it },
                        onSubmit = submitQuery,
                    )
                }
                item(key = "filters") {
                    SearchFilterRow(
                        active = filter,
                        onSelect = { filter = it },
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }

                if (filteredResults.isEmpty()) {
                    item(key = "empty") {
                        SearchEmptyState(
                            text = if (queryResults.isEmpty()) {
                                "没有找到与「$submittedQuery」相关的记忆"
                            } else {
                                "没有「${filter.label}」类型的记忆"
                            },
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                } else {
                    items(filteredResults, key = { it.id }) { memory ->
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
private fun QueryEditor(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSubmit: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var focused by remember { mutableStateOf(false) }
    var moveSelectionToEnd by remember { mutableStateOf(false) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(CircleShape)
            .background(IdeaLoopWhite.copy(alpha = 0.85f))
            .border(1.dp, Color(0xFFE0E7FF), CircleShape)
            .semantics { contentDescription = "修改搜索" }
            .clickable {
                moveSelectionToEnd = true
                onValueChange(value.copy(selection = TextRange(value.text.length)))
                focusRequester.requestFocus()
                keyboardController?.show()
            }
            .padding(horizontal = 12.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = Color(0xFF6366F1),
            modifier = Modifier.size(16.dp),
        )
        BasicTextField(
            value = value,
            onValueChange = { updated ->
                if (moveSelectionToEnd) {
                    moveSelectionToEnd = false
                    onValueChange(updated.copy(selection = TextRange(updated.text.length)))
                } else {
                    onValueChange(updated)
                }
            },
            singleLine = true,
            textStyle = TextStyle(color = IdeaLoopSlate800, fontSize = 13.sp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .onFocusChanged { state ->
                    if (state.isFocused && !focused) {
                        moveSelectionToEnd = true
                        onValueChange(value.copy(selection = TextRange(value.text.length)))
                    }
                    focused = state.isFocused
                }
                .testTag("search_results_input"),
        )
    }
}

@Composable
private fun SearchFilterRow(
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
                    .shadow(
                        elevation = if (selected) 4.dp else 0.dp,
                        shape = CircleShape,
                        ambientColor = IdeaLoopBlue900.copy(alpha = 0.22f),
                        spotColor = IdeaLoopBlue900.copy(alpha = 0.22f),
                    )
                    .clip(CircleShape)
                    .background(if (selected) IdeaLoopBlue900 else IdeaLoopWhite.copy(alpha = 0.70f))
                    .border(
                        1.dp,
                        if (selected) IdeaLoopWhite.copy(alpha = 0.40f) else Color(0xFFBAD2FF).copy(alpha = 0.55f),
                        CircleShape,
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

@Composable
private fun SearchEmptyState(
    text: String,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(82.dp)
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        IdeaLoopWhite.copy(alpha = 0.85f),
                        Color(0xFFF4F7FF).copy(alpha = 0.75f),
                    ),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.85f), shape),
    ) {
        Text(
            text = text,
            color = IdeaLoopSlate500,
            fontSize = 12.5.sp,
        )
    }
}

@Preview(
    name = "P14 · 关键词搜索结果 · 360",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Preview(
    name = "P14 · 关键词搜索结果 · 412",
    device = "spec:width=412dp,height=915dp,dpi=440",
    showBackground = true,
)
@Composable
private fun SearchResultsPreview() {
    IdeaLoopTheme { SearchResultsScreen(query = "广州") }
}

@Preview(
    name = "P14 · 无结果 · 360",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Composable
private fun EmptySearchResultsPreview() {
    IdeaLoopTheme { SearchResultsScreen(query = "火星基地") }
}
