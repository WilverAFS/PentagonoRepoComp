package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextOverflow
import com.ingsoftware.pentagono.data.OrdenEntity
import com.ingsoftware.pentagono.viewmodel.OrdenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarOrdenScreen(
    viewModel: OrdenViewModel,
    onBack: () -> Unit = {},
    onOpenOrden: (OrdenEntity) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val scope = rememberCoroutineScope()

    var idOrden by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf<OrdenEntity?>(null) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { PentagonoTopBar(title = "Buscar Orden", onMenuClick = { onMenuClick() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = idOrden,
                onValueChange = { idOrden = it },
                label = { Text("ID de Orden") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            val id = idOrden.toIntOrNull()
                            if (id != null) {
                                val orden = viewModel.findById(id)
                                if (orden != null) {
                                    resultado = orden
                                    mensajeError = null
                                } else {
                                    resultado = null
                                    mensajeError = "No se encontró ninguna orden con ID $id"
                                }
                            } else {
                                mensajeError = "Ingrese un número válido"
                            }
                        }
                    }) {
                        Icon(Icons.Filled.Search, contentDescription = "Buscar")
                    }
                }
            )

            if (mensajeError != null) {
                Text(mensajeError!!, color = colorScheme.error)
            }

            resultado?.let { orden ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("ID: ${orden.id_orden}", style = MaterialTheme.typography.bodyMedium)
                        Text("Cotización: ${orden.id_cotizacion}", style = MaterialTheme.typography.bodyMedium)
                        Text("Empleado: ${orden.id_empleado}", style = MaterialTheme.typography.bodyMedium)
                        Text("Dueño: ${orden.id_dueño}", style = MaterialTheme.typography.bodyMedium)
                        Text("Inicio: ${orden.fecha_inicio}", style = MaterialTheme.typography.bodyMedium)
                        Text("Fin: ${orden.fecha_fin ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                        Text("Estado: ${orden.estado}", style = MaterialTheme.typography.bodyMedium)
                        Text("Entrega: ${orden.fecha_entrega ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)

                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = { onOpenOrden(orden) },
                            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                        ) {
                            Text("Abrir Detalle")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            OutlinedButton(onClick = { onBack() }) { Text("Regresar") }
        }
    }
}
