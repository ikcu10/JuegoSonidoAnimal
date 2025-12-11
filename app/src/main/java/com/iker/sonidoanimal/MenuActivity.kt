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
    }

    override fun onResume() {
        super.onResume()
        setupMenu()
    }

    private fun setupMenu() {
        val imgAvatar = findViewById<ImageView>(R.id.imgAvatar)
        val prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE)

        val maxNivelDesbloqueado = prefs.getInt("nivelDesbloqueado", 1)

        val avatarElegido = prefs.getString("avatarGuardado", null)
        when (avatarElegido) {
            "oso" -> imgAvatar.setImageResource(R.drawable.avatar_oso)
            "oso_panda" -> imgAvatar.setImageResource(R.drawable.avatar_oso_panda)
            "pinguino" -> imgAvatar.setImageResource(R.drawable.avatar_pinguino)
            "zorro" -> imgAvatar.setImageResource(R.drawable.avatar_zorro)
            "tigre" -> imgAvatar.setImageResource(R.drawable.avatar_tigre)
            "leon" -> imgAvatar.setImageResource(R.drawable.avatar_leon)
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
            val nivelBoton = index + 1

            if (nivelBoton <= maxNivel) {
                // NIVEL DESBLOQUEADO
                button.isEnabled = true
                button.text = nivelBoton.toString()
                button.setBackgroundResource(R.drawable.round_button)
                button.alpha = 1.0f
            } else {
                // NIVEL BLOQUEADO
                button.isEnabled = false
                button.text = "🔒"
                button.textSize = 85f
                button.setBackgroundResource(R.drawable.round_button_locked)
                button.alpha = 0.7f
            }

            button.setOnClickListener {
                val intent = Intent(this, JuegoAnimalesActivity::class.java)
                intent.putExtra("nivel", nivelBoton)
                startActivity(intent)
            }
        }
    }
}
