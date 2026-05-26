package com.ingsoftware.pentagono.data
import androidx.room.Database
import androidx.room.RoomDatabase
@Database(
    entities = [
        ClienteEntity::class,
        CotizacionEntity::class,
        OrdenEntity::class,
        EmpleadoEntity::class,
        DueñoEntity::class,
        LogEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clienteDao(): ClienteDao
    abstract fun cotizacionDao(): CotizacionDao
    abstract fun ordenDao(): OrdenDao
    abstract fun empleadoDao(): EmpleadoDao
    abstract fun dueñoDao(): DueñoDao
    abstract fun logDao(): LogDao
}
