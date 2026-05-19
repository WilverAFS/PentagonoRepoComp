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
    onEditCliente: (ClienteEntity) -> Unit = {}
) {
    val clientes by viewModel.clientes.collectAsState()
    var query by remember { mutableStateOf("") }
    var resultados by remember { mutableStateOf(listOf<ClienteEntity>()) }

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Buscar Cliente",
                onMenuClick = { onBack() }
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
                    query = it
                    resultados = if (query.isBlank()) {
                        emptyList()
                    } else {
                        when {
                            query.matches(Regex("^[0-9]+$")) -> {
                                // Si es número, buscar por ID exacto o teléfono exacto
                                clientes.filter {
                                    it.id_cliente.toString() == query || it.telefono == query
                                }
                            }
                            else -> {
                                // Si es texto, buscar coincidencias en nombre
                                clientes.filter {
                                    it.nombre.contains(query, ignoreCase = true)
                                }
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
                                Text("ID: ${cliente.id_cliente}", style = MaterialTheme.typography.bodySmall)
                                Text(cliente.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("Tel: ${cliente.telefono}", style = MaterialTheme.typography.bodyMedium)
                                Text("Correo: ${cliente.correo}", style = MaterialTheme.typography.bodyMedium)
                                Text("Dir: ${cliente.direccion}", style = MaterialTheme.typography.bodyMedium)
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
