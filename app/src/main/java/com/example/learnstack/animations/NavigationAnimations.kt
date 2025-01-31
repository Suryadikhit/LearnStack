package com.example.learnstack.animations


import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

// Define a reusable function to generate slide animations with customizable speed
fun getSlideInOutAnimation(speedFactor: Int) = Pair(
    slideInHorizontally(
        initialOffsetX = { 1000 },
        animationSpec = tween(durationMillis = speedFactor)
    ),
    slideOutHorizontally(
        targetOffsetX = { -1000 },
        animationSpec = tween(durationMillis = speedFactor)
    )
)

fun getPopSlideInOutAnimation(speedFactor: Int) = Pair(
    slideInHorizontally(
        initialOffsetX = { -1000 },
        animationSpec = tween(durationMillis = speedFactor)
    ),
    slideOutHorizontally(
        targetOffsetX = { 1000 },
        animationSpec = tween(durationMillis = speedFactor)
    )
)
