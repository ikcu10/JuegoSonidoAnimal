import pandas as pd
import json
import random
import io
import base64
import numpy as np
from datetime import datetime, timedelta

# Gráficos
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt

# IA / Machine Learning
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier
# NUEVO: Importamos métricas avanzadas
from sklearn.metrics import accuracy_score, precision_score, recall_score, confusion_matrix, ConfusionMatrixDisplay

# ... (LAS FUNCIONES generar_datos_simulados Y aplanar_json SE QUEDAN IGUAL) ...
# ... (CÓPIALAS DEL CÓDIGO ANTERIOR O DÉJALAS COMO ESTABAN) ...
# Para ahorrar espacio, asumo que tienes generar_datos_simulados y aplanar_json aquí.
# Si borraste todo, dímelo y te las pego enteras, pero son las mismas de antes.

# ==========================================
# GENERADOR DE DATOS (REPETIMOS BREVEMENTE PARA QUE EL SCRIPT FUNCIONE AL PEGAR)
def generar_datos_simulados(n_sesiones=150):
    datos = []
    usuarios_fieles = ["Iker", "Maria", "GamerPro", "Luisa"]
    usuarios_abandono = ["TestUser", "Carlos", "Juan", "Invitado"]
    todos = usuarios_fieles + usuarios_abandono
    for i in range(n_sesiones):
        usuario_actual = random.choice(todos)
        if usuario_actual in usuarios_fieles:
            duracion = random.randint(120, 600)
            nivel_max = random.randint(3, 5)
            puntos_base = 100
        else:
            duracion = random.randint(10, 100)
            nivel_max = random.randint(1, 2)
            puntos_base = 0
        sesion = {
            "username": usuario_actual,
            "session_id": f"sess_{1000 + i}",
            "date_time": (datetime.now() - timedelta(days=random.randint(0, 30))).isoformat(),
            "session_length": duracion,
            "level_reached": nivel_max,
            "levels": [],
            "total_points": 0, "total_errors": 0 # Inicializamos
        }
        total_puntos = 0
        for nivel in range(1, 6):
            if nivel <= nivel_max:
                pts = random.choice([0, 50, 100]) + puntos_base
                if pts > 100: pts = 100
                total_puntos += pts
        sesion["total_points"] = total_puntos
        datos.append(sesion)
    return datos

def aplanar_json(json_data):
    return {
        "username": json_data.get("username"),
        "session_id": json_data["session_id"],
        "session_length": json_data["session_length"],
        "level_reached": json_data["level_reached"],
        "total_points": json_data.get("total_points", 0)
    }

def plot_to_base64():
    buffer = io.BytesIO()
    plt.savefig(buffer, format='png', bbox_inches='tight')
    buffer.seek(0)
    img_str = base64.b64encode(buffer.getvalue()).decode('utf-8')
    buffer.close()
    plt.close()
    return img_str

# ==========================================
# PROCESO PRINCIPAL (MODIFICADO PARA CUMPLIR REQUISITOS)
# ==========================================
def procesar_datos_y_graficos():
    try:
        # A. PREPARACIÓN
        lista_raw = generar_datos_simulados(150)
        datos_aplanados = [aplanar_json(d) for d in lista_raw]
        df = pd.DataFrame(datos_aplanados)
        df = df.dropna(subset=['username'])

        # Etiqueta: Returning Player (1 si aparece >= 2 veces)
        conteos = df['username'].value_counts()
        df['returning_player'] = df['username'].apply(lambda x: 1 if conteos[x] >= 2 else 0)

        # B. MODELO ML
        features = ['session_length', 'level_reached', 'total_points']
        X = df[features]
        y = df['returning_player']

        # Split y Entrenamiento
        X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
        modelo = DecisionTreeClassifier(max_depth=3)
        modelo.fit(X_train, y_train)
        predicciones = modelo.predict(X_test)

        # --- NUEVO: CÁLCULO DE TODAS LAS MÉTRICAS SOLICITADAS ---
        accuracy = accuracy_score(y_test, predicciones)
        precision = precision_score(y_test, predicciones, zero_division=0)
        recall = recall_score(y_test, predicciones, zero_division=0)

        # --- C. GRÁFICOS ---
        imagenes_base64 = {}

        # Gráfico 1: MATRIZ DE CONFUSIÓN (Método Manual Robusto)
        plt.figure(figsize=(5, 4))
        cm = confusion_matrix(y_test, predicciones)

        # 1. Dibujamos el cuadro de colores
        plt.imshow(cm, interpolation='nearest', cmap='Blues')
        plt.title('Matriz de Confusión')
        plt.colorbar()

        # 2. Configuramos los Ejes (evitando el error de los ticks)
        # Detectamos cuántas clases hay realmente en el test
        clases_reales = len(np.unique(y_test))
        tick_marks = np.arange(clases_reales)
        nombres = ["Abandona", "Vuelve"]

        # Solo ponemos las etiquetas que existan
        plt.xticks(tick_marks, nombres[:clases_reales])
        plt.yticks(tick_marks, nombres[:clases_reales])
        plt.ylabel('Realidad')
        plt.xlabel('Predicción')

        # 3. Escribimos los números dentro de los cuadros
        thresh = cm.max() / 2.
        for i in range(cm.shape[0]):
            for j in range(cm.shape[1]):
                plt.text(j, i, format(cm[i, j], 'd'),
                         horizontalalignment="center",
                         color="white" if cm[i, j] > thresh else "black")

        imagenes_base64['confusion_matrix'] = plot_to_base64()

        # Gráfico 2: Importancia de Variables (Obligatorio según texto)
        importancia = pd.Series(modelo.feature_importances_, index=features)
        plt.figure(figsize=(6, 4))
        importancia.plot(kind='barh', color='purple')
        plt.title('Importancia de Variables')
        imagenes_base64['feature_importance'] = plot_to_base64()

        # Gráfico 3: Histograma (Distribución de datos)
        plt.figure(figsize=(6, 4))
        plt.hist(df['session_length'], bins=10, color='orange', edgecolor='black')
        plt.title('Distribución Duración (Seg)')
        imagenes_base64['hist_distribucion'] = plot_to_base64()

        # D. RETORNO DE DATOS
        resultado = {
            "metrics": {
                "player_count": int(df['username'].nunique()),
                "accuracy": round(accuracy, 2),
                "precision": round(precision, 2), # NUEVO
                "recall": round(recall, 2)        # NUEVO
            },
            "charts": imagenes_base64
        }

        return json.dumps(resultado)

    except Exception as e:
        import traceback
        return json.dumps({"error": str(e)})