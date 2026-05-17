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
import com.ingsoftware.pentagono.model.Dueño

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionScreen(
    dueñoActual: Dueño,
    dueños: List<Dueño> = emptyList(),
    onBack: () -> Unit = {},
    onAddDueño: () -> Unit = {},
    onSearchDueño: () -> Unit = {},
    onEditDueño: (Dueño) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Configuración",
                onMenuClick = { onBack() }
            )
        },
        bottomBar = {
            PentagonoBottomBar(
                onSearchClick = { onSearchDueño() },
                onAddClick = { onAddDueño() }
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
            // Datos del dueño actual
            Text(
                "Dueño Actual",
                style = MaterialTheme.typography.headlineMedium,
                color = colorScheme.primary
            )

            Spacer(Modifier.height(12.dp))

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
                        Text("ID: ${dueñoActual.id_dueño}", style = MaterialTheme.typography.bodySmall)
                        Text("Nombre: ${dueñoActual.nombre}", style = MaterialTheme.typography.titleMedium)
                        Text("Contraseña: ${"*".repeat(dueñoActual.contraseña.length)}", style = MaterialTheme.typography.bodyMedium)
                    }
                    IconButton(onClick = { onEditDueño(dueñoActual) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar Dueño Actual")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Otros Dueños",
                style = MaterialTheme.typography.headlineSmall,
                color = colorScheme.primary
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dueños) { dueño ->
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
                                Text("ID: ${dueño.id_dueño}", style = MaterialTheme.typography.bodySmall)
                                Text("Nombre: ${dueño.nombre}", style = MaterialTheme.typography.titleMedium)
                                Text("Contraseña: ${"*".repeat(dueño.contraseña.length)}", style = MaterialTheme.typography.bodyMedium)
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfiguracionScreenPreviewLight() {
    val dueñoActual = Dueño(1, "Administrador", "admin123")
    val dueñosDemo = listOf(
        Dueño(2, "Carlos", "pass123"),
        Dueño(3, "Ana", "clave456")
    )
    MaterialTheme(colorScheme = lightColorScheme()) {
        ConfiguracionScreen(dueñoActual = dueñoActual, dueños = dueñosDemo)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ConfiguracionScreenPreviewDark() {
    val dueñoActual = Dueño(1, "Administrador", "admin123")
    val dueñosDemo = listOf(
        Dueño(2, "Carlos", "pass123"),
        Dueño(3, "Ana", "clave456")
    )
    MaterialTheme(colorScheme = darkColorScheme()) {
        ConfiguracionScreen(dueñoActual = dueñoActual, dueños = dueñosDemo)
    }
}
