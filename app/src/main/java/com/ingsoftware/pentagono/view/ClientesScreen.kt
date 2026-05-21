package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
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
    val clientes by viewModel.clientes.collectAsState()
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
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
                        Column(
                            modifier = Modifier.weight(1f) // ✅ ocupa todo el espacio disponible
                        ) {
                            Text("Teléfono: ${cliente.telefono}", style = MaterialTheme.typography.bodySmall)

                            val nombreCompleto = listOfNotNull(
                                cliente.nombre.takeIf { it.isNotBlank() },
                                cliente.apellidoPaterno?.takeIf { it.isNotBlank() },
                                cliente.apellidoMaterno?.takeIf { it.isNotBlank() }
                            ).joinToString(" ")

                            Text(
                                nombreCompleto,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 2, // ✅ permite salto de línea
                                overflow = TextOverflow.Ellipsis
                            )

                            Text("Correo: ${cliente.correo ?: "-"}",
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                "Dirección: ${cliente.calle} #${cliente.numeroExterior}${cliente.numeroInterior?.let { " Int. $it" } ?: ""}, ${cliente.colonia}, ${cliente.municipio}, ${cliente.estado}",
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 3, // ✅ dirección larga se parte en varias líneas
                                overflow = TextOverflow.Ellipsis
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
