package com.example.idealoop

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MemoryFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun libraryAndDetail_keepBarsFixedAndSupportBothEntryPoints() {
        enterHome()

        composeRule.onNodeWithText("记忆").performClick()
        waitForText("AIGC 创新赛策划模板")

        composeRule.onNodeWithText("图片").performClick()
        composeRule.onNodeWithText("广州周末旅行攻略").assertIsDisplayed()
        composeRule.onAllNodesWithText("AIGC 创新赛策划模板").assertCountEquals(0)
        composeRule.onNodeWithText("全部").performClick()

        composeRule.onNodeWithTag("memory_list")
            .performScrollToNode(hasText("AI Agent 论文阅读笔记"))
        composeRule.onNodeWithTag("memory_top_bar").assertIsDisplayed()
        composeRule.onNodeWithTag("bottom_navigation").assertIsDisplayed()

        composeRule.onNodeWithText("广州周末旅行攻略").performClick()
        composeRule.onNodeWithText("记忆详情").assertIsDisplayed()
        composeRule.onNodeWithText("摘要").assertIsDisplayed()
        composeRule.onNodeWithText("内容").assertIsDisplayed()
        composeRule.onNodeWithText("标签").assertIsDisplayed()
        composeRule.onNodeWithText("关联记忆").assertIsDisplayed()
        composeRule.onNodeWithTag("memory_detail_list")
            .performScrollToNode(hasText("关联记忆"))
        composeRule.onNodeWithText("记忆详情").assertIsDisplayed()
        composeRule.onNodeWithText("珠江夜游船票收藏").performClick()
        composeRule.onNodeWithText("珠江夜游船票收藏").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("广州周末旅行攻略").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithTag("memory_list").assertIsDisplayed()

        composeRule.onNodeWithText("首页").performClick()
        composeRule.onNodeWithText("广州周末旅行攻略").performClick()
        composeRule.onNodeWithText("记忆详情").assertIsDisplayed()
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
