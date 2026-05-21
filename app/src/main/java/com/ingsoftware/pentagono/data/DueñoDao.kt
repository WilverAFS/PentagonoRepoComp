package com.ingsoftware.pentagono.data

import androidx.room.*

@Dao
interface DueñoDao {
    @Query("SELECT * FROM dueños")
    suspend fun getDueños(): List<DueñoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDueño(dueño: DueñoEntity)

    @Update
    suspend fun updateDueño(dueño: DueñoEntity)

    @Delete
    suspend fun deleteDueño(dueño: DueñoEntity)

    // 🔎 Búsquedas específicas
    @Query("SELECT * FROM dueños WHERE id_dueño = :id")
    suspend fun findById(id: Int): DueñoEntity?

    @Query("SELECT * FROM dueños WHERE nombre LIKE '%' || :nombre || '%'")
    suspend fun findByNombre(nombre: String): List<DueñoEntity>
}
