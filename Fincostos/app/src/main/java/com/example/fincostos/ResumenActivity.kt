package com.example.fincostos

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class ResumenActivity : AppCompatActivity() {

    private lateinit var repository: com.example.fincostos.data.repository.FincostosRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)

        // Obtener repository desde Application
        repository = (application as FincostosApplication).repository

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
            lifecycleScope.launch {
                try {
                    val mesActual = YearMonth.now()
                    Log.d("ResumenActivity", "Cargando resumen para: $mesActual")
                    
                    // Mostrar mes actual
                    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
                    tvMesActual.text = mesActual.format(formatter).replaceFirstChar { it.uppercase() }

                    // Calcular valores
                    val ingresos = repository.obtenerTotalIngresos(mesActual)
                    val gastos = repository.obtenerTotalGastos(mesActual)
                    val resultado = repository.obtenerUtilidad(mesActual)
                    val cajas = repository.obtenerTotalCajas(mesActual)
                    val costoPorCaja = repository.obtenerCostoPorCaja(mesActual)
                    
                    Log.d("ResumenActivity", "Ingresos: $ingresos, Gastos: $gastos, Cajas: $cajas")

                    // Mostrar valores con formato de pesos colombianos
                    tvIngresos.text = "\$${formatearDinero(ingresos)}"
                    tvGastos.text = "\$${formatearDinero(gastos)}"
                    tvCajas.text = "$cajas"
                    tvCostoPorCaja.text = "\$${formatearDinero(costoPorCaja)}"

                    // Mostrar resultado con color
                    tvResultado.text = "\$${formatearDinero(resultado)}"
                    tvResultado.setTextColor(
                        if (resultado >= 0) 
                            getColor(android.R.color.holo_green_dark)
                        else 
                            getColor(android.R.color.holo_red_dark)
                    )
                } catch (e: Exception) {
                    Log.e("ResumenActivity", "Error cargando resumen", e)
                    Toast.makeText(this@ResumenActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
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
