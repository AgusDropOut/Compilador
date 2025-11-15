package Compilador.ModuloLexico.ReportesDeError;

import Compilador.ModuloLexico.AccionSemantica;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.Puntero;
import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.Util.RecolectorDeErrores;

import java.io.IOException;

public class AccionSemantica15 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        RecolectorDeErrores.agregarError(" sufijo de constante inválido, las constantes de tipo ulong debe terminar con sufijo UL",AnalizadorLexico.getNumeroDeLinea());
        AnalizadorLexico.setEstadoActual(0);
        lexema.setLength(0);
    }
}
