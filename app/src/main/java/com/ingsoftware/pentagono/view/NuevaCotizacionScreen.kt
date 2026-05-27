package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions   // correcto para KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction          // correcto
import androidx.compose.ui.text.input.KeyboardType      // correcto
import com.ingsoftware.pentagono.data.CotizacionEntity
import com.ingsoftware.pentagono.viewmodel.CotizacionViewModel
import com.ingsoftware.pentagono.viewmodel.ClienteViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaCotizacionScreen(
    viewModel: CotizacionViewModel,
    clienteViewModel: ClienteViewModel,
    onSaveSuccess: () -> Unit = {},
    onAddCliente: (String) -> Unit = {},
    onMenuClick: () -> Unit = {},
    onCancel: () -> Unit = {},   // ✅ callback para cancelar
    prefilledTelefono: String = ""
) {
    val colorScheme = MaterialTheme.colorScheme
    var telefono by remember { mutableStateOf(prefilledTelefono) }
    var concepto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // ✅ Validaciones
    val montoRegex = Regex("^[0-9]{1,3}(,?[0-9]{3})*(\\.[0-9]{1,2})?$")
    val telefonoValido = Validaciones.validarTelefono(telefono.replace(" ", "").replace("-", ""))
    val conceptoValido = concepto.isNotBlank()
    val descripcionValido = descripcion.isNotBlank()
    val montoValido = monto.isNotBlank() &&
            montoRegex.matches(monto) &&
            monto.replace(",", "").toDoubleOrNull() != null &&
            monto.replace(",", "").toDouble() >= 0

    val hayErrores = !telefonoValido || !conceptoValido || !descripcionValido || !montoValido

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Nueva Cotización",
                onMenuClick = { onMenuClick() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CampoTelefono(telefono, { telefono = it }, obligatorio = true)
            CampoObligatorio(concepto, { concepto = it }, label = "Concepto")

            // ✅ Descripción multilinea
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción de materiales") },
                modifier = Modifier.fillMaxWidth(),
                isError = !descripcionValido,
                maxLines = 5
            )
            if (!descripcionValido) {
                Text("Campo obligatorio", color = colorScheme.error)
            }

            // ✅ Monto flexible (con o sin comas)
            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto total") },
                singleLine = true,
                isError = !montoValido,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )
            if (!montoValido) {
                if (monto.isBlank()) Text("Campo obligatorio", color = colorScheme.error)
                else Text("Monto inválido, máximo 2 decimales", color = colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))

            // ✅ Botones alineados como en NuevoDueñoScreen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            val cliente = clienteViewModel.findByTelefono(telefono)
                            if (cliente == null) {
                                showDialog = true
                            } else {
                                val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                val montoFinal = monto.replace(",", "").toDouble()
                                val nuevaCotizacion = CotizacionEntity(
                                    id_cliente = cliente.id_cliente,
                                    fecha = fecha,
                                    concepto = concepto.trim(),
                                    descripcion = descripcion.trim(),
                                    monto = montoFinal,
                                    estado_cotizacion = "pendiente",
                                    estado_pago = "pendiente"
                                )
                                viewModel.addCotizacion(nuevaCotizacion)
                                onSaveSuccess()
                            }
                        }
                    },
                    enabled = !hayErrores,
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Text("Aceptar", color = colorScheme.onSecondary)
                }

                OutlinedButton(onClick = { onCancel() }) {
                    Text("Cancelar")
                }
            }

            if (hayErrores) {
                Text("Algunos campos están vacíos o tienen errores", color = colorScheme.error)
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Cliente no encontrado") },
                    text = { Text("¿Desea registrar al cliente con teléfono $telefono?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            onAddCliente(telefono)
                        }) {
                            Text("Registrar Cliente")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
