package com.example.idealoop.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopElevation
import com.example.idealoop.ui.theme.IdeaLoopRadii
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopSpacing
import com.example.idealoop.ui.theme.IdeaLoopWhite

@Composable
fun IdeaLoopTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    rightContent: (@Composable RowScope.() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xEBF4F7FF),
                        Color(0xD9F7F9FF),
                        Color(0xB3F7F9FF),
                    ),
                ),
            )
            .border(BorderStroke(1.dp, IdeaLoopWhite.copy(alpha = 0.60f)))
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = IdeaLoopSpacing.xl, vertical = IdeaLoopSpacing.md),
    ) {
        if (onBack != null) {
            CircularIconButton(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "返回",
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart),
            )
        }

        Text(
            text = title,
            color = IdeaLoopBlue900,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Center),
        )

        if (rightContent != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = rightContent,
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }
    }
}

enum class IdeaLoopBottomItem(
    val label: String,
    val icon: ImageVector,
) {
    Home("首页", Icons.Outlined.Home),
    Memory("记忆", Icons.AutoMirrored.Outlined.MenuBook),
    Review("复盘", Icons.Outlined.BarChart),
    Profile("我的", Icons.Outlined.Person),
}

@Composable
fun IdeaLoopBottomNavigation(
    activeItem: IdeaLoopBottomItem,
    onItemSelected: (IdeaLoopBottomItem) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = IdeaLoopRadii.extraLarge, topEnd = IdeaLoopRadii.extraLarge))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xB3F7F9FF),
                        Color(0xD9F7F9FF),
                        Color(0xEBF4F7FF),
                    ),
                ),
            )
            .border(
                BorderStroke(1.dp, IdeaLoopWhite.copy(alpha = 0.60f)),
                RoundedCornerShape(topStart = IdeaLoopRadii.extraLarge, topEnd = IdeaLoopRadii.extraLarge),
            )
            .windowInsetsPadding(WindowInsets.navigationBars),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = IdeaLoopSpacing.xl, vertical = IdeaLoopSpacing.xs),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            BottomNavItem(IdeaLoopBottomItem.Home, activeItem, onItemSelected)
            BottomNavItem(IdeaLoopBottomItem.Memory, activeItem, onItemSelected)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(54.dp),
            ) {
                CircularIconButton(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "添加",
                    onClick = onAdd,
                    size = 42.dp,
                )
            }

            BottomNavItem(IdeaLoopBottomItem.Review, activeItem, onItemSelected)
            BottomNavItem(IdeaLoopBottomItem.Profile, activeItem, onItemSelected)
        }
    }
}

@Composable
private fun RowScope.BottomNavItem(
    item: IdeaLoopBottomItem,
    activeItem: IdeaLoopBottomItem,
    onItemSelected: (IdeaLoopBottomItem) -> Unit,
) {
    val selected = item == activeItem
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(IdeaLoopSpacing.xxs),
        modifier = Modifier
            .size(width = 48.dp, height = 52.dp)
            .clickable { onItemSelected(item) },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(width = 40.dp, height = 28.dp)
                .clip(CircleShape)
                .background(if (selected) IdeaLoopWhite.copy(alpha = 0.75f) else Color.Transparent)
                .then(
                    if (selected) Modifier.border(
                        1.dp,
                        IdeaLoopWhite.copy(alpha = 0.85f),
                        CircleShape,
                    ) else Modifier,
                ),
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = if (selected) IdeaLoopBlue900 else IdeaLoopSlate500.copy(alpha = 0.85f),
                modifier = Modifier.size(19.dp),
            )
        }
        Text(
            text = item.label,
            color = if (selected) IdeaLoopBlue900 else IdeaLoopSlate500.copy(alpha = 0.85f),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(IdeaLoopSpacing.lg),
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(IdeaLoopRadii.medium)
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .shadow(
                elevation = IdeaLoopElevation.card,
                shape = shape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.08f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.10f),
            )
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        IdeaLoopWhite.copy(alpha = 0.92f),
                        Color(0xFFF0F5FF).copy(alpha = 0.82f),
                    ),
                ),
            )
            .border(1.dp, Color(0xFFCBD5FE).copy(alpha = 0.50f), shape)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick,
                    )
                } else {
                    Modifier
                },
            )
            .padding(contentPadding),
        content = content,
    )
}

@Composable
fun IdeaLoopTag(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFFDBEAFE).copy(alpha = 0.70f),
    contentColor: Color = Color(0xFF1D4ED8),
) {
    Text(
        text = text,
        color = contentColor,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            .clip(CircleShape)
            .background(containerColor)
            .padding(horizontal = IdeaLoopSpacing.sm, vertical = 2.dp),
    )
}

@Composable
fun CircularIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 36.dp,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(size)
            .shadow(
                elevation = IdeaLoopElevation.low,
                shape = CircleShape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.08f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.08f),
            )
            .clip(CircleShape)
            .background(
                Brush.linearGradient(
                    listOf(
                        IdeaLoopWhite.copy(alpha = 0.88f),
                        Color(0xFFE6EFFF),
                    ),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), CircleShape),
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = IdeaLoopBlue900,
            modifier = Modifier.size(size * 0.50f),
        )
    }
}
