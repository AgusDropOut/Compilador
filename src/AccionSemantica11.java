import java.io.IOException;

public class AccionSemantica11 implements AccionSemantica{
    @Override
    public void realizar(String codigoFuente, Puntero puntero, StringBuilder lexema, TablaDeSimbolos tablaDeSimbolos) throws IOException {
        System.out.println(this.getClass().getSimpleName());
        puntero.retroceder();
        double valor = Double.parseDouble(lexema.toString().replace("D", "E"));


        if ((valor > Double.MIN_NORMAL && valor < Double.MAX_VALUE) || (valor < -Double.MIN_NORMAL && valor > -Double.MAX_VALUE)){
            tablaDeSimbolos.addSimbolo(lexema.toString());
            AnalizadorLexico.palabrasReservadasEncontradas.add(lexema.toString());
            //Añadir devolver CTE + puntero a TS
        } else {
            System.out.println("Error: El numero " + lexema.toString() + " esta fuera del rango permitido para el sufijo double(D)" );
        }


    }
}
