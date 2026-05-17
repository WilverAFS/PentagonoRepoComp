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
import com.ingsoftware.pentagono.model.Cotizacion
import com.ingsoftware.pentagono.model.EstadoCotizacion
import com.ingsoftware.pentagono.model.EstadoPago

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CotizacionScreen(
    cotizaciones: List<Cotizacion> = emptyList(),
    onBack: () -> Unit = {},
    onAddCotizacion: () -> Unit = {},
    onSearchCotizacion: () -> Unit = {},
    onEditCotizacion: (Cotizacion) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Cotizaciones",
                onMenuClick = { onBack() }
            )
        },
        bottomBar = {
            PentagonoBottomBar(
                onSearchClick = { onSearchCotizacion() },
                onAddClick = { onAddCotizacion() }
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
                "Listado de Cotizaciones",
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cotizaciones) { cotizacion ->
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
                                Text("ID: ${cotizacion.id_cotizacion}", style = MaterialTheme.typography.bodySmall)
                                Text("Cliente: ${cotizacion.id_cliente}", style = MaterialTheme.typography.bodyMedium)
                                Text("Fecha: ${cotizacion.fecha}", style = MaterialTheme.typography.bodyMedium)
                                Text("Descripción: ${cotizacion.descripcion}", style = MaterialTheme.typography.bodyMedium)
                                Text("Monto: $${cotizacion.monto}", style = MaterialTheme.typography.bodyMedium)
                                Text("Estado: ${cotizacion.estado}", style = MaterialTheme.typography.bodyMedium)
                                Text("Pago: ${cotizacion.pago}", style = MaterialTheme.typography.bodyMedium)
                            }
                            IconButton(onClick = { onEditCotizacion(cotizacion) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar Cotización")
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
fun CotizacionScreenPreviewLight() {
    val cotizacionesDemo = listOf(
        Cotizacion(1, 101, "2026-05-17", "Instalación de vidrio templado", 2500.0, EstadoCotizacion.PENDIENTE, EstadoPago.ANTICIPO),
        Cotizacion(2, 102, "2026-05-10", "Puerta corrediza", 1800.0, EstadoCotizacion.ACEPTADO, EstadoPago.COMPLETO)
    )
    MaterialTheme(colorScheme = lightColorScheme()) {
        CotizacionScreen(cotizaciones = cotizacionesDemo)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CotizacionScreenPreviewDark() {
    val cotizacionesDemo = listOf(
        Cotizacion(1, 101, "2026-05-17", "Instalación de vidrio templado", 2500.0, EstadoCotizacion.PENDIENTE, EstadoPago.ANTICIPO),
        Cotizacion(2, 102, "2026-05-10", "Puerta corrediza", 1800.0, EstadoCotizacion.ACEPTADO, EstadoPago.COMPLETO)
    )
    MaterialTheme(colorScheme = darkColorScheme()) {
        CotizacionScreen(cotizaciones = cotizacionesDemo)
    }
}
