package com.luisdev.marknotes.core.ui.theme

import org.jetbrains.compose.resources.Font
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import ant_simulator.composeapp.generated.resources.Inter_Regular
import ant_simulator.composeapp.generated.resources.JetBrainsMono_Regular
import ant_simulator.composeapp.generated.resources.Outfit_Regular
import ant_simulator.composeapp.generated.resources.Res
import com.luisdev.antsimulator.core.utils.FontSizeOption

@Composable
fun getTypography(fontSizeOption: FontSizeOption): Typography {
    val displayFontFamily = FontFamily(
        Font(Res.font.Outfit_Regular)
    )

    val bodyFontFamily = FontFamily(
        Font(Res.font.Inter_Regular)
    )

    val baseline = Typography()
    val scale = fontSizeOption.scale

    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily, fontSize = baseline.displayLarge.fontSize * scale),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily, fontSize = baseline.displayMedium.fontSize * scale),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily, fontSize = baseline.displaySmall.fontSize * scale),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily, fontSize = baseline.headlineLarge.fontSize * scale),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily, fontSize = baseline.headlineMedium.fontSize * scale),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily, fontSize = baseline.headlineSmall.fontSize * scale),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily, fontSize = baseline.titleLarge.fontSize * scale),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily, fontSize = baseline.titleMedium.fontSize * scale),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily, fontSize = baseline.titleSmall.fontSize * scale),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily, fontSize = baseline.bodyLarge.fontSize * scale),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily, fontSize = baseline.bodyMedium.fontSize * scale),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily, fontSize = baseline.bodySmall.fontSize * scale),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily, fontSize = baseline.labelLarge.fontSize * scale),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily, fontSize = baseline.labelMedium.fontSize * scale),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily, fontSize = baseline.labelSmall.fontSize * scale),
    )
}
