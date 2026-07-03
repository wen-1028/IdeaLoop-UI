package com.example.idealoop

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CoreNavigationFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeEntryPointsAndBottomNavigation_keepExpectedBackStack() {
        enterHome()

        composeRule.onNodeWithText("行动建议").performClick()
        waitForText("4 条建议待处理")
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("搜索记忆...").assertIsDisplayed()

        composeRule.onNodeWithText("记忆").performClick()
        composeRule.onNodeWithTag("memory_list").assertIsDisplayed()
        composeRule.onNodeWithText("复盘").performClick()
        waitForText("本周记忆总览")
        composeRule.onNodeWithText("我的").performClick()
        waitForText("Wen")
        composeRule.onNodeWithText("首页").performClick()
        waitForText("搜索记忆...")
    }

    private fun enterHome() {
        waitForText("欢迎使用 Idea Loop")
        composeRule.onNodeWithText("获取验证码").performClick()
        composeRule.onNodeWithTag("verification_input").performTextInput("123456")
        composeRule.onNodeWithText("登录 / 注册").performClick()
        composeRule.onNodeWithText("开启旅途").performClick()
        waitForText("搜索记忆...")
    }

    private fun waitForText(text: String, timeoutMillis: Long = 6_000) {
        composeRule.waitUntil(timeoutMillis = timeoutMillis) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
