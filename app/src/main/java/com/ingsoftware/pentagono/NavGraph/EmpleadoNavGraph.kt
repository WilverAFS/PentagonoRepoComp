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
import com.ingsoftware.pentagono.viewmodel.LogViewModel
import com.ingsoftware.pentagono.view.EmpleadosScreen
import com.ingsoftware.pentagono.view.NuevoEmpleadoScreen
import com.ingsoftware.pentagono.view.BuscarEmpleadoScreen
import com.ingsoftware.pentagono.view.EditarEmpleadoScreen
import com.ingsoftware.pentagono.model.TipoLog

fun NavGraphBuilder.empleadoNavGraph(
    navController: NavController,
    empleadoVM: EmpleadoViewModel,
    logVM: LogViewModel   // ✅ nuevo parámetro
) {
    // 📌 Lista de empleados
    composable("empleados/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        EmpleadosScreen(
            viewModel = empleadoVM,
            onBack = { navController.popBackStack() },
            onAddEmpleado = { navController.navigate("nuevoEmpleado/$dueñoId") },
            onSearchEmpleado = { navController.navigate("buscarEmpleado/$dueñoId") },
            onEditEmpleado = { empleado ->
                navController.navigate("editarEmpleado/${empleado.id_empleado}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Nuevo empleado
    composable("nuevoEmpleado/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        NuevoEmpleadoScreen(
            viewModel = empleadoVM,
            onBack = { navController.popBackStack() },
            onSaveSuccess = {
                // ✅ Registrar log de tipo ADD usando .value
                val nuevo = empleadoVM.empleados.value.lastOrNull()
                if (nuevo != null) {
                    logVM.registrarLog(
                        idDueño = dueñoId,
                        tipo = TipoLog.ADD,
                        descripcion = "Empleado agregado: ${nuevo.nombre} (CURP: ${nuevo.curp})"
                    )
                }
                navController.popBackStack()
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Buscar empleado
    composable("buscarEmpleado/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        BuscarEmpleadoScreen(
            viewModel = empleadoVM,
            onBack = { navController.popBackStack() },
            onEditEmpleado = { empleado ->
                navController.navigate("editarEmpleado/${empleado.id_empleado}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Editar empleado
    composable("editarEmpleado/{id}/{dueñoId}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        val empleados by empleadoVM.empleados.collectAsState()
        val empleado = empleados.find { it.id_empleado == id }

        if (empleado != null) {
            EditarEmpleadoScreen(
                viewModel = empleadoVM,
                empleado = empleado,
                onBack = {
                    // ✅ Registrar log de tipo UPDATE
                    logVM.registrarLog(
                        idDueño = dueñoId,
                        tipo = TipoLog.UPDATE,
                        descripcion = "Empleado actualizado: ${empleado.nombre} (CURP: ${empleado.curp})"
                    )
                    navController.popBackStack()
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
