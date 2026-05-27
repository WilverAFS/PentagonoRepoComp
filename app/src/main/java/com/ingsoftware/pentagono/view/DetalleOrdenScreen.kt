package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.OrdenEntity
import com.ingsoftware.pentagono.model.EstadoOrden
import com.ingsoftware.pentagono.model.Orden.Companion.getSystemDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleOrdenScreen(
    orden: OrdenEntity,
    onUpdateEstado: (OrdenEntity, EstadoOrden) -> Unit,
    onMenuClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    val fechaActual = LocalDate.parse(getSystemDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val fechaFin = orden.fecha_fin?.let { LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
    val diasRestantes = fechaFin?.let { ChronoUnit.DAYS.between(fechaActual, it).toInt() }

    val (mensajeFecha, colorFecha) = when {
        fechaFin == null -> "Fecha de entrega no definida" to colorScheme.surfaceVariant
        diasRestantes!! > 2 -> "Faltan $diasRestantes días para la entrega" to colorScheme.surfaceVariant
        diasRestantes in 0..1 -> "Plazo por vencer: faltan $diasRestantes días" to colorScheme.errorContainer
        diasRestantes < 0 -> "Orden atrasada por ${-diasRestantes} días" to colorScheme.error
        else -> "" to colorScheme.surfaceVariant
    }

    val estadoColor = when (orden.estado) {
        EstadoOrden.PENDIENTE -> colorScheme.error
        EstadoOrden.TERMINADO -> colorScheme.secondary
        EstadoOrden.ENTREGADO -> colorScheme.primary
        EstadoOrden.CANCELADO -> colorScheme.outline
    }

    Scaffold(
        topBar = { PentagonoTopBar(title = "Detalle de Orden", onMenuClick = { onMenuClick() }) }
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
            if (orden.estado != EstadoOrden.CANCELADO && orden.estado != EstadoOrden.ENTREGADO) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorFecha)
                        .padding(12.dp)
                ) {
                    Text(mensajeFecha, style = MaterialTheme.typography.bodyLarge)
                }
            }

            Text("Orden ID: ${orden.id_orden}", style = MaterialTheme.typography.bodyLarge)
            Text("Cotización ID: ${orden.id_cotizacion}")
            Text("Empleado ID: ${orden.id_empleado}")
            Text("Dueño ID: ${orden.id_dueño}")
            Text("Fecha inicio: ${orden.fecha_inicio}")
            Text("Fecha fin acordada: ${orden.fecha_fin ?: "No definida"}")

            Text(
                "Estado actual: ${orden.estado}",
                style = MaterialTheme.typography.bodyLarge,
                color = estadoColor
            )

            Text("Fecha entrega real: ${orden.fecha_entrega ?: "Pendiente"}")

            Spacer(Modifier.height(24.dp))

            when (orden.estado) {
                EstadoOrden.PENDIENTE -> {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            onClick = { onUpdateEstado(orden.copy(estado = EstadoOrden.TERMINADO), EstadoOrden.TERMINADO) },
                            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.secondary)
                        ) { Text("Marcar como Terminado") }

                        OutlinedButton(
                            onClick = { onUpdateEstado(orden.copy(estado = EstadoOrden.CANCELADO), EstadoOrden.CANCELADO) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) { Text("Cancelar Orden", color = Color.White ) }
                    }
                }
                EstadoOrden.TERMINADO -> {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(
                            onClick = {
                                onUpdateEstado(
                                    orden.copy(
                                        estado = EstadoOrden.ENTREGADO,
                                        fecha_entrega = getSystemDate()
                                    ),
                                    EstadoOrden.ENTREGADO
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                        ) { Text("Marcar como Entregado", color = colorScheme.onSecondary) }

                        OutlinedButton(
                            onClick = { onUpdateEstado(orden.copy(estado = EstadoOrden.CANCELADO), EstadoOrden.CANCELADO) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) { Text("Cancelar Orden", color = Color.White) }
                    }
                }
                EstadoOrden.ENTREGADO -> {
                    if (orden.fecha_entrega != null && fechaFin != null) {
                        val fechaEntrega = LocalDate.parse(orden.fecha_entrega, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        val diferencia = ChronoUnit.DAYS.between(fechaEntrega, fechaFin).toInt()

                        val mensajeEntrega: String
                        val colorEntrega: Color

                        if (diferencia >= 0) {
                            mensajeEntrega = if (diferencia == 0) "Orden entregada en la fecha acordada"
                            else "Orden entregada con ${diferencia} días de anticipación"
                            colorEntrega = colorScheme.primary
                        } else {
                            mensajeEntrega = "Orden entregada con ${-diferencia} días de atraso"
                            colorEntrega = colorScheme.outline
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorEntrega)
                                .padding(12.dp)
                        ) {
                            Text(mensajeEntrega, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                        }
                    }
                }
                EstadoOrden.CANCELADO -> {
                    // No se muestran botones ni contador de fechas
                }
            }
        }
    }
}
