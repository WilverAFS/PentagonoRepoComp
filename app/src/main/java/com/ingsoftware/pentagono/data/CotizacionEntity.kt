package com.ingsoftware.pentagono.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ingsoftware.pentagono.model.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "cotizaciones")
data class CotizacionEntity(
    @PrimaryKey val id_cotizacion: Int,
    val fecha: String, // autorrellenada
    val descripcion: String?,
    val monto: Double,
    val estado_cotizacion: String, // "ACEPTADO", "PENDIENTE", "RECHAZADO"
    val estado_pago: String        // "CANCELADO", "PENDIENTE", "ANTICIPO", "COMPLETO"
)
