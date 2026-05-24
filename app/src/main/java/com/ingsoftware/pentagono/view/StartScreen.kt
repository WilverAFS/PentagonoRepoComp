package com.ingsoftware.pentagono.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.ingsoftware.pentagono.R
import com.ingsoftware.pentagono.data.DueñoEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    dueño: DueñoEntity,
    onMenuClick: (Int) -> Unit = {},
    onExit: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Vidrios y Cristales Pentágono",
                onMenuClick = { onMenuClick(dueño.id_dueño) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {

            Spacer(Modifier.height(16.dp))

            // Saludo
            Text(
                text = "Bienvenido, ${dueño.nombre}",
                style = MaterialTheme.typography.headlineSmall,
                color = colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(12.dp))

            // Card info del negocio
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "\"Claridad y resistencia\ndesde todos los ángulos.\"",
                        style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                        color = colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = colorScheme.outlineVariant)
                    Spacer(Modifier.height(12.dp))
                    InfoRow(label = "Dirección", value = "Villa de Zaachila, Oaxaca")
                    InfoRow(label = "Horario", value = "Lun – Sáb, 9:00 AM – 6:00 PM")
                    InfoRow(label = "Tel", value = "951-123-4567")
                }
            }

            Spacer(Modifier.height(20.dp))

            // Sección servicios
            SectionHeader(title = "Servicios disponibles")
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ServiceCard(
                    title = "Vidrio Arquitectónico",
                    description = "Ventanales, fachadas y domos.",
                    modifier = Modifier.weight(1f)
                )
                ServiceCard(
                    title = "Diseño de Interiores",
                    description = "Canceles, espejos y barandales.",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ServiceCard(
                    title = "Cortes Especializados",
                    description = "Precisión y acabados de calidad.",
                    modifier = Modifier.weight(1f)
                )
                ServiceCard(
                    title = "Mantenimiento",
                    description = "Cambio de cristales dañados.",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(20.dp))

            // Sección dashboard
            SectionHeader(title = "Dashboard")
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardCard(
                    icon = R.drawable.ic_orders,
                    title = "Órdenes activas",
                    value = "3",
                    modifier = Modifier.weight(1f)
                )
                DashboardCard(
                    icon = R.drawable.ic_order_pending,
                    title = "Pendientes",
                    value = "5",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DashboardCard(
                    icon = R.drawable.ic_order_overdue,
                    title = "Atrasadas",
                    value = "2",
                    modifier = Modifier.weight(1f),
                    valueColor = MaterialTheme.colorScheme.error
                )
                DashboardCard(
                    icon = R.drawable.ic_contract,
                    title = "Cotizaciones",
                    value = "4",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Acciones rápidas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Salir — outlined rojo
                OutlinedButton(
                    onClick = onExit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorScheme.error
                    )
                ) {
                    Text("Salir", style = MaterialTheme.typography.labelLarge, maxLines = 1)
                }

                // Cotizar — teal secundario
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1.4f),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.secondary,
                        contentColor = colorScheme.onSecondary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text("Cotizar", style = MaterialTheme.typography.labelLarge, maxLines = 1)
                }

                // Órdenes — verde primario
                Button(
                    onClick = {},
                    modifier = Modifier.weight(1.4f),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text("Órdenes", style = MaterialTheme.typography.labelLarge, maxLines = 1)
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ── Componentes internos ──────────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ServiceCard(title: String, description: String, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DashboardCard(
    icon: Int,
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = MaterialTheme.colorScheme.primary
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = title,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = valueColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}