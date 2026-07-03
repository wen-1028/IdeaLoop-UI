package com.example.idealoop

import com.example.idealoop.feature.awake.AwakeContract
import com.example.idealoop.feature.awake.AwakeSampleData
import com.example.idealoop.feature.awake.AwakeTrigger
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AwakeContractTest {
    @Test
    fun awakeRoutesAndDetailSectionsMatchFigma() {
        assertEquals("P16", AwakeContract.listRoute)
        assertEquals("P17", AwakeContract.locationRoute)
        assertEquals("P18", AwakeContract.timeRoute)
        assertEquals(
            listOf("唤醒原因", "原始记忆", "相关记忆"),
            AwakeContract.detailSections,
        )
    }

    @Test
    fun awakeListIsOrderedFromNearestToFarthest() {
        assertEquals(
            listOf("market", "competition", "guangzhou", "library"),
            AwakeSampleData.items.map { it.id },
        )
        assertTrue(AwakeSampleData.items.zipWithNext().all { (a, b) -> a.recencyOrder < b.recencyOrder })
    }

    @Test
    fun cardsExposeLocationTimeAndRelationLabels() {
        assertEquals("地点唤醒", AwakeTrigger.Location.label)
        assertEquals("时间唤醒", AwakeTrigger.Time.label)
        assertEquals("关联唤醒", AwakeTrigger.Relation.label)
        assertEquals(AwakeContract.locationRoute, AwakeContract.routeFor(AwakeTrigger.Location))
        assertEquals(AwakeContract.timeRoute, AwakeContract.routeFor(AwakeTrigger.Time))
        assertEquals(AwakeContract.locationRoute, AwakeContract.routeFor(AwakeTrigger.Relation))
    }
}
