package com.ingsoftware.pentagono.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.ingsoftware.pentagono.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(onMenuClick: () -> Unit = {}) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            // Aquí reutilizamos la barra superior institucional
            PentagonoTopBar(
                title = "Vidrios y Cristales Pentágono",
                onMenuClick = onMenuClick
            )
        }
    ) { innerPadding ->
        // Scroll vertical aplicado a toda la columna
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Información del negocio en tarjeta
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "\"Claridad y resistencia \n desde todos los ángulos.\"",
                        style = MaterialTheme.typography.headlineMedium.copy(fontStyle = FontStyle.Italic),
                        color = colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Dirección: Villa de Zaachila, Oaxaca", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSurface)
                    Text("Horario: Lunes a Sábado, 9:00 AM - 6:00 PM", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSurface)
                    Text("Tel: 951-123-4567", style = MaterialTheme.typography.bodyLarge, color = colorScheme.onSurface)
                }
            }

            // Servicios en cuadrícula 2x2
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Servicios Disponibles:", style = MaterialTheme.typography.titleLarge, color= colorScheme.secondary)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ServiceCard("Vidrio Arquitectónico", "Instalación de ventanales, fachadas y domos.", Modifier.weight(1f))
                    ServiceCard("Diseño de Interiores", "Canceles de baño, espejos a medida y barandales de cristal templado.", Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ServiceCard("Cortes Especializados", "Cristales con cortes precisos y acabados de alta calidad.", Modifier.weight(1f))
                    ServiceCard("Mantenimiento y Reposición", "Cambio de cristales rotos o dañados.", Modifier.weight(1f))
                }
            }

            // Dashboard en cuadrícula 2x2
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Dashboard:", style = MaterialTheme.typography.titleLarge, color = colorScheme.secondary)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardCard(icon = R.drawable.ic_orders, title = "Órdenes activas", value = "3", Modifier.weight(1f))
                    DashboardCard(icon = R.drawable.ic_order_pending, title = "Pendientes", value = "5", Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardCard(icon = R.drawable.ic_order_overdue, title = "Atrasadas", value = "2", Modifier.weight(1f), valueColor = Color.Black)
                    DashboardCard(icon = R.drawable.ic_contract, title = "Cotizaciones", value = "4", Modifier.weight(1f))
                }
            }

            // Acceso rápido
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)) {
                    Text("Cotizaciones", color = colorScheme.onSecondary)
                }
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)) {
                    Text("Órdenes", color = colorScheme.onSecondary)
                }
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Salir", color = colorScheme.onPrimary)
                }
            }
        }
    }
}

@Composable
fun ServiceCard(title: String, description: String, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = colorScheme.primary)
            Spacer(Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurface)
        }
    }
}

@Composable
fun DashboardCard(icon: Int, title: String, value: String, modifier: Modifier = Modifier, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = valueColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartScreenPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        StartScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartScreenPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        StartScreen()
    }
}
