package com.ingsoftware.pentagono.data

import androidx.room.*

@Dao
interface OrdenDao {
    @Query("SELECT * FROM ordenes")
    suspend fun getOrdenes(): List<OrdenEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addOrden(orden: OrdenEntity)

    @Update
    suspend fun updateOrden(orden: OrdenEntity)

    @Delete
    suspend fun deleteOrden(orden: OrdenEntity)

    // 🔎 Búsquedas específicas
    @Query("SELECT * FROM ordenes WHERE id_orden = :id")
    suspend fun findById(id: Int): OrdenEntity?

    @Query("SELECT * FROM ordenes WHERE id_cotizacion = :idCotizacion")
    suspend fun findByCotizacion(idCotizacion: Int): List<OrdenEntity>

    @Query("SELECT * FROM ordenes WHERE id_empleado = :idEmpleado")
    suspend fun findByEmpleado(idEmpleado: Int): List<OrdenEntity>

    @Query("SELECT * FROM ordenes WHERE id_dueño = :idDueño")
    suspend fun findByDueño(idDueño: Int): List<OrdenEntity>

    @Query("SELECT * FROM ordenes WHERE estado = :estado")
    suspend fun findByEstado(estado: String): List<OrdenEntity>
}
