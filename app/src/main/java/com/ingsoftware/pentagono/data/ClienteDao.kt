package com.ingsoftware.pentagono.data

import androidx.room.*

@Dao
interface ClienteDao {
    @Query("SELECT * FROM clientes")
    suspend fun getAllClientes(): List<ClienteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCliente(cliente: ClienteEntity)

    @Update
    suspend fun updateCliente(cliente: ClienteEntity)

    @Delete
    suspend fun deleteCliente(cliente: ClienteEntity)
}

// Se repite la misma estructura para CotizacionDao, OrdenDao, EmpleadoDao, DueñoDao, LogDao
