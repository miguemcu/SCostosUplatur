package com.example.fincostos

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fincostos.data.AppRepository
import com.example.fincostos.data.Venta
import com.example.fincostos.utils.DateUtils
import com.example.fincostos.utils.IntegerMoneyTextWatcher
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class VentaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venta)

        // Referencias a los campos
        val etFecha = findViewById<TextInputEditText>(R.id.etFechaVenta)
        val etCajas = findViewById<TextInputEditText>(R.id.etCajas)
        val etPrecio = findViewById<TextInputEditText>(R.id.etPrecioPorCaja)
        val etTotal = findViewById<TextInputEditText>(R.id.etTotal)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarVenta)
        val btnCancelar = findViewById<Button>(R.id.btnCancelarVenta)

        // Llenar fecha con hoy
        etFecha.setText(DateUtils.todayAsString())

        // Agregar separador de miles a precio (pesos colombianos sin decimales)
        etPrecio.addTextChangedListener(IntegerMoneyTextWatcher(etPrecio))

        // Listener para cancelar
        btnCancelar.setOnClickListener {
            finish()
        }

        // Crear listener para actualizar total
        val actualizarTotal: () -> Unit = {
            try {
                val cajas = etCajas.text.toString().toIntOrNull() ?: 0
                val precio = etPrecio.text.toString().replace(",", "").toDoubleOrNull() ?: 0.0
                val total = cajas * precio
                
                // Formatear total con separador de miles (sin decimales)
                val symbols = DecimalFormatSymbols(Locale.US)
                val df = DecimalFormat("#,##0", symbols)
                etTotal.setText(df.format(total.toLong()))
            } catch (e: Exception) {
                // Ignorar errores de conversión
            }
        }

        // Agregar listeners a los campos
        etCajas.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = actualizarTotal()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etPrecio.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = actualizarTotal()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Listener para guardar
        btnGuardar.setOnClickListener {
            if (validarCampos(etFecha, etCajas, etPrecio)) {
                try {
                    val venta = Venta(
                        fecha = DateUtils.parseDate(etFecha.text.toString()),
                        cajas = etCajas.text.toString().toInt(),
                        precioPorCaja = etPrecio.text.toString().replace(",", "").toDouble()
                    )

                    AppRepository.agregarVenta(venta)
                    Toast.makeText(this, "Venta registrada ✓", Toast.LENGTH_SHORT).show()
                    
                    // Volver a main
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validarCampos(vararg campos: TextInputEditText): Boolean {
        campos.forEach {
            if (it.text.isNullOrBlank()) {
                it.error = "Campo requerido"
                return false
            }
        }
        return true
    }
}
