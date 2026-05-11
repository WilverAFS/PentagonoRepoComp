package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Configuración",
                onMenuClick = { onBack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Ajustes del sistema", style = MaterialTheme.typography.headlineMedium, color = colorScheme.primary)

            var tema by remember { mutableStateOf("") }
            var notificaciones by remember { mutableStateOf("") }
            var seguridad by remember { mutableStateOf("") }

            OutlinedTextField(
                value = tema,
                onValueChange = { tema = it },
                label = { Text("Tema de la aplicación") },
                leadingIcon = { Icon(Icons.Filled.Palette, contentDescription = "Tema") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notificaciones,
                onValueChange = { notificaciones = it },
                label = { Text("Preferencias de notificaciones") },
                leadingIcon = { Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = seguridad,
                onValueChange = { seguridad = it },
                label = { Text("Opciones de seguridad") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Seguridad") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onSave() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
            ) {
                Icon(Icons.Filled.Settings, contentDescription = "Guardar", tint = colorScheme.onSecondary)
                Spacer(Modifier.width(8.dp))
                Text("Guardar Configuración", color = colorScheme.onSecondary)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfiguracionScreenPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        ConfiguracionScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfiguracionScreenPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        ConfiguracionScreen()
    }
}
