package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdenesScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Órdenes de Trabajo",
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
            Text("Nueva Orden", style = MaterialTheme.typography.headlineMedium, color = colorScheme.primary)

            var cliente by remember { mutableStateOf("") }
            var trabajo by remember { mutableStateOf("") }
            var responsable by remember { mutableStateOf("") }

            OutlinedTextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = { Text("Cliente") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = trabajo,
                onValueChange = { trabajo = it },
                label = { Text("Trabajo a realizar") },
                leadingIcon = { Icon(Icons.Filled.Work, contentDescription = "Trabajo") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = responsable,
                onValueChange = { responsable = it },
                label = { Text("Responsable") },
                leadingIcon = { Icon(Icons.Filled.Assignment, contentDescription = "Responsable") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onSave() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
            ) {
                Text("Guardar Orden", color = colorScheme.onSecondary)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrdenesScreenPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        OrdenesScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrdenesScreenPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        OrdenesScreen()
    }
}
