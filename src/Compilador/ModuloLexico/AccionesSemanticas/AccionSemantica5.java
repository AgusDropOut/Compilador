package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.*;

import java.io.IOException;
;

public class AccionSemantica5 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.append(c);
        String numero = lexema.toString().replace("UL", "");
        long lexemaLong = Long.parseLong(numero);

        if((lexemaLong >= 0) && (lexemaLong < Math.pow(2,32))){
            ElementoTablaDeSimbolos elementoTablaDeSimbolos = new ElementoTablaDeSimbolos();
            elementoTablaDeSimbolos.setTipo("ulong");
            elementoTablaDeSimbolos.setValor(Double.parseDouble(numero));
            TablaDeSimbolos.addSimbolo(lexema.toString(), elementoTablaDeSimbolos);
            MapaDeTokensAID.tokenCTE = true;
        } else {
            System.out.println("Error linea " + AnalizadorLexico.getNumeroDeLinea()  + ":" + " El numero " + lexema.toString() + " esta fuera del rango permitido para el sufijo ulong" );
        }
    }
}
