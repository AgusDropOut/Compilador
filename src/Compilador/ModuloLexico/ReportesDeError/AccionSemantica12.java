package Compilador.ModuloLexico.ReportesDeError;

import Compilador.ModuloLexico.AccionSemantica;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.Puntero;
import Compilador.ModuloLexico.TablaDeSimbolos;

import java.io.IOException;

public class AccionSemantica12  implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.append(c);
        System.out.println("Error línea " + AnalizadorLexico.getNumeroDeLinea() + ":" + " El caracter "+ lexema +" ingresado no es un caracter válido");
        lexema.setLength(0);
        AnalizadorLexico.setEstadoActual(0);
    }
}
