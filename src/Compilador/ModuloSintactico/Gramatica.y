%{
package Compilador.ModuloSintactico;

import java.io.*;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.TablaDeSimbolos;
%}

%left '+' '-'
%left '*' '/'


%token WHILE  IF  ELSE  ENDIF  PRINT  RETURN  DO  CTE  ID  ASIG  TRUNC  CR  ULONG  COMP  CADENA FLECHA

%%
programa              : ID '{' list_sentencia '}'
                      | error '{' list_sentencia '}' { yyerror("Error: Falta definir un nombre al programa"); }
                      | ID error list_sentencia '}' { yyerror("Error: Falta delimitador del programa '{' al inicio"); }
                      | ID error list_sentencia error { yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }

                      ;

list_sentencia        : /* empty */
                      | list_sentencia sentencia
                      ;

sentencia             : sentencia_declarativa
                      | sentencia_ejecutable ';'
                      | sentencia_ejecutable error   { yyerror("Sentencia no reconocida, se esperaba ';'"); }
                      ;

sentencia_declarativa : tipo list_vars ';'
                      | tipo list_vars error { yyerror("Sentencia no reconocida, se esperaba ';'"); }
                      | tipo ID '(' parametros_formales ')' '{' list_sentencia sentencia_return '}'
                      ;

parametros_formales   : parametro_formal
                      | parametros_formales ',' parametro_formal
                      ;

parametro_formal      : semantica tipo ID
                      | tipo ID
                      ;

semantica             : CR
                      ;

sentencia_return      : RETURN expresion ';'
                      | RETURN expresion error { yyerror("Sentencia no reconocida, se esperaba ';'"); }
                      | /* empty */
                      ;

tipo                  : ULONG
                      ;

list_ctes             : CTE
                      | list_ctes ',' CTE
                      ;

list_vars             : ID
                      | list_vars ',' ID
                      | ID '.' ID
                      | list_vars ',' ID '.' ID
                      ;
/*
list_var_mix          : ID '.' ID
                      | list_var_mix ',' ID '.' ID
                      ; */

sentencia_ejecutable  : invocacion_funcion
                      | IF '(' condicion ')' '{' bloque_ejecutable '}' ELSE '{' bloque_ejecutable '}' ENDIF
                      | IF '(' condicion ')' '{' bloque_ejecutable '}' ENDIF
                      | IF '(' condicion ')' sentencia_ejecutable ';' ELSE sentencia_ejecutable ';' ENDIF
                      | IF '(' condicion ')' sentencia_ejecutable ';' ENDIF
                      | IF '(' condicion ')' '{' bloque_ejecutable '}' ELSE sentencia_ejecutable ';' ENDIF
                      | IF '(' condicion ')' sentencia_ejecutable ';' ELSE '{' bloque_ejecutable '}' ENDIF
                      | PRINT '(' CADENA ')'
                      | PRINT '(' expresion ')'
                      | asignacion_simple
                      | asignacion_multiple
                      | expresion_lambda
                      | WHILE '(' condicion ')' DO '{' bloque_ejecutable '}'
                      | WHILE '(' condicion ')' DO sentencia_ejecutable
                      | WHILE '(' error ')' DO sentencia_ejecutable { yyerror("Error en la condición del WHILE, línea " + AnalizadorLexico.getNumeroDeLinea()); }
                      | WHILE '(' condicion ')' DO error { yyerror("Error en el cuerpo del WHILE, línea " + AnalizadorLexico.getNumeroDeLinea());  }
                      ;

invocacion_funcion    : ID '(' parametros_reales ')'
                      ;

bloque_ejecutable     : sentencia_ejecutable
                      | bloque_ejecutable  sentencia_ejecutable
                      ;


parametros_reales     : parametro_real
                      | parametros_reales ',' parametro_real
                      ;

parametro_real        : expresion FLECHA ID
                      ;

condicion             : expresion COMP expresion
                     /* | invocacion_funcion COMP expresion
                      | expresion COMP invocacion_funcion
                      | invocacion_funcion COMP invocacion_funcion */
                      | expresion error expresion { yyerror("Error, comparador invalido en la condición, línea: " + AnalizadorLexico.getNumeroDeLinea()); }
                      ;

asignacion_simple     : ID ASIG expresion
                      | ID '.' ID ASIG expresion
                      ;

asignacion_multiple   : list_vars '=' list_ctes
                    /*  | list_var_mix '=' list_ctes ';' */
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

factor                : ID {System.out.println("DEBUG factor: se detectó ID -> " + $1.sval);}
                      | CTE {System.out.println("DEBUG factor: se detectó CTE -> " + $1.sval);}
                      | ID '.' ID
                      | '-' CTE {System.out.println("DEBUG factor: se detectó -CTE -> " + $2.sval);}
                      | invocacion_funcion
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