package Compilador.ModuloSemantico;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Compilador.ModuloSintactico.ParserVal;

public class ArregloTercetos {
    private static List<Terceto> tercetos = new ArrayList<>();
    private static Stack<Integer> pilaIf = new Stack<>();
    private static Stack<Integer> pilaWhile = new Stack<>();

    public static ParserVal crearTerceto(String operador, String op1, String op2) {
        // Devolver numero del terceto
        Terceto nuevo = new Terceto(operador, op1, op2);
        tercetos.add(nuevo);

        ParserVal val = new ParserVal();
        val.sval = String.valueOf(tercetos.size() - 1);
        return val;
    }

    // Crea el BF y lo apila
    public static ParserVal crearTercetoBackPatchingIF(String operador, String op1, String op2){
        Terceto nuevo = new Terceto(operador, op1, op2);
        tercetos.add(nuevo);
        pilaIf.push(tercetos.size() - 1);

        ParserVal val = new ParserVal();
        val.sval = String.valueOf(tercetos.size() - 1);
        return val;
    }

    // Completa el BF y crea el BI (salto incondicional)
    public static ParserVal crearTercetoBackPatchingIFDesapilaryCompletar(String operador, String op1, String op2){
        int tercetoBF = pilaIf.pop(); // posición del BF
        // El BF salta al siguiente terceto (inicio del bloque ELSE)
        tercetos.get(tercetoBF).setOp2(String.valueOf(tercetos.size()+1));

        // Creamos el BI (salta al final del if)
        Terceto nuevo = new Terceto(operador, op1, op2);
        tercetos.add(nuevo);
        pilaIf.push(tercetos.size() - 1); // apilamos el BI para completarlo al ENDIF

        ParserVal val = new ParserVal();
        val.sval = String.valueOf(tercetos.size() - 1);
        return val;
    }

    // Completa el BI al llegar al ENDIF
    public static void completarTercetoBackPatchingIF(){
        int tercetoBI = pilaIf.pop();
        tercetos.get(tercetoBI).setOp2(String.valueOf(tercetos.size()));
    }




    public static void apilarTercetoInicialWHILE() {
        pilaWhile.push(tercetos.size());
    }

    public static ParserVal crearTercetoBackPatchingWHILE(String operador, String op1,String op2){
        Terceto nuevo = new Terceto(operador, op1, op2);
        tercetos.add(nuevo);
        pilaIf.push(tercetos.size() - 1);
        ParserVal val = new ParserVal();
        val.sval = String.valueOf(tercetos.size() - 1);
        return val;
    }

    public static ParserVal completarBackPatchingWHILE(){
        int tercetoBF = pilaIf.pop(); // posición del BF
        int inicioWhile = pilaWhile.pop(); // posición inicial del WHILE
        tercetos.get(tercetoBF).setOp2(String.valueOf(tercetos.size()+1)); // El BF salta al siguiente terceto (después del WHILE)
        // Creamos el BI (salto incondicional al inicio del WHILE)
        Terceto nuevo = new Terceto("bl", String.valueOf(inicioWhile), null);
        tercetos.add(nuevo);
        ParserVal val = new ParserVal();
        val.sval = String.valueOf(tercetos.size() - 1);
        return val;

    }




    public static void imprimirTercetos() {
        System.out.println("===== LISTA DE TERCETOS =====");
        for (int i = 0; i < tercetos.size(); i++) {
            Terceto t = tercetos.get(i);
            // Ejemplo de salida: [0] (BF, cond1, ?)
            System.out.printf("[%d] (%s, %s, %s)%n",
                    i,
                    t.getOperador(),
                    t.getOp1() != null ? t.getOp1() : "_",
                    t.getOp2() != null ? t.getOp2() : "_");
        }
        System.out.println("=============================");
    }

}
