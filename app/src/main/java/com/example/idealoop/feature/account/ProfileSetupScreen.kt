package com.example.idealoop.feature.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopIndigo400
import com.example.idealoop.ui.theme.IdeaLoopIndigo500
import com.example.idealoop.ui.theme.IdeaLoopSlate400
import com.example.idealoop.ui.theme.IdeaLoopSlate600
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite

@Composable
fun ProfileSetupScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var nickname by rememberSaveable { mutableStateOf(AccountSampleData.nickname) }
    var selected by remember { mutableStateOf(AccountSampleData.selectedPreferences) }
    val focusManager = LocalFocusManager.current
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    AccountBackground(modifier) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding(),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = maxHeight)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 24.dp),
            ) {
                AccountBackButton(onClick = onBack)

                Spacer(Modifier.height(16.dp))

                AccountGradientTitle(text = "让 Idea Loop 更懂你")

                Spacer(Modifier.height(10.dp))

                AccountBulletSubtitle(alignTop = true) {
                    AccountMutedText(
                        text = "选择你最常记录的内容类型，Idea Loop 会为你优化标签、提醒、复盘和行动建议。",
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }

                Spacer(Modifier.height(32.dp))

                AccountFieldLabel(text = "昵称")
                Spacer(Modifier.height(10.dp))

                AccountInputSurface(
                    cornerRadius = 20.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    BasicTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color(0xFF1E1B4B),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() },
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                contentAlignment = Alignment.CenterStart,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 20.dp),
                            ) {
                                if (nickname.isEmpty()) {
                                    Text(
                                        text = "请输入你的昵称",
                                        color = IdeaLoopSlate400,
                                        fontSize = 16.sp,
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                Spacer(Modifier.height(32.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    AccountFieldLabel(text = "记忆偏好")
                    Text(
                        text = "可多选",
                        color = IdeaLoopSlate400.copy(alpha = 0.95f),
                        fontSize = 11.sp,
                        lineHeight = 17.sp,
                    )
                }

                Spacer(Modifier.height(12.dp))

                PreferenceGrid(
                    selected = selected,
                    onToggle = { preference ->
                        selected = if (preference in selected) {
                            selected - preference
                        } else {
                            selected + preference
                        }
                    },
                )

                if (!imeVisible) {
                    Spacer(Modifier.height(40.dp))

                    AccountGradientButton(
                        text = "开启旅途",
                        onClick = {
                            focusManager.clearFocus()
                            onContinue()
                        },
                    )
                } else {
                    Spacer(Modifier.height(88.dp))
                }
            }

            if (imeVisible) {
                AccountGradientButton(
                    text = "开启旅途",
                    onClick = {
                        focusManager.clearFocus()
                        onContinue()
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun PreferenceGrid(
    selected: Set<MemoryPreference>,
    onToggle: (MemoryPreference) -> Unit,
) {
    val preferences = MemoryPreference.entries
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        preferences.chunked(2).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                rowItems.forEach { preference ->
                    PreferenceCard(
                        preference = preference,
                        selected = preference in selected,
                        onClick = { onToggle(preference) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun PreferenceCard(
    preference: MemoryPreference,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(16.dp)
    val borderColor = if (selected) IdeaLoopIndigo400 else IdeaLoopWhite.copy(alpha = 0.80f)
    Box(
        modifier = modifier
            .height(52.dp)
            .shadow(
                elevation = if (selected) 4.dp else 2.dp,
                shape = shape,
                ambientColor = IdeaLoopIndigo500.copy(alpha = if (selected) 0.10f else 0.04f),
                spotColor = IdeaLoopIndigo500.copy(alpha = if (selected) 0.10f else 0.04f),
            )
            .clip(shape)
            .background(
                if (selected) {
                    Brush.linearGradient(
                        listOf(
                            Color(0xFFEEF2FF).copy(alpha = 0.80f),
                            Color(0xFFFAF5FF).copy(alpha = 0.80f),
                        ),
                    )
                } else {
                    Brush.linearGradient(
                        listOf(
                            IdeaLoopWhite.copy(alpha = 0.60f),
                            IdeaLoopWhite.copy(alpha = 0.60f),
                        ),
                    )
                },
            )
            .border(if (selected) 1.5.dp else 1.dp, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize(),
        ) {
            Icon(
                imageVector = preferenceIcon(preference),
                contentDescription = null,
                tint = if (selected) IdeaLoopIndigo500 else IdeaLoopSlate400,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = preference.label,
                color = if (selected) Color(0xFF4338CA) else IdeaLoopSlate600,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                modifier = Modifier.weight(1f),
            )
        }
        if (selected) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(IdeaLoopIndigo500),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "已选择",
                    tint = IdeaLoopWhite,
                    modifier = Modifier.size(10.dp),
                )
            }
        }
    }
}

private fun preferenceIcon(preference: MemoryPreference): ImageVector = when (preference) {
    MemoryPreference.Learning -> Icons.Outlined.Book
    MemoryPreference.Inspiration -> Icons.Outlined.AutoAwesome
    MemoryPreference.Work -> Icons.Outlined.WorkOutline
    MemoryPreference.Travel -> Icons.Outlined.LocationOn
    MemoryPreference.Tasks -> Icons.Outlined.Checklist
    MemoryPreference.Reading -> Icons.Outlined.BookmarkBorder
}

@Preview(
    name = "P03 · 注册补充信息",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Composable
private fun ProfileSetupScreenPreview() {
    IdeaLoopTheme {
        ProfileSetupScreen(onBack = {}, onContinue = {})
    }
}
