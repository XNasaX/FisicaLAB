<div align="center">

# 🧪 **FisicaLAB**
### *Simulador Interactivo de Física en Java (Alpha 2.0)*
  
<p align="center">
  <a href="https://www.java.com">
    <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  </a>
  <a href="https://opensource.org/licenses/MIT">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge" alt="Licencia MIT">
  </a>
  <img src="https://img.shields.io/badge/Estado-Alpha_3.0-red?style=for-the-badge" alt="Estado Alpha 2.0">
</p>

<img width="1600" height="896" alt="PRE ALPHA (1)" src="https://github.com/user-attachments/assets/26c4b4ff-845a-4229-a5c3-319b294d4ce3" />

</div>

---
## 📝 Descripción
**FisicaLAB** es un simulador educativo desarrollado en Java para visualizar y experimentar con fenómenos de cinemática de forma interactiva. Ideal para alumnos, docentes y autodidactas.

¡Ahora en su **versión 2.0**! Esta actualización masiva incluye controles de teclado, un sistema de energía, modo de pantalla completa y parámetros de simulación avanzados como la gravedad variable.

---

## 🛠️ Stack Tecnológico
<p align="left">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Java%20Swing-596D70?style=for-the-badge&logo=java&logoColor=white" alt="Java Swing">
  <img src="https://img.shields.io/badge/Apache%20NetBeans-1D2029?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white" alt="Apache NetBeans">
</p>

---

## 🚀 Instalación y Ejecución

Sigue estos pasos para ejecutar el proyecto en tu máquina local.

### Requisitos
- **JDK** 8 (o superior, probado hasta 12).
- **NetBeans IDE** 12 (o superior).

### Pasos
1.  Clona este repositorio:
    ```sh
    git clone [[https://github.com/](https://github.com/)[TU_USUARIO]/[NOMBRE_DEL_REPO].git](https://github.com/XNasaX/FisicaLAB)
    ```
2.  Abre **NetBeans IDE**.
3.  Ve a `File -> Open Project...` y selecciona la carpeta del proyecto que acabas de clonar.
4.  Busca el archivo `SimuladorFrame.java` (dentro de `com.mycompany.fisicalab.core`).
5.  Haz clic derecho sobre el archivo y selecciona `Run File`. La ventana se maximizará automáticamente.

---

## 🎮 Controles de Teclado (Nuevo en v2.0)
Controla las simulaciones directamente desde tu teclado:

| Tecla | Acción Global |
| :--- | :--- |
| **[ ESPACIO ]** | Iniciar / Pausar la simulación |
| **[ R ]** | Reiniciar la simulación |
| **[ V ]** | Mostrar / Ocultar vectores |

| Tecla | Caída Libre | Tiro Parabólico |
| :--- | :--- | :--- |
| **[ E ]** | Mostrar / Ocultar panel de Energía | - |
| **[ ↑ ] / [ ↓ ]** | - | Ajustar Ángulo (+/- 1°) |
| **[ ← ] / [ → ]** | - | Ajustar Velocidad (+/- 1 m/s) |

---

## ⚙️ Parámetros Globales (Nuevo en v2.0)
Puedes modificar estos valores en tiempo real:

-   **Gravedad Variable:** Ajusta la gravedad de **$0.1$ a $20.0 m/s^2$**. ¡Prueba simulaciones en la Luna ($1.6 m/s^2$), Marte ($3.7 m/s^2$) o Júpiter ($24.8 m/s^2$)!
-   **Velocidad de Simulación:** Controla el "delta time" (10-100 ms) para ver simulaciones en cámara lenta (51-100 ms) o rápida (10-29 ms).

---

## 🔬 Simulaciones Disponibles (Actualizadas v2.0)

### Movimiento Rectilíneo Uniforme (MRU)
-   **Velocidad:** $1$ a $30 m/s$.
-   **Posición Inicial (x₀):** Define el punto de partida.
-   **Distancia Objetivo:** Fija una meta visual (10-200 m).
-   **Modo Infinito:** Activa un bucle continuo.
-   **Visual:** Barra de progreso y líneas de meta.

### Caída Libre
-   **Altura Inicial:** Rango ampliado (10-200 m).
-   **Velocidad Inicial (v₀):** Lanza el objeto hacia arriba (v > 0) o hacia abajo (v < 0).
-   **Masa:** Configura la masa del objeto (0.1-10 kg).
-   **NUEVO Sistema de Energía:** Activa con la tecla **[E]** para ver la conservación de Energía Potencial ($E_p$) y Cinética ($E_k$) en tiempo real.

### Tiro Parabólico
-   **Velocidad Inicial y Ángulo:** Ajustables con sliders o con las **flechas del teclado**.
-   **Altura Inicial (h₀):** Lanza el proyectil desde una altura configurable.
-   **Información Extendida:** Calcula y muestra el alcance máximo, altura máxima y tiempo de vuelo.

---

## 📊 Características

### Implementadas (v2.0)
-   ✅ **Modo Pantalla Completa** y ventana redimensionable.
-   ✅ **Controles de Teclado** (KeyBindings) para una simulación interactiva.
-   ✅ **Parámetros de Gravedad y Velocidad de Simulación** globales.
-   ✅ **Sistema de Energía (Ep/Ek)** en Caída Libre.
-   ✅ **Parámetros extendidos** en todas las simulaciones (posición inicial, velocidad inicial, masa, etc.).
-   ✅ **Controles de UI mejorados:** Sliders, campos de texto para valores exactos y paneles con scroll.
-   ✅ **Información extendida** (alcance, altura máx, etc.).
-   ✅ **Motor de simulación física** en tiempo real.
-   ✅ **Interfaz gráfica moderna** con Swing.

### 🚧 Roadmap (Próximos Pasos v3.0)
-   🔷 **Gráficas dinámicas** en tiempo real (posición vs. tiempo, etc.).
-   🔷 **Simulación MRUV** completa.
-   🔷 **Exportación de datos** a CSV.
-   🔷 **Modo Comparación** (split screen).
-   🔷 **Presets de Planetas** (para gravedad).
-   🔷 **Física más avanzada:** Resistencia del aire y Colisiones.

---

## 📜 Licencia
Este proyecto está distribuido bajo la Licencia MIT. Consulta el archivo `LICENSE` en el repositorio para más detalles.

---

## 👨‍💻 Autor y Contacto

**[NAZA_DR - XNasaX DESING]**

<p>
  <a href="https://github.com/XNasaX" target="_blank">
    <img src="https://img.shields.io/badge/GitHub-XNasaX-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub">
  </a>
  <a href="mailto:naza.dr.off@gmail.com" target="_blank">
    <img src="https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white" alt="Email">
  </a>
</p>

### 🙏 Agradecimientos
- Inspirado por los principios de física de Newton.
- Gracias a la comunidad educativa por el feedback.
- Agradecimiento especial a profesores de física que validaron el contenido.
- PROFE DE CALCULO - I LOVE YOU FOR ORIUNDO.

---

## ⭐ ¡Apoya el Proyecto!

Si te gusta este proyecto:
- ⭐ Dale una **estrella** en GitHub.
- 🔄 **Compártelo** con otros educadores.
- 🐛 **Reporta bugs** o sugiere mejoras en la sección de *Issues*.
- 🤝 **Contribuye** con código.

<div align="center">

*"La física es divertida cuando puedes experimentar con ella interactivamente"*

<br>

![Física](https://img.shields.io/badge/Física-Cinemática-blue?style=flat-square)
![Educación](https://img.shields.io/badge/Educación-Interactiva-green?style=flat-square)
![Juego](https://img.shields.io/badge/Juego-Educativo-orange?style=flat-square)

</div>
