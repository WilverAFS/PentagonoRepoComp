package com.ingsoftware.pentagono.data

class ClienteRepository(private val dao: ClienteDao) {

    // Obtener todos los clientes
    suspend fun getClientes(): List<ClienteEntity> = dao.getClientes()

    // Insertar nuevo cliente
    suspend fun addCliente(cliente: ClienteEntity) = dao.addCliente(cliente)

    // Actualizar cliente existente
    suspend fun updateCliente(cliente: ClienteEntity) = dao.updateCliente(cliente)

    // Eliminar cliente
    suspend fun deleteCliente(cliente: ClienteEntity) = dao.deleteCliente(cliente)

    // Buscar por teléfono (PK)
    suspend fun findByTelefono(telefono: Int): List<ClienteEntity> = dao.findByTelefono(telefono)

    // Buscar por nombre (coincidencia parcial)
    suspend fun findByNombre(nombre: String): List<ClienteEntity> = dao.findByNombre(nombre)
}
