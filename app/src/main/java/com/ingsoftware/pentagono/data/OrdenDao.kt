package com.ingsoftware.pentagono.data
import androidx.room.*
import com.ingsoftware.pentagono.model.Orden

@Dao
interface OrdenDao {
    @Query("SELECT * FROM ordenes")
    suspend fun getAllOrdenes(): List<OrdenEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrden(orden: OrdenEntity)

    @Update
    suspend fun updateOrden(orden: OrdenEntity)

    @Delete
    suspend fun deleteOrden(orden: OrdenEntity)
}