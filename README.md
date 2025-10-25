<div align="center">

# 🧪 **FisicaLAB**
### *Simulador Interactivo de Física en Java*
  
<p align="center">
  <a href="https://www.java.com">
    <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java">
  </a>
  <a href="https://opensource.org/licenses/MIT">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge" alt="Licencia MIT">
  </a>
  <img src="https://img.shields.io/badge/Estado-PRE_Alpha_1.0-red?style=for-the-badge" alt="Estado Alpha">
</p>

<img width="1600" height="896" alt="Banner FisicaLAB" src="https://github.com/user-attachments/assets/e7a26aaf-2e85-4e4b-b192-e821f66abf4e" />

</div>

---

## 📝 Descripción
**FisicaLAB** es un simulador educativo desarrollado en Java para visualizar y experimentar con fenómenos de cinemática de forma interactiva. Ideal para alumnos, docentes y autodidactas.

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
    git clone [https://github.com/](https://github.com/)[TU_USUARIO]/[NOMBRE_DEL_REPO].git
    ```
2.  Abre **NetBeans IDE**.
3.  Ve a `File -> Open Project...` y selecciona la carpeta del proyecto que acabas de clonar.
4.  Busca el archivo `SimuladorFrame.java` (dentro de `com.mycompany.fisicalab.core`).
5.  Haz clic derecho sobre el archivo y selecciona `Run File`.

---

## 🎮 Simulaciones Disponibles

- ✅ **Movimiento Rectilíneo Uniforme (MRU)**
- ✅ **Caída Libre**
- ✅ **Tiro Parabólico**

---

### Movimiento Rectilíneo Uniforme (MRU)
- **Uso:** Ajusta la velocidad con el slider (1-20 m/s).
- **Observa:** El objeto se mueve a velocidad constante.
- **Ecuación:** $x = x_0 + v \cdot t$

### Caída Libre
- **Uso:** Configura la altura inicial (10-100 m) y presiona "Soltar".
- **Observa:** La aceleración gravitacional constante ($9.8 m/s^2$).
- **Ecuaciones:** $y = y_0 - \frac{1}{2}gt^2$ y $v = g \cdot t$

### Tiro Parabólico
- **Uso:** Ajusta la velocidad inicial (5-50 m/s) y el ángulo de lanzamiento (0-90°).
- **Observa:** La trayectoria parabólica. La línea punteada muestra la trayectoria predicha.
- **Análisis:** Componente horizontal (MRU) y vertical (MRUV).

---

## 📊 Características

### Implementadas (v1.0)
- ✅ **Motor de simulación física en tiempo real.**
- ✅ **Interfaz gráfica moderna con Swing.**
- ✅ **Controles interactivos** (sliders, botones).
- ✅ **Visualización de vectores de velocidad.**
- ✅ **Información en tiempo real** (posición, velocidad, tiempo, altura).
- ✅ **Botones con diseño redondeado** y paleta de colores profesional.
- ✅ **Animaciones fluidas (30 FPS).**

### 🚧 Roadmap (Próximas Mejoras)
- 🔷 **MRUV** (Movimiento rectilíneo uniformemente variado).
- 🔷 **Gráficas dinámicas** (exportables a .png).
- 🔷 Migración opcional a **JavaFX** para animaciones más fluidas.
- ⏳ **Sistema de recursos** (imágenes y sonidos).
- ⏳ **Historial de resultados** y exportación a CSV.
- ⏳ **Más simulaciones** (Estática, Leyes de Newton, Energía).
- ⏳ **Sistema de guardado/carga** de configuraciones.

---

## 📁 Estructura del Proyecto
```
com.mycompany.fisicalab/
├── core/
│   ├── SimuladorFrame.java       # Ventana principal y punto de entrada
│   ├── MotorSimulacion.java      # Motor físico y cálculos
│   └── Escenario.java            # Base para escenarios gráficos
├── ui/
│   ├── MenuPrincipal.java        # Menú de inicio
│   ├── SimulacionMRU.java        # Movimiento rectilíneo uniforme
│   ├── SimulacionCaidaLibre.java # Caída libre
│   └── SimulacionTiroParabolico.java # Tiro parabólico
└── utils/
    ├── UIHelper.java             # Utilidades de interfaz
    ├── ArchivoDatos.java         # Persistencia de datos
    ├── Recursos.java             # Gestor de imágenes (placeholder)
    └── Sonido.java               # Gestor de audio (placeholder)
```
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
