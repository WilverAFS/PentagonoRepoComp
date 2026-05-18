package com.ingsoftware.pentagono.data
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ingsoftware.pentagono.model.*

@Entity(tableName = "clientes")
data class ClienteEntity(
    @PrimaryKey val id_cliente: Int,
    val nombre: String,
    val telefono: String,
    val correo: String,
    val direccion: String
)
