package com.ingsoftware.pentagono.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingsoftware.pentagono.R
import com.ingsoftware.pentagono.data.DueñoEntity
import com.ingsoftware.pentagono.ui.theme.VerdeOscuro
import com.ingsoftware.pentagono.ui.theme.VerdeWelcome
import com.ingsoftware.pentagono.viewmodel.CotizacionViewModel
import com.ingsoftware.pentagono.viewmodel.OrdenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    val ordenes      by ordenVM.ordenes.collectAsState()
    val cotizaciones by cotizacionVM.cotizaciones.collectAsState()
    val fechaActual  = LocalDate.now()

    fun safeParseDate(fecha: String?): LocalDate? = try {
        if (fecha != null) LocalDate.parse(fecha) else null
    } catch (e: Exception) { null }

    val pendientes  = ordenes.count { it.estado.name == "PENDIENTE" }
    val porEntregar = ordenes.count { it.estado.name == "TERMINADO" }
    val atrasadas    = ordenes.count {
        val f = safeParseDate(it.fecha_fin)
        f != null && f.isBefore(fechaActual) &&
                it.estado.name != "ENTREGADO" && it.estado.name != "CANCELADO"
    }
    val totalCotizaciones = cotizaciones.size

    val fechaFmt = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("es", "MX"))
    val fechaTexto = fechaActual.format(fechaFmt)

    // Actividad reciente: últimas 3 órdenes + cotizaciones combinadas por fecha
    val actividadReciente = remember(ordenes, cotizaciones) {
        val items = mutableListOf<Triple<String, String, String>>()
        cotizaciones.sortedByDescending { it.id_cotizacion }.take(3).forEach { cot ->
            items.add(Triple("COT", "Cotización creada", cot.fecha.take(10)))
        }
        ordenes.sortedByDescending { it.id_orden }.take(3).forEach { ord ->
            val label = when (ord.estado.name) {
                "TERMINADO" -> "Orden completada"
                "ENTREGADO" -> "Orden entregada"
                "CANCELADO" -> "Orden cancelada"
                else -> "Orden actualizada"
            }
            items.add(Triple("ORD", label, ord.fecha_inicio.take(10)))
        }
        items.sortedByDescending { it.third }.take(5)
    }

    val welcomeGradient = Brush.verticalGradient(
        colors = listOf(VerdeWelcome, VerdeOscuro)
    )

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title = "Inicio",
                onMenuClick = { onMenuClick(dueño.id_dueño) }
            )
        },
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

            // Banner de bienvenida
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(welcomeGradient)
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = fechaTexto,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Bienvenido, ${dueño.nombre}",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Tienes $pendientes tareas pendientes hoy",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Fila superior de stat cards
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                DashboardCard2(
                    icon        = R.drawable.ic_order_pending,
                    iconBgColor = Color(0xFFFFF3E0),
                    iconTint    = Color(0xFFF57C00),
                    value       = pendientes.toString(),
                    valueColor  = Color(0xFFF57C00),
                    title       = "Pendientes",
                    subtitle    = if (pendientes > 0) "+$pendientes hoy" else "Al día",
                    modifier    = Modifier.weight(1f)
                )
                DashboardCard2(
                    icon        = R.drawable.ic_orders,
                    iconBgColor = Color(0xFFE8F5E9),
                    iconTint    = Color(0xFF388E3C),
                    value       = porEntregar.toString(),
                    valueColor  = Color(0xFF388E3C),
                    title       = "Por Entregar",
                    subtitle    = if (porEntregar > 0) "$porEntregar esta semana" else "Sin pendientes",
                    modifier    = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(10.dp))

            // Fila inferior de stat cards
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                DashboardCard2(
                    icon        = R.drawable.ic_order_overdue,
                    iconBgColor = Color(0xFFFFEBEE),
                    iconTint    = Color(0xFFC62828),
                    value       = atrasadas.toString(),
                    valueColor  = Color(0xFFC62828),
                    title       = "Atrasadas",
                    subtitle    = if (atrasadas > 0) "Atención requerida" else "Sin retrasos",
                    modifier    = Modifier.weight(1f)
                )
                DashboardCard2(
                    icon        = R.drawable.ic_contract,
                    iconBgColor = Color(0xFFF9FBE7),
                    iconTint    = Color(0xFF827717),
                    value       = totalCotizaciones.toString(),
                    valueColor  = Color(0xFF827717),
                    title       = "Cotizaciones",
                    subtitle    = if (totalCotizaciones > 0) "+$totalCotizaciones este mes" else "Sin cotizaciones",
                    modifier    = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(20.dp))

            // Actividad reciente
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Actividad Reciente",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.onSurface
                )
                TextButton(onClick = { onNavigateCotizaciones(dueño.id_dueño) }) {
                    Text("Ver todo →", color = colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                }
            }

            if (actividadReciente.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = CardDefaults.cardColors(containerColor = colorScheme.surface)
                ) {
                    Text(
                        text = "No hay actividad reciente.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = CardDefaults.cardColors(containerColor = colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    actividadReciente.forEachIndexed { index, (tipo, label, fecha) ->
                        if (index > 0) HorizontalDivider(color = colorScheme.outlineVariant, thickness = 0.5.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar inicial
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tipo.take(1),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (tipo == "COT") "Cotización" else "Orden de trabajo",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                    color = colorScheme.onSurface
                                )
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = fecha,
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Botones de acceso rápido
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick  = { onNavigateCotizaciones(dueño.id_dueño) },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor   = Color.White
                    )
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Cotizaciones", style = MaterialTheme.typography.labelLarge)
                }
                Button(
                    onClick  = { onNavigateOrdenes(dueño.id_dueño) },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor   = Color.White
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_orders),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Órdenes", style = MaterialTheme.typography.labelLarge)
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick  = onExit,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = colorScheme.error)
            ) {
                Text("Salir", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun DashboardCard2(
    icon: Int,
    iconBgColor: Color,
    iconTint: Color,
    value: String,
    valueColor: Color,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter     = painterResource(id = icon),
                    contentDescription = title,
                    tint        = iconTint,
                    modifier    = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text  = value,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 28.sp
                ),
                color = valueColor
            )
            Text(
                text  = title,
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.onSurface
            )
            Text(
                text  = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ServiceCard(title: String, description: String, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = colorScheme.primary)
            Spacer(Modifier.height(4.dp))
            Text(description, style = MaterialTheme.typography.bodyMedium, color = colorScheme.onSurfaceVariant)
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
        modifier  = modifier,
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter     = painterResource(id = icon),
                contentDescription = title,
                modifier    = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text  = value,
                style = MaterialTheme.typography.headlineMedium,
                color = valueColor
            )
            Text(
                text  = title,
                style = MaterialTheme.typography.labelMedium,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}
