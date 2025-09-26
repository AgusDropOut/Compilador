%{
package Compilador.ModuloSintactico;

import java.io.*;
import Compilador.ModuloLexico.AnalizadorLexico;
%}

%token WHILE  IF  ELSE  ENDIF  PRINT  RETURN  DO  CTE  ID  ASIG  TRUNC  CR  ULONG  COMP  CADENA

%%
programa              : ID '{' list_sentencia '}'
                      ;

list_sentencia        : /* empty */
                      | list_sentencia sentencia
                      ;

sentencia             : sentencia_declarativa
                      | sentencia_ejecutable
                      | sentencia_control
                      ;

sentencia_declarativa : tipo list_vars';'
                      | tipo ID '{' parametros_formales '}' '{' list_sentencia sentencia_return'}'
                      ;

parametros_formales   : parametro_formal
                      | parametros_formales ',' parametro_formal
                      ;

parametro_formal      : semantica tipo ID
                      | tipo ID
                      ;

semantica             : CR
                      ;

sentencia_return      : RETURN expresion';'
                      | /* empty */
                      ;

tipo                  : ULONG
                      ;

list_ctes             : CTE
                      | list_ctes ',' CTE
                      ;

list_vars             : ID
                      | list_vars ',' ID
                      ;

list_var_mix          : ID
                      | ID '.' ID
                      | list_var_mix ',' ID
                      | list_var_mix ',' ID '.' ID
                      ;

sentencia_ejecutable  : invocacion_funcion
                      | IF '(' condicion ')' '{' bloque_ejecutable '}' ELSE '{' bloque_ejecutable '}' ENDIF ';'
                      | IF '(' condicion ')' '{' bloque_ejecutable '}' ENDIF ';'
                      | IF '(' condicion ')' sentencia_ejecutable ';' ELSE sentencia_ejecutable ';' ENDIF ';'
                      | IF '(' condicion ')' sentencia_ejecutable ';' ENDIF ';'
                      | IF '(' condicion ')' '{' bloque_ejecutable '}' ELSE sentencia_ejecutable ';' ENDIF ';'
                      | IF '(' condicion ')' sentencia_ejecutable ';' ELSE '{' bloque_ejecutable '}' ENDIF ';'
                      | PRINT '(' CADENA ')' ';'
                      | PRINT '(' expresion ')' ';'
                      | asignacion_simple
                      | asignacion_multiple
                      ;

invocacion_funcion    : ID '(' parametros_reales ')'
                      ;

bloque_ejecutable     : sentencia_ejecutable
                      | bloque_ejecutable sentencia_ejecutable
                      ;


parametros_reales     : parametro_real
                      : parametros_reales ',' parametro_real
                      ;

parametro_real        : expresion '->' ID
                      ;

condicion             : expresion COMP expresion
                      | invocacion_funcion COMP expresion
                      | expresion COMP invocacion_funcion
                      | invocacion_funcion COMP invocacion_funcion
                      ;

asignacion_simple     : ID ASIG expresion ';'
                      | ID '.' ID ASIG expresion ';'
                      ;

asignacion_multiple   : list_vars '=' list_ctes ';'
                      | list_var_mix '=' list_ctes ';'
                      ;

sentencia_control     : WHILE '(' condicion ')' DO '{' bloque_ejecutable '}' ';'
                      | WHILE '(' condicion ')' DO sentencia_ejecutable ';'
                      ;

expresion_lambda      : '(' tipo ID ')' '{' bloque_ejecutable '}' '(' factor ')'
                      ;

expresion             : expresion '+' termino
                      | expresion '-' termino
                      | termino
                      | TRUNC '(' expresion ')'
                      ;

termino               : termino '*' factor
                      | termino '/' factor
                      | factor
                      ;

factor                : ID
                      | CTE
                      | ID '.' ID
                      ;


%%

public void yyerror(String s) {
    System.err.println("Error de sintaxis en línea "
        + AnalizadorLexico.getNumeroDeLinea() + ": " + s);
}

public int yylex() {
    try {
        return AnalizadorLexico.yylex();
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}