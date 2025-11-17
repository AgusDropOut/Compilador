package Compilador.ModuloGC;

public class ContadorVarAux {
    public static int  contador;

    public static int getContador() {
        return contador;
    }

    public static int obtenerSiguiente(){
        return ++contador;
    }
}
