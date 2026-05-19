package com.ingsoftware.pentagono.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingsoftware.pentagono.data.EmpleadoEntity
import com.ingsoftware.pentagono.data.EmpleadoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmpleadoViewModel(private val repository: EmpleadoRepository) : ViewModel() {
    private val _empleados = MutableStateFlow<List<EmpleadoEntity>>(emptyList())
    val empleados: StateFlow<List<EmpleadoEntity>> = _empleados

    init { loadEmpleados() }

    fun loadEmpleados() {
        viewModelScope.launch { _empleados.value = repository.getEmpleados() }
    }

    fun addEmpleado(empleado: EmpleadoEntity) {
        viewModelScope.launch {
            repository.addEmpleado(empleado)
            loadEmpleados()
        }
    }

    fun updateEmpleado(empleado: EmpleadoEntity) {
        viewModelScope.launch {
            repository.updateEmpleado(empleado)
            loadEmpleados()
        }
    }

    fun deleteEmpleado(empleado: EmpleadoEntity) {
        viewModelScope.launch {
            repository.deleteEmpleado(empleado)
            loadEmpleados()
        }
    }
}
