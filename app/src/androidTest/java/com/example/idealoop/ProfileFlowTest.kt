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
class ProfileFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun profilePermissionsLogoutAndBackFlowMatchFigma() {
        enterHome()
        composeRule.onNodeWithText("我的").performClick()
        waitForText("Wen")

        listOf("个人信息", "记忆偏好", "隐私与权限", "账号安全", "意见反馈", "退出登录").forEach {
            composeRule.onNodeWithText(it).assertIsDisplayed()
        }

        composeRule.onNodeWithText("隐私与权限").performClick()
        waitForText("隐私优先")
        composeRule.onNodeWithText("用于在合适地点唤醒相关记忆").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("位置权限 已开启").performClick()
        composeRule.onNodeWithContentDescription("位置权限 已关闭").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("Wen").assertIsDisplayed()

        composeRule.onNodeWithText("退出登录").performClick()
        composeRule.onNodeWithText("确定退出登录？").assertIsDisplayed()
        composeRule.onNodeWithText("取消").performClick()
        composeRule.onNodeWithText("Wen").assertIsDisplayed()

        composeRule.onNodeWithText("退出登录").performClick()
        composeRule.onNodeWithText("确认").performClick()
        waitForText("欢迎使用 Idea Loop")
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
