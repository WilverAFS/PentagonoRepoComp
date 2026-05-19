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

    Scaffold(topBar = { PentagonoTopBar(title = "Buscar Empleado", onMenuClick = { onBack() }) }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().background(colorScheme.background).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    resultados = if (query.isBlank()) emptyList() else {
                        when {
                            query.matches(Regex("^[0-9]+$")) -> {
                                empleados.filter { it.id_empleado.toString() == query || it.telefono == query }
                            }
                            else -> {
                                empleados.filter { it.nombre.contains(query, ignoreCase = true) }
                            }
                        }
                    }
                },
                label = { Text("Buscar por ID, Nombre o Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (resultados.isEmpty() && query.isNotBlank()) {
                Text("No se encontraron resultados", color = Color.Red)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(resultados) { empleado ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = colorScheme.surface)) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("ID: ${empleado.id_empleado}", style = MaterialTheme.typography.bodySmall)
                                Text(empleado.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("Tel: ${empleado.telefono}", style = MaterialTheme.typography.bodyMedium)
                                Text("Correo: ${empleado.correo}", style = MaterialTheme.typography.bodyMedium)
                                Text("Puesto: ${empleado.puesto}", style = MaterialTheme.typography.bodyMedium)
                                Text("Dir: ${empleado.direccion}", style = MaterialTheme.typography.bodyMedium)
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
