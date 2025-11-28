package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.utils.UserManager;
import com.mycompany.fisicalab.utils.UIHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginRegisterDialog extends JDialog {

    private UserManager userManager;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;

    public LoginRegisterDialog(Frame owner, UserManager userManager) {
        super(owner, "Iniciar Sesión / Registrar", true);
        this.userManager = userManager;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setResizable(false);

        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Iniciar Sesión");
        registerButton = new JButton("Registrar");
        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);

        // Estilos
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);

        usernameField.setFont(fieldFont);
        passwordField.setFont(fieldFont);
        loginButton.setFont(buttonFont);
        registerButton.setFont(buttonFont);

        loginButton.setBackground(UIHelper.COLOR_PRIMARIO);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerButton.setBackground(UIHelper.COLOR_SECUNDARIO);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Bienvenido a FisicaLAB");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Espacio
        gbc.gridy++;
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)), gbc);

        // Usuario
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Contraseña
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        // Botones
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, gbc);

        // Mensaje
        gbc.gridy++;
        mainPanel.add(messageLabel, gbc);
        
        // Mensaje de límite de usuarios
        if (userManager.getUserCount() >= UserManager.MAX_USERS) {
            JLabel limitMessage = new JLabel("Máximo de " + UserManager.MAX_USERS + " usuarios registrados.");
            limitMessage.setHorizontalAlignment(SwingConstants.CENTER);
            limitMessage.setForeground(Color.ORANGE.darker());
            limitMessage.setFont(new Font("Arial", Font.ITALIC, 12));
            gbc.gridy++;
            mainPanel.add(limitMessage, gbc);
            registerButton.setEnabled(false); // Deshabilitar registro si se alcanzó el límite
        }

        add(mainPanel);
    }

    private void setupListeners() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (userManager.authenticateUser(username, password)) {
                    messageLabel.setForeground(UIHelper.COLOR_EXITO);
                    messageLabel.setText("¡Inicio de sesión exitoso!");
                    Timer timer = new Timer(1000, ev -> dispose());
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    messageLabel.setForeground(UIHelper.COLOR_PELIGRO);
                    messageLabel.setText("Usuario o contraseña incorrectos.");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                    messageLabel.setForeground(UIHelper.COLOR_PELIGRO);
                    messageLabel.setText("Usuario y contraseña no pueden estar vacíos.");
                    return;
                }
                if (userManager.registerUser(username, password)) {
                    messageLabel.setForeground(UIHelper.COLOR_EXITO);
                    messageLabel.setText("¡Registro exitoso! Ahora puedes iniciar sesión.");
                    // Opcional: iniciar sesión automáticamente después del registro
                    // userManager.authenticateUser(username, password);
                    // Timer timer = new Timer(1000, ev -> dispose());
                    // timer.setRepeats(false);
                    // timer.start();
                } else {
                    messageLabel.setForeground(UIHelper.COLOR_PELIGRO);
                    messageLabel.setText("Error al registrar usuario. ¿Ya existe o se alcanzó el límite?");
                }
            }
        });
    }
    
    public String getLoggedInUser() {
        return userManager.getCurrentUser();
    }
}
