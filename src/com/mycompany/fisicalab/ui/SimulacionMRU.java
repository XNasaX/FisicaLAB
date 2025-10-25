package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.Escenario;
import com.mycompany.fisicalab.core.MotorSimulacion;
import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Simulación de Movimiento Rectilíneo Uniforme (MRU) v2.0
 * Con más parámetros configurables
 */
public class SimulacionMRU extends JPanel {
    
    private SimuladorFrame frame;
    private EscenarioMRU escenario;
    private MotorSimulacion motor;
    
    private JButton btnIniciar, btnPausar, btnReiniciar, btnVolver;
    private JSlider sliderVelocidad, sliderVelocidadSim, sliderDistancia;
    private JLabel labelVelocidad, labelVelocidadSim, labelDistancia;
    private JTextField txtPosicionInicial, txtTiempoObjetivo;
    private JCheckBox chkMostrarVectores, chkModoInfinito;
    
    private double velocidad = 5.0; // m/s
    private double posicionInicial = 0.0; // m
    private double distanciaObjetivo = 50.0; // m
    private double tiempoObjetivo = 0.0; // s (0 = sin límite)
    private int velocidadSimulacion = 30; // ms
    private boolean mostrarVectores = true;
    private boolean modoInfinito = false;
    
    public SimulacionMRU(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(UIHelper.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        motor = new MotorSimulacion(velocidadSimulacion);
        escenario = new EscenarioMRU(900, 500);
        
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
    }
    
    private void inicializarComponentes() {
        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSuperior.setOpaque(false);
        JLabel titulo = UIHelper.crearTitulo("Movimiento Rectilíneo Uniforme v2.0");
        titulo.setForeground(UIHelper.COLOR_PRIMARIO);
        panelSuperior.add(titulo);
        
        // Panel de controles
        JPanel panelControlesInterno = new JPanel();
        panelControlesInterno.setLayout(new BoxLayout(panelControlesInterno, BoxLayout.Y_AXIS));
        panelControlesInterno.setBackground(Color.WHITE);
        panelControlesInterno.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // === VELOCIDAD ===
        JLabel labelTituloVel = new JLabel("Velocidad (m/s):");
        labelTituloVel.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloVel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderVelocidad = UIHelper.crearSlider(1, 30, 5);
        sliderVelocidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderVelocidad.addChangeListener(e -> {
            velocidad = sliderVelocidad.getValue();
            labelVelocidad.setText(String.format("v = %.1f m/s", velocidad));
            escenario.setVelocidad(velocidad);
        });
        
        labelVelocidad = new JLabel(String.format("v = %.1f m/s", velocidad));
        labelVelocidad.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelVelocidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // === POSICIÓN INICIAL ===
        JLabel labelTituloPosInicial = new JLabel("Posición Inicial (m):");
        labelTituloPosInicial.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloPosInicial.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtPosicionInicial = UIHelper.crearCampoTexto("0.0");
        txtPosicionInicial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPosicionInicial.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPosicionInicial.addActionListener(e -> actualizarPosicionInicial());
        txtPosicionInicial.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                actualizarPosicionInicial();
            }
        });
        
        // === DISTANCIA OBJETIVO ===
        JLabel labelTituloDistancia = new JLabel("Distancia Objetivo (m):");
        labelTituloDistancia.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloDistancia.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderDistancia = new JSlider(10, 200, 50);
        sliderDistancia.setMajorTickSpacing(50);
        sliderDistancia.setMinorTickSpacing(10);
        sliderDistancia.setPaintTicks(true);
        sliderDistancia.setPaintLabels(true);
        sliderDistancia.setFont(new Font("Arial", Font.PLAIN, 9));
        sliderDistancia.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderDistancia.addChangeListener(e -> {
            distanciaObjetivo = sliderDistancia.getValue();
            labelDistancia.setText(String.format("d = %.1f m", distanciaObjetivo));
            escenario.setDistanciaObjetivo(distanciaObjetivo);
        });
        
        labelDistancia = new JLabel(String.format("d = %.1f m", distanciaObjetivo));
        labelDistancia.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelDistancia.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // === TIEMPO OBJETIVO ===
        JLabel labelTituloTiempo = new JLabel("Tiempo Objetivo (s, 0=sin límite):");
        labelTituloTiempo.setFont(new Font("Arial", Font.BOLD, 13));
        labelTituloTiempo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtTiempoObjetivo = UIHelper.crearCampoTexto("0.0");
        txtTiempoObjetivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtTiempoObjetivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtTiempoObjetivo.addActionListener(e -> actualizarTiempoObjetivo());
        txtTiempoObjetivo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                actualizarTiempoObjetivo();
            }
        });
        
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
        
        chkModoInfinito = new JCheckBox("Modo infinito (bucle)", false);
        chkModoInfinito.setFont(new Font("Arial", Font.PLAIN, 12));
        chkModoInfinito.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkModoInfinito.addActionListener(e -> {
            modoInfinito = chkModoInfinito.isSelected();
            escenario.setModoInfinito(modoInfinito);
        });
        
        // Botones
        btnIniciar = UIHelper.crearBotonRedondeado("Iniciar (SPACE)", UIHelper.COLOR_EXITO);
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
        btnVolver.addActionListener(e -> frame.mostrarMenuPrincipal());
        
        // Información
        JTextArea textoInfo = new JTextArea();
        textoInfo.setText("⌨️ CONTROLES:\n" +
                         "ESPACIO: Iniciar/Pausar\n" +
                         "R: Reiniciar\n" +
                         "V: Toggle vectores\n\n" +
                         "📐 MRU:\n" +
                         "• Velocidad constante\n" +
                         "• Aceleración = 0\n" +
                         "• x = x₀ + v·t\n" +
                         "• Movimiento uniforme");
        textoInfo.setEditable(false);
        textoInfo.setWrapStyleWord(true);
        textoInfo.setLineWrap(true);
        textoInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        textoInfo.setBackground(new Color(236, 240, 241));
        textoInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textoInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Agregar componentes
        panelControlesInterno.add(labelTituloVel);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(sliderVelocidad);
        panelControlesInterno.add(labelVelocidad);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloPosInicial);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(txtPosicionInicial);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloDistancia);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(sliderDistancia);
        panelControlesInterno.add(labelDistancia);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloTiempo);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(txtTiempoObjetivo);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloVelSim);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 3)));
        panelControlesInterno.add(sliderVelocidadSim);
        panelControlesInterno.add(labelVelocidadSim);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        panelControlesInterno.add(labelTituloOpciones);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 5)));
        panelControlesInterno.add(chkMostrarVectores);
        panelControlesInterno.add(chkModoInfinito);
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
        scrollControles.setPreferredSize(new Dimension(290, 500));
        scrollControles.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 2));
        
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollControles, BorderLayout.WEST);
        add(escenario, BorderLayout.CENTER);
    }
    
    private void actualizarPosicionInicial() {
        try {
            double valor = Double.parseDouble(txtPosicionInicial.getText());
            posicionInicial = valor;
            txtPosicionInicial.setText(String.format("%.1f", posicionInicial));
            escenario.setPosicionInicial(posicionInicial);
        } catch (NumberFormatException ex) {
            txtPosicionInicial.setText("0.0");
            posicionInicial = 0.0;
        }
    }
    
    private void actualizarTiempoObjetivo() {
        try {
            double valor = Double.parseDouble(txtTiempoObjetivo.getText());
            if (valor < 0) valor = 0;
            tiempoObjetivo = valor;
            txtTiempoObjetivo.setText(String.format("%.1f", tiempoObjetivo));
            escenario.setTiempoObjetivo(tiempoObjetivo);
        } catch (NumberFormatException ex) {
            txtTiempoObjetivo.setText("0.0");
            tiempoObjetivo = 0.0;
        }
    }
    
    private void iniciarSimulacion() {
        escenario.setVelocidad(velocidad);
        escenario.setPosicionInicial(posicionInicial);
        escenario.setDistanciaObjetivo(distanciaObjetivo);
        escenario.setTiempoObjetivo(tiempoObjetivo);
        
        motor = new MotorSimulacion(velocidadSimulacion);
        motor.iniciar(e -> {
            escenario.actualizar();
            escenario.repaint();
        });
        
        btnIniciar.setEnabled(false);
        btnPausar.setEnabled(true);
        sliderVelocidad.setEnabled(false);
        txtPosicionInicial.setEnabled(false);
        sliderDistancia.setEnabled(false);
        txtTiempoObjetivo.setEnabled(false);
        sliderVelocidadSim.setEnabled(false);
        chkModoInfinito.setEnabled(false);
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
        txtPosicionInicial.setEnabled(true);
        sliderDistancia.setEnabled(true);
        txtTiempoObjetivo.setEnabled(true);
        sliderVelocidadSim.setEnabled(true);
        chkModoInfinito.setEnabled(true);
    }
    
    // Clase interna para el escenario MRU
    private class EscenarioMRU extends Escenario {
        
        private double posicionX;
        private double velocidadActual;
        private double tiempoTotal;
        private double x0, distancia, tiempoLimite;
        private boolean mostrarVectores;
        private boolean modoInfinito;
        private boolean objetivoAlcanzado;
        
        public EscenarioMRU(int ancho, int alto) {
            super(ancho, alto);
            this.motor = SimulacionMRU.this.motor;
            this.mostrarVectores = true;
            this.modoInfinito = false;
            reiniciar();
        }
        
        public void setVelocidad(double v) {
            this.velocidadActual = v;
        }
        
        public void setPosicionInicial(double x) {
            this.x0 = x;
        }
        
        public void setDistanciaObjetivo(double d) {
            this.distancia = d;
        }
        
        public void setTiempoObjetivo(double t) {
            this.tiempoLimite = t;
        }
        
        public void setMostrarVectores(boolean mostrar) {
            this.mostrarVectores = mostrar;
        }
        
        public void setModoInfinito(boolean infinito) {
            this.modoInfinito = infinito;
        }
        
        public void reiniciar() {
            posicionX = x0;
            tiempoTotal = 0;
            objetivoAlcanzado = false;
        }
        
        @Override
        public void actualizar() {
            double dt = motor.getDeltaTime();
            tiempoTotal = motor.getTiempoTranscurrido();
            
            // Cálculo MRU: x = x0 + v*t
            posicionX = MotorSimulacion.calcularPosicionMRU(x0, velocidadActual, tiempoTotal);
            
            // Verificar si alcanzó la distancia objetivo
            if (!modoInfinito && (posicionX - x0) >= distancia) {
                objetivoAlcanzado = true;
                motor.detener();
            }
            
            // Verificar límite de tiempo
            if (tiempoLimite > 0 && tiempoTotal >= tiempoLimite) {
                motor.detener();
            }
            
            // Modo infinito: reiniciar cuando sale de pantalla
            if (modoInfinito && metrosAPixeles(posicionX - x0) > ancho - 100) {
                motor.reiniciar();
                reiniciar();
            }
        }
        
        @Override
        protected void dibujar(Graphics2D g2d) {
            // Cuadrícula de fondo
            dibujarCuadricula(g2d, 50);
            
            // Línea de piso
            int pisoY = alto - 100;
            g2d.setColor(new Color(52, 73, 94));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(0, pisoY, ancho, pisoY);
            
            // Línea de inicio
            int inicioX = 50 + metrosAPixeles(x0);
            g2d.setColor(new Color(46, 204, 113));
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                         BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
            g2d.drawLine(inicioX, pisoY - 100, inicioX, pisoY);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("INICIO", inicioX - 20, pisoY - 110);
            
            // Línea de meta (si no es modo infinito)
            if (!modoInfinito) {
                int metaX = inicioX + metrosAPixeles(distancia);
                if (metaX < ancho) {
                    g2d.setColor(new Color(231, 76, 60));
                    g2d.drawLine(metaX, pisoY - 100, metaX, pisoY);
                    g2d.drawString("META", metaX - 15, pisoY - 110);
                }
            }
            
            // Objeto móvil
            int objetoX = 50 + metrosAPixeles(posicionX);
            int objetoY = pisoY - 25;
            
            // Cambiar color si alcanzó el objetivo
            Color colorObjeto = objetivoAlcanzado ? 
                               new Color(46, 204, 113) : new Color(52, 152, 219);
            dibujarObjeto(g2d, objetoX, objetoY, 15, colorObjeto);
            
            // Vector velocidad
            if (mostrarVectores && velocidadActual > 0) {
                g2d.setColor(new Color(39, 174, 96));
                g2d.setStroke(new BasicStroke(3));
                int longitudFlecha = (int)(velocidadActual * 10);
                g2d.drawLine(objetoX, objetoY, objetoX + longitudFlecha, objetoY);
                // Punta de flecha
                g2d.drawLine(objetoX + longitudFlecha, objetoY, 
                            objetoX + longitudFlecha - 8, objetoY - 5);
                g2d.drawLine(objetoX + longitudFlecha, objetoY, 
                            objetoX + longitudFlecha - 8, objetoY + 5);
                
                // Etiqueta del vector
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString("v = " + String.format("%.1f", velocidadActual) + " m/s", 
                              objetoX + longitudFlecha + 5, objetoY - 5);
            }
            
            // Trayectoria recorrida (línea punteada)
            g2d.setColor(new Color(41, 128, 185, 100));
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                         BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            g2d.drawLine(inicioX, pisoY - 25, objetoX, pisoY - 25);
            
            // Calcular datos
            double distanciaRecorrida = posicionX - x0;
            double tiempoEstimado = distancia / velocidadActual;
            double velocidadMedia = distanciaRecorrida / (tiempoTotal > 0 ? tiempoTotal : 1);
            
            // Información en pantalla
            String estadoStr = objetivoAlcanzado ? " ✓ META ALCANZADA" : 
                              (tiempoLimite > 0 && tiempoTotal >= tiempoLimite) ? " ⏰ TIEMPO LÍMITE" :
                              modoInfinito ? " ∞ MODO INFINITO" : " 🏃 EN MOVIMIENTO";
            
            String[] info = {
                "⏱️ Tiempo: " + String.format("%.2f s", tiempoTotal) + estadoStr,
                "📍 Posición: " + String.format("%.2f m", posicionX),
                "📏 Distancia recorrida: " + String.format("%.2f m", distanciaRecorrida),
                "➡️ Velocidad: " + String.format("%.2f m/s", velocidadActual) + " (constante)",
                "📊 Velocidad media: " + String.format("%.2f m/s", velocidadMedia),
                "⚡ Aceleración: 0.00 m/s²",
                "",
                "🎯 Distancia objetivo: " + String.format("%.2f m", distancia),
                "⏰ Tiempo estimado: " + String.format("%.2f s", tiempoEstimado),
                tiempoLimite > 0 ? "⏳ Tiempo límite: " + String.format("%.2f s", tiempoLimite) : ""
            };
            dibujarInfo(g2d, info, 20, 20);
            
            // Mostrar progreso si no es modo infinito
            if (!modoInfinito && distancia > 0) {
                int barraX = ancho - 220;
                int barraY = 30;
                int barraAncho = 200;
                int barraAlto = 25;
                
                double progreso = Math.min(1.0, distanciaRecorrida / distancia);
                
                // Fondo de la barra
                g2d.setColor(new Color(236, 240, 241));
                g2d.fillRoundRect(barraX, barraY, barraAncho, barraAlto, 10, 10);
                
                // Progreso
                g2d.setColor(objetivoAlcanzado ? new Color(46, 204, 113) : new Color(52, 152, 219));
                g2d.fillRoundRect(barraX, barraY, (int)(barraAncho * progreso), barraAlto, 10, 10);
                
                // Borde
                g2d.setColor(new Color(189, 195, 199));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(barraX, barraY, barraAncho, barraAlto, 10, 10);
                
                // Texto de progreso
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                String progresoTexto = String.format("%.1f%%", progreso * 100);
                FontMetrics fm = g2d.getFontMetrics();
                int textoX = barraX + (barraAncho - fm.stringWidth(progresoTexto)) / 2;
                int textoY = barraY + (barraAlto + fm.getAscent()) / 2 - 2;
                g2d.drawString(progresoTexto, textoX, textoY);
            }
        }
    }
}
