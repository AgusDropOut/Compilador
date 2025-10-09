package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.*;

import java.io.FilterOutputStream;
import java.io.IOException;

public class AccionSemantica6 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        puntero.retroceder();
        double valor = Double.parseDouble(lexema.toString().replace("D", "E"));


        if (((valor > 2.2250738585072014e-308 && valor < 1.7976931348623157e308) ||
                (valor < -2.2250738585072014e-308 && valor > -1.7976931348623157e308)) || valor == 0.0) {

            ElementoTablaDeSimbolos elementoTablaDeSimbolos = new ElementoTablaDeSimbolos();
            elementoTablaDeSimbolos.setTipo("dfloat");
            elementoTablaDeSimbolos.setValor(valor);
            TablaDeSimbolos.addSimbolo(String.valueOf(lexema), elementoTablaDeSimbolos);
            MapaDeTokensAID.tokenCTE = true;
        } else {
            System.out.println("Error linea " + AnalizadorLexico.getNumeroDeLinea() +
                    ": El numero " + lexema.toString() + " esta fuera del rango permitido para double.");
        }
    }
}
