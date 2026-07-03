package com.example.idealoop.feature.home

import com.example.idealoop.feature.memory.MemoryItem
import com.example.idealoop.feature.memory.MemorySource

object HomeContract {
    val route = "P04"
    val title = "Idea Loop"
    val searchHint = "搜索记忆..."
}

enum class AwakeTriggerType {
    Location,
    Time,
    Relation,
}

data class AwakeCard(
    val id: String,
    val triggerType: AwakeTriggerType,
    val triggerLabel: String,
    val title: String,
    val content: String,
)

typealias RecentMemory = MemoryItem

data class HomeUiState(
    val captureCount: Int,
    val suggestionCount: Int,
    val awakeCards: List<AwakeCard>,
    val recentMemories: List<RecentMemory>,
)

object HomeSampleData {
    val normal = HomeUiState(
        captureCount = 23,
        suggestionCount = 4,
        awakeCards = listOf(
            AwakeCard(
                id = "a1",
                triggerType = AwakeTriggerType.Location,
                triggerLabel = "地点唤醒 · 距你 200m",
                title = "你快到永辉超市了",
                content = "我想起你上周保存过「番茄牛腩菜谱」。",
            ),
            AwakeCard(
                id = "a2",
                triggerType = AwakeTriggerType.Time,
                triggerLabel = "时间唤醒 · 截止前 3 天",
                title = "AIGC 比赛截止还有 3 天",
                content = "你保存过「作品策划模板」，里面有评分标准和提交要求。",
            ),
            AwakeCard(
                id = "a3",
                triggerType = AwakeTriggerType.Relation,
                triggerLabel = "关联唤醒 · 发现 3 条相关记忆",
                title = "你正在整理广州旅行计划",
                content = "发现 3 条与「广州」相关的记忆，包含攻略、餐厅和行程路线。",
            ),
        ),
        recentMemories = listOf(
            RecentMemory(
                id = "m1",
                title = "AIGC 创新赛策划模板",
                summary = "比赛作品策划模板、提交要求、复赛准备清单与评分表",
                tags = listOf("比赛项目", "待处理"),
                source = MemorySource.Link,
                time = "昨天 21:08",
            ),
            RecentMemory(
                id = "guangzhou",
                title = "广州周末旅行攻略",
                summary = "景点推荐、餐厅清单、两日行程路线，适合周末使用",
                tags = listOf("旅行攻略", "周末可用"),
                source = MemorySource.Image,
                time = "2 天前",
                place = "广州",
            ),
            RecentMemory(
                id = "m3",
                title = "每周设计灵感收集",
                summary = "本周收集到的10个超赞的网页设计参考，包含卡片拟态和排版。",
                tags = listOf("灵感", "设计"),
                source = MemorySource.Text,
                time = "3 天前",
                place = "Web",
            ),
        ),
    )
}
