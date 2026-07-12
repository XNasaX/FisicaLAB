package modulos;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalculusModel {

    private final ArrayList<String> functions;
    private final ArrayList<Point2D.Double> points;

    public double X_offset;
    public double Y_offset;
    public double scaleFactor;

    public CalculusModel() {

        functions = new ArrayList<>();
        points = new ArrayList<>();
        X_offset = 0;
        Y_offset = 0;
        scaleFactor = 55;
    }

    public void addFunction(String function) {

        String cleaned = function.trim();

        if (!cleaned.isEmpty()) {

            functions.add(cleaned);
        }
    }

    public void removeFunction(int index) {

        if (index >= 0 && index < functions.size()) {

            functions.remove(index);
        }
    }

    public List<String> getFunctions() {

        return Collections.unmodifiableList(functions);
    }

    public void addPoint(double x, double y) {

        points.add(new Point2D.Double(x, y));
    }

    public List<Point2D.Double> getPoints() {

        return Collections.unmodifiableList(points);
    }
}
