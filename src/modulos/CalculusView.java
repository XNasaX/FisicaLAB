package modulos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class CalculusView extends JFrame {

    private final JTextField functionField;
    private final JList<String> functionList;
    private final DefaultListModel<String> functionListModel;
    private final JButton addButton;
    private final JButton removeButton;
    private final JButton backButton;
    private final GraphPanel graphPanel;

    public CalculusView(CalculusModel model) {

        super("CalculusGrapher360");

        functionField = new JTextField();
        functionListModel = new DefaultListModel<>();
        functionList = new JList<>(functionListModel);
        addButton = new JButton("AGREGAR");
        removeButton = new JButton("QUITAR");
        backButton = new JButton("VOLVER");
        graphPanel = new GraphPanel(model);

        setLayout(new BorderLayout());
        add(createSidePanel(), BorderLayout.WEST);
        add(graphPanel, BorderLayout.CENTER);

        setSize(1300, 760);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private JPanel createSidePanel() {

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(15, 18, 30));
        panel.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        0,
                        2,
                        new Color(0, 180, 255)
                )
        );
        panel.setPreferredSize(new java.awt.Dimension(310, 0));

        JLabel title =
                new JLabel(
                        "CalculusGrapher360",
                        SwingConstants.CENTER
                );
        title.setBounds(18, 20, 270, 35);
        title.setForeground(new Color(0, 220, 255));
        title.setFont(new Font("JetBrains Mono", Font.BOLD, 18));
        panel.add(title);

        JLabel inputLabel =
                new JLabel("Funcion f(x)");
        inputLabel.setBounds(28, 82, 240, 25);
        inputLabel.setForeground(Color.WHITE);
        inputLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        panel.add(inputLabel);

        functionField.setBounds(28, 112, 250, 38);
        functionField.setBackground(new Color(28, 32, 48));
        functionField.setForeground(Color.WHITE);
        functionField.setCaretColor(Color.WHITE);
        functionField.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        functionField.setText("x^2");
        panel.add(functionField);

        addButton.setBounds(28, 165, 118, 38);
        styleButton(addButton, new Color(0, 145, 255));
        panel.add(addButton);

        removeButton.setBounds(160, 165, 118, 38);
        styleButton(removeButton, new Color(220, 70, 80));
        panel.add(removeButton);

        JLabel listLabel =
                new JLabel("Funciones creadas");
        listLabel.setBounds(28, 230, 250, 25);
        listLabel.setForeground(Color.WHITE);
        listLabel.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        panel.add(listLabel);

        functionList.setBackground(new Color(25, 28, 44));
        functionList.setForeground(Color.WHITE);
        functionList.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));

        JScrollPane scroll =
                new JScrollPane(functionList);
        scroll.setBounds(28, 260, 250, 245);
        panel.add(scroll);

        JLabel help =
                new JLabel(
                        "<html>"
                        + "Ejemplos:<br>"
                        + "x^2<br>"
                        + "2*x + 1<br>"
                        + "sin(x)<br>"
                        + "cos(x)<br><br>"
                        + "Click izquierdo crea puntos.<br>"
                        + "Arrastre derecho mueve el plano.<br>"
                        + "Rueda del mouse hace zoom."
                        + "</html>"
                );
        help.setBounds(28, 535, 250, 170);
        help.setForeground(new Color(210, 220, 230));
        help.setFont(new Font("JetBrains Mono", Font.PLAIN, 12));
        panel.add(help);

        backButton.setBounds(28, 720, 250, 42);
        styleButton(backButton, new Color(70, 80, 105));
        panel.add(backButton);

        return panel;
    }

    private void styleButton(JButton button, Color color) {

        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
    }

    public JTextField getFunctionField() {

        return functionField;
    }

    public JList<String> getFunctionList() {

        return functionList;
    }

    public DefaultListModel<String> getFunctionListModel() {

        return functionListModel;
    }

    public JButton getAddButton() {

        return addButton;
    }

    public JButton getRemoveButton() {

        return removeButton;
    }

    public JButton getBackButton() {

        return backButton;
    }

    public GraphPanel getGraphPanel() {

        return graphPanel;
    }
}
