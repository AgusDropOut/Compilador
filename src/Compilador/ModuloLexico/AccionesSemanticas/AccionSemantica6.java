package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.*;

import java.io.FilterOutputStream;
import java.io.IOException;

public class AccionSemantica6 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        puntero.retroceder();
        double valor = Double.parseDouble(lexema.toString().replace("D", "E"));


        if ((valor > Double.MIN_NORMAL && valor < Double.MAX_VALUE) || (valor < -Double.MIN_NORMAL && valor > -Double.MAX_VALUE)){
            ElementoTablaDeSimbolos elementoTablaDeSimbolos = new ElementoTablaDeSimbolos();
            elementoTablaDeSimbolos.setTipo("dfloat");
            elementoTablaDeSimbolos.setValor(String.valueOf(valor));
            tablaDeSimbolos.addSimbolo(String.valueOf(lexema), elementoTablaDeSimbolos);
            MapaDeTokensAID.tokenCTE = true;
        } else {
            System.out.println("Error linea " + AnalizadorLexico.getNumeroDeLinea()  + ":" +" El numero " + lexema.toString() + " esta fuera del rango permitido para el sufijo double(D)" );
        }


    }
}
