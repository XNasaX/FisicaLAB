package modulos;

import javax.swing.SwingUtilities;

public class CalculusGrapher {

    private final CalculusModel model;
    private final CalculusView view;
    private final CalculusController controller;

    public CalculusGrapher() {

        model = new CalculusModel();
        view = new CalculusView(model);
        controller = new CalculusController(model, view);
    }

    public void show() {

        view.setVisible(true);
    }

    public CalculusView getView() {

        return view;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                () -> new CalculusGrapher().show()
        );
    }
}
