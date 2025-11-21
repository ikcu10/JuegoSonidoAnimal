package com.example.juego_animales

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class CorrectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correcto) // tu XML de check

        val btnSiguiente: Button = findViewById(R.id.btnSiguiente)
        val btnMenu: Button = findViewById(R.id.btnMenu)

        btnSiguiente.setOnClickListener {
            // Ir al siguiente nivel (ejemplo: otra pregunta del quiz)
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        btnMenu.setOnClickListener {
            // Volver al menú principal
            val intent = Intent(this, NivelesActivity::class.java)
            startActivity(intent)
        }
    }
}
