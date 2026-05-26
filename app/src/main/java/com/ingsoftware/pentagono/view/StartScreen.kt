package com.ingsoftware.pentagono.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.ingsoftware.pentagono.R
import com.ingsoftware.pentagono.data.DueñoEntity
import com.ingsoftware.pentagono.viewmodel.OrdenViewModel
import com.ingsoftware.pentagono.viewmodel.CotizacionViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    dueño: DueñoEntity,
    ordenVM: OrdenViewModel,
    cotizacionVM: CotizacionViewModel,
    onMenuClick: (Int) -> Unit = {},
    onExit: () -> Unit = {},
    onNavigateCotizaciones: (Int) -> Unit = {},
    onNavigateOrdenes: (Int) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    // ✅ Obtener datos de BD
    val ordenes by ordenVM.ordenes.collectAsState()
    val cotizaciones by cotizacionVM.cotizaciones.collectAsState()

    // ✅ Fecha actual
    val fechaActual = LocalDate.now()

    // ✅ Función segura para parsear fechas
    fun safeParseDate(fecha: String?): LocalDate? {
        return try {
            if (fecha != null) LocalDate.parse(fecha) else null
        } catch (e: Exception) {
            null
        }
    }

    // ✅ Calcular métricas
    val pendientes = ordenes.count {
        it.estado.name == "PENDIENTE" &&
                (safeParseDate(it.fecha_fin)?.isAfter(fechaActual) != false)
    }

    val porEntregar = ordenes.count {
        it.estado.name == "TERMINADO" &&
                (safeParseDate(it.fecha_fin)?.isAfter(fechaActual) != false)
    }

    val atrasadas = ordenes.count {
        val fechaFin = safeParseDate(it.fecha_fin)
        fechaFin != null &&
                fechaFin.isBefore(fechaActual) &&
                it.estado.name != "ENTREGADO" &&
                it.estado.name != "CANCELADO"
    }

    val cotizacionesPendientes = cotizaciones.count { it.estado_cotizacion == "pendiente" }

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
        ) {
            // ✅ Saludo personalizado
            Text(
                text = "Bienvenido, ${dueño.nombre}",
                style = MaterialTheme.typography.headlineSmall,
                color = colorScheme.primary,
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                textAlign = TextAlign.Center
            )

            // ✅ Información del negocio
            Card(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
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

            // ✅ Dashboard dinámico
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Dashboard:", style = MaterialTheme.typography.titleLarge, color = colorScheme.secondary)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardCard(icon = R.drawable.ic_order_pending, title = "Pendientes", value = pendientes.toString(), Modifier.weight(1f))
                    DashboardCard(icon = R.drawable.ic_orders, title = "Por Entregar", value = porEntregar.toString(), Modifier.weight(1f))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DashboardCard(icon = R.drawable.ic_order_overdue, title = "Atrasadas", value = atrasadas.toString(), Modifier.weight(1f), valueColor = Color.Red)
                    DashboardCard(icon = R.drawable.ic_contract, title = "Cotizaciones Pendientes", value = cotizacionesPendientes.toString(), Modifier.weight(1f))
                }
            }

            // ✅ Acceso rápido
            Row(
                Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onExit() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                    Text("Salir", color = colorScheme.onPrimary)
                }
                Button(onClick = { onNavigateCotizaciones(dueño.id_dueño) }, colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)) {
                    Text("Cotizaciones", color = colorScheme.onSecondary)
                }
                Button(onClick = { onNavigateOrdenes(dueño.id_dueño) }, colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)) {
                    Text("Órdenes", color = colorScheme.onSecondary)
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
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
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
