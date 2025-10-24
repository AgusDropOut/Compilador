%{

package Compilador.ModuloSintactico;
import java.io.*;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.ModuloLexico.ElementoTablaDeSimbolos;
import Compilador.ModuloSemantico.ArregloTercetos;
import Compilador.ModuloSemantico.ControlAsigMultiple;
import java.util.Stack;

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
                                                                                            salirAmbito();
                                                                                            chequearReturn();
                                                                                          }
                      ;

header_funcion        : tipo ID {
                                 declaracionDeFuncion($2.sval, ambito, "Función");
                                 entrarAmbito($2.sval);
                                 pilaReturns.push($2.sval + ":" + "false");
                                }
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

sentencia_return      : RETURN expresion  {registrarReturn();}
                      ;

tipo                  : ULONG {tipo = "ulong";}
                      ;

list_ctes             : CTE {$$.tipo = obtenerTipoDeSimbolo($1.sval); ControlAsigMultiple.pushTipoDer($$.tipo);}
                      | list_ctes ',' CTE { $$.tipo = $1.tipo; ControlAsigMultiple.pushTipoDer(obtenerTipoDeSimbolo($3.sval)); }
                      | list_ctes CTE { yyerror("Error: se esperaba ',' entre constantes"); }
                      ;

list_vars_mix         : ID {$$ = chequearAmbito("", ambito, $1.sval); $$.tipo = obtenerTipoDeSimbolo($$.sval); ControlAsigMultiple.pushTipoIzq($$.tipo); }
                      | list_vars_mix ',' ID {$$ = chequearAmbito("", ambito, $3.sval); $$.tipo = obtenerTipoDeSimbolo($$.sval); ControlAsigMultiple.pushTipoIzq($$.tipo);}
                      | ID '.' ID { $$ = chequearAmbito($1.sval, ambito, $3.sval);  $$.tipo = obtenerTipoDeSimbolo($$.sval); ControlAsigMultiple.pushTipoIzq($$.tipo);}
                      | list_vars_mix ',' ID '.' ID { $$ = chequearAmbito($3.sval, ambito, $5.sval); $$.tipo = obtenerTipoDeSimbolo($$.sval); ControlAsigMultiple.pushTipoIzq($$.tipo);}
                      | list_vars_mix ID { yyerror("Error: se esperaba ',' entre variables"); }
                      | list_vars_mix ID '.' ID { yyerror("Error: se esperaba ',' entre variables"); }
                      ;

list_vars             : ID {$$ = declaracionDeVariable($1.sval, tipo, ambito, "Variable");}
                      | list_vars ',' ID {$$ = declaracionDeVariable($3.sval, tipo, ambito, "Variable");}
                      | list_vars ID { yyerror("Error: se esperaba ',' entre variables"); }
                      ;



sentencia_ejecutable  : /*1*/IF condicion_if '{' bloque_ejecutable '}' else '{' bloque_ejecutable '}' end_if { reportarEstructura("IF"); }
                      | /*2*/IF condicion_if '{' bloque_ejecutable '}' end_if { reportarEstructura("IF"); }
                      | /*3*/IF condicion_if sentencia_ejecutable ';' else sentencia_ejecutable ';' end_if { reportarEstructura("IF"); }
                      | /*4*/IF condicion_if sentencia_ejecutable ';' end_if { reportarEstructura("IF"); }
                      | /*5*/IF condicion_if '{' bloque_ejecutable '}' else sentencia_ejecutable ';' end_if { reportarEstructura("IF"); }
                      | /*6*/IF condicion_if sentencia_ejecutable ';' else '{' bloque_ejecutable '}' end_if { reportarEstructura("IF"); }
                      | PRINT '(' CADENA ')' { reportarEstructura("PRINT"); }
                      | PRINT '(' expresion ')' { reportarEstructura("PRINT"); }
                      | asignacion_simple
                      | asignacion_multiple
                      | sentencia_return
                      | while condicion_while DO '{' bloque_ejecutable '}' { reportarEstructura("WHILE"); $$ = ArregloTercetos.completarBackPatchingWHILE(); }
                      | while condicion_while DO sentencia_ejecutable { reportarEstructura("WHILE"); $$ = ArregloTercetos.completarBackPatchingWHILE(); }
                      | while condicion_while DO  { yyerror("Error: falta cuerpo del WHILE");   }
                      | while condicion_while DO '{'  '}' { yyerror("Error: falta cuerpo del WHILE");  }
                      | PRINT '('  ')' { yyerror("Error: falta argumento dentro del print"); }
                      /* */
                      | while condicion_while '{' bloque_ejecutable '}'  { yyerror("Error: falta palabra reservada DO");  }
                      | while condicion_while sentencia_ejecutable { yyerror("Error: falta palabra reservada DO");  }
                      /* */
                      | /*1*/IF condicion_if '{'  '}' else '{' bloque_ejecutable '}' end_if {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*1*/IF condicion_if '{' bloque_ejecutable '}' else '{'  '}' end_if {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*1*/IF condicion_if '{'  '}' else '{'  '}' end_if                  {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*2*/IF condicion_if '{'  '}' end_if                                {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*3*/IF condicion_if  else sentencia_ejecutable ';' end_if          {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*3*/IF condicion_if sentencia_ejecutable ';' else   end_if         {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*3*/IF condicion_if   else   end_if                                {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*4*/IF condicion_if  end_if                                        {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*5*/IF condicion_if '{'  '}' else sentencia_ejecutable ';' end_if  {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*5*/IF condicion_if '{' bloque_ejecutable '}' else  end_if         {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*5*/IF condicion_if '{'  '}' else  end_if                          {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*6*/IF condicion_if  else '{' bloque_ejecutable '}' end_if         {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*6*/IF condicion_if sentencia_ejecutable ';' else '{'  '}' end_if  {yyerror("Error: Falta contenido en bloque then/else");}
                      | /*6*/IF condicion_if  else '{'  '}' end_if
                      ;

else                  : ELSE {ArregloTercetos.crearTercetoBackPatchingIFDesapilaryCompletar("bl", null,null);}
                      ;

while                 : WHILE { ArregloTercetos.apilarTercetoInicialWHILE(); }
                      ;




end_if                : ENDIF {ArregloTercetos.completarTercetoBackPatchingIF();}
                      | /* empty */ {yyerror("Error: falta palabra reservada 'endif'");}
                      ;

condicion_if          : '(' condicion ')' {ArregloTercetos.crearTercetoBackPatchingIF("bf", $2.sval,null);}
                      | condicion ')' {yyerror("Error: falta parentesis de apertura '(' en condicion");}
                      | '(' condicion {yyerror("Error: falta parentesis de cierre ')' en condicion");}
                      | condicion {yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
                      ;

condicion_while       : '(' condicion ')' {ArregloTercetos.crearTercetoBackPatchingWHILE("bf", $2.sval,null);}
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

condicion             : expresion COMP expresion {$$ = ArregloTercetos.crearTerceto("COMP", $1.sval, $3.sval); $$.tipo = chequearTipos($1.tipo, $3.tipo); }
                      ;

asignacion_simple     : var_asignacion_simple ASIG expresion { reportarEstructura("asignacion simple");
                                                               $$ = ArregloTercetos.crearTerceto(":=", $1.sval, $3.sval);
                                                               $$.tipo = chequearTipos($1.tipo, $3.tipo);
                                                               }
                      ;

var_asignacion_simple : ID {$$ = chequearAmbito("", ambito, $1.sval); $$.tipo = obtenerTipoDeSimbolo($$.sval); }
                      | ID '.' ID {$$ = chequearAmbito($1.sval, ambito, $1.sval); $$.tipo = obtenerTipoDeSimbolo($$.sval); }
                      ;

asignacion_multiple   : list_vars_mix '=' list_ctes { reportarEstructura("asignacion multiple"); ControlAsigMultiple.compararTipos();}
                      ;


expresion_lambda      : '(' tipo ID ')' '{' bloque_ejecutable '}' '(' factor ')' { reportarEstructura("expresion lambda"); }
                      | '(' tipo ID ')'  bloque_ejecutable '}' '(' factor ')' { yyerror("Error: falta '{' en la expresion lambda"); }
                      | '(' tipo ID ')' '{' bloque_ejecutable  '(' factor ')' { yyerror("Error: falta '}' en la expresion lambda"); }
                      | '(' tipo ID ')'  bloque_ejecutable '('  factor ')'  { yyerror("Error: falta '{' y '}' en la expresion lambda"); }
                      ;

expresion             : expresion '+' termino {$$ = ArregloTercetos.crearTerceto("+", $1.sval, $3.sval); $$.tipo = chequearTipos($1.tipo, $3.tipo); }
                      | expresion '-' termino {$$ = ArregloTercetos.crearTerceto("-", $1.sval, $3.sval); $$.tipo = chequearTipos($1.tipo, $3.tipo);}
                      | termino {$$ = $1; $$.tipo = $1.tipo;}
                      | error '+' termino { yyerror("Error: operando a la izquierda invalido"); }
                      | expresion '+' error { yyerror("Error: operando a la derecha invalido"); }
                      | error '-' termino { yyerror("Error: operando a la izquierda invalido"); }
                      | expresion '-' error { yyerror("Error: operando a la derecha invalido"); }
                      | error '+' error { yyerror("Error: operandos a la izquierda y derecha invalidos"); }
                      | error '-' error { yyerror("Error: operandos a la izquierda y derecha invalidos"); }
                      | TRUNC '(' expresion ')' {$$.tipo = "ulong";}
                      | TRUNC '(' expresion error { yyerror("Error: falta ')' en la expresion TRUNC"); }
                      | TRUNC error  expresion ')' { yyerror("Error: falta '(' en la expresion TRUNC"); }
                      | TRUNC error expresion error { yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
                      | expresion_lambda
                      ;




termino               : termino '*' factor {$$ = ArregloTercetos.crearTerceto("*", $1.sval, $3.sval); $$.tipo = chequearTipos($1.tipo, $3.tipo); }
                      | termino '/' factor {$$ = ArregloTercetos.crearTerceto("/", $1.sval, $3.sval); $$.tipo = chequearTipos($1.tipo, $3.tipo);}
                      | error '*' factor { yyerror("Error: operando a la izquierda invalido"); }
                      | termino '*' error { yyerror("Error: operando a la derecha invalido"); }
                      | error '/' factor { yyerror("Error: operando a la izquierda invalido"); }
                      | termino '/' error { yyerror("Error: operando a la derecha invalido"); }
                      | error '*' error { yyerror("Error: operandos a la izquierda y derecha invalidos"); }
                      | error '/' error { yyerror("Error: operandos a la izquierda y derecha invalidos"); }
                      | factor {$$ = $1; $$.tipo = $1.tipo;}
                      ;

factor                : ID {$$ = chequearAmbito("", ambito, $1.sval); $$.tipo = obtenerTipoDeSimbolo($$.sval); }
                      | CTE {$$.tipo = obtenerTipoDeSimbolo($1.sval); $$ = $1; }
                      | ID '.' ID { $$ = chequearAmbito($1.sval, ambito, $3.sval); $$.tipo = obtenerTipoDeSimbolo($$.sval); }
                      | ID '(' parametros_reales ')' {$$ = chequearAmbito("", ambito, $1.sval); $$.tipo = obtenerTipoDeSimbolo($$.sval);}
                      |'-' CTE { $$ = constanteNegativa($2); $$.tipo = obtenerTipoDeSimbolo($$.sval);}
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

public ParserValExt constanteNegativa(ParserValExt clave) {
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

    ParserValExt val = new ParserValExt();
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
public ParserValExt declaracionDeVariable(String token, String tipo, String ambito, String uso){
    String nombreNuevo = token + ":" + ambito;
    ParserValExt nuevoParserValExt = new ParserValExt();
    if (!TablaDeSimbolos.estaSimbolo(nombreNuevo)) {
        ElementoTablaDeSimbolos nuevoElem = new ElementoTablaDeSimbolos();
        nuevoElem.setTipo(tipo);
        nuevoElem.setAmbito(ambito);
        nuevoElem.setUso(uso);
        TablaDeSimbolos.addSimbolo(nombreNuevo, nuevoElem);
        nuevoParserValExt.sval = nombreNuevo;
    } else {
        yyerror("Error: Redeclaracion de variable " + token + " en el ambito " + ambito);
        nuevoParserValExt.sval = token;
    }
    return nuevoParserValExt;
}

public ParserValExt chequearAmbito(String prefijo, String ambitoReal, String nombreIdentificador) {
    ElementoTablaDeSimbolos elem = null;
    String claveBuscada = null;
    String ambitoDeBusqueda = ambito; // copia de la variable global ambito
    boolean simboloEncontrado = false;
    String ambitoActual = "";
    ParserValExt val = new ParserValExt();
    val.sval = nombreIdentificador; // Valor por defecto si no se encuentra

    if (!prefijo.isEmpty()) {
        while (!simboloEncontrado && !ambitoDeBusqueda.isEmpty()) {
            int pos = ambitoDeBusqueda.lastIndexOf(":");
            System.out.println("ambito: " + ambitoDeBusqueda);

            if (pos == -1) {
                ambitoActual = ambitoDeBusqueda;
            } else {
                ambitoActual = ambitoDeBusqueda.substring(pos + 1);
            }

            if (ambitoActual.equals(prefijo)) {
                claveBuscada = nombreIdentificador + ":" + ambitoDeBusqueda;
                elem = TablaDeSimbolos.getSimbolo(claveBuscada);
                simboloEncontrado = true;

                if (elem == null) {
                    yyerror("El símbolo '" + nombreIdentificador +
                        "' no se encuentra en el ámbito del prefijo '" + prefijo + "'.");
                } else {
                    val.sval = claveBuscada;
                }
            }

            // Evita StringIndexOutOfBoundsException
            if (pos == -1) {
            yyerror("El símbolo '" + nombreIdentificador +
                                    "' no se encuentra en el ámbito del prefijo '" + prefijo + "'.");
                ambitoDeBusqueda = ""; // ya no hay más niveles
            } else {
                ambitoDeBusqueda = ambitoDeBusqueda.substring(0, pos);
            }
        }
    } else {
        claveBuscada = nombreIdentificador + ":" + ambitoDeBusqueda;
        elem = TablaDeSimbolos.getSimbolo(claveBuscada);
        if (elem == null) {
            yyerror("El símbolo '" + nombreIdentificador +
                "' no se encuentra en el ámbito actual '" + ambito + "'.");
        } else {
            val.sval = claveBuscada;
        }
    }

    return val;
}

public void declaracionDeFuncion(String token, String ambito, String uso) {
   //Comprobamos si ya existe un token con ese nombre en la TS
    ElementoTablaDeSimbolos original = TablaDeSimbolos.getSimbolo(token);

    if (original != null) {
        String usoOriginal = original.getUso();
        // Si ya existe y su uso es "Funcion" => redeclaración
        if ("Función".equals(usoOriginal)) {
            yyerror("Error: Redeclaracion de función " + token);
            return;
        } else {
            // Existe otro símbolo con el mismo nombre (variable/otro) => lo tratamos como redeclaración
            yyerror("Error: Nombre ya utilizado por otro símbolo: " + token);
            return;
        }
    }

    // No existe, se añade como función
    ElementoTablaDeSimbolos nuevoElem = new ElementoTablaDeSimbolos();
    nuevoElem.setTipo(tipo);
    nuevoElem.setAmbito(ambito);
    nuevoElem.setUso("Función");
    TablaDeSimbolos.addSimbolo(token + ":" + ambito, nuevoElem);
}

public static Stack<String> pilaReturns = new Stack<>();

//Chequear que, al final de la función, se haya declarado los returns
 public void chequearReturn() {
        if (!pilaReturns.isEmpty()) {
            String top = pilaReturns.pop();
            String[] partes = top.split(":");
            String nombreFunc = partes[0];
            boolean tieneReturn = Boolean.parseBoolean(partes[1]);

            if (!tieneReturn) {
                yyerror("Error: la función " + nombreFunc + " no tiene sentencia return.");
            }
        }
        else {
          yyerror("Error: Pila de returns vacía. Declaración de múltiples return en mismo bloque de código.");
        }
 }

//Cuando se encuentra con un return, modificar su estado a true
public void registrarReturn() {
        if (!pilaReturns.isEmpty()) {
            String top = pilaReturns.pop();
            String[] partes = top.split(":");
            String nombreFunc = partes[0];
            pilaReturns.push(nombreFunc + ":true");
        } else {
            yyerror("Error: Sentencia return declarada fuera de función");
        }
}

//Función para el chequeo de tipos de variables y constantes
public String chequearTipos(String tipo1, String tipo2){
    if (!tipo1.equals(tipo2)){
        yyerror("Error: Tipos incompatibles para la operación (" + tipo1 + " y " + tipo2 +").");
        return "error";
    }
    else{
        return tipo1;
    }
}

//Función para obtener el tipo de una variable o constante

public String obtenerTipoDeSimbolo(String claveTS) {
    ElementoTablaDeSimbolos elemento = TablaDeSimbolos.getSimbolo(claveTS);
    if (elemento != null) {
        return elemento.getTipo();
    }
    else{
        yyerror("Error: El elemento no existe");
        return "error";
    }
}
