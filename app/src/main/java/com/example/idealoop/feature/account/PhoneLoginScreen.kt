package com.example.idealoop.feature.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import com.example.idealoop.ui.theme.IdeaLoopIndigo500
import com.example.idealoop.ui.theme.IdeaLoopSlate400
import com.example.idealoop.ui.theme.IdeaLoopSlate700
import com.example.idealoop.ui.theme.IdeaLoopTheme

@Composable
fun PhoneLoginScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var phone by rememberSaveable { mutableStateOf(AccountSampleData.phone) }
    val focusManager = LocalFocusManager.current

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
                AccountLogo(size = 64.dp)

                Spacer(Modifier.height(24.dp))

                AccountGradientTitle(text = "欢迎使用 Idea Loop")

                Spacer(Modifier.height(10.dp))

                AccountBulletSubtitle {
                    AccountMutedText(text = "输入手机号开始记录你的灵感")
                }

                Spacer(Modifier.height(32.dp))

                AccountFieldLabel(text = "手机号")
                Spacer(Modifier.height(8.dp))

                AccountInputSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                    ) {
                        Text(
                            text = "+86",
                            color = IdeaLoopSlate700,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                        HorizontalDivider()
                        BasicTextField(
                            value = phone,
                            onValueChange = { phone = AccountInputRules.formatPhone(it) },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color(0xFF1E293B),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.45.sp,
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Phone,
                                imeAction = ImeAction.Done,
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { focusManager.clearFocus() },
                            ),
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                AccountGradientButton(
                    text = "获取验证码",
                    onClick = {
                        focusManager.clearFocus()
                        onContinue()
                    },
                )

                Spacer(Modifier.height(40.dp))

                PrivacyAgreement()
            }
        }
    }
}

@Composable
private fun PrivacyAgreement() {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = IdeaLoopSlate400.copy(alpha = 0.95f))) {
                append("登录即代表同意 ")
            }
            withStyle(SpanStyle(color = IdeaLoopIndigo500, fontWeight = FontWeight.SemiBold)) {
                append("《用户协议》")
            }
            withStyle(SpanStyle(color = IdeaLoopSlate400.copy(alpha = 0.95f))) {
                append(" 与 ")
            }
            withStyle(SpanStyle(color = IdeaLoopIndigo500, fontWeight = FontWeight.SemiBold)) {
                append("《隐私政策》")
            }
        },
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.24.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
}

@Preview(
    name = "P01 · 手机号登录",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Composable
private fun PhoneLoginScreenPreview() {
    IdeaLoopTheme {
        PhoneLoginScreen(onContinue = {})
    }
}
