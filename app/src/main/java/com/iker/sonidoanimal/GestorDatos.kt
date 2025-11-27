package com.iker.sonidoanimal

import android.content.Context
import android.os.Environment
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object GestorDatos {
    fun guardarPartida(context: Context, partida: Partida) {
        try {
            // 1. Convertir los datos a formato JSON
            val json = JSONObject()
            json.put("nombre", partida.nombreNino)
            json.put("nivel", partida.nivel)
            json.put("tiempo_segundos", partida.tiempoSegundos)
            json.put("errores", partida.errores)
            json.put("puntuacion", partida.puntuacion)
            json.put("fecha", partida.fechaHora)

            // 2. Crear el nombre del archivo (Ej: partida_Iker_Nivel1_123456.json)
            val timeStamp = System.currentTimeMillis()
            val nombreArchivo = "partida_${partida.nombreNino}_Nivel${partida.nivel}_$timeStamp.json"

            // 3. Decidir dónde guardar (Carpeta Documentos de la app)
            // Ruta: Android/data/com.iker.sonidoanimal/files/Documents
            val directorio = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

            if (directorio != null && !directorio.exists()) {
                directorio.mkdirs()
            }

            // 4. Escribir el archivo
            val archivo = File(directorio, nombreArchivo)
            val writer = FileWriter(archivo)
            writer.append(json.toString())
            writer.flush()
            writer.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Función auxiliar para obtener la fecha actual bonita
    fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }
}