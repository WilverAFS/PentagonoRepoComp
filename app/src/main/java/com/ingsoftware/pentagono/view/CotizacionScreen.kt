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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import com.ingsoftware.pentagono.data.CotizacionEntity
import com.ingsoftware.pentagono.viewmodel.CotizacionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CotizacionScreen(
    viewModel: CotizacionViewModel,
    onBack: () -> Unit = {},
    onAddCotizacion: () -> Unit = {},
    onSearchCotizacion: () -> Unit = {},
    onEditCotizacion: (CotizacionEntity) -> Unit = {}
) {
    val cotizaciones by viewModel.cotizaciones.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(title = "Cotizaciones", onMenuClick = { onBack() })
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

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
                            Column(
                                modifier = Modifier.weight(1f) // ✅ ocupa todo el espacio disponible
                            ) {
                                Text(
                                    "ID Cotización: ${cotizacion.id_cotizacion}",
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Fecha: ${cotizacion.fecha}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Descripción: ${cotizacion.descripcion ?: "-"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 3, // ✅ descripción larga se parte en varias líneas
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Monto: $${cotizacion.monto}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Estado Cotización: ${cotizacion.estado_cotizacion}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "Estado Pago: ${cotizacion.estado_pago}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
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
