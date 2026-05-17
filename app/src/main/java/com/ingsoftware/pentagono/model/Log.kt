package com.ingsoftware.pentagono.model

data class Log(
    val id_log: Int,
    val id_dueño: Int,
    val tipo: TipoLog,
    val descripcion: String,
    val fecha: String
)

enum class TipoLog{
    ADD, UPDATE, DELETE, ACCESS
}