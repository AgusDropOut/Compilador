//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "Gramatica.y"
package Compilador.ModuloSintactico;

import java.io.*;
import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.TablaDeSimbolos;
//#line 23 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short WHILE=257;
public final static short IF=258;
public final static short ELSE=259;
public final static short ENDIF=260;
public final static short PRINT=261;
public final static short RETURN=262;
public final static short DO=263;
public final static short CTE=264;
public final static short ID=265;
public final static short ASIG=266;
public final static short TRUNC=267;
public final static short CR=268;
public final static short ULONG=269;
public final static short COMP=270;
public final static short CADENA=271;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    2,    2,    3,    3,    8,
    8,   10,   10,   11,    9,    9,    6,   13,   13,    7,
    7,    7,    7,    4,    4,    4,    4,    4,    4,    4,
    4,    4,    4,    4,    4,   14,   16,   16,   20,   20,
   21,   15,   15,   15,   15,   15,   17,   17,   18,    5,
    5,    5,    5,   19,   12,   12,   12,   12,   23,   23,
   23,   22,   22,   22,   22,
};
final static short yylen[] = {                            2,
    4,    0,    2,    1,    1,    1,    2,    3,    9,    1,
    3,    3,    2,    1,    3,    0,    1,    1,    3,    1,
    3,    3,    5,    1,   13,    9,   11,    8,   12,   12,
    5,    5,    1,    1,    1,    4,    1,    2,    1,    3,
    3,    3,    3,    3,    3,    3,    4,    6,    4,    9,
    7,    7,    7,   10,    3,    3,    1,    4,    3,    3,
    1,    1,    1,    3,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,   17,
    1,    0,    3,    4,    5,    6,    0,    0,   24,   33,
   34,   35,    7,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   63,    0,    0,    0,
    0,    0,   61,    0,    0,    0,    0,    0,    0,    0,
    0,   39,    0,    0,    0,    0,    8,    0,   18,    0,
    0,   65,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   47,    0,   36,    0,    0,
    0,   14,    0,    0,   10,    0,   22,    0,   49,    0,
    0,   64,    0,    0,    0,    0,    0,   44,    0,   45,
    0,   59,   60,    0,    0,   31,   32,   41,   40,    0,
    0,   13,    0,    0,    0,   23,   19,    0,   58,    0,
    0,    0,   37,    0,    0,   48,    0,    2,   11,   12,
   52,   53,    0,   51,    0,   38,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   28,    0,    0,    0,   50,
    0,    0,   26,    0,    0,    0,    0,    9,    0,    0,
    0,    0,   54,   15,    0,    0,    0,   27,    0,   29,
   30,   25,
};
final static short yydgoto[] = {                          2,
    4,   13,   14,  123,   16,   17,   18,   84,  149,   85,
   86,   40,   60,   19,   42,  124,   20,   21,   22,   51,
   52,   43,   44,
};
final static short yysindex[] = {                      -236,
  -61,    0,    0,  -15,   11,   32,   43,   66,    1,    0,
    0, -190,    0,    0,    0,    0, -169,    7,    0,    0,
    0,    0,    0,   48,   44,   -1,   52,   52, -157, -141,
    6,  -20, -137, -126,   98, -123,    0,   19,  102,  -11,
 -125,  103,    0,   65,  105,   97,  106,   77,   33,  -28,
   17,    0, -115,  113, -155, -110,    0,  110,    0,   50,
 -105,    0, -104,   52,   52,  -29,  -29,   44,   44, -101,
  -29,  -29,   14,  108,  112,    0,  -96,    0,   52,   52,
   41,    0,  -93,   85,    0, -190,    0,  -92,    0,  -90,
   45,    0,   80,  -10,   65,   65,  -10,    0,  -10,    0,
   13,    0,    0,   45,  116,    0,    0,    0,    0,   39,
   45,    0,   53, -155,  -88,    0,    0,  119,    0,  120,
   45,  122,    0,   15, -129,    0,   23,    0,    0,    0,
    0,    0,   24,    0, -127,    0,   29,  123,  143,   -9,
  125,   34,  127,   45,  128,    0,  -29,   52,   63,    0,
   45,  130,    0,   35,  -70,  150,   60,    0,   40,  -68,
  -67,  135,    0,    0,  -65,  137,  138,    0,  139,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,   12,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   56,    0,    0,    0,    0,    0,    0,   -5,    0,    0,
    0,    0,    0,  -38,    0,  -41,    0,    0,    0,    0,
    0,    0,   16,    0,    0,    0,    0,  -16,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  158,  -33,  -32,  159,    0,  160,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   78,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
   74,    0,    0,   26,    0,    2,  187,    0,    0,   91,
    0,   22,    0,   -2,  181,  -40,    0,    0,    0,    0,
  129,  -52,   68,
};
final static int YYTABLESIZE=319;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         62,
   62,   62,   57,   62,   57,   62,   57,   55,   56,   55,
   56,   55,   56,   30,   66,   36,   67,   62,  102,  103,
   57,   41,   41,   33,   12,   55,   56,   21,    1,   15,
   12,   66,   66,   67,   67,   62,   62,   62,   57,   62,
   28,   62,   21,   36,   21,   55,   29,   48,   49,   50,
   33,   56,   12,   12,   12,   20,   83,   78,   28,   22,
   79,    3,   12,   12,   63,   98,  100,   34,   12,   23,
  127,   24,   20,   12,   12,   66,   22,   67,   10,   12,
  133,   66,   25,   67,   12,   93,   94,  115,   36,   97,
   99,   76,   36,   90,  156,   31,   36,  126,  105,   20,
   50,  110,   66,  154,   67,   26,   71,   53,   89,   11,
  159,   72,   82,   10,   20,   83,  118,   75,  164,   66,
  119,   67,   66,   54,   67,  113,  122,   58,  114,  137,
  138,  142,  143,   95,   96,  121,  104,   59,   61,  135,
   62,   64,   63,   70,   69,   73,   74,  139,  141,  136,
   80,  144,  136,   81,   87,   88,  151,   91,  136,  161,
   92,  101,  145,  111,  165,   15,  106,  152,  108,  157,
  107,  112,  116,  117,  125,  128,  130,  131,  132,  136,
  134,  146,  147,  150,  136,  153,  155,  158,  160,  162,
  163,  166,  167,  168,  169,  170,  171,  172,   46,   42,
   43,  140,   16,   32,  129,   45,    0,  109,    0,    0,
    0,    0,    0,    0,   62,    0,    0,   57,    0,    0,
    0,    0,   55,   56,    0,    0,    0,    0,   62,    0,
   62,   57,    0,   57,   37,   46,   55,   56,   55,   56,
    5,    6,    7,   77,   65,    8,    5,    6,    7,    9,
   62,    8,  148,   10,    0,    9,    0,    0,   68,   10,
    0,    0,   37,   46,   62,   39,   27,    0,  120,   47,
    7,    7,    7,    8,    8,    8,    0,    9,    9,    9,
    7,    7,    0,    8,    8,    0,    7,    9,    9,    8,
    0,    7,    7,    9,    8,    8,    0,    7,    9,    9,
    8,    0,    7,   35,    9,    8,    0,   37,   38,    9,
   39,   37,   38,    0,   39,   37,   46,    0,   39,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   41,   45,   43,   47,   45,   41,   41,   43,
   43,   45,   45,   12,   43,   45,   45,   59,   71,   72,
   59,   24,   25,   44,   40,   59,   59,   44,  265,    4,
   40,   43,   43,   45,   45,   41,   42,   43,   59,   45,
   40,   47,   59,   45,   61,   40,   46,   26,   27,   28,
   44,   46,   40,   40,   40,   44,   55,   41,   40,   44,
   44,  123,   40,   40,   46,   68,   69,   61,   40,   59,
  111,   40,   61,   40,   40,   43,   61,   45,  269,   40,
  121,   43,   40,   45,   40,   64,   65,   86,   45,   68,
   69,   59,   45,   44,  147,  265,   45,   59,   73,   44,
   79,   80,   43,  144,   45,   40,   42,  265,   59,  125,
  151,   47,  268,  269,   59,  114,   91,   41,   59,   43,
   41,   45,   43,  265,   45,   41,  101,  265,   44,  259,
  260,  259,  260,   66,   67,  123,  123,  264,   41,  125,
  264,   40,   46,   41,  270,   41,   41,  125,  125,  124,
  266,  123,  127,   41,  265,   46,  123,  263,  133,  125,
  265,  263,  137,  123,  125,  140,   59,  142,  265,  148,
   59,  265,  265,  264,   59,  123,  265,   59,   59,  154,
   59,   59,   40,   59,  159,   59,   59,  125,   59,  260,
   41,  260,  260,   59,  260,   59,   59,   59,   41,   41,
   41,  128,  125,   17,  114,   25,   -1,   79,   -1,   -1,
   -1,   -1,   -1,   -1,  256,   -1,   -1,  256,   -1,   -1,
   -1,   -1,  256,  256,   -1,   -1,   -1,   -1,  270,   -1,
  272,  270,   -1,  272,  264,  265,  270,  270,  272,  272,
  256,  257,  258,  272,  256,  261,  256,  257,  258,  265,
  256,  261,  262,  269,   -1,  265,   -1,   -1,  270,  269,
   -1,   -1,  264,  265,  270,  267,  266,   -1,  256,  271,
  258,  258,  258,  261,  261,  261,   -1,  265,  265,  265,
  258,  258,   -1,  261,  261,   -1,  258,  265,  265,  261,
   -1,  258,  258,  265,  261,  261,   -1,  258,  265,  265,
  261,   -1,  258,  256,  265,  261,   -1,  264,  265,  265,
  267,  264,  265,   -1,  267,  264,  265,   -1,  267,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=272;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
null,"'='",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"WHILE","IF","ELSE","ENDIF","PRINT","RETURN",
"DO","CTE","ID","ASIG","TRUNC","CR","ULONG","COMP","CADENA","\"->\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID '{' list_sentencia '}'",
"list_sentencia :",
"list_sentencia : list_sentencia sentencia",
"sentencia : sentencia_declarativa",
"sentencia : sentencia_ejecutable",
"sentencia : sentencia_control",
"sentencia : error ';'",
"sentencia_declarativa : tipo list_vars ';'",
"sentencia_declarativa : tipo ID '(' parametros_formales ')' '{' list_sentencia sentencia_return '}'",
"parametros_formales : parametro_formal",
"parametros_formales : parametros_formales ',' parametro_formal",
"parametro_formal : semantica tipo ID",
"parametro_formal : tipo ID",
"semantica : CR",
"sentencia_return : RETURN expresion ';'",
"sentencia_return :",
"tipo : ULONG",
"list_ctes : CTE",
"list_ctes : list_ctes ',' CTE",
"list_vars : ID",
"list_vars : list_vars ',' ID",
"list_vars : ID '.' ID",
"list_vars : list_vars ',' ID '.' ID",
"sentencia_ejecutable : invocacion_funcion",
"sentencia_ejecutable : IF '(' condicion ')' '{' bloque_ejecutable '}' ELSE '{' bloque_ejecutable '}' ENDIF ';'",
"sentencia_ejecutable : IF '(' condicion ')' '{' bloque_ejecutable '}' ENDIF ';'",
"sentencia_ejecutable : IF '(' condicion ')' sentencia_ejecutable ';' ELSE sentencia_ejecutable ';' ENDIF ';'",
"sentencia_ejecutable : IF '(' condicion ')' sentencia_ejecutable ';' ENDIF ';'",
"sentencia_ejecutable : IF '(' condicion ')' '{' bloque_ejecutable '}' ELSE sentencia_ejecutable ';' ENDIF ';'",
"sentencia_ejecutable : IF '(' condicion ')' sentencia_ejecutable ';' ELSE '{' bloque_ejecutable '}' ENDIF ';'",
"sentencia_ejecutable : PRINT '(' CADENA ')' ';'",
"sentencia_ejecutable : PRINT '(' expresion ')' ';'",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : expresion_lambda",
"invocacion_funcion : ID '(' parametros_reales ')'",
"bloque_ejecutable : sentencia_ejecutable",
"bloque_ejecutable : bloque_ejecutable sentencia_ejecutable",
"parametros_reales : parametro_real",
"parametros_reales : parametros_reales ',' parametro_real",
"parametro_real : expresion \"->\" ID",
"condicion : expresion COMP expresion",
"condicion : invocacion_funcion COMP expresion",
"condicion : expresion COMP invocacion_funcion",
"condicion : invocacion_funcion COMP invocacion_funcion",
"condicion : expresion error expresion",
"asignacion_simple : ID ASIG expresion ';'",
"asignacion_simple : ID '.' ID ASIG expresion ';'",
"asignacion_multiple : list_vars '=' list_ctes ';'",
"sentencia_control : WHILE '(' condicion ')' DO '{' bloque_ejecutable '}' ';'",
"sentencia_control : WHILE '(' condicion ')' DO sentencia_ejecutable ';'",
"sentencia_control : WHILE '(' error ')' DO sentencia_ejecutable ';'",
"sentencia_control : WHILE '(' condicion ')' DO error ';'",
"expresion_lambda : '(' tipo ID ')' '{' bloque_ejecutable '}' '(' factor ')'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"expresion : TRUNC '(' expresion ')'",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : ID '.' ID",
"factor : '-' CTE",
};

//#line 138 "Gramatica.y"

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
//#line 380 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 7:
//#line 26 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 46:
//#line 98 "Gramatica.y"
{ yyerror("Error, comparador invalido en la condición, línea: " + AnalizadorLexico.getNumeroDeLinea()); }
break;
case 52:
//#line 112 "Gramatica.y"
{ yyerror("Error en la condición del WHILE, línea " + AnalizadorLexico.getNumeroDeLinea()); }
break;
case 53:
//#line 113 "Gramatica.y"
{ yyerror("Error en el cuerpo del WHILE, línea " + AnalizadorLexico.getNumeroDeLinea());  }
break;
case 62:
//#line 130 "Gramatica.y"
{System.out.println("DEBUG factor: se detectó ID -> " + val_peek(0).sval);}
break;
case 63:
//#line 131 "Gramatica.y"
{System.out.println("DEBUG factor: se detectó CTE -> " + val_peek(0).sval);}
break;
case 65:
//#line 133 "Gramatica.y"
{System.out.println("DEBUG factor: se detectó -CTE -> " + val_peek(0).sval);}
break;
//#line 557 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
