package com.ingsoftware.pentagono.data

class ClienteRepository(private val dao: ClienteDao) {
    suspend fun getClientes() = dao.getAllClientes()
    suspend fun addCliente(cliente: ClienteEntity) = dao.insertCliente(cliente)
    suspend fun updateCliente(cliente: ClienteEntity) = dao.updateCliente(cliente)
    suspend fun deleteCliente(cliente: ClienteEntity) = dao.deleteCliente(cliente)
}

// Se crean repositorios similares para Cotizacion, Orden, Empleado, Dueño y Log
