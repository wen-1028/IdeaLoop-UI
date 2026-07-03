package com.example.idealoop.feature.review

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopIndigo500
import com.example.idealoop.ui.theme.IdeaLoopPurple500
import com.example.idealoop.ui.theme.IdeaLoopSlate400
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopSlate600
import com.example.idealoop.ui.theme.IdeaLoopWhite

@Composable
internal fun ReviewBackground(content: @Composable BoxScope.() -> Unit) {
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
        content = content,
    )
}

@Composable
internal fun ReviewSectionCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.07f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.07f),
            )
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(
                        IdeaLoopWhite.copy(alpha = 0.95f),
                        Color(0xFFF4F7FF).copy(alpha = 0.85f),
                    ),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .padding(contentPadding),
        content = content,
    )
}

@Composable
internal fun ReviewSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    showSparkle: Boolean = false,
    onAll: (() -> Unit)? = null,
    allDescription: String = "",
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (showSparkle) {
                Icon(
                    imageVector = Icons.Outlined.AutoAwesome,
                    contentDescription = null,
                    tint = IdeaLoopIndigo500,
                    modifier = Modifier.size(13.dp),
                )
            }
            Text(
                text = title,
                color = IdeaLoopBlue900,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        if (onAll != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(1.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .semantics { contentDescription = allDescription }
                    .clickable(onClick = onAll)
                    .padding(start = 12.dp, top = 4.dp, bottom = 4.dp),
            ) {
                Text(
                    text = "全部",
                    color = IdeaLoopIndigo500,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Medium,
                )
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = IdeaLoopIndigo500,
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@Composable
internal fun ActivityTrendChart(data: List<Int>) {
    val days = listOf("一", "二", "三", "四", "五", "六", "日")
    Column(Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .height(76.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(68.dp),
            ) {
                Text("10", color = IdeaLoopSlate400, fontSize = 8.sp, modifier = Modifier.align(Alignment.TopEnd))
                Text("5", color = IdeaLoopSlate400, fontSize = 8.sp, modifier = Modifier.align(Alignment.CenterEnd))
                Text("0", color = IdeaLoopSlate400, fontSize = 8.sp, modifier = Modifier.align(Alignment.BottomEnd))
            }
            Spacer(Modifier.width(5.dp))
            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .height(68.dp),
            ) {
                val max = 10f
                val step = if (data.size > 1) size.width / (data.size - 1) else 0f
                val points = data.mapIndexed { index, value ->
                    Offset(index * step, size.height * (1f - value / max))
                }
                val dash = PathEffect.dashPathEffect(floatArrayOf(2.dp.toPx(), 3.dp.toPx()))
                listOf(0f, size.height / 2f, size.height).forEach { y ->
                    drawLine(
                        color = IdeaLoopSlate400.copy(alpha = 0.18f),
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx(),
                        pathEffect = dash,
                    )
                }
                if (points.isNotEmpty()) {
                    val line = Path().apply {
                        moveTo(points.first().x, points.first().y)
                        points.drop(1).forEach { lineTo(it.x, it.y) }
                    }
                    val area = Path().apply {
                        addPath(line)
                        lineTo(points.last().x, size.height)
                        lineTo(points.first().x, size.height)
                        close()
                    }
                    drawPath(
                        path = area,
                        brush = Brush.verticalGradient(
                            listOf(
                                Color(0xFFA5B4FC).copy(alpha = 0.35f),
                                Color(0xFFA5B4FC).copy(alpha = 0f),
                            ),
                        ),
                    )
                    drawPath(
                        path = line,
                        brush = Brush.horizontalGradient(listOf(IdeaLoopIndigo500, IdeaLoopPurple500)),
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
                    )
                    points.forEach { point ->
                        drawCircle(IdeaLoopWhite, radius = 2.5.dp.toPx(), center = point)
                        drawCircle(
                            IdeaLoopIndigo500,
                            radius = 2.5.dp.toPx(),
                            center = point,
                            style = Stroke(width = 1.5.dp.toPx()),
                        )
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp),
        ) {
            days.forEach { day ->
                Text(text = day, color = IdeaLoopSlate400, fontSize = 9.sp)
            }
        }
    }
}

@Composable
internal fun ReviewStatBlock(
    number: Int,
    unit: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = number.toString(),
                color = IdeaLoopBlue900,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(text = unit, color = IdeaLoopSlate500, fontSize = 11.sp, modifier = Modifier.padding(bottom = 4.dp))
        }
        Text(text = label, color = IdeaLoopSlate500, fontSize = 11.sp)
    }
}

@Composable
internal fun SuggestionCard(
    suggestion: ReviewSuggestion,
    onRelated: () -> Unit,
    onHandled: () -> Unit,
    onIgnored: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(18.dp)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                Brush.linearGradient(
                    listOf(IdeaLoopWhite.copy(alpha = 0.92f), Color(0xFFF4F7FF).copy(alpha = 0.82f)),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), shape)
            .padding(14.dp),
    ) {
        Text(suggestion.title, color = IdeaLoopBlue900, fontSize = 13.5.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Text(suggestion.reason, color = IdeaLoopSlate600, fontSize = 12.sp, lineHeight = 18.sp)
        Spacer(Modifier.height(6.dp))
        Text(
            text = "关联 ${suggestion.relatedCount} 条记忆",
            color = IdeaLoopIndigo500,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(Modifier.height(12.dp))
        SuggestionButton(SuggestionAction.ViewRelated.label, onRelated, primary = true)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            SuggestionButton(SuggestionAction.Handled.label, onHandled, Modifier.weight(1f))
            SuggestionButton(SuggestionAction.IgnoreThisWeek.label, onIgnored, Modifier.weight(1f))
        }
    }
}

@Composable
private fun SuggestionButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    primary: Boolean = false,
) {
    val shape = CircleShape
    val interaction = remember { MutableInteractionSource() }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    if (primary) {
                        listOf(IdeaLoopWhite.copy(alpha = 0.95f), Color(0xFFEEF2FF).copy(alpha = 0.85f))
                    } else {
                        listOf(IdeaLoopWhite.copy(alpha = 0.90f), Color(0xFFF8FAFC).copy(alpha = 0.85f))
                    },
                ),
            )
            .border(
                1.dp,
                if (primary) Color(0xFFBAD2FF).copy(alpha = 0.60f) else Color(0xFFCBD5E1).copy(alpha = 0.55f),
                shape,
            )
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
    ) {
        Text(
            text = label,
            color = if (primary) IdeaLoopBlue900 else IdeaLoopSlate500,
            fontSize = if (primary) 12.5.sp else 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
internal fun ReviewSummaryCard(
    title: String,
    subtitle: String? = null,
) {
    ReviewSectionCard {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(listOf(IdeaLoopWhite, Color(0xFFEEF2FF))))
                    .border(1.dp, IdeaLoopWhite.copy(alpha = 0.95f), CircleShape),
            ) {
                Icon(Icons.Outlined.AutoAwesome, null, tint = IdeaLoopIndigo500, modifier = Modifier.size(18.dp))
            }
            Column(Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = IdeaLoopBlue900,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (subtitle != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(subtitle, color = IdeaLoopSlate500, fontSize = 11.5.sp)
                }
            }
        }
    }
}

internal fun reviewThemeIcon(icon: ReviewThemeIcon): ImageVector = when (icon) {
    ReviewThemeIcon.Idea -> Icons.Outlined.Lightbulb
    ReviewThemeIcon.Travel -> Icons.Outlined.Explore
    ReviewThemeIcon.Study -> Icons.Outlined.School
    ReviewThemeIcon.Work -> Icons.Outlined.WorkOutline
    ReviewThemeIcon.Food -> Icons.Outlined.Restaurant
    ReviewThemeIcon.Music -> Icons.Outlined.MusicNote
    ReviewThemeIcon.Sparkle -> Icons.Outlined.AutoAwesome
    ReviewThemeIcon.Camera -> Icons.Outlined.CameraAlt
}
