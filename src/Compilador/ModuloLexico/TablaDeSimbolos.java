package Compilador.ModuloLexico;

import java.util.*;

public class TablaDeSimbolos {
    private static Map<String, ElementoTablaDeSimbolos> tablaSimbolos = new Hashtable<>();

    public TablaDeSimbolos(){

    }

    public static boolean estaSimbolo(String key) {
        return tablaSimbolos.containsKey(key);
    }

    public static void addSimbolo(String key, ElementoTablaDeSimbolos elemento) {
        tablaSimbolos.put(key, elemento);
    }
    public static ElementoTablaDeSimbolos getSimbolo(String key) {

        return tablaSimbolos.getOrDefault(key,null);
    }
    public static int getSize() {
        return tablaSimbolos.size();
    }
    public static void imprimirTabla() {
        System.out.println(" ");
        System.out.println("Tabla de simbolos: ");
        for (Map.Entry<String, ElementoTablaDeSimbolos> entrada : tablaSimbolos.entrySet()) {
            System.out.println("Lexema: " + entrada.getKey() + " | " + entrada.getValue().toString());
        }
    }




}
