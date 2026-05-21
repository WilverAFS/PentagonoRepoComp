package com.ingsoftware.pentagono.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dueños")
data class DueñoEntity(
    @PrimaryKey val id_dueño: Int,          // PK
    val nombre: String,                     // Nombre del dueño
    val contraseña: String                  // Contraseña
)
