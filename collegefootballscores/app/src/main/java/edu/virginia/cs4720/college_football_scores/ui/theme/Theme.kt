package edu.virginia.cs4720.college_football_scores.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// primaryContainer/errorContainer are used for the winner/loser highlight on finished games
private val DarkColorScheme = darkColorScheme(
    primary = GoldLight,
    onPrimary = Ink,
    background = TurfDark,
    onBackground = TextLight,
    surface = CardDark,
    onSurface = TextLight,
    primaryContainer = WinGreenDark,
    onPrimaryContainer = OnWinGreenDark,
    errorContainer = LossRedDark,
    onErrorContainer = OnLossRedDark
)

private val LightColorScheme = lightColorScheme(
    primary = Gold,
    onPrimary = Ink,
    background = Turf,
    onBackground = Color.White,
    surface = CardWhite,
    onSurface = Ink,
    primaryContainer = WinGreenLight,
    onPrimaryContainer = OnWinGreenLight,
    errorContainer = LossRedLight,
    onErrorContainer = OnLossRedLight
)

@Composable
fun CollegefootballscoresTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}