package com.example.ui_rgr.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Green600,
    onPrimary = Gray50,
    primaryContainer = Green500,
    onPrimaryContainer = Gray900,
    secondary = Gray700,
    onSecondary = Gray50,
    secondaryContainer = Gray800,
    onSecondaryContainer = Gray200,
    background = Gray900,
    onBackground = Gray200,
    surface = Gray800,
    onSurface = Gray200,
    surfaceVariant = Gray700,
    onSurfaceVariant = Gray400,
    outline = Gray700
)

private val LightColorScheme = lightColorScheme(
    primary = Green600,
    onPrimary = Gray50,
    primaryContainer = Green500,
    onPrimaryContainer = Gray900,
    secondary = Gray700,
    onSecondary = Gray50,
    secondaryContainer = Gray50,
    onSecondaryContainer = Gray700,
    background = Gray300,
    onBackground = Gray800,
    surface = Color.White,
    onSurface = Gray800,
    surfaceVariant = Gray50,
    onSurfaceVariant = Gray600,
    outline = Gray400
)

@Composable
fun UI_RGRTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}