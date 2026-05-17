package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.model.Orden
import com.ingsoftware.pentagono.model.EstadoOrden

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenesScreen(
    ordenes: List<Orden> = emptyList(),
    onBack: () -> Unit = {},
    onAddOrden: () -> Unit = {},
    onSearchOrden: () -> Unit = {},
    onEditOrden: (Orden) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Órdenes de Trabajo",
                onMenuClick = { onBack() }
            )
        },
        bottomBar = {
            PentagonoBottomBar(
                onSearchClick = { onSearchOrden() },
                onAddClick = { onAddOrden() }
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
                "Listado de Órdenes",
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(ordenes) { orden ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("ID: ${orden.id_orden}", style = MaterialTheme.typography.bodySmall)
                                Text("Cotización: ${orden.id_cotizacion}", style = MaterialTheme.typography.bodyMedium)
                                Text("Empleado: ${orden.id_empleado}", style = MaterialTheme.typography.bodyMedium)
                                Text("Dueño: ${orden.id_dueño}", style = MaterialTheme.typography.bodyMedium)
                                Text("Inicio: ${orden.fecha_inicio}", style = MaterialTheme.typography.bodyMedium)
                                Text("Fin: ${orden.fecha_fin ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                                Text("Estado: ${orden.estado}", style = MaterialTheme.typography.bodyMedium)
                                Text("Entrega: ${orden.fecha_entrega ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                            }
                            IconButton(onClick = { onEditOrden(orden) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar Orden")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrdenesScreenPreviewLight() {
    val ordenesDemo = listOf(
        Orden(1, 101, 201, 301, "2026-05-01", "2026-05-10", EstadoOrden.PENDIENTE, null),
        Orden(2, 102, 202, 301, "2026-04-15", "2026-04-20", EstadoOrden.TERMINADO, "2026-04-21")
    )
    MaterialTheme(colorScheme = lightColorScheme()) {
        OrdenesScreen(ordenes = ordenesDemo)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrdenesScreenPreviewDark() {
    val ordenesDemo = listOf(
        Orden(1, 101, 201, 301, "2026-05-01", "2026-05-10", EstadoOrden.PENDIENTE, null),
        Orden(2, 102, 202, 301, "2026-04-15", "2026-04-20", EstadoOrden.TERMINADO, "2026-04-21")
    )
    MaterialTheme(colorScheme = darkColorScheme()) {
        OrdenesScreen(ordenes = ordenesDemo)
    }
}
