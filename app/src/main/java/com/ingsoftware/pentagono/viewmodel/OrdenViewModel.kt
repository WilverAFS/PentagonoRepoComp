package com.ingsoftware.pentagono.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingsoftware.pentagono.data.OrdenEntity
import com.ingsoftware.pentagono.data.OrdenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrdenViewModel(private val repository: OrdenRepository) : ViewModel() {
    private val _ordenes = MutableStateFlow<List<OrdenEntity>>(emptyList())
    val ordenes: StateFlow<List<OrdenEntity>> = _ordenes

    init { loadOrdenes() }

    fun loadOrdenes() {
        viewModelScope.launch { _ordenes.value = repository.getOrdenes() }
    }

    fun addOrden(orden: OrdenEntity) {
        viewModelScope.launch {
            repository.addOrden(orden)
            loadOrdenes()
        }
    }

    fun updateOrden(orden: OrdenEntity) {
        viewModelScope.launch {
            repository.updateOrden(orden)
            loadOrdenes()
        }
    }

    fun deleteOrden(orden: OrdenEntity) {
        viewModelScope.launch {
            repository.deleteOrden(orden)
            loadOrdenes()
        }
    }
}
