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

        // 1. Inicializar Python
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        // BOTÓN 1: JUGAR
        val botonComenzar = findViewById<Button>(R.id.boton_comenzar)
        botonComenzar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // BOTÓN 2: ESTADÍSTICAS
        val botonDashboard = findViewById<Button>(R.id.boton_ir_dashboard)
        botonDashboard.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}