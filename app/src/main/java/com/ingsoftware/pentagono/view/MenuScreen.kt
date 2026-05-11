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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onNavigate: (String) -> Unit = {},
    onBack: () -> Unit = {} // nueva acción para regresar
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Menú de Opciones",
                onMenuClick = { onBack() } // al dar clic regresa a la vista anterior
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
                onNavigate("cotizaciones")
            }
            MenuButton("Órdenes de trabajo", Icons.Filled.Work, colorScheme.secondary, colorScheme.onPrimary) {
                onNavigate("ordenes")
            }
            MenuButton("Clientes", Icons.Filled.People, colorScheme.secondary, colorScheme.onPrimary) {
                onNavigate("clientes")
            }
            MenuButton("Empleados", Icons.Filled.Person, colorScheme.secondary, colorScheme.onPrimary) {
                onNavigate("empleados")
            }
            MenuButton("Configuración", Icons.Filled.Settings, colorScheme.secondary, colorScheme.onPrimary) {
                onNavigate("configuracion")
            }
            MenuButton("Logs", Icons.Filled.List, colorScheme.secondary, colorScheme.onPrimary) {
                onNavigate("logs")
            }

            Spacer(Modifier.height(8.dp))
            // Botón de salir en rojo
            MenuButton("Salir", Icons.Filled.ExitToApp, Color.Red, colorScheme.onPrimary) {
                onNavigate("salir")
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MenuScreenPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        MenuScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MenuScreenPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        MenuScreen()
    }
}
