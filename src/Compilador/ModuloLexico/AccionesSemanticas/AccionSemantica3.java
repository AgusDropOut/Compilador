package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.*;

import java.io.IOException;

public class AccionSemantica3 implements AccionSemantica {

    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        puntero.retroceder();
        if (PalabrasReservadas.existe(lexema.toString())){
            AnalizadorLexico.palabrasReservadasEncontradas.add(lexema.toString());
        } else {
            System.out.println("Error linea "+ AnalizadorLexico.getNumeroDeLinea()  + ":" +" La palabra reservada " + lexema.toString() + " no existe");
        }

    }
}
