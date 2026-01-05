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
import com.google.android.material.textfield.TextInputEditText

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

        // Listener para cancelar
        btnCancelar.setOnClickListener {
            finish()
        }

        // Crear listener para actualizar total
        val actualizarTotal: () -> Unit = {
            try {
                val cajas = etCajas.text.toString().toIntOrNull() ?: 0
                val precio = etPrecio.text.toString().toDoubleOrNull() ?: 0.0
                val total = cajas * precio
                etTotal.setText(String.format("%.2f", total))
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
                        precioPorCaja = etPrecio.text.toString().toDouble()
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
