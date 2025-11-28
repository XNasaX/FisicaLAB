package com.mycompany.fisicalab.utils;

import java.awt.Image;
import java.awt.MediaTracker;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 * Gestor de recursos (imágenes y assets)
 * Permite cargar imágenes desde recursos internos o desde URLs externas.
 */
public class Recursos {
    
    private static final String RUTA_IMAGENES = "/imagenes/";
    private static final String RUTA_ICONOS = "/iconos/";
    
    // Cache para imágenes cargadas desde URL
    private static final Map<String, ImageIcon> cacheImagenesURL = new HashMap<>();
    
    /**
     * Carga una imagen desde los recursos internos del proyecto.
     */
    public static ImageIcon cargarImagen(String nombre) {
        try {
            String ruta = RUTA_IMAGENES + nombre;
            URL url = Recursos.class.getResource(ruta);
            
            if (url != null) {
                return new ImageIcon(url);
            } else {
                System.err.println("Recurso interno no encontrado: " + ruta);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen interna: " + nombre + " - " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Carga una imagen desde una URL externa.
     * Las imágenes cargadas se cachean para evitar recargas innecesarias.
     */
    public static ImageIcon cargarImagenDesdeURL(String urlString) {
        if (cacheImagenesURL.containsKey(urlString)) {
            return cacheImagenesURL.get(urlString);
        }
        
        try {
            URL url = new URL(urlString);
            ImageIcon imageIcon = new ImageIcon(url);
            
            // Verificar si la imagen se cargó correctamente
            if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                cacheImagenesURL.put(urlString, imageIcon);
                return imageIcon;
            } else {
                System.err.println("No se pudo cargar la imagen desde la URL (estado incompleto): " + urlString);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen desde URL: " + urlString + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Carga un icono desde los recursos internos del proyecto.
     */
    public static ImageIcon cargarIcono(String nombre) {
        try {
            String ruta = RUTA_ICONOS + nombre;
            URL url = Recursos.class.getResource(ruta);
            
            if (url != null) {
                return new ImageIcon(url);
            } else {
                System.err.println("Recurso interno de icono no encontrado: " + ruta);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el icono interno: " + nombre + " - " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Redimensiona una imagen a las dimensiones especificadas.
     */
    public static ImageIcon redimensionarImagen(ImageIcon icono, int ancho, int alto) {
        if (icono == null || icono.getImage() == null) return null;
        
        Image img = icono.getImage();
        Image imgRedimensionada = img.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imgRedimensionada);
    }
    
    /**
     * Verifica si existe un recurso interno.
     */
    public static boolean existeRecurso(String ruta) {
        return Recursos.class.getResource(ruta) != null;
    }
    
    /**
     * Obtiene la ruta base de los recursos internos.
     */
    public static String getRutaBase() {
        URL url = Recursos.class.getResource("/");
        return (url != null) ? url.getPath() : null;
    }
}
