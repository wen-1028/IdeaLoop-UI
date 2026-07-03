package com.example.idealoop.feature.profile

object ProfileContract {
    const val profileRoute = "P24"
    const val permissionsRoute = "P25"
    const val logoutRoute = "P28"
    const val emptyRoute = "P29"
    const val errorRoute = "P30"
}

enum class ProfileMenuType {
    PersonalInfo,
    MemoryPreference,
    Permissions,
    AccountSecurity,
    Feedback,
    Logout,
}

data class ProfileMenuEntry(
    val type: ProfileMenuType,
    val label: String,
    val description: String,
)

data class PermissionItem(
    val id: String,
    val label: String,
    val description: String,
)

data class PermissionState(
    val values: Map<String, Boolean>,
) {
    fun isEnabled(id: String): Boolean = values[id] == true

    fun toggle(id: String): PermissionState = copy(
        values = values + (id to !isEnabled(id)),
    )

    companion object {
        fun enabledByDefault() = PermissionState(
            ProfileSampleData.permissions.associate { it.id to true },
        )
    }
}

data class ProfileStatePage(
    val topBarTitle: String,
    val title: String,
    val message: String,
    val primaryAction: String,
    val secondaryAction: String? = null,
    val backRoute: String,
    val primaryRoute: String,
)

object ProfileSampleData {
    const val displayName = "Wen"
    const val phone = "+86 138 8888 1024"
    const val logoutTitle = "确定退出登录？"
    val logoutActions = listOf("取消", "确认")

    val menuEntries = listOf(
        ProfileMenuEntry(ProfileMenuType.PersonalInfo, "个人信息", "昵称、头像、手机号"),
        ProfileMenuEntry(ProfileMenuType.MemoryPreference, "记忆偏好", "学习资料、创作灵感"),
        ProfileMenuEntry(ProfileMenuType.Permissions, "隐私与权限", "位置、相册、麦克风"),
        ProfileMenuEntry(ProfileMenuType.AccountSecurity, "账号安全", "手机号绑定与登录方式"),
        ProfileMenuEntry(ProfileMenuType.Feedback, "意见反馈", "告诉我们你的想法"),
        ProfileMenuEntry(ProfileMenuType.Logout, "退出登录", ""),
    )

    val permissions = listOf(
        PermissionItem("location", "位置", "用于在合适地点唤醒相关记忆"),
        PermissionItem("album", "相册", "用于保存图片到记忆库"),
        PermissionItem("camera", "相机", "用于拍照快速记录"),
        PermissionItem("microphone", "麦克风", "用于录制语音灵感"),
    )

    val emptyState = ProfileStatePage(
        topBarTitle = "记忆库",
        title = "还没有记忆",
        message = "从第一条记录开始建立你的记忆库",
        primaryAction = "添加第一条记录",
        backRoute = "P11",
        primaryRoute = "P06",
    )

    val errorState = ProfileStatePage(
        topBarTitle = "",
        title = "暂时无法完成操作",
        message = "请检查网络后重试",
        primaryAction = "重试",
        secondaryAction = "返回",
        backRoute = "P04",
        primaryRoute = "P04",
    )
}
