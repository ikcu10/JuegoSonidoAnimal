package com.iker.sonidoanimal

import android.content.Intent
import android.os.Bundle
import android.util.Log // Nuevo: Para mostrar los datos en consola
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python // Nuevo: Para arrancar Chaquopy
import com.chaquo.python.android.AndroidPlatform // Nuevo: Para arrancar Chaquopy

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Esto carga tu XML

        // --- INICIO CÓDIGO EJERCICIO PANDAS ---
        // Esto se ejecuta "silenciosamente" al abrir la app.
        // No afecta a tu botón ni a la pantalla.
        try {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(this))
            }

            val py = Python.getInstance()
            // Asegúrate de que tu archivo se llama "script.py" en la carpeta python
            val modulo = py.getModule("script")

            // Llamamos a la función
            val resultado = modulo.callAttr("cargar_y_explorar")

            // Imprimimos el resultado en el Logcat
            Log.d("PANDAS_LOG", "\n" + resultado.toString())

        } catch (e: Exception) {
            Log.e("PANDAS_ERROR", "Hubo un fallo en Python: " + e.message)
        }
        // --- FIN CÓDIGO EJERCICIO PANDAS ---


        // TU CÓDIGO ORIGINAL DEL JUEGO (INTACTO)
        val botonComenzar = findViewById<Button>(R.id.boton_comenzar)
        botonComenzar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}