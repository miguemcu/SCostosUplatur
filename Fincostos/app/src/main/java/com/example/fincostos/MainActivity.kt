package com.example.fincostos

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.fincostos.data.AppRepository
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cardGasto = findViewById<MaterialCardView>(R.id.cardGasto)
        val cardVenta = findViewById<MaterialCardView>(R.id.cardVenta)
        val cardResumen = findViewById<MaterialCardView>(R.id.cardResumen)
        val fabSettings = findViewById<FloatingActionButton>(R.id.fabSettings)

        // TextViews para mostrar datos
        val tvNombreFinca = findViewById<TextView>(R.id.tvNombreFinca)
        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)

        // Actualizar datos de configuración
        actualizarDatos(tvNombreFinca, tvSaludo)

        cardGasto.setOnClickListener {
            startActivity(Intent(this, GastoActivity::class.java))
        }

        cardVenta.setOnClickListener {
            startActivity(Intent(this, VentaActivity::class.java))
        }

        cardResumen.setOnClickListener {
            startActivity(Intent(this, ResumenActivity::class.java))
        }

        fabSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // Actualizar datos cada vez que se regresa a la pantalla
        val tvNombreFinca = findViewById<TextView>(R.id.tvNombreFinca)
        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        actualizarDatos(tvNombreFinca, tvSaludo)
    }

    private fun actualizarDatos(tvNombreFinca: TextView, tvSaludo: TextView) {
        val config = AppRepository.obtenerConfiguracion()
        tvNombreFinca.text = config.nombreFinca
        tvSaludo.text = "¡Hola, ${config.nombreFinquero}!"
    }
}
