package com.mycompany.fisicalab.core;

import com.mycompany.fisicalab.utils.UIHelper;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel; // Necesario para que Escenario extienda JPanel

/**
 * Escenario para la simulación de Estática.
 * Permite visualizar objetos y fuerzas en equilibrio.
 */
public class EscenarioEstatica extends Escenario {

    public List<Fuerza> fuerzas; // Cambiado a público para acceso desde SimulacionEstatica
    private List<ObjetoEstatico> objetos;
    private Point puntoApoyo; // Punto de referencia para torques

    public EscenarioEstatica(int ancho, int alto) {
        super(ancho, alto);
        fuerzas = new ArrayList<>();
        objetos = new ArrayList<>();
        puntoApoyo = new Point(ancho / 2, alto / 2); // Punto de apoyo inicial en el centro
    }

    public void addFuerza(Fuerza fuerza) {
        this.fuerzas.add(fuerza);
    }

    public void addObjeto(ObjetoEstatico objeto) {
        this.objetos.add(objeto);
    }

    public void setPuntoApoyo(Point p) {
        this.puntoApoyo = p;
    }

    @Override
    public void actualizar() {
        // En estática, no hay actualización de movimiento en el tiempo.
        // Aquí se podría recalcular el equilibrio si los parámetros cambian.
    }

    @Override
    protected void dibujar(Graphics2D g2d) {
        // Dibujar fondo simple
        g2d.setColor(new Color(240, 248, 255)); // Alice Blue
        g2d.fillRect(0, 0, ancho, alto);

        dibujarCuadricula(g2d, 50);
        dibujarEjes(g2d, ancho / 2, alto / 2); // Ejes en el centro

        // Dibujar punto de apoyo
        g2d.setColor(Color.BLUE);
        g2d.fillOval(puntoApoyo.x - 5, puntoApoyo.y - 5, 10, 10);
        g2d.drawString("Punto de Apoyo", puntoApoyo.x + 10, puntoApoyo.y + 5);

        // Dibujar objetos estáticos
        for (ObjetoEstatico obj : objetos) {
            obj.dibujar(g2d, escalaPixeles);
        }

        // Dibujar fuerzas
        for (Fuerza f : fuerzas) {
            f.dibujar(g2d, escalaPixeles);
        }

        // Mostrar información de equilibrio
        String[] info = calcularEquilibrio();
        dibujarInfo(g2d, info, 20, 20);
    }

    private String[] calcularEquilibrio() {
        double sumFx = 0;
        double sumFy = 0;
        double sumTorque = 0;

        for (Fuerza f : fuerzas) {
            sumFx += f.getComponenteX();
            sumFy += f.getComponenteY();
            sumTorque += f.calcularTorque(puntoApoyo, escalaPixeles);
        }

        // Considerar el peso de los objetos si se implementa
        // for (ObjetoEstatico obj : objetos) {
        //     sumFy -= obj.getMasa() * MotorSimulacion.getGravedad(); // Peso hacia abajo
        //     // Calcular torque del peso
        // }

        List<String> infoList = new ArrayList<>();
        infoList.add("ESTÁTICA:");
        infoList.add(String.format("ΣFx = %.2f N", sumFx));
        infoList.add(String.format("ΣFy = %.2f N", sumFy));
        infoList.add(String.format("Στ = %.2f Nm", sumTorque));

        boolean enEquilibrio = Math.abs(sumFx) < 0.01 && Math.abs(sumFy) < 0.01 && Math.abs(sumTorque) < 0.01;
        infoList.add("Estado: " + (enEquilibrio ? "En Equilibrio ✅" : "Desequilibrio ❌"));

        return infoList.toArray(new String[0]);
    }

    // Clases internas para Estática (pueden ser clases separadas si se vuelven complejas)

    /**
     * Representa una fuerza aplicada en el escenario.
     */
    public static class Fuerza {
        private double magnitud; // N
        private double angulo;   // grados
        private Point puntoAplicacion; // píxeles
        private Color color;

        public Fuerza(double magnitud, double angulo, Point puntoAplicacion, Color color) {
            this.magnitud = magnitud;
            this.angulo = angulo;
            this.puntoAplicacion = puntoAplicacion;
            this.color = color;
        }

        public double getComponenteX() {
            return magnitud * Math.cos(Math.toRadians(angulo));
        }

        public double getComponenteY() {
            return magnitud * Math.sin(Math.toRadians(angulo));
        }

        public double calcularTorque(Point puntoReferencia, double escalaPixeles) {
            // Vector de posición desde el punto de referencia al punto de aplicación
            double rx = (puntoAplicacion.x - puntoReferencia.x) / escalaPixeles;
            double ry = (puntoAplicacion.y - puntoReferencia.y) / escalaPixeles;

            // Componentes de la fuerza
            double fx = getComponenteX();
            double fy = getComponenteY();

            // Torque = r x F = rx*Fy - ry*Fx
            return rx * fy - ry * fx;
        }

        public void dibujar(Graphics2D g2d, double escalaPixeles) {
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(2));

            int x1 = puntoAplicacion.x;
            int y1 = puntoAplicacion.y;

            // Longitud de la flecha proporcional a la magnitud
            int longitudPx = (int) (magnitud * 5); // Ajustar factor de escala visual

            // Calcular punto final de la flecha
            int x2 = x1 + (int) (longitudPx * Math.cos(Math.toRadians(angulo)));
            int y2 = y1 - (int) (longitudPx * Math.sin(Math.toRadians(angulo))); // Y invertido en Swing

            UIHelper.dibujarFlecha(g2d, x1, y1, x2, y2);

            // Etiqueta de magnitud
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            g2d.drawString(String.format("%.1f N", magnitud), x2 + 5, y2 - 5);
        }
    }

    /**
     * Representa un objeto rígido en el escenario de estática.
     */
    public static class ObjetoEstatico {
        private double masa; // kg
        private Rectangle bounds; // en píxeles
        private Color color;

        public ObjetoEstatico(double masa, Rectangle bounds, Color color) {
            this.masa = masa;
            this.bounds = bounds;
            this.color = color;
        }

        public double getMasa() {
            return masa;
        }

        public void dibujar(Graphics2D g2d, double escalaPixeles) {
            g2d.setColor(color);
            g2d.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            g2d.setColor(color.darker());
            g2d.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
            
            // Opcional: dibujar centro de masa
            // g2d.setColor(Color.BLACK);
            // g2d.fillOval(bounds.x + bounds.width/2 - 2, bounds.y + bounds.height/2 - 2, 4, 4);
        }
    }
}
