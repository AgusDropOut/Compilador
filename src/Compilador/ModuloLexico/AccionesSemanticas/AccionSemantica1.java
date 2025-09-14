package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.AccionSemantica;
import Compilador.ModuloLexico.Puntero;
import Compilador.ModuloLexico.TablaDeSimbolos;

import java.io.IOException;


public class AccionSemantica1 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.setLength(0);
        lexema.append(c);
    }
}
