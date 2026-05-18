package com.ingsoftware.pentagono.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingsoftware.pentagono.data.ClienteRepository
import com.ingsoftware.pentagono.data.ClienteEntity
import kotlinx.coroutines.launch

class ClienteViewModel(private val repository: ClienteRepository) : ViewModel() {
    var clientes: List<ClienteEntity> = emptyList()

    fun loadClientes() {
        viewModelScope.launch {
            clientes = repository.getClientes()
        }
    }

    fun addCliente(cliente: ClienteEntity) {
        viewModelScope.launch {
            repository.addCliente(cliente)
            loadClientes()
        }
    }
}
