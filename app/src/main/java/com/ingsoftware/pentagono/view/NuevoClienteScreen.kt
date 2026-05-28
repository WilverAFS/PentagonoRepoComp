package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.ClienteEntity
import com.ingsoftware.pentagono.viewmodel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoClienteScreen(
    viewModel: ClienteViewModel,
    onBack: () -> Unit = {},
    onSaveSuccess: () -> Unit = {},
    onGoToCotizacion: (() -> Unit)? = null,
    onMenuClick: () -> Unit = {},
    prefilledTelefono: String = ""
) {
    val colorScheme     = MaterialTheme.colorScheme
    val clientes        by viewModel.clientes.collectAsState()
    var clienteGuardado by remember { mutableStateOf(false) }

    // ── Datos personales
    var nombre         by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }

    // ── Contacto
    var telefono by remember { mutableStateOf(prefilledTelefono) }
    var correo   by remember { mutableStateOf("") }

    // ── Domicilio
    var calle          by remember { mutableStateOf("") }
    var numeroExterior by remember { mutableStateOf("") }
    var numeroInterior by remember { mutableStateOf("") }
    var colonia        by remember { mutableStateOf("") }
    var municipio      by remember { mutableStateOf("") }
    var estado         by remember { mutableStateOf("Oaxaca") }

    // ── Validaciones
    val telefonoFmt   = telefono.replace(" ", "").replace("-", "")
    val telFormatoOk  = Validaciones.validarTelefono(telefonoFmt)
    val telUnico      = clientes.none { it.telefono == telefono.trim() }
    val telefonoValido = telFormatoOk && telUnico

    val nombreValido       = nombre.isNotBlank() && Validaciones.validarNombre(nombre)
    val calleValida        = calle.isNotBlank()
    val numExtValido       = numeroExterior.isNotBlank() && Validaciones.validarNumero(numeroExterior)
    val coloniaValida      = colonia.isNotBlank()
    val municipioValido    = municipio.isNotBlank()
    val estadoValido       = estado.isNotBlank()
    val correoValido       = correo.isBlank() || Validaciones.validarCorreo(correo)

    val hayErrores = !telefonoValido || !nombreValido || !calleValida || !numExtValido ||
                     !coloniaValida || !municipioValido || !estadoValido || !correoValido

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title          = "Nuevo Cliente",
                showBackButton = true,
                onBackClick    = onBack
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
            // ── Datos personales ───────────────────────────────────────────
            ClienteSectionLabel("Datos personales", colorScheme.primary)

            CampoNombre(nombre, { nombre = it }, label = "Nombre", obligatorio = true)
            CampoNombre(apellidoPaterno, { apellidoPaterno = it }, label = "Apellido Paterno (opcional)", obligatorio = false)
            CampoNombre(apellidoMaterno, { apellidoMaterno = it }, label = "Apellido Materno (opcional)", obligatorio = false)

            // ── Contacto ──────────────────────────────────────────────────
            Spacer(Modifier.height(4.dp))
            ClienteSectionLabel("Contacto", colorScheme.primary)

            val telefonoError = when {
                telefono.isBlank() -> "Campo obligatorio"
                !telFormatoOk      -> "Debe contener exactamente 10 dígitos"
                !telUnico          -> "Ya existe un cliente con este teléfono"
                else               -> null
            }
            OutlinedTextField(
                value           = telefono,
                onValueChange   = { telefono = it },
                label           = { Text("Teléfono (10 dígitos)") },
                singleLine      = true,
                isError         = telefonoError != null,
                modifier        = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
            )
            telefonoError?.let { Text(it, color = colorScheme.error, style = MaterialTheme.typography.labelMedium) }

            CampoCorreo(correo, { correo = it }, obligatorio = false)

            // ── Domicilio ─────────────────────────────────────────────────
            Spacer(Modifier.height(4.dp))
            ClienteSectionLabel("Domicilio", colorScheme.primary)

            CampoObligatorio(calle, { calle = it }, label = "Calle")
            CampoNumero(numeroExterior, { numeroExterior = it }, label = "Número Exterior", obligatorio = true)
            OutlinedTextField(
                value           = numeroInterior,
                onValueChange   = { numeroInterior = it },
                label           = { Text("Número Interior (opcional)") },
                modifier        = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
            )
            CampoObligatorio(colonia,   { colonia   = it }, label = "Colonia")
            CampoObligatorio(municipio, { municipio = it }, label = "Municipio")
            CampoObligatorio(estado,    { estado    = it }, label = "Estado")

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        viewModel.addCliente(
                            ClienteEntity(
                                telefono       = telefono.trim(),
                                nombre         = nombre.trim(),
                                apellidoPaterno= if (apellidoPaterno.isBlank()) null else apellidoPaterno.trim(),
                                apellidoMaterno= if (apellidoMaterno.isBlank()) null else apellidoMaterno.trim(),
                                calle          = calle.trim(),
                                numeroExterior = numeroExterior.trim(),
                                numeroInterior = if (numeroInterior.isBlank()) null else numeroInterior.trim(),
                                colonia        = colonia.trim(),
                                municipio      = municipio.trim(),
                                estado         = estado.trim(),
                                correo         = if (correo.isBlank()) null else correo.trim()
                            )
                        )
                        if (onGoToCotizacion != null) clienteGuardado = true else onSaveSuccess()
                    },
                    enabled = !hayErrores && !clienteGuardado,
                    colors  = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) { Text("Guardar") }

                OutlinedButton(onClick = onBack, enabled = !clienteGuardado) {
                    Text("Cancelar")
                }
            }

            if (hayErrores && !clienteGuardado)
                Text("Completa todos los campos requeridos", color = colorScheme.error, style = MaterialTheme.typography.bodySmall)

            // Bloque post-guardado
            if (clienteGuardado && onGoToCotizacion != null) {
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(32.dp))
                        Spacer(Modifier.height(6.dp))
                        Text("¡Cliente registrado exitosamente!", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold))
                        Spacer(Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(onClick = onGoToCotizacion, colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)) {
                                Icon(Icons.Default.Receipt, null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Generar Cotización")
                            }
                            OutlinedButton(onClick = onSaveSuccess) { Text("Volver") }
                        }
                    }
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun ClienteSectionLabel(text: String, color: androidx.compose.ui.graphics.Color) {
    Text(text, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = color)
}
