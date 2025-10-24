package Compilador.ModuloSintactico;

public class ParserValExt extends ParserVal {
    public String tipo;

    public ParserValExt() {
        super();
        this.tipo = "desconocido";
    }

    public ParserValExt(int val) {
        super(val);
        this.tipo = "desconocido";
    }

    public ParserValExt(double val) {
        super(val);
        this.tipo = "desconocido";
    }

    public ParserValExt(String val) {
        super(val);
        this.tipo = "desconocido";
    }

    public ParserValExt(Object val) {
        super(val);
        this.tipo = "desconocido";
    }
}
