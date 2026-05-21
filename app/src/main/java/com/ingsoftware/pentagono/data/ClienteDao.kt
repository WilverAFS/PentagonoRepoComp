package com.ingsoftware.pentagono.data

import androidx.room.*

@Dao
interface ClienteDao {
    @Query("SELECT * FROM clientes")
    suspend fun getClientes(): List<ClienteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCliente(cliente: ClienteEntity)

    @Update
    suspend fun updateCliente(cliente: ClienteEntity)

    @Delete
    suspend fun deleteCliente(cliente: ClienteEntity)

    // Búsquedas específicas
    @Query("SELECT * FROM clientes WHERE telefono = :telefono")
    suspend fun findByTelefono(telefono: Int): List<ClienteEntity>

    @Query("SELECT * FROM clientes WHERE nombre LIKE '%' || :nombre || '%'")
    suspend fun findByNombre(nombre: String): List<ClienteEntity>
}
