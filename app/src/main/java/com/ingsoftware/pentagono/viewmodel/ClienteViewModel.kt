package com.ingsoftware.pentagono.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingsoftware.pentagono.data.ClienteEntity
import com.ingsoftware.pentagono.data.ClienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ClienteViewModel(private val repository: ClienteRepository) : ViewModel() {
    private val _clientes = MutableStateFlow<List<ClienteEntity>>(emptyList())
    val clientes: StateFlow<List<ClienteEntity>> = _clientes
    init { loadClientes() }
    fun loadClientes() {
        viewModelScope.launch { _clientes.value = repository.getClientes() }
    }
    fun addCliente(cliente: ClienteEntity) {
        viewModelScope.launch {
            repository.addCliente(cliente)
            loadClientes()
        }
    }
    fun updateCliente(cliente: ClienteEntity) {
        viewModelScope.launch {
            repository.updateCliente(cliente)
            loadClientes()
        }
    }
    fun deleteCliente(cliente: ClienteEntity) {
        viewModelScope.launch {
            repository.deleteCliente(cliente)
            loadClientes()
        }
    }
    suspend fun findByTelefono(telefono: String): ClienteEntity? = repository.findByTelefono(telefono)
    suspend fun findByNombre(nombre: String): List<ClienteEntity> {
        return repository.findByNombre(nombre)
    }
}

