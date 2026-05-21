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
import com.ingsoftware.pentagono.data.EmpleadoEntity
import com.ingsoftware.pentagono.viewmodel.EmpleadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoEmpleadoScreen(
    viewModel: EmpleadoViewModel,
    onBack: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    var curp by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var numeroExterior by remember { mutableStateOf("") }
    var numeroInterior by remember { mutableStateOf("") }
    var colonia by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Oaxaca") } // default

    // Errores
    var curpError by remember { mutableStateOf(false) }
    var nombreError by remember { mutableStateOf(false) }
    var apellidoPaternoError by remember { mutableStateOf(false) }
    var apellidoMaternoError by remember { mutableStateOf(false) }
    var telefonoError by remember { mutableStateOf(false) }
    var numeroExteriorError by remember { mutableStateOf(false) }
    var correoError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { PentagonoTopBar(title = "Nuevo Empleado", onMenuClick = { onBack() }) }
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
            // CURP
            OutlinedTextField(
                value = curp,
                onValueChange = {
                    curp = it.uppercase()
                    curpError = !(curp.matches(Regex("^[A-Z0-9]{18}$")))
                },
                label = { Text("CURP (18 caracteres)") },
                isError = curpError,
                modifier = Modifier.fillMaxWidth()
            )
            if (curpError) Text("CURP inválida, debe tener 18 caracteres alfanuméricos", color = Color.Red)

            // Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = !it.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
                },
                label = { Text("Nombre") },
                isError = nombreError,
                modifier = Modifier.fillMaxWidth()
            )
            if (nombreError) Text("Solo se permiten letras", color = Color.Red)

            // Apellidos
            OutlinedTextField(
                value = apellidoPaterno,
                onValueChange = {
                    apellidoPaterno = it
                    apellidoPaternoError = apellidoPaterno.isNotBlank() &&
                            !apellidoPaterno.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
                },
                label = { Text("Apellido Paterno") },
                isError = apellidoPaternoError,
                modifier = Modifier.fillMaxWidth()
            )
            if (apellidoPaternoError) Text("Solo se permiten letras", color = Color.Red)

            OutlinedTextField(
                value = apellidoMaterno,
                onValueChange = {
                    apellidoMaterno = it
                    apellidoMaternoError = apellidoMaterno.isNotBlank() &&
                            !apellidoMaterno.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
                },
                label = { Text("Apellido Materno") },
                isError = apellidoMaternoError,
                modifier = Modifier.fillMaxWidth()
            )
            if (apellidoMaternoError) Text("Solo se permiten letras", color = Color.Red)

            // Teléfono
            OutlinedTextField(
                value = telefono,
                onValueChange = {
                    telefono = it
                    val clean = telefono.replace(" ", "").replace("-", "")
                    telefonoError = !(clean.matches(Regex("^[0-9]{10}$")))
                },
                label = { Text("Teléfono (10 dígitos)") },
                isError = telefonoError,
                modifier = Modifier.fillMaxWidth()
            )
            if (telefonoError) Text("Debe contener exactamente 10 dígitos", color = Color.Red)

            // Correo opcional
            OutlinedTextField(
                value = correo,
                onValueChange = {
                    correo = it
                    correoError = correo.isNotBlank() &&
                            !correo.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"))
                },
                label = { Text("Correo electrónico (opcional)") },
                isError = correoError,
                modifier = Modifier.fillMaxWidth()
            )
            if (correoError) Text("Formato de correo inválido", color = Color.Red)

            // Dirección
            OutlinedTextField(value = calle, onValueChange = { calle = it }, label = { Text("Calle") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(
                value = numeroExterior,
                onValueChange = {
                    numeroExterior = it
                    numeroExteriorError = !numeroExterior.matches(Regex("^[0-9]+$"))
                },
                label = { Text("Número Exterior") },
                isError = numeroExteriorError,
                modifier = Modifier.fillMaxWidth()
            )
            if (numeroExteriorError) Text("Debe ser un número", color = Color.Red)

            OutlinedTextField(value = numeroInterior, onValueChange = { numeroInterior = it }, label = { Text("Número Interior (opcional)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = colonia, onValueChange = { colonia = it }, label = { Text("Colonia") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = municipio, onValueChange = { municipio = it }, label = { Text("Municipio") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado") }, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        val cleanCurp = curp.trim()
                        val cleanNombre = nombre.trim()
                        val cleanApellidoPaterno = apellidoPaterno.trim()
                        val cleanApellidoMaterno = apellidoMaterno.trim()
                        val cleanTelefono = telefono.replace(" ", "").replace("-", "").trim()
                        val cleanCorreo = correo.trim()
                        val cleanCalle = calle.trim()
                        val cleanNumeroExterior = numeroExterior.trim()
                        val cleanNumeroInterior = numeroInterior.trim()
                        val cleanColonia = colonia.trim()
                        val cleanMunicipio = municipio.trim()
                        val cleanEstado = estado.trim()

                        if (!curpError && !nombreError && !apellidoPaternoError && !apellidoMaternoError &&
                            !telefonoError && !numeroExteriorError && !correoError &&
                            cleanCurp.isNotBlank() && cleanNombre.isNotBlank() &&
                            cleanApellidoPaterno.isNotBlank() && cleanApellidoMaterno.isNotBlank() &&
                            cleanTelefono.isNotBlank() && cleanCalle.isNotBlank() &&
                            cleanNumeroExterior.isNotBlank() && cleanColonia.isNotBlank() &&
                            cleanMunicipio.isNotBlank() && cleanEstado.isNotBlank()
                        ) {
                            val nuevoEmpleado = EmpleadoEntity(
                                curp = cleanCurp,
                                nombre = cleanNombre,
                                apellidoPaterno = cleanApellidoPaterno,
                                apellidoMaterno = cleanApellidoMaterno,
                                telefono = cleanTelefono,
                                correo = if (cleanCorreo.isBlank()) null else cleanCorreo,
                                calle = cleanCalle,
                                numeroExterior = cleanNumeroExterior.toInt(),
                                numeroInterior = if (cleanNumeroInterior.isBlank()) null else cleanNumeroInterior,
                                colonia = cleanColonia,
                                municipio = cleanMunicipio,
                                estado = cleanEstado
                            )
                            viewModel.addEmpleado(nuevoEmpleado)
                            onBack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Text("Aceptar")
                }
                OutlinedButton(onClick = { onBack() }) { Text("Cancelar") }
            }
        }
    }
}

