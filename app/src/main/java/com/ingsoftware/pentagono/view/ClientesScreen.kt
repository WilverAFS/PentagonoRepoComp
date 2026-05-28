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
import com.ingsoftware.pentagono.data.ClienteEntity
import com.ingsoftware.pentagono.ui.theme.VerdeWelcome
import com.ingsoftware.pentagono.viewmodel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientesScreen(
    viewModel: ClienteViewModel,
    onBack: () -> Unit = {},
    onAddCliente: () -> Unit = {},
    onSearchCliente: () -> Unit = {},
    onEditCliente: (ClienteEntity) -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val clientes    by viewModel.clientes.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    var query by remember { mutableStateOf("") }

    val clientesFiltrados = remember(clientes, query) {
        val q = query.trim()
        if (q.isBlank()) clientes
        else clientes.filter { c ->
            c.nombre.contains(q, ignoreCase = true) ||
            (c.apellidoPaterno?.contains(q, ignoreCase = true) == true) ||
            (c.apellidoMaterno?.contains(q, ignoreCase = true) == true) ||
            c.telefono.contains(q) ||
            (c.correo?.contains(q, ignoreCase = true) == true)
        }
    }

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title          = "Clientes",
                showBackButton = true,
                onBackClick    = onBack
            )
        },
        floatingActionButton = {
            PentagonoFab(onClick = onAddCliente)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
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
                            Text(
                                text  = "Total Clientes",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text  = "${clientes.size}",
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text  = "Ingresos Total",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text  = "—",
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
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
                    placeholder   = { Text("Buscar por nombre, teléfono o email..") },
                    leadingIcon   = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = colorScheme.onSurfaceVariant)
                    },
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

            if (clientesFiltrados.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), contentAlignment = Alignment.Center) {
                        Text("No se encontraron clientes.", color = colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                items(clientesFiltrados) { cliente ->
                    ClienteCard(cliente = cliente, onEdit = { onEditCliente(cliente) })
                }
            }

            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun ClienteCard(
    cliente: ClienteEntity,
    onEdit: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val nombreCompleto = listOfNotNull(
        cliente.nombre.takeIf { it.isNotBlank() },
        cliente.apellidoPaterno?.takeIf { it.isNotBlank() },
        cliente.apellidoMaterno?.takeIf { it.isNotBlank() }
    ).joinToString(" ")

    val iniciales = buildString {
        cliente.nombre.firstOrNull()?.let { append(it.uppercaseChar()) }
        cliente.apellidoPaterno?.firstOrNull()?.let { append(it.uppercaseChar()) }
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
            // Avatar con iniciales
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(VerdeWelcome),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = iniciales,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text  = nombreCompleto,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(6.dp))

                // Teléfono
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, null, modifier = Modifier.size(13.dp), tint = colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text(cliente.telefono, style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant)
                }

                // Email
                if (!cliente.correo.isNullOrBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Email, null, modifier = Modifier.size(13.dp), tint = colorScheme.primary)
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text  = cliente.correo,
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Dirección
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(13.dp), tint = colorScheme.primary)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text  = "${cliente.colonia}, ${cliente.municipio}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = colorScheme.onSurfaceVariant)
            }
        }
    }
}
