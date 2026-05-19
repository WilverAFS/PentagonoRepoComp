package com.ingsoftware.pentagono.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ingsoftware.pentagono.data.CotizacionEntity
import com.ingsoftware.pentagono.data.CotizacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CotizacionViewModel(private val repository: CotizacionRepository) : ViewModel() {
    private val _cotizaciones = MutableStateFlow<List<CotizacionEntity>>(emptyList())
    val cotizaciones: StateFlow<List<CotizacionEntity>> = _cotizaciones

    init { loadCotizaciones() }

    fun loadCotizaciones() {
        viewModelScope.launch { _cotizaciones.value = repository.getCotizaciones() }
    }

    fun addCotizacion(cotizacion: CotizacionEntity) {
        viewModelScope.launch {
            repository.addCotizacion(cotizacion)
            loadCotizaciones()
        }
    }

    fun updateCotizacion(cotizacion: CotizacionEntity) {
        viewModelScope.launch {
            repository.updateCotizacion(cotizacion)
            loadCotizaciones()
        }
    }

    fun deleteCotizacion(cotizacion: CotizacionEntity) {
        viewModelScope.launch {
            repository.deleteCotizacion(cotizacion)
            loadCotizaciones()
        }
    }
}
