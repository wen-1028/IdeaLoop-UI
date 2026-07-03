package com.example.idealoop.feature.account

object AccountContract {
    val phoneRoute = "P01"
    val verificationRoute = "P02"
    val profileRoute = "P03"
}

object AccountInputRules {
    fun formatPhone(value: String): String {
        val digits = value.filter(Char::isDigit).take(11)
        return buildList {
            if (digits.isNotEmpty()) add(digits.take(3))
            if (digits.length > 3) add(digits.substring(3, minOf(7, digits.length)))
            if (digits.length > 7) add(digits.substring(7))
        }.joinToString(" ")
    }

    fun verificationCode(value: String): String =
        value.filter(Char::isDigit).take(6)
}

enum class MemoryPreference(val label: String) {
    Learning("学习资料"),
    Inspiration("创作灵感"),
    Work("工作项目"),
    Travel("旅行生活"),
    Tasks("日常待办"),
    Reading("知识阅读"),
}

object AccountSampleData {
    val phone = "138 8888 1024"
    val nickname = "Wen"
    val selectedPreferences = setOf(
        MemoryPreference.Inspiration,
        MemoryPreference.Learning,
    )
}
