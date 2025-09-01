import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        MapaDeSimbolos mapa = new MapaDeSimbolos();
        AnalizadorLexico analizadorLexico = new AnalizadorLexico(new MatrizDeTransicion(mapa), "src/archivo.txt", new TablaAccionesSemanticas(mapa));
        analizadorLexico.analizar();
    }
}