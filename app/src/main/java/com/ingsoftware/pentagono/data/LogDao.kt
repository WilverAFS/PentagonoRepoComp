package com.ingsoftware.pentagono.data
import androidx.room.*
import com.ingsoftware.pentagono.model.Log

@Dao
interface LogDao {
    @Query("SELECT * FROM logs")
    suspend fun getAllLogs(): List<LogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: LogEntity)

    @Update
    suspend fun updateLog(log: LogEntity)

    @Delete
    suspend fun deleteLog(log: LogEntity)
}