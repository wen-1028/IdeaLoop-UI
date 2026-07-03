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
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccountFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun p01ToP04_supportsForwardAndBackFlow() {
        composeRule.onNodeWithText("获取验证码").assertIsDisplayed()
        assertTrue(
            composeRule.onAllNodesWithText("让随手记录，\n在未来重新产生价值")
                .fetchSemanticsNodes().isEmpty(),
        )
        composeRule.onNodeWithText("获取验证码").assertIsDisplayed()

        composeRule.onNodeWithText("获取验证码").performClick()
        composeRule.onNodeWithText("输入验证码").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("欢迎使用 Idea Loop").assertIsDisplayed()

        composeRule.onNodeWithText("获取验证码").performClick()
        composeRule.onNodeWithTag("verification_input").performTextInput("123456")
        composeRule.onNodeWithText("登录 / 注册").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("让 Idea Loop 更懂你").assertIsDisplayed()
        composeRule.onNodeWithText("Wen").performClick()
        composeRule.onNodeWithText("开启旅途").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("输入验证码").assertIsDisplayed()
        composeRule.onNodeWithText("登录 / 注册").performClick()
        composeRule.onNodeWithText("开启旅途").assertIsDisplayed().performClick()
        composeRule.onNodeWithText("搜索记忆...").assertIsDisplayed()

        composeRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        composeRule.onNodeWithText("让 Idea Loop 更懂你").assertIsDisplayed()
    }

    private fun waitForText(text: String) {
        composeRule.waitUntil(timeoutMillis = 6_000) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
