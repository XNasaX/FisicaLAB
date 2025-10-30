<div align="center">

# 🧪 **FisicaLAB**
### *Simulador Interactivo de Física en Java (Alpha 3.0)*

<h1 align="center">
  <a href="https://github.com/XNasaX/FisicaLAB">
    <img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&duration=2500&pause=700&color=3AE0D8&center=true&vCenter=true&width=435&lines=FisicaLAB+3.0;Simulador+Interactivo+de+Física;Aprende+Jugando%2C+Juega+Aprendiendo!">
  </a>
</h1>

<p align="center">
  <a href="https://www.java.com"><img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"></a>
  <a href="https://opensource.org/licenses/MIT"><img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge"></a>
  <img src="https://img.shields.io/badge/Estado-Alpha_3.0-red?style=for-the-badge">
</p>


---

<img width="1600" height="896" alt="PRE ALPHA (1)" src="https://github.com/user-attachments/assets/26c4b4ff-845a-4229-a5c3-319b294d4ce3" />

</div>

---
## 📝 Descripción
**FisicaLAB** es un simulador educativo desarrollado en Java para visualizar y experimentar con fenómenos de cinemática de forma interactiva. Ideal para alumnos, docentes y autodidactas.

¡Bienvenido a la **versión 3.0 Alpha**! Esta gran actualización introduce el **Modo Juego**, un completo **sistema de misiones** con 15 desafíos, puntuación de XP y estrellas, junto con un **Modo Aprende** para la exploración libre.

> 💡 “Aprender física nunca había sido tan divertido”.

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
- **JDK** 8 (o superior).
- **NetBeans IDE** 12 (o superior).

### Pasos
1.  Clona este repositorio:
    ```sh
    git clone [https://github.com/XNasaX/FisicaLAB.git](https://github.com/XNasaX/FisicaLAB.git)
    ```
    
2.  Abre **NetBeans IDE**.

3.  Ve a `File -> Open Project...` y selecciona la carpeta del proyecto que acabas de clonar.

4.  Busca el archivo `SimuladorFrame.java` (dentro de `com.mycompany.fisicalab.core`).

5.  Haz clic derecho sobre el archivo y selecciona `Run File`. La ventana se maximizará automáticamente.


---

## 🎮 Novedades v3.0: ¡Modo Juego!
La actualización 3.0 se centra en la gamificación del aprendizaje. Ahora puedes elegir entre dos modos en el menú principal:

### 1. 🎮 Modo Juego
¡Pon a prueba tus conocimientos! Este modo te reta a completar 15 misiones de dificultad creciente.
- **Sistema de Misiones:** 5 misiones para MRU, 5 para Caída Libre y 5 para Tiro Parabólico.
- **Puntuación y Estrellas:** Gana XP y hasta 3 estrellas (⭐⭐⭐) por misión según tu precisión.
- **Dificultad Progresiva:** Las misiones van desde "Fácil" (😊) hasta "Muy Difícil" (😱).
- **Interfaz de Misión:** Cada misión tiene una pantalla de información detallada con objetivos, restricciones y puntuación.

Pon a prueba tus conocimientos con 15 misiones únicas:
- **5 misiones de MRU**
- **5 misiones de Caída Libre**
- **5 misiones de Tiro Parabólico**

Características:
- **Sistema de puntuación XP y estrellas (⭐)**
- **Dificultad progresiva: Fácil → Muy Difícil**
- **Pantalla de misión: Objetivos, condiciones y recompensas**

### 2. 📚 Modo Aprende
El modo clásico de simulación libre. Perfecto para experimentar sin límites.
- **Acceso Directo:** Entra a cualquier simulación (MRU, Caída Libre, Tiro Parabólico).
- **Laboratorio Abierto:** Juega con todos los parámetros como gravedad, velocidad, masa, etc.
- **Fórmulas Clave:** Revisa las fórmulas principales antes de entrar.

---

## ⌨️ Controles de Teclado
Controla las simulaciones directamente desde tu teclado:

| Tecla | Acción Global |
| :--- | :--- |
| **[ ESPACIO ]** | Iniciar / Pausar la simulación |
| **[ R ]** | Reiniciar la simulación |
| **[ V ]** | Mostrar / Ocultar vectores (en MRU y Caída Libre) |

| Tecla | Caída Libre | Tiro Parabólico |
| :--- | :--- | :--- |
| **[ E ]** | Mostrar / Ocultar panel de Energía | - |
| **[ ↑ ] / [ ↓ ]** | - | Ajustar Ángulo (+/- 1°) |
| **[ ← ] / [ → ]** | - | Ajustar Velocidad (+/- 1 m/s) |

---

## 📊 Características Implementadas (v3.0)
- ✅ **NUEVO: Sistema de Modos (Juego/Aprende)**: Elige entre desafíos (Modo Juego) o exploración libre (Modo Aprende).
- ✅ **NUEVO: Sistema de 15 Misiones**: Supera 5 desafíos únicos para MRU, Caída Libre y Tiro Parabólico.
- ✅ **NUEVO: Evaluación de Misiones**: Gana puntos XP y de 1 a 3 estrellas según tu precisión y desempeño.
- ✅ **NUEVO: Interfaz de Misión**: Paneles detallados que explican el objetivo, las restricciones y la recompensa de cada misión.
- ✅ **Motor de simulación física** en tiempo real.
- ✅ **Parámetros Avanzados:** Ajusta la gravedad, masa, velocidad de simulación, y más.
- ✅ **Sistema de Energía (Ep/Ek)**: Visualiza la conservación de la energía en la simulación de Caída Libre.
- ✅ **Controles de Teclado** (KeyBindings) para una simulación interactiva.
- ✅ **Modo Pantalla Completa** y ventana redimensionable.
- ✅ **Interfaz gráfica moderna** y limpia con componentes Swing personalizados.

---

## 🚧 Roadmap (Próximos Pasos)
- 🔷 **Gráficas dinámicas** en tiempo real (posición vs. tiempo, etc.).
- 🔷 **Simulación MRUV** (Movimiento Rectilíneo Uniformemente Variado).
- 🔷 **Tutoriales Interactivos** (activar la tarjeta "Próximamente" del Modo Aprende).
- 🔷 **Exportación de datos** de simulación a CSV.
- 🔷 **Física más avanzada:** Resistencia del aire y Colisiones.
- 🔷 **Presets de Planetas** (para gravedad).

---

## 📜 Licencia
Este proyecto está distribuido bajo la Licencia MIT. Consulta el archivo `LICENSE.txt` en el repositorio para más detalles.

---

## 👨‍💻 Autor y Contacto

<h3>✨ Desarrollado por <strong>NAZA_DR</strong></h3>
<h4>🧠 XNasaX DESIGN — Innovación y Física Interactiva</h4>

<p>
  <a href="https://github.com/XNasaX" target="_blank">
    <img src="https://img.shields.io/badge/GitHub-XNasaX-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub">
  </a>
  <a href="mailto:naza.dr.off@gmail.com" target="_blank">
    <img src="https://img.shields.io/badge/Email-naza.dr.off%40gmail.com-D14836?style=for-the-badge&logo=gmail&logoColor=white" alt="Email">
  </a>
</p>

---

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

*"La física es divertida cuando puedes experimentar con ella interactivamente"*

![Física](https://img.shields.io/badge/Física-Cinemática-blue?style=flat-square)
![Educación](https://img.shields.io/badge/Educación-Interactiva-green?style=flat-square)
![Juego](https://img.shields.io/badge/Juego-Educativo-orange?style=flat-square)

</div>
