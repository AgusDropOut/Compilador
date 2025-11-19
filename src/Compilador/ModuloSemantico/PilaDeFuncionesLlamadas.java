package Compilador.ModuloSemantico;

import java.util.Stack;

public class PilaDeFuncionesLlamadas {
    private static Stack<String> llamados = new Stack<>();

    public static void iniciarLlamada(String funcion) {
        llamados.push(funcion);
    }
    public static String getTopeActual() {
        return llamados.peek();
    }

    public static void finalizarLlamada() {
        llamados.pop();
    }
}