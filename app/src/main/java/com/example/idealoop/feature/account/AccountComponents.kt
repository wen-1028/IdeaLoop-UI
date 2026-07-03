package com.example.idealoop.feature.account

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.R
import com.example.idealoop.ui.theme.IdeaLoopBackgroundBottom
import com.example.idealoop.ui.theme.IdeaLoopBackgroundMiddle
import com.example.idealoop.ui.theme.IdeaLoopBackgroundTop
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopIndigo400
import com.example.idealoop.ui.theme.IdeaLoopIndigo500
import com.example.idealoop.ui.theme.IdeaLoopPurple400
import com.example.idealoop.ui.theme.IdeaLoopPurple500
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopWhite

@Composable
internal fun AccountBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
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
        Canvas(Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        IdeaLoopPurple400.copy(alpha = 0.25f),
                        IdeaLoopPurple400.copy(alpha = 0f),
                    ),
                    center = Offset(size.width * 0.20f, 0f),
                    radius = size.maxDimension * 0.50f,
                ),
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        IdeaLoopIndigo400.copy(alpha = 0.25f),
                        IdeaLoopIndigo400.copy(alpha = 0f),
                    ),
                    center = Offset(size.width * 0.90f, size.height),
                    radius = size.maxDimension * 0.50f,
                ),
            )
        }
        content()
    }
}

@Composable
internal fun AccountLogo(
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size),
    ) {
        Image(
            painter = painterResource(R.drawable.idea_loop_logo),
            contentDescription = "Idea Loop Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
internal fun AccountGradientTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.28).sp,
        style = MaterialTheme.typography.displaySmall.copy(
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to IdeaLoopBlue900,
                    0.55f to IdeaLoopIndigo500,
                    1f to IdeaLoopPurple500,
                ),
            ),
        ),
        modifier = modifier,
    )
}

@Composable
internal fun AccountBulletSubtitle(
    modifier: Modifier = Modifier,
    alignTop: Boolean = false,
    content: @Composable () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = if (alignTop) Alignment.Top else Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .padding(top = if (alignTop) 8.dp else 0.dp)
                .size(4.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        listOf(IdeaLoopIndigo500, IdeaLoopPurple500),
                    ),
                ),
        )
        content()
    }
}

@Composable
internal fun AccountBackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = CircleShape
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .shadow(
                elevation = 4.dp,
                shape = shape,
                ambientColor = IdeaLoopIndigo500.copy(alpha = 0.08f),
                spotColor = IdeaLoopIndigo500.copy(alpha = 0.08f),
            )
            .clip(shape)
            .background(IdeaLoopWhite.copy(alpha = 0.64f))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.82f), shape)
            .clickable(onClick = onClick),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = "返回",
            tint = IdeaLoopIndigo500,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
internal fun AccountGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = CircleShape
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .shadow(
                elevation = 8.dp,
                shape = shape,
                ambientColor = IdeaLoopIndigo400.copy(alpha = 0.28f),
                spotColor = IdeaLoopIndigo400.copy(alpha = 0.28f),
            )
            .clip(shape)
            .background(
                Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to IdeaLoopIndigo400,
                        0.55f to IdeaLoopIndigo500,
                        1f to IdeaLoopPurple400,
                    ),
                ),
            )
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.40f), shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(25.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(IdeaLoopWhite.copy(alpha = 0.22f), IdeaLoopWhite.copy(alpha = 0f)),
                    ),
                ),
        )
        Text(
            text = text,
            color = IdeaLoopWhite,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.32.sp,
        )
    }
}

@Composable
internal fun AccountFieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = IdeaLoopBlue900,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.48.sp,
        modifier = modifier,
    )
}

@Composable
internal fun AccountInputSurface(
    modifier: Modifier = Modifier,
    cornerRadius: androidx.compose.ui.unit.Dp = 16.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = shape,
                ambientColor = IdeaLoopBlue900.copy(alpha = 0.06f),
                spotColor = IdeaLoopBlue900.copy(alpha = 0.06f),
            )
            .clip(shape)
            .background(IdeaLoopWhite.copy(alpha = 0.76f))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.72f), shape),
        content = content,
    )
}

@Composable
internal fun AccountMutedText(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = IdeaLoopSlate500.copy(alpha = 0.95f),
        fontSize = 13.5.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.14.sp,
        modifier = modifier,
    )
}

@Composable
internal fun HorizontalDivider() {
    Spacer(
        modifier = Modifier
            .width(1.dp)
            .height(20.dp)
            .background(Color(0xFFE2E8F0)),
    )
}
