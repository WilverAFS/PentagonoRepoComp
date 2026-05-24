package com.ingsoftware.pentagono.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object Validaciones {
    val telefonoRegex = Regex("^\\d{10}$")
    val correoRegex   = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    val nombreRegex   = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")
    val numeroRegex   = Regex("^[0-9]+$")
    val curpRegex     = Regex("^[A-Z0-9]{18}$")
    val fechaRegex    = Regex("^\\d{4}-\\d{2}-\\d{2}$")

    fun validarTelefono(telefono: String) = telefonoRegex.matches(telefono)
    fun validarCorreo(correo: String)     = correoRegex.matches(correo)
    fun validarNombre(nombre: String)     = nombreRegex.matches(nombre)
    fun validarNumero(numero: String)     = numeroRegex.matches(numero)
    fun validarCurp(curp: String)         = curpRegex.matches(curp)
    fun validarFecha(fecha: String)       = fechaRegex.matches(fecha)
}

// ── Helpers internos ──────────────────────────────────────────────────────────

@Composable
private fun ErrorText(message: String) {
    Text(
        text = message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
    )
}

@Composable
private fun PentagonoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodyMedium) },
        singleLine = true,
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorScheme.primary,
            unfocusedBorderColor = colorScheme.outline,
            errorBorderColor = colorScheme.error,
            focusedLabelColor = colorScheme.primary,
            unfocusedLabelColor = colorScheme.onSurfaceVariant,
            cursorColor = colorScheme.primary
        )
    )
}

// ── Campos públicos ───────────────────────────────────────────────────────────

@Composable
fun CampoTelefono(value: String, onValueChange: (String) -> Unit, obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarTelefono(value))
    PentagonoTextField(value, onValueChange, "Teléfono (10 dígitos)", isError)
    if (isError) {
        if (obligatorio && value.isBlank()) ErrorText("Campo obligatorio")
        else ErrorText("Debe contener exactamente 10 dígitos")
    }
}

@Composable
fun CampoCorreo(value: String, onValueChange: (String) -> Unit, obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarCorreo(value))
    PentagonoTextField(value, onValueChange, "Correo electrónico", isError)
    if (isError) {
        if (obligatorio && value.isBlank()) ErrorText("Campo obligatorio")
        else ErrorText("Formato de correo inválido")
    }
}

@Composable
fun CampoNombre(value: String, onValueChange: (String) -> Unit, label: String = "Nombre", obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarNombre(value))
    PentagonoTextField(value, onValueChange, label, isError)
    if (isError) {
        if (obligatorio && value.isBlank()) ErrorText("Campo obligatorio")
        else ErrorText("Solo letras permitidas")
    }
}

@Composable
fun CampoNumero(value: String, onValueChange: (String) -> Unit, label: String, obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarNumero(value))
    PentagonoTextField(value, onValueChange, label, isError)
    if (isError) {
        if (obligatorio && value.isBlank()) ErrorText("Campo obligatorio")
        else ErrorText("Debe ser un número")
    }
}

@Composable
fun CampoCurp(value: String, onValueChange: (String) -> Unit, obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarCurp(value))
    PentagonoTextField(value, { onValueChange(it.uppercase()) }, "CURP (18 caracteres)", isError)
    if (isError) {
        if (obligatorio && value.isBlank()) ErrorText("Campo obligatorio")
        else ErrorText("CURP inválida: 18 caracteres alfanuméricos")
    }
}

@Composable
fun CampoFecha(value: String, onValueChange: (String) -> Unit, label: String = "Fecha (YYYY-MM-DD)", obligatorio: Boolean = true) {
    val isError = (obligatorio && value.isBlank()) || (value.isNotBlank() && !Validaciones.validarFecha(value))
    PentagonoTextField(value, onValueChange, label, isError)
    if (isError) {
        if (obligatorio && value.isBlank()) ErrorText("Campo obligatorio")
        else ErrorText("Formato de fecha inválido (YYYY-MM-DD)")
    }
}

@Composable
fun CampoObligatorio(value: String, onValueChange: (String) -> Unit, label: String) {
    val isError = value.isBlank()
    PentagonoTextField(value, onValueChange, label, isError)
    if (isError) ErrorText("Campo obligatorio")
}