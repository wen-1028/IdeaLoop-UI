package com.example.idealoop.feature.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopSlate400
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite

@Composable
fun SearchInputScreen(
    onBack: () -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    initialQuery: String = "",
    autoFocus: Boolean = true,
) {
    var query by rememberSaveable(initialQuery) { mutableStateOf(initialQuery) }
    var recentSearches by remember { mutableStateOf(SearchSampleData.recentSearches) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun submit(text: String) {
        val normalized = text.trim()
        if (normalized.isNotEmpty()) {
            focusManager.clearFocus()
            onSearch(normalized)
        }
    }

    LaunchedEffect(autoFocus) {
        if (autoFocus) focusRequester.requestFocus()
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
                    title = "搜索",
                    onBack = onBack,
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
                    .padding(top = 16.dp, bottom = 24.dp),
            ) {
                SearchField(
                    query = query,
                    onQueryChange = { query = it },
                    onClear = { query = "" },
                    onSubmit = { submit(query) },
                    focusRequester = focusRequester,
                )

                Spacer(Modifier.height(20.dp))

                RecentSearchesCard(
                    searches = recentSearches,
                    onClear = { recentSearches = emptyList() },
                    onSelect = { selected ->
                        query = selected
                        submit(selected)
                    },
                )
            }
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    onSubmit: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    val shape = CircleShape
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(
                elevation = 4.dp,
                shape = shape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.07f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.07f),
            )
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    listOf(
                        IdeaLoopWhite.copy(alpha = 0.95f),
                        Color(0xFFF4F7FF).copy(alpha = 0.88f),
                    ),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .padding(horizontal = 16.dp),
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = IdeaLoopBlue900,
            modifier = Modifier.size(17.dp),
        )
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = TextStyle(
                color = IdeaLoopBlue900,
                fontSize = 13.5.sp,
                lineHeight = 20.sp,
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(onSearch = { onSubmit() }),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (query.isEmpty()) {
                        Text(
                            text = "搜索关键词，或直接问一句",
                            color = IdeaLoopSlate400,
                            fontSize = 13.5.sp,
                        )
                    }
                    innerTextField()
                }
            },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
                .testTag("search_input"),
        )
        if (query.isNotEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEEF2FF).copy(alpha = 0.85f))
                    .clickable(onClick = onClear),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "清除搜索",
                    tint = IdeaLoopSlate500,
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@Composable
private fun RecentSearchesCard(
    searches: List<String>,
    onClear: () -> Unit,
    onSelect: (String) -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
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
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "最近搜索",
                color = IdeaLoopBlue900,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
            )
            if (searches.isNotEmpty()) {
                Text(
                    text = "清空",
                    color = IdeaLoopSlate400,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable(onClick = onClear),
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        if (searches.isEmpty()) {
            Text(
                text = "暂无搜索记录",
                color = IdeaLoopSlate400,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        } else {
            RecentSearchFlowRow(searches = searches, onSelect = onSelect)
        }
    }
}

@Composable
private fun RecentSearchFlowRow(
    searches: List<String>,
    onSelect: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        searches.chunked(3).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowItems.forEach { text ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(IdeaLoopWhite.copy(alpha = 0.80f))
                            .border(1.dp, Color(0xFFBAD2FF).copy(alpha = 0.55f), CircleShape)
                            .clickable { onSelect(text) }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccessTime,
                            contentDescription = null,
                            tint = IdeaLoopSlate400,
                            modifier = Modifier.size(11.dp),
                        )
                        Text(
                            text = text,
                            color = IdeaLoopBlue900,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "P13 · 搜索输入 · 360",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Preview(
    name = "P13 · 搜索输入 · 412",
    device = "spec:width=412dp,height=915dp,dpi=440",
    showBackground = true,
)
@Composable
private fun SearchInputPreview() {
    IdeaLoopTheme {
        SearchInputScreen(onBack = {}, onSearch = {}, autoFocus = false)
    }
}
