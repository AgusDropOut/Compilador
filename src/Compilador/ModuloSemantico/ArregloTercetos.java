package Compilador.ModuloSemantico;


import java.util.ArrayList;
import java.util.List;
import Compilador.ModuloSintactico.ParserVal;

public class ArregloTercetos {
    private static List<Terceto> tercetos = new ArrayList<>();

    public static ParserVal crearTerceto(String operador, String op1, String op2) {
        // Devolver numero del terceto
        Terceto nuevo = new Terceto(operador, op1, op2);
        tercetos.add(nuevo);

        ParserVal val = new ParserVal();
        val.sval = String.valueOf(tercetos.size() - 1);
        return val;
    }

}
