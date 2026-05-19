package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.EmpleadoEntity
import com.ingsoftware.pentagono.viewmodel.EmpleadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoEmpleadoScreen(
    viewModel: EmpleadoViewModel,
    onBack: () -> Unit = {}
) {
    val empleados by viewModel.empleados.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    val nextId = if (empleados.isEmpty()) 1 else empleados.maxOf { it.id_empleado } + 1

    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var puesto by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf(false) }
    var telefonoError by remember { mutableStateOf(false) }
    var correoError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { PentagonoTopBar(title = "Nuevo Empleado", onMenuClick = { onBack() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().background(colorScheme.background).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("ID asignado: $nextId", style = MaterialTheme.typography.bodyLarge)

            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = !it.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
                },
                label = { Text("Nombre completo") },
                isError = nombreError,
                modifier = Modifier.fillMaxWidth()
            )
            if (nombreError) Text("Solo se permiten letras", color = Color.Red)

            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    telefono = it
                    telefonoError = !it.matches(Regex("^[0-9]+$"))
                },
                label = { Text("Teléfono") },
                isError = telefonoError,
                modifier = Modifier.fillMaxWidth()
            )
            if (telefonoError) Text("Solo se permiten números", color = Color.Red)

            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    correoError = !it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))
                },
                label = { Text("Correo electrónico") },
                isError = correoError,
                modifier = Modifier.fillMaxWidth()
            )
            if (correoError) Text("Formato de correo inválido", color = Color.Red)

            OutlinedTextField(value = puesto, onValueChange = { puesto = it }, label = { Text("Puesto") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = {
                    if (!nombreError && !telefonoError && !correoError &&
                        nombre.isNotBlank() && telefono.isNotBlank() && correo.isNotBlank()
                    ) {
                        val nuevoEmpleado = EmpleadoEntity(nextId, nombre, telefono, correo, puesto, direccion)
                        viewModel.addEmpleado(nuevoEmpleado)
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
