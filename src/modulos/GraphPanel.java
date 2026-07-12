package modulos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JPanel;

public class GraphPanel extends JPanel {

    private final CalculusModel model;
    private final ExpressionParser parser;
    private final Color[] colors;

    public GraphPanel(CalculusModel model) {

        this.model = model;
        parser = new ExpressionParser();
        colors = new Color[] {
                new Color(0, 170, 255),
                new Color(255, 120, 0),
                new Color(0, 210, 130),
                new Color(220, 80, 255),
                new Color(255, 220, 0)
        };

        setBackground(new Color(8, 10, 18));
    }

    public double toScreenX(double logicalX) {

        return getWidth() / 2.0
                + model.X_offset
                + logicalX * model.scaleFactor;
    }

    public double toScreenY(double logicalY) {

        return getHeight() / 2.0
                + model.Y_offset
                - logicalY * model.scaleFactor;
    }

    public double toLogicalX(double screenX) {

        return (
                screenX
                - getWidth() / 2.0
                - model.X_offset
        ) / model.scaleFactor;
    }

    public double toLogicalY(double screenY) {

        return -(
                screenY
                - getHeight() / 2.0
                - model.Y_offset
        ) / model.scaleFactor;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        drawGrid(g2);
        drawAxes(g2);
        drawFunctions(g2);
        drawPoints(g2);
        drawHud(g2);
    }

    private void drawGrid(Graphics2D g2) {

        double step = niceStep(70 / model.scaleFactor);
        double minX = toLogicalX(0);
        double maxX = toLogicalX(getWidth());
        double minY = toLogicalY(getHeight());
        double maxY = toLogicalY(0);

        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(80, 90, 110, 70));

        double startX = Math.floor(minX / step) * step;

        for (double x = startX; x <= maxX; x += step) {

            double sx = toScreenX(x);
            g2.draw(new Line2D.Double(sx, 0, sx, getHeight()));
        }

        double startY = Math.floor(minY / step) * step;

        for (double y = startY; y <= maxY; y += step) {

            double sy = toScreenY(y);
            g2.draw(new Line2D.Double(0, sy, getWidth(), sy));
        }

        g2.setColor(new Color(150, 160, 180, 130));
        g2.setFont(new Font("JetBrains Mono", Font.PLAIN, 11));

        for (double x = startX; x <= maxX; x += step) {

            double sx = toScreenX(x);

            if (Math.abs(x) > step / 2) {

                g2.drawString(format(x), (int) sx + 4, (int) toScreenY(0) - 4);
            }
        }

        for (double y = startY; y <= maxY; y += step) {

            double sy = toScreenY(y);

            if (Math.abs(y) > step / 2) {

                g2.drawString(format(y), (int) toScreenX(0) + 6, (int) sy - 4);
            }
        }
    }

    private void drawAxes(Graphics2D g2) {

        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(235, 240, 250));

        double xAxisY = toScreenY(0);
        double yAxisX = toScreenX(0);

        g2.draw(new Line2D.Double(0, xAxisY, getWidth(), xAxisY));
        g2.draw(new Line2D.Double(yAxisX, 0, yAxisX, getHeight()));

        g2.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        g2.drawString("X", getWidth() - 24, (int) xAxisY - 8);
        g2.drawString("Y", (int) yAxisX + 8, 20);
    }

    private void drawFunctions(Graphics2D g2) {

        int index = 0;

        for (String function : model.getFunctions()) {

            g2.setColor(colors[index % colors.length]);
            g2.setStroke(new BasicStroke(2.4f));

            Point2D.Double previous = null;

            for (int px = 0; px < getWidth(); px++) {

                double logicalX = toLogicalX(px);
                double logicalY;

                try {

                    logicalY = parser.evaluate(function, logicalX);

                } catch (Exception ex) {

                    previous = null;
                    continue;
                }

                if (!Double.isFinite(logicalY)
                        || Math.abs(logicalY) > 1_000_000) {

                    previous = null;
                    continue;
                }

                double py = toScreenY(logicalY);

                if (py < -10000 || py > getHeight() + 10000) {

                    previous = null;
                    continue;
                }

                Point2D.Double current =
                        new Point2D.Double(px, py);

                if (previous != null) {

                    g2.draw(
                            new Line2D.Double(
                                    previous,
                                    current
                            )
                    );
                }

                previous = current;
            }

            index++;
        }
    }

    private void drawPoints(Graphics2D g2) {

        g2.setColor(new Color(255, 60, 70));

        for (Point2D.Double point : model.getPoints()) {

            double sx = toScreenX(point.x);
            double sy = toScreenY(point.y);

            g2.fillOval(
                    (int) sx - 5,
                    (int) sy - 5,
                    10,
                    10
            );

            g2.drawString(
                    "(" + format(point.x) + ", " + format(point.y) + ")",
                    (int) sx + 8,
                    (int) sy - 8
            );
        }
    }

    private void drawHud(Graphics2D g2) {

        g2.setColor(new Color(10, 15, 25, 190));
        g2.fillRoundRect(18, getHeight() - 72, 420, 48, 12, 12);

        g2.setColor(new Color(220, 230, 240));
        g2.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        g2.drawString(
                "Click izquierdo: punto | Arrastre derecho/rueda: mover | Rueda: zoom",
                34,
                getHeight() - 44
        );
        g2.drawString(
                "Zoom: " + String.format("%.2f", model.scaleFactor),
                34,
                getHeight() - 27
        );
    }

    private double niceStep(double raw) {

        double exponent = Math.floor(Math.log10(raw));
        double base = Math.pow(10, exponent);
        double fraction = raw / base;

        if (fraction < 2) {

            return base;
        }

        if (fraction < 5) {

            return 2 * base;
        }

        return 5 * base;
    }

    private String format(double value) {

        if (Math.abs(value) >= 1000 || Math.abs(value) < 0.01 && value != 0) {

            return String.format("%.1e", value);
        }

        if (Math.abs(value - Math.round(value)) < 0.0001) {

            return String.valueOf((long) Math.round(value));
        }

        return String.format("%.2f", value);
    }
}
