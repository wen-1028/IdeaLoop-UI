package com.example.idealoop

import org.junit.Assert.assertNull
import org.junit.Test

class StartupContractTest {
    @Test
    fun startupContract_isRemoved() {
        val contractClass = runCatching {
            Class.forName("com.example.idealoop.feature.startup.StartupContract")
        }.getOrNull()

        assertNull("P00 startup contract must not exist", contractClass)
    }
}
