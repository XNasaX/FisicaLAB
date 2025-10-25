package com.mycompany.fisicalab.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Utilidades para crear componentes UI con estilo moderno
 */
public class UIHelper {
    
    // Paleta de colores
    public static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    public static final Color COLOR_SECUNDARIO = new Color(52, 152, 219);
    public static final Color COLOR_EXITO = new Color(39, 174, 96);
    public static final Color COLOR_PELIGRO = new Color(231, 76, 60);
    public static final Color COLOR_ADVERTENCIA = new Color(243, 156, 18);
    public static final Color COLOR_FONDO = new Color(236, 240, 241);
    public static final Color COLOR_TEXTO = new Color(44, 62, 80);
    
    /**
     * Crea un botón con estilo redondeado
     */
    public static JButton crearBotonRedondeado(String texto, Color colorFondo) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo
                if (getModel().isPressed()) {
                    g2d.setColor(colorFondo.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(colorFondo.brighter());
                } else {
                    g2d.setColor(colorFondo);
                }
                
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Texto
                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };
        
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(150, 45));
        
        return boton;
    }
    
    /**
     * Crea un panel con bordes redondeados
     */
    public static JPanel crearPanelRedondeado(Color colorFondo) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2d.setColor(colorFondo);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
                
                g2d.dispose();
            }
        };
        
        panel.setOpaque(false);
        return panel;
    }
    
    /**
     * Crea un campo de texto con estilo
     */
    public static JTextField crearCampoTexto(String placeholder) {
        JTextField campo = new JTextField(placeholder);
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return campo;
    }
    
    /**
     * Crea una etiqueta con estilo de título
     */
    public static JLabel crearTitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(COLOR_TEXTO);
        return label;
    }
    
    /**
     * Crea una etiqueta con estilo de subtítulo
     */
    public static JLabel crearSubtitulo(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(COLOR_TEXTO);
        return label;
    }
    
    /**
     * Crea un separador visual
     */
    public static JSeparator crearSeparador() {
        JSeparator separador = new JSeparator();
        separador.setForeground(new Color(189, 195, 199));
        return separador;
    }
    
    /**
     * Crea un slider con estilo
     */
    public static JSlider crearSlider(int min, int max, int inicial) {
        JSlider slider = new JSlider(min, max, inicial);
        slider.setMajorTickSpacing((max - min) / 4);
        slider.setMinorTickSpacing((max - min) / 20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setFont(new Font("Arial", Font.PLAIN, 10));
        return slider;
    }
    
    /**
     * Muestra un diálogo de error
     */
    public static void mostrarError(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Muestra un diálogo de información
     */
    public static void mostrarInfo(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Formatea un número con decimales
     */
    public static String formatearDecimal(double valor, int decimales) {
        return String.format("%." + decimales + "f", valor);
    }
}