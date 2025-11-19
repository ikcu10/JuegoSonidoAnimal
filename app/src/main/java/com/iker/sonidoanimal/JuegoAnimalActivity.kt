package com.iker.sonidoanimal

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class JuegoAnimalesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvQuestion: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSound: ImageView
    private lateinit var adapter: AnimalAdapter

    private var currentLevel = 1
    private var isInteractionEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sonido_animal)

        setupViews()
        setupLevel(currentLevel)
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerViewAnimals)
        tvQuestion = findViewById(R.id.texto_pregunta)
        progressBar = findViewById(R.id.barra_progreso)
        btnSound = findViewById(R.id.boton_sonido)

        // recyclerView.layoutManager =
        //    LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        btnSound.setOnClickListener {
            // Lógica sonido posterior
        }
    }

    private fun setupLevel(level: Int) {
        val animals = getAnimalsForLevel(level)

        // CONFIGURAR GRID - 4 CARDS EN 1 FILA
        recyclerView.layoutManager = GridLayoutManager(this,
                                                       when (animals.size) {
                                                           2 -> 2
                                                           3 -> 3
                                                           4 -> 4  // ← CAMBIO: 4 columnas en 1 fila
                                                           else -> 2
                                                       }
                                                      )

        isInteractionEnabled = true
        adapter = AnimalAdapter(animals) { selectedAnimal ->
            if (isInteractionEnabled) {
                handleAnimalSelection(selectedAnimal, animals)
            }
        }

        // CALCULAR TAMAÑO - 4 CARDS MÁS PEQUEÑAS
        recyclerView.post {
            val containerWidth = recyclerView.width - recyclerView.paddingStart - recyclerView.paddingEnd
            val cardSize = when (animals.size) {
                2 -> (containerWidth / 2.9).toInt() // Grandes
                3 -> (containerWidth / 3.5).toInt() // Medianas
                4 -> (containerWidth / 4.4).toInt() // ← PEQUEÑAS para 4 en línea
                else -> (containerWidth / 2.2).toInt()
            }
            adapter.setCardSize(cardSize)
        }

        recyclerView.adapter = adapter
        updateProgress(level)
    }

    private fun getAnimalsForLevel(level: Int): List<Animal> {
        return when (level) {
            1 -> listOf(
                Animal(1, R.drawable.animal_perro, "Perro", R.raw.sonido_perro, true),
                Animal(2, R.drawable.animal_gato, "Gato", R.raw.sonido_perro)
                       )

            2 -> listOf(
                Animal(1, R.drawable.animal_vaca, "León", R.raw.sonido_perro, true),
                Animal(2, R.drawable.animal_conejo, "Elefante", R.raw.sonido_perro)
                       )

            3 -> listOf(
                Animal(1, R.drawable.animal_rana, "Pájaro", R.raw.sonido_perro,true),
                Animal(2, R.drawable.animal_raton, "Oveja", R.raw.sonido_perro),
                Animal(3, R.drawable.animal_loro, "Cerdo", R.raw.sonido_perro)
                       )

            4 -> listOf(
                Animal(1, R.drawable.animal_cerdo, "Vaca", R.raw.sonido_perro, true),
                Animal(2, R.drawable.animal_koala, "Caballo", R.raw.sonido_perro),
                Animal(3, R.drawable.animal_ardilla, "Gallina", R.raw.sonido_perro)
                       )

            5 -> listOf(
                Animal(1, R.drawable.animal_pato, "Pez", R.raw.sonido_perro, true),
                Animal(2, R.drawable.animal_oveja, "Tigre", R.raw.sonido_perro),
                Animal(3, R.drawable.animal_aguila, "Oso", R.raw.sonido_perro),
                Animal(4, R.drawable.animal_tortuga, "Mono", R.raw.sonido_perro)
                       )

            6 -> listOf(
                Animal(1, R.drawable.animal_elefante, "Serpiente", R.raw.sonido_perro, true),
                Animal(2, R.drawable.animal_cabra, "Ardilla", R.raw.sonido_perro),
                Animal(3, R.drawable.animal_vaca, "Zorro", R.raw.sonido_perro),
                Animal(4, R.drawable.animal_hamster, "Lobo", R.raw.sonido_perro)
                       )

            7 -> listOf(
                Animal(1, R.drawable.animal_zorro, "Jirafa", R.raw.sonido_perro, true),
                Animal(2, R.drawable.animal_girafa, "Cebra", R.raw.sonido_perro),
                Animal(3, R.drawable.animal_pinguino, "Hipopótamo", R.raw.sonido_perro),
                Animal(4, R.drawable.animal_rinoceronte, "Cocodrilo", R.raw.sonido_perro)
                       )

            8 -> listOf(
                Animal(1, R.drawable.animal_cocodrilo, "Pingüino", R.raw.sonido_perro, true),
                Animal(2, R.drawable.animal_oso_panda, "Foca", R.raw.sonido_perro),
                Animal(3, R.drawable.animal_koala, "Ballena", R.raw.sonido_perro),
                Animal(4, R.drawable.animal_oso, "Delfín", R.raw.sonido_perro)
                       )

            9 -> listOf(
                Animal(1, R.drawable.animal_perro, "Águila", R.raw.sonido_perro, true),
                Animal(2, R.drawable.animal_lobo, "Búho", R.raw.sonido_perro)
                       )

            10 -> listOf(
                Animal(1, R.drawable.animal_leon, "Mariposa", R.raw.sonido_perro),
                Animal(2, R.drawable.animal_tigre, "Abeja", R.raw.sonido_perro, true)
                        )

            else -> emptyList()
        }
    }

    private fun handleAnimalSelection(selectedAnimal: Animal, allAnimals: List<Animal>) {
        isInteractionEnabled = false

        if (selectedAnimal.isCorrect) {
            // ACIERTO - Verde y pasar nivel después de 3 segundos
            Handler(Looper.getMainLooper()).postDelayed({
                                                            currentLevel++
                                                            if (currentLevel <= 10) {
                                                                setupLevel(currentLevel)
                                                            } else {
                                                                // Juego completado
                                                                Toast.makeText(
                                                                    this,
                                                                    "¡Juego completado!",
                                                                    Toast.LENGTH_SHORT
                                                                              ).show()
                                                            }
                                                        }, 3000)
        } else {
            // ERROR - Rojo y desactivar card después de 3 segundos
            selectedAnimal.isSelected = true
            Handler(Looper.getMainLooper()).postDelayed({
                                                            // Marcar card como desactivada (gris)
                                                            selectedAnimal.isSelected =
                                                                false // Reset para cambiar a gris
                                                            adapter.notifyItemChanged(
                                                                allAnimals.indexOf(
                                                                    selectedAnimal
                                                                                  )
                                                                                     )
                                                        }, 3000)
        }

        adapter.notifyDataSetChanged()
    }

    private fun updateProgress(level: Int) {
        progressBar.progress = (level * 10)
        tvQuestion.text = "Nivel $level"
    }
}