package com.example.idealoop.feature.review

import com.example.idealoop.feature.memory.MemoryItem
import com.example.idealoop.feature.memory.MemorySource

object ReviewContract {
    const val overviewRoute = "P19"
    const val suggestionsRoute = "P31"
    const val relatedRoutePattern = "P32/{suggestionId}"
    const val suggestionIdArgument = "suggestionId"
    const val themesRoute = "P33"
    const val themeMemoriesRoutePattern = "P34/{themeId}"
    const val themeIdArgument = "themeId"

    fun relatedRoute(suggestionId: String) = "P32/$suggestionId"
    fun themeMemoriesRoute(themeId: String) = "P34/$themeId"
}

enum class SuggestionAction(val label: String) {
    ViewRelated("查看相关记忆"),
    Handled("已处理"),
    IgnoreThisWeek("本周忽略"),
}

enum class ReviewThemeIcon {
    Idea,
    Travel,
    Study,
    Work,
    Food,
    Music,
    Sparkle,
    Camera,
}

data class ReviewSuggestion(
    val id: String,
    val title: String,
    val reason: String,
    val relatedCount: Int,
)

data class SuggestionCollection(
    val items: List<ReviewSuggestion>,
) {
    fun apply(id: String, action: SuggestionAction): SuggestionCollection = when (action) {
        SuggestionAction.ViewRelated -> this
        SuggestionAction.Handled,
        SuggestionAction.IgnoreThisWeek,
        -> copy(items = items.filterNot { it.id == id })
    }
}

data class ReviewWeek(
    val label: String,
    val total: Int,
    val themeCount: Int,
    val actionCount: Int,
    val daily: List<Int>,
    val suggestions: List<ReviewSuggestion>,
)

data class ReviewTheme(
    val id: String,
    val name: String,
    val count: Int,
    val tags: List<String>,
    val icon: ReviewThemeIcon,
    val accent: Long,
)

object ReviewSampleData {
    val currentSuggestions = listOf(
        ReviewSuggestion("aigc", "整理 AIGC 比赛策划材料", "本周保存了 8 条相关内容，临近提交日。", 8),
        ReviewSuggestion("guangzhou", "汇总广州两日行程", "你已有路线、餐厅、夜游 3 类信息。", 6),
        ReviewSuggestion("course", "把课程截图整理成复习卡片", "考试周临近，复习资料较分散。", 5),
        ReviewSuggestion("agent", "归档 AI Agent 论文笔记", "近期收藏的论文尚未整理。", 4),
    )

    private val previousSuggestions = listOf(
        ReviewSuggestion("interview", "整理面试问题清单", "近两周保存的面试题可以归为一组。", 7),
        ReviewSuggestion("recipe", "回顾上周食谱收藏", "你保存了 5 条菜谱但还没尝试。", 5),
        ReviewSuggestion("coffee", "汇总朋友推荐的咖啡馆", "朋友推荐了 4 家咖啡馆，可建一张地图。", 4),
    )

    val weeks = listOf(
        ReviewWeek("5.12 - 5.18", 23, 3, 4, listOf(2, 4, 3, 5, 3, 4, 2), currentSuggestions),
        ReviewWeek("5.5 - 5.11", 17, 2, 3, listOf(1, 3, 2, 4, 2, 3, 2), previousSuggestions),
    )

    val themes = listOf(
        ReviewTheme("aigc", "AIGC 比赛", 8, listOf("比赛", "待处理"), ReviewThemeIcon.Idea, 0xFF6366F1),
        ReviewTheme("guangzhou", "广州旅行", 6, listOf("旅行", "周末"), ReviewThemeIcon.Travel, 0xFF06B6D4),
        ReviewTheme("course", "课程复习", 5, listOf("学习", "笔记"), ReviewThemeIcon.Study, 0xFFA855F7),
        ReviewTheme("interview", "求职面试", 7, listOf("面试题", "公司清单"), ReviewThemeIcon.Work, 0xFF0EA5E9),
        ReviewTheme("food", "美食探店", 5, listOf("餐厅", "甜品"), ReviewThemeIcon.Food, 0xFFF59E0B),
        ReviewTheme("concert", "演唱会与活动", 4, listOf("票务", "待确认"), ReviewThemeIcon.Music, 0xFFEC4899),
        ReviewTheme("inspiration", "灵感速记", 9, listOf("灵感", "草稿"), ReviewThemeIcon.Sparkle, 0xFF8B5CF6),
        ReviewTheme("photo", "摄影素材", 3, listOf("风景", "构图"), ReviewThemeIcon.Camera, 0xFF14B8A6),
    )

    private val aigcMemories = listOf(
        memo("review-aigc-official", "AIGC 比赛官方公告", "保存的官方赛程、评审标准与提交要求。", listOf("比赛", "官方"), MemorySource.Link, "本周一"),
        memo("review-aigc-plan", "策划草稿 v2", "周末整理的产品方向与核心卖点。", listOf("策划", "草稿"), MemorySource.Text, "本周二", "图书馆"),
        memo("review-aigc-score", "评分表截图", "上届评审表，重点关注的指标已标注。", listOf("比赛", "评审"), MemorySource.Image, "本周三"),
        memo("review-aigc-talk", "和 Lin 的语音讨论", "讨论 demo 演示流程与时间分配。", listOf("比赛", "演示"), MemorySource.Voice, "本周三"),
        memo("review-aigc-demo", "竞品 demo 录屏", "去年获奖项目的演示视频片段。", listOf("比赛", "demo"), MemorySource.Shot, "本周四"),
        memo("review-aigc-list", "提交所需材料清单", "PPT / Demo 视频 / 团队简介，需逐项核对。", listOf("比赛", "清单"), MemorySource.Text, "本周四"),
        memo("review-aigc-team", "团队分工备忘", "周末同步分工：策划 / 视觉 / 工程负责人。", listOf("比赛", "分工"), MemorySource.Text, "本周五"),
        memo("review-aigc-judges", "评委名单与背景", "整理的评委关注方向，便于针对性介绍。", listOf("比赛", "评委"), MemorySource.Link, "本周五"),
    )

    private val themeMemories = mapOf(
        "aigc" to aigcMemories,
        "guangzhou" to listOf(
            memo("review-gz-route", "广州两日路线草图", "周末整理的核心景点动线与时间分配。", listOf("旅行", "路线"), MemorySource.Text, "上周一", "家中"),
            memo("review-gz-taotaoju", "陶陶居总店地址", "保存的老字号粤菜餐厅位置与营业时间。", listOf("美食", "餐厅"), MemorySource.Link, "上周一"),
            memo("review-gz-river", "珠江夜游票务页", "夜游班次表与购票入口。", listOf("夜游", "票务"), MemorySource.Link, "上周二"),
            memo("review-gz-photo", "永庆坊街景照片", "拍摄的西关骑楼与小巷氛围。", listOf("街景"), MemorySource.Image, "上周三", "永庆坊"),
            memo("review-gz-dessert", "朋友推荐的甜品店", "椰汁西米露与双皮奶的招牌店清单。", listOf("甜品"), MemorySource.Voice, "上周四"),
            memo("review-gz-hotel", "酒店预订截图", "确认的两晚住宿与入住时间。", listOf("住宿"), MemorySource.Shot, "上周五"),
        ),
        "course" to listOf(
            memo("review-course-structure", "数据结构重点截图", "课件中标注的核心算法章节。", listOf("学习", "课件"), MemorySource.Image, "本周一"),
            memo("review-course-linear", "线代笔记草稿", "整理的特征值与对角化思路。", listOf("笔记"), MemorySource.Text, "本周二", "图书馆"),
            memo("review-course-plan", "复习计划备忘", "考试周前每天分配的复习时段。", listOf("计划"), MemorySource.Text, "本周二"),
            memo("review-course-link", "老师讲义链接", "保存的章节练习与解答链接。", listOf("资料"), MemorySource.Link, "本周三"),
            memo("review-course-talk", "和同学的讨论语音", "讨论了几道典型例题的解法。", listOf("讨论"), MemorySource.Voice, "本周四"),
        ),
        "interview" to listOf(
            memo("review-job-list", "公司投递清单", "整理的目标公司与岗位投递进度。", listOf("面试", "清单"), MemorySource.Text, "本周一"),
            memo("review-job-algorithm", "常考算法题汇总", "保存的高频题与思路要点。", listOf("面试题"), MemorySource.Link, "本周二"),
            memo("review-job-resume", "简历最新版", "更新后的项目经历与技能描述。", listOf("简历"), MemorySource.Text, "本周二"),
            memo("review-job-outfit", "面试穿搭参考", "保存的商务休闲穿搭组合。", listOf("穿搭"), MemorySource.Image, "本周三"),
            memo("review-job-mock", "Mock 面试录音", "上周自录的自我介绍与项目讲解。", listOf("练习"), MemorySource.Voice, "本周四"),
            memo("review-job-news", "公司近况新闻", "目标公司最新业务动态与产品发布。", listOf("调研"), MemorySource.Link, "本周五"),
            memo("review-job-salary", "薪酬调研截图", "同类岗位的薪酬区间与城市差异。", listOf("薪酬"), MemorySource.Shot, "本周五"),
        ),
        "food" to listOf(
            memo("review-food-poster", "新店开业海报", "市中心新开的日料店及优惠信息。", listOf("餐厅"), MemorySource.Image, "本周一"),
            memo("review-food-dessert", "甜品清单收藏", "朋友推荐的几家网红甜品店。", listOf("甜品"), MemorySource.Link, "本周二"),
            memo("review-food-booking", "周末聚餐预订", "确认的包间与人数。", listOf("预订"), MemorySource.Text, "本周三"),
            memo("review-food-menu", "拉面店菜单照片", "拍摄的招牌拉面与小菜价格表。", listOf("菜单"), MemorySource.Image, "本周四", "天河"),
            memo("review-food-video", "探店视频片段", "保存的探店博主对该餐厅的评测。", listOf("视频"), MemorySource.Shot, "本周五"),
        ),
        "concert" to listOf(
            memo("review-event-schedule", "演唱会日程表", "近期巡演城市与具体日期。", listOf("票务"), MemorySource.Link, "本周一"),
            memo("review-event-ticket", "抢票备忘", "开票时间、平台与备选场次。", listOf("待确认"), MemorySource.Text, "本周二"),
            memo("review-event-poster", "音乐节嘉宾阵容", "海报上的演出嘉宾与时段安排。", listOf("活动"), MemorySource.Image, "本周三"),
            memo("review-event-talk", "和朋友的同行讨论", "讨论了拼车、住宿与时间安排。", listOf("同行"), MemorySource.Voice, "本周四"),
        ),
        "inspiration" to listOf(
            memo("review-idea-card", "产品 idea：灵感卡片", "随手记录的小工具想法。", listOf("灵感"), MemorySource.Text, "本周一"),
            memo("review-idea-poster", "海报排版参考", "保存的几个不错的海报版式。", listOf("设计"), MemorySource.Image, "本周一"),
            memo("review-idea-flow", "读书摘录：心流", "关于专注与心流的章节摘录。", listOf("摘录"), MemorySource.Text, "本周二"),
            memo("review-idea-script", "短视频脚本草稿", "一个生活记录类视频的开头脚本。", listOf("草稿"), MemorySource.Text, "本周二"),
            memo("review-idea-color", "配色方案截图", "保存的莫兰迪与高级灰组合。", listOf("配色"), MemorySource.Shot, "本周三"),
            memo("review-idea-talk", "对话灵感语音", "和朋友聊到的一个产品方向。", listOf("对话"), MemorySource.Voice, "本周三"),
            memo("review-idea-web", "网页设计灵感", "收藏的几个高质量品牌官网。", listOf("灵感"), MemorySource.Link, "本周四"),
            memo("review-idea-writing", "写作主题清单", "近期可以写的几个公众号选题。", listOf("选题"), MemorySource.Text, "本周五"),
            memo("review-idea-illustration", "插画风格收藏", "保存的几位插画师的作品集。", listOf("插画"), MemorySource.Image, "本周五"),
        ),
        "photo" to listOf(
            memo("review-photo-sky", "黄昏天空照片", "周末傍晚在天台拍的色温变化。", listOf("风景"), MemorySource.Image, "本周一", "天台"),
            memo("review-photo-compose", "构图参考截图", "保存的三分法与对称构图示例。", listOf("构图"), MemorySource.Shot, "本周二"),
            memo("review-photo-street", "旧城街拍底片", "胶片相机拍摄的街角小店。", listOf("街拍"), MemorySource.Image, "本周四", "西关"),
        ),
    )

    fun suggestion(id: String): ReviewSuggestion =
        (currentSuggestions + previousSuggestions).firstOrNull { it.id == id } ?: currentSuggestions.first()

    fun theme(id: String): ReviewTheme = themes.firstOrNull { it.id == id } ?: themes.first()

    fun memoriesForSuggestion(id: String): List<MemoryItem> = when (id) {
        "aigc" -> aigcMemories
        else -> memoriesForTheme(id)
    }

    fun memoriesForTheme(id: String): List<MemoryItem> = themeMemories[id].orEmpty()

    private fun memo(
        id: String,
        title: String,
        summary: String,
        tags: List<String>,
        source: MemorySource,
        time: String,
        place: String? = null,
    ) = MemoryItem(id, title, summary, tags, source, time, place)
}
