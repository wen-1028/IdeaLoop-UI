package com.example.idealoop

import com.example.idealoop.feature.memory.MemoryContract
import com.example.idealoop.feature.memory.MemoryFilter
import com.example.idealoop.feature.memory.MemorySampleData
import org.junit.Assert.assertEquals
import org.junit.Test

class MemoryContractTest {
    @Test
    fun routesMatchP11AndP12() {
        assertEquals("P11", MemoryContract.libraryRoute)
        assertEquals("P12/guangzhou", MemoryContract.detailRoute("guangzhou"))
    }

    @Test
    fun filtersMatchFigmaOrderAndSource() {
        assertEquals(
            listOf("全部", "图片", "文字", "语音", "链接"),
            MemoryFilter.entries.map { it.label },
        )
        assertEquals(
            listOf("广州周末旅行攻略"),
            MemorySampleData.filtered(MemoryFilter.Image).map { it.title },
        )
    }

    @Test
    fun detailSectionsStayInFigmaOrder() {
        assertEquals(
            listOf("标题", "摘要", "内容", "标签", "关联记忆"),
            MemoryContract.detailSections,
        )
    }
}
