package com.mycompany.fisicalab.modos;

import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;

/**
 * Panel de selección entre Modo Juego y Modo Aprende
 * Versión 3.0
 */
public class SeleccionModo extends JPanel {
    
    private SimuladorFrame frame;
    
    public SeleccionModo(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(UIHelper.COLOR_FONDO);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(80, 50, 50, 50));
        
        // Título
        JLabel titulo = new JLabel("Selecciona tu Modo de Juego");
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(UIHelper.COLOR_PRIMARIO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitulo = new JLabel("¿Cómo quieres aprender física hoy?");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitulo.setForeground(new Color(52, 73, 94));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCentral.add(titulo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCentral.add(subtitulo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 60)));
        
        // Panel con las dos opciones lado a lado
        JPanel panelOpciones = new JPanel(new GridLayout(1, 2, 40, 0));
        panelOpciones.setOpaque(false);
        panelOpciones.setMaximumSize(new Dimension(900, 400));
        
        // Tarjeta Modo Juego
        JPanel tarjetaJuego = crearTarjetaModo(
            "🎮 MODO JUEGO",
            "Desafíos y Misiones",
            new String[]{
                "✓ 15 misiones emocionantes",
                "✓ Sistema de puntuación",
                "✓ Gana estrellas ⭐⭐⭐",
                "✓ Desbloquea logros",
                "✓ Compite contra ti mismo"
            },
            UIHelper.COLOR_EXITO,
            "juego"
        );
        
        // Tarjeta Modo Aprende
        JPanel tarjetaModoAprende = crearTarjetaModo(
            "📚 MODO APRENDE",
            "Exploración Libre",
            new String[]{
                "✓ Simulaciones sin límites",
                "✓ Tutoriales interactivos",
                "✓ Experimentos guiados",
                "✓ Gráficas en tiempo real",
                "✓ Exporta tus datos"
            },
            UIHelper.COLOR_SECUNDARIO,
            "aprende"
        );
        
        panelOpciones.add(tarjetaJuego);
        panelOpciones.add(tarjetaModoAprende);
        
        panelCentral.add(panelOpciones);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Botón volver
        JButton btnVolver = UIHelper.crearBotonRedondeado("← Volver al Menú Principal", 
                                                           new Color(149, 165, 166));
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.setPreferredSize(new Dimension(280, 50));
        btnVolver.addActionListener(e -> frame.mostrarMenuPrincipal());
        
        panelCentral.add(btnVolver);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Versión en esquina
        JPanel panelVersion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVersion.setOpaque(false);
        JLabel labelVersion = new JLabel("v3.0 Alpha");
        labelVersion.setFont(new Font("Arial", Font.ITALIC, 12));
        labelVersion.setForeground(new Color(127, 140, 141));
        panelVersion.add(labelVersion);
        
        add(panelVersion, BorderLayout.SOUTH);
    }
    
    private JPanel crearTarjetaModo(String titulo, String subtitulo, String[] caracteristicas,
                                    Color colorAccento, String modo) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(colorAccento, 3),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Título
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(colorAccento);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitulo = new JLabel(subtitulo);
        lblSubtitulo.setFont(new Font("Arial", Font.ITALIC, 14));
        lblSubtitulo.setForeground(new Color(127, 140, 141));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 5)));
        tarjeta.add(lblSubtitulo);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Separador
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        tarjeta.add(separador);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Características
        for (String caracteristica : caracteristicas) {
            JLabel lblCaract = new JLabel(caracteristica);
            lblCaract.setFont(new Font("Arial", Font.PLAIN, 14));
            lblCaract.setAlignmentX(Component.LEFT_ALIGNMENT);
            tarjeta.add(lblCaract);
            tarjeta.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        
        tarjeta.add(Box.createVerticalGlue());
        
        // Botón de selección
        JButton btnSeleccionar = new JButton("SELECCIONAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color colorFondo = colorAccento;
                if (getModel().isPressed()) {
                    colorFondo = colorAccento.darker();
                } else if (getModel().isRollover()) {
                    colorFondo = colorAccento.brighter();
                }
                
                g2d.setColor(colorFondo);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };
        
        btnSeleccionar.setFocusPainted(false);
        btnSeleccionar.setBorderPainted(false);
        btnSeleccionar.setContentAreaFilled(false);
        btnSeleccionar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSeleccionar.setPreferredSize(new Dimension(200, 50));
        btnSeleccionar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnSeleccionar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnSeleccionar.addActionListener(e -> seleccionarModo(modo));
        
        tarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        tarjeta.add(btnSeleccionar);
        
        return tarjeta;
    }
    
    private void seleccionarModo(String modo) {
        if (modo.equals("juego")) {
            ModoJuego modoJuego = new ModoJuego(frame);
            frame.mostrarSimulacion(modoJuego);
        } else if (modo.equals("aprende")) {
            ModoAprende modoAprende = new ModoAprende(frame);
            frame.mostrarSimulacion(modoAprende);
        }
    }
}