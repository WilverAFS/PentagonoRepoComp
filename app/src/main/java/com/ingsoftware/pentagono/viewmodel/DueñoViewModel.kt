package com.ingsoftware.pentagono.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingsoftware.pentagono.data.DueñoEntity
import com.ingsoftware.pentagono.data.DueñoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DueñoViewModel(private val repository: DueñoRepository) : ViewModel() {
    private val _dueños = MutableStateFlow<List<DueñoEntity>>(emptyList())
    val dueños: StateFlow<List<DueñoEntity>> = _dueños

    init { loadDueños() }

    fun loadDueños() {
        viewModelScope.launch { _dueños.value = repository.getDueños() }
    }

    fun addDueño(dueño: DueñoEntity) {
        viewModelScope.launch {
            repository.addDueño(dueño)
            loadDueños()
        }
    }

    fun updateDueño(dueño: DueñoEntity) {
        viewModelScope.launch {
            repository.updateDueño(dueño)
            loadDueños()
        }
    }

    fun deleteDueño(dueño: DueñoEntity) {
        viewModelScope.launch {
            repository.deleteDueño(dueño)
            loadDueños()
        }
    }

    // 🔎 Métodos de búsqueda
    suspend fun findById(id: Int): DueñoEntity? = repository.findById(id)
    suspend fun findByNombre(nombre: String): List<DueñoEntity> = repository.findByNombre(nombre)
}

