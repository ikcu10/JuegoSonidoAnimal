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

        val erroresPrevios = intent.getIntExtra("erroresPrevios", 0)

        val btnReintentar = findViewById<Button>(R.id.btnReintar)

        btnReintentar.setOnClickListener {
            val listaFallos = intent.getIntegerArrayListExtra("listaFallos") ?: arrayListOf()

            val intent = Intent(this, JuegoAnimalesActivity::class.java)
            intent.putExtra("nivel", nivelActual)
            intent.putExtra("reintentar", true)
            intent.putIntegerArrayListExtra("listaFallos", listaFallos)

            intent.putExtra("erroresPrevios", erroresPrevios)

            startActivity(intent)
            finish()
        }
    }
}