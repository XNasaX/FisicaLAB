package modulos;

import principal.MenuPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MRU extends JPanel implements ActionListener {

    private static final int MAX_TRAIL_POINTS = 300;

    Timer timer;

    double x = 0;

    double velocidad;

    double distanciaObjetivo;

    double tiempo = 0;

    JTextField txtVelocidad;

    JTextField txtDistancia;

    JLabel lblVelocidad;
    JLabel lblDistancia;
    JLabel lblTiempo;
    JLabel lblEstado;
    JTextArea areaInfo;

    ArrayList<Point> trail;

    String vehiculo = "Deportivo";

    JComboBox<String> comboVehiculo;

    boolean simulacionActiva = false;

    public MRU() {

        setLayout(null);

        setBackground(new Color(10, 10, 20));

        trail = new ArrayList<>();

        // =====================================================
        // BOTON VOLVER
        // =====================================================

        JButton btnVolver = new JButton("←");

        btnVolver.setBounds(20, 20, 60, 40);

        btnVolver.setBackground(new Color(35, 35, 50));

        btnVolver.setForeground(Color.WHITE);

        btnVolver.setFocusPainted(false);

        btnVolver.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                22
        ));

        add(btnVolver);

        btnVolver.addActionListener(e -> {

            Window ventana =
                    SwingUtilities.getWindowAncestor(this);

            ventana.dispose();

            if (MenuPrincipal.menu != null) {

                MenuPrincipal.menu.setVisible(true);
            }

        });

        // =====================================================
        // TITULO
        // =====================================================

        JLabel titulo =
                new JLabel("MOVIMIENTO RECTILINEO UNIFORME");

        titulo.setBounds(300, 20, 700, 40);

        titulo.setForeground(
                new Color(0, 220, 255)
        );

        titulo.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                28
        ));

        add(titulo);

        // =====================================================
        // PANEL CONTROL
        // =====================================================

        JPanel panelControl = new JPanel();

        panelControl.setLayout(null);

        panelControl.setBounds(20, 90, 250, 550);

        panelControl.setBackground(
                new Color(18, 18, 30)
        );

        add(panelControl);

        // =====================================================
        // VELOCIDAD
        // =====================================================

        JLabel lblV =
                new JLabel("Velocidad");

        lblV.setBounds(30, 70, 180, 30);

        lblV.setForeground(Color.WHITE);

        lblV.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(lblV);

        txtVelocidad = new JTextField();

        txtVelocidad.setBounds(30, 110, 170, 40);

        txtVelocidad.setBackground(
                new Color(30, 30, 45)
        );

        txtVelocidad.setForeground(Color.WHITE);

        txtVelocidad.setCaretColor(Color.WHITE);

        txtVelocidad.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(txtVelocidad);

        // =====================================================
        // DISTANCIA
        // =====================================================

        JLabel lblD =
                new JLabel("Distancia");

        lblD.setBounds(30, 180, 180, 30);

        lblD.setForeground(Color.WHITE);

        lblD.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(lblD);

        txtDistancia = new JTextField();

        txtDistancia.setBounds(30, 220, 170, 40);

        txtDistancia.setBackground(
                new Color(30, 30, 45)
        );

        txtDistancia.setForeground(Color.WHITE);

        txtDistancia.setCaretColor(Color.WHITE);

        txtDistancia.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(txtDistancia);

        // =====================================================
        // VEHICULO
        // =====================================================

        JLabel lblVehiculo =
                new JLabel("Vehiculo");

        lblVehiculo.setBounds(30, 290, 180, 30);

        lblVehiculo.setForeground(Color.WHITE);

        lblVehiculo.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(lblVehiculo);

        String[] vehiculos = {
                "Bicicleta",
                "Moto",
                "Deportivo"
        };

        comboVehiculo =
                new JComboBox<>(vehiculos);

        comboVehiculo.setBounds(
                30,
                330,
                170,
                40
        );

        comboVehiculo.setBackground(
                new Color(30,30,45)
        );

        comboVehiculo.setForeground(Color.WHITE);

        comboVehiculo.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                15
        ));

        panelControl.add(comboVehiculo);

        // =====================================================
        // BOTON
        // =====================================================

        JButton btnIniciar =
                new JButton("INICIAR");

        btnIniciar.setBounds(40, 430, 160, 55);

        btnIniciar.setBackground(
                new Color(0, 140, 255)
        );

        btnIniciar.setForeground(Color.WHITE);

        btnIniciar.setFocusPainted(false);

        btnIniciar.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                18
        ));

        panelControl.add(btnIniciar);

        JButton btnPausar =
                new JButton("PAUSAR");

        btnPausar.setBounds(40, 495, 160, 38);

        btnPausar.setBackground(new Color(35, 35, 55));

        btnPausar.setForeground(Color.WHITE);

        btnPausar.setFocusPainted(false);

        btnPausar.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                14
        ));

        panelControl.add(btnPausar);

        // =====================================================
        // PANEL DATOS
        // =====================================================

        JPanel panelDatos = new JPanel();

        panelDatos.setLayout(null);

        panelDatos.setBounds(1000, 90, 220, 500);

        panelDatos.setBackground(
                new Color(18, 18, 30)
        );

        add(panelDatos);

        lblVelocidad = crearDato(90);

        lblVelocidad.setText("Velocidad: 0");

        lblDistancia = crearDato(180);

        lblDistancia.setText("Distancia: 0");

        lblTiempo = crearDato(270);

        lblTiempo.setText("Tiempo: 0 s");

        lblEstado = crearDato(360);

        lblEstado.setText("Estado: Esperando");

        areaInfo = new JTextArea();

        areaInfo.setBounds(20, 415, 180, 70);

        areaInfo.setBackground(new Color(25,25,35));

        areaInfo.setForeground(new Color(0,255,180));

        areaInfo.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                12
        ));

        areaInfo.setEditable(false);

        areaInfo.setText("Formula: d = v * t\nProgreso: 0%\nTiempo estimado: 0 s");

        panelDatos.add(lblVelocidad);

        panelDatos.add(lblDistancia);

        panelDatos.add(lblTiempo);

        panelDatos.add(lblEstado);

        panelDatos.add(areaInfo);

        // =====================================================
        // TIMER
        // =====================================================

        timer = new Timer(16, this);

        btnIniciar.addActionListener(e -> {

            try {

                velocidad =
                        Double.parseDouble(
                                txtVelocidad.getText()
                        );

                distanciaObjetivo =
                        Double.parseDouble(
                                txtDistancia.getText()
                        );

                if (velocidad <= 0 || distanciaObjetivo <= 0) {

                    throw new IllegalArgumentException();
                }

                vehiculo =
                        comboVehiculo
                                .getSelectedItem()
                                .toString();

                if (vehiculo.equals("Bicicleta")) {

                    velocidad *= 0.5;

                }

                else if (vehiculo.equals("Moto")) {

                    velocidad *= 1.2;

                }

                else if (vehiculo.equals("Deportivo")) {

                    velocidad *= 1.8;

                }

                x = 0;

                tiempo = 0;

                trail.clear();

                lblEstado.setText(
                        "Estado: En movimiento"
                );

                simulacionActiva = true;

                timer.start();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(
                        null,
                        "Ingrese valores validos"
                );
            }
        });

        btnPausar.addActionListener(e -> {

            if (!simulacionActiva) {

                return;
            }

            if (timer.isRunning()) {

                timer.stop();

                lblEstado.setText("Estado: Pausado");

                btnPausar.setText("REANUDAR");
            }

            else {

                timer.start();

                lblEstado.setText("Estado: En movimiento");

                btnPausar.setText("PAUSAR");
            }
        });
    }

    @Override
    public void doLayout() {

        int ancho = getWidth();
        int alto = getHeight();

        if (getComponentCount() >= 4) {

            getComponent(0).setBounds(20, 20, 60, 40);
            getComponent(1).setBounds(Math.max(320, ancho / 2 - 380), 20, 760, 45);
            getComponent(2).setBounds(25, 90, 280, Math.max(540, alto - 135));
            getComponent(3).setBounds(Math.max(980, ancho - 300), 90, 270, 500);
        }
    }

    // =====================================================
    // LABELS
    // =====================================================

    private JLabel crearDato(int y) {

        JLabel lbl = new JLabel();

        lbl.setBounds(20, y, 200, 40);

        lbl.setForeground(Color.WHITE);

        lbl.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                15
        ));

        return lbl;
    }

    private void actualizarParametrosDesdeCampos() {

        try {

            double nuevaVelocidad =
                    Double.parseDouble(txtVelocidad.getText());

            double nuevaDistancia =
                    Double.parseDouble(txtDistancia.getText());

            if (nuevaVelocidad <= 0 || nuevaDistancia <= 0) {

                return;
            }

            String seleccionado =
                    comboVehiculo
                            .getSelectedItem()
                            .toString();

            if (seleccionado.equals("Bicicleta")) {

                nuevaVelocidad *= 0.5;
            }

            else if (seleccionado.equals("Moto")) {

                nuevaVelocidad *= 1.2;
            }

            else if (seleccionado.equals("Deportivo")) {

                nuevaVelocidad *= 1.8;
            }

            velocidad = nuevaVelocidad;

            distanciaObjetivo = nuevaDistancia;

            vehiculo = seleccionado;

        } catch (Exception ex) {

            // Se conserva el ultimo valor valido mientras el usuario escribe.
        }
    }

    private int sueloY() {

        return Math.max(520, getHeight() - 180);
    }

    private void dibujarPanel(Graphics2D g2, int x, int y, int w, int h, Color color) {

        g2.setColor(new Color(15, 20, 35, 190));

        g2.fillRoundRect(x, y, w, h, 16, 16);

        g2.setColor(color);

        g2.setStroke(new BasicStroke(2));

        g2.drawRoundRect(x, y, w, h, 16, 16);
    }

    private void dibujarAnalisis(Graphics2D g2) {

        int wPanel = 610;
        int hPanel = 165;
        int xPanel = Math.max(330, getWidth() - wPanel - 330);
        int yPanel = Math.max(430, getHeight() - hPanel - 40);

        dibujarPanel(g2, xPanel, yPanel, wPanel, hPanel, new Color(0, 220, 255));

        g2.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        g2.setColor(Color.CYAN);
        g2.drawString("GRAFICA DISTANCIA - TIEMPO", xPanel + 20, yPanel + 30);

        int gx = xPanel + 45;
        int gy = yPanel + 50;
        int gw = 520;
        int gh = 85;

        g2.setColor(new Color(255,255,255,90));
        g2.drawLine(gx, gy + gh, gx + gw, gy + gh);
        g2.drawLine(gx, gy, gx, gy + gh);

        double distanciaActual = x / 5;
        double progreso = 0;

        if (distanciaObjetivo > 0) {

            progreso = Math.min(1, distanciaActual / distanciaObjetivo);
        }

        int px = gx + (int)(gw * progreso);
        int py = gy + gh - (int)(gh * progreso);

        g2.setColor(new Color(0, 255, 255));
        g2.setStroke(new BasicStroke(4));
        g2.drawLine(gx, gy + gh, px, py);
        g2.fillOval(px - 5, py - 5, 10, 10);

        g2.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        g2.setColor(Color.WHITE);
        g2.drawString("0 s", gx - 5, gy + gh + 20);
        g2.drawString(String.format("%.1f m", distanciaActual), px - 20, py - 10);
        g2.drawString("En MRU la grafica es una recta: misma distancia por cada segundo.", gx, yPanel + 150);

    }

    // =====================================================
    // DIBUJO
    // =====================================================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // CIELO

        GradientPaint cielo =
                new GradientPaint(
                        0,
                        0,
                        new Color(10,10,25),
                        0,
                        700,
                        new Color(30,30,60)
                );

        g2.setPaint(cielo);

        g2.fillRect(
                0,
                0,
                getWidth(),
                getHeight()
        );

        // ESTRELLAS

        g2.setColor(Color.WHITE);

        for (int i = 0; i < 80; i++) {

            int sx = (i * 97) % 1300;

            int sy = (i * 53) % 300;

            g2.fillOval(sx, sy, 2, 2);
        }

        // POSTES

        int sueloY =
                sueloY();

        for (int i = -200; i < 2000; i += 180) {

            int posteX =
                    i - ((int)x % 180);

            g2.setColor(
                    new Color(50,50,50)
            );

            g2.fillRect(
                    posteX,
                    sueloY - 220,
                    10,
                    220
            );

            g2.setColor(
                    new Color(255,255,120,180)
            );

            g2.fillOval(
                    posteX - 15,
                    sueloY - 245,
                    40,
                    40
            );
        }

        // ARBOLES

        for (int i = -200; i < 2000; i += 250) {

            int arbolX =
                    i - ((int)x % 250);

            g2.setColor(
                    new Color(90,60,30)
            );

            g2.fillRect(
                    arbolX,
                    sueloY - 120,
                    20,
                    100
            );

            g2.setColor(
                    new Color(20,120,40)
            );

            g2.fillOval(
                    arbolX - 35,
                    sueloY - 170,
                    90,
                    70
            );
        }

        // PISTA

        g2.setColor(
                new Color(70,70,70)
        );

        g2.fillRect(
                0,
                sueloY,
                getWidth(),
                125
        );

        // LINEAS

        for (int i = -200; i < 2000; i += 120) {

            int lineaX =
                    i - ((int)x % 120);

            g2.setColor(Color.WHITE);

            g2.fillRect(
                    lineaX,
                    sueloY + 55,
                    70,
                    8
            );
        }

        // TRAIL

        for (Point p : trail) {

            g2.setColor(
                    new Color(0,255,255,80)
            );

            g2.fillOval(
                    p.x,
                    p.y,
                    8,
                    8
            );
        }

        int carX = Math.max(460, getWidth() / 2 - 40);

        int carY = sueloY - 70;

        // =====================================================
        // BICICLETA
        // =====================================================

        if (vehiculo.equals("Bicicleta")) {

            g2.setColor(Color.WHITE);

            g2.drawOval(
                    carX,
                    carY + 20,
                    35,
                    35
            );

            g2.drawOval(
                    carX + 60,
                    carY + 20,
                    35,
                    35
            );

            g2.drawLine(
                    carX + 18,
                    carY + 38,
                    carX + 48,
                    carY + 10
            );

            g2.drawLine(
                    carX + 48,
                    carY + 10,
                    carX + 78,
                    carY + 38
            );

            g2.drawLine(
                    carX + 48,
                    carY + 10,
                    carX + 48,
                    carY + 40
            );
        }

        // =====================================================
        // MOTO
        // =====================================================

        else if (vehiculo.equals("Moto")) {

            g2.setColor(Color.RED);

            g2.fillRoundRect(
                    carX + 20,
                    carY + 10,
                    70,
                    25,
                    20,
                    20
            );

            g2.setColor(Color.BLACK);

            g2.fillOval(
                    carX,
                    carY + 25,
                    30,
                    30
            );

            g2.fillOval(
                    carX + 75,
                    carY + 25,
                    30,
                    30
            );

            g2.setColor(Color.YELLOW);

            g2.fillOval(
                    carX + 85,
                    carY + 18,
                    10,
                    10
            );
        }

        // =====================================================
        // DEPORTIVO
        // =====================================================

        else if (vehiculo.equals("Deportivo")) {

            g2.setColor(
                    new Color(0,255,255,60)
            );

            g2.fillRoundRect(
                    carX - 10,
                    carY - 10,
                    180,
                    80,
                    35,
                    35
            );

            g2.setColor(
                    new Color(0,140,255)
            );

            g2.fillRoundRect(
                    carX,
                    carY,
                    160,
                    50,
                    30,
                    30
            );

            Polygon techo = new Polygon();

            techo.addPoint(carX + 35, carY);

            techo.addPoint(carX + 60, carY - 30);

            techo.addPoint(carX + 115, carY - 30);

            techo.addPoint(carX + 135, carY);

            g2.setColor(
                    new Color(0,180,255)
            );

            g2.fillPolygon(techo);

            g2.setColor(
                    new Color(180,240,255)
            );

            g2.fillRoundRect(
                    carX + 63,
                    carY - 24,
                    48,
                    18,
                    10,
                    10
            );

            // LUCES

            g2.setColor(
                    new Color(255,255,120,120)
            );

            Polygon luz1 = new Polygon();

            luz1.addPoint(carX + 160, carY + 10);
            luz1.addPoint(carX + 260, carY - 10);
            luz1.addPoint(carX + 260, carY + 55);

            g2.fillPolygon(luz1);

            g2.setColor(Color.YELLOW);

            g2.fillOval(
                    carX + 145,
                    carY + 18,
                    12,
                    12
            );

            g2.setColor(Color.BLACK);

            g2.fillOval(
                    carX + 15,
                    carY + 38,
                    38,
                    38
            );

            g2.fillOval(
                    carX + 105,
                    carY + 38,
                    38,
                    38
            );
        }

        // =====================================================
        // VELOCIMETRO
        // =====================================================

        int cx = Math.max(980, getWidth() - 165);

        int cy = Math.max(330, getHeight() - 405);

        int radio = 90;

        g2.setColor(
                new Color(25,25,35)
        );

        g2.fillOval(
                cx - radio,
                cy - radio,
                radio * 2,
                radio * 2
        );

        g2.setColor(Color.WHITE);

        g2.setStroke(new BasicStroke(4));

        g2.drawOval(
                cx - radio,
                cy - radio,
                radio * 2,
                radio * 2
        );

        g2.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                18
        ));

        g2.drawString(
                "KM/H",
                cx - 30,
                cy + 55
        );

        double angulo =
                Math.toRadians(
                        180 +
                        (velocidad * 4)
                );

        int agujaX =
                (int)(
                        cx +
                        Math.cos(angulo)
                        * 65
                );

        int agujaY =
                (int)(
                        cy +
                        Math.sin(angulo)
                        * 65
                );

        g2.setColor(Color.RED);

        g2.setStroke(new BasicStroke(5));

        g2.drawLine(
                cx,
                cy,
                agujaX,
                agujaY
        );

        g2.fillOval(
                cx - 8,
                cy - 8,
                16,
                16
        );

        g2.setColor(Color.CYAN);

        g2.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                24
        ));

        g2.drawString(
                String.format("%.0f", velocidad),
                cx - 20,
                cy + 15
        );

        dibujarAnalisis(g2);
    }

    // =====================================================
    // MOVIMIENTO
    // =====================================================

    @Override
    public void actionPerformed(ActionEvent e) {

        actualizarParametrosDesdeCampos();

        x += velocidad * 0.35;

        tiempo += 0.016;

        trail.add(
                new Point(
                        550,
                        sueloY() - 30
                )
        );

        if (trail.size() > MAX_TRAIL_POINTS) {

            trail.remove(0);
        }

        lblVelocidad.setText(
                "Velocidad: "
                + String.format("%.1f", velocidad)
                + " m/s"
        );

        lblDistancia.setText(
                "Distancia: "
                + String.format("%.1f", x / 5)
                + " m"
        );

        lblTiempo.setText(
                "Tiempo: "
                + String.format("%.2f", tiempo)
                + " s"
        );

        double distanciaActual = x / 5;

        double progreso =
                Math.min(100, (distanciaActual / distanciaObjetivo) * 100);

        double tiempoEstimado =
                distanciaObjetivo / velocidad;

        areaInfo.setText(
                "Formula: d = v * t\n"
                + "Progreso: "
                + String.format("%.1f", progreso)
                + "%\nTiempo estimado: "
                + String.format("%.2f", tiempoEstimado)
                + " s"
        );

        repaint();

        if ((x / 5) >= distanciaObjetivo) {

            lblEstado.setText(
                    "Estado: Finalizado"
            );

            simulacionActiva = false;

            timer.stop();

            int opcion =
                    JOptionPane.showConfirmDialog(
                            null,
                            "¿Deseas reiniciar?",
                            "Simulacion terminada",
                            JOptionPane.YES_NO_OPTION
                    );

            if (opcion
                    == JOptionPane.YES_OPTION) {

                x = 0;

                tiempo = 0;

                trail.clear();

                lblVelocidad.setText(
                        "Velocidad: 0"
                );

                lblDistancia.setText(
                        "Distancia: 0"
                );

                lblTiempo.setText(
                        "Tiempo: 0 s"
                );

                lblEstado.setText(
                        "Estado: Esperando"
                );

                areaInfo.setText(
                        "Formula: d = v * t\n"
                        + "Progreso: 0%\n"
                        + "Tiempo estimado: 0 s"
                );

                repaint();
            }
        }
    }

    public static void main(String[] args) {

        JFrame ventana =
                new JFrame("MRU ULTRA PRO");

        ventana.add(new MRU());

        ventana.setSize(1300, 700);

        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);

        ventana.setLocationRelativeTo(null);

        ventana.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        ventana.setVisible(true);
    }
}
