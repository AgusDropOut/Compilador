package Compilador.ModuloLexico.ReportesDeError;

import Compilador.ModuloLexico.AccionSemantica;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.Puntero;
import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.Util.RecolectorDeErrores;

import java.io.IOException;

public class AccionSemantica14 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.append(c);
        RecolectorDeErrores.agregarError("Constante ulong inválida: debe ser un número entero seguido de UL (por ejemplo: 0UL, 25UL, 999UL).", AnalizadorLexico.getNumeroDeLinea());
        lexema.setLength(0);
        AnalizadorLexico.setEstadoActual(0);
    }
}
