package com.example.idealoop.feature.search

import com.example.idealoop.feature.memory.MemoryItem
import com.example.idealoop.feature.memory.MemorySampleData
import com.example.idealoop.feature.memory.MemorySource
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object SearchContract {
    const val inputRoute = "P13"
    const val resultsRoutePattern = "P14/{query}"
    const val xiaolingRoutePattern = "P35/{query}"
    const val queryArgument = "query"

    fun resultsRoute(query: String) = "P14/${encode(query)}"

    fun xiaolingRoute(query: String) = "P35/${encode(query)}"

    fun decodeQuery(query: String): String = URLDecoder.decode(query, StandardCharsets.UTF_8.name())

    fun isNaturalLanguage(query: String): Boolean {
        val text = query.trim()
        return listOf("帮我", "找一下", "相关的记忆", "有哪些", "什么", "？", "?")
            .any(text::contains)
    }

    private fun encode(query: String): String =
        URLEncoder.encode(query.trim(), StandardCharsets.UTF_8.name())
}

object SearchSampleData {
    val recentSearches = listOf("广州旅行", "AIGC 比赛", "毕业设计", "番茄牛腩", "陶陶居")

    private val guangzhouResults = listOf(
        MemoryItem(
            id = "guangzhou",
            title = "广州周末旅行攻略",
            summary = "周末路线、餐厅推荐、行程安排",
            tags = listOf("旅行攻略"),
            source = MemorySource.Image,
            time = "2 天前",
            place = "广州",
        ),
        MemoryItem(
            id = "taotaoju",
            title = "陶陶居点心推荐",
            summary = "广州早茶餐厅清单与点心榜单",
            tags = listOf("旅行攻略"),
            source = MemorySource.Image,
            time = "上周",
            place = "广州",
        ),
        MemoryItem(
            id = "zhujiang",
            title = "珠江夜游船票",
            summary = "夜游路线、票价信息收藏",
            tags = listOf("旅行攻略"),
            source = MemorySource.Link,
            time = "3 天前",
            place = "广州",
        ),
        MemoryItem(
            id = "shamian-route",
            title = "沙面骑行路线笔记",
            summary = "沿江骑行路线、补给点与拍照机位",
            tags = listOf("旅行攻略"),
            source = MemorySource.Text,
            time = "4 天前",
            place = "广州",
        ),
        MemoryItem(
            id = "guangzhou-tower",
            title = "广州塔灯光秀时间",
            summary = "周末灯光秀场次与观赏点建议",
            tags = listOf("旅行攻略"),
            source = MemorySource.Text,
            time = "上周",
            place = "广州",
        ),
        MemoryItem(
            id = "cantonese-notes",
            title = "粤语口语速记",
            summary = "在广州录的常用口语片段",
            tags = listOf("旅行攻略"),
            source = MemorySource.Voice,
            time = "6 天前",
            place = "广州",
        ),
        MemoryItem(
            id = "dessert-shops",
            title = "朋友推荐的糖水店",
            summary = "语音记录的几家本地糖水推荐",
            tags = listOf("旅行攻略"),
            source = MemorySource.Voice,
            time = "2 周前",
            place = "广州",
        ),
        MemoryItem(
            id = "guangzhou-metro",
            title = "广州地铁线路图",
            summary = "保存的官方线路图与换乘攻略",
            tags = listOf("旅行攻略"),
            source = MemorySource.Link,
            time = "上月",
            place = "广州",
        ),
    )

    val xiaolingRelated = listOf(
        MemoryItem(
            id = "guangzhou",
            title = "广州周末旅行攻略",
            summary = "两日游路线：沙面、永庆坊、珠江夜游，适合轻松散步和拍照。",
            tags = listOf("旅行攻略", "周末"),
            source = MemorySource.Image,
            time = "5 月 28 日",
            place = "广州",
        ),
        MemoryItem(
            id = "taotaoju",
            title = "陶陶居早茶备忘",
            summary = "推荐虾饺、凤爪和奶黄包，上午 10 点前到店排队更短。",
            tags = listOf("早茶", "美食"),
            source = MemorySource.Text,
            time = "5 月 21 日",
            place = "广州",
        ),
        MemoryItem(
            id = "zhujiang",
            title = "珠江夜游订票链接",
            summary = "大沙头码头 20:00 航班视野更好，记得提前 30 分钟取票。",
            tags = listOf("行程", "夜景"),
            source = MemorySource.Link,
            time = "4 月 16 日",
        ),
    )

    private val searchIndex = (MemorySampleData.library + guangzhouResults)
        .associateBy { it.id }
        .values
        .toList()

    fun search(query: String): List<MemoryItem> {
        val terms = searchTerms(query)
        if (terms.isEmpty()) return emptyList()
        return searchIndex.filter { memory ->
            val searchable = buildString {
                append(memory.title)
                append(' ')
                append(memory.summary)
                append(' ')
                append(memory.tags.joinToString(" "))
                append(' ')
                append(memory.place.orEmpty())
            }
            terms.any(searchable::contains)
        }
    }

    private fun searchTerms(query: String): List<String> {
        val text = query.trim()
        if (text.isEmpty()) return emptyList()
        val knownTerms = listOf(
            "广州", "旅行", "周末", "AIGC", "比赛", "毕业设计", "番茄", "牛腩",
            "陶陶居", "粤语", "珠江", "沙面", "地铁", "灯光秀", "糖水",
        ).filter(text::contains)
        return knownTerms.ifEmpty { listOf(text) }
    }
}
