package com.ingsoftware.pentagono.view

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // filtros
    var fechaFiltroActivo by remember { mutableStateOf(false) }
    var fechaInicio by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFin by remember { mutableStateOf<LocalDate?>(null) }

    var estadoPago by remember { mutableStateOf("TODO") }
    var estadoCot by remember { mutableStateOf("TODO") }

    // helpers reutilizables
    @Composable
    fun SmallDropdownButton(label: String, iconDesc: String = "Filtro", content: @Composable ColumnScope.() -> Unit) {
        var expanded by remember { mutableStateOf(false) }
        Box {
            TextButton(onClick = { expanded = true }) {
                Icon(Icons.Default.FilterList, contentDescription = iconDesc)
                Spacer(Modifier.width(6.dp))
                Text(label)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Column { content() }
            }
        }
    }

    @Composable
    fun DatePickerField(value: LocalDate?, label: String, onDateSelected: (LocalDate) -> Unit) {
        OutlinedTextField(
            value = value?.format(dateFmt) ?: "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    val today = LocalDate.now()
                    DatePickerDialog(
                        context,
                        { _, y, m, d -> onDateSelected(LocalDate.of(y, m + 1, d)) },
                        today.year, today.monthValue - 1, today.dayOfMonth
                    ).show()
                }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Seleccionar fecha")
                }
            }
        )
    }

    // filtrado combinado
    val cotizacionesFiltradas = remember(cotizaciones, fechaFiltroActivo, fechaInicio, fechaFin, estadoPago, estadoCot) {
        cotizaciones.filter { c ->
            val cumpleFecha = try {
                if (!fechaFiltroActivo) true
                else {
                    val fechaSolo = c.fecha.split(" ")[0]
                    val fechaLog = LocalDate.parse(fechaSolo, dateFmt)
                    val desde = fechaInicio?.let { !fechaLog.isBefore(it) } ?: true
                    val hasta = fechaFin?.let { !fechaLog.isAfter(it) } ?: true
                    desde && hasta
                }
            } catch (e: Exception) { true }

            val cumplePago = when (estadoPago.uppercase()) {
                "TODO" -> true
                else -> c.estado_pago.equals(estadoPago, ignoreCase = true)
            }

            val cumpleCot = when (estadoCot.uppercase()) {
                "TODO" -> true
                else -> c.estado_cotizacion.equals(estadoCot, ignoreCase = true)
            }

            cumpleFecha && cumplePago && cumpleCot
        }.sortedByDescending { it.id_cotizacion }
    }

    Scaffold(
        topBar = { PentagonoTopBar(title = "Cotizaciones", onMenuClick = { onMenuClick() }) },
        bottomBar = {
            PentagonoBottomBar(onSearchClick = { onSearchCotizacion() }, onAddClick = { onAddCotizacion() })
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
            // fila de filtros: fecha (izq), estadoPago (centro), estadoCot (der)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                // Fecha
                SmallDropdownButton(
                    label = if (fechaFiltroActivo) "Fecha: activo" else "Fecha: sin filtro",
                    iconDesc = "Filtro fecha"
                ) {
                    DropdownMenuItem(text = { Text("Sin filtro de fecha") }, onClick = {
                        fechaFiltroActivo = false; fechaInicio = null; fechaFin = null
                    })
                    DropdownMenuItem(text = { Text("Aplicar filtro de fecha") }, onClick = {
                        fechaFiltroActivo = true
                    })
                }

                // Estado Pago (centro)
                SmallDropdownButton(label = estadoPago, iconDesc = "Filtro estado pago") {
                    listOf("TODO", "pendiente", "anticipo", "completado", "cancelado").forEach { opt ->
                        DropdownMenuItem(text = { Text(opt) }, onClick = { estadoPago = opt })
                    }
                }

                // Estado Cotizacion (derecha)
                SmallDropdownButton(label = estadoCot, iconDesc = "Filtro estado cotizacion") {
                    listOf("TODO", "pendiente", "aceptado", "rechazado").forEach { opt ->
                        DropdownMenuItem(text = { Text(opt) }, onClick = { estadoCot = opt })
                    }
                }
            }

            // campos de fecha visibles si se activa filtro fecha
            if (fechaFiltroActivo) {
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        DatePickerField(value = fechaInicio, label = "Fecha Inicio") { fechaInicio = it }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        DatePickerField(value = fechaFin, label = "Fecha Fin") { fechaFin = it }
                    }
                }
            }

            // título y lista / mensaje
            //Text("Listado de Cotizaciones", style = MaterialTheme.typography.headlineSmall, color = colorScheme.primary)
            //Spacer(Modifier.height(8.dp))

            if (cotizacionesFiltradas.isEmpty()) {
                Text("No hay cotizaciones que cumplan los filtros seleccionados.", color = colorScheme.error)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(cotizacionesFiltradas) { cotizacion ->
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = colorScheme.surface)) {
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
}
