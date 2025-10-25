package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.Escenario;
import com.mycompany.fisicalab.core.MotorSimulacion;
import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;

/**
 * Simulación de Caída Libre
 */
public class SimulacionCaidaLibre extends JPanel {
    
    private SimuladorFrame frame;
    private EscenarioCaidaLibre escenario;
    private MotorSimulacion motor;
    
    private JButton btnIniciar, btnPausar, btnReiniciar, btnVolver;
    private JSlider sliderAltura;
    private JLabel labelAltura;
    
    private double alturaInicial = 50.0; // metros
    
    public SimulacionCaidaLibre(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(UIHelper.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        motor = new MotorSimulacion(30);
        escenario = new EscenarioCaidaLibre(900, 600);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSuperior.setOpaque(false);
        JLabel titulo = UIHelper.crearTitulo("Caída Libre");
        panelSuperior.add(titulo);
        
        // Panel de controles
        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setBackground(Color.WHITE);
        panelControles.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panelControles.setPreferredSize(new Dimension(250, 600));
        
        // Control de altura
        JLabel labelTituloAltura = new JLabel("Altura Inicial (m):");
        labelTituloAltura.setFont(new Font("Arial", Font.BOLD, 14));
        labelTituloAltura.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderAltura = UIHelper.crearSlider(10, 100, 50);
        sliderAltura.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderAltura.addChangeListener(e -> {
            alturaInicial = sliderAltura.getValue();
            labelAltura.setText(String.format("h₀ = %.1f m", alturaInicial));
            escenario.setAlturaInicial(alturaInicial);
        });
        
        labelAltura = new JLabel(String.format("h₀ = %.1f m", alturaInicial));
        labelAltura.setFont(new Font("Monospaced", Font.PLAIN, 14));
        labelAltura.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Botones
        btnIniciar = UIHelper.crearBotonRedondeado("Soltar", UIHelper.COLOR_EXITO);
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
        
        // Información
        JTextArea textoInfo = new JTextArea();
        textoInfo.setText("Caída Libre:\n" +
                         "• v₀ = 0 m/s\n" +
                         "• a = g = 9.8 m/s²\n" +
                         "• y = y₀ - ½gt²\n" +
                         "• v = gt\n\n" +
                         "El objeto cae bajo\n" +
                         "la influencia de la\n" +
                         "gravedad únicamente.");
        textoInfo.setEditable(false);
        textoInfo.setWrapStyleWord(true);
        textoInfo.setLineWrap(true);
        textoInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        textoInfo.setBackground(new Color(236, 240, 241));
        textoInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textoInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Agregar componentes
        panelControles.add(labelTituloAltura);
        panelControles.add(Box.createRigidArea(new Dimension(0, 5)));
        panelControles.add(sliderAltura);
        panelControles.add(labelAltura);
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
        escenario.setAlturaInicial(alturaInicial);
        motor.iniciar(e -> {
            escenario.actualizar();
            escenario.repaint();
        });
        
        btnIniciar.setEnabled(false);
        btnPausar.setEnabled(true);
        sliderAltura.setEnabled(false);
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
    }
    
    // Escenario interno
    private class EscenarioCaidaLibre extends Escenario {
        
        private double posicionY;
        private double velocidadY;
        private double altura;
        private double tiempoTotal;
        private boolean enSuelo;
        
        public EscenarioCaidaLibre(int ancho, int alto) {
            super(ancho, alto);
            this.motor = SimulacionCaidaLibre.this.motor;
            this.escalaPixeles = 5.0; // 5 píxeles por metro
            reiniciar();
        }
        
        public void setAlturaInicial(double h) {
            this.altura = h;
        }
        
        public void reiniciar() {
            posicionY = altura;
            velocidadY = 0;
            tiempoTotal = 0;
            enSuelo = false;
        }
        
        @Override
        public void actualizar() {
            if (enSuelo) return;
            
            double dt = motor.getDeltaTime();
            tiempoTotal = motor.getTiempoTranscurrido();
            
            // Cálculo caída libre
            posicionY = altura - 0.5 * MotorSimulacion.GRAVEDAD * tiempoTotal * tiempoTotal;
            velocidadY = MotorSimulacion.GRAVEDAD * tiempoTotal;
            
            // Verificar impacto con el suelo
            if (posicionY <= 0) {
                posicionY = 0;
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
            for (int i = 0; i <= 100; i += 10) {
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
            
            // Césped
            g2d.setColor(new Color(76, 153, 0));
            g2d.fillRect(0, sueloY - 10, ancho, 10);
            
            // Objeto cayendo
            int objetoX = ancho / 2;
            int objetoY = sueloY - metrosAPixeles(posicionY) - 15;
            dibujarObjeto(g2d, objetoX, objetoY, 15, new Color(231, 76, 60));
            
            // Vector velocidad
            if (!enSuelo && velocidadY > 0) {
                g2d.setColor(new Color(39, 174, 96));
                g2d.setStroke(new BasicStroke(3));
                int longitudFlecha = Math.min((int)(velocidadY * 5), 100);
                g2d.drawLine(objetoX, objetoY, objetoX, objetoY + longitudFlecha);
                // Punta
                g2d.drawLine(objetoX, objetoY + longitudFlecha, 
                            objetoX - 5, objetoY + longitudFlecha - 8);
                g2d.drawLine(objetoX, objetoY + longitudFlecha, 
                            objetoX + 5, objetoY + longitudFlecha - 8);
                
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString("v", objetoX + 10, objetoY + longitudFlecha / 2);
            }
            
            // Línea punteada de altura inicial
            g2d.setColor(new Color(100, 100, 100, 150));
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                                         BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
            int alturaInicialY = sueloY - metrosAPixeles(altura) - 15;
            g2d.drawLine(100, alturaInicialY, ancho - 100, alturaInicialY);
            
            // Información
            String estado = enSuelo ? " (EN SUELO)" : "";
            String[] info = {
                String.format("Tiempo: %.2f s%s", tiempoTotal, estado),
                String.format("Altura: %.2f m", Math.max(0, posicionY)),
                String.format("Velocidad: %.2f m/s", velocidadY),
                String.format("Aceleración: %.2f m/s²", MotorSimulacion.GRAVEDAD)
            };
            dibujarInfo(g2d, info, 20, 20);
        }
    }
}