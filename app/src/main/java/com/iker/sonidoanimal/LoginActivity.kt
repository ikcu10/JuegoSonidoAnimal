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

    // Método
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Llamada a método (vincula el xml con este código)
        setContentView(R.layout.activity_login)
        // Declaración de Variables
        val botonJugar = findViewById<Button>(R.id.boton_comenzar)
        val botonMas = findViewById<android.widget.ImageButton>(R.id.boton_mas)
        val entradaNombre = findViewById<EditText>(R.id.entrada_nombre)

        // Esto se ejecuta cuando AvatarActivity termina y vuelve a LoginActivity, transformamos el botón redondo genérico en la cara del animal
        val launcherAvatar = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Recuperamos el nombre del animal (ej: "oso")
                val animal = result.data?.getStringExtra("avatar")

                if (animal != null) {
                    avatarSeleccionado = animal

                    // 1. ELIMINAR TEXTO: ImageButton no tiene propiedad .text, así que borramos esa línea.

                    // 2. CAMBIAR IMAGEN: Usamos setImageResource en vez de setBackgroundResource
                    when (animal) {
                        "oso" -> botonMas.setImageResource(R.drawable.avatar_oso)
                        "oso_panda" -> botonMas.setImageResource(R.drawable.avatar_oso_panda)
                        "pinguino" -> botonMas.setImageResource(R.drawable.avatar_pinguino)
                        "zorro" -> botonMas.setImageResource(R.drawable.avatar_zorro)
                        "tigre" -> botonMas.setImageResource(R.drawable.avatar_tigre)
                        "leon" -> botonMas.setImageResource(R.drawable.avatar_leon)
                    }

                    // BORRAMOS EL CÍRCULO DE FONDO
                    botonMas.background = null

                    // Padding entre la imágen del animal y el botón
                    botonMas.setPadding(10, 10, 10, 10)

                    // (Opcional) Aseguramos que se escale bien
                    botonMas.scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
                }
            }
        }

        // Llamada a método
        // 1. BOTÓN MAS (+): Abre la pantalla de seleccion, pero usando lo anterior para que espere la respuesta
        botonMas.setOnClickListener {
            val intent = Intent(this, AvatarActivity::class.java)
            launcherAvatar.launch(intent)
        }

        // Lamada a método
        // 2. BOTÓN JUGAR: Valida que todo está correcto (nobre y avatar), resetea el jeugo para que el nuevo jugador empiece limpio desde cero y te lleva a la siguiente pantalla
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

            editor.putInt("numeroPartida", 1)
            editor.apply() // Confirmamos el guardado

            GestorDatos.iniciarNuevaSesion(nombreUsuario)

            // SI TODO ESTÁ BIEN -> IR AL MENÚ
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}
