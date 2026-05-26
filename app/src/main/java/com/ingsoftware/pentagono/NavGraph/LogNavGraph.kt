package com.ingsoftware.pentagono.NavGraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ingsoftware.pentagono.navigateIfNotCurrent
import com.ingsoftware.pentagono.viewmodel.LogViewModel
import com.ingsoftware.pentagono.view.LogsScreen
// import com.ingsoftware.pentagono.view.BuscarLogScreen  // 🔒 aún no implementado

fun NavGraphBuilder.logNavGraph(
    navController: NavController,
    logVM: LogViewModel
) {
    // 📌 Lista de logs
    composable("logs/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        LogsScreen(
            viewModel = logVM,
            onBack = { navController.popBackStack() },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }

    // 📌 Buscar log (comentado hasta que se implemente)
    /*
    composable("buscarLog/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        BuscarLogScreen(
            viewModel = logVM,
            onBack = { navController.popBackStack() },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }
    */
}
