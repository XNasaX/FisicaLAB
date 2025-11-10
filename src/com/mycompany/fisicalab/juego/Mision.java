package com.mycompany.fisicalab.juego;

import java.io.Serializable;

/**
 * Clase base abstracta para todas las misiones del juego
 * Versión 3.0
 */
public abstract class Mision implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // Identificación
    protected String id;
    protected String nombre;
    protected String descripcion;
    protected String tipoSimulacion; // "MRU", "CAIDA_LIBRE", "TIRO_PARABOLICO"
    
    // Dificultad y puntuación
    protected int dificultad; // 1-5 estrellas máximas posibles
    protected int puntosBase; // Puntos al completar con 1 estrella
    
    // Estado
    protected boolean desbloqueada;
    protected boolean completada;
    protected int estrellasObtenidas; // 0-3
    protected int mejorPuntuacion;
    
    // Restricciones y objetivos
    protected String objetivo; // Descripción del objetivo
    protected String[] restricciones; // Lista de restricciones
    protected double valorObjetivo; // Valor a alcanzar
    protected double tolerancia; // Margen de error permitido
    
    // Constructor
    public Mision(String id, String nombre, String descripcion, String tipo, int dificultad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoSimulacion = tipo;
        this.dificultad = dificultad;
        this.puntosBase = dificultad * 100;
        this.desbloqueada = false;
        this.completada = false;
        this.estrellasObtenidas = 0;
        this.mejorPuntuacion = 0;
    }
    
    /**
     * Evalúa si la misión fue completada
     * @param resultado El resultado obtenido de la simulación
     * @return true si se completó el objetivo básico
     */
    public abstract boolean evaluar(double resultado);
    
    /**
     * Calcula cuántas estrellas se obtienen según la precisión
     * @param precision Precisión del resultado (0.0 a 1.0)
     * @return Número de estrellas obtenidas (1-3)
     */
    public int calcularEstrellas(double precision) {
        if (precision >= 0.95) {
            return 3; // Perfecto
        } else if (precision >= 0.80) {
            return 2; // Bien
        } else if (precision >= 0.70) {
            return 1; // Completado
        } else {
            return 0; // Fallido
        }
    }
    
    /**
     * Calcula los puntos obtenidos según las estrellas
     * @param estrellas Estrellas obtenidas (1-3)
     * @return Puntos totales
     */
    public int calcularPuntos(int estrellas) {
        switch (estrellas) {
            case 3:
                return (int)(puntosBase * 2.0) + 50; // Bonus perfecto
            case 2:
                return (int)(puntosBase * 1.5);
            case 1:
                return puntosBase;
            default:
                return 0;
        }
    }
    
    /**
     * Calcula la precisión del resultado
     * @param resultado Resultado obtenido
     * @return Precisión (0.0 a 1.0)
     */
    protected double calcularPrecision(double resultado) {
        double diferencia = Math.abs(resultado - valorObjetivo);
        double precision = 1.0 - (diferencia / (tolerancia > 0 ? tolerancia : valorObjetivo));
        return Math.max(0.0, Math.min(1.0, precision));
    }
    
    /**
     * Registra el resultado de un intento
     */
    public void registrarResultado(double resultado) {
        boolean exito = evaluar(resultado);
        
        if (exito) {
            double precision = calcularPrecision(resultado);
            int estrellas = calcularEstrellas(precision);
            int puntos = calcularPuntos(estrellas);
            
            if (!completada) {
                completada = true;
                estrellasObtenidas = estrellas;
                mejorPuntuacion = puntos;
            } else {
                // Actualizar si es mejor que antes
                if (estrellas > estrellasObtenidas) {
                    estrellasObtenidas = estrellas;
                }
                if (puntos > mejorPuntuacion) {
                    mejorPuntuacion = puntos;
                }
            }
        }
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getTipoSimulacion() { return tipoSimulacion; }
    public int getDificultad() { return dificultad; }
    public int getPuntosBase() { return puntosBase; }
    public boolean isDesbloqueada() { return desbloqueada; }
    public void setDesbloqueada(boolean desbloqueada) { this.desbloqueada = desbloqueada; }
    public boolean isCompletada() { return completada; }
    public int getEstrellasObtenidas() { return estrellasObtenidas; }
    public int getMejorPuntuacion() { return mejorPuntuacion; }
    public String getObjetivo() { return objetivo; }
    public String[] getRestricciones() { return restricciones; }
    
    /**
     * Retorna un String con las estrellas visuales
     */
    public String getEstrellasString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (i < estrellasObtenidas) {
                sb.append("⭐");
            } else {
                sb.append("☆");
            }
        }
        return sb.toString();
    }
    
    /**
     * Retorna el icono según dificultad
     */
    public String getIconoDificultad() {
        switch (dificultad) {
            case 1: return "😊 Fácil";
            case 2: return "🙂 Normal";
            case 3: return "😐 Medio";
            case 4: return "😰 Difícil";
            case 5: return "😱 Muy Difícil";
            default: return "❓";
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s [%s] - %s %s", 
                           nombre, 
                           tipoSimulacion, 
                           completada ? "✓" : "✗",
                           getEstrellasString());
    }
}
