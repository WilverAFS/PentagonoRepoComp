package com.ingsoftware.pentagono.data

class CotizacionRepository(private val dao: CotizacionDao) {

    // CRUD
    suspend fun getCotizaciones(): List<CotizacionEntity> = dao.getCotizaciones()
    suspend fun addCotizacion(cotizacion: CotizacionEntity) = dao.addCotizacion(cotizacion)
    suspend fun updateCotizacion(cotizacion: CotizacionEntity) = dao.updateCotizacion(cotizacion)
    suspend fun deleteCotizacion(cotizacion: CotizacionEntity) = dao.deleteCotizacion(cotizacion)

    // 🔎 Búsquedas
    suspend fun findById(id: Int): CotizacionEntity? = dao.findById(id)
    suspend fun findByEstadoCotizacion(estado: String): List<CotizacionEntity> = dao.findByEstadoCotizacion(estado)
    suspend fun findByEstadoPago(estadoPago: String): List<CotizacionEntity> = dao.findByEstadoPago(estadoPago)
    suspend fun findByCliente(idCliente: Int): List<CotizacionEntity> = dao.findByCliente(idCliente)
    suspend fun findByConcepto(concepto: String): List<CotizacionEntity> = dao.findByConcepto(concepto)
}
