package com.ingsoftware.pentagono.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PentagonoBottomBar(
    onSearchClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    BottomAppBar(
        containerColor = colorScheme.surface,
        contentColor = colorScheme.primary
    ) {

        Spacer(Modifier.weight(1f)) // empuja los íconos a la derecha

        IconButton(onClick = onSearchClick) {
            Icon(Icons.Filled.Search, contentDescription = "Buscar Elemento")
        }

        IconButton(onClick = onAddClick) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar Elemento")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PentagonoBottomBarPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        PentagonoBottomBar()
    }
}

@Preview(showBackground = true)
@Composable
fun PentagonoBottomBarPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        PentagonoBottomBar()
    }
}
