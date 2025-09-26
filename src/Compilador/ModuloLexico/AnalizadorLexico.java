package Compilador.ModuloLexico;

import Compilador.ModuloSintactico.Parser;
import Compilador.ModuloSintactico.ParserVal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AnalizadorLexico {
    private static MatrizDeTransicion matrizDeTransicion;
    private static int linea = 0;
    private static TablaAccionesSemanticas tablaAccionesSemanticas;
    public static  ArrayList<String> palabrasReservadasEncontradas =  new ArrayList<>();
    private static Puntero puntero;
    private static String content;
    private static StringBuilder lexema;
    private static int estadoActual = 0;
    private static TablaDeSimbolos tablaDeSimbolos;
    public static ParserVal yylval;

    public AnalizadorLexico(MatrizDeTransicion matrizDeTransicion, String codigoFuente, TablaAccionesSemanticas accionesSemanticas, TablaDeSimbolos tablaDeSimbolos, ParserVal yyval) throws IOException {
        this.matrizDeTransicion = matrizDeTransicion;
        this.puntero = new Puntero(0);
        this.content = new String(Files.readAllBytes(Paths.get(codigoFuente)));
        this.lexema = new StringBuilder();
        this.tablaAccionesSemanticas = accionesSemanticas;
        this.tablaDeSimbolos = tablaDeSimbolos;
        this.yylval = yyval;
    }

    public static int yylex() throws IOException {
        int siguienteEstado = 0;
        char c = 'a';
        boolean tokenLeido = false;
        int token = -1;
        lexema.setLength(0);

        while (puntero.getPuntero() <= content.length() && !tokenLeido) {
            //Manejo de fin de archivo
            if(puntero.getPuntero() < content.length()) {
                c = content.charAt(puntero.getPuntero());
            } else {
                c = '$';
                return 0;
            }
            //Manejo de saltos de linea
            if (c == '\n' || c == '\r') {
                if (siguienteEstado != MatrizDeTransicion.FINAL) {
                    linea++;
                }
            }


            siguienteEstado = matrizDeTransicion.getEstado(estadoActual, c);

            if (siguienteEstado == MatrizDeTransicion.FINAL) {
                // Ejecutar la acción correspondiente al estado actual antes de reiniciar
                tablaAccionesSemanticas.getAccionSemantica(estadoActual, c)
                        .realizar(content, puntero, lexema, tablaDeSimbolos);
                estadoActual = 0; // reiniciar
                System.out.println("Lexema: " + lexema.toString());
                tokenLeido = true;
            } else if (siguienteEstado == MatrizDeTransicion.ERROR) {
                tablaAccionesSemanticas.getAccionSemantica(estadoActual, c)
                        .realizar(content, puntero, lexema, tablaDeSimbolos);
            } else {
                tablaAccionesSemanticas.getAccionSemantica(estadoActual, c)
                        .realizar(content, puntero, lexema, tablaDeSimbolos);
                estadoActual = siguienteEstado;
            }
            puntero.avanzar();
        }


        return MapaDeTokensAID.getToken(lexema.toString());
    }

    public static int getNumeroDeLinea(){
        return linea;
    }
    public static void setEstadoActual(int nuevoEstado){
        estadoActual = nuevoEstado;
    }

}
