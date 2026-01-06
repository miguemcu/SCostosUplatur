package com.example.fincostos

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fincostos.data.Gasto
import com.example.fincostos.utils.DateUtils
import com.example.fincostos.utils.IntegerMoneyTextWatcher
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class GastoActivity : AppCompatActivity() {

    private lateinit var repository: com.example.fincostos.data.repository.FincostosRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gasto)

        // Obtener repository desde Application
        repository = (application as FincostosApplication).repository

        // Referencias a los campos
        val etFecha = findViewById<TextInputEditText>(R.id.etFecha)
        val etTipoGasto = findViewById<TextInputEditText>(R.id.etTipoGasto)
        val etConcepto = findViewById<TextInputEditText>(R.id.etConcepto)
        val etValor = findViewById<TextInputEditText>(R.id.etValor)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarGasto)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Llenar fecha con hoy
        etFecha.setText(DateUtils.todayAsString())

        // Agregar separador de miles al campo de valor (pesos colombianos)
        etValor.addTextChangedListener(IntegerMoneyTextWatcher(etValor))

        // Listener para cancelar
        btnCancelar.setOnClickListener {
            finish()
        }

        // Listener para guardar
        btnGuardar.setOnClickListener {
            if (validarCampos(etFecha, etTipoGasto, etConcepto, etValor)) {
                lifecycleScope.launch {
                    try {
                        val gasto = Gasto(
                            fecha = DateUtils.parseDate(etFecha.text.toString()),
                            tipoGasto = etTipoGasto.text.toString(),
                            concepto = etConcepto.text.toString(),
                            valor = etValor.text.toString().replace(",", "").toDouble()
                        )

                        Log.d("GastoActivity", "Guardando gasto: $gasto")
                        repository.agregarGasto(gasto)
                        Log.d("GastoActivity", "Gasto guardado exitosamente")
                        Toast.makeText(this@GastoActivity, "Gasto registrado ✓", Toast.LENGTH_SHORT).show()
                        
                        // Volver a main
                        finish()
                    } catch (e: Exception) {
                        Log.e("GastoActivity", "Error guardando gasto", e)
                        Toast.makeText(this@GastoActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
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

