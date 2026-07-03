package com.example.idealoop.feature.awake

import com.example.idealoop.feature.memory.MemoryItem
import com.example.idealoop.feature.memory.MemorySource

object AwakeContract {
    const val listRoute = "P16"
    const val locationRoute = "P17"
    const val timeRoute = "P18"
    val detailSections = listOf("唤醒原因", "原始记忆", "相关记忆")

    fun routeFor(trigger: AwakeTrigger): String = when (trigger) {
        AwakeTrigger.Location,
        AwakeTrigger.Relation,
        -> locationRoute
        AwakeTrigger.Time -> timeRoute
    }
}

enum class AwakeTrigger(val label: String) {
    Location("地点唤醒"),
    Time("时间唤醒"),
    Relation("关联唤醒"),
}

data class AwakeItem(
    val id: String,
    val trigger: AwakeTrigger,
    val title: String,
    val description: String,
    val meta: String,
    val recencyOrder: Int,
)

data class AwakeDetail(
    val triggerLine: String,
    val reason: String,
    val originalMemory: MemoryItem,
    val relatedMemories: List<MemoryItem>,
)

object AwakeSampleData {
    val items = listOf(
        AwakeItem(
            id = "market",
            trigger = AwakeTrigger.Location,
            title = "经过永辉超市",
            description = "你上周保存过番茄牛腩菜谱，可以顺路买齐食材。",
            meta = "5 分钟前 · 距你 200m",
            recencyOrder = 1,
        ),
        AwakeItem(
            id = "competition",
            trigger = AwakeTrigger.Time,
            title = "AIGC 创新赛提交日临近",
            description = "你保存过策划模板和评分表，距截止还有 3 天。",
            meta = "今早 09:00",
            recencyOrder = 2,
        ),
        AwakeItem(
            id = "guangzhou",
            trigger = AwakeTrigger.Relation,
            title = "正在整理广州旅行计划",
            description = "3 条旧记忆可能有用：陶陶居、珠江夜游、沙面路线。",
            meta = "昨天 21:30",
            recencyOrder = 3,
        ),
        AwakeItem(
            id = "library",
            trigger = AwakeTrigger.Location,
            title = "回到图书馆",
            description = "你在这里保存过 4 份毕业设计参考资料。",
            meta = "前天 14:20",
            recencyOrder = 4,
        ),
    )

    val locationDetail = AwakeDetail(
        triggerLine = "永辉超市 · 距你 200m",
        reason = "你上周保存过「番茄牛腩菜谱」，现在经过超市附近，正适合采购食材。",
        originalMemory = MemoryItem(
            id = "recipe",
            title = "番茄牛腩菜谱",
            summary = "食材：番茄 4 个、牛腩 500g、姜片、葱…",
            tags = listOf("菜谱"),
            source = MemorySource.Text,
            time = "上周三",
        ),
        relatedMemories = listOf(
            MemoryItem(
                id = "shopping-list",
                title = "周末买菜清单",
                summary = "本周末计划做的菜与对应食材",
                tags = listOf("生活"),
                source = MemorySource.Text,
                time = "昨天",
            ),
            MemoryItem(
                id = "market-hours",
                title = "家附近超市营业时间",
                summary = "永辉、盒马、华润营业时段记录",
                tags = listOf("生活"),
                source = MemorySource.Image,
                time = "上周",
            ),
        ),
    )

    val timeDetail = AwakeDetail(
        triggerLine = "距截止 3 天",
        reason = "AIGC 创新赛提交日临近，你保存过的策划模板和评分表可能现在派得上用场。",
        originalMemory = MemoryItem(
            id = "aigc",
            title = "AIGC 创新赛策划模板",
            summary = "作品策划模板、复赛准备",
            tags = listOf("比赛"),
            source = MemorySource.Link,
            time = "昨天",
        ),
        relatedMemories = listOf(
            MemoryItem(
                id = "score-sheet",
                title = "比赛评分表",
                summary = "评审维度、加分项说明",
                tags = listOf("比赛"),
                source = MemorySource.Image,
                time = "3 天前",
            ),
            MemoryItem(
                id = "aigc-cases",
                title = "AIGC 案例收集",
                summary = "参考作品与灵感整理",
                tags = listOf("比赛", "灵感"),
                source = MemorySource.Link,
                time = "上周",
            ),
        ),
    )
}
