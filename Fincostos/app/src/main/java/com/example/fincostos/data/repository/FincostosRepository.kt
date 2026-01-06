package com.example.fincostos.data.repository

import com.example.fincostos.data.Configuracion
import com.example.fincostos.data.Gasto
import com.example.fincostos.data.Venta
import com.example.fincostos.data.database.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth

class FincostosRepository(
    private val configuracionDao: ConfiguracionDao,
    private val gastoDao: GastoDao,
    private val ventaDao: VentaDao
) {
    
    // =============== CONFIGURACIÓN ===============
    
    suspend fun obtenerConfiguracion(): Configuracion {
        val entity = configuracionDao.obtenerConfiguracion()
        return if (entity != null) {
            Configuracion(
                nombreFinca = entity.nombreFinca,
                nombreFinquero = entity.nombreFinquero
            )
        } else {
            // Crear configuración por defecto
            val defaultConfig = ConfiguracionEntity(
                id = 1,
                nombreFinca = "Mi Finca",
                nombreFinquero = "Finquero",
                mesActual = YearMonth.now().monthValue,
                anioActual = YearMonth.now().year,
                fechaCreacion = LocalDate.now()
            )
            configuracionDao.insertarConfiguracion(defaultConfig)
            Configuracion("Mi Finca", "Finquero")
        }
    }

    suspend fun actualizarConfiguracion(nombreFinca: String, nombreFinquero: String) {
        val entity = configuracionDao.obtenerConfiguracion() ?: ConfiguracionEntity(
            id = 1,
            nombreFinca = nombreFinca,
            nombreFinquero = nombreFinquero,
            mesActual = YearMonth.now().monthValue,
            anioActual = YearMonth.now().year,
            fechaCreacion = LocalDate.now()
        )
        
        val updated = entity.copy(
            nombreFinca = nombreFinca.ifBlank { "Mi Finca" },
            nombreFinquero = nombreFinquero.ifBlank { "Finquero" }
        )
        
        configuracionDao.actualizarConfiguracion(updated)
    }

    // =============== GASTOS ===============
    
    suspend fun agregarGasto(gasto: Gasto) {
        val entity = GastoEntity(
            fecha = gasto.fecha,
            tipoGasto = gasto.tipoGasto,
            concepto = gasto.concepto,
            valor = gasto.valor,
            observaciones = gasto.observaciones
        )
        gastoDao.insertarGasto(entity)
    }

    suspend fun obtenerGastosDelMes(yearMonth: YearMonth): List<Gasto> {
        val inicio = yearMonth.atDay(1).toEpochDay()
        val fin = yearMonth.atEndOfMonth().toEpochDay()
        
        return gastoDao.obtenerGastosPorRango(inicio, fin).map { entity ->
            Gasto(
                id = entity.id,
                fecha = entity.fecha,
                tipoGasto = entity.tipoGasto,
                concepto = entity.concepto,
                valor = entity.valor,
                observaciones = entity.observaciones
            )
        }
    }

    // =============== VENTAS ===============
    
    suspend fun agregarVenta(venta: Venta) {
        val entity = VentaEntity(
            fecha = venta.fecha,
            cajas = venta.cajas,
            precioPorCaja = venta.precioPorCaja,
            cliente = venta.cliente
        )
        ventaDao.insertarVenta(entity)
    }

    suspend fun obtenerVentasDelMes(yearMonth: YearMonth): List<Venta> {
        val inicio = yearMonth.atDay(1).toEpochDay()
        val fin = yearMonth.atEndOfMonth().toEpochDay()
        
        return ventaDao.obtenerVentasPorRango(inicio, fin).map { entity ->
            Venta(
                id = entity.id,
                fecha = entity.fecha,
                cajas = entity.cajas,
                precioPorCaja = entity.precioPorCaja,
                cliente = entity.cliente
            )
        }
    }

    // =============== CÁLCULOS ===============
    
    suspend fun obtenerTotalGastos(yearMonth: YearMonth): Double {
        return obtenerGastosDelMes(yearMonth).sumOf { it.valor }
    }

    suspend fun obtenerTotalIngresos(yearMonth: YearMonth): Double {
        return obtenerVentasDelMes(yearMonth).sumOf { it.total }
    }

    suspend fun obtenerTotalCajas(yearMonth: YearMonth): Int {
        return obtenerVentasDelMes(yearMonth).sumOf { it.cajas }
    }

    suspend fun obtenerUtilidad(yearMonth: YearMonth): Double {
        return obtenerTotalIngresos(yearMonth) - obtenerTotalGastos(yearMonth)
    }

    suspend fun obtenerCostoPorCaja(yearMonth: YearMonth): Double {
        val totalCajas = obtenerTotalCajas(yearMonth)
        return if (totalCajas > 0) obtenerTotalGastos(yearMonth) / totalCajas else 0.0
    }
}
