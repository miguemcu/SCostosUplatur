package com.example.fincostos.data

import java.time.LocalDate
import java.time.YearMonth

// Data class para configuración
data class Configuracion(
    val nombreFinca: String = "Mi Finca",
    val nombreFinquero: String = "Finquero"
)

// Singleton para manejar datos en memoria
object AppRepository {
    private val gastos = mutableListOf<Gasto>()
    private val ventas = mutableListOf<Venta>()
    private var configuracion = Configuracion()

    fun actualizarConfiguracion(nombreFinca: String, nombreFinquero: String) {
        configuracion = Configuracion(
            nombreFinca = nombreFinca.ifBlank { "Mi Finca" },
            nombreFinquero = nombreFinquero.ifBlank { "Finquero" }
        )
    }

    fun obtenerConfiguracion(): Configuracion = configuracion

    fun agregarGasto(gasto: Gasto) {
        gastos.add(gasto)
    }

    fun agregarVenta(venta: Venta) {
        ventas.add(venta)
    }

    fun obtenerGastosDelMes(yearMonth: YearMonth): List<Gasto> {
        return gastos.filter {
            YearMonth.from(it.fecha) == yearMonth
        }
    }

    fun obtenerVentasDelMes(yearMonth: YearMonth): List<Venta> {
        return ventas.filter {
            YearMonth.from(it.fecha) == yearMonth
        }
    }

    fun obtenerTotalGastos(yearMonth: YearMonth): Double {
        return obtenerGastosDelMes(yearMonth).sumOf { it.valor }
    }

    fun obtenerTotalIngresos(yearMonth: YearMonth): Double {
        return obtenerVentasDelMes(yearMonth).sumOf { it.total }
    }

    fun obtenerTotalCajas(yearMonth: YearMonth): Int {
        return obtenerVentasDelMes(yearMonth).sumOf { it.cajas }
    }

    fun obtenerUtilidad(yearMonth: YearMonth): Double {
        return obtenerTotalIngresos(yearMonth) - obtenerTotalGastos(yearMonth)
    }

    fun obtenerCostoPorCaja(yearMonth: YearMonth): Double {
        val totalCajas = obtenerTotalCajas(yearMonth)
        return if (totalCajas > 0) obtenerTotalGastos(yearMonth) / totalCajas else 0.0
    }

    fun obtenerTodosLosGastos(): List<Gasto> = gastos.toList()
    fun obtenerTodasLasVentas(): List<Venta> = ventas.toList()
    
    fun limpiar() {
        gastos.clear()
        ventas.clear()
    }
}
