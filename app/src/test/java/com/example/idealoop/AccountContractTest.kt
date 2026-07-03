package com.example.idealoop

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class AccountContractTest {
    @Test
    fun accountRoutes_matchFigmaFlow() {
        val contractClass = runCatching {
            Class.forName("com.example.idealoop.feature.account.AccountContract")
        }.getOrNull()

        assertNotNull("Account contract must exist", contractClass)

        val contract = contractClass!!.getField("INSTANCE").get(null)
        assertEquals("P01", contractClass.getMethod("getPhoneRoute").invoke(contract))
        assertEquals("P02", contractClass.getMethod("getVerificationRoute").invoke(contract))
        assertEquals("P03", contractClass.getMethod("getProfileRoute").invoke(contract))
    }

    @Test
    fun accountInputRules_keepLocalInputsDeterministic() {
        val rulesClass = runCatching {
            Class.forName("com.example.idealoop.feature.account.AccountInputRules")
        }.getOrNull()

        assertNotNull("Account input rules must exist", rulesClass)

        val rules = rulesClass!!.getField("INSTANCE").get(null)
        assertEquals(
            "138 8888 1024",
            rulesClass.getMethod("formatPhone", String::class.java).invoke(rules, "138a8888-1024"),
        )
        assertEquals(
            "123456",
            rulesClass.getMethod("verificationCode", String::class.java).invoke(rules, "12a345678"),
        )
    }
}
