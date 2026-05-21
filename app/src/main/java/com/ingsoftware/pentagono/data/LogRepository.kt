package com.ingsoftware.pentagono.data

class LogRepository(private val dao: LogDao) {

    // CRUD
    suspend fun getLogs(): List<LogEntity> = dao.getLogs()
    suspend fun addLog(log: LogEntity) = dao.addLog(log)
    suspend fun updateLog(log: LogEntity) = dao.updateLog(log)
    suspend fun deleteLog(log: LogEntity) = dao.deleteLog(log)

    // 🔎 Búsquedas
    suspend fun findById(id: Int): LogEntity? = dao.findById(id)
    suspend fun findByDueño(idDueño: Int): List<LogEntity> = dao.findByDueño(idDueño)
    suspend fun findByTipo(tipo: String): List<LogEntity> = dao.findByTipo(tipo)
    suspend fun findByFecha(fecha: String): List<LogEntity> = dao.findByFecha(fecha)
}
