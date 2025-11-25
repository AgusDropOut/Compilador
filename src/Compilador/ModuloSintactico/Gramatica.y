%{
package Compilador.ModuloSintactico;
import java.io.*;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.ModuloLexico.ElementoTablaDeSimbolos;
import Compilador.ModuloSemantico.ArregloTercetos;
import Compilador.ModuloSemantico.ControlAsigMultiple;
import java.util.Stack;
import Compilador.ModuloSemantico.PilaDeFuncionesLlamadas;
import Compilador.Util.RegistroDeConstantes;
import Compilador.Util.RecolectorDeErrores;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
%}

%left '+' '-'
%left '*' '/'

%token WHILE  IF  ELSE  ENDIF  PRINT  RETURN  DO  CTE  ID  ASIG  TRUNC  CR  ULONG  CADENA FLECHA EQ GEQ LEQ NEQ

%%
programa              : nombre_programa  '{' list_sentencia '}'{ reportarEstructura("Fin del programa");}
                      | nombre_programa  '{' list_sentencia '}' '}'{ yyerror("Error: '}' de mas al final de programa");}
                      | nombre_programa  list_sentencia '}' { yyerror("Error: Falta delimitador del programa '{' al inicio"); }
                      | nombre_programa '{' list_sentencia  { yyerror("Error: Falta delimitador del programa '}' al final"); }
                      | nombre_programa  list_sentencia  { yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
                      | '{' list_sentencia '}' { yyerror("Error: Falta definir el nombre del programa"); }
                      ;

nombre_programa       : ID  {ambito = $1.sval; prog_principal = $1.sval;}
                      ;

declaracion_funcion   : header_funcion '(' parametros_formales ')' '{' list_sentencia '}' { reportarEstructura("declaracion de funcion");
                                                                                            String etiquetaFin = "fin_" + ambito;
                                                                                            ArregloTercetos.crearTerceto(etiquetaFin, "_", "_");
                                                                                            salirAmbito();
                                                                                            chequearReturn();
                                                                                          }
                      ;

header_funcion        : tipo ID  {
                                 declaracionDeFuncion($2.sval, ambito, "Función");
                                 entrarAmbito($2.sval);
                                 pilaReturns.push($2.sval + ":" + "false");
                                 String etiquetaInicio = "ini_" + ambito ;
                                 ArregloTercetos.crearTerceto(etiquetaInicio, "_", "_");
                                }
                      | tipo  { yyerror("Error: Falta definir un nombre a la función"); }
                      ;

list_sentencia        : /* empty */
                      | list_sentencia sentencia
                      ;

sentencia             : sentencia_declarativa punto_y_coma
                      | sentencia_ejecutable punto_y_coma
                      | declaracion_funcion
                      | declaracion_funcion ';' { yyerror("Error: ';' de mas despues de declaracion de funcion"); }
                      | error ';' { yyerror("Error: sentencia invalida escrita"); }
                      ;

/* Busca sincronizarse con el final de una sentencia ejecutable o declarativa */
punto_y_coma          : ';'
                      | error ';' { yyerror("Error: falta ';' al final de la sentencia o sentencia invalida escrita"); }
                      ;
/* Busca sincronizarse con el final del cuerpo de una funcion o estructura */
punto_y_coma_cuerpo   : ';'
                      | error '}' { yyerror("Error: falta ';' al final de la sentencia o sentencia invalida escrita"); }
                      ;


sentencia_declarativa : tipo list_vars { reportarEstructura("declaracion de variable(s)"); }
                      ;

parametros_formales   : parametro_formal
                      | parametros_formales ',' parametro_formal
                      ;

parametro_formal      : semantica tipo ID {registrarParametroFuncion($3.sval,$1.sval);}
                      | semantica ID { yyerror("Error: Falta definir el tipo del parametro formal"); }
                      | semantica tipo { yyerror("Error: Falta definir un nombre al parametro formal"); }
                      ;

semantica             : CR {$$.sval = "cr";}
                      | /* empty */ { $$.sval = "cv"; }
                      ;

sentencia_return      : RETURN '(' expresion ')'  {registrarReturn();
                                                          String varRetorno = "_ret_" + ambito;
                                                          ArregloTercetos.crearTerceto(":=", varRetorno, $3.sval);
                                                          ArregloTercetos.crearTerceto("RETURN", varRetorno, $3.sval);
                                                          ArregloTercetos.crearTerceto("JMP", "fin_" + ambito, null);
                                             }
                      ;

tipo                  : ULONG {tipo = "ulong";}
                      ;

list_ctes             : CTE { $$.tipo = obtenerTipoDeSimbolo($1.sval); ControlAsigMultiple.pushTipoDer($$.tipo); $$.sval = $1.sval; }
                      | list_ctes ',' CTE { $$.tipo = $1.tipo; String t = obtenerTipoDeSimbolo($3.sval); ControlAsigMultiple.pushTipoDer(t); $$.sval = $1.sval + "," + $3.sval; }
                      | list_ctes CTE { yyerror("Error: se esperaba ',' entre constantes"); $$ = $1; }
                      ;



list_vars_mix         : identificador {ControlAsigMultiple.pushTipoIzq($$.tipo); }
                      | list_vars_mix ',' identificador { ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo($3.sval)); $$.sval = $1.sval + "," + $3.sval; }
                      | list_vars_mix  identificador { yyerror("Error: se esperaba ',' entre variables"); $$ = $1; }
                      ;

list_vars             : ID {$$ = declaracionDeVariable($1.sval, tipo, ambito, "Variable");}
                      | list_vars ',' ID {$$ = declaracionDeVariable($3.sval, tipo, ambito, "Variable");}
                      | list_vars ID { yyerror("Error: se esperaba ',' entre variables"); }
                      ;


cuerpo_ejecutable      : bloque_ejecutable
                       | sentencia_ejecutable_simple
                       ;

sentencia_ejecutable_simple : asignacion_simple
                            | asignacion_multiple
                            | sentencia_return
                            | sentencia_print
                            ;




sentencia_ejecutable  : sentencia_if
                      | asignacion_simple
                      | asignacion_multiple
                      | sentencia_return
                      | expresion_lambda
                      | sentencia_while
                      | sentencia_print
                      ;



bloque_ejecutable     : '{' lista_sentencias_ejecutable '}'
                      | '{' '}' {yyerror("Error: Cuerpo ejecutable vacio");}
                      | '{' error '}' {yyerror("Error: sentencia invalida dentro del cuerpo ejecutable");}
                      ;

lista_sentencias_ejecutable  : lista_sentencias_ejecutable sentencia_ejecutable punto_y_coma_cuerpo
                             | sentencia_ejecutable punto_y_coma_cuerpo
                             ;

sentencia_if          : if condicion_if cuerpo_ejecutable else cuerpo_ejecutable end_if { reportarEstructura("IF"); }
                      | if condicion_if cuerpo_ejecutable end_if { reportarEstructura("IF"); }
                      ;

sentencia_print       : PRINT '(' CADENA ')' { reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", $3.sval, null); }
                      | PRINT '(' expresion ')' { reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", $3.sval, null); }
                      | PRINT '('  ')' { yyerror("Error: falta argumento dentro del print"); }
                      ;

sentencia_while       : while condicion_while do cuerpo_ejecutable {
                            reportarEstructura("WHILE");
                            $$ = ArregloTercetos.completarBackPatchingWHILE();
                      }
                      ;


do                    : DO
                      | /* empty */ {yyerror("Error: falta palabra reservada 'do' en estructura while");}
                      ;

else                  : ELSE {ArregloTercetos.crearTercetoBackPatchingIFDesapilaryCompletar("bl", null,null);}
                      ;

if                    : IF {ArregloTercetos.crearTerceto("IF_START", "_", "_");}
                      ;

while                 : WHILE {
                                ArregloTercetos.apilarTercetoInicialWHILE();

                                ArregloTercetos.crearTerceto("WHILE_START", "_", "_");
                              }
                      ;

end_if                : ENDIF {ArregloTercetos.completarTercetoBackPatchingIF();}
                      | /* empty */ {yyerror("Error: falta palabra reservada 'endif'");}
                      ;

condicion_if          : '(' condicion ')' {ArregloTercetos.crearTercetoBackPatchingIF("bf", $2.sval,null);}
                      | condicion ')' {yyerror("Error: falta parentesis de apertura '(' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", $1.sval,null);}
                      | '(' condicion {yyerror("Error: falta parentesis de cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", $2.sval,null);}
                      | condicion {yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", $1.sval,null);}
                      ;

condicion_while       : '(' condicion ')' {ArregloTercetos.crearTercetoBackPatchingWHILE("bf", $2.sval,null);}
                      | condicion ')' {yyerror("Error: falta parentesis de apertura '(' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", $1.sval,null);}
                      | '(' condicion {yyerror("Error: falta parentesis de cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", $2.sval,null);}
                      | condicion {yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", $1.sval,null);}
                      ;



parametros_reales     : parametro_real
                      | parametros_reales ',' parametro_real
                      | error { yyerror("Error: Declaracion de parametro real invalida"); }
                      ;

parametro_real        : expresion FLECHA ID {
                                                String claveFuncionSinCorregir = PilaDeFuncionesLlamadas.getTopeActual();

                                                int primerSeparador = claveFuncionSinCorregir.indexOf(":");

                                                String nombreFuncion;
                                                String ambitoContenedor;
                                                String ambitoDeclaracionParametro;

                                                if (primerSeparador == -1) {
                                                    // Caso de error: La pila contiene solo el nombre simple (ej: "C")
                                                    // No se puede reconstruir el ámbito de declaración. Asumimos el ámbito actual como contenedor.

                                                    nombreFuncion = claveFuncionSinCorregir; // "C"
                                                    // Esta es la parte débil: ¿Cuál es el ámbito contenedor?
                                                    // Si la llamada es C(...) desde el main, el ámbito es "PROGRAMAFUNCIONAL".
                                                    // Usaremos la variable 'ambito' o un valor conocido:
                                                    ambitoContenedor = prog_principal; // Reemplaza por la forma correcta de obtener el ámbito global si es diferente

                                                    // Construimos el ámbito de declaración: PROGRAMAFUNCIONAL:C
                                                    ambitoDeclaracionParametro = ambitoContenedor + ":" + nombreFuncion;
                                                } else {
                                                    // Caso normal: Clave canónica (ej: C:PROGRAMAFUNCIONAL)
                                                    nombreFuncion = claveFuncionSinCorregir.substring(0, primerSeparador);
                                                    ambitoContenedor = claveFuncionSinCorregir.substring(primerSeparador + 1);
                                                    ambitoDeclaracionParametro = ambitoContenedor + ":" + nombreFuncion;
                                                }
                                                // ----------------------------------------------------------------------

                                                // 4. Construir la clave de búsqueda del parámetro (Ej: TEST:PROGRAMAFUNCIONAL:C)
                                                String paramFormal = val_peek(0).sval + ":" + ambitoDeclaracionParametro;

                                                String tipoFormal = obtenerTipoDeSimbolo(paramFormal);

                                                yyval.tipo = chequearTipos(val_peek(2).tipo, tipoFormal, "->");
                                                realizarPasajeCopiaValor(paramFormal, val_peek(2).sval);
                                                if (esParametroCR(paramFormal)) {
                                                    registrarVinculoCR(paramFormal, val_peek(2).sval);
                                                }
                                            }
                      | expresion FLECHA  { yyerror("Error: Falta definir el nombre del parametro formal"); }
                      | expresion ID { yyerror("Error: Falta '->' en la especificacion de parametro real"); }
                      ;

condicion            : expresion comp expresion {$$ = ArregloTercetos.crearTerceto($2.sval, $1.sval, $3.sval); $$.tipo = chequearTipos($1.tipo, $3.tipo, $2.sval); }
                     | expresion factor_simple  { yyerror("Error: falta operador de comparacion en la condicion"); }
                     ;

comp                 : EQ  { $$.sval = "=="; }
                     | GEQ { $$.sval = ">="; }
                     | LEQ { $$.sval = "<="; }
                     | NEQ { $$.sval = "=!"; }
                     | '>' { $$.sval = ">"; }
                     | '<' { $$.sval = "<"; }
                     | '=' {yyerror("Error: Operador invalido");}
                     | '=' '<' {yyerror("Error: Operador invalido");}
                     | '=' '>' {yyerror("Error: Operador invalido");}
                     | '!' '=' {yyerror("Error: Operador invalido");}

                     ;

asignacion_simple     : var_asignacion_simple ASIG expresion { reportarEstructura("asignacion simple");
                                                               $$ = ArregloTercetos.crearTerceto(":=", $1.sval, $3.sval);
                                                               $$.tipo = chequearTipos($1.tipo, $3.tipo, ":=");
                                                               }
                      ;

var_asignacion_simple : identificador { $$ = $1; $$.tipo = $1.tipo; }
                      ;

asignacion_multiple   : list_vars_mix '=' list_ctes
                            {
                              reportarEstructura("asignacion multiple");
                              ControlAsigMultiple.compararTipos();
                              crearTercetosAsigMultiple($1.sval, $3.sval);
                            }
                      ;

header_lambda         : '(' tipo ID ')'
                           {
                             reportarEstructura("expresión lambda");
                             contadorLambda++;
                             String nombreLambda = "_lambda" + contadorLambda;
                             String claveLambda  = nombreLambda + ":" + ambito;
                             ParserValExt paramFormal = registrarParametroFuncion($3.sval, "cv");
                             ArregloTercetos.crearTerceto("ini_" + claveLambda, "_", "_");
                             $$.sval = claveLambda;
                             $$.tipo = paramFormal.sval;
                           }
                           ;

expresion_lambda      : header_lambda bloque_ejecutable
                            {
                              ArregloTercetos.crearTerceto("fin_" + $1.sval, "_", "_");
                            }
                            '(' factor_lambda ')'
                            {
                              String tipoFormal = obtenerTipoDeSimbolo($1.tipo);
                              chequearTipos(tipoFormal, $6.tipo, ":=");
                              ArregloTercetos.crearTerceto(":=", $1.tipo, $6.sval);
                              ArregloTercetos.crearTerceto("CALL", $1.sval, null);
                            }

                      ;

factor_lambda         : identificador { $$ = $1; $$.tipo = $1.tipo; }
                      | CTE     { $$ = $1; $$.tipo = obtenerTipoDeSimbolo($1.sval); }
                      | '-' CTE { $$ = constanteNegativa($2); $$.tipo = obtenerTipoDeSimbolo($$.sval); }
                      ;

expresion             : expresion operador_expresion termino {$$ = ArregloTercetos.crearTerceto($2.sval, $1.sval, $3.sval); $$.tipo = chequearTipos($1.tipo, $3.tipo, $2.sval);
                        System.out.println("$1: "+ $1.sval + " $2: " + $2.sval + " $3: " + $3.sval + " -> $$: " + $$.sval);}
                      | termino {$$ = $1; $$.tipo = $1.tipo;}
                      | error operador_expresion termino { yyerror("Error: operando a la izquierda invalido"); }
                      | expresion operador_expresion error { yyerror("Error: operando a la derecha invalido"); }
                      | error operador_expresion error { yyerror("Error: operandos a la izquierda y derecha invalidos"); }
                      | error ';' { yyerror("Error: error en expresion, revisar '+' '-'"); }
                      ;




termino               : termino operador_termino factor {$$ = ArregloTercetos.crearTerceto($2.sval, $1.sval, $3.sval); $$.tipo = chequearTipos($1.tipo, $3.tipo, $2.sval); }
                      | factor {$$ = $1; $$.tipo = $1.tipo;}
                      | error operador_termino factor { yyerror("Error: operando a la izquierda invalido"); }
                      | termino operador_termino error { yyerror("Error: operando a la derecha invalido"); }
                      | error operador_termino error { yyerror("Error: operandos a la izquierda y derecha invalidos"); }
                      ;



operador_expresion    : '+' {
                          $$ = new ParserValExt(); // Nuevo objeto
                          $$.sval = "+";
                        }
                      | '-' {
                          $$ = new ParserValExt(); // Nuevo objeto
                          $$.sval = "-";
                        }
                      ;

operador_termino      : '*' {
                          $$ = new ParserValExt();
                          $$.sval = "*";
                        }
                      | '/' {
                          $$ = new ParserValExt();
                          $$.sval = "/";
                        }
                      ;


factor                : identificador {$$ = $1; $$.tipo = $1.tipo; }
                      | CTE {$$.tipo = obtenerTipoDeSimbolo($1.sval); $$ = $1; }
                      | inicio_llamado parametros_reales ')' {
                                                       ParserValExt tCall = ArregloTercetos.crearTerceto("CALL", $1.sval, null);
                                                       String tipoRet = obtenerTipoDeSimbolo($1.sval);
                                                       $$.tipo = tipoRet;
                                                       String temp = "_t" + ArregloTercetos.declararTemporal(tipoRet, ambito);
                                                       declaracionDeVariable(temp, tipoRet, ambito, "temporal");
                                                       ArregloTercetos.crearTerceto(":=", temp, tCall.sval);
                                                       $$.sval = temp;
                                                       PilaDeFuncionesLlamadas.finalizarLlamada();
                                                       realizarPasajesCopiaResultado();
                                         }

                      |'-' CTE { $$ = constanteNegativa($2); $$.tipo = obtenerTipoDeSimbolo($$.sval);}
                      | trunc { $$ = $1; $$.tipo = "ulong"; }
                      ;

/* factor_simple es una version de factor sin el manejo de constantes negativas -> Sirve para algunos manejos de errores */
factor_simple         : identificador {$$ = $1; $$.tipo = $1.tipo; }
                      | CTE {$$.tipo = obtenerTipoDeSimbolo($1.sval); $$.sval = $1.sval; }
                      | inicio_llamado parametros_reales ')' {
                                                                             ParserValExt tCall = ArregloTercetos.crearTerceto("CALL", $1.sval, null);
                                                                             String tipoRet = obtenerTipoDeSimbolo($1.sval);
                                                                             $$.tipo = tipoRet;
                                                                             String temp = "_t" + ArregloTercetos.declararTemporal(tipoRet, ambito);
                                                                             declaracionDeVariable(temp, tipoRet, ambito, "temporal");
                                                                             ArregloTercetos.crearTerceto(":=", temp, tCall.sval);
                                                                             $$.sval = temp;
                                                                             PilaDeFuncionesLlamadas.finalizarLlamada();
                                                                             realizarPasajesCopiaResultado();
                                                               }

                      | trunc { $$ = $1; $$.tipo = "ulong"; }
                      ;

trunc                 :TRUNC '(' CTE ')' {String tipo = obtenerTipoDeSimbolo($3.sval);
                                          if(! tipo.equals("dfloat")){
                                             yyerror("Error: La funcion TRUNC solo acepta operandos de tipo dfloat.");
                                          }
                                          $$ = ArregloTercetos.crearTerceto("TRUNC", $3.sval, null);
                                          $$.tipo = "ulong";}

                      | TRUNC '(' CTE error { yyerror("Error: falta ')' en la expresion TRUNC"); }
                      | TRUNC error  CTE ')' { yyerror("Error: falta '(' en la expresion TRUNC"); }
                      | TRUNC error CTE error { yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
                      ;




inicio_llamado        : identificador '(' {
                             // CORRECCIÓN 1: Usar $$.sval
                             PilaDeFuncionesLlamadas.iniciarLlamada($$.sval);
                           }
                      ;

identificador        : ID { $$ = chequearAmbito("", ambito, $1.sval); $$.tipo = obtenerTipoDeSimbolo($$.sval); }
                     | ID '.' ID { $$ = chequearAmbito($1.sval, ambito, $3.sval); $$.tipo = obtenerTipoDeSimbolo($$.sval); }
                     ;



%%
public static final Set<String> erroresEmitidos = new HashSet<>();
public static int ultimaLineaError;
public static String prog_principal;

public void resetErrores() {
    erroresEmitidos.clear();
}

public void yyerror(String s) {
     int linea = AnalizadorLexico.getNumeroDeLinea();
     if (linea != ultimaLineaError){
        resetErrores();
     }
     ultimaLineaError = linea;
     String clave = linea + "|" + s;
        if (erroresEmitidos.add(clave)) {
           RecolectorDeErrores.agregarError(s, linea);
        }
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
    nuevo.setUso("cte");

    String nombreNuevo = "-" + clave.sval;
    TablaDeSimbolos.addSimbolo(nombreNuevo, nuevo);
    RegistroDeConstantes.registrarConstante(nombreNuevo);
    RegistroDeConstantes.desregistrarConstante(clave.sval);

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

public static String ambito = "";
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
    String ambitoDeBusqueda = ambito;
    boolean simboloEncontrado = false;
    String ambitoActual = "";
    ParserValExt val = new ParserValExt();
    val.sval = nombreIdentificador;

    if (!prefijo.isEmpty()) {

        while (!ambitoDeBusqueda.isEmpty()) {
            int pos = ambitoDeBusqueda.lastIndexOf(":");

            if (pos == -1) {
                ambitoActual = ambitoDeBusqueda;
            } else {
                ambitoActual = ambitoDeBusqueda.substring(pos + 1);
            }

            if (ambitoActual.equals(prefijo)) {
                claveBuscada = nombreIdentificador + ":" + ambitoDeBusqueda;
                elem = TablaDeSimbolos.getSimbolo(claveBuscada);

                if (elem != null) {
                    val.sval = claveBuscada;
                    simboloEncontrado = true;
                    break;
                }
            }

            if (pos == -1) {
                ambitoDeBusqueda = "";
            } else {
                ambitoDeBusqueda = ambitoDeBusqueda.substring(0, pos);
            }
        }

        if (!simboloEncontrado) {
            yyerror("El símbolo '" + nombreIdentificador +
                    "' no se encuentra en el ámbito del prefijo '" + prefijo + "'.");
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
    ElementoTablaDeSimbolos original = TablaDeSimbolos.getSimbolo(token);

    if (original != null) {
        String usoOriginal = original.getUso();
        if ("Función".equals(usoOriginal)) {
            yyerror("Error: Redeclaracion de función " + token);
            return;
        } else {
            yyerror("Error: Nombre ya utilizado por otro símbolo: " + token);
            return;
        }
    }

    ElementoTablaDeSimbolos nuevoElem = new ElementoTablaDeSimbolos();
    nuevoElem.setTipo(tipo);
    nuevoElem.setAmbito(ambito);
    nuevoElem.setUso("Función");
    TablaDeSimbolos.addSimbolo(token + ":" + ambito, nuevoElem);
}

public static Stack<String> pilaReturns = new Stack<>();

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

public void registrarReturn() {
        if (!pilaReturns.isEmpty()) {
            String top = pilaReturns.pop();
            String[] partes = top.split(":");
            String nombreFunc = partes[0];
            pilaReturns.push(nombreFunc + ":true");
        } else {
            yyerror("Error: Sentencia return declarada en lugar inválido");
        }
}

public String chequearTipos(String tipo1, String tipo2, String operacion){

    if(tipo1.equals("dfloat") && tipo2.equals("dfloat")){
        yyerror("Error: No se pueden realizar operaciones entre operandos de tipo dfloat.");
        return "error";
    }

    if(tipo1.equals("error") || tipo2.equals("error")){
        return "error";
    }

    if(!tipo1.equals(tipo2)){
        yyerror("Error: Tipos incompatibles para la operación (" + tipo1 + " " + operacion + " " + tipo2 + ").");
        return "error";
    }

    return tipo1;
}

public String obtenerTipoDeSimbolo(String claveTS) {
    ElementoTablaDeSimbolos elemento = TablaDeSimbolos.getSimbolo(claveTS);
    if (elemento != null) {
        return elemento.getTipo();
    }
    else{
        yyerror("Error: El elemento "+ claveTS + " no existe");
        return "error";
    }
}

public ParserValExt registrarParametroFuncion(String parametro,String semantica) {
    String nombreParametro = parametro + ":" + ambito;
    ElementoTablaDeSimbolos elemento = new ElementoTablaDeSimbolos();
    elemento.setSemanticaPasaje(semantica);
    elemento.setTipo(tipo);
    elemento.setAmbito(ambito);
    elemento.setUso("parametro");
    TablaDeSimbolos.addSimbolo(nombreParametro, elemento);

    ParserValExt val = new ParserValExt();
    val.sval = nombreParametro;
    return val;
}

public void realizarPasajeCopiaValor(String parametro, String valor) {
    ElementoTablaDeSimbolos elemento = TablaDeSimbolos.getSimbolo(parametro);
    if (elemento != null) {
        if(elemento.getSemanticaPasaje().equals("cv")) {
            ArregloTercetos.crearTerceto(":=", parametro, valor);
        }
    } else {
        yyerror("Error: El parámetro " + parametro + " no existe en la tabla de símbolos.");
    }
}


static Map<String, String> vinculosCR = new HashMap<>();

public void registrarVinculoCR(String formal, String real) {
    ElementoTablaDeSimbolos elem = TablaDeSimbolos.getSimbolo(real);
    if (elem == null) {
        yyerror("Error: la variable utilizada para el pasaje copia resultado no existe en el ámbito actual o se esta utilizando algo que no es una variable.");
        return;
    }

    if (!"Variable".equals(elem.getUso()) && !"parametro".equals(elem.getUso())) {
        yyerror("Error: el parámetro de copia-resultado debe ser una variable" );
        return;
    }

    vinculosCR.put(formal, real);
}

public boolean esParametroCR(String nombreParamFormal) {
    ElementoTablaDeSimbolos e = TablaDeSimbolos.getSimbolo(nombreParamFormal);
    return e != null && "cr".equalsIgnoreCase(e.getSemanticaPasaje());
}

public void realizarPasajesCopiaResultado() {
    for (Map.Entry<String, String> entry : vinculosCR.entrySet()) {
        String formal = entry.getKey();
        String real = entry.getValue();
        ArregloTercetos.crearTerceto(":=", real, formal);
    }
    vinculosCR.clear();
}

public String obtenerAmbito(String claveTS) {
    ElementoTablaDeSimbolos elemento = TablaDeSimbolos.getSimbolo(claveTS);
    if (elemento != null) {
        int pos = claveTS.indexOf(":");
        return ambito + ":" + claveTS.substring(0, pos);

    } else {
        yyerror("Error: El elemento " + claveTS + " no existe");
        return "error";
    }

}

public static int contadorLambda = 0;

private static List<String> splitLista(String entrada) {
    List<String> salida = new ArrayList<>();
    if (entrada == null || entrada.isEmpty()) {
        return salida;
    }
    for (String s : entrada.split(",")){
        salida.add(s.trim());
    }
    return salida;
}

public static void crearTercetosAsigMultiple(String variables, String constantes){
    List<String> ladoIzquierdo = splitLista(variables);
    List<String> ladoDerecho = splitLista(constantes);
      int n = Math.min(ladoIzquierdo.size(), ladoDerecho.size());
      for (int i = 0; i < n; i++) {
        ArregloTercetos.crearTerceto(":=", ladoIzquierdo.get(i), ladoDerecho.get(i));
      }
}