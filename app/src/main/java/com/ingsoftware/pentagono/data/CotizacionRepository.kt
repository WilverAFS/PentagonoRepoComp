package com.ingsoftware.pentagono.data

import com.ingsoftware.pentagono.model.Cotizacion

class CotizacionRepository (private val dao: CotizacionDao){
    suspend fun getCotizacion() = dao.getAllCotizaciones()
    suspend fun addCotizacion(cotizacion: CotizacionEntity) = dao.insertCotizacion(cotizacion)
    suspend fun updateCotizacion(cotizacion: CotizacionEntity) = dao.updateCotizacion(cotizacion)
    suspend fun deleteCotizacion(cotizacion: CotizacionEntity) = dao.deleteCotizacion(cotizacion)
}