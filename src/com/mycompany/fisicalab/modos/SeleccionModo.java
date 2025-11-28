package com.mycompany.fisicalab.modos;

import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.ui.SimulacionCaidaLibre;
import com.mycompany.fisicalab.ui.SimulacionMRU;
import com.mycompany.fisicalab.ui.SimulacionMRUV;
import com.mycompany.fisicalab.ui.SimulacionTiroParabolico;
import com.mycompany.fisicalab.ui.SimulacionEstatica; // Importar el nuevo simulador de Estática
import com.mycompany.fisicalab.utils.UIHelper;
import java.awt.*;
import javax.swing.*;

/**
 * Panel de selección entre Modo Juego y Modo Aprende
 * Versión 3.0
 */
public class SeleccionModo extends JPanel {
    
    private SimuladorFrame frame;
    private String modoActual; // Para saber si estamos en "aprende" o "juego"
    
    public SeleccionModo(SimuladorFrame frame, String modo) {
        this.frame = frame;
        this.modoActual = modo;
        setLayout(new BorderLayout());
        setBackground(UIHelper.COLOR_FONDO);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        // Título
        JLabel titulo = new JLabel("Selecciona tu Actividad");
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(UIHelper.COLOR_PRIMARIO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitulo = new JLabel(modoActual.equals("aprende") ? 
                                      "Explora y aprende con nuestras simulaciones." : 
                                      "Acepta desafíos y gana logros.");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitulo.setForeground(new Color(52, 73, 94));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCentral.add(titulo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCentral.add(subtitulo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Panel de opciones dinámicas
        JPanel panelOpciones = new JPanel(new GridLayout(0, 2, 20, 20)); // Filas dinámicas, 2 columnas
        panelOpciones.setOpaque(false);
        panelOpciones.setMaximumSize(new Dimension(800, 600));
        
        if (modoActual.equals("aprende")) {
            // Opciones para Modo Aprende
            panelOpciones.add(crearBotonModo("Simulación Libre MRU", UIHelper.COLOR_PRIMARIO, "MRU"));
            panelOpciones.add(crearBotonModo("Simulación Libre MRUV", new Color(241, 196, 15), "MRUV"));
            panelOpciones.add(crearBotonModo("Simulación Libre Caída Libre", UIHelper.COLOR_SECUNDARIO, "CAIDA_LIBRE"));
            panelOpciones.add(crearBotonModo("Simulación Libre Tiro Parabólico", UIHelper.COLOR_EXITO, "TIRO_PARABOLICO"));
            panelOpciones.add(crearBotonModo("Simulación Libre Estática", new Color(0, 173, 239), "ESTATICA")); // Nuevo botón para Estática
            panelOpciones.add(crearBotonModo("Ejercicios Aleatorios", new Color(155, 89, 182), "EJERCICIOS_APRENDE"));
        } else if (modoActual.equals("juego")) {
            // Opciones para Modo Juego
            panelOpciones.add(crearBotonModo("Desafíos de MRU", UIHelper.COLOR_PRIMARIO, "DESAFIO_MRU"));
            panelOpciones.add(crearBotonModo("Desafíos de MRUV", new Color(241, 196, 15), "DESAFIO_MRUV"));
            panelOpciones.add(crearBotonModo("Desafíos de Caída Libre", UIHelper.COLOR_SECUNDARIO, "DESAFIO_CAIDA_LIBRE"));
            panelOpciones.add(crearBotonModo("Desafíos de Tiro Parabólico", UIHelper.COLOR_EXITO, "DESAFIO_TIRO_PARABOLICO"));
            panelOpciones.add(crearBotonModo("Logros y Puntuación", new Color(147, 112, 219), "LOGROS_PUNTUACION"));
        }
        
        panelCentral.add(panelOpciones);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Botón volver
        JButton btnVolver = UIHelper.crearBotonRedondeado("Volver al Menú Principal", 
                                                           new Color(149, 165, 166));
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.setPreferredSize(new Dimension(280, 50));
        btnVolver.addActionListener(e -> frame.mostrarMenuPrincipal());
        
        panelCentral.add(btnVolver);
        
        add(panelCentral, BorderLayout.CENTER);
        
        // Versión en esquina
        JPanel panelVersion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelVersion.setOpaque(false);
        JLabel labelVersion = new JLabel("v3.0 Alpha");
        labelVersion.setFont(new Font("Arial", Font.ITALIC, 12));
        labelVersion.setForeground(new Color(127, 140, 141));
        panelVersion.add(labelVersion);
        
        add(panelVersion, BorderLayout.SOUTH);
    }
    
    private JButton crearBotonModo(String texto, Color color, String accion) {
        JButton boton = UIHelper.crearBotonRedondeado(texto, color);
        boton.setPreferredSize(new Dimension(300, 60));
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.addActionListener(e -> ejecutarAccionModo(accion));
        return boton;
    }
    
    private void ejecutarAccionModo(String accion) {
        JPanel simulacion = null;
        switch (accion) {
            case "MRU":
                simulacion = new SimulacionMRU(frame);
                break;
            case "MRUV":
                simulacion = new SimulacionMRUV(frame);
                break;
            case "CAIDA_LIBRE":
                simulacion = new SimulacionCaidaLibre(frame);
                break;
            case "TIRO_PARABOLICO":
                simulacion = new SimulacionTiroParabolico(frame);
                break;
            case "ESTATICA": // Nuevo caso para Estática
                simulacion = new SimulacionEstatica(frame);
                break;
            case "EJERCICIOS_APRENDE":
                JOptionPane.showMessageDialog(this, "Ejercicios Aleatorios (BETA)", "Modo Aprende", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "DESAFIO_MRU":
            case "DESAFIO_MRUV":
            case "DESAFIO_CAIDA_LIBRE":
            case "DESAFIO_TIRO_PARABOLICO":
                JOptionPane.showMessageDialog(this, "Desafío de " + accion.replace("DESAFIO_", "") + " (BETA)", "Modo Juego", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "LOGROS_PUNTUACION":
                JOptionPane.showMessageDialog(this, "Logros y Puntuación (BETA)", "Modo Juego", JOptionPane.INFORMATION_MESSAGE);
                break;
        }
        
        if (simulacion != null) {
            frame.mostrarSimulacion(simulacion);
        }
    }
}
