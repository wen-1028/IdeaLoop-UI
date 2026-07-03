package com.example.idealoop

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AwakeFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeCarouselListDetailsAndBackFlowMatchFigma() {
        enterHome()

        composeRule.onNodeWithText("你快到永辉超市了").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("今日唤醒轮播").performTouchInput { swipeLeft() }
        composeRule.onNodeWithText("AIGC 比赛截止还有 3 天").assertIsDisplayed()

        composeRule.onNodeWithText("全部").performClick()
        composeRule.onNodeWithText("今日唤醒").assertIsDisplayed()
        composeRule.onNodeWithText("经过永辉超市").assertIsDisplayed()
        composeRule.onAllNodesWithText("地点唤醒")[0].assertIsDisplayed()
        composeRule.onNodeWithText("时间唤醒").assertIsDisplayed()
        composeRule.onNodeWithText("关联唤醒").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("唤醒卡片 market").performClick()
        composeRule.onNodeWithText("位置唤醒").assertIsDisplayed()
        assertDetailSections()
        composeRule.onNodeWithText("番茄牛腩菜谱").performClick()
        composeRule.onNodeWithText("记忆详情").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("位置唤醒").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("返回").performClick()

        composeRule.onNodeWithContentDescription("唤醒卡片 competition").performClick()
        composeRule.onNodeWithText("时间唤醒").assertIsDisplayed()
        assertDetailSections()
        assertTrue(composeRule.onAllNodesWithText("推荐动作").fetchSemanticsNodes().isEmpty())
        assertTrue(composeRule.onAllNodesWithText("提醒").fetchSemanticsNodes().isEmpty())
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("搜索记忆...").assertIsDisplayed()
    }

    private fun assertDetailSections() {
        composeRule.onNodeWithText("唤醒原因").assertIsDisplayed()
        composeRule.onNodeWithText("原始记忆").assertIsDisplayed()
        composeRule.onNodeWithText("相关记忆").assertIsDisplayed()
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
