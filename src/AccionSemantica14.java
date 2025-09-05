import java.io.IOException;

public class AccionSemantica14 implements AccionSemantica{
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        char c = codigoFuente.charAt(puntero.getPuntero());
        lexema.setLength(0);
        lexema.append(c);
        AnalizadorLexico.palabrasReservadasEncontradas.add(lexema.toString());
    }
}
