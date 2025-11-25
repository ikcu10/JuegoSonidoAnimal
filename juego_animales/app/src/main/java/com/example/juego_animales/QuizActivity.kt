package com.example.juego_animales

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton

class QuizActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var respuestaCorrecta: String = ""
    private var nivelActual: Int = 1
    private var fallos: Int = 0
    private val totalPreguntas = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val botonSonido: ImageView = findViewById(R.id.boton_sonido)
        val boton1: ImageButton = findViewById(R.id.boton1)
        val boton2: ImageButton = findViewById(R.id.boton2)
        val boton3: ImageButton = findViewById(R.id.boton3)
        val boton4: ImageButton = findViewById(R.id.boton4)

        // Recoger nivel y fallos que vienen del Intent
        nivelActual = intent.getIntExtra("nivel", 1)
        fallos = intent.getIntExtra("fallos", 0)

        // Configuración dinámica según nivel
        when (nivelActual) {
            1 -> {
                botonSonido.tag = R.raw.perro
                boton1.setImageResource(R.drawable.animal_perro)
                boton2.setImageResource(R.drawable.animal_gato)
                boton3.setImageResource(R.drawable.animal_pato)
                boton4.setImageResource(R.drawable.animal_rana)
                respuestaCorrecta = "perro"

                boton1.setOnClickListener { comprobarRespuesta("perro") }
                boton2.setOnClickListener { comprobarRespuesta("gato") }
                boton3.setOnClickListener { comprobarRespuesta("pato") }
                boton4.setOnClickListener { comprobarRespuesta("rana") }
            }
            2 -> {
                botonSonido.tag = R.raw.gato
                boton1.setImageResource(R.drawable.conejo)
                boton2.setImageResource(R.drawable.animal_gato)
                boton3.setImageResource(R.drawable.jirafa)
                boton4.setImageResource(R.drawable.loro)
                respuestaCorrecta = "gato"

                boton1.setOnClickListener { comprobarRespuesta("conejo") }
                boton2.setOnClickListener { comprobarRespuesta("gato") }
                boton3.setOnClickListener { comprobarRespuesta("jirafa") }
                boton4.setOnClickListener { comprobarRespuesta("loro") }
            }
            3 -> {
                botonSonido.tag = R.raw.pato
                boton1.setImageResource(R.drawable.animal_perro)
                boton2.setImageResource(R.drawable.animal_gato)
                boton3.setImageResource(R.drawable.animal_pato)
                boton4.setImageResource(R.drawable.animal_rana)
                respuestaCorrecta = "pato"

                boton1.setOnClickListener { comprobarRespuesta("perro") }
                boton2.setOnClickListener { comprobarRespuesta("gato") }
                boton3.setOnClickListener { comprobarRespuesta("pato") }
                boton4.setOnClickListener { comprobarRespuesta("rana") }
            }
            4 -> {
                botonSonido.tag = R.raw.pinguino
                boton1.setImageResource(R.drawable.animal_rana)
                boton2.setImageResource(R.drawable.pinguino)
                boton3.setImageResource(R.drawable.oso)
                boton4.setImageResource(R.drawable.loro)
                respuestaCorrecta = "pinguino"

                boton1.setOnClickListener { comprobarRespuesta("rana") }
                boton2.setOnClickListener { comprobarRespuesta("pinguino") }
                boton3.setOnClickListener { comprobarRespuesta("oso") }
                boton4.setOnClickListener { comprobarRespuesta("loro") }
            }
            5 -> {
                botonSonido.tag = R.raw.ardilla
                boton1.setImageResource(R.drawable.leon)
                boton2.setImageResource(R.drawable.mono)
                boton3.setImageResource(R.drawable.ardilla)
                boton4.setImageResource(R.drawable.animal_perro)
                respuestaCorrecta = "ardilla"

                boton1.setOnClickListener { comprobarRespuesta("leon") }
                boton2.setOnClickListener { comprobarRespuesta("mono") }
                boton3.setOnClickListener { comprobarRespuesta("ardilla") }
                boton4.setOnClickListener { comprobarRespuesta("perro") }
            }
            6 -> {
                botonSonido.tag = R.raw.delfin
                boton1.setImageResource(R.drawable.delfin)
                boton2.setImageResource(R.drawable.pajaro_azul)
                boton3.setImageResource(R.drawable.buho)
                boton4.setImageResource(R.drawable.colibri)
                respuestaCorrecta = "delfin"

                boton1.setOnClickListener { comprobarRespuesta("delfin") }
                boton2.setOnClickListener { comprobarRespuesta("pajaro_azul") }
                boton3.setOnClickListener { comprobarRespuesta("buho") }
                boton4.setOnClickListener { comprobarRespuesta("colibri") }
            }
            7 -> {
                botonSonido.tag = R.raw.leon
                boton1.setImageResource(R.drawable.leon)
                boton2.setImageResource(R.drawable.oso)
                boton3.setImageResource(R.drawable.rata)
                boton4.setImageResource(R.drawable.pinguino)
                respuestaCorrecta = "leon"

                boton1.setOnClickListener { comprobarRespuesta("leon") }
                boton2.setOnClickListener { comprobarRespuesta("oso") }
                boton3.setOnClickListener { comprobarRespuesta("rata") }
                boton4.setOnClickListener { comprobarRespuesta("pinguino") }
            }
            8 -> {
                botonSonido.tag = R.raw.zorro
                boton1.setImageResource(R.drawable.animal_rana)
                boton2.setImageResource(R.drawable.delfin)
                boton3.setImageResource(R.drawable.loro)
                boton4.setImageResource(R.drawable.zorro)
                respuestaCorrecta = "zorro"

                boton1.setOnClickListener { comprobarRespuesta("animal_rana") }
                boton2.setOnClickListener { comprobarRespuesta("delfin") }
                boton3.setOnClickListener { comprobarRespuesta("loro") }
                boton4.setOnClickListener { comprobarRespuesta("zorro") }
            }
            9 -> {
                botonSonido.tag = R.raw.mono
                boton1.setImageResource(R.drawable.mono)
                boton2.setImageResource(R.drawable.animal_pato)
                boton3.setImageResource(R.drawable.buho)
                boton4.setImageResource(R.drawable.loro)
                respuestaCorrecta = "mono"

                boton1.setOnClickListener { comprobarRespuesta("mono") }
                boton2.setOnClickListener { comprobarRespuesta("animal_pato") }
                boton3.setOnClickListener { comprobarRespuesta("buho") }
                boton4.setOnClickListener { comprobarRespuesta("loro") }
            }
            10 -> {
                botonSonido.tag = R.raw.pajaro
                boton1.setImageResource(R.drawable.animal_gato)
                boton2.setImageResource(R.drawable.pajaro_azul)
                boton3.setImageResource(R.drawable.ardilla)
                boton4.setImageResource(R.drawable.jirafa)
                respuestaCorrecta = "pajaro_azul"

                boton1.setOnClickListener { comprobarRespuesta("animal_gato") }
                boton2.setOnClickListener { comprobarRespuesta("pajaro_azul") }
                boton3.setOnClickListener { comprobarRespuesta("ardilla") }
                boton4.setOnClickListener { comprobarRespuesta("jirafa") }
            }
        }

        // Altavoz → reproducir sonido
        botonSonido.setOnClickListener {
            mediaPlayer?.release()
            val sonidoId = botonSonido.tag as Int
            mediaPlayer = MediaPlayer.create(this, sonidoId)
            mediaPlayer?.start()

            // Detener el sonido después de 5 segundos
            Handler(Looper.getMainLooper()).postDelayed({
                                                            mediaPlayer?.stop()
                                                            mediaPlayer?.release()
                                                            mediaPlayer = null
                                                        }, 5000)
        }
    }

    private fun comprobarRespuesta(respuesta: String) {
        val esCorrecta = (respuesta == respuestaCorrecta)

        if (esCorrecta) {
            // Si es el último nivel, mostrar pantalla de resultados
            if (nivelActual >= totalPreguntas) {
                val resultIntent = Intent(this, ResultActivity::class.java)
                resultIntent.putExtra("fallos", fallos)
                resultIntent.putExtra("total", totalPreguntas)
                startActivity(resultIntent)
                finish()
            } else {
                val intent = Intent(this, CorrectActivity::class.java)
                intent.putExtra("nivel", nivelActual)
                intent.putExtra("fallos", fallos)
                intent.putExtra("total", totalPreguntas)
                startActivity(intent)
            }
        } else {
            val intent = Intent(this, IncorrectActivity::class.java)
            intent.putExtra("nivel", nivelActual)
            intent.putExtra("fallos", fallos)
            intent.putExtra("total", totalPreguntas)
            startActivity(intent)
        }
    }
}
