package com.iker.sonidoanimal

import android.content.Intent
import android.media.MediaPlayer
import androidx.cardview.widget.CardView
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class JuegoAnimalesActivity : AppCompatActivity() {


    private lateinit var contenedorAnimales: LinearLayout
    private lateinit var tvQuestion: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSound: ImageView
    private lateinit var tvScore: TextView

    private var currentLevel = 1
    private var isInteractionEnabled = true

    // Variables nuevas para estadísticas
    private var startTime: Long = 0
    private var nombreNino: String = "Jugador" // Valor por defecto

    private var numeroPartida = 1
    private var puntuacionAcumuladaPrevia = 0
    private var puntosEsteNivel = 100

    private var erroresNivel = 0

    private var clicksAyuda = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sonido_animal)

        currentLevel = intent.getIntExtra("nivel", 1)
        setupViews()
        setupLevel(currentLevel)
    }

    private fun setupViews() {
        //recyclerView = findViewById(R.id.recyclerViewAnimals)
        contenedorAnimales = findViewById(R.id.contenedorAnimales)
        tvQuestion = findViewById(R.id.texto_pregunta)
        progressBar = findViewById(R.id.barra_progreso)
        btnSound = findViewById(R.id.boton_sonido)

        tvScore = findViewById(R.id.texto_puntuacion)

        val prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE)
        nombreNino = prefs.getString("nombreNino", "Jugador") ?: "Jugador"

        numeroPartida = prefs.getInt("numeroPartida", 1)
        // Si es el Nivel 1, reseteamos la cuenta a 0.
        // Si es otro nivel, leemos cuántos puntos llevamos acumulados.
        if (currentLevel == 1) {
            puntuacionAcumuladaPrevia = 0
            prefs.edit().putInt("puntosTotales", 0).apply()
        } else {
            puntuacionAcumuladaPrevia = prefs.getInt("puntosTotales", 0)
        }

        btnSound.setOnClickListener {
            clicksAyuda++
            val sonidoNivel = getSoundForLevel(currentLevel)
            playAnimalSound(sonidoNivel)
        }
    }

    private fun setupLevel(level: Int) {
        SoundManager.stop()
        startTime = System.currentTimeMillis()

        clicksAyuda = 0

        // --- RECUPERAR ERRORES (SI VIENE DE REINTENTAR) ---
        erroresNivel = intent.getIntExtra("erroresPrevios", 0)

        // Calculamos cuánto vale el premio ahora mismo
        if (erroresNivel == 0) {
            puntosEsteNivel = 100
        } else if (erroresNivel == 1) {
            puntosEsteNivel = 50
        } else if (erroresNivel == 2) {
            puntosEsteNivel = 25
        } else {
            puntosEsteNivel = 0
        }

        // --- VISUALIZACIÓN ---
        // Mostramos SOLO lo que tiene seguro. No sumamos el premio todavía.
        tvScore.text = "Puntos: $puntuacionAcumuladaPrevia"

        isInteractionEnabled = true

        val animals = getAnimalsForLevel(level)
        contenedorAnimales.removeAllViews()

        val anchoPantalla = resources.displayMetrics.widthPixels
        val sizeCard = when (animals.size) {
            2 -> anchoPantalla / 4
            3 -> anchoPantalla / 4
            4 -> anchoPantalla / 4
            else -> anchoPantalla / 3
        }

        val esReintento = intent.getBooleanExtra("reintentar", false)
        val listaFallos = intent.getIntegerArrayListExtra("listaFallos") ?: arrayListOf()

        for (animal in animals) {
            val card = layoutInflater.inflate(R.layout.item_animal_card, contenedorAnimales, false)
            val cardView = card.findViewById<CardView>(R.id.cardAnimal)
            val img = card.findViewById<ImageView>(R.id.ivAnimal)

            img.setImageResource(animal.imageRes)

            val params = LinearLayout.LayoutParams(sizeCard, sizeCard)
            params.weight = 1f
            params.marginStart = 16
            params.marginEnd = 16
            cardView.layoutParams = params

            if (esReintento && listaFallos.contains(animal.id)) {
                cardView.setCardBackgroundColor(ContextCompat.getColor(this, R.color.disabled_color))
                cardView.isEnabled = false
            }

            cardView.setOnClickListener {
                if (isInteractionEnabled) {
                    isInteractionEnabled = false
                    handleAnimalSelection(animal, animals, cardView)
                }
            }

            contenedorAnimales.addView(card)
        }

        updateProgress(level)
    }

    private fun getAnimalsForLevel(level: Int): List<Animal> {
        return when (level) {
            1 -> listOf(
                Animal(1, R.drawable.animal_perro, "Perro", R.raw.sonido_perro, true),
                Animal(2, R.drawable.animal_gato, "Gato", R.raw.sonido_gato)
                       )

            2 -> listOf(
                Animal(1, R.drawable.animal_vaca, "Vaca", R.raw.sonido_vaca, true),
                Animal(2, R.drawable.animal_conejo, "Conejo", R.raw.sonido_conejo)
                       )

            3 -> listOf(
                Animal(1, R.drawable.animal_rana, "Rana", R.raw.sonido_rana,true),
                Animal(2, R.drawable.animal_raton, "Raton", R.raw.sonido_raton),
                Animal(3, R.drawable.animal_loro, "Loro", R.raw.sonido_loro)
                       )

            4 -> listOf(
                Animal(1, R.drawable.animal_cerdo, "Cerdo", R.raw.sonido_cerdo, true),
                Animal(2, R.drawable.animal_koala, "Koala", R.raw.sonido_koala),
                Animal(3, R.drawable.animal_ardilla, "Ardilla", R.raw.sonido_ardilla)
                       )

            5 -> listOf(
                Animal(1, R.drawable.animal_pato, "Pata", R.raw.sonido_pato, true),
                Animal(2, R.drawable.animal_oveja, "Oveja", R.raw.sonido_oveja),
                Animal(3, R.drawable.animal_aguila, "Aguila", R.raw.sonido_aguila),
                Animal(4, R.drawable.animal_tortuga, "Tortuga", R.raw.sonido_tortuga)
                       )

            6 -> listOf(
                Animal(1, R.drawable.animal_elefante, "Elefante", R.raw.soido_elefante, true),
                Animal(2, R.drawable.animal_cabra, "Cabra", R.raw.sonido_cabra),
                Animal(3, R.drawable.animal_vaca, "Vaca", R.raw.sonido_vaca),
                Animal(4, R.drawable.animal_hamster, "Hamster", R.raw.sonido_hamster)
                       )

            7 -> listOf(
                Animal(1, R.drawable.animal_zorro, "Zorro", R.raw.sonido_zorro, true),
                Animal(2, R.drawable.animal_jirafa, "Girafa", R.raw.sonido_jirafa),
                Animal(3, R.drawable.animal_pinguino, "Pinguino", R.raw.sonido_pinguino),
                Animal(4, R.drawable.animal_rinoceronte, "Rinoceronte", R.raw.sonido_rinoceronte)
                       )

            8 -> listOf(
                Animal(1, R.drawable.animal_cocodrilo, "Cocodrilo", R.raw.sonido_cocodrilo, true),
                Animal(2, R.drawable.animal_oso_panda, "Panda", R.raw.sonido_oso_panda),
                Animal(3, R.drawable.animal_koala, "Koala", R.raw.sonido_koala),
                Animal(4, R.drawable.animal_oso, "Oso", R.raw.sonido_oso)
                       )

            9 -> listOf(
                Animal(1, R.drawable.animal_perro, "Perro", R.raw.sonido_perro),
                Animal(2, R.drawable.animal_lobo, "Lobo", R.raw.sonido_lobo, true)
                       )

            10 -> listOf(
                Animal(1, R.drawable.animal_leon, "Leon", R.raw.sonido_leon, true),
                Animal(2, R.drawable.animal_tigre, "Tigre", R.raw.sonido_tigre)
                        )

            else -> emptyList()
        }
    }

    private fun handleAnimalSelection(selectedAnimal: Animal, allAnimals: List<Animal>, cardView: CardView) {
        val context = this
        if (selectedAnimal.isCorrect) {
            // --- ACIERTO ---
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.correct_color))
            cardView.isEnabled = false
            playAnimalSound(selectedAnimal.soundRes)
            playVoiceCorrect()

            val puntuacionFinal = puntuacionAcumuladaPrevia + puntosEsteNivel

            tvScore.text = "Puntos: $puntuacionFinal"
            // Como ha ganado, guardamos el total en memoria para el siguiente nivel
            val prefs = getSharedPreferences("MisPreferencias", MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putInt("puntosTotales", puntuacionFinal)
            editor.apply()
            // ---------------------------------------

            procesarFinDeNivel()

            Handler(Looper.getMainLooper()).postDelayed({
                                                            // --- CRUCE DE CAMINOS (FIN DEL JUEGO) ---
                                                            if (currentLevel == 10) {
                                                                // Si es el nivel 10, vamos a la pantalla final
                                                                val intent = Intent(this@JuegoAnimalesActivity, ActivityPuntos::class.java)
                                                                startActivity(intent)
                                                                finish()
                                                            } else {
                                                                // Si no, seguimos jugando normal
                                                                val intent = Intent(this@JuegoAnimalesActivity, ResultadoCorrectoActivity::class.java)
                                                                intent.putExtra("nivel", currentLevel)
                                                                startActivity(intent)
                                                                finish()
                                                            }
                                                            // ----------------------------------------
                                                        }, 2000)

        } else {
            // --- ERROR (FALLO) ---
            erroresNivel++

            // REGLA: 100 -> 50 -> 25 -> 0
            if (erroresNivel == 1) {
                puntosEsteNivel = 50
            } else if (erroresNivel == 2) {
                puntosEsteNivel = 25
            } else {
                puntosEsteNivel = 0
            }

            // NOTA: NO cambiamos tvScore. Sigue viendo sus puntos seguros.
            // Simplemente sabe que cuando gane, ganará menos.

            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.wrong_color))
            cardView.isEnabled = false
            playAnimalSound(selectedAnimal.soundRes)
            playVoiceIncorrect()

            val listaFallos = intent.getIntegerArrayListExtra("listaFallos") ?: arrayListOf()
            listaFallos.add(selectedAnimal.id)


            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@JuegoAnimalesActivity, ResultadoIncorrectoActivity::class.java)
                intent.putExtra("nivel", currentLevel)
                intent.putIntegerArrayListExtra("listaFallos", listaFallos)

                // PASAMOS EL CONTADOR DE ERRORES AL REINTENTO
                intent.putExtra("erroresPrevios", erroresNivel)

                startActivity(intent)
                finish()
            }, 2000)
        }
    }

    private fun procesarFinDeNivel() {
        // 1. Calculamos cuánto tardó (en segundos)
        val tiempoNivel = (System.currentTimeMillis() - startTime) / 1000

        // 2. Enviamos todos los datos al Gestor para que actualice el JSON
        GestorDatos.registrarNivel(
            context = this,
            nivel = currentLevel,
            duracionSegundos = tiempoNivel,
            errores = erroresNivel,
            puntos = puntosEsteNivel,
            clicksAyuda = clicksAyuda // Pasamos el contador de clicks
        )
    }

    private fun updateScoreDisplay() {
        tvScore.text = "Puntos: $puntuacionAcumuladaPrevia"
    }

    private fun updateProgress(level: Int) {
        progressBar.progress = (level * 10)
        tvQuestion.text = "Nivel $level"
    }

    private fun playAnimalSound(soundRes: Int) {
        SoundManager.play(this, soundRes)
    }

    private fun playVoiceCorrect() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.sonido_correcto) // tu audio "Muy bien hecho"
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { it.release() }
    }

    private fun playVoiceIncorrect(){
        val mediaPlayer = MediaPlayer.create(this, R.raw.sonido_incorrecto)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { it.release() }
    }

    private fun getSoundForLevel(level: Int): Int {
        return when (level) {
            1 -> R.raw.sonido_perro
            2 -> R.raw.sonido_vaca
            3 -> R.raw.sonido_rana
            4 -> R.raw.sonido_cerdo
            5 -> R.raw.sonido_pato
            6 -> R.raw.soido_elefante
            7 -> R.raw.sonido_zorro
            8 -> R.raw.sonido_cocodrilo
            9 -> R.raw.sonido_lobo
            10 -> R.raw.sonido_leon
            else -> R.raw.sonido_perro
        }
    }

    override fun onPause() {
        super.onPause()
        SoundManager.stop()
    }

}