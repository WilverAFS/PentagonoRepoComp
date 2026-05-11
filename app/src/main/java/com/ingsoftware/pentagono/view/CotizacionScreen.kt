package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CotizacionScreen(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Cotizaciones",
                onMenuClick = { onBack() } // vuelve a la vista anterior
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
            Text("Nueva Cotización", style = MaterialTheme.typography.headlineMedium, color = colorScheme.primary)

            // Campos de formulario
            var cliente by remember { mutableStateOf(TextFieldValue("")) }
            var descripcion by remember { mutableStateOf(TextFieldValue("")) }
            var precio by remember { mutableStateOf(TextFieldValue("")) }

            OutlinedTextField(
                value = cliente,
                onValueChange = { cliente = it },
                label = { Text("Cliente") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Cliente") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción del trabajo") },
                leadingIcon = { Icon(Icons.Filled.Description, contentDescription = "Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio estimado") },
                leadingIcon = { Icon(Icons.Filled.PriceChange, contentDescription = "Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { onSave() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
            ) {
                Text("Guardar Cotización", color = colorScheme.onSecondary)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CotizacionScreenPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        CotizacionScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CotizacionScreenPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        CotizacionScreen()
    }
}
