package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.ui.theme.VerdeWelcome

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    dueñoId: Int,
    onNavigate: (String) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    data class MenuItem(val label: String, val icon: ImageVector, val route: String)

    val menuItems = listOf(
        MenuItem("Cotizaciones",  Icons.Filled.Receipt,  "cotizaciones/$dueñoId"),
        MenuItem("Órdenes",       Icons.Filled.Work,     "ordenes/$dueñoId"),
        MenuItem("Clientes",      Icons.Filled.People,   "clientes/$dueñoId"),
        MenuItem("Empleados",     Icons.Filled.Person,   "empleados/$dueñoId"),
        MenuItem("Configuración", Icons.Filled.Settings, "configuracion/$dueñoId"),
        MenuItem("Logs",          Icons.Filled.List,     "logs/$dueñoId")
    )

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title          = "Menú Principal",
                showBackButton = true,
                onBackClick    = { onNavigate("start/$dueñoId") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            LazyVerticalGrid(
                columns             = GridCells.Fixed(2),
                modifier            = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 600.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp),
                userScrollEnabled     = false
            ) {
                items(menuItems) { item ->
                    MenuGridCard(
                        label   = item.label,
                        icon    = item.icon,
                        onClick = { onNavigate(item.route) }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Tarjeta de información del negocio
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(
                    containerColor = VerdeWelcome
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text  = "Pentagono Vidriería",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text  = "Sistema de gestión profesional para vidriería",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF8BC34A), CircleShape)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text  = "Villa de Zaachila, Oaxaca, México",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun MenuGridCard(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        onClick   = onClick,
        modifier  = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint     = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text      = label,
                style     = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color     = colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MenuButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        onClick   = onClick,
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(12.dp),
        color     = colorScheme.surface,
        tonalElevation  = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment   = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colorScheme.secondary,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint     = colorScheme.onSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text  = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurface
                )
            }
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint     = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
