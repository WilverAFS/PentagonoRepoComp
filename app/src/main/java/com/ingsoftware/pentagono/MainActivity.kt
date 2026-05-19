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
                val db = Room.databaseBuilder(context, AppDatabase::class.java, "pentagono_db").build()

                // ✅ Instanciamos los ViewModels una sola vez y los compartimos
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
                        LoginScreen(onLoginSuccess = { navController.navigate("start") })
                    }
                    composable("start") {
                        StartScreen(
                            onMenuClick = { navController.navigate("menu") },
                            onExit = { finish() }
                        )
                    }
                    composable("menu") {
                        MenuScreen(
                            onNavigate = { destino -> navController.navigate(destino) },
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // ✅ Clientes
                    composable("clientes") {
                        ClientesScreen(
                            viewModel = clienteViewModel,
                            onBack = { navController.popBackStack() },
                            onAddCliente = { navController.navigate("nuevoCliente") },
                            onSearchCliente = { navController.navigate("buscarCliente") },
                            onEditCliente = { cliente ->
                                navController.navigate("editarCliente/${cliente.id_cliente}")
                            }
                        )
                    }

                    composable("buscarCliente") {
                        BuscarClienteScreen(
                            viewModel = clienteViewModel,
                            onBack = { navController.popBackStack() },
                            onEditCliente = { cliente ->
                                navController.navigate("editarCliente/${cliente.id_cliente}")
                            }
                        )
                    }

                    composable("nuevoCliente") {
                        NuevoClienteScreen(
                            viewModel = clienteViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("editarCliente/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        val clientes by clienteViewModel.clientes.collectAsState()
                        val cliente = clientes.find { it.id_cliente == id }

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

                    // ✅ Empleados
                    composable("empleados") {
                        EmpleadosScreen(
                            viewModel = empleadoViewModel,
                            onBack = { navController.popBackStack() },
                            onAddEmpleado = { navController.navigate("nuevoEmpleado") },
                            onSearchEmpleado = { /* lógica de búsqueda */ },
                            onEditEmpleado = { empleado ->
                                navController.navigate("editarEmpleado/${empleado.id_empleado}")
                            }
                        )
                    }

                    // ✅ Cotizaciones
                    composable("cotizaciones") {
                        CotizacionScreen(
                            viewModel = cotizacionViewModel,
                            onBack = { navController.popBackStack() },
                            onAddCotizacion = { navController.navigate("nuevaCotizacion") },
                            onSearchCotizacion = { /* lógica de búsqueda */ },
                            onEditCotizacion = { cotizacion ->
                                navController.navigate("editarCotizacion/${cotizacion.id_cotizacion}")
                            }
                        )
                    }

                    // ✅ Órdenes
                    composable("ordenes") {
                        OrdenesScreen(
                            viewModel = ordenViewModel,
                            onBack = { navController.popBackStack() },
                            onAddOrden = { navController.navigate("nuevaOrden") },
                            onSearchOrden = { /* lógica de búsqueda */ },
                            onEditOrden = { orden ->
                                navController.navigate("editarOrden/${orden.id_orden}")
                            }
                        )
                    }

                    // ✅ Logs
                    composable("logs") {
                        LogsScreen(
                            viewModel = logViewModel,
                            onBack = { navController.popBackStack() },
                            onSearchLog = { /* lógica de búsqueda de logs */ }
                        )
                    }

                    // ✅ Configuración (Dueños)
                    composable("configuracion") {
                        val dueñoActual = DueñoEntity(1, "Administrador", "admin123")
                        ConfiguracionScreen(
                            viewModel = dueñoViewModel,
                            dueñoActual = dueñoActual,
                            onBack = { navController.popBackStack() },
                            onAddDueño = { navController.navigate("nuevoDueño") },
                            onSearchDueño = { /* lógica de búsqueda */ },
                            onEditDueño = { dueño ->
                                navController.navigate("editarDueño/${dueño.id_dueño}")
                            }
                        )
                    }
                }
            }
        }
    }
}
