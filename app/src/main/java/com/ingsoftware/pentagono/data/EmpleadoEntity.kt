package com.ingsoftware.pentagono.data
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ingsoftware.pentagono.model.*

@Entity(tableName = "empleados")
data class EmpleadoEntity(
    @PrimaryKey val id_empleado: Int,
    val nombre: String,
    val telefono: String,
    val correo: String,
    val puesto: String,
    val direccion: String
)
