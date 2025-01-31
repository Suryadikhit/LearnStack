package com.example.learnstack.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Light Theme Custom Colors
val LightPink = Color(0xFFFAE6E7)
val LightGrayGreen = Color(0xFFF5F7F6)
val LightGridCardColor = Color(0xFFFFFFFF) // New color for grid cards in light mode

// Dark Mode Background Color
val DarkBackgroundColor = Color(0xFF303030)
val DarkCardColor = Color(0xFF424242)

// Light Theme Colors
val AppLightTheme = lightColorScheme(
    primary = Color(0xFF4E4A5D),
    onPrimary = Color.White,
    background = LightGrayGreen,
    onBackground = Color.Black,
    surface = Color.White, // Default for cards/surfaces
    onSurface = Color.Black,
    primaryContainer = LightPink, // Use this for ActionCard background
    onPrimaryContainer = Color.Black,
    surfaceVariant = LightGridCardColor, // Add grid card color for light theme
    // Custom color for the first word
    tertiary = Color.Black // Use `tertiary` for first word in light mode
)

// Dark Theme Colors
val AppDarkTheme = darkColorScheme(
    primary = Color(0xFFBC98F1),
    onPrimary = Color.Black,
    background = DarkBackgroundColor,
    onBackground = Color.White,
    surface = DarkCardColor, // Default for cards/surfaces
    onSurface = Color.White,
    primaryContainer = Color(0xFF473334), // Use this for ActionCard background
    onPrimaryContainer = Color.White,
    surfaceVariant = DarkBackgroundColor, // Add grid card color for dark theme
    // Custom color for the first word
    tertiary = Color.White // Use `tertiary` for first word in dark mode
)
