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
        val img4 = findViewById<ImageView>(R.id.img_grafico4)
        val img5 = findViewById<ImageView>(R.id.img_grafico5)
        val img6 = findViewById<ImageView>(R.id.img_grafico6)

        // Ejecutamos la carga de datos nada más abrir la pantalla
        cargarDatosPython(txtMetricas, img1, img2, img3, img4, img5, img6)
    }

    private fun cargarDatosPython(txt: TextView,
                                  i1: ImageView, i2: ImageView, i3: ImageView,
                                  i4: ImageView, i5: ImageView, i6: ImageView) {
        try {
            val py = Python.getInstance()

            val modulo = py.getModule("script")

            val resultadoJsonString = modulo.callAttr("procesar_datos_y_graficos").toString()
            val json = JSONObject(resultadoJsonString)

            if (json.has("error")) {
                txt.text = "Error: " + json.getString("error")
                return
            }

            // 1. OBTENER MÉTRICAS
            val metrics = json.getJSONObject("metrics")

            // Datos básicos y de IA
            val jugadores = metrics.getInt("player_count")
            val acc = metrics.getDouble("accuracy")
            val prec = metrics.getDouble("precision")
            val rec = metrics.getDouble("recall")

            // Datos Nuevos (Estadísticas de Negocio)
            // Usamos optDouble por seguridad, por si alguno viene nulo
            val retention = metrics.optDouble("retention_rate", 0.0)
            val churn = metrics.optDouble("churn_rate", 0.0)
            val avgSec = metrics.optDouble("avg_session_sec", 0.0)
            val dau = metrics.optDouble("avg_dau", 0.0)

            val avgMin = String.format("%.2f", avgSec / 60.0)


            txt.text = """
                📊 MÉTRICAS DE USUARIO:
                
                • Jugadores Totales: $jugadores
                • Tasa de Retención: $retention%
                • Tasa de Abandono: $churn%
                • Usuarios/Día (DAU): $dau
                • Tiempo Medio: $avgMin min
                
                -----------------------------
                
                🤖 RENDIMIENTO MODELO IA:
                
                • Accuracy (Exactitud): ${(acc * 100).toInt()}%
                • Precision: ${(prec * 100).toInt()}%
                • Recall (Sensibilidad): ${(rec * 100).toInt()}%
                
                (Gráficos visuales abajo 👇)
            """.trimIndent()


            // 3. MOSTRAR GRÁFICOS
            val charts = json.getJSONObject("charts")

            // IA
            if (charts.has("confusion_matrix")) i1.setImageBitmap(convertirBase64(charts.getString("confusion_matrix")))
            if (charts.has("feature_importance")) i2.setImageBitmap(convertirBase64(charts.getString("feature_importance")))

            // DATOS
            if (charts.has("hist_distribucion")) i3.setImageBitmap(convertirBase64(charts.getString("hist_distribucion")))
            if (charts.has("scatter_corr")) i4.setImageBitmap(convertirBase64(charts.getString("scatter_corr")))
            if (charts.has("line_dau")) i5.setImageBitmap(convertirBase64(charts.getString("line_dau")))
            if (charts.has("bar_churn")) i6.setImageBitmap(convertirBase64(charts.getString("bar_churn")))

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