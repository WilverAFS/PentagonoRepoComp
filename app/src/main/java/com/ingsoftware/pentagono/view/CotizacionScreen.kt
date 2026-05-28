package com.ingsoftware.pentagono.view

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.CotizacionEntity
import com.ingsoftware.pentagono.viewmodel.CotizacionViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CotizacionScreen(
    viewModel: CotizacionViewModel,
    onBack: () -> Unit = {},
    onAddCotizacion: () -> Unit = {},
    onSearchCotizacion: () -> Unit = {},
    onOpenCotizacion: (CotizacionEntity) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val cotizaciones by viewModel.cotizaciones.collectAsState()
    val colorScheme  = MaterialTheme.colorScheme
    val context      = LocalContext.current
    val dateFmt      = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    var fechaFiltroActivo by remember { mutableStateOf(false) }
    var fechaInicio by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFin    by remember { mutableStateOf<LocalDate?>(null) }
    var estadoPago  by remember { mutableStateOf("TODO") }
    var estadoCot   by remember { mutableStateOf("TODO") }
    var mostrarFiltros by remember { mutableStateOf(false) }

    val cotizacionesFiltradas = remember(cotizaciones, fechaFiltroActivo, fechaInicio, fechaFin, estadoPago, estadoCot) {
        cotizaciones.filter { c ->
            val cumpleFecha = try {
                if (!fechaFiltroActivo) true
                else {
                    val fechaSolo = c.fecha.split(" ")[0]
                    val fechaLog  = LocalDate.parse(fechaSolo, dateFmt)
                    val desde     = fechaInicio?.let { !fechaLog.isBefore(it) } ?: true
                    val hasta     = fechaFin?.let { !fechaLog.isAfter(it) } ?: true
                    desde && hasta
                }
            } catch (e: Exception) { true }

            val cumplePago = when (estadoPago.uppercase()) {
                "TODO" -> true
                else   -> c.estado_pago.equals(estadoPago, ignoreCase = true)
            }
            val cumpleCot = when (estadoCot.uppercase()) {
                "TODO" -> true
                else   -> c.estado_cotizacion.equals(estadoCot, ignoreCase = true)
            }
            cumpleFecha && cumplePago && cumpleCot
        }.sortedByDescending { it.id_cotizacion }
    }

    val totalPendientes = cotizaciones.count { it.estado_cotizacion.equals("pendiente", ignoreCase = true) }
    val totalAceptadas  = cotizaciones.count { it.estado_cotizacion.equals("aceptado",  ignoreCase = true) }
    val hoyMes          = LocalDate.now()
    val totalMonto      = cotizaciones
        .filter { c ->
            try {
                val fechaCot = LocalDate.parse(c.fecha.split(" ")[0], dateFmt)
                fechaCot.year == hoyMes.year && fechaCot.month == hoyMes.month
            } catch (e: Exception) { false }
        }
        .sumOf { it.monto }
    val montoFormateado = when {
        totalMonto >= 1_000_000 -> "$%.1fM".format(totalMonto / 1_000_000)
        totalMonto >= 1_000     -> "$%.0fK".format(totalMonto / 1_000)
        else                    -> "$%.0f".format(totalMonto)
    }

    val filtroActivo = fechaFiltroActivo || estadoPago != "TODO" || estadoCot != "TODO"
    val filtroTexto  = when {
        filtroActivo -> "Filtros activos"
        else         -> "Todas las cotizaciones"
    }

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title            = "Cotizaciones",
                showBackButton   = true,
                onBackClick      = onBack,
                showSearchAction = true,
                onSearchClick    = onSearchCotizacion
            )
        },
        floatingActionButton = {
            PentagonoFab(onClick = onAddCotizacion)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // Chip de filtro activo
            Surface(
                onClick   = { mostrarFiltros = !mostrarFiltros },
                shape     = RoundedCornerShape(12.dp),
                color     = colorScheme.surfaceVariant,
                modifier  = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = null,
                            tint     = colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text  = filtroTexto,
                                style = MaterialTheme.typography.labelMedium,
                                color = colorScheme.primary
                            )
                            Text(
                                text  = if (filtroActivo) "Toca para modificar filtros" else "Todas las cotizaciones",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Icon(
                        imageVector = if (mostrarFiltros) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = colorScheme.onSurfaceVariant
                    )
                }
            }

            // Panel de filtros expandible
            if (mostrarFiltros) {
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Filtro estado cotización
                    var expandedCot by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(onClick = { expandedCot = true }, modifier = Modifier.fillMaxWidth()) {
                            Text(if (estadoCot == "TODO") "Estado" else estadoCot, maxLines = 1)
                        }
                        DropdownMenu(expanded = expandedCot, onDismissRequest = { expandedCot = false }) {
                            listOf("TODO", "pendiente", "aceptado", "rechazado").forEach { opt ->
                                DropdownMenuItem(text = { Text(opt) }, onClick = { estadoCot = opt; expandedCot = false })
                            }
                        }
                    }
                    // Filtro estado pago
                    var expandedPago by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(onClick = { expandedPago = true }, modifier = Modifier.fillMaxWidth()) {
                            Text(if (estadoPago == "TODO") "Pago" else estadoPago, maxLines = 1)
                        }
                        DropdownMenu(expanded = expandedPago, onDismissRequest = { expandedPago = false }) {
                            listOf("TODO", "pendiente", "anticipo", "completado", "cancelado").forEach { opt ->
                                DropdownMenuItem(text = { Text(opt) }, onClick = { estadoPago = opt; expandedPago = false })
                            }
                        }
                    }
                    // Activar/desactivar filtro de fecha
                    OutlinedButton(
                        onClick  = { fechaFiltroActivo = !fechaFiltroActivo; if (!fechaFiltroActivo) { fechaInicio = null; fechaFin = null } },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (fechaFiltroActivo) "Fecha ✓" else "Fecha", maxLines = 1)
                    }
                }
                // Selectores de fecha cuando está activo
                if (fechaFiltroActivo) {
                    Spacer(Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick  = {
                                val hoy = LocalDate.now()
                                DatePickerDialog(context, { _, y, m, d ->
                                    fechaInicio = LocalDate.of(y, m + 1, d)
                                }, hoy.year, hoy.monthValue - 1, hoy.dayOfMonth).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(fechaInicio?.toString() ?: "Desde", maxLines = 1)
                        }
                        OutlinedButton(
                            onClick  = {
                                val hoy = LocalDate.now()
                                DatePickerDialog(context, { _, y, m, d ->
                                    fechaFin = LocalDate.of(y, m + 1, d)
                                }, hoy.year, hoy.monthValue - 1, hoy.dayOfMonth).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(fechaFin?.toString() ?: "Hasta", maxLines = 1)
                        }
                    }
                }
                if (filtroActivo) {
                    TextButton(onClick = {
                        estadoCot = "TODO"; estadoPago = "TODO"
                        fechaFiltroActivo = false; fechaInicio = null; fechaFin = null
                    }) {
                        Text("Limpiar filtros", color = colorScheme.error)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Fila de estadísticas
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatMiniCard("$totalPendientes", "Pendientes", Color(0xFFF57C00), modifier = Modifier.weight(1f))
                StatMiniCard("$totalAceptadas",  "Aceptadas",  colorScheme.primary, modifier = Modifier.weight(1f))
                StatMiniCard(montoFormateado,     "Este mes",   Color(0xFF2E7D32), modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            if (cotizacionesFiltradas.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 32.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "No hay cotizaciones con los filtros seleccionados.",
                        color = colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding      = PaddingValues(bottom = 80.dp)
                ) {
                    items(cotizacionesFiltradas) { cot ->
                        CotizacionCard(cotizacion = cot, onClick = { onOpenCotizacion(cot) })
                    }
                }
            }
        }
    }
}

@Composable
private fun CotizacionCard(
    cotizacion: CotizacionEntity,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val (estadoColor, estadoBg) = when (cotizacion.estado_cotizacion.lowercase()) {
        "aceptado"  -> Pair(Color(0xFF2E7D32), Color(0xFFE8F5E9))
        "rechazado" -> Pair(Color(0xFFC62828), Color(0xFFFFEBEE))
        else        -> Pair(Color(0xFFF57C00), Color(0xFFFFF3E0))
    }

    val fechaDisplay = cotizacion.fecha.take(10).replace("-", "/").let {
        val parts = it.split("/")
        if (parts.size == 3) "${parts[2]} ${mesCorto(parts[1].toIntOrNull() ?: 1)} ${parts[0]}"
        else it
    }

    Card(
        onClick   = onClick,
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge ID
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = colorScheme.surfaceVariant
                ) {
                    Text(
                        text     = "COT-%03d".format(cotizacion.id_cotizacion),
                        style    = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color    = colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                // Badge estado
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = estadoBg
                ) {
                    Text(
                        text     = cotizacion.estado_cotizacion.uppercase(),
                        style    = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color    = estadoColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text  = cotizacion.concepto,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(14.dp), tint = colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text  = "Cliente #${cotizacion.id_cliente}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(14.dp), tint = colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text  = fechaDisplay,
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text  = "$ ${"%.0f".format(cotizacion.monto)}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun StatMiniCard(value: String, label: String, valueColor: Color, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text  = value,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = valueColor
            )
            Text(
                text  = label,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun mesCorto(mes: Int) = when (mes) {
    1 -> "Ene"; 2 -> "Feb"; 3 -> "Mar"; 4 -> "Abr"; 5 -> "May"; 6 -> "Jun"
    7 -> "Jul"; 8 -> "Ago"; 9 -> "Sep"; 10 -> "Oct"; 11 -> "Nov"; else -> "Dic"
}
