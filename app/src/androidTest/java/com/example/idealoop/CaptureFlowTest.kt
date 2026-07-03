package com.example.idealoop

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.geometry.Offset
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CaptureFlowTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun centralAddOpensP06_andSupportsModesTextLinksScrollingAndBack() {
        enterHome()

        composeRule.onNodeWithContentDescription("添加").performClick()
        composeRule.onNodeWithText("小灵").assertIsDisplayed()
        composeRule.onNodeWithText("嗨 Wen，有什么想问的，或者让我帮你搜索记忆？").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("切换模式").assertTextEquals("Chat")

        composeRule.onNodeWithTag("chat_input").performTextInput("帮我找比赛资料")
        composeRule.onNodeWithTag("chat_input").assertIsFocused()
        composeRule.onNodeWithText("小灵").assertIsDisplayed()
        composeRule.onNodeWithText("嗨 Wen，有什么想问的，或者让我帮你搜索记忆？").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("发送").performClick()
        composeRule.onNodeWithTag("chat_input").assertIsNotFocused()
        composeRule.onNodeWithText("帮我找比赛资料").assertIsDisplayed()

        composeRule.onNodeWithTag("chat_input").performTextInput("https://example.com/idea-loop")
        composeRule.onNodeWithContentDescription("发送").performClick()
        composeRule.onNodeWithContentDescription("链接消息").assertIsDisplayed()
        composeRule.onNodeWithText("https://example.com/idea-loop").assertIsDisplayed()

        repeat(6) { index ->
            val message = "本地记录 ${index + 1}"
            composeRule.onNodeWithTag("chat_input").performTextInput(message)
            composeRule.onNodeWithContentDescription("发送").performClick()
        }
        composeRule.onNodeWithText("本地记录 6").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("切换模式").performClick()
        composeRule.onNodeWithText("Record").performClick()
        composeRule.onNodeWithContentDescription("切换模式").assertTextEquals("Record")
        composeRule.onNodeWithText("输入记忆内容…").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("搜索记忆...").assertIsDisplayed()
    }

    @Test
    fun chatVoiceAndPhotosStayOutsideSaveFlow() {
        enterHome()
        composeRule.onNodeWithContentDescription("添加").performClick()

        composeRule.onNodeWithContentDescription("语音").performClick()
        composeRule.onNodeWithText("按住说话").performTouchInput {
            down(center)
            advanceEventTime(800)
            up()
        }
        waitForText("听到啦，正在为你检索相关记忆…")
        assertTrue(composeRule.onAllNodesWithText("待保存记忆卡片").fetchSemanticsNodes().isEmpty())
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("搜索记忆...").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("添加").performClick()
        composeRule.onNodeWithContentDescription("附件").performClick()
        composeRule.onNodeWithText("相册").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("选择照片 p1").performClick()
        composeRule.onNodeWithText("发送 · 1").performClick()
        waitForText("收到 1 张照片，已为你找到 3 条相关记忆。")
        assertTrue(composeRule.onAllNodesWithText("待保存记忆卡片").fetchSemanticsNodes().isEmpty())

        composeRule.onNodeWithTag("chat_input").performTextInput("继续问广州行程")
        composeRule.onNodeWithContentDescription("发送").performClick()
        waitForText("继续问广州行程")
        val relatedTop = composeRule.onNodeWithText("相关记忆")
            .fetchSemanticsNode().boundsInRoot.top
        val latestMessageTop = composeRule.onNodeWithText("继续问广州行程")
            .fetchSemanticsNode().boundsInRoot.top
        assertTrue("最新消息应显示在相关记忆下方", latestMessageTop > relatedTop)

        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("搜索记忆...").assertIsDisplayed()
        assertTrue(composeRule.onAllNodesWithText("相册").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun voiceHold_canSlideUpToCancelWithoutSending() {
        enterHome()
        composeRule.onNodeWithContentDescription("添加").performClick()
        composeRule.onNodeWithContentDescription("语音").performClick()
        composeRule.onNodeWithContentDescription("按住说话，上滑取消").assertIsDisplayed()

        composeRule.onNodeWithText("按住说话").performTouchInput {
            val start = center
            down(start)
            advanceEventTime(300)
            moveTo(start + Offset(0f, -240f))
            advanceEventTime(300)
            up()
        }

        composeRule.onNodeWithText("按住说话").assertIsDisplayed()
        assertTrue(
            "取消录音后不应进入语音结果页",
            composeRule.onAllNodesWithText("听到啦，正在为你检索相关记忆…")
                .fetchSemanticsNodes().isEmpty(),
        )
    }

    @Test
    fun recordTextAnalyzesEditsSavesAndClosesBackToP06() {
        enterHome()
        composeRule.onNodeWithContentDescription("添加").performClick()
        composeRule.onNodeWithContentDescription("切换模式").performClick()
        composeRule.onNodeWithText("Record").performClick()

        composeRule.onNodeWithTag("chat_input").performTextInput("帮我整理广州周末攻略")
        composeRule.onNodeWithContentDescription("发送").performClick()
        waitForText("正在整理成记忆卡片")
        waitForText("待保存记忆卡片", timeoutMillis = 4_000)
        composeRule.onNodeWithText("标题").assertIsDisplayed()
        composeRule.onNodeWithText("摘要").assertIsDisplayed()
        composeRule.onNodeWithText("标签").assertIsDisplayed()
        composeRule.onNodeWithText("内容类型").assertIsDisplayed()

        composeRule.onNodeWithText("重新分析").performClick()
        waitForText("正在整理成记忆卡片")
        waitForText("待保存记忆卡片", timeoutMillis = 4_000)

        composeRule.onNodeWithText("修改").performClick()
        composeRule.onNodeWithText("完成").assertIsDisplayed()
        composeRule.onNodeWithText("完成").performClick()
        composeRule.onNodeWithText("保存").performClick()
        composeRule.onNodeWithText("已保存到你的记忆库").assertIsDisplayed()
        composeRule.onNodeWithText("关闭").performClick()

        composeRule.onNodeWithText("小灵").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("切换模式").assertTextEquals("Record")
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("搜索记忆...").assertIsDisplayed()
    }

    @Test
    fun recordVoiceAndPhotosReachPendingCardThroughAnalysis() {
        enterHome()
        composeRule.onNodeWithContentDescription("添加").performClick()
        composeRule.onNodeWithContentDescription("切换模式").performClick()
        composeRule.onNodeWithText("Record").performClick()

        composeRule.onNodeWithContentDescription("语音").performClick()
        composeRule.onNodeWithText("按住说话").performTouchInput {
            down(center)
            advanceEventTime(800)
            up()
        }
        waitForText("正在整理成记忆卡片")
        waitForText("待保存记忆卡片", timeoutMillis = 4_000)
        composeRule.onNodeWithContentDescription("返回").performClick()
        composeRule.onNodeWithText("搜索记忆...").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("添加").performClick()
        composeRule.onNodeWithContentDescription("切换模式").assertTextEquals("Record")
        composeRule.onNodeWithContentDescription("附件").performClick()
        composeRule.onNodeWithContentDescription("选择照片 p3").performClick()
        composeRule.onNodeWithText("发送 · 1").performClick()
        waitForText("正在整理成记忆卡片")
        waitForText("待保存记忆卡片", timeoutMillis = 4_000)
        composeRule.onNodeWithText("图片").assertIsDisplayed()
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
