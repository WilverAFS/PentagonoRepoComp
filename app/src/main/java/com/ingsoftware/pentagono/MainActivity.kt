package com.ingsoftware.pentagono

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ingsoftware.pentagono.ui.theme.PentagonoTheme
import com.ingsoftware.pentagono.view.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PentagonoTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(onLoginSuccess = { navController.navigate("start") })
                    }
                    composable("start") {
                        StartScreen(onMenuClick = { navController.navigate("menu") },
                            onExit = {finish()}
                            )
                    }
                    composable("menu") {
                        MenuScreen(
                            onNavigate = { destino ->
                                navController.navigate(destino)
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("cotizaciones") {
                        CotizacionScreen(
                            onBack = { navController.popBackStack() },
                            onSave = { navController.popBackStack() }
                        )
                    }
                    composable("ordenes") {
                        OrdenesScreen(
                            onBack = { navController.popBackStack() },
                            onSave = { navController.popBackStack() }
                        )
                    }
                    composable("clientes") {
                        ClientesScreen(
                            onBack = { navController.popBackStack() },
                            onSave = { navController.popBackStack() }
                        )
                    }
                    composable("empleados") {
                        EmpleadosScreen(
                            onBack = { navController.popBackStack() },
                            onSave = { navController.popBackStack() }
                        )
                    }
                    composable("logs") {
                        LogsScreen(
                            onBack = { navController.popBackStack() },
                            onClearLogs = { /* lógica para limpiar logs */ }
                        )
                    }
                    composable("configuracion") {
                        ConfiguracionScreen(
                            onBack = { navController.popBackStack() },
                            onSave = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
