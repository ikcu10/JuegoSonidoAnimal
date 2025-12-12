import pandas as pd
import json
import io
import base64
import numpy as np
import os
import glob
from datetime import datetime, timedelta

# Importamos la librería para conectar con Android
from com.chaquo.python import Python

# Gráficos
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.dates as mdates

# IA / Machine Learning
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score, precision_score, recall_score, confusion_matrix

# 1. APLANAR EL JSON Y CALCULAR PUNTOS
def aplanar_json(data):
    puntos_totales = 0
    errores_totales = 0

    if "levels" in data and isinstance(data["levels"], list):
        for nivel in data["levels"]:
            puntos_totales += nivel.get("points_scored", 0)
            errores_totales += nivel.get("errors", 0)

    return {
        "username": data.get("username"),
        "session_id": data.get("session_id"),
        "date_time": data.get("date_time"),
        "session_length": data.get("session_length"),
        "level_reached": data.get("level_reached"),
        "total_points": puntos_totales,
        "total_errors": errores_totales
    }

# 2. FUNCIÓN IMÁGENES
def plot_to_base64():
    buffer = io.BytesIO()
    plt.savefig(buffer, format='png', bbox_inches='tight', dpi=300)
    buffer.seek(0)
    img_str = base64.b64encode(buffer.getvalue()).decode('utf-8')
    buffer.close()
    plt.close()
    return img_str

# 3. PROCESO PRINCIPAL
def procesar_datos_y_graficos():
    try:
        plt.rcParams.update({'font.size': 14})
        datos_cargados = []

        # A. LECTURA DEL STORAGE
        context = Python.getPlatform().getApplication()
        ruta_base = str(context.getExternalFilesDir(None))
        ruta_completa = os.path.join(ruta_base, "Documents", "*.json")
        lista_archivos = glob.glob(ruta_completa)

        # SEGURIDAD
        if not lista_archivos:
            return json.dumps({"error": f"No se encontraron datos en {ruta_base}/Documents. ¿Has ejecutado el generador de Kotlin?"})

        # Leemos archivo por archivo
        for archivo in lista_archivos:
            try:
                with open(archivo, 'r') as f:
                    contenido = json.load(f)
                    datos_cargados.append(contenido)
            except:
                pass

        # B. PROCESAMIENTO PANDAS y IA

        # Convertimos a DataFrame
        datos_aplanados = [aplanar_json(d) for d in datos_cargados]
        df = pd.DataFrame(datos_aplanados)
        df = df.dropna(subset=['username'])

        # Convertir tipos de datos
        df['date_time'] = pd.to_datetime(df['date_time'])
        df['session_minutes'] = df['session_length'] / 60.0

        # ETIQUETA 'RETURNING PLAYER'
        conteos = df['username'].value_counts()
        df['returning_player'] = df['username'].apply(lambda x: 1 if conteos[x] > 1 else 0)

        # C. CÁLCULO DE MÉTRICAS GLOBALES

        # 1. Player Count (Total usuarios únicos)
        total_unique_players = len(conteos)

        # 2. Conteo de Fieles vs Turistas
        returning_users_count = sum(conteos > 1)

        # 3. Retention Rate (% de usuarios que vuelven)
        # Fórmula: (Usuarios que repiten / Total usuarios únicos) * 100
        if total_unique_players > 0:
            retention_rate = (returning_users_count / total_unique_players) * 100
        else:
            retention_rate = 0.0

        # 4. Churn Rate (% de usuarios que abandonan tras 1 sesión)
        churn_rate = 100.0 - retention_rate

        # 5. Average Session Length (Duración media en SEGUNDOS)
        avg_session_seconds = df['session_length'].mean()

        # 6. DAU (Daily Active Users) - Promedio
        df['date_only'] = df['date_time'].dt.date
        dau_series = df.groupby('date_only')['username'].nunique()
        avg_dau = dau_series.mean() # Promedio de usuarios por día

        # ENTRENAMIENTO DEL MODELO
        features = ['session_length', 'level_reached', 'total_points']
        X = df[features]
        y = df['returning_player']

        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

        modelo = DecisionTreeClassifier(max_depth=3)
        modelo.fit(X_train, y_train)
        predicciones = modelo.predict(X_test)

        # Métricas
        try:
            acc = accuracy_score(y_test, predicciones)
            prec = precision_score(y_test, predicciones, zero_division=0)
            rec = recall_score(y_test, predicciones, zero_division=0)
        except:
            acc, prec, rec = 0.0, 0.0, 0.0

        # GENERACIÓN DE GRÁFICOS
        imagenes = {}

        # IA

        # Gráfico 1: Matriz de Confusión
        plt.figure(figsize=(12, 5)) # Tamaño grande
        cm = confusion_matrix(y_test, predicciones)

        plt.imshow(cm, interpolation='nearest', cmap='Blues')
        plt.title('Matriz de Confusión')
        plt.colorbar()

        clases_reales = len(np.unique(y_test))
        tick_marks = np.arange(clases_reales)
        nombres = ["Abandona", "Vuelve"]

        plt.xticks(tick_marks, nombres[:clases_reales])
        plt.yticks(tick_marks, nombres[:clases_reales])
        plt.ylabel('Realidad')
        plt.xlabel('Predicción')

        thresh = cm.max() / 2.
        for i in range(cm.shape[0]):
            for j in range(cm.shape[1]):
                plt.text(j, i, format(cm[i, j], 'd'),
                         horizontalalignment="center",
                         color="white" if cm[i, j] > thresh else "black")

        imagenes['confusion_matrix'] = plot_to_base64()

        # Gráfico 2: Importancia de Variables
        plt.figure(figsize=(12, 5))
        importancia = pd.Series(modelo.feature_importances_, index=features)
        importancia.plot(kind='barh', color='purple')
        plt.title('Importancia de Variables')
        plt.tight_layout()
        imagenes['feature_importance'] = plot_to_base64()

        # SECCIÓN ANÁLISIS DATOS

        # 3. Distribución Puntos
        plt.figure(figsize=(12, 5))
        plt.hist(df['total_points'], bins=10, color='green', edgecolor='black')
        plt.title('Análisis: Distribución de Puntos')
        plt.tight_layout()
        imagenes['hist_distribucion'] = plot_to_base64()

        # 4. Correlación
        plt.figure(figsize=(12, 5))
        plt.scatter(df['session_minutes'], df['total_points'], alpha=0.5, c='blue')
        plt.title('Análisis: Correlación Tiempo vs Puntos')
        plt.xlabel('Minutos')
        plt.ylabel('Puntos')
        plt.grid(True)
        plt.tight_layout()
        imagenes['scatter_corr'] = plot_to_base64()

        # 5. Evolución
        plt.figure(figsize=(12, 5))

        # Ordenamos por fecha
        dau_sorted = dau_series.sort_index()

        # Pintamos la línea roja con puntos
        plt.plot(dau_sorted.index, dau_sorted.values, marker='o', color='red', linestyle='-', linewidth=2)

        plt.title('6. Evolución de Jugadores Activos (DAU)')
        plt.ylabel('Usuarios Únicos')
        plt.xlabel('Fecha (Día/Mes)')
        plt.grid(True, linestyle='--', alpha=0.7)

        # Definimos el formato: Día/Mes
        myFmt = mdates.DateFormatter('%d/%m')
        plt.gca().xaxis.set_major_formatter(myFmt)

        # Rotamos las fechas
        plt.gcf().autofmt_xdate()

        plt.tight_layout()
        imagenes['line_dau'] = plot_to_base64()

        # 6. Abandono (Churn)
        abandonos = df[df['returning_player'] == 0]
        conteo_abandono = abandonos['level_reached'].value_counts().sort_index()
        plt.figure(figsize=(12, 5))
        conteo_abandono.plot(kind='bar', color='gray', edgecolor='black')
        plt.title('Análisis: ¿Dónde abandonan?')
        plt.xlabel('Nivel')
        plt.tight_layout()
        imagenes['bar_churn'] = plot_to_base64()

        # D. RETORNO DE DATOS JSON
        resultado = {
            "metrics": {
                "player_count": int(df['username'].nunique()),
                "retention_rate": round(retention_rate, 2),     # NUEVO
                "churn_rate": round(churn_rate, 2),             # NUEVO
                "avg_session_sec": round(avg_session_seconds, 2), # NUEVO
                "avg_dau": round(avg_dau, 2),
                "accuracy": round(acc, 2),
                "precision": round(prec, 2),
                "recall": round(rec, 2)
            },
            "charts": imagenes
        }

        return json.dumps(resultado)

    except Exception as e:
        import traceback
        return json.dumps({"error": str(e) + "\n" + traceback.format_exc()})