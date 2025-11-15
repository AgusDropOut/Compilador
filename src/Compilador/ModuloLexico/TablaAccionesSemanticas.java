package Compilador.ModuloLexico;

import Compilador.ModuloLexico.AccionesSemanticas.*;
import Compilador.ModuloLexico.ReportesDeError.*;

public class TablaAccionesSemanticas {
    private final MapaDeSimbolos SYMBOL_MAP;

    public static final AccionSemantica[][] TABLE = new AccionSemantica[17][22];

    public TablaAccionesSemanticas(MapaDeSimbolos map) {
        this.SYMBOL_MAP= map;
        // Fila 0
        TABLE[0][0] = new AccionSemantica1();
        TABLE[0][1] = new AccionSemantica1();
        TABLE[0][2] = new AccionSemantica1();
        TABLE[0][3] = new AccionSemantica1();
        TABLE[0][4] = new AccionSemantica1();
        TABLE[0][5] = new AccionSemantica1();
        TABLE[0][6] = new AccionSemantica12();
        TABLE[0][7] = new AccionSemantica8();
        TABLE[0][8] = new AccionSemantica1();
        TABLE[0][9] = new AccionSemantica1();
        TABLE[0][10] = new AccionSemantica8();
        TABLE[0][11] = new AccionSemantica1();
        TABLE[0][12] = new AccionSemantica1();
        TABLE[0][13] = new AccionSemantica1();
        TABLE[0][14] = new AccionSemantica1();
        TABLE[0][15] = new AccionSemantica7();
        TABLE[0][16] = new AccionSemantica7();
        TABLE[0][17] = new AccionSemantica12();
        TABLE[0][18] = new AccionSemantica1();

        // Fila 1
        TABLE[1][0] = new AccionSemantica2();
        TABLE[1][1] = new AccionSemantica4();
        TABLE[1][2] = new AccionSemantica2();
        TABLE[1][3] = new AccionSemantica2();
        TABLE[1][4] = new AccionSemantica2();
        TABLE[1][5] = new AccionSemantica2();
        TABLE[1][6] = new AccionSemantica2();
        TABLE[1][7] = new AccionSemantica4();
        TABLE[1][8] = new AccionSemantica4();
        TABLE[1][9] = new AccionSemantica4();
        TABLE[1][10] = new AccionSemantica4();
        TABLE[1][11] = new AccionSemantica4();
        TABLE[1][12] = new AccionSemantica4();
        TABLE[1][13] = new AccionSemantica4();
        TABLE[1][14] = new AccionSemantica4();
        TABLE[1][15] = new AccionSemantica4();
        TABLE[1][16] = new AccionSemantica4();
        TABLE[1][17] = new AccionSemantica4();
        TABLE[1][18] = new AccionSemantica4();

        // Fila 2
        TABLE[2][0] = new AccionSemantica2();
        TABLE[2][1] = new AccionSemantica14();
        TABLE[2][2] = new AccionSemantica2();
        TABLE[2][3] = new AccionSemantica14();
        TABLE[2][4] = new AccionSemantica14();
        TABLE[2][5] = new AccionSemantica14();
        TABLE[2][6] = new AccionSemantica14();
        TABLE[2][7] = new AccionSemantica14();
        TABLE[2][8] = new AccionSemantica14();
        TABLE[2][9] = new AccionSemantica2();
        TABLE[2][10] = new AccionSemantica14();
        TABLE[2][11] = new AccionSemantica14();
        TABLE[2][12] = new AccionSemantica14();
        TABLE[2][13] = new AccionSemantica14();
        TABLE[2][14] = new AccionSemantica14();
        TABLE[2][15] = new AccionSemantica14();
        TABLE[2][16] = new AccionSemantica14();
        TABLE[2][17] = new AccionSemantica14();
        TABLE[2][18] = new AccionSemantica14();

        // Fila 3
        TABLE[3][0] = new AccionSemantica15();
        TABLE[3][1] = new AccionSemantica15();
        TABLE[3][2] = new AccionSemantica15();
        TABLE[3][3] = new AccionSemantica5();
        TABLE[3][4] = new AccionSemantica15();
        TABLE[3][5] = new AccionSemantica15();
        TABLE[3][6] = new AccionSemantica15();
        TABLE[3][7] = new AccionSemantica15();
        TABLE[3][8] = new AccionSemantica15();
        TABLE[3][9] = new AccionSemantica15();
        TABLE[3][10] = new AccionSemantica15();
        TABLE[3][11] = new AccionSemantica15();
        TABLE[3][12] = new AccionSemantica15();
        TABLE[3][13] = new AccionSemantica15();
        TABLE[3][14] = new AccionSemantica15();
        TABLE[3][15] = new AccionSemantica15();
        TABLE[3][16] = new AccionSemantica15();
        TABLE[3][17] = new AccionSemantica15();
        TABLE[3][18] = new AccionSemantica15();

        // Fila 4
        for (int j = 0; j < 20; j++) {
            if (j == 13) {
                TABLE[4][j] = new AccionSemantica9();
            } else {
                TABLE[4][j] = new AccionSemantica10();
            }
        }

        // Fila 5
        for (int j = 0; j < 20; j++) {
            if (j == 11 || j == 17) {
                TABLE[5][j] = new AccionSemantica9();
            } else {
                TABLE[5][j] = new AccionSemantica10();
            }
        }

        // Fila 6
        for (int j = 0; j < 20; j++) {
            if (j == 11) {
                TABLE[6][j] = new AccionSemantica9();
            } else {
                TABLE[6][j] = new AccionSemantica10();
            }
        }

        // Fila 7
        for (int j = 0; j < 20; j++) {
            if (j == 18) {
                TABLE[7][j] = new AccionSemantica12();
            } else if (j == 16) {
                TABLE[7][j] = new AccionSemantica17();
            } else {
                TABLE[7][j] = new AccionSemantica11();
            }
        }

        // Fila 8
        TABLE[8][0] = new AccionSemantica3();
        TABLE[8][1] = new AccionSemantica2();
        for (int j = 2; j < 20; j++) {
            TABLE[8][j] = new AccionSemantica3();
        }


        // Fila 9
        TABLE[9][0] = new AccionSemantica2();
        for (int j = 1; j < 20; j++) {
            TABLE[9][j] = new AccionSemantica19();
        }
        TABLE[9][2] = new AccionSemantica10();
        TABLE[9][3] = new AccionSemantica10();
        TABLE[9][4] = new AccionSemantica10();
        TABLE[9][5] = new AccionSemantica10();

        // Fila 10
        TABLE[10][0] = new AccionSemantica2();
        for (int j = 1; j < 20; j++) {
            if (j == 4) {
                TABLE[10][j] = new AccionSemantica2();
            } else {
                TABLE[10][j] = new AccionSemantica6();
            }
        }

        // Fila 11
        for (int j = 0; j < 20; j++) {
            if (j == 7 || j == 8) {
                TABLE[11][j] = new AccionSemantica2();
            } else {
                TABLE[11][j] = new AccionSemantica19();
            }
        }

        // Fila 12
        TABLE[12][0] = new AccionSemantica2();
        for (int j = 1; j < 20; j++) {
            TABLE[12][j] = new AccionSemantica6();
        }

        // Fila 13
        for (int j = 0; j < 20; j++) {
            if (j == 14) {
                TABLE[13][j] = new AccionSemantica2();
            } else {
                TABLE[13][j] = new AccionSemantica20();
            }
        }

        // Fila 14
        for (int j = 0; j < 20; j++) {
            TABLE[14][j] = new AccionSemantica2();
        }

        // Fila 15
        for (int j = 0; j < 20; j++) {
            if (j == 14) {
                TABLE[15][j] = new AccionSemantica9();
            } else {
                TABLE[15][j] = new AccionSemantica2();
            }
        }

        // Fila 16
        for (int j = 0; j < 20; j++) {
            if (j == 11) {
                TABLE[16][j] = new AccionSemantica9();
            } else {
                TABLE[16][j] = new AccionSemantica16();
            }
        }

        //Columna 19
        TABLE[0][19] = new AccionSemantica1();
        TABLE[1][19] = new AccionSemantica4();
        TABLE[2][19] = new AccionSemantica14();
        TABLE[3][19] = new AccionSemantica15();
        TABLE[4][19] = new AccionSemantica16();
        TABLE[5][19] = new AccionSemantica16();
        TABLE[6][19] = new AccionSemantica16();
        TABLE[7][19] = new AccionSemantica11();
        TABLE[8][19] = new AccionSemantica3();
        TABLE[9][19] = new AccionSemantica19();
        TABLE[10][19] = new AccionSemantica6();
        TABLE[11][19] = new AccionSemantica19();
        TABLE[12][19] = new AccionSemantica6();
        TABLE[13][19] = new AccionSemantica20();
        TABLE[14][19] = new AccionSemantica2();
        TABLE[15][19] = new AccionSemantica2();
        TABLE[16][19] = new AccionSemantica16();

        //Columna 20 (final de archivo)
        TABLE[0][20] = new AccionSemantica7();
        TABLE[1][20] = new AccionSemantica4();
        TABLE[2][20] = new AccionSemantica18();
        TABLE[3][20] = new AccionSemantica18();
        TABLE[4][20] = new AccionSemantica10();
        TABLE[5][20] = new AccionSemantica10();
        TABLE[6][20] = new AccionSemantica10();
        TABLE[7][20] = new AccionSemantica18();
        TABLE[8][20] = new AccionSemantica3();
        TABLE[9][20] = new AccionSemantica18();
        TABLE[10][20] = new AccionSemantica6();
        TABLE[11][20] = new AccionSemantica18();
        TABLE[12][20] = new AccionSemantica6();
        TABLE[13][20] = new AccionSemantica18();
        TABLE[14][20] = new AccionSemantica21();
        TABLE[15][20] = new AccionSemantica18();
        TABLE[16][20] = new AccionSemantica18();

        //Columna 21 (Caracter invalido)
        TABLE[0][21] = new AccionSemantica13();
        TABLE[1][21] = new AccionSemantica13();
        TABLE[2][21] = new AccionSemantica13();
        TABLE[3][21] = new AccionSemantica13();
        TABLE[4][21] = new AccionSemantica13();
        TABLE[5][21] = new AccionSemantica13();
        TABLE[6][21] = new AccionSemantica13();
        TABLE[7][21] = new AccionSemantica13();
        TABLE[8][21] = new AccionSemantica13();
        TABLE[9][21] = new AccionSemantica13();
        TABLE[10][21] = new AccionSemantica13();
        TABLE[11][21] = new AccionSemantica13();
        TABLE[12][21] = new AccionSemantica13();
        TABLE[13][21] = new AccionSemantica13();
        TABLE[14][21] = new AccionSemantica13();
        TABLE[15][21] = new AccionSemantica13();
        TABLE[16][21] = new AccionSemantica13();

    }


    public AccionSemantica getAccionSemantica( int estado,char simbolo ) {
        return TABLE[estado][getColumna(simbolo)];
    }

    private int getColumna(char simbolo) {
        return SYMBOL_MAP.get(simbolo);
    }

}
