package com.ingsoftware.pentagono.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
) {
    companion object {
        /**
         * ✅ Devuelve la fecha actual del sistema en formato YYYY-MM-DD
         * Se usará para inicializar fecha_inicio de las órdenes.
         */
        fun getSystemDate(): String {
            val fecha = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return fecha.format(formatter)
        }
    }
}
