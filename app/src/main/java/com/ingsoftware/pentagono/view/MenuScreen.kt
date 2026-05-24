package com.ingsoftware.pentagono.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                title = "Menú de Opciones",
                onMenuClick = { /* ✅ No hace nada en MenuScreen */ }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.End
        ) {

            MenuButton("Cotizaciones", Icons.Filled.Receipt, colorScheme.secondary, colorScheme.onPrimary) {
                onNavigate("cotizaciones/$dueñoId")
            }
            MenuButton("Órdenes de trabajo", Icons.Filled.Work, colorScheme.secondary, colorScheme.onPrimary) {
                onNavigate("ordenes/$dueñoId")
            }
            MenuButton("Clientes", Icons.Filled.People, colorScheme.secondary, colorScheme.onPrimary) {
                onNavigate("clientes/$dueñoId")
            }
            MenuButton("Empleados", Icons.Filled.Person, colorScheme.secondary, colorScheme.onPrimary) {
                onNavigate("empleados/$dueñoId")
            }
            MenuButton("Configuración", Icons.Filled.Settings, colorScheme.secondary, colorScheme.onPrimary) {
                onNavigate("configuracion/$dueñoId")
            }
            MenuButton("Logs", Icons.Filled.List, colorScheme.secondary, colorScheme.onPrimary) {
                onNavigate("logs/$dueñoId")
            }


            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onNavigate("salir") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Salir", color = colorScheme.onBackground)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Salir", tint = colorScheme.onBackground)
                    }
                }

                Button(
                    onClick = { onNavigate("start/$dueñoId") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Inicio", color = colorScheme.background)
                        Spacer(Modifier.width(8.dp))
                        Icon(Icons.Filled.Home, contentDescription = "Inicio", tint = colorScheme.background)
                    }
                }
            }
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    backgroundColor: Color,
    textColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, color = textColor)
            Spacer(Modifier.width(8.dp))
            Icon(icon, contentDescription = text, tint = textColor)
        }
    }
}
