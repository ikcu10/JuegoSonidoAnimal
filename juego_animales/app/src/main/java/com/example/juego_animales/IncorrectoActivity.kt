package com.example.juego_animales

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class IncorrectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incorrecto) // tu XML de cruz

        val btnSiguiente: Button = findViewById(R.id.btnSiguiente)
        val btnMenu: Button = findViewById(R.id.btnMenu)

        btnSiguiente.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        btnMenu.setOnClickListener {
            val intent = Intent(this, NivelesActivity::class.java)
            startActivity(intent)
        }
    }
}