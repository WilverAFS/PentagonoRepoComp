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
import com.ingsoftware.pentagono.view.OrdenesScreen
// import com.ingsoftware.pentagono.view.NuevaOrdenScreen
// import com.ingsoftware.pentagono.view.BuscarOrdenScreen
// import com.ingsoftware.pentagono.view.EditarOrdenScreen

fun NavGraphBuilder.ordenNavGraph(
    navController: NavController,
    ordenVM: OrdenViewModel
) {
    // 📌 Lista de órdenes
    composable("ordenes/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        OrdenesScreen(
            viewModel = ordenVM,
            onBack = { navController.popBackStack() },
            onAddOrden = { navController.navigate("nuevaOrden/$dueñoId") },   // 🔒 aún no implementado
            onSearchOrden = { navController.navigate("buscarOrden/$dueñoId") }, // 🔒 aún no implementado
            onEditOrden = { orden ->
                navController.navigate("editarOrden/${orden.id_orden}/$dueñoId") // 🔒 aún no implementado
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }

    // 📌 Nueva orden (comentado hasta que se implemente)
    /*
    composable("nuevaOrden/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        NuevaOrdenScreen(
            viewModel = ordenVM,
            onBack = { navController.popBackStack() },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }
    */

    // 📌 Buscar orden (comentado hasta que se implemente)
    /*
    composable("buscarOrden/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        BuscarOrdenScreen(
            viewModel = ordenVM,
            onBack = { navController.popBackStack() },
            onEditOrden = { orden ->
                navController.navigate("editarOrden/${orden.id_orden}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }
    */

    // 📌 Editar orden (comentado hasta que se implemente)
    /*
    composable("editarOrden/{id}/{dueñoId}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        val ordenes by ordenVM.ordenes.collectAsState()
        val orden = ordenes.find { it.id_orden == id }

        if (orden != null) {
            EditarOrdenScreen(
                viewModel = ordenVM,
                orden = orden,
                onBack = { navController.popBackStack() },
                onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
    */
}
