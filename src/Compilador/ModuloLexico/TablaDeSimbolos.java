package Compilador.ModuloLexico;

import java.util.*;

public class TablaDeSimbolos {
    private static Map<String, ElementoTablaDeSimbolos> tablaSimbolos = new Hashtable<>();

    public TablaDeSimbolos(){

    }

    public boolean estaSimbolo(String key) {
        return tablaSimbolos.containsKey(key);
    }

    public void addSimbolo(String key, ElementoTablaDeSimbolos elemento) {
        tablaSimbolos.put(key, elemento);
    }
    public static ElementoTablaDeSimbolos getSimbolo(String key) {

        return tablaSimbolos.getOrDefault(key,null);
    }
    public static int getSize() {
        return tablaSimbolos.size();
    }




}
