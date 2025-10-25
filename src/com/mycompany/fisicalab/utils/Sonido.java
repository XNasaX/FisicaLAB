package com.mycompany.fisicalab.utils;

import javax.sound.sampled.*;
import java.io.File;

/**
 * Gestor de efectos de sonido
 * Por ahora es un placeholder - se implementará cuando tengamos archivos de audio
 */
public class Sonido {
    
    private static final String RUTA_SONIDOS = "/sonidos/";
    private static boolean sonidoActivo = true;
    
    /**
     * Reproduce un efecto de sonido
     */
    public static void reproducir(String nombreArchivo) {
        if (!sonidoActivo) return;
        
        try {
            String ruta = RUTA_SONIDOS + nombreArchivo;
            java.net.URL url = Sonido.class.getResource(ruta);
            
            if (url != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                
                // Cerrar recursos cuando termine
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("No se pudo reproducir el sonido: " + nombreArchivo);
        }
    }
    
    /**
     * Reproduce un sonido desde un archivo externo
     */
    public static void reproducirArchivo(String rutaCompleta) {
        if (!sonidoActivo) return;
        
        try {
            File archivo = new File(rutaCompleta);
            
            if (archivo.exists()) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Error al reproducir archivo: " + e.getMessage());
        }
    }
    
    /**
     * Reproduce un sonido en bucle
     */
    public static Clip reproducirEnBucle(String nombreArchivo) {
        if (!sonidoActivo) return null;
        
        try {
            String ruta = RUTA_SONIDOS + nombreArchivo;
            java.net.URL url = Sonido.class.getResource(ruta);
            
            if (url != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                return clip;
            }
        } catch (Exception e) {
            System.err.println("No se pudo reproducir en bucle: " + nombreArchivo);
        }
        
        return null;
    }
    
    /**
     * Detiene un clip de audio
     */
    public static void detener(Clip clip) {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
    
    /**
     * Activa o desactiva el sonido
     */
    public static void setSonidoActivo(boolean activo) {
        sonidoActivo = activo;
    }
    
    /**
     * Verifica si el sonido está activo
     */
    public static boolean isSonidoActivo() {
        return sonidoActivo;
    }
    
    /**
     * Genera un beep del sistema
     */
    public static void beep() {
        if (sonidoActivo) {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }
}