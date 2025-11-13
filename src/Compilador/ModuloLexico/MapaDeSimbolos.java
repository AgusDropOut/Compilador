package Compilador.ModuloLexico;

import java.util.HashMap;
import java.util.Map;

public class MapaDeSimbolos {
    private final Map<Character, Integer> SYMBOL_MAP =  new HashMap<>();
    public MapaDeSimbolos() {
        for (char c = 'a'; c <= 'z'; c++) {
            SYMBOL_MAP.put(c, 1);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            SYMBOL_MAP.put(c, 5);
        }
        SYMBOL_MAP.put('U', 2);
        SYMBOL_MAP.put('L', 3);
        SYMBOL_MAP.put('D', 4);
        for (int n = 0; n <= 9; n++) {
            SYMBOL_MAP.put((char) ('0' + n), 0);
        }
        SYMBOL_MAP.put(':', 19);
        SYMBOL_MAP.put('$', 20);
        SYMBOL_MAP.put('+', 7);
        SYMBOL_MAP.put('-', 8);
        SYMBOL_MAP.put('.', 9);
        SYMBOL_MAP.put('=', 11);
        SYMBOL_MAP.put('<', 12);
        SYMBOL_MAP.put('>', 13);
        SYMBOL_MAP.put('#', 14);
        SYMBOL_MAP.put(' ', 15);
        SYMBOL_MAP.put('\t', 15);
        SYMBOL_MAP.put('\n', 16);
        SYMBOL_MAP.put('\r', 16);

        SYMBOL_MAP.put('!', 17);
        SYMBOL_MAP.put('"', 18);
        SYMBOL_MAP.put('%', 6);


        SYMBOL_MAP.put('_', 10);
        SYMBOL_MAP.put('{', 10);
        SYMBOL_MAP.put('}', 10);
        SYMBOL_MAP.put(';', 10);
        SYMBOL_MAP.put('*', 10);
        SYMBOL_MAP.put('/', 10);
        SYMBOL_MAP.put('(', 10);
        SYMBOL_MAP.put(')', 10);
        SYMBOL_MAP.put(',', 10);
    }

    public int get(char c) {
        if(SYMBOL_MAP.containsKey(c)) {
            return SYMBOL_MAP.get(c);
        } else {
            //Error de compilacion, consideramos devolver columna correspondiente a caracter invalido
            return 21;
        }
    }
}
