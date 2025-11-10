package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.Escenario;
import com.mycompany.fisicalab.core.MotorSimulacion;
import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Simulación de Movimiento Rectilíneo Uniforme (MRU) v3.0
 * Nueva interfaz reorganizada
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
    
    private double velocidad = 5.0;
    private double posicionInicial = 0.0;
    private double distanciaObjetivo = 50.0;
    private double tiempoObjetivo = 0.0;
    private int velocidadSimulacion = 30;
    private boolean mostrarVectores = true;
    private boolean modoInfinito = false;
    
    public SimulacionMRU(SimuladorFrame frame) {
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
        // ===== PANEL SUPERIOR (TÍTULO) =====
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 15, 5, 15));
        
        JLabel titulo = new JLabel("Movimiento Rectilíneo Uniforme v2.0");
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
        
        // Velocidad
        agregarControl(panelControlesInterno, "Velocidad (m/s):");
        sliderVelocidad = UIHelper.crearSlider(1, 30, 5);
        sliderVelocidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderVelocidad.addChangeListener(e -> {
            velocidad = sliderVelocidad.getValue();
            labelVelocidad.setText(String.format("v = %.1f m/s", velocidad));
        });
        labelVelocidad = new JLabel(String.format("v = %.1f m/s", velocidad));
        labelVelocidad.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelVelocidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelControlesInterno.add(sliderVelocidad);
        panelControlesInterno.add(labelVelocidad);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Posición Inicial
        agregarControl(panelControlesInterno, "Posición Inicial (m):");
        txtPosicionInicial = UIHelper.crearCampoTexto("0.0");
        txtPosicionInicial.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        txtPosicionInicial.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPosicionInicial.addActionListener(e -> actualizarPosicionInicial());
        panelControlesInterno.add(txtPosicionInicial);
        panelControlesInterno.add(Box.createRigidArea(new Dimension(0, 12)));
        
        // Distancia Objetivo
        agregarControl(panelControlesInterno, "Distancia Objetivo (m):");
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
        });
        labelDistancia = new JLabel(String.format("d = %.1f m", distanciaObjetivo));
        labelDistancia.setFont(new Font("Monospaced", Font.PLAIN, 12));
        labelDistancia.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelControlesInterno.add(sliderDistancia);
        panelControlesInterno.add(labelDistancia);
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
        escenario = new EscenarioMRU(900, 500);
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
        
        JLabel labelMru = new JLabel("□ MRU:");
        labelMru.setFont(new Font("Arial", Font.BOLD, 11));
        panel.add(labelMru);
        
        String[] formulas = {
            "• Velocidad constante",
            "• Aceleración = 0",
            "• x = x₀ + v·t",
            "• Movimiento uniforme"
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
        }
    }
    
    private void iniciarSimulacion() {
        motor = new MotorSimulacion(velocidadSimulacion);
        escenario.setParametros(velocidad, posicionInicial, distanciaObjetivo, 
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
        sliderVelocidad.setEnabled(!deshabilitar);
        txtPosicionInicial.setEnabled(!deshabilitar);
        sliderDistancia.setEnabled(!deshabilitar);
        txtTiempoObjetivo.setEnabled(!deshabilitar);
        sliderVelocidadSim.setEnabled(!deshabilitar);
        chkModoInfinito.setEnabled(!deshabilitar);
    }
    
    // Clase interna EscenarioMRU
    private class EscenarioMRU extends Escenario {
        
        private MotorSimulacion motorLocal;
        private double posicionX, velocidadActual, tiempoTotal;
        private double x0, distancia, tiempoLimite;
        private boolean mostrarVectores, modoInfinito, objetivoAlcanzado;
        
        public EscenarioMRU(int ancho, int alto) {
            super(ancho, alto);
            reiniciar();
        }
        
        public void setMotor(MotorSimulacion motor) {
            this.motorLocal = motor;
        }
        
        public void setParametros(double vel, double pos, double dist, double tiempo, 
                                  boolean vectores, boolean infinito) {
            this.velocidadActual = vel;
            this.x0 = pos;
            this.distancia = dist;
            this.tiempoLimite = tiempo;
            this.mostrarVectores = vectores;
            this.modoInfinito = infinito;
        }
        
        public void reiniciar() {
            posicionX = x0;
            tiempoTotal = 0;
            objetivoAlcanzado = false;
        }
        
        @Override
        public void actualizar() {
            if (motorLocal == null) return;
            
            tiempoTotal = motorLocal.getTiempoTranscurrido();
            posicionX = MotorSimulacion.calcularPosicionMRU(x0, velocidadActual, tiempoTotal);
            
            if (!modoInfinito && (posicionX - x0) >= distancia) {
                objetivoAlcanzado = true;
                motorLocal.detener();
            }
            
            if (tiempoLimite > 0 && tiempoTotal >= tiempoLimite) {
                motorLocal.detener();
            }
            
            if (modoInfinito && metrosAPixeles(posicionX - x0) > ancho - 100) {
                motorLocal.reiniciar();
                reiniciar();
            }
        }
        
        @Override
        protected void dibujar(Graphics2D g2d) {
            dibujarCuadricula(g2d, 50);
            
            int pisoY = alto - 80;
            g2d.setColor(new Color(52, 73, 94));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(0, pisoY, ancho, pisoY);
            
            int inicioX = 50 + metrosAPixeles(x0);
            g2d.setColor(new Color(46, 204, 113));
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                         BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
            g2d.drawLine(inicioX, pisoY - 100, inicioX, pisoY);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("INICIO", inicioX - 20, pisoY - 110);
            
            if (!modoInfinito) {
                int metaX = inicioX + metrosAPixeles(distancia);
                if (metaX < ancho) {
                    g2d.setColor(new Color(231, 76, 60));
                    g2d.drawLine(metaX, pisoY - 100, metaX, pisoY);
                    g2d.drawString("META", metaX - 15, pisoY - 110);
                }
            }
            
            int objetoX = 50 + metrosAPixeles(posicionX);
            int objetoY = pisoY - 25;
            
            Color colorObjeto = objetivoAlcanzado ? 
                               new Color(46, 204, 113) : new Color(52, 152, 219);
            dibujarObjeto(g2d, objetoX, objetoY, 15, colorObjeto);
            
            if (mostrarVectores && velocidadActual > 0) {
                g2d.setColor(new Color(39, 174, 96));
                g2d.setStroke(new BasicStroke(3));
                int longitudFlecha = (int)(velocidadActual * 10);
                g2d.drawLine(objetoX, objetoY, objetoX + longitudFlecha, objetoY);
                g2d.drawLine(objetoX + longitudFlecha, objetoY, 
                            objetoX + longitudFlecha - 8, objetoY - 5);
                g2d.drawLine(objetoX + longitudFlecha, objetoY, 
                            objetoX + longitudFlecha - 8, objetoY + 5);
                
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString("v = " + String.format("%.1f", velocidadActual) + " m/s", 
                              objetoX + longitudFlecha + 5, objetoY - 5);
            }
            
            g2d.setColor(new Color(41, 128, 185, 100));
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                         BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            g2d.drawLine(inicioX, pisoY - 25, objetoX, pisoY - 25);
            
            double distanciaRecorrida = posicionX - x0;
            double velocidadMedia = distanciaRecorrida / (tiempoTotal > 0 ? tiempoTotal : 1);
            
            String estadoStr = objetivoAlcanzado ? " ✓ META ALCANZADA" : 
                              (tiempoLimite > 0 && tiempoTotal >= tiempoLimite) ? " ⏰ TIEMPO LÍMITE" :
                              modoInfinito ? " ∞ MODO INFINITO" : " 🏃 EN MOVIMIENTO";
            
            String[] info = {
                "⏱️ Tiempo: " + String.format("%.2f s", tiempoTotal) + estadoStr,
                "📍 Posición: " + String.format("%.2f m", posicionX),
                "📏 Distancia recorrida: " + String.format("%.2f m", distanciaRecorrida),
                "➡️ Velocidad: " + String.format("%.2f m/s", velocidadActual) + " (constante)",
                "📊 Velocidad media: " + String.format("%.2f m/s", velocidadMedia),
                "⚡ Aceleración: 0.00 m/s²"
            };
            dibujarInfo(g2d, info, 20, 20);
            
            if (!modoInfinito && distancia > 0) {
                int barraX = ancho - 220;
                int barraY = 30;
                int barraAncho = 200;
                int barraAlto = 25;
                
                double progreso = Math.min(1.0, distanciaRecorrida / distancia);
                
                g2d.setColor(new Color(236, 240, 241));
                g2d.fillRoundRect(barraX, barraY, barraAncho, barraAlto, 10, 10);
                
                g2d.setColor(objetivoAlcanzado ? new Color(46, 204, 113) : new Color(52, 152, 219));
                g2d.fillRoundRect(barraX, barraY, (int)(barraAncho * progreso), barraAlto, 10, 10);
                
                g2d.setColor(new Color(189, 195, 199));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(barraX, barraY, barraAncho, barraAlto, 10, 10);
                
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

