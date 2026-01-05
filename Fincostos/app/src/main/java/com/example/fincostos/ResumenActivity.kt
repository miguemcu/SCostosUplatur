package com.example.fincostos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fincostos.data.AppRepository
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class ResumenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)

        val tvIngresos = findViewById<TextView>(R.id.tvIngresos)
        val tvGastos = findViewById<TextView>(R.id.tvGastos)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val tvCajas = findViewById<TextView>(R.id.tvCajas)
        val tvCostoPorCaja = findViewById<TextView>(R.id.tvCostoPorCaja)
        val tvMesActual = findViewById<TextView>(R.id.tvMesActual)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnRefrescar = findViewById<Button>(R.id.btnRefrescar)

        // Función para formatear dinero (pesos colombianos sin decimales)
        val formatearDinero: (Double) -> String = { valor ->
            val symbols = DecimalFormatSymbols(Locale.US)
            val df = DecimalFormat("#,##0", symbols)
            df.format(valor.toLong())
        }

        // Función para actualizar datos
        val actualizarDatos = {
            val mesActual = YearMonth.now()
            
            // Mostrar mes actual
            val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
            tvMesActual.text = mesActual.format(formatter).replaceFirstChar { it.uppercase() }

            // Calcular valores
            val ingresos = AppRepository.obtenerTotalIngresos(mesActual)
            val gastos = AppRepository.obtenerTotalGastos(mesActual)
            val resultado = AppRepository.obtenerUtilidad(mesActual)
            val cajas = AppRepository.obtenerTotalCajas(mesActual)
            val costoPorCaja = AppRepository.obtenerCostoPorCaja(mesActual)

            // Mostrar valores con formato de pesos colombianos
            tvIngresos.text = "$${formatearDinero(ingresos)}"
            tvGastos.text = "$${formatearDinero(gastos)}"
            tvCajas.text = "$cajas"
            tvCostoPorCaja.text = "$${formatearDinero(costoPorCaja)}"

            // Mostrar resultado con color
            tvResultado.text = "$${formatearDinero(resultado)}"
            tvResultado.setTextColor(
                if (resultado >= 0) 
                    getColor(android.R.color.holo_green_dark)
                else 
                    getColor(android.R.color.holo_red_dark)
            )
        }

        // Ejecutar al iniciar
        actualizarDatos()

        // Botón volver
        btnVolver.setOnClickListener {
            finish()
        }

        // Botón refrescar
        btnRefrescar.setOnClickListener {
            actualizarDatos()
        }
    }
}
