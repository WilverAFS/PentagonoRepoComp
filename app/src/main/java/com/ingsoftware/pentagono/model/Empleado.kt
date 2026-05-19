package com.ingsoftware.pentagono.model

data class Empleado(
    val id_empleado: Int,
    val nombre: String,
    val telefono: String,
    val correo: String,
    val puesto: String,
    val direccion: String
)
//QUITAR PUESTO, CAMBIAR CORREO POR CURP(pk)