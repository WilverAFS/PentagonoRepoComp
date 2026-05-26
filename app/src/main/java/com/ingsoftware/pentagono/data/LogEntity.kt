package com.ingsoftware.pentagono.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ingsoftware.pentagono.model.TipoLog

@Entity(tableName = "logs")
data class LogEntity(
    @PrimaryKey(autoGenerate = true) val id_log: Int = 0,   // ✅ autoincremental
    val id_dueño: Int,
    val tipo: TipoLog,
    val descripcion: String,
    val fecha: String
)
