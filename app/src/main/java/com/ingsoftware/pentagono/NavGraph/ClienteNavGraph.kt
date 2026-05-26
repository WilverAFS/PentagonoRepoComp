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
import com.ingsoftware.pentagono.view.BuscarClienteScreen
import com.ingsoftware.pentagono.view.ClientesScreen
import com.ingsoftware.pentagono.view.EditarClienteScreen
import com.ingsoftware.pentagono.view.NuevoClienteScreen
import com.ingsoftware.pentagono.viewmodel.ClienteViewModel
import com.ingsoftware.pentagono.viewmodel.CotizacionViewModel
import com.ingsoftware.pentagono.viewmodel.LogViewModel
import com.ingsoftware.pentagono.model.TipoLog

fun NavGraphBuilder.clienteNavGraph(
    navController: NavController,
    clienteVM: ClienteViewModel,
    cotizacionVM: CotizacionViewModel,
    logVM: LogViewModel   // ✅ nuevo parámetro
) {
    // 📌 Lista de clientes
    composable("clientes/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        ClientesScreen(
            viewModel = clienteVM,
            onBack = { navController.popBackStack() },
            onAddCliente = { navController.navigate("nuevoCliente/$dueñoId") },
            onSearchCliente = { navController.navigate("buscarCliente/$dueñoId") },
            onEditCliente = { cliente -> navController.navigate("editarCliente/${cliente.telefono}/$dueñoId") },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Buscar cliente
    composable("buscarCliente/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        BuscarClienteScreen(
            viewModel = clienteVM,
            onBack = { navController.popBackStack() },
            onEditCliente = { cliente ->
                navController.navigate("editarCliente/${cliente.telefono}/$dueñoId")
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Nuevo cliente
    composable("nuevoCliente/{dueñoId}") { backStackEntry ->
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        NuevoClienteScreen(
            viewModel = clienteVM,
            onBack = { navController.popBackStack() },
            onSaveSuccess = {
                // ✅ Registrar log de tipo ADD usando .value
                val nuevo = clienteVM.clientes.value.lastOrNull()
                if (nuevo != null) {
                    logVM.registrarLog(
                        idDueño = dueñoId,
                        tipo = TipoLog.ADD,
                        descripcion = "Cliente agregado: ${nuevo.nombre} (Tel: ${nuevo.telefono})"
                    )
                }
                navController.popBackStack()
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Nuevo cliente con teléfono prellenado
    composable("nuevoCliente/{telefono}/{dueñoId}") { backStackEntry ->
        val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        NuevoClienteScreen(
            viewModel = clienteVM,
            prefilledTelefono = telefono,
            onBack = { navController.popBackStack() },
            onSaveSuccess = {
                // ✅ Registrar log de tipo ADD usando .value
                val nuevo = clienteVM.clientes.value.lastOrNull()
                if (nuevo != null) {
                    logVM.registrarLog(
                        idDueño = dueñoId,
                        tipo = TipoLog.ADD,
                        descripcion = "Cliente agregado: ${nuevo.nombre} (Tel: ${nuevo.telefono})"
                    )
                }
                navController.navigate("nuevaCotizacion?telefono=$telefono/$dueñoId") {
                    popUpTo("nuevoCliente/$telefono/$dueñoId") { inclusive = true }
                }
            },
            onMenuClick = { navController.navigateIfNotCurrent("menu/$dueñoId") }
        )
    }

    // 📌 Editar cliente
    composable("editarCliente/{telefono}/{dueñoId}") { backStackEntry ->
        val telefono = backStackEntry.arguments?.getString("telefono") ?: ""
        val dueñoId = backStackEntry.arguments?.getString("dueñoId")?.toIntOrNull() ?: 0
        val clientes by clienteVM.clientes.collectAsState()
        val cliente = clientes.find { it.telefono == telefono }

        if (cliente != null) {
            EditarClienteScreen(
                viewModel = clienteVM,
                cliente = cliente,
                onBack = {
                    // ✅ Registrar log de tipo UPDATE
                    logVM.registrarLog(
                        idDueño = dueñoId,
                        tipo = TipoLog.UPDATE,
                        descripcion = "Cliente actualizado: ${cliente.nombre} (Tel: ${cliente.telefono})"
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
