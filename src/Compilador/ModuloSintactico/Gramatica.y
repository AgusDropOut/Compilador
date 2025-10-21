%{
package Compilador.ModuloSintactico;

import java.io.*;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.ModuloLexico.ElementoTablaDeSimbolos;
%}

%left '+' '-'
%left '*' '/'



%token WHILE  IF  ELSE  ENDIF  PRINT  RETURN  DO  CTE  ID  ASIG  TRUNC  CR  ULONG  COMP  CADENA FLECHA

%%
programa              : ID '{' list_sentencia '}' { }
                      |  '{' list_sentencia '}' { yyerror("Error: Falta definir un nombre al programa"); }
                      | ID  list_sentencia '}' { yyerror("Error: Falta delimitador del programa '{' al inicio"); }
                      | ID  list_sentencia  { yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
                      ;

list_sentencia        : /* empty */
                      | list_sentencia sentencia
                      ;

declaracion_funcion   : header_funcion '(' parametros_formales ')' '{' list_sentencia '}' { reportarEstructura("declaracion de funcion");
                                                                                            salirAmbito();}
                      ;

header_funcion        : tipo ID {entrarAmbito($2.sval); System.out.println(ambito);}
                      | tipo error { yyerror("Error: Falta definir un nombre a la función"); }
                      ;


sentencia             : sentencia_declarativa ';'
                      | sentencia_ejecutable ';'
                      | declaracion_funcion
                      | sentencia_declarativa error { yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
                      | sentencia_ejecutable error { yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
                      | error ';' {yyerror("Error: Sentencia invalida");}
                      ;

sentencia_declarativa : tipo list_vars { reportarEstructura("declaracion de variable(s)"); }
                      ;

parametros_formales   : parametro_formal
                      | parametros_formales ',' parametro_formal
                      ;

parametro_formal      : semantica tipo ID
                      | tipo ID
                      | semantica tipo error { yyerror("Error: Falta definir el nombre del parametro formal"); }
                      | tipo error { yyerror("Error: Falta definir el nombre del parametro formal"); }
                      | semantica error ID { yyerror("Error: Falta definir el tipo del parametro formal"); }
                      | error ID { yyerror("Error: Falta definir el tipo del parametro formal"); }
                      ;

semantica             : CR
                      ;

sentencia_return      : RETURN expresion ';'
                      | RETURN expresion error { yyerror("Sentencia no reconocida, se esperaba ';'"); }
                      ;

tipo                  : ULONG {tipo = "ulong";}
                      ;

list_ctes             : CTE
                      | list_ctes ',' CTE
                      | list_ctes CTE { yyerror("Error: se esperaba ',' entre constantes"); }
                      ;

list_vars             : ID {$$ = declaracionDeVariable($1.sval, tipo, ambito, "Variable");}
                      | list_vars ',' ID {$$ = declaracionDeVariable($3.sval, tipo, ambito, "Variable");}
                      | ID '.' ID {}
                      | list_vars ',' ID '.' ID
                      | list_vars ID { yyerror("Error: se esperaba ',' entre variables"); }
                      | list_vars ID '.' ID { yyerror("Error: se esperaba ',' entre variables"); }
                      ;



sentencia_ejecutable  : /*1*/IF condicion_if_while '{' bloque_ejecutable '}' ELSE '{' bloque_ejecutable '}' end_if { reportarEstructura("IF"); }
                      | /*2*/IF condicion_if_while '{' bloque_ejecutable '}' end_if { reportarEstructura("IF"); }
                      | /*3*/IF condicion_if_while sentencia_ejecutable ';' ELSE sentencia_ejecutable ';' end_if { reportarEstructura("IF"); }
                      | /*4*/IF condicion_if_while sentencia_ejecutable ';' end_if { reportarEstructura("IF"); }
                      | /*5*/IF condicion_if_while '{' bloque_ejecutable '}' ELSE sentencia_ejecutable ';' end_if { reportarEstructura("IF"); }
                      | /*6*/IF condicion_if_while sentencia_ejecutable ';' ELSE '{' bloque_ejecutable '}' end_if { reportarEstructura("IF"); }
                      | PRINT '(' CADENA ')' { reportarEstructura("PRINT"); }
                      | PRINT '(' expresion ')' { reportarEstructura("PRINT"); }
                      | asignacion_simple
                      | asignacion_multiple
                      | sentencia_return
                      | WHILE condicion_if_while DO '{' bloque_ejecutable '}' { reportarEstructura("WHILE"); }
                      | WHILE condicion_if_while DO sentencia_ejecutable { reportarEstructura("WHILE"); }
                      | WHILE condicion_if_while DO  { yyerror("Error: falta cuerpo del WHILE");  }
                      | WHILE condicion_if_while DO '{'  '}' { yyerror("Error: falta cuerpo del WHILE");  }
                      | PRINT '('  ')' { yyerror("Error: falta argumento dentro del print"); }
                      /* */
                      | WHILE condicion_if_while '{' bloque_ejecutable '}'  { yyerror("Error: falta palabra reservada DO");  }
                      | WHILE condicion_if_while sentencia_ejecutable { yyerror("Error: falta palabra reservada DO");  }
                      /* */
                      | /*1*/IF condicion_if_while '{'  '}' ELSE '{' bloque_ejecutable '}' end_if {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*1*/IF condicion_if_while '{' bloque_ejecutable '}' ELSE '{'  '}' end_if {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*1*/IF condicion_if_while '{'  '}' ELSE '{'  '}' end_if                  {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*2*/IF condicion_if_while '{'  '}' end_if                                {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*3*/IF condicion_if_while  ELSE sentencia_ejecutable ';' end_if          {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*3*/IF condicion_if_while sentencia_ejecutable ';' ELSE   end_if         {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*3*/IF condicion_if_while   ELSE   end_if                                {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*4*/IF condicion_if_while  end_if                                        {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*5*/IF condicion_if_while '{'  '}' ELSE sentencia_ejecutable ';' end_if  {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*5*/IF condicion_if_while '{' bloque_ejecutable '}' ELSE  end_if         {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*5*/IF condicion_if_while '{'  '}' ELSE  end_if                          {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*6*/IF condicion_if_while  ELSE '{' bloque_ejecutable '}' end_if         {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*6*/IF condicion_if_while sentencia_ejecutable ';' ELSE '{'  '}' end_if  {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*6*/IF condicion_if_while  ELSE '{'  '}' end_if
                      ;



end_if                : ENDIF
                      | /* empty */ {yyerror("Error: falta palabra reservada 'endif'");}
                      ;

condicion_if_while    : '(' condicion ')'
                      | condicion ')' {yyerror("Error: falta parentesis de apertura '(' en condicion");}
                      | '(' condicion {yyerror("Error: falta parentesis de cierre ')' en condicion");}
                      | condicion {yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
                      ;




bloque_ejecutable     : sentencia_ejecutable ';'
                      | bloque_ejecutable  sentencia_ejecutable ';'
                      | bloque_ejecutable error { yyerror("Sentencia no reconocida, se esperaba ';'"); }
                      | sentencia_ejecutable error { yyerror("Sentencia no reconocida, se esperaba ';'"); }
                      | bloque_ejecutable sentencia_ejecutable error { yyerror("Sentencia no reconocida, se esperaba ';'"); }
                      ;


parametros_reales     : parametro_real
                      | parametros_reales ',' parametro_real
                      | error { yyerror("Error: Declaracion de parametro real invalida"); }
                      ;

parametro_real        : expresion FLECHA ID
                      | expresion FLECHA error { yyerror("Error: Falta definir el nombre del parametro formal"); }
                      | expresion ID { yyerror("Error: Falta '->' en la especificacion de parametro real"); }
                      ;

condicion             : expresion COMP expresion
                      ;

asignacion_simple     : ID ASIG expresion { reportarEstructura("asignacion simple"); }
                      | ID '.' ID ASIG expresion { reportarEstructura("asignacion simple");
                                                   $$ = chequearAmbito($1.sval, ambito, $3.sval); }
                      ;

asignacion_multiple   : list_vars '=' list_ctes { reportarEstructura("asignacion multiple"); }
                      ;


expresion_lambda      : '(' tipo ID ')' '{' bloque_ejecutable '}' '(' factor ')' { reportarEstructura("expresion lambda"); }
                      | '(' tipo ID ')'  bloque_ejecutable '}' '(' factor ')' { yyerror("Error: falta '{' en la expresion lambda"); }
                      | '(' tipo ID ')' '{' bloque_ejecutable  '(' factor ')' { yyerror("Error: falta '}' en la expresion lambda"); }
                      | '(' tipo ID ')'  bloque_ejecutable '('  factor ')'  { yyerror("Error: falta '{' y '}' en la expresion lambda"); }
                      ;

expresion             : expresion '+' termino
                      | expresion '-' termino
                      | termino
                      | error '+' termino { yyerror("Error: operando a la izquierda invalido"); }
                      | expresion '+' error { yyerror("Error: operando a la derecha invalido"); }
                      | error '-' termino { yyerror("Error: operando a la izquierda invalido"); }
                      | expresion '-' error { yyerror("Error: operando a la derecha invalido"); }
                      | error '+' error { yyerror("Error: operandos a la izquierda y derecha invalidos"); }
                      | error '-' error { yyerror("Error: operandos a la izquierda y derecha invalidos"); }
                      | TRUNC '(' expresion ')'
                      | TRUNC '(' expresion error { yyerror("Error: falta ')' en la expresion TRUNC"); }
                      | TRUNC error  expresion ')' { yyerror("Error: falta '(' en la expresion TRUNC"); }
                      | TRUNC error expresion error { yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
                      | expresion_lambda
                      ;




termino               : termino '*' factor
                      | termino '/' factor
                      | error '*' factor { yyerror("Error: operando a la izquierda invalido"); }
                      | termino '*' error { yyerror("Error: operando a la derecha invalido"); }
                      | error '/' factor { yyerror("Error: operando a la izquierda invalido"); }
                      | termino '/' error { yyerror("Error: operando a la derecha invalido"); }
                      | error '*' error { yyerror("Error: operandos a la izquierda y derecha invalidos"); }
                      | error '/' error { yyerror("Error: operandos a la izquierda y derecha invalidos"); }
                      | factor
                      ;

factor                : ID
                      | CTE
                      | ID '.' ID
                      | ID '(' parametros_reales ')'
                      |'-' CTE { $$ = constanteNegativa($2); }
                      ;


%%

public void yyerror(String s) {
    System.err.println("Error de sintaxis en línea "
        + AnalizadorLexico.getNumeroDeLinea() + ": " + s);
}

public int yylex() {
    try {
        short returnval = AnalizadorLexico.yylex();
        yylval = AnalizadorLexico.yylval;
        return returnval;
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

public void reportarEstructura(String estructura) {
    System.out.println("Estructura " + estructura + " reconocida en línea "
        + AnalizadorLexico.getNumeroDeLinea());
}

public ParserVal constanteNegativa(ParserVal clave) {
    ElementoTablaDeSimbolos original = TablaDeSimbolos.getSimbolo(clave.sval);

    if (original == null) {
        yyerror("Constante no encontrada en la tabla de símbolos: " + clave.sval);
        return clave;
    }

    double valorNegado = -original.getValor();
    String tipo = original.getTipo();

    if (!estaDentroDeRango(valorNegado, tipo)) {
       yyerror("El número -" + original.getValor() +
                           " está fuera del rango permitido para el tipo " + tipo + ".");
        return clave;
    }

    ElementoTablaDeSimbolos nuevo = new ElementoTablaDeSimbolos();
    nuevo.setValor(valorNegado);
    nuevo.setTipo(tipo);
    nuevo.setUso("cte_negada");

    String nombreNuevo = "-" + clave.sval;
    TablaDeSimbolos.addSimbolo(nombreNuevo, nuevo);

    ParserVal val = new ParserVal();
    val.sval = nombreNuevo;
    return val;
}

public boolean estaDentroDeRango(double valor, String tipo) {
    switch (tipo.toLowerCase()) {
        case "dfloat":
            return (valor >= 2.2250738585072014e-308 && valor <= 1.7976931348623157e308) ||
                   (valor <= -2.2250738585072014e-308 && valor >= -1.7976931348623157e308) ||
                   valor == 0.0;
        case "ulong":
            return false;
        default:
            return true;
    }
}

public static String ambito = "PROGRAMA";
public void entrarAmbito(String ambitoNuevo){
    ambito = ambito+":"+ambitoNuevo;
}
public void salirAmbito(){
    int indiceUltimoDosPuntos = ambito.lastIndexOf(":");

    if (indiceUltimoDosPuntos > 0) {
        ambito = ambito.substring(0, indiceUltimoDosPuntos);
    }

}
public static String tipo;
public ParserVal declaracionDeVariable(String token, String tipo, String ambito, String uso){
    String nombreNuevo = token + ":" + ambito;
    ParserVal nuevoParserVal = new ParserVal();
    if (!TablaDeSimbolos.estaSimbolo(nombreNuevo)) {
        ElementoTablaDeSimbolos nuevoElem = new ElementoTablaDeSimbolos();
        nuevoElem.setTipo(tipo);
        nuevoElem.setAmbito(ambito);
        nuevoElem.setUso(uso);
        TablaDeSimbolos.addSimbolo(nombreNuevo, nuevoElem);
        nuevoParserVal.sval = nombreNuevo;
    } else {
        yyerror("Error: Redeclaracion de variable " + token);
        nuevoParserVal.sval = token;
    }
    return nuevoParserVal;
}

// ---- FUNCION PRINCIPAL ----
public ParserVal chequearAmbito(String prefijo, String ambitoReal, String nombreIdentificador) {
    ElementoTablaSimbolos elem = null;
    String claveBuscada = null;

    // 1️⃣ Si hay prefijo explícito (p. ej. objeto.campo)
    if (prefijo != null && !prefijo.isEmpty()) {
        claveBuscada = prefijo + ":" + nombreIdentificador;
        elem = tablaSimbolos.get(claveBuscada);

        if (elem == null) {
            yyerror("El símbolo '" + nombreIdentificador +
                    "' no se encuentra en el ámbito del prefijo '" + prefijo + "'.");
            return null;
        }
    }
    else {
        // 2️⃣ Sin prefijo → buscamos en la jerarquía PROG:FUNC:BLOCK:...
        String[] niveles = ambitoReal.split(":");

        for (int i = niveles.length; i > 0 && elem == null; i--) {
            String ambitoParcial = String.join(":", Arrays.copyOfRange(niveles, 0, i));
            claveBuscada = ambitoParcial + ":" + nombreIdentificador;
            elem = tablaSimbolos.get(claveBuscada);
        }

        // 3️⃣ Intentar ámbito global si no se encontró en ninguno
        if (elem == null)
            elem = tablaSimbolos.get(nombreIdentificador);

        if (elem == null) {
            yyerror("El símbolo '" + nombreIdentificador +
                    "' no fue declarado en ningún ámbito visible (actual: " + ambitoReal + ").");
            return null;
        }
    }

    // 4️⃣ Construir un ParserVal asociado al símbolo encontrado
    ParserVal val = new ParserVal();
    val.sval = claveBuscada;      // Nombre mangleado completo
    val.obj  = elem;              // Referencia al elemento de la tabla
    return val;
}