package com.ingsoftware.pentagono.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ingsoftware.pentagono.R

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Mensajes de error por campo (null = sin error)
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var cargando by remember { mutableStateOf(false) }

    val focusPassword = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val colorScheme = MaterialTheme.colorScheme

    // Lógica de autenticación extraída (igual que LoginActivity)
    fun iniciarSesion() {
        // Limpiar errores previos
        usernameError = null
        passwordError = null

        // Validar campos vacíos
        var esValido = true
        if (username.isBlank()) {
            usernameError = "Ingresa tu usuario"
            esValido = false
        }
        if (password.isBlank()) {
            passwordError = "Ingresa tu contraseña"
            esValido = false
        }
        if (!esValido) return

        cargando = true
        focusManager.clearFocus()

        // TODO: reemplazar con Retrofit / Firebase cuando se conecte la BD
        if (username == "admin" && password == "admin") {
            cargando = false
            onLoginSuccess()
        } else {
            cargando = false
            passwordError = "Usuario o contraseña incorrectos"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
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
                usernameError = null
            },
            label = { Text("Usuario") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Usuario") },
            isError = usernameError != null,
            supportingText = {
                if (usernameError != null)
                    Text(usernameError!!, color = colorScheme.error)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusPassword.requestFocus() }
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Campo contraseña
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            isError = passwordError != null,
            supportingText = {
                if (passwordError != null)
                    Text(passwordError!!, color = colorScheme.error)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { iniciarSesion() }
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusPassword)
        )

        // Link recuperar contraseña
        TextButton(
            onClick = { /* TODO: navegar a RecuperarContrasenaScreen */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("¿Olvidaste tu contraseña?", color = colorScheme.primary)
        }

        Spacer(Modifier.height(16.dp))

        // Botón ingresar
        Button(
            onClick = { iniciarSesion() },
            enabled = !cargando,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
                Text("Iniciando...", color = colorScheme.onSecondary)
            } else {
                Text("Ingresar", color = colorScheme.onSecondary)
            }
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