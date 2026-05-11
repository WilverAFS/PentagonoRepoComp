package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    onBack: () -> Unit = {},
    onClearLogs: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    // Ejemplo de registros simulados
    val logs = listOf(
        "Usuario admin inició sesión",
        "Se creó una cotización para Cliente A",
        "Orden de trabajo #123 marcada como completada",
        "Empleado Juan Pérez agregado",
        "Error: conexión fallida al servidor"
    )

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Logs del Sistema",
                onMenuClick = { onBack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp)
        ) {
            Text("Historial de eventos", style = MaterialTheme.typography.headlineMedium, color = colorScheme.primary)

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(logs) { log ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorScheme.secondary)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(log, color = colorScheme.onSecondary)
                            Icon(Icons.Filled.List, contentDescription = "Log", tint = colorScheme.onSecondary)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { onClearLogs() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Borrar", tint = colorScheme.onPrimary)
                Spacer(Modifier.width(8.dp))
                Text("Borrar Logs", color = colorScheme.onPrimary)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LogsScreenPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        LogsScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LogsScreenPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        LogsScreen()
    }
}
