package com.example.idealoop.feature.account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
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
import com.example.idealoop.ui.theme.IdeaLoopIndigo400
import com.example.idealoop.ui.theme.IdeaLoopIndigo500
import com.example.idealoop.ui.theme.IdeaLoopSlate400
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite
import kotlinx.coroutines.delay

@Composable
fun VerificationScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var code by rememberSaveable { mutableStateOf("") }
    var countdown by rememberSaveable { mutableIntStateOf(48) }
    var isFocused by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1_000)
            countdown -= 1
        }
    }

    LaunchedEffect(code) {
        if (code.length == 6) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }
    }

    AccountBackground(modifier) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = maxHeight)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 24.dp),
            ) {
                AccountBackButton(
                    onClick = onBack,
                    modifier = Modifier.padding(start = 0.dp),
                )

                Spacer(Modifier.height(24.dp))

                AccountGradientTitle(text = "输入验证码")

                Spacer(Modifier.height(10.dp))

                AccountBulletSubtitle {
                    Text(
                        text = buildAnnotatedString {
                            append("已发送至 ")
                            withStyle(
                                SpanStyle(
                                    color = Color(0xFF1E3A8A),
                                    fontWeight = FontWeight.SemiBold,
                                ),
                            ) {
                                append("+86 138 8888 1024")
                            }
                        },
                        color = Color(0xFF64748B).copy(alpha = 0.95f),
                        fontSize = 13.5.sp,
                        lineHeight = 21.sp,
                        letterSpacing = 0.14.sp,
                    )
                }

                Spacer(Modifier.height(40.dp))

                VerificationCodeField(
                    value = code,
                    onValueChange = { code = AccountInputRules.verificationCode(it) },
                    focused = isFocused,
                    onFocusChanged = { isFocused = it },
                )

                Spacer(Modifier.height(32.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = IdeaLoopIndigo500,
                                fontWeight = FontWeight.Bold,
                            ),
                        ) {
                            append("${countdown}s")
                        }
                        withStyle(SpanStyle(color = IdeaLoopSlate400.copy(alpha = 0.95f))) {
                            append(" 后可重新发送")
                        }
                    },
                    textAlign = TextAlign.Center,
                    fontSize = 13.sp,
                    lineHeight = 20.sp,
                    letterSpacing = 0.26.sp,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(40.dp))

                AccountGradientButton(
                    text = "登录 / 注册",
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onContinue()
                    },
                )

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun VerificationCodeField(
    value: String,
    onValueChange: (String) -> Unit,
    focused: Boolean,
    onFocusChanged: (Boolean) -> Unit,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = TextStyle(color = Color.Transparent),
        cursorBrush = SolidColor(Color.Transparent),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions.Default,
        decorationBox = { innerTextField ->
            BoxWithConstraints(Modifier.fillMaxWidth()) {
                val gap = 10.dp
                val availableCellWidth = (maxWidth - gap * 5) / 6
                val cellWidth = minOf(46.dp, availableCellWidth)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(gap),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    repeat(6) { index ->
                        VerificationCell(
                            digit = value.getOrNull(index)?.toString().orEmpty(),
                            active = focused && index == value.length.coerceAtMost(5),
                            width = cellWidth,
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(1.dp)
                        .alpha(0f),
                ) {
                    innerTextField()
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { onFocusChanged(it.isFocused) }
            .testTag("verification_input"),
    )
}

@Composable
private fun VerificationCell(
    digit: String,
    active: Boolean,
    width: androidx.compose.ui.unit.Dp,
) {
    val filled = digit.isNotEmpty()
    val shape = RoundedCornerShape(16.dp)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = width, height = 56.dp)
            .shadow(
                elevation = if (filled || active) 8.dp else 4.dp,
                shape = shape,
                ambientColor = IdeaLoopIndigo500.copy(alpha = if (filled || active) 0.15f else 0.06f),
                spotColor = IdeaLoopIndigo500.copy(alpha = if (filled || active) 0.15f else 0.06f),
            )
            .clip(shape)
            .background(IdeaLoopWhite.copy(alpha = if (filled || active) 1f else 0.60f))
            .border(
                width = if (filled || active) 2.dp else 1.dp,
                color = if (filled || active) {
                    IdeaLoopIndigo400
                } else {
                    IdeaLoopWhite.copy(alpha = 0.80f)
                },
                shape = shape,
            ),
    ) {
        Text(
            text = digit,
            color = Color(0xFF312E81),
            fontSize = 24.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Preview(
    name = "P02 · 验证码",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Composable
private fun VerificationScreenPreview() {
    IdeaLoopTheme {
        VerificationScreen(onBack = {}, onContinue = {})
    }
}
