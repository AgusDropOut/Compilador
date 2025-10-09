package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.*;

import java.io.IOException;

public class AccionSemantica12 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.append(c);
        MapaDeTokensAID.tokenCADENA = true;
        ElementoTablaDeSimbolos elementoTablaDeSimbolos = new ElementoTablaDeSimbolos();
        elementoTablaDeSimbolos.setTipo("dfloat");
        TablaDeSimbolos.addSimbolo(lexema.toString(), elementoTablaDeSimbolos);

    }
}
