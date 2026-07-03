package com.example.idealoop

import com.example.idealoop.feature.search.SearchContract
import com.example.idealoop.feature.search.SearchSampleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchContractTest {
    @Test
    fun routesMatchP13P14AndP35() {
        assertEquals("P13", SearchContract.inputRoute)
        assertTrue(SearchContract.resultsRoute("广州").startsWith("P14/"))
        assertTrue(SearchContract.xiaolingRoute("帮我找一下广州相关记忆").startsWith("P35/"))
    }

    @Test
    fun keywordSearchOnlyReturnsMatchingMemories() {
        assertEquals(
            listOf("粤语口语速记"),
            SearchSampleData.search("粤语").map { it.title },
        )
        assertTrue(SearchSampleData.search("完全不存在").isEmpty())
    }

    @Test
    fun searchSpecificCopyWinsWhenIdsOverlapWithTheLibrary() {
        val guangzhou = SearchSampleData.search("广州")
            .first { it.id == "guangzhou" }

        assertEquals("周末路线、餐厅推荐、行程安排", guangzhou.summary)
        assertEquals(listOf("旅行攻略"), guangzhou.tags)
    }

    @Test
    fun naturalLanguageAndKeywordUseTheSameLocalSearchIndex() {
        val keywordResults = SearchSampleData.search("广州")
        val naturalResults = SearchSampleData.search("帮我找一下广州周末旅行相关的记忆")

        assertEquals(keywordResults, naturalResults)
        assertTrue(SearchContract.isNaturalLanguage("帮我找一下广州周末旅行相关的记忆"))
        assertFalse(SearchContract.isNaturalLanguage("广州"))
    }

    @Test
    fun recentSearchesMatchFigmaOrder() {
        assertEquals(
            listOf("广州旅行", "AIGC 比赛", "毕业设计", "番茄牛腩", "陶陶居"),
            SearchSampleData.recentSearches,
        )
    }
}
