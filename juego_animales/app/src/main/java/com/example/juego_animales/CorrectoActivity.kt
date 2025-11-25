package com.example.juego_animales

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CorrectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correcto)

        val btnSiguiente: Button = findViewById(R.id.btnSiguiente)
        val btnMenu: Button = findViewById(R.id.btnMenu)

        // Recuperar nivel actual y fallos acumulados
        val nivelActual = intent.getIntExtra("nivel", 1)
        val fallos = intent.getIntExtra("fallos", 0)
        val totalPreguntas = intent.getIntExtra("total", 10)

        btnSiguiente.setOnClickListener {
            if (nivelActual >= totalPreguntas) {
                // Si ya hemos terminado, mostrar pantalla de resultados
                val resultIntent = Intent(this, ResultActivity::class.java)
                resultIntent.putExtra("fallos", fallos)
                resultIntent.putExtra("total", totalPreguntas)
                startActivity(resultIntent)
                finish()
            } else {
                // Avanzar al siguiente nivel
                val intent = Intent(this, QuizActivity::class.java)
                intent.putExtra("nivel", nivelActual + 1)
                intent.putExtra("fallos", fallos) // mantener contador
                intent.putExtra("total", totalPreguntas)
                startActivity(intent)
                finish()
            }
        }

        btnMenu.setOnClickListener {
            // Volver al menú principal
            val intent = Intent(this, NivelesActivity::class.java)
            intent.putExtra("fallos", fallos)
            intent.putExtra("total", totalPreguntas)
            startActivity(intent)
            finish()
        }
    }
}
