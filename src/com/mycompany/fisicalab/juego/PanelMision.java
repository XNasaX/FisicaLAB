package com.mycompany.fisicalab.juego;

import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.modos.ModoJuego;
import com.mycompany.fisicalab.ui.*;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;

/**
 * Panel que muestra la información de una misión y lanza la simulación correspondiente
 * Versión 3.0
 */
public class PanelMision extends JPanel {
    
    private SimuladorFrame frame;
    private Mision mision;
    private ModoJuego modoJuego;
    
    public PanelMision(SimuladorFrame frame, Mision mision, ModoJuego modoJuego) {
        this.frame = frame;
        this.mision = mision;
        this.modoJuego = modoJuego;
        
        setLayout(new BorderLayout());
        setBackground(UIHelper.COLOR_FONDO);
        
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        // Panel central con información de la misión
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));
        
        // Título de la misión
        JLabel lblNombre = new JLabel(mision.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 36));
        lblNombre.setForeground(UIHelper.COLOR_PRIMARIO);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTipo = new JLabel("📋 " + mision.getTipoSimulacion().replace("_", " "));
        lblTipo.setFont(new Font("Arial", Font.ITALIC, 16));
        lblTipo.setForeground(new Color(127, 140, 141));
        lblTipo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelCentral.add(lblNombre);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));
        panelCentral.add(lblTipo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Panel de información con fondo
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(Color.WHITE);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIHelper.COLOR_PRIMARIO, 2),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        panelInfo.setMaximumSize(new Dimension(700, 400));
        
        // Descripción
        JLabel lblDescTitulo = new JLabel("📝 DESCRIPCIÓN:");
        lblDescTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblDescTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea txtDesc = new JTextArea(mision.getDescripcion());
        txtDesc.setFont(new Font("Arial", Font.PLAIN, 13));
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);
        txtDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(lblDescTitulo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 8)));
        panelInfo.add(txtDesc);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Objetivo
        JLabel lblObjTitulo = new JLabel("🎯 OBJETIVO:");
        lblObjTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblObjTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblObj = new JLabel(mision.getObjetivo());
        lblObj.setFont(new Font("Arial", Font.PLAIN, 13));
        lblObj.setForeground(UIHelper.COLOR_EXITO);
        lblObj.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(lblObjTitulo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblObj);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Restricciones
        JLabel lblRestTitulo = new JLabel("⚠️ RESTRICCIONES:");
        lblRestTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblRestTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(lblRestTitulo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        
        for (String restriccion : mision.getRestricciones()) {
            JLabel lblRest = new JLabel(restriccion);
            lblRest.setFont(new Font("Arial", Font.PLAIN, 12));
            lblRest.setAlignmentX(Component.LEFT_ALIGNMENT);
            panelInfo.add(lblRest);
        }
        
        panelInfo.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Información de dificultad y puntos
        JPanel panelEstadisticas = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        panelEstadisticas.setOpaque(false);
        
        JLabel lblDif = new JLabel(mision.getIconoDificultad());
        lblDif.setFont(new Font("Arial", Font.BOLD, 13));
        
        JLabel lblPuntos = new JLabel("💰 " + mision.getPuntosBase() + " XP base");
        lblPuntos.setFont(new Font("Arial", Font.BOLD, 13));
        lblPuntos.setForeground(UIHelper.COLOR_ADVERTENCIA);
        
        JLabel lblEstrellas = new JLabel("⭐ Máximo: 3 estrellas");
        lblEstrellas.setFont(new Font("Arial", Font.BOLD, 13));
        
        panelEstadisticas.add(lblDif);
        panelEstadisticas.add(lblPuntos);
        panelEstadisticas.add(lblEstrellas);
        
        panelInfo.add(panelEstadisticas);
        
        panelCentral.add(panelInfo);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setOpaque(false);
        
        JButton btnCancelar = UIHelper.crearBotonRedondeado("← Cancelar", new Color(149, 165, 166));
        btnCancelar.setPreferredSize(new Dimension(160, 50));
        btnCancelar.addActionListener(e -> volverAModoJuego());
        
        JButton btnComenzar = UIHelper.crearBotonRedondeado("▶ Comenzar Misión", UIHelper.COLOR_EXITO);
        btnComenzar.setPreferredSize(new Dimension(200, 50));
        btnComenzar.addActionListener(e -> iniciarSimulacion());
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnComenzar);
        
        panelCentral.add(panelBotones);
        
        add(panelCentral, BorderLayout.CENTER);
    }
    
    private void volverAModoJuego() {
        frame.mostrarSimulacion(modoJuego);
    }
    
    private void iniciarSimulacion() {
        // Aquí abrimos la simulación correspondiente
        // Por ahora solo abrimos la simulación normal
        JPanel simulacion = null;
        
        switch (mision.getTipoSimulacion()) {
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
            
            // TODO: En versión completa, aquí se debería:
            // 1. Pasar la misión a la simulación
            // 2. La simulación evaluaría el resultado
            // 3. Al terminar, mostraría el resultado y volvería a ModoJuego
            
            // Mostrar diálogo informativo temporal
            SwingUtilities.invokeLater(() -> {
                UIHelper.mostrarInfo(simulacion, 
                    "MISIÓN ACTIVA: " + mision.getNombre() + "\n\n" +
                    "Por ahora, usa la simulación normal.\n" +
                    "En la versión completa, la misión se evaluará automáticamente.\n\n" +
                    "Objetivo: " + mision.getObjetivo());
            });
        }
    }
}