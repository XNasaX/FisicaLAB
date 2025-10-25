package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.Escenario;
import com.mycompany.fisicalab.core.MotorSimulacion;
import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Simulación de Tiro Parabólico
 */
public class SimulacionTiroParabolico extends JPanel {
    
    private SimuladorFrame frame;
    private EscenarioTiroParabolico escenario;
    private MotorSimulacion motor;
    
    private JButton btnIniciar, btnPausar, btnReiniciar, btnVolver;
    private JSlider sliderVelocidad, sliderAngulo;
    private JLabel labelVelocidad, labelAngulo;
    
    private double velocidadInicial = 20.0; // m/s
    private double angulo = 45.0; // grados
    
    public SimulacionTiroParabolico(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBackground(UIHelper.COLOR_FONDO);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        motor = new MotorSimulacion(30);
        escenario = new EscenarioTiroParabolico(900, 600);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Panel superior
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSuperior.setOpaque(false);
        JLabel titulo = UIHelper.crearTitulo("Tiro Parabólico");
        panelSuperior.add(titulo);
        
        // Panel de controles
        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));
        panelControles.setBackground(Color.WHITE);
        panelControles.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panelControles.setPreferredSize(new Dimension(270, 600));
        
        // Control de velocidad
        JLabel labelTituloVel = new JLabel("Velocidad Inicial (m/s):");
        labelTituloVel.setFont(new Font("Arial", Font.BOLD, 14));
        labelTituloVel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderVelocidad = UIHelper.crearSlider(5, 50, 20);
        sliderVelocidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderVelocidad.addChangeListener(e -> {
            velocidadInicial = sliderVelocidad.getValue();
            labelVelocidad.setText(String.format("v₀ = %.1f m/s", velocidadInicial));
            escenario.setParametros(velocidadInicial, angulo);
        });
        
        labelVelocidad = new JLabel(String.format("v₀ = %.1f m/s", velocidadInicial));
        labelVelocidad.setFont(new Font("Monospaced", Font.PLAIN, 14));
        labelVelocidad.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Control de ángulo
        JLabel labelTituloAng = new JLabel("Ángulo (grados):");
        labelTituloAng.setFont(new Font("Arial", Font.BOLD, 14));
        labelTituloAng.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sliderAngulo = UIHelper.crearSlider(0, 90, 45);
        sliderAngulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderAngulo.addChangeListener(e -> {
            angulo = sliderAngulo.getValue();
            labelAngulo.setText(String.format("θ = %.1f°", angulo));
            escenario.setParametros(velocidadInicial, angulo);
        });
        
        labelAngulo = new JLabel(String.format("θ = %.1f°", angulo));
        labelAngulo.setFont(new Font("Monospaced", Font.PLAIN, 14));
        labelAngulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Botones
        btnIniciar = UIHelper.crearBotonRedondeado("Lanzar", UIHelper.COLOR_EXITO);
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
        textoInfo.setText("Tiro Parabólico:\n" +
                         "• Componentes:\n" +
                         "  vₓ = v₀·cos(θ)\n" +
                         "  vᵧ = v₀·sin(θ)\n\n" +
                         "• Horizontal (MRU):\n" +
                         "  x = vₓ·t\n\n" +
                         "• Vertical (MRUV):\n" +
                         "  y = vᵧ·t - ½g·t²\n\n" +
                         "Ajusta velocidad y\n" +
                         "ángulo para ver la\n" +
                         "trayectoria parabólica.");
        textoInfo.setEditable(false);
        textoInfo.setWrapStyleWord(true);
        textoInfo.setLineWrap(true);
        textoInfo.setFont(new Font("Arial", Font.PLAIN, 11));
        textoInfo.setBackground(new Color(236, 240, 241));
        textoInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textoInfo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Agregar componentes
        panelControles.add(labelTituloVel);
        panelControles.add(Box.createRigidArea(new Dimension(0, 5)));
        panelControles.add(sliderVelocidad);
        panelControles.add(labelVelocidad);
        panelControles.add(Box.createRigidArea(new Dimension(0, 15)));
        panelControles.add(labelTituloAng);
        panelControles.add(Box.createRigidArea(new Dimension(0, 5)));
        panelControles.add(sliderAngulo);
        panelControles.add(labelAngulo);
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
        escenario.setParametros(velocidadInicial, angulo);
        motor.iniciar(e -> {
            escenario.actualizar();
            escenario.repaint();
        });
        
        btnIniciar.setEnabled(false);
        btnPausar.setEnabled(true);
        sliderVelocidad.setEnabled(false);
        sliderAngulo.setEnabled(false);
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
    }
    
    // Escenario interno
    private class EscenarioTiroParabolico extends Escenario {
        
        private double posicionX, posicionY;
        private double velocidadX, velocidadY;
        private double v0, theta;
        private double tiempoTotal;
        private boolean enSuelo;
        private List<Point> trayectoria;
        
        public EscenarioTiroParabolico(int ancho, int alto) {
            super(ancho, alto);
            this.motor = SimulacionTiroParabolico.this.motor;
            this.escalaPixeles = 8.0; // 8 píxeles por metro
            this.trayectoria = new ArrayList<>();
            reiniciar();
        }
        
        public void setParametros(double velocidad, double angulo) {
            this.v0 = velocidad;
            this.theta = angulo;
        }
        
        public void reiniciar() {
            posicionX = 0;
            posicionY = 0;
            tiempoTotal = 0;
            enSuelo = false;
            trayectoria.clear();
            
            // Calcular componentes de velocidad
            double radianes = Math.toRadians(theta);
            velocidadX = v0 * Math.cos(radianes);
            velocidadY = v0 * Math.sin(radianes);
        }
        
        @Override
        public void actualizar() {
            if (enSuelo) return;
            
            double dt = motor.getDeltaTime();
            tiempoTotal = motor.getTiempoTranscurrido();
            
            // Movimiento horizontal (MRU)
            posicionX = velocidadX * tiempoTotal;
            
            // Movimiento vertical (MRUV)
            posicionY = velocidadY * tiempoTotal - 0.5 * MotorSimulacion.GRAVEDAD * tiempoTotal * tiempoTotal;
            
            // Guardar punto de trayectoria
            if (posicionY >= 0) {
                int screenX = 100 + metrosAPixeles(posicionX);
                int screenY = alto - 100 - metrosAPixeles(posicionY);
                trayectoria.add(new Point(screenX, screenY));
            }
            
            // Verificar impacto con el suelo
            if (posicionY <= 0) {
                posicionY = 0;
                enSuelo = true;
                motor.detener();
            }
        }
        
        @Override
        protected void dibujar(Graphics2D g2d) {
            // Fondo con cielo degradado
            GradientPaint cielo = new GradientPaint(0, 0, new Color(135, 206, 250),
                                                     0, alto, new Color(240, 248, 255));
            g2d.setPaint(cielo);
            g2d.fillRect(0, 0, ancho, alto);
            
            // Cuadrícula
            dibujarCuadricula(g2d, 50);
            
            // Suelo
            int sueloY = alto - 100;
            g2d.setColor(new Color(101, 67, 33));
            g2d.fillRect(0, sueloY, ancho, 100);
            
            g2d.setColor(new Color(76, 153, 0));
            g2d.fillRect(0, sueloY - 10, ancho, 10);
            
            // Cañón/Lanzador
            int cannonX = 100;
            int cannonY = sueloY;
            
            // Base del cañón
            g2d.setColor(new Color(52, 73, 94));
            g2d.fillRect(cannonX - 20, cannonY - 20, 40, 20);
            
            // Tubo del cañón
            g2d.setStroke(new BasicStroke(8));
            double radianes = Math.toRadians(theta);
            int tuboLargo = 40;
            int tuboX2 = cannonX + (int)(tuboLargo * Math.cos(radianes));
            int tuboY2 = cannonY - (int)(tuboLargo * Math.sin(radianes));
            g2d.drawLine(cannonX, cannonY, tuboX2, tuboY2);
            
            // Indicador de ángulo
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(new Color(230, 126, 34));
            int radioArco = 30;
            g2d.drawArc(cannonX - radioArco, cannonY - radioArco, 
                       radioArco * 2, radioArco * 2, 
                       0, (int)theta);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(String.format("%.0f°", theta), 
                          cannonX + 35, cannonY - 10);
            
            // Trayectoria predicha (línea punteada)
            if (!motor.isEnEjecucion() && !enSuelo) {
                g2d.setColor(new Color(100, 100, 100, 100));
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, 
                                             BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0));
                
                Path2D trayectoriaPredicha = new Path2D.Double();
                boolean primero = true;
                
                for (double t = 0; t <= 10; t += 0.1) {
                    double x = velocidadX * t;
                    double y = velocidadY * t - 0.5 * MotorSimulacion.GRAVEDAD * t * t;
                    
                    if (y < 0) break;
                    
                    int screenX = 100 + metrosAPixeles(x);
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
            
            // Trayectoria recorrida (línea sólida)
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
                int objetoX = 100 + metrosAPixeles(posicionX);
                int objetoY = sueloY - metrosAPixeles(posicionY);
                dibujarObjeto(g2d, objetoX, objetoY, 12, new Color(231, 76, 60));
                
                // Vectores de velocidad
                if (motor.isEnEjecucion()) {
                    // Velocidad horizontal (verde)
                    g2d.setColor(new Color(39, 174, 96));
                    g2d.setStroke(new BasicStroke(3));
                    int vxLargo = (int)(velocidadX * 3);
                    g2d.drawLine(objetoX, objetoY, objetoX + vxLargo, objetoY);
                    // Flecha
                    g2d.drawLine(objetoX + vxLargo, objetoY, 
                                objetoX + vxLargo - 6, objetoY - 4);
                    g2d.drawLine(objetoX + vxLargo, objetoY, 
                                objetoX + vxLargo - 6, objetoY + 4);
                    g2d.drawString("vₓ", objetoX + vxLargo + 5, objetoY - 5);
                    
                    // Velocidad vertical (azul)
                    double vyActual = velocidadY - MotorSimulacion.GRAVEDAD * tiempoTotal;
                    g2d.setColor(new Color(41, 128, 185));
                    int vyLargo = (int)(Math.abs(vyActual) * 3);
                    int direccionY = vyActual < 0 ? 1 : -1;
                    g2d.drawLine(objetoX, objetoY, objetoX, objetoY + direccionY * vyLargo);
                    // Flecha
                    if (vyLargo > 0) {
                        g2d.drawLine(objetoX, objetoY + direccionY * vyLargo, 
                                    objetoX - 4, objetoY + direccionY * vyLargo - direccionY * 6);
                        g2d.drawLine(objetoX, objetoY + direccionY * vyLargo, 
                                    objetoX + 4, objetoY + direccionY * vyLargo - direccionY * 6);
                    }
                    g2d.drawString("vᵧ", objetoX + 10, objetoY + direccionY * vyLargo / 2);
                }
            }
            
            // Calcular datos importantes
            double alcanceMax = (v0 * v0 * Math.sin(2 * Math.toRadians(theta))) / MotorSimulacion.GRAVEDAD;
            double alturaMax = (v0 * v0 * Math.sin(Math.toRadians(theta)) * Math.sin(Math.toRadians(theta))) 
                              / (2 * MotorSimulacion.GRAVEDAD);
            double tiempoVuelo = (2 * v0 * Math.sin(Math.toRadians(theta))) / MotorSimulacion.GRAVEDAD;
            
            // Información en pantalla
            String estado = enSuelo ? " (IMPACTO)" : "";
            String[] info = {
                String.format("Tiempo: %.2f s%s", tiempoTotal, estado),
                String.format("Posición: (%.2f, %.2f) m", posicionX, Math.max(0, posicionY)),
                String.format("vₓ: %.2f m/s (constante)", velocidadX),
                String.format("vᵧ: %.2f m/s", velocidadY - MotorSimulacion.GRAVEDAD * tiempoTotal),
                "",
                String.format("Alcance máx: %.2f m", alcanceMax),
                String.format("Altura máx: %.2f m", alturaMax),
                String.format("Tiempo vuelo: %.2f s", tiempoVuelo)
            };
            dibujarInfo(g2d, info, 20, 20);
        }
    }
}