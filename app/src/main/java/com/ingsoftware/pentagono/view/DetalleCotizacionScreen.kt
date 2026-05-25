package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.CotizacionEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCotizacionScreen(
    cotizacion: CotizacionEntity,
    onUpdateEstadoCotizacion: (CotizacionEntity, String) -> Unit = { _, _ -> },
    onUpdateEstadoPago: (CotizacionEntity, String) -> Unit = { _, _ -> },
    onMenuClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    var showConfirmDialog by remember { mutableStateOf<Pair<String, String>?>(null) }

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Detalle Cotización",
                onMenuClick = { onMenuClick() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Datos principales
            Text("Información de la Cotización", style = MaterialTheme.typography.headlineSmall, color = colorScheme.primary)
            Divider()

            Text("ID: ${cotizacion.id_cotizacion}", style = MaterialTheme.typography.bodyLarge)
            Text("Cliente: ${cotizacion.id_cliente}", style = MaterialTheme.typography.bodyLarge)
            Text("Fecha: ${cotizacion.fecha}", style = MaterialTheme.typography.bodyLarge)
            Text("Concepto: ${cotizacion.concepto}", style = MaterialTheme.typography.bodyLarge)
            Text("Monto: $${cotizacion.monto}", style = MaterialTheme.typography.bodyLarge)

            Spacer(Modifier.height(8.dp))

            Text("Descripción:", style = MaterialTheme.typography.titleMedium, color = colorScheme.primary)
            Text(cotizacion.descripcion, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(16.dp))

            // Estado Cotización
            Text("Estado Cotización:", style = MaterialTheme.typography.titleMedium)
            EstadoChip(
                estado = cotizacion.estado_cotizacion,
                colores = mapOf(
                    "aceptado" to Color.Green,
                    "pendiente" to Color(0xFFFFA500),
                    "rechazado" to Color.Gray
                )
            )

            if (cotizacion.estado_cotizacion == "pendiente") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { showConfirmDialog = "cotizacion" to "aceptado" },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                    ) { Text("Aceptar", color = Color.Black) }

                    Button(
                        onClick = { showConfirmDialog = "cotizacion" to "rechazado" },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) { Text("Rechazar", color = Color.White) }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Estado Pago
            Text("Estado Pago:", style = MaterialTheme.typography.titleMedium)
            EstadoChip(
                estado = cotizacion.estado_pago,
                colores = mapOf(
                    "cancelado" to Color.Red,
                    "anticipo" to Color.Blue,
                    "completado" to Color.Green
                )
            )

            if (cotizacion.estado_cotizacion == "aceptado") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (cotizacion.estado_pago != "completado" && cotizacion.estado_pago != "cancelado") {
                        Button(
                            onClick = { showConfirmDialog = "pago" to "completado" },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                        ) { Text("Completado", color = Color.Black) }

                        if (cotizacion.estado_pago == "pendiente") {
                            Button(
                                onClick = { showConfirmDialog = "pago" to "anticipo" },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                            ) { Text("Anticipo", color = Color.White) }
                        }

                        Button(
                            onClick = { showConfirmDialog = "pago" to "cancelado" },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) { Text("Cancelado", color = Color.White) }
                    }
                }
            }
        }

        // 🔔 Confirmación irreversible
        showConfirmDialog?.let { (tipo, nuevoEstado) ->
            AlertDialog(
                onDismissRequest = { showConfirmDialog = null },
                title = { Text("Confirmar acción") },
                text = {
                    Text(
                        if (tipo == "cotizacion")
                            "¿Está seguro de ${if (nuevoEstado == "aceptado") "aceptar" else "rechazar"} esta cotización? \n Esta acción es irreversible."
                        else
                            "¿Está seguro de marcar el pago como $nuevoEstado? \n Esta acción es irreversible."
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (tipo == "cotizacion") {
                            if (nuevoEstado == "rechazado") {
                                onUpdateEstadoPago(cotizacion, "cancelado")
                                onUpdateEstadoCotizacion(cotizacion, "rechazado")
                            } else {
                                onUpdateEstadoCotizacion(cotizacion, "aceptado")
                            }
                        } else {
                            onUpdateEstadoPago(cotizacion, nuevoEstado)
                        }
                        showConfirmDialog = null
                    }) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = null }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
fun EstadoChip(estado: String, colores: Map<String, Color>) {
    val color = colores[estado] ?: Color.LightGray
    Surface(
        color = color,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            estado.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
