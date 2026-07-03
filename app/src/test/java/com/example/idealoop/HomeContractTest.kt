package com.example.idealoop

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class HomeContractTest {
    @Test
    fun homeContract_matchesFigmaP04() {
        val contractClass = runCatching {
            Class.forName("com.example.idealoop.feature.home.HomeContract")
        }.getOrNull()

        assertNotNull("P04 home contract must exist", contractClass)

        val contract = contractClass!!.getField("INSTANCE").get(null)
        assertEquals("P04", contractClass.getMethod("getRoute").invoke(contract))
        assertEquals("Idea Loop", contractClass.getMethod("getTitle").invoke(contract))
        assertEquals("搜索记忆...", contractClass.getMethod("getSearchHint").invoke(contract))
    }

    @Test
    fun sampleData_matchesFigmaNormalState() {
        val sampleClass = runCatching {
            Class.forName("com.example.idealoop.feature.home.HomeSampleData")
        }.getOrNull()

        assertNotNull("P04 sample data must exist", sampleClass)

        val sample = sampleClass!!.getField("INSTANCE").get(null)
        val state = sampleClass.getMethod("getNormal").invoke(sample)
        val stateClass = state.javaClass

        assertEquals(23, stateClass.getMethod("getCaptureCount").invoke(state))
        assertEquals(4, stateClass.getMethod("getSuggestionCount").invoke(state))
        assertEquals(3, (stateClass.getMethod("getAwakeCards").invoke(state) as List<*>).size)
        assertEquals(3, (stateClass.getMethod("getRecentMemories").invoke(state) as List<*>).size)
    }
}
