package com.ingsoftware.pentagono.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingsoftware.pentagono.R
import com.ingsoftware.pentagono.data.DueñoEntity
import com.ingsoftware.pentagono.ui.theme.VerdeGradEnd
import com.ingsoftware.pentagono.ui.theme.VerdeGradStart
import com.ingsoftware.pentagono.ui.theme.VerdeOscuro
import com.ingsoftware.pentagono.viewmodel.DueñoViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: DueñoViewModel,
    onLoginSuccess: (DueñoEntity) -> Unit = {}
) {
    var username        by remember { mutableStateOf("") }
    var password        by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var usernameError   by remember { mutableStateOf<String?>(null) }
    var passwordError   by remember { mutableStateOf<String?>(null) }
    var cargando        by remember { mutableStateOf(false) }

    val focusPassword  = remember { FocusRequester() }
    val focusManager   = LocalFocusManager.current
    val colorScheme    = MaterialTheme.colorScheme
    val coroutineScope = rememberCoroutineScope()

    val gradient = Brush.verticalGradient(
        colors = listOf(VerdeGradStart, VerdeOscuro, VerdeGradEnd)
    )

    fun iniciarSesion() {
        usernameError = null
        passwordError = null
        var esValido  = true
        if (username.isBlank()) { usernameError = "Ingresa tu usuario";    esValido = false }
        if (password.isBlank()) { passwordError = "Ingresa tu contraseña"; esValido = false }
        if (!esValido) return

        cargando = true
        focusManager.clearFocus()
        coroutineScope.launch {
            val dueño = viewModel.autenticar(username.trim(), password.trim())
            cargando  = false
            if (dueño != null) onLoginSuccess(dueño)
            else passwordError = "Usuario o contraseña incorrectos"
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sección superior
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_vidrieria),
                    contentDescription = "Logo Pentagono",
                    modifier = Modifier.size(250.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Pentagono",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Sistema de Gestión Vidriería",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
            } // cierre Box

            // Tarjeta flotante con el formulario
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it; usernameError = null },
                        label = { Text("Usuario") },
                        placeholder = { Text("Usuario") },
                        isError = usernameError != null,
                        supportingText = { if (usernameError != null) Text(usernameError!!, color = colorScheme.error) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusPassword.requestFocus() }),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor    = colorScheme.primary,
                            unfocusedBorderColor  = colorScheme.outline,
                            focusedLabelColor     = colorScheme.primary,
                            cursorColor           = colorScheme.primary,
                            focusedTextColor      = Color(0xFF1C1B1F),
                            unfocusedTextColor    = Color(0xFF1C1B1F),
                            focusedPlaceholderColor   = Color(0xFF6B6B6B),
                            unfocusedPlaceholderColor = Color(0xFF6B6B6B)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; passwordError = null },
                        label = { Text("Contraseña") },
                        placeholder = { Text("Contraseña") },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                                    tint = colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = passwordError != null,
                        supportingText = { if (passwordError != null) Text(passwordError!!, color = colorScheme.error) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { iniciarSesion() }),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor        = colorScheme.primary,
                            unfocusedBorderColor      = colorScheme.outline,
                            focusedLabelColor         = colorScheme.primary,
                            cursorColor               = colorScheme.primary,
                            focusedTextColor          = Color(0xFF1C1B1F),
                            unfocusedTextColor        = Color(0xFF1C1B1F),
                            focusedPlaceholderColor   = Color(0xFF6B6B6B),
                            unfocusedPlaceholderColor = Color(0xFF6B6B6B)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusPassword)
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = { iniciarSesion() },
                        enabled = !cargando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor         = colorScheme.primary,
                            contentColor           = Color.White,
                            disabledContainerColor = colorScheme.outline,
                            disabledContentColor   = colorScheme.onSurfaceVariant
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(
                                modifier    = Modifier.size(20.dp),
                                color       = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Iniciando...", style = MaterialTheme.typography.labelLarge)
                        } else {
                            Text(
                                "Iniciar sesión",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = "Si olvidaste tu contraseña consulta a un dueño",
                        style = MaterialTheme.typography.bodySmall,
                        color = colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Texto de ubicación al fondo
            Text(
                text = "Villa de Zaachila, Oaxaca, México",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp, bottom = 52.dp)
            )
        }
    }
}
