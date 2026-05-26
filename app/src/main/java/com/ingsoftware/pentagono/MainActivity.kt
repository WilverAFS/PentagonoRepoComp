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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.ingsoftware.pentagono.data.*
import com.ingsoftware.pentagono.viewmodel.*
import com.ingsoftware.pentagono.view.LoginScreen
import com.ingsoftware.pentagono.view.StartScreen
import com.ingsoftware.pentagono.view.MenuScreen
import com.ingsoftware.pentagono.ui.theme.PentagonoTheme
import com.ingsoftware.pentagono.NavGraph.clienteNavGraph
import com.ingsoftware.pentagono.NavGraph.empleadoNavGraph
import com.ingsoftware.pentagono.NavGraph.cotizacionNavGraph
import com.ingsoftware.pentagono.NavGraph.ordenNavGraph
import com.ingsoftware.pentagono.NavGraph.dueñoNavGraph
import com.ingsoftware.pentagono.NavGraph.logNavGraph
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

// ✅ extensión para navegación segura
fun NavController.navigateIfNotCurrent(route: String) {
    val currentRoute = currentBackStackEntry?.destination?.route
    // ✅ Si la ruta actual es la misma, no navega
    if (currentRoute != route) {
        navigate(route)
    }
}


class MainActivity : ComponentActivity() {
    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PentagonoTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                // ✅ Inicialización de DB y ViewModels
                val db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "pentagono_db"
                ).fallbackToDestructiveMigration().build()

                val clienteVM = ClienteViewModel(ClienteRepository(db.clienteDao()))
                val empleadoVM = EmpleadoViewModel(EmpleadoRepository(db.empleadoDao()))
                val cotizacionVM = CotizacionViewModel(CotizacionRepository(db.cotizacionDao()))
                val ordenVM = OrdenViewModel(OrdenRepository(db.ordenDao()))
                val logVM = LogViewModel(LogRepository(db.logDao()))
                val dueñoVM = DueñoViewModel(DueñoRepository(db.dueñoDao()))

                NavHost(navController = navController, startDestination = "login") {

                    // 📌 Login
                    composable("login") {
                        LoginScreen(
                            viewModel = dueñoVM,
                            onLoginSuccess = { dueño ->
                                navController.navigate("start/${dueño.id_dueño}")
                            }
                        )
                    }

                    // 📌 StartScreen con logo y menú
                    composable("start/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        val dueños by dueñoVM.dueños.collectAsState()
                        val dueño = dueños.find { it.id_dueño == id }

                        if (dueño != null) {
                            StartScreen(
                                dueño = dueño,
                                ordenVM = ordenVM,                  // ✅ se pasa el ViewModel de órdenes
                                cotizacionVM = cotizacionVM,        // ✅ se pasa el ViewModel de cotizaciones
                                onMenuClick = { navController.navigateIfNotCurrent("menu/${dueño.id_dueño}") },
                                onExit = { finish() },
                                onNavigateCotizaciones = { dueñoId ->
                                    navController.navigateIfNotCurrent("cotizaciones/$dueñoId")
                                },
                                onNavigateOrdenes = { dueñoId ->
                                    navController.navigateIfNotCurrent("ordenes/$dueñoId")
                                }
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }


                    // 📌 MenuScreen
                    composable("menu/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                        if (id != null) {
                            MenuScreen(
                                dueñoId = id,
                                onNavigate = { destino -> navController.navigateIfNotCurrent(destino) }
                            )
                        }
                    }

                    // 📌 Subgrafos CRUD
                    clienteNavGraph(navController, clienteVM, cotizacionVM)
                    empleadoNavGraph(navController, empleadoVM)
                    cotizacionNavGraph(navController, cotizacionVM, clienteVM)
                    ordenNavGraph(navController, ordenVM, empleadoVM)
                    dueñoNavGraph(navController, dueñoVM)
                    logNavGraph(navController, logVM)
                }
            }
        }
    }
}
