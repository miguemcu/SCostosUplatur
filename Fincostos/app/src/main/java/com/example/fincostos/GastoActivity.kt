package com.example.fincostos

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fincostos.data.AppRepository
import com.example.fincostos.data.Gasto
import com.example.fincostos.utils.DateUtils
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDate

class GastoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gasto)

        // Referencias a los campos
        val etFecha = findViewById<TextInputEditText>(R.id.etFecha)
        val etTipoGasto = findViewById<TextInputEditText>(R.id.etTipoGasto)
        val etConcepto = findViewById<TextInputEditText>(R.id.etConcepto)
        val etValor = findViewById<TextInputEditText>(R.id.etValor)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarGasto)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        // Llenar fecha con hoy
        etFecha.setText(DateUtils.todayAsString())

        // Listener para cancelar
        btnCancelar.setOnClickListener {
            finish()
        }

        // Listener para guardar
        btnGuardar.setOnClickListener {
            if (validarCampos(etFecha, etTipoGasto, etConcepto, etValor)) {
                try {
                    val gasto = Gasto(
                        fecha = DateUtils.parseDate(etFecha.text.toString()),
                        tipoGasto = etTipoGasto.text.toString(),
                        concepto = etConcepto.text.toString(),
                        valor = etValor.text.toString().toDouble()
                    )

                    AppRepository.agregarGasto(gasto)
                    Toast.makeText(this, "Gasto registrado ✓", Toast.LENGTH_SHORT).show()
                    
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

