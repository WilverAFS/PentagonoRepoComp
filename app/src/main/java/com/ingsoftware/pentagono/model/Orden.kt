package com.ingsoftware.pentagono.model

enum class EstadoOrden {
    PENDIENTE, TERMINADO, ENTREGADO, CANCELADO
}

data class Orden(
    val id_orden: Int,
    val id_cotizacion: Int,
    val id_empleado: Int,
    val id_dueño: Int,
    val fecha_inicio: String,
    val fecha_fin: String?,
    val estado: EstadoOrden,
    val fecha_entrega: String?
)
