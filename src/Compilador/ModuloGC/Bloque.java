package Compilador.ModuloGC;

public class Bloque {
    private int inicio;
    private int fin;
    private int inicioElse;
    private boolean esIfElse;

    public int getInicio() {
        return inicio;
    }
    public int getFin() {
        return fin;
    }
    public int getInicioElse() {
        return inicioElse;
    }
    public void setInicio(int inicio) {
        this.inicio = inicio;
    }
    public void setFin(int fin) {
        this.fin = fin;
    }
    public void setInicioElse(int inicioElse) {
        this.inicioElse = inicioElse;
    }
    public boolean esIfElse() {
        return esIfElse;
    }
    public void setEsIfElse(boolean esIfElse) {
        this.esIfElse = esIfElse;
    }
}
