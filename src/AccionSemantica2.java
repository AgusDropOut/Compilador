import java.awt.event.ActionListener;
import java.io.IOException;

public class AccionSemantica2 implements AccionSemantica {
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.append(c);
        //System.out.println("AccionSemantica2 realizar");
    }
}
