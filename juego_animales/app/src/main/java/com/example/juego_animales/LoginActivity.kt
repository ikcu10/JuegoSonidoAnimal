package com.example.juego_animales

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val botonJugar = findViewById<Button>(R.id.boton_comenzar_login)
        val entradaNombre = findViewById<EditText>(R.id.entrada_nombre)

        botonJugar.setOnClickListener {
            val nombreUsuario = entradaNombre.text.toString().trim()

            // Validar que no esté vacío
            if (nombreUsuario.isEmpty()) {
                Toast.makeText(this, "Debes introducir tu nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validar que solo tenga letras (sin números)
            val regexSoloLetras = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$".toRegex()
            if (!regexSoloLetras.matches(nombreUsuario)) {
                Toast.makeText(this, "El nombre debe contener solo letras", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Si pasa las validaciones → abrir AvatarActivity
            val intent = Intent(this, AvatarActivity::class.java)
            intent.putExtra("nombre", nombreUsuario)
            startActivity(intent)
        }
    }
}
