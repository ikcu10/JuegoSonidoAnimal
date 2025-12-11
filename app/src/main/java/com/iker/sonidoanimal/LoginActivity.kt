package com.iker.sonidoanimal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts

class LoginActivity : AppCompatActivity() {


    private var avatarSeleccionado: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val botonJugar = findViewById<Button>(R.id.boton_comenzar)
        val botonMas = findViewById<android.widget.ImageButton>(R.id.boton_mas)
        val entradaNombre = findViewById<EditText>(R.id.entrada_nombre)

        val launcherAvatar = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {

                val animal = result.data?.getStringExtra("avatar")

                if (animal != null) {
                    avatarSeleccionado = animal


                    when (animal) {
                        "oso" -> botonMas.setImageResource(R.drawable.avatar_oso)
                        "oso_panda" -> botonMas.setImageResource(R.drawable.avatar_oso_panda)
                        "pinguino" -> botonMas.setImageResource(R.drawable.avatar_pinguino)
                        "zorro" -> botonMas.setImageResource(R.drawable.avatar_zorro)
                        "tigre" -> botonMas.setImageResource(R.drawable.avatar_tigre)
                        "leon" -> botonMas.setImageResource(R.drawable.avatar_leon)
                    }

                    botonMas.background = null
                    botonMas.setPadding(10, 10, 10, 10)
                    botonMas.scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
                }
            }
        }

        // 1. BOTÓN MAS
        botonMas.setOnClickListener {
            val intent = Intent(this, AvatarActivity::class.java)
            launcherAvatar.launch(intent)
        }

        // 2. BOTÓN JUGAR
        botonJugar.setOnClickListener {
            val nombreUsuario = entradaNombre.text.toString().trim()

            // Validación

            if (nombreUsuario.isEmpty()) {
                Toast.makeText(this, "Debes introducir tu nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val regexSoloLetras = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$".toRegex()
            if (!regexSoloLetras.matches(nombreUsuario)) {
                Toast.makeText(this, "El nombre debe contener solo letras", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (avatarSeleccionado == null) {
                Toast.makeText(this, "¡Pulsa el + para elegir tu avatar!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // GUARDADO EN MEMORIA
            val prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE)
            val editor = prefs.edit()

            editor.putString("avatarGuardado", avatarSeleccionado)
            editor.putString("nombreNino", nombreUsuario)

            editor.putInt("nivelDesbloqueado", 1)
            editor.putInt("puntosTotales", 0)
            editor.putInt("numeroPartida", 1)
            editor.apply()

            GestorDatos.iniciarNuevaSesion(nombreUsuario)

            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}
