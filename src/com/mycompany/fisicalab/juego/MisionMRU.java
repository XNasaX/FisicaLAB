package com.mycompany.fisicalab.juego;

import java.util.ArrayList;
import java.util.List;

/**
 * Misiones específicas para Movimiento Rectilíneo Uniforme
 * Versión 3.0
 */
public class MisionMRU extends Mision {
    
    private TipoMisionMRU tipoMision;
    
    public enum TipoMisionMRU {
        DISTANCIA_SIMPLE,      // Alcanzar distancia
        TIEMPO_EXACTO,         // Tiempo específico
        VELOCIDAD_ESPECIFICA,  // Velocidad exacta
        PRECISION_ALTA,        // Alta precisión
        CONTRA_RELOJ          // Límite de tiempo
    }
    
    public MisionMRU(String id, String nombre, String descripcion, int dificultad,
                     TipoMisionMRU tipo, double objetivo, double tolerancia) {
        super(id, nombre, descripcion, "MRU", dificultad);
        this.tipoMision = tipo;
        this.valorObjetivo = objetivo;
        this.tolerancia = tolerancia;
        configurarMision();
    }
    
    private void configurarMision() {
        switch (tipoMision) {
            case DISTANCIA_SIMPLE:
                this.objetivo = "Alcanzar exactamente " + valorObjetivo + " metros";
                this.restricciones = new String[]{
                    "• Usa cualquier velocidad",
                    "• Detente lo más cerca posible del objetivo"
                };
                break;
                
            case TIEMPO_EXACTO:
                this.objetivo = "Completar en exactamente " + valorObjetivo + " segundos";
                this.restricciones = new String[]{
                    "• Ajusta la velocidad correctamente",
                    "• Observa el cronómetro"
                };
                break;
                
            case VELOCIDAD_ESPECIFICA:
                this.objetivo = "Usar velocidad de " + valorObjetivo + " m/s";
                this.restricciones = new String[]{
                    "• Velocidad exacta requerida",
                    "• Alcanza la meta con esa velocidad"
                };
                break;
                
            case PRECISION_ALTA:
                this.objetivo = "Precisión extrema: ±" + tolerancia + "m";
                this.restricciones = new String[]{
                    "• Margen de error muy pequeño",
                    "• Requiere cálculo preciso"
                };
                break;
                
            case CONTRA_RELOJ:
                this.objetivo = "Completar en menos de " + valorObjetivo + " segundos";
                this.restricciones = new String[]{
                    "• Velocidad mínima requerida",
                    "• Contra el tiempo"
                };
                break;
        }
    }
    
    @Override
    public boolean evaluar(double resultado) {
        double diferencia = Math.abs(resultado - valorObjetivo);
        
        switch (tipoMision) {
            case DISTANCIA_SIMPLE:
            case TIEMPO_EXACTO:
            case VELOCIDAD_ESPECIFICA:
            case PRECISION_ALTA:
                return diferencia <= tolerancia;
                
            case CONTRA_RELOJ:
                return resultado <= valorObjetivo;
                
            default:
                return false;
        }
    }
    
    /**
     * Crea las 5 misiones de MRU para v3.0
     */
    public static List<MisionMRU> crearMisionesIniciales() {
        List<MisionMRU> misiones = new ArrayList<>();
        
        // Misión 1: Primer Paso (Fácil)
        MisionMRU m1 = new MisionMRU(
            "MRU_01",
            "Primer Paso",
            "Tu primera misión: mueve el objeto exactamente 10 metros. ¡Usa cualquier velocidad!",
            1, // Dificultad: Fácil
            TipoMisionMRU.DISTANCIA_SIMPLE,
            10.0, // Objetivo: 10m
            2.0   // Tolerancia: ±2m
        );
        m1.setDesbloqueada(true); // Primera misión siempre desbloqueada
        misiones.add(m1);
        
        // Misión 2: Velocidad Exacta (Normal)
        MisionMRU m2 = new MisionMRU(
            "MRU_02",
            "Velocidad Exacta",
            "Alcanza los 20 metros usando exactamente 5 m/s. ¡Calcula bien!",
            2, // Dificultad: Normal
            TipoMisionMRU.VELOCIDAD_ESPECIFICA,
            5.0,  // Objetivo: 5 m/s
            0.5   // Tolerancia: ±0.5 m/s
        );
        misiones.add(m2);
        
        // Misión 3: Contra el Reloj (Normal)
        MisionMRU m3 = new MisionMRU(
            "MRU_03",
            "Contra el Reloj",
            "Recorre 50 metros en menos de 8 segundos. ¡Velocidad es clave!",
            2, // Dificultad: Normal
            TipoMisionMRU.CONTRA_RELOJ,
            8.0,  // Objetivo: < 8 segundos
            0.0   // Sin tolerancia (debe ser menor)
        );
        misiones.add(m3);
        
        // Misión 4: Precisión Perfecta (Difícil)
        MisionMRU m4 = new MisionMRU(
            "MRU_04",
            "Precisión Perfecta",
            "Detente exactamente en 47.5 metros. Margen: solo ±0.5m. ¡Máxima precisión!",
            4, // Dificultad: Difícil
            TipoMisionMRU.PRECISION_ALTA,
            47.5, // Objetivo: 47.5m
            0.5   // Tolerancia: ±0.5m
        );
        misiones.add(m4);
        
        // Misión 5: Maratón (Muy Difícil)
        MisionMRU m5 = new MisionMRU(
            "MRU_05",
            "Maratón",
            "Recorre 200 metros en exactamente 20 segundos. ¡Cálculo perfecto necesario!",
            5, // Dificultad: Muy Difícil
            TipoMisionMRU.TIEMPO_EXACTO,
            20.0, // Objetivo: 20s
            1.0   // Tolerancia: ±1s
        );
        misiones.add(m5);
        
        return misiones;
    }
    
    public TipoMisionMRU getTipoMision() {
        return tipoMision;
    }
}