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
import com.ingsoftware.pentagono.data.ClienteEntity
import com.ingsoftware.pentagono.viewmodel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoClienteScreen(
    viewModel: ClienteViewModel,
    onBack: () -> Unit = {},
    onSaveSuccess: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    prefilledTelefono: String = ""
) {
    val colorScheme = MaterialTheme.colorScheme

    var telefono by remember { mutableStateOf(prefilledTelefono) }
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var numeroExterior by remember { mutableStateOf("") }
    var numeroInterior by remember { mutableStateOf("") }
    var colonia by remember { mutableStateOf("") }
    var municipio by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Oaxaca") }
    var correo by remember { mutableStateOf("") }

    // Validaciones
    val telefonoValido = Validaciones.validarTelefono(telefono.replace(" ", "").replace("-", ""))
    val nombreValido = nombre.isNotBlank() && Validaciones.validarNombre(nombre)
    val calleValida = calle.isNotBlank()
    val numeroExteriorValido = numeroExterior.isNotBlank() && Validaciones.validarNumero(numeroExterior)
    val coloniaValida = colonia.isNotBlank()
    val municipioValido = municipio.isNotBlank()
    val estadoValido = estado.isNotBlank()
    val correoValido = correo.isBlank() || Validaciones.validarCorreo(correo)

    val hayErrores = !telefonoValido || !nombreValido || !calleValida || !numeroExteriorValido ||
            !coloniaValida || !municipioValido || !estadoValido || !correoValido

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Nuevo Cliente",
                onMenuClick = { onMenuClick() }
            )
        }
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
            CampoTelefono(telefono, { telefono = it }, obligatorio = true)

            CampoNombre(nombre, { nombre = it }, label = "Nombre", obligatorio = true)

            CampoNombre(apellidoPaterno, { apellidoPaterno = it }, label = "Apellido Paterno (opcional)", obligatorio = false)

            CampoNombre(apellidoMaterno, { apellidoMaterno = it }, label = "Apellido Materno (opcional)", obligatorio = false)

            CampoObligatorio(calle, { calle = it }, label = "Calle")

            CampoNumero(numeroExterior, { numeroExterior = it }, label = "Número Exterior", obligatorio = true)

            OutlinedTextField(
                value = numeroInterior,
                onValueChange = { numeroInterior = it },
                label = { Text("Número Interior (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            CampoObligatorio(colonia, { colonia = it }, label = "Colonia")

            CampoObligatorio(municipio, { municipio = it }, label = "Municipio")

            CampoObligatorio(estado, { estado = it }, label = "Estado")

            CampoCorreo(correo, { correo = it }, obligatorio = false)

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        val nuevoCliente = ClienteEntity(
                            telefono = telefono.trim(),
                            nombre = nombre.trim(),
                            apellidoPaterno = if (apellidoPaterno.isBlank()) null else apellidoPaterno.trim(),
                            apellidoMaterno = if (apellidoMaterno.isBlank()) null else apellidoMaterno.trim(),
                            calle = calle.trim(),
                            numeroExterior = numeroExterior.trim(),
                            numeroInterior = if (numeroInterior.isBlank()) null else numeroInterior.trim(),
                            colonia = colonia.trim(),
                            municipio = municipio.trim(),
                            estado = estado.trim(),
                            correo = if (correo.isBlank()) null else correo.trim()
                        )
                        viewModel.addCliente(nuevoCliente)
                        onSaveSuccess()
                    },
                    enabled = !hayErrores, // ✅ botón deshabilitado si hay errores
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Text("Aceptar")
                }

                OutlinedButton(onClick = { onBack() }) {
                    Text("Cancelar")
                }
            }

            if (hayErrores) {
                Text("Algunos campos están vacíos o tienen errores", color = colorScheme.error)
            }
        }
    }
}
