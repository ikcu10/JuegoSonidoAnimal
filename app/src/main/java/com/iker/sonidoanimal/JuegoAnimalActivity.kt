package com.iker.sonidoanimal

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        setContentView(R.layout.activity_main)

        setupViews()
        setupLevel(currentLevel)
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerViewAnimals)
        tvQuestion = findViewById(R.id.texto_pregunta)
        progressBar = findViewById(R.id.barra_progreso)
        btnSound = findViewById(R.id.boton_sonido)

        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        btnSound.setOnClickListener {
            // Lógica sonido posterior
        }
    }

    private fun setupLevel(level: Int) {
        val animals = getAnimalsForLevel(level)
        isInteractionEnabled = true

        adapter = AnimalAdapter(animals) { selectedAnimal ->
            if (isInteractionEnabled) {
                handleAnimalSelection(selectedAnimal, animals)
            }
        }

        recyclerView.adapter = adapter
        updateProgress(level)
    }

    private fun getAnimalsForLevel(level: Int): List<Animal> {
        return when (level) {
            1 -> listOf(
                Animal(1, R.drawable.animal_perro, "Perro", R.raw.sonido_perro, true),
                Animal(2, R.drawable.animal_gato, "Gato", R.raw.sonido_gato)
                       )
            2 -> listOf(
                Animal(1, R.drawable.animal_vaca, "León", R.raw.sonido_leon, true),
                Animal(2, R.drawable.animal_conejo, "Elefante", R.raw.sonido_elefante)
                       )
            3 -> listOf(
                Animal(1, R.drawable.animal_rana, "Pájaro", R.raw.sonido_pajaro),
                Animal(2, R.drawable.animal_raton, "Oveja", R.raw.sonido_oveja, true),
                Animal(3, R.drawable.animal_loro, "Cerdo", R.raw.sonido_cerdo)
                       )
            4 -> listOf(
                Animal(1, R.drawable.animal_vaca, "Vaca", R.raw.sonido_vaca, true),
                Animal(2, R.drawable.animal_caballo, "Caballo", R.raw.sonido_caballo),
                Animal(3, R.drawable.animal_gallina, "Gallina", R.raw.sonido_gallina)
                       )
            5 -> listOf(
                Animal(1, R.drawable.animal_pez, "Pez", R.raw.sonido_pez),
                Animal(2, R.drawable.animal_tigre, "Tigre", R.raw.sonido_tigre, true),
                Animal(3, R.drawable.animal_oso, "Oso", R.raw.sonido_oso),
                Animal(4, R.drawable.animal_mono, "Mono", R.raw.sonido_mono)
                       )
            6 -> listOf(
                Animal(1, R.drawable.animal_serpiente, "Serpiente", R.raw.sonido_serpiente),
                Animal(2, R.drawable.animal_ardilla, "Ardilla", R.raw.sonido_ardilla),
                Animal(3, R.drawable.animal_zorro, "Zorro", R.raw.sonido_zorro, true),
                Animal(4, R.drawable.animal_lobo, "Lobo", R.raw.sonido_lobo)
                       )
            7 -> listOf(
                Animal(1, R.drawable.animal_jirafa, "Jirafa", R.raw.sonido_jirafa, true),
                Animal(2, R.drawable.animal_cebra, "Cebra", R.raw.sonido_cebra),
                Animal(3, R.drawable.animal_hipopotamo, "Hipopótamo", R.raw.sonido_hipopotamo),
                Animal(4, R.drawable.animal_cocodrilo, "Cocodrilo", R.raw.sonido_cocodrilo)
                       )
            8 -> listOf(
                Animal(1, R.drawable.animal_pinguino, "Pingüino", R.raw.sonido_pinguino),
                Animal(2, R.drawable.animal_foca, "Foca", R.raw.sonido_foca),
                Animal(3, R.drawable.animal_ballena, "Ballena", R.raw.sonido_ballena),
                Animal(4, R.drawable.animal_delfin, "Delfín", R.raw.sonido_delfin, true)
                       )
            9 -> listOf(
                Animal(1, R.drawable.animal_aguila, "Águila", R.raw.sonido_aguila, true),
                Animal(2, R.drawable.animal_buho, "Búho", R.raw.sonido_buho)
                       )
            10 -> listOf(
                Animal(1, R.drawable.animal_mariposa, "Mariposa", R.raw.sonido_mariposa),
                Animal(2, R.drawable.animal_abeja, "Abeja", R.raw.sonido_abeja, true)
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
                                                                Toast.makeText(this, "¡Juego completado!", Toast.LENGTH_SHORT).show()
                                                            }
                                                        }, 3000)
        } else {
            // ERROR - Rojo y desactivar card después de 3 segundos
            selectedAnimal.isSelected = true
            Handler(Looper.getMainLooper()).postDelayed({
                                                            // Marcar card como desactivada (gris)
                                                            selectedAnimal.isSelected = false // Reset para cambiar a gris
                                                            adapter.notifyItemChanged(allAnimals.indexOf(selectedAnimal))
                                                        }, 3000)
        }

        adapter.notifyDataSetChanged()
    }

    private fun updateProgress(level: Int) {
        progressBar.progress = (level * 10)
        tvQuestion.text = "Nivel $level"
    }
}