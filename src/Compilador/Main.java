package Compilador;

import Compilador.ModuloGC.AnalizadorDeBloques;
import Compilador.ModuloGC.GeneradorDeCodigo;
import Compilador.ModuloLexico.*;
import Compilador.ModuloSemantico.ArregloTercetos;
import Compilador.ModuloSemantico.Terceto;
import Compilador.ModuloSintactico.Parser;
import Compilador.ModuloSintactico.ParserValExt;
import Compilador.Util.RecolectorDeErrores;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Por favor, indica el archivo fuente como parámetro.");
            System.exit(1);
        }

        String nombreArchivo = args[0];

        MapaDeSimbolos mapa = new MapaDeSimbolos();
        TablaDeSimbolos tablaDeSimbolos = new TablaDeSimbolos();
        ParserValExt yylval = new ParserValExt();

        AnalizadorLexico lexer = new AnalizadorLexico(
                new MatrizDeTransicion(mapa),
                nombreArchivo,
                new TablaAccionesSemanticas(mapa),
                tablaDeSimbolos,
                yylval
        );

        Parser parser = new Parser();
        parser.run();
        lexer.imprimirTokens();
        TablaDeSimbolos.imprimirTabla();
        ArregloTercetos.imprimirTercetos();
        RecolectorDeErrores.imprimirMensajes();
        if(RecolectorDeErrores.hayErrores()) {
            System.out.println("No se puede generar código debido a errores previos.");
            System.exit(1);
        }
        ArrayList<Terceto> tercetosGenerados = new ArrayList<>(ArregloTercetos.obtenerTercetos());
        GeneradorDeCodigo productorAssembler = new GeneradorDeCodigo(tercetosGenerados, "programita");
        productorAssembler.generarCodigo();
        productorAssembler.imprimirAssembler();
        productorAssembler.guardarArchivoWat();


    }
}