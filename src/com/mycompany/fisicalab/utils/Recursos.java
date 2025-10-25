package com.mycompany.fisicalab.utils;

import javax.swing.ImageIcon;
import java.awt.Image;

/**
 * Gestor de recursos (imágenes y assets)
 * Por ahora es un placeholder - se implementará cuando tengamos recursos
 */
public class Recursos {
    
    private static final String RUTA_IMAGENES = "/imagenes/";
    private static final String RUTA_ICONOS = "/iconos/";
    
    /**
     * Carga una imagen desde los recursos
     * Por ahora retorna null - implementar cuando tengamos imágenes
     */
    public static ImageIcon cargarImagen(String nombre) {
        try {
            // Intentar cargar desde recursos
            String ruta = RUTA_IMAGENES + nombre;
            java.net.URL url = Recursos.class.getResource(ruta);
            
            if (url != null) {
                return new ImageIcon(url);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen: " + nombre);
        }
        
        return null;
    }
    
    /**
     * Carga un icono desde los recursos
     */
    public static ImageIcon cargarIcono(String nombre) {
        try {
            String ruta = RUTA_ICONOS + nombre;
            java.net.URL url = Recursos.class.getResource(ruta);
            
            if (url != null) {
                return new ImageIcon(url);
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono: " + nombre);
        }
        
        return null;
    }
    
    /**
     * Redimensiona una imagen
     */
    public static ImageIcon redimensionarImagen(ImageIcon icono, int ancho, int alto) {
        if (icono == null) return null;
        
        Image img = icono.getImage();
        Image imgRedimensionada = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imgRedimensionada);
    }
    
    /**
     * Verifica si existe un recurso
     */
    public static boolean existeRecurso(String ruta) {
        return Recursos.class.getResource(ruta) != null;
    }
    
    /**
     * Obtiene la ruta base de los recursos
     */
    public static String getRutaBase() {
        return Recursos.class.getResource("/").getPath();
    }
}