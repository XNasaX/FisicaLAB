package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.Escenario;
import com.mycompany.fisicalab.core.MotorSimulacion;
import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Simulación de Caída Libre v2.0
 * Con más parámetros configurables
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
    
    private double alturaInicial = 50.0; // metros
    private double velocidadInicial = 0.0; // m/s
    private double gravedad = 9.8; // m/s²
    private double masa = 1.0; // kg
    private int velocidadSimulacion = 30; // ms
    private boolean mostrarVectores = true;
    private boolean mostrarEnergia = false;
    
    public SimulacionCaidaLibre(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(UIHelper.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        motor = new MotorSimulacion(velocidadSimulacion);
        escenario = new EscenarioCaidaLibre(900, 600);
        
        inicializarComponentes();
        configurarTeclado();
    }
    
    private void configurarTeclado() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        // ESPACIO - Iniciar/Pausar
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
        
        // R - Reiniciar
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "reiniciar");
        actionMap.put("reiniciar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reiniciarSimulacion();
            }
        });
        
        // V - Toggle vectores
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0), "toggleVectores");
        actionMap.put("toggleVectores", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chkMostrarVectores.setSelected(!chkMostrarVectores.isSelected());
            }
        });
        
        // E - Toggle energía
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "toggleEnergia");
        actionMap.put("toggleEnergia", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chkMostrarEnergia.setSelected(!chkMostrarEnergia.isSelected());
            }
        });
    }
    
    private void inicializarComponentes() {
        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSuperior.setOpaque(false);
        JLabel titulo = UIHelper.crearTitulo("Caída Libre v2.0");
        titulo.setForeground(UIHelper.COLOR_SECUNDARIO);
        panelSuperior.add(titulo);
        
        // Panel de controles
        JPanel panelControlesInterno = new JPanel();
        panelControlesInterno.setLayout(new BoxLayout(panelControlesInterno, BoxLayout.Y_AXIS));
        panelControlesInterno.setBackground(Color.WHITE);
        panelControlesInterno.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // === ALTURA INICIAL ===
        JLabel labelTituloAltura = new JLabel("Altura Inicial (m):");
        labelTituloAltura.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloAltura.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderAltura = UIHelper.crearSlider(10, 200, 50);
        sliderAltura.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderAltura.addChangeListener(e -> {
            alturaInicial = sliderAltura.getValue();
            labelAltura.setText(String.format("h₀ = %.1f m", alturaInicial));
            escenario.setAlturaInicial(alturaInicial);
        });
        
        labelAltura = new JLabel(String.format("h₀ = %.1f m", alturaInicial));
        labelAltura.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelAltura.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // === VELOCIDAD INICIAL ===
        JLabel labelTituloVelInicial = new JLabel("Velocidad Inicial (m/s):");
        labelTituloVelInicial.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloVelInicial.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtVelocidadInicial = UIHelper.crearCampoTexto("0.0");
        txtVelocidadInicial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtVelocidadInicial.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtVelocidadInicial.setToolTipText("Negativo = hacia abajo, Positivo = hacia arriba");
        txtVelocidadInicial.addActionListener(e -> actualizarVelocidadInicial());
        txtVelocidadInicial.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                actualizarVelocidadInicial();
            }
        });
        
        JLabel labelInfoVel = new JLabel("(-: abajo, +: arriba)");
        labelInfoVel.setFont(new Font("Arial", Font.ITALIC, 10));
        labelInfoVel.setForeground(Color.GRAY);
        labelInfoVel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // === MASA ===
        JLabel labelTituloMasa = new JLabel("Masa del objeto (kg):");
        labelTituloMasa.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloMasa.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderMasa = new JSlider(1, 100, 10); // 0.1 a 10.0 kg
        sliderMasa.setMajorTickSpacing(25);
        sliderMasa.setMinorTickSpacing(5);
        sliderMasa.setPaintTicks(true);
        sliderMasa.setFont(new Font("Arial", Font.PLAIN, 9));
        sliderMasa.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderMasa.addChangeListener(e -> {
            masa = sliderMasa.getValue() / 10.0;
            labelMasa.setText(String.format("m = %.1f kg", masa));
            escenario.setMasa(masa);
        });
        
        labelMasa = new JLabel(String.format("m = %.1f kg", masa));
        labelMasa.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelMasa.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // === GRAVEDAD ===
        JLabel labelTituloGrav = new JLabel("Gravedad (m/s²):");
        labelTituloGrav.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloGrav.setAlignmentX(Component.LEFT_ALIGNMENT);
        
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
            escenario.setAlturaInicial(alturaInicial);
        });
        
        labelGravedad = new JLabel(String.format("g = %.1f m/s²", gravedad));
        labelGravedad.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelGravedad.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelInfoGrav = new JLabel("(Tierra=9.8, Luna=1.6, Marte=3.7)");
        labelInfoGrav.setFont(new Font("Arial", Font.ITALIC, 9));
        labelInfoGrav.setForeground(Color.GRAY);
        labelInfoGrav.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // === VELOCIDAD DE SIMULACIÓN ===
        JLabel labelTituloVelSim = new JLabel("Velocidad Simulación:");
        labelTituloVelSim.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloVelSim.setAlignmentX(Component.LEFT_ALIGNMENT);
        
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
        
        // === OPCIONES ===
        JLabel labelTituloOpciones = new JLabel("Opciones:");
        labelTituloOpciones.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloOpciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        chkMostrarVectores = new JCheckBox("Mostrar vectores (V)", true);
        chkMostrarVectores.setFont(new Font("Arial", Font.PLAIN, 12));
        chkMostrarVectores.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkMostrarVectores.addActionListener(e -> {
            mostrarVectores = chkMostrarVectores.isSelected();
            escenario.setMostrarVectores(mostrarVectores);
            escenario.repaint();
        });
        
        chkMostrarEnergia = new JCheckBox("Mostrar energía (E)", false);
        chkMostrarEnergia.setFont(new Font("Arial", Font.PLAIN, 12));
        chkMostrarEnergia.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkMostrarEnergia.addActionListener(e -> {
            mostrarEnergia = chkMostrarEnergia.isSelected();
            escenario.setMostrarEnergia(mostrarEnergia);
            escenario.repaint();
        });
        
        // Botones
        btnIniciar = UIHelper.crearBotonRedondeado("Soltar (SPACE)", UIHelper.COLOR_EXITO);
        btnIniciar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnIniciar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnIniciar.addActionListener(e -> iniciarSimulacion());
        
        btnPausar = UIHelper.crearBotonRedondeado("Pausar", UIHelper.COLOR_ADVERTENCIA);
        btnPausar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPausar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnPausar.setEnabled(false);
        btnPausar.addActionListener(e -> pausarSimulacion());
        
        btnReiniciar = UIHelper.crearBotonRedondeado("Reiniciar (R)", UIHelper.COLOR_SECUNDARIO);
        btnReiniciar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnReiniciar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnReiniciar.addActionListener(e -> reiniciarSimulacion());
        
        btnVolver = UIHelper.crearBotonRedondeado("Volver al Menú", UIHelper.COLOR_PELIGRO);
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVolver.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnVolver.addActionListener(e -> {
            MotorSimulacion.resetGravedad();
            frame.mostrarMenuPrincipal();
        });
        
        // Información
        JTextArea textoInfo = new JTextArea();
        textoInfo.setText("⌨️ CONTROLES:\n" +
                         "ESPACIO: Soltar/Pausar\n" +
                         "R: Reiniciar\n" +
                         "V: Toggle vectores\n" +
                         "E: Toggle energía\n\n" +
                         "📐 CAÍDA LIBRE:\n" +
                         "y = h₀ + v₀·t - ½g·t²\n" +
                         "v = v₀ - g·t\n" +
                         "Eₚ = m·g·h\n" +
                         "Eₖ = ½m·v²");
        textoInfo.setEditable(false);
        textoInfo.setWrapStyleWord(true);
        textoInfo.setLineWrap(true);
        textoInfo.setFont(new Font("Arial", Font.PLAIN, 10));
        textoInfo.setBackground(new Color(236, 240, 241));
        textoInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textoInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Agregar componentes
        panelControlesInterno.add(labelTituloAltura);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(sliderAltura);
        panelControlesInterno.add(labelAltura);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloVelInicial);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(txtVelocidadInicial);
        panelControlesInterno.add(labelInfoVel);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloMasa);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(sliderMasa);
        panelControlesInterno.add(labelMasa);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloGrav);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(sliderGravedad);
        panelControlesInterno.add(labelGravedad);
        panelControlesInterno.add(labelInfoGrav);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloVelSim);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(sliderVelocidadSim);
        panelControlesInterno.add(labelVelocidadSim);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloOpciones);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 5)));
        panelControlesInterno.add(chkMostrarVectores);
        panelControlesInterno.add(chkMostrarEnergia);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panelControlesInterno.add(btnIniciar);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 8)));
        panelControlesInterno.add(btnPausar);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 8)));
        panelControlesInterno.add(btnReiniciar);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 15)));
        panelControlesInterno.add(textoInfo);
        panelControlesInterno.add(Box.createVerticalGlue());
        panelControlesInterno.add(btnVolver);
        
        JScrollPane scrollControles = new JScrollPane(panelControlesInterno);
        scrollControles.setPreferredSize(new Dimension(290, 600));
        scrollControles.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollControles, BorderLayout.WEST);
        add(escenario, BorderLayout.CENTER);
    }
    
    private void actualizarVelocidadInicial() {
        try {
            double valor = Double.parseDouble(txtVelocidadInicial.getText());
            velocidadInicial = valor;
            txtVelocidadInicial.setText(String.format("%.1f", velocidadInicial));
            escenario.setVelocidadInicial(velocidadInicial);
        } catch (NumberFormatException ex) {
            txtVelocidadInicial.setText("0.0");
            velocidadInicial = 0.0;
        }
    }
    
    private void iniciarSimulacion() {
        escenario.setAlturaInicial(alturaInicial);
        escenario.setVelocidadInicial(velocidadInicial);
        escenario.setMasa(masa);
        
        motor = new MotorSimulacion(velocidadSimulacion);
        motor.iniciar(e -> {
            escenario.actualizar();
            escenario.repaint();
        });
        
        btnIniciar.setEnabled(false);
        btnPausar.setEnabled(true);
        sliderAltura.setEnabled(false);
        txtVelocidadInicial.setEnabled(false);
        sliderMasa.setEnabled(false);
        sliderGravedad.setEnabled(false);
        sliderVelocidadSim.setEnabled(false);
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
        sliderAltura.setEnabled(true);
        txtVelocidadInicial.setEnabled(true);
        sliderMasa.setEnabled(true);
        sliderGravedad.setEnabled(true);
        sliderVelocidadSim.setEnabled(true);
    }
    
    // Escenario interno
    private class EscenarioCaidaLibre extends Escenario {
        
        private double posicionY;
        private double velocidadY;
        private double altura, v0, m;
        private double tiempoTotal;
        private boolean enSuelo;
        private boolean mostrarVectores;
        private boolean mostrarEnergia;
        
        public EscenarioCaidaLibre(int ancho, int alto) {
            super(ancho, alto);
            this.motor = SimulacionCaidaLibre.this.motor;
            this.escalaPixeles = 2.5;
            this.mostrarVectores = true;
            this.mostrarEnergia = false;
            reiniciar();
        }
        
        public void setAlturaInicial(double h) {
            this.altura = h;
        }
        
        public void setVelocidadInicial(double v) {
            this.v0 = v;
        }
        
        public void setMasa(double masa) {
            this.m = masa;
        }
        
        public void setMostrarVectores(boolean mostrar) {
            this.mostrarVectores = mostrar;
        }
        
        public void setMostrarEnergia(boolean mostrar) {
            this.mostrarEnergia = mostrar;
        }
        
        public void reiniciar() {
            posicionY = altura;
            velocidadY = v0;
            tiempoTotal = 0;
            enSuelo = false;
        }
        
        @Override
        public void actualizar() {
            if (enSuelo) return;
            
            double dt = motor.getDeltaTime();
            tiempoTotal = motor.getTiempoTranscurrido();
            
            // Cálculo con velocidad inicial: y = h0 + v0*t - 0.5*g*t²
            posicionY = altura + v0 * tiempoTotal - 0.5 * MotorSimulacion.getGravedad() * tiempoTotal * tiempoTotal;
            velocidadY = v0 - MotorSimulacion.getGravedad() * tiempoTotal;
            
            if (posicionY <= 0) {
                posicionY = 0;
                velocidadY = 0;
                enSuelo = true;
                motor.detener();
            }
        }
        
        @Override
        protected void dibujar(Graphics2D g2d) {
            // Cielo degradado
            GradientPaint cielo = new GradientPaint(0, 0, new Color(135, 206, 250),
                                                     0, alto, new Color(240, 248, 255));
            g2d.setPaint(cielo);
            g2d.fillRect(0, 0, ancho, alto);
            
            // Escala vertical
            g2d.setColor(new Color(100, 100, 100));
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            for (int i = 0; i <= 200; i += 20) {
                int y = alto - 50 - metrosAPixeles(i);
                if (y > 0 && y < alto - 50) {
                    g2d.drawLine(50, y, 60, y);
                    g2d.drawString(i + "m", 15, y + 5);
                }
            }
            
            // Suelo
            int sueloY = alto - 50;
            g2d.setColor(new Color(101, 67, 33));
            g2d.fillRect(0, sueloY, ancho, 50);
            
            g2d.setColor(new Color(76, 153, 0));
            g2d.fillRect(0, sueloY - 10, ancho, 10);
            
            // Línea de altura inicial
            g2d.setColor(new Color(100, 100, 100, 150));
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                                         BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            int alturaInicialY = sueloY - metrosAPixeles(altura) - 15;
            if (alturaInicialY > 0) {
                g2d.drawLine(100, alturaInicialY, ancho - 100, alturaInicialY);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString(String.format("h₀ = %.1f m", altura), ancho - 90, alturaInicialY - 5);
            }
            
            // Objeto cayendo
            int objetoX = ancho / 2;
            int objetoY = sueloY - metrosAPixeles(posicionY) - 15;
            
            Color colorObjeto = enSuelo ? new Color(46, 204, 113) : new Color(231, 76, 60);
            int radioObjeto = (int)(15 * Math.sqrt(m)); // Radio proporcional a masa
            dibujarObjeto(g2d, objetoX, objetoY, radioObjeto, colorObjeto);
            
            // Vector velocidad
            if (mostrarVectores && !enSuelo && Math.abs(velocidadY) > 0.1) {
                g2d.setColor(new Color(39, 174, 96));
                g2d.setStroke(new BasicStroke(3));
                int longitudFlecha = Math.min((int)(Math.abs(velocidadY) * 5), 100);
                int direccion = velocidadY < 0 ? 1 : -1; // Negativo = hacia abajo
                g2d.drawLine(objetoX, objetoY, objetoX, objetoY + direccion * longitudFlecha);
                
                // Punta
                g2d.drawLine(objetoX, objetoY + direccion * longitudFlecha, 
                            objetoX - 5, objetoY + direccion * longitudFlecha - direccion * 8);
                g2d.drawLine(objetoX, objetoY + direccion * longitudFlecha, 
                            objetoX + 5, objetoY + direccion * longitudFlecha - direccion * 8);
                
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString(String.format("v=%.1f", Math.abs(velocidadY)), 
                              objetoX + 15, objetoY + direccion * longitudFlecha / 2);
            }
            
            // Vector gravedad (constante)
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
            
            // Calcular energías
            double energiaPotencial = m * MotorSimulacion.getGravedad() * posicionY;
            double energiaCinetica = 0.5 * m * velocidadY * velocidadY;
            double energiaTotal = energiaPotencial + energiaCinetica;
            double energiaInicialTotal = m * MotorSimulacion.getGravedad() * altura + 0.5 * m * v0 * v0;
            
            // Información en pantalla
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
            
            // Barra de energía (si está activada)
            if (mostrarEnergia && energiaInicialTotal > 0) {
                int barraX = ancho - 250;
                int barraY = 30;
                int barraAncho = 230;
                int barraAlto = 120;
                
                // Fondo
                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fillRoundRect(barraX, barraY, barraAncho, barraAlto, 15, 15);
                g2d.setColor(new Color(189, 195, 199));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(barraX, barraY, barraAncho, barraAlto, 15, 15);
                
                // Título
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 13));
                g2d.drawString("ENERGÍA", barraX + 10, barraY + 20);
                
                // Barras de energía
                int barraEY = barraY + 35;
                int barraEAncho = 200;
                int barraEAlto = 20;
                
                double propEp = energiaPotencial / energiaInicialTotal;
                double propEk = energiaCinetica / energiaInicialTotal;
                
                // Energía Potencial
                g2d.setColor(new Color(41, 128, 185));
                g2d.fillRect(barraX + 10, barraEY, (int)(barraEAncho * propEp), barraEAlto);
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.PLAIN, 11));
                g2d.drawString(String.format("Eₚ: %.1f J (%.0f%%)", energiaPotencial, propEp * 100), 
                              barraX + 15, barraEY + 15);
                
                // Energía Cinética
                barraEY += 30;
                g2d.setColor(new Color(231, 76, 60));
                g2d.fillRect(barraX + 10, barraEY, (int)(barraEAncho * propEk), barraEAlto);
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.format("Eₖ: %.1f J (%.0f%%)", energiaCinetica, propEk * 100), 
                              barraX + 15, barraEY + 15);
                
                // Total
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
