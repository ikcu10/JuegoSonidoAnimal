import pandas as pd
import json
import random
import io
from datetime import datetime, timedelta

# --- 1. GENERADOR DE DATOS (Simula tu JSON real) ---
def generar_datos_simulados(n_sesiones=50):
    datos = []
    usuarios = ["Iker", "Maria", "Juan", "Ana", "TestUser"]

    for i in range(n_sesiones):
        # Estructura PADRE
        sesion = {
            "username": random.choice(usuarios),
            "session_id": f"20251206_{100000 + i}",
            "date_time": (datetime.now() - timedelta(days=random.randint(0, 10))).isoformat(),
            "session_length": random.randint(20, 300),
            "level_reached": random.randint(1, 5),
            "levels": [] # La lista anidada
        }

        # Estructura HIJOS (Tus niveles)
        for nivel in range(1, 6):
            stats_nivel = {
                "level": nivel,
                "level_length": random.randint(0, 60),
                "n_attempts": random.randint(1, 3),
                "errors": random.randint(0, 5),
                "points_scored": random.choice([0, 50, 100]),
                "help_clicks": random.randint(0, 2)
            }
            sesion["levels"].append(stats_nivel)

        datos.append(sesion)
    return datos

# --- 2. EL APLANADOR (Transforma JSON complejo a Tabla Plana) ---
def aplanar_json(json_data):
    # Cogemos los datos generales
    fila = {
        "username": json_data["username"],
        "session_id": json_data["session_id"],
        "date_time": json_data["date_time"],
        "session_length": json_data["session_length"],
        "level_reached": json_data["level_reached"]
    }

    # "Aplanamos" los niveles: De lista a columnas
    for nivel_data in json_data["levels"]:
        n = nivel_data["level"]
        # Creamos columnas: level_1_errors, level_1_points...
        fila[f"level_{n}_length"] = nivel_data["level_length"]
        fila[f"level_{n}_attempts"] = nivel_data["n_attempts"]
        fila[f"level_{n}_errors"] = nivel_data["errors"]
        fila[f"level_{n}_points"] = nivel_data["points_scored"]
        fila[f"level_{n}_help_clicks"] = nivel_data["help_clicks"]

    return fila

# --- 3. FUNCIÓN PRINCIPAL (La que llamará Kotlin) ---
def cargar_y_explorar():
    try:
        # A. Generamos y aplanamos los datos
        lista_raw = generar_datos_simulados(50)
        datos_aplanados = [aplanar_json(d) for d in lista_raw]

        # B. Creamos el DataFrame
        df = pd.DataFrame(datos_aplanados)

        # C. Ejecutamos las inspecciones que pide el ejercicio
        buffer = io.StringIO() # Usamos un buffer para capturar el texto

        buffer.write("=== 1. DF.HEAD() (Primeras filas) ===\n")
        buffer.write(df.head().to_string())
        buffer.write("\n\n")

        buffer.write("=== 2. DF.INFO() (Estructura y Nulos) ===\n")
        df.info(buf=buffer)
        buffer.write("\n")

        buffer.write("=== 3. DF.DESCRIBE() (Estadísticas) ===\n")
        buffer.write(df.describe().to_string())

        return buffer.getvalue() # Devolvemos todo el texto junto

    except Exception as e:
        return f"❌ Error en Python: {str(e)}"