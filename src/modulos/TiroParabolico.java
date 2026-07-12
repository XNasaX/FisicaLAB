package modulos;

import principal.MenuPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TiroParabolico extends JPanel implements ActionListener {

    private static final int MAX_TRAIL_POINTS = 500;

    Timer timer;

    double x = 0;
    double y = 0;

    double velocidadInicial;
    double angulo;

    double vx;
    double vy;

    double gravedad = 0.35;

    double tiempo = 0;

    int rebotes = 0;

    boolean finalizado = false;

    boolean simulacionActiva = false;

    int cameraX = 0;

    JTextField txtVelocidad;
    JTextField txtAngulo;

    JLabel lblVelocidad;
    JLabel lblAltura;
    JLabel lblDistancia;
    JLabel lblTiempo;
    JLabel lblEstado;

    JTextArea areaRebotes;
    JTextArea areaInfo;

    ArrayList<Point> trail;

    String proyectil = "Pelota";

    JComboBox<String> comboProyectil;

    public TiroParabolico() {

        setLayout(null);

        setBackground(Color.BLACK);

        trail = new ArrayList<>();

        // =====================================================
        // BOTON VOLVER
        // =====================================================

        JButton btnVolver = new JButton("←");

        btnVolver.setBounds(20, 20, 60, 40);

        btnVolver.setBackground(new Color(35,35,50));

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
                new JLabel("TIRO PARABOLICO");

        titulo.setBounds(420, 20, 500, 40);

        titulo.setForeground(
                new Color(255,140,0)
        );

        titulo.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                30
        ));

        add(titulo);

        // =====================================================
        // PANEL CONTROL
        // =====================================================

        JPanel panelControl = new JPanel();

        panelControl.setLayout(null);

        panelControl.setBounds(20, 90, 250, 550);

        panelControl.setBackground(
                new Color(18,18,30)
        );

        add(panelControl);

        // =====================================================
        // VELOCIDAD
        // =====================================================

        JLabel lblV =
                new JLabel("Velocidad");

        lblV.setBounds(30, 50, 180, 30);

        lblV.setForeground(Color.WHITE);

        lblV.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(lblV);

        txtVelocidad = new JTextField();

        txtVelocidad.setBounds(30, 85, 170, 40);

        txtVelocidad.setBackground(
                new Color(30,30,45)
        );

        txtVelocidad.setForeground(Color.WHITE);

        txtVelocidad.setCaretColor(Color.WHITE);

        panelControl.add(txtVelocidad);

        // =====================================================
        // ANGULO
        // =====================================================

        JLabel lblA =
                new JLabel("Angulo");

        lblA.setBounds(30, 145, 180, 30);

        lblA.setForeground(Color.WHITE);

        lblA.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(lblA);

        txtAngulo = new JTextField();

        txtAngulo.setBounds(30, 180, 170, 40);

        txtAngulo.setBackground(
                new Color(30,30,45)
        );

        txtAngulo.setForeground(Color.WHITE);

        txtAngulo.setCaretColor(Color.WHITE);

        panelControl.add(txtAngulo);

        // =====================================================
        // PROYECTIL
        // =====================================================

        JLabel lblP =
                new JLabel("Proyectil");

        lblP.setBounds(30, 240, 180, 30);

        lblP.setForeground(Color.WHITE);

        lblP.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(lblP);

        String[] proyectiles = {
                "Pelota",
                "Bomba",
                "Meteorito"
        };

        comboProyectil =
                new JComboBox<>(proyectiles);

        comboProyectil.setBounds(
                30,
                275,
                170,
                40
        );

        comboProyectil.setBackground(
                new Color(30,30,45)
        );

        comboProyectil.setForeground(Color.WHITE);

        panelControl.add(comboProyectil);

        // =====================================================
        // BOTON
        // =====================================================

        JButton btnIniciar =
                new JButton("LANZAR");

        btnIniciar.setBounds(40, 390, 160, 55);

        btnIniciar.setBackground(
                new Color(255,140,0)
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

        btnPausar.setBounds(40, 460, 160, 38);

        btnPausar.setBackground(new Color(35,35,55));

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

        panelDatos.setBounds(1000, 90, 240, 560);

        panelDatos.setBackground(
                new Color(18,18,30)
        );

        add(panelDatos);

        lblVelocidad = crearDato(40);

        lblVelocidad.setText("Velocidad: 0");

        lblAltura = crearDato(100);

        lblAltura.setText("Altura: 0");

        lblDistancia = crearDato(160);

        lblDistancia.setText("Distancia: 0");

        lblTiempo = crearDato(220);

        lblTiempo.setText("Tiempo: 0");

        lblEstado = crearDato(280);

        lblEstado.setText("Estado: Esperando");

        areaInfo = new JTextArea();

        areaInfo.setBounds(20, 320, 200, 70);

        areaInfo.setBackground(new Color(25,25,35));

        areaInfo.setForeground(new Color(255,180,80));

        areaInfo.setFont(
                new Font(
                        "JetBrains Mono",
                        Font.PLAIN,
                        12
                )
        );

        areaInfo.setEditable(false);

        areaInfo.setText("Alcance teorico: 0 m\nAltura max.: 0 m\nVuelo: 0 s");

        panelDatos.add(lblVelocidad);

        panelDatos.add(lblAltura);

        panelDatos.add(lblDistancia);

        panelDatos.add(lblTiempo);

        panelDatos.add(lblEstado);

        panelDatos.add(areaInfo);

        // =====================================================
        // HISTORIAL REBOTES
        // =====================================================

        areaRebotes = new JTextArea();

        areaRebotes.setBounds(20, 410, 200, 110);

        areaRebotes.setBackground(
                new Color(25,25,35)
        );

        areaRebotes.setForeground(
                new Color(0,255,120)
        );

        areaRebotes.setFont(
                new Font(
                        "JetBrains Mono",
                        Font.PLAIN,
                        12
                )
        );

        areaRebotes.setEditable(false);

        panelDatos.add(areaRebotes);

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

                angulo =
                        Double.parseDouble(
                                txtAngulo.getText()
                        );

                if (velocidadInicial <= 0
                        || angulo <= 0
                        || angulo >= 90) {

                    throw new IllegalArgumentException();
                }

                proyectil =
                        comboProyectil
                                .getSelectedItem()
                                .toString();

                double rad =
                        Math.toRadians(angulo);

                vx =
                        velocidadInicial
                                * Math.cos(rad);

                vy =
                        velocidadInicial
                                * Math.sin(rad);

                double alcanceTeorico =
                        (velocidadInicial * velocidadInicial
                        * Math.sin(2 * rad)) / 9.8;

                double alturaMaxima =
                        (vy * vy) / (2 * 9.8);

                double tiempoVuelo =
                        (2 * vy) / 9.8;

                x = 0;

                y = 0;

                tiempo = 0;

                rebotes = 0;

                finalizado = false;

                cameraX = 0;

                trail.clear();

                areaRebotes.setText("");

                areaInfo.setText(
                        "Alcance teorico: "
                        + String.format("%.1f", alcanceTeorico)
                        + " m\nAltura max.: "
                        + String.format("%.1f", alturaMaxima)
                        + " m\nVuelo: "
                        + String.format("%.2f", tiempoVuelo)
                        + " s"
                );

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
            getComponent(1).setBounds(Math.max(360, ancho / 2 - 260), 20, 520, 45);
            getComponent(2).setBounds(25, 90, 280, Math.max(550, alto - 135));
            getComponent(3).setBounds(Math.max(980, ancho - 310), 90, 280, 560);
        }
    }

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

    private int sueloY() {

        return Math.max(560, getHeight() - 170);
    }

    private void dibujarPanel(Graphics2D g2, int x, int y, int w, int h, Color color) {

        g2.setColor(new Color(18, 18, 30, 200));

        g2.fillRoundRect(x, y, w, h, 16, 16);

        g2.setColor(color);

        g2.setStroke(new BasicStroke(2));

        g2.drawRoundRect(x, y, w, h, 16, 16);
    }

    private void dibujarTrayectoriaPredicha(Graphics2D g2) {

        if (velocidadInicial <= 0) {

            return;
        }

        double rad = Math.toRadians(angulo);
        double vxTeorica = velocidadInicial * Math.cos(rad);
        double vyTeorica = velocidadInicial * Math.sin(rad);

        g2.setColor(new Color(255, 220, 0, 130));
        g2.setStroke(new BasicStroke(
                2,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,
                1,
                new float[] {8, 8},
                0
        ));

        int baseY = sueloY() - 60;
        int anteriorX = 390;
        int anteriorY = baseY;

        for (double t = 0.1; t <= 9; t += 0.12) {

            double px = vxTeorica * t * 0.6;
            double py = (vyTeorica * t) - (0.5 * 9.8 * t * t);

            if (py < 0) {

                break;
            }

            int actualX = 390 + (int)(px * 5);
            int actualY = baseY - (int)(py * 5);

            g2.drawLine(anteriorX, anteriorY, actualX, actualY);

            anteriorX = actualX;
            anteriorY = actualY;
        }
    }

    private void dibujarAnalisis(Graphics2D g2) {

        int wPanel = 610;
        int hPanel = 145;
        int xPanel = Math.max(330, getWidth() - wPanel - 330);
        int yPanel = Math.max(440, getHeight() - hPanel - 40);

        dibujarPanel(g2, xPanel, yPanel, wPanel, hPanel, new Color(255, 160, 0));

        g2.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        g2.setColor(Color.ORANGE);
        g2.drawString("COMPONENTES DEL LANZAMIENTO", xPanel + 20, yPanel + 30);

        int origenX = xPanel + 90;
        int origenY = yPanel + 98;

        int vxLargo = Math.min(180, (int)(Math.abs(vx) * 4));
        int vyLargo = Math.min(80, (int)(Math.abs(vy) * 3));

        g2.setStroke(new BasicStroke(5));
        g2.setColor(new Color(0, 220, 255));
        g2.drawLine(origenX, origenY, origenX + vxLargo, origenY);
        g2.fillPolygon(
                new int[] {origenX + vxLargo, origenX + vxLargo - 12, origenX + vxLargo - 12},
                new int[] {origenY, origenY - 8, origenY + 8},
                3
        );

        g2.setColor(new Color(255, 120, 0));
        g2.drawLine(origenX, origenY, origenX, origenY - vyLargo);
        g2.fillPolygon(
                new int[] {origenX, origenX - 8, origenX + 8},
                new int[] {origenY - vyLargo, origenY - vyLargo + 12, origenY - vyLargo + 12},
                3
        );

        g2.setFont(new Font("JetBrains Mono", Font.PLAIN, 13));
        g2.setColor(Color.WHITE);
        g2.drawString("vx: " + String.format("%.1f", vx) + " m/s", xPanel + 300, yPanel + 66);
        g2.drawString("vy: " + String.format("%.1f", vy) + " m/s", xPanel + 300, yPanel + 92);
        g2.drawString("Linea punteada: trayectoria teorica sin rebotes.", xPanel + 20, yPanel + 125);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // =====================================================
        // CAMARA
        // =====================================================

        cameraX =
                (int)x - 250;

        if (cameraX < 0) {

            cameraX = 0;
        }

        g2.translate(-cameraX, 0);

        // =====================================================
        // FONDO
        // =====================================================

        GradientPaint cielo =
                new GradientPaint(
                        0,
                        0,
                        new Color(5,5,20),
                        0,
                        700,
                        new Color(25,25,60)
                );

        g2.setPaint(cielo);

        g2.fillRect(
                cameraX,
                0,
                6000,
                getHeight()
        );

        // =====================================================
        // ESTRELLAS
        // =====================================================

        g2.setColor(Color.WHITE);

        for (int i = 0; i < 300; i++) {

            int sx = i * 80;

            int sy = (i * 37) % 500;

            g2.fillOval(
                    sx,
                    sy,
                    2,
                    2
            );
        }

        // =====================================================
        // SUELO
        // =====================================================

        int suelo =
                sueloY();

        g2.setColor(
                new Color(70,70,70)
        );

        g2.fillRect(
                cameraX,
                suelo,
                6000,
                Math.max(140, getHeight() - suelo)
        );

        // =====================================================
        // CAÑON
        // =====================================================

        int cannonX = 320;

        int cannonY = suelo - 60;

        g2.setColor(
                new Color(40,40,40)
        );

        g2.fillOval(
                cannonX - 20,
                cannonY + 10,
                90,
                50
        );

        g2.setStroke(new BasicStroke(18));

        g2.setColor(
                new Color(80,80,80)
        );

        g2.drawLine(
                cannonX + 20,
                cannonY + 15,
                cannonX + 95,
                cannonY - 40
        );

        // =====================================================
        // TRAIL
        // =====================================================

        for (Point p : trail) {

            g2.setColor(
                    new Color(255,140,0,80)
            );

            g2.fillOval(
                    p.x,
                    p.y,
                    8,
                    8
            );
        }

        dibujarTrayectoriaPredicha(g2);

        // =====================================================
        // PROYECTIL
        // =====================================================

        int ballX =
                (int)x + 390;

        int ballY =
                suelo - 60 - (int)y;

        if (proyectil.equals("Pelota")) {

            g2.setColor(Color.WHITE);

            g2.fillOval(
                    ballX,
                    ballY,
                    35,
                    35
            );

            g2.setColor(Color.BLACK);

            g2.drawOval(
                    ballX,
                    ballY,
                    35,
                    35
            );
        }

        else if (proyectil.equals("Bomba")) {

            g2.setColor(Color.BLACK);

            g2.fillOval(
                    ballX,
                    ballY,
                    35,
                    35
            );

            g2.setColor(Color.ORANGE);

            g2.fillRect(
                    ballX + 13,
                    ballY - 10,
                    6,
                    12
            );
        }

        else {

            g2.setColor(
                    new Color(255,80,0)
            );

            g2.fillOval(
                    ballX,
                    ballY,
                    40,
                    40
            );

            g2.setColor(
                    new Color(255,180,0,120)
            );

            g2.fillOval(
                    ballX - 10,
                    ballY - 10,
                    60,
                    60
            );
        }

        g2.translate(cameraX, 0);

        dibujarAnalisis(g2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        tiempo += 0.016;

        x += vx * 0.12;

        y += vy * 0.12;

        vy -= gravedad;

        trail.add(
                new Point(
                        (int)x + 400,
                        sueloY() - 40 - (int)y
                )
        );

        if (trail.size() > MAX_TRAIL_POINTS) {

            trail.remove(0);
        }

        // =====================================================
        // REBOTES
        // =====================================================

        if (y <= 0) {

            y = 0;

            vy = -vy * 0.55;

            rebotes++;

            double alturaRebote =
                    Math.abs(vy) * 3;

            areaRebotes.append(
                    "Rebote "
                    + rebotes
                    + " -> "
                    + String.format(
                            "%.1f",
                            alturaRebote
                    )
                    + " m\n"
            );

            if (Math.abs(vy) < 1.5) {

                vy = 0;

                vx *= 0.96;
            }
        }

        // =====================================================
        // DATOS
        // =====================================================

        double velocidadReal =
                Math.sqrt(vx * vx + vy * vy);

        lblVelocidad.setText(
                "Velocidad: "
                + String.format("%.1f", velocidadReal)
                + " m/s"
        );

        lblAltura.setText(
                "Altura: "
                + String.format("%.1f", y / 5)
                + " m"
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

        repaint();

        // =====================================================
        // FINAL
        // =====================================================

        if (vx < 0.2 && y == 0 && !finalizado) {

            finalizado = true;

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

                y = 0;

                tiempo = 0;

                cameraX = 0;

                trail.clear();

                areaRebotes.setText("");

                lblVelocidad.setText(
                        "Velocidad: 0"
                );

                lblAltura.setText(
                        "Altura: 0"
                );

                lblDistancia.setText(
                        "Distancia: 0"
                );

                lblTiempo.setText(
                        "Tiempo: 0"
                );

                lblEstado.setText(
                        "Estado: Esperando"
                );

                areaInfo.setText(
                        "Alcance teorico: 0 m\n"
                        + "Altura max.: 0 m\n"
                        + "Vuelo: 0 s"
                );

                repaint();
            }
        }
    }

    public static void main(String[] args) {

        JFrame ventana =
                new JFrame("TIRO PARABOLICO");

        ventana.add(new TiroParabolico());

        ventana.setSize(1300, 700);

        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);

        ventana.setLocationRelativeTo(null);

        ventana.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        ventana.setVisible(true);
    }
}
