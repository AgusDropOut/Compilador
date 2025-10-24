package Compilador.ModuloLexico;

import Compilador.ModuloSintactico.Parser;

import java.util.HashMap;

public class PalabrasReservadas {
    public static final String[] palabrasReservadas = {
            "while","if","else","endif","print","return","do","ulong", "trunc"
    };

    private static final HashMap<String, Short> mapaPalabrasReservadas = new HashMap<>();

    static {

        mapaPalabrasReservadas.put("while", Parser.WHILE);
        mapaPalabrasReservadas.put("if", Parser.IF);
        mapaPalabrasReservadas.put("else", Parser.ELSE);
        mapaPalabrasReservadas.put("endif", Parser.ENDIF);
        mapaPalabrasReservadas.put("print", Parser.PRINT);
        mapaPalabrasReservadas.put("return", Parser.RETURN);
        mapaPalabrasReservadas.put("do", Parser.DO);
        mapaPalabrasReservadas.put("ulong", Parser.ULONG);
        mapaPalabrasReservadas.put("trunc", Parser.TRUNC);
    }

    public static boolean existe(String palabra) {
        return mapaPalabrasReservadas.containsKey(palabra);
    }

    public static int getToken(String palabra) {
        return mapaPalabrasReservadas.getOrDefault((Object) palabra, (short) -1);
    }
}
