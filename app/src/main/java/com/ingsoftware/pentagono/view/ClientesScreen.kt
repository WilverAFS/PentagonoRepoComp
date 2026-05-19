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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.ClienteEntity
import com.ingsoftware.pentagono.viewmodel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientesScreen(
    viewModel: ClienteViewModel,
    onBack: () -> Unit = {},
    onAddCliente: () -> Unit = {},
    onSearchCliente: () -> Unit = {},
    onEditCliente: (ClienteEntity) -> Unit = {}
) {
    val clientes by viewModel.clientes.collectAsState() // Observamos el flujo de datos

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Clientes",
                onMenuClick = { onBack() }
            )
        },
        bottomBar = {
            PentagonoBottomBar(
                onSearchClick = { onSearchCliente() },
                onAddClick = { onAddCliente() }
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
                "Clientes totales",
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(clientes) { cliente ->
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

