public class TablaAccionesSemanticas {
    private final MapaDeSimbolos SYMBOL_MAP;

    public static final AccionSemantica[][] TABLE = new AccionSemantica[16][20];

    public TablaAccionesSemanticas(MapaDeSimbolos map) {
        this.SYMBOL_MAP= map;
        // Fila 0
        TABLE[0][0] = new AccionSemantica6();
        TABLE[0][1] = new AccionSemantica1();
        TABLE[0][2] = new AccionSemantica1();
        TABLE[0][3] = new AccionSemantica1();
        TABLE[0][4] = new AccionSemantica1();
        TABLE[0][5] = new AccionSemantica1();
        TABLE[0][6] = null;
        TABLE[0][7] = new AccionSemantica14();
        TABLE[0][8] = new AccionSemantica15();
        TABLE[0][9] = new AccionSemantica9();
        TABLE[0][10] = new AccionSemantica14();
        TABLE[0][11] = new AccionSemantica15();
        TABLE[0][12] = new AccionSemantica15();
        TABLE[0][13] = new AccionSemantica15();
        TABLE[0][14] = new AccionSemantica15();
        TABLE[0][15] = new AccionSemantica13();
        TABLE[0][16] = new AccionSemantica13();
        TABLE[0][17] = null;
        TABLE[0][18] = new AccionSemantica15();

        // Fila 1
        TABLE[1][0] = new AccionSemantica4();
        TABLE[1][1] = new AccionSemantica5();
        TABLE[1][2] = new AccionSemantica4();
        TABLE[1][3] = new AccionSemantica4();
        TABLE[1][4] = new AccionSemantica4();
        TABLE[1][5] = new AccionSemantica4();
        TABLE[1][6] = new AccionSemantica4();
        TABLE[1][7] = new AccionSemantica5();
        TABLE[1][8] = new AccionSemantica5();
        TABLE[1][9] = new AccionSemantica5();
        TABLE[1][10] = new AccionSemantica5();
        TABLE[1][11] = new AccionSemantica5();
        TABLE[1][12] = new AccionSemantica5();
        TABLE[1][13] = new AccionSemantica5();
        TABLE[1][14] = new AccionSemantica5();
        TABLE[1][15] = new AccionSemantica5();
        TABLE[1][16] = new AccionSemantica5();
        TABLE[1][17] = new AccionSemantica5();
        TABLE[1][18] = new AccionSemantica5();

        // Fila 2
        TABLE[2][0] = new AccionSemantica7();
        TABLE[2][1] = null;
        TABLE[2][2] = new AccionSemantica2();
        TABLE[2][3] = null;
        TABLE[2][4] = null;
        TABLE[2][5] = null;
        TABLE[2][6] = null;
        TABLE[2][7] = null;
        TABLE[2][8] = null;
        TABLE[2][9] = new AccionSemantica10();
        TABLE[2][10] = null;
        TABLE[2][11] = null;
        TABLE[2][12] = null;
        TABLE[2][13] = null;
        TABLE[2][14] = null;
        TABLE[2][15] = null;
        TABLE[2][16] = null;
        TABLE[2][17] = null;
        TABLE[2][18] = null;

        // Fila 3
        TABLE[3][0] = null;
        TABLE[3][1] = null;
        TABLE[3][2] = null;
        TABLE[3][3] = new AccionSemantica8();
        TABLE[3][4] = null;
        TABLE[3][5] = null;
        TABLE[3][6] = null;
        TABLE[3][7] = null;
        TABLE[3][8] = null;
        TABLE[3][9] = null;
        TABLE[3][10] = null;
        TABLE[3][11] = null;
        TABLE[3][12] = null;
        TABLE[3][13] = null;
        TABLE[3][14] = null;
        TABLE[3][15] = null;
        TABLE[3][16] = null;
        TABLE[3][17] = null;
        TABLE[3][18] = null;

        // Fila 4
        for (int j = 0; j < 19; j++) {
            if (j == 13) {
                TABLE[4][j] = new AccionSemantica16();
            } else {
                TABLE[4][j] = new AccionSemantica17();
            }
        }

        // Fila 5
        for (int j = 0; j < 19; j++) {
            if (j == 11 || j == 17) {
                TABLE[5][j] = new AccionSemantica16();
            } else {
                TABLE[5][j] = new AccionSemantica17();
            }
        }

        // Fila 6
        for (int j = 0; j < 19; j++) {
            if (j == 11) {
                TABLE[6][j] = new AccionSemantica16();
            } else {
                TABLE[6][j] = new AccionSemantica17();
            }
        }

        // Fila 7
        for (int j = 0; j < 19; j++) {
            if (j == 18) {
                TABLE[7][j] = new AccionSemantica16();
            } else if (j == 16) {
                TABLE[7][j] = null;
            } else {
                TABLE[7][j] = new AccionSemantica18();
            }
        }

        // Fila 8
        TABLE[8][0] = new AccionSemantica3();
        TABLE[8][1] = new AccionSemantica2();
        for (int j = 2; j < 19; j++) {
            TABLE[8][j] = new AccionSemantica3();
        }


        // Fila 9
        TABLE[9][0] = new AccionSemantica7();
        for (int j = 1; j < 19; j++) {
            TABLE[9][j] = null;
        }

        // Fila 10
        TABLE[10][0] = new AccionSemantica7();
        for (int j = 1; j < 19; j++) {
            if (j == 4) {
                TABLE[10][j] = new AccionSemantica2();
            } else {
                TABLE[10][j] = new AccionSemantica11();
            }
        }

        // Fila 11
        for (int j = 0; j < 19; j++) {
            if (j == 7 || j == 8) {
                TABLE[11][j] = new AccionSemantica12();
            } else {
                TABLE[11][j] = null;
            }
        }

        // Fila 12
        TABLE[12][0] = new AccionSemantica7();
        for (int j = 1; j < 19; j++) {
            TABLE[12][j] = new AccionSemantica11();
        }

        // Fila 13
        for (int j = 0; j < 19; j++) {
            if (j == 14) {
                TABLE[13][j] = new AccionSemantica12();
            } else {
                TABLE[13][j] = null;
            }
        }

        // Fila 14
        for (int j = 0; j < 19; j++) {
            if (j == 14) {
                TABLE[14][j] = new AccionSemantica12();
            } else {
                TABLE[14][j] = new AccionSemantica18();
            }
        }

        // Fila 15
        for (int j = 0; j < 19; j++) {
            if (j == 14) {
                TABLE[15][j] = new AccionSemantica16();
            } else {
                TABLE[15][j] = new AccionSemantica18();
            }
        }
    }


    public AccionSemantica getAccionSemantica( int estado,char simbolo ) {
        return TABLE[estado][getColumna(simbolo)];
    }

    private int getColumna(char simbolo) {
        return SYMBOL_MAP.get(simbolo);
    }

}
