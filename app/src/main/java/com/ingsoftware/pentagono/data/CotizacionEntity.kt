package com.ingsoftware.pentagono.data
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ingsoftware.pentagono.model.*

@Entity(tableName = "cotizaciones")
data class CotizacionEntity(
    @PrimaryKey val id_cotizacion: Int,
    val id_cliente: Int,
    val fecha: String,
    val descripcion: String,
    val monto: Double,
    val estado: EstadoCotizacion,
    val pago: EstadoPago
)
