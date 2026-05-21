package com.ingsoftware.pentagono.data

class OrdenRepository(private val dao: OrdenDao) {

    // CRUD
    suspend fun getOrdenes(): List<OrdenEntity> = dao.getOrdenes()
    suspend fun addOrden(orden: OrdenEntity) = dao.addOrden(orden)
    suspend fun updateOrden(orden: OrdenEntity) = dao.updateOrden(orden)
    suspend fun deleteOrden(orden: OrdenEntity) = dao.deleteOrden(orden)

    // 🔎 Búsquedas
    suspend fun findById(id: Int): OrdenEntity? = dao.findById(id)
    suspend fun findByCotizacion(idCotizacion: Int): List<OrdenEntity> = dao.findByCotizacion(idCotizacion)
    suspend fun findByEmpleado(idEmpleado: Int): List<OrdenEntity> = dao.findByEmpleado(idEmpleado)
    suspend fun findByDueño(idDueño: Int): List<OrdenEntity> = dao.findByDueño(idDueño)
    suspend fun findByEstado(estado: String): List<OrdenEntity> = dao.findByEstado(estado)
}
