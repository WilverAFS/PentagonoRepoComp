package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.data.EmpleadoEntity
import com.ingsoftware.pentagono.ui.theme.VerdeWelcome
import com.ingsoftware.pentagono.viewmodel.EmpleadoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmpleadosScreen(
    viewModel: EmpleadoViewModel,
    onBack: () -> Unit = {},
    onAddEmpleado: () -> Unit = {},
    onSearchEmpleado: () -> Unit = {},
    onEditEmpleado: (EmpleadoEntity) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val empleados   by viewModel.empleados.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    var query by remember { mutableStateOf("") }

    val empleadosFiltrados = remember(empleados, query) {
        val q = query.trim()
        if (q.isBlank()) empleados
        else empleados.filter { e ->
            e.nombre.contains(q, ignoreCase = true) ||
            e.apellidoPaterno.contains(q, ignoreCase = true) ||
            e.apellidoMaterno.contains(q, ignoreCase = true) ||
            e.telefono.contains(q) ||
            e.curp.contains(q, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title          = "Empleados",
                showBackButton = true,
                onBackClick    = onBack
            )
        },
        floatingActionButton = {
            PentagonoFab(onClick = onAddEmpleado)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background),
            contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Tarjeta de estadísticas
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(16.dp),
                    colors   = CardDefaults.cardColors(containerColor = VerdeWelcome)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Empleados", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                            Text("${empleados.size}", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Activos", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
                            Text("${empleados.size}", style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
            }

            // Barra de búsqueda
            item {
                OutlinedTextField(
                    value         = query,
                    onValueChange = { query = it },
                    placeholder   = { Text("Buscar por nombre, CURP o teléfono...") },
                    leadingIcon   = { Icon(Icons.Default.Search, contentDescription = null, tint = colorScheme.onSurfaceVariant) },
                    trailingIcon  = if (query.isNotBlank()) ({
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar")
                        }
                    }) else null,
                    singleLine    = true,
                    shape         = RoundedCornerShape(14.dp),
                    colors        = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.outline
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(4.dp))
            }

            if (empleadosFiltrados.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                        Text("No se encontraron empleados.", color = colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                items(empleadosFiltrados) { empleado ->
                    EmpleadoCard(empleado = empleado, onEdit = { onEditEmpleado(empleado) })
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun EmpleadoCard(
    empleado: EmpleadoEntity,
    onEdit: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val nombreCompleto = listOfNotNull(
        empleado.nombre.takeIf { it.isNotBlank() },
        empleado.apellidoPaterno.takeIf { it.isNotBlank() },
        empleado.apellidoMaterno.takeIf { it.isNotBlank() }
    ).joinToString(" ")

    val iniciales = buildString {
        empleado.nombre.firstOrNull()?.let { append(it.uppercaseChar()) }
        empleado.apellidoPaterno.firstOrNull()?.let { append(it.uppercaseChar()) }
    }.take(2)

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(iniciales, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color.White)
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text     = nombreCompleto,
                    style    = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color    = colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Badge, null, modifier = Modifier.size(13.dp), tint = colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text(empleado.curp, style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, null, modifier = Modifier.size(13.dp), tint = colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text(empleado.telefono, style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant)
                }
                if (!empleado.correo.isNullOrBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Email, null, modifier = Modifier.size(13.dp), tint = colorScheme.primary)
                        Spacer(Modifier.width(4.dp))
                        Text(empleado.correo, style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(13.dp), tint = colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text("${empleado.colonia}, ${empleado.municipio}", style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = colorScheme.onSurfaceVariant)
            }
        }
    }
}
