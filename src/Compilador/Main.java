package Compilador;

import Compilador.ModuloLexico.*;
import Compilador.ModuloSintactico.Parser;
import Compilador.ModuloSintactico.ParserVal;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        ///////////////////////////////////////FALTA PASAR EL CODIGO FUENTE POR ARGS////////////////////////////////////////////


        MapaDeSimbolos mapa = new MapaDeSimbolos();
        TablaDeSimbolos tablaDeSimbolos = new TablaDeSimbolos();
        ParserVal yylval = new ParserVal();
        AnalizadorLexico lexer = new AnalizadorLexico(new MatrizDeTransicion(mapa), "src/archivo.txt", new TablaAccionesSemanticas(mapa), tablaDeSimbolos, yylval);
        Parser parser = new Parser();
        parser.run();
    }
}