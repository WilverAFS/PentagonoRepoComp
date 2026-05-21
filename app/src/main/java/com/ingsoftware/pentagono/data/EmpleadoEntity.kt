package com.ingsoftware.pentagono.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "empleados")
data class EmpleadoEntity(
    @PrimaryKey val curp: String, // PK
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "apellido_paterno") val apellidoPaterno: String,
    @ColumnInfo(name = "apellido_materno") val apellidoMaterno: String,
    @ColumnInfo(name = "telefono") val telefono: String,
    @ColumnInfo(name = "correo") val correo: String?,
    @ColumnInfo(name = "calle") val calle: String,
    @ColumnInfo(name = "numero_exterior") val numeroExterior: Int,
    @ColumnInfo(name = "numero_interior") val numeroInterior: String?,
    @ColumnInfo(name = "colonia") val colonia: String,
    @ColumnInfo(name = "municipio") val municipio: String,
    @ColumnInfo(name = "estado") val estado: String
)
