package com.mycompany.fisicalab.core;

import javax.swing.Timer;
import java.awt.event.ActionListener;

/**
 * Motor de simulación física
 * Gestiona el tiempo y los cálculos físicos básicos
 */
public class MotorSimulacion {
    
    private Timer timer;
    private double tiempoTranscurrido; // en segundos
    private double deltaTime; // intervalo de tiempo en segundos
    private boolean enEjecucion;
    
    // Constantes físicas (modificables)
    private static double gravedad = 9.8; // m/s²
    
    public MotorSimulacion(int intervaloMs) {
        this.deltaTime = intervaloMs / 1000.0; // convertir ms a segundos
        this.tiempoTranscurrido = 0.0;
        this.enEjecucion = false;
    }
    
    // Getters y setters para gravedad
    public static double getGravedad() {
        return gravedad;
    }
    
    public static void setGravedad(double g) {
        gravedad = g;
    }
    
    public static void resetGravedad() {
        gravedad = 9.8;
    }
    
    public void iniciar(ActionListener actualizacion) {
        if (timer != null) {
            timer.stop();
        }
        
        tiempoTranscurrido = 0.0;
        enEjecucion = true;
        
        timer = new Timer((int)(deltaTime * 1000), e -> {
            if (enEjecucion) {
                tiempoTranscurrido += deltaTime;
                actualizacion.actionPerformed(e);
            }
        });
        timer.start();
    }
    
    public void pausar() {
        enEjecucion = false;
    }
    
    public void reanudar() {
        enEjecucion = true;
    }
    
    public void detener() {
        if (timer != null) {
            timer.stop();
            enEjecucion = false;
        }
    }
    
    public void reiniciar() {
        tiempoTranscurrido = 0.0;
        enEjecucion = false;
    }
    
    // Cálculos de cinemática
    
    /**
     * Calcula posición en MRU: x = x0 + v*t
     */
    public static double calcularPosicionMRU(double posicionInicial, double velocidad, double tiempo) {
        return posicionInicial + velocidad * tiempo;
    }
    
    /**
     * Calcula posición en MRUV: x = x0 + v0*t + (1/2)*a*t²
     */
    public static double calcularPosicionMRUV(double posicionInicial, double velocidadInicial, 
                                               double aceleracion, double tiempo) {
        return posicionInicial + velocidadInicial * tiempo + 0.5 * aceleracion * tiempo * tiempo;
    }
    
    /**
     * Calcula velocidad en MRUV: v = v0 + a*t
     */
    public static double calcularVelocidadMRUV(double velocidadInicial, double aceleracion, double tiempo) {
        return velocidadInicial + aceleracion * tiempo;
    }
    
    /**
     * Calcula componente horizontal en tiro parabólico
     */
    public static double calcularPosicionHorizontal(double x0, double velocidadX, double tiempo) {
        return x0 + velocidadX * tiempo;
    }
    
    /**
     * Calcula componente vertical en tiro parabólico
     */
    public static double calcularPosicionVertical(double y0, double velocidadY, double tiempo) {
        return y0 + velocidadY * tiempo - 0.5 * gravedad * tiempo * tiempo;
    }
    
    // Getters
    public double getTiempoTranscurrido() {
        return tiempoTranscurrido;
    }
    
    public double getDeltaTime() {
        return deltaTime;
    }
    
    public boolean isEnEjecucion() {
        return enEjecucion;
    }
}
