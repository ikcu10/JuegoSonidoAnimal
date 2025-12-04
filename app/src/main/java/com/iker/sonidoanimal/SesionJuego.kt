package com.iker.sonidoanimal

/**
 * Representa la sesión activa del jugador.
 * Contiene la información global y la lista de todos los niveles que vaya superando.
 */
data class SesionJuego(
    var username: String = "",          // Cambiará cuando el usuario haga Login
    var sessionId: String = "",         // Se generará al empezar
    var dateTime: String = "",          // Se guardará la fecha de inicio
    var sessionLength: Long = 0,        // Se calculará al final del todo

    // La lista donde iremos guardando los 'DatosNivel'.
    // Es 'val' porque la lista es siempre la misma caja, aunque metamos objetos dentro.
    val levels: ArrayList<DatosNivel> = ArrayList(),

    var levelReached: Int = 1           // Irá subiendo según gane niveles
)