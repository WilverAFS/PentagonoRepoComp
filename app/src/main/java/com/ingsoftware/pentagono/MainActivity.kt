package com.ingsoftware.pentagono

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ingsoftware.pentagono.ui.theme.PentagonoTheme
import com.ingsoftware.pentagono.view.CotizacionScreen
import com.ingsoftware.pentagono.view.LoginScreen
import com.ingsoftware.pentagono.view.MenuScreen
import com.ingsoftware.pentagono.view.StartScreen

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
                        StartScreen(onMenuClick = { navController.navigate("menu") })
                    }
                    composable("menu") {
                        MenuScreen(
                            onNavigate = { destino ->
                                navController.navigate(destino)
                            },
                            onBack = { navController.popBackStack() } // 🔹 aquí regresa a StartScreen
                        )
                    }
                    // Aquí luego agregaremos las demás pantallas: cotizaciones, órdenes, etc.

                    composable("cotizaciones") {
                        CotizacionScreen(
                            onBack = { navController.popBackStack() },
                            onSave = {
                                // Aquí puedes manejar la lógica de guardado
                                navController.popBackStack() // vuelve al menú después de guardar
                            }
                        )
                    }

                }
            }
        }
    }
}
