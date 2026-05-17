package com.ingsoftware.pentagono.model

enum class EstadoCotizacion {
    ACEPTADO, PENDIENTE, RECHAZADO
}

enum class EstadoPago {
    CANCELADO, PENDIENTE, ANTICIPO, COMPLETO
}

data class Cotizacion(
    val id_cotizacion: Int,
    val id_cliente: Int,
    val fecha: String,
    val descripcion: String,
    val monto: Double,
    val estado: EstadoCotizacion,
    val pago: EstadoPago
)
