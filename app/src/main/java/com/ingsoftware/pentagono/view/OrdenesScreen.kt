package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import com.ingsoftware.pentagono.data.OrdenEntity
import com.ingsoftware.pentagono.viewmodel.OrdenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenesScreen(
    viewModel: OrdenViewModel,
    onBack: () -> Unit = {},
    onSearchOrden: () -> Unit = {},
    onOpenOrden: (OrdenEntity) -> Unit = {},   // ✅ ahora usamos onOpenOrden
    onMenuClick: () -> Unit = {}
) {
    val ordenes by viewModel.ordenes.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    // ✅ Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { PentagonoTopBar(title = "Órdenes de Trabajo", onMenuClick = { onMenuClick() }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            PentagonoBottomBar(
                onSearchClick = { onSearchOrden() },
                onAddClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Solo se pueden crear órdenes cuando una cotización es marcada como aceptada"
                        )
                    }
                }
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text("ID: ${orden.id_orden}", style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Cotización: ${orden.id_cotizacion}", style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Empleado: ${orden.id_empleado}", style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Dueño: ${orden.id_dueño}", style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Inicio: ${orden.fecha_inicio}", style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Fin: ${orden.fecha_fin ?: "N/A"}", style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Estado: ${orden.estado}", style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                Text("Entrega: ${orden.fecha_entrega ?: "N/A"}", style = MaterialTheme.typography.bodyMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }

                            // ✅ Ícono de abrir detalle
                            IconButton(onClick = { onOpenOrden(orden) }) {
                                Icon(Icons.Filled.OpenInNew, contentDescription = "Abrir Orden")
                            }
                        }
                    }
                }
            }
        }
    }
}
