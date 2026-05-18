package com.ingsoftware.pentagono.data

import com.ingsoftware.pentagono.model.Orden

class OrdenRepository(private val dao: OrdenDao) {
    suspend fun getOrdenes() = dao.getAllOrdenes()
    suspend fun addOrden(orden: OrdenEntity) = dao.insertOrden(orden)
    suspend fun updateOrden(orden: OrdenEntity) = dao.updateOrden(orden)
    suspend fun deleteOrden(orden: OrdenEntity) = dao.deleteOrden(orden)
}