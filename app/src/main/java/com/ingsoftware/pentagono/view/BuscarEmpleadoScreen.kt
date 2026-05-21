package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.EmpleadoEntity
import com.ingsoftware.pentagono.viewmodel.EmpleadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarEmpleadoScreen(
    viewModel: EmpleadoViewModel,
    onBack: () -> Unit = {},
    onEditEmpleado: (EmpleadoEntity) -> Unit = {}
) {
    val empleados by viewModel.empleados.collectAsState()
    var query by remember { mutableStateOf("") }
    var resultados by remember { mutableStateOf(listOf<EmpleadoEntity>()) }

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = { PentagonoTopBar(title = "Buscar Empleado", onMenuClick = { onBack() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    resultados = if (query.isBlank()) {
                        emptyList()
                    } else {
                        when {
                            // Si es numérico, buscar por teléfono exacto
                            query.matches(Regex("^[0-9]+$")) -> {
                                empleados.filter { it.telefono == query }
                            }
                            else -> {
                                // Si es texto, buscar por CURP exacto o coincidencias en nombre/apellidos
                                empleados.filter {
                                    it.curp.equals(query, ignoreCase = true) ||
                                            it.nombre.contains(query, ignoreCase = true) ||
                                            it.apellidoPaterno.contains(query, ignoreCase = true) ||
                                            it.apellidoMaterno.contains(query, ignoreCase = true)
                                }
                            }
                        }
                    }
                },
                label = { Text("Buscar por CURP, Nombre o Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (resultados.isEmpty() && query.isNotBlank()) {
                Text("No se encontraron resultados", color = Color.Red)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(resultados) { empleado ->
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
                                Text("CURP (PK): ${empleado.curp}", style = MaterialTheme.typography.bodySmall)
                                Text("${empleado.nombre} ${empleado.apellidoPaterno} ${empleado.apellidoMaterno}", style = MaterialTheme.typography.titleMedium)
                                Text("Tel: ${empleado.telefono}", style = MaterialTheme.typography.bodyMedium)
                                Text("Correo: ${empleado.correo ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                                Text("Dirección: ${empleado.calle} #${empleado.numeroExterior}${empleado.numeroInterior?.let { " Int. $it" } ?: ""}, ${empleado.colonia}, ${empleado.municipio}, ${empleado.estado}", style = MaterialTheme.typography.bodyMedium)
                            }
                            IconButton(onClick = { onEditEmpleado(empleado) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar Empleado")
                            }
                        }
                    }
                }
            }
        }
    }
}
