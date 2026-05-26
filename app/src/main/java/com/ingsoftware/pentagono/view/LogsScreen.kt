package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.viewmodel.LogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    viewModel: LogViewModel,
    onBack: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val logs by viewModel.logs.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    // ✅ Orden inverso: más recientes primero
    val logsOrdenados = logs.sortedByDescending { it.id_log }

    Scaffold(
        topBar = { PentagonoTopBar(title = "Logs del Sistema", onMenuClick = { onMenuClick() }) }
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

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(logsOrdenados) { log ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // ✅ Compacto, sin etiquetas
                            Text("{#${log.id_log}, Dueño ${log.id_dueño}, ${log.fecha}}")
                            Text("${log.tipo}: ${log.descripcion}")
                        }
                    }
                }
            }
        }
    }
}
