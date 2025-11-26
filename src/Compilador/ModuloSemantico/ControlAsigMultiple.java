package Compilador.ModuloSemantico;

import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloSintactico.Parser;
import Compilador.Util.RecolectorDeErrores;

import java.util.Stack;

/*public class ControlAsigMultiple {
    private static Stack<String> pilaTiposIzq = new Stack<>();
    private static Stack<String> pilaTiposDer = new Stack<>();
    private static int contadorIzq = 0;
    private static int contadorDer = 0;

    public ControlAsigMultiple() {
    }

    public static void pushTipoIzq(String tipo) {
        pilaTiposIzq.push(tipo);
        contadorIzq++;
    }

    public static void pushTipoDer(String tipo) {
        pilaTiposDer.push(tipo);
        contadorDer++;
    }

    public static void compararTipos(){
        if (contadorIzq > contadorDer) {
            System.out.println("Error: No pueden haber más elementos del lado izquierdo que del lado derecho en una asignación múltiple.");
        }

        while (!pilaTiposIzq.isEmpty()) {
            String tipoIzq = pilaTiposIzq.pop();
            String tipoDer = pilaTiposDer.pop();
            System.out.println("Comparando tipos: " + tipoIzq + " con " + tipoDer);
            if (!tipoIzq.equals(tipoDer)) {
                System.out.println("Error de tipo en asignación múltiple: no se puede asignar " + tipoDer + " a " + tipoIzq);
            }
        }
        if(!pilaTiposDer.isEmpty()){
            while(!pilaTiposDer.isEmpty()){
                pilaTiposDer.pop();
            }
        }
    }
}*/

// java

import java.util.ArrayList;
import java.util.List;

public class ControlAsigMultiple {
    private static final List<String> listaTiposIzq = new ArrayList<>();
    private static final List<String> listaTiposDer = new ArrayList<>();

    public ControlAsigMultiple() {}

    public static void pushTipoIzq(String tipo) {
        listaTiposIzq.add(tipo);
    }

    public static void pushTipoDer(String tipo) {
        listaTiposDer.add(tipo);
    }

    public static void compararTipos(){
        int cantElemsIzq = listaTiposIzq.size();
        int cantElemsDer = listaTiposDer.size();
        int nMin = Math.min(cantElemsIzq, cantElemsDer);

        for (int i = 0; i < nMin; i++) {
            String tipoIzq = listaTiposIzq.get(i);
            String tipoDer = listaTiposDer.get(i);
            System.out.println("Comparando tipos: " + tipoIzq + " con " + tipoDer);
            if (!tipoIzq.equals(tipoDer)) {
                RecolectorDeErrores.agregarError("Error de tipo en asignación múltiple: no se puede asignar " + tipoDer + " a " + tipoIzq, AnalizadorLexico.getNumeroDeLinea());
            }
        }

        if (cantElemsIzq > cantElemsDer) {
            RecolectorDeErrores.agregarError("Error: número mayor de variables del lado izquierdo en asignación múltiple (cant_izq: " + cantElemsIzq + " cant_der: " + cantElemsDer + ")",AnalizadorLexico.getNumeroDeLinea());
        }

        if (cantElemsIzq < cantElemsDer) {
            RecolectorDeErrores.agregarWarning("Warning: menor cantidad de elementos del lado izquierdo en asignación multiple, se descartaran los elementos adicionales del lado derecho.", AnalizadorLexico.getNumeroDeLinea());
        }

        // limpiar listas
        listaTiposIzq.clear();
        listaTiposDer.clear();
    }
}

