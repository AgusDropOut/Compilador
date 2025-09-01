public class TablaAccionesSemanticas {
    private final MapaDeSimbolos SYMBOL_MAP;

    public static final AccionSemantica[][] TABLE = new AccionSemantica[16][20];

    static {
        // Inicializar todo como null
        for (int i = 0; i < TABLE.length; i++) {
            for (int j = 0; j < TABLE[i].length; j++) {
                TABLE[i][j] = null;
            }
        }

        // Columna 2 (índice 1) → letra minúscula
        TABLE[0][1] = new AccionSemantica1();
        TABLE[8][1] = new AccionSemantica2();
        TABLE[0][15] = new AccionSemantica13();

        // Fila 8 → resto de columnas → AccionSemantica3, excepto columna 2
        for (int j = 0; j < TABLE[8].length; j++) {
            if (j != 1) {
                TABLE[8][j] = new AccionSemantica3();
            }
        }
    }


    public TablaAccionesSemanticas(MapaDeSimbolos map) {
        this.SYMBOL_MAP= map;
    }

    public AccionSemantica getAccionSemantica( int estado,char simbolo ) {
        return TABLE[estado][getColumna(simbolo)];
    }
    private int getColumna(char simbolo) {
        return SYMBOL_MAP.get(simbolo);
    }

}
