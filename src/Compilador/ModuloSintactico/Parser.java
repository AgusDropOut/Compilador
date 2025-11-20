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
//#line 35 "Parser.java"




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
//## **user defined:ParserValExt
String   yytext;//user variable to return contextual strings
ParserValExt yyval; //used to return semantic vals from action routines
ParserValExt yylval;//the 'lval' (result) I got from yylex()
ParserValExt valstk[] = new ParserValExt[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new ParserValExt();
  yylval=new ParserValExt();
  valptr=-1;
}
final void val_push(ParserValExt val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    ParserValExt[] newstack = new ParserValExt[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final ParserValExt val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final ParserValExt val_peek(int relative)
{
  return valstk[valptr-relative];
}
final ParserValExt dup_yyval(ParserValExt val)
{
  return val;
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
public final static short CADENA=270;
public final static short FLECHA=271;
public final static short EQ=272;
public final static short GEQ=273;
public final static short LEQ=274;
public final static short NEQ=275;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    0,    0,    1,    3,    4,    4,
    2,    2,    7,    7,    7,    9,    9,    8,    5,    5,
   12,   12,   12,   12,   12,   12,   13,   14,    6,   16,
   16,   16,   17,   17,   17,   17,   17,   17,   11,   11,
   11,   18,   18,   20,   20,   20,   20,   10,   10,   10,
   10,   10,   10,   10,   19,   19,   27,   27,   24,   24,
   23,   23,   23,   26,   34,   34,   30,   28,   32,   31,
   31,   29,   29,   29,   29,   33,   33,   33,   33,   36,
   36,   36,   37,   37,   37,   35,   35,   38,   38,   38,
   38,   38,   38,   38,   38,   38,   38,   21,   39,   39,
   22,   40,   41,   25,   42,   42,   42,   15,   15,   15,
   15,   15,   15,   15,   15,   15,   43,   43,   43,   43,
   43,   43,   43,   43,   43,   44,   44,   44,   44,   44,
   44,   44,   44,   44,   45,   45,
};
final static short yylen[] = {                            2,
    4,    5,    3,    3,    2,    3,    1,    6,    3,    2,
    0,    2,    2,    2,    1,    1,    1,    2,    1,    3,
    3,    2,    3,    2,    3,    2,    1,    4,    1,    1,
    3,    2,    1,    3,    3,    5,    2,    4,    1,    3,
    2,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    3,    2,    3,    2,    6,    4,
    4,    4,    3,    4,    1,    0,    1,    1,    1,    1,
    0,    3,    2,    2,    1,    3,    2,    2,    1,    1,
    3,    1,    3,    3,    2,    3,    4,    1,    1,    1,
    1,    1,    1,    1,    2,    2,    2,    3,    1,    3,
    3,    4,    0,    6,    1,    1,    2,    3,    3,    1,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    1,    1,    1,    3,    3,    2,
    4,    4,    4,    4,    2,    4,
};
final static short yydefred[] = {                         0,
    7,   11,    0,    0,    0,   11,    0,   69,   68,    0,
    0,    0,   29,    6,    0,   15,    0,    0,   12,    0,
    0,   51,    0,   49,   50,   54,   48,   52,   53,    0,
    0,    0,    0,    0,    3,    0,    0,    0,    0,    0,
   27,    0,    0,   19,    0,   10,    0,    0,   17,   16,
   13,   14,    0,    0,    0,    0,    0,  127,    0,    0,
    0,    0,    0,    0,    0,  125,    0,    0,    0,    0,
    0,    0,  103,    0,    0,   63,    0,    0,    0,    0,
   26,    0,    0,   24,   22,    0,    0,    9,   41,    0,
    0,    0,   30,    0,    0,    0,    0,    0,  130,  135,
    0,    0,    0,    0,    0,    0,    0,   88,   89,   90,
   91,   92,   93,    0,    0,    0,   46,    0,   42,   43,
   44,   45,   47,   73,    0,    0,    0,    0,    0,   80,
    0,   65,    0,   77,    0,   56,    0,    0,    0,    2,
   61,   62,   28,  102,   11,   20,   25,   23,   21,   40,
   38,    0,   32,    0,    0,    0,    0,    0,  123,  119,
  124,  121,    0,    0,    0,   72,    0,    0,    0,    0,
    0,   96,   95,   97,    0,   67,   70,    0,   60,  120,
  117,  122,  118,   85,    0,  129,    0,   76,   64,   58,
   55,    0,    0,    0,   36,   31,  136,  134,  133,  132,
  131,   87,    0,   84,   83,   81,   57,    0,  106,  105,
    0,    8,   59,  107,  104,
};
final static short yydgoto[] = {                          3,
    4,    5,   16,   17,   42,   18,   19,   20,   51,   21,
   48,   44,   45,   22,   62,   94,   23,  118,  119,  120,
   24,   25,   26,   27,   28,   29,  138,   30,   63,  178,
  179,   31,   69,  133,   64,  129,  130,  116,   32,   33,
  139,  211,   65,   66,   67,
};
final static short yysindex[] = {                       -98,
    0,    0,    0,  -47,   -5,    0,  229,    0,    0,    8,
   40,   64,    0,    0, -167,    0, -226, -205,    0,  -53,
  -53,    0,  -11,    0,    0,    0,    0,    0,    0,   24,
   46, -157,   -9,  242,    0,  210,   68, -137, -134, -110,
    0,    3, -153,    0, -217,    0,  129,   -4,    0,    0,
    0,    0,  146,  -72,  -69,  473,  -54,    0,   38,   -8,
   68,  411,  -74,  170,  -23,    0,   80,   68,  -13,  224,
   68,  274,    0,  154,  237,    0,   63,  164,    0,  259,
    0,  178, -226,    0,    0,   55, -111,    0,    0,   57,
  100,  257,    0,  -31,  125,  297,  301,  311,    0,    0,
  122,   62,   84,  359,   68,  323,  333,    0,    0,    0,
    0,    0,    0,   81,  341,   68,    0, -238,    0,    0,
    0,    0,    0,    0,  346,  362,  473,  -40,   21,    0,
  365,    0,  -74,    0,  130,    0,  -53,  283,  383,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  165,    0,  188,   19,  -23,   19,  -23,    0,    0,
    0,    0,  399,  -37,   -7,    0,  410,   19,  -23,   19,
  -23,    0,    0,    0,  130,    0,    0,  -74,    0,    0,
    0,    0,    0,    0,  -75,    0,   68,    0,    0,    0,
    0,  -53,  -43,  252,    0,    0,    0,    0,    0,    0,
    0,    0,  169,    0,    0,    0,    0,  201,    0,    0,
  401,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    1,    0,    0,  468,    0,    0,    0,
    0,  104,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  469,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  115,  -52,    0,    0,
    0,    0,    2,    0,    0,    0,    0,    0,  -33,    0,
    0,    0,    0,  202,   34,    0,    0,    0,  281, -112,
    0,    0,    0,  470,    0,    0,    0,    0,  145,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   39,    0,   99,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  309,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  372,    0,    0,    0,  -39,    0,    0,
    0,    0,    0,    0,    0,    0,   83,    0,    0,    0,
  -85,    0,    0,    0,  222,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   56,   78,  101,  123,    0,    0,
    0,    0,   12,    0,    0,    0,    0,  153,  175,  360,
  400,    0,    0,    0,  -18,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -39,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   52,    0,    0,    0,   53,    0,    0,   10,  -56,
    0,  393,    0,  -48,  421,    0,    0,  -97,  446,    0,
  -46,  -45,  -26,    0,    0,    0,    0,    0,    0,    0,
  286,    0,    0,    0,   61,    0,  306,    0,    0,    0,
    0,    0,  389,   74,    0,
};
final static int YYTABLESIZE=686;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        126,
   11,  208,  106,  199,  107,   50,   18,  126,  126,  126,
   79,  126,  154,  126,  117,  137,  121,  122,  125,   71,
  176,  177,   86,  126,    2,  126,  126,  126,  126,   40,
   52,  103,   54,  201,   15,  189,  123,   78,   86,   90,
   11,   41,   13,   82,  128,   37,   83,   36,   72,   55,
   46,   13,  128,  128,  128,    7,  128,   34,  128,   47,
   97,  186,   37,   61,  187,   98,  110,   39,   57,   43,
  128,  128,  128,  128,  110,    6,  110,  100,  110,   37,
  203,  192,   34,  101,  117,   68,  121,  122,  115,  126,
   57,   70,  110,  110,  110,  110,  115,   87,  115,   34,
  115,   13,   84,  142,   86,  106,  123,  107,   71,   38,
  111,   85,   57,   72,  115,  115,  115,  115,  111,   14,
  111,  104,  111,   82,   57,   11,   82,   79,  131,  117,
   80,  121,  122,  116,  128,   43,  111,  111,  111,  111,
  173,  116,  172,  116,  148,  116,  190,   33,   79,   79,
   79,  123,   79,  149,   81,  113,  110,  101,   39,  116,
  116,  116,  116,  113,   33,  113,    1,  113,   88,   57,
  160,  162,  106,   39,  107,   78,   78,   78,  115,   78,
  204,  113,  113,  113,  113,  112,   10,   11,   35,  205,
   12,   91,   92,  112,   93,  112,  194,  112,  181,  183,
  111,  207,   49,   18,  143,   35,  106,  108,  107,   99,
  124,  112,  112,  112,  112,  108,   71,  108,  198,  108,
  209,  210,  126,  116,  184,  126,  126,  126,  126,  126,
  185,  126,  153,  108,  108,  108,  108,  126,  126,  126,
  126,  126,   86,   86,   86,  113,   86,  102,  200,  132,
   76,    8,    9,   53,   57,   10,   11,   11,   11,   12,
   89,   11,   11,   13,  134,   11,   37,  128,   15,   11,
  128,  128,  128,  128,  128,  112,  128,  141,  140,   56,
   98,   15,  128,  128,  128,  128,  128,   58,   59,  110,
   60,   15,  110,  110,  110,  110,  110,  108,  110,  144,
  145,   56,  152,   34,  110,  110,  110,  110,  110,   58,
   59,  115,   60,   15,  115,  115,  115,  115,  115,  147,
  115,  150,   15,   56,   75,  164,  115,  115,  115,  115,
  115,   58,   59,  111,   60,  127,  111,  111,  111,  111,
  111,   57,  111,   58,   59,   57,   60,  165,  111,  111,
  111,  111,  111,   35,  101,   57,  116,  101,  101,  116,
  116,  116,  116,  116,  151,  116,   74,   57,   33,   99,
   39,  116,  116,  116,  116,  116,  212,   57,  113,   39,
  155,  113,  113,  113,  113,  113,  163,  113,   58,   59,
   57,   60,  114,  113,  113,  113,  113,  113,  136,  166,
  114,  174,  114,   66,  114,  188,   57,  191,  112,   35,
  100,  112,  112,  112,  112,  112,   94,  112,  114,  114,
  114,  114,  193,  112,  112,  112,  112,  112,  177,  195,
  108,   74,  109,  108,  108,  108,  108,  108,  197,  108,
  109,  215,  109,  115,  109,  108,  108,  108,  108,  108,
  202,  196,  106,  106,  107,  107,   77,   78,  109,  109,
  109,  109,   75,   75,  214,   56,   75,    5,    4,    1,
  113,  114,  112,   58,   59,  146,   60,   98,   73,   75,
   98,   98,  114,  156,  158,    8,    9,  128,  213,   10,
   11,  135,  206,   12,  169,  171,    0,   13,    8,    9,
    0,    0,   10,   11,    0,    0,   12,    0,    8,    9,
   13,    0,   10,   11,   97,   95,   12,   96,    0,   98,
   13,    0,  109,    0,    0,  167,    0,    0,    0,    0,
    8,    9,    0,    0,   10,   11,  175,    0,   12,    8,
    9,   66,   66,   10,   11,   66,    0,   12,    0,    0,
    0,    0,  157,    0,    0,    0,  159,    0,    0,    0,
   58,   59,    0,   60,   58,   59,  161,   60,    0,   74,
   74,    0,    0,   74,   58,   59,    0,   60,  168,    0,
    0,    0,    0,    0,    0,    0,   58,   59,  170,   60,
    0,    0,    0,    0,    0,    0,   58,   59,    0,   60,
    0,  180,    0,    0,    0,    0,    0,  128,    0,   58,
   59,    0,   60,    0,    0,  114,    0,  182,  114,  114,
  114,  114,  114,    0,  114,   58,   59,   94,   60,    0,
  114,  114,  114,  114,  114,   94,   94,    0,   94,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  109,    0,    0,  109,  109,
  109,  109,  109,    0,  109,    0,  105,    0,    0,    0,
  109,  109,  109,  109,  109,    0,    0,    0,    0,    0,
    0,    0,  108,  109,  110,  111,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
    0,   45,   43,   41,   45,   59,   59,   41,   42,   43,
  123,   45,   44,   47,   63,   72,   63,   63,   42,   59,
  259,  260,   41,   47,  123,   59,   60,   61,   62,  256,
   21,   40,   44,   41,   40,  133,   63,  123,  256,   44,
   40,  268,  269,   41,   33,   44,   44,   40,  123,   61,
  256,  269,   41,   42,   43,    4,   45,    6,   47,  265,
   42,   41,   61,   40,   44,   47,   33,   15,   45,   17,
   59,   60,   61,   62,   41,  123,   43,   40,   45,   40,
  178,  138,   44,   46,  133,   40,  133,  133,   33,  123,
   45,   31,   59,   60,   61,   62,   41,   45,   43,   61,
   45,  269,  256,   41,  123,   43,  133,   45,  266,   46,
   33,  265,   45,  123,   59,   60,   61,   62,   41,  125,
   43,   61,   45,   41,   45,  125,   44,  265,   68,  178,
  265,  178,  178,   33,  123,   83,   59,   60,   61,   62,
   60,   41,   62,   43,  256,   45,  137,   44,  261,  262,
  263,  178,  265,  265,  265,   33,  123,   59,   44,   59,
   60,   61,   62,   41,   61,   43,  265,   45,   40,   45,
   97,   98,   43,   59,   45,  261,  262,  263,  123,  265,
  256,   59,   60,   61,   62,   33,  261,  262,   44,  265,
  265,   46,  265,   41,  264,   43,  145,   45,  125,  126,
  123,  192,  256,  256,   41,   61,   43,   33,   45,  264,
   41,   59,   60,   61,   62,   41,  256,   43,  256,   45,
  264,  265,  256,  123,  265,  259,  260,  261,  262,  263,
  271,  265,  264,   59,   60,   61,   62,  271,  272,  273,
  274,  275,  261,  262,  263,  123,  265,  256,  256,  263,
   41,  257,  258,  265,   45,  261,  262,  257,  258,  265,
  265,  261,  262,  269,   41,  265,  265,  256,   40,  269,
  259,  260,  261,  262,  263,  123,  265,   41,  125,  256,
   59,   40,  271,  272,  273,  274,  275,  264,  265,  256,
  267,   40,  259,  260,  261,  262,  263,  123,  265,   41,
  123,  256,   46,  265,  271,  272,  273,  274,  275,  264,
  265,  256,  267,   40,  259,  260,  261,  262,  263,  265,
  265,  265,   40,  256,  123,  264,  271,  272,  273,  274,
  275,  264,  265,  256,  267,  256,  259,  260,  261,  262,
  263,   45,  265,  264,  265,   45,  267,  264,  271,  272,
  273,  274,  275,  125,  256,   45,  256,  259,  260,  259,
  260,  261,  262,  263,  265,  265,  125,   45,  265,  266,
  256,  271,  272,  273,  274,  275,  125,   45,  256,  265,
  256,  259,  260,  261,  262,  263,  265,  265,  264,  265,
   45,  267,   33,  271,  272,  273,  274,  275,  125,   41,
   41,   61,   43,  123,   45,   41,   45,  125,  256,  265,
  266,  259,  260,  261,  262,  263,   45,  265,   59,   60,
   61,   62,   40,  271,  272,  273,  274,  275,  260,  265,
  256,  123,   33,  259,  260,  261,  262,  263,   40,  265,
   41,   41,   43,   33,   45,  271,  272,  273,  274,  275,
   41,  264,   43,   43,   45,   45,   36,   37,   59,   60,
   61,   62,  261,  262,  264,  256,  265,    0,    0,    0,
   60,   61,   62,  264,  265,   83,  267,  256,   33,  270,
  259,  260,  123,   95,   96,  257,  258,   67,  203,  261,
  262,   71,  187,  265,  106,  107,   -1,  269,  257,  258,
   -1,   -1,  261,  262,   -1,   -1,  265,   -1,  257,  258,
  269,   -1,  261,  262,   42,   43,  265,   45,   -1,   47,
  269,   -1,  123,   -1,   -1,  105,   -1,   -1,   -1,   -1,
  257,  258,   -1,   -1,  261,  262,  116,   -1,  265,  257,
  258,  261,  262,  261,  262,  265,   -1,  265,   -1,   -1,
   -1,   -1,  256,   -1,   -1,   -1,  256,   -1,   -1,   -1,
  264,  265,   -1,  267,  264,  265,  256,  267,   -1,  261,
  262,   -1,   -1,  265,  264,  265,   -1,  267,  256,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,  256,  267,
   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,   -1,  267,
   -1,  256,   -1,   -1,   -1,   -1,   -1,  187,   -1,  264,
  265,   -1,  267,   -1,   -1,  256,   -1,  256,  259,  260,
  261,  262,  263,   -1,  265,  264,  265,  256,  267,   -1,
  271,  272,  273,  274,  275,  264,  265,   -1,  267,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  256,   -1,   -1,  259,  260,
  261,  262,  263,   -1,  265,   -1,  256,   -1,   -1,   -1,
  271,  272,  273,  274,  275,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  272,  273,  274,  275,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=275;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"'!'",null,null,null,null,null,null,"'('","')'","'*'","'+'",
"','","'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,
"';'","'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"WHILE","IF","ELSE","ENDIF","PRINT",
"RETURN","DO","CTE","ID","ASIG","TRUNC","CR","ULONG","CADENA","FLECHA","EQ",
"GEQ","LEQ","NEQ",
};
final static String yyrule[] = {
"$accept : programa",
"programa : nombre_programa '{' list_sentencia '}'",
"programa : nombre_programa '{' list_sentencia '}' '}'",
"programa : nombre_programa list_sentencia '}'",
"programa : nombre_programa '{' list_sentencia",
"programa : nombre_programa list_sentencia",
"programa : '{' list_sentencia '}'",
"nombre_programa : ID",
"declaracion_funcion : header_funcion parametros_formales ')' '{' list_sentencia '}'",
"header_funcion : tipo ID '('",
"header_funcion : tipo error",
"list_sentencia :",
"list_sentencia : list_sentencia sentencia",
"sentencia : sentencia_declarativa punto_y_coma",
"sentencia : sentencia_ejecutable punto_y_coma",
"sentencia : declaracion_funcion",
"punto_y_coma : ';'",
"punto_y_coma : error",
"sentencia_declarativa : tipo list_vars",
"parametros_formales : parametro_formal",
"parametros_formales : parametros_formales ',' parametro_formal",
"parametro_formal : semantica tipo ID",
"parametro_formal : tipo ID",
"parametro_formal : semantica tipo error",
"parametro_formal : tipo error",
"parametro_formal : semantica error ID",
"parametro_formal : error ID",
"semantica : CR",
"sentencia_return : RETURN '(' expresion ')'",
"tipo : ULONG",
"list_ctes : CTE",
"list_ctes : list_ctes ',' CTE",
"list_ctes : list_ctes CTE",
"list_vars_mix : ID",
"list_vars_mix : list_vars_mix ',' ID",
"list_vars_mix : ID '.' ID",
"list_vars_mix : list_vars_mix ',' ID '.' ID",
"list_vars_mix : list_vars_mix ID",
"list_vars_mix : list_vars_mix ID '.' ID",
"list_vars : ID",
"list_vars : list_vars ',' ID",
"list_vars : list_vars ID",
"cuerpo_ejecutable : bloque_ejecutable",
"cuerpo_ejecutable : sentencia_ejecutable_simple",
"sentencia_ejecutable_simple : asignacion_simple",
"sentencia_ejecutable_simple : asignacion_multiple",
"sentencia_ejecutable_simple : sentencia_return",
"sentencia_ejecutable_simple : sentencia_print",
"sentencia_ejecutable : sentencia_if",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_return",
"sentencia_ejecutable : expresion_lambda",
"sentencia_ejecutable : sentencia_while",
"sentencia_ejecutable : sentencia_print",
"bloque_ejecutable : '{' lista_sentencias_ejecutable '}'",
"bloque_ejecutable : '{' '}'",
"lista_sentencias_ejecutable : lista_sentencias_ejecutable sentencia_ejecutable punto_y_coma",
"lista_sentencias_ejecutable : sentencia_ejecutable punto_y_coma",
"sentencia_if : if condicion_if cuerpo_ejecutable else cuerpo_ejecutable end_if",
"sentencia_if : if condicion_if cuerpo_ejecutable end_if",
"sentencia_print : PRINT '(' CADENA ')'",
"sentencia_print : PRINT '(' expresion ')'",
"sentencia_print : PRINT '(' ')'",
"sentencia_while : while condicion_while do cuerpo_ejecutable",
"do : DO",
"do :",
"else : ELSE",
"if : IF",
"while : WHILE",
"end_if : ENDIF",
"end_if :",
"condicion_if : '(' condicion ')'",
"condicion_if : condicion ')'",
"condicion_if : '(' condicion",
"condicion_if : condicion",
"condicion_while : '(' condicion ')'",
"condicion_while : condicion ')'",
"condicion_while : '(' condicion",
"condicion_while : condicion",
"parametros_reales : parametro_real",
"parametros_reales : parametros_reales ',' parametro_real",
"parametros_reales : error",
"parametro_real : expresion FLECHA ID",
"parametro_real : expresion FLECHA error",
"parametro_real : expresion ID",
"condicion : expresion comp expresion",
"condicion : expresion error expresion ')'",
"comp : EQ",
"comp : GEQ",
"comp : LEQ",
"comp : NEQ",
"comp : '>'",
"comp : '<'",
"comp : '='",
"comp : '=' '<'",
"comp : '=' '>'",
"comp : '!' '='",
"asignacion_simple : var_asignacion_simple ASIG expresion",
"var_asignacion_simple : ID",
"var_asignacion_simple : ID '.' ID",
"asignacion_multiple : list_vars_mix '=' list_ctes",
"header_lambda : '(' tipo ID ')'",
"$$1 :",
"expresion_lambda : header_lambda bloque_ejecutable $$1 '(' factor_lambda ')'",
"factor_lambda : ID",
"factor_lambda : CTE",
"factor_lambda : '-' CTE",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"expresion : error '+' termino",
"expresion : expresion '+' error",
"expresion : error '-' termino",
"expresion : expresion '-' error",
"expresion : error '+' error",
"expresion : error '-' error",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : error '*' factor",
"termino : termino '*' error",
"termino : error '/' factor",
"termino : termino '/' error",
"termino : error '*' error",
"termino : error '/' error",
"termino : factor",
"factor : ID",
"factor : CTE",
"factor : ID '.' ID",
"factor : inicio_llamado parametros_reales ')'",
"factor : '-' CTE",
"factor : TRUNC '(' CTE ')'",
"factor : TRUNC '(' CTE error",
"factor : TRUNC error CTE ')'",
"factor : TRUNC error CTE error",
"inicio_llamado : ID '('",
"inicio_llamado : ID '.' ID '('",
};

//#line 384 "Gramatica.y"
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

    if (!"Variable".equals(elem.getUso())) {
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
//#line 895 "Parser.java"
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
case 1:
//#line 27 "Gramatica.y"
{ reportarEstructura("Fin del programa");}
break;
case 2:
//#line 28 "Gramatica.y"
{ yyerror("Error: '}' de mas al final de programa");}
break;
case 3:
//#line 29 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '{' al inicio"); }
break;
case 4:
//#line 30 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '}' al final"); }
break;
case 5:
//#line 31 "Gramatica.y"
{ yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
break;
case 6:
//#line 32 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del programa"); }
break;
case 7:
//#line 35 "Gramatica.y"
{ambito = val_peek(0).sval; prog_principal = val_peek(0).sval;}
break;
case 8:
//#line 38 "Gramatica.y"
{ reportarEstructura("declaracion de funcion");
                                                                                            String etiquetaFin = "fin_" + ambito;
                                                                                            ArregloTercetos.crearTerceto(etiquetaFin, "_", "_");
                                                                                            salirAmbito();
                                                                                            chequearReturn();
                                                                                          }
break;
case 9:
//#line 46 "Gramatica.y"
{
                                 declaracionDeFuncion(val_peek(1).sval, ambito, "Función");
                                 entrarAmbito(val_peek(1).sval);
                                 pilaReturns.push(val_peek(1).sval + ":" + "false");
                                 String etiquetaInicio = "ini_" + ambito ;
                                 ArregloTercetos.crearTerceto(etiquetaInicio, "_", "_");
                                }
break;
case 10:
//#line 53 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre a la función"); }
break;
case 17:
//#line 66 "Gramatica.y"
{ yyerror("Error: falta ';' al final de la sentencia o sentencia invalida escrita"); }
break;
case 18:
//#line 70 "Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 21:
//#line 77 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cr");}
break;
case 22:
//#line 78 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cv");}
break;
case 23:
//#line 79 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 24:
//#line 80 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 25:
//#line 81 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 26:
//#line 82 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 28:
//#line 88 "Gramatica.y"
{registrarReturn();
                                                          String varRetorno = "_ret_" + ambito;
                                                          ArregloTercetos.crearTerceto(":=", varRetorno, val_peek(1).sval);
                                                          ArregloTercetos.crearTerceto("RETURN", varRetorno, val_peek(1).sval);
                                                          ArregloTercetos.crearTerceto("JMP", "fin_" + ambito, null);
                                             }
break;
case 29:
//#line 96 "Gramatica.y"
{tipo = "ulong";}
break;
case 30:
//#line 99 "Gramatica.y"
{ yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); ControlAsigMultiple.pushTipoDer(yyval.tipo); yyval.sval = val_peek(0).sval; }
break;
case 31:
//#line 100 "Gramatica.y"
{ yyval.tipo = val_peek(2).tipo; String t = obtenerTipoDeSimbolo(val_peek(0).sval); ControlAsigMultiple.pushTipoDer(t); yyval.sval = val_peek(2).sval + "," + val_peek(0).sval; }
break;
case 32:
//#line 101 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre constantes"); yyval = val_peek(1); }
break;
case 33:
//#line 104 "Gramatica.y"
{ String clave = chequearAmbito("", ambito, val_peek(0).sval).sval; yyval.sval = clave; yyval.tipo = obtenerTipoDeSimbolo(clave); ControlAsigMultiple.pushTipoIzq(yyval.tipo); }
break;
case 34:
//#line 105 "Gramatica.y"
{ String clave = chequearAmbito("", ambito, val_peek(0).sval).sval; yyval.tipo = val_peek(2).tipo; ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(clave)); yyval.sval = val_peek(2).sval + "," + clave; }
break;
case 35:
//#line 106 "Gramatica.y"
{ String clave = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval).sval; yyval.sval = clave; yyval.tipo = obtenerTipoDeSimbolo(clave); ControlAsigMultiple.pushTipoIzq(yyval.tipo); }
break;
case 36:
//#line 107 "Gramatica.y"
{ String clave = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval).sval; yyval.tipo = val_peek(4).tipo; ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(clave)); yyval.sval = val_peek(4).sval + "," + clave; }
break;
case 37:
//#line 108 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); yyval = val_peek(1); }
break;
case 38:
//#line 109 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); yyval = val_peek(3); }
break;
case 39:
//#line 112 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 40:
//#line 113 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 41:
//#line 114 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 56:
//#line 143 "Gramatica.y"
{yyerror("Error: Cuerpo ejecutable vacio");}
break;
case 59:
//#line 150 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 60:
//#line 151 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 61:
//#line 154 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 62:
//#line 155 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 63:
//#line 156 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 64:
//#line 159 "Gramatica.y"
{
                            reportarEstructura("WHILE");
                            yyval = ArregloTercetos.completarBackPatchingWHILE();
                            ArregloTercetos.crearTerceto("WHILE_END", "_", "_");
                      }
break;
case 66:
//#line 168 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'do' en estructura while");}
break;
case 67:
//#line 171 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIFDesapilaryCompletar("bl", null,null);}
break;
case 68:
//#line 174 "Gramatica.y"
{ArregloTercetos.crearTerceto("IF_START", "_", "_");}
break;
case 69:
//#line 177 "Gramatica.y"
{
                                ArregloTercetos.apilarTercetoInicialWHILE();

                                ArregloTercetos.crearTerceto("WHILE_START", "_", "_");
                              }
break;
case 70:
//#line 184 "Gramatica.y"
{ArregloTercetos.completarTercetoBackPatchingIF();}
break;
case 71:
//#line 185 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 72:
//#line 188 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 73:
//#line 189 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 74:
//#line 190 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(0).sval,null);}
break;
case 75:
//#line 191 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(0).sval,null);}
break;
case 76:
//#line 194 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 77:
//#line 195 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 78:
//#line 196 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(0).sval,null);}
break;
case 79:
//#line 197 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(0).sval,null);}
break;
case 82:
//#line 204 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 83:
//#line 207 "Gramatica.y"
{
                                                String claveFuncionSinCorregir = PilaDeFuncionesLlamadas.getTopeActual();

                                                int primerSeparador = claveFuncionSinCorregir.indexOf(":");

                                                String nombreFuncion;
                                                String ambitoContenedor;
                                                String ambitoDeclaracionParametro;

                                                if (primerSeparador == -1) {
                                                    /* Caso de error: La pila contiene solo el nombre simple (ej: "C")*/
                                                    /* No se puede reconstruir el ámbito de declaración. Asumimos el ámbito actual como contenedor.*/

                                                    nombreFuncion = claveFuncionSinCorregir; /* "C"*/
                                                    /* Esta es la parte débil: ¿Cuál es el ámbito contenedor?*/
                                                    /* Si la llamada es C(...) desde el main, el ámbito es "PROGRAMAFUNCIONAL".*/
                                                    /* Usaremos la variable 'ambito' o un valor conocido:*/
                                                    ambitoContenedor = prog_principal; /* Reemplaza por la forma correcta de obtener el ámbito global si es diferente*/

                                                    /* Construimos el ámbito de declaración: PROGRAMAFUNCIONAL:C*/
                                                    ambitoDeclaracionParametro = ambitoContenedor + ":" + nombreFuncion;
                                                } else {
                                                    /* Caso normal: Clave canónica (ej: C:PROGRAMAFUNCIONAL)*/
                                                    nombreFuncion = claveFuncionSinCorregir.substring(0, primerSeparador);
                                                    ambitoContenedor = claveFuncionSinCorregir.substring(primerSeparador + 1);
                                                    ambitoDeclaracionParametro = ambitoContenedor + ":" + nombreFuncion;
                                                }
                                                /* ----------------------------------------------------------------------*/

                                                /* 4. Construir la clave de búsqueda del parámetro (Ej: TEST:PROGRAMAFUNCIONAL:C)*/
                                                String paramFormal = val_peek(0).sval + ":" + ambitoDeclaracionParametro;

                                                String tipoFormal = obtenerTipoDeSimbolo(paramFormal);

                                                yyval.tipo = chequearTipos(val_peek(2).tipo, tipoFormal, "->");
                                                realizarPasajeCopiaValor(paramFormal, val_peek(2).sval);
                                                if (esParametroCR(paramFormal)) {
                                                    registrarVinculoCR(paramFormal, val_peek(2).sval);
                                                }
                                            }
break;
case 84:
//#line 247 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 85:
//#line 248 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 86:
//#line 251 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, val_peek(1).sval); }
break;
case 87:
//#line 252 "Gramatica.y"
{ yyerror("Error: falta operador de comparacion en la condicion"); }
break;
case 88:
//#line 255 "Gramatica.y"
{ yyval.sval = "=="; }
break;
case 89:
//#line 256 "Gramatica.y"
{ yyval.sval = ">="; }
break;
case 90:
//#line 257 "Gramatica.y"
{ yyval.sval = "<="; }
break;
case 91:
//#line 258 "Gramatica.y"
{ yyval.sval = "=!"; }
break;
case 92:
//#line 259 "Gramatica.y"
{ yyval.sval = ">"; }
break;
case 93:
//#line 260 "Gramatica.y"
{ yyval.sval = "<"; }
break;
case 94:
//#line 261 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 95:
//#line 262 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 96:
//#line 263 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 97:
//#line 264 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 98:
//#line 268 "Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                               yyval = ArregloTercetos.crearTerceto(":=", val_peek(2).sval, val_peek(0).sval);
                                                               yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, ":=");
                                                               }
break;
case 99:
//#line 274 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 100:
//#line 275 "Gramatica.y"
{yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(2).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 101:
//#line 279 "Gramatica.y"
{
                              reportarEstructura("asignacion multiple");
                              ControlAsigMultiple.compararTipos();
                              crearTercetosAsigMultiple(val_peek(2).sval, val_peek(0).sval);
                            }
break;
case 102:
//#line 287 "Gramatica.y"
{
                             reportarEstructura("expresión lambda");
                             contadorLambda++;
                             String nombreLambda = "_lambda" + contadorLambda;
                             String claveLambda  = nombreLambda + ":" + ambito;
                             ParserValExt paramFormal = registrarParametroFuncion(val_peek(1).sval, "cv");
                             ArregloTercetos.crearTerceto("ini_" + claveLambda, "_", "_");
                             yyval.sval = claveLambda;
                             yyval.tipo = paramFormal.sval;
                           }
break;
case 103:
//#line 300 "Gramatica.y"
{
                              ArregloTercetos.crearTerceto("fin_" + val_peek(1).sval, "_", "_");
                            }
break;
case 104:
//#line 304 "Gramatica.y"
{
                              String tipoFormal = obtenerTipoDeSimbolo(val_peek(5).tipo);
                              chequearTipos(tipoFormal, val_peek(0).tipo, ":=");
                              ArregloTercetos.crearTerceto(":=", val_peek(5).tipo, val_peek(0).sval);
                              ArregloTercetos.crearTerceto("CALL", val_peek(5).sval, null);
                            }
break;
case 105:
//#line 313 "Gramatica.y"
{ yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 106:
//#line 314 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); }
break;
case 107:
//#line 315 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 108:
//#line 318 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("+", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "+"); }
break;
case 109:
//#line 319 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("-", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "-");}
break;
case 110:
//#line 320 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 111:
//#line 321 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 112:
//#line 322 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 113:
//#line 323 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 114:
//#line 324 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 115:
//#line 325 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 116:
//#line 326 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 117:
//#line 329 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("*", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "*"); }
break;
case 118:
//#line 330 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("/", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "/");}
break;
case 119:
//#line 331 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 120:
//#line 332 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 121:
//#line 333 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 122:
//#line 334 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 123:
//#line 335 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 124:
//#line 336 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 125:
//#line 337 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 126:
//#line 340 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 127:
//#line 341 "Gramatica.y"
{yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); yyval = val_peek(0); }
break;
case 128:
//#line 342 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 129:
//#line 343 "Gramatica.y"
{
                                                       ParserValExt tCall = ArregloTercetos.crearTerceto("CALL", val_peek(2).sval, null);
                                                       String tipoRet = obtenerTipoDeSimbolo(val_peek(2).sval);
                                                       yyval.tipo = tipoRet;
                                                       String temp = "_t" + ArregloTercetos.declararTemporal(tipoRet, ambito);
                                                       declaracionDeVariable(temp, tipoRet, ambito, "temporal");
                                                       ArregloTercetos.crearTerceto(":=", temp, tCall.sval);
                                                       yyval.sval = temp;
                                                       PilaDeFuncionesLlamadas.finalizarLlamada();
                                                       realizarPasajesCopiaResultado();
                                         }
break;
case 130:
//#line 355 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval);}
break;
case 131:
//#line 356 "Gramatica.y"
{       String tipo = obtenerTipoDeSimbolo(val_peek(1).sval);
                                                  if(! tipo.equals("dfloat")){
                                                      RecolectorDeErrores.agregarError("Error: La funcion TRUNC solo acepta operandos de tipo dfloat.",AnalizadorLexico.getNumeroDeLinea());
                                                  }
                                                  yyval = ArregloTercetos.crearTerceto("TRUNC", val_peek(1).sval, null);
                                                  yyval.tipo = "ulong";
                      }
break;
case 132:
//#line 363 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 133:
//#line 364 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 134:
//#line 365 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 135:
//#line 368 "Gramatica.y"
{
                             /* $$ obtiene la clave canónica (ej: C:PROGRAMAFUNCIONAL)*/
                             yyval = chequearAmbito("", ambito, val_peek(1).sval);
                             /* CORRECCIÓN 1: Usar $$.sval*/
                             PilaDeFuncionesLlamadas.iniciarLlamada(yyval.sval);
                           }
break;
case 136:
//#line 374 "Gramatica.y"
{
                             /* $$ obtiene la clave canónica (ej: C:PROGRAMAFUNCIONAL)*/
                             yyval = chequearAmbito(val_peek(3).sval, ambito, val_peek(1).sval);
                             /* CORRECCIÓN 2: Usar $$.sval*/
                             PilaDeFuncionesLlamadas.iniciarLlamada(yyval.sval);
                                      }
break;
//#line 1588 "Parser.java"
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
