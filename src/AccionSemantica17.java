import java.io.IOException;

public class AccionSemantica17 implements AccionSemantica{
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        puntero.retroceder();
        AnalizadorLexico.palabrasReservadasEncontradas.add(lexema.toString());
    }
}
