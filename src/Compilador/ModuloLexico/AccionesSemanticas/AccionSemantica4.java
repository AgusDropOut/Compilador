package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.*;

import java.io.IOException;

public class AccionSemantica4 implements AccionSemantica {

    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        puntero.retroceder();

        if(lexema.toString().length() > 20) {
            StringBuilder lexemaTruncado = new StringBuilder(lexema.toString().substring(0, 20));
            System.out.println("WARNING linea " + AnalizadorLexico.getNumeroDeLinea()  + ":" +" el identificador " + lexema.toString() + " supera los 20 caracteres maximos, sera truncado a " + lexemaTruncado);
            lexema = lexemaTruncado;
        }


        if (tablaDeSimbolos.estaSimbolo(lexema.toString())){
            AnalizadorLexico.palabrasReservadasEncontradas.add(lexema.toString());
        } else {
            tablaDeSimbolos.addSimbolo(lexema.toString());
            AnalizadorLexico.palabrasReservadasEncontradas.add(lexema.toString());
        }
        MapaDeTokensAID.tokenID = true;
    }
}
