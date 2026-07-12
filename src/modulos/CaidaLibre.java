package modulos;

import principal.MenuPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class CaidaLibre extends JPanel implements ActionListener {

    Timer timer;

    double alturaInicial;

    double yPelota;

    double velocidad = 0;

    double gravedad = 0.55;

    double rebote = 0.72;

    double peso;

    int rebotes = 0;

    boolean iniciado = false;

    boolean simulacionActiva = false;

    String objeto = "Pelota";

    JTextField txtAltura;
    JTextField txtPeso;

    JLabel lblAltura;
    JLabel lblVelocidad;
    JLabel lblRebotes;
    JLabel lblEstado;
    JLabel lblObjeto;

    JTextArea areaRebotes;
    JTextArea areaInfo;

    Random random = new Random();

    int[] nubeX = new int[10];
    int[] nubeY = new int[10];

    int[] aveX = new int[8];
    int[] aveY = new int[8];

    public CaidaLibre() {

        setLayout(null);

        setBackground(new Color(135, 210, 255));

        for (int i = 0; i < nubeX.length; i++) {

            nubeX[i] = random.nextInt(1200);

            nubeY[i] = random.nextInt(250);
        }

        for (int i = 0; i < aveX.length; i++) {

            aveX[i] = random.nextInt(1200);

            aveY[i] = random.nextInt(300);
        }

        // =====================================================
        // BOTON VOLVER
        // =====================================================

        JButton btnVolver = new JButton("←");

        btnVolver.setBounds(20, 20, 60, 40);

        btnVolver.setBackground(new Color(40, 40, 40));

        btnVolver.setForeground(Color.WHITE);

        btnVolver.setFocusPainted(false);

        btnVolver.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                20
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
        new JLabel("CAIDA LIBRE");

titulo.setBounds(500, 20, 400, 40);

titulo.setForeground(Color.BLACK);

titulo.setFont(new Font(
        "JetBrains Mono",
        Font.BOLD,
        30
));

add(titulo);

// =====================================================
// SUBTITULO
// =====================================================

JLabel subtitulo =
        new JLabel("Objeto cayendo desde el cielo");

subtitulo.setBounds(430, 70, 500, 30);

subtitulo.setForeground(Color.BLACK);

subtitulo.setFont(new Font(
        "JetBrains Mono",
        Font.PLAIN,
        18
));

add(subtitulo);

        // =====================================================
        // PANEL CONTROL
        // =====================================================

        JPanel panelControl = new JPanel();

        panelControl.setLayout(null);

        panelControl.setBounds(20, 90, 250, 500);

        panelControl.setBackground(
                new Color(25, 25, 35)
        );

        add(panelControl);

        JLabel lblA =
                new JLabel("Altura Inicial");

        lblA.setBounds(30, 70, 180, 30);

        lblA.setForeground(Color.WHITE);

        lblA.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(lblA);

        txtAltura = new JTextField();

        txtAltura.setBounds(30, 110, 170, 40);

        txtAltura.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(txtAltura);

        // =====================================================
        // PESO
        // =====================================================

        JLabel lblPeso =
                new JLabel("Peso (kg)");

        lblPeso.setBounds(30, 180, 180, 30);

        lblPeso.setForeground(Color.WHITE);

        lblPeso.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(lblPeso);

        txtPeso = new JTextField();

        txtPeso.setBounds(30, 220, 170, 40);

        txtPeso.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                16
        ));

        panelControl.add(txtPeso);

        // =====================================================
        // BOTON
        // =====================================================

        JButton btnIniciar =
                new JButton("SOLTAR");

        btnIniciar.setBounds(40, 320, 160, 55);

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

        btnPausar.setBounds(40, 390, 160, 40);

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

        panelDatos.setBounds(980, 90, 240, 560);

        panelDatos.setBackground(
                new Color(25, 25, 35)
        );

        add(panelDatos);

        JLabel tituloDatos =
                new JLabel("DATOS");

        tituloDatos.setBounds(60, 20, 150, 30);

        tituloDatos.setForeground(Color.CYAN);

        tituloDatos.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                24
        ));

        panelDatos.add(tituloDatos);

        lblAltura = crearDato(80);
        lblVelocidad = crearDato(140);
        lblRebotes = crearDato(200);
        lblEstado = crearDato(260);
        lblObjeto = crearDato(320);

        lblAltura.setText("Altura: 0");
        lblVelocidad.setText("Velocidad: 0");
        lblRebotes.setText("Rebotes: 0");
        lblEstado.setText("Estado: Esperando");
        lblObjeto.setText("Objeto: Ninguno");

        areaInfo = new JTextArea();

        areaInfo.setBounds(20, 365, 190, 55);

        areaInfo.setBackground(new Color(35, 35, 45));

        areaInfo.setForeground(new Color(0, 255, 180));

        areaInfo.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                12
        ));

        areaInfo.setEditable(false);

        areaInfo.setText("Energia: 0 J\nTiempo teorico: 0 s");

        panelDatos.add(lblAltura);
        panelDatos.add(lblVelocidad);
        panelDatos.add(lblRebotes);
        panelDatos.add(lblEstado);
        panelDatos.add(lblObjeto);
        panelDatos.add(areaInfo);

        areaRebotes = new JTextArea();

        areaRebotes.setBounds(20, 440, 190, 80);

        areaRebotes.setBackground(
                new Color(35, 35, 45)
        );

        areaRebotes.setForeground(
                new Color(0, 255, 120)
        );

        areaRebotes.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                12
        ));

        areaRebotes.setEditable(false);

        panelDatos.add(areaRebotes);

        // =====================================================
        // TIMER
        // =====================================================

        timer = new Timer(16, this);

        btnIniciar.addActionListener(e -> {

            try {

                alturaInicial =
                        Double.parseDouble(
                                txtAltura.getText()
                        );

                peso =
                        Double.parseDouble(
                                txtPeso.getText()
                        );

                if (alturaInicial <= 0 || peso <= 0) {

                    throw new IllegalArgumentException();
                }

                // =====================================================
                // OBJETOS SEGUN PESO
                // =====================================================

                if (peso <= 1) {

                    objeto = "Pelota";

                    rebote = 0.82;

                    gravedad = 0.45;

                }

                else if (peso <= 5) {

                    objeto = "Piedra";

                    rebote = 0.55;

                    gravedad = 0.60;

                }

                else if (peso <= 20) {

                    objeto = "Televisor";

                    rebote = 0.30;

                    gravedad = 0.75;

                }

                else if (peso <= 50) {

                    objeto = "Moto";

                    rebote = 0.18;

                    gravedad = 0.90;

                }

                else {

                    objeto = "Auto";

                    rebote = 0.08;

                    gravedad = 1.10;

                }

                yPelota =
                        Math.max(90, sueloY() - (alturaInicial * 3));

                velocidad = 0;

                rebotes = 0;

                iniciado = true;

                areaRebotes.setText("");

                double tiempoTeorico =
                        Math.sqrt((2 * alturaInicial) / 9.8);

                areaInfo.setText(
                        "Energia pot.: "
                        + String.format("%.1f", peso * 9.8 * alturaInicial)
                        + " J\nTiempo teorico: "
                        + String.format("%.2f", tiempoTeorico)
                        + " s"
                );

                lblEstado.setText(
                        "Estado: Cayendo"
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

                lblEstado.setText("Estado: Cayendo");

                btnPausar.setText("PAUSAR");
            }
        });
    }

    @Override
    public void doLayout() {

        int ancho = getWidth();
        int alto = getHeight();

        if (getComponentCount() >= 5) {

            getComponent(0).setBounds(20, 20, 60, 40);
            getComponent(1).setBounds(Math.max(360, ancho / 2 - 220), 20, 440, 45);
            getComponent(2).setBounds(Math.max(340, ancho / 2 - 260), 68, 520, 30);
            getComponent(3).setBounds(25, 110, 280, Math.max(500, alto - 155));
            getComponent(4).setBounds(Math.max(980, ancho - 310), 90, 280, 560);
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
                14
        ));

        return lbl;
    }

    private int sueloY() {

        return Math.max(560, getHeight() - 170);
    }

    private void dibujarPanel(Graphics2D g2, int x, int y, int w, int h, Color color) {

        g2.setColor(new Color(245, 250, 255, 205));

        g2.fillRoundRect(x, y, w, h, 16, 16);

        g2.setColor(color);

        g2.setStroke(new BasicStroke(2));

        g2.drawRoundRect(x, y, w, h, 16, 16);
    }

    private void dibujarAnalisis(Graphics2D g2) {

        int xPanel = 330;
        int yPanel = Math.max(130, getHeight() / 2 - 210);
        int wPanel = 230;
        int hPanel = 390;

        dibujarPanel(g2, xPanel, yPanel, wPanel, hPanel, new Color(0, 120, 200));

        g2.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        g2.setColor(new Color(0, 80, 140));
        g2.drawString("MEDIDOR DE ALTURA", xPanel + 22, yPanel + 32);

        int barraX = xPanel + 80;
        int barraY = yPanel + 65;
        int barraH = 270;

        g2.setColor(new Color(210, 225, 235));
        g2.fillRoundRect(barraX, barraY, 34, barraH, 14, 14);

        double alturaActual =
                Math.max(0, (sueloY() - yPelota) / 3);

        double alturaBase =
                Math.max(1, alturaInicial);

        int nivel =
                Math.min(barraH, (int)((alturaActual / alturaBase) * barraH));

        g2.setColor(new Color(0, 170, 255));
        g2.fillRoundRect(barraX, barraY + barraH - nivel, 34, nivel, 14, 14);

        g2.setColor(new Color(40, 60, 70));
        g2.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));

        for (int i = 0; i <= 5; i++) {

            int yMarca = barraY + i * (barraH / 5);
            g2.drawLine(barraX + 42, yMarca, barraX + 58, yMarca);
            g2.drawString((int)(alturaBase - (alturaBase * i / 5)) + "m", barraX + 64, yMarca + 4);
        }

        double energiaTotal =
                peso * 9.8 * Math.max(0, alturaInicial);

        double energiaCinetica =
                0.5 * peso * velocidad * velocidad;

        double proporcion =
                energiaTotal > 0 ? Math.min(1, energiaCinetica / energiaTotal) : 0;

        g2.setColor(new Color(40, 60, 70));
        g2.drawString("Transformacion de energia", xPanel + 24, yPanel + 360);
        g2.setColor(new Color(220, 220, 220));
        g2.fillRect(xPanel + 24, yPanel + 372, 180, 12);
        g2.setColor(new Color(0, 180, 90));
        g2.fillRect(xPanel + 24, yPanel + 372, (int)(180 * proporcion), 12);
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
                        new Color(120, 200, 255),
                        0,
                        700,
                        new Color(230, 240, 255)
                );

        g2.setPaint(cielo);

        g2.fillRect(
                0,
                0,
                getWidth(),
                getHeight()
        );

        // =====================================================
        // NUBES
        // =====================================================

        for (int i = 0; i < nubeX.length; i++) {

            g2.setColor(
                    new Color(255,255,255,220)
            );

            g2.fillOval(
                    nubeX[i],
                    nubeY[i],
                    90,
                    45
            );

            g2.fillOval(
                    nubeX[i] + 30,
                    nubeY[i] - 20,
                    90,
                    55
            );

            g2.fillOval(
                    nubeX[i] + 70,
                    nubeY[i],
                    90,
                    45
            );
        }

        // =====================================================
        // AVES
        // =====================================================

        g2.setColor(Color.BLACK);

        for (int i = 0; i < aveX.length; i++) {

            g2.drawArc(
                    aveX[i],
                    aveY[i],
                    20,
                    10,
                    0,
                    180
            );

            g2.drawArc(
                    aveX[i] + 15,
                    aveY[i],
                    20,
                    10,
                    0,
                    180
            );
        }

        // =====================================================
        // OBJETOS
        // =====================================================

        int objetoX =
                Math.max(420, getWidth() / 2 - 40);

        int objetoY = (int)yPelota;

        // =====================================================
        // PELOTA
        // =====================================================

        if (objeto.equals("Pelota")) {

            g2.setColor(Color.WHITE);

            g2.fillOval(
                    objetoX,
                    objetoY,
                    50,
                    50
            );

            g2.setColor(Color.BLACK);

            g2.drawOval(
                    objetoX,
                    objetoY,
                    50,
                    50
            );

            g2.fillOval(
                    objetoX + 18,
                    objetoY + 18,
                    12,
                    12
            );
        }

        // =====================================================
        // PIEDRA
        // =====================================================

        else if (objeto.equals("Piedra")) {

            g2.setColor(
                    new Color(90,90,90)
            );

            g2.fillOval(
                    objetoX,
                    objetoY,
                    60,
                    60
            );

            g2.setColor(Color.DARK_GRAY);

            g2.drawOval(
                    objetoX,
                    objetoY,
                    60,
                    60
            );
        }

        // =====================================================
        // TELEVISOR
        // =====================================================

        else if (objeto.equals("Televisor")) {

            g2.setColor(Color.BLACK);

            g2.fillRoundRect(
                    objetoX,
                    objetoY,
                    80,
                    55,
                    10,
                    10
            );

            g2.setColor(
                    new Color(0,255,180)
            );

            g2.fillRect(
                    objetoX + 8,
                    objetoY + 8,
                    64,
                    35
            );

            g2.setColor(Color.GRAY);

            g2.fillRect(
                    objetoX + 30,
                    objetoY + 45,
                    15,
                    10
            );
        }

        // =====================================================
        // MOTO
        // =====================================================

        else if (objeto.equals("Moto")) {

            g2.setColor(Color.RED);

            g2.fillRoundRect(
                    objetoX + 10,
                    objetoY + 10,
                    70,
                    25,
                    20,
                    20
            );

            g2.setColor(Color.BLACK);

            g2.fillOval(
                    objetoX,
                    objetoY + 25,
                    25,
                    25
            );

            g2.fillOval(
                    objetoX + 60,
                    objetoY + 25,
                    25,
                    25
            );
        }

        // =====================================================
        // AUTO
        // =====================================================

        else if (objeto.equals("Auto")) {

            g2.setColor(
                    new Color(0,140,255)
            );

            g2.fillRoundRect(
                    objetoX,
                    objetoY,
                    120,
                    40,
                    20,
                    20
            );

            Polygon techo = new Polygon();

            techo.addPoint(objetoX + 25, objetoY);

            techo.addPoint(objetoX + 45, objetoY - 20);

            techo.addPoint(objetoX + 85, objetoY - 20);

            techo.addPoint(objetoX + 105, objetoY);

            g2.fillPolygon(techo);

            g2.setColor(Color.BLACK);

            g2.fillOval(
                    objetoX + 10,
                    objetoY + 30,
                    25,
                    25
            );

            g2.fillOval(
                    objetoX + 80,
                    objetoY + 30,
                    25,
                    25
            );
        }

        // =====================================================
        // EFECTO VELOCIDAD
        // =====================================================

        for (int i = 0; i < 5; i++) {

            g2.setColor(
                    new Color(
                            255,
                            255,
                            255,
                            60
                    )
            );

            g2.fillRect(
                    objetoX + 22,
                    objetoY - 60 - (i * 18),
                    8,
                    14
            );
        }

        // =====================================================
        // SUELO
        // =====================================================

        int suelo =
                sueloY();

        g2.setColor(
                new Color(70, 160, 70)
        );

        g2.fillRect(
                0,
                suelo,
                getWidth(),
                Math.max(160, getHeight() - suelo)
        );

        dibujarAnalisis(g2);
    }

    // =====================================================
    // MOVIMIENTO
    // =====================================================

    @Override
    public void actionPerformed(ActionEvent e) {

        velocidad += gravedad;

        yPelota += velocidad;

        double alturaActual =
                Math.max(0, (sueloY() - yPelota) / 3);

        lblAltura.setText(
                "Altura: "
                + String.format(
                        "%.1f",
                        alturaActual
                )
                + " m"
        );

        lblVelocidad.setText(
                "Velocidad: "
                + String.format(
                        "%.1f",
                        velocidad
                )
        );

        lblRebotes.setText(
                "Rebotes: "
                + rebotes
        );

        lblObjeto.setText(
                "Objeto: "
                + objeto
        );

        double energiaPotencial =
                peso * 9.8 * alturaActual;

        double energiaCinetica =
                0.5 * peso * velocidad * velocidad;

        areaInfo.setText(
                "Ep: "
                + String.format("%.1f", energiaPotencial)
                + " J\nEc: "
                + String.format("%.1f", energiaCinetica)
                + " J"
        );

        // =====================================================
        // REBOTES
        // =====================================================

        int puntoImpacto =
                sueloY() - 50;

        if (yPelota >= puntoImpacto) {

            yPelota = puntoImpacto;

            velocidad = -velocidad * rebote;

            rebotes++;

            double alturaRebote =
                    Math.abs(velocidad) * 8;

            areaRebotes.append(
                    "Rebote "
                    + rebotes
                    + " → "
                    + String.format(
                            "%.2f",
                            alturaRebote
                    )
                    + " m\n"
            );

            if (Math.abs(velocidad) < 2) {

                timer.stop();

                simulacionActiva = false;

                lblEstado.setText(
                        "Estado: Finalizado"
                );

                int opcion =
                        JOptionPane.showConfirmDialog(
                                null,
                                "¿Deseas reiniciar?",
                                "Simulacion terminada",
                                JOptionPane.YES_NO_OPTION
                        );

                if (opcion
                        == JOptionPane.YES_OPTION) {

                    velocidad = 0;

                    rebotes = 0;

                    iniciado = false;

                    areaRebotes.setText("");

                    areaInfo.setText(
                            "Energia: 0 J\n"
                            + "Tiempo teorico: 0 s"
                    );

                    lblAltura.setText("Altura: 0");

                    lblVelocidad.setText("Velocidad: 0");

                    lblRebotes.setText("Rebotes: 0");

                    lblEstado.setText(
                            "Estado: Esperando"
                    );

                    lblObjeto.setText(
                            "Objeto: Ninguno"
                    );

                    repaint();
                }
            }
        }

        repaint();
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        JFrame ventana =
                new JFrame("CAIDA LIBRE");

        ventana.add(new CaidaLibre());

        ventana.setSize(1300, 700);

        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);

        ventana.setLocationRelativeTo(null);

        ventana.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        ventana.setVisible(true);
    }
}
