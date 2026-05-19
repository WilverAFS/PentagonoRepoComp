package com.ingsoftware.pentagono.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.R

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(32.dp),
        verticalArrangement = Arrangement.Top, // Logo y título arriba
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        // Logo corporativo centrado arriba
        Image(
            painter = painterResource(id = R.drawable.ic_logo_vidrieria),
            contentDescription = "Logo Vidriería Pentágono",
            modifier = Modifier
                .size(250.dp)
                .padding(bottom = 24.dp)
        )

        // Título
        Text(
            "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            color = colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        // Campo usuario
        OutlinedTextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = false
            },
            label = { Text("Usuario") },
            isError = usernameError
        )
        if (usernameError) {
            Text(
                "El usuario es obligatorio",
                color = colorScheme.error,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(Modifier.height(12.dp))

        // Campo contraseña
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError
        )
        if (passwordError) {
            Text(
                "La contraseña es obligatoria",
                color = colorScheme.error,
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(Modifier.height(24.dp))

        // Botón ingresar
        Button(
            onClick = {
                //----------------------------------------------
                //onLoginSuccess()//solo PRUEBAS BORRAR AL FINAL
                //-----------------------------------------------
                when {
                    username.isBlank() -> {
                        usernameError = true
                        errorMessage = "Debes ingresar el usuario"
                    }
                    password.isBlank() -> {
                        passwordError = true
                        errorMessage = "Debes ingresar la contraseña"
                    }
                    username == "admin" && password == "admin_123" -> {
                        errorMessage = ""
                        onLoginSuccess()
                    }
                    else -> {
                        errorMessage = "Credenciales inválidas"

                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
        ) {
            Text("Ingresar", color = colorScheme.onSecondary)
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                errorMessage,
                color = colorScheme.error,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        LoginScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        LoginScreen()
    }
}
