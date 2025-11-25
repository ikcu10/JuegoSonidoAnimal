package com.example.juego_animales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.content.Intent
import android.media.Image

class NivelesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aventura)

        val avatarElegido = intent.getStringExtra("avatar")

        val imgAvatar = findViewById<ImageView>(R.id.imgAvatar)

        // Dependiendo del avatar elegido, mostramos su imagen
        when (avatarElegido) {
            "oso" -> imgAvatar.setImageResource(R.drawable.oso)
            "rata" -> imgAvatar.setImageResource(R.drawable.rata)
            "pinguino" -> imgAvatar.setImageResource(R.drawable.pinguino)
            "zorro" -> imgAvatar.setImageResource(R.drawable.zorro)
            "conejo" -> imgAvatar.setImageResource(R.drawable.conejo)
            "jirafa" -> imgAvatar.setImageResource(R.drawable.jirafa)
            else -> imgAvatar.setImageResource(R.drawable.ic_launcher_foreground)
        }

        // Botones de misiones
        val btnMision1 = findViewById<ImageView>(R.id.btnMision1)
        val btnMision2 = findViewById<ImageView>(R.id.btnMision2)
        val btnMision3 = findViewById<ImageView>(R.id.btnMision3)
        val btnMision4 = findViewById<ImageView>(R.id.btnMision4)
        val btnMision5 = findViewById<ImageView>(R.id.btnMision5)
        val btnMision6 = findViewById<ImageView>(R.id.btnMision6)
        val btnMision7 = findViewById<ImageView>(R.id.btnMision7)
        val btnMision8 = findViewById<ImageView>(R.id.btnMision8)
        val btnMision9 = findViewById<ImageView>(R.id.btnMision9)
        val btnMision10 = findViewById<ImageView>(R.id.btnMision10)


        btnMision1.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("nivel", 1) // abrir nivel 1
            startActivity(intent)
        }

        btnMision2.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("nivel", 2) // abrir nivel 2
            startActivity(intent)
        }

        btnMision3.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("nivel", 3) // abrir nivel 3
            startActivity(intent)
        }

        btnMision4.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("nivel", 4) // abrir nivel 4
            startActivity(intent)
        }

        btnMision5.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("nivel", 5) // abrir nivel 5
            startActivity(intent)
        }

        btnMision6.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("nivel", 6) // abrir nivel 6
            startActivity(intent)
        }

        btnMision7.setOnClickListener {
            val intent = Intent (this, QuizActivity::class.java)
            intent.putExtra("nivel", 7) //abrir nivel 7
            startActivity(intent)
        }

        btnMision8.setOnClickListener {
            val intent = Intent (this, QuizActivity::class.java)
            intent.putExtra("nivel", 8) //abrir nivel 8
            startActivity(intent)
        }

        btnMision9.setOnClickListener {
            val intent = Intent (this, QuizActivity::class.java)
            intent.putExtra("nivel", 9) //abrir nivel 9
            startActivity(intent)
        }

        btnMision10.setOnClickListener {
            val intent = Intent (this, QuizActivity::class.java)
            intent.putExtra("nivel", 10) //abrir nivel 10
            startActivity(intent)
        }
    }
}
