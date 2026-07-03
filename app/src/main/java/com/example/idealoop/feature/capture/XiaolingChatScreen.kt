package com.example.idealoop.feature.capture

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Image as ImageIcon
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.R
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopSlate400
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopTheme
import com.example.idealoop.ui.theme.IdeaLoopWhite

@Composable
fun XiaolingChatScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    initialMode: ChatMode = ChatMode.Chat,
    onModeChanged: (ChatMode) -> Unit = {},
    onRecordMessage: (ChatMessageContent) -> Unit = {},
    onOpenAlbum: (ChatMode) -> Unit = {},
    onVoiceMessage: (ChatMode) -> Unit = {},
) {
    var mode by rememberSaveable { mutableStateOf(initialMode) }
    var draft by rememberSaveable { mutableStateOf("") }
    var voiceInput by rememberSaveable { mutableStateOf(false) }
    var nextMessageId by rememberSaveable { mutableLongStateOf(1L) }
    val messages = remember(initialMode) {
        mutableStateListOf<ChatMessage>().apply { addAll(initialChatMessages(initialMode)) }
    }
    val listState = rememberLazyListState()
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val focusManager = LocalFocusManager.current

    fun sendDraft() {
        val content = parseLocalMessage(draft) ?: return
        draft = ""
        if (mode == ChatMode.Record) {
            focusManager.clearFocus()
            onRecordMessage(content)
        } else {
            messages += ChatMessage(
                id = nextMessageId++,
                author = ChatAuthor.User,
                content = content,
            )
        }
    }

    LaunchedEffect(messages.size, imeBottom) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Box(
        modifier = modifier
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
        ) {
            IdeaLoopTopBar(
                title = "小灵",
                onBack = onBack,
            )
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 32.dp,
                    end = 20.dp,
                    bottom = 24.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("chat_list"),
            ) {
                items(messages, key = { it.id }) { message ->
                    ChatMessageRow(message)
                }
            }
            ChatComposer(
                mode = mode,
                imeVisible = imeBottom > 0,
                voiceInput = voiceInput,
                draft = draft,
                onDraftChange = { draft = it },
                onModeChange = {
                    mode = it
                    onModeChanged(it)
                },
                onAttachment = { onOpenAlbum(mode) },
                onVoice = { voiceInput = true },
                onKeyboard = { voiceInput = false },
                onVoiceSend = {
                    voiceInput = false
                    onVoiceMessage(mode)
                },
                onSend = ::sendDraft,
            )
        }
    }
}

@Composable
internal fun ChatMessageRow(message: ChatMessage) {
    if (message.author == ChatAuthor.Assistant) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth(),
        ) {
            AssistantAvatar()
            MessageBubble(message = message, assistant = true)
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth(),
        ) {
            MessageBubble(message = message, assistant = false)
            UserAvatar()
        }
    }
}

@Composable
internal fun AssistantAvatar() {
    Image(
        painter = painterResource(R.drawable.xiaoling_avatar),
        contentDescription = "小灵 Logo",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(36.dp)
            .shadow(2.dp, CircleShape)
            .clip(CircleShape)
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), CircleShape),
    )
}

@Composable
internal fun UserAvatar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(36.dp)
            .shadow(2.dp, CircleShape)
            .clip(CircleShape)
            .background(IdeaLoopWhite.copy(alpha = 0.90f))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.95f), CircleShape),
    ) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = "Wen",
            tint = Color(0xFF334155),
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
internal fun MessageBubble(
    message: ChatMessage,
    assistant: Boolean,
) {
    val shape = RoundedCornerShape(16.dp)
    val voice = !assistant && message.content is ChatMessageContent.Voice
    Box(
        modifier = Modifier
            .widthIn(max = 252.dp)
            .shadow(
                elevation = if (assistant) 4.dp else 3.dp,
                shape = shape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.08f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.08f),
            )
            .clip(shape)
            .background(
                if (voice) {
                    Brush.linearGradient(listOf(Color(0xFF6366F1), Color(0xFF818CF8)))
                } else {
                    Brush.linearGradient(if (assistant) {
                        listOf(IdeaLoopWhite.copy(alpha = 0.95f), Color(0xFFEEF4FF).copy(alpha = 0.88f))
                    } else {
                        listOf(IdeaLoopWhite.copy(alpha = 0.92f), Color(0xFFEEF2FF).copy(alpha = 0.85f))
                    })
                },
            )
            .border(
                1.dp,
                if (voice) Color(0xFFA5B4FC) else if (assistant) IdeaLoopWhite.copy(alpha = 0.90f) else Color(0xFFE0E7FF).copy(alpha = 0.80f),
                shape,
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
    ) {
        when (val content = message.content) {
            is ChatMessageContent.Text -> Text(
                text = content.text,
                color = Color(0xFF31518F),
                fontSize = 13.sp,
                lineHeight = 20.sp,
            )
            is ChatMessageContent.Link -> LinkMessage(content.url)
            is ChatMessageContent.Voice -> VoiceMessage(content)
            is ChatMessageContent.Photos -> PhotoMessage(content.ids)
        }
    }
}

@Composable
private fun VoiceMessage(content: ChatMessageContent.Voice) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.semantics { contentDescription = "语音消息" },
    ) {
        Text(
            text = content.duration,
            color = IdeaLoopWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
            contentDescription = null,
            tint = IdeaLoopWhite,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun PhotoMessage(ids: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.semantics { contentDescription = "照片消息" },
    ) {
        ids.take(3).forEachIndexed { index, _ ->
            Box(
                modifier = Modifier
                    .size(if (ids.size == 1) 116.dp else 62.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(photoBrush(index)),
            )
        }
    }
}

internal fun photoBrush(index: Int): Brush = when (index % 5) {
    0 -> Brush.linearGradient(listOf(Color(0xFFC7DEFF), Color(0xFFE8F0FF)))
    1 -> Brush.linearGradient(listOf(Color(0xFFE0C3FC), Color(0xFF8EC5FC)))
    2 -> Brush.linearGradient(listOf(Color(0xFFFDD892), Color(0xFFD1FDFF)))
    3 -> Brush.linearGradient(listOf(Color(0xFFFBC2EB), Color(0xFFA6C1EE)))
    else -> Brush.linearGradient(listOf(Color(0xFFA8EDEA), Color(0xFFFED6E3)))
}

@Composable
private fun LinkMessage(url: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.semantics { contentDescription = "链接消息" },
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Link,
                contentDescription = null,
                tint = Color(0xFF6366F1),
                modifier = Modifier.size(14.dp),
            )
            Text(
                text = "链接",
                color = IdeaLoopBlue900,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Text(
            text = url,
            color = Color(0xFF4F46E5),
            fontSize = 11.sp,
            lineHeight = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
internal fun ChatComposer(
    mode: ChatMode,
    imeVisible: Boolean,
    voiceInput: Boolean,
    draft: String,
    onDraftChange: (String) -> Unit,
    onModeChange: (ChatMode) -> Unit,
    onAttachment: () -> Unit,
    onVoice: () -> Unit,
    onKeyboard: () -> Unit,
    onVoiceSend: () -> Unit,
    onSend: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val cancelThresholdPx = with(LocalDensity.current) { 60.dp.toPx() }
    var recording by remember { mutableStateOf(false) }
    var cancelPending by remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (recording) 0.98f else 1f,
        animationSpec = tween(140),
        label = "voice hold scale",
    )

    fun dismissKeyboard() {
        focusManager.clearFocus(force = true)
        keyboardController?.hide()
    }

    fun submitText() {
        onSend()
        dismissKeyboard()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xE0F7F9FF),
                        Color(0xEFF4F7FF),
                    ),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.85f))
            .then(if (imeVisible) Modifier else Modifier.navigationBarsPadding()),
    ) {
        if (recording) {
            VoiceCancelIndicator(
                cancelPending = cancelPending,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-50).dp),
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
        ) {
            if (voiceInput) {
                ComposerIconButton(
                    imageVector = Icons.Outlined.Keyboard,
                    contentDescription = "切换键盘",
                    onClick = onKeyboard,
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(38.dp)
                        .weight(1f)
                        .graphicsLayer {
                            scaleX = pressScale
                            scaleY = pressScale
                        }
                        .clip(CircleShape)
                        .background(
                            when {
                                cancelPending -> Color(0xFFFFE4E6).copy(alpha = 0.90f)
                                recording -> Color(0xFFDDE8FF).copy(alpha = 0.94f)
                                else -> Color(0xFFEEF2FF).copy(alpha = 0.90f)
                            },
                        )
                        .border(
                            1.dp,
                            if (cancelPending) Color(0xFFFDA4AF) else Color(0xFFBAD2FF).copy(alpha = 0.55f),
                            CircleShape,
                        )
                        .semantics {
                            contentDescription = "按住说话，上滑取消"
                            onClick {
                                onVoiceSend()
                                true
                            }
                        }
                        .pointerInput(onVoiceSend, cancelThresholdPx) {
                            awaitEachGesture {
                                val down = awaitFirstDown(requireUnconsumed = false)
                                recording = true
                                cancelPending = false
                                var pressed = true
                                while (pressed) {
                                    val change = awaitPointerEvent().changes
                                        .firstOrNull { it.id == down.id }
                                        ?: break
                                    cancelPending = down.position.y - change.position.y > cancelThresholdPx
                                    pressed = change.pressed
                                    change.consume()
                                }
                                val cancelled = cancelPending
                                recording = false
                                cancelPending = false
                                if (!cancelled) onVoiceSend()
                            }
                        },
                ) {
                    if (recording) {
                        VoiceWaveform(cancelPending = cancelPending)
                    } else {
                        Text(
                            text = "按住说话",
                            color = Color(0xFF4969D8).copy(alpha = 0.72f),
                            fontSize = 15.sp,
                        )
                    }
                }
                CurrentModeBadge(mode)
            } else {
                ComposerIconButton(
                    imageVector = Icons.Outlined.ImageIcon,
                    contentDescription = "附件",
                    onClick = {
                        dismissKeyboard()
                        onAttachment()
                    },
                )
                ComposerIconButton(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = "语音",
                    onClick = {
                        dismissKeyboard()
                        onVoice()
                    },
                )
                BasicTextField(
                    value = draft,
                    onValueChange = onDraftChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color(0xFF334155),
                        fontSize = 13.sp,
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Send,
                    ),
                    keyboardActions = KeyboardActions(onSend = { submitText() }),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (draft.isEmpty()) {
                                Text(
                                    text = mode.placeholder,
                                    color = IdeaLoopSlate400.copy(alpha = 0.80f),
                                    fontSize = 13.sp,
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input"),
                )
                ModeSelector(
                    mode = mode,
                    onModeChange = onModeChange,
                )
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    IdeaLoopBlue900.copy(alpha = if (draft.isBlank()) 0.55f else 1f),
                                    Color(0xFF3B82F6).copy(alpha = if (draft.isBlank()) 0.55f else 1f),
                                ),
                            ),
                        )
                        .clickable(enabled = draft.isNotBlank(), onClick = ::submitText),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Send,
                        contentDescription = "发送",
                        tint = IdeaLoopWhite,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun VoiceCancelIndicator(
    cancelPending: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(44.dp)
            .shadow(8.dp, CircleShape)
            .clip(CircleShape)
            .background(if (cancelPending) Color(0xFFE11D48) else IdeaLoopWhite.copy(alpha = 0.96f))
            .border(
                1.dp,
                if (cancelPending) Color(0xFFE11D48) else Color(0xFFBAD2FF).copy(alpha = 0.70f),
                CircleShape,
            ),
    ) {
        Icon(
            imageVector = Icons.Outlined.Close,
            contentDescription = if (cancelPending) "松开取消" else "上滑取消",
            tint = if (cancelPending) IdeaLoopWhite else IdeaLoopSlate400,
            modifier = Modifier.size(19.dp),
        )
    }
}

@Composable
private fun VoiceWaveform(cancelPending: Boolean) {
    val transition = rememberInfiniteTransition(label = "voice waveform")
    val pulse by transition.animateFloat(
        initialValue = 0.50f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(420),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "voice waveform pulse",
    )
    val heights = listOf(10, 16, 22, 14, 20, 12, 18)
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(24.dp),
    ) {
        heights.forEachIndexed { index, height ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height((height * (if (index % 2 == 0) pulse else 1.5f - pulse / 2f)).dp)
                    .clip(CircleShape)
                    .background(if (cancelPending) Color(0xFFE11D48) else Color(0xFF6366F1)),
            )
        }
    }
}

@Composable
private fun CurrentModeBadge(mode: ChatMode) {
    Text(
        text = mode.label,
        color = if (mode == ChatMode.Chat) IdeaLoopBlue900 else Color(0xFF6D28D9),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .height(28.dp)
            .clip(CircleShape)
            .background(IdeaLoopWhite.copy(alpha = 0.78f))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.85f), CircleShape)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    )
}

@Composable
private fun ComposerIconButton(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(IdeaLoopWhite.copy(alpha = 0.75f))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.85f), CircleShape)
            .clickable(onClick = onClick),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = IdeaLoopBlue900,
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun ModeSelector(
    mode: ChatMode,
    onModeChange: (ChatMode) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(28.dp)
                .clip(CircleShape)
                .background(
                    if (mode == ChatMode.Chat) {
                        Color(0xFFEFF6FF).copy(alpha = 0.95f)
                    } else {
                        Color(0xFFF5F3FF).copy(alpha = 0.95f)
                    },
                )
                .border(
                    1.dp,
                    if (mode == ChatMode.Chat) Color(0xFFBFDBFE) else Color(0xFFDDD6FE),
                    CircleShape,
                )
                .semantics { contentDescription = "切换模式" }
                .clickable { expanded = true }
                .padding(horizontal = 10.dp),
        ) {
            Text(
                text = mode.label,
                color = if (mode == ChatMode.Chat) IdeaLoopBlue900 else Color(0xFF6D28D9),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Icon(
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = null,
                tint = IdeaLoopSlate500,
                modifier = Modifier.size(11.dp),
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color(0xFFF8FAFF),
            shape = RoundedCornerShape(14.dp),
        ) {
            ChatMode.entries.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.label,
                            color = IdeaLoopBlue900,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    },
                    onClick = {
                        onModeChange(option)
                        expanded = false
                    },
                    leadingIcon = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.width(16.dp),
                        ) {
                            if (option == mode) {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = null,
                                    tint = Color(0xFF6366F1),
                                    modifier = Modifier.size(12.dp),
                                )
                            }
                        }
                    },
                )
            }
        }
    }
}

@Preview(
    name = "P06 · 小灵 Chat · 360",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Preview(
    name = "P06 · 小灵 Chat · 412",
    device = "spec:width=412dp,height=915dp,dpi=440",
    showBackground = true,
)
@Composable
private fun XiaolingChatPreview() {
    IdeaLoopTheme {
        XiaolingChatScreen(onBack = {}, initialMode = ChatMode.Chat)
    }
}

@Preview(
    name = "P06 · 小灵 Record · 360",
    device = "spec:width=360dp,height=800dp,dpi=440",
    showBackground = true,
)
@Composable
private fun XiaolingRecordPreview() {
    IdeaLoopTheme {
        XiaolingChatScreen(onBack = {}, initialMode = ChatMode.Record)
    }
}
