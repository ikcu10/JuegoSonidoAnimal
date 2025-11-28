package com.iker.sonidoanimal

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.view.View

class ResultadoCorrectoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_correcto)

        val nivelActual = intent.getIntExtra("nivel", 1)
        val nivelMaximo = 10

        // 1. Abrimos la memoria
        val prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE)
        val maxNivelDesbloqueado = prefs.getInt("nivelDesbloqueado", 1)

        // 2. Si acabamos de superar nuestro propio record
        // Entonces desbloqueamos el siguiente nivel
        if (nivelActual == maxNivelDesbloqueado)
        {
            val editor = prefs.edit()
            editor.putInt("nivelDesbloqueado", maxNivelDesbloqueado + 1)
            editor.apply()
        }

        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)
        val btnMenu = findViewById<Button>(R.id.btnMenu)

        if (nivelActual == nivelMaximo) {
            btnSiguiente.visibility = View.GONE
        }

        btnSiguiente.setOnClickListener {
            val siguienteNivel = nivelActual + 1
            if (siguienteNivel <= 10) {
                val intent = Intent(this, JuegoAnimalesActivity::class.java)
                intent.putExtra("nivel", siguienteNivel)
                startActivity(intent)
            } else {
                Toast.makeText(this, "¡Juego completado!", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

        btnMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}