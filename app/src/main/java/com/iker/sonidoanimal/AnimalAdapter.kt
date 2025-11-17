package com.iker.sonidoanimal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class AnimalAdapter(
    private val animals: List<Animal>,
    private val onAnimalSelected: (Animal) -> Unit
                   ) : RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder>() {

    class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardAnimal)
        val imageView: ImageView = itemView.findViewById(R.id.ivAnimal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal_card, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animals[position]

        holder.imageView.setImageResource(animal.imageRes)
        updateCardAppearance(holder, animal)

        holder.cardView.setOnClickListener {
            onAnimalSelected(animal)
            animal.isSelected = true
            updateCardAppearance(holder, animal)
        }
    }

    private fun updateCardAppearance(holder: AnimalViewHolder, animal: Animal) {
        val context = holder.itemView.context

        when {
            animal.isCorrect && animal.isSelected -> {
                // ACIERTO - Verde
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.correct_color))
                holder.cardView.cardElevation = 12f
            }
            animal.isSelected -> {
                // ERROR - Rojo (momentáneo)
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.wrong_color))
            }
            else -> {
                // NORMAL o DESACTIVADO - Azul o Gris
                val color = if (animal.isSelected) {
                    // Estado desactivado (gris)
                    ContextCompat.getColor(context, R.color.disabled_color)
                } else {
                    // Estado normal
                    ContextCompat.getColor(context, R.color.card_default_color)
                }
                holder.cardView.setCardBackgroundColor(color)
                holder.cardView.cardElevation = 4f
            }
        }

        // Desactivar clics si está en estado de error
        holder.cardView.isEnabled = !animal.isSelected
    }

    override fun getItemCount() = animals.size
}