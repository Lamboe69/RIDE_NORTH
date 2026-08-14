package com.ridenorth.rider.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

val ForestDark = Color(0xFF042F2E)
val ForestGreen = Color(0xFF0D9488)
val LeafGreen = Color(0xFF2DD4BF)
val Emerald = Color(0xFF14B8A6)
val Mint = Color(0xFFCCFBF1)
val Gold = Color(0xFFF59E0B)
val AmberTint = Color(0xFFFEF3C7)
val Mist = Color(0xFFF0FDFA)
val Slate = Color(0xFF64748B)
val Danger = Color(0xFFDC2626)

private val LightColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = Color.White,
    primaryContainer = Mint,
    onPrimaryContainer = Color(0xFF042F2E),
    secondary = Color(0xFFB45309),
    onSecondary = Color.White,
    secondaryContainer = AmberTint,
    onSecondaryContainer = Color(0xFF451A03),
    tertiary = Emerald,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF99F6E4),
    onTertiaryContainer = Color(0xFF134E4A),
    background = Mist,
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFDFF3EF),
    onSurfaceVariant = Slate,
    outline = Color(0xFFC9DED9),
    outlineVariant = Color(0xFFE2E8F0),
    error = Danger,
    onError = Color.White,
    errorContainer = Color(0xFFFEE2E2),
    onErrorContainer = Color(0xFF450A0A),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF5EEAD4),
    onPrimary = Color(0xFF042F2E),
    primaryContainer = Color(0xFF115E59),
    onPrimaryContainer = Color(0xFFCCFBF1),
    secondary = Color(0xFFFBBF24),
    onSecondary = Color(0xFF451A03),
    secondaryContainer = Color(0xFF78350F),
    onSecondaryContainer = Color(0xFFFEF3C7),
    tertiary = Color(0xFF2DD4BF),
    onTertiary = Color(0xFF042F2E),
    tertiaryContainer = Color(0xFF115E59),
    onTertiaryContainer = Color(0xFF99F6E4),
    background = Color(0xFF0B1413),
    onBackground = Color(0xFFE0F2EF),
    surface = Color(0xFF111B19),
    onSurface = Color(0xFFE0F2EF),
    surfaceVariant = Color(0xFF18322F),
    onSurfaceVariant = Color(0xFF9CB8B4),
    outline = Color(0xFF2F514C),
    outlineVariant = Color(0xFF213C38),
    error = Color(0xFFF87171),
    onError = Color(0xFF450A0A),
    errorContainer = Color(0xFF450A0A),
    onErrorContainer = Color(0xFFFECACA),
)

private val RideNorthShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(26.dp),
)

@Composable
fun RideNorthTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(colorScheme = colorScheme, shapes = RideNorthShapes, content = content)
}
