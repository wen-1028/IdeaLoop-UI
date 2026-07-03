package com.example.idealoop

import com.example.idealoop.feature.profile.PermissionState
import com.example.idealoop.feature.profile.ProfileContract
import com.example.idealoop.feature.profile.ProfileSampleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileContractTest {
    @Test
    fun profileRoutesMatchFigmaPages() {
        assertEquals("P24", ProfileContract.profileRoute)
        assertEquals("P25", ProfileContract.permissionsRoute)
        assertEquals("P28", ProfileContract.logoutRoute)
        assertEquals("P29", ProfileContract.emptyRoute)
        assertEquals("P30", ProfileContract.errorRoute)
    }

    @Test
    fun profileContainsOnlyRequestedEntries() {
        assertEquals(
            listOf("个人信息", "记忆偏好", "隐私与权限", "账号安全", "意见反馈", "退出登录"),
            ProfileSampleData.menuEntries.map { it.label },
        )
    }

    @Test
    fun permissionsUseFigmaExplanationsAndToggleLocally() {
        assertEquals(
            listOf("位置", "相册", "相机", "麦克风"),
            ProfileSampleData.permissions.map { it.label },
        )
        assertEquals(
            listOf(
                "用于在合适地点唤醒相关记忆",
                "用于保存图片到记忆库",
                "用于拍照快速记录",
                "用于录制语音灵感",
            ),
            ProfileSampleData.permissions.map { it.description },
        )
        val initial = PermissionState.enabledByDefault()
        assertTrue(initial.isEnabled("location"))
        val toggled = initial.toggle("location")
        assertFalse(toggled.isEnabled("location"))
        assertTrue(toggled.isEnabled("album"))
    }

    @Test
    fun statePagesHaveConciseCopyAndExplicitActions() {
        assertEquals("还没有记忆", ProfileSampleData.emptyState.title)
        assertEquals("从第一条记录开始建立你的记忆库", ProfileSampleData.emptyState.message)
        assertEquals("添加第一条记录", ProfileSampleData.emptyState.primaryAction)
        assertEquals("P11", ProfileSampleData.emptyState.backRoute)

        assertEquals("暂时无法完成操作", ProfileSampleData.errorState.title)
        assertEquals("请检查网络后重试", ProfileSampleData.errorState.message)
        assertEquals("重试", ProfileSampleData.errorState.primaryAction)
        assertEquals("返回", ProfileSampleData.errorState.secondaryAction)
        assertEquals("P04", ProfileSampleData.errorState.backRoute)
    }

    @Test
    fun logoutDialogContainsOnlyConfirmationActions() {
        assertEquals("确定退出登录？", ProfileSampleData.logoutTitle)
        assertEquals(listOf("取消", "确认"), ProfileSampleData.logoutActions)
    }
}
