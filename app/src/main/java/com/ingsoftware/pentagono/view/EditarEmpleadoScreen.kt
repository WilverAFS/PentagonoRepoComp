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
fun EditarEmpleadoScreen(
    viewModel: EmpleadoViewModel,
    empleado: EmpleadoEntity,
    onBack: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    var nombre by remember { mutableStateOf(empleado.nombre) }
    var telefono by remember { mutableStateOf(empleado.telefono) }
    var correo by remember { mutableStateOf(empleado.correo) }
    var puesto by remember { mutableStateOf(empleado.puesto) }
    var direccion by remember { mutableStateOf(empleado.direccion) }

    var nombreError by remember { mutableStateOf(false) }
    var telefonoError by remember { mutableStateOf(false) }
    var correoError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { PentagonoTopBar(title = "Editar Empleado", onMenuClick = { onBack() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().background(colorScheme.background).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("ID Empleado: ${empleado.id_empleado}", style = MaterialTheme.typography.bodyLarge)

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
                        val empleadoEditado = empleado.copy(
                            nombre = nombre,
                            telefono = telefono,
                            correo = correo,
                            puesto = puesto,
                            direccion = direccion
                        )
                        viewModel.updateEmpleado(empleadoEditado)
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
