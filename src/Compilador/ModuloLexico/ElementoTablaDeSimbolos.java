package Compilador.ModuloLexico;

public class ElementoTablaDeSimbolos {
    private double valor;
    private String tipo;
    private String ambito;
    private String uso;
    private String semanticaPasaje;

    public ElementoTablaDeSimbolos() {
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }
    public String getAmbito() {
        return ambito;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public String getTipo() {
        return tipo;
    }
    public void setValor(Double valor) {
        this.valor = valor;
    }
    public double getValor() {
        return valor;
    }
    public void setUso(String uso) {
        this.uso = uso;
    }
    public String getUso() {
        return uso;
    }
    public void setSemanticaPasaje(String semanticaPasaje) {
        this.semanticaPasaje = semanticaPasaje;
    }
    public String getSemanticaPasaje() {
        return semanticaPasaje;
    }

    @Override
    public String toString() {
        return "ElementoTablaDeSimbolos{" +
                "valor='" + valor + '\'' +
                ", tipo=" + tipo +
                ", uso=" + uso +
                ", ambito=" + ambito +
                ", semanticaPasaje=" + semanticaPasaje +
                '}';
    }
}
