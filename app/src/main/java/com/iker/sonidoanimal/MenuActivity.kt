package com.iker.sonidoanimal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu) // tu XML del menú

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
