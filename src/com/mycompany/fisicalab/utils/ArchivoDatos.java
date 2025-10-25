package com.mycompany.fisicalab.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para guardar y cargar resultados de simulaciones
 */
public class ArchivoDatos {
    
    private static final String ARCHIVO_RESULTADOS = "fisicalab_resultados.dat";
    
    /**
     * Clase interna para almacenar un resultado
     */
    public static class Resultado implements Serializable {
        private static final long serialVersionUID = 1L;
        
        public String tipoSimulacion;
        public String fecha;
        public double parametro1;
        public double parametro2;
        public double resultadoPrincipal;
        public String notas;
        
        public Resultado(String tipo, String fecha, double p1, double p2, double resultado, String notas) {
            this.tipoSimulacion = tipo;
            this.fecha = fecha;
            this.parametro1 = p1;
            this.parametro2 = p2;
            this.resultadoPrincipal = resultado;
            this.notas = notas;
        }
        
        @Override
        public String toString() {
            return String.format("%s | %s | %.2f | %.2f | %.2f | %s", 
                               tipoSimulacion, fecha, parametro1, parametro2, resultadoPrincipal, notas);
        }
    }
    
    /**
     * Guarda un resultado en el archivo
     */
    public static boolean guardarResultado(Resultado resultado) {
        List<Resultado> resultados = cargarResultados();
        resultados.add(resultado);
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_RESULTADOS))) {
            oos.writeObject(resultados);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar resultado: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Carga todos los resultados del archivo
     */
    @SuppressWarnings("unchecked")
    public static List<Resultado> cargarResultados() {
        File archivo = new File(ARCHIVO_RESULTADOS);
        
        if (!archivo.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO_RESULTADOS))) {
            return (List<Resultado>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar resultados: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Elimina todos los resultados
     */
    public static boolean limpiarResultados() {
        File archivo = new File(ARCHIVO_RESULTADOS);
        
        if (archivo.exists()) {
            return archivo.delete();
        }
        
        return true;
    }
    
    /**
     * Obtiene el número de resultados guardados
     */
    public static int contarResultados() {
        return cargarResultados().size();
    }
    
    /**
     * Exporta resultados a formato CSV
     */
    public static boolean exportarCSV(String nombreArchivo) {
        List<Resultado> resultados = cargarResultados();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("Simulacion,Fecha,Parametro1,Parametro2,Resultado,Notas");
            
            for (Resultado r : resultados) {
                writer.printf("%s,%s,%.2f,%.2f,%.2f,%s%n",
                            r.tipoSimulacion, r.fecha, r.parametro1, 
                            r.parametro2, r.resultadoPrincipal, r.notas);
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error al exportar CSV: " + e.getMessage());
            return false;
        }
    }
}