package com.example.idealoop

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ReviewFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun reviewWeekSuggestionsThemesAndBackFlowMatchFigma() {
        enterHome()
        composeRule.onNodeWithText("复盘").performClick()

        waitForText("5.12 - 5.18")
        composeRule.onNodeWithText("本周记忆总览").assertIsDisplayed()
        composeRule.onNodeWithText("主题分布").performScrollTo().assertIsDisplayed()
        val density = composeRule.activity.resources.displayMetrics.density
        val themeBounds = composeRule.onNodeWithContentDescription("主题 aigc")
            .fetchSemanticsNode().boundsInRoot
        val countBounds = composeRule.onAllNodesWithText("8 条", useUnmergedTree = true)[0]
            .fetchSemanticsNode().boundsInRoot
        val bottomMarginDp = (themeBounds.bottom - countBounds.bottom) / density
        assertTrue(
            "主题计数应完整显示并与卡片底部保持安全间距，实际为 ${bottomMarginDp}dp；卡片=$themeBounds，计数=$countBounds",
            bottomMarginDp >= 12f,
        )
        composeRule.onAllNodesWithText("行动建议")[1].assertIsDisplayed()
        composeRule.onNodeWithContentDescription("上一周").performClick()
        composeRule.onNodeWithText("5.5 - 5.11").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("下一周").performClick()
        composeRule.onNodeWithText("5.12 - 5.18").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("查看全部行动建议").performClick()
        waitForText("4 条建议待处理")
        composeRule.onAllNodesWithText("查看相关记忆")[0].performClick()
        waitForText("相关记忆")
        composeRule.onNodeWithText("整理 AIGC 比赛策划材料").assertIsDisplayed()
        composeRule.onNodeWithText("共关联 8 条记忆").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("返回").performClick()

        composeRule.onAllNodesWithText("已处理")[0].performClick()
        composeRule.onNodeWithText("3 条建议待处理").assertIsDisplayed()
        assertTrue(composeRule.onAllNodesWithText("整理 AIGC 比赛策划材料").fetchSemanticsNodes().isEmpty())
        composeRule.onAllNodesWithText("本周忽略")[0].performClick()
        composeRule.onNodeWithText("2 条建议待处理").assertIsDisplayed()
        assertTrue(composeRule.onAllNodesWithText("新建计划").fetchSemanticsNodes().isEmpty())
        assertTrue(composeRule.onAllNodesWithText("待办").fetchSemanticsNodes().isEmpty())
        composeRule.onNodeWithContentDescription("返回").performClick()

        composeRule.onNodeWithContentDescription("查看全部主题").performClick()
        waitForText("8 个主题 · 共 47 条记忆")
        composeRule.onNodeWithContentDescription("主题 guangzhou").performClick()
        waitForText("广州旅行")
        composeRule.onNodeWithText("共 6 条记忆").assertIsDisplayed()
        composeRule.onNodeWithText("广州两日路线草图").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("主题分布").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("5.12 - 5.18").assertIsDisplayed()
    }

    private fun enterHome() {
        waitForText("欢迎使用 Idea Loop")
        composeRule.onNodeWithText("获取验证码").performClick()
        composeRule.onNodeWithTag("verification_input").performTextInput("123456")
        composeRule.onNodeWithText("登录 / 注册").performClick()
        composeRule.onNodeWithText("开启旅途").performClick()
        waitForText("搜索记忆...")
    }

    private fun waitForText(text: String) {
        composeRule.waitUntil(timeoutMillis = 6_000) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
