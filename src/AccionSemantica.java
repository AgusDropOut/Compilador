import java.io.IOException;

public interface AccionSemantica {
    void realizar(String codigoFuente,Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException;
}
