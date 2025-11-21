package com.example.juego_animales

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.content.Intent
import android.os.Handler
import android.os.Looper

class QuizActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        val botonSonido: ImageView = findViewById(R.id.boton_sonido)
        val botonPerro: ImageView = findViewById(R.id.boton_perro)
        val botonGato: ImageView = findViewById(R.id.boton_gato)
        val botonPato: ImageView = findViewById(R.id.boton_pato)
        val botonRana: ImageView = findViewById(R.id.boton_rana)



        // Guardamos el audio que queremos en una "tag" del ImageView
        botonSonido.tag = R.raw.perro

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
                mediaPlayer = null}, 5000) // 5000 milisegundos = 5 segundos

        }

        // Botón perro → respuesta correcta
        botonPerro.setOnClickListener {
            val intent = Intent(this, CorrectActivity::class.java)
            startActivity(intent)
        }

        // Botón gato → respuesta incorrecta
        botonGato.setOnClickListener {
            val intent = Intent(this, IncorrectActivity::class.java)
            startActivity(intent)
        }

        // Botón pato → respuesta incorrecta
        botonPato.setOnClickListener {
            val intent = Intent(this, IncorrectActivity::class.java)
            startActivity(intent)
        }

        // Botón rana → respuesta incorrecta
        botonRana.setOnClickListener {
            val intent = Intent(this, IncorrectActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
