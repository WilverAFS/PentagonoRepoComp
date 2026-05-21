package com.ingsoftware.pentagono

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.ingsoftware.pentagono.data.*
import com.ingsoftware.pentagono.viewmodel.*
import com.ingsoftware.pentagono.view.*
import com.ingsoftware.pentagono.ui.theme.PentagonoTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PentagonoTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "pentagono_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                val clienteViewModel = ClienteViewModel(ClienteRepository(db.clienteDao()))
                val empleadoViewModel = EmpleadoViewModel(EmpleadoRepository(db.empleadoDao()))
                val cotizacionViewModel = CotizacionViewModel(CotizacionRepository(db.cotizacionDao()))
                val ordenViewModel = OrdenViewModel(OrdenRepository(db.ordenDao()))
                val logViewModel = LogViewModel(LogRepository(db.logDao()))
                val dueñoViewModel = DueñoViewModel(DueñoRepository(db.dueñoDao()))

                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            viewModel = dueñoViewModel,
                            onLoginSuccess = { dueño ->
                                navController.navigate("start/${dueño.id_dueño}")
                            }
                        )
                    }
                    composable("start/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        val dueños by dueñoViewModel.dueños.collectAsState()
                        val dueño = dueños.find { it.id_dueño == id }

                        if (dueño != null) {
                            StartScreen(
                                dueño = dueño,
                                onMenuClick = { idDueño -> navController.navigate("menu/$idDueño") },
                                onExit = { finish() }
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    composable("menu/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        if (id != null) {
                            MenuScreen(
                                dueñoId = id,
                                onNavigate = { destino -> navController.navigate(destino) },
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                    composable("clientes") {
                        ClientesScreen(
                            viewModel = clienteViewModel,
                            onBack = { navController.popBackStack() },
                            onAddCliente = { navController.navigate("nuevoCliente") },
                            onSearchCliente = { navController.navigate("buscarCliente") },
                            onEditCliente = { cliente ->
                                navController.navigate("editarCliente/${cliente.telefono}")
                            }
                        )
                    }
                    composable("buscarCliente") {
                        BuscarClienteScreen(
                            viewModel = clienteViewModel,
                            onBack = { navController.popBackStack() },
                            onEditCliente = { cliente ->
                                navController.navigate("editarCliente/${cliente.telefono}")
                            }
                        )
                    }
                    composable("nuevoCliente") {
                        NuevoClienteScreen(
                            viewModel = clienteViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("editarCliente/{telefono}") { backStackEntry ->
                        val telefono = backStackEntry.arguments?.getString("telefono")?.toIntOrNull()
                        val clientes by clienteViewModel.clientes.collectAsState()
                        val cliente = clientes.find { it.telefono == telefono }

                        if (cliente != null) {
                            EditarClienteScreen(
                                viewModel = clienteViewModel,
                                cliente = cliente,
                                onBack = { navController.popBackStack() }
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    composable("empleados") {
                        EmpleadosScreen(
                            viewModel = empleadoViewModel,
                            onBack = { navController.popBackStack() },
                            onAddEmpleado = { navController.navigate("nuevoEmpleado") },
                            onSearchEmpleado = { navController.navigate("buscarEmpleado") },
                            onEditEmpleado = { empleado ->
                                navController.navigate("editarEmpleado/${empleado.curp}")
                            }
                        )
                    }
                    composable("nuevoEmpleado") {
                        NuevoEmpleadoScreen(
                            viewModel = empleadoViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("buscarEmpleado") {
                        BuscarEmpleadoScreen(
                            viewModel = empleadoViewModel,
                            onBack = { navController.popBackStack() },
                            onEditEmpleado = { empleado ->
                                navController.navigate("editarEmpleado/${empleado.curp}")
                            }
                        )
                    }
                    composable("editarEmpleado/{curp}") { backStackEntry ->
                        val curp = backStackEntry.arguments?.getString("curp")
                        val empleados by empleadoViewModel.empleados.collectAsState()
                        val empleado = empleados.find { it.curp == curp }

                        if (empleado != null) {
                            EditarEmpleadoScreen(
                                viewModel = empleadoViewModel,
                                empleado = empleado,
                                onBack = { navController.popBackStack() }
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    composable("cotizaciones") {
                        CotizacionScreen(
                            viewModel = cotizacionViewModel,
                            onBack = { navController.popBackStack() },
                            onAddCotizacion = { navController.navigate("nuevaCotizacion") },
                            onSearchCotizacion = { navController.navigate("buscarCotizacion") },
                            onEditCotizacion = { cotizacion ->
                                navController.navigate("editarCotizacion/${cotizacion.id_cotizacion}")
                            }
                        )
                    }
                    composable("ordenes") {
                        OrdenesScreen(
                            viewModel = ordenViewModel,
                            onBack = { navController.popBackStack() },
                            onAddOrden = { navController.navigate("nuevaOrden") },
                            onSearchOrden = { navController.navigate("buscarOrden") },
                            onEditOrden = { orden ->
                                navController.navigate("editarOrden/${orden.id_orden}")
                            }
                        )
                    }
                    composable("logs") {
                        LogsScreen(
                            viewModel = logViewModel,
                            onBack = { navController.popBackStack() },
                            onSearchLog = { navController.navigate("buscarLog") }
                        )
                    }
                    composable("configuracion/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        val dueños by dueñoViewModel.dueños.collectAsState()
                        val dueño = dueños.find { it.id_dueño == id }

                        if (dueño != null) {
                            ConfiguracionScreen(
                                viewModel = dueñoViewModel,
                                dueñoActual = dueño, // ✅ dueño autenticado
                                onBack = { navController.popBackStack() },
                                onAddDueño = { navController.navigate("nuevoDueño") },
                                onSearchDueño = { navController.navigate("buscarDueño") },
                                onEditDueño = { dueno ->
                                    navController.navigate("editarDueño/${dueno.id_dueño}")
                                }
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    composable("buscarDueño") {
                        BuscarDueñoScreen(
                            viewModel = dueñoViewModel,
                            onBack = { navController.popBackStack() },
                            onEditDueño = { dueño ->
                                navController.navigate("editarDueño/${dueño.id_dueño}")
                            }
                        )
                    }
                    composable("nuevoDueño") {
                        NuevoDueñoScreen(
                            viewModel = dueñoViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("editarDueño/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        val dueños by dueñoViewModel.dueños.collectAsState()
                        val dueño = dueños.find { it.id_dueño == id }

                        if (dueño != null) {
                            EditarDueñoScreen(
                                viewModel = dueñoViewModel,
                                dueño = dueño,
                                onBack = { navController.popBackStack() }
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
