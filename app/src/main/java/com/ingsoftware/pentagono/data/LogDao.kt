package com.ingsoftware.pentagono.data

import androidx.room.*

@Dao
interface LogDao {
    @Query("SELECT * FROM logs")
    suspend fun getLogs(): List<LogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLog(log: LogEntity)

    @Update
    suspend fun updateLog(log: LogEntity)

    @Delete
    suspend fun deleteLog(log: LogEntity)

    // 🔎 Búsquedas específicas
    @Query("SELECT * FROM logs WHERE id_log = :id")
    suspend fun findById(id: Int): LogEntity?

    @Query("SELECT * FROM logs WHERE id_dueño = :idDueño")
    suspend fun findByDueño(idDueño: Int): List<LogEntity>

    @Query("SELECT * FROM logs WHERE tipo = :tipo")
    suspend fun findByTipo(tipo: String): List<LogEntity>

    @Query("SELECT * FROM logs WHERE fecha = :fecha")
    suspend fun findByFecha(fecha: String): List<LogEntity>
}
