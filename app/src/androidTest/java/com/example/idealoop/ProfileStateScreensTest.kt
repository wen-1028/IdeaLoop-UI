package com.example.idealoop

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.idealoop.feature.profile.EmptyMemoryStateScreen
import com.example.idealoop.feature.profile.ErrorStateScreen
import com.example.idealoop.ui.theme.IdeaLoopTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class ProfileStateScreensTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun emptyStateUsesUnifiedLogoAndReturnsToMemory() {
        var returned = false
        var added = false
        composeRule.setContent {
            IdeaLoopTheme {
                EmptyMemoryStateScreen(
                    onBack = { returned = true },
                    onAdd = { added = true },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Idea Loop Logo").assertIsDisplayed()
        composeRule.onNodeWithText("还没有记忆").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("返回").performClick()
        assertTrue(returned)
        composeRule.onNodeWithText("添加第一条记录").performClick()
        assertTrue(added)
    }

    @Test
    fun errorStateUsesUnifiedLogoAndOffersRetryAndReturn() {
        var retried = false
        var returned = false
        composeRule.setContent {
            IdeaLoopTheme {
                ErrorStateScreen(
                    onRetry = { retried = true },
                    onBack = { returned = true },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Idea Loop Logo").assertIsDisplayed()
        composeRule.onNodeWithText("暂时无法完成操作").assertIsDisplayed()
        composeRule.onNodeWithText("重试").performClick()
        composeRule.onNodeWithText("返回").performClick()
        assertTrue(retried)
        assertTrue(returned)
    }
}
