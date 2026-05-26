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
import com.ingsoftware.pentagono.view.OrdenesScreen
import com.ingsoftware.pentagono.view.NuevaOrdenScreen
import com.ingsoftware.pentagono.view.DetalleOrdenScreen
import com.ingsoftware.pentagono.view.BuscarOrdenScreen   // ✅ importamos la nueva vista

fun NavGraphBuilder.ordenNavGraph(
    navController: NavController,
    ordenVM: OrdenViewModel,
    empleadoVM: EmpleadoViewModel
) {
    // 📌 Lista de órdenes
    composable("ordenes/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        OrdenesScreen(
            viewModel = ordenVM,
            onBack = { navController.popBackStack() },
            //onAddOrden = { /* ✅ ya no navega, Snackbar se maneja en OrdenesScreen */ },
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
            onSaveOrden = { orden -> ordenVM.addOrden(orden) },
            onBack = { navController.popBackStack() },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Buscar orden por ID
    composable("buscarOrden/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        BuscarOrdenScreen(
            viewModel = ordenVM,
            onBack = { navController.popBackStack() },
            onOpenOrden = { orden ->
                navController.navigate("detalleOrden/${orden.id_orden}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Detalle de orden
    composable("detalleOrden/{id}/{dueñoId}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        val ordenes by ordenVM.ordenes.collectAsState()
        val orden = ordenes.find { it.id_orden == id }

        if (orden != null) {
            DetalleOrdenScreen(
                orden = orden,
                onUpdateEstado = { o, nuevoEstado ->
                    ordenVM.updateOrden(o.copy(estado = nuevoEstado))
                },
                onBack = { navController.popBackStack() },
                onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
