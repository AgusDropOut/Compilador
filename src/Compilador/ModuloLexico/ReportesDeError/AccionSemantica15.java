package Compilador.ModuloLexico.ReportesDeError;

import Compilador.ModuloLexico.AccionSemantica;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.Puntero;
import Compilador.ModuloLexico.TablaDeSimbolos;

import java.io.IOException;

public class AccionSemantica15 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println("Error línea " + AnalizadorLexico.getNumeroDeLinea() + ":" + " sufijo de constante inválido, las constantes de tipo ulong debe terminar con sufijo UL");
    }
}
