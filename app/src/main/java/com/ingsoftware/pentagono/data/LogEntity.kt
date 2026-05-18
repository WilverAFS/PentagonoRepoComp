package com.ingsoftware.pentagono.data
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ingsoftware.pentagono.model.*

@Entity(tableName = "logs")
data class LogEntity(
    @PrimaryKey val id_log: Int,
    val id_dueño: Int,
    val tipo: TipoLog,
    val descripcion: String,
    val fecha: String
)
