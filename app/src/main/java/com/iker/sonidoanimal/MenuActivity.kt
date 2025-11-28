package com.iker.sonidoanimal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        // La configuración la hacemos en onResume para que se actualice al volver de jugar
    }

    // Usamos onResume para refrescar los candados cada vez que la pantalla aparece
    override fun onResume() {
        super.onResume()
        setupMenu()
    }

    private fun setupMenu() {
        val imgAvatar = findViewById<ImageView>(R.id.imgAvatar)
        val prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE)

        // 1. RECUPERAMOS EL PROGRESO (Por defecto nivel 1 desbloqueado)
        val maxNivelDesbloqueado = prefs.getInt("nivelDesbloqueado", 1)

        // Cargar Avatar (Tu código de siempre)
        val avatarElegido = prefs.getString("avatarGuardado", null)
        when (avatarElegido) {
            "oso" -> imgAvatar.setImageResource(R.drawable.animal_oso)
            "rata" -> imgAvatar.setImageResource(R.drawable.animal_raton)
            "pinguino" -> imgAvatar.setImageResource(R.drawable.animal_pinguino)
            "zorro" -> imgAvatar.setImageResource(R.drawable.animal_zorro)
            "conejo" -> imgAvatar.setImageResource(R.drawable.animal_conejo)
            "jirafa" -> imgAvatar.setImageResource(R.drawable.animal_girafa)
            else -> imgAvatar.setImageResource(R.drawable.ic_launcher_foreground)
        }

        setupButtons(maxNivelDesbloqueado)
    }

    private fun setupButtons(maxNivel: Int) {
        val botones = arrayOf(
            findViewById<Button>(R.id.btn1),
            findViewById<Button>(R.id.btn2),
            findViewById<Button>(R.id.btn3),
            findViewById<Button>(R.id.btn4),
            findViewById<Button>(R.id.btn5),
            findViewById<Button>(R.id.btn6),
            findViewById<Button>(R.id.btn7),
            findViewById<Button>(R.id.btn8),
            findViewById<Button>(R.id.btn9),
            findViewById<Button>(R.id.btn10)
        )

        botones.forEachIndexed { index, button ->
            val nivelBoton = index + 1 // El botón 0 es el Nivel 1

            if (nivelBoton <= maxNivel) {
                // --- NIVEL DESBLOQUEADO (NORMAL) ---
                button.isEnabled = true // Se puede pulsar
                button.text = nivelBoton.toString() // Muestra el número "1", "2"...
                button.setBackgroundResource(R.drawable.round_button) // Fondo blanco original
                button.alpha = 1.0f // Totalmente visible
            } else {
                // --- NIVEL BLOQUEADO (CANDADO) ---
                button.isEnabled = false // NO se puede pulsar
                button.text = "🔒" // Muestra el candado
                button.setBackgroundResource(R.drawable.round_button_locked) // Fondo gris
                button.alpha = 0.7f // Un poco transparente
            }

            button.setOnClickListener {
                val intent = Intent(this, JuegoAnimalesActivity::class.java)
                intent.putExtra("nivel", nivelBoton)
                startActivity(intent)
            }
        }
    }
}
