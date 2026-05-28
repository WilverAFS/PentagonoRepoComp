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
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.DueñoEntity
import com.ingsoftware.pentagono.viewmodel.DueñoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(
    viewModel: DueñoViewModel,
    dueñoActual: DueñoEntity,                 // ✅ dueño autenticado
    onBack: () -> Unit = {},
    onAddDueño: () -> Unit = {},
    onSearchDueño: () -> Unit = {},
    onEditDueño: (DueñoEntity) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val dueños by viewModel.dueños.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title            = "Configuración",
                showBackButton   = true,
                onBackClick      = onBack,
                showSearchAction = true,
                onSearchClick    = onSearchDueño
            )
        },
        floatingActionButton = {
            PentagonoFab(onClick = onAddDueño)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(16.dp)
        ) {
            // ✅ Datos del dueño autenticado
            Text("Dueño Actual", style = MaterialTheme.typography.headlineMedium, color = colorScheme.primary)
            Spacer(Modifier.height(12.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("ID: ${dueñoActual.id_dueño}")
                        Text("Nombre: ${dueñoActual.nombre}")
                        Text("Contraseña: ${"*".repeat(dueñoActual.contraseña.length)}") // ✅ oculta contraseña
                    }
                    IconButton(onClick = { onEditDueño(dueñoActual) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar Dueño Actual")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ✅ Lista de otros dueños
            Text("Otros Dueños", style = MaterialTheme.typography.headlineSmall, color = colorScheme.primary)
            Spacer(Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(dueños.filter { it.id_dueño != dueñoActual.id_dueño }) { dueño -> // ✅ excluye al actual
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("ID: ${dueño.id_dueño}")
                                Text("Nombre: ${dueño.nombre}")
                                Text("Contraseña: ${"*".repeat(dueño.contraseña.length)}")
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
