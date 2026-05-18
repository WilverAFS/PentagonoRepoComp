package com.ingsoftware.pentagono.data

import com.ingsoftware.pentagono.model.Empleado

class EmpleadoRepository(private val dao: EmpleadoDao) {
    suspend fun getEmpleados() = dao.getAllEmpleados()
    suspend fun addEmpleado(empleado: EmpleadoEntity) = dao.insertEmpleado(empleado)
    suspend fun updateEmpleado(empleado: EmpleadoEntity) = dao.updateEmpleado(empleado)
    suspend fun deleteEmpleado(empleado: EmpleadoEntity) = dao.deleteEmpleado(empleado)
}