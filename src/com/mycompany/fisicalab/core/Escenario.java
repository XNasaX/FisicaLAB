package com.mycompany.fisicalab.core;

import javax.swing.*;
import java.awt.*;

/**
 * Clase base para todos los escenarios de simulación
 * Proporciona la estructura común para dibujar y actualizar
 */
public abstract class Escenario extends JPanel {
    
    protected MotorSimulacion motor;
    protected int ancho;
    protected int alto;
    protected double escalaPixeles; // píxeles por metro
    
    public Escenario(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.escalaPixeles = 50.0; // 50 píxeles = 1 metro por defecto
        
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(new Color(240, 248, 255)); // Alice Blue
        setDoubleBuffered(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Activar antialiasing para mejor calidad visual
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        dibujar(g2d);
    }
    
    /**
     * Método abstracto para que cada simulación dibuje su contenido
     */
    protected abstract void dibujar(Graphics2D g2d);
    
    /**
     * Actualiza el estado de la simulación
     */
    public abstract void actualizar();
    
    /**
     * Convierte metros a píxeles
     */
    protected int metrosAPixeles(double metros) {
        return (int)(metros * escalaPixeles);
    }
    
    /**
     * Convierte píxeles a metros
     */
    protected double pixelesAMetros(int pixeles) {
        return pixeles / escalaPixeles;
    }
    
    /**
     * Dibuja una cuadrícula de referencia
     */
    protected void dibujarCuadricula(Graphics2D g2d, int espaciado) {
        g2d.setColor(new Color(200, 200, 200, 100));
        g2d.setStroke(new BasicStroke(1));
        
        // Líneas verticales
        for (int x = 0; x < ancho; x += espaciado) {
            g2d.drawLine(x, 0, x, alto);
        }
        
        // Líneas horizontales
        for (int y = 0; y < alto; y += espaciado) {
            g2d.drawLine(0, y, ancho, y);
        }
    }
    
    /**
     * Dibuja ejes coordenados
     */
    protected void dibujarEjes(Graphics2D g2d, int origenX, int origenY) {
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        
        // Eje X
        g2d.drawLine(0, origenY, ancho, origenY);
        
        // Eje Y
        g2d.drawLine(origenX, 0, origenX, alto);
        
        // Flechas
        int tamFlecha = 10;
        // Flecha eje X
        g2d.drawLine(ancho, origenY, ancho - tamFlecha, origenY - tamFlecha/2);
        g2d.drawLine(ancho, origenY, ancho - tamFlecha, origenY + tamFlecha/2);
        
        // Flecha eje Y
        g2d.drawLine(origenX, 0, origenX - tamFlecha/2, tamFlecha);
        g2d.drawLine(origenX, 0, origenX + tamFlecha/2, tamFlecha);
        
        // Etiquetas
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("X", ancho - 20, origenY - 10);
        g2d.drawString("Y", origenX + 10, 15);
    }
    
    /**
     * Dibuja un objeto circular (móvil)
     */
    protected void dibujarObjeto(Graphics2D g2d, int x, int y, int radio, Color color) {
        // Sombra
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(x - radio + 2, y - radio + 2, radio * 2, radio * 2);
        
        // Objeto principal
        g2d.setColor(color);
        g2d.fillOval(x - radio, y - radio, radio * 2, radio * 2);
        
        // Borde
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x - radio, y - radio, radio * 2, radio * 2);
    }
    
    /**
     * Dibuja información de texto
     */
    protected void dibujarInfo(Graphics2D g2d, String[] lineas, int x, int y) {
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        // Fondo semi-transparente
        FontMetrics fm = g2d.getFontMetrics();
        int anchoMax = 0;
        for (String linea : lineas) {
            anchoMax = Math.max(anchoMax, fm.stringWidth(linea));
        }
        
        int padding = 10;
        g2d.setColor(new Color(255, 255, 255, 220));
        g2d.fillRoundRect(x - padding, y - padding, 
                         anchoMax + 2*padding, 
                         lineas.length * fm.getHeight() + padding, 
                         10, 10);
        
        // Borde
        g2d.setColor(new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(x - padding, y - padding, 
                         anchoMax + 2*padding, 
                         lineas.length * fm.getHeight() + padding, 
                         10, 10);
        
        // Texto
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < lineas.length; i++) {
            g2d.drawString(lineas[i], x, y + i * fm.getHeight() + fm.getAscent());
        }
    }
}