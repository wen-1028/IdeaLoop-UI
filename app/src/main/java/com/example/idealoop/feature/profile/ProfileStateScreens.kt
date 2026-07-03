package com.example.idealoop.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.idealoop.R
import com.example.idealoop.ui.components.IdeaLoopTopBar
import com.example.idealoop.ui.theme.IdeaLoopBlue900
import com.example.idealoop.ui.theme.IdeaLoopIndigo400
import com.example.idealoop.ui.theme.IdeaLoopSlate500
import com.example.idealoop.ui.theme.IdeaLoopSlate800
import com.example.idealoop.ui.theme.IdeaLoopWhite
import com.example.idealoop.ui.theme.IdeaLoopTheme

@Composable
fun EmptyMemoryStateScreen(
    onBack: () -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatePageScaffold(
        topBarTitle = ProfileSampleData.emptyState.topBarTitle,
        onBack = onBack,
        modifier = modifier,
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
                .padding(bottom = 48.dp),
        ) {
            ProfileSurface(
                modifier = Modifier.widthIn(max = 320.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
            ) {
                StateLogo(modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(Modifier.size(20.dp))
                Text(
                    text = ProfileSampleData.emptyState.title,
                    color = IdeaLoopBlue900,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                Spacer(Modifier.size(6.dp))
                Text(
                    text = ProfileSampleData.emptyState.message,
                    color = IdeaLoopSlate500,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
            Spacer(Modifier.size(24.dp))
            ProfilePillButton(
                text = ProfileSampleData.emptyState.primaryAction,
                onClick = onAdd,
                primary = true,
                modifier = Modifier.widthIn(min = 190.dp),
            )
        }
    }
}

@Composable
fun ErrorStateScreen(
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    StatePageScaffold(
        topBarTitle = ProfileSampleData.errorState.topBarTitle,
        onBack = onBack,
        modifier = modifier,
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
                .padding(bottom = 48.dp),
        ) {
            StateLogo()
            Spacer(Modifier.size(24.dp))
            Text(
                text = ProfileSampleData.errorState.title,
                color = IdeaLoopSlate800,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.size(8.dp))
            Text(
                text = ProfileSampleData.errorState.message,
                color = IdeaLoopSlate500,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.size(32.dp))
            ProfilePillButton(
                text = ProfileSampleData.errorState.primaryAction,
                onClick = onRetry,
                primary = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.size(10.dp))
            ProfilePillButton(
                text = ProfileSampleData.errorState.secondaryAction.orEmpty(),
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun StatePageScaffold(
    topBarTitle: String,
    onBack: () -> Unit,
    modifier: Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    ProfileBackground {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = { IdeaLoopTopBar(title = topBarTitle, onBack = onBack) },
            content = content,
        )
    }
}

@Composable
private fun StateLogo(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(IdeaLoopWhite.copy(alpha = 0.85f))
            .border(1.dp, IdeaLoopWhite.copy(alpha = 0.90f), CircleShape),
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        listOf(IdeaLoopIndigo400.copy(alpha = 0.26f), Color.Transparent),
                    ),
                ),
        )
        Image(
            painter = painterResource(R.drawable.idea_loop_logo),
            contentDescription = "Idea Loop Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(56.dp),
        )
    }
}

@Preview(name = "P29 · 空状态 · 360", device = "spec:width=360dp,height=800dp,dpi=440")
@Preview(name = "P29 · 空状态 · 412", device = "spec:width=412dp,height=915dp,dpi=440")
@Composable
private fun EmptyStatePreview() {
    IdeaLoopTheme { EmptyMemoryStateScreen({}, {}) }
}

@Preview(name = "P30 · 错误状态 · 360", device = "spec:width=360dp,height=800dp,dpi=440")
@Preview(name = "P30 · 错误状态 · 412", device = "spec:width=412dp,height=915dp,dpi=440")
@Composable
private fun ErrorStatePreview() {
    IdeaLoopTheme { ErrorStateScreen({}, {}) }
}
