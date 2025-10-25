package com.mycompany.fisicalab.ui;

import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;

/**
 * Menú principal de la aplicación
 */
public class MenuPrincipal extends JPanel {
    
    private SimuladorFrame frame;
    
    public MenuPrincipal(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(UIHelper.COLOR_FONDO);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // Título
        JLabel titulo = UIHelper.crearTitulo("FISICALAB");
        titulo.setFont(new Font("Arial", Font.BOLD, 52));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setForeground(UIHelper.COLOR_PRIMARIO);
        
        JLabel subtitulo = UIHelper.crearSubtitulo("Simulador de Física Interactivo v2.0");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel version = new JLabel("Alpha 2.0 - Edición Avanzada");
        version.setFont(new Font("Arial", Font.ITALIC, 14));
        version.setForeground(new Color(127, 140, 141));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCentral.add(titulo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCentral.add(subtitulo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 5)));
        panelCentral.add(version);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 0, 15));
        panelBotones.setOpaque(false);
        panelBotones.setMaximumSize(new Dimension(300, 300));
        
        // Botón MRU
        JButton btnMRU = UIHelper.crearBotonRedondeado("Movimiento Rectilíneo Uniforme", 
                                                        UIHelper.COLOR_PRIMARIO);
        btnMRU.addActionListener(e -> abrirSimulacion("MRU"));
        
        // Botón Caída Libre
        JButton btnCaidaLibre = UIHelper.crearBotonRedondeado("Caída Libre", 
                                                               UIHelper.COLOR_SECUNDARIO);
        btnCaidaLibre.addActionListener(e -> abrirSimulacion("CAIDA_LIBRE"));
        
        // Botón Tiro Parabólico
        JButton btnTiroParabolico = UIHelper.crearBotonRedondeado("Tiro Parabólico", 
                                                                   UIHelper.COLOR_EXITO);
        btnTiroParabolico.addActionListener(e -> abrirSimulacion("TIRO_PARABOLICO"));
        
        // Botón Historial (deshabilitado por ahora)
        JButton btnHistorial = UIHelper.crearBotonRedondeado("Historial de Resultados", 
                                                              new Color(149, 165, 166));
        btnHistorial.setEnabled(false);
        
        // Botón Salir
        JButton btnSalir = UIHelper.crearBotonRedondeado("Salir", UIHelper.COLOR_PELIGRO);
        btnSalir.addActionListener(e -> System.exit(0));
        
        panelBotones.add(btnMRU);
        panelBotones.add(btnCaidaLibre);
        panelBotones.add(btnTiroParabolico);
        panelBotones.add(btnHistorial);
        panelBotones.add(btnSalir);
        
        panelCentral.add(panelBotones);
        
        // Panel inferior con información
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInfo.setOpaque(false);
        JLabel labelVersion = new JLabel("Versión Alpha 2.0 | Controles de teclado | Parámetros avanzados | Pantalla completa");
        labelVersion.setFont(new Font("Arial", Font.ITALIC, 12));
        labelVersion.setForeground(new Color(127, 140, 141));
        panelInfo.add(labelVersion);
        
        JLabel labelControles = new JLabel(" | ESPACIO: Iniciar | R: Reiniciar | V: Vectores | E: Energía");
        labelControles.setFont(new Font("Arial", Font.BOLD, 11));
        labelControles.setForeground(UIHelper.COLOR_PRIMARIO);
        panelInfo.add(labelControles);
        
        add(panelCentral, BorderLayout.CENTER);
        add(panelInfo, BorderLayout.SOUTH);
    }
    
    private void abrirSimulacion(String tipo) {
        JPanel simulacion = null;
        
        switch (tipo) {
            case "MRU":
                simulacion = new SimulacionMRU(frame);
                break;
            case "CAIDA_LIBRE":
                simulacion = new SimulacionCaidaLibre(frame);
                break;
            case "TIRO_PARABOLICO":
                simulacion = new SimulacionTiroParabolico(frame);
                break;
        }
        
        if (simulacion != null) {
            frame.mostrarSimulacion(simulacion);
        }
    }
}
