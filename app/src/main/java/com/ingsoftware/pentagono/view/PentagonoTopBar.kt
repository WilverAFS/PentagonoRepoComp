package com.ingsoftware.pentagono.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.tooling.preview.Preview
import com.ingsoftware.pentagono.R
import com.ingsoftware.pentagono.ui.theme.PentagonoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PentagonoTopBar(
    title: String = "Vidrios y Cristales Pentágono",
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = colorScheme.primary
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_vidrieria),
                    contentDescription = "Logo Vidriería Pentágono",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(start = 8.dp)
                )
            }
        },
        actions = {
            if (showBackButton) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_vidrieria),
                    contentDescription = "Logo Vidriería Pentágono",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 4.dp)
                )
            } else {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Opciones",
                        tint = colorScheme.primary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        },
        // surfaceVariant = VerdeFondo (0xFFEAF3DE) — verde muy claro, logo resalta bien
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = colorScheme.surfaceVariant,
            titleContentColor = colorScheme.primary,
            navigationIconContentColor = colorScheme.primary,
            actionIconContentColor = colorScheme.primary
        )
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarNormalPreview() {
    PentagonoTheme(darkTheme = false) { PentagonoTopBar(showBackButton = false) }
}

@Preview(showBackground = true)
@Composable
fun TopBarBackPreview() {
    PentagonoTheme(darkTheme = false) { PentagonoTopBar(title = "Menú", showBackButton = true) }
}