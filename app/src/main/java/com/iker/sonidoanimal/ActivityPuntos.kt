package com.iker.sonidoanimal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActivityPuntos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puntos)

        // 1. Referencias a los elementos del XML
        val tvPuntos = findViewById<TextView>(R.id.tvPuntos)
        val btnReiniciar = findViewById<Button>(R.id.boton_reiniciar)

        // 2. Leer la memoria para mostrar los Puntos Totales conseguidos
        val prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE)
        val puntosTotales = prefs.getInt("puntosTotales", 0)

        // Actualizamos el texto gigante
        tvPuntos.text = "$puntosTotales Puntos"

        // 3. Configurar el botón de Reiniciar (Volver a Jugar)
        btnReiniciar.setOnClickListener {
            val editor = prefs.edit()

            // --- RESETEO DE DATOS ---
            editor.putInt("nivelDesbloqueado", 1) // Volvemos al nivel 1
            editor.putInt("puntosTotales", 0)     // Volvemos a 0 puntos

            // --- GESTIÓN DE SESIONES (JSON) ---
            // Leemos qué número de partida era esta (por defecto 1)
            val numeroPartidaActual = prefs.getInt("numeroPartida", 1)
            // Guardamos que la siguiente será la partida +1 (ej: Partida 2)
            editor.putInt("numeroPartida", numeroPartidaActual + 1)

            editor.apply() // Guardamos cambios
            // ----------------------------------

            // Volvemos al Menú (ahora aparecerá todo bloqueado menos el 1)
            val intent = Intent(this, MenuActivity::class.java)
            // Estas flags sirven para borrar el historial de pantallas anteriores
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}