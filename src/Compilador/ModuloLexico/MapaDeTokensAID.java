package Compilador.ModuloLexico;

import Compilador.ModuloSintactico.Parser;
import Compilador.ModuloSintactico.ParserVal;

import java.util.HashMap;

public class MapaDeTokensAID {
    private static final HashMap<String, Short> mapa = new HashMap<>();
    public static boolean tokenID = false;
    public static boolean tokenCTE = false;
    public static boolean tokenCADENA = false;

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
        mapa.put("->", Parser.FLECHA);
        mapa.put("cr",Parser.CR);

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


    public static short getToken(String lexema) {
        if(existe(lexema)) {
            return mapa.get(lexema);
        } else {
            if(tokenID) {
                AnalizadorLexico.yylval = new ParserVal(lexema);
                tokenID = false;
                return Parser.ID;
            } else if (tokenCTE)  {
                AnalizadorLexico.yylval = new ParserVal(lexema);
                tokenCTE = false;
                return Parser.CTE;
            } else {
                AnalizadorLexico.yylval = new ParserVal(lexema);
                tokenCADENA = false;
                return Parser.CADENA;
            }
        }
    }

    public static boolean existe(String lexema) {
        return mapa.containsKey(lexema);
    }
}

