package com.ingsoftware.pentagono.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "clientes",
    indices = [Index(value = ["telefono"], unique = true)] // Teléfono único
)
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true) val id_cliente: Int = 0, // PK autogenerado
    @ColumnInfo(name = "telefono") val telefono: String,      // exactamente 10 dígitos
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "apellido_paterno") val apellidoPaterno: String?,
    @ColumnInfo(name = "apellido_materno") val apellidoMaterno: String?,
    @ColumnInfo(name = "calle") val calle: String,
    @ColumnInfo(name = "numero_exterior") val numeroExterior: String,
    @ColumnInfo(name = "numero_interior") val numeroInterior: String?,
    @ColumnInfo(name = "colonia") val colonia: String,
    @ColumnInfo(name = "municipio") val municipio: String,
    @ColumnInfo(name = "estado") val estado: String,
    @ColumnInfo(name = "correo") val correo: String?
)
