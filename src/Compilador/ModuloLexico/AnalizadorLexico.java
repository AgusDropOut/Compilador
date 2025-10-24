package Compilador.ModuloLexico;

import Compilador.ModuloSintactico.Parser;
import Compilador.ModuloSintactico.ParserValExt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AnalizadorLexico {
    private static MatrizDeTransicion matrizDeTransicion;
    private static int linea = 1;
    private static TablaAccionesSemanticas tablaAccionesSemanticas;
    public static  ArrayList<String> TokensEncontrados =  new ArrayList<>();
    private static Puntero puntero;
    private static String content;
    private static StringBuilder lexema;
    private static int estadoActual = 0;
    private static TablaDeSimbolos tablaDeSimbolos;
    public static ParserValExt yylval;
    public static boolean flag =true;

    public AnalizadorLexico(MatrizDeTransicion matrizDeTransicion, String codigoFuente, TablaAccionesSemanticas accionesSemanticas, TablaDeSimbolos tablaDeSimbolos, ParserValExt yyval) throws IOException {
        AnalizadorLexico.matrizDeTransicion = matrizDeTransicion;
        puntero = new Puntero(0);
        content = new String(Files.readAllBytes(Paths.get(codigoFuente)));
        lexema = new StringBuilder();
        tablaAccionesSemanticas = accionesSemanticas;
        AnalizadorLexico.tablaDeSimbolos = tablaDeSimbolos;
        yylval = yyval;
    }

    public static short yylex() throws IOException {
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
            siguienteEstado = matrizDeTransicion.getEstado(estadoActual, c);
            //Manejo de saltos de linea
            if (c == '\n' || c == '\r') {
                if (!flag) {
                    linea++;
                    flag = true;
                } else {
                    flag = false;
                }
            }




            if (siguienteEstado == MatrizDeTransicion.FINAL) {
                // Ejecutar la acción correspondiente al estado actual antes de reiniciar
                tablaAccionesSemanticas.getAccionSemantica(estadoActual, c)
                        .realizar(content, puntero, lexema, tablaDeSimbolos);
                estadoActual = 0; // reiniciar
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
        TokensEncontrados.add(lexema.toString());
        return MapaDeTokensAID.getToken(lexema.toString());
    }

    public static int getNumeroDeLinea(){
        return linea;
    }
    public static void setEstadoActual(int nuevoEstado){
        estadoActual = nuevoEstado;
    }

    public void imprimirTokens(){
        System.out.println("Tokens encontrados: ");
        for (String tocken: TokensEncontrados) {
            System.out.print(tocken + "  ");
        }
    }


}
