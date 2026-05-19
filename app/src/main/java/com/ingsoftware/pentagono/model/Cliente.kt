package com.ingsoftware.pentagono.model

data class Cliente(
    val id_cliente: Int,
    val nombre: String,
    val telefono: String,
    val correo: String,
    val direccion: String
)
//CAMBIAR PK POR TELEFONO e implementar validacion