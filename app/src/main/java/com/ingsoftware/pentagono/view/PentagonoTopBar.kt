package com.ingsoftware.pentagono.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PentagonoTopBar(
    title: String = "Vidrios y Cristales Pentágono",
    showBackButton: Boolean = false,
    showSearchAction: Boolean = false,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    CenterAlignedTopAppBar(
        title = {
            Text(
                text  = title,
                style = MaterialTheme.typography.titleLarge,
                color = colorScheme.onSurface
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint     = colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_vidrieria),
                    contentDescription = "Logo Pentagono",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(start = 8.dp)
                )
            }
        },
        actions = {
            when {
                showSearchAction -> {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Buscar",
                            tint     = colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                !showBackButton -> {
                    IconButton(onClick = onMenuClick) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Opciones",
                            tint     = colorScheme.onSurface,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor            = colorScheme.surface,
            titleContentColor         = colorScheme.onSurface,
            navigationIconContentColor = colorScheme.onSurface,
            actionIconContentColor    = colorScheme.onSurface
        )
    )
}
