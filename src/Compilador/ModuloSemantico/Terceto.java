package Compilador.ModuloSemantico;

public class Terceto {
    private String operador;
    private String op1;
    private String op2;

    // los operandos pueden ser referencias a la tabla de simbolos (lexemas) o referencia a otro terceto

    public Terceto(String operador, String op1, String op2) {
        this.operador = operador;
        this.op1 = op1;
        this.op2 = op2;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public String getOp1() {
        return op1;
    }

    public String getOp2() {
        return op2;
    }

    public String getOperador() {
        return operador;
    }
}
