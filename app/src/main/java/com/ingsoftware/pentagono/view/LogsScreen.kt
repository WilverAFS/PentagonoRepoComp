package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.model.Log
import com.ingsoftware.pentagono.model.TipoLog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    logs: List<Log> = emptyList(),
    onBack: () -> Unit = {},
    onSearchLog: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Logs del Sistema",
                onMenuClick = { onBack() }
            )
        },
        bottomBar = {
            PentagonoBottomBar(
                onSearchClick = { onSearchLog() },
                onAddClick = { } // no se usa aquí
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(16.dp)
        ) {
            Text(
                "Historial de Logs",
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(logs) { log ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("ID: ${log.id_log}", style = MaterialTheme.typography.bodySmall)
                            Text("Dueño: ${log.id_dueño}", style = MaterialTheme.typography.bodyMedium)
                            Text("Tipo: ${log.tipo}", style = MaterialTheme.typography.bodyMedium)
                            Text("Descripción: ${log.descripcion}", style = MaterialTheme.typography.bodyMedium)
                            Text("Fecha: ${log.fecha}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LogsScreenPreviewLight() {
    val logsDemo = listOf(
        Log(1, 301, TipoLog.ADD, "Se agregó un nuevo cliente", "2026-05-17"),
        Log(2, 301, TipoLog.UPDATE, "Se actualizó una cotización", "2026-05-16"),
        Log(3, 301, TipoLog.ACCESS, "Dueño accedió al sistema", "2026-05-15")
    )
    MaterialTheme(colorScheme = lightColorScheme()) {
        LogsScreen(logs = logsDemo)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LogsScreenPreviewDark() {
    val logsDemo = listOf(
        Log(1, 301, TipoLog.ADD, "Se agregó un nuevo cliente", "2026-05-17"),
        Log(2, 301, TipoLog.UPDATE, "Se actualizó una cotización", "2026-05-16"),
        Log(3, 301, TipoLog.ACCESS, "Dueño accedió al sistema", "2026-05-15")
    )
    MaterialTheme(colorScheme = darkColorScheme()) {
        LogsScreen(logs = logsDemo)
    }
}
