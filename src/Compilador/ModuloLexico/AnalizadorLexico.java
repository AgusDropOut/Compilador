package Compilador.ModuloLexico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class AnalizadorLexico {
    private MatrizDeTransicion matrizDeTransicion;
    private TablaAccionesSemanticas tablaAccionesSemanticas;
    public static  ArrayList<String> palabrasReservadasEncontradas =  new ArrayList<>();
    Puntero puntero;
    String content;
    StringBuilder lexema;
    int estadoActual = 0;
    private TablaDeSimbolos tablaDeSimbolos;

    public AnalizadorLexico(MatrizDeTransicion matrizDeTransicion, String codigoFuente, TablaAccionesSemanticas accionesSemanticas, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        this.matrizDeTransicion = matrizDeTransicion;
        this.puntero = new Puntero(0);
        this.content = new String(Files.readAllBytes(Paths.get(codigoFuente)));
        this.lexema = new StringBuilder();
        this.tablaAccionesSemanticas = accionesSemanticas;
        this.tablaDeSimbolos = tablaDeSimbolos;
    }

    public void analizar() throws IOException {
        int siguienteEstado = 0;
        char c = 'a';

        while (puntero.getPuntero() <= content.length()) {
            System.out.println(c);
            if(puntero.getPuntero() < content.length()) {
                c = content.charAt(puntero.getPuntero());
            } else {
                c = '$';
            }
            siguienteEstado = matrizDeTransicion.getEstado(estadoActual, c);

            System.out.println(estadoActual);
            if (siguienteEstado == MatrizDeTransicion.FINAL) {
                System.out.println("FINAL");
                // Ejecutar la acción correspondiente al estado actual antes de reiniciar
                tablaAccionesSemanticas.getAccionSemantica(estadoActual, c)
                        .realizar(content, puntero, lexema, tablaDeSimbolos);
                estadoActual = 0; // reiniciar
            } else if (siguienteEstado == MatrizDeTransicion.ERROR) {
                // Manejar error
                System.out.println("ERROR");
                estadoActual = 0;
            } else {
                tablaAccionesSemanticas.getAccionSemantica(estadoActual, c)
                        .realizar(content, puntero, lexema, tablaDeSimbolos);
                estadoActual = siguienteEstado;
                System.out.println(siguienteEstado);
            }
            puntero.avanzar();
        }

        System.out.println(palabrasReservadasEncontradas);
    }

}
