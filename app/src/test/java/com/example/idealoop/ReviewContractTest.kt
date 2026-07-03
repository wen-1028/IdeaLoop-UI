package com.example.idealoop

import com.example.idealoop.feature.review.ReviewContract
import com.example.idealoop.feature.review.ReviewSampleData
import com.example.idealoop.feature.review.SuggestionAction
import com.example.idealoop.feature.review.SuggestionCollection
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReviewContractTest {
    @Test
    fun reviewRoutesMatchFigmaPages() {
        assertEquals("P19", ReviewContract.overviewRoute)
        assertEquals("P31", ReviewContract.suggestionsRoute)
        assertEquals("P32/{suggestionId}", ReviewContract.relatedRoutePattern)
        assertEquals("P33", ReviewContract.themesRoute)
        assertEquals("P34/{themeId}", ReviewContract.themeMemoriesRoutePattern)
        assertEquals("P32/aigc", ReviewContract.relatedRoute("aigc"))
        assertEquals("P34/guangzhou", ReviewContract.themeMemoriesRoute("guangzhou"))
    }

    @Test
    fun weekDataSwitchesFromCurrentToPreviousWeek() {
        assertEquals(listOf("5.12 - 5.18", "5.5 - 5.11"), ReviewSampleData.weeks.map { it.label })
        assertEquals(listOf(23, 3, 4), ReviewSampleData.weeks.first().let { listOf(it.total, it.themeCount, it.actionCount) })
        assertEquals(listOf(2, 4, 3, 5, 3, 4, 2), ReviewSampleData.weeks.first().daily)
        assertEquals(listOf(17, 2, 3), ReviewSampleData.weeks.last().let { listOf(it.total, it.themeCount, it.actionCount) })
    }

    @Test
    fun suggestionActionsExposeOnlyRequestedChoicesAndDismissLocally() {
        assertEquals(
            listOf("查看相关记忆", "已处理", "本周忽略"),
            SuggestionAction.entries.map { it.label },
        )
        val initial = SuggestionCollection(ReviewSampleData.currentSuggestions)
        val viewed = initial.apply("aigc", SuggestionAction.ViewRelated)
        val handled = initial.apply("aigc", SuggestionAction.Handled)
        val ignored = initial.apply("aigc", SuggestionAction.IgnoreThisWeek)

        assertEquals(initial, viewed)
        assertFalse(handled.items.any { it.id == "aigc" })
        assertFalse(ignored.items.any { it.id == "aigc" })
    }

    @Test
    fun themeCountsAndMemoryCollectionsMatchFigma() {
        assertEquals(8, ReviewSampleData.themes.size)
        assertEquals(47, ReviewSampleData.themes.sumOf { it.count })
        assertEquals(8, ReviewSampleData.memoriesForSuggestion("aigc").size)
        assertEquals(6, ReviewSampleData.memoriesForTheme("guangzhou").size)
        assertTrue(ReviewSampleData.memoriesForTheme("aigc").all { "比赛" in it.tags || it.id == "review-aigc-plan" })
    }
}
