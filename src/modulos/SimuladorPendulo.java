package modulos;

import principal.MenuPrincipal;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class SimuladorPendulo extends JFrame {

    public SimuladorPendulo() {

        super("Simulador de Pendulo Simple");

        PendulumPanel pendulumPanel =
                new PendulumPanel();

        ControlPanel controlPanel =
                new ControlPanel(pendulumPanel);

        setLayout(new BorderLayout());
        add(pendulumPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new SimuladorPendulo().setVisible(true)
        );
    }

    static class PendulumPanel extends JPanel {

        private static final double DT = 0.016;

        private double length = 2.0;
        private double mass = 1.0;
        private double damping = 0.08;
        private double gravity = 9.81;
        private double initialAngle = Math.toRadians(35);
        private double theta = initialAngle;
        private double omega = 0.0;
        private double elapsedTime = 0.0;
        private boolean running = false;
        private final Timer timer;

        PendulumPanel() {

            setBackground(new Color(12, 16, 28));

            timer = new Timer(16, e -> {

                if (running) {

                    stepPhysics();
                    elapsedTime += DT;
                }

                repaint();
            });

            timer.start();
        }

        public void setLength(double length) {

            this.length = length;
        }

        public void setMass(double mass) {

            this.mass = mass;
        }

        public void setDamping(double damping) {

            this.damping = damping;
        }

        public void setGravity(double gravity) {

            this.gravity = gravity;
        }

        public void setInitialAngleDegrees(double degrees) {

            this.initialAngle = Math.toRadians(degrees);

            if (!running) {

                theta = initialAngle;
                omega = 0;
                elapsedTime = 0;
                repaint();
            }
        }

        public void startSimulation() {

            running = true;
        }

        public void pauseSimulation() {

            running = false;
        }

        public void resetSimulation() {

            running = false;
            theta = initialAngle;
            omega = 0;
            elapsedTime = 0;
            repaint();
        }

        // Fisica: RK4 para theta'' + (b/m)*theta' + (g/L)*sin(theta) = 0.
        private void stepPhysics() {

            double[] k1 = derivative(theta, omega);
            double[] k2 = derivative(
                    theta + 0.5 * DT * k1[0],
                    omega + 0.5 * DT * k1[1]
            );
            double[] k3 = derivative(
                    theta + 0.5 * DT * k2[0],
                    omega + 0.5 * DT * k2[1]
            );
            double[] k4 = derivative(
                    theta + DT * k3[0],
                    omega + DT * k3[1]
            );

            theta += DT / 6.0
                    * (k1[0] + 2 * k2[0] + 2 * k3[0] + k4[0]);
            omega += DT / 6.0
                    * (k1[1] + 2 * k2[1] + 2 * k3[1] + k4[1]);
        }

        private double[] derivative(double angle, double angularVelocity) {

            double angularAcceleration =
                    -((damping / mass) * angularVelocity)
                    -((gravity / length) * Math.sin(angle));

            return new double[] {
                    angularVelocity,
                    angularAcceleration
            };
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            drawBackground(g2);
            drawPendulum(g2);
            drawInfo(g2);
        }

        private void drawBackground(Graphics2D g2) {

            g2.setColor(new Color(18, 24, 40));
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(255, 255, 255, 22));

            for (int x = 0; x < getWidth(); x += 40) {

                g2.drawLine(x, 0, x, getHeight());
            }

            for (int y = 0; y < getHeight(); y += 40) {

                g2.drawLine(0, y, getWidth(), y);
            }
        }

        // Renderizado: se escala longitud fisica en metros a pixeles visibles.
        private void drawPendulum(Graphics2D g2) {

            int pivotX = getWidth() / 2;
            int pivotY = 90;
            double pixelsPerMeter =
                    Math.min(170, (getHeight() - 180) / Math.max(1.0, length));
            int ropePixels =
                    (int) (length * pixelsPerMeter);

            int bobX =
                    pivotX + (int) (ropePixels * Math.sin(theta));
            int bobY =
                    pivotY + (int) (ropePixels * Math.cos(theta));

            g2.setStroke(new BasicStroke(4));
            g2.setColor(new Color(230, 230, 235));
            g2.drawLine(pivotX, pivotY, bobX, bobY);

            g2.setColor(new Color(255, 180, 0));
            g2.fillOval(pivotX - 9, pivotY - 9, 18, 18);

            int radius =
                    (int) Math.max(18, Math.min(45, 18 + mass * 5));

            g2.setColor(new Color(0, 180, 255));
            g2.fillOval(
                    bobX - radius,
                    bobY - radius,
                    radius * 2,
                    radius * 2
            );

            g2.setColor(new Color(170, 235, 255));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(
                    bobX - radius,
                    bobY - radius,
                    radius * 2,
                    radius * 2
            );

            g2.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
            g2.setColor(new Color(220, 230, 240));
            g2.drawString("Pivote fijo", pivotX + 14, pivotY - 12);
        }

        private void drawInfo(Graphics2D g2) {

            g2.setColor(new Color(8, 12, 22, 205));
            g2.fillRoundRect(18, 18, 320, 100, 14, 14);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
            g2.drawString(
                    "Angulo: "
                    + String.format("%.2f", Math.toDegrees(theta))
                    + " grados",
                    34,
                    48
            );
            g2.drawString(
                    "Vel. angular: "
                    + String.format("%.3f", omega)
                    + " rad/s",
                    34,
                    75
            );
            g2.drawString(
                    "Tiempo: "
                    + String.format("%.2f", elapsedTime)
                    + " s",
                    34,
                    102
            );
        }
    }

    static class ControlPanel extends JPanel {

        ControlPanel(PendulumPanel pendulum) {

            setLayout(null);
            setPreferredSize(new java.awt.Dimension(270, 0));
            setBackground(new Color(16, 20, 34));
            setBorder(
                    BorderFactory.createMatteBorder(
                            0,
                            2,
                            0,
                            0,
                            new Color(0, 180, 255)
                    )
            );

            JLabel title = new JLabel("CONTROLES", SwingConstants.CENTER);
            title.setBounds(25, 20, 220, 35);
            title.setForeground(new Color(0, 220, 255));
            title.setFont(new Font("JetBrains Mono", Font.BOLD, 20));
            add(title);

            JSlider lengthSlider = createSlider(50, 500, 200);
            addLabeledSlider("Longitud L: 2.00 m", lengthSlider, 80, value -> {
                pendulum.setLength(value / 100.0);
                return "Longitud L: " + String.format("%.2f", value / 100.0) + " m";
            });

            JSlider massSlider = createSlider(10, 500, 100);
            addLabeledSlider("Masa m: 1.00 kg", massSlider, 170, value -> {
                pendulum.setMass(value / 100.0);
                return "Masa m: " + String.format("%.2f", value / 100.0) + " kg";
            });

            JSlider dampingSlider = createSlider(0, 100, 8);
            addLabeledSlider("Friccion b: 0.08", dampingSlider, 260, value -> {
                pendulum.setDamping(value / 100.0);
                return "Friccion b: " + String.format("%.2f", value / 100.0);
            });

            JSlider gravitySlider = createSlider(100, 2000, 981);
            addLabeledSlider("Gravedad g: 9.81", gravitySlider, 350, value -> {
                pendulum.setGravity(value / 100.0);
                return "Gravedad g: " + String.format("%.2f", value / 100.0);
            });

            JSlider angleSlider = createSlider(-85, 85, 35);
            addLabeledSlider("Angulo inicial: 35 grados", angleSlider, 440, value -> {
                pendulum.setInitialAngleDegrees(value);
                return "Angulo inicial: " + value + " grados";
            });

            JButton start = createButton("INICIAR", new Color(0, 150, 255));
            start.setBounds(35, 535, 200, 42);
            start.addActionListener(e -> pendulum.startSimulation());
            add(start);

            JButton pause = createButton("PAUSAR", new Color(65, 75, 95));
            pause.setBounds(35, 590, 200, 42);
            pause.addActionListener(e -> pendulum.pauseSimulation());
            add(pause);

            JButton reset = createButton("REINICIAR", new Color(255, 130, 0));
            reset.setBounds(35, 645, 200, 42);
            reset.addActionListener(e -> pendulum.resetSimulation());
            add(reset);

            JButton back = createButton("VOLVER", new Color(120, 70, 200));
            back.setBounds(35, 700, 200, 36);
            back.addActionListener(e -> {
                Window window = SwingUtilities.getWindowAncestor(this);

                if (window != null) {

                    window.dispose();
                }

                if (MenuPrincipal.menu != null) {

                    MenuPrincipal.menu.setVisible(true);
                }
            });
            add(back);
        }

        private JSlider createSlider(int min, int max, int value) {

            JSlider slider = new JSlider(min, max, value);
            slider.setBackground(new Color(16, 20, 34));
            slider.setForeground(Color.WHITE);
            return slider;
        }

        private void addLabeledSlider(
                String text,
                JSlider slider,
                int y,
                SliderFormatter formatter
        ) {

            JLabel label = new JLabel(text);
            label.setBounds(25, y, 220, 24);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
            add(label);

            slider.setBounds(25, y + 28, 220, 42);
            slider.addChangeListener(e -> label.setText(formatter.format(slider.getValue())));
            add(slider);
        }

        private JButton createButton(String text, Color color) {

            JButton button = new JButton(text);
            button.setBackground(color);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
            return button;
        }
    }

    interface SliderFormatter {

        String format(int value);
    }
}
