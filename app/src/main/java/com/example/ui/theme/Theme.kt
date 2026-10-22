package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme =
  lightColorScheme(
    primary = ScholarTealPrimary,
    onPrimary = ScholarTealOnPrimary,
    primaryContainer = Color(0xFFD2F4F2),
    onPrimaryContainer = Color(0xFF00201F),
    secondary = SlateSecondary,
    onSecondary = SlateOnSecondary,
    secondaryContainer = Color(0xFFE2EFEF),
    onSecondaryContainer = Color(0xFF0D2120),
    tertiary = AmberTertiary,
    onTertiary = AmberOnTertiary,
    tertiaryContainer = Color(0xFFFFECC8),
    onTertiaryContainer = Color(0xFF2B1700),
    background = Color(0xFFF6F9F8),
    onBackground = Color(0xFF161C1C),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF161C1C),
    surfaceVariant = Color(0xFFE9F0EE),
    onSurfaceVariant = Color(0xFF384544),
    outline = Color(0xFFB0BEBC),
  )

private val DarkColorScheme =
  darkColorScheme(
    primary = DarkTealPrimary,
    onPrimary = DarkTealOnPrimary,
    primaryContainer = DarkTealContainer,
    onPrimaryContainer = DarkTealOnContainer,
    secondary = DarkSlateSecondary,
    onSecondary = DarkSlateOnSecondary,
    secondaryContainer = DarkSlateSecondaryContainer,
    onSecondaryContainer = DarkSlateOnSecondaryContainer,
    tertiary = DarkAmberTertiary,
    onTertiary = DarkAmberOnTertiary,
    tertiaryContainer = DarkAmberTertiaryContainer,
    onTertiaryContainer = DarkAmberOnTertiaryContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Enforce clean, bright Light Mode as requested
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
