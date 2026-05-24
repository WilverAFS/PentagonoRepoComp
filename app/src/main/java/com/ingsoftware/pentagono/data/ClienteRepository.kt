package com.ingsoftware.pentagono.data

class ClienteRepository(private val dao: ClienteDao) {
    suspend fun getClientes(): List<ClienteEntity> = dao.getClientes()
    suspend fun addCliente(cliente: ClienteEntity) = dao.addCliente(cliente)
    suspend fun updateCliente(cliente: ClienteEntity) = dao.updateCliente(cliente)
    suspend fun deleteCliente(cliente: ClienteEntity) = dao.deleteCliente(cliente)
    suspend fun findByTelefono(telefono: String): ClienteEntity? = dao.findByTelefono(telefono)
    suspend fun findByNombre(nombre: String): List<ClienteEntity> = dao.findByNombre(nombre)
}
