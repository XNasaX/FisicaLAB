package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.Escenario;
import com.mycompany.fisicalab.core.MotorSimulacion;
import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Simulación de Caída Libre v2.6
 * Nueva interfaz reorganizada
 */
public class SimulacionCaidaLibre extends JPanel {
    
    private SimuladorFrame frame;
    private EscenarioCaidaLibre escenario;
    private MotorSimulacion motor;
    
    private JButton btnIniciar, btnPausar, btnReiniciar, btnVolver;
    private JSlider sliderAltura, sliderGravedad, sliderVelocidadSim, sliderMasa;
    private JLabel labelAltura, labelGravedad, labelVelocidadSim, labelMasa;
    private JTextField txtVelocidadInicial;
    private JCheckBox chkMostrarVectores, chkMostrarEnergia;
    
    private double alturaInicial = 50.0;
    private double velocidadInicial = 0.0;
    private double gravedad = 9.8;
    private double masa = 1.0;
    private int velocidadSimulacion = 30;
    private boolean mostrarVectores = true;
    private boolean mostrarEnergia = false;
    
    public SimulacionCaidaLibre(SimuladorFrame frame) {
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
        
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "toggleEnergia");
        actionMap.put("toggleEnergia", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chkMostrarEnergia.setSelected(!chkMostrarEnergia.isSelected());
            }
        });
    }
    
    private void inicializarComponentes() {
        // ===== PANEL SUPERIOR (TÍTULO) =====
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        
        JLabel titulo = new JLabel("Caída Libre v2.0");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(UIHelper.COLOR_SECUNDARIO);
        panelTitulo.add(titulo, BorderLayout.WEST);
        
        // ===== PANEL IZQUIERDO (CONTROLES) =====
        JPanel panelControlesInterno = new JPanel();
        panelControlesInterno.setLayout(new BoxLayout(panelControlesInterno, BoxLayout.Y_AXIS));
        panelControlesInterno.setBackground(Color.WHITE);
        panelControlesInterno.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Altura Inicial
        agregarControl(panelControlesInterno, "Altura Inicial (m):");
        sliderAltura = UIHelper.crearSlider(10, 200, 50);
        sliderAltura.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderAltura.addChangeListener(e -> {
            alturaInicial = sliderAltura.getValue();
            labelAltura.setText(String.format("h₀ = %.1f m", alturaInicial));
        });
        labelAltura = new JLabel(String.format("h₀ = %.1f m", alturaInicial));
        labelAltura.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelAltura.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelControlesInterno.add(sliderAltura);
        panelControlesInterno.add(labelAltura);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Velocidad Inicial
        agregarControl(panelControlesInterno, "Velocidad Inicial (m/s):");
        txtVelocidadInicial = UIHelper.crearCampoTexto("0.0");
        txtVelocidadInicial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtVelocidadInicial.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtVelocidadInicial.setToolTipText("Negativo = hacia abajo, Positivo = hacia arriba");
        txtVelocidadInicial.addActionListener(e -> actualizarVelocidadInicial());
        JLabel labelInfoVel = new JLabel("(-: abajo, +: arriba)");
        labelInfoVel.setFont(new Font("Arial", Font.ITALIC, 10));
        labelInfoVel.setForeground(Color.GRAY);
        labelInfoVel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelControlesInterno.add(txtVelocidadInicial);
        panelControlesInterno.add(labelInfoVel);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Masa
        agregarControl(panelControlesInterno, "Masa del objeto (kg):");
        sliderMasa = new JSlider(1, 100, 10);
        sliderMasa.setMajorTickSpacing(25);
        sliderMasa.setMinorTickSpacing(5);
        sliderMasa.setPaintTicks(true);
        sliderMasa.setFont(new Font("Arial", Font.PLAIN, 9));
        sliderMasa.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderMasa.addChangeListener(e -> {
            masa = sliderMasa.getValue() / 10.0;
            labelMasa.setText(String.format("m = %.1f kg", masa));
        });
        labelMasa = new JLabel(String.format("m = %.1f kg", masa));
        labelMasa.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelMasa.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelControlesInterno.add(sliderMasa);
        panelControlesInterno.add(labelMasa);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Gravedad
        agregarControl(panelControlesInterno, "Gravedad (m/s²):");
        sliderGravedad = new JSlider(1, 200, 98);
        sliderGravedad.setMajorTickSpacing(50);
        sliderGravedad.setMinorTickSpacing(10);
        sliderGravedad.setPaintTicks(true);
        sliderGravedad.setFont(new Font("Arial", Font.PLAIN, 9));
        sliderGravedad.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderGravedad.addChangeListener(e -> {
            gravedad = sliderGravedad.getValue() / 10.0;
            labelGravedad.setText(String.format("g = %.1f m/s²", gravedad));
            MotorSimulacion.setGravedad(gravedad);
        });
        labelGravedad = new JLabel(String.format("g = %.1f m/s²", gravedad));
        labelGravedad.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelGravedad.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel labelInfoGrav = new JLabel("(Tierra=9.8, Luna=1.6, Marte=3.7)");
        labelInfoGrav.setFont(new Font("Arial", Font.ITALIC, 9));
        labelInfoGrav.setForeground(Color.GRAY);
        labelInfoGrav.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelControlesInterno.add(sliderGravedad);
        panelControlesInterno.add(labelGravedad);
        panelControlesInterno.add(labelInfoGrav);
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
        
        chkMostrarEnergia = new JCheckBox("Mostrar energía (E)", false);
        chkMostrarEnergia.setFont(new Font("Arial", Font.PLAIN, 12));
        chkMostrarEnergia.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkMostrarEnergia.addActionListener(e -> {
            mostrarEnergia = chkMostrarEnergia.isSelected();
        });
        
        panelControlesInterno.add(chkMostrarVectores);
        panelControlesInterno.add(chkMostrarEnergia);
        panelControlesInterno.add(Box.createVerticalGlue());
        
        JScrollPane scrollControles = new JScrollPane(panelControlesInterno);
        scrollControles.setPreferredSize(new Dimension(320, 600));
        scrollControles.setBorder(null);
        
        // ===== PANEL CENTRAL (SIMULACIÓN) =====
        escenario = new EscenarioCaidaLibre(900, 500);
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
        btnVolver.addActionListener(e -> {
            MotorSimulacion.resetGravedad();
            frame.mostrarMenuPrincipal();
        });
        panelBotonesIzq.add(btnVolver);
        
        // Botones en el centro
        JPanel panelBotonesCentro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotonesCentro.setOpaque(false);
        
        btnIniciar = crearBoton("Soltar (SPACE)", UIHelper.COLOR_EXITO, 160, 50);
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
            "ESPACIO: Soltar/Pausar",
            "R: Reiniciar",
            "V: Toggle vectores",
            "E: Toggle energía"
        };
        
        for (String control : controles) {
            JLabel label = new JLabel(control);
            label.setFont(new Font("Arial", Font.PLAIN, 10));
            panel.add(label);
        }
        
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        JLabel labelFormulas = new JLabel("□ CAÍDA LIBRE:");
        labelFormulas.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(labelFormulas);
        
        String[] formulas = {
            "y = h₀ + v₀·t - ½g·t²",
            "v = v₀ - g·t",
            "Eₚ = m·g·h",
            "Eₖ = ½m·v²"
        };
        
        for (String formula : formulas) {
            JLabel label = new JLabel(formula);
            label.setFont(new Font("Arial", Font.PLAIN, 10));
            panel.add(label);
        }
        
        return panel;
    }
    
    private void actualizarVelocidadInicial() {
        try {
            double valor = Double.parseDouble(txtVelocidadInicial.getText());
            velocidadInicial = valor;
            txtVelocidadInicial.setText(String.format("%.1f", velocidadInicial));
        } catch (NumberFormatException ex) {
            txtVelocidadInicial.setText("0.0");
            velocidadInicial = 0.0;
        }
    }
    
    private void iniciarSimulacion() {
        motor = new MotorSimulacion(velocidadSimulacion);
        escenario.setParametros(alturaInicial, velocidadInicial, masa, mostrarVectores, mostrarEnergia);
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
        sliderAltura.setEnabled(!deshabilitar);
        txtVelocidadInicial.setEnabled(!deshabilitar);
        sliderMasa.setEnabled(!deshabilitar);
        sliderGravedad.setEnabled(!deshabilitar);
        sliderVelocidadSim.setEnabled(!deshabilitar);
    }
    
    // Clase interna EscenarioCaidaLibre
    private class EscenarioCaidaLibre extends Escenario {
        
        private MotorSimulacion motorLocal;
        private double posicionY, velocidadY, altura, v0, m, tiempoTotal;
        private boolean enSuelo, mostrarVectores, mostrarEnergia;
        
        public EscenarioCaidaLibre(int ancho, int alto) {
            super(ancho, alto);
            this.escalaPixeles = 2.5;
            reiniciar();
        }
        
        public void setMotor(MotorSimulacion motor) {
            this.motorLocal = motor;
        }
        
        public void setParametros(double h, double v, double masa, boolean vectores, boolean energia) {
            this.altura = h;
            this.v0 = v;
            this.m = masa;
            this.mostrarVectores = vectores;
            this.mostrarEnergia = energia;
        }
        
        public void reiniciar() {
            posicionY = altura;
            velocidadY = v0;
            tiempoTotal = 0;
            enSuelo = false;
        }
        
        @Override
        public void actualizar() {
            if (enSuelo || motorLocal == null) return;
            
            tiempoTotal = motorLocal.getTiempoTranscurrido();
            posicionY = altura + v0 * tiempoTotal - 0.5 * MotorSimulacion.getGravedad() * tiempoTotal * tiempoTotal;
            velocidadY = v0 - MotorSimulacion.getGravedad() * tiempoTotal;
            
            if (posicionY <= 0) {
                posicionY = 0;
                velocidadY = 0;
                enSuelo = true;
                motorLocal.detener();
            }
        }
        
        @Override
        protected void dibujar(Graphics2D g2d) {
            GradientPaint cielo = new GradientPaint(0, 0, new Color(135, 206, 250),
                                                     0, alto, new Color(240, 248, 255));
            g2d.setPaint(cielo);
            g2d.fillRect(0, 0, ancho, alto);
            
            g2d.setColor(new Color(100, 100, 100));
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            for (int i = 0; i <= 200; i += 20) {
                int y = alto - 50 - metrosAPixeles(i);
                if (y > 0 && y < alto - 50) {
                    g2d.drawLine(50, y, 60, y);
                    g2d.drawString(i + "m", 15, y + 5);
                }
            }
            
            int sueloY = alto - 50;
            g2d.setColor(new Color(101, 67, 33));
            g2d.fillRect(0, sueloY, ancho, 50);
            g2d.setColor(new Color(76, 153, 0));
            g2d.fillRect(0, sueloY - 10, ancho, 10);
            
            g2d.setColor(new Color(100, 100, 100, 150));
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                                         BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            int alturaInicialY = sueloY - metrosAPixeles(altura) - 15;
            if (alturaInicialY > 0) {
                g2d.drawLine(100, alturaInicialY, ancho - 100, alturaInicialY);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString(String.format("h₀ = %.1f m", altura), ancho - 90, alturaInicialY - 5);
            }
            
            int objetoX = ancho / 2;
            int objetoY = sueloY - metrosAPixeles(posicionY) - 15;
            
            Color colorObjeto = enSuelo ? new Color(46, 204, 113) : new Color(231, 76, 60);
            int radioObjeto = (int)(15 * Math.sqrt(m));
            dibujarObjeto(g2d, objetoX, objetoY, radioObjeto, colorObjeto);
            
            if (mostrarVectores && !enSuelo && Math.abs(velocidadY) > 0.1) {
                g2d.setColor(new Color(39, 174, 96));
                g2d.setStroke(new BasicStroke(3));
                int longitudFlecha = Math.min((int)(Math.abs(velocidadY) * 5), 100);
                int direccion = velocidadY < 0 ? 1 : -1;
                g2d.drawLine(objetoX, objetoY, objetoX, objetoY + direccion * longitudFlecha);
                g2d.drawLine(objetoX, objetoY + direccion * longitudFlecha, 
                            objetoX - 5, objetoY + direccion * longitudFlecha - direccion * 8);
                g2d.drawLine(objetoX, objetoY + direccion * longitudFlecha, 
                            objetoX + 5, objetoY + direccion * longitudFlecha - direccion * 8);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString(String.format("v=%.1f", Math.abs(velocidadY)), 
                              objetoX + 15, objetoY + direccion * longitudFlecha / 2);
            }
            
            if (mostrarVectores && !enSuelo) {
                g2d.setColor(new Color(155, 89, 182));
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                                             BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
                int gFlecha = (int)(MotorSimulacion.getGravedad() * 8);
                g2d.drawLine(objetoX + 30, objetoY, objetoX + 30, objetoY + gFlecha);
                g2d.drawLine(objetoX + 30, objetoY + gFlecha, 
                            objetoX + 25, objetoY + gFlecha - 8);
                g2d.drawLine(objetoX + 30, objetoY + gFlecha, 
                            objetoX + 35, objetoY + gFlecha - 8);
                g2d.drawString("g", objetoX + 40, objetoY + gFlecha / 2);
            }
            
            double energiaPotencial = m * MotorSimulacion.getGravedad() * posicionY;
            double energiaCinetica = 0.5 * m * velocidadY * velocidadY;
            double energiaTotal = energiaPotencial + energiaCinetica;
            
            String estado = enSuelo ? " ⚫ EN SUELO" : 
                           velocidadY < 0 ? " ⬇️ CAYENDO" : " ⬆️ SUBIENDO";
            
            String[] info = {
                "⏱️ Tiempo: " + String.format("%.2f s", tiempoTotal) + estado,
                "📍 Altura: " + String.format("%.2f m", Math.max(0, posicionY)),
                "⬇️ Velocidad: " + String.format("%.2f m/s", velocidadY),
                "⚡ Aceleración: " + String.format("%.2f m/s²", MotorSimulacion.getGravedad()),
                "⚖️ Masa: " + String.format("%.2f kg", m),
                "",
                mostrarEnergia ? "🔋 Eₚ: " + String.format("%.2f J", energiaPotencial) : "",
                mostrarEnergia ? "⚡ Eₖ: " + String.format("%.2f J", energiaCinetica) : "",
                mostrarEnergia ? "💯 E total: " + String.format("%.2f J", energiaTotal) : ""
            };
            dibujarInfo(g2d, info, 20, 20);
            
            if (mostrarEnergia && energiaTotal > 0) {
                int barraX = ancho - 250;
                int barraY = 30;
                int barraAncho = 230;
                int barraAlto = 120;
                
                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fillRoundRect(barraX, barraY, barraAncho, barraAlto, 15, 15);
                g2d.setColor(new Color(189, 195, 199));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(barraX, barraY, barraAncho, barraAlto, 15, 15);
                
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 13));
                g2d.drawString("ENERGÍA", barraX + 10, barraY + 20);
                
                int barraEY = barraY + 35;
                int barraEAncho = 200;
                int barraEAlto = 20;
                
                double energiaInicialTotal = m * MotorSimulacion.getGravedad() * altura + 0.5 * m * v0 * v0;
                double propEp = energiaPotencial / energiaInicialTotal;
                double propEk = energiaCinetica / energiaInicialTotal;
                
                g2d.setColor(new Color(41, 128, 185));
                g2d.fillRect(barraX + 10, barraEY, (int)(barraEAncho * propEp), barraEAlto);
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.PLAIN, 11));
                g2d.drawString(String.format("Eₚ: %.1f J (%.0f%%)", energiaPotencial, propEp * 100), 
                              barraX + 15, barraEY + 15);
                
                barraEY += 30;
                g2d.setColor(new Color(231, 76, 60));
                g2d.fillRect(barraX + 10, barraEY, (int)(barraEAncho * propEk), barraEAlto);
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.format("Eₖ: %.1f J (%.0f%%)", energiaCinetica, propEk * 100), 
                              barraX + 15, barraEY + 15);
                
                barraEY += 30;
                g2d.setColor(new Color(46, 204, 113));
                g2d.fillRect(barraX + 10, barraEY, barraEAncho, barraEAlto);
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.format("Total: %.1f J", energiaTotal), 
                              barraX + 15, barraEY + 15);
            }
        }
    }
}
