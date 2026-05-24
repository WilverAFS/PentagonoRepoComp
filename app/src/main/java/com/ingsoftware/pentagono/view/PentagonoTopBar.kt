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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.tooling.preview.Preview
import com.ingsoftware.pentagono.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PentagonoTopBar(
    title: String = "Vidrios y Cristales Pentágono",
    onMenuClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.onSecondary
            )
        },
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_vidrieria),
                contentDescription = "Logo Vidriería Pentágono",
                modifier = Modifier
                    .size(48.dp)
                    .padding(start = 8.dp)
            )
        },
        actions = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Filled.Menu,
                    contentDescription = "Opciones",
                    tint = colorScheme.onSecondary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.secondary,
            titleContentColor = colorScheme.onPrimary
        )
    )
}
