package com.iker.sonidoanimal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class ResultadoIncorrectoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado_incorrecto)

        val nivelActual = intent.getIntExtra("nivel", 1)

        val btnReintentar = findViewById<Button>(R.id.btnReintar)
        val btnMenu = findViewById<Button>(R.id.btnMenu)

        btnReintentar.setOnClickListener {
            val intent = Intent(this, JuegoAnimalesActivity::class.java)
            intent.putExtra("nivel", nivelActual)
            intent.putExtra("reintento", true) // Para que se marque el que fallaste
            startActivity(intent)
            finish()
        }

        btnMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}