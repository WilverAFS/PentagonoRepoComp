package com.ingsoftware.pentagono.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ingsoftware.pentagono.model.EstadoOrden

@Entity(tableName = "ordenes")
data class OrdenEntity(
    @PrimaryKey val id_orden: Int,
    val id_cotizacion: Int,
    val id_empleado: Int,
    val id_dueño: Int,
    val fecha_inicio: String,
    val fecha_fin: String?,
    val estado: EstadoOrden,
    val fecha_entrega: String?
)
