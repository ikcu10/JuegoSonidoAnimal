package com.iker.sonidoanimal

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import org.json.JSONObject

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val txtMetricas = findViewById<TextView>(R.id.txt_metricas)
        val img1 = findViewById<ImageView>(R.id.img_grafico1)
        val img2 = findViewById<ImageView>(R.id.img_grafico2)
        val img3 = findViewById<ImageView>(R.id.img_grafico3)

        // Ejecutamos la carga de datos nada más abrir la pantalla
        cargarDatosPython(txtMetricas, img1, img2, img3)
    }

    private fun cargarDatosPython(txt: TextView, i1: ImageView, i2: ImageView, i3: ImageView) {
        try {
            val py = Python.getInstance()
            val modulo = py.getModule("script")

            val resultadoJsonString = modulo.callAttr("procesar_datos_y_graficos").toString()
            val json = JSONObject(resultadoJsonString)

            if (json.has("error")) {
                txt.text = "Error: " + json.getString("error")
                return
            }

            // 1. Mostrar Métricas COMPLETAS (Según requisitos)
            val metrics = json.getJSONObject("metrics")
            val jugadores = metrics.getInt("player_count")
            val acc = metrics.getDouble("accuracy")
            val prec = metrics.getDouble("precision")
            val rec = metrics.getDouble("recall")

            txt.text = "📊 RESULTADOS DEL MODELO ML:\n\n" +
                    "• Jugadores Únicos: $jugadores\n" +
                    "-----------------------------\n" +
                    "• Accuracy (Exactitud): ${(acc * 100).toInt()}%\n" +
                    "• Precision: ${(prec * 100).toInt()}%\n" +
                    "• Recall (Sensibilidad): ${(rec * 100).toInt()}%\n" +
                    "-----------------------------\n" +
                    "Interpretación visual abajo:"

            // 2. Mostrar Gráficos ACTUALIZADOS
            val charts = json.getJSONObject("charts")

            // Gráfico 1: Matriz de Confusión (NUEVO)
            i1.setImageBitmap(convertirBase64(charts.getString("confusion_matrix")))

            // Gráfico 2: Importancia de Variables
            i2.setImageBitmap(convertirBase64(charts.getString("feature_importance")))

            // Gráfico 3: Distribución
            i3.setImageBitmap(convertirBase64(charts.getString("hist_distribucion")))

            Toast.makeText(this, "Informe generado correctamente", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            txt.text = "Error cargando datos: ${e.message}"
            e.printStackTrace()
        }
    }

    private fun convertirBase64(base64Str: String): android.graphics.Bitmap {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}