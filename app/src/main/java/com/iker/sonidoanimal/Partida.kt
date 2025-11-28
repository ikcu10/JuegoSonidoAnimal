package com.iker.sonidoanimal

data class Partida(
    val nombreNino: String,
    val nivel: Int,
    val numeroPartida: Int,
    val tiempoSegundos: Long,
    val errores: Int,
    val puntuacion: Int,
    val fechaHora: String
                  )
