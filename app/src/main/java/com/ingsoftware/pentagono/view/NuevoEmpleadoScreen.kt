package com.ingsoftware.pentagono.view

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ingsoftware.pentagono.data.EmpleadoEntity
import com.ingsoftware.pentagono.viewmodel.EmpleadoViewModel
import java.time.LocalDate

// ── CURP helpers ─────────────────────────────────────────────────────────────

private fun normStr(s: String) = s.uppercase()
    .replace('Á', 'A').replace('É', 'E').replace('Í', 'I')
    .replace('Ó', 'O').replace('Ú', 'U').replace('Ü', 'U')
    .replace('Ñ', 'X')
    .filter { it.isLetter() }

private val VOCALES    = setOf('A', 'E', 'I', 'O', 'U')
private val CONSONANTES = setOf('B','C','D','F','G','H','J','K','L','M','N','P','Q','R','S','T','V','W','X','Y','Z')

private fun primerLetra(s: String)   = normStr(s).firstOrNull() ?: 'X'
private fun primerVocalInt(s: String)= normStr(s).drop(1).firstOrNull { it in VOCALES    } ?: 'X'
private fun primerConsInt(s: String) = normStr(s).drop(1).firstOrNull { it in CONSONANTES } ?: 'X'

private fun curp16(
    nombre: String, apPat: String, apMat: String,
    anio: Int, mes: Int, dia: Int, sexo: String, codigoEdo: String
): String {
    val p1  = primerLetra(apPat)
    val p2  = primerVocalInt(apPat)
    val p3  = if (apMat.isBlank()) 'X' else primerLetra(apMat)
    val p4  = primerLetra(nombre)
    val yy  = "%02d".format(anio % 100)
    val mm  = "%02d".format(mes)
    val dd  = "%02d".format(dia)
    val sex = if (sexo == "M") 'M' else 'H'
    val edo = codigoEdo.uppercase().take(2).padEnd(2, 'X')
    val p14 = primerConsInt(apPat)
    val p15 = if (apMat.isBlank()) 'X' else primerConsInt(apMat)
    val p16 = primerConsInt(nombre)
    return "$p1$p2$p3$p4$yy$mm$dd$sex$edo$p14$p15$p16"
}

private val ESTADOS_MX = listOf(
    "AS" to "Aguascalientes",    "BC" to "Baja California",
    "BS" to "Baja California Sur","CC" to "Campeche",
    "CL" to "Coahuila",          "CM" to "Colima",
    "CS" to "Chiapas",           "CH" to "Chihuahua",
    "DF" to "Ciudad de México",  "DG" to "Durango",
    "GT" to "Guanajuato",        "GR" to "Guerrero",
    "HG" to "Hidalgo",           "JC" to "Jalisco",
    "MC" to "Estado de México",  "MN" to "Michoacán",
    "MS" to "Morelos",           "NT" to "Nayarit",
    "NL" to "Nuevo León",        "OC" to "Oaxaca",
    "PL" to "Puebla",            "QT" to "Querétaro",
    "QR" to "Quintana Roo",      "SP" to "San Luis Potosí",
    "SL" to "Sinaloa",           "SR" to "Sonora",
    "TC" to "Tabasco",           "TS" to "Tamaulipas",
    "TL" to "Tlaxcala",          "VZ" to "Veracruz",
    "YN" to "Yucatán",           "ZS" to "Zacatecas",
    "NE" to "Nacido en el extranjero"
)

// ── Screen ────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoEmpleadoScreen(
    viewModel: EmpleadoViewModel,
    onBack: () -> Unit = {},
    onSaveSuccess: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val empleados   by viewModel.empleados.collectAsState()
    val context     = LocalContext.current

    // ── Datos personales
    var nombre         by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var sexo           by remember { mutableStateOf("H") }
    var fechaNac       by remember { mutableStateOf<LocalDate?>(null) }
    var codigoEdo      by remember { mutableStateOf("OC") }
    var sexoExpanded   by remember { mutableStateOf(false) }
    var edoExpanded    by remember { mutableStateOf(false) }

    // ── Homoclave (últimos 2 chars, ingresados manualmente)
    var homoclave by remember { mutableStateOf("") }

    // ── Contacto y domicilio
    var telefono       by remember { mutableStateOf("") }
    var correo         by remember { mutableStateOf("") }
    var calle          by remember { mutableStateOf("") }
    var numeroExterior by remember { mutableStateOf("") }
    var numeroInterior by remember { mutableStateOf("") }
    var colonia        by remember { mutableStateOf("") }
    var municipio      by remember { mutableStateOf("") }
    var estadoDom      by remember { mutableStateOf("Oaxaca") }

    // ── CURP auto-generada (16 chars) ─────────────────────────────────────
    val auto16 = remember(nombre, apellidoPaterno, apellidoMaterno, fechaNac, sexo, codigoEdo) {
        val f = fechaNac
        if (nombre.isNotBlank() && apellidoPaterno.isNotBlank() && f != null)
            curp16(nombre.trim(), apellidoPaterno.trim(), apellidoMaterno.trim(),
                   f.year, f.monthValue, f.dayOfMonth, sexo, codigoEdo)
        else ""
    }

    val homoUp      = homoclave.uppercase()
    val homoRegex   = Regex("^[0-9A-Z][0-9]$")
    val homoValida  = homoRegex.matches(homoUp)
    val curpFinal   = "$auto16$homoUp"
    val curpOk      = auto16.length == 16 && homoValida && Validaciones.validarCurp(curpFinal)
    val curpUnica   = empleados.none { it.curp.equals(curpFinal, ignoreCase = true) }

    // ── Validaciones ──────────────────────────────────────────────────────
    val nombreOk   = nombre.isNotBlank()       && Validaciones.validarNombre(nombre)
    val apPatOk    = apellidoPaterno.isNotBlank() && Validaciones.validarNombre(apellidoPaterno)
    val telOk      = Validaciones.validarTelefono(telefono.replace(" ","").replace("-",""))
    val correoOk   = correo.isBlank() || Validaciones.validarCorreo(correo)
    val calleOk    = calle.isNotBlank()
    val numExtOk   = numeroExterior.isNotBlank() && Validaciones.validarNumero(numeroExterior)
    val coloniaOk  = colonia.isNotBlank()
    val municipioOk= municipio.isNotBlank()
    val estadoDomOk= estadoDom.isNotBlank()

    val hayErrores = !nombreOk || !apPatOk || fechaNac == null ||
                     !curpOk || !curpUnica ||
                     !telOk || !correoOk ||
                     !calleOk || !numExtOk || !coloniaOk || !municipioOk || !estadoDomOk

    Scaffold(
        topBar = {
            PentagonoTopBar(
                title          = "Nuevo Empleado",
                showBackButton = true,
                onBackClick    = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Datos personales ─────────────────────────────────────────
            SectionLabel("Datos personales", colorScheme.primary)

            CampoNombre(nombre, { nombre = it }, label = "Nombre", obligatorio = true)
            CampoNombre(apellidoPaterno, { apellidoPaterno = it }, label = "Apellido Paterno", obligatorio = true)
            CampoNombre(apellidoMaterno, { apellidoMaterno = it }, label = "Apellido Materno (opcional)", obligatorio = false)

            // Fecha de nacimiento
            val hoyRef = LocalDate.now()
            OutlinedButton(
                onClick = {
                    val y = fechaNac?.year            ?: (hoyRef.year - 25)
                    val m = fechaNac?.monthValue?.minus(1) ?: (hoyRef.monthValue - 1)
                    val d = fechaNac?.dayOfMonth      ?: hoyRef.dayOfMonth
                    DatePickerDialog(context, { _, year, month, day ->
                        fechaNac = LocalDate.of(year, month + 1, day)
                    }, y, m, d).show()
                },
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(fechaNac?.toString() ?: "Fecha de nacimiento *")
            }
            if (fechaNac == null)
                Text("Campo obligatorio", color = colorScheme.error, style = MaterialTheme.typography.labelMedium)

            // Sexo
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { sexoExpanded = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Text("Sexo: ${if (sexo == "H") "Hombre (H)" else "Mujer (M)"}")
                }
                DropdownMenu(expanded = sexoExpanded, onDismissRequest = { sexoExpanded = false }) {
                    DropdownMenuItem(text = { Text("Hombre (H)") }, onClick = { sexo = "H"; sexoExpanded = false })
                    DropdownMenuItem(text = { Text("Mujer (M)")  }, onClick = { sexo = "M"; sexoExpanded = false })
                }
            }

            // Estado de nacimiento
            val edoNombre = ESTADOS_MX.find { it.first == codigoEdo }?.second ?: ""
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { edoExpanded = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
                    Text("Nació en: $codigoEdo – $edoNombre", maxLines = 1)
                }
                DropdownMenu(
                    expanded = edoExpanded,
                    onDismissRequest = { edoExpanded = false },
                    modifier = Modifier.heightIn(max = 280.dp)
                ) {
                    ESTADOS_MX.forEach { (code, name) ->
                        DropdownMenuItem(
                            text  = { Text("$code – $name") },
                            onClick = { codigoEdo = code; edoExpanded = false }
                        )
                    }
                }
            }

            // ── Sección CURP ─────────────────────────────────────────────
            Spacer(Modifier.height(4.dp))
            SectionLabel("CURP", colorScheme.primary)

            // Bloque auto-generado (16 chars)
            if (auto16.length == 16) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(10.dp),
                    colors   = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Generado automáticamente (primeros 16):", style = MaterialTheme.typography.labelSmall, color = colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Text(auto16, style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold, letterSpacing = 2.sp), color = colorScheme.primary)
                    }
                }
            } else {
                Text(
                    "Completa nombre, apellido paterno y fecha de nacimiento para auto-generar los primeros 16 caracteres.",
                    style = MaterialTheme.typography.bodySmall, color = colorScheme.onSurfaceVariant
                )
            }

            // Campo homoclave (últimos 2 chars)
            OutlinedTextField(
                value         = homoUp,
                onValueChange = { if (it.length <= 2) homoclave = it.uppercase() },
                label         = { Text("Últimos 2 caracteres de la CURP (confirmación física)") },
                placeholder   = { Text("Ej: A6") },
                singleLine    = true,
                isError       = homoUp.isNotBlank() && !homoValida,
                supportingText = {
                    when {
                        homoUp.isBlank() -> Text("Revisa tu CURP oficial e ingresa el carácter alfanumérico + dígito final")
                        !homoValida -> Text("Formato: 1 carácter ([0-9A-Z]) + 1 dígito", color = colorScheme.error)
                        else -> {}
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Vista previa CURP completa
            if (auto16.length == 16 && homoUp.length == 2) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(10.dp),
                    colors   = CardDefaults.cardColors(
                        containerColor = if (curpOk && curpUnica) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("CURP completa:", style = MaterialTheme.typography.labelSmall, color = colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            curpFinal,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                            color = if (curpOk && curpUnica) Color(0xFF2E7D32) else colorScheme.error
                        )
                        if (!curpOk)   Text("CURP inválida según el formato oficial", color = colorScheme.error, style = MaterialTheme.typography.labelSmall)
                        if (!curpUnica) Text("Ya existe un empleado con esta CURP",   color = colorScheme.error, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            // ── Contacto ─────────────────────────────────────────────────
            Spacer(Modifier.height(4.dp))
            SectionLabel("Contacto", colorScheme.primary)
            CampoTelefono(telefono, { telefono = it }, obligatorio = true)
            CampoCorreo(correo,   { correo   = it }, obligatorio = false)

            // ── Domicilio ─────────────────────────────────────────────────
            Spacer(Modifier.height(4.dp))
            SectionLabel("Domicilio", colorScheme.primary)
            CampoObligatorio(calle,    { calle    = it }, label = "Calle")
            CampoNumero(numeroExterior, { numeroExterior = it }, label = "Número Exterior", obligatorio = true)
            OutlinedTextField(
                value = numeroInterior, onValueChange = { numeroInterior = it },
                label = { Text("Número Interior (opcional)") }, modifier = Modifier.fillMaxWidth()
            )
            CampoObligatorio(colonia,   { colonia   = it }, label = "Colonia")
            CampoObligatorio(municipio, { municipio = it }, label = "Municipio")
            CampoObligatorio(estadoDom, { estadoDom = it }, label = "Estado")

            Spacer(Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {
                        viewModel.addEmpleado(
                            EmpleadoEntity(
                                id_empleado    = 0,
                                curp           = curpFinal,
                                nombre         = nombre.trim(),
                                apellidoPaterno= apellidoPaterno.trim(),
                                apellidoMaterno= apellidoMaterno.trim(),
                                telefono       = telefono.trim(),
                                correo         = if (correo.isBlank()) null else correo.trim(),
                                calle          = calle.trim(),
                                numeroExterior = numeroExterior.trim().toInt(),
                                numeroInterior = if (numeroInterior.isBlank()) null else numeroInterior.trim(),
                                colonia        = colonia.trim(),
                                municipio      = municipio.trim(),
                                estado         = estadoDom.trim()
                            )
                        )
                        onSaveSuccess()
                    },
                    enabled = !hayErrores,
                    colors  = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) { Text("Guardar") }
                OutlinedButton(onClick = onBack) { Text("Cancelar") }
            }

            if (hayErrores) Text("Completa todos los campos requeridos", color = colorScheme.error, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String, color: androidx.compose.ui.graphics.Color) {
    Text(text, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = color)
}
