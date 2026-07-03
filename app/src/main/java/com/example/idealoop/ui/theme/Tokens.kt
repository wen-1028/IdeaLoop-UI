package com.example.idealoop.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object IdeaLoopSpacing {
    val xxs = 4.dp
    val xs = 6.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 20.dp
    val xxl = 24.dp
    val section = 32.dp
    val hero = 48.dp
}

object IdeaLoopRadii {
    val small = 12.dp
    val medium = 16.dp
    val large = 20.dp
    val extraLarge = 22.dp
    val pill = 100.dp
}

object IdeaLoopElevation {
    val low = 2.dp
    val card = 8.dp
    val floating = 12.dp
}

val IdeaLoopShapes = Shapes(
    small = RoundedCornerShape(IdeaLoopRadii.small),
    medium = RoundedCornerShape(IdeaLoopRadii.medium),
    large = RoundedCornerShape(IdeaLoopRadii.large),
    extraLarge = RoundedCornerShape(IdeaLoopRadii.extraLarge),
)
