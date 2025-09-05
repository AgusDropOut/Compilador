import java.io.IOException;

public class AccionSemantica16 implements AccionSemantica{
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.append(c);
        AnalizadorLexico.palabrasReservadasEncontradas.add(lexema.toString());
    }
}
