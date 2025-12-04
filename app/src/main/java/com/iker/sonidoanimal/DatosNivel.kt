package com.iker.sonidoanimal

/**
 * Representa los datos estadísticos de UN solo nivel completado.
 * Usamos 'val' porque una vez guardado el historial del nivel, no debe modificarse.
 */
data class DatosNivel(
    val level: Int,           // El número del nivel (1, 2, 3...)
    val levelLength: Long,    // Cuánto tardó (en segundos)
    val nAttempts: Int,       // Número de intentos
    val errors: Int,          // Número de errores cometidos
    val pointsScored: Int,    // Puntos ganados (100, 50, 25, 0)
    val helpClicks: Int       // Veces que pulsó ayuda
)