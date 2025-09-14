package Compilador;

import Compilador.ModuloLexico.*;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        MapaDeSimbolos mapa = new MapaDeSimbolos();
        TablaDeSimbolos tablaDeSimbolos = new TablaDeSimbolos();
        AnalizadorLexico analizadorLexico = new AnalizadorLexico(new MatrizDeTransicion(mapa), "src/archivo.txt", new TablaAccionesSemanticas(mapa), tablaDeSimbolos);
        analizadorLexico.analizar();
    }
}