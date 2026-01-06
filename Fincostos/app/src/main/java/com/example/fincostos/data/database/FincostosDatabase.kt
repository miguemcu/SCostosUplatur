package com.example.fincostos.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ConfiguracionEntity::class, GastoEntity::class, VentaEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FincostosDatabase : RoomDatabase() {
    
    abstract fun configuracionDao(): ConfiguracionDao
    abstract fun gastoDao(): GastoDao
    abstract fun ventaDao(): VentaDao

    companion object {
        @Volatile
        private var INSTANCE: FincostosDatabase? = null

        fun getDatabase(context: Context): FincostosDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FincostosDatabase::class.java,
                    "fincostos_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
