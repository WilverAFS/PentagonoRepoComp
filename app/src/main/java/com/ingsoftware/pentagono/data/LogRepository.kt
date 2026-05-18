package com.ingsoftware.pentagono.data

import com.ingsoftware.pentagono.model.Log

class LogRepository(private val dao: LogDao) {
    suspend fun getLogs() = dao.getAllLogs()
    suspend fun addLog(log: LogEntity) = dao.insertLog(log)
    suspend fun updateLog(log: LogEntity) = dao.updateLog(log)
    suspend fun deleteLog(log: LogEntity) = dao.deleteLog(log)
}