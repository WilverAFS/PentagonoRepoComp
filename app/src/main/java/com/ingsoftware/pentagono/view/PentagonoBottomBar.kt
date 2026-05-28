package com.ingsoftware.pentagono.view

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PentagonoBottomBar(
    onSearchClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    // Kept for API compatibility — screens pass this to Scaffold.bottomBar.
    // It renders nothing; each screen places its FAB via Scaffold.floatingActionButton.
    BottomAppBar(
        containerColor = Color.Transparent,
        contentColor   = Color.Transparent,
        tonalElevation = 0.dp
    ) {}
}

@Composable
fun PentagonoFab(onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    FloatingActionButton(
        onClick        = onClick,
        containerColor = colorScheme.primary,
        contentColor   = Color.White,
        shape          = CircleShape
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Agregar")
    }
}
