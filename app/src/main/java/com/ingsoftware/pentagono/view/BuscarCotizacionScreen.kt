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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.CotizacionEntity
import com.ingsoftware.pentagono.viewmodel.CotizacionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarCotizacionScreen(
    viewModel: CotizacionViewModel,
    onBack: () -> Unit = {},
    onOpenCotizacion: (CotizacionEntity) -> Unit = {},
    onMenuClick: () -> Unit = {}   // ✅ nuevo parámetro para consistencia
) {
    val cotizaciones by viewModel.cotizaciones.collectAsState()
    var query by remember { mutableStateOf("") }
    var resultados by remember { mutableStateOf(listOf<CotizacionEntity>()) }

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Buscar Cotización",
                onMenuClick = { onMenuClick() } // ✅ menú → MenuScreen
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it.trim()
                    resultados = if (query.isBlank()) {
                        emptyList()
                    } else {
                        when {
                            // 🔹 Si es numérico, puede ser idCotizacion o idCliente
                            query.matches(Regex("^[0-9]+$")) -> {
                                val id = query.toInt()
                                cotizaciones.filter {
                                    it.id_cotizacion == id || it.id_cliente == id
                                }
                            }
                            else -> {
                                // 🔹 Si es texto, buscar por concepto
                                cotizaciones.filter {
                                    it.concepto.contains(query, ignoreCase = true)
                                }
                            }
                        }
                    }
                },
                label = { Text("Buscar por ID Cotización, ID Cliente o Concepto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (resultados.isEmpty() && query.isNotBlank()) {
                Text("No se encontraron resultados", color = Color.Red)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(resultados) { cotizacion ->
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
                                Text("ID Cotización: ${cotizacion.id_cotizacion}", style = MaterialTheme.typography.bodySmall)
                                Text("Cliente: ${cotizacion.id_cliente}", style = MaterialTheme.typography.bodyMedium)
                                Text("Fecha: ${cotizacion.fecha}", style = MaterialTheme.typography.bodyMedium)
                                Text("Concepto: ${cotizacion.concepto}", style = MaterialTheme.typography.bodyMedium)
                                Text("Monto: $${cotizacion.monto}", style = MaterialTheme.typography.bodyMedium)
                                Text("Estado Cotización: ${cotizacion.estado_cotizacion}", style = MaterialTheme.typography.bodyMedium)
                                Text("Estado Pago: ${cotizacion.estado_pago}", style = MaterialTheme.typography.bodyMedium)
                            }
                            IconButton(onClick = { onOpenCotizacion(cotizacion) }) {
                                Icon(Icons.Filled.OpenInNew, contentDescription = "Abrir Cotización")
                            }
                        }
                    }
                }
            }
        }
    }
}
