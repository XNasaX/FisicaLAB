package com.mycompany.fisicalab.modos;

import com.mycompany.fisicalab.core.SimuladorFrame;
import com.mycompany.fisicalab.juego.*;
import com.mycompany.fisicalab.utils.UIHelper;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Modo Juego - Sistema de misiones y desafíos
 * Versión 3.0
 */
public class ModoJuego extends JPanel {
    
    private SimuladorFrame frame;
    private List<Mision> todasLasMisiones;
    private int puntosTotales;
    private int misionesCompletadas;
    
    public ModoJuego(SimuladorFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(UIHelper.COLOR_FONDO);
        
        cargarMisiones();
        inicializarComponentes();
    }
    
    private void cargarMisiones() {
        todasLasMisiones = new ArrayList<>();
        
        // Cargar misiones de MRU
        todasLasMisiones.addAll(MisionMRU.crearMisionesIniciales());
        
        // Cargar misiones de Caída Libre
        todasLasMisiones.addAll(MisionCaidaLibre.crearMisionesIniciales());
        
        // Cargar misiones de Tiro Parabólico
        todasLasMisiones.addAll(MisionTiroParabolico.crearMisionesIniciales());
        
        // Calcular estadísticas
        calcularEstadisticas();
    }
    
    private void calcularEstadisticas() {
        puntosTotales = 0;
        misionesCompletadas = 0;
        
        for (Mision m : todasLasMisiones) {
            if (m.isCompletada()) {
                misionesCompletadas++;
                puntosTotales += m.getMejorPuntuacion();
            }
        }
    }
    
    private void inicializarComponentes() {
        // Panel superior con título y stats
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        
        JLabel titulo = new JLabel("🎮 MODO JUEGO - Misiones y Desafíos");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(UIHelper.COLOR_EXITO);
        
        // Panel de estadísticas
        JPanel panelStats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelStats.setOpaque(false);
        
        JLabel lblMisiones = new JLabel(String.format("📊 Misiones: %d/%d", 
                                        misionesCompletadas, todasLasMisiones.size()));
        lblMisiones.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel lblPuntos = new JLabel(String.format("⚡ Puntos: %d XP", puntosTotales));
        lblPuntos.setFont(new Font("Arial", Font.BOLD, 14));
        lblPuntos.setForeground(UIHelper.COLOR_ADVERTENCIA);
        
        panelStats.add(lblMisiones);
        panelStats.add(lblPuntos);
        
        panelSuperior.add(titulo, BorderLayout.WEST);
        panelSuperior.add(panelStats, BorderLayout.EAST);
        
        // Panel central con las misiones organizadas por simulación
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Agrupar misiones por tipo
        List<Mision> misionesMRU = new ArrayList<>();
        List<Mision> misionesCaida = new ArrayList<>();
        List<Mision> misionesTiro = new ArrayList<>();
        
        for (Mision m : todasLasMisiones) {
            switch (m.getTipoSimulacion()) {
                case "MRU":
                    misionesMRU.add(m);
                    break;
                case "CAIDA_LIBRE":
                    misionesCaida.add(m);
                    break;
                case "TIRO_PARABOLICO":
                    misionesTiro.add(m);
                    break;
            }
        }
        
        // Crear secciones para cada tipo
        panelCentral.add(crearSeccionMisiones("? Movimiento Rectilíneo Uniforme", misionesMRU, UIHelper.COLOR_PRIMARIO));
        panelCentral.add(Box.createRigidArea(new Dimension(0, 15)));
        panelCentral.add(crearSeccionMisiones("🪂 Caída Libre", misionesCaida, UIHelper.COLOR_SECUNDARIO));
        panelCentral.add(Box.createRigidArea(new Dimension(0, 15)));
        panelCentral.add(crearSeccionMisiones("🎯 Tiro Parabólico", misionesTiro, UIHelper.COLOR_EXITO));
        
        JScrollPane scrollPane = new JScrollPane(panelCentral);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Panel inferior con botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelInferior.setOpaque(false);
        
        JButton btnVolver = UIHelper.crearBotonRedondeado("← Volver", UIHelper.COLOR_PELIGRO);
        btnVolver.setPreferredSize(new Dimension(140, 45));
        btnVolver.addActionListener(e -> {
            SeleccionModo seleccion = new SeleccionModo(frame);
            frame.mostrarSimulacion(seleccion);
        });
        
        JButton btnEstadisticas = UIHelper.crearBotonRedondeado("📊 Estadísticas", new Color(149, 165, 166));
        btnEstadisticas.setPreferredSize(new Dimension(160, 45));
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());
        
        panelInferior.add(btnVolver);
        panelInferior.add(btnEstadisticas);
        
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearSeccionMisiones(String titulo, List<Mision> misiones, Color colorAccento) {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setOpaque(false);
        
        // Título de sección
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(colorAccento);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        seccion.add(lblTitulo);
        seccion.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Tarjetas de misiones
        for (Mision mision : misiones) {
            JPanel tarjeta = crearTarjetaMision(mision, colorAccento);
            tarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
            seccion.add(tarjeta);
            seccion.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        return seccion;
    }
    
    private JPanel crearTarjetaMision(Mision mision, Color colorAccento) {
        JPanel tarjeta = new JPanel(new BorderLayout(15, 10));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(mision.isDesbloqueada() ? colorAccento : Color.GRAY, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        tarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Panel izquierdo (info)
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setOpaque(false);
        
        JLabel lblNombre = new JLabel(mision.getNombre());
        lblNombre.setFont(new Font("Arial", Font.BOLD, 16));
        lblNombre.setForeground(mision.isDesbloqueada() ? Color.BLACK : Color.GRAY);
        
        JLabel lblDescripcion = new JLabel("<html>" + mision.getDescripcion() + "</html>");
        lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDescripcion.setForeground(new Color(127, 140, 141));
        
        JLabel lblDificultad = new JLabel(mision.getIconoDificultad() + " | " + 
                                         mision.getPuntosBase() + " XP base");
        lblDificultad.setFont(new Font("Arial", Font.ITALIC, 11));
        lblDificultad.setForeground(new Color(149, 165, 166));
        
        panelInfo.add(lblNombre);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblDescripcion);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblDificultad);
        
        // Panel derecho (estado y botón)
        JPanel panelDerecho = new JPanel();
        panelDerecho.setLayout(new BoxLayout(panelDerecho, BoxLayout.Y_AXIS));
        panelDerecho.setOpaque(false);
        
        if (mision.isCompletada()) {
            JLabel lblEstrellas = new JLabel(mision.getEstrellasString());
            lblEstrellas.setFont(new Font("Arial", Font.PLAIN, 24));
            lblEstrellas.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel lblPuntos = new JLabel(mision.getMejorPuntuacion() + " XP");
            lblPuntos.setFont(new Font("Arial", Font.BOLD, 14));
            lblPuntos.setForeground(UIHelper.COLOR_EXITO);
            lblPuntos.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            panelDerecho.add(lblEstrellas);
            panelDerecho.add(lblPuntos);
            panelDerecho.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        JButton btnJugar;
        if (!mision.isDesbloqueada()) {
            btnJugar = UIHelper.crearBotonRedondeado("🔒 Bloqueada", new Color(149, 165, 166));
            btnJugar.setEnabled(false);
        } else if (mision.isCompletada()) {
            btnJugar = UIHelper.crearBotonRedondeado("▶ Reintentar", colorAccento);
        } else {
            btnJugar = UIHelper.crearBotonRedondeado("▶ Jugar", colorAccento);
        }
        
        btnJugar.setPreferredSize(new Dimension(140, 40));
        btnJugar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnJugar.addActionListener(e -> iniciarMision(mision));
        
        panelDerecho.add(btnJugar);
        
        tarjeta.add(panelInfo, BorderLayout.CENTER);
        tarjeta.add(panelDerecho, BorderLayout.EAST);
        
        return tarjeta;
    }
    
    private void iniciarMision(Mision mision) {
        if (!mision.isDesbloqueada()) {
            UIHelper.mostrarInfo(this, "Esta misión aún no está desbloqueada.");
            return;
        }
        
        // Aquí iríamos a la simulación específica con la misión activa
        // Por ahora mostraremos un diálogo con la info de la misión
        PanelMision panelMision = new PanelMision(frame, mision, this);
        frame.mostrarSimulacion(panelMision);
    }
    
    private void mostrarEstadisticas() {
        StringBuilder stats = new StringBuilder();
        stats.append("📊 ESTADÍSTICAS GENERALES\n\n");
        stats.append(String.format("Misiones completadas: %d/%d\n", misionesCompletadas, todasLasMisiones.size()));
        stats.append(String.format("Puntos totales: %d XP\n\n", puntosTotales));
        
        int estrellasTotales = 0;
        for (Mision m : todasLasMisiones) {
            estrellasTotales += m.getEstrellasObtenidas();
        }
        
        stats.append(String.format("Estrellas obtenidas: %d/%d\n", estrellasTotales, todasLasMisiones.size() * 3));
        stats.append(String.format("Progreso: %.1f%%", (misionesCompletadas * 100.0) / todasLasMisiones.size()));
        
        UIHelper.mostrarInfo(this, stats.toString());
    }
    
    public void actualizarDespuesDeMision() {
        // Recargar misiones y actualizar UI
        calcularEstadisticas();
        removeAll();
        inicializarComponentes();
        revalidate();
        repaint();
    }
}