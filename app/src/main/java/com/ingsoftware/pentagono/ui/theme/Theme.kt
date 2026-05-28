package com.ingsoftware.pentagono.ui.theme

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
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        letterSpacing = (-0.3).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        letterSpacing = (-0.2).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp
    )
)

// ── Paleta Verde Pentagono ────────────────────────────────────────────────────
val VerdeVivo           = Color(0xFF3E7C2A)
val VerdeHover          = Color(0xFF6CB05A)
val VerdeOscuro         = Color(0xFF1F5C18)
val VerdeFondo          = Color(0xFFEEF7E8)
val VerdeGradStart      = Color(0xFF7DC460)
val VerdeGradEnd        = Color(0xFF1F5C18)
val VerdeWelcome        = Color(0xFF2A5E1A)

val TealPrincipal       = Color(0xFF1D9E75)
val TealClaro           = Color(0xFF5DCAA5)
val TealFondo           = Color(0xFFE1F5EE)

val GrisOscuro          = Color(0xFF2C2C2A)
val GrisMedio           = Color(0xFF5F5E5A)
val GrisClaro           = Color(0xFFD3D1C7)
val GrisFondo           = Color(0xFFF2F7EE)

val Rojo                = Color(0xFFB03030)
val RojoClaro           = Color(0xFFFCEBEB)

val Negro               = Color(0xFF000000)
val Blanco              = Color(0xFFFFFFFF)

private val DarkColorScheme = darkColorScheme(
    primary              = VerdeHover,
    onPrimary            = Color(0xFF173404),
    primaryContainer     = VerdeOscuro,
    onPrimaryContainer   = VerdeFondo,
    secondary            = TealClaro,
    onSecondary          = Color(0xFF04342C),
    secondaryContainer   = Color(0xFF085041),
    onSecondaryContainer = TealFondo,
    background           = Color(0xFF0F1A08),
    onBackground         = Color(0xFFE0EDD0),
    surface              = Color(0xFF1A2A10),
    onSurface            = Color(0xFFD0E8BA),
    surfaceVariant       = Color(0xFF253A18),
    onSurfaceVariant     = Color(0xFFAAC98E),
    error                = Color(0xFFF09595),
    onError              = Color(0xFF501313),
    outline              = Color(0xFF4A6A28),
    outlineVariant       = Color(0xFF2E4218)
)

private val LightColorScheme = lightColorScheme(
    primary              = VerdeVivo,
    onPrimary            = Blanco,
    primaryContainer     = VerdeOscuro,
    onPrimaryContainer   = VerdeFondo,
    secondary            = TealPrincipal,
    onSecondary          = Blanco,
    secondaryContainer   = TealFondo,
    onSecondaryContainer = Color(0xFF04342C),
    background           = GrisFondo,
    onBackground         = GrisOscuro,
    surface              = Blanco,
    onSurface            = GrisOscuro,
    surfaceVariant       = VerdeFondo,
    onSurfaceVariant     = GrisMedio,
    error                = Rojo,
    onError              = Blanco,
    errorContainer       = RojoClaro,
    outline              = GrisClaro,
    outlineVariant       = Color(0xFFE0EDD8)
)

@Composable
fun PentagonoTheme(
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
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}