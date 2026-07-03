package com.example.idealoop

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.idealoop.feature.home.HomeCallbacks
import com.example.idealoop.navigation.IdeaLoopNavHost

@Composable
fun IdeaLoopApp(
    modifier: Modifier = Modifier,
    homeCallbacks: HomeCallbacks = HomeCallbacks(),
) {
    IdeaLoopNavHost(
        modifier = modifier.fillMaxSize(),
        homeCallbacks = homeCallbacks,
    )
}
