package edu.virginia.cs4720.uva_bucket_list.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import edu.virginia.cs4720.uva_bucket_list.R

// Marker-style font bundled in res/font
val MarkerFont = FontFamily(Font(R.font.permanent_marker))

// Start from the Material defaults and swap the font in every text style
private val defaults = Typography()

val Typography = Typography(
    displayLarge = defaults.displayLarge.copy(fontFamily = MarkerFont),
    displayMedium = defaults.displayMedium.copy(fontFamily = MarkerFont),
    displaySmall = defaults.displaySmall.copy(fontFamily = MarkerFont),
    headlineLarge = defaults.headlineLarge.copy(fontFamily = MarkerFont),
    headlineMedium = defaults.headlineMedium.copy(fontFamily = MarkerFont),
    headlineSmall = defaults.headlineSmall.copy(fontFamily = MarkerFont),
    titleLarge = defaults.titleLarge.copy(fontFamily = MarkerFont),
    titleMedium = defaults.titleMedium.copy(fontFamily = MarkerFont),
    titleSmall = defaults.titleSmall.copy(fontFamily = MarkerFont),
    bodyLarge = defaults.bodyLarge.copy(fontFamily = MarkerFont),
    bodyMedium = defaults.bodyMedium.copy(fontFamily = MarkerFont),
    bodySmall = defaults.bodySmall.copy(fontFamily = MarkerFont),
    labelLarge = defaults.labelLarge.copy(fontFamily = MarkerFont),
    labelMedium = defaults.labelMedium.copy(fontFamily = MarkerFont),
    labelSmall = defaults.labelSmall.copy(fontFamily = MarkerFont),
)
