package com.ingsoftware.pentagono.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ForeignKey

@Entity(
    tableName = "cotizaciones",
    foreignKeys = [
        ForeignKey(
            entity = ClienteEntity::class,
            parentColumns = ["id_cliente"],
            childColumns = ["id_cliente"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_cliente"])]
)
data class CotizacionEntity(
    @PrimaryKey(autoGenerate = true) val id_cotizacion: Int = 0,
    val id_cliente: Int, // FK hacia ClienteEntity
    val fecha: String,
    val concepto: String,
    val descripcion: String,
    val monto: Double,
    val estado_cotizacion: String,
    val estado_pago: String
)
