package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.Escenario;
import com.mycompany.fisicalab.core.MotorSimulacion;
import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulación de Tiro Parabólico v2.0
 * Con controles de teclado y parámetros avanzados
 */
public class SimulacionTiroParabolico extends JPanel {
    
    private SimuladorFrame frame;
    private EscenarioTiroParabolico escenario;
    private MotorSimulacion motor;
    
    private JButton btnIniciar, btnPausar, btnReiniciar, btnVolver;
    private JSlider sliderVelocidad, sliderAngulo, sliderGravedad, sliderVelocidadSim;
    private JLabel labelVelocidad, labelAngulo, labelGravedad, labelVelocidadSim;
    private JTextField txtAlturaInicial;
    
    private double velocidadInicial = 20.0; // m/s
    private double angulo = 45.0; // grados
    private double gravedad = 9.8; // m/s²
    private double alturaInicial = 0.0; // m
    private int velocidadSimulacion = 30; // ms
    
    public SimulacionTiroParabolico(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(UIHelper.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        motor = new MotorSimulacion(velocidadSimulacion);
        escenario = new EscenarioTiroParabolico(900, 600);
        
        inicializarComponentes();
        configurarTeclado();
    }
    
    private void configurarTeclado() {
        // Configurar KeyBindings para controles con teclado
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        
        // Flecha ARRIBA - Aumentar ángulo
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "aumentarAngulo");
        actionMap.put("aumentarAngulo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!motor.isEnEjecucion() && angulo < 90) {
                    angulo = Math.min(90, angulo + 1);
                    sliderAngulo.setValue((int)angulo);
                }
            }
        });
        
        // Flecha ABAJO - Disminuir ángulo
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "disminuirAngulo");
        actionMap.put("disminuirAngulo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!motor.isEnEjecucion() && angulo > 0) {
                    angulo = Math.max(0, angulo - 1);
                    sliderAngulo.setValue((int)angulo);
                }
            }
        });
        
        // Flecha DERECHA - Aumentar velocidad
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "aumentarVelocidad");
        actionMap.put("aumentarVelocidad", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!motor.isEnEjecucion() && velocidadInicial < 50) {
                    velocidadInicial = Math.min(50, velocidadInicial + 1);
                    sliderVelocidad.setValue((int)velocidadInicial);
                }
            }
        });
        
        // Flecha IZQUIERDA - Disminuir velocidad
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "disminuirVelocidad");
        actionMap.put("disminuirVelocidad", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!motor.isEnEjecucion() && velocidadInicial > 5) {
                    velocidadInicial = Math.max(5, velocidadInicial - 1);
                    sliderVelocidad.setValue((int)velocidadInicial);
                }
            }
        });
        
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
    }
    
    private void inicializarComponentes() {
        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSuperior.setOpaque(false);
        JLabel titulo = UIHelper.crearTitulo("Tiro Parabólico v2.0");
        titulo.setForeground(UIHelper.COLOR_EXITO);
        panelSuperior.add(titulo);
        
        // Panel de controles con scroll
        JPanel panelControlesInterno = new JPanel();
        panelControlesInterno.setLayout(new BoxLayout(panelControlesInterno, BoxLayout.Y_AXIS));
        panelControlesInterno.setBackground(Color.WHITE);
        panelControlesInterno.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // === VELOCIDAD INICIAL ===
        JLabel labelTituloVel = new JLabel("Velocidad Inicial (m/s):");
        labelTituloVel.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloVel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderVelocidad = UIHelper.crearSlider(5, 50, 20);
        sliderVelocidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderVelocidad.addChangeListener(e -> {
            velocidadInicial = sliderVelocidad.getValue();
            labelVelocidad.setText(String.format("v₀ = %.1f m/s", velocidadInicial));
            escenario.setParametros(velocidadInicial, angulo, alturaInicial);
        });
        
        labelVelocidad = new JLabel(String.format("v₀ = %.1f m/s", velocidadInicial));
        labelVelocidad.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelVelocidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // === ÁNGULO ===
        JLabel labelTituloAng = new JLabel("Ángulo (grados):");
        labelTituloAng.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloAng.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderAngulo = UIHelper.crearSlider(0, 90, 45);
        sliderAngulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderAngulo.addChangeListener(e -> {
            angulo = sliderAngulo.getValue();
            labelAngulo.setText(String.format("θ = %.1f°", angulo));
            escenario.setParametros(velocidadInicial, angulo, alturaInicial);
        });
        
        labelAngulo = new JLabel(String.format("θ = %.1f°", angulo));
        labelAngulo.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelAngulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // === ALTURA INICIAL ===
        JLabel labelTituloAltura = new JLabel("Altura Inicial (m):");
        labelTituloAltura.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloAltura.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtAlturaInicial = UIHelper.crearCampoTexto("0.0");
        txtAlturaInicial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtAlturaInicial.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtAlturaInicial.addActionListener(e -> actualizarAlturaInicial());
        txtAlturaInicial.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                actualizarAlturaInicial();
            }
        });
        
        // === GRAVEDAD ===
        JLabel labelTituloGrav = new JLabel("Gravedad (m/s²):");
        labelTituloGrav.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloGrav.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderGravedad = new JSlider(1, 200, 98); // 0.1 a 20.0 m/s² (multiplicado por 10)
        sliderGravedad.setMajorTickSpacing(50);
        sliderGravedad.setMinorTickSpacing(10);
        sliderGravedad.setPaintTicks(true);
        sliderGravedad.setFont(new Font("Arial", Font.PLAIN, 9));
        sliderGravedad.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderGravedad.addChangeListener(e -> {
            gravedad = sliderGravedad.getValue() / 10.0;
            labelGravedad.setText(String.format("g = %.1f m/s²", gravedad));
            MotorSimulacion.setGravedad(gravedad);
            escenario.setParametros(velocidadInicial, angulo, alturaInicial);
        });
        
        labelGravedad = new JLabel(String.format("g = %.1f m/s²", gravedad));
        labelGravedad.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelGravedad.setAlignmentX(Component.LEFT_ALIGNMENT);
        
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
            String velocidad = velocidadSimulacion < 30 ? "Rápida" : 
                              velocidadSimulacion > 50 ? "Lenta" : "Normal";
            labelVelocidadSim.setText(String.format("%d ms (%s)", velocidadSimulacion, velocidad));
        });
        
        labelVelocidadSim = new JLabel(String.format("%d ms (Normal)", velocidadSimulacion));
        labelVelocidadSim.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelVelocidadSim.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Botones
        btnIniciar = UIHelper.crearBotonRedondeado("Lanzar (SPACE)", UIHelper.COLOR_EXITO);
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
        
        // Información de controles
        JTextArea textoControles = new JTextArea();
        textoControles.setText("⌨️ CONTROLES TECLADO:\n" +
                               "↑ ↓ : Ajustar ángulo\n" +
                               "← →: Ajustar velocidad\n" +
                               "ESPACIO: Lanzar/Pausar\n" +
                               "R: Reiniciar\n\n" +
                               "🎯 ECUACIONES:\n" +
                               "vₓ = v₀·cos(θ)\n" +
                               "vᵧ = v₀·sin(θ)\n" +
                               "x = vₓ·t\n" +
                               "y = h₀ + vᵧ·t - ½g·t²");
        textoControles.setEditable(false);
        textoControles.setWrapStyleWord(true);
        textoControles.setLineWrap(true);
        textoControles.setFont(new Font("Arial", Font.PLAIN, 10));
        textoControles.setBackground(new Color(236, 240, 241));
        textoControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textoControles.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Agregar componentes
        panelControlesInterno.add(labelTituloVel);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(sliderVelocidad);
        panelControlesInterno.add(labelVelocidad);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloAng);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(sliderAngulo);
        panelControlesInterno.add(labelAngulo);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloAltura);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(txtAlturaInicial);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloGrav);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(sliderGravedad);
        panelControlesInterno.add(labelGravedad);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloVelSim);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(sliderVelocidadSim);
        panelControlesInterno.add(labelVelocidadSim);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 15)));
        
        panelControlesInterno.add(btnIniciar);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 8)));
        panelControlesInterno.add(btnPausar);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 8)));
        panelControlesInterno.add(btnReiniciar);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 15)));
        panelControlesInterno.add(textoControles);
        panelControlesInterno.add(Box.createVerticalGlue());
        panelControlesInterno.add(btnVolver);
        
        // Scroll para controles
        JScrollPane scrollControles = new JScrollPane(panelControlesInterno);
        scrollControles.setPreferredSize(new Dimension(290, 600));
        scrollControles.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollControles, BorderLayout.WEST);
        add(escenario, BorderLayout.CENTER);
    }
    
    private void actualizarAlturaInicial() {
        try {
            double valor = Double.parseDouble(txtAlturaInicial.getText());
            if (valor < 0) valor = 0;
            if (valor > 100) valor = 100;
            alturaInicial = valor;
            txtAlturaInicial.setText(String.format("%.1f", alturaInicial));
            escenario.setParametros(velocidadInicial, angulo, alturaInicial);
        } catch (NumberFormatException ex) {
            txtAlturaInicial.setText("0.0");
            alturaInicial = 0.0;
        }
    }
    
    private void iniciarSimulacion() {
        escenario.setParametros(velocidadInicial, angulo, alturaInicial);
        motor = new MotorSimulacion(velocidadSimulacion);
        motor.iniciar(e -> {
            escenario.actualizar();
            escenario.repaint();
        });
        
        btnIniciar.setEnabled(false);
        btnPausar.setEnabled(true);
        sliderVelocidad.setEnabled(false);
        sliderAngulo.setEnabled(false);
        txtAlturaInicial.setEnabled(false);
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
        sliderVelocidad.setEnabled(true);
        sliderAngulo.setEnabled(true);
        txtAlturaInicial.setEnabled(true);
        sliderGravedad.setEnabled(true);
        sliderVelocidadSim.setEnabled(true);
    }
    
    // Escenario interno
    private class EscenarioTiroParabolico extends Escenario {
        
        private double posicionX, posicionY;
        private double velocidadX, velocidadY;
        private double v0, theta, h0;
        private double tiempoTotal;
        private boolean enSuelo;
        private List<Point> trayectoria;
        
        public EscenarioTiroParabolico(int ancho, int alto) {
            super(ancho, alto);
            this.motor = SimulacionTiroParabolico.this.motor;
            this.escalaPixeles = 8.0;
            this.trayectoria = new ArrayList<>();
            reiniciar();
        }
        
        public void setParametros(double velocidad, double angulo, double altura) {
            this.v0 = velocidad;
            this.theta = angulo;
            this.h0 = altura;
        }
        
        public void reiniciar() {
            posicionX = 0;
            posicionY = h0;
            tiempoTotal = 0;
            enSuelo = false;
            trayectoria.clear();
            
            double radianes = Math.toRadians(theta);
            velocidadX = v0 * Math.cos(radianes);
            velocidadY = v0 * Math.sin(radianes);
        }
        
        @Override
        public void actualizar() {
            if (enSuelo) return;
            
            double dt = motor.getDeltaTime();
            tiempoTotal = motor.getTiempoTranscurrido();
            
            posicionX = velocidadX * tiempoTotal;
            posicionY = h0 + velocidadY * tiempoTotal - 0.5 * MotorSimulacion.getGravedad() * tiempoTotal * tiempoTotal;
            
            if (posicionY >= 0) {
                int screenX = 100 + metrosAPixeles(posicionX);
                int screenY = alto - 100 - metrosAPixeles(posicionY);
                if (screenX < ancho) {
                    trayectoria.add(new Point(screenX, screenY));
                }
            }
            
            if (posicionY <= 0) {
                posicionY = 0;
                enSuelo = true;
                motor.detener();
            }
        }
        
        @Override
        protected void dibujar(Graphics2D g2d) {
            GradientPaint cielo = new GradientPaint(0, 0, new Color(135, 206, 250),
                                                     0, alto, new Color(240, 248, 255));
            g2d.setPaint(cielo);
            g2d.fillRect(0, 0, ancho, alto);
            
            dibujarCuadricula(g2d, 50);
            
            int sueloY = alto - 100;
            g2d.setColor(new Color(101, 67, 33));
            g2d.fillRect(0, sueloY, ancho, 100);
            g2d.setColor(new Color(76, 153, 0));
            g2d.fillRect(0, sueloY - 10, ancho, 10);
            
            int cannonX = 100;
            int cannonY = sueloY - metrosAPixeles(h0);
            
            g2d.setColor(new Color(52, 73, 94));
            g2d.fillRect(cannonX - 20, cannonY - 10, 40, 20);
            
            g2d.setStroke(new BasicStroke(8));
            double radianes = Math.toRadians(theta);
            int tuboLargo = 40;
            int tuboX2 = cannonX + (int)(tuboLargo * Math.cos(radianes));
            int tuboY2 = cannonY - (int)(tuboLargo * Math.sin(radianes));
            g2d.drawLine(cannonX, cannonY, tuboX2, tuboY2);
            
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(new Color(230, 126, 34));
            int radioArco = 30;
            g2d.drawArc(cannonX - radioArco, cannonY - radioArco, 
                       radioArco * 2, radioArco * 2, 
                       0, (int)theta);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(String.format("%.0f°", theta), 
                          cannonX + 35, cannonY - 10);
            
            // Trayectoria predicha
            if (!motor.isEnEjecucion() && !enSuelo) {
                g2d.setColor(new Color(100, 100, 100, 100));
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                                             BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
                
                Path2D trayectoriaPredicha = new Path2D.Double();
                boolean primero = true;
                
                for (double t = 0; t <= 15; t += 0.1) {
                    double x = velocidadX * t;
                    double y = h0 + velocidadY * t - 0.5 * MotorSimulacion.getGravedad() * t * t;
                    
                    if (y < 0) break;
                    
                    int screenX = cannonX + metrosAPixeles(x);
                    int screenY = sueloY - metrosAPixeles(y);
                    
                    if (screenX > ancho) break;
                    
                    if (primero) {
                        trayectoriaPredicha.moveTo(screenX, screenY);
                        primero = false;
                    } else {
                        trayectoriaPredicha.lineTo(screenX, screenY);
                    }
                }
                
                g2d.draw(trayectoriaPredicha);
            }
            
            // Trayectoria recorrida
            if (trayectoria.size() > 1) {
                g2d.setColor(new Color(231, 76, 60));
                g2d.setStroke(new BasicStroke(3));
                
                for (int i = 0; i < trayectoria.size() - 1; i++) {
                    Point p1 = trayectoria.get(i);
                    Point p2 = trayectoria.get(i + 1);
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
            
            // Objeto en movimiento
            if (posicionY >= 0) {
                int objetoX = cannonX + metrosAPixeles(posicionX);
                int objetoY = sueloY - metrosAPixeles(posicionY);
                dibujarObjeto(g2d, objetoX, objetoY, 12, new Color(231, 76, 60));
                
                if (motor.isEnEjecucion()) {
                    // Vector velocidad horizontal
                    g2d.setColor(new Color(39, 174, 96));
                    g2d.setStroke(new BasicStroke(3));
                    int vxLargo = (int)(velocidadX * 3);
                    g2d.drawLine(objetoX, objetoY, objetoX + vxLargo, objetoY);
                    g2d.drawLine(objetoX + vxLargo, objetoY, 
                                objetoX + vxLargo - 6, objetoY - 4);
                    g2d.drawLine(objetoX + vxLargo, objetoY, 
                                objetoX + vxLargo - 6, objetoY + 4);
                    g2d.drawString("vₓ", objetoX + vxLargo + 5, objetoY - 5);
                    
                    // Vector velocidad vertical
                    double vyActual = velocidadY - MotorSimulacion.getGravedad() * tiempoTotal;
                    g2d.setColor(new Color(41, 128, 185));
                    int vyLargo = (int)(Math.abs(vyActual) * 3);
                    int direccionY = vyActual < 0 ? 1 : -1;
                    g2d.drawLine(objetoX, objetoY, objetoX, objetoY + direccionY * vyLargo);
                    if (vyLargo > 0) {
                        g2d.drawLine(objetoX, objetoY + direccionY * vyLargo, 
                                    objetoX - 4, objetoY + direccionY * vyLargo - direccionY * 6);
                        g2d.drawLine(objetoX, objetoY + direccionY * vyLargo, 
                                    objetoX + 4, objetoY + direccionY * vyLargo - direccionY * 6);
                    }
                    g2d.drawString("vᵧ", objetoX + 10, objetoY + direccionY * vyLargo / 2);
                }
            }
            
            // Calcular datos
            double alcanceMax = (v0 * v0 * Math.sin(2 * Math.toRadians(theta))) / MotorSimulacion.getGravedad();
            double alturaMax = h0 + (v0 * v0 * Math.sin(Math.toRadians(theta)) * Math.sin(Math.toRadians(theta))) 
                              / (2 * MotorSimulacion.getGravedad());
            double tiempoVuelo = (velocidadY + Math.sqrt(velocidadY * velocidadY + 2 * MotorSimulacion.getGravedad() * h0)) 
                                / MotorSimulacion.getGravedad();
            
            String estado = enSuelo ? " ⚫ IMPACTO" : " 🚀 VOLANDO";
            String[] info = {
                "⏱️ Tiempo: " + String.format("%.2f s", tiempoTotal) + estado,
                "📍 Posición: (" + String.format("%.2f", posicionX) + ", " + String.format("%.2f", Math.max(0, posicionY)) + ") m",
                "➡️ vₓ: " + String.format("%.2f m/s", velocidadX) + " (constante)",
                "⬇️ vᵧ: " + String.format("%.2f m/s", velocidadY - MotorSimulacion.getGravedad() * tiempoTotal),
                "",
                "🎯 Alcance máx: " + String.format("%.2f m", alcanceMax),
                "⬆️ Altura máx: " + String.format("%.2f m", alturaMax),
                "⏰ Tiempo vuelo: " + String.format("%.2f s", tiempoVuelo),
                "🌍 Gravedad: " + String.format("%.2f m/s²", MotorSimulacion.getGravedad())
            };
            dibujarInfo(g2d, info, 20, 20);
        }
    }
}
