package modulos;

import principal.MenuPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MRUV extends JPanel implements ActionListener {

    private static final int MAX_TRAIL_POINTS = 300;

    Timer timer;

    double x = 0;

    double velocidadInicial;

    double aceleracion;

    double velocidadActual;

    double distanciaObjetivo;

    double tiempo = 0;

    JTextField txtVelocidad;

    JTextField txtAceleracion;

    JTextField txtDistancia;

    JLabel lblVelocidad;
    JLabel lblDistancia;
    JLabel lblTiempo;
    JLabel lblEstado;
    JLabel lblAceleracion;
    JTextArea areaInfo;

    ArrayList<Point> trail;

    String vehiculo = "Deportivo";

    JComboBox<String> comboVehiculo;

    boolean simulacionActiva = false;

    public MRUV() {

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
                new JLabel("MOVIMIENTO RECTILINEO UNIFORME VARIADO");

        titulo.setBounds(220, 20, 900, 40);

        titulo.setForeground(
                new Color(255, 120, 0)
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

        panelControl.setBounds(20, 90, 250, 560);

        panelControl.setBackground(
                new Color(18, 18, 30)
        );

        add(panelControl);

        // =====================================================
        // VELOCIDAD
        // =====================================================

        JLabel lblV =
                new JLabel("Velocidad Inicial");

        lblV.setBounds(30, 40, 180, 30);

        lblV.setForeground(Color.WHITE);

        lblV.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                15
        ));

        panelControl.add(lblV);

        txtVelocidad = new JTextField();

        txtVelocidad.setBounds(30, 75, 170, 40);

        txtVelocidad.setBackground(
                new Color(30,30,45)
        );

        txtVelocidad.setForeground(Color.WHITE);

        txtVelocidad.setCaretColor(Color.WHITE);

        panelControl.add(txtVelocidad);

        // =====================================================
        // ACELERACION
        // =====================================================

        JLabel lblA =
                new JLabel("Aceleracion");

        lblA.setBounds(30, 135, 180, 30);

        lblA.setForeground(Color.WHITE);

        lblA.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                15
        ));

        panelControl.add(lblA);

        txtAceleracion = new JTextField();

        txtAceleracion.setBounds(30, 170, 170, 40);

        txtAceleracion.setBackground(
                new Color(30,30,45)
        );

        txtAceleracion.setForeground(Color.WHITE);

        txtAceleracion.setCaretColor(Color.WHITE);

        panelControl.add(txtAceleracion);

        // =====================================================
        // DISTANCIA
        // =====================================================

        JLabel lblD =
                new JLabel("Distancia");

        lblD.setBounds(30, 230, 180, 30);

        lblD.setForeground(Color.WHITE);

        lblD.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                15
        ));

        panelControl.add(lblD);

        txtDistancia = new JTextField();

        txtDistancia.setBounds(30, 265, 170, 40);

        txtDistancia.setBackground(
                new Color(30,30,45)
        );

        txtDistancia.setForeground(Color.WHITE);

        txtDistancia.setCaretColor(Color.WHITE);

        panelControl.add(txtDistancia);

        // =====================================================
        // VEHICULO
        // =====================================================

        JLabel lblVehiculo =
                new JLabel("Vehiculo");

        lblVehiculo.setBounds(30, 325, 180, 30);

        lblVehiculo.setForeground(Color.WHITE);

        lblVehiculo.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                15
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
                360,
                170,
                40
        );

        comboVehiculo.setBackground(
                new Color(30,30,45)
        );

        comboVehiculo.setForeground(Color.WHITE);

        panelControl.add(comboVehiculo);

        // =====================================================
        // BOTON
        // =====================================================

        JButton btnIniciar =
                new JButton("INICIAR");

        btnIniciar.setBounds(40, 460, 160, 55);

        btnIniciar.setBackground(
                new Color(255,120,0)
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

        btnPausar.setBounds(40, 520, 160, 32);

        btnPausar.setBackground(new Color(35,35,55));

        btnPausar.setForeground(Color.WHITE);

        btnPausar.setFocusPainted(false);

        btnPausar.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                13
        ));

        panelControl.add(btnPausar);

        // =====================================================
        // PANEL DATOS
        // =====================================================

        JPanel panelDatos = new JPanel();

        panelDatos.setLayout(null);

        panelDatos.setBounds(1000, 90, 240, 520);

        panelDatos.setBackground(
                new Color(18,18,30)
        );

        add(panelDatos);

        lblVelocidad = crearDato(70);

        lblVelocidad.setText("Velocidad: 0");

        lblAceleracion = crearDato(150);

        lblAceleracion.setText("Aceleracion: 0");

        lblDistancia = crearDato(230);

        lblDistancia.setText("Distancia: 0");

        lblTiempo = crearDato(310);

        lblTiempo.setText("Tiempo: 0 s");

        lblEstado = crearDato(390);

        lblEstado.setText("Estado: Esperando");

        areaInfo = new JTextArea();

        areaInfo.setBounds(20, 440, 200, 60);

        areaInfo.setBackground(new Color(25,25,35));

        areaInfo.setForeground(new Color(255,180,80));

        areaInfo.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                12
        ));

        areaInfo.setEditable(false);

        areaInfo.setText("Formula: d = vi*t + 1/2*a*t^2\nProgreso: 0%");

        panelDatos.add(lblVelocidad);

        panelDatos.add(lblAceleracion);

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

                velocidadInicial =
                        Double.parseDouble(
                                txtVelocidad.getText()
                        );

                aceleracion =
                        Double.parseDouble(
                                txtAceleracion.getText()
                        );

                distanciaObjetivo =
                        Double.parseDouble(
                                txtDistancia.getText()
                        );

                if (velocidadInicial < 0
                        || aceleracion <= 0
                        || distanciaObjetivo <= 0) {

                    throw new IllegalArgumentException();
                }

                vehiculo =
                        comboVehiculo
                                .getSelectedItem()
                                .toString();

                velocidadActual = velocidadInicial;

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
            getComponent(1).setBounds(Math.max(320, ancho / 2 - 455), 20, 910, 45);
            getComponent(2).setBounds(25, 90, 280, Math.max(560, alto - 135));
            getComponent(3).setBounds(Math.max(980, ancho - 310), 90, 280, 520);
        }
    }

    // =====================================================
    // LABELS
    // =====================================================

    private JLabel crearDato(int y) {

        JLabel lbl = new JLabel();

        lbl.setBounds(20, y, 220, 40);

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

            double nuevaAceleracion =
                    Double.parseDouble(txtAceleracion.getText());

            double nuevaDistancia =
                    Double.parseDouble(txtDistancia.getText());

            if (nuevaAceleracion <= 0 || nuevaDistancia <= 0) {

                return;
            }

            aceleracion = nuevaAceleracion;

            distanciaObjetivo = nuevaDistancia;

            vehiculo =
                    comboVehiculo
                            .getSelectedItem()
                            .toString();

        } catch (Exception ex) {

            // Se conserva el ultimo valor valido mientras el usuario escribe.
        }
    }

    private int sueloY() {

        return Math.max(520, getHeight() - 180);
    }

    private void dibujarPanel(Graphics2D g2, int x, int y, int w, int h, Color color) {

        g2.setColor(new Color(18, 18, 30, 195));

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

        dibujarPanel(g2, xPanel, yPanel, wPanel, hPanel, new Color(255, 140, 0));

        g2.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        g2.setColor(Color.ORANGE);
        g2.drawString("GRAFICA VELOCIDAD - TIEMPO", xPanel + 20, yPanel + 30);

        int gx = xPanel + 45;
        int gy = yPanel + 52;
        int gw = 390;
        int gh = 82;

        g2.setColor(new Color(255,255,255,90));
        g2.drawLine(gx, gy + gh, gx + gw, gy + gh);
        g2.drawLine(gx, gy, gx, gy + gh);

        double escalaVelocidad =
                Math.max(1, velocidadActual + Math.abs(aceleracion) + 10);

        int px = gx + Math.min(gw, (int)(tiempo * 45));
        int py = gy + gh - (int)((velocidadActual / escalaVelocidad) * gh);

        g2.setColor(new Color(255, 170, 0));
        g2.setStroke(new BasicStroke(4));
        g2.drawLine(gx, gy + gh, px, py);
        g2.fillOval(px - 5, py - 5, 10, 10);

        int flechaX = xPanel + 475;
        int flechaY = yPanel + 105;
        int largo = Math.min(90, 20 + (int)(Math.abs(aceleracion) * 8));

        g2.setColor(new Color(255, 140, 0, 90));
        g2.fillRoundRect(flechaX - 20, flechaY - 45, 95, 85, 12, 12);
        g2.setColor(Color.ORANGE);
        g2.setStroke(new BasicStroke(5));
        g2.drawLine(flechaX, flechaY, flechaX + largo, flechaY);
        g2.fillPolygon(
                new int[] {flechaX + largo, flechaX + largo - 12, flechaX + largo - 12},
                new int[] {flechaY, flechaY - 8, flechaY + 8},
                3
        );

        g2.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        g2.setColor(Color.WHITE);
        g2.drawString("a = " + String.format("%.1f", aceleracion) + " m/s2", flechaX - 10, flechaY + 30);
        g2.drawString("La pendiente de esta grafica representa la aceleracion.", gx, yPanel + 150);
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

        // =====================================================
        // CIELO
        // =====================================================

        GradientPaint cielo =
                new GradientPaint(
                        0,
                        0,
                        new Color(5,5,15),
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

        // =====================================================
        // ESTRELLAS
        // =====================================================

        g2.setColor(Color.WHITE);

        for (int i = 0; i < 90; i++) {

            int sx = (i * 97) % 1300;

            int sy = (i * 53) % 320;

            g2.fillOval(sx, sy, 2, 2);
        }

        // =====================================================
        // POSTES
        // =====================================================

        int sueloY =
                sueloY();

        for (int i = -200; i < 2200; i += 180) {

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

        // =====================================================
        // PISTA
        // =====================================================

        g2.setColor(
                new Color(70,70,70)
        );

        g2.fillRect(
                0,
                sueloY,
                getWidth(),
                125
        );

        for (int i = -200; i < 2200; i += 120) {

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

        // =====================================================
        // TRAIL
        // =====================================================

        for (Point p : trail) {

            g2.setColor(
                    new Color(255,140,0,70)
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

            g2.drawOval(carX, carY + 20, 35, 35);

            g2.drawOval(carX + 60, carY + 20, 35, 35);

            g2.drawLine(carX + 18, carY + 38,
                    carX + 48, carY + 10);

            g2.drawLine(carX + 48, carY + 10,
                    carX + 78, carY + 38);

            g2.drawLine(carX + 48, carY + 10,
                    carX + 48, carY + 40);
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

        else {

            g2.setColor(
                    new Color(255,140,0,70)
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
                    new Color(255,120,0)
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
                    new Color(255,170,0)
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
                        (velocidadActual * 3)
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

        g2.setColor(Color.ORANGE);

        g2.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                24
        ));

        g2.drawString(
                String.format("%.0f", velocidadActual),
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

        tiempo += 0.016;

        velocidadActual += aceleracion * 0.016;

        x += velocidadActual * 0.35;

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
                + String.format("%.1f", velocidadActual)
                + " m/s"
        );

        lblAceleracion.setText(
                "Aceleracion: "
                + String.format("%.1f", aceleracion)
                + " m/s²"
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

        areaInfo.setText(
                "Formula: d = vi*t + 1/2*a*t^2\n"
                + "Progreso: "
                + String.format("%.1f", progreso)
                + "%\nVel. final: "
                + String.format("%.1f", velocidadActual)
                + " m/s"
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

                velocidadActual = velocidadInicial;

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
                        "Formula: d = vi*t + 1/2*a*t^2\n"
                        + "Progreso: 0%"
                );

                repaint();
            }
        }
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        JFrame ventana =
                new JFrame("MRUV ULTRA PRO");

        ventana.add(new MRUV());

        ventana.setSize(1300, 700);

        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);

        ventana.setLocationRelativeTo(null);

        ventana.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        ventana.setVisible(true);
    }
}
