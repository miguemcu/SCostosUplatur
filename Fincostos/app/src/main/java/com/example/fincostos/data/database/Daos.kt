package com.example.fincostos.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfiguracionDao {
    @Query("SELECT * FROM configuracion WHERE id = 1")
    suspend fun obtenerConfiguracion(): ConfiguracionEntity?

    @Query("SELECT * FROM configuracion WHERE id = 1")
    fun obtenerConfiguracionFlow(): Flow<ConfiguracionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarConfiguracion(config: ConfiguracionEntity)

    @Update
    suspend fun actualizarConfiguracion(config: ConfiguracionEntity)
}

@Dao
interface GastoDao {
    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    fun obtenerTodosLosGastos(): Flow<List<GastoEntity>>

    @Query("SELECT * FROM gastos WHERE fecha >= :fechaInicio AND fecha <= :fechaFin ORDER BY fecha DESC")
    suspend fun obtenerGastosPorRango(fechaInicio: Long, fechaFin: Long): List<GastoEntity>

    @Insert
    suspend fun insertarGasto(gasto: GastoEntity)

    @Update
    suspend fun actualizarGasto(gasto: GastoEntity)

    @Delete
    suspend fun eliminarGasto(gasto: GastoEntity)

    @Query("DELETE FROM gastos")
    suspend fun eliminarTodosLosGastos()
}

@Dao
interface VentaDao {
    @Query("SELECT * FROM ventas ORDER BY fecha DESC")
    fun obtenerTodasLasVentas(): Flow<List<VentaEntity>>

    @Query("SELECT * FROM ventas WHERE fecha >= :fechaInicio AND fecha <= :fechaFin ORDER BY fecha DESC")
    suspend fun obtenerVentasPorRango(fechaInicio: Long, fechaFin: Long): List<VentaEntity>

    @Insert
    suspend fun insertarVenta(venta: VentaEntity)

    @Update
    suspend fun actualizarVenta(venta: VentaEntity)

    @Delete
    suspend fun eliminarVenta(venta: VentaEntity)

    @Query("DELETE FROM ventas")
    suspend fun eliminarTodasLasVentas()
}
