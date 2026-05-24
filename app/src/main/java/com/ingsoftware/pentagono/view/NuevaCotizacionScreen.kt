package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    onBack: () -> Unit = {},
    onSaveSuccess: () -> Unit = {},
    onAddCliente: (String) -> Unit = {},
    onMenuClick: () -> Unit = {},   // ✅ nuevo parámetro para consistencia
    prefilledTelefono: String = ""
) {
    val colorScheme = MaterialTheme.colorScheme
    var telefono by remember { mutableStateOf(prefilledTelefono) }
    var concepto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Validaciones
    val montoRegex = Regex("^\\d+(\\.\\d{1,2})?$")
    val telefonoValido = Validaciones.validarTelefono(telefono.replace(" ", "").replace("-", ""))
    val conceptoValido = concepto.isNotBlank()
    val descripcionValido = descripcion.isNotBlank()
    val montoValido = monto.isNotBlank() && montoRegex.matches(monto) && monto.toDoubleOrNull() != null && monto.toDouble() >= 0

    val hayErrores = !telefonoValido || !conceptoValido || !descripcionValido || !montoValido

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Nueva Cotización",
                onMenuClick = { onMenuClick() } // ✅ menú → MenuScreen
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
            // Botón atrás dentro del contenido (si lo quieres mantener)
            Button(
                onClick = { onBack() },
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
            ) {
                Text("Regresar", color = colorScheme.onPrimary)
            }

            Spacer(Modifier.height(16.dp))

            CampoTelefono(telefono, { telefono = it }, obligatorio = true)
            CampoObligatorio(concepto, { concepto = it }, label = "Concepto")
            CampoObligatorio(descripcion, { descripcion = it }, label = "Descripción de materiales")

            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto total") },
                singleLine = true,
                isError = !montoValido,
                modifier = Modifier.fillMaxWidth()
            )
            if (!montoValido) {
                if (monto.isBlank()) Text("Campo obligatorio", color = colorScheme.error)
                else Text("Monto inválido, máximo 2 decimales", color = colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        val cliente = clienteViewModel.findByTelefono(telefono)
                        if (cliente == null) {
                            showDialog = true
                        } else {
                            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val nuevaCotizacion = CotizacionEntity(
                                id_cliente = cliente.id_cliente,
                                fecha = fecha,
                                concepto = concepto.trim(),
                                descripcion = descripcion.trim(),
                                monto = monto.toDouble(),
                                estado_cotizacion = "pendiente",
                                estado_pago = "pendiente"
                            )
                            viewModel.addCotizacion(nuevaCotizacion)
                            onSaveSuccess()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary),
                enabled = !hayErrores
            ) {
                Text("Guardar Cotización", color = colorScheme.onPrimary)
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
