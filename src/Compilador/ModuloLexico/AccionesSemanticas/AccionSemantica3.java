package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.*;
import Compilador.Util.RecolectorDeErrores;

import java.io.IOException;

public class AccionSemantica3 implements AccionSemantica {

    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        puntero.retroceder();
        if (PalabrasReservadas.existe(lexema.toString())){

        } else {
            RecolectorDeErrores.agregarError(" La palabra reservada " + lexema.toString() + " no existe", AnalizadorLexico.getNumeroDeLinea());
        }

    }
}
