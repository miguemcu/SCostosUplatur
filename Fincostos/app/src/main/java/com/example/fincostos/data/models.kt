package com.example.fincostos.data

import java.time.LocalDate

data class Gasto(
    val id: Int = 0,
    val fecha: LocalDate,
    val tipoGasto: String,
    val concepto: String,
    val valor: Double,
    val observaciones: String = ""
)

data class Venta(
    val id: Int = 0,
    val fecha: LocalDate,
    val cajas: Int,
    val precioPorCaja: Double,
    val cliente: String = ""
) {
    val total: Double get() = cajas * precioPorCaja
}
