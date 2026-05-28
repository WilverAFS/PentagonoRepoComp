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
import com.ingsoftware.pentagono.viewmodel.OrdenViewModel
import com.ingsoftware.pentagono.viewmodel.EmpleadoViewModel
import com.ingsoftware.pentagono.viewmodel.LogViewModel
import com.ingsoftware.pentagono.view.OrdenesScreen
import com.ingsoftware.pentagono.view.NuevaOrdenScreen
import com.ingsoftware.pentagono.view.DetalleOrdenScreen
import com.ingsoftware.pentagono.view.BuscarOrdenScreen
import com.ingsoftware.pentagono.model.TipoLog

fun NavGraphBuilder.ordenNavGraph(
    navController: NavController,
    ordenVM: OrdenViewModel,
    empleadoVM: EmpleadoViewModel,
    logVM: LogViewModel   // ✅ nuevo parámetro
) {
    // 📌 Lista de órdenes
    composable("ordenes/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        OrdenesScreen(
            viewModel = ordenVM,
            onBack = { navController.popBackStack() },
            onSearchOrden = { navController.navigate("buscarOrden/$dueñoId") },
            onOpenOrden = { orden ->
                navController.navigate("detalleOrden/${orden.id_orden}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Nueva orden desde cotización aceptada
    composable("nuevaOrden/{idCotizacion}/{dueñoId}") { backStackEntry ->
        val idCotizacion = backStackEntry.arguments?.getString("idCotizacion")?.toIntOrNull() ?: 0
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        val empleados by empleadoVM.empleados.collectAsState()

        NuevaOrdenScreen(
            idCotizacion = idCotizacion,
            dueñoId = dueñoId,
            empleados = empleados,
            onSaveOrden = { orden ->
                ordenVM.addOrden(orden)

                // ✅ Registrar log de tipo ADD usando .value
                val nueva = ordenVM.ordenes.value.lastOrNull()
                if (nueva != null) {
                    logVM.registrarLog(
                        idDueño = dueñoId,
                        tipo = TipoLog.ADD,
                        descripcion = "Orden creada ID=${nueva.id_orden} desde cotización $idCotizacion"
                    )
                }
                navController.popBackStack()
            },
            onBack = { navController.popBackStack() },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Buscar orden por ID
    composable("buscarOrden/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        BuscarOrdenScreen(
            viewModel = ordenVM,
            onOpenOrden = { orden ->
                navController.navigate("detalleOrden/${orden.id_orden}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Detalle de orden
    composable("detalleOrden/{id}/{dueñoId}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        val ordenes by ordenVM.ordenes.collectAsState()
        val orden = ordenes.find { it.id_orden == id }

        if (orden != null) {
            DetalleOrdenScreen(
                orden = orden,
                onUpdateEstado = { o, nuevoEstado ->
                    ordenVM.updateOrden(o.copy(estado = nuevoEstado))

                    // ✅ Registrar log de tipo UPDATE
                    logVM.registrarLog(
                        idDueño = dueñoId,
                        tipo = TipoLog.UPDATE,
                        descripcion = "Orden ${o.id_orden} actualizada a estado $nuevoEstado"
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
}
