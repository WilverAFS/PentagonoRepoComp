package com.ingsoftware.pentagono.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import com.ingsoftware.pentagono.ui.theme.PentagonoTheme

@Composable
fun PentagonoBottomBar(
    onSearchClick: () -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    BottomAppBar(
        containerColor = colorScheme.surface,
        contentColor = colorScheme.primary,
        tonalElevation = 4.dp
    ) {
        Spacer(Modifier.weight(1f))

        IconButton(onClick = onSearchClick) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Buscar",
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }

        FloatingActionButton(
            onClick = onAddClick,
            containerColor = colorScheme.secondary,
            contentColor = colorScheme.onSecondary,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = 2.dp,
                pressedElevation = 6.dp
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Agregar",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PentagonoBottomBarPreviewLight() {
    PentagonoTheme(darkTheme = false) { PentagonoBottomBar() }
}

@Preview(showBackground = true)
@Composable
fun PentagonoBottomBarPreviewDark() {
    PentagonoTheme(darkTheme = true) { PentagonoBottomBar() }
}