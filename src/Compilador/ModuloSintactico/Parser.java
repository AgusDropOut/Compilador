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
    2,    2,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    8,    5,    5,   12,   12,   12,   12,   12,   12,
   13,   14,    6,   16,   16,   16,   17,   17,   17,   17,
   17,   17,   11,   11,   11,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,   21,   18,   25,
   22,   22,   19,   19,   19,   19,   26,   26,   26,   26,
   20,   20,   20,   20,   20,   28,   28,   28,   29,   29,
   29,   27,   30,   30,   30,   30,   30,   30,   30,   30,
   30,   30,   23,   31,   31,   24,   32,   33,   10,   10,
   10,   10,   34,   34,   34,   15,   15,   15,   15,   15,
   15,   15,   15,   15,   35,   35,   35,   35,   35,   35,
   35,   35,   35,   36,   36,   36,   36,   36,   36,   36,
   36,   36,   37,   37,
};
final static short yylen[] = {                            2,
    4,    5,    3,    3,    2,    3,    1,    6,    3,    2,
    0,    2,    2,    2,    1,    1,    2,    2,    3,    3,
    1,    2,    1,    3,    3,    2,    3,    2,    3,    2,
    1,    4,    1,    1,    3,    2,    1,    3,    3,    5,
    2,    4,    1,    3,    2,   10,    6,    8,    5,    9,
    9,    4,    4,    1,    1,    1,    6,    4,    3,    5,
    3,    5,    3,    9,    9,    8,    5,    6,    6,    4,
    3,    8,    7,    6,    7,    8,    6,    1,    1,    1,
    1,    1,    3,    2,    2,    1,    3,    2,    2,    1,
    2,    3,    2,    2,    3,    1,    3,    1,    3,    3,
    2,    3,    1,    1,    1,    1,    1,    1,    1,    2,
    2,    2,    3,    1,    3,    3,    4,    0,    8,    6,
    6,    5,    1,    1,    2,    3,    3,    1,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    3,    1,    1,    1,    3,    3,    2,    4,    4,
    4,    4,    2,    4,
};
final static short yydefred[] = {                         0,
    7,   11,    0,    0,    0,   11,    0,    0,   80,   79,
    0,    0,    0,   33,    6,    0,    0,    0,    0,   12,
    0,    0,   16,   56,    0,    0,   54,   55,    0,    0,
    0,    0,    3,   18,    0,    0,    0,    0,   17,    0,
   31,    0,    0,   23,    0,   10,    0,    0,    0,   13,
    0,   14,    0,    0,    0,    0,    0,  145,    0,    0,
    0,    0,    0,    0,    0,  143,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   61,    0,    0,    0,
    0,   30,    0,    0,   28,   26,    0,    0,    9,   45,
    0,   19,   20,    0,    0,   34,    0,    0,    0,    0,
    0,  148,  153,    0,    0,    0,    0,    0,    0,  103,
  104,  105,  106,  107,  108,    0,    0,    0,   82,   78,
   81,    0,    0,    0,   71,   84,    0,    0,    0,    0,
    0,   96,    0,    0,    0,   63,   88,    0,    0,   94,
   91,   93,    0,    0,    0,    2,   52,   53,   32,  117,
   11,   24,   29,   27,   25,   44,   42,    0,   36,    0,
    0,    0,    0,    0,  141,  137,  142,  139,    0,    0,
    0,   83,    0,    0,    0,    0,  111,  110,  112,    0,
    0,    0,    0,    0,    0,   70,  138,  135,  140,  136,
  101,    0,  147,    0,   87,    0,   58,    0,  118,    0,
    0,    0,   95,   92,    0,   40,   35,  154,  152,  151,
  150,  149,    0,   67,    0,    0,   49,    0,    0,    0,
  100,   99,   97,   60,    0,   62,    0,    0,    0,  122,
    8,    0,    0,   74,    0,   47,    0,    0,   69,   77,
    0,   68,   57,    0,  120,  121,    0,    0,    0,    0,
    0,   73,    0,    0,    0,   75,    0,  124,  123,    0,
   66,    0,   72,    0,    0,    0,   76,    0,   48,  125,
  119,   64,   65,    0,   50,   51,   46,
};
final static short yydgoto[] = {                          3,
    4,    5,   17,   18,   42,   19,   20,   21,   73,   23,
   48,   44,   45,   24,   62,   97,   25,   26,   63,   74,
  124,  125,   27,   28,   29,   69,   64,  131,  132,  118,
   30,   31,  227,  260,   65,   66,   67,
};
final static short yysindex[] = {                      -116,
    0,    0,    0, -119,  283,    0,  308,  -45,    0,    0,
  -16,   -2,   24,    0,    0, -188,   49, -219, -230,    0,
  -46,  -41,    0,    0,  -13,  176,    0,    0,  295, -165,
  702,  322,    0,    0,   48,   55, -148, -142,    0, -141,
    0,   12, -211,    0, -204,    0,   88,  -32,   79,    0,
   84,    0,   98, -102,  -93,  123,  -76,    0,   27,  -24,
   55,  -28,  556,  148,   41,    0,  329,   55,  596,  157,
   55, -159,  -20,  245,   87,  165,    0,   64,  116,    0,
  172,    0,   96, -219,    0,    0,  -17, -193,    0,    0,
   37,    0,    0,   40,  275,    0,  -35,  373,  383,  413,
  487,    0,    0,   59,   61,   62,  297,  497,  501,    0,
    0,    0,    0,    0,    0,  114,  286,   55,    0,    0,
    0,  384,  290,  265,    0,    0,  511,  516,  123,  -37,
   81,    0,  317,  708, -159,    0,    0,  271,  355,    0,
    0,    0,  320,   55,   15,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   99,    0,   97,
   43,   41,   43,   41,    0,    0,    0,    0,  323,  -38,
  -18,    0,   43,   41,   43,   41,    0,    0,    0,  271,
  -87,  -66,  -87,  720,  312,    0,    0,    0,    0,    0,
    0, -190,    0,   55,    0,  763,    0,  718,    0,   55,
   55,  141,    0,    0,  345,    0,    0,    0,    0,    0,
    0,    0,  636,    0,  -87,  680,    0, -145,  730, -145,
    0,    0,    0,    0,  740,    0,  332,  258,  296,    0,
    0,  786,  314,    0,  696,    0,  788,  324,    0,    0,
 -145,    0,    0,   10,    0,    0, -145,  750, -145,  797,
  327,    0, -145,  761, -145,    0,  125,    0,    0,  346,
    0, -145,    0, -145,  774, -145,    0, -145,    0,    0,
    0,    0,    0, -145,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    1,    0,    0,  396,   11,    0,    0,
    0,    0,  -25,    0,    0,    0,   21,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  397,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -34,   25,    0,    0,
    0,    0,   -4,    0,    0,    0,    0,    0,   35,    0,
    0,    0,    0,  659,   94,    0,    0,    0,    0,  647,
    0,    0,    0,    0,  409,    0,    0,    0,    0,  -15,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   -1,    0,   28,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  670,    0,    0,    0,
    0,    0,    0,    0,    0,  523,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  101,    0,
    0,    0,  686,   32,    0,    0,    0,   33,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  119,  142,  164,  194,    0,    0,    0,    0,   71,    0,
    0,    0,  401,  431,  453,  475,    0,    0,    0,  365,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   16,    0,    0,    0,   26,    0,    0,   -5,    0,
    0,  326,    0,    0,  706,    0,    0,    0,    0,  560,
 -153,  621,    0,    0,    0,    0,   18,    0,  217,    0,
    0,    0,    0,    0,   42,   20,    0,
};
final static int YYTABLESIZE=1062;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         22,
   11,   22,  210,    6,  117,  108,    2,  109,  160,   43,
   21,   91,   50,   34,  108,  106,  109,   52,   37,    7,
   15,   32,  212,   35,   43,   46,   22,  213,   39,  216,
   54,  115,  116,  114,   47,   37,   40,   36,  141,   41,
   11,   38,   38,   43,   85,   39,   70,   55,   41,   14,
   21,   87,   83,   86,  257,   84,   41,  123,  215,   38,
   15,  235,  154,  136,   14,  221,  103,  144,  145,   37,
   88,  155,  104,  204,  222,  144,  144,  144,  107,  144,
   14,  144,  127,   22,  100,  133,  116,  128,   77,  101,
   59,  113,   57,  144,  144,  144,  144,    9,   10,   57,
   71,   11,   12,  146,  148,   13,  108,   39,  109,   43,
  119,  146,  146,  146,  121,  146,   80,  146,  185,  166,
  168,  193,   81,   82,  194,   11,  128,   89,  197,  146,
  146,  146,  146,  145,  128,   21,  128,   92,  128,  162,
  164,   98,   93,   94,   98,   15,  188,  190,    1,  174,
  176,  133,  128,  128,  128,  128,  149,  144,  108,  133,
  109,  133,   95,  133,  100,   98,  205,   99,  119,  101,
   96,  120,  121,  178,  129,  177,  145,  133,  133,  133,
  133,  230,  129,  108,  129,  109,  129,  102,  126,  142,
    9,   10,  145,  146,   11,   12,  134,  137,   13,   22,
  129,  129,  129,  129,  134,  147,  134,  233,  134,   49,
  238,  146,  150,  145,   51,   61,  128,  209,  151,  145,
   57,   43,  134,  134,  134,  134,  131,  191,  159,  251,
   43,  105,   90,  192,  131,  140,  131,  211,  131,   37,
  114,  133,  145,  110,  111,  112,  113,  153,  145,   39,
  115,   53,  131,  131,  131,  131,   11,   11,   11,  145,
   41,   11,   11,   38,  129,   11,   21,   21,   21,   11,
  203,   21,   21,  258,  259,   21,   15,   15,   15,   21,
   22,   15,   15,  116,  144,   15,  134,   59,  113,   15,
  144,  144,  144,  144,  144,  144,  144,  144,  245,  144,
  108,  156,  109,   56,  157,  144,  144,  144,  144,  144,
   56,   58,   59,  108,   60,  109,  131,   76,   58,   59,
  158,   60,   16,  169,  170,  171,  146,  146,  146,  146,
  146,  146,  146,  146,   68,  146,  246,  172,  108,   57,
  109,  146,  146,  146,  146,  146,  179,   16,  183,  128,
  128,  128,  128,  128,  128,  128,  128,  195,  128,  201,
  207,   16,  208,  206,  128,  128,  128,  128,  128,  143,
  220,  244,  249,   57,  133,  133,  133,  133,  133,  133,
  133,  133,  255,  133,   16,  266,  271,  184,  270,  133,
  133,  133,  133,  133,  200,    5,    4,  129,  129,  129,
  129,  129,  129,  129,  129,  102,  129,   15,    1,  152,
  223,    0,  129,  129,  129,  129,  129,   57,    0,  134,
  134,  134,  134,  134,  134,  134,  134,   57,  134,    0,
    0,   56,   33,  130,  134,  134,  134,  134,  134,   58,
   59,  130,   60,  130,    0,  130,   75,    0,    0,  131,
  131,  131,  131,  131,  131,  131,  131,   57,  131,  130,
  130,  130,  130,  126,  131,  131,  131,  131,  131,  231,
    0,  126,    0,  126,    0,  126,    0,    0,    0,  199,
    0,    0,    0,    0,    0,  132,    0,  102,    0,  126,
  126,  126,  126,  132,    0,  132,    0,  132,    0,    0,
  142,    9,   10,    0,    0,   11,   12,  127,  181,   13,
    0,  132,  132,  132,  132,  127,    0,  127,    0,  127,
  119,    9,   10,  130,  121,   11,   12,    0,    0,   13,
    0,   57,    0,  127,  127,  127,  127,    0,    8,    9,
   10,   57,    0,   11,   12,   57,    0,   13,    0,    0,
   56,   14,    0,  126,    0,   57,    0,    0,   58,   59,
   57,   60,    0,    8,    9,   10,    0,  109,   11,   12,
    0,    0,   13,    0,    0,  132,   14,    8,    9,   10,
    0,    0,   11,   12,  129,    0,   13,    0,    0,    0,
   14,    0,   58,   59,    0,   60,    0,  127,    0,    0,
    8,    9,   10,    0,    0,   11,   12,    0,    0,   13,
  142,    9,   10,   14,    0,   11,   12,    0,    0,   13,
  102,  102,  102,  102,  102,  102,  102,  102,  161,  102,
    0,  139,    0,    0,    0,    0,   58,   59,  163,   60,
    9,   10,    0,    0,   11,   12,   58,   59,   13,   60,
    0,    0,    0,    0,    0,    0,  130,  130,  130,  130,
  130,  130,  130,  130,    0,  130,    0,    0,  165,    0,
    0,  130,  130,  130,  130,  130,   58,   59,  122,   60,
    0,  182,    0,    0,    0,    0,  126,  126,  126,  126,
  126,  126,  126,  126,  198,  126,    0,    0,    0,    0,
    0,  126,  126,  126,  126,  126,    0,    0,  132,  132,
  132,  132,  132,  132,  132,  132,    0,  132,  135,    0,
    0,    0,    0,  132,  132,  132,  132,  132,    0,    0,
  127,  127,  127,  127,  127,  127,  127,  127,    0,  127,
   78,   79,  167,  219,  186,  127,  127,  127,  127,  127,
   58,   59,  173,   60,    0,  225,  175,    0,  232,    0,
   58,   59,    0,   60,   58,   59,  187,   60,    0,   90,
    0,  189,  130,    0,   58,   59,  138,   60,  109,   58,
   59,   86,   60,    0,    0,    0,  109,  109,    0,  109,
    0,  248,   85,    0,    0,    0,  254,    0,    0,    0,
    0,  214,  237,  217,    0,    0,    0,    0,   89,  265,
    0,  119,    9,   10,  120,  121,   11,   12,  250,    0,
   13,    0,    0,  180,   72,    0,    0,    0,    0,    0,
  196,    0,    0,  234,    0,  236,  239,    0,  240,    0,
  242,    0,  226,    0,  218,    0,    0,    0,    0,  202,
    0,    0,    9,   10,  241,  252,   11,   12,  134,    0,
   13,  256,    0,    0,  243,    0,    0,  261,    0,  263,
    0,    0,    0,  267,  262,  269,    0,    0,    0,    0,
    0,    0,  272,    0,  273,  268,  275,  224,  276,    0,
    0,  119,    9,   10,  277,  121,   11,   12,  274,  130,
   13,    0,    0,   90,   90,  228,  229,   90,   90,   90,
  247,   90,  253,    0,   86,   86,   86,   86,   86,   86,
   86,  264,    0,   86,    0,   85,   85,   85,   85,   85,
   85,   85,    0,    0,   85,  119,    9,   10,    0,  121,
   11,   12,   89,   89,   13,    0,   89,   89,   89,    0,
   89,  119,    9,   10,    0,  121,   11,   12,    9,   10,
   13,    0,   11,   12,    9,   10,   13,    0,   11,   12,
    0,    0,   13,  142,    9,   10,    9,   10,   11,   12,
   11,   12,   13,    0,   13,  142,    9,   10,    0,    0,
   11,   12,    0,    0,   13,  142,    9,   10,    0,    0,
   11,   12,    0,    0,   13,  142,    9,   10,    0,    0,
   11,   12,    0,    0,   13,    0,  142,    9,   10,    9,
   10,   11,   12,   11,   12,   13,    0,   13,    0,  142,
    9,   10,    0,    0,   11,   12,    0,    0,   13,    0,
    0,    0,    9,   10,    9,   10,   11,   12,   11,   12,
   13,    0,   13,    9,   10,    0,    0,   11,   12,    0,
    0,   13,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
    0,    7,   41,  123,   33,   43,  123,   45,   44,   44,
    0,   44,   59,   59,   43,   40,   45,   59,   44,    4,
    0,    6,   41,   40,   59,  256,   32,  181,   44,  183,
   44,   60,   61,   62,  265,   61,  256,   40,   59,   44,
   40,   16,   44,   18,  256,   61,   29,   61,  268,  269,
   40,  256,   41,  265,   45,   44,   61,   63,  125,   61,
   40,  215,  256,   69,  269,  256,   40,   33,   74,   46,
   45,  265,   46,   59,  265,   41,   42,   43,   61,   45,
  269,   47,   42,   59,   42,   68,   59,   47,   41,   47,
   59,   59,   45,   59,   60,   61,   62,  257,  258,   45,
  266,  261,  262,   33,   41,  265,   43,   59,   45,   84,
  256,   41,   42,   43,  260,   45,  265,   47,  124,  100,
  101,   41,  265,  265,   44,  125,   33,   40,  134,   59,
   60,   61,   62,  139,   41,  125,   43,   59,   45,   98,
   99,   41,   59,   46,   44,  125,  127,  128,  265,  108,
  109,   33,   59,   60,   61,   62,   41,  123,   43,   41,
   45,   43,  265,   45,   42,   43,  151,   45,  256,   47,
  264,  259,  260,   60,   33,   62,  182,   59,   60,   61,
   62,   41,   41,   43,   43,   45,   45,  264,   41,  256,
  257,  258,  198,  123,  261,  262,   33,   41,  265,  205,
   59,   60,   61,   62,   41,   41,   43,  213,   45,  256,
  216,  125,   41,  219,  256,   40,  123,  256,  123,  225,
   45,  256,   59,   60,   61,   62,   33,  265,  264,  235,
  265,  256,  265,  271,   41,  256,   43,  256,   45,  265,
  266,  123,  248,  272,  273,  274,  275,  265,  254,  265,
  266,  265,   59,   60,   61,   62,  256,  257,  258,  265,
  265,  261,  262,  265,  123,  265,  256,  257,  258,  269,
  256,  261,  262,  264,  265,  265,  256,  257,  258,  269,
  256,  261,  262,  256,   40,  265,  123,  256,  256,  269,
  256,  257,  258,  259,  260,  261,  262,  263,   41,  265,
   43,  265,   45,  256,  265,  271,  272,  273,  274,  275,
  256,  264,  265,   43,  267,   45,  123,  270,  264,  265,
   46,  267,   40,  265,  264,  264,  256,  257,  258,  259,
  260,  261,  262,  263,   40,  265,   41,   41,   43,   45,
   45,  271,  272,  273,  274,  275,   61,   40,   59,  256,
  257,  258,  259,  260,  261,  262,  263,   41,  265,   40,
  264,   40,   40,  265,  271,  272,  273,  274,  275,  125,
   59,   40,   59,   45,  256,  257,  258,  259,  260,  261,
  262,  263,   59,  265,   40,   59,   41,  123,  264,  271,
  272,  273,  274,  275,   40,    0,    0,  256,  257,  258,
  259,  260,  261,  262,  263,   41,  265,  125,    0,   84,
  194,   -1,  271,  272,  273,  274,  275,   45,   -1,  256,
  257,  258,  259,  260,  261,  262,  263,   45,  265,   -1,
   -1,  256,  125,   33,  271,  272,  273,  274,  275,  264,
  265,   41,  267,   43,   -1,   45,  125,   -1,   -1,  256,
  257,  258,  259,  260,  261,  262,  263,   45,  265,   59,
   60,   61,   62,   33,  271,  272,  273,  274,  275,  125,
   -1,   41,   -1,   43,   -1,   45,   -1,   -1,   -1,  125,
   -1,   -1,   -1,   -1,   -1,   33,   -1,  123,   -1,   59,
   60,   61,   62,   41,   -1,   43,   -1,   45,   -1,   -1,
  256,  257,  258,   -1,   -1,  261,  262,   33,  125,  265,
   -1,   59,   60,   61,   62,   41,   -1,   43,   -1,   45,
  256,  257,  258,  123,  260,  261,  262,   -1,   -1,  265,
   -1,   45,   -1,   59,   60,   61,   62,   -1,  256,  257,
  258,   45,   -1,  261,  262,   45,   -1,  265,   -1,   -1,
  256,  269,   -1,  123,   -1,   45,   -1,   -1,  264,  265,
   45,  267,   -1,  256,  257,  258,   -1,   45,  261,  262,
   -1,   -1,  265,   -1,   -1,  123,  269,  256,  257,  258,
   -1,   -1,  261,  262,  256,   -1,  265,   -1,   -1,   -1,
  269,   -1,  264,  265,   -1,  267,   -1,  123,   -1,   -1,
  256,  257,  258,   -1,   -1,  261,  262,   -1,   -1,  265,
  256,  257,  258,  269,   -1,  261,  262,   -1,   -1,  265,
  256,  257,  258,  259,  260,  261,  262,  263,  256,  265,
   -1,   72,   -1,   -1,   -1,   -1,  264,  265,  256,  267,
  257,  258,   -1,   -1,  261,  262,  264,  265,  265,  267,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,   -1,  265,   -1,   -1,  256,   -1,
   -1,  271,  272,  273,  274,  275,  264,  265,  123,  267,
   -1,  122,   -1,   -1,   -1,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  135,  265,   -1,   -1,   -1,   -1,
   -1,  271,  272,  273,  274,  275,   -1,   -1,  256,  257,
  258,  259,  260,  261,  262,  263,   -1,  265,  123,   -1,
   -1,   -1,   -1,  271,  272,  273,  274,  275,   -1,   -1,
  256,  257,  258,  259,  260,  261,  262,  263,   -1,  265,
   35,   36,  256,  184,  124,  271,  272,  273,  274,  275,
  264,  265,  256,  267,   -1,  196,  256,   -1,  123,   -1,
  264,  265,   -1,  267,  264,  265,  256,  267,   -1,  123,
   -1,  256,   67,   -1,  264,  265,   71,  267,  256,  264,
  265,  123,  267,   -1,   -1,   -1,  264,  265,   -1,  267,
   -1,  232,  123,   -1,   -1,   -1,  237,   -1,   -1,   -1,
   -1,  181,  123,  183,   -1,   -1,   -1,   -1,  123,  250,
   -1,  256,  257,  258,  259,  260,  261,  262,  123,   -1,
  265,   -1,   -1,  118,  123,   -1,   -1,   -1,   -1,   -1,
  123,   -1,   -1,  213,   -1,  215,  216,   -1,  218,   -1,
  220,   -1,  125,   -1,  125,   -1,   -1,   -1,   -1,  144,
   -1,   -1,  257,  258,  125,  235,  261,  262,  263,   -1,
  265,  241,   -1,   -1,  125,   -1,   -1,  247,   -1,  249,
   -1,   -1,   -1,  253,  125,  255,   -1,   -1,   -1,   -1,
   -1,   -1,  262,   -1,  264,  125,  266,  125,  268,   -1,
   -1,  256,  257,  258,  274,  260,  261,  262,  125,  194,
  265,   -1,   -1,  257,  258,  200,  201,  261,  262,  263,
  125,  265,  125,   -1,  256,  257,  258,  259,  260,  261,
  262,  125,   -1,  265,   -1,  256,  257,  258,  259,  260,
  261,  262,   -1,   -1,  265,  256,  257,  258,   -1,  260,
  261,  262,  257,  258,  265,   -1,  261,  262,  263,   -1,
  265,  256,  257,  258,   -1,  260,  261,  262,  257,  258,
  265,   -1,  261,  262,  257,  258,  265,   -1,  261,  262,
   -1,   -1,  265,  256,  257,  258,  257,  258,  261,  262,
  261,  262,  265,   -1,  265,  256,  257,  258,   -1,   -1,
  261,  262,   -1,   -1,  265,  256,  257,  258,   -1,   -1,
  261,  262,   -1,   -1,  265,  256,  257,  258,   -1,   -1,
  261,  262,   -1,   -1,  265,   -1,  256,  257,  258,  257,
  258,  261,  262,  261,  262,  265,   -1,  265,   -1,  256,
  257,  258,   -1,   -1,  261,  262,   -1,   -1,  265,   -1,
   -1,   -1,  257,  258,  257,  258,  261,  262,  261,  262,
  265,   -1,  265,  257,  258,   -1,   -1,  261,  262,   -1,
   -1,  265,
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
"sentencia : sentencia_declarativa ';'",
"sentencia : sentencia_ejecutable ';'",
"sentencia : declaracion_funcion",
"sentencia : expresion_lambda",
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
"inicio_llamado : ID '.' ID '('",
};

//#line 389 "Gramatica.y"
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
//#line 1009 "Parser.java"
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
//#line 31 "Gramatica.y"
{ reportarEstructura("Fin del programa");}
break;
case 2:
//#line 32 "Gramatica.y"
{ yyerror("Error: '}' de mas al final de programa");}
break;
case 3:
//#line 33 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '{' al inicio"); }
break;
case 4:
//#line 34 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '}' al final"); }
break;
case 5:
//#line 35 "Gramatica.y"
{ yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
break;
case 6:
//#line 36 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del programa"); }
break;
case 7:
//#line 39 "Gramatica.y"
{ambito = val_peek(0).sval; prog_principal = val_peek(0).sval;}
break;
case 8:
//#line 42 "Gramatica.y"
{ reportarEstructura("declaracion de funcion");
                                                                                            String etiquetaFin = "fin_" + ambito;
                                                                                            ArregloTercetos.crearTerceto(etiquetaFin, "_", "_");
                                                                                            salirAmbito();
                                                                                            chequearReturn();
                                                                                          }
break;
case 9:
//#line 50 "Gramatica.y"
{
                                 declaracionDeFuncion(val_peek(1).sval, ambito, "Función");
                                 entrarAmbito(val_peek(1).sval);
                                 pilaReturns.push(val_peek(1).sval + ":" + "false");
                                 String etiquetaInicio = "ini_" + ambito ;
                                 ArregloTercetos.crearTerceto(etiquetaInicio, "_", "_");
                                }
break;
case 10:
//#line 57 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre a la función"); }
break;
case 17:
//#line 68 "Gramatica.y"
{ yyerror("Error: No debe haber ';' después de la declaración de función"); }
break;
case 18:
//#line 69 "Gramatica.y"
{ yyerror("Error: Sentencia inválida detectada — se descartó hasta ';'"); }
break;
case 19:
//#line 70 "Gramatica.y"
{ yyerror("Error de sintaxis: declaración mal formada o faltante del ';'"); }
break;
case 20:
//#line 71 "Gramatica.y"
{ yyerror("Error de sintaxis: sentencia ejecutable mal formada o faltante del ';'"); }
break;
case 21:
//#line 72 "Gramatica.y"
{ yyerror("Error: Sentencia mal formada o falta ';' antes del fin del bloque"); }
break;
case 22:
//#line 75 "Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 25:
//#line 82 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cr");}
break;
case 26:
//#line 83 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cv");}
break;
case 27:
//#line 84 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 28:
//#line 85 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 29:
//#line 86 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 30:
//#line 87 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 32:
//#line 93 "Gramatica.y"
{registrarReturn();
                                                          String varRetorno = "_ret_" + ambito;
                                                          ArregloTercetos.crearTerceto(":=", varRetorno, val_peek(1).sval);
                                                          ArregloTercetos.crearTerceto("RETURN", varRetorno, val_peek(1).sval);
                                                          ArregloTercetos.crearTerceto("JMP", "fin_" + ambito, null);
                                             }
break;
case 33:
//#line 101 "Gramatica.y"
{tipo = "ulong";}
break;
case 34:
//#line 104 "Gramatica.y"
{ yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); ControlAsigMultiple.pushTipoDer(yyval.tipo); yyval.sval = val_peek(0).sval; }
break;
case 35:
//#line 105 "Gramatica.y"
{ yyval.tipo = val_peek(2).tipo; String t = obtenerTipoDeSimbolo(val_peek(0).sval); ControlAsigMultiple.pushTipoDer(t); yyval.sval = val_peek(2).sval + "," + val_peek(0).sval; }
break;
case 36:
//#line 106 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre constantes"); yyval = val_peek(1); }
break;
case 37:
//#line 109 "Gramatica.y"
{ String clave = chequearAmbito("", ambito, val_peek(0).sval).sval; yyval.sval = clave; yyval.tipo = obtenerTipoDeSimbolo(clave); ControlAsigMultiple.pushTipoIzq(yyval.tipo); }
break;
case 38:
//#line 110 "Gramatica.y"
{ String clave = chequearAmbito("", ambito, val_peek(0).sval).sval; yyval.tipo = val_peek(2).tipo; ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(clave)); yyval.sval = val_peek(2).sval + "," + clave; }
break;
case 39:
//#line 111 "Gramatica.y"
{ String clave = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval).sval; yyval.sval = clave; yyval.tipo = obtenerTipoDeSimbolo(clave); ControlAsigMultiple.pushTipoIzq(yyval.tipo); }
break;
case 40:
//#line 112 "Gramatica.y"
{ String clave = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval).sval; yyval.tipo = val_peek(4).tipo; ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(clave)); yyval.sval = val_peek(4).sval + "," + clave; }
break;
case 41:
//#line 113 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); yyval = val_peek(1); }
break;
case 42:
//#line 114 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); yyval = val_peek(3); }
break;
case 43:
//#line 117 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 44:
//#line 118 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 45:
//#line 119 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 46:
//#line 124 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 47:
//#line 125 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 48:
//#line 126 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 49:
//#line 127 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 50:
//#line 128 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 51:
//#line 129 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 52:
//#line 131 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 53:
//#line 132 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 57:
//#line 138 "Gramatica.y"
{
                            reportarEstructura("WHILE");
                            yyval = ArregloTercetos.completarBackPatchingWHILE();
                            ArregloTercetos.crearTerceto("WHILE_END", "_", "_");
                        }
break;
case 58:
//#line 143 "Gramatica.y"
{
                            reportarEstructura("WHILE");
                            yyval = ArregloTercetos.completarBackPatchingWHILE();
                            ArregloTercetos.crearTerceto("WHILE_END", "_", "_");
                        }
break;
case 59:
//#line 149 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");   }
break;
case 60:
//#line 150 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 61:
//#line 151 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 62:
//#line 152 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 63:
//#line 153 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 64:
//#line 156 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 65:
//#line 157 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 66:
//#line 158 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 67:
//#line 159 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 68:
//#line 160 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 69:
//#line 161 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 70:
//#line 162 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 71:
//#line 163 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 72:
//#line 164 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 73:
//#line 165 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 74:
//#line 166 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 75:
//#line 167 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 76:
//#line 168 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 78:
//#line 172 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIFDesapilaryCompletar("bl", null,null);}
break;
case 79:
//#line 175 "Gramatica.y"
{ArregloTercetos.crearTerceto("IF_START", "_", "_");}
break;
case 80:
//#line 178 "Gramatica.y"
{
                                ArregloTercetos.apilarTercetoInicialWHILE();

                                ArregloTercetos.crearTerceto("WHILE_START", "_", "_");
                              }
break;
case 81:
//#line 185 "Gramatica.y"
{ArregloTercetos.completarTercetoBackPatchingIF();}
break;
case 82:
//#line 186 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 83:
//#line 189 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 84:
//#line 190 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 85:
//#line 191 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 86:
//#line 192 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 87:
//#line 195 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 88:
//#line 196 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 89:
//#line 197 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 90:
//#line 198 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 93:
//#line 203 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 94:
//#line 204 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 95:
//#line 205 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 98:
//#line 210 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 99:
//#line 213 "Gramatica.y"
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
case 100:
//#line 253 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 101:
//#line 254 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 102:
//#line 257 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, val_peek(1).sval); }
break;
case 103:
//#line 259 "Gramatica.y"
{ yyval.sval = "=="; }
break;
case 104:
//#line 260 "Gramatica.y"
{ yyval.sval = ">="; }
break;
case 105:
//#line 261 "Gramatica.y"
{ yyval.sval = "<="; }
break;
case 106:
//#line 262 "Gramatica.y"
{ yyval.sval = "=!"; }
break;
case 107:
//#line 263 "Gramatica.y"
{ yyval.sval = ">"; }
break;
case 108:
//#line 264 "Gramatica.y"
{ yyval.sval = "<"; }
break;
case 109:
//#line 265 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 110:
//#line 266 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 111:
//#line 267 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 112:
//#line 268 "Gramatica.y"
{RecolectorDeErrores.agregarError("Error: Operador invalido",AnalizadorLexico.getNumeroDeLinea());}
break;
case 113:
//#line 271 "Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                               yyval = ArregloTercetos.crearTerceto(":=", val_peek(2).sval, val_peek(0).sval);
                                                               yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, ":=");
                                                               }
break;
case 114:
//#line 277 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 115:
//#line 278 "Gramatica.y"
{yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(2).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 116:
//#line 282 "Gramatica.y"
{
                              reportarEstructura("asignacion multiple");
                              ControlAsigMultiple.compararTipos();
                              crearTercetosAsigMultiple(val_peek(2).sval, val_peek(0).sval);
                            }
break;
case 117:
//#line 290 "Gramatica.y"
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
//#line 303 "Gramatica.y"
{
                              ArregloTercetos.crearTerceto("fin_" + val_peek(3).sval, "_", "_");
                            }
break;
case 119:
//#line 307 "Gramatica.y"
{
                              String tipoFormal = obtenerTipoDeSimbolo(val_peek(7).tipo);
                              chequearTipos(tipoFormal, val_peek(1).tipo, ":=");
                              ArregloTercetos.crearTerceto(":=", val_peek(7).tipo, val_peek(1).sval);
                              ArregloTercetos.crearTerceto("CALL", val_peek(7).sval, null);
                            }
break;
case 120:
//#line 313 "Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 121:
//#line 314 "Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 122:
//#line 315 "Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 123:
//#line 318 "Gramatica.y"
{ yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 124:
//#line 319 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); }
break;
case 125:
//#line 320 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 126:
//#line 323 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("+", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "+"); }
break;
case 127:
//#line 324 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("-", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "-");}
break;
case 128:
//#line 325 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 129:
//#line 326 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 130:
//#line 327 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 131:
//#line 328 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 132:
//#line 329 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 133:
//#line 330 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 134:
//#line 331 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 135:
//#line 334 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("*", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "*"); }
break;
case 136:
//#line 335 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("/", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, "/");}
break;
case 137:
//#line 336 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 138:
//#line 337 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 139:
//#line 338 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 140:
//#line 339 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 141:
//#line 340 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 142:
//#line 341 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 143:
//#line 342 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 144:
//#line 345 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 145:
//#line 346 "Gramatica.y"
{yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); yyval = val_peek(0); }
break;
case 146:
//#line 347 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 147:
//#line 348 "Gramatica.y"
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
//#line 360 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval);}
break;
case 149:
//#line 361 "Gramatica.y"
{       String tipo = obtenerTipoDeSimbolo(val_peek(1).sval);
                                                  if(! tipo.equals("dfloat")){
                                                      RecolectorDeErrores.agregarError("Error: La funcion TRUNC solo acepta operandos de tipo dfloat.",AnalizadorLexico.getNumeroDeLinea());
                                                  }
                                                  yyval = ArregloTercetos.crearTerceto("TRUNC", val_peek(1).sval, null);
                                                  yyval.tipo = "ulong";
                      }
break;
case 150:
//#line 368 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 151:
//#line 369 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 152:
//#line 370 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 153:
//#line 373 "Gramatica.y"
{
                             /* $$ obtiene la clave canónica (ej: C:PROGRAMAFUNCIONAL)*/
                             yyval = chequearAmbito("", ambito, val_peek(1).sval);
                             /* CORRECCIÓN 1: Usar $$.sval*/
                             PilaDeFuncionesLlamadas.iniciarLlamada(yyval.sval);
                           }
break;
case 154:
//#line 379 "Gramatica.y"
{
                             /* $$ obtiene la clave canónica (ej: C:PROGRAMAFUNCIONAL)*/
                             yyval = chequearAmbito(val_peek(3).sval, ambito, val_peek(1).sval);
                             /* CORRECCIÓN 2: Usar $$.sval*/
                             PilaDeFuncionesLlamadas.iniciarLlamada(yyval.sval);
                                      }
break;
//#line 1822 "Parser.java"
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
