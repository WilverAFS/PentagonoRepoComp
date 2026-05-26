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
            val existente = repository.findByCotizacion(orden.id_cotizacion)
            if (existente.isNotEmpty()) {
                // Ya existe una orden para esta cotización → evitar duplicados
                println("Error: Ya existe una orden para la cotización ${orden.id_cotizacion}")
            } else {
                repository.addOrden(orden)
                loadOrdenes()
            }
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

    // 🔎 Métodos de búsqueda
    suspend fun findById(id: Int): OrdenEntity? = repository.findById(id)
    suspend fun findByCotizacion(idCotizacion: Int): List<OrdenEntity> = repository.findByCotizacion(idCotizacion)
    suspend fun findByEmpleado(idEmpleado: Int): List<OrdenEntity> = repository.findByEmpleado(idEmpleado)
    suspend fun findByDueño(idDueño: Int): List<OrdenEntity> = repository.findByDueño(idDueño)
    suspend fun findByEstado(estado: String): List<OrdenEntity> = repository.findByEstado(estado)
}

