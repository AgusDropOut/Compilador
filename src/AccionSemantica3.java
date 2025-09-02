import java.io.IOException;

public class AccionSemantica3 implements AccionSemantica {

    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        puntero.retroceder();
        if (PalabrasReservadas.existe(lexema.toString())){
            AnalizadorLexico.palabrasReservadasEncontradas.add(lexema.toString());
            System.out.println("Palabras reservadas: " + lexema.toString());
            lexema.setLength(0);
        } else {
            System.out.println("No se encontro la palabra reservada");
            lexema.setLength(0);
        }
        //System.out.println("AccionSemantica3 realizar");
    }
}
