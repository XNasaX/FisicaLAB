package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Menú principal de la aplicación con fondo animado
 * Versión 2.5
 */
public class MenuPrincipal extends JPanel {
    
    private SimuladorFrame frame;
    private Timer animacionTimer;
    private List<PapelFlotante> papeles;
    private Random random;
    
    // Clase interna para papeles flotantes
    private class PapelFlotante {
        double x, y;
        double velocidadX, velocidadY;
        double rotacion, velocidadRotacion;
        int ancho, alto;
        Color color;
        
        public PapelFlotante(int panelAncho, int panelAlto) {
            random = new Random();
            x = random.nextInt(panelAncho);
            y = random.nextInt(panelAlto);
            velocidadX = (random.nextDouble() - 0.5) * 0.5;
            velocidadY = (random.nextDouble() - 0.5) * 0.5;
            rotacion = random.nextDouble() * 360;
            velocidadRotacion = (random.nextDouble() - 0.5) * 0.3;
            ancho = 40 + random.nextInt(20);
            alto = 50 + random.nextInt(20);
            
            // Colores pasteles para los papeles
            int opcion = random.nextInt(5);
            switch(opcion) {
                case 0: color = new Color(174, 214, 241, 40); break; // Azul claro
                case 1: color = new Color(162, 217, 206, 40); break; // Verde agua
                case 2: color = new Color(250, 219, 216, 40); break; // Rosa claro
                case 3: color = new Color(249, 231, 159, 40); break; // Amarillo claro
                default: color = new Color(210, 180, 222, 40); break; // Lila claro
            }
        }
        
        public void actualizar(int panelAncho, int panelAlto) {
            x += velocidadX;
            y += velocidadY;
            rotacion += velocidadRotacion;
            
            // Rebote en bordes
            if (x < -ancho) x = panelAncho;
            if (x > panelAncho) x = -ancho;
            if (y < -alto) y = panelAlto;
            if (y > panelAlto) y = -alto;
        }
        
        public void dibujar(Graphics2D g2d) {
            AffineTransform original = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(Math.toRadians(rotacion));
            
            // Sombra
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.fillRect(-ancho/2 + 2, -alto/2 + 2, ancho, alto);
            
            // Papel
            g2d.setColor(color);
            g2d.fillRect(-ancho/2, -alto/2, ancho, alto);
            
            // Borde
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(-ancho/2, -alto/2, ancho, alto);
            
            // Líneas decorativas (simulando texto)
            g2d.setColor(new Color(100, 100, 100, 30));
            for (int i = 0; i < 3; i++) {
                int yLinea = -alto/2 + 10 + i * 8;
                g2d.drawLine(-ancho/2 + 5, yLinea, ancho/2 - 5, yLinea);
            }
            
            g2d.setTransform(original);
        }
    }
    
    public MenuPrincipal(SimuladorFrame frame) {
        this.frame = frame;
        this.random = new Random();
        setLayout(new BorderLayout());
        setBackground(new Color(236, 240, 241));
        
        inicializarPapeles();
        inicializarComponentes();
        iniciarAnimacion();
    }
    
    private void inicializarPapeles() {
        papeles = new ArrayList<>();
        // Crear entre 15-20 papeles flotantes
        int numPapeles = 15 + random.nextInt(6);
        for (int i = 0; i < numPapeles; i++) {
            papeles.add(new PapelFlotante(1200, 800));
        }
    }
    
    private void iniciarAnimacion() {
        animacionTimer = new Timer(30, e -> {
            for (PapelFlotante papel : papeles) {
                papel.actualizar(getWidth(), getHeight());
            }
            repaint();
        });
        animacionTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Dibujar papeles flotantes
        for (PapelFlotante papel : papeles) {
            papel.dibujar(g2d);
        }
    }
    
    private void inicializarComponentes() {
        // Panel central con logo y botones
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(80, 50, 50, 50));
        
        // Logo/Título (aquí puedes poner tu logo si lo tienes)
        JLabel logo = crearLogo();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitulo = new JLabel("Simulador de Física Interactivo");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitulo.setForeground(new Color(52, 73, 94));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCentral.add(logo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 15)));
        panelCentral.add(subtitulo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(6, 1, 0, 20));
        panelBotones.setOpaque(false);
        panelBotones.setMaximumSize(new Dimension(350, 420));
        
        // Botón MRU
        JButton btnMRU = crearBotonMenu("🏃 Movimiento Rectilíneo Uniforme", UIHelper.COLOR_PRIMARIO);
        btnMRU.addActionListener(e -> abrirSimulacion("MRU"));
        
        // Botón Caída Libre
        JButton btnCaidaLibre = crearBotonMenu("🪂 Caída Libre", UIHelper.COLOR_SECUNDARIO);
        btnCaidaLibre.addActionListener(e -> abrirSimulacion("CAIDA_LIBRE"));
        
        // Botón Tiro Parabólico
        JButton btnTiroParabolico = crearBotonMenu("🎯 Tiro Parabólico", UIHelper.COLOR_EXITO);
        btnTiroParabolico.addActionListener(e -> abrirSimulacion("TIRO_PARABOLICO"));
        
        // NUEVO: Botón Modos de Juego (v3.0)
        JButton btnModos = crearBotonMenu("🎮 Modos de Juego (NUEVO)", new Color(155, 89, 182));
        btnModos.addActionListener(e -> abrirSeleccionModo());
        
        // Botón Historial (deshabilitado por ahora)
        JButton btnHistorial = crearBotonMenu("📊 Historial de Resultados", new Color(149, 165, 166));
        btnHistorial.setEnabled(false);
        
        // Botón Salir
        JButton btnSalir = crearBotonMenu("🚪 Salir", UIHelper.COLOR_PELIGRO);
        btnSalir.addActionListener(e -> {
            animacionTimer.stop();
            System.exit(0);
        });
        
        panelBotones.add(btnMRU);
        panelBotones.add(btnCaidaLibre);
        panelBotones.add(btnTiroParabolico);
        panelBotones.add(btnModos);
        panelBotones.add(btnHistorial);
        panelBotones.add(btnSalir);
        
        panelCentral.add(panelBotones);
        
        // Versión en esquina inferior derecha
        JPanel panelVersion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVersion.setOpaque(false);
        JLabel labelVersion = new JLabel("v2.5 Alpha");
        labelVersion.setFont(new Font("Arial", Font.ITALIC, 12));
        labelVersion.setForeground(new Color(127, 140, 141));
        panelVersion.add(labelVersion);
        
        add(panelCentral, BorderLayout.CENTER);
        add(panelVersion, BorderLayout.SOUTH);
    }
    
    private JLabel crearLogo() {
        // Logo de FisicaLab (texto estilizado)
        JLabel logo = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                
                // Sombra del texto
                g2d.setFont(new Font("Arial", Font.BOLD, 72));
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.drawString("FisicaLAB", 5, 75);
                
                // Texto principal con degradado
                GradientPaint gradient = new GradientPaint(
                    0, 0, UIHelper.COLOR_PRIMARIO,
                    200, 0, UIHelper.COLOR_SECUNDARIO
                );
                g2d.setPaint(gradient);
                g2d.drawString("FisicaLAB", 0, 70);
                
                // Decoración de átomo pequeño
                g2d.setColor(UIHelper.COLOR_EXITO);
                g2d.setStroke(new BasicStroke(2));
                int atomX = 380;
                int atomY = 35;
                g2d.drawOval(atomX - 15, atomY - 15, 30, 30);
                g2d.fillOval(atomX - 4, atomY - 4, 8, 8);
                
                // Órbitas
                g2d.drawOval(atomX - 20, atomY - 10, 40, 20);
                g2d.drawOval(atomX - 10, atomY - 20, 20, 40);
            }
        };
        
        logo.setPreferredSize(new Dimension(400, 80));
        return logo;
    }
    
    private JButton crearBotonMenu(String texto, Color color) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Efecto hover
                Color colorFondo = color;
                if (getModel().isPressed()) {
                    colorFondo = color.darker();
                } else if (getModel().isRollover() && isEnabled()) {
                    colorFondo = color.brighter();
                }
                
                // Sombra
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 25, 25);
                
                // Fondo del botón
                g2d.setColor(colorFondo);
                g2d.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 25, 25);
                
                // Brillo superior
                GradientPaint brillo = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 50),
                    0, getHeight() / 2, new Color(255, 255, 255, 0)
                );
                g2d.setPaint(brillo);
                g2d.fillRoundRect(0, 0, getWidth() - 6, getHeight() / 2, 25, 25);
                
                // Borde
                g2d.setColor(colorFondo.darker());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 25, 25);
                
                // Texto
                g2d.setColor(isEnabled() ? Color.WHITE : new Color(200, 200, 200));
                g2d.setFont(getFont());
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
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(350, 60));
        
        return boton;
    }
    
    private void abrirSimulacion(String tipo) {
        animacionTimer.stop(); // Detener animación al salir del menú
        
        JPanel simulacion = null;
        
        switch (tipo) {
            case "MRU":
                simulacion = new SimulacionMRU(frame);
                break;
            case "CAIDA_LIBRE":
                simulacion = new SimulacionCaidaLibre(frame);
                break;
            case "TIRO_PARABOLICO":
                simulacion = new SimulacionTiroParabolico(frame);
                break;
        }
        
        if (simulacion != null) {
            frame.mostrarSimulacion(simulacion);
        }
    }
    
    private void abrirSeleccionModo() {
        animacionTimer.stop();
        com.mycompany.fisicalab.modos.SeleccionModo seleccion = 
            new com.mycompany.fisicalab.modos.SeleccionModo(frame);
        frame.mostrarSimulacion(seleccion);
    }
    
    // Reiniciar animación cuando se vuelve al menú
    public void reiniciarAnimacion() {
        if (animacionTimer != null && !animacionTimer.isRunning()) {
            animacionTimer.start();
        }
    }
}
