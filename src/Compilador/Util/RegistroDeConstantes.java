package Compilador.Util;

import Compilador.ModuloLexico.TablaDeSimbolos;

import java.util.HashMap;
import java.util.Map;

public class RegistroDeConstantes {

    // Map que guarda la constante como clave y la cantidad de referencias activas
    private static Map<String, Integer> registro = new HashMap<>();

    /**
     * Registra una referencia a una constante.
     * Si no existía, la agrega con contador = 1.
     * Si ya existía, incrementa su contador.
     */
    public static void registrarConstante(String constante) {
        registro.put(constante, registro.getOrDefault(constante, 0) + 1);
    }

    /**
     * Desregistra una referencia a una constante.
     * Si el contador llega a 0, la elimina completamente.
     */
    public static void desregistrarConstante(String constante) {
        if (registro.containsKey(constante)) {
            int count = registro.get(constante) - 1;
            if (count <= 0) {
                registro.remove(constante);
                // 👇 también podrías eliminarla de la TablaDeSimbolos acá si querés
                TablaDeSimbolos.eliminarSimbolo(constante);
                System.out.println("Constante eliminada del registro: " + constante);
            } else {
                registro.put(constante, count);
            }
        }
    }

    /**
     * Consulta cuántas referencias activas tiene una constante.
     */
    public static int obtenerReferencias(String constante) {
        return registro.getOrDefault(constante, 0);
    }

    /**
     * Muestra el estado actual del registro (opcional para depuración).
     */
    public static void mostrarRegistro() {
        System.out.println("---- Registro de Constantes ----");
        for (Map.Entry<String, Integer> entry : registro.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }
        System.out.println("--------------------------------");
    }
}