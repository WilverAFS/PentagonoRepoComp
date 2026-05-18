package com.ingsoftware.pentagono.data
import androidx.room.*
import com.ingsoftware.pentagono.model.Empleado

@Dao
interface EmpleadoDao {
    @Query("SELECT * FROM empleados")
    suspend fun getAllEmpleados(): List<EmpleadoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmpleado(empleado: EmpleadoEntity)

    @Update
    suspend fun updateEmpleado(empleado: EmpleadoEntity)

    @Delete
    suspend fun deleteEmpleado(empleado: EmpleadoEntity)
}