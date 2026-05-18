package com.ingsoftware.pentagono.data
import androidx.room.*
import com.ingsoftware.pentagono.model.Dueño

@Dao
interface DueñoDao {
    @Query("SELECT * FROM dueños")
    suspend fun getAllDueños(): List<DueñoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDueño(dueño: DueñoEntity)

    @Update
    suspend fun updateDueño(dueño: DueñoEntity)

    @Delete
    suspend fun deleteDueño(dueño: DueñoEntity)
}