import java.io.IOException;

public class AccionSemantica4 implements AccionSemantica{

    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        puntero.retroceder();

        if (tablaDeSimbolos.estaSimbolo(lexema.toString())){
            AnalizadorLexico.palabrasReservadasEncontradas.add(lexema.toString());
            System.out.println("Identificador leido: " + lexema.toString());
            lexema.setLength(0);
        } else {
            System.out.println("No se encontro el identificador, dar de alta");
            tablaDeSimbolos.addSimbolo(lexema.toString());
            AnalizadorLexico.palabrasReservadasEncontradas.add(lexema.toString());
            System.out.println("Identificador leido: " + lexema.toString());
            lexema.setLength(0);
        }
    }
}
