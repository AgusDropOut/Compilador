package Compilador.ModuloLexico;

import Compilador.ModuloSintactico.Parser;

import java.util.HashMap;

public class MapaDeTokensAID {
    private static final HashMap<String, Short> mapa = new HashMap<>();
    public static boolean tokenID = false;
    public static boolean tokenCTE = false;

    static {
        // Palabras reservadas
        mapa.put("while", Parser.WHILE);
        mapa.put("if", Parser.IF);
        mapa.put("else", Parser.ELSE);
        mapa.put("endif", Parser.ENDIF);
        mapa.put("print", Parser.PRINT);
        mapa.put("return", Parser.RETURN);
        mapa.put("do", Parser.DO);
        mapa.put("ulong", Parser.ULONG);
        mapa.put("trunc", Parser.TRUNC);
        mapa.put(":=", Parser.ASIG);

        mapa.put("<=", Parser.COMP);
        mapa.put(">=", Parser.COMP);
        mapa.put("!=", Parser.COMP);
        mapa.put("==", Parser.COMP);
        mapa.put("<", Parser.COMP);
        mapa.put(">", Parser.COMP);



        mapa.put("+", (short)'+');
        mapa.put("-", (short)'-');
        mapa.put("*", (short)'*');
        mapa.put("/", (short)'/');
        mapa.put("(", (short)'(');
        mapa.put(")", (short)')');
        mapa.put("{", (short)'{');
        mapa.put("}", (short)'}');
        mapa.put(";", (short)';');
        mapa.put(".", (short)'.');
        mapa.put("=", (short)'=');
        mapa.put("#", (short)'#');
        mapa.put("_", (short)'_');
        mapa.put("!", (short)'!');
        mapa.put(",", (short)',');

    }


    public static int getToken(String lexema) {
        if(existe(lexema)) {
            return mapa.get(lexema);
        } else {
            if(tokenID) {
                AnalizadorLexico.yyval.sval = lexema;
                tokenID = false;
                return Parser.ID;
            } else  {
                AnalizadorLexico.yyval.sval = lexema;
                tokenCTE = false;
                return Parser.CTE;
            }
        }
    }

    public static boolean existe(String lexema) {
        return mapa.containsKey(lexema);
    }
}

