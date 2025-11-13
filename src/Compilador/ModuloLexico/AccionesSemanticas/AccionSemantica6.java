package Compilador.ModuloLexico.AccionesSemanticas;

import Compilador.ModuloLexico.*;
import Compilador.Util.RecolectorDeErrores;
import Compilador.Util.RegistroDeConstantes;

import java.io.FilterOutputStream;
import java.io.IOException;

public class AccionSemantica6 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        puntero.retroceder();

        // Convertir D a E para formato estándar y parsear
        double valor = Double.parseDouble(lexema.toString().replace("D", "E"));

        // Normalizar el valor a notación científica (clave única)
        String claveNormalizada = String.format("%e", valor);  // ej: 2.100000e+00

        // Validar rango IEEE 754 double
        if (((valor > 2.2250738585072014e-308 && valor < 1.7976931348623157e308) ||
                (valor < -2.2250738585072014e-308 && valor > -1.7976931348623157e308)) || valor == 0.0) {

            // Si no existe, crear y agregar
            if (!TablaDeSimbolos.estaSimbolo(claveNormalizada)) {
                ElementoTablaDeSimbolos elemento = new ElementoTablaDeSimbolos();
                elemento.setTipo("dfloat");
                elemento.setValor(valor);
                elemento.setUso("cte");

                TablaDeSimbolos.addSimbolo(claveNormalizada, elemento);
            }

            // Registrar referencia (incrementa contador)
            RegistroDeConstantes.registrarConstante(claveNormalizada);
            MapaDeTokensAID.tokenCTE = true;

            lexema.setLength(0);
            lexema.append(claveNormalizada);

        } else {
            RecolectorDeErrores.agregarError("El número " + lexema.toString() + " está fuera del rango permitido para double.", AnalizadorLexico.getNumeroDeLinea());
        }
    }
}
