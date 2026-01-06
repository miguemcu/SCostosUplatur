package com.example.fincostos

import android.app.Application
import com.example.fincostos.data.database.FincostosDatabase
import com.example.fincostos.data.repository.FincostosRepository

class FincostosApplication : Application() {
    
    val database by lazy { FincostosDatabase.getDatabase(this) }
    val repository by lazy {
        FincostosRepository(
            database.configuracionDao(),
            database.gastoDao(),
            database.ventaDao()
        )
    }
}
