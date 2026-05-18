package com.ingsoftware.pentagono.data

import com.ingsoftware.pentagono.model.Dueño

class DueñoRepository(private val dao: DueñoDao) {
    suspend fun getDueños() = dao.getAllDueños()
    suspend fun addDueño(dueño: DueñoEntity) = dao.insertDueño(dueño)
    suspend fun updateDueño(dueño: DueñoEntity) = dao.updateDueño(dueño)
    suspend fun deleteDueño(dueño: DueñoEntity) = dao.deleteDueño(dueño)
}