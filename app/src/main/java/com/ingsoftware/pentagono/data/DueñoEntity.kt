package com.ingsoftware.pentagono.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "dueños",
    indices = [Index(value = ["nombre"], unique = true)] // ✅ nombre único
)
data class DueñoEntity(
    @PrimaryKey(autoGenerate = true) val id_dueño: Int = 0,   // ✅ autoincremental
    val nombre: String,                                       // Nombre único del dueño
    val contraseña: String                                    // Contraseña
)
