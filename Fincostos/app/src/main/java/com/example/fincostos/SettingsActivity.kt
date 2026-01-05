package com.example.fincostos

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fincostos.data.AppRepository
import com.google.android.material.textfield.TextInputEditText

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val etNombreFinca = findViewById<TextInputEditText>(R.id.etNombreFinca)
        val etNombreFinquero = findViewById<TextInputEditText>(R.id.etNombreFinquero)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarConfig)
        val btnCancelar = findViewById<Button>(R.id.btnCancelarConfig)

        // Cargar configuración actual
        val config = AppRepository.obtenerConfiguracion()
        etNombreFinca.setText(config.nombreFinca)
        etNombreFinquero.setText(config.nombreFinquero)

        // Listener para cancelar
        btnCancelar.setOnClickListener {
            finish()
        }

        // Listener para guardar
        btnGuardar.setOnClickListener {
            val nombreFinca = etNombreFinca.text.toString().trim()
            val nombreFinquero = etNombreFinquero.text.toString().trim()

            if (nombreFinca.isNotEmpty() && nombreFinquero.isNotEmpty()) {
                AppRepository.actualizarConfiguracion(nombreFinca, nombreFinquero)
                Toast.makeText(this, "Configuración guardada ✓", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
