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
import java.util.HashMap;
import java.util.Map;

//#line 31 "Parser.java"




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
public final static short COMP=270;
public final static short CADENA=271;
public final static short FLECHA=272;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    3,    4,    4,    2,
    2,    2,    2,    2,    2,    7,    5,    5,   10,   10,
   10,   10,   10,   10,   11,   12,    6,   14,   14,   14,
   15,   15,   15,   15,   15,   15,    9,    9,    9,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,   18,   22,   19,   19,   16,   16,   16,   16,   23,
   23,   23,   23,   17,   17,   17,   17,   17,   25,   25,
   25,   26,   26,   26,   24,   20,   27,   27,   21,   28,
   28,   28,   28,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   30,   30,   30,
   30,   30,   30,   30,   30,   30,   29,   29,   29,   29,
   29,   31,
};
final static short yylen[] = {                            2,
    4,    3,    3,    2,    0,    2,    7,    2,    2,    2,
    2,    1,    2,    2,    2,    2,    1,    3,    3,    2,
    3,    2,    3,    2,    1,    2,    1,    1,    3,    2,
    1,    3,    3,    5,    2,    4,    1,    3,    2,   10,
    6,    8,    5,    9,    9,    4,    4,    1,    1,    1,
    6,    4,    3,    5,    3,    5,    3,    9,    9,    8,
    5,    6,    6,    4,    3,    8,    7,    6,    7,    8,
    6,    1,    1,    1,    0,    3,    2,    2,    1,    3,
    2,    2,    1,    2,    3,    2,    2,    3,    1,    3,
    1,    3,    3,    2,    3,    3,    1,    3,    3,   10,
    9,    9,    8,    3,    3,    1,    3,    3,    3,    3,
    3,    3,    4,    4,    4,    4,    1,    3,    3,    3,
    3,    3,    3,    3,    3,    1,    1,    1,    3,    4,
    2,    1,
};
final static short yydefred[] = {                         0,
    0,    5,    0,    5,    0,    0,    0,    0,   73,    0,
    0,    0,    0,   27,    3,    6,   12,    0,    0,    0,
    0,   50,    0,   48,   49,    0,    0,    2,    1,   15,
    0,    0,  128,    0,    0,    0,    0,    0,    0,  117,
  126,    0,    0,    0,    0,    0,    0,    0,    9,    0,
    0,   13,   10,   14,   11,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  131,    0,    0,    0,
    0,    0,    0,    0,    0,   72,   74,    0,    0,    0,
   65,   77,    0,    0,    0,    0,   55,    0,    0,    0,
   25,    0,    0,   17,    0,   39,    0,    0,    0,   28,
    0,    0,    0,    0,   57,   81,    0,    0,    0,    0,
    0,  124,  120,  125,  122,  129,    0,    0,    0,   76,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   64,  121,  118,  123,  119,    0,    0,    0,   89,
   46,   47,   24,    0,    0,   22,   20,    0,    0,   38,
   36,    0,   30,    0,   80,    0,   52,    0,  116,  115,
  114,  113,    0,    0,   61,   87,   84,   86,    0,    0,
    0,   43,    0,    0,    0,   94,    0,  130,    0,    5,
   18,   23,   21,   19,   34,   29,   54,    0,   56,    0,
    0,    0,    0,   68,    0,   41,   88,   85,    0,    0,
   63,   71,    0,   62,   93,   92,   90,    0,   51,    0,
    0,    0,    0,    0,    0,    0,    0,   67,    0,    0,
    0,   69,    7,    0,    0,    0,    0,   60,    0,   66,
    0,    0,    0,   70,    0,   42,    0,    0,    0,  103,
   58,   59,    0,   44,   45,    0,  102,  101,   40,  100,
};
final static short yydgoto[] = {                          3,
    5,   16,   17,   18,   92,   19,   20,  127,   51,   94,
   95,   22,   37,  101,   23,   38,  128,   80,   81,   24,
   25,   26,   60,   39,  139,  140,   27,   40,   41,   42,
   43,
};
final static short yysindex[] = {                      -123,
 -113,    0,    0,    0,  -88,  395,  451,  -12,    0,  167,
   -1,  171,    5,    0,    0,    0,    0,   47, -206,  -53,
  -46,    0,  -18,    0,    0,  188, -160,    0,    0,    0,
   73, -168,    0,   64,    8,  157,  -16, -108,   89,    0,
    0,   51,   94,  147, -134,   96, -122, -224,    0,    0,
  -36,    0,    0,    0,    0,  101, -107, -104,  157,  368,
  123,  171,   76,  185,  224,  253,    0,  -99,  171,  171,
  -98,  130,  268,  270,  171,    0,    0,  586,  116,  436,
    0,    0,  273,  285,  208,  135,    0,   26,    0,  -82,
    0,   56, -195,    0, -215,    0,  -81,  -80,  140,    0,
  -33,  150,  525, -125,    0,    0,   96,   70,   51,   70,
   51,    0,    0,    0,    0,    0,   23,   60,  152,    0,
   70,   51,   70,   51,   96, -177,  -43,  209, -177,  592,
  136,    0,    0,    0,    0,    0,   73,  -20,   78,    0,
    0,    0,    0,   71, -224,    0,    0,  -67, -184,    0,
    0,  -66,    0,  -64,    0,  601,    0,  415,    0,    0,
    0,    0,  527,  472,    0,    0,    0,    0, -177,  -42,
  487,    0,  -59,  537,  -59,    0, -170,    0,  171,    0,
    0,    0,    0,    0,    0,    0,    0,  550,    0, -125,
  225,  611,  145,    0,  496,    0,    0,    0,  613,  146,
    0,    0,  -59,    0,    0,    0,    0,  466,    0,  242,
  166,   35,  -59,  560,  -59,  625,  149,    0,  -59,  570,
  -59,    0,    0,  177,   35,   35,  262,    0,  -59,    0,
  -59,  580,  -59,    0,  -59,    0,   35,  276,  279,    0,
    0,    0,  -59,    0,    0,  292,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    1,    0,    0,    0,  335,    0,    0,    0,    0,    0,
    0,    0,    2,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -38,    0,    0,    0,  -41,  325,    0,
    0,  -21,    0,    0,    0,   -6,    0,    0,    0,  -10,
   25,    0,    0,    0,    0,   -9,    0,    0,    0,    0,
  505,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  356,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,   18,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   -4,    0,
   30,  516,   40,    0,    0,    0,   45,   15,   32,   49,
   66,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   86,  103,  120,  137,  346,  -41,    0,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,  115,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -41,    0,    0,    0,    0,  -41,    0,
  -41,    0,  -41,    0,  -41,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -41,    0,    0,    0,    0,    0,
    0,    0,  -41,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -41,    0,  -41,    0,    0,    0,  -41,    0,
  -41,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
  -41,    0,  -41,    0,  -41,    0,    0,    0,    0,    0,
    0,    0,  -41,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   10,    0,    0,    0,    0,  -17,    0,  348,    0,  192,
    0,    0,  503,    0,    0,    0,  725,  -93,  471,    0,
    0,    0,    0,   29,    0,  160,    0,    0,  412,   50,
    0,
};
final static int YYTABLESIZE=941;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                          2,
    5,  132,  127,  127,  127,   53,  127,   97,  127,    4,
  154,    6,   55,    7,   78,  167,  198,   75,   71,  106,
  127,  106,   73,  106,   74,   57,   73,   71,   74,    8,
   93,   90,  164,   37,   35,  171,   15,  106,   44,   32,
  148,   71,   58,   91,   14,   31,   30,   70,   37,   49,
   47,   35,   26,   14,   61,  111,   32,  111,   50,  111,
  146,   33,   31,  160,   72,   73,  142,   74,   73,  147,
   74,  183,  107,  111,  107,  195,  107,  149,   33,   32,
  184,   76,   77,   16,  127,  205,   48,  102,   99,  112,
  107,  112,   83,  112,  206,   67,  144,   84,   53,  145,
  162,  106,   73,   96,   74,   62,  109,  112,  109,   68,
  109,   65,  109,  111,   65,   63,   66,   64,  178,   66,
   32,  179,  122,  124,  109,    5,  108,   93,  108,   82,
  108,    9,   10,   85,   14,   11,   12,  111,   73,   13,
   74,    1,   89,  104,  108,  104,   98,  104,    9,   10,
   76,   77,   11,   12,  107,   91,   13,   99,   91,  100,
  110,  104,  110,  106,  110,  116,  119,    8,    9,   10,
  120,  112,   11,   12,  129,  141,   13,  105,  110,  105,
   14,  105,  143,  150,  151,  152,   45,   87,  109,  208,
  155,   32,  163,  180,  175,  105,   45,  182,  185,  186,
   77,   32,   52,  215,  221,  226,   36,  233,  108,   54,
   45,   32,  166,  197,   75,   32,  237,  127,  127,  127,
  127,  127,  127,  127,  127,  104,  127,   59,   96,   32,
  153,  127,   32,  127,  106,  106,  106,  106,  106,  106,
  106,  106,  110,  106,  176,   37,   56,   45,  106,   26,
  106,  177,   32,   75,   37,   35,    5,    5,    5,  105,
   32,    5,    5,   69,  212,    5,   31,   97,   32,    5,
  111,  111,  111,  111,  111,  111,  111,  111,  159,  111,
   16,  225,   33,   98,  111,   99,  111,  107,  107,  107,
  107,  107,  107,  107,  107,   53,  107,   32,   33,   34,
   96,  107,  240,  107,  112,  112,  112,  112,  112,  112,
  112,  112,   32,  112,   32,  161,  247,   32,  112,  248,
  112,  109,  109,  109,  109,  109,  109,  109,  109,   32,
  109,  108,  250,  169,    4,  109,  181,  109,  207,   33,
   34,  108,  108,  108,  108,  108,  108,  108,  108,  211,
  108,    0,   21,   21,   21,  108,    0,  108,  104,  104,
  104,  104,  104,  104,  104,  104,  224,  104,    0,    0,
    0,    0,  104,    0,  104,  110,  110,  110,  110,  110,
  110,  110,  110,   79,  110,   79,   95,    0,    0,  110,
    0,  110,  105,  105,  105,  105,  105,  105,  105,  105,
    0,  105,   31,    0,   95,    0,  105,  105,  105,    0,
   33,   34,   31,   35,   78,    0,    0,   86,    0,    0,
   33,   34,   31,   35,    0,   14,   31,  131,    0,    0,
   33,   34,    0,   35,   33,   34,    0,   35,    0,    0,
  110,    0,    0,   31,    0,    0,    0,   79,   33,   34,
  157,   33,   34,    0,   35,    0,    0,    0,    0,    0,
    0,    0,    0,  137,  168,    9,   10,    0,   95,   11,
   12,   33,   34,   13,   35,  170,  113,  115,   78,  112,
  168,    9,   10,    0,    0,   11,   12,   33,   34,   13,
  104,    0,    0,    0,  134,  136,    0,  168,    9,   10,
    0,    0,   11,   12,    0,  170,   13,    0,  114,    0,
    0,  193,    0,    0,   46,    0,   33,   34,  200,   28,
    0,  170,    0,  121,    0,  123,    0,    0,  133,    0,
    0,   33,   34,   33,   34,  170,   33,   34,  170,  189,
  135,    0,  217,    0,    0,    0,   88,    0,   33,   34,
  132,    0,    0,    0,    0,   21,    0,  170,  130,    0,
    0,  170,    0,    0,  107,    0,    0,  170,    0,    0,
    0,  117,  118,    0,    0,   29,    0,  125,    0,  170,
   79,   79,   79,   79,   79,   79,   79,  138,    0,   79,
  223,    0,    0,    0,  192,    0,  165,    0,    0,  172,
    0,   95,   95,   95,   95,   95,   95,   95,   95,  199,
   95,   78,   78,   78,   78,   78,   78,   78,  216,    0,
   78,    0,    0,  227,    9,   10,    0,   83,   11,   12,
  103,    0,   13,    0,  194,    0,  238,  239,   82,  196,
    0,  201,    0,  202,    0,  204,    0,  156,  246,  190,
    8,    9,   10,    0,    0,   11,   12,    0,    0,   13,
    0,  203,    0,   14,    0,  218,    0,    0,    0,    0,
  168,    9,   10,  222,  209,   11,   12,    0,    0,   13,
    0,  138,    0,  228,  229,  230,    0,    0,    0,  234,
    0,  236,    9,   10,  235,   77,   11,   12,    0,  241,
   13,  242,    0,  244,  243,  245,    8,    9,   10,    0,
  126,   11,   12,  249,    0,   13,  173,    0,    0,   14,
    0,    8,    9,   10,    0,  187,   11,   12,    9,   10,
   13,   77,   11,   12,   14,  213,   13,  219,    0,    0,
    0,    0,    0,    9,   10,    0,   77,   11,   12,  231,
    0,   13,    9,   10,    0,   77,   11,   12,    0,    0,
   13,   83,   83,    0,    0,   83,   83,   83,    0,   83,
    0,    0,   82,   82,    0,    0,   82,   82,   82,    0,
   82,    9,   10,    9,   10,   11,   12,   11,   12,   13,
    0,   13,  168,    9,   10,    0,    0,   11,   12,    0,
    0,   13,    0,    0,    0,  168,    9,   10,    0,    0,
   11,   12,    0,    0,   13,  168,    9,   10,    0,    0,
   11,   12,    0,    0,   13,  168,    9,   10,  158,    0,
   11,   12,    0,    0,   13,  168,    9,   10,    0,    0,
   11,   12,    9,   10,   13,    0,   11,   12,    9,   10,
   13,    0,   11,   12,  174,    0,   13,    9,   10,    0,
    0,   11,   12,    0,    0,   13,    0,    9,   10,    9,
   10,   11,   12,   11,   12,   13,    0,   13,    0,    0,
  188,    9,   10,    0,    0,   11,   12,  191,    0,   13,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  210,    0,  214,    0,    0,    0,
    0,    0,    0,  220,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  232,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                        123,
    0,   40,   41,   42,   43,   59,   45,   44,   47,  123,
   44,    2,   59,    4,  123,   59,   59,   59,   36,   41,
   59,   43,   43,   45,   45,   44,   43,   45,   45,   40,
   48,  256,  126,   44,   44,  129,  125,   59,   40,   44,
  256,   59,   61,  268,  269,   44,   59,   40,   59,  256,
   46,   61,   59,  269,   26,   41,   61,   43,  265,   45,
  256,   44,   61,   41,   36,   43,   41,   45,   43,  265,
   45,  256,   41,   59,   43,  169,   45,   95,   61,   45,
  265,  259,  260,   59,  123,  256,   40,   59,   59,   41,
   59,   43,   42,   45,  265,  264,   41,   47,   59,   44,
   41,  123,   43,   59,   45,  266,   41,   59,   43,   46,
   45,   42,   63,   64,   42,   43,   47,   45,   41,   47,
   45,   44,   73,   74,   59,  125,   41,  145,   43,   41,
   45,  257,  258,   40,  269,  261,  262,  123,   43,  265,
   45,  265,  265,   41,   59,   43,   46,   45,  257,  258,
  259,  260,  261,  262,  123,   41,  265,  265,   44,  264,
   41,   59,   43,   41,   45,  265,  265,  256,  257,  258,
   41,  123,  261,  262,   59,   41,  265,   41,   59,   43,
  269,   45,  265,  265,  265,   46,   40,   41,  123,  180,
   41,   45,   41,  123,   59,   59,   40,  265,  265,  264,
  260,   45,  256,   59,   59,   40,   40,   59,  123,  256,
   40,   45,  256,  256,  256,   45,   40,  256,  257,  258,
  259,  260,  261,  262,  263,  123,  265,   40,  265,   45,
  264,  270,   45,  272,  256,  257,  258,  259,  260,  261,
  262,  263,  123,  265,  265,  256,  265,   40,  270,  256,
  272,  272,   45,  270,  265,  265,  256,  257,  258,  123,
  265,  261,  262,  256,   40,  265,  265,  266,   45,  269,
  256,  257,  258,  259,  260,  261,  262,  263,  256,  265,
  256,   40,  265,  266,  270,  256,  272,  256,  257,  258,
  259,  260,  261,  262,  263,  256,  265,   45,  264,  265,
  256,  270,   41,  272,  256,  257,  258,  259,  260,  261,
  262,  263,   45,  265,   45,  256,   41,   45,  270,   41,
  272,  256,  257,  258,  259,  260,  261,  262,  263,   45,
  265,  256,   41,  125,    0,  270,  145,  272,  179,  264,
  265,  256,  257,  258,  259,  260,  261,  262,  263,  125,
  265,   -1,    5,    6,    7,  270,   -1,  272,  256,  257,
  258,  259,  260,  261,  262,  263,  125,  265,   -1,   -1,
   -1,   -1,  270,   -1,  272,  256,  257,  258,  259,  260,
  261,  262,  263,   59,  265,   38,   41,   -1,   -1,  270,
   -1,  272,  256,  257,  258,  259,  260,  261,  262,  263,
   -1,  265,  256,   -1,   59,   -1,  270,   60,  272,   -1,
  264,  265,  256,  267,   59,   -1,   -1,  271,   -1,   -1,
  264,  265,  256,  267,   -1,  269,  256,   80,   -1,   -1,
  264,  265,   -1,  267,  264,  265,   -1,  267,   -1,   -1,
  256,   -1,   -1,  256,   -1,   -1,   -1,  123,  264,  265,
  103,  264,  265,   -1,  267,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  256,  256,  257,  258,   -1,  123,  261,
  262,  264,  265,  265,  267,  128,   65,   66,  123,  256,
  256,  257,  258,   -1,   -1,  261,  262,  264,  265,  265,
  123,   -1,   -1,   -1,   83,   84,   -1,  256,  257,  258,
   -1,   -1,  261,  262,   -1,  158,  265,   -1,  256,   -1,
   -1,  164,   -1,   -1,   12,   -1,  264,  265,  171,  125,
   -1,  174,   -1,  256,   -1,  256,   -1,   -1,  256,   -1,
   -1,  264,  265,  264,  265,  188,  264,  265,  191,  125,
  256,   -1,  195,   -1,   -1,   -1,   44,   -1,  264,  265,
   80,   -1,   -1,   -1,   -1,  208,   -1,  210,  123,   -1,
   -1,  214,   -1,   -1,   62,   -1,   -1,  220,   -1,   -1,
   -1,   69,   70,   -1,   -1,  125,   -1,   75,   -1,  232,
  256,  257,  258,  259,  260,  261,  262,   85,   -1,  265,
  125,   -1,   -1,   -1,  123,   -1,  126,   -1,   -1,  129,
   -1,  256,  257,  258,  259,  260,  261,  262,  263,  123,
  265,  256,  257,  258,  259,  260,  261,  262,  123,   -1,
  265,   -1,   -1,  212,  257,  258,   -1,  123,  261,  262,
  263,   -1,  265,   -1,  164,   -1,  225,  226,  123,  169,
   -1,  171,   -1,  173,   -1,  175,   -1,  123,  237,  123,
  256,  257,  258,   -1,   -1,  261,  262,   -1,   -1,  265,
   -1,  125,   -1,  269,   -1,  195,   -1,   -1,   -1,   -1,
  256,  257,  258,  203,  125,  261,  262,   -1,   -1,  265,
   -1,  179,   -1,  213,  125,  215,   -1,   -1,   -1,  219,
   -1,  221,  257,  258,  125,  260,  261,  262,   -1,  229,
  265,  231,   -1,  233,  125,  235,  256,  257,  258,   -1,
  125,  261,  262,  243,   -1,  265,  125,   -1,   -1,  269,
   -1,  256,  257,  258,   -1,  125,  261,  262,  257,  258,
  265,  260,  261,  262,  269,  125,  265,  125,   -1,   -1,
   -1,   -1,   -1,  257,  258,   -1,  260,  261,  262,  125,
   -1,  265,  257,  258,   -1,  260,  261,  262,   -1,   -1,
  265,  257,  258,   -1,   -1,  261,  262,  263,   -1,  265,
   -1,   -1,  257,  258,   -1,   -1,  261,  262,  263,   -1,
  265,  257,  258,  257,  258,  261,  262,  261,  262,  265,
   -1,  265,  256,  257,  258,   -1,   -1,  261,  262,   -1,
   -1,  265,   -1,   -1,   -1,  256,  257,  258,   -1,   -1,
  261,  262,   -1,   -1,  265,  256,  257,  258,   -1,   -1,
  261,  262,   -1,   -1,  265,  256,  257,  258,  104,   -1,
  261,  262,   -1,   -1,  265,  256,  257,  258,   -1,   -1,
  261,  262,  257,  258,  265,   -1,  261,  262,  257,  258,
  265,   -1,  261,  262,  130,   -1,  265,  257,  258,   -1,
   -1,  261,  262,   -1,   -1,  265,   -1,  257,  258,  257,
  258,  261,  262,  261,  262,  265,   -1,  265,   -1,   -1,
  156,  257,  258,   -1,   -1,  261,  262,  163,   -1,  265,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  190,   -1,  192,   -1,   -1,   -1,
   -1,   -1,   -1,  199,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  216,
};
}
final static short YYFINAL=3;
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
"DO","CTE","ID","ASIG","TRUNC","CR","ULONG","COMP","CADENA","FLECHA",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID '{' list_sentencia '}'",
"programa : '{' list_sentencia '}'",
"programa : ID list_sentencia '}'",
"programa : ID list_sentencia",
"list_sentencia :",
"list_sentencia : list_sentencia sentencia",
"declaracion_funcion : header_funcion '(' parametros_formales ')' '{' list_sentencia '}'",
"header_funcion : tipo ID",
"header_funcion : tipo error",
"sentencia : sentencia_declarativa ';'",
"sentencia : sentencia_ejecutable ';'",
"sentencia : declaracion_funcion",
"sentencia : sentencia_declarativa error",
"sentencia : sentencia_ejecutable error",
"sentencia : error ';'",
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
"sentencia_return : RETURN expresion",
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
"sentencia_ejecutable : IF condicion_if '{' bloque_ejecutable '}' else '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : IF condicion_if '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : IF condicion_if sentencia_ejecutable ';' else sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : IF condicion_if sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : IF condicion_if '{' bloque_ejecutable '}' else sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : IF condicion_if sentencia_ejecutable ';' else '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : PRINT '(' CADENA ')'",
"sentencia_ejecutable : PRINT '(' expresion ')'",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_return",
"sentencia_ejecutable : while condicion_while DO '{' bloque_ejecutable '}'",
"sentencia_ejecutable : while condicion_while DO sentencia_ejecutable",
"sentencia_ejecutable : while condicion_while DO",
"sentencia_ejecutable : while condicion_while DO '{' '}'",
"sentencia_ejecutable : PRINT '(' ')'",
"sentencia_ejecutable : while condicion_while '{' bloque_ejecutable '}'",
"sentencia_ejecutable : while condicion_while sentencia_ejecutable",
"sentencia_ejecutable : IF condicion_if '{' '}' else '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : IF condicion_if '{' bloque_ejecutable '}' else '{' '}' end_if",
"sentencia_ejecutable : IF condicion_if '{' '}' else '{' '}' end_if",
"sentencia_ejecutable : IF condicion_if '{' '}' end_if",
"sentencia_ejecutable : IF condicion_if else sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : IF condicion_if sentencia_ejecutable ';' else end_if",
"sentencia_ejecutable : IF condicion_if else end_if",
"sentencia_ejecutable : IF condicion_if end_if",
"sentencia_ejecutable : IF condicion_if '{' '}' else sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : IF condicion_if '{' bloque_ejecutable '}' else end_if",
"sentencia_ejecutable : IF condicion_if '{' '}' else end_if",
"sentencia_ejecutable : IF condicion_if else '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : IF condicion_if sentencia_ejecutable ';' else '{' '}' end_if",
"sentencia_ejecutable : IF condicion_if else '{' '}' end_if",
"else : ELSE",
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
"bloque_ejecutable : sentencia_ejecutable ';'",
"bloque_ejecutable : bloque_ejecutable sentencia_ejecutable ';'",
"bloque_ejecutable : bloque_ejecutable error",
"bloque_ejecutable : sentencia_ejecutable error",
"bloque_ejecutable : bloque_ejecutable sentencia_ejecutable error",
"parametros_reales : parametro_real",
"parametros_reales : parametros_reales ',' parametro_real",
"parametros_reales : error",
"parametro_real : expresion FLECHA ID",
"parametro_real : expresion FLECHA error",
"parametro_real : expresion ID",
"condicion : expresion COMP expresion",
"asignacion_simple : var_asignacion_simple ASIG expresion",
"var_asignacion_simple : ID",
"var_asignacion_simple : ID '.' ID",
"asignacion_multiple : list_vars_mix '=' list_ctes",
"expresion_lambda : '(' tipo ID ')' '{' bloque_ejecutable '}' '(' factor ')'",
"expresion_lambda : '(' tipo ID ')' bloque_ejecutable '}' '(' factor ')'",
"expresion_lambda : '(' tipo ID ')' '{' bloque_ejecutable '(' factor ')'",
"expresion_lambda : '(' tipo ID ')' bloque_ejecutable '(' factor ')'",
"expresion : expresion '+' termino",
"expresion : expresion '-' termino",
"expresion : termino",
"expresion : error '+' termino",
"expresion : expresion '+' error",
"expresion : error '-' termino",
"expresion : expresion '-' error",
"expresion : error '+' error",
"expresion : error '-' error",
"expresion : TRUNC '(' expresion ')'",
"expresion : TRUNC '(' expresion error",
"expresion : TRUNC error expresion ')'",
"expresion : TRUNC error expresion error",
"expresion : expresion_lambda",
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
"factor : inicio_llamado '(' parametros_reales ')'",
"factor : '-' CTE",
"inicio_llamado : ID",
};

//#line 306 "Gramatica.y"

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
    // Chequeo: que 'real' exista en la tabla de símbolos
    ElementoTablaDeSimbolos elem = TablaDeSimbolos.getSimbolo(real);
    if (elem == null) {
        yyerror("Error: la variable '" + real + "' no existe en el ámbito actual.");
        return;
    }

    // Chequeo: que sea una variable, no una constante ni otra cosa
    if (!"Variable".equals(elem.getUso())) {
        yyerror("Error: el parámetro de copia-resultado debe ser una variable" );
        return;
    }

    // Si pasa los chequeos, registramos el vínculo
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
    vinculosCR.clear(); // Limpiamos para la próxima llamada
}

public String obtenerAmbito(String claveTS) {
    ElementoTablaDeSimbolos elemento = TablaDeSimbolos.getSimbolo(claveTS);
    if (elemento != null) {
        int pos = claveTS.indexOf(":");  // busca el primer ':'
        return ambito + ":" + claveTS.substring(0, pos); // devuelve solo la parte antes de ':'

    } else {
        yyerror("Error: El elemento " + claveTS + " no existe");
        return "error";
    }

}
//#line 906 "Parser.java"
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
//#line 25 "Gramatica.y"
{ }
break;
case 2:
//#line 26 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre al programa"); }
break;
case 3:
//#line 27 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '{' al inicio"); }
break;
case 4:
//#line 28 "Gramatica.y"
{ yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
break;
case 7:
//#line 35 "Gramatica.y"
{ reportarEstructura("declaracion de funcion");
                                                                                            /* Etiqueta de fin de función*/
                                                                                            String etiquetaFin = "fin_" + ambito; /* o derivar el nombre de la función de otra forma*/
                                                                                            ArregloTercetos.crearTerceto(etiquetaFin, "_", "_");
                                                                                            salirAmbito();
                                                                                            chequearReturn();

                                                                                          }
break;
case 8:
//#line 45 "Gramatica.y"
{
                                 declaracionDeFuncion(val_peek(0).sval, ambito, "Función");
                                 entrarAmbito(val_peek(0).sval);
                                 pilaReturns.push(val_peek(0).sval + ":" + "false");

                                 /* Crear etiqueta de inicio de función para los tercetos*/
                                 String etiquetaInicio = "ini_" + ambito ;
                                 ArregloTercetos.crearTerceto(etiquetaInicio, "_", "_");
                                }
break;
case 9:
//#line 54 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre a la función"); }
break;
case 13:
//#line 61 "Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 14:
//#line 62 "Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 15:
//#line 63 "Gramatica.y"
{yyerror("Error: Sentencia invalida");}
break;
case 16:
//#line 66 "Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 19:
//#line 73 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cr");}
break;
case 20:
//#line 74 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cv");}
break;
case 21:
//#line 75 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 22:
//#line 76 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 23:
//#line 77 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 24:
//#line 78 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 26:
//#line 84 "Gramatica.y"
{registrarReturn();

                                                          /* Crear nombre para la variable de retorno (puede ser _ret_funcion:ambito)*/
                                                          String varRetorno = "_ret_" + ambito;

                                                          /* Generar terceto para asignar el valor de retorno*/
                                                          ArregloTercetos.crearTerceto(":=", varRetorno, val_peek(0).sval);

                                                          /* Generar terceto de salto al final de la función*/
                                                          ArregloTercetos.crearTerceto("JMP", "fin_" + ambito, null);

                                             }
break;
case 27:
//#line 98 "Gramatica.y"
{tipo = "ulong";}
break;
case 28:
//#line 101 "Gramatica.y"
{yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); ControlAsigMultiple.pushTipoDer(yyval.tipo);}
break;
case 29:
//#line 102 "Gramatica.y"
{ yyval.tipo = val_peek(2).tipo; ControlAsigMultiple.pushTipoDer(obtenerTipoDeSimbolo(val_peek(0).sval)); }
break;
case 30:
//#line 103 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre constantes"); }
break;
case 31:
//#line 106 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); ControlAsigMultiple.pushTipoIzq(yyval.tipo); }
break;
case 32:
//#line 107 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); ControlAsigMultiple.pushTipoIzq(yyval.tipo);}
break;
case 33:
//#line 108 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval);  yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); ControlAsigMultiple.pushTipoIzq(yyval.tipo);}
break;
case 34:
//#line 109 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); ControlAsigMultiple.pushTipoIzq(yyval.tipo);}
break;
case 35:
//#line 110 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 36:
//#line 111 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 37:
//#line 114 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 38:
//#line 115 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 39:
//#line 116 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 40:
//#line 121 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 41:
//#line 122 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 42:
//#line 123 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 43:
//#line 124 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 44:
//#line 125 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 45:
//#line 126 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 46:
//#line 127 "Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 47:
//#line 128 "Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 51:
//#line 132 "Gramatica.y"
{ reportarEstructura("WHILE"); yyval = ArregloTercetos.completarBackPatchingWHILE(); }
break;
case 52:
//#line 133 "Gramatica.y"
{ reportarEstructura("WHILE"); yyval = ArregloTercetos.completarBackPatchingWHILE(); }
break;
case 53:
//#line 134 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");   }
break;
case 54:
//#line 135 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 55:
//#line 136 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 56:
//#line 138 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 57:
//#line 139 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 58:
//#line 141 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 59:
//#line 142 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 60:
//#line 143 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 61:
//#line 144 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 62:
//#line 145 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 63:
//#line 146 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 64:
//#line 147 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 65:
//#line 148 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 66:
//#line 149 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 67:
//#line 150 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 68:
//#line 151 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 69:
//#line 152 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 70:
//#line 153 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 72:
//#line 157 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIFDesapilaryCompletar("bl", null,null);}
break;
case 73:
//#line 160 "Gramatica.y"
{ ArregloTercetos.apilarTercetoInicialWHILE(); }
break;
case 74:
//#line 166 "Gramatica.y"
{ArregloTercetos.completarTercetoBackPatchingIF();}
break;
case 75:
//#line 167 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 76:
//#line 170 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 77:
//#line 171 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 78:
//#line 172 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 79:
//#line 173 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 80:
//#line 176 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 81:
//#line 177 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 82:
//#line 178 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 83:
//#line 179 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 86:
//#line 187 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 87:
//#line 188 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 88:
//#line 189 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 91:
//#line 195 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 92:
//#line 198 "Gramatica.y"
{
                                                    String funcionActual = PilaDeFuncionesLlamadas.desapilarFuncion();
                                                    String paramFormal = val_peek(0).sval + ":" + funcionActual;
                                                    String tipoFormal = obtenerTipoDeSimbolo(paramFormal);
                                                    yyval.tipo = chequearTipos(val_peek(2).tipo, tipoFormal);
                                                    realizarPasajeCopiaValor(paramFormal, val_peek(2).sval);

                                                    /* ⚡ Nuevo: registrar vínculo si el parámetro es cr*/
                                                    if (esParametroCR(paramFormal)) {
                                                        registrarVinculoCR(paramFormal, val_peek(2).sval);
                                                    }
                                            }
break;
case 93:
//#line 210 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 94:
//#line 211 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 95:
//#line 214 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("COMP", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo); }
break;
case 96:
//#line 217 "Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                               yyval = ArregloTercetos.crearTerceto(":=", val_peek(2).sval, val_peek(0).sval);
                                                               yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo);
                                                               }
break;
case 97:
//#line 223 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 98:
//#line 224 "Gramatica.y"
{yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(2).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 99:
//#line 227 "Gramatica.y"
{ reportarEstructura("asignacion multiple"); ControlAsigMultiple.compararTipos();}
break;
case 100:
//#line 231 "Gramatica.y"
{ reportarEstructura("expresion lambda"); }
break;
case 101:
//#line 232 "Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 102:
//#line 233 "Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 103:
//#line 234 "Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 104:
//#line 237 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("+", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo); }
break;
case 105:
//#line 238 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("-", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo);}
break;
case 106:
//#line 239 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 107:
//#line 240 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 108:
//#line 241 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 109:
//#line 242 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 110:
//#line 243 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 111:
//#line 244 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 112:
//#line 245 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 113:
//#line 246 "Gramatica.y"
{yyval.tipo = "ulong";}
break;
case 114:
//#line 247 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 115:
//#line 248 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 116:
//#line 249 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 118:
//#line 256 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("*", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo); }
break;
case 119:
//#line 257 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("/", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo);}
break;
case 120:
//#line 258 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 121:
//#line 259 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 122:
//#line 260 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 123:
//#line 261 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 124:
//#line 262 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 125:
//#line 263 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 126:
//#line 264 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 127:
//#line 267 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 128:
//#line 268 "Gramatica.y"
{yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); yyval = val_peek(0); }
break;
case 129:
//#line 269 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 130:
//#line 270 "Gramatica.y"
{

                        /* ⚡ Generar terceto CALL usando el ID que guardó inicio_llamado*/
                        yyval = ArregloTercetos.crearTerceto("CALL", val_peek(3).sval, null);

                        /* ⚡ Guardar el tipo de retorno de la función*/
                        yyval.tipo = obtenerTipoDeSimbolo(val_peek(3).sval);

                        /* ⚡ Crear una temporal para almacenar el valor de retorno*/
                        String temp = "_t" + ArregloTercetos.declararTemporal(yyval.tipo,ambito); /* nombre temporal único*/
                        declaracionDeVariable(temp, yyval.tipo, ambito, "temporal");

                        /* ⚡ Asignar el valor retornado (_ret_<funcion>) a la temporal*/
                        String retName = "_ret_" + obtenerAmbito(val_peek(3).sval);
                        ArregloTercetos.crearTerceto(":=", temp, retName);

                        /* ⚡ Guardar temporal como valor semántico del factor*/
                        yyval.sval = temp;

                        /* ⚡ Finalizar la gestión de pila de funciones*/
                        PilaDeFuncionesLlamadas.finalizarLlamada();

                        /* ⚡ Realizar pasajes de copia-resultado si hay CR*/
                         realizarPasajesCopiaResultado();
                      }
break;
case 131:
//#line 295 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval);}
break;
case 132:
//#line 300 "Gramatica.y"
{ yyval = chequearAmbito("", ambito, val_peek(0).sval);
      PilaDeFuncionesLlamadas.iniciarLlamada(ambito+":"+val_peek(0).sval); }
break;
//#line 1580 "Parser.java"
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
