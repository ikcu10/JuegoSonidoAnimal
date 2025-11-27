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

        // --- INICIO CÓDIGO NUEVO ---
        // 1. Buscamos el hueco de la imagen que creamos en el XML
        val imgAvatar = findViewById<ImageView>(R.id.imgAvatar)

        // 2. Recogemos el texto que nos mandó la otra pantalla (ej: "oso", "zorro")
        val prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE)
        val avatarElegido = prefs.getString("avatarGuardado", null)

        // 3. Dependiendo del texto, ponemos una imagen u otra
        when (avatarElegido) {
            "oso" -> imgAvatar.setImageResource(R.drawable.animal_oso)
            "rata" -> imgAvatar.setImageResource(R.drawable.animal_raton)
            "pinguino" -> imgAvatar.setImageResource(R.drawable.animal_pinguino)
            "zorro" -> imgAvatar.setImageResource(R.drawable.animal_zorro)
            "conejo" -> imgAvatar.setImageResource(R.drawable.animal_conejo)
            "jirafa" -> imgAvatar.setImageResource(R.drawable.animal_girafa)
            // Si no llega nada (o hay error), ponemos una imagen por defecto
            else -> imgAvatar.setImageResource(R.drawable.ic_launcher_foreground)
        }
        setupButtons()
    }

    private fun setupButtons() {
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
            button.setOnClickListener {
                val intent = Intent(this, JuegoAnimalesActivity::class.java)
                intent.putExtra("nivel", index + 1) // niveles 1 a 10
                startActivity(intent)
            }
        }
    }
}
