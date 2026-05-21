package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.EmpleadoEntity
import com.ingsoftware.pentagono.viewmodel.EmpleadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadosScreen(
    viewModel: EmpleadoViewModel,
    onBack: () -> Unit = {},
    onAddEmpleado: () -> Unit = {},
    onSearchEmpleado: () -> Unit = {},
    onEditEmpleado: (EmpleadoEntity) -> Unit = {}
) {
    val empleados by viewModel.empleados.collectAsState()

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
                            Column(
                                modifier = Modifier.weight(1f) // ✅ ocupa todo el espacio disponible
                            ) {
                                Text(
                                    "CURP (PK): ${empleado.curp}",
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                val nombreCompleto = listOfNotNull(
                                    empleado.nombre.takeIf { it.isNotBlank() },
                                    empleado.apellidoPaterno.takeIf { it.isNotBlank() },
                                    empleado.apellidoMaterno.takeIf { it.isNotBlank() }
                                ).joinToString(" ")

                                Text(
                                    nombreCompleto,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 2, // ✅ permite salto de línea
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    "Tel: ${empleado.telefono}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    "Correo: ${empleado.correo ?: "-"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    "Dirección: ${empleado.calle} #${empleado.numeroExterior}${empleado.numeroInterior?.let { " Int. $it" } ?: ""}, ${empleado.colonia}, ${empleado.municipio}, ${empleado.estado}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 3, // ✅ dirección larga se parte en varias líneas
                                    overflow = TextOverflow.Ellipsis
                                )
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
