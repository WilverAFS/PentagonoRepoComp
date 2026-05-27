package com.ingsoftware.pentagono.NavGraph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ingsoftware.pentagono.navigateIfNotCurrent
import com.ingsoftware.pentagono.viewmodel.ClienteViewModel
import com.ingsoftware.pentagono.viewmodel.CotizacionViewModel
import com.ingsoftware.pentagono.viewmodel.LogViewModel
import com.ingsoftware.pentagono.viewmodel.OrdenViewModel
import com.ingsoftware.pentagono.view.CotizacionScreen
import com.ingsoftware.pentagono.view.NuevaCotizacionScreen
import com.ingsoftware.pentagono.view.BuscarCotizacionScreen
import com.ingsoftware.pentagono.view.DetalleCotizacionScreen
import com.ingsoftware.pentagono.model.TipoLog

fun NavGraphBuilder.cotizacionNavGraph(
    navController: NavController,
    cotizacionVM: CotizacionViewModel,
    clienteVM: ClienteViewModel,
    logVM: LogViewModel,
    ordenVM: OrdenViewModel
) {
    // 📌 Lista de cotizaciones
    composable("cotizaciones/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        val cotizaciones by cotizacionVM.cotizaciones.collectAsState()
        val ordenes by ordenVM.ordenes.collectAsState()

        // Diagnóstico global
        cotizaciones.filter { it.estado_cotizacion == "aceptado" }.forEach { c ->
            val existeOrden = ordenes.any { it.id_cotizacion == c.id_cotizacion }
            if (!existeOrden) {
                cotizacionVM.updateCotizacion(c.copy(estado_cotizacion = "pendiente"))
                logVM.registrarLog(
                    idDueño = dueñoId,
                    tipo = TipoLog.UPDATE,
                    descripcion = "Cotización ${c.id_cotizacion} restablecida a pendiente por no tener orden asociada"
                )
            }
        }

        CotizacionScreen(
            viewModel = cotizacionVM,
            onAddCotizacion = { navController.navigate("nuevaCotizacion/$dueñoId") },
            onSearchCotizacion = { navController.navigate("buscarCotizacion/$dueñoId") },
            onOpenCotizacion = { cotizacion ->
                navController.navigate("detalleCotizacion/${cotizacion.id_cotizacion}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Detalle de cotización
    composable("detalleCotizacion/{id}/{dueñoId}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        val cotizaciones by cotizacionVM.cotizaciones.collectAsState()
        val ordenes by ordenVM.ordenes.collectAsState()
        val cotizacion = cotizaciones.find { it.id_cotizacion == id }

        if (cotizacion != null) {
            val existeOrden = ordenes.any { it.id_cotizacion == cotizacion.id_cotizacion }
            if (cotizacion.estado_cotizacion == "aceptado" && !existeOrden) {
                cotizacionVM.updateCotizacion(cotizacion.copy(estado_cotizacion = "pendiente"))
                logVM.registrarLog(
                    idDueño = dueñoId,
                    tipo = TipoLog.UPDATE,
                    descripcion = "Cotización ${cotizacion.id_cotizacion} restablecida a pendiente por no tener orden asociada"
                )
            }

            DetalleCotizacionScreen(
                cotizacion = cotizacion,
                onUpdateEstadoCotizacion = { c, nuevoEstado ->
                    cotizacionVM.updateCotizacion(c.copy(estado_cotizacion = nuevoEstado))
                    logVM.registrarLog(
                        idDueño = dueñoId,
                        tipo = TipoLog.UPDATE,
                        descripcion = "Cotización ${c.id_cotizacion} actualizada a estado $nuevoEstado"
                    )
                    if (nuevoEstado.equals("aceptado", ignoreCase = true)) {
                        navController.navigate("nuevaOrden/${c.id_cotizacion}/$dueñoId")
                    }
                },
                onUpdateEstadoPago = { c, nuevoEstado ->
                    cotizacionVM.updateCotizacion(c.copy(estado_pago = nuevoEstado))
                    logVM.registrarLog(
                        idDueño = dueñoId,
                        tipo = TipoLog.UPDATE,
                        descripcion = "Cotización ${c.id_cotizacion} pago actualizado a $nuevoEstado"
                    )
                },
                onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    // 📌 Nueva cotización
    composable("nuevaCotizacion/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        NuevaCotizacionScreen(
            viewModel = cotizacionVM,
            clienteViewModel = clienteVM,
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") },
            onCancel = { navController.popBackStack() },
            onSaveSuccess = {
                // ✅ Registrar log al agregar nueva cotización
                logVM.registrarLog(
                    idDueño = dueñoId,
                    tipo = TipoLog.ADD,
                    descripcion = "Se agregó una nueva cotización"
                )
                navController.popBackStack()
            }
        )
    }

    // 📌 Buscar cotización
    composable("buscarCotizacion/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        BuscarCotizacionScreen(
            viewModel = cotizacionVM,
            onOpenCotizacion = { cotizacion ->
                navController.navigate("detalleCotizacion/${cotizacion.id_cotizacion}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }
}
