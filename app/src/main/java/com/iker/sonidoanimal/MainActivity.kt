package com.iker.sonidoanimal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Esto carga tu XML

        // Botón para abrir la segunda pantalla
        val botonComenzar = findViewById<Button>(R.id.boton_comenzar)
        botonComenzar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
//ppp