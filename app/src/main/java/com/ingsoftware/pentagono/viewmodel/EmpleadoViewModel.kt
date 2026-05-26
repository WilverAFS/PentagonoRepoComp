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

    // ✅ Nuevo StateFlow para errores
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init { loadEmpleados() }

    fun loadEmpleados() {
        viewModelScope.launch { _empleados.value = repository.getEmpleados() }
    }

    fun addEmpleado(empleado: EmpleadoEntity) {
        viewModelScope.launch {
            // ✅ Validar unicidad de CURP antes de insertar
            val existente = repository.findByCurp(empleado.curp)
            if (existente != null) {
                _error.value = "Ya existe un empleado con la CURP proporcionada"
            } else {
                repository.addEmpleado(empleado)
                loadEmpleados()
                _error.value = null // limpiar error si todo salió bien
            }
        }
    }

    fun updateEmpleado(empleado: EmpleadoEntity) {
        viewModelScope.launch {
            repository.updateEmpleado(empleado)
            loadEmpleados()
            _error.value = null // limpiar error en caso de edición exitosa
        }
    }

    fun deleteEmpleado(empleado: EmpleadoEntity) {
        viewModelScope.launch {
            repository.deleteEmpleado(empleado)
            loadEmpleados()
            _error.value = null // limpiar error en caso de eliminación exitosa
        }
    }

    // 🔎 Métodos de búsqueda
    suspend fun findByCurp(curp: String): EmpleadoEntity? {
        return repository.findByCurp(curp)
    }

    suspend fun findByNombre(nombre: String): List<EmpleadoEntity> {
        return repository.findByNombre(nombre)
    }

    suspend fun findByTelefono(telefono: String): List<EmpleadoEntity> {
        return repository.findByTelefono(telefono)
    }
}
