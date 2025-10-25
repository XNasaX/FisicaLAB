package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.Escenario;
import com.mycompany.fisicalab.core.MotorSimulacion;
import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;

/**
 * Simulación de Movimiento Rectilíneo Uniforme (MRU)
 */
public class SimulacionMRU extends JPanel {
    
    private SimuladorFrame frame;
    private EscenarioMRU escenario;
    private MotorSimulacion motor;
    
    private JButton btnIniciar, btnPausar, btnReiniciar, btnVolver;
    private JSlider sliderVelocidad;
    private JLabel labelVelocidad;
    
    private double velocidad = 5.0; // m/s
    
    public SimulacionMRU(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(UIHelper.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        motor = new MotorSimulacion(30); // 30ms por frame
        escenario = new EscenarioMRU(900, 500);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Panel superior con título
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSuperior.setOpaque(false);
        JLabel titulo = UIHelper.crearTitulo("Movimiento Rectilíneo Uniforme");
        panelSuperior.add(titulo);
        
        // Panel izquierdo con controles
        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setBackground(Color.WHITE);
        panelControles.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panelControles.setPreferredSize(new Dimension(250, 500));
        
        // Controles de velocidad
        JLabel labelTituloVel = new JLabel("Velocidad (m/s):");
        labelTituloVel.setFont(new Font("Arial", Font.BOLD, 14));
        labelTituloVel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderVelocidad = UIHelper.crearSlider(1, 20, 5);
        sliderVelocidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderVelocidad.addChangeListener(e -> {
            velocidad = sliderVelocidad.getValue();
            labelVelocidad.setText(String.format("v = %.1f m/s", velocidad));
            escenario.setVelocidad(velocidad);
        });
        
        labelVelocidad = new JLabel(String.format("v = %.1f m/s", velocidad));
        labelVelocidad.setFont(new Font("Monospaced", Font.PLAIN, 14));
        labelVelocidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Botones de control
        btnIniciar = UIHelper.crearBotonRedondeado("Iniciar", UIHelper.COLOR_EXITO);
        btnIniciar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnIniciar.addActionListener(e -> iniciarSimulacion());
        
        btnPausar = UIHelper.crearBotonRedondeado("Pausar", UIHelper.COLOR_ADVERTENCIA);
        btnPausar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnPausar.setEnabled(false);
        btnPausar.addActionListener(e -> pausarSimulacion());
        
        btnReiniciar = UIHelper.crearBotonRedondeado("Reiniciar", UIHelper.COLOR_SECUNDARIO);
        btnReiniciar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnReiniciar.addActionListener(e -> reiniciarSimulacion());
        
        btnVolver = UIHelper.crearBotonRedondeado("Volver al Menú", UIHelper.COLOR_PELIGRO);
        btnVolver.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnVolver.addActionListener(e -> frame.mostrarMenuPrincipal());
        
        // Información teórica
        JTextArea textoInfo = new JTextArea();
        textoInfo.setText("MRU:\n" +
                         "• Velocidad constante\n" +
                         "• Aceleración = 0\n" +
                         "• x = x₀ + v·t\n\n" +
                         "Ajusta la velocidad y\n" +
                         "observa el movimiento.");
        textoInfo.setEditable(false);
        textoInfo.setWrapStyleWord(true);
        textoInfo.setLineWrap(true);
        textoInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        textoInfo.setBackground(new Color(236, 240, 241));
        textoInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textoInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Agregar componentes al panel de controles
        panelControles.add(labelTituloVel);
        panelControles.add(Box.createRigidArea(new Dimension(0, 5)));
        panelControles.add(sliderVelocidad);
        panelControles.add(labelVelocidad);
        panelControles.add(Box.createRigidArea(new Dimension(0, 20)));
        panelControles.add(btnIniciar);
        panelControles.add(Box.createRigidArea(new Dimension(0, 10)));
        panelControles.add(btnPausar);
        panelControles.add(Box.createRigidArea(new Dimension(0, 10)));
        panelControles.add(btnReiniciar);
        panelControles.add(Box.createRigidArea(new Dimension(0, 20)));
        panelControles.add(textoInfo);
        panelControles.add(Box.createVerticalGlue());
        panelControles.add(btnVolver);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(panelControles, BorderLayout.WEST);
        add(escenario, BorderLayout.CENTER);
    }
    
    private void iniciarSimulacion() {
        escenario.setVelocidad(velocidad);
        motor.iniciar(e -> {
            escenario.actualizar();
            escenario.repaint();
        });
        
        btnIniciar.setEnabled(false);
        btnPausar.setEnabled(true);
        sliderVelocidad.setEnabled(false);
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
    }
    
    // Clase interna para el escenario MRU
    private class EscenarioMRU extends Escenario {
        
        private double posicionX;
        private double velocidadActual;
        private double tiempoTotal;
        
        public EscenarioMRU(int ancho, int alto) {
            super(ancho, alto);
            this.motor = SimulacionMRU.this.motor;
            reiniciar();
        }
        
        public void setVelocidad(double v) {
            this.velocidadActual = v;
        }
        
        public void reiniciar() {
            posicionX = 0;
            tiempoTotal = 0;
        }
        
        @Override
        public void actualizar() {
            double dt = motor.getDeltaTime();
            tiempoTotal = motor.getTiempoTranscurrido();
            
            // Cálculo MRU: x = x0 + v*t
            posicionX = MotorSimulacion.calcularPosicionMRU(0, velocidadActual, tiempoTotal);
            
            // Reiniciar si sale de la pantalla
            if (metrosAPixeles(posicionX) > ancho - 50) {
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
            
            // Objeto móvil
            int objetoX = 50 + metrosAPixeles(posicionX);
            int objetoY = pisoY - 25;
            dibujarObjeto(g2d, objetoX, objetoY, 15, new Color(231, 76, 60));
            
            // Vector velocidad
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
            g2d.drawString("v", objetoX + longitudFlecha + 5, objetoY - 5);
            
            // Información en pantalla
            String[] info = {
                String.format("Tiempo: %.2f s", tiempoTotal),
                String.format("Posición: %.2f m", posicionX),
                String.format("Velocidad: %.2f m/s", velocidadActual),
                String.format("Aceleración: 0.00 m/s²")
            };
            dibujarInfo(g2d, info, 20, 20);
        }
    }
}