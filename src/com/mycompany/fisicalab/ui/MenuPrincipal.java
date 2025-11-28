package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.modos.SeleccionModo;
import com.mycompany.fisicalab.utils.UserManager;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Menú principal de la aplicación con fondo animado
 * Versión 2.5
 */
public class MenuPrincipal extends JPanel {
    
    private SimuladorFrame frame;
    private Timer animacionTimer;
    private List<PapelFlotante> papeles;
    private Random random;
    private UserManager userManager;
    private JLabel userLabel;
    
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
            
            int opcion = random.nextInt(5);
            switch(opcion) {
                case 0: color = new Color(174, 214, 241, 40); break;
                case 1: color = new Color(162, 217, 206, 40); break;
                case 2: color = new Color(250, 219, 216, 40); break;
                case 3: color = new Color(249, 231, 159, 40); break;
                default: color = new Color(210, 180, 222, 40); break;
            }
        }
        
        public void actualizar(int panelAncho, int panelAlto) {
            x += velocidadX;
            y += velocidadY;
            rotacion += velocidadRotacion;
            
            if (x < -ancho) x = panelAncho;
            if (x > panelAncho) x = -ancho;
            if (y < -alto) y = panelAlto;
            if (y > panelAlto) y = -alto;
        }
        
        public void dibujar(Graphics2D g2d) {
            AffineTransform original = g2d.getTransform();
            g2d.translate(x, y);
            g2d.rotate(Math.toRadians(rotacion));
            
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.fillRect(-ancho/2 + 2, -alto/2 + 2, ancho, alto);
            
            g2d.setColor(color);
            g2d.fillRect(-ancho/2, -alto/2, ancho, alto);
            
            g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 80));
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(-ancho/2, -alto/2, ancho, alto);
            
            g2d.setColor(new Color(100, 100, 100, 30));
            for (int i = 0; i < 3; i++) {
                int yLinea = -alto/2 + 10 + i * 8;
                g2d.drawLine(-ancho/2 + 5, yLinea, ancho/2 - 5, yLinea);
            }
            
            g2d.setTransform(original);
        }
    }
    
    public MenuPrincipal(SimuladorFrame frame, UserManager userManager) { // Aceptar UserManager
        this.frame = frame;
        this.random = new Random();
        this.userManager = userManager; // Usar la instancia pasada
        setLayout(new BorderLayout());
        setBackground(new Color(26, 32, 44)); // Fondo azul oscuro
        
        inicializarPapeles(); // Mantener la animación de papeles
        inicializarComponentes();
        iniciarAnimacion(); // Iniciar la animación
        
        if (!userManager.isUserLoggedIn()) {
            mostrarLoginRegister();
        }
        actualizarUsuarioLogueado();
    }
    
    private void mostrarLoginRegister() {
        LoginRegisterDialog dialog = new LoginRegisterDialog(frame, userManager);
        dialog.setVisible(true);
        actualizarUsuarioLogueado();
    }
    
    private void actualizarUsuarioLogueado() {
        if (userManager.isUserLoggedIn()) {
            userLabel.setText("Usuario: " + userManager.getCurrentUser());
        } else {
            userLabel.setText("No logueado");
        }
    }
    
    private void inicializarPapeles() {
        papeles = new ArrayList<>();
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
        // Panel superior para iconos de usuario, ayuda y configuración
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Izquierda: Icono de usuario y nombre
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelUsuario.setOpaque(false);
        JLabel iconoUsuario = new JLabel(cargarIcono("/com/mycompany/fisicalab/recursos/user_icon.png", 40, 40)); // 40x40px
        userLabel = new JLabel("perfil 1");
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setForeground(Color.WHITE);
        panelUsuario.add(iconoUsuario);
        panelUsuario.add(userLabel);
        panelSuperior.add(panelUsuario, BorderLayout.WEST);

        // Derecha: Iconos de ayuda y configuración
        JPanel panelIconosDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelIconosDerecha.setOpaque(false);
        JLabel iconoAyuda = new JLabel(cargarIcono("/com/mycompany/fisicalab/recursos/help_icon.png", 40, 40)); // 40x40px
        JLabel iconoConfig = new JLabel(cargarIcono("/com/mycompany/fisicalab/recursos/settings_icon.png", 40, 40)); // 40x40px
        panelIconosDerecha.add(iconoAyuda);
        panelIconosDerecha.add(iconoConfig);
        panelSuperior.add(panelIconosDerecha, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel central con logo y botones principales
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Logo de FisicaLab (imagen PNG)
        JLabel logoLabel = null;
        try {
            BufferedImage img = ImageIO.read(getClass().getResource("/com/mycompany/fisicalab/recursos/logo.png"));
            logoLabel = new JLabel(new ImageIcon(img.getScaledInstance(400, 200, Image.SCALE_SMOOTH))); // 400x200px
        } catch (IOException e) {
            e.printStackTrace();
            logoLabel = new JLabel("FisicaLAB");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 72));
            logoLabel.setForeground(Color.WHITE);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCentral.add(logoLabel);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 50)));
        
        // Panel de botones principales (Modo Aprende, Modo Juego)
        JPanel panelBotonesPrincipales = new JPanel(new GridLayout(1, 2, 30, 0));
        panelBotonesPrincipales.setOpaque(false);
        panelBotonesPrincipales.setMaximumSize(new Dimension(800, 100));
        
        JButton btnModoAprende = crearBotonMenu("MODO APRENDE", new Color(231, 76, 60));
        btnModoAprende.addActionListener(e -> abrirSeleccionModo("aprende"));
        
        JButton btnModoJuego = crearBotonMenu("MODO JUEGO", new Color(46, 204, 113));
        btnModoJuego.addActionListener(e -> abrirSeleccionModo("juego"));
        
        panelBotonesPrincipales.add(btnModoAprende);
        panelBotonesPrincipales.add(btnModoJuego);
        
        panelCentral.add(panelBotonesPrincipales);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 30)));

        // Panel de botones secundarios (Mejor Puntaje, Logros)
        JPanel panelBotonesSecundarios = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotonesSecundarios.setOpaque(false);
        panelBotonesSecundarios.setMaximumSize(new Dimension(400, 60));

        JButton btnMejorPuntaje = crearBotonMenu("MEJOR PUNTAJE", new Color(0, 173, 239));
        btnMejorPuntaje.setPreferredSize(new Dimension(180, 50));
        btnMejorPuntaje.setFont(new Font("Arial", Font.BOLD, 14));
        btnMejorPuntaje.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Mejor Puntaje (BETA)"));

        JButton btnLogros = crearBotonMenu("LOGROS", new Color(147, 112, 219));
        btnLogros.setPreferredSize(new Dimension(180, 50));
        btnLogros.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogros.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Logros (BETA)"));

        panelBotonesSecundarios.add(btnMejorPuntaje);
        panelBotonesSecundarios.add(btnLogros);

        panelCentral.add(panelBotonesSecundarios);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Panel inferior para "Visita nuestra página" y versión
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Izquierda: Visita nuestra página
        JPanel panelWeb = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelWeb.setOpaque(false);
        JLabel labelVisitaWeb = new JLabel("VISITA NUESTRA PAGINA");
        labelVisitaWeb.setFont(new Font("Arial", Font.PLAIN, 12));
        labelVisitaWeb.setForeground(Color.WHITE);
        JButton btnWeb = crearBotonWeb("LINK DE LA PAGINA", "https://fisica-lab-alpha-web.vercel.app/");
        btnWeb.setPreferredSize(new Dimension(150, 30));
        btnWeb.setFont(new Font("Arial", Font.BOLD, 10));
        panelWeb.add(labelVisitaWeb);
        panelWeb.add(btnWeb);
        panelInferior.add(panelWeb, BorderLayout.WEST);

        // Derecha: Versión
        JLabel labelVersion = new JLabel("BETA: 1.0.0");
        labelVersion.setFont(new Font("Arial", Font.ITALIC, 12));
        labelVersion.setForeground(Color.WHITE);
        JPanel panelVersion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVersion.setOpaque(false);
        panelVersion.add(labelVersion);
        panelInferior.add(panelVersion, BorderLayout.EAST);

        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JButton crearBotonWeb(String texto, String url) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setBackground(new Color(46, 204, 113));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "No se pudo abrir la página web: " + url, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        return boton;
    }
    
    private ImageIcon cargarIcono(String path, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(getClass().getResource(path));
            return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private JLabel crearLogo() {
        return new JLabel(); 
    }
    
    private JButton crearBotonMenu(String texto, Color color) {
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color colorFondo = color;
                if (getModel().isPressed()) {
                    colorFondo = color.darker();
                } else if (getModel().isRollover() && isEnabled()) {
                    colorFondo = color.brighter();
                }
                
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 25, 25);
                
                g2d.setColor(colorFondo);
                g2d.fillRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 25, 25);
                
                GradientPaint brillo = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 50),
                    0, getHeight() / 2, new Color(255, 255, 255, 0)
                );
                g2d.setPaint(brillo);
                g2d.fillRoundRect(0, 0, getWidth() - 6, getHeight() / 2, 25, 25);
                
                g2d.setColor(colorFondo.darker());
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(0, 0, getWidth() - 6, getHeight() - 6, 25, 25);
                
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
        animacionTimer.stop();
        
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
            case "MRUV":
                simulacion = new SimulacionMRUV(frame);
                break;
        }
        
        if (simulacion != null) {
            frame.mostrarSimulacion(simulacion);
        }
    }
    
    private void abrirSeleccionModo(String modo) {
        animacionTimer.stop();
        SeleccionModo seleccion = new SeleccionModo(frame, modo);
        frame.mostrarSimulacion(seleccion);
    }
    
    public void reiniciarAnimacion() {
        if (animacionTimer != null && !animacionTimer.isRunning()) {
            animacionTimer.start();
        }
    }
}
