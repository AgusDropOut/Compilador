import java.io.IOException;


public class AccionSemantica1 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.setLength(0);
        lexema.append(c);
        //System.out.println("AccionSemantica1 realizar");
    }
}
