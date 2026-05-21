package com.ingsoftware.pentagono.data

import androidx.room.*
import com.ingsoftware.pentagono.data.EmpleadoEntity

@Dao
interface EmpleadoDao {
    @Query("SELECT * FROM empleados")
    suspend fun getEmpleados(): List<EmpleadoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addEmpleado(empleado: EmpleadoEntity)

    @Update
    suspend fun updateEmpleado(empleado: EmpleadoEntity)

    @Delete
    suspend fun deleteEmpleado(empleado: EmpleadoEntity)

    // 🔎 Búsquedas específicas
    @Query("SELECT * FROM empleados WHERE curp = :curp")
    suspend fun findByCurp(curp: String): EmpleadoEntity?

    @Query("SELECT * FROM empleados WHERE nombre LIKE '%' || :nombre || '%'")
    suspend fun findByNombre(nombre: String): List<EmpleadoEntity>

    @Query("SELECT * FROM empleados WHERE telefono = :telefono")
    suspend fun findByTelefono(telefono: String): List<EmpleadoEntity>
}
