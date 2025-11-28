package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.EscenarioMRUV;
import com.mycompany.fisicalab.core.MotorSimulacion;
import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * Simulación de Movimiento Rectilíneo Uniformemente Variado (MRUV) v1.0 (Beta)
 * Permite explorar el movimiento con aceleración constante.
 */
public class SimulacionMRUV extends JPanel {
    
    private SimuladorFrame frame;
    private EscenarioMRUV escenario;
    private MotorSimulacion motor;
    
    private JButton btnIniciar, btnPausar, btnReiniciar, btnVolver;
    private JSlider sliderVelocidadInicial, sliderAceleracion, sliderVelocidadSim;
    private JLabel labelVelocidadInicial, labelAceleracion, labelVelocidadSim;
    private JTextField txtPosicionInicial, txtTiempoObjetivo;
    private JCheckBox chkMostrarVectores, chkModoInfinito;
    
    private double velocidadInicial = 0.0;
    private double aceleracion = 1.0;
    private double posicionInicial = 0.0;
    private double tiempoObjetivo = 0.0;
    private int velocidadSimulacion = 30;
    private boolean mostrarVectores = true;
    private boolean modoInfinito = false;
    
    public SimulacionMRUV(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(UIHelper.COLOR_FONDO);
        
        motor = new MotorSimulacion(velocidadSimulacion);
        
        inicializarComponentes();
        configurarTeclado();
    }
    
    private void configurarTeclado() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "iniciarPausar");
        actionMap.put("iniciarPausar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!btnIniciar.isEnabled() && btnPausar.isEnabled()) {
                    pausarSimulacion();
                } else if (btnIniciar.isEnabled()) {
                    iniciarSimulacion();
                }
            }
        });
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "reiniciar");
        actionMap.put("reiniciar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarSimulacion();
            }
        });
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0), "toggleVectores");
        actionMap.put("toggleVectores", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chkMostrarVectores.setSelected(!chkMostrarVectores.isSelected());
            }
        });
    }
    
    private void inicializarComponentes() {
        // ===== PANEL SUPERIOR (TITULO) =====
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        
        JLabel titulo = new JLabel("Movimiento Rectilíneo Uniformemente Variado v1.0 (Beta)");
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
        
        // Velocidad Inicial
        agregarControl(panelControlesInterno, "Velocidad Inicial (m/s):");
        sliderVelocidadInicial = UIHelper.crearSlider(-15, 15, 0);
        sliderVelocidadInicial.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderVelocidadInicial.addChangeListener(e -> {
            velocidadInicial = sliderVelocidadInicial.getValue();
            labelVelocidadInicial.setText(String.format("v0 = %.1f m/s", velocidadInicial));
        });
        labelVelocidadInicial = new JLabel(String.format("v0 = %.1f m/s", velocidadInicial));
        labelVelocidadInicial.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelVelocidadInicial.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelControlesInterno.add(sliderVelocidadInicial);
        panelControlesInterno.add(labelVelocidadInicial);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Aceleración
        agregarControl(panelControlesInterno, "Aceleración (m/s²):");
        sliderAceleracion = UIHelper.crearSlider(-5, 5, 1);
        sliderAceleracion.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderAceleracion.addChangeListener(e -> {
            aceleracion = sliderAceleracion.getValue();
            labelAceleracion.setText(String.format("a = %.1f m/s²", aceleracion));
        });
        labelAceleracion = new JLabel(String.format("a = %.1f m/s²", aceleracion));
        labelAceleracion.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelAceleracion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelControlesInterno.add(sliderAceleracion);
        panelControlesInterno.add(labelAceleracion);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Posición Inicial
        agregarControl(panelControlesInterno, "Posición Inicial (m):");
        txtPosicionInicial = UIHelper.crearCampoTexto("0.0");
        txtPosicionInicial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPosicionInicial.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPosicionInicial.addActionListener(e -> actualizarPosicionInicial());
        panelControlesInterno.add(txtPosicionInicial);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Tiempo Objetivo
        agregarControl(panelControlesInterno, "Tiempo Objetivo (s, 0=sin límite):");
        txtTiempoObjetivo = UIHelper.crearCampoTexto("0.0");
        txtTiempoObjetivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtTiempoObjetivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtTiempoObjetivo.addActionListener(e -> actualizarTiempoObjetivo());
        panelControlesInterno.add(txtTiempoObjetivo);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Velocidad Simulación
        agregarControl(panelControlesInterno, "Velocidad Simulación:");
        sliderVelocidadSim = new JSlider(10, 100, 30);
        sliderVelocidadSim.setMajorTickSpacing(30);
        sliderVelocidadSim.setMinorTickSpacing(10);
        sliderVelocidadSim.setPaintTicks(true);
        sliderVelocidadSim.setFont(new Font("Arial", Font.PLAIN, 9));
        sliderVelocidadSim.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderVelocidadSim.addChangeListener(e -> {
            velocidadSimulacion = sliderVelocidadSim.getValue();
            String velocidadStr = velocidadSimulacion < 30 ? "Rápida" : 
                                 velocidadSimulacion > 50 ? "Lenta" : "Normal";
            labelVelocidadSim.setText(String.format("%d ms (%s)", velocidadSimulacion, velocidadStr));
        });
        labelVelocidadSim = new JLabel(String.format("%d ms (Normal)", velocidadSimulacion));
        labelVelocidadSim.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelVelocidadSim.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelControlesInterno.add(sliderVelocidadSim);
        panelControlesInterno.add(labelVelocidadSim);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Opciones
        agregarControl(panelControlesInterno, "Opciones:");
        chkMostrarVectores = new JCheckBox("Mostrar vectores (V)", true);
        chkMostrarVectores.setFont(new Font("Arial", Font.PLAIN, 12));
        chkMostrarVectores.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkMostrarVectores.addActionListener(e -> {
            mostrarVectores = chkMostrarVectores.isSelected();
        });
        
        chkModoInfinito = new JCheckBox("Modo infinito (bucle)", false);
        chkModoInfinito.setFont(new Font("Arial", Font.PLAIN, 12));
        chkModoInfinito.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkModoInfinito.addActionListener(e -> {
            modoInfinito = chkModoInfinito.isSelected();
        });
        
        panelControlesInterno.add(chkMostrarVectores);
        panelControlesInterno.add(chkModoInfinito);
        panelControlesInterno.add(Box.createVerticalGlue());
        
        JScrollPane scrollControles = new JScrollPane(panelControlesInterno);
        scrollControles.setPreferredSize(new Dimension(320, 600));
        scrollControles.setBorder(null);
        
        // ===== PANEL CENTRAL (SIMULACIÓN) =====
        escenario = new EscenarioMRUV(900, 500);
        JPanel panelSimulacion = new JPanel(new BorderLayout());
        panelSimulacion.setBackground(Color.WHITE);
        panelSimulacion.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        panelSimulacion.add(escenario, BorderLayout.CENTER);
        
        // ===== PANEL INFERIOR (BOTONES + INFO) =====
        JPanel panelInferior = new JPanel(new BorderLayout(10, 0));
        panelInferior.setOpaque(false);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        
        // Botones a la izquierda
        JPanel panelBotonesIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelBotonesIzq.setOpaque(false);
        
        btnVolver = crearBoton("Volver al Menú", UIHelper.COLOR_PELIGRO, 160, 50);
        btnVolver.addActionListener(e -> frame.mostrarMenuPrincipal());
        panelBotonesIzq.add(btnVolver);
        
        // Botones en el centro
        JPanel panelBotonesCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotonesCentro.setOpaque(false);
        
        btnIniciar = crearBoton("Iniciar (SPACE)", UIHelper.COLOR_EXITO, 160, 50);
        btnIniciar.addActionListener(e -> iniciarSimulacion());
        
        btnPausar = crearBoton("Pausar", UIHelper.COLOR_ADVERTENCIA, 130, 50);
        btnPausar.setEnabled(false);
        btnPausar.addActionListener(e -> pausarSimulacion());
        
        btnReiniciar = crearBoton("Reiniciar (R)", UIHelper.COLOR_SECUNDARIO, 150, 50);
        btnReiniciar.addActionListener(e -> reiniciarSimulacion());
        
        panelBotonesCentro.add(btnIniciar);
        panelBotonesCentro.add(btnPausar);
        panelBotonesCentro.add(btnReiniciar);
        
        // Panel de información a la derecha
        JPanel panelInfo = crearPanelInfo();
        
        panelInferior.add(panelBotonesIzq, BorderLayout.WEST);
        panelInferior.add(panelBotonesCentro, BorderLayout.CENTER);
        panelInferior.add(panelInfo, BorderLayout.EAST);
        
        // ===== AGREGAR TODO AL PANEL PRINCIPAL =====
        add(panelTitulo, BorderLayout.NORTH);
        add(scrollControles, BorderLayout.WEST);
        add(panelSimulacion, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
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
    
    private JPanel crearPanelInfo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        JLabel labelTitulo = new JLabel("□ CONTROLES:");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(labelTitulo);
        
        String[] controles = {
            "ESPACIO: Iniciar/Pausar",
            "R: Reiniciar",
            "V: Toggle vectores"
        };
        
        for (String control : controles) {
            JLabel label = new JLabel(control);
            label.setFont(new Font("Arial", Font.PLAIN, 10));
            panel.add(label);
        }
        
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JLabel labelMruv = new JLabel("MRUV:");
        labelMruv.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(labelMruv);
        
        String[] formulas = {
            "• Aceleración constante",
            "• Velocidad variable",
            "• x = x0 + v0*t + 0.5*a*t²",
            "• v = v0 + a*t"
        };
        
        for (String formula : formulas) {
            JLabel label = new JLabel(formula);
            label.setFont(new Font("Arial", Font.PLAIN, 10));
            panel.add(label);
        }
        
        return panel;
    }
    
    private void actualizarPosicionInicial() {
        try {
            double valor = Double.parseDouble(txtPosicionInicial.getText());
            posicionInicial = valor;
            txtPosicionInicial.setText(String.format("%.1f", posicionInicial));
        } catch (NumberFormatException ex) {
            txtPosicionInicial.setText("0.0");
            posicionInicial = 0.0;
            JOptionPane.showMessageDialog(this, "Entrada inválida para Posición Inicial. Se estableció a 0.0.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarTiempoObjetivo() {
        try {
            double valor = Double.parseDouble(txtTiempoObjetivo.getText());
            if (valor < 0) valor = 0;
            tiempoObjetivo = valor;
            txtTiempoObjetivo.setText(String.format("%.1f", tiempoObjetivo));
        } catch (NumberFormatException ex) {
            txtTiempoObjetivo.setText("0.0");
            tiempoObjetivo = 0.0;
            JOptionPane.showMessageDialog(this, "Entrada inválida para Tiempo Objetivo. Se estableció a 0.0.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void iniciarSimulacion() {
        motor = new MotorSimulacion(velocidadSimulacion);
        escenario.setParametros(velocidadInicial, aceleracion, posicionInicial, 
                               tiempoObjetivo, mostrarVectores, modoInfinito);
        escenario.setMotor(motor);
        
        motor.iniciar(e -> {
            escenario.actualizar();
            escenario.repaint();
        });
        
        btnIniciar.setEnabled(false);
        btnPausar.setEnabled(true);
        deshabilitarControles(true);
    }
    
    private void pausarSimulacion() {
        if (motor.isEnEjecucion()) {
            motor.pausar();
            btnPausar.setText("Reanudar");
        } else {
            motor.reanudar();
            btnPausar.setText("Pausar");
        }
    }
    
    private void reiniciarSimulacion() {
        motor.detener();
        motor.reiniciar();
        escenario.reiniciar();
        escenario.repaint();
        
        btnIniciar.setEnabled(true);
        btnPausar.setEnabled(false);
        btnPausar.setText("Pausar");
        deshabilitarControles(false);
    }
    
    private void deshabilitarControles(boolean deshabilitar) {
        sliderVelocidadInicial.setEnabled(!deshabilitar);
        sliderAceleracion.setEnabled(!deshabilitar);
        txtPosicionInicial.setEnabled(!deshabilitar);
        txtTiempoObjetivo.setEnabled(!deshabilitar);
        sliderVelocidadSim.setEnabled(!deshabilitar);
        chkModoInfinito.setEnabled(!deshabilitar);
    }
}
