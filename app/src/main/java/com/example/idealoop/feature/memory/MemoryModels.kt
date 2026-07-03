package com.example.idealoop.feature.memory

object MemoryContract {
    const val libraryRoute = "P11"
    const val detailRoutePattern = "P12/{memoryId}"
    const val memoryIdArgument = "memoryId"
    val detailSections = listOf("标题", "摘要", "内容", "标签", "关联记忆")

    fun detailRoute(memoryId: String) = "P12/$memoryId"
}

enum class MemorySource {
    Image,
    Text,
    Voice,
    Link,
    Shot,
}

enum class MemoryFilter(
    val label: String,
    val source: MemorySource?,
) {
    All("全部", null),
    Image("图片", MemorySource.Image),
    Text("文字", MemorySource.Text),
    Voice("语音", MemorySource.Voice),
    Link("链接", MemorySource.Link),
}

data class MemoryItem(
    val id: String,
    val title: String,
    val summary: String,
    val tags: List<String>,
    val source: MemorySource,
    val time: String,
    val place: String? = null,
)

data class MemoryDetail(
    val item: MemoryItem,
    val savedTime: String,
    val content: String,
    val detailTags: List<String>,
    val related: List<MemoryItem>,
)

object MemorySampleData {
    val library = listOf(
        MemoryItem(
            id = "aigc",
            title = "AIGC 创新赛策划模板",
            summary = "比赛作品策划模板、提交要求、复赛准备清单与评分表",
            tags = listOf("比赛项目", "待处理"),
            source = MemorySource.Link,
            time = "昨天 21:08",
        ),
        MemoryItem(
            id = "guangzhou",
            title = "广州周末旅行攻略",
            summary = "景点推荐、餐厅清单、两日行程路线，适合周末使用",
            tags = listOf("旅行攻略", "周末可用"),
            source = MemorySource.Image,
            time = "2 天前",
            place = "广州",
        ),
        MemoryItem(
            id = "recipe",
            title = "番茄牛腩菜谱",
            summary = "食材清单、烹饪步骤，到超市附近时可提醒采购",
            tags = listOf("菜谱", "到地点提醒"),
            source = MemorySource.Text,
            time = "上周三",
        ),
        MemoryItem(
            id = "coldplay",
            title = "Coldplay 演唱会信息",
            summary = "活动时间、地点、票务信息、待确认行程",
            tags = listOf("活动", "待确认"),
            source = MemorySource.Link,
            time = "上周一",
        ),
        MemoryItem(
            id = "agent-paper",
            title = "AI Agent 论文阅读笔记",
            summary = "个人知识管理、记忆网络相关论文与灵感",
            tags = listOf("灵感", "学习资料"),
            source = MemorySource.Voice,
            time = "5 月 18 日",
        ),
    )

    fun filtered(filter: MemoryFilter): List<MemoryItem> =
        filter.source?.let { source -> library.filter { it.source == source } } ?: library

    private val zhujiang = MemoryItem(
        id = "zhujiang",
        title = "珠江夜游船票收藏",
        summary = "同一地点 · 可能用于周末计划",
        tags = listOf("旅行"),
        source = MemorySource.Link,
        time = "3 天前",
        place = "广州",
    )

    private val taotaoju = MemoryItem(
        id = "taotaoju",
        title = "陶陶居点心推荐",
        summary = "相似主题 · 早茶餐厅清单",
        tags = listOf("美食"),
        source = MemorySource.Image,
        time = "上周",
        place = "广州",
    )

    private val guangzhou = library.first { it.id == "guangzhou" }

    private val details = mapOf(
        "guangzhou" to MemoryDetail(
            item = guangzhou.copy(
                summary = "两日行程包括沙面、永庆坊、陈家祠等景点；推荐 5 家餐厅含点都德、陶陶居；建议周五晚出发，周日下午返程。",
            ),
            savedTime = "2 天前保存",
            content = "沙面欧式建筑步道、永庆坊潮玩街区、陈家祠岭南建筑、北京路步行街、珠江夜游。",
            detailTags = listOf("旅行攻略", "广州", "周末可用"),
            related = listOf(zhujiang, taotaoju),
        ),
        "zhujiang" to MemoryDetail(
            item = zhujiang.copy(summary = "珠江夜游票务信息与登船码头，适合广州周末行程的夜间安排。"),
            savedTime = "3 天前保存",
            content = "天字码头出发，全程 60 分钟，途经海心沙、广州塔；建议提前 30 分钟到达取票。",
            detailTags = listOf("旅行", "广州", "夜游"),
            related = listOf(
                guangzhou.copy(summary = "两日行程、餐厅与路线", tags = listOf("旅行攻略")),
                taotaoju,
            ),
        ),
        "taotaoju" to MemoryDetail(
            item = taotaoju.copy(summary = "陶陶居必点早茶清单与最佳到店时间，适合周末早茶聚会。"),
            savedTime = "上周保存",
            content = "招牌虾饺、流沙包、凤爪、马拉糕；建议工作日 10 点前到店，可避开排队高峰。",
            detailTags = listOf("美食", "广州", "早茶"),
            related = listOf(
                guangzhou.copy(summary = "两日行程、餐厅与路线", tags = listOf("旅行攻略")),
                zhujiang.copy(summary = "夜间路线推荐"),
            ),
        ),
    )

    fun detail(memoryId: String): MemoryDetail = details[memoryId] ?: details.getValue("guangzhou")
}
