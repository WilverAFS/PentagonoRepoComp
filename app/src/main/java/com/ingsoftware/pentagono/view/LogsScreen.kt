package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ingsoftware.pentagono.viewmodel.LogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    viewModel: LogViewModel,
    onBack: () -> Unit = {},
    onSearchLog: () -> Unit = {}
) {
    val logs by viewModel.logs.collectAsState()

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = { PentagonoTopBar(title = "Logs del Sistema", onMenuClick = { onBack() }) },
        bottomBar = { PentagonoBottomBar(onSearchClick = { onSearchLog() }, onAddClick = { }) } // sin botón de añadir
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().background(colorScheme.background).padding(16.dp)
        ) {
            Text("Historial de Logs", style = MaterialTheme.typography.headlineMedium, color = colorScheme.primary)

            Spacer(Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(logs) { log ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("ID: ${log.id_log}")
                            Text("Dueño: ${log.id_dueño}")
                            Text("Tipo: ${log.tipo}")
                            Text("Descripción: ${log.descripcion}")
                            Text("Fecha: ${log.fecha}")
                        }
                    }
                }
            }
        }
    }
}
