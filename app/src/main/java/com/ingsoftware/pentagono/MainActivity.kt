package com.ingsoftware.pentagono

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.ingsoftware.pentagono.data.*
import com.ingsoftware.pentagono.viewmodel.*
import com.ingsoftware.pentagono.model.*
import com.ingsoftware.pentagono.ui.theme.PentagonoTheme
import com.ingsoftware.pentagono.view.*

class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PentagonoTheme {
                val navController = rememberNavController()

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

                    //Integración con Room y ViewModel para Clientes
                    composable("clientes") {
                        val context = LocalContext.current
                        val db = Room.databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            "pentagono_db"
                        ).build()
                        val repository = ClienteRepository(db.clienteDao())
                        val viewModel = ClienteViewModel(repository)

                        ClientesScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onAddCliente = { navController.navigate("nuevoCliente") },
                            onSearchCliente = { /* lógica para buscar cliente */ },
                            onEditCliente = { cliente ->
                                navController.navigate("editarCliente/${cliente.id_cliente}")
                            }
                        )
                    }

                    composable("empleados") {
                        val context = LocalContext.current
                        val db = Room.databaseBuilder(context, AppDatabase::class.java, "pentagono_db").build()
                        val repository = EmpleadoRepository(db.empleadoDao())
                        val viewModel = EmpleadoViewModel(repository)

                        EmpleadosScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onAddEmpleado = { navController.navigate("nuevoEmpleado") },
                            onSearchEmpleado = { /* lógica de búsqueda */ },
                            onEditEmpleado = { empleado ->
                                navController.navigate("editarEmpleado/${empleado.id_empleado}")
                            }
                        )
                    }


                    composable("cotizaciones") {
                        val context = LocalContext.current
                        val db = Room.databaseBuilder(context, AppDatabase::class.java, "pentagono_db").build()
                        val repository = CotizacionRepository(db.cotizacionDao())
                        val viewModel = CotizacionViewModel(repository)

                        CotizacionScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onAddCotizacion = { navController.navigate("nuevaCotizacion") },
                            onSearchCotizacion = { /* lógica de búsqueda */ },
                            onEditCotizacion = { cotizacion ->
                                navController.navigate("editarCotizacion/${cotizacion.id_cotizacion}")
                            }
                        )
                    }


                    composable("ordenes") {
                        val context = LocalContext.current
                        val db = Room.databaseBuilder(context, AppDatabase::class.java, "pentagono_db").build()
                        val repository = OrdenRepository(db.ordenDao())
                        val viewModel = OrdenViewModel(repository)

                        OrdenesScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onAddOrden = { navController.navigate("nuevaOrden") },
                            onSearchOrden = { /* lógica de búsqueda */ },
                            onEditOrden = { orden ->
                                navController.navigate("editarOrden/${orden.id_orden}")
                            }
                        )
                    }


                    composable("logs") {
                        val context = LocalContext.current
                        val db = Room.databaseBuilder(context, AppDatabase::class.java, "pentagono_db").build()
                        val repository = LogRepository(db.logDao())
                        val viewModel = LogViewModel(repository)

                        LogsScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() },
                            onSearchLog = { /* lógica de búsqueda de logs */ }
                        )
                    }


                    composable("configuracion") {
                        val context = LocalContext.current
                        val db = Room.databaseBuilder(context, AppDatabase::class.java, "pentagono_db").build()
                        val repository = DueñoRepository(db.dueñoDao())
                        val viewModel = DueñoViewModel(repository)

                        // El dueño actual se obtiene del login, aquí lo ponemos fijo como ejemplo
                        val dueñoActual = DueñoEntity(1, "Administrador", "admin123")

                        ConfiguracionScreen(
                            viewModel = viewModel,
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
