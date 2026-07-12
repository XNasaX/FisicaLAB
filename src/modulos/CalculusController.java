package modulos;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;

public class CalculusController {

    private final CalculusModel model;
    private final CalculusView view;
    private int lastMouseX;
    private int lastMouseY;
    private boolean panning;

    public CalculusController(
            CalculusModel model,
            CalculusView view
    ) {

        this.model = model;
        this.view = view;
        bindActions();
        bindMouse();
    }

    private void bindActions() {

        view.getAddButton().addActionListener(
                e -> addFunction()
        );

        view.getFunctionField().addActionListener(
                e -> addFunction()
        );

        view.getRemoveButton().addActionListener(
                e -> removeSelectedFunction()
        );

        view.getBackButton().addActionListener(
                e -> view.dispose()
        );

        view.getFunctionList().addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_DELETE) {

                    removeSelectedFunction();
                }
            }
        });
    }

    private void bindMouse() {

        GraphPanel panel = view.getGraphPanel();

        MouseAdapter mouse =
                new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {

                        lastMouseX = e.getX();
                        lastMouseY = e.getY();
                        panning =
                                SwingUtilities.isRightMouseButton(e)
                                || SwingUtilities.isMiddleMouseButton(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                        panning = false;
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {

                        if (SwingUtilities.isLeftMouseButton(e)
                                && e.getClickCount() == 1) {

                            double x =
                                    panel.toLogicalX(e.getX());

                            double y =
                                    panel.toLogicalY(e.getY());

                            model.addPoint(x, y);
                            panel.repaint();
                        }
                    }

                    @Override
                    public void mouseDragged(MouseEvent e) {

                        if (!panning) {

                            return;
                        }

                        int deltaX = e.getX() - lastMouseX;
                        int deltaY = e.getY() - lastMouseY;

                        model.X_offset += deltaX;
                        model.Y_offset += deltaY;

                        lastMouseX = e.getX();
                        lastMouseY = e.getY();

                        panel.repaint();
                    }

                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e) {

                        double logicalXBefore =
                                panel.toLogicalX(e.getX());

                        double logicalYBefore =
                                panel.toLogicalY(e.getY());

                        if (e.getWheelRotation() < 0) {

                            model.scaleFactor *= 1.1;
                        }

                        else {

                            model.scaleFactor /= 1.1;
                        }

                        model.scaleFactor =
                                Math.max(
                                        8,
                                        Math.min(600, model.scaleFactor)
                                );

                        model.X_offset =
                                e.getX()
                                - panel.getWidth() / 2.0
                                - logicalXBefore * model.scaleFactor;

                        model.Y_offset =
                                e.getY()
                                - panel.getHeight() / 2.0
                                + logicalYBefore * model.scaleFactor;

                        panel.repaint();
                    }
                };

        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        panel.addMouseWheelListener(mouse);
    }

    private void addFunction() {

        String function =
                view.getFunctionField().getText();

        model.addFunction(function);
        refreshFunctionList();
        view.getGraphPanel().repaint();
    }

    private void removeSelectedFunction() {

        int index =
                view.getFunctionList()
                        .getSelectedIndex();

        model.removeFunction(index);
        refreshFunctionList();
        view.getGraphPanel().repaint();
    }

    private void refreshFunctionList() {

        view.getFunctionListModel().clear();

        for (String function : model.getFunctions()) {

            view.getFunctionListModel().addElement(function);
        }
    }
}
