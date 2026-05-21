package com.ingsoftware.pentagono.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingsoftware.pentagono.data.LogEntity
import com.ingsoftware.pentagono.data.LogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LogViewModel(private val repository: LogRepository) : ViewModel() {
    private val _logs = MutableStateFlow<List<LogEntity>>(emptyList())
    val logs: StateFlow<List<LogEntity>> = _logs

    init { loadLogs() }

    fun loadLogs() {
        viewModelScope.launch { _logs.value = repository.getLogs() }
    }

    fun addLog(log: LogEntity) {
        viewModelScope.launch {
            repository.addLog(log)
            loadLogs()
        }
    }

    fun updateLog(log: LogEntity) {
        viewModelScope.launch {
            repository.updateLog(log)
            loadLogs()
        }
    }

    fun deleteLog(log: LogEntity) {
        viewModelScope.launch {
            repository.deleteLog(log)
            loadLogs()
        }
    }

    // 🔎 Métodos de búsqueda
    suspend fun findById(id: Int): LogEntity? = repository.findById(id)
    suspend fun findByDueño(idDueño: Int): List<LogEntity> = repository.findByDueño(idDueño)
    suspend fun findByTipo(tipo: String): List<LogEntity> = repository.findByTipo(tipo)
    suspend fun findByFecha(fecha: String): List<LogEntity> = repository.findByFecha(fecha)
}

