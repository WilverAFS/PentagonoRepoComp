package com.ingsoftware.pentagono.data

class DueñoRepository(private val dao: DueñoDao) {

    // CRUD
    suspend fun getDueños(): List<DueñoEntity> = dao.getDueños()
    suspend fun addDueño(dueño: DueñoEntity) = dao.addDueño(dueño)
    suspend fun updateDueño(dueño: DueñoEntity) = dao.updateDueño(dueño)
    suspend fun deleteDueño(dueño: DueñoEntity) = dao.deleteDueño(dueño)

    // 🔎 Búsquedas
    suspend fun findById(id: Int): DueñoEntity? = dao.findById(id)
    suspend fun findByNombre(nombre: String): List<DueñoEntity> = dao.findByNombre(nombre)
}
