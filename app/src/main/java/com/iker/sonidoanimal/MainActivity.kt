package com.iker.sonidoanimal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Inicializar Python (OBLIGATORIO)
        // Lo iniciamos aquí para que esté listo en toda la app
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        // ---------------------------------------------------------
        // BOTÓN 1: JUGAR (Tu lógica original)
        // ---------------------------------------------------------
        val botonComenzar = findViewById<Button>(R.id.boton_comenzar)
        botonComenzar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // ---------------------------------------------------------
        // BOTÓN 2: ESTADÍSTICAS (El que faltaba)
        // ---------------------------------------------------------
        // Buscamos el botón nuevo por su ID del XML
        val botonDashboard = findViewById<Button>(R.id.boton_ir_dashboard)

        // Le decimos que al hacer click, abra la DashboardActivity
        botonDashboard.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}