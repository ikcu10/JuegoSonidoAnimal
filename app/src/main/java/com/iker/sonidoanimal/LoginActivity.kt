package com.iker.sonidoanimal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts

class LoginActivity : AppCompatActivity() {

    // Variable para guardar qué animal eligió el usuario
    private var avatarSeleccionado: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val botonJugar = findViewById<Button>(R.id.boton_comenzar)
        val botonMas = findViewById<Button>(R.id.boton_mas)
        val entradaNombre = findViewById<EditText>(R.id.entrada_nombre)

        // Esto se ejecuta cuando vuelves de elegir avatar
        val launcherAvatar = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Recuperamos el nombre del animal (ej: "oso")
                val animal = result.data?.getStringExtra("avatar")

                if (animal != null) {
                    avatarSeleccionado = animal

                    // Quitamos el texto "+"
                    botonMas.text = ""

                    // Ponemos la imagen del animal en el botón
                    when (animal) {
                        "oso" -> botonMas.setBackgroundResource(R.drawable.avatar_oso)
                        "rata" -> botonMas.setBackgroundResource(R.drawable.avatar_oso_panda)
                        "pinguino" -> botonMas.setBackgroundResource(R.drawable.avatar_pinguino)
                        "zorro" -> botonMas.setBackgroundResource(R.drawable.avatar_pinguino)
                        "conejo" -> botonMas.setBackgroundResource(R.drawable.avatar_tigre)
                        "jirafa" -> botonMas.setBackgroundResource(R.drawable.avatar_leon)
                    }
                }
            }
        }

        // 1. BOTÓN MAS (+): Abre la selección de Avatar esperando respuesta
        botonMas.setOnClickListener {
            val intent = Intent(this, AvatarActivity::class.java)
            launcherAvatar.launch(intent)
        }

        // 2. BOTÓN JUGAR: Valida y nos lleva al MENÚ
        botonJugar.setOnClickListener {
            val nombreUsuario = entradaNombre.text.toString().trim()

            // Validación de Nombre vacío
            if (nombreUsuario.isEmpty()) {
                Toast.makeText(this, "Debes introducir tu nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación de Solo letras
            val regexSoloLetras = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+$".toRegex()
            if (!regexSoloLetras.matches(nombreUsuario)) {
                Toast.makeText(this, "El nombre debe contener solo letras", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validación: ¿Ha elegido avatar?
            if (avatarSeleccionado == null) {
                Toast.makeText(this, "¡Pulsa el + para elegir tu avatar!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // --- GUARDADO EN MEMORIA ---
            val prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE)
            val editor = prefs.edit()

            // 1. Guardamos el avatar
            editor.putString("avatarGuardado", avatarSeleccionado)
            // 2. Guardamos el nombre del niño (NUEVO)
            editor.putString("nombreNino", nombreUsuario)

            editor.putInt("nivelDesbloqueado", 1)
            editor.putInt("puntosTotales", 0)




            editor.apply() // Confirmamos el guardado

            // SI TODO ESTÁ BIEN -> IR AL MENÚ
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}
