package com.example.idealoop

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun homeAndLibrarySearch_supportRecentNaturalEmptyDetailAndBackFlows() {
        enterHome()

        composeRule.onNodeWithText("搜索记忆...").performClick()
        waitForText("最近搜索")
        composeRule.onNodeWithText("广州旅行").performClick()
        composeRule.onNodeWithText("搜索结果").assertIsDisplayed()
        composeRule.onAllNodesWithText("AIGC 创新赛策划模板").assertCountEquals(0)
        composeRule.onNodeWithText("广州周末旅行攻略").performClick()
        composeRule.onNodeWithText("记忆详情").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("搜索结果").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("最近搜索").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("搜索记忆...").assertIsDisplayed()

        composeRule.onNodeWithText("记忆").performClick()
        composeRule.onNodeWithContentDescription("搜索").performClick()
        composeRule.onNodeWithTag("search_input")
            .performTextReplacement("帮我找一下广州周末旅行相关的记忆")
        composeRule.onNodeWithTag("search_input").performImeAction()
        composeRule.onNodeWithText("找到 3 条相关记忆，已按相关度排序。").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("切换模式").assertTextEquals("Chat")
        composeRule.onNodeWithText("广州周末旅行攻略").performClick()
        composeRule.onNodeWithText("记忆详情").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithContentDescription("收起相关记忆").performClick()
        composeRule.onAllNodesWithText("广州周末旅行攻略").assertCountEquals(0)
        composeRule.onNodeWithContentDescription("返回").performClick()

        composeRule.onNodeWithTag("search_input").performTextReplacement("火星基地")
        composeRule.onNodeWithTag("search_input").performImeAction()
        composeRule.onNodeWithText("没有找到与「火星基地」相关的记忆").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("最近搜索").assertIsDisplayed()
    }

    @Test
    fun resultQuery_canBeTappedToSearchAgain() {
        enterHome()

        composeRule.onNodeWithText("搜索记忆...").performClick()
        composeRule.onNodeWithTag("search_input").performTextReplacement("广州")
        composeRule.onNodeWithTag("search_input").performImeAction()
        composeRule.onNodeWithText("搜索结果").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("修改搜索").performClick()
        composeRule.onNodeWithText("搜索结果").assertIsDisplayed()
        composeRule.onAllNodesWithText("最近搜索").assertCountEquals(0)
        composeRule.onNodeWithTag("search_results_input").assertTextEquals("广州")
        composeRule.onNodeWithTag("search_results_input").performTextInput("行")
        composeRule.onNodeWithTag("search_results_input").assertTextEquals("广州行")
        composeRule.onNodeWithTag("search_results_input").performTextReplacement("AIGC")
        composeRule.onNodeWithTag("search_results_input").performImeAction()

        composeRule.onNodeWithText("搜索结果").assertIsDisplayed()
        composeRule.onNodeWithText("AIGC 创新赛策划模板").assertIsDisplayed()
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
