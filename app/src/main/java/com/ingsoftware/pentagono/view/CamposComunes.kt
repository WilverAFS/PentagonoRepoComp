package com.ingsoftware.pentagono.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

object Validaciones {
    val telefonoRegex = Regex("^\\d{10}$")
    val correoRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    val nombreRegex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")
    val numeroRegex = Regex("^[0-9]+$")
    val curpRegex = Regex("^[A-Z0-9]{18}$")
    val fechaRegex = Regex("^\\d{4}-\\d{2}-\\d{2}$") // formato YYYY-MM-DD

    fun validarTelefono(telefono: String) = telefonoRegex.matches(telefono)
    fun validarCorreo(correo: String) = correoRegex.matches(correo)
    fun validarNombre(nombre: String) = nombreRegex.matches(nombre)
    fun validarNumero(numero: String) = numeroRegex.matches(numero)
    fun validarCurp(curp: String) = curpRegex.matches(curp)
    fun validarFecha(fecha: String) = fechaRegex.matches(fecha)
}

@Composable
fun CampoTelefono(value: String, onValueChange: (String) -> Unit, obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarTelefono(value))
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text("Teléfono (10 dígitos)") },
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth()
    )
    if (isError) {
        if (obligatorio && value.isBlank()) Text("Campo obligatorio", color = Color.Red)
        else Text("Debe contener exactamente 10 dígitos", color = Color.Red)
    }
}

@Composable
fun CampoCorreo(value: String, onValueChange: (String) -> Unit, obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarCorreo(value))
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text("Correo electrónico") },
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth()
    )
    if (isError) {
        if (obligatorio && value.isBlank()) Text("Campo obligatorio", color = Color.Red)
        else Text("Formato de correo inválido", color = Color.Red)
    }
}

@Composable
fun CampoNombre(value: String, onValueChange: (String) -> Unit, label: String = "Nombre", obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarNombre(value))
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth()
    )
    if (isError) {
        if (obligatorio && value.isBlank()) Text("Campo obligatorio", color = Color.Red)
        else Text("Solo letras permitidas", color = Color.Red)
    }
}

@Composable
fun CampoNumero(value: String, onValueChange: (String) -> Unit, label: String, obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarNumero(value))
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth()
    )
    if (isError) {
        if (obligatorio && value.isBlank()) Text("Campo obligatorio", color = Color.Red)
        else Text("Debe ser un número", color = Color.Red)
    }
}

@Composable
fun CampoCurp(value: String, onValueChange: (String) -> Unit, obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarCurp(value))
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.uppercase()) },
        label = { Text("CURP (18 caracteres)") },
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth()
    )
    if (isError) {
        if (obligatorio && value.isBlank()) Text("Campo obligatorio", color = Color.Red)
        else Text("CURP inválida, debe tener 18 caracteres alfanuméricos", color = Color.Red)
    }
}

@Composable
fun CampoFecha(value: String, onValueChange: (String) -> Unit, label: String = "Fecha (YYYY-MM-DD)", obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarFecha(value))
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth()
    )
    if (isError) {
        if (obligatorio && value.isBlank()) Text("Campo obligatorio", color = Color.Red)
        else Text("Formato de fecha inválido", color = Color.Red)
    }
}

@Composable
fun CampoObligatorio(value: String, onValueChange: (String) -> Unit, label: String) {
    val isError = value.isBlank()
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth()
    )
    if (isError) Text("Campo obligatorio", color = Color.Red)
}
