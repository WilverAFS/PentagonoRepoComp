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

// Tipografía base (Roboto)
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)

// Colores principales
val VerdePrincipal = Color(0xFF3FE048)
val GrisPrincipal = Color(0xFF616161)
val NaranjaPrincipal = Color(0xFF304FFE)

// Colores secundarios
val Negro = Color(0xFF000000)
val Blanco = Color(0xFFFFFFFF)

// Paleta para modo oscuro
private val DarkColorScheme = darkColorScheme(
    primary = VerdePrincipal,
    secondary = NaranjaPrincipal,
    background = Negro,
    surface = GrisPrincipal,
    onPrimary = Blanco,
    onSecondary = Negro,
    onBackground = Blanco,
    onSurface = Blanco
)

// Paleta para modo claro
private val LightColorScheme = lightColorScheme(
    primary = VerdePrincipal,
    secondary = NaranjaPrincipal,
    background = Blanco,
    surface = GrisPrincipal,
    onPrimary = Blanco,
    onSecondary = Negro,
    onBackground = Negro,
    onSurface = Blanco
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
