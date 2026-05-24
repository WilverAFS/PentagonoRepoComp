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
import com.ingsoftware.pentagono.viewmodel.EmpleadoViewModel
import com.ingsoftware.pentagono.view.EmpleadosScreen
import com.ingsoftware.pentagono.view.NuevoEmpleadoScreen
import com.ingsoftware.pentagono.view.BuscarEmpleadoScreen
import com.ingsoftware.pentagono.view.EditarEmpleadoScreen

fun NavGraphBuilder.empleadoNavGraph(
    navController: NavController,
    empleadoVM: EmpleadoViewModel
) {
    // 📌 Lista de empleados
    composable("empleados/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        EmpleadosScreen(
            viewModel = empleadoVM,
            onBack = { navController.popBackStack() },
            onAddEmpleado = { navController.navigate("nuevoEmpleado/$dueñoId") },
            onSearchEmpleado = { navController.navigate("buscarEmpleado/$dueñoId") },
            onEditEmpleado = { empleado ->
                navController.navigate("editarEmpleado/${empleado.curp}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }

    // 📌 Nuevo empleado
    composable("nuevoEmpleado/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        NuevoEmpleadoScreen(
            viewModel = empleadoVM,
            onBack = { navController.popBackStack() },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }

    // 📌 Buscar empleado
    composable("buscarEmpleado/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        BuscarEmpleadoScreen(
            viewModel = empleadoVM,
            onBack = { navController.popBackStack() },
            onEditEmpleado = { empleado ->
                navController.navigate("editarEmpleado/${empleado.curp}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") } // ✅ menú → MenuScreen
        )
    }

    // 📌 Editar empleado
    composable("editarEmpleado/{curp}/{dueñoId}") { backStackEntry ->
        val curp = backStackEntry.arguments?.getString("curp")
        val dueñoId = backStackEntry.arguments?.getString("dueñoId") ?: ""
        val empleados by empleadoVM.empleados.collectAsState()
        val empleado = empleados.find { it.curp == curp }

        if (empleado != null) {
            EditarEmpleadoScreen(
                viewModel = empleadoVM,
                empleado = empleado,
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
