package com.ingsoftware.pentagono.view

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.OrdenEntity
import com.ingsoftware.pentagono.viewmodel.OrdenViewModel
import kotlinx.coroutines.launch
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
    val ordenes by viewModel.ordenes.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateTimeFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    // --- filtros ---
    var fechaFiltroActivo by remember { mutableStateOf(false) }
    var fechaCampo by remember { mutableStateOf("INICIO") } // "INICIO","FIN","ENTREGA"
    var fechaInicio by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFin by remember { mutableStateOf<LocalDate?>(null) }
    var expandedFechaMenu by remember { mutableStateOf(false) }

    // Estados válidos: TODO, PENDIENTE, TERMINADO, ENTREGADO, CANCELADO
    var estadoFiltro by remember { mutableStateOf("TODO") }

    // Reutilizables compactos
    @Composable
    fun SmallFilterButton(label: String, onClick: () -> Unit) {
        TextButton(onClick = onClick) {
            Icon(Icons.Default.FilterList, contentDescription = "Filtro tipo")
            Spacer(Modifier.width(6.dp))
            Text(label)
        }
    }

    @Composable
    fun DateField(label: String, value: LocalDate?, onSelect: (LocalDate) -> Unit) {
        OutlinedTextField(
            value = value?.format(dateFmt) ?: "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    val hoy = LocalDate.now()
                    DatePickerDialog(
                        context,
                        { _, y, m, d -> onSelect(LocalDate.of(y, m + 1, d)) },
                        hoy.year, hoy.monthValue - 1, hoy.dayOfMonth
                    ).show()
                }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Seleccionar fecha")
                }
            }
        )
    }

    // --- filtrado combinado ---
    val ordenesFiltradas by remember(ordenes, fechaFiltroActivo, fechaCampo, fechaInicio, fechaFin, estadoFiltro) {
        derivedStateOf {
            ordenes.filter { o ->
                // seleccionar la fecha correspondiente del registro
                val fechaRaw = when (fechaCampo) {
                    "INICIO" -> o.fecha_inicio
                    "FIN" -> o.fecha_fin
                    "ENTREGA" -> o.fecha_entrega
                    else -> o.fecha_inicio
                }

                val cumpleFecha = try {
                    if (!fechaFiltroActivo) true
                    else {
                        // intentar parsear como LocalDateTime, si falla tomar solo la parte fecha
                        val dt = try {
                            LocalDateTime.parse(fechaRaw ?: "", dateTimeFmt)
                        } catch (e: Exception) {
                            val fechaSolo = (fechaRaw ?: "").split(" ").firstOrNull() ?: ""
                            LocalDate.parse(fechaSolo, dateFmt).atStartOfDay()
                        }
                        val logDate = dt.toLocalDate()
                        val desde = fechaInicio?.let { !logDate.isBefore(it) } ?: true
                        val hasta = fechaFin?.let { !logDate.isAfter(it) } ?: true
                        desde && hasta
                    }
                } catch (e: Exception) {
                    true
                }

                val cumpleEstado = when (estadoFiltro.uppercase()) {
                    "TODO" -> true
                    else -> {
                        // CORRECCIÓN: asegurar que comparamos Strings para usar el parámetro ignoreCase
                        val estadoRegistro = o.estado?.toString() ?: ""
                        estadoRegistro.equals(estadoFiltro, ignoreCase = true)
                    }
                }

                cumpleFecha && cumpleEstado
            }.sortedByDescending { it.id_orden }
        }
    }

    Scaffold(
        topBar = { PentagonoTopBar(title = "Órdenes de Trabajo", onMenuClick = { onMenuClick() }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            PentagonoBottomBar(
                onSearchClick = { onSearchOrden() },
                onAddClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Solo se pueden crear órdenes cuando una cotización es marcada como aceptada")
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Fila de filtros: fecha (izq), selector campo fecha (centro), estado (derecha)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                // Fecha: botón y menú
                SmallFilterButton(
                    label = if (fechaFiltroActivo) "Fecha: activo (${fechaCampo.lowercase()})" else "Fecha: sin filtro"
                ) { expandedFechaMenu = true }

                DropdownMenu(expanded = expandedFechaMenu, onDismissRequest = { expandedFechaMenu = false }) {
                    DropdownMenuItem(text = { Text("Sin filtro de datos") }, onClick = {
                        fechaFiltroActivo = false; fechaInicio = null; fechaFin = null; expandedFechaMenu = false
                    })
                    DropdownMenuItem(text = { Text("Filtrar por fecha inicio") }, onClick = {
                        fechaFiltroActivo = true; fechaCampo = "INICIO"; expandedFechaMenu = false
                    })
                    DropdownMenuItem(text = { Text("Filtrar por fecha fin") }, onClick = {
                        fechaFiltroActivo = true; fechaCampo = "FIN"; expandedFechaMenu = false
                    })
                    DropdownMenuItem(text = { Text("Filtrar por fecha entrega") }, onClick = {
                        fechaFiltroActivo = true; fechaCampo = "ENTREGA"; expandedFechaMenu = false
                    })
                }

                // Estado (derecha) — anclado a la derecha para que el menú no desplace el botón
                Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                    var expandedEstadoLocal by remember { mutableStateOf(false) }
                    SmallFilterButton(label = estadoFiltro) { expandedEstadoLocal = true }

                    DropdownMenu(
                        expanded = expandedEstadoLocal,
                        onDismissRequest = { expandedEstadoLocal = false },
                        modifier = Modifier.wrapContentSize(Alignment.TopEnd)
                    ) {
                        listOf("TODO", "PENDIENTE", "TERMINADO", "ENTREGADO", "CANCELADO").forEach { opt ->
                            DropdownMenuItem(text = { Text(opt) }, onClick = {
                                estadoFiltro = opt
                                expandedEstadoLocal = false
                            })
                        }
                    }
                }
            }

            // Campos de fecha visibles si está activo
            if (fechaFiltroActivo) {
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        DateField(label = "Fecha Inicio", value = fechaInicio) { fechaInicio = it }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        DateField(label = "Fecha Fin", value = fechaFin) { fechaFin = it }
                    }
                }
            }

            if (ordenesFiltradas.isEmpty()) {
                Text("No hay órdenes que cumplan los filtros seleccionados.", color = colorScheme.error, style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(ordenesFiltradas) { orden ->
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = colorScheme.surface)) {
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
}
