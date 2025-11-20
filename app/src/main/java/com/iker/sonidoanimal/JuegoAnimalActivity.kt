package com.iker.sonidoanimal

import android.media.MediaPlayer
import androidx.cardview.widget.CardView
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class JuegoAnimalesActivity : AppCompatActivity() {

    //private lateinit var recyclerView: RecyclerView
    private lateinit var contenedorAnimales: LinearLayout
    private lateinit var tvQuestion: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSound: ImageView
    //private lateinit var adapter: AnimalAdapter

    private var currentLevel = 1
    private var isInteractionEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sonido_animal)

        setupViews()
        setupLevel(currentLevel)
    }

    private fun setupViews() {
        //recyclerView = findViewById(R.id.recyclerViewAnimals)
        contenedorAnimales = findViewById(R.id.contenedorAnimales)
        tvQuestion = findViewById(R.id.texto_pregunta)
        progressBar = findViewById(R.id.barra_progreso)
        btnSound = findViewById(R.id.boton_sonido)

        // recyclerView.layoutManager =
        //    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        btnSound.setOnClickListener {
            val sonidoNivel = getSoundForLevel(currentLevel)
            playAnimalSound(sonidoNivel)
        }
    }

    private fun setupLevel(level: Int) {
        SoundManager.stop()
        val animals = getAnimalsForLevel(level)

        isInteractionEnabled = true
        // Limpiar el contenedor
        contenedorAnimales.removeAllViews()

        val anchoPantalla = resources.displayMetrics.widthPixels
        val sizeCard = when (animals.size) {
            2 -> anchoPantalla / 4
            3 -> anchoPantalla / 4
            4 -> anchoPantalla / 4
            else -> anchoPantalla / 3
        }

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

            cardView.setOnClickListener {
                if (isInteractionEnabled) {
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
                Animal(1, R.drawable.animal_vaca, "León", R.raw.sonido_vaca, true),
                Animal(2, R.drawable.animal_conejo, "Elefante", R.raw.sonido_conejo)
                       )

            3 -> listOf(
                Animal(1, R.drawable.animal_rana, "Pájaro", R.raw.sonido_rana,true),
                Animal(2, R.drawable.animal_raton, "Oveja", R.raw.sonido_raton),
                Animal(3, R.drawable.animal_loro, "Cerdo", R.raw.sonido_loro)
                       )

            4 -> listOf(
                Animal(1, R.drawable.animal_cerdo, "Vaca", R.raw.sonido_cerdo, true),
                Animal(2, R.drawable.animal_koala, "Caballo", R.raw.sonido_koala),
                Animal(3, R.drawable.animal_ardilla, "Gallina", R.raw.sonido_ardilla)
                       )

            5 -> listOf(
                Animal(1, R.drawable.animal_pato, "Pez", R.raw.sonido_pato, true),
                Animal(2, R.drawable.animal_oveja, "Tigre", R.raw.sonido_oveja),
                Animal(3, R.drawable.animal_aguila, "Oso", R.raw.sonido_aguila),
                Animal(4, R.drawable.animal_tortuga, "Mono", R.raw.sonido_tortuga)
                       )

            6 -> listOf(
                Animal(1, R.drawable.animal_elefante, "Serpiente", R.raw.soido_elefante, true),
                Animal(2, R.drawable.animal_cabra, "Ardilla", R.raw.sonido_cabra),
                Animal(3, R.drawable.animal_vaca, "Zorro", R.raw.sonido_vaca),
                Animal(4, R.drawable.animal_hamster, "Lobo", R.raw.sonido_hamster)
                       )

            7 -> listOf(
                Animal(1, R.drawable.animal_zorro, "Jirafa", R.raw.sonido_zorro, true),
                Animal(2, R.drawable.animal_girafa, "Cebra", R.raw.sonido_jirafa),
                Animal(3, R.drawable.animal_pinguino, "Hipopótamo", R.raw.sonido_pinguino),
                Animal(4, R.drawable.animal_rinoceronte, "Cocodrilo", R.raw.sonido_rinoceronte)
                       )

            8 -> listOf(
                Animal(1, R.drawable.animal_cocodrilo, "Pingüino", R.raw.sonido_cocodrilo, true),
                Animal(2, R.drawable.animal_oso_panda, "Foca", R.raw.sonido_oso_panda),
                Animal(3, R.drawable.animal_koala, "Ballena", R.raw.sonido_koala),
                Animal(4, R.drawable.animal_oso, "Delfín", R.raw.sonido_oso)
                       )

            9 -> listOf(
                Animal(1, R.drawable.animal_perro, "Águila", R.raw.sonido_perro),
                Animal(2, R.drawable.animal_lobo, "Búho", R.raw.sonido_lobo, true)
                       )

            10 -> listOf(
                Animal(1, R.drawable.animal_leon, "Mariposa", R.raw.sonido_leon, true),
                Animal(2, R.drawable.animal_tigre, "Abeja", R.raw.sonido_tigre)
                        )

            else -> emptyList()
        }
    }

    private fun handleAnimalSelection(selectedAnimal: Animal, allAnimals: List<Animal>, cardView: CardView) {
        if (!isInteractionEnabled) return

        isInteractionEnabled = false
        selectedAnimal.isSelected = true

        val context = this

        if (selectedAnimal.isCorrect) {
            // ACIERTO: poner verde
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.correct_color))
            cardView.isEnabled = false

            // Reproducir sonido animal + voz "Muy bien hecho"
            playAnimalSound(selectedAnimal.soundRes)
            playVoiceCorrect()

            // Pasar al siguiente nivel después de 2-3 segundos
            Handler(Looper.getMainLooper()).postDelayed({
                currentLevel++
                if (currentLevel <= 10) {
                    setupLevel(currentLevel)
                } else {
                    Toast.makeText(context, "¡Juego completado!", Toast.LENGTH_SHORT).show()
                }
            }, 2000)

        } else {
            // ERROR: poner rojo
            cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.wrong_color))
            cardView.isEnabled = false

            // Reproducir sonido animal (opcional)
            playAnimalSound(selectedAnimal.soundRes)
            playVoiceIncorrect()

            // Después de 2 segundos, poner gris para indicar que ya no se puede seleccionar
            Handler(Looper.getMainLooper()).postDelayed({
                cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.disabled_color))
                isInteractionEnabled = true
            }, 2000)
        }
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