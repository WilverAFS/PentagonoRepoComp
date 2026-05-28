package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.ClienteEntity
import com.ingsoftware.pentagono.data.CotizacionEntity
import com.ingsoftware.pentagono.viewmodel.ClienteViewModel
import com.ingsoftware.pentagono.viewmodel.CotizacionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevaCotizacionScreen(
    viewModel: CotizacionViewModel,
    clienteViewModel: ClienteViewModel,
    onSaveSuccess: () -> Unit = {},
    onAddCliente: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onCancel: () -> Unit = {},
    prefilledTelefono: String = ""
) {
    val colorScheme = MaterialTheme.colorScheme
    val clientes    by clienteViewModel.clientes.collectAsState()

    var searchQuery          by remember { mutableStateOf("") }
    var clienteSeleccionado  by remember { mutableStateOf<ClienteEntity?>(null) }
    var mostrarSugerencias   by remember { mutableStateOf(false) }

    var concepto    by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var monto       by remember { mutableStateOf("") }

    // Auto-seleccionar si viene con teléfono prellenado
    LaunchedEffect(clientes, prefilledTelefono) {
        if (prefilledTelefono.isNotBlank() && clienteSeleccionado == null) {
            clientes.find { it.telefono == prefilledTelefono }?.let {
                clienteSeleccionado = it
            }
        }
    }

    val resultados = remember(clientes, searchQuery, clienteSeleccionado) {
        val q = searchQuery.trim()
        if (q.isBlank() || clienteSeleccionado != null) emptyList()
        else clientes.filter { c ->
            c.nombre.contains(q, ignoreCase = true) ||
            (c.apellidoPaterno?.contains(q, ignoreCase = true) == true) ||
            (c.apellidoMaterno?.contains(q, ignoreCase = true) == true) ||
            c.telefono.contains(q) ||
            (c.correo?.contains(q, ignoreCase = true) == true)
        }.take(5)
    }

    val montoRegex  = Regex("^[0-9]{1,3}(,?[0-9]{3})*(\\.[0-9]{1,2})?$")
    val montoValido = monto.isNotBlank() &&
            montoRegex.matches(monto) &&
            monto.replace(",", "").toDoubleOrNull() != null &&
            monto.replace(",", "").toDouble() >= 0
    val hayErrores  = clienteSeleccionado == null || concepto.isBlank() || descripcion.isBlank() || !montoValido

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title          = "Nueva Cotización",
                showBackButton = true,
                onBackClick    = onCancel
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // ── Sección cliente ──────────────────────────────────────────
            Text("Cliente", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = colorScheme.primary)

            if (clienteSeleccionado == null) {
                // Campo de búsqueda
                OutlinedTextField(
                    value         = searchQuery,
                    onValueChange = { searchQuery = it; mostrarSugerencias = it.isNotBlank() },
                    placeholder   = { Text("Buscar por teléfono, nombre o correo") },
                    leadingIcon   = { Icon(Icons.Default.Search, null, tint = colorScheme.onSurfaceVariant) },
                    trailingIcon  = if (searchQuery.isNotBlank()) ({
                        IconButton(onClick = { searchQuery = ""; mostrarSugerencias = false }) {
                            Icon(Icons.Default.Close, null)
                        }
                    }) else null,
                    singleLine    = true,
                    shape         = RoundedCornerShape(12.dp),
                    modifier      = Modifier.fillMaxWidth()
                )

                // Resultados / opción registrar
                if (mostrarSugerencias && searchQuery.isNotBlank()) {
                    Card(
                        modifier  = Modifier.fillMaxWidth(),
                        shape     = RoundedCornerShape(12.dp),
                        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column {
                            if (resultados.isEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onAddCliente() }
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.PersonAdd, null, tint = colorScheme.primary, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(12.dp))
                                    Column {
                                        Text("Sin coincidencias", style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant)
                                        Text("¿Desea registrar al cliente?", style = MaterialTheme.typography.bodyMedium, color = colorScheme.primary, fontWeight = FontWeight.Medium)
                                    }
                                }
                            } else {
                                resultados.forEachIndexed { idx, cliente ->
                                    if (idx > 0) HorizontalDivider(color = colorScheme.outlineVariant)
                                    val nombreCompleto = listOfNotNull(
                                        cliente.nombre.takeIf { it.isNotBlank() },
                                        cliente.apellidoPaterno?.takeIf { it.isNotBlank() }
                                    ).joinToString(" ")
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                clienteSeleccionado = cliente
                                                mostrarSugerencias  = false
                                            }
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Person, null, tint = colorScheme.primary, modifier = Modifier.size(18.dp))
                                        Spacer(Modifier.width(12.dp))
                                        Column {
                                            Text(nombreCompleto, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                            Text(cliente.telefono, style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant)
                                            cliente.correo?.takeIf { it.isNotBlank() }?.let {
                                                Text(it, style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Cliente ya seleccionado — tarjeta de confirmación
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = CardDefaults.cardColors(containerColor = colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, null, tint = colorScheme.primary)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            val nombreCompleto = listOfNotNull(
                                clienteSeleccionado!!.nombre.takeIf { it.isNotBlank() },
                                clienteSeleccionado!!.apellidoPaterno?.takeIf { it.isNotBlank() },
                                clienteSeleccionado!!.apellidoMaterno?.takeIf { it.isNotBlank() }
                            ).joinToString(" ")
                            Text(nombreCompleto, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = colorScheme.onPrimaryContainer)
                            Text("Tel: ${clienteSeleccionado!!.telefono}", style = MaterialTheme.typography.bodySmall, color = colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                        }
                        IconButton(onClick = { clienteSeleccionado = null; searchQuery = "" }) {
                            Icon(Icons.Default.Close, "Cambiar cliente", tint = colorScheme.onPrimaryContainer)
                        }
                    }
                }
            }

            // ── Detalles de la cotización ────────────────────────────────
            Spacer(Modifier.height(4.dp))
            Text("Detalles", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = colorScheme.primary)

            CampoObligatorio(concepto, { concepto = it }, label = "Concepto")

            OutlinedTextField(
                value         = descripcion,
                onValueChange = { descripcion = it },
                label         = { Text("Descripción de materiales") },
                modifier      = Modifier.fillMaxWidth(),
                isError       = descripcion.isNotBlank() && descripcion.isBlank(),
                maxLines      = 5
            )
            if (descripcion.isBlank())
                Text("Campo obligatorio", color = colorScheme.error, style = MaterialTheme.typography.labelMedium)

            OutlinedTextField(
                value         = monto,
                onValueChange = { monto = it },
                label         = { Text("Monto total") },
                singleLine    = true,
                isError       = monto.isNotBlank() && !montoValido,
                modifier      = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                leadingIcon   = { Text("$", style = MaterialTheme.typography.bodyMedium, color = colorScheme.onSurfaceVariant) }
            )
            if (monto.isBlank())
                Text("Campo obligatorio", color = colorScheme.error, style = MaterialTheme.typography.labelMedium)
            else if (!montoValido)
                Text("Monto inválido, máximo 2 decimales", color = colorScheme.error, style = MaterialTheme.typography.labelMedium)

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        val cliente = clienteSeleccionado ?: return@Button
                        val fecha   = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                        viewModel.addCotizacion(
                            CotizacionEntity(
                                id_cliente        = cliente.id_cliente,
                                fecha             = fecha,
                                concepto          = concepto.trim(),
                                descripcion       = descripcion.trim(),
                                monto             = monto.replace(",", "").toDouble(),
                                estado_cotizacion = "pendiente",
                                estado_pago       = "pendiente"
                            )
                        )
                        onSaveSuccess()
                    },
                    enabled = !hayErrores,
                    colors  = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) { Text("Guardar") }

                OutlinedButton(onClick = onCancel) { Text("Cancelar") }
            }

            if (clienteSeleccionado == null)
                Text("Selecciona un cliente para continuar", color = colorScheme.error, style = MaterialTheme.typography.bodySmall)

            Spacer(Modifier.height(40.dp))
        }
    }
}
