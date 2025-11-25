package com.example.juego_animales

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class IncorrectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incorrecto)

        val btnReintentar: Button = findViewById(R.id.btnSiguiente) // mismo botón, pero aquí es "Reintentar"
        val btnMenu: Button = findViewById(R.id.btnMenu)

        // Recuperar nivel actual y fallos acumulados
        val nivelActual = intent.getIntExtra("nivel", 1)
        var fallos = intent.getIntExtra("fallos", 0)
        val totalPreguntas = intent.getIntExtra("total", 10)

        // Cada vez que llegamos aquí, sumamos un fallo
        fallos++

        btnReintentar.setOnClickListener {
            // Volver al mismo nivel para repetirlo
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("nivel", nivelActual)
            intent.putExtra("fallos", fallos)
            intent.putExtra("total", totalPreguntas)
            startActivity(intent)
            finish()
        }

        btnMenu.setOnClickListener {
            val intent = Intent(this, NivelesActivity::class.java)
            intent.putExtra("fallos", fallos)
            intent.putExtra("total", totalPreguntas)
            startActivity(intent)
            finish()
        }
    }
}
