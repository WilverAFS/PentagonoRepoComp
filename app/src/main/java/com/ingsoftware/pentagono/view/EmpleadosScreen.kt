package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadosScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Empleados",
                onMenuClick = { onBack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Nuevo Empleado", style = MaterialTheme.typography.headlineMedium, color = colorScheme.primary)

            var nombre by remember { mutableStateOf("") }
            var telefono by remember { mutableStateOf("") }
            var correo by remember { mutableStateOf("") }
            var puesto by remember { mutableStateOf("") }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = "Teléfono") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Correo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = puesto,
                onValueChange = { puesto = it },
                label = { Text("Puesto") },
                leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = "Puesto") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onSave() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
            ) {
                Text("Guardar Empleado", color = colorScheme.onSecondary)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmpleadosScreenPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        EmpleadosScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmpleadosScreenPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        EmpleadosScreen()
    }
}
