package com.ingsoftware.pentagono.data

class EmpleadoRepository(private val dao: EmpleadoDao) {

    // CRUD
    suspend fun getEmpleados(): List<EmpleadoEntity> = dao.getEmpleados()
    suspend fun addEmpleado(empleado: EmpleadoEntity) = dao.addEmpleado(empleado)
    suspend fun updateEmpleado(empleado: EmpleadoEntity) = dao.updateEmpleado(empleado)
    suspend fun deleteEmpleado(empleado: EmpleadoEntity) = dao.deleteEmpleado(empleado)

    // 🔎 Búsquedas
    suspend fun findByCurp(curp: String): EmpleadoEntity? = dao.findByCurp(curp)
    suspend fun findByNombre(nombre: String): List<EmpleadoEntity> = dao.findByNombre(nombre)
    suspend fun findByTelefono(telefono: String): List<EmpleadoEntity> = dao.findByTelefono(telefono)
}
