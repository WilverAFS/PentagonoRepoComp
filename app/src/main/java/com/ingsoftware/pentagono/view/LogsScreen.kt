package com.ingsoftware.pentagono.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.viewmodel.LogViewModel
import com.ingsoftware.pentagono.model.TipoLog
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogsScreen(
    viewModel: LogViewModel,
    onBack: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val logs by viewModel.logs.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    // Filtros
    var filtroSeleccionado by remember { mutableStateOf("TODO") }
    var expandedTipo by remember { mutableStateOf(false) }

    // Modo de filtro de fecha: NONE, DATE, DATETIME
    var filtroFechaModo by remember { mutableStateOf("NONE") }
    var expandedFecha by remember { mutableStateOf(false) }

    var fechaInicio by remember { mutableStateOf<LocalDate?>(null) }
    var fechaFin by remember { mutableStateOf<LocalDate?>(null) }
    var horaInicio by remember { mutableStateOf<LocalTime?>(null) }
    var horaFin by remember { mutableStateOf<LocalTime?>(null) }

    // Filtrado combinado: tipo + fecha/fecha-hora
    val logsFiltrados = logs.filter { log ->
        val cumpleTipo = when (filtroSeleccionado) {
            "TODO" -> true
            "ACCESS" -> log.tipo == TipoLog.ACCESS
            "UPDATE" -> log.tipo == TipoLog.UPDATE
            "ADD" -> log.tipo == TipoLog.ADD
            else -> true
        }

        val cumpleFecha = try {
            // El campo log.fecha puede contener "yyyy-MM-dd" o "yyyy-MM-dd HH:mm:ss"
            // Intentamos parsear como LocalDateTime; si falla, extraemos la parte de fecha.
            val logDateTime = try {
                LocalDateTime.parse(log.fecha, dateTimeFormatter)
            } catch (e: Exception) {
                // Si solo tiene fecha (sin hora), parsear como LocalDate y convertir a LocalDateTime a medianoche
                val fechaSolo = log.fecha.split(" ")[0]
                LocalDate.parse(fechaSolo, dateFormatter).atStartOfDay()
            }

            when (filtroFechaModo) {
                "DATE" -> {
                    val logDate = logDateTime.toLocalDate()
                    val desde = fechaInicio?.let { !logDate.isBefore(it) } ?: true
                    val hasta = fechaFin?.let { !logDate.isAfter(it) } ?: true
                    desde && hasta
                }
                "DATETIME" -> {
                    val desde = if (fechaInicio != null && horaInicio != null) {
                        val desdeDateTime = LocalDateTime.of(fechaInicio, horaInicio)
                        !logDateTime.isBefore(desdeDateTime)
                    } else true
                    val hasta = if (fechaFin != null && horaFin != null) {
                        val hastaDateTime = LocalDateTime.of(fechaFin, horaFin)
                        !logDateTime.isAfter(hastaDateTime)
                    } else true
                    desde && hasta
                }
                else -> true
            }
        } catch (e: Exception) {
            true
        }

        cumpleTipo && cumpleFecha
    }.sortedByDescending { it.id_log }

    Scaffold(
        topBar = { PentagonoTopBar(title = "Logs del Sistema", onMenuClick = { onMenuClick() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(16.dp)
        ) {
            // Fila de filtros: fecha (izq) y tipo (der)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Filtro de fecha con 3 opciones
                Box {
                    TextButton(onClick = { expandedFecha = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtro Fecha")
                        Spacer(Modifier.width(8.dp))
                        Text(
                            when (filtroFechaModo) {
                                "DATE" -> "Filtro: Fecha"
                                "DATETIME" -> "Filtro: Fecha y Hora"
                                else -> "Sin filtro de fecha"
                            }
                        )
                    }
                    DropdownMenu(
                        expanded = expandedFecha,
                        onDismissRequest = { expandedFecha = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sin filtro de fecha") },
                            onClick = {
                                filtroFechaModo = "NONE"
                                fechaInicio = null
                                fechaFin = null
                                horaInicio = null
                                horaFin = null
                                expandedFecha = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Aplicar filtro de fecha") },
                            onClick = {
                                filtroFechaModo = "DATE"
                                // limpiar horas si existían
                                horaInicio = null
                                horaFin = null
                                expandedFecha = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Aplicar filtro de fecha y hora") },
                            onClick = {
                                filtroFechaModo = "DATETIME"
                                expandedFecha = false
                            }
                        )
                    }
                }

                // Filtro por tipo
                Box {
                    TextButton(onClick = { expandedTipo = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtro Tipo")
                        Spacer(Modifier.width(8.dp))
                        Text(filtroSeleccionado)
                    }
                    DropdownMenu(
                        expanded = expandedTipo,
                        onDismissRequest = { expandedTipo = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("TODO") },
                            onClick = {
                                filtroSeleccionado = "TODO"
                                expandedTipo = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("ACCESS") },
                            onClick = {
                                filtroSeleccionado = "ACCESS"
                                expandedTipo = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("UPDATE") },
                            onClick = {
                                filtroSeleccionado = "UPDATE"
                                expandedTipo = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("ADD") },
                            onClick = {
                                filtroSeleccionado = "ADD"
                                expandedTipo = false
                            }
                        )
                    }
                }
            }

            // Si se aplica filtro por fecha o fecha+hora, mostrar campos de fecha
            if (filtroFechaModo == "DATE" || filtroFechaModo == "DATETIME") {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = fechaInicio?.format(dateFormatter) ?: "",
                        onValueChange = {},
                        label = { Text("Fecha Inicio") },
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(onClick = {
                                val hoy = LocalDate.now()
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        fechaInicio = LocalDate.of(year, month + 1, dayOfMonth)
                                    },
                                    hoy.year, hoy.monthValue - 1, hoy.dayOfMonth
                                ).show()
                            }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Seleccionar fecha inicio")
                            }
                        }
                    )

                    OutlinedTextField(
                        value = fechaFin?.format(dateFormatter) ?: "",
                        onValueChange = {},
                        label = { Text("Fecha Fin") },
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(onClick = {
                                val hoy = LocalDate.now()
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        fechaFin = LocalDate.of(year, month + 1, dayOfMonth)
                                    },
                                    hoy.year, hoy.monthValue - 1, hoy.dayOfMonth
                                ).show()
                            }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Seleccionar fecha fin")
                            }
                        }
                    )
                }
            }

            // Si se aplica filtro por fecha y hora, mostrar campos de hora
            if (filtroFechaModo == "DATETIME") {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = horaInicio?.toString() ?: "",
                        onValueChange = {},
                        label = { Text("Hora Inicio (HH:mm)") },
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(onClick = {
                                val ahora = LocalTime.now()
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        horaInicio = LocalTime.of(hour, minute)
                                    },
                                    ahora.hour, ahora.minute, true
                                ).show()
                            }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Seleccionar hora inicio")
                            }
                        }
                    )

                    OutlinedTextField(
                        value = horaFin?.toString() ?: "",
                        onValueChange = {},
                        label = { Text("Hora Fin (HH:mm)") },
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            IconButton(onClick = {
                                val ahora = LocalTime.now()
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        horaFin = LocalTime.of(hour, minute)
                                    },
                                    ahora.hour, ahora.minute, true
                                ).show()
                            }) {
                                Icon(Icons.Default.FilterList, contentDescription = "Seleccionar hora fin")
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Mostrar resultado o mensaje claro si no hay coincidencias
            if (logsFiltrados.isEmpty()) {
                Text(
                    "No hay datos que cumplan los filtros seleccionados.",
                    color = colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(logsFiltrados) { log ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("{#${log.id_log}, Dueño ${log.id_dueño}, ${log.fecha}}")
                                Text("${log.tipo}: ${log.descripcion}")
                            }
                        }
                    }
                }
            }
        }
    }
}
