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
import com.ingsoftware.pentagono.data.ClienteEntity
import com.ingsoftware.pentagono.viewmodel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarClienteScreen(
    viewModel: ClienteViewModel,
    onBack: () -> Unit = {},
    onEditCliente: (ClienteEntity) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val clientes by viewModel.clientes.collectAsState()
    var query by remember { mutableStateOf("") }
    var resultados by remember { mutableStateOf(listOf<ClienteEntity>()) }

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Buscar Cliente",
                onMenuClick = { onMenuClick() }
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
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it.trim()
                    resultados = if (query.isBlank()) {
                        emptyList()
                    } else {
                        when {
                            query.matches(Regex("^\\d{10}$")) -> {
                                // Teléfono exacto
                                clientes.filter { it.telefono == query }
                            }
                            query.contains("@") -> {
                                // Correo
                                clientes.filter { (it.correo ?: "").contains(query, ignoreCase = true) }
                            }
                            else -> {
                                // Nombre o apellidos
                                clientes.filter {
                                    it.nombre.contains(query, ignoreCase = true) ||
                                            (it.apellidoPaterno?.contains(query, ignoreCase = true) ?: false) ||
                                            (it.apellidoMaterno?.contains(query, ignoreCase = true) ?: false)
                                }
                            }
                        }
                    }
                },
                label = { Text("Buscar por Teléfono, Nombre o Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (resultados.isEmpty() && query.isNotBlank()) {
                Text("No se encontraron resultados", color = Color.Red)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(resultados) { cliente ->
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
                                Text("Teléfono: ${cliente.telefono}", style = MaterialTheme.typography.bodySmall)

                                val nombreCompleto = listOfNotNull(
                                    cliente.nombre.takeIf { it.isNotBlank() },
                                    cliente.apellidoPaterno?.takeIf { it.isNotBlank() },
                                    cliente.apellidoMaterno?.takeIf { it.isNotBlank() }
                                ).joinToString(" ")

                                Text(nombreCompleto, style = MaterialTheme.typography.titleMedium)
                                Text("Correo: ${cliente.correo ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    "Dirección: ${cliente.calle} #${cliente.numeroExterior}${cliente.numeroInterior?.let { " Int. $it" } ?: ""}, ${cliente.colonia}, ${cliente.municipio}, ${cliente.estado}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            IconButton(onClick = { onEditCliente(cliente) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar Cliente")
                            }
                        }
                    }
                }
            }
        }
    }
}
