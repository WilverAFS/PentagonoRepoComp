package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.OrdenEntity
import com.ingsoftware.pentagono.data.EmpleadoEntity
import com.ingsoftware.pentagono.model.EstadoOrden
import com.ingsoftware.pentagono.model.Orden.Companion.getSystemDate
import android.app.DatePickerDialog
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun NuevaOrdenScreen(
    idCotizacion: Int,
    dueñoId: Int,
    empleados: List<EmpleadoEntity>,
    onSaveOrden: (OrdenEntity) -> Unit,
    onBack: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current

    var empleadoSeleccionado by remember { mutableStateOf<EmpleadoEntity?>(null) }
    var fechaFin by remember { mutableStateOf("") }

    // Validaciones
    val empleadoValido = empleadoSeleccionado != null
    val fechaValida = fechaFin.isNotBlank() && Validaciones.validarFecha(fechaFin)
    val hayErrores = !empleadoValido || !fechaValida

    Scaffold(
        topBar = { PentagonoTopBar(title = "Nueva Orden de Trabajo", onMenuClick = { onMenuClick() }) }
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
            Text("Cotización asociada: $idCotizacion", style = MaterialTheme.typography.bodyLarge)
            Text("Dueño: $dueñoId", style = MaterialTheme.typography.bodyLarge)

            // ✅ Selección de empleado
            var expanded by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = empleadoSeleccionado?.nombre ?: "",
                onValueChange = {},
                label = { Text("Empleado asignado") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Seleccionar empleado")
                    }
                },
                isError = !empleadoValido
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                empleados.forEach { empleado ->
                    DropdownMenuItem(
                        text = { Text(empleado.nombre) },
                        onClick = {
                            empleadoSeleccionado = empleado
                            expanded = false
                        }
                    )
                }
            }
            if (!empleadoValido) Text("Debe seleccionar un empleado", color = colorScheme.error)

            // ✅ Selector de fecha con calendario
            OutlinedTextField(
                value = fechaFin,
                onValueChange = {},
                label = { Text("Fecha de finalización") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = {
                        val calendario = Calendar.getInstance()
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                // Guardamos la fecha en formato YYYY-MM-DD
                                fechaFin = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
                            },
                            calendario.get(Calendar.YEAR),
                            calendario.get(Calendar.MONTH),
                            calendario.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }) {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "Seleccionar fecha")
                    }
                },
                isError = !fechaValida
            )
            if (!fechaValida) Text("Debe seleccionar una fecha válida", color = colorScheme.error)

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        val nuevaOrden = OrdenEntity(
                            id_orden = idCotizacion,
                            id_cotizacion = idCotizacion,
                            id_empleado = empleadoSeleccionado!!.id_empleado,
                            id_dueño = dueñoId,
                            fecha_inicio = getSystemDate(),
                            fecha_fin = fechaFin,
                            estado = EstadoOrden.PENDIENTE,
                            fecha_entrega = null
                        )
                        onSaveOrden(nuevaOrden)
                        onBack()
                    },
                    enabled = !hayErrores,
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Text("Guardar Orden")
                }
                OutlinedButton(onClick = { onBack() }) { Text("Cancelar") }
            }

            if (hayErrores) {
                Text("Debe completar todos los campos correctamente", color = colorScheme.error)
            }
        }
    }
}
