package Compilador.ModuloSemantico;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Compilador.ModuloLexico.ElementoTablaDeSimbolos;
import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.ModuloSintactico.ParserValExt;

public class ArregloTercetos {
    private static List<Terceto> tercetos = new ArrayList<>();
    private static Stack<Integer> pilaIf = new Stack<>();
    private static Stack<Integer> pilaWhile = new Stack<>();

    public static ParserValExt crearTerceto(String operador, String op1, String op2) {
        // Devolver numero del terceto
        Terceto nuevo = new Terceto(operador, op1, op2);
        tercetos.add(nuevo);

        ParserValExt val = new ParserValExt();
        val.sval = String.valueOf(tercetos.size() - 1);
        return val;
    }

    // Crea el BF y lo apila
    public static ParserValExt crearTercetoBackPatchingIF(String operador, String op1, String op2){
        Terceto nuevo = new Terceto(operador, op1, op2);
        tercetos.add(nuevo);
        pilaIf.push(tercetos.size() - 1);

        ParserValExt val = new ParserValExt();
        val.sval = String.valueOf(tercetos.size() - 1);
        return val;
    }

    // Completa el BF y crea el BI (salto incondicional)
    public static ParserValExt crearTercetoBackPatchingIFDesapilaryCompletar(String operador, String op1, String op2){
        int tercetoBF = pilaIf.pop(); // posición del BF
        // El BF salta al siguiente terceto (inicio del bloque ELSE)
        tercetos.get(tercetoBF).setOp2(String.valueOf(tercetos.size()+1));

        // Creamos el BI (salta al final del if)
        Terceto nuevo = new Terceto(operador, op1, op2);
        tercetos.add(nuevo);
        pilaIf.push(tercetos.size() - 1); // apilamos el BI para completarlo al ENDIF

        ParserValExt val = new ParserValExt();
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

    public static ParserValExt crearTercetoBackPatchingWHILE(String operador, String op1,String op2){
        Terceto nuevo = new Terceto(operador, op1, op2);
        tercetos.add(nuevo);
        pilaIf.push(tercetos.size() - 1);
        ParserValExt val = new ParserValExt();
        val.sval = String.valueOf(tercetos.size() - 1);
        return val;
    }

    public static ParserValExt completarBackPatchingWHILE(){
        int tercetoBF = pilaIf.pop(); // posición del BF
        int inicioWhile = pilaWhile.pop(); // posición inicial del WHILE
        tercetos.get(tercetoBF).setOp2(String.valueOf(tercetos.size()+1)); // El BF salta al siguiente terceto (después del WHILE)
        // Creamos el BI (salto incondicional al inicio del WHILE)
        Terceto nuevo = new Terceto("bl", String.valueOf(inicioWhile), null);
        tercetos.add(nuevo);
        ParserValExt val = new ParserValExt();
        val.sval = String.valueOf(tercetos.size() - 1);
        return val;

    }




    public static void imprimirTercetos() {
        System.out.println("===== LISTA DE TERCETOS =====");
        Pattern patronEntero = Pattern.compile("^\\d+$"); // solo enteros (positivos o negativos)

        for (int i = 0; i < tercetos.size(); i++) {
            Terceto t = tercetos.get(i);
            String op1 = t.getOp1();
            String op2 = t.getOp2();

            // Si son enteros puros, los mostramos entre []
            if (op1 != null && patronEntero.matcher(op1).matches()) {
                op1 = "[" + op1 + "]";
            }
            if (op2 != null && patronEntero.matcher(op2).matches()) {
                op2 = "[" + op2 + "]";
            }

            System.out.printf("[%d] (%s, %s, %s)%n",
                    i,
                    t.getOperador(),
                    op1 != null ? op1 : "_",
                    op2 != null ? op2 : "_");
        }
        System.out.println("=============================");
    }

    private static int contadorTemporales = 0;

    /**
     * Genera un nombre único para una variable temporal.
     * @return nombre de la variable temporal (ej: _t1, _t2, ...)
     */
    public static String nuevaTemporal() {
        contadorTemporales++;
        return "_t" + contadorTemporales;
    }


    public static String declararTemporal(String tipo, String ambitoActual) {
        String nombreTemp = nuevaTemporal();
        // Agregar ambito al nombre de la tabla de símbolos
        String nombreCompleto = nombreTemp + ":" + ambitoActual;

        // Crear elemento en la tabla de símbolos
        ElementoTablaDeSimbolos elem = new ElementoTablaDeSimbolos();
        elem.setTipo(tipo);
        elem.setUso("temporal");
        elem.setAmbito(ambitoActual);
        TablaDeSimbolos.addSimbolo(nombreCompleto, elem);

        return nombreCompleto;
    }
}
