package Compilador.ModuloLexico;

public class ElementoTablaDeSimbolos {
    private String valor;
    private String tipo;
    private String ambito;
    private String uso;

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
    public void setValor(String valor) {
        this.valor = valor;
    }
    public String getValor() {
        return valor;
    }
    public void setUso(String uso) {
        this.uso = uso;
    }
    public String getUso() {
        return uso;
    }

    @Override
    public String toString() {
        return "ElementoTablaDeSimbolos{" +
                "valor='" + valor + '\'' +
                ", tipo=" + tipo +
                ", uso=" + uso +
                '}';
    }
}
