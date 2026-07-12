package principal;

import modulos.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class MenuPrincipal extends JPanel implements ActionListener {

    private static final int LOGO_SIZE = 360;

    private static final int TITULO_WIDTH = 560;

    private static final int TITULO_HEIGHT = 314;

    private static final int MENU_BUTTON_WIDTH = 245;

    private static final int MENU_BUTTON_HEIGHT = 45;

    private static final int MENU_BUTTON_GAP = 14;

    private static final int MENU_BUTTON_COUNT = 7;

    public static JFrame menu;

    Timer timer;

    ArrayList<Point> particulas;

    Random random;

    Image logo;

    Image tituloImagen;

    Image enlace;

    JButton btnEnlace;

    JLabel lblVersion;

    JWindow ventanaVersion;

    Timer timerEnlace;

    int enlaceBaseX;

    int enlaceBaseY;

    int enlaceTick;

    int tituloTick;

    public MenuPrincipal() {

        setLayout(null);

        setBackground(new Color(5, 5, 20));

        particulas = new ArrayList<>();

        random = new Random();

        // =====================================================
        // LOGO
        // =====================================================

        try {

            java.net.URL ruta =
                    getClass().getResource(
                            "/imagenes/logo.png"
                    );

            logo = new ImageIcon(ruta).getImage();

        } catch (Exception e) {

            e.printStackTrace();
        }

        // =====================================================
        // TITULO IMAGEN
        // =====================================================

        try {

            java.net.URL rutaTitulo =
                    getClass().getResource(
                            "/imagenes/titulo.png"
                    );

            tituloImagen = new ImageIcon(rutaTitulo).getImage();

        } catch (Exception e) {

            e.printStackTrace();
        }

        // =====================================================
        // LINK IMAGEN
        // =====================================================        
        
        
        try {

            java.net.URL rutaEnlace =
                    getClass().getResource(
                            "/imagenes/Enlace.png"
                    );

            enlace = new ImageIcon(rutaEnlace).getImage();

            btnEnlace =
                    crearBotonEnlace(enlace);

        } catch (Exception e) {

            e.printStackTrace();
        }
        
        
        // =====================================================
        // PARTICULAS
        // =====================================================

        for (int i = 0; i < 140; i++) {

            particulas.add(

                    new Point(

                            random.nextInt(1400),

                            random.nextInt(800)

                    )
            );
        }

        // =====================================================
        // BOTONES
        // =====================================================

        JButton btnMRU =
                crearBoton(
                        "MRU",
                        70,
                        220
                );

        JButton btnMRUV =
                crearBoton(
                        "MRUV",
                        70,
                        310
                );

        JButton btnCaida =
                crearBoton(
                        "CAIDA LIBRE",
                        70,
                        400
                );

        JButton btnTiro =
                crearBoton(
                        "TIRO PARABOLICO",
                        70,
                        490
                );

        JButton btnCalculus =
                crearBoton(
                        "CALCULUS 360",
                        70,
                        535
                );

        JButton btnPendulo =
                crearBoton(
                        "PENDULO",
                        70,
                        560
                );

        JButton btnSalir =
                crearBoton(
                        "SALIR",
                        70,
                        580
                );

        JButton btnOcio =
                crearBotonColor(
                        "OCIO",
                        new Color(255, 135, 0)
                );

        add(btnMRU);

        add(btnMRUV);

        add(btnCaida);

        add(btnTiro);

        add(btnCalculus);

        add(btnPendulo);

        add(btnSalir);

        add(btnOcio);

        if (btnEnlace != null) {

            add(btnEnlace);
        }

        lblVersion = crearEtiquetaVersion();

        add(lblVersion);

        // =====================================================
        // EVENTOS
        // =====================================================

        btnMRU.addActionListener(
                e -> abrirModulo(new MRU())
        );

        btnMRUV.addActionListener(
                e -> abrirModulo(new MRUV())
        );

        btnCaida.addActionListener(
                e -> abrirModulo(new CaidaLibre())
        );

        btnTiro.addActionListener(
                e -> abrirModulo(new TiroParabolico())
        );

        btnCalculus.addActionListener(e -> {

            CalculusGrapher grapher =
                    new CalculusGrapher();

            grapher.getView().addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {

                    menu.setVisible(true);
                }
            });

            grapher.show();

            menu.setVisible(false);
        });

        btnPendulo.addActionListener(e -> {

            SimuladorPendulo pendulo =
                    new SimuladorPendulo();

            pendulo.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {

                    menu.setVisible(true);
                }
            });

            pendulo.setExtendedState(JFrame.MAXIMIZED_BOTH);
            pendulo.setVisible(true);

            menu.setVisible(false);
        });

        btnSalir.addActionListener(
                e -> System.exit(0)
        );

        btnOcio.addActionListener(e -> {

            OcioJuegaAprendiendo juego =
                    new OcioJuegaAprendiendo();

            juego.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosed(WindowEvent e) {

                    menu.setVisible(true);
                }
            });

            juego.setVisible(true);

            menu.setVisible(false);
        });

        // =====================================================
        // TIMER
        // =====================================================

        timer = new Timer(30, this);

        timer.start();
    }

    @Override
    public void doLayout() {

        int ancho = getWidth();

        if (getComponentCount() >= MENU_BUTTON_COUNT) {

            int xBoton = 28;
            int yInicial =
                    Math.max(
                            250,
                            getHeight()
                            - 40
                            - (
                                    MENU_BUTTON_HEIGHT * MENU_BUTTON_COUNT
                                    + MENU_BUTTON_GAP * (MENU_BUTTON_COUNT - 1)
                            )
                    );

            for (int i = 0; i < MENU_BUTTON_COUNT; i++) {

                getComponent(i).setBounds(
                        xBoton,
                        yInicial
                        + i * (
                                MENU_BUTTON_HEIGHT
                                + MENU_BUTTON_GAP
                        ),
                        MENU_BUTTON_WIDTH,
                        MENU_BUTTON_HEIGHT
                );
            }

            if (getComponentCount() > MENU_BUTTON_COUNT) {

                getComponent(MENU_BUTTON_COUNT).setBounds(
                        xBoton
                        + MENU_BUTTON_WIDTH
                        + 12,
                        yInicial
                        + (MENU_BUTTON_COUNT - 1)
                        * (
                                MENU_BUTTON_HEIGHT
                                + MENU_BUTTON_GAP
                        ),
                        120,
                        MENU_BUTTON_HEIGHT
                );
            }
        }

        if (btnEnlace != null) {

            enlaceBaseX = Math.max(20, ancho - 180);
            enlaceBaseY = 20;

            if (!timerEnlace.isRunning()) {

                btnEnlace.setBounds(enlaceBaseX, enlaceBaseY, 140, 140);
            }
        }

        if (lblVersion != null) {

            lblVersion.setBounds(
                    Math.max(20, getWidth() - 155),
                    Math.max(20, getHeight() - 38),
                    135,
                    24
            );
        }
    }

    // =====================================================
    // BOTONES
    // =====================================================

    private JButton crearBoton(
            String texto,
            int x,
            int y
    ) {

        JButton btn =
                new JButton(texto);

        btn.setBounds(x, y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT);

        btn.setBackground(
                new Color(15, 15, 35)
        );

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setBorder(
                BorderFactory.createLineBorder(
                        new Color(0, 220, 255),
                        2
                )
        );

        btn.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                15
        ));

        // =====================================================
        // EFECTO HOVER
        // =====================================================

        btn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                btn.setBackground(
                        new Color(0, 120, 180)
                );

                btn.setBorder(
                        BorderFactory.createLineBorder(
                                Color.CYAN,
                                3
                        )
                );
            }

            @Override
            public void mouseExited(MouseEvent e) {

                btn.setBackground(
                        new Color(15, 15, 35)
                );

                btn.setBorder(
                        BorderFactory.createLineBorder(
                                new Color(0, 220, 255),
                                2
                        )
                );
            }
        });

        return btn;
    }

    private JButton crearBotonColor(
            String texto,
            Color color
    ) {

        JButton btn =
                crearBoton(texto, 0, 0);

        btn.setBackground(color);

        btn.setBorder(
                BorderFactory.createLineBorder(
                        new Color(255, 230, 120),
                        2
                )
        );

        return btn;
    }

    private JButton crearBotonEnlace(Image imagen) {

        JButton btn =
                new JButton();

        Image iconoEscalado =
                imagen.getScaledInstance(
                        120,
                        120,
                        Image.SCALE_SMOOTH
                );

        btn.setIcon(new ImageIcon(iconoEscalado));

        btn.setBounds(0, 0, 140, 140);

        btn.setContentAreaFilled(false);

        btn.setBorder(
                BorderFactory.createLineBorder(
                        new Color(0, 220, 255),
                        2
                )
        );

        btn.setFocusPainted(false);

        btn.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        btn.setToolTipText(
                "Abrir pagina web"
        );

        timerEnlace =
                new Timer(
                        35,
                        e -> agitarBotonEnlace()
                );

        btn.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {

                enlaceTick = 0;

                btn.setBorder(
                        BorderFactory.createLineBorder(
                                Color.CYAN,
                                3
                        )
                );

                timerEnlace.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {

                timerEnlace.stop();

                btn.setBorder(
                        BorderFactory.createLineBorder(
                                new Color(0, 220, 255),
                                2
                        )
                );

                btn.setBounds(
                        enlaceBaseX,
                        enlaceBaseY,
                        140,
                        140
                );
            }
        });

        btn.addActionListener(
                e -> abrirPaginaWeb()
        );

        return btn;
    }

    private JLabel crearEtiquetaVersion() {

        JLabel lbl =
                new JLabel(
                        "Version 1.6.0",
                        SwingConstants.RIGHT
                );

        lbl.setForeground(new Color(190, 220, 235));

        lbl.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                12
        ));

        lbl.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        lbl.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                alternarVentanaVersion();
            }
        });

        return lbl;
    }

    private void alternarVentanaVersion() {

        if (ventanaVersion != null
                && ventanaVersion.isVisible()) {

            ocultarVentanaVersion();
        }

        else {

            mostrarVentanaVersion();
        }
    }

    private void mostrarVentanaVersion() {

        if (ventanaVersion != null
                && ventanaVersion.isVisible()) {

            return;
        }

        ventanaVersion =
                new JWindow();

        JTextArea texto =
                new JTextArea(textoVersiones());

        texto.setEditable(false);

        texto.setLineWrap(true);

        texto.setWrapStyleWord(true);

        texto.setBackground(new Color(250, 250, 245));

        texto.setForeground(new Color(25, 25, 30));

        texto.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                12
        ));

        texto.setMargin(new Insets(14, 14, 14, 14));

        JScrollPane scroll =
                new JScrollPane(texto);

        scroll.setBorder(
                BorderFactory.createLineBorder(
                        new Color(0, 180, 255),
                        2
                )
        );

        ventanaVersion.add(scroll);

        ventanaVersion.setSize(470, 420);

        Point ubicacion =
                lblVersion.getLocationOnScreen();

        ventanaVersion.setLocation(
                Math.max(10, ubicacion.x - 455),
                Math.max(10, ubicacion.y - 425)
        );

        ventanaVersion.setVisible(true);
    }

    private void ocultarVentanaVersion() {

        if (ventanaVersion != null) {

            ventanaVersion.setVisible(false);

            ventanaVersion.dispose();

            ventanaVersion = null;
        }
    }

    private String textoVersiones() {

        return ""
                + "HISTORIAL DE VERSIONES - SIMULADOR DE FISICA\n"
                + "Version actual: 1.6.0\n"
                + "================================================\n\n"
                + "Version 0.1.0 - Primer prototipo\n"
                + "NUEVO:\n"
                + "- Se creo la estructura inicial del proyecto en Swing.\n"
                + "- Se agrego el primer simulador de Tiro Parabolico basico.\n"
                + "ARREGLADO:\n"
                + "- Ajustes iniciales de ventana y cierre de aplicacion.\n\n"
                + "Version 0.4.0 - Simuladores de movimiento\n"
                + "NUEVO:\n"
                + "- Se agregaron MRU y MRUV.\n"
                + "- Se implementaron controles de velocidad, distancia y aceleracion.\n"
                + "ARREGLADO:\n"
                + "- Correcciones en etiquetas de tiempo y distancia.\n"
                + "- Mejoras menores en el repintado de animaciones.\n\n"
                + "Version 0.7.0 - Menu principal\n"
                + "NUEVO:\n"
                + "- Se agrego el MenuPrincipal con botones para abrir modulos.\n"
                + "- Se incorporo fondo animado con particulas.\n"
                + "ARREGLADO:\n"
                + "- Se corrigio el retorno desde simuladores hacia el menu.\n"
                + "- Se evito que ventanas secundarias cerraran toda la aplicacion.\n\n"
                + "Version 1.0.0 - Caida Libre y experiencia visual\n"
                + "NUEVO:\n"
                + "- Se agrego el modulo de Caida Libre.\n"
                + "- Se agregaron objetos por peso, rebotes y datos de energia.\n"
                + "ARREGLADO:\n"
                + "- Ajustes de validacion para entradas negativas o vacias.\n"
                + "- Correccion de estados al finalizar simulaciones.\n\n"
                + "Version 1.2.0 - Pantalla completa\n"
                + "NUEVO:\n"
                + "- Menu y simuladores se abren maximizados.\n"
                + "- Se agregaron paneles de resultados y graficas.\n"
                + "ARREGLADO:\n"
                + "- Reorganizacion de controles para evitar espacios vacios.\n"
                + "- Limite de puntos en rastros visuales para evitar lentitud.\n\n"
                + "Version 1.4.0 - Rediseño del menu\n"
                + "NUEVO:\n"
                + "- Se reemplazo el titulo de texto por imagen animada.\n"
                + "- Se agrego logo centrado y boton de enlace web animado.\n"
                + "ARREGLADO:\n"
                + "- Correccion del orden visual del menu.\n"
                + "- Ajuste de botones para no chocar con imagenes grandes.\n\n"
                + "Version 1.5.0 - CalculusGrapher360\n"
                + "NUEVO:\n"
                + "- Se agrego CalculusGrapher360 con arquitectura MVC.\n"
                + "- Se implemento plano cartesiano con zoom, arrastre y puntos.\n"
                + "- Se agrego parser para polinomios, sin(x), cos(x) y tan(x).\n"
                + "ARREGLADO:\n"
                + "- Se agrego boton VOLVER para regresar al menu principal.\n\n"
                + "Version 1.5.2 - Historial interactivo\n"
                + "NUEVO:\n"
                + "- Se agrego etiqueta de version en el menu principal.\n"
                + "- Se agrego esta hoja de historial al hacer click en la version.\n"
                + "ARREGLADO:\n"
                + "- Mejoras de organizacion visual en el menu inferior.\n"
                + "- Ajuste de textos informativos para evitar desbordes.\n\n"
                + "Version 1.6.0 - Version actual\n"
                + "NUEVO:\n"
                + "- Se agrego el minijuego Ocio JuegaAprendiendo tipo Angry Birds simplificado.\n"
                + "- Se agregaron bloques de hielo, madera, piedra y obsidiana con durabilidad.\n"
                + "- Se implemento record persistente del minijuego usando archivo de texto.\n"
                + "- Se agrego el Simulador de Pendulo Simple con fisica RK4 amortiguada.\n"
                + "- Se agregaron controles de longitud, masa, friccion, gravedad y angulo inicial.\n"
                + "ARREGLADO:\n"
                + "- Se reorganizo el menu para incluir nuevos accesos sin tapar el boton salir.\n"
                + "- Se agregaron botones VOLVER en modulos nuevos para regresar al menu principal.\n"
                + "- Se valido la compilacion general despues de integrar los nuevos modulos.\n";
    }

    private void agitarBotonEnlace() {

        if (btnEnlace == null) {

            return;
        }

        int desplazamientoX =
                (int)(
                        Math.sin(enlaceTick * 1.8)
                        * 7
                );

        int desplazamientoY =
                (enlaceTick % 2 == 0)
                ? 2
                : -2;

        btnEnlace.setBounds(
                enlaceBaseX + desplazamientoX,
                enlaceBaseY + desplazamientoY,
                140,
                140
        );

        enlaceTick++;
    }

    private void abrirPaginaWeb() {

        try {

            if (Desktop.isDesktopSupported()) {

                Desktop.getDesktop().browse(
                        new java.net.URI(
                                "https://fisica-lab-alpha-web.vercel.app/"
                        )
                );
            }

            else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo abrir el navegador."
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo abrir la pagina web."
            );
        }
    }

    // =====================================================
    // ABRIR MODULOS
    // =====================================================

    private void abrirModulo(JPanel panel) {

        JFrame ventana =
                new JFrame();

        ventana.add(panel);

        ventana.setSize(1300, 700);

        ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);

        ventana.setLocationRelativeTo(null);

        ventana.setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        ventana.setVisible(true);

        menu.setVisible(false);
    }

    private void dibujarTarjeta(
            Graphics2D g2,
            int x,
            int y,
            int w,
            int h,
            String titulo,
            String[] lineas,
            Color acento
    ) {

        g2.setColor(new Color(12, 18, 38, 190));

        g2.fillRoundRect(x, y, w, h, 18, 18);

        g2.setColor(acento);

        g2.setStroke(new BasicStroke(2));

        g2.drawRoundRect(x, y, w, h, 18, 18);

        g2.setFont(new Font(
                "JetBrains Mono",
                Font.BOLD,
                17
        ));

        g2.drawString(titulo, x + 18, y + 34);

        g2.setFont(new Font(
                "JetBrains Mono",
                Font.PLAIN,
                13
        ));

        g2.setColor(new Color(225, 235, 245));

        int lineaY = y + 62;

        for (String linea : lineas) {

            g2.drawString(linea, x + 18, lineaY);

            lineaY += 22;
        }
    }

    private void dibujarPanelesInformativos(Graphics2D g2) {

        int derecha = Math.max(460, getWidth() - 500);
        int inicioY = Math.max(175, getHeight() / 2 - 285);

        dibujarTarjeta(
                g2,
                derecha,
                inicioY,
                440,
                105,
                "MRU",
                new String[] {
                        "Velocidad constante",
                        "Formula: d = v * t",
                        "Progreso y tiempo"
                },
                new Color(0, 220, 255)
        );

        dibujarTarjeta(
                g2,
                derecha,
                inicioY + 125,
                440,
                105,
                "MRUV",
                new String[] {
                        "Aceleracion constante",
                        "Velocidad final en vivo",
                        "Grafica distancia-tiempo"
                },
                new Color(255, 150, 0)
        );

        dibujarTarjeta(
                g2,
                derecha,
                inicioY + 250,
                440,
                105,
                "CAIDA LIBRE",
                new String[] {
                        "Altura, rebotes y energia",
                        "Energia y altura",
                        "Objetos segun peso"
                },
                new Color(0, 255, 150)
        );

        dibujarTarjeta(
                g2,
                derecha,
                inicioY + 375,
                440,
                105,
                "TIRO PARABOLICO",
                new String[] {
                        "Trayectoria predicha",
                        "Alcance y altura maxima",
                        "Componentes vx y vy"
                },
                new Color(255, 180, 0)
        );
    }

    private void dibujarTituloImagen(Graphics2D g2) {

        if (tituloImagen == null) {

            return;
        }

        int x = 35;
        int y = 35;
        int centroX = x + TITULO_WIDTH / 2;
        int centroY = y + TITULO_HEIGHT / 2;

        double angulo =
                Math.toRadians(
                        Math.sin(tituloTick * 0.08)
                        * 4
                );

        Graphics2D copia =
                (Graphics2D) g2.create();

        copia.rotate(
                angulo,
                centroX,
                centroY
        );

        copia.drawImage(
                tituloImagen,
                x,
                y,
                TITULO_WIDTH,
                TITULO_HEIGHT,
                this
        );

        copia.dispose();
    }

    // =====================================================
    // PINTAR FONDO
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
        // GRADIENTE
        // =====================================================

        GradientPaint fondo =
                new GradientPaint(
                        0,
                        0,
                        new Color(5, 5, 20),
                        0,
                        getHeight(),
                        new Color(15, 15, 45)
                );

        g2.setPaint(fondo);

        g2.fillRect(
                0,
                0,
                getWidth(),
                getHeight()
        );

        // =====================================================
        // PARTICULAS
        // =====================================================

        for (Point p : particulas) {

            g2.setColor(
                    new Color(
                            0,
                            255,
                            255,
                            120
                    )
            );

            g2.fillOval(
                    p.x,
                    p.y,
                    4,
                    4
            );
        }

        // =====================================================
        // LINEAS FUTURISTAS
        // =====================================================

        g2.setColor(
                new Color(
                        0,
                        255,
                        255,
                        25
                )
        );

        for (int i = 0; i < getWidth(); i += 80) {

            g2.drawLine(
                    i,
                    0,
                    i - 250,
                    getHeight()
            );
        }

        dibujarTituloImagen(g2);

        dibujarPanelesInformativos(g2);

        // =====================================================
        // LOGO
        // =====================================================

        if (logo != null) {

            g2.drawImage(
                    logo,
                    Math.max(20, (getWidth() - LOGO_SIZE) / 2),
                    Math.max(20, (getHeight() - LOGO_SIZE) / 2),
                    LOGO_SIZE,
                    LOGO_SIZE,
                    this
            );
        }
    }

    // =====================================================
    // ANIMACION PARTICULAS
    // =====================================================

    @Override
    public void actionPerformed(ActionEvent e) {

        for (Point p : particulas) {

            p.y += 1;

            if (p.y > getHeight()) {

                p.y = 0;

                p.x =
                        random.nextInt(getWidth());
            }
        }

        tituloTick++;

        repaint();
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        menu =
                new JFrame(
                        "SIMULADOR DE FISICA"
                );

        menu.add(new MenuPrincipal());

        menu.setSize(1300, 700);

        menu.setExtendedState(JFrame.MAXIMIZED_BOTH);

        menu.setLocationRelativeTo(null);

        menu.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        menu.setVisible(true);
    }
}
