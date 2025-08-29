import java.util.HashMap;
import java.util.Map;

public class MatrizDeTransicion {

    public static final int ERROR = -1;
    public static final int FINAL = -2;   // Estados (filas) x columnas (simbolos)
    private final Map<Character, Integer> SYMBOL_MAP =  new HashMap<>();
    public MatrizDeTransicion() {
        for (char c = 'a'; c <= 'z'; c++) {
            SYMBOL_MAP.put(c, 1);
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            SYMBOL_MAP.put(c, 2);
        }
        for (int n = 0; n <=9; n++){
            SYMBOL_MAP.put((char)n, 0);
        }
        SYMBOL_MAP.put('_', 3);
        SYMBOL_MAP.put('$', 19);
        SYMBOL_MAP.put('+', 7);
        SYMBOL_MAP.put('-', 8);
        SYMBOL_MAP.put('.', 9);
        SYMBOL_MAP.put('{', 10);
        SYMBOL_MAP.put('=', 11);
        SYMBOL_MAP.put('<', 12);
        SYMBOL_MAP.put('>', 13);
        SYMBOL_MAP.put('#', 14);
        SYMBOL_MAP.put(' ', 15);
        SYMBOL_MAP.put('\n', 16);
        SYMBOL_MAP.put('!', 17);
        SYMBOL_MAP.put('"', 18);
        SYMBOL_MAP.put('%', 6);
        SYMBOL_MAP.put('M', 5);
        SYMBOL_MAP.put('D', 4);
    }
    public static final int[][] TABLE = {
            // d,  l,  U,  L,  D,  M,  %,  +,  -,  .,  {,  =,  <,  >,  #,  bl, nl,  !,  ",  $
            {2, 8, 1, 1, 1, 1, -1, -2, 4, 9, -2, 5, 6, 6, 13, 0, 0, -1, 7, -2},
            {1, -2, -2, -2, -1, 1, 1, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {2, -1, 3, -1, -1, -1, -1, -1, -1, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2},
            {-1, -1, -1, -2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2},
            {-2, -2, -2, -2, -1, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -1, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-2, -2, -2, -2, -1, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, -1, 7, -2, -2},
            {-2, 8, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2},
            {10, -2, -2, -2, 11, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-1, -1, -1, -1, -1, -1, -1, 12, 12, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2},
            {12, -2, -2, -2, -1, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2},
            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 14, -1, -1, -1, -2},
            {14, 14, 14, 14, -1, 14, 14, 14, 14, 14, 14, 14, 14, 14, 15, 14, 14, 14, 14, -2},
            {14, 14, 14, 14, -1, 14, 14, 14, 14, 14, 14, 14, 14, 14, 0, 14, 14, 14, 14, -1}
    };

    public int getEstado( int estado,char simbolo ) {
        return TABLE[estado][getColumna(simbolo)];
    }
    private int getColumna(char simbolo) {
        return SYMBOL_MAP.get(simbolo);
    }



}