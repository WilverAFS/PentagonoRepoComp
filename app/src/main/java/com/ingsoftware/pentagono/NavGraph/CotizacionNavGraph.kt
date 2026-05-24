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
import com.ingsoftware.pentagono.view.CotizacionScreen
import com.ingsoftware.pentagono.view.NuevaCotizacionScreen
import com.ingsoftware.pentagono.view.BuscarCotizacionScreen
import com.ingsoftware.pentagono.view.DetalleCotizacionScreen

fun NavGraphBuilder.cotizacionNavGraph(
    navController: NavController,
    cotizacionVM: CotizacionViewModel,
    clienteVM: ClienteViewModel
) {
    // 📌 Lista de cotizaciones
    composable("cotizaciones/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        CotizacionScreen(
            viewModel = cotizacionVM,
            onBack = { navController.popBackStack() },
            onAddCotizacion = { navController.navigate("nuevaCotizacion/$dueñoId") },
            onSearchCotizacion = { navController.navigate("buscarCotizacion/$dueñoId") },
            onOpenCotizacion = { cotizacion ->
                navController.navigate("detalleCotizacion/${cotizacion.id_cotizacion}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }

    // 📌 Detalle de cotización
    composable("detalleCotizacion/{id}/{dueñoId}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        val cotizaciones by cotizacionVM.cotizaciones.collectAsState()
        val cotizacion = cotizaciones.find { it.id_cotizacion == id }

        if (cotizacion != null) {
            DetalleCotizacionScreen(
                cotizacion = cotizacion,
                onUpdateEstadoCotizacion = { c, nuevoEstado ->
                    cotizacionVM.updateCotizacion(c.copy(estado_cotizacion = nuevoEstado))
                },
                onUpdateEstadoPago = { c, nuevoEstado ->
                    cotizacionVM.updateCotizacion(c.copy(estado_pago = nuevoEstado))
                },
                onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }


    // 📌 Nueva cotización
    composable("nuevaCotizacion/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        NuevaCotizacionScreen(
            viewModel = cotizacionVM,
            clienteViewModel = clienteVM,
            onBack = { navController.popBackStack() },
            onSaveSuccess = { navController.popBackStack() },
            onAddCliente = { telefono -> navController.navigate("nuevoCliente/$telefono/$dueñoId") },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }

    // 📌 Nueva cotización con teléfono prellenado
    composable("nuevaCotizacion?telefono={telefono}/{dueñoId}") { backStackEntry ->
        val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        NuevaCotizacionScreen(
            viewModel = cotizacionVM,
            clienteViewModel = clienteVM,
            prefilledTelefono = telefono,
            onBack = { navController.popBackStack() },
            onSaveSuccess = { navController.popBackStack() },
            onAddCliente = { tel -> navController.navigate("nuevoCliente/$tel/$dueñoId") },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }

    // 📌 Buscar cotización
    composable("buscarCotizacion/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        BuscarCotizacionScreen(
            viewModel = cotizacionVM,
            onBack = { navController.popBackStack() },
            onOpenCotizacion = { cotizacion ->
                navController.navigate("detalleCotizacion/${cotizacion.id_cotizacion}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }
}
