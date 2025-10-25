<div align="center">

# 🧪 **FisicaLAB**
### *Simulador Interactivo de Física en Java*
  
📌 *Versión Alpha+ 1.0 | Proyecto 100% Java (NetBeans)*

<!-- Reemplaza la ruta con tu logo o imagen -->
![Logo del Proyecto](<img width="1600" height="896" alt="Diseño sin título (2)" src="https://github.com/user-attachments/assets/9fd19431-3812-4923-b5af-6ff8c24fc9ef" />)

</div>

---

# Descripción
**FisicaLAB** es un simulador educativo desarrollado en Java para visualizar y experimentar con fenómenos de cinemática de forma interactiva. Ideal para alumnos, docentes y autodidactas.

---

# Simulaciones disponibles
- ✅ **Movimiento Rectilíneo Uniforme (MRU)**  
- ✅ **Caída Libre**  
- ✅ **Tiro Parabólico**

---

# Requisitos
- **JDK** 8 o superior  
- **NetBeans IDE** 12 o superior  
- **Librerías externas:** No requiere (solo API estándar de Java)

---

# Instalación y ejecución
```bash
git clone https://github.com/tu-usuario/FisicaLAB.git
Abre NetBeans

File > Open Project → selecciona la carpeta FisicaLAB

Ejecuta con Run ▶

Ejemplo de código (Tiro Parabólico)
java
Copiar código
double gravedad = 9.8;            // g (m/s^2)
double velocidad = 20.0;          // v0 (m/s)
double angulo = Math.toRadians(45); // θ en radianes

double vx = velocidad * Math.cos(angulo);
double vy = velocidad * Math.sin(angulo);

double tiempoVuelo = (2 * vy) / gravedad;
double alcance = (Math.pow(velocidad, 2) * Math.sin(2 * angulo)) / gravedad;
double alturaMax = (vy * vy) / (2 * gravedad);
Interfaz y funcionamiento
UI con Swing (formularios y controles: sliders, botones y campos numéricos).

Animaciones controladas por javax.swing.Timer.

Visualización en tiempo real de posición, velocidad (Vx, Vy), tiempo y altura máxima.

Próximas mejoras
🔷 MRUV (Movimiento rectilíneo uniformemente variado)

🔷 Gráficas dinámicas (exportables)

🔷 Migración opcional a JavaFX para animaciones más fluidas

Contribuir
Fork

git checkout -b feature/tu-feature

git commit -m "Descripción"

git push origin feature/tu-feature

Abre un Pull Request

Licencia
Este proyecto está bajo la Licencia MIT.
(Reemplaza [AÑO] [TU NOMBRE] con los tuyos)

sql
Copiar código
MIT License

Copyright (c) [AÑO] [TU NOMBRE]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
<div align="center"> ⭐ *Si te gusta el proyecto, dale una estrella en GitHub* **"La física se aprende experimentando."** </div> ```
