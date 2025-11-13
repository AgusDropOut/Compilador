package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.*;
import Compilador.Util.RecolectorDeErrores;

import java.io.IOException;

public class AccionSemantica4 implements AccionSemantica {

    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        puntero.retroceder();

        if(lexema.toString().length() > 20) {
            StringBuilder lexemaTruncado = new StringBuilder(lexema.toString().substring(0, 20));
            RecolectorDeErrores.agregarWarning(" el identificador " + lexema.toString() + " supera los 20 caracteres maximos, sera truncado a " + lexemaTruncado, AnalizadorLexico.getNumeroDeLinea());
            lexema.setLength(0);
            lexema.append(lexemaTruncado);
        }
        MapaDeTokensAID.tokenID = true;
    }
}
