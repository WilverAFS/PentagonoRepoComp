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
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import com.ingsoftware.pentagono.data.OrdenEntity
import com.ingsoftware.pentagono.viewmodel.OrdenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenesScreen(
    viewModel: OrdenViewModel,
    onBack: () -> Unit = {},
    onAddOrden: () -> Unit = {},
    onSearchOrden: () -> Unit = {},
    onEditOrden: (OrdenEntity) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val ordenes by viewModel.ordenes.collectAsState()

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = { PentagonoTopBar(title = "Órdenes de Trabajo", onMenuClick = { onMenuClick() }) },
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
            Text("Listado de Órdenes",
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                            Column(
                                modifier = Modifier.weight(1f) // ✅ ocupa todo el espacio disponible
                            ) {
                                Text(
                                    "ID: ${orden.id_orden}",
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Cotización: ${orden.id_cotizacion}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Empleado: ${orden.id_empleado}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Dueño: ${orden.id_dueño}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Inicio: ${orden.fecha_inicio}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Fin: ${orden.fecha_fin ?: "N/A"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Estado: ${orden.estado}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Entrega: ${orden.fecha_entrega ?: "N/A"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
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
