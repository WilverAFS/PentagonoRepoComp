package com.ingsoftware.pentagono

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ingsoftware.pentagono.model.*
import com.ingsoftware.pentagono.ui.theme.PentagonoTheme
import com.ingsoftware.pentagono.view.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PentagonoTheme {
                val navController = rememberNavController() // ✅ inicialización correcta

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
                    composable("cotizaciones") {
                        CotizacionScreen(
                            onBack = { navController.popBackStack() },
                            onAddCotizacion = { navController.navigate("nuevaCotizacion") },
                            onSearchCotizacion = { /* lógica de búsqueda */ },
                            onEditCotizacion = { cotizacion ->
                                navController.navigate("editarCotizacion/${cotizacion.id_cotizacion}")
                            }
                        )
                    }

                    composable("ordenes") {
                        OrdenesScreen(
                            onBack = { navController.popBackStack() },
                            onAddOrden = { navController.navigate("nuevaOrden") },
                            onSearchOrden = { /* lógica de búsqueda */ },
                            onEditOrden = { orden ->
                                navController.navigate("editarOrden/${orden.id_orden}")
                            }
                        )
                    }

                    composable("clientes") {
                        ClientesScreen(
                            onBack = { navController.popBackStack() },
                            onAddCliente = { /* lógica para agregar cliente */ },
                            onSearchCliente = { /* lógica para buscar cliente */ },
                            onEditCliente = { cliente ->
                                // lógica para editar cliente
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("empleados") {
                        EmpleadosScreen(
                            onBack = { navController.popBackStack() },
                            onAddEmpleado = {
                                // lógica para agregar empleado
                                // por ahora puede ser un navController.navigate("nuevoEmpleado")
                            },
                            onSearchEmpleado = {
                                // lógica para buscar empleado
                            },
                            onEditEmpleado = { empleado ->
                                // lógica para editar empleado
                                // ejemplo: navController.navigate("editarEmpleado/${empleado.id_empleado}")
                            }
                        )
                    }

                    composable("logs") {
                        LogsScreen(
                            onBack = { navController.popBackStack() },
                            onSearchLog = { /* lógica de búsqueda de logs */ }
                        )
                    }

                    composable("configuracion") {
                        ConfiguracionScreen(
                            dueñoActual = Dueño(1, "Administrador", "admin123"), // se pasará dinámicamente al iniciar sesión
                            dueños = listOf(
                                Dueño(2, "Carlos", "pass123"),
                                Dueño(3, "Ana", "clave456")
                            ),
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
