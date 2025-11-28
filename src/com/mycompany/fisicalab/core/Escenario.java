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
     * Dibuja un objeto con forma y color personalizables.
     * @param g2d Contexto gráfico 2D.
     * @param x Coordenada X central del objeto.
     * @param y Coordenada Y central del objeto.
     * @param tamano Tamaño del objeto (radio para círculo, mitad de lado para cuadrado/triángulo).
     * @param color Color principal del objeto.
     * @param forma Tipo de forma (0: círculo, 1: cuadrado, 2: triángulo).
     */
    protected void dibujarObjeto(Graphics2D g2d, int x, int y, int tamano, Color color, int forma) {
        // Sombra
        g2d.setColor(new Color(0, 0, 0, 50));
        Shape sombra = null;
        switch (forma) {
            case 0: // Círculo
                sombra = new java.awt.geom.Ellipse2D.Double(x - tamano + 2, y - tamano + 2, tamano * 2, tamano * 2);
                break;
            case 1: // Cuadrado
                sombra = new java.awt.Rectangle(x - tamano + 2, y - tamano + 2, tamano * 2, tamano * 2);
                break;
            case 2: // Triángulo
                int[] xPointsSombra = {x + 2, x - tamano + 2, x + tamano + 2};
                int[] yPointsSombra = {y - tamano + 2, y + tamano + 2, y + tamano + 2};
                sombra = new Polygon(xPointsSombra, yPointsSombra, 3);
                break;
        }
        if (sombra != null) {
            g2d.fill(sombra);
        }

        // Objeto principal
        g2d.setColor(color);
        Shape objeto = null;
        switch (forma) {
            case 0: // Círculo
                objeto = new java.awt.geom.Ellipse2D.Double(x - tamano, y - tamano, tamano * 2, tamano * 2);
                break;
            case 1: // Cuadrado
                objeto = new java.awt.Rectangle(x - tamano, y - tamano, tamano * 2, tamano * 2);
                break;
            case 2: // Triángulo
                int[] xPoints = {x, x - tamano, x + tamano};
                int[] yPoints = {y - tamano, y + tamano, y + tamano};
                objeto = new Polygon(xPoints, yPoints, 3);
                break;
        }
        if (objeto != null) {
            g2d.fill(objeto);
        }

        // Borde
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2));
        if (objeto != null) {
            g2d.draw(objeto);
        }
    }

    /**
     * Dibuja un fondo con un degradado de cielo y un suelo.
     * @param g2d Contexto gráfico 2D.
     * @param pisoY Coordenada Y donde comienza el suelo.
     * @param colorCielo1 Color superior del cielo.
     * @param colorCielo2 Color inferior del cielo.
     * @param colorSuelo Color del suelo.
     */
    protected void dibujarFondo(Graphics2D g2d, int pisoY, Color colorCielo1, Color colorCielo2, Color colorSuelo) {
        // Cielo con degradado
        GradientPaint cieloGradient = new GradientPaint(0, 0, colorCielo1, 0, pisoY, colorCielo2);
        g2d.setPaint(cieloGradient);
        g2d.fillRect(0, 0, ancho, pisoY);

        // Suelo
        g2d.setColor(colorSuelo);
        g2d.fillRect(0, pisoY, ancho, alto - pisoY);
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
