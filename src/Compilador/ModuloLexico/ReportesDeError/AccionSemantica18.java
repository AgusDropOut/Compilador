package Compilador.ModuloLexico.ReportesDeError;

import Compilador.ModuloLexico.AccionSemantica;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.Puntero;
import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.Util.RecolectorDeErrores;

import java.io.IOException;

public class AccionSemantica18 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.append(c);
        RecolectorDeErrores.agregarError(" no es válido en la definición de una constante punto flotante. Se espera definición estilo <secuencia_digitos>.D <secuencia_digitos><signo_exponente> <exponente>", AnalizadorLexico.getNumeroDeLinea());
        lexema.setLength(0);
        AnalizadorLexico.setEstadoActual(0);
    }
}
