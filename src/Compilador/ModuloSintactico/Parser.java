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
public final static short LOWER_THAN_CALL=257;
public final static short WHILE=258;
public final static short IF=259;
public final static short ELSE=260;
public final static short ENDIF=261;
public final static short PRINT=262;
public final static short RETURN=263;
public final static short DO=264;
public final static short CTE=265;
public final static short ID=266;
public final static short ASIG=267;
public final static short TRUNC=268;
public final static short CR=269;
public final static short ULONG=270;
public final static short CADENA=271;
public final static short FLECHA=272;
public final static short EQ=273;
public final static short GEQ=274;
public final static short LEQ=275;
public final static short NEQ=276;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    0,    0,    1,    3,    4,    4,
    2,    2,    7,    7,    7,    7,    7,    7,    7,    7,
    8,    5,    5,   11,   11,   11,   11,   11,   11,   12,
   13,    6,   15,   15,   15,   16,   16,   16,   16,   16,
   16,   10,   10,   10,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,   20,   17,   25,
   21,   21,   18,   18,   18,   18,   26,   26,   26,   26,
   19,   19,   19,   19,   19,   28,   28,   28,   29,   29,
   29,   27,   30,   30,   30,   30,   30,   30,   30,   30,
   30,   30,   22,   31,   31,   23,   32,   33,   24,   24,
   24,   24,   34,   34,   34,   14,   14,   14,   14,   14,
   14,   14,   14,   14,   35,   35,   35,   35,   35,   35,
   35,   35,   35,   36,   36,   36,   36,   36,   36,   36,
   36,   36,   37,
};
final static short yylen[] = {                            2,
    4,    5,    3,    3,    2,    3,    1,    6,    3,    2,
    0,    2,    2,    2,    1,    2,    2,    3,    3,    1,
    2,    1,    3,    3,    2,    3,    2,    3,    2,    1,
    4,    1,    1,    3,    2,    1,    3,    3,    5,    2,
    4,    1,    3,    2,   10,    6,    8,    5,    9,    9,
    4,    4,    1,    1,    1,    1,    6,    4,    3,    5,
    3,    5,    3,    9,    9,    8,    5,    6,    6,    4,
    3,    8,    7,    6,    7,    8,    6,    1,    1,    1,
    1,    1,    3,    2,    2,    1,    3,    2,    2,    1,
    2,    3,    2,    2,    3,    1,    3,    1,    3,    3,
    2,    3,    1,    1,    1,    1,    1,    1,    1,    2,
    2,    2,    3,    1,    3,    3,    4,    0,    8,    6,
    6,    5,    1,    1,    2,    3,    3,    1,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    1,    1,    1,    3,    3,    2,    4,    4,
    4,    4,    2,
};
final static short yydefred[] = {                         0,
    7,   11,    0,    0,    0,   11,    0,    0,    0,   80,
   79,    0,    0,    0,   32,    6,    0,    0,    0,   12,
    0,    0,   55,    0,    0,   53,   54,   56,    0,    0,
    0,    0,    3,   17,    0,    0,    0,    0,   16,    0,
   30,    0,    0,   22,    0,   10,    0,    0,    0,   13,
    0,   14,    0,    0,    0,    0,    0,    0,  145,    0,
    0,    0,    0,    0,    0,  143,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   61,    0,    0,
    0,   29,    0,    0,   27,   25,    0,    0,    9,   44,
    0,   18,   19,    0,    0,   33,    0,    0,    0,    0,
    0,  148,    0,  153,    0,    0,    0,    0,    0,  103,
  104,  105,  106,  107,  108,    0,    0,    0,   82,   78,
   81,    0,    0,    0,   71,   84,    0,    0,    0,    0,
    0,   96,    0,    0,    0,   63,   88,    0,    0,   94,
   91,   93,    0,    0,    0,    2,  117,   51,   52,   31,
   11,   23,   28,   26,   24,   43,   41,    0,   35,    0,
    0,    0,    0,    0,  141,  137,  142,  139,   83,  146,
    0,    0,    0,    0,    0,    0,  111,  110,  112,    0,
    0,    0,    0,    0,    0,   70,  138,  135,  140,  136,
  101,    0,  147,    0,   87,    0,   58,    0,    0,  118,
    0,    0,   95,   92,    0,   39,   34,  152,  151,  150,
  149,    0,   67,    0,    0,   48,    0,    0,    0,  100,
   99,   97,   60,    0,   62,    0,    0,  122,    0,    8,
    0,    0,   74,    0,   46,    0,    0,   69,   77,    0,
   68,   57,  120,    0,  121,    0,    0,    0,    0,    0,
   73,    0,    0,    0,   75,    0,  124,  123,    0,   66,
    0,   72,    0,    0,    0,   76,    0,   47,  125,  119,
   64,   65,    0,   49,   50,   45,
};
final static short yydgoto[] = {                          3,
    4,    5,   17,   18,   42,   19,   20,   21,   73,   48,
   44,   45,   23,   62,   97,   24,   25,   63,   74,  124,
  125,   26,   27,   28,   29,   69,   64,  131,  132,  118,
   30,   31,  227,  259,   65,   66,   67,
};
final static short yysindex[] = {                      -105,
    0,    0,    0, -118,  -23,    0,  263,  -25, -228,    0,
    0,   15,   27,   40,    0,    0,   49, -221, -210,    0,
  -45,  -43,    0,  -36,   44,    0,    0,    0,  100, -130,
  698,  280,    0,    0, -124,  187,  -39, -115,    0, -114,
    0,  109, -203,    0, -243,    0,  119,  -32,  122,    0,
  128,    0,  111,  -90,  -77,   62,  -75,  -39,    0,   57,
  -40,  -22,  317,  150,   82,    0,  712,  -39,  531,  151,
  -39,  216,  -37,  544,   68,  153,  156,    0,   42,  117,
    0,    0,   75, -221,    0,    0,  -67, -202,    0,    0,
  -66,    0,    0,  -61,  161,    0,  -20,  751,  764,  768,
  772,    0,  173,    0,  -46,    5,   34,  783,  787,    0,
    0,    0,    0,    0,    0,   39,  236,  -39,    0,    0,
    0,  601,  264,  488,    0,    0,  791,  798,   62,  -17,
  136,    0,  283,  700,  216,    0,    0,   98,  568,    0,
    0,    0,  583,  285,  -15,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   60,    0,   67,
   91,   82,   91,   82,    0,    0,    0,    0,    0,    0,
  -38,    6,   91,   82,   91,   82,    0,    0,    0,   98,
 -150,  579, -150,  630,  276,    0,    0,    0,    0,    0,
    0, -186,    0,  -39,    0,  676,    0,  599,  583,    0,
  124,  -39,    0,    0,  293,    0,    0,    0,    0,    0,
    0,  500,    0, -150,  513,    0, -122,  612, -122,    0,
    0,    0,    0,  628,    0,  129,  312,    0,  130,    0,
  725,  295,    0,  525,    0,  727,  296,    0,    0, -122,
    0,    0,    0,   36,    0, -122,  639, -122,  752,  299,
    0, -122,  652, -122,    0,  102,    0,    0,  328,    0,
 -122,    0, -122,  674, -122,    0, -122,    0,    0,    0,
    0,    0, -122,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    1,    0,    0,  370,   10,    0,    0,
    0,    0,    0,  -29,    0,    0,   19,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  373,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   -8,    9,    0,    0,
    0,    0,  -24,    0,    0,    0,    0,    0,    0,   32,
    0,    0,    0,  354,   55,    0,    0,    0,    0,  662,
    0,    0,    0,    0,  375,    0,    0,    0,    0,    0,
   17,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    8,    0,   23,    0,    0,    0,
    0,    0,  473,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  802,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  145,    0,
    0,    0,  685,   31,    0,    0,    0,   66,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   87,  123,  163,  331,    0,    0,    0,    0,    0,    0,
    0,    0,  371,  401,  424,  447,    0,    0,    0,   78,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    3,    0,    0,    0,  482,    0,    0,   -3,    0,
  294,    0,    0,  960,    0,    0,    0,    0,  878, -138,
  917,    0,    0,    0,    0,    0,    4,    0,  183,    0,
    0,    0,    0,    0,   14,  -70,    0,
};
final static int YYTABLESIZE=1190;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        107,
   11,   22,  209,   22,    6,   57,    7,   54,   32,   20,
  117,   91,   87,   50,   36,   52,    9,    2,   15,   40,
  108,  141,  109,  160,   55,  108,   15,  109,   22,  166,
  168,   36,   70,   34,   40,   42,   40,  115,  116,  114,
   11,   15,  212,  204,  215,   46,  211,   41,   15,   20,
   42,   37,   85,  154,   36,   47,  188,  190,   15,  123,
   38,  103,   86,  155,  144,  136,   37,   21,   37,  220,
  145,  133,  144,  144,  144,  234,  144,   38,  144,  221,
  256,  116,  149,   58,  108,   38,  109,  128,   57,   59,
  144,  144,  144,  144,  128,  128,  104,  128,  178,  128,
  177,   16,  105,  100,   98,  119,   99,   39,  101,  120,
  121,  162,  164,  128,  128,  128,  128,  102,  102,  133,
  185,  174,  176,  127,  113,   11,  133,  133,  128,  133,
  197,  133,  100,  119,   20,  145,   71,  101,  121,   68,
  108,   76,  109,   15,   57,  133,  133,  133,  133,   83,
   81,   82,   84,  205,  144,  129,   94,  150,   89,  108,
    1,  109,  129,  129,  228,  129,  108,  129,  109,  243,
  245,  108,  108,  109,  109,   95,  193,  128,  145,  194,
   92,  129,  129,  129,  129,   98,   93,   96,   98,  102,
  126,  137,  146,  147,  145,  134,  148,  151,  153,  156,
  102,   22,  134,  134,  157,  134,  158,  134,  232,  133,
   49,  237,   51,  169,  145,  106,   56,  208,  140,  170,
  145,  134,  134,  134,  134,   59,   60,   78,   61,   53,
  250,   57,    8,   90,   10,   11,   36,  114,   12,   13,
  203,   40,   14,  145,  159,  129,   15,   42,  191,  145,
  110,  111,  112,  113,  192,    9,   11,   42,   11,   11,
  145,  210,   11,   11,   21,   20,   11,   20,   20,  171,
   11,   20,   20,   37,   15,   20,   15,   15,  116,   20,
   15,   15,   38,  115,   15,  134,   59,  144,   15,  144,
  144,  144,  144,  144,  144,  144,  179,  144,  172,   56,
  257,  258,    9,  144,  144,  144,  144,  144,   59,   60,
  128,   61,  128,  128,  128,  128,  128,  128,  128,    9,
  128,  113,  183,  195,  202,  206,  128,  128,  128,  128,
  128,  207,    9,  102,  219,  102,  102,  102,  102,  102,
  102,  102,  133,  102,  133,  133,  133,  133,  133,  133,
  133,  244,  133,  248,  254,   56,    9,  265,  133,  133,
  133,  133,  133,  131,   59,   60,  269,   61,  270,    5,
  131,  131,    4,  131,    1,  131,  222,  152,  129,    0,
  129,  129,  129,  129,  129,  129,  129,   33,  129,  131,
  131,  131,  131,   86,  129,  129,  129,  129,  129,    0,
    0,    0,    0,  130,   75,    0,    0,    0,    0,    0,
  130,  130,    0,  130,    0,  130,    0,  230,  134,    0,
  134,  134,  134,  134,  134,  134,  134,    0,  134,  130,
  130,  130,  130,  126,  134,  134,  134,  134,  134,  122,
  126,  126,   56,  126,    0,  126,    0,    0,    0,    0,
    0,   59,   60,  131,   61,    0,  132,   77,    0,  126,
  126,  126,  126,  132,  132,    0,  132,    0,  132,    0,
    0,    0,    0,   10,   11,    0,   86,   12,   13,  127,
    0,   14,  132,  132,  132,  132,  127,  127,    0,  127,
   35,  127,    0,  130,    0,    0,    0,    0,    0,   43,
    0,    0,    0,    0,    0,  127,  127,  127,  127,    0,
    0,    0,   85,    0,    0,    0,    0,    0,    8,    0,
   10,   11,    0,  126,   12,   13,   88,    9,   14,    0,
    0,    0,   15,    0,    0,    8,    0,   10,   11,    9,
    0,   12,   13,    0,    0,   14,  132,    0,    8,   15,
   10,   11,    9,    0,   12,   13,    0,    0,   14,    0,
    0,    0,   15,    0,    9,   43,    0,    0,    0,  127,
    9,    0,  119,    0,   10,   11,  120,  121,   12,   13,
    0,    0,   14,  143,    0,    0,  131,    0,  131,  131,
  131,  131,  131,  131,  131,   85,  131,    0,    0,    0,
    0,    0,  131,  131,  131,  131,  131,  199,    0,   86,
  184,   86,   86,   86,   86,   86,   86,    0,    9,   86,
    0,    0,  231,    0,   35,    0,  130,   57,  130,  130,
  130,  130,  130,  130,  130,  236,  130,    0,    9,    0,
    9,    0,  130,  130,  130,  130,  130,  249,    0,    0,
    0,    9,    0,  135,    0,    0,  126,    0,  126,  126,
  126,  126,  126,  126,  126,    0,  126,    9,  144,    9,
    0,    0,  126,  126,  126,  126,  126,    0,    9,  132,
   35,  132,  132,  132,  132,  132,  132,  132,    0,  132,
    0,    9,  200,    0,    0,  132,  132,  132,  132,  132,
    0,   90,  127,  214,  127,  127,  127,  127,  127,  127,
  127,    0,  127,    9,    0,    9,    0,    0,  127,  127,
  127,  127,  127,  225,   89,  181,    0,    0,   85,    0,
   85,   85,   85,   85,   85,   85,  240,    9,   85,    9,
    0,    0,    0,  119,    0,   10,   11,    0,  121,   12,
   13,    0,  242,   14,  217,  119,   57,   10,   11,    0,
  121,   12,   13,  261,    9,   14,    9,    0,  119,    0,
   10,   11,    0,  121,   12,   13,  267,    0,   14,    0,
  119,    0,   10,   11,   90,  121,   12,   13,   10,   11,
   14,    9,   12,   13,  134,   57,   14,    0,  273,  142,
  223,   10,   11,    0,    0,   12,   13,   89,   57,   14,
    0,    0,   57,    0,    0,    0,   57,    0,    0,    0,
   72,    0,  196,  142,    0,   10,   11,   57,    0,   12,
   13,   57,    0,   14,  142,   57,   10,   11,   56,    0,
   12,   13,   57,    0,   14,    0,  109,   59,   60,  246,
   61,  252,   15,    0,  142,    0,   10,   11,   10,   11,
   12,   13,   12,   13,   14,    0,   14,  142,    0,   10,
   11,    0,    0,   12,   13,    0,  263,   14,    0,    0,
    0,    0,    0,  142,    0,   10,   11,   10,   11,   12,
   13,   12,   13,   14,  142,   14,   10,   11,    0,    0,
   12,   13,    0,    0,   14,    0,    0,  142,    0,   10,
   11,    0,    0,   12,   13,    0,    0,   14,    0,   90,
   90,    0,    0,   90,   90,   90,    0,   90,    0,  142,
    0,   10,   11,   10,   11,   12,   13,   12,   13,   14,
    0,   14,   89,   89,    0,    0,   89,   89,   89,  139,
   89,    0,    0,    0,    0,   10,   11,   10,   11,   12,
   13,   12,   13,   14,    0,   14,    0,  129,    0,    0,
    0,    0,    0,    0,    0,    0,   59,   60,    0,   61,
    0,    0,   10,   11,   10,   11,   12,   13,   12,   13,
   14,    0,   14,    0,    0,   79,   80,    0,    0,  182,
    0,    0,    0,    0,    0,    0,  161,    0,    0,   10,
   11,    0,  198,   12,   13,   59,   60,   14,   61,  163,
    0,    0,    0,  165,    0,    0,  130,  167,   59,   60,
  138,   61,   59,   60,    0,   61,   59,   60,  173,   61,
  186,    0,  175,    0,    0,    0,  187,   59,   60,    0,
   61,   59,   60,  189,   61,   59,   60,  109,   61,    0,
    0,  218,   59,   60,    0,   61,  109,  109,    0,  109,
    0,    0,    0,  224,    0,    0,    0,  180,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  213,    0,  216,
    0,    0,  201,    0,    0,    0,    0,    0,  247,    0,
    0,    0,    0,  253,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  264,    0,  233,    0,
  235,  238,    0,  239,    0,  241,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  251,    0,    0,  130,    0,    0,  255,    0,  226,    0,
    0,  229,  260,    0,  262,    0,    0,    0,  266,    0,
  268,    0,    0,    0,    0,    0,    0,  271,    0,  272,
    0,  274,    0,  275,    0,    0,    0,    0,    0,  276,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,    5,   41,    7,  123,   45,    4,   44,    6,    0,
   33,   44,  256,   59,   44,   59,   40,  123,    0,   44,
   43,   59,   45,   44,   61,   43,  270,   45,   32,  100,
  101,   61,   29,   59,  256,   44,   61,   60,   61,   62,
   40,  270,  181,   59,  183,  256,   41,  269,  270,   40,
   59,   44,  256,  256,   40,  266,  127,  128,   40,   63,
   44,   58,  266,  266,   33,   69,   40,   59,   61,  256,
   74,   68,   41,   42,   43,  214,   45,   61,   47,  266,
   45,   59,   41,   40,   43,   46,   45,   33,   45,   59,
   59,   60,   61,   62,   40,   41,   40,   43,   60,   45,
   62,  125,   46,   42,   43,  256,   45,   59,   47,  260,
  261,   98,   99,   59,   60,   61,   62,   40,   41,   33,
  124,  108,  109,   42,   59,  125,   40,   41,   47,   43,
  134,   45,   42,  256,  125,  139,  267,   47,  261,   40,
   43,  266,   45,  125,   45,   59,   60,   61,   62,   41,
  266,  266,   44,  151,  123,   33,   46,   41,   40,   43,
  266,   45,   40,   41,   41,   43,   43,   45,   45,   41,
   41,   43,   43,   45,   45,  266,   41,  123,  182,   44,
   59,   59,   60,   61,   62,   41,   59,  265,   44,  265,
   41,   41,  125,   41,  198,   33,   41,  123,  266,  266,
  123,  205,   40,   41,  266,   43,   46,   45,  212,  123,
  256,  215,  256,   41,  218,  256,  256,  256,  256,  266,
  224,   59,   60,   61,   62,  265,  266,   41,  268,  266,
  234,   45,  256,  266,  258,  259,  266,  267,  262,  263,
  256,  266,  266,  247,  265,  123,  270,  256,  266,  253,
  273,  274,  275,  276,  272,   40,  256,  266,  258,  259,
  264,  256,  262,  263,  256,  256,  266,  258,  259,  265,
  270,  262,  263,  266,  256,  266,  258,  259,  256,  270,
  262,  263,  266,  267,  266,  123,  256,  256,  270,  258,
  259,  260,  261,  262,  263,  264,   61,  266,  265,  256,
  265,  266,   40,  272,  273,  274,  275,  276,  265,  266,
  256,  268,  258,  259,  260,  261,  262,  263,  264,   40,
  266,  256,   59,   41,   40,  266,  272,  273,  274,  275,
  276,  265,   40,  256,   59,  258,  259,  260,  261,  262,
  263,  264,  256,  266,  258,  259,  260,  261,  262,  263,
  264,   40,  266,   59,   59,  256,   40,   59,  272,  273,
  274,  275,  276,   33,  265,  266,  265,  268,   41,    0,
   40,   41,    0,   43,    0,   45,  194,   84,  256,   -1,
  258,  259,  260,  261,  262,  263,  264,  125,  266,   59,
   60,   61,   62,   40,  272,  273,  274,  275,  276,   -1,
   -1,   -1,   -1,   33,  125,   -1,   -1,   -1,   -1,   -1,
   40,   41,   -1,   43,   -1,   45,   -1,  125,  256,   -1,
  258,  259,  260,  261,  262,  263,  264,   -1,  266,   59,
   60,   61,   62,   33,  272,  273,  274,  275,  276,  123,
   40,   41,  256,   43,   -1,   45,   -1,   -1,   -1,   -1,
   -1,  265,  266,  123,  268,   -1,   33,  271,   -1,   59,
   60,   61,   62,   40,   41,   -1,   43,   -1,   45,   -1,
   -1,   -1,   -1,  258,  259,   -1,  123,  262,  263,   33,
   -1,  266,   59,   60,   61,   62,   40,   41,   -1,   43,
    9,   45,   -1,  123,   -1,   -1,   -1,   -1,   -1,   18,
   -1,   -1,   -1,   -1,   -1,   59,   60,   61,   62,   -1,
   -1,   -1,   40,   -1,   -1,   -1,   -1,   -1,  256,   -1,
  258,  259,   -1,  123,  262,  263,   45,   40,  266,   -1,
   -1,   -1,  270,   -1,   -1,  256,   -1,  258,  259,   40,
   -1,  262,  263,   -1,   -1,  266,  123,   -1,  256,  270,
  258,  259,   40,   -1,  262,  263,   -1,   -1,  266,   -1,
   -1,   -1,  270,   -1,   40,   84,   -1,   -1,   -1,  123,
   40,   -1,  256,   -1,  258,  259,  260,  261,  262,  263,
   -1,   -1,  266,   40,   -1,   -1,  256,   -1,  258,  259,
  260,  261,  262,  263,  264,  123,  266,   -1,   -1,   -1,
   -1,   -1,  272,  273,  274,  275,  276,   40,   -1,  256,
  123,  258,  259,  260,  261,  262,  263,   -1,   40,  266,
   -1,   -1,  123,   -1,  143,   -1,  256,   45,  258,  259,
  260,  261,  262,  263,  264,  123,  266,   -1,   40,   -1,
   40,   -1,  272,  273,  274,  275,  276,  123,   -1,   -1,
   -1,   40,   -1,  123,   -1,   -1,  256,   -1,  258,  259,
  260,  261,  262,  263,  264,   -1,  266,   40,  125,   40,
   -1,   -1,  272,  273,  274,  275,  276,   -1,   40,  256,
  199,  258,  259,  260,  261,  262,  263,  264,   -1,  266,
   -1,   40,  125,   -1,   -1,  272,  273,  274,  275,  276,
   -1,   40,  256,  125,  258,  259,  260,  261,  262,  263,
  264,   -1,  266,   40,   -1,   40,   -1,   -1,  272,  273,
  274,  275,  276,  125,   40,  125,   -1,   -1,  256,   -1,
  258,  259,  260,  261,  262,  263,  125,   40,  266,   40,
   -1,   -1,   -1,  256,   -1,  258,  259,   -1,  261,  262,
  263,   -1,  125,  266,  125,  256,   45,  258,  259,   -1,
  261,  262,  263,  125,   40,  266,   40,   -1,  256,   -1,
  258,  259,   -1,  261,  262,  263,  125,   -1,  266,   -1,
  256,   -1,  258,  259,  123,  261,  262,  263,  258,  259,
  266,   40,  262,  263,  264,   45,  266,   -1,  125,  256,
  125,  258,  259,   -1,   -1,  262,  263,  123,   45,  266,
   -1,   -1,   45,   -1,   -1,   -1,   45,   -1,   -1,   -1,
  123,   -1,  123,  256,   -1,  258,  259,   45,   -1,  262,
  263,   45,   -1,  266,  256,   45,  258,  259,  256,   -1,
  262,  263,   45,   -1,  266,   -1,   45,  265,  266,  125,
  268,  125,  270,   -1,  256,   -1,  258,  259,  258,  259,
  262,  263,  262,  263,  266,   -1,  266,  256,   -1,  258,
  259,   -1,   -1,  262,  263,   -1,  125,  266,   -1,   -1,
   -1,   -1,   -1,  256,   -1,  258,  259,  258,  259,  262,
  263,  262,  263,  266,  256,  266,  258,  259,   -1,   -1,
  262,  263,   -1,   -1,  266,   -1,   -1,  256,   -1,  258,
  259,   -1,   -1,  262,  263,   -1,   -1,  266,   -1,  258,
  259,   -1,   -1,  262,  263,  264,   -1,  266,   -1,  256,
   -1,  258,  259,  258,  259,  262,  263,  262,  263,  266,
   -1,  266,  258,  259,   -1,   -1,  262,  263,  264,   72,
  266,   -1,   -1,   -1,   -1,  258,  259,  258,  259,  262,
  263,  262,  263,  266,   -1,  266,   -1,  256,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  265,  266,   -1,  268,
   -1,   -1,  258,  259,  258,  259,  262,  263,  262,  263,
  266,   -1,  266,   -1,   -1,   36,   37,   -1,   -1,  122,
   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,   -1,  258,
  259,   -1,  135,  262,  263,  265,  266,  266,  268,  256,
   -1,   -1,   -1,  256,   -1,   -1,   67,  256,  265,  266,
   71,  268,  265,  266,   -1,  268,  265,  266,  256,  268,
  124,   -1,  256,   -1,   -1,   -1,  256,  265,  266,   -1,
  268,  265,  266,  256,  268,  265,  266,  256,  268,   -1,
   -1,  184,  265,  266,   -1,  268,  265,  266,   -1,  268,
   -1,   -1,   -1,  196,   -1,   -1,   -1,  118,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  181,   -1,  183,
   -1,   -1,  143,   -1,   -1,   -1,   -1,   -1,  231,   -1,
   -1,   -1,   -1,  236,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  249,   -1,  212,   -1,
  214,  215,   -1,  217,   -1,  219,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  234,   -1,   -1,  194,   -1,   -1,  240,   -1,  199,   -1,
   -1,  202,  246,   -1,  248,   -1,   -1,   -1,  252,   -1,
  254,   -1,   -1,   -1,   -1,   -1,   -1,  261,   -1,  263,
   -1,  265,   -1,  267,   -1,   -1,   -1,   -1,   -1,  273,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=276;
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
null,null,null,null,null,null,null,null,"LOWER_THAN_CALL","WHILE","IF","ELSE",
"ENDIF","PRINT","RETURN","DO","CTE","ID","ASIG","TRUNC","CR","ULONG","CADENA",
"FLECHA","EQ","GEQ","LEQ","NEQ",
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
"sentencia : sentencia_declarativa ';'",
"sentencia : sentencia_ejecutable ';'",
"sentencia : declaracion_funcion",
"sentencia : declaracion_funcion ';'",
"sentencia : error ';'",
"sentencia : sentencia_declarativa error ';'",
"sentencia : sentencia_ejecutable error ';'",
"sentencia : error",
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
"sentencia_ejecutable : if condicion_if '{' bloque_ejecutable '}' else '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : if condicion_if '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : if condicion_if sentencia_ejecutable ';' else sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : if condicion_if sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : if condicion_if '{' bloque_ejecutable '}' else sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : if condicion_if sentencia_ejecutable ';' else '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : PRINT '(' CADENA ')'",
"sentencia_ejecutable : PRINT '(' expresion ')'",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_return",
"sentencia_ejecutable : expresion_lambda",
"sentencia_ejecutable : while condicion_while DO '{' bloque_ejecutable '}'",
"sentencia_ejecutable : while condicion_while DO sentencia_ejecutable",
"sentencia_ejecutable : while condicion_while DO",
"sentencia_ejecutable : while condicion_while DO '{' '}'",
"sentencia_ejecutable : PRINT '(' ')'",
"sentencia_ejecutable : while condicion_while '{' bloque_ejecutable '}'",
"sentencia_ejecutable : while condicion_while sentencia_ejecutable",
"sentencia_ejecutable : if condicion_if '{' '}' else '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : if condicion_if '{' bloque_ejecutable '}' else '{' '}' end_if",
"sentencia_ejecutable : if condicion_if '{' '}' else '{' '}' end_if",
"sentencia_ejecutable : if condicion_if '{' '}' end_if",
"sentencia_ejecutable : if condicion_if else sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : if condicion_if sentencia_ejecutable ';' else end_if",
"sentencia_ejecutable : if condicion_if else end_if",
"sentencia_ejecutable : if condicion_if end_if",
"sentencia_ejecutable : if condicion_if '{' '}' else sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : if condicion_if '{' bloque_ejecutable '}' else end_if",
"sentencia_ejecutable : if condicion_if '{' '}' else end_if",
"sentencia_ejecutable : if condicion_if else '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : if condicion_if sentencia_ejecutable ';' else '{' '}' end_if",
"sentencia_ejecutable : if condicion_if else '{' '}' end_if",
"else : ELSE",
"if : IF",
"while : WHILE",
"end_if : ENDIF",
"end_if : error",
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
"condicion : expresion comp expresion",
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
"expresion_lambda : header_lambda '{' bloque_ejecutable '}' $$1 '(' factor_lambda ')'",
"expresion_lambda : header_lambda '{' bloque_ejecutable '(' expresion ')'",
"expresion_lambda : header_lambda bloque_ejecutable '}' '(' expresion ')'",
"expresion_lambda : header_lambda bloque_ejecutable '(' expresion ')'",
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
};

//#line 346 "Gramatica.y"
// ... resto del código Java (sin cambios) ...
public static final Set<String> erroresEmitidos = new HashSet<>();
public static int ultimaLineaError;

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
//#line 1033 "Parser.java"
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
//#line 29 "Gramatica.y"
{ reportarEstructura("Fin del programa");}
break;
case 2:
//#line 30 "Gramatica.y"
{ yyerror("Error: '}' de mas al final de programa");}
break;
case 3:
//#line 31 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '{' al inicio"); }
break;
case 4:
//#line 32 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '}' al final"); }
break;
case 5:
//#line 33 "Gramatica.y"
{ yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
break;
case 6:
//#line 34 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del programa"); }
break;
case 7:
//#line 37 "Gramatica.y"
{ambito = val_peek(0).sval;}
break;
case 8:
//#line 40 "Gramatica.y"
{ reportarEstructura("declaracion de funcion");
                                                                                            String etiquetaFin = "fin_" + ambito;
                                                                                            ArregloTercetos.crearTerceto(etiquetaFin, "_", "_");
                                                                                            salirAmbito();
                                                                                            chequearReturn();
                                                                                          }
break;
case 9:
//#line 48 "Gramatica.y"
{
                                 declaracionDeFuncion(val_peek(1).sval, ambito, "Función");
                                 entrarAmbito(val_peek(1).sval);
                                 pilaReturns.push(val_peek(1).sval + ":" + "false");
                                 String etiquetaInicio = "ini_" + ambito ;
                                 ArregloTercetos.crearTerceto(etiquetaInicio, "_", "_");
                                }
break;
case 10:
//#line 55 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre a la función"); }
break;
case 16:
//#line 65 "Gramatica.y"
{ yyerror("Error: No debe haber ';' después de la declaración de función"); }
break;
case 17:
//#line 66 "Gramatica.y"
{ yyerror("Error: Sentencia inválida detectada — se descartó hasta ';'"); }
break;
case 18:
//#line 67 "Gramatica.y"
{ yyerror("Error de sintaxis: declaración mal formada o faltante del ';'"); }
break;
case 19:
//#line 68 "Gramatica.y"
{ yyerror("Error de sintaxis: sentencia ejecutable mal formada o faltante del ';'"); }
break;
case 20:
//#line 69 "Gramatica.y"
{ yyerror("Error: Sentencia mal formada o falta ';' antes del fin del bloque"); }
break;
case 21:
//#line 72 "Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 24:
//#line 79 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cr");}
break;
case 25:
//#line 80 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cv");}
break;
case 26:
//#line 81 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 27:
//#line 82 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 28:
//#line 83 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 29:
//#line 84 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 31:
//#line 90 "Gramatica.y"
{registrarReturn();
                                                          String varRetorno = "_ret_" + ambito;
                                                          ArregloTercetos.crearTerceto(":=", varRetorno, val_peek(1).sval);
                                                          ArregloTercetos.crearTerceto("RETURN", varRetorno, val_peek(1).sval);
                                                          ArregloTercetos.crearTerceto("JMP", "fin_" + ambito, null);
                                             }
break;
case 32:
//#line 98 "Gramatica.y"
{tipo = "ulong";}
break;
case 33:
//#line 101 "Gramatica.y"
{ yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); ControlAsigMultiple.pushTipoDer(yyval.tipo); yyval.sval = val_peek(0).sval; }
break;
case 34:
//#line 102 "Gramatica.y"
{ yyval.tipo = val_peek(2).tipo; String t = obtenerTipoDeSimbolo(val_peek(0).sval); ControlAsigMultiple.pushTipoDer(t); yyval.sval = val_peek(2).sval + "," + val_peek(0).sval; }
break;
case 35:
//#line 103 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre constantes"); yyval = val_peek(1); }
break;
case 36:
//#line 106 "Gramatica.y"
{ String clave = chequearAmbito("", ambito, val_peek(0).sval).sval; yyval.sval = clave; yyval.tipo = obtenerTipoDeSimbolo(clave); ControlAsigMultiple.pushTipoIzq(yyval.tipo); }
break;
case 37:
//#line 107 "Gramatica.y"
{ String clave = chequearAmbito("", ambito, val_peek(0).sval).sval; yyval.tipo = val_peek(2).tipo; ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(clave)); yyval.sval = val_peek(2).sval + "," + clave; }
break;
case 38:
//#line 108 "Gramatica.y"
{ String clave = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval).sval; yyval.sval = clave; yyval.tipo = obtenerTipoDeSimbolo(clave); ControlAsigMultiple.pushTipoIzq(yyval.tipo); }
break;
case 39:
//#line 109 "Gramatica.y"
{ String clave = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval).sval; yyval.tipo = val_peek(4).tipo; ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(clave)); yyval.sval = val_peek(4).sval + "," + clave; }
break;
case 40:
//#line 110 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); yyval = val_peek(1); }
break;
case 41:
//#line 111 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); yyval = val_peek(3); }
break;
case 42:
//#line 114 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 43:
//#line 115 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 44:
//#line 116 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 45:
//#line 121 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 46:
//#line 122 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 47:
//#line 123 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 48:
//#line 124 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 49:
//#line 125 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 50:
//#line 126 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 51:
//#line 128 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 52:
//#line 129 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 57:
//#line 136 "Gramatica.y"
{
                            reportarEstructura("WHILE");
                            yyval = ArregloTercetos.completarBackPatchingWHILE();
                            ArregloTercetos.crearTerceto("WHILE_END", "_", "_");
                        }
break;
case 58:
//#line 141 "Gramatica.y"
{
                            reportarEstructura("WHILE");
                            yyval = ArregloTercetos.completarBackPatchingWHILE();
                            ArregloTercetos.crearTerceto("WHILE_END", "_", "_");
                        }
break;
case 59:
//#line 147 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");   }
break;
case 60:
//#line 148 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 61:
//#line 149 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 62:
//#line 150 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 63:
//#line 151 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 64:
//#line 154 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 65:
//#line 155 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 66:
//#line 156 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 67:
//#line 157 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 68:
//#line 158 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 69:
//#line 159 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 70:
//#line 160 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 71:
//#line 161 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 72:
//#line 162 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 73:
//#line 163 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 74:
//#line 164 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 75:
//#line 165 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 76:
//#line 166 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 78:
//#line 170 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIFDesapilaryCompletar("bl", null,null);}
break;
case 79:
//#line 173 "Gramatica.y"
{ArregloTercetos.crearTerceto("IF_START", "_", "_");}
break;
case 80:
//#line 176 "Gramatica.y"
{
                                ArregloTercetos.apilarTercetoInicialWHILE();

                                ArregloTercetos.crearTerceto("WHILE_START", "_", "_");
                              }
break;
case 81:
//#line 183 "Gramatica.y"
{ArregloTercetos.completarTercetoBackPatchingIF();}
break;
case 82:
//#line 184 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 83:
//#line 187 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 84:
//#line 188 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 85:
//#line 189 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 86:
//#line 190 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 87:
//#line 193 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 88:
//#line 194 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 89:
//#line 195 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 90:
//#line 196 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 93:
//#line 201 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 94:
//#line 202 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 95:
//#line 203 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 98:
//#line 208 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 99:
//#line 211 "Gramatica.y"
{
                                                    String funcionActual = PilaDeFuncionesLlamadas.desapilarFuncion();
                                                    String paramFormal = val_peek(0).sval + ":" + funcionActual;
                                                    String tipoFormal = obtenerTipoDeSimbolo(paramFormal);
                                                    yyval.tipo = chequearTipos(val_peek(2).tipo, tipoFormal, "->");
                                                    realizarPasajeCopiaValor(paramFormal, val_peek(2).sval);
                                                    if (esParametroCR(paramFormal)) {
                                                        registrarVinculoCR(paramFormal, val_peek(2).sval);
                                                    }
                                            }
break;
case 100:
//#line 221 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 101:
//#line 222 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 102:
//#line 225 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, val_peek(1).sval); }
break;
case 103:
//#line 227 "Gramatica.y"
{ yyval.sval = "=="; }
break;
case 104:
//#line 228 "Gramatica.y"
{ yyval.sval = ">="; }
break;
case 105:
//#line 229 "Gramatica.y"
{ yyval.sval = "<="; }
break;
case 106:
//#line 230 "Gramatica.y"
{ yyval.sval = "=!"; }
break;
case 107:
//#line 231 "Gramatica.y"
{ yyval.sval = ">"; }
break;
case 108:
//#line 232 "Gramatica.y"
{ yyval.sval = "<"; }
break;
case 109:
//#line 233 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 110:
//#line 234 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 111:
//#line 235 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 112:
//#line 236 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 113:
//#line 239 "Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                               yyval = ArregloTercetos.crearTerceto(":=", val_peek(2).sval, val_peek(0).sval);
                                                               yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, ":=");
                                                               }
break;
case 114:
//#line 245 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 115:
//#line 246 "Gramatica.y"
{yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(2).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 116:
//#line 250 "Gramatica.y"
{
                              reportarEstructura("asignacion multiple");
                              ControlAsigMultiple.compararTipos();
                              crearTercetosAsigMultiple(val_peek(2).sval, val_peek(0).sval);
                            }
break;
case 117:
//#line 258 "Gramatica.y"
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
case 118:
//#line 271 "Gramatica.y"
{
                              ArregloTercetos.crearTerceto("fin_" + val_peek(3).sval, "_", "_");
                            }
break;
case 119:
//#line 275 "Gramatica.y"
{
                              String tipoFormal = obtenerTipoDeSimbolo(val_peek(7).tipo);
                              chequearTipos(tipoFormal, val_peek(1).tipo, ":=");
                              ArregloTercetos.crearTerceto(":=", val_peek(7).tipo, val_peek(1).sval);
                              ArregloTercetos.crearTerceto("CALL", val_peek(7).sval, null);
                            }
break;
case 120:
//#line 281 "Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 121:
//#line 282 "Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 122:
//#line 283 "Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 123:
//#line 286 "Gramatica.y"
{ yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 124:
//#line 287 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); }
break;
case 125:
//#line 288 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 126:
//#line 291 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("+", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "+"); }
break;
case 127:
//#line 292 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("-", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "-");}
break;
case 128:
//#line 293 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 129:
//#line 294 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 130:
//#line 295 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 131:
//#line 296 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 132:
//#line 297 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 133:
//#line 298 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 134:
//#line 299 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 135:
//#line 302 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("*", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "*"); }
break;
case 136:
//#line 303 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("/", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "/");}
break;
case 137:
//#line 304 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 138:
//#line 305 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 139:
//#line 306 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 140:
//#line 307 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 141:
//#line 308 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 142:
//#line 309 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 143:
//#line 310 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 144:
//#line 313 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 145:
//#line 314 "Gramatica.y"
{yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); yyval = val_peek(0); }
break;
case 146:
//#line 315 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 147:
//#line 316 "Gramatica.y"
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
case 148:
//#line 327 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval);}
break;
case 149:
//#line 328 "Gramatica.y"
{       String tipo = obtenerTipoDeSimbolo(val_peek(1).sval);
                                                  if(! tipo.equals("dfloat")){
                                                      RecolectorDeErrores.agregarError("Error: La funcion TRUNC solo acepta operandos de tipo dfloat.",AnalizadorLexico.getNumeroDeLinea());
                                                  }
                                                  yyval = ArregloTercetos.crearTerceto("TRUNC", val_peek(1).sval, null);
                                                  yyval.tipo = "ulong";
                      }
break;
case 150:
//#line 335 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 151:
//#line 336 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 152:
//#line 337 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 153:
//#line 340 "Gramatica.y"
{
                             yyval = chequearAmbito("", ambito, val_peek(1).sval);
                             PilaDeFuncionesLlamadas.iniciarLlamada(ambito+":"+val_peek(1).sval);
                           }
break;
//#line 1805 "Parser.java"
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
