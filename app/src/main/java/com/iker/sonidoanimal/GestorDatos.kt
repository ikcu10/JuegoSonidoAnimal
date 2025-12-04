package com.iker.sonidoanimal

import android.content.Context
import android.os.Environment
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object GestorDatos {

    // --- MEMORIA RAM (El estado actual del juego) ---
    // Mantenemos una instancia viva de la sesión mientras la app está abierta.
    private val sesionActual = SesionJuego()

    // Variable auxiliar para calcular el tiempo total de la sesión (session_length)
    private var tiempoInicioSesion: Long = 0

    // -----------------------------------------------------------------------
    // 1. INICIAR SESIÓN (Se llama desde LoginActivity)
    // -----------------------------------------------------------------------
    fun iniciarNuevaSesion(nombreUsuario: String) {
        // Reseteamos los datos para empezar limpio
        sesionActual.username = nombreUsuario
        sesionActual.levels.clear()
        sesionActual.levelReached = 1

        // Generamos el ID de sesión y la fecha de inicio
        sesionActual.sessionId = generarIdSesion()
        sesionActual.dateTime = obtenerFechaFormatoIso()

        // Marcamos la hora exacta en milisegundos para calcular luego la duración
        tiempoInicioSesion = System.currentTimeMillis()
    }

    // -----------------------------------------------------------------------
    // 2. REGISTRAR UN NIVEL (Se llama desde JuegoAnimalesActivity al ganar)
    // -----------------------------------------------------------------------
    fun registrarNivel(
        context: Context,
        nivel: Int,
        duracionSegundos: Long,
        errores: Int,
        puntos: Int,
        clicksAyuda: Int
    ) {
        // Calculamos intentos: Si tuvo 2 errores, es que lo intentó 3 veces (2 fallos + 1 acierto)
        // Si prefieres contar solo "partidas jugadas", pon 1. Aquí asumo intentos de respuesta.
        val intentosTotales = errores + 1

        // Creamos el objeto con los datos de ESTE nivel
        val datosNivel = DatosNivel(
            level = nivel,
            levelLength = duracionSegundos,
            nAttempts = intentosTotales,
            errors = errores,
            pointsScored = puntos,
            helpClicks = clicksAyuda
        )

        // Lo añadimos a la lista de la sesión en memoria
        sesionActual.levels.add(datosNivel)

        // Actualizamos hasta qué nivel ha llegado el jugador
        if (nivel > sesionActual.levelReached) {
            sesionActual.levelReached = nivel
        }

        // Actualizamos la duración total de la sesión hasta este momento
        val tiempoActual = System.currentTimeMillis()
        sesionActual.sessionLength = (tiempoActual - tiempoInicioSesion) / 1000

        // GUARDAMOS EN DISCO INMEDIATAMENTE
        // Así, si cierra la app, no pierde lo que lleva jugado.
        guardarJsonEnDisco(context)
    }

    // -----------------------------------------------------------------------
    // 3. GUARDAR EN DISCO (Convierte los objetos a JSON manual)
    // -----------------------------------------------------------------------
    private fun guardarJsonEnDisco(context: Context) {
        try {
            // PASO A: Crear el objeto JSON raíz (La Sesión)
            val jsonRaiz = JSONObject()
            jsonRaiz.put("username", sesionActual.username)
            jsonRaiz.put("session_id", sesionActual.sessionId)
            jsonRaiz.put("date_time", sesionActual.dateTime)
            jsonRaiz.put("session_length", sesionActual.sessionLength)
            jsonRaiz.put("level_reached", sesionActual.levelReached)

            // PASO B: Crear el Array JSON para los niveles
            val jsonArrayNiveles = JSONArray()

            // Recorremos nuestra lista de objetos y los convertimos a JSON uno a uno
            for (nivel in sesionActual.levels) {
                val jsonNivel = JSONObject()
                // Aquí usamos los nombres con guión bajo que pide el ejercicio
                jsonNivel.put("level", nivel.level)
                jsonNivel.put("level_length", nivel.levelLength)
                jsonNivel.put("n_attempts", nivel.nAttempts)
                jsonNivel.put("errors", nivel.errors)
                jsonNivel.put("points_scored", nivel.pointsScored)
                jsonNivel.put("help_clicks", nivel.helpClicks)

                // Añadimos este nivel al array
                jsonArrayNiveles.put(jsonNivel)
            }

            // Metemos el array dentro del objeto raíz
            jsonRaiz.put("levels", jsonArrayNiveles)

            // PASO C: Escribir el archivo
            // Nombre del archivo: sesion_iker_20251203_001.json
            val nombreArchivo = "sesion_${sesionActual.username}_${sesionActual.sessionId}.json"

            // Guardamos en la carpeta pública de documentos de la app para que puedas verlo fácil
            val directorio = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

            // Si el directorio no existe, lo creamos
            if (directorio != null && !directorio.exists()) {
                directorio.mkdirs()
            }

            val archivo = File(directorio, nombreArchivo)
            val writer = FileWriter(archivo)

            // Escribimos el JSON con indentación de 4 espacios (para que se vea bonito)
            writer.write(jsonRaiz.toString(4))
            writer.flush()
            writer.close()

        } catch (e: Exception) {
            e.printStackTrace() // Si falla, lo veremos en el Logcat
        }
    }

    // --- FUNCIONES AUXILIARES ---

    private fun generarIdSesion(): String {
        // Genera algo como: 20251203_153022
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun obtenerFechaFormatoIso(): String {
        // Formato estándar: 2025-12-03T15:30:00
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}