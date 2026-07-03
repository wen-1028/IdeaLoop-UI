package com.example.idealoop.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MicNone
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.ui.components.IdeaLoopBottomItem
import com.example.idealoop.ui.components.IdeaLoopBottomNavigation
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopIndigo500
import com.example.idealoop.ui.theme.IdeaLoopPurple500
import com.example.idealoop.ui.theme.IdeaLoopSlate400
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopWhite
import com.example.idealoop.ui.theme.IdeaLoopTheme

data class ProfileCallbacks(
    val onPersonalInfo: () -> Unit = {},
    val onMemoryPreference: () -> Unit = {},
    val onPermissions: () -> Unit = {},
    val onAccountSecurity: () -> Unit = {},
    val onFeedback: () -> Unit = {},
    val onLogout: () -> Unit = {},
    val onBottomItem: (IdeaLoopBottomItem) -> Unit = {},
    val onAdd: () -> Unit = {},
)

@Composable
fun ProfileScreen(
    callbacks: ProfileCallbacks = ProfileCallbacks(),
    modifier: Modifier = Modifier,
) {
    ProfileBackground {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = { IdeaLoopTopBar(title = "我的") },
            bottomBar = {
                IdeaLoopBottomNavigation(
                    activeItem = IdeaLoopBottomItem.Profile,
                    onItemSelected = callbacks.onBottomItem,
                    onAdd = callbacks.onAdd,
                )
            },
        ) { innerPadding ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 32.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                item {
                    ProfileHeader()
                    Spacer(Modifier.height(8.dp))
                }
                items(
                    ProfileSampleData.menuEntries.filter { it.type != ProfileMenuType.Logout },
                    key = { it.type },
                ) { entry ->
                    ProfileMenuCard(
                        entry = entry,
                        onClick = {
                            when (entry.type) {
                                ProfileMenuType.PersonalInfo -> callbacks.onPersonalInfo()
                                ProfileMenuType.MemoryPreference -> callbacks.onMemoryPreference()
                                ProfileMenuType.Permissions -> callbacks.onPermissions()
                                ProfileMenuType.AccountSecurity -> callbacks.onAccountSecurity()
                                ProfileMenuType.Feedback -> callbacks.onFeedback()
                                ProfileMenuType.Logout -> Unit
                            }
                        },
                    )
                }
                item {
                    Spacer(Modifier.height(8.dp))
                    LogoutRow(onClick = callbacks.onLogout)
                }
            }
        }
    }
}

@Composable
fun PermissionsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    initialState: PermissionState = PermissionState.enabledByDefault(),
) {
    var permissionState by remember(initialState) { mutableStateOf(initialState) }
    ProfileBackground {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = { IdeaLoopTopBar(title = "隐私与权限", onBack = onBack) },
        ) { innerPadding ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 24.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                item {
                    PrivacyIntroCard()
                    Spacer(Modifier.height(8.dp))
                }
                items(ProfileSampleData.permissions, key = { it.id }) { permission ->
                    PermissionRow(
                        permission = permission,
                        enabled = permissionState.isEnabled(permission.id),
                        onToggle = { permissionState = permissionState.toggle(permission.id) },
                    )
                }
            }
        }
    }
}

@Composable
fun LogoutConfirmationDialogContent(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ProfileSurface(
        modifier = modifier.widthIn(max = 312.dp),
        contentPadding = PaddingValues(20.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(56.dp)
                .clip(CircleShape)
                .background(IdeaLoopWhite.copy(alpha = 0.85f))
                .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), CircleShape),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = null,
                tint = IdeaLoopBlue900,
                modifier = Modifier.size(22.dp),
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = ProfileSampleData.logoutTitle,
            color = IdeaLoopBlue900,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            ProfilePillButton(
                text = ProfileSampleData.logoutActions[0],
                onClick = onCancel,
                modifier = Modifier.weight(1f),
            )
            ProfilePillButton(
                text = ProfileSampleData.logoutActions[1],
                onClick = onConfirm,
                primary = true,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ProfileHeader() {
    ProfileSurface {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileRoundIcon(Icons.Outlined.Person, size = 56.dp, iconSize = 28.dp, tint = Color(0xFF334155))
            Column(Modifier.weight(1f)) {
                Text(ProfileSampleData.displayName, color = IdeaLoopBlue900, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(ProfileSampleData.phone, color = IdeaLoopSlate500, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun ProfileMenuCard(entry: ProfileMenuEntry, onClick: () -> Unit) {
    ProfileSurface(
        onClick = onClick,
        contentPadding = PaddingValues(14.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileRoundIcon(menuIcon(entry.type), size = 36.dp, iconSize = 16.dp)
            Column(Modifier.weight(1f)) {
                Text(entry.label, color = IdeaLoopBlue900, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(2.dp))
                Text(
                    text = entry.description,
                    color = IdeaLoopSlate500,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Icon(Icons.Outlined.ChevronRight, null, tint = IdeaLoopSlate400, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun LogoutRow(onClick: () -> Unit) {
    ProfileSurface(onClick = onClick, contentPadding = PaddingValues(14.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Icon(Icons.AutoMirrored.Outlined.Logout, null, tint = IdeaLoopSlate500, modifier = Modifier.size(15.dp))
            Text("退出登录", color = IdeaLoopSlate500, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun PrivacyIntroCard() {
    ProfileSurface {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileRoundIcon(Icons.Outlined.Shield, size = 48.dp, iconSize = 20.dp)
            Column(Modifier.weight(1f)) {
                Text("隐私优先", color = IdeaLoopBlue900, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Idea Loop 仅在你授权后使用以下权限，所有数据本地优先存储。",
                    color = IdeaLoopSlate500,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                )
            }
        }
    }
}

@Composable
private fun PermissionRow(
    permission: PermissionItem,
    enabled: Boolean,
    onToggle: () -> Unit,
) {
    ProfileSurface(contentPadding = PaddingValues(14.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileRoundIcon(permissionIcon(permission.id), size = 36.dp, iconSize = 16.dp)
            Column(Modifier.weight(1f)) {
                Text(permission.label, color = IdeaLoopBlue900, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(2.dp))
                Text(permission.description, color = IdeaLoopSlate500, fontSize = 11.sp, lineHeight = 15.sp)
            }
            PermissionToggle(permission.label, enabled, onToggle)
        }
    }
}

@Composable
private fun PermissionToggle(
    label: String,
    enabled: Boolean,
    onToggle: () -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    Box(
        contentAlignment = if (enabled) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = Modifier
            .size(width = 44.dp, height = 24.dp)
            .semantics {
                contentDescription = "${label}权限 ${if (enabled) "已开启" else "已关闭"}"
            }
            .clip(CircleShape)
            .background(
                if (enabled) {
                    Brush.linearGradient(listOf(IdeaLoopIndigo500, IdeaLoopPurple500))
                } else {
                    Brush.linearGradient(listOf(Color(0xFFCBD5E1), Color(0xFFCBD5E1)))
                },
            )
            .clickable(interactionSource = interaction, indication = null, onClick = onToggle)
            .padding(2.dp),
    ) {
        Box(
            Modifier
                .size(20.dp)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(IdeaLoopWhite),
        )
    }
}

@Composable
internal fun ProfilePillButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primary: Boolean = false,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(48.dp)
            .clip(CircleShape)
            .background(
                if (primary) {
                    Brush.linearGradient(listOf(Color(0xFF818CF8), IdeaLoopIndigo500, Color(0xFFA78BFA)))
                } else {
                    Brush.verticalGradient(listOf(IdeaLoopWhite.copy(alpha = 0.96f), Color(0xFFE8F0FF).copy(alpha = 0.85f)))
                },
            )
            .border(
                1.dp,
                if (primary) IdeaLoopWhite.copy(alpha = 0.40f) else Color(0xFFBAD2FF).copy(alpha = 0.60f),
                CircleShape,
            )
            .clickable(onClick = onClick),
    ) {
        Text(
            text = text,
            color = if (primary) IdeaLoopWhite else IdeaLoopBlue900,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
internal fun ProfileSurface(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)
    val interaction = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, shape, ambientColor = IdeaLoopBlue900.copy(alpha = 0.06f), spotColor = IdeaLoopBlue900.copy(alpha = 0.06f))
            .clip(shape)
            .background(Brush.linearGradient(listOf(IdeaLoopWhite.copy(alpha = 0.92f), Color(0xFFF4F7FF).copy(alpha = 0.82f))))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(interactionSource = interaction, indication = null, onClick = onClick)
                } else {
                    Modifier
                },
            )
            .padding(contentPadding),
        content = content,
    )
}

@Composable
private fun ProfileRoundIcon(
    icon: ImageVector,
    size: androidx.compose.ui.unit.Dp,
    iconSize: androidx.compose.ui.unit.Dp,
    tint: Color = IdeaLoopBlue900,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(IdeaLoopWhite.copy(alpha = 0.85f))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), CircleShape),
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(iconSize))
    }
}

@Composable
internal fun ProfileBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to IdeaLoopBackgroundTop,
                        0.40f to IdeaLoopBackgroundMiddle,
                        1f to IdeaLoopBackgroundBottom,
                    ),
                ),
            ),
    ) { content() }
}

private fun menuIcon(type: ProfileMenuType): ImageVector = when (type) {
    ProfileMenuType.PersonalInfo -> Icons.Outlined.Person
    ProfileMenuType.MemoryPreference -> Icons.Outlined.AutoAwesome
    ProfileMenuType.Permissions -> Icons.Outlined.Shield
    ProfileMenuType.AccountSecurity -> Icons.Outlined.Lock
    ProfileMenuType.Feedback -> Icons.Outlined.ChatBubbleOutline
    ProfileMenuType.Logout -> Icons.AutoMirrored.Outlined.Logout
}

private fun permissionIcon(id: String): ImageVector = when (id) {
    "location" -> Icons.Outlined.LocationOn
    "album" -> Icons.Outlined.Image
    "camera" -> Icons.Outlined.PhotoCamera
    else -> Icons.Outlined.MicNone
}

@Preview(name = "P24 · 我的 · 360", device = "spec:width=360dp,height=800dp,dpi=440")
@Preview(name = "P24 · 我的 · 412", device = "spec:width=412dp,height=915dp,dpi=440")
@Composable
private fun ProfilePreview() {
    IdeaLoopTheme { ProfileScreen() }
}

@Preview(name = "P25 · 隐私与权限", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun PermissionsPreview() {
    IdeaLoopTheme { PermissionsScreen(onBack = {}) }
}

@Preview(name = "P28 · 退出确认", device = "spec:width=360dp,height=800dp,dpi=440")
@Composable
private fun LogoutPreview() {
    IdeaLoopTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x2E1E3A8A))
                .padding(24.dp),
        ) {
            LogoutConfirmationDialogContent({}, {})
        }
    }
}
