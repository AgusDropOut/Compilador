package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.AccionSemantica;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.Puntero;
import Compilador.ModuloLexico.TablaDeSimbolos;

import java.io.IOException;
;

public class AccionSemantica5 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.append(c);
        String numero = lexema.toString().replace("UL", "");
        long lexemaLong = Long.parseLong(numero);

        if((lexemaLong >= 0) && (lexemaLong < Math.pow(2,32))){
            tablaDeSimbolos.addSimbolo(lexema.toString());
            AnalizadorLexico.palabrasReservadasEncontradas.add(lexema.toString());
            //Añadir devolver CTE + puntero a TS
        } else {
            System.out.println("Error: El numero " + lexema.toString() + " esta fuera del rango permitido para el sufijo ulong" );
        }
    }
}
