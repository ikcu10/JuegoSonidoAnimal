package com.example.juego_animales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import android.content.Intent


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
        val btnMision1 = findViewById<ImageView>(R.id.btnMision1)

        btnMision1.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }
    }
}