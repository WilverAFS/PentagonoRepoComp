package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    dueñoId: Int,
    onNavigate: (String) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Menú",
                showBackButton = true,
                onBackClick = { onNavigate("start/$dueñoId") },
                onMenuClick = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Accesos rápidos",
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            MenuButton("Cotizaciones",       Icons.Filled.Receipt,  { onNavigate("cotizaciones/$dueñoId") })
            MenuButton("Órdenes de trabajo", Icons.Filled.Work,     { onNavigate("ordenes/$dueñoId") })
            MenuButton("Clientes",           Icons.Filled.People,   { onNavigate("clientes/$dueñoId") })
            MenuButton("Empleados",          Icons.Filled.Person,   { onNavigate("empleados/$dueñoId") })
            MenuButton("Configuración",      Icons.Filled.Settings, { onNavigate("configuracion/$dueñoId") })
            MenuButton("Logs",               Icons.Filled.List,     { onNavigate("logs/$dueñoId") })

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onNavigate("salir") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorScheme.error
                    )
                ) {
                    Icon(Icons.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Salir", style = MaterialTheme.typography.labelLarge)
                }

                Button(
                    onClick = { onNavigate("start/$dueñoId") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Icon(Icons.Filled.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Inicio", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cajita teal con ícono blanco — mayor contraste y visibilidad
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colorScheme.secondary,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = colorScheme.onSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurface
                )
            }
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}