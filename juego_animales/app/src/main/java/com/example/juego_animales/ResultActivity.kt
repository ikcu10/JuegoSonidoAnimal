package com.example.juego_animales

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Recuperar los fallos acumulados
        val fallos = intent.getIntExtra("fallos", 0)
        val total = intent.getIntExtra("total", 10)

        val texto = findViewById<TextView>(R.id.texto_resultado)
        texto.text = "Has cometido $fallos fallos en $total niveles"

        // Botón para volver a jugar desde el nivel 1
        findViewById<View>(R.id.boton_reintentar).setOnClickListener {
            val intentQuiz = Intent(this, QuizActivity::class.java)
            intentQuiz.putExtra("nivel", 1)
            intentQuiz.putExtra("fallos", 0) // reiniciamos contador
            startActivity(intentQuiz)
            finish()
        }

        // Botón para volver al menú principal
        findViewById<View>(R.id.boton_menu).setOnClickListener {
            val intentMenu = Intent(this, NivelesActivity::class.java)
            startActivity(intentMenu)
            finish()
        }
    }
}
