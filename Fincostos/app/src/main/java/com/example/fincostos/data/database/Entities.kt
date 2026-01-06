package com.example.fincostos.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalDate

// Converters para Room (LocalDate)
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}

@Entity(tableName = "configuracion")
data class ConfiguracionEntity(
    @PrimaryKey val id: Int = 1,
    val nombreFinca: String,
    val nombreFinquero: String,
    val mesActual: Int,
    val anioActual: Int,
    val fechaCreacion: LocalDate
)

@Entity(tableName = "gastos")
data class GastoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: LocalDate,
    val tipoGasto: String,
    val concepto: String,
    val valor: Double,
    val observaciones: String = ""
)

@Entity(tableName = "ventas")
data class VentaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fecha: LocalDate,
    val cajas: Int,
    val precioPorCaja: Double,
    val cliente: String = ""
) {
    val total: Double get() = cajas * precioPorCaja
}
