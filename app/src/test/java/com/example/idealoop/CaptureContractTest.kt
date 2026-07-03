package com.example.idealoop

import com.example.idealoop.feature.capture.CaptureContract
import com.example.idealoop.feature.capture.ChatMessageContent
import com.example.idealoop.feature.capture.ChatMode
import com.example.idealoop.feature.capture.CaptureInputKind
import com.example.idealoop.feature.capture.captureDestination
import com.example.idealoop.feature.capture.parseLocalMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CaptureContractTest {
    @Test
    fun p06RouteAndModesMatchFigma() {
        assertEquals("P06", CaptureContract.route)
        assertEquals(listOf("Chat", "Record"), ChatMode.entries.map { it.label })
        assertEquals("向小灵提问…", ChatMode.Chat.placeholder)
        assertEquals("输入记忆内容…", ChatMode.Record.placeholder)
    }

    @Test
    fun blankDraftIsNotSent() {
        assertNull(parseLocalMessage("   "))
    }

    @Test
    fun textAndLinkDraftsProduceDistinctLocalMessages() {
        val text = parseLocalMessage("帮我找一下比赛资料")
        val link = parseLocalMessage("https://example.com/idea-loop")

        assertTrue(text is ChatMessageContent.Text)
        assertEquals(
            "https://example.com/idea-loop",
            (link as ChatMessageContent.Link).url,
        )
    }

    @Test
    fun captureRoutesMatchFigmaPageNumbers() {
        assertEquals("P06", CaptureContract.route)
        assertEquals("P07", CaptureContract.analysisRoute)
        assertEquals("P09", CaptureContract.pendingMemoryRoute)
        assertEquals("P10", CaptureContract.savedRoute)
        assertEquals("P36", CaptureContract.chatVoiceRoute)
        assertEquals("P37", CaptureContract.recordVoiceRoute)
        assertEquals("P38", CaptureContract.albumRoute)
        assertEquals("P39", CaptureContract.chatPhotosRoute)
    }

    @Test
    fun chatInputsNeverEnterSaveFlow() {
        assertNull(captureDestination(ChatMode.Chat, CaptureInputKind.Text))
        assertNull(captureDestination(ChatMode.Chat, CaptureInputKind.Link))
        assertEquals(
            CaptureContract.chatVoiceRoute,
            captureDestination(ChatMode.Chat, CaptureInputKind.Voice),
        )
        assertEquals(
            CaptureContract.chatPhotosRoute,
            captureDestination(ChatMode.Chat, CaptureInputKind.Photos),
        )
    }

    @Test
    fun recordInputsAlwaysEnterAnalysisBeforeSave() {
        assertEquals(
            CaptureContract.analysisRoute,
            captureDestination(ChatMode.Record, CaptureInputKind.Text),
        )
        assertEquals(
            CaptureContract.analysisRoute,
            captureDestination(ChatMode.Record, CaptureInputKind.Link),
        )
        assertEquals(
            CaptureContract.recordVoiceRoute,
            captureDestination(ChatMode.Record, CaptureInputKind.Voice),
        )
        assertEquals(
            CaptureContract.analysisRoute,
            captureDestination(ChatMode.Record, CaptureInputKind.Photos),
        )
    }
}
