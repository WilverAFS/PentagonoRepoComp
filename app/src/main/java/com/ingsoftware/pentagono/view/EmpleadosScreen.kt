package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.model.Empleado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadosScreen(
    empleados: List<Empleado> = emptyList(),
    onBack: () -> Unit = {},
    onAddEmpleado: () -> Unit = {},
    onSearchEmpleado: () -> Unit = {},
    onEditEmpleado: (Empleado) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Empleados",
                onMenuClick = { onBack() }
            )
        },
        bottomBar = {
            PentagonoBottomBar(
                onSearchClick = { onSearchEmpleado() },
                onAddClick = { onAddEmpleado() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(16.dp)
        ) {
            Text(
                "Listado de Empleados",
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(empleados) { empleado ->
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmpleadosScreenPreviewLight() {
    val empleadosDemo = listOf(
        Empleado(1, "Carlos Pérez", "9511111111", "carlos@mail.com", "Supervisor", "Calle Hidalgo #45"),
        Empleado(2, "Ana López", "9512222222", "ana@mail.com", "Administradora", "Av. Juárez #78")
    )
    MaterialTheme(colorScheme = lightColorScheme()) {
        EmpleadosScreen(empleados = empleadosDemo)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmpleadosScreenPreviewDark() {
    val empleadosDemo = listOf(
        Empleado(1, "Carlos Pérez", "9511111111", "carlos@mail.com", "Supervisor", "Calle Hidalgo #45"),
        Empleado(2, "Ana López", "9512222222", "ana@mail.com", "Administradora", "Av. Juárez #78")
    )
    MaterialTheme(colorScheme = darkColorScheme()) {
        EmpleadosScreen(empleados = empleadosDemo)
    }
}
