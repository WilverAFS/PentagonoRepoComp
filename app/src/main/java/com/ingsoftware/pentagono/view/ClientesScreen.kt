package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.model.Cliente

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientesScreen(
    clientes: List<Cliente> = emptyList(),
    onBack: () -> Unit = {},
    onAddCliente: () -> Unit = {},
    onSearchCliente: () -> Unit = {},
    onEditCliente: (Cliente) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Clientes",
                onMenuClick = { onBack() }
            )
        },
        bottomBar = {
            // Barra inferior con botones de acción
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClientesScreenPreviewLight() {
    val clientesDemo = listOf(
        Cliente(1, "María Martínez", "9511234567", "maria@mail.com", "Calle Reforma #123"),
        Cliente(2, "José Torres", "9517654321", "jose@mail.com", "Av. Universidad #456")
    )
    MaterialTheme(colorScheme = lightColorScheme()) {
        ClientesScreen(clientes = clientesDemo)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ClientesScreenPreviewDark() {
    val clientesDemo = listOf(
        Cliente(1, "María Martínez", "9511234567", "maria@mail.com", "Calle Reforma #123"),
        Cliente(2, "José Torres", "9517654321", "jose@mail.com", "Av. Universidad #456")
    )
    MaterialTheme(colorScheme = darkColorScheme()) {
        ClientesScreen(clientes = clientesDemo)
    }
}
