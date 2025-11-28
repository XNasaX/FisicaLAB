package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.EscenarioEstatica;
import com.mycompany.fisicalab.core.EscenarioEstatica.Fuerza;
import com.mycompany.fisicalab.core.EscenarioEstatica.ObjetoEstatico;
import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulación de Estática v1.0 (Beta)
 * Permite configurar fuerzas y objetos para analizar el equilibrio.
 */
public class SimulacionEstatica extends JPanel {

    private SimuladorFrame frame;
    private EscenarioEstatica escenario;

    private JSlider sliderMagnitud, sliderAngulo;
    private JLabel labelMagnitud, labelAngulo;
    private JButton btnAddFuerza, btnClearFuerzas, btnSetPuntoApoyo, btnVolver;
    private JCheckBox chkMostrarInfo;

    private double magnitudFuerza = 50.0; // N
    private double anguloFuerza = 0.0;   // grados
    private boolean settingPuntoApoyo = false;

    public SimulacionEstatica(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(UIHelper.COLOR_FONDO);

        escenario = new EscenarioEstatica(900, 600); // Tamaño del escenario
        
        inicializarComponentes();
        setupEscenarioListeners();
    }

    private void inicializarComponentes() {
        // ===== PANEL SUPERIOR (TITULO) =====
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));

        JLabel titulo = new JLabel("Estática v1.0 (Beta)");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(UIHelper.COLOR_PRIMARIO);
        panelTitulo.add(titulo, BorderLayout.WEST);

        // ===== PANEL IZQUIERDO (CONTROLES) =====
        JPanel panelControlesInterno = new JPanel();
        panelControlesInterno.setLayout(new BoxLayout(panelControlesInterno, BoxLayout.Y_AXIS));
        panelControlesInterno.setBackground(Color.WHITE);
        panelControlesInterno.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Magnitud de la fuerza
        agregarControl(panelControlesInterno, "Magnitud (N):");
        sliderMagnitud = UIHelper.crearSlider(0, 200, (int) magnitudFuerza);
        sliderMagnitud.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderMagnitud.addChangeListener(e -> {
            magnitudFuerza = sliderMagnitud.getValue();
            labelMagnitud.setText(String.format("F = %.1f N", magnitudFuerza));
            escenario.repaint();
        });
        labelMagnitud = new JLabel(String.format("F = %.1f N", magnitudFuerza));
        labelMagnitud.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelMagnitud.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelControlesInterno.add(sliderMagnitud);
        panelControlesInterno.add(labelMagnitud);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));

        // Ángulo de la fuerza
        agregarControl(panelControlesInterno, "Ángulo (grados):");
        sliderAngulo = UIHelper.crearSlider(0, 360, (int) anguloFuerza);
        sliderAngulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderAngulo.addChangeListener(e -> {
            anguloFuerza = sliderAngulo.getValue();
            labelAngulo.setText(String.format("θ = %.0f°", anguloFuerza));
            escenario.repaint();
        });
        labelAngulo = new JLabel(String.format("θ = %.0f°", anguloFuerza));
        labelAngulo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelAngulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelControlesInterno.add(sliderAngulo);
        panelControlesInterno.add(labelAngulo);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));

        // Botón para añadir fuerza
        btnAddFuerza = crearBoton("Añadir Fuerza (Click en Escenario)", UIHelper.COLOR_PRIMARIO, 280, 50);
        btnAddFuerza.addActionListener(e -> {
            settingPuntoApoyo = false;
            UIHelper.mostrarInfo(this, "Haz click en el escenario para aplicar la fuerza.");
        });
        panelControlesInterno.add(btnAddFuerza);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 10)));

        // Botón para limpiar fuerzas
        btnClearFuerzas = crearBoton("Limpiar Fuerzas", UIHelper.COLOR_PELIGRO, 280, 50);
        btnClearFuerzas.addActionListener(e -> {
            escenario.fuerzas.clear();
            escenario.repaint();
        });
        panelControlesInterno.add(btnClearFuerzas);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 10)));

        // Botón para establecer punto de apoyo
        btnSetPuntoApoyo = crearBoton("Establecer Punto de Apoyo", new Color(155, 89, 182), 280, 50);
        btnSetPuntoApoyo.addActionListener(e -> {
            settingPuntoApoyo = true;
            UIHelper.mostrarInfo(this, "Haz click en el escenario para establecer el punto de apoyo.");
        });
        panelControlesInterno.add(btnSetPuntoApoyo);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 10)));

        // Checkbox para mostrar/ocultar información
        chkMostrarInfo = new JCheckBox("Mostrar Información de Equilibrio", true);
        chkMostrarInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        chkMostrarInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkMostrarInfo.addActionListener(e -> {
            // La lógica de dibujarInfo ya maneja esto internamente
            escenario.repaint();
        });
        panelControlesInterno.add(chkMostrarInfo);
        panelControlesInterno.add(Box.createVerticalGlue());

        JScrollPane scrollControles = new JScrollPane(panelControlesInterno);
        scrollControles.setPreferredSize(new Dimension(320, 600));
        scrollControles.setBorder(null);

        // ===== PANEL CENTRAL (SIMULACIÓN) =====
        JPanel panelSimulacion = new JPanel(new BorderLayout());
        panelSimulacion.setBackground(Color.WHITE);
        panelSimulacion.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        panelSimulacion.add(escenario, BorderLayout.CENTER);

        // ===== PANEL INFERIOR (BOTONES) =====
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelInferior.setOpaque(false);

        btnVolver = crearBoton("Volver al Menú", UIHelper.COLOR_PELIGRO, 160, 50);
        btnVolver.addActionListener(e -> frame.mostrarMenuPrincipal());
        panelInferior.add(btnVolver);

        add(panelTitulo, BorderLayout.NORTH);
        add(scrollControles, BorderLayout.WEST);
        add(panelSimulacion, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void setupEscenarioListeners() {
        escenario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (settingPuntoApoyo) {
                    escenario.setPuntoApoyo(e.getPoint());
                    settingPuntoApoyo = false;
                    UIHelper.mostrarInfo(SimulacionEstatica.this, "Punto de apoyo establecido en (" + e.getX() + ", " + e.getY() + ")");
                } else {
                    // Añadir fuerza en la posición del click
                    Fuerza nuevaFuerza = new Fuerza(magnitudFuerza, anguloFuerza, e.getPoint(), Color.RED);
                    escenario.addFuerza(nuevaFuerza);
                    UIHelper.mostrarInfo(SimulacionEstatica.this, String.format("Fuerza de %.1f N a %.0f° añadida en (%d, %d)", magnitudFuerza, anguloFuerza, e.getX(), e.getY()));
                }
                escenario.repaint();
            }
        });
    }

    private void agregarControl(JPanel panel, String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private JButton crearBoton(String texto, Color color, int ancho, int alto) {
        JButton boton = UIHelper.crearBotonRedondeado(texto, color);
        boton.setPreferredSize(new Dimension(ancho, alto));
        return boton;
    }
}
