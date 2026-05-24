package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.EmpleadoEntity
import com.ingsoftware.pentagono.viewmodel.EmpleadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoEmpleadoScreen(
    viewModel: EmpleadoViewModel,
    onBack: () -> Unit = {},
    onMenuClick: () -> Unit = {}
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
    var estado by remember { mutableStateOf("Oaxaca") }

    // Validaciones
    val curpValida = curp.isNotBlank() && Validaciones.validarCurp(curp)
    val nombreValido = nombre.isNotBlank() && Validaciones.validarNombre(nombre)
    val apellidoPaternoValido = apellidoPaterno.isNotBlank() && Validaciones.validarNombre(apellidoPaterno)
    val apellidoMaternoValido = apellidoMaterno.isNotBlank() && Validaciones.validarNombre(apellidoMaterno)
    val telefonoValido = Validaciones.validarTelefono(telefono.replace(" ", "").replace("-", ""))
    val correoValido = correo.isBlank() || Validaciones.validarCorreo(correo)
    val calleValida = calle.isNotBlank()
    val numeroExteriorValido = numeroExterior.isNotBlank() && Validaciones.validarNumero(numeroExterior)
    val coloniaValida = colonia.isNotBlank()
    val municipioValido = municipio.isNotBlank()
    val estadoValido = estado.isNotBlank()

    val hayErrores = !curpValida || !nombreValido || !apellidoPaternoValido || !apellidoMaternoValido ||
            !telefonoValido || !correoValido || !calleValida || !numeroExteriorValido ||
            !coloniaValida || !municipioValido || !estadoValido

    Scaffold(
        topBar = { PentagonoTopBar(title = "Nuevo Empleado", onMenuClick = { onMenuClick() }) }
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
            CampoCurp(curp, { curp = it }, obligatorio = true)

            CampoNombre(nombre, { nombre = it }, label = "Nombre", obligatorio = true)

            CampoNombre(apellidoPaterno, { apellidoPaterno = it }, label = "Apellido Paterno", obligatorio = true)

            CampoNombre(apellidoMaterno, { apellidoMaterno = it }, label = "Apellido Materno", obligatorio = true)

            CampoTelefono(telefono, { telefono = it }, obligatorio = true)

            CampoCorreo(correo, { correo = it }, obligatorio = false)

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

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        val nuevoEmpleado = EmpleadoEntity(
                            curp = curp.trim(),
                            nombre = nombre.trim(),
                            apellidoPaterno = apellidoPaterno.trim(),
                            apellidoMaterno = apellidoMaterno.trim(),
                            telefono = telefono.trim(),
                            correo = if (correo.isBlank()) null else correo.trim(),
                            calle = calle.trim(),
                            numeroExterior = numeroExterior.trim().toInt(),
                            numeroInterior = if (numeroInterior.isBlank()) null else numeroInterior.trim(),
                            colonia = colonia.trim(),
                            municipio = municipio.trim(),
                            estado = estado.trim()
                        )
                        viewModel.addEmpleado(nuevoEmpleado)
                        onBack()
                    },
                    enabled = !hayErrores, // ✅ botón deshabilitado si hay errores
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Text("Aceptar")
                }
                OutlinedButton(onClick = { onBack() }) { Text("Cancelar") }
            }

            if (hayErrores) {
                Text("Algunos campos están vacíos o tienen errores", color = colorScheme.error)
            }
        }
    }
}
