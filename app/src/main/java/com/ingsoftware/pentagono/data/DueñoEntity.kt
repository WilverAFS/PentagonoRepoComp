package com.ingsoftware.pentagono.data
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ingsoftware.pentagono.model.*

@Entity(tableName = "dueños")
data class DueñoEntity(
    @PrimaryKey val id_dueño: Int,
    val nombre: String,
    val contraseña: String
)
