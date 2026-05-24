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
import com.ingsoftware.pentagono.viewmodel.DueñoViewModel
import com.ingsoftware.pentagono.view.BuscarDueñoScreen
import com.ingsoftware.pentagono.view.NuevoDueñoScreen
import com.ingsoftware.pentagono.view.EditarDueñoScreen
import com.ingsoftware.pentagono.view.ConfiguracionScreen

fun NavGraphBuilder.dueñoNavGraph(
    navController: NavController,
    dueñoVM: DueñoViewModel
) {
    // 📌 Configuración del dueño autenticado
    composable("configuracion/{id}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
        val dueños by dueñoVM.dueños.collectAsState()
        val dueño = dueños.find { it.id_dueño == id }

        if (dueño != null) {
            ConfiguracionScreen(
                viewModel = dueñoVM,
                dueñoActual = dueño,
                onBack = { navController.popBackStack() },
                onAddDueño = { navController.navigate("nuevoDueño/$id") },
                onSearchDueño = { navController.navigate("buscarDueño/$id") },
                onEditDueño = { dueno -> navController.navigate("editarDueño/${dueno.id_dueño}/$id") },
                onMenuClick = { navController.navigateIfNotCurrent("menu/$id") } // ✅ menú → MenuScreen
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }

    // 📌 Buscar dueño
    composable("buscarDueño/{id}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
        BuscarDueñoScreen(
            viewModel = dueñoVM,
            onBack = { navController.popBackStack() },
            onEditDueño = { dueño ->
                navController.navigate("editarDueño/${dueño.id_dueño}/$id")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$id") } // ✅ menú → MenuScreen
        )
    }

    // 📌 Nuevo dueño
    composable("nuevoDueño/{id}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
        NuevoDueñoScreen(
            viewModel = dueñoVM,
            onBack = { navController.popBackStack() },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$id") } // ✅ menú → MenuScreen
        )
    }

    // 📌 Editar dueño
    composable("editarDueño/{id}/{dueñoId}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull()
        val dueños by dueñoVM.dueños.collectAsState()
        val dueño = dueños.find { it.id_dueño == id }

        if (dueño != null) {
            EditarDueñoScreen(
                viewModel = dueñoVM,
                dueño = dueño,
                onBack = { navController.popBackStack() },
                onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
