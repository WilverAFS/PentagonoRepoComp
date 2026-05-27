package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.DueñoEntity
import com.ingsoftware.pentagono.viewmodel.DueñoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoDueñoScreen(
    viewModel: DueñoViewModel,
    onBack: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    var nombre by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    // Validaciones
    val nombreValido = nombre.isNotBlank() && Validaciones.validarNombre(nombre)
    val contraseñaValida = contraseña.isNotBlank() && contraseña.length >= 4
    val hayErrores = !nombreValido || !contraseñaValida

    Scaffold(
        topBar = { PentagonoTopBar(title = "Nuevo Dueño", onMenuClick = { onMenuClick() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CampoNombre(nombre, { nombre = it }, label = "Nombre", obligatorio = true)

            OutlinedTextField(
                value = contraseña,
                onValueChange = { contraseña = it },
                label = { Text("Contraseña") },
                singleLine = true,
                isError = !contraseñaValida,
                modifier = Modifier.fillMaxWidth()
            )
            if (!contraseñaValida) {
                if (contraseña.isBlank()) Text("Campo obligatorio", color = Color.Red)
                else Text("Debe tener al menos 4 caracteres", color = Color.Red)
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        try {
                            val nuevoDueño = DueñoEntity(
                                nombre = nombre.trim(),
                                contraseña = contraseña.trim()
                            )
                            viewModel.addDueño(nuevoDueño)
                            onBack()
                        } catch (e: Exception) {
                            errorMensaje = "El nombre ya existe, elija otro"
                        }
                    },
                    enabled = !hayErrores,
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Text("Aceptar", color = colorScheme.onSecondary)
                }
                OutlinedButton(onClick = { onBack() }) {
                    Text("Cancelar")
                }
            }

            if (hayErrores) {
                Text("Algunos campos están vacíos o tienen errores", color = colorScheme.error)
            }

            errorMensaje?.let {
                Text(it, color = colorScheme.error)
            }
        }
    }
}
