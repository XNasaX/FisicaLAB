package com.mycompany.fisicalab.core;

import com.mycompany.fisicalab.ui.MenuPrincipal;
import javax.swing.*;
import java.awt.*;

/**
 * Punto de entrada principal de la aplicación FisicaLab
 */
public class SimuladorFrame extends JFrame {
    
    public SimuladorFrame() {
        setTitle("FisicaLab - Simulador de Física");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        
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
