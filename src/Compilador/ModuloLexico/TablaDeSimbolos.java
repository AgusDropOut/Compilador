package Compilador.ModuloLexico;

import java.util.*;

public class TablaDeSimbolos {
    private Map<String, List<String>> tablaSimbolos = new Hashtable<>();

    public TablaDeSimbolos(){

    }

    public boolean estaSimbolo(String key) {
        return tablaSimbolos.containsKey(key);
    }

    public void addSimbolo(String key){
        List<String> lista = new ArrayList<>();
        tablaSimbolos.put(key, lista);
    }
}
