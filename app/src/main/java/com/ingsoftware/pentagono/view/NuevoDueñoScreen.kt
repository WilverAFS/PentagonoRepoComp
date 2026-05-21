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
    onBack: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    var idDueño by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var contraseña by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf(false) }
    var contraseñaError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { PentagonoTopBar(title = "Nuevo Dueño", onMenuClick = { onBack() }) }
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
            OutlinedTextField(value = idDueño, onValueChange = { idDueño = it }, label = { Text("ID Dueño") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = !nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
                },
                label = { Text("Nombre") },
                isError = nombreError,
                modifier = Modifier.fillMaxWidth()
            )
            if (nombreError) Text("Solo letras permitidas", color = Color.Red)

            OutlinedTextField(
                value = contraseña,
                onValueChange = {
                    contraseña = it
                    contraseñaError = contraseña.length < 4
                },
                label = { Text("Contraseña") },
                isError = contraseñaError,
                modifier = Modifier.fillMaxWidth()
            )
            if (contraseñaError) Text("Debe tener al menos 4 caracteres", color = Color.Red)

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = {
                    val cleanId = idDueño.trim()
                    val cleanNombre = nombre.trim()
                    val cleanContraseña = contraseña.trim()

                    if (!nombreError && !contraseñaError && cleanId.isNotBlank() && cleanNombre.isNotBlank() && cleanContraseña.isNotBlank()) {
                        val nuevoDueño = DueñoEntity(
                            id_dueño = cleanId.toInt(),
                            nombre = cleanNombre,
                            contraseña = cleanContraseña
                        )
                        viewModel.addDueño(nuevoDueño)
                        onBack()
                    }
                }, colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)) {
                    Text("Aceptar")
                }
                OutlinedButton(onClick = { onBack() }) { Text("Cancelar") }
            }
        }
    }
}
