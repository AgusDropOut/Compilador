package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.AccionSemantica;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.Puntero;
import Compilador.ModuloLexico.TablaDeSimbolos;

import java.io.IOException;

public class AccionSemantica11 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        char c = codigoFuente.charAt(puntero.getPuntero());
        if (c == '\n' || c == '\r'){
            System.out.println("Error linea " + AnalizadorLexico.getNumeroDeLinea() + ":" +" No se puede realizar saltos de linea" );
        }
        else {
            lexema.append(c);
        }

    }
}
