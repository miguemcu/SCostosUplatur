package com.example.fincostos

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var repository: com.example.fincostos.data.repository.FincostosRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Obtener repository desde Application
        repository = (application as FincostosApplication).repository

        val etNombreFinca = findViewById<TextInputEditText>(R.id.etNombreFinca)
        val etNombreFinquero = findViewById<TextInputEditText>(R.id.etNombreFinquero)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarConfig)
        val btnCancelar = findViewById<Button>(R.id.btnCancelarConfig)

        // Cargar configuración actual
        lifecycleScope.launch {
            try {
                val config = repository.obtenerConfiguracion()
                etNombreFinca.setText(config.nombreFinca)
                etNombreFinquero.setText(config.nombreFinquero)
            } catch (e: Exception) {
                // Error al cargar configuración
            }
        }

        // Listener para cancelar
        btnCancelar.setOnClickListener {
            finish()
        }

        // Listener para guardar
        btnGuardar.setOnClickListener {
            val nombreFinca = etNombreFinca.text.toString().trim()
            val nombreFinquero = etNombreFinquero.text.toString().trim()

            if (nombreFinca.isNotEmpty() && nombreFinquero.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        repository.actualizarConfiguracion(nombreFinca, nombreFinquero)
                        Toast.makeText(this@SettingsActivity, "Configuración guardada ✓", Toast.LENGTH_SHORT).show()
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(this@SettingsActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
