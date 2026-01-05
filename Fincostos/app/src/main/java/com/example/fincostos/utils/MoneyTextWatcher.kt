package com.example.fincostos.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MoneyTextWatcher(private val editText: EditText) : TextWatcher {

    private var isUpdating = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isUpdating) return

        isUpdating = true

        val text = s.toString().replace(",", "").replace(".", "")
        
        if (text.isEmpty()) {
            editText.setText("")
            isUpdating = false
            return
        }

        // Convertir a número
        try {
            val value = text.toLong()
            
            // Crear formato con separador de miles
            val symbols = DecimalFormatSymbols(Locale.US)
            val df = DecimalFormat("#,##0", symbols)
            
            val formatted = df.format(value / 100.0)
            editText.setText(formatted)
            editText.setSelection(formatted.length)
        } catch (e: Exception) {
            editText.setText("")
        }

        isUpdating = false
    }
}

// Para pesos colombianos: solo separador de miles, sin decimales
class IntegerMoneyTextWatcher(private val editText: EditText) : TextWatcher {

    private var isUpdating = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isUpdating) return

        isUpdating = true

        val text = s.toString().replace(",", "")
        
        if (text.isEmpty()) {
            editText.setText("")
            isUpdating = false
            return
        }

        try {
            val value = text.toLong()
            
            // Crear formato con separador de miles (sin decimales)
            val symbols = DecimalFormatSymbols(Locale.US)
            val df = DecimalFormat("#,##0", symbols)
            
            val formatted = df.format(value)
            editText.setText(formatted)
            editText.setSelection(formatted.length)
        } catch (e: Exception) {
            editText.setText("")
        }

        isUpdating = false
    }
}

class DecimalMoneyTextWatcher(private val editText: EditText) : TextWatcher {

    private var isUpdating = false

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isUpdating) return

        isUpdating = true

        val text = s.toString().replace(",", "")
        
        if (text.isEmpty()) {
            editText.setText("")
            isUpdating = false
            return
        }

        try {
            // Limitar a 2 decimales
            val parts = text.split(".")
            var formattedText: String
            
            if (parts.size == 1) {
                // Sin punto decimal
                val value = parts[0].toLong()
                val symbols = DecimalFormatSymbols(Locale.US)
                val df = DecimalFormat("#,##0", symbols)
                formattedText = df.format(value)
            } else {
                // Con punto decimal
                val intPart = parts[0].toLong()
                val symbols = DecimalFormatSymbols(Locale.US)
                val df = DecimalFormat("#,##0", symbols)
                val formattedInt = df.format(intPart)
                
                val decimalPart = parts[1].take(2)
                formattedText = "$formattedInt.$decimalPart"
            }
            
            editText.setText(formattedText)
            editText.setSelection(formattedText.length)
        } catch (e: Exception) {
            editText.setText("")
        }

        isUpdating = false
    }
}
