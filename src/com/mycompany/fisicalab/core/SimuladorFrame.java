package com.mycompany.fisicalab.core;

import com.mycompany.fisicalab.ui.MenuPrincipal;
import javax.swing.*;
import java.awt.*;

/**
 * Punto de entrada principal de la aplicación FisicaLab
 */
public class SimuladorFrame extends JFrame {
    
    public SimuladorFrame() {
        setTitle("FisicaLab - Simulador de Física v2.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Pantalla completa o maximizada
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false); // Mantener barra de título
        
        // Tamaño mínimo
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Configurar look and feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Mostrar menú principal
        mostrarMenuPrincipal();
    }
    
    public void mostrarMenuPrincipal() {
        getContentPane().removeAll();
        MenuPrincipal menu = new MenuPrincipal(this);
        setContentPane(menu);
        revalidate();
        repaint();
    }
    
    public void mostrarSimulacion(JPanel simulacion) {
        getContentPane().removeAll();
        setContentPane(simulacion);
        revalidate();
        repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimuladorFrame frame = new SimuladorFrame();
            frame.setVisible(true);
        });
    }
}
