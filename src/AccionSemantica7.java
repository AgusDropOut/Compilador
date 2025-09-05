import java.io.IOException;

public class AccionSemantica7 implements AccionSemantica{

    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.append(c);
        //System.out.println("AccionSemantica2 realizar");

    }
}
