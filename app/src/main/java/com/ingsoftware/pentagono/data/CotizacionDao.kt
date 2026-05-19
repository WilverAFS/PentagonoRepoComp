package com.ingsoftware.pentagono.data
import androidx.room.*
import com.ingsoftware.pentagono.model.Cotizacion

@Dao
interface CotizacionDao {
    @Query("SELECT * FROM cotizaciones")
    suspend fun getAllCotizaciones(): List<CotizacionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCotizacion(cotizacion: CotizacionEntity)

    @Update
    suspend fun updateCotizacion(cotizacion: CotizacionEntity)

    @Delete
    suspend fun deleteCotizacion(cotizacion: CotizacionEntity)
}