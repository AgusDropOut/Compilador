package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.AccionSemantica;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.Puntero;
import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.Util.RecolectorDeErrores;

import java.io.IOException;

public class AccionSemantica11 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        char c = codigoFuente.charAt(puntero.getPuntero());
        if (c == '\n' || c == '\r'){
            RecolectorDeErrores.agregarError(" No se puede realizar saltos de linea dentro de una cadena de caracteres", AnalizadorLexico.getNumeroDeLinea());
        }
        else {
            lexema.append(c);
        }

    }
}
