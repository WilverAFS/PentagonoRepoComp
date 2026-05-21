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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.DueñoEntity
import com.ingsoftware.pentagono.viewmodel.DueñoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarDueñoScreen(
    viewModel: DueñoViewModel,
    onBack: () -> Unit = {},
    onEditDueño: (DueñoEntity) -> Unit = {}
) {
    val dueños by viewModel.dueños.collectAsState()
    var query by remember { mutableStateOf("") }
    var resultados by remember { mutableStateOf(listOf<DueñoEntity>()) }

    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = { PentagonoTopBar(title = "Buscar Dueño", onMenuClick = { onBack() }) }
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
                            // Si es numérico, buscar por ID exacto
                            query.matches(Regex("^[0-9]+$")) -> {
                                dueños.filter { it.id_dueño.toString() == query }
                            }
                            else -> {
                                // Si es texto, buscar coincidencias en nombre
                                dueños.filter {
                                    it.nombre.contains(query, ignoreCase = true)
                                }
                            }
                        }
                    }
                },
                label = { Text("Buscar por ID o Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (resultados.isEmpty() && query.isNotBlank()) {
                Text("No se encontraron resultados", color = Color.Red)
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(resultados) { dueño ->
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
                            Column(modifier = Modifier.weight(1f)) {
                                Text("ID: ${dueño.id_dueño}", style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)

                                Text(
                                    dueño.nombre,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    "Contraseña: ${"*".repeat(dueño.contraseña.length)}", // ✅ oculta contraseña
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(onClick = { onEditDueño(dueño) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar Dueño")
                            }
                        }
                    }
                }
            }
        }
    }
}
