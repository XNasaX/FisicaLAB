package com.mycompany.fisicalab.modos;

import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.ui.*;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;

/**
 * Modo Aprende - Exploración libre con tutoriales
 * Versión 3.0
 */
public class ModoAprende extends JPanel {
    
    private SimuladorFrame frame;
    
    public ModoAprende(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(UIHelper.COLOR_FONDO);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        
        JLabel titulo = new JLabel("📚 MODO APRENDE - Exploración Libre");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(UIHelper.COLOR_SECUNDARIO);
        
        panelSuperior.add(titulo, BorderLayout.WEST);
        
        // Panel central con opciones
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JLabel subtitulo = new JLabel("Selecciona una simulación para experimentar sin límites");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitulo.setForeground(new Color(52, 73, 94));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCentral.add(subtitulo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Grid de simulaciones
        JPanel gridSimulaciones = new JPanel(new GridLayout(2, 2, 20, 20));
        gridSimulaciones.setOpaque(false);
        gridSimulaciones.setMaximumSize(new Dimension(900, 500));
        
        // Tarjeta MRU
        JPanel tarjetaMRU = crearTarjetaSimulacion(
            "🏃 MRU",
            "Movimiento Rectilíneo Uniforme",
            new String[]{
                "• Velocidad constante",
                "• Sin aceleración",
                "• Fórmula: x = x₀ + v·t"
            },
            UIHelper.COLOR_PRIMARIO,
            "MRU"
        );
        
        // Tarjeta Caída Libre
        JPanel tarjetaCaida = crearTarjetaSimulacion(
            "🪂 Caída Libre",
            "Movimiento bajo gravedad",
            new String[]{
                "• Aceleración constante (g)",
                "• Energía potencial y cinética",
                "• Fórmula: y = h₀ + v₀·t - ½g·t²"
            },
            UIHelper.COLOR_SECUNDARIO,
            "CAIDA_LIBRE"
        );
        
        // Tarjeta Tiro Parabólico
        JPanel tarjetaTiro = crearTarjetaSimulacion(
            "🎯 Tiro Parabólico",
            "Movimiento en dos dimensiones",
            new String[]{
                "• Componentes vₓ y vᵧ",
                "• Trayectoria parabólica",
                "• Alcance y altura máxima"
            },
            UIHelper.COLOR_EXITO,
            "TIRO_PARABOLICO"
        );
        
        // Tarjeta Tutoriales (próximamente)
        JPanel tarjetaTutoriales = crearTarjetaSimulacion(
            "📖 Tutoriales",
            "Aprende paso a paso",
            new String[]{
                "• Lecciones interactivas",
                "• Experimentos guiados",
                "• Biblioteca de física"
            },
            new Color(155, 89, 182),
            "TUTORIALES"
        );
        
        gridSimulaciones.add(tarjetaMRU);
        gridSimulaciones.add(tarjetaCaida);
        gridSimulaciones.add(tarjetaTiro);
        gridSimulaciones.add(tarjetaTutoriales);
        
        panelCentral.add(gridSimulaciones);
        
        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelInferior.setOpaque(false);
        
        JButton btnVolver = UIHelper.crearBotonRedondeado("← Volver", UIHelper.COLOR_PELIGRO);
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.addActionListener(e -> {
            SeleccionModo seleccion = new SeleccionModo(frame);
            frame.mostrarSimulacion(seleccion);
        });
        
        panelInferior.add(btnVolver);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearTarjetaSimulacion(String titulo, String descripcion, 
                                          String[] caracteristicas, Color color, String tipo) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Icono y título
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(color);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblDesc = new JLabel(descripcion);
        lblDesc.setFont(new Font("Arial", Font.ITALIC, 12));
        lblDesc.setForeground(new Color(127, 140, 141));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 5)));
        tarjeta.add(lblDesc);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Separador
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        tarjeta.add(sep);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Características
        for (String caract : caracteristicas) {
            JLabel lblCaract = new JLabel(caract);
            lblCaract.setFont(new Font("Arial", Font.PLAIN, 11));
            lblCaract.setAlignmentX(Component.LEFT_ALIGNMENT);
            tarjeta.add(lblCaract);
            tarjeta.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        tarjeta.add(Box.createVerticalGlue());
        
        // Botón
        JButton btnAbrir = new JButton(tipo.equals("TUTORIALES") ? "Próximamente" : "EXPLORAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color colorFondo = isEnabled() ? color : new Color(200, 200, 200);
                if (getModel().isPressed() && isEnabled()) {
                    colorFondo = color.darker();
                } else if (getModel().isRollover() && isEnabled()) {
                    colorFondo = color.brighter();
                }
                
                g2d.setColor(colorFondo);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };
        
        btnAbrir.setFocusPainted(false);
        btnAbrir.setBorderPainted(false);
        btnAbrir.setContentAreaFilled(false);
        btnAbrir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAbrir.setPreferredSize(new Dimension(150, 35));
        btnAbrir.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        btnAbrir.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        if (tipo.equals("TUTORIALES")) {
            btnAbrir.setEnabled(false);
        } else {
            btnAbrir.addActionListener(e -> abrirSimulacion(tipo));
        }
        
        tarjeta.add(Box.createRigidArea(new Dimension(0, 10)));
        tarjeta.add(btnAbrir);
        
        return tarjeta;
    }
    
    private void abrirSimulacion(String tipo) {
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
}