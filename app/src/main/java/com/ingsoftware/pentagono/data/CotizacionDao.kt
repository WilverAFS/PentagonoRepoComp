package com.ingsoftware.pentagono.data
import androidx.room.*
@Dao
interface CotizacionDao {
    @Query("SELECT * FROM cotizaciones")
    suspend fun getCotizaciones(): List<CotizacionEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCotizacion(cotizacion: CotizacionEntity)
    @Update
    suspend fun updateCotizacion(cotizacion: CotizacionEntity)
    @Delete
    suspend fun deleteCotizacion(cotizacion: CotizacionEntity)
    @Query("SELECT * FROM cotizaciones WHERE id_cotizacion = :id")
    suspend fun findById(id: Int): CotizacionEntity?

    @Query("SELECT * FROM cotizaciones WHERE estado_cotizacion = :estado")
    suspend fun findByEstadoCotizacion(estado: String): List<CotizacionEntity>

    @Query("SELECT * FROM cotizaciones WHERE estado_pago = :estadoPago")
    suspend fun findByEstadoPago(estadoPago: String): List<CotizacionEntity>

    @Query("SELECT * FROM cotizaciones WHERE id_cliente = :idCliente")
    suspend fun findByCliente(idCliente: Int): List<CotizacionEntity>
    @Query("SELECT * FROM cotizaciones WHERE concepto LIKE '%' || :concepto || '%'")
    suspend fun findByConcepto(concepto: String): List<CotizacionEntity>
}
