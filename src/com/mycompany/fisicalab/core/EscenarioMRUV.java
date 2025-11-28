package com.mycompany.fisicalab.core;

import com.mycompany.fisicalab.utils.UIHelper;
import java.awt.*;
import java.util.Random;
import javax.swing.JPanel;

/**
 * Escenario para la simulación de Movimiento Rectilíneo Uniformemente Variado (MRUV).
 */
public class EscenarioMRUV extends Escenario {
    
    private MotorSimulacion motorLocal;
    private double posicionX, velocidadActual, aceleracionActual, tiempoTotal;
    private double x0, v0, a, tiempoLimite;
    private boolean mostrarVectores, modoInfinito;
    private int formaObjeto; // 0: círculo, 1: cuadrado, 2: triángulo
    private Color colorObjeto;
    private Random random;
    
    public EscenarioMRUV(int ancho, int alto) {
        super(ancho, alto);
        random = new Random();
        seleccionarEstiloAleatorio();
        reiniciar();
    }
    
    private void seleccionarEstiloAleatorio() {
        formaObjeto = random.nextInt(3); // 0, 1 o 2
        colorObjeto = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
    
    public void setMotor(MotorSimulacion motor) {
        this.motorLocal = motor;
    }
    
    public void setParametros(double velInicial, double acel, double posInicial, 
                              double tiempo, boolean vectores, boolean infinito) {
        this.v0 = velInicial;
        this.a = acel;
        this.x0 = posInicial;
        this.tiempoLimite = tiempo;
        this.mostrarVectores = vectores;
        this.modoInfinito = infinito;
    }
    
    public void reiniciar() {
        posicionX = x0;
        velocidadActual = v0;
        aceleracionActual = a;
        tiempoTotal = 0;
        seleccionarEstiloAleatorio(); // Nuevo estilo al reiniciar
    }
    
    @Override
    public void actualizar() {
        if (motorLocal == null) return;
        
        tiempoTotal = motorLocal.getTiempoTranscurrido();
        posicionX = MotorSimulacion.calcularPosicionMRUV(x0, v0, a, tiempoTotal);
        velocidadActual = MotorSimulacion.calcularVelocidadMRUV(v0, a, tiempoTotal);
        
        if (tiempoLimite > 0 && tiempoTotal >= tiempoLimite) {
            motorLocal.detener();
        }
        
        if (modoInfinito && (metrosAPixeles(posicionX - x0) > ancho - 100 || metrosAPixeles(posicionX - x0) < -50)) {
            motorLocal.reiniciar();
            reiniciar();
        }
    }
    
    @Override
    protected void dibujar(Graphics2D g2d) {
        // Dibujar fondo con degradado de cielo y suelo
        dibujarFondo(g2d, alto - 80, 
                     new Color(173, 216, 230), // Azul claro para el cielo
                     new Color(135, 206, 235), // Azul cielo para el cielo
                     new Color(144, 238, 144)); // Verde claro para el suelo
        
        dibujarCuadricula(g2d, 50);
        
        int pisoY = alto - 80;
        g2d.setColor(new Color(52, 73, 94));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(0, pisoY, ancho, pisoY);
        
        int inicioX = 50 + metrosAPixeles(x0);
        g2d.setColor(new Color(46, 204, 113));
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                     BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
        g2d.drawLine(inicioX, pisoY - 100, inicioX, pisoY);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("INICIO", inicioX - 20, pisoY - 110);
        
        int objetoX = 50 + metrosAPixeles(posicionX);
        int objetoY = pisoY - 25;
        
        dibujarObjeto(g2d, objetoX, objetoY, 15, colorObjeto, formaObjeto); // Usa el color y forma aleatorios
        
        if (mostrarVectores) {
            // Vector de velocidad
            g2d.setColor(new Color(39, 174, 96));
            g2d.setStroke(new BasicStroke(3));
            int longitudFlechaVel = (int)(velocidadActual * 10);
            UIHelper.dibujarFlecha(g2d, objetoX, objetoY, objetoX + longitudFlechaVel, objetoY);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("v = " + String.format("%.1f", velocidadActual) + " m/s", 
                          objetoX + longitudFlechaVel + 5, objetoY - 5);

            // Vector de aceleración
            g2d.setColor(new Color(231, 76, 60));
            g2d.setStroke(new BasicStroke(2));
            int longitudFlechaAcel = (int)(aceleracionActual * 10);
            UIHelper.dibujarFlecha(g2d, objetoX, objetoY + 20, objetoX + longitudFlechaAcel, objetoY + 20);
            g2d.drawString("a = " + String.format("%.1f", aceleracionActual) + " m/s²", 
                          objetoX + longitudFlechaAcel + 5, objetoY + 15);
        }
        
        g2d.setColor(new Color(41, 128, 185, 100));
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                     BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
        g2d.drawLine(inicioX, pisoY - 25, objetoX, pisoY - 25);
        
        String estadoStr = (tiempoLimite > 0 && tiempoTotal >= tiempoLimite) ? " TIEMPO LIMITE" :
                          modoInfinito ? " MODO INFINITO" : " EN MOVIMIENTO";
        
        String[] info = {
            "Tiempo: " + String.format("%.2f s", tiempoTotal) + estadoStr,
            "Posicion: " + String.format("%.2f m", posicionX),
            "Velocidad: " + String.format("%.2f m/s", velocidadActual),
            "Aceleracion: " + String.format("%.2f m/s²", aceleracionActual) + " (constante)"
        };
        dibujarInfo(g2d, info, 20, 20);
    }
}
