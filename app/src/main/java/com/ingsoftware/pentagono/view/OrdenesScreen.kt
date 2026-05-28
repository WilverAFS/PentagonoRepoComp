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
import com.ingsoftware.pentagono.data.OrdenEntity
import com.ingsoftware.pentagono.viewmodel.OrdenViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenesScreen(
    viewModel: OrdenViewModel,
    onBack: () -> Unit = {},
    onSearchOrden: () -> Unit = {},
    onOpenOrden: (OrdenEntity) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val ordenes      by viewModel.ordenes.collectAsState()
    val colorScheme  = MaterialTheme.colorScheme
    val context      = LocalContext.current

    val dateFmt     = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateTimeFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val fechaActual = LocalDate.now()

    var fechaFiltroActivo by remember { mutableStateOf(false) }
    var fechaCampo        by remember { mutableStateOf("INICIO") }
    var fechaInicio       by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFin          by remember { mutableStateOf<LocalDate?>(null) }
    var estadoFiltro      by remember { mutableStateOf("TODO") }
    var mostrarFiltros    by remember { mutableStateOf(false) }

    val ordenesFiltradas by remember(ordenes, fechaFiltroActivo, fechaCampo, fechaInicio, fechaFin, estadoFiltro) {
        derivedStateOf {
            ordenes.filter { o ->
                val fechaRaw = when (fechaCampo) {
                    "INICIO"  -> o.fecha_inicio
                    "FIN"     -> o.fecha_fin
                    "ENTREGA" -> o.fecha_entrega
                    else      -> o.fecha_inicio
                }
                val cumpleFecha = try {
                    if (!fechaFiltroActivo) true
                    else {
                        val dt = try {
                            LocalDateTime.parse(fechaRaw ?: "", dateTimeFmt)
                        } catch (e: Exception) {
                            val fechaSolo = (fechaRaw ?: "").split(" ").firstOrNull() ?: ""
                            LocalDate.parse(fechaSolo, dateFmt).atStartOfDay()
                        }
                        val logDate = dt.toLocalDate()
                        val desde   = fechaInicio?.let { !logDate.isBefore(it) } ?: true
                        val hasta   = fechaFin?.let { !logDate.isAfter(it) } ?: true
                        desde && hasta
                    }
                } catch (e: Exception) { true }

                val cumpleEstado = when (estadoFiltro.uppercase()) {
                    "TODO" -> true
                    else   -> (o.estado?.toString() ?: "").equals(estadoFiltro, ignoreCase = true)
                }
                cumpleFecha && cumpleEstado
            }.sortedByDescending { it.id_orden }
        }
    }

    val statPendiente  = ordenes.count { it.estado.name == "PENDIENTE" }
    val statEntregadas = ordenes.count { it.estado.name == "ENTREGADO" }
    val statTerminadas = ordenes.count { it.estado.name == "TERMINADO" }
    val statAtrasadas  = ordenes.count {
        val f = try { LocalDate.parse(it.fecha_fin ?: "", dateFmt) } catch (e: Exception) { null }
        f != null && f.isBefore(fechaActual) && it.estado.name != "ENTREGADO" && it.estado.name != "CANCELADO"
    }

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title            = "Órdenes de Trabajo",
                showBackButton   = true,
                onBackClick      = onBack,
                showSearchAction = true,
                onSearchClick    = onSearchOrden
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // Fila de stats
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OrdenStatChip("$statPendiente",  "Pendientes",  Color(0xFFF57C00), modifier = Modifier.weight(1f))
                OrdenStatChip("$statEntregadas", "Entregadas",  Color(0xFF1565C0), modifier = Modifier.weight(1f))
                OrdenStatChip("$statTerminadas", "Terminadas",  Color(0xFF2E7D32), modifier = Modifier.weight(1f))
                OrdenStatChip("$statAtrasadas",  "Atrasadas",   Color(0xFFC62828), modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            // Chip de filtro
            Surface(
                onClick  = { mostrarFiltros = !mostrarFiltros },
                shape    = RoundedCornerShape(12.dp),
                color    = colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.FilterList, null, tint = colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text  = if (estadoFiltro == "TODO") "Todas las órdenes" else "Estado: $estadoFiltro",
                            style = MaterialTheme.typography.labelMedium,
                            color = colorScheme.primary
                        )
                    }
                    Icon(
                        imageVector = if (mostrarFiltros) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null, tint = colorScheme.onSurfaceVariant
                    )
                }
            }

            if (mostrarFiltros) {
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Filtro estado
                    var expandedEstado by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(onClick = { expandedEstado = true }, modifier = Modifier.fillMaxWidth()) {
                            Text(if (estadoFiltro == "TODO") "Estado" else estadoFiltro, maxLines = 1)
                        }
                        DropdownMenu(expanded = expandedEstado, onDismissRequest = { expandedEstado = false }) {
                            listOf("TODO", "PENDIENTE", "TERMINADO", "ENTREGADO", "CANCELADO").forEach { opt ->
                                DropdownMenuItem(text = { Text(opt) }, onClick = { estadoFiltro = opt; expandedEstado = false })
                            }
                        }
                    }
                    // Filtro campo fecha
                    var expandedCampo by remember { mutableStateOf(false) }
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(onClick = { expandedCampo = true }, modifier = Modifier.fillMaxWidth()) {
                            Text(if (!fechaFiltroActivo) "Fecha" else fechaCampo.lowercase(), maxLines = 1)
                        }
                        DropdownMenu(expanded = expandedCampo, onDismissRequest = { expandedCampo = false }) {
                            DropdownMenuItem(text = { Text("Sin filtro fecha") }, onClick = {
                                fechaFiltroActivo = false; fechaInicio = null; fechaFin = null; expandedCampo = false
                            })
                            listOf("INICIO" to "Fecha inicio", "FIN" to "Fecha fin", "ENTREGA" to "Fecha entrega").forEach { (val_, label) ->
                                DropdownMenuItem(text = { Text(label) }, onClick = {
                                    fechaFiltroActivo = true; fechaCampo = val_; expandedCampo = false
                                })
                            }
                        }
                    }
                }
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
                        ) { Text(fechaInicio?.toString() ?: "Desde", maxLines = 1) }
                        OutlinedButton(
                            onClick  = {
                                val hoy = LocalDate.now()
                                DatePickerDialog(context, { _, y, m, d ->
                                    fechaFin = LocalDate.of(y, m + 1, d)
                                }, hoy.year, hoy.monthValue - 1, hoy.dayOfMonth).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) { Text(fechaFin?.toString() ?: "Hasta", maxLines = 1) }
                    }
                }
                if (estadoFiltro != "TODO" || fechaFiltroActivo) {
                    TextButton(onClick = {
                        estadoFiltro = "TODO"
                        fechaFiltroActivo = false; fechaInicio = null; fechaFin = null
                    }) {
                        Text("Limpiar filtros", color = colorScheme.error)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            if (ordenesFiltradas.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 32.dp), contentAlignment = Alignment.Center) {
                    Text("No hay órdenes con los filtros seleccionados.", color = colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding      = PaddingValues(bottom = 80.dp)
                ) {
                    items(ordenesFiltradas) { orden ->
                        OrdenCard(orden = orden, onClick = { onOpenOrden(orden) }, fechaActual = fechaActual)
                    }
                }
            }
        }
    }
}

@Composable
private fun OrdenCard(
    orden: OrdenEntity,
    onClick: () -> Unit,
    fechaActual: LocalDate
) {
    val colorScheme = MaterialTheme.colorScheme
    val dateFmt     = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val estadoNombre = orden.estado.name
    val (estadoColor, estadoBg) = when {
        estadoNombre == "TERMINADO" || estadoNombre == "ENTREGADO" ->
            Pair(Color(0xFF2E7D32), Color(0xFFE8F5E9))
        estadoNombre == "CANCELADO" ->
            Pair(Color(0xFF757575), Color(0xFFF5F5F5))
        run {
            val f = try { LocalDate.parse(orden.fecha_fin ?: "", dateFmt) } catch (e: Exception) { null }
            f != null && f.isBefore(fechaActual)
        } -> Pair(Color(0xFFC62828), Color(0xFFFFEBEE))
        estadoNombre == "PENDIENTE" -> Pair(Color(0xFFF57C00), Color(0xFFFFF3E0))
        else -> Pair(Color(0xFF1565C0), Color(0xFFE3F2FD))
    }

    val estadoDisplay = estadoNombre

    Card(
        onClick   = onClick,
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(shape = RoundedCornerShape(6.dp), color = colorScheme.surfaceVariant) {
                    Text(
                        text     = "ORD-%03d".format(orden.id_orden),
                        style    = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color    = colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Surface(shape = RoundedCornerShape(6.dp), color = estadoBg) {
                    Text(
                        text     = estadoDisplay,
                        style    = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color    = estadoColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            Text(
                text  = "Cotización #${orden.id_cotizacion}",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(14.dp), tint = colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(4.dp))
                    Text("Cliente #${orden.id_cotizacion}", style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Build, null, modifier = Modifier.size(14.dp), tint = colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(4.dp))
                    Text("Emp. #${orden.id_empleado}", style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant)
                }
            }

            Spacer(Modifier.height(6.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.DateRange, null, modifier = Modifier.size(14.dp), tint = colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(4.dp))
                    Text("Inicio: ${orden.fecha_inicio.take(10)}", style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.DateRange, null, modifier = Modifier.size(14.dp), tint = colorScheme.onSurfaceVariant)
                    Spacer(Modifier.width(4.dp))
                    Text("Entrega: ${orden.fecha_fin?.take(10) ?: "N/A"}", style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun OrdenStatChip(
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = when (label) {
                    "Pendientes" -> Icons.Default.AccessTime
                    "Entregadas" -> Icons.Default.LocalShipping
                    "Terminadas" -> Icons.Default.CheckCircle
                    else         -> Icons.Default.Warning
                },
                contentDescription = label,
                tint     = color,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text  = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
            Text(
                text  = label,
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}
