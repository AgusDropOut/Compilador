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
import Compilador.Util.RecolectorDeErrores; /* <-- 1. IMPORTACIÓN AÑADIDA*/
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

//#line 37 "Parser.java"




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
public final static short COMP=271;
public final static short CADENA=272;
public final static short FLECHA=273;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    0,    0,    1,    3,    4,    4,
    2,    2,    7,    7,    7,    7,    7,    7,    7,    7,
    8,    5,    5,   11,   11,   11,   11,   11,   11,   12,
   13,    6,   15,   15,   15,   16,   16,   16,   16,   16,
   16,   10,   10,   10,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,    9,    9,    9,    9,    9,   19,   24,   20,
   20,   17,   17,   17,   17,   25,   25,   25,   25,   18,
   18,   18,   18,   18,   27,   27,   27,   28,   28,   28,
   26,   21,   29,   29,   22,   30,   31,   23,   23,   23,
   23,   32,   32,   32,   14,   14,   14,   14,   14,   14,
   14,   14,   14,   33,   33,   33,   33,   33,   33,   33,
   33,   33,   34,   34,   34,   34,   34,   34,   34,   34,
   34,   35,
};
final static short yylen[] = {                            2,
    4,    5,    3,    3,    2,    3,    1,    6,    3,    2,
    0,    2,    2,    2,    1,    2,    2,    2,    2,    1,
    2,    1,    3,    3,    2,    3,    2,    3,    2,    1,
    4,    1,    1,    3,    2,    1,    3,    3,    5,    2,
    4,    1,    3,    2,   10,    6,    8,    5,    9,    9,
    4,    4,    1,    1,    1,    1,    6,    4,    3,    5,
    3,    5,    3,    9,    9,    8,    5,    6,    6,    4,
    3,    8,    7,    6,    7,    8,    6,    1,    1,    1,
    1,    3,    2,    2,    1,    3,    2,    2,    1,    2,
    3,    2,    2,    3,    1,    3,    1,    3,    3,    2,
    3,    3,    1,    3,    3,    4,    0,    8,    6,    6,
    5,    1,    1,    2,    3,    3,    1,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,    1,    1,    1,    3,    3,    2,    4,    4,    4,
    4,    2,
};
final static short yydefred[] = {                         0,
    7,   11,    0,    0,    0,   11,    0,    0,    0,   79,
    0,    0,    0,    0,   32,    6,    0,    0,    0,   12,
    0,    0,   55,    0,   53,   54,   56,    0,    0,    0,
    0,    3,   19,    0,    0,    0,    0,  134,    0,    0,
    0,    0,    0,    0,  132,    0,    0,    0,    0,   16,
    0,   30,    0,    0,   22,    0,   10,    0,    0,   17,
   13,   18,   14,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  137,
    0,  142,    0,    0,    0,    0,    0,    0,   81,   78,
   80,    0,    0,    0,   71,   83,    0,    0,    0,    0,
    0,   95,    0,   61,    0,    0,    0,   29,    0,    0,
   27,   25,    0,    0,    9,   44,    0,    0,    0,   33,
    0,    0,    0,    0,   63,   87,    0,    0,   93,   90,
   92,    0,    0,    0,    2,  106,    0,    0,    0,    0,
  130,  126,  131,  128,   82,  135,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   70,  127,
  124,  129,  125,  100,    0,  136,    0,   51,   52,   31,
   11,   23,   28,   26,   24,   43,   41,    0,   35,    0,
   86,    0,   58,    0,    0,  107,    0,    0,   94,   91,
  141,  140,  139,  138,    0,   67,    0,    0,   48,    0,
    0,    0,   99,   98,   96,    0,   39,   34,   60,    0,
   62,    0,    0,  111,    0,    0,    0,   74,    0,   46,
    0,    0,   69,   77,    0,   68,    8,   57,  109,    0,
  110,    0,    0,    0,    0,    0,   73,    0,    0,    0,
   75,    0,  113,  112,    0,   66,    0,   72,    0,    0,
    0,   76,    0,   47,  114,  108,   64,   65,    0,   49,
   50,   45,
};
final static short yydgoto[] = {                          3,
    4,    5,   17,   18,   53,   19,   20,   21,   72,   59,
   55,   56,   23,   41,  121,   24,   42,   73,   94,   95,
   25,   26,   27,   28,   68,   43,  101,  102,   29,   30,
  213,  245,   44,   45,   46,
};
final static short yysindex[] = {                      -115,
    0,    0,    0, -112,  184,    0,  244,  -16, -210,    0,
  -36,   27,   47,   35,    0,    0,   42, -231, -221,    0,
  -47,  -46,    0,  -10,    0,    0,    0,  152, -173,  580,
  279,    0,    0, -152,   82, -146,  292,    0,   58,  -33,
  -40,  332,   98,   29,    0,  305,  102,  292, -126,    0,
 -113,    0,  104, -204,    0, -224,    0,  117,   17,    0,
    0,    0,    0,  121,  -95,  -92,  292,  433,  133,  292,
  578,  -45,  442,   51,  136,  509,  567,  598,  635,    0,
  142,    0,  -81,  292,  292,  661,  676,  292,    0,    0,
    0,  599,  129,  375,    0,    0,  680,  684,   82,  -12,
  105,    0,  146,    0,   65,   87,    0,    0,   66, -231,
    0,    0,  -75, -202,    0,    0,  -67,  -66,  156,    0,
  -21,  166,  590,  578,    0,    0,   64,  453,    0,    0,
    0,  229,  175,  -44,    0,    0,   36,   29,   36,   29,
    0,    0,    0,    0,    0,    0,   -1,   41,   36,   29,
   36,   29,   64, -119,  464, -119,  615,  158,    0,    0,
    0,    0,    0,    0, -200,    0,  292,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   -8,    0,   22,
    0,  617,    0,  475,  229,    0,  113,  292,    0,    0,
    0,    0,    0,    0,  388,    0, -119,  405,    0, -171,
  490, -171,    0,    0,    0,  306,    0,    0,    0,  510,
    0,  120,  179,    0,  125,  636,  240,    0,  416,    0,
  646,  241,    0,    0, -171,    0,    0,    0,    0,  -39,
    0, -171,  523, -171,  662,  248,    0, -171,  534, -171,
    0,   59,    0,    0,  285,    0, -171,    0, -171,  556,
 -171,    0, -171,    0,    0,    0,    0,    0, -171,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    1,    0,    0,  339,   10,    0,    0,
    0,    0,    0,  -14,    0,    0,   19,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  340,    0,    0,    0,    0,    0,    0,    0,  139,    0,
    0,    0,  348,  -23,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   61,  -43,    0,
    0,    0,    0,    4,    0,    0,    0,    0,  496,    0,
    0,    0,    0,  341,    0,    0,    0,    0,    0,    0,
  366,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  128,    0,
    0,    0,    0,    0,    0,    0,   55,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   13,    0,
  -38,  562,  -35,    0,    0,    0,  -31,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   32,   52,   72,   93,
    0,    0,    0,    0,    0,    0,    0,    0,  119,  163,
  206,  261,  321,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,   23,    0,    0,    0,  841,    0,    0,   -5,    0,
  232,    0,    0,  822,    0,    0,    0,  843, -101,  799,
    0,    0,    0,    0,    0,   21,    0,  177,    0,    0,
    0,    0,   -7,   24,    0,
};
final static int YYTABLESIZE=1078;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         22,
   11,   22,   86,   37,   87,  242,   85,    2,   36,   20,
    6,   61,   63,  130,  190,   21,  117,  117,   15,  117,
  105,  117,  180,   59,   51,   22,    7,  102,   31,   36,
   86,  113,   87,   65,   57,  117,   93,   52,   15,  192,
   11,   86,   33,   87,   58,   15,   36,   40,   69,   20,
   66,  111,  195,  174,  198,  203,   37,   81,   15,   15,
  117,  112,  125,  175,   40,  204,   47,  134,  138,  140,
   97,  122,  122,   37,  122,   98,  122,   78,  150,  152,
   49,  194,   79,   86,   89,   87,   48,  122,  158,   91,
  122,  118,  118,   70,  118,  219,  118,   82,   38,  117,
   50,  142,  144,   83,   42,  169,   86,   86,   87,   87,
  118,  123,  123,   75,  123,   38,  123,  183,   80,   42,
  161,  163,  134,   78,   76,   11,   77,  170,   79,   86,
  123,   87,  120,  120,   20,  120,   89,  120,   96,  107,
   90,   91,  104,   15,  109,  166,   36,  110,  167,  134,
    1,  120,  108,  214,  122,   86,  115,   87,  119,  119,
  229,  119,   86,  119,   87,  231,  118,   86,   97,   87,
  119,   97,  120,  126,  118,  135,  136,  119,  134,  133,
  133,  133,  145,  133,  146,  133,  168,  156,  171,  217,
  173,   67,  222,  206,  123,  134,   36,  133,  176,  177,
   22,  178,  115,  115,  134,  115,  181,  115,   60,   62,
  129,  189,   21,  236,  188,  120,  202,  105,  230,   35,
   59,  115,   84,    9,  102,  243,  244,  134,   38,   39,
   88,   40,  117,  134,  117,  117,  117,  117,  117,  117,
  117,  119,  117,  179,  134,  121,  121,  117,  121,  117,
  121,   36,  103,  164,  191,   64,   11,  207,   11,   11,
  165,  133,   11,   11,  121,   20,   11,   20,   20,   40,
   11,   20,   20,   36,   15,   20,   15,   15,   37,   20,
   15,   15,  116,    9,   15,  115,  208,  122,   15,  122,
  122,  122,  122,  122,  122,  122,  193,  122,  234,  240,
  116,  116,  122,  116,  122,  116,  251,  118,   16,  118,
  118,  118,  118,  118,  118,  118,   42,  118,    9,  116,
   38,  104,  118,  255,  118,  256,   42,  123,  121,  123,
  123,  123,  123,  123,  123,  123,   36,  123,    5,    4,
    1,  172,  123,  205,  123,    9,    0,    0,  120,   36,
  120,  120,  120,  120,  120,  120,  120,   35,  120,    0,
  101,  101,    0,  120,    0,  120,   38,   39,   32,   40,
    0,    9,    0,  103,  119,    0,  119,  119,  119,  119,
  119,  119,  119,  116,  119,    0,    0,   85,    0,  119,
    0,  119,    0,    0,  133,    0,  133,  133,  133,  133,
  133,  133,  133,   74,  133,   84,    0,   35,    0,  133,
    0,  133,    0,    0,    9,    0,   38,   39,  115,   40,
  115,  115,  115,  115,  115,  115,  115,    9,  115,    0,
  227,    0,    0,  115,    0,  115,    0,    0,    0,    8,
    0,   10,   11,  101,    9,   12,   13,    0,    0,   14,
    0,    0,    0,   15,   92,    9,    0,    0,    0,    0,
    0,  121,    0,  121,  121,  121,  121,  121,  121,  121,
   85,  121,    9,    0,    0,    0,  121,    0,  121,    0,
    0,  132,    0,    0,   35,    0,    0,    0,   84,    0,
    0,    0,  185,   38,   39,    0,   40,  157,   15,    8,
    0,   10,   11,    9,    0,   12,   13,    0,    0,   14,
  216,    0,    0,   15,    9,    0,  116,    0,  116,  116,
  116,  116,  116,  116,  116,    0,  116,  221,    0,    9,
    0,  116,    0,  116,    8,   89,   10,   11,  235,    0,
   12,   13,    0,    0,   14,    0,    0,   35,   15,    9,
    0,    0,    0,   36,    0,  124,   38,   39,    0,   40,
   99,    8,    9,   10,   11,    0,  133,   12,   13,   38,
   39,   14,   40,    9,    0,   15,  101,  186,  101,  101,
  101,  101,  101,  101,  101,    0,  101,   89,  197,   10,
   11,   90,   91,   12,   13,    9,    0,   14,    0,  211,
    0,   88,    0,   85,    0,   85,   85,   85,   85,   85,
   85,   36,    0,   85,  225,    0,    0,    9,   89,    9,
    0,   84,    0,   84,   84,   84,   84,   84,   84,    9,
   89,   84,   10,   11,  228,   91,   12,   13,    9,    0,
   14,    0,   36,   89,    0,   10,   11,  247,   91,   12,
   13,    0,    0,   14,    9,    0,    9,    0,  253,    0,
   89,    0,   10,   11,    0,   91,   12,   13,    0,    0,
   14,   89,    0,   10,   11,    9,   91,   12,   13,   36,
  259,   14,    0,    0,   88,    9,    0,    0,    0,    0,
   10,   11,    0,    0,   12,   13,  123,  131,   14,   10,
   11,    9,   71,   12,   13,   36,    0,   14,  131,    0,
   10,   11,  182,    0,   12,   13,    0,    0,   14,  131,
   36,   10,   11,  154,   36,   12,   13,    0,   36,   14,
  131,    0,   10,   11,    0,    0,   12,   13,    0,  200,
   14,  209,    0,    0,    0,  131,    0,   10,   11,    0,
    0,   12,   13,   89,   89,   14,    0,   89,   89,   89,
  232,   89,    0,    0,  137,  131,    0,   10,   11,    0,
  238,   12,   13,   38,   39,   14,   40,    0,  131,    0,
   10,   11,    0,    0,   12,   13,  249,    0,   14,  131,
    0,   10,   11,    0,    0,   12,   13,    0,    0,   14,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  131,    0,   10,   11,    0,    0,   12,   13,   88,
   88,   14,  139,   88,   88,   88,    0,   88,    0,    0,
    0,   38,   39,    0,   40,   10,   11,   10,   11,   12,
   13,   12,   13,   14,    0,   14,    0,   10,   11,   34,
    0,   12,   13,  141,    0,   14,   10,   11,   54,    0,
   12,   13,   38,   39,   14,   40,    0,  100,  105,  106,
    0,    0,   10,   11,   10,   11,   12,   13,   12,   13,
   14,    0,   14,    0,    0,    0,    0,    0,    0,    0,
  143,  127,  159,   10,   11,    0,  114,   12,   13,   38,
   39,   14,   40,   10,   11,  147,  148,   12,   13,  153,
    0,   14,    0,  128,    0,    0,  149,    0,    0,   10,
   11,    0,    0,   12,   13,   38,   39,   14,   40,    0,
    0,  151,    0,    0,  155,  160,    0,    0,    0,  162,
   38,   39,    0,   40,   38,   39,    0,   40,   38,   39,
   54,   40,  196,  187,  199,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  184,    0,    0,    0,
    0,    0,   34,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  100,    0,
    0,    0,    0,  218,    0,  220,  223,    0,  224,  201,
  226,    0,    0,    0,    0,    0,  212,    0,    0,  215,
    0,    0,    0,    0,    0,    0,    0,  237,    0,    0,
    0,    0,    0,  241,  210,   34,    0,    0,    0,    0,
  246,    0,  248,    0,    0,    0,  252,    0,  254,    0,
    0,    0,    0,    0,    0,  257,    0,  258,    0,  260,
    0,  261,    0,    0,    0,    0,    0,  262,  233,    0,
    0,    0,    0,  239,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  250,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                          5,
    0,    7,   43,   40,   45,   45,   40,  123,   45,    0,
  123,   59,   59,   59,   59,   59,   40,   41,    0,   43,
   59,   45,   44,   59,  256,   31,    4,   59,    6,   44,
   43,  256,   45,   44,  256,   59,   42,  269,  270,   41,
   40,   43,   59,   45,  266,  270,   61,   44,   28,   40,
   61,  256,  154,  256,  156,  256,   44,   37,   40,  270,
   44,  266,   68,  266,   61,  266,   40,   73,   76,   77,
   42,   40,   41,   61,   43,   47,   45,   42,   86,   87,
   46,   41,   47,   43,  256,   45,   40,   67,   94,  261,
   59,   40,   41,  267,   43,  197,   45,   40,   44,  123,
   59,   78,   79,   46,   44,   41,   43,   43,   45,   45,
   59,   40,   41,  266,   43,   61,   45,  123,  265,   59,
   97,   98,  128,   42,   43,  125,   45,   41,   47,   43,
   59,   45,   40,   41,  125,   43,  256,   45,   41,  266,
  260,  261,   41,  125,   41,   41,   45,   44,   44,  155,
  266,   59,  266,   41,  123,   43,   40,   45,   40,   41,
   41,   43,   43,   45,   45,   41,   46,   43,   41,   45,
  266,   44,  265,   41,  123,  125,   41,   59,  184,   41,
   42,   43,   41,   45,  266,   47,   41,   59,  123,  195,
  266,   40,  198,  171,  123,  201,   45,   59,  266,  266,
  206,   46,   40,   41,  210,   43,   41,   45,  256,  256,
  256,  256,  256,  219,   40,  123,   59,  256,   40,  256,
  256,   59,  256,   40,  256,  265,  266,  233,  265,  266,
  271,  268,  256,  239,  258,  259,  260,  261,  262,  263,
  264,  123,  266,  265,  250,   40,   41,  271,   43,  273,
   45,  266,  267,  266,  256,  266,  256,  266,  258,  259,
  273,  123,  262,  263,   59,  256,  266,  258,  259,  266,
  270,  262,  263,   45,  256,  266,  258,  259,  266,  270,
  262,  263,  266,   40,  266,  123,  265,  256,  270,  258,
  259,  260,  261,  262,  263,  264,  256,  266,   59,   59,
   40,   41,  271,   43,  273,   45,   59,  256,  125,  258,
  259,  260,  261,  262,  263,  264,  256,  266,   40,   59,
  266,  267,  271,  265,  273,   41,  266,  256,  123,  258,
  259,  260,  261,  262,  263,  264,   45,  266,    0,    0,
    0,  110,  271,  167,  273,   40,   -1,   -1,  256,   45,
  258,  259,  260,  261,  262,  263,  264,  256,  266,   -1,
   40,   41,   -1,  271,   -1,  273,  265,  266,  125,  268,
   -1,   40,   -1,  272,  256,   -1,  258,  259,  260,  261,
  262,  263,  264,  123,  266,   -1,   -1,   40,   -1,  271,
   -1,  273,   -1,   -1,  256,   -1,  258,  259,  260,  261,
  262,  263,  264,  125,  266,   40,   -1,  256,   -1,  271,
   -1,  273,   -1,   -1,   40,   -1,  265,  266,  256,  268,
  258,  259,  260,  261,  262,  263,  264,   40,  266,   -1,
  125,   -1,   -1,  271,   -1,  273,   -1,   -1,   -1,  256,
   -1,  258,  259,  123,   40,  262,  263,   -1,   -1,  266,
   -1,   -1,   -1,  270,  123,   40,   -1,   -1,   -1,   -1,
   -1,  256,   -1,  258,  259,  260,  261,  262,  263,  264,
  123,  266,   40,   -1,   -1,   -1,  271,   -1,  273,   -1,
   -1,   40,   -1,   -1,  256,   -1,   -1,   -1,  123,   -1,
   -1,   -1,   40,  265,  266,   -1,  268,  123,  270,  256,
   -1,  258,  259,   40,   -1,  262,  263,   -1,   -1,  266,
  123,   -1,   -1,  270,   40,   -1,  256,   -1,  258,  259,
  260,  261,  262,  263,  264,   -1,  266,  123,   -1,   40,
   -1,  271,   -1,  273,  256,   40,  258,  259,  123,   -1,
  262,  263,   -1,   -1,  266,   -1,   -1,  256,  270,   40,
   -1,   -1,   -1,   45,   -1,  123,  265,  266,   -1,  268,
  256,  256,   40,  258,  259,   -1,  125,  262,  263,  265,
  266,  266,  268,   40,   -1,  270,  256,  125,  258,  259,
  260,  261,  262,  263,  264,   -1,  266,  256,  125,  258,
  259,  260,  261,  262,  263,   40,   -1,  266,   -1,  125,
   -1,   40,   -1,  256,   -1,  258,  259,  260,  261,  262,
  263,   45,   -1,  266,  125,   -1,   -1,   40,  123,   40,
   -1,  256,   -1,  258,  259,  260,  261,  262,  263,   40,
  256,  266,  258,  259,  125,  261,  262,  263,   40,   -1,
  266,   -1,   45,  256,   -1,  258,  259,  125,  261,  262,
  263,   -1,   -1,  266,   40,   -1,   40,   -1,  125,   -1,
  256,   -1,  258,  259,   -1,  261,  262,  263,   -1,   -1,
  266,  256,   -1,  258,  259,   40,  261,  262,  263,   45,
  125,  266,   -1,   -1,  123,   40,   -1,   -1,   -1,   -1,
  258,  259,   -1,   -1,  262,  263,  264,  256,  266,  258,
  259,   40,  123,  262,  263,   45,   -1,  266,  256,   -1,
  258,  259,  123,   -1,  262,  263,   -1,   -1,  266,  256,
   45,  258,  259,  125,   45,  262,  263,   -1,   45,  266,
  256,   -1,  258,  259,   -1,   -1,  262,  263,   -1,  125,
  266,  125,   -1,   -1,   -1,  256,   -1,  258,  259,   -1,
   -1,  262,  263,  258,  259,  266,   -1,  262,  263,  264,
  125,  266,   -1,   -1,  256,  256,   -1,  258,  259,   -1,
  125,  262,  263,  265,  266,  266,  268,   -1,  256,   -1,
  258,  259,   -1,   -1,  262,  263,  125,   -1,  266,  256,
   -1,  258,  259,   -1,   -1,  262,  263,   -1,   -1,  266,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  256,   -1,  258,  259,   -1,   -1,  262,  263,  258,
  259,  266,  256,  262,  263,  264,   -1,  266,   -1,   -1,
   -1,  265,  266,   -1,  268,  258,  259,  258,  259,  262,
  263,  262,  263,  266,   -1,  266,   -1,  258,  259,    9,
   -1,  262,  263,  256,   -1,  266,  258,  259,   18,   -1,
  262,  263,  265,  266,  266,  268,   -1,   46,   47,   48,
   -1,   -1,  258,  259,  258,  259,  262,  263,  262,  263,
  266,   -1,  266,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,   70,   94,  258,  259,   -1,   56,  262,  263,  265,
  266,  266,  268,  258,  259,   84,   85,  262,  263,   88,
   -1,  266,   -1,   71,   -1,   -1,  256,   -1,   -1,  258,
  259,   -1,   -1,  262,  263,  265,  266,  266,  268,   -1,
   -1,  256,   -1,   -1,   92,  256,   -1,   -1,   -1,  256,
  265,  266,   -1,  268,  265,  266,   -1,  268,  265,  266,
  110,  268,  154,  132,  156,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  124,   -1,   -1,   -1,
   -1,   -1,  132,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  167,   -1,
   -1,   -1,   -1,  195,   -1,  197,  198,   -1,  200,  157,
  202,   -1,   -1,   -1,   -1,   -1,  185,   -1,   -1,  188,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  219,   -1,   -1,
   -1,   -1,   -1,  225,  182,  185,   -1,   -1,   -1,   -1,
  232,   -1,  234,   -1,   -1,   -1,  238,   -1,  240,   -1,
   -1,   -1,   -1,   -1,   -1,  247,   -1,  249,   -1,  251,
   -1,  253,   -1,   -1,   -1,   -1,   -1,  259,  216,   -1,
   -1,   -1,   -1,  221,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  235,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=273;
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
null,null,null,null,null,null,null,"LOWER_THAN_CALL","WHILE","IF","ELSE",
"ENDIF","PRINT","RETURN","DO","CTE","ID","ASIG","TRUNC","CR","ULONG","COMP",
"CADENA","FLECHA",
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
"sentencia : sentencia_declarativa error",
"sentencia : sentencia_ejecutable error",
"sentencia : error ';'",
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
"sentencia_ejecutable : expresion_lambda",
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
"condicion : expresion COMP expresion",
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
"factor : TRUNC '(' expresion ')'",
"factor : TRUNC '(' expresion error",
"factor : TRUNC error expresion ')'",
"factor : TRUNC error expresion error",
"inicio_llamado : ID '('",
};

//#line 450 "Gramatica.y"
public static final Set<String> erroresEmitidos = new HashSet<>();
public static int ultimaLineaError;

public void resetErrores() {
    erroresEmitidos.clear();
}


public void yyerror(String s) {
     int linea = AnalizadorLexico.getNumeroDeLinea();
     if (linea != ultimaLineaError){ //En caso de cambiar de linea, reseteamos los errores que fuimos capturando
        resetErrores();
     }
     ultimaLineaError = linea;
     String clave = linea + "|" + s; // (línea, mensaje de error)
        if (erroresEmitidos.add(clave)) { //Si todavía no se mostró el error en esa línea, lo agregamos al recolector
           RecolectorDeErrores.agregarError(s, linea); // <-- NUEVA LÍNEA
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
    String ambitoDeBusqueda = ambito; // copia de la variable global ambito
    boolean simboloEncontrado = false;
    String ambitoActual = "";
    ParserValExt val = new ParserValExt();
    val.sval = nombreIdentificador; // Valor por defecto

    if (!prefijo.isEmpty()) {

        // Bucle para buscar el ámbito del prefijo
        while (!ambitoDeBusqueda.isEmpty()) {
            int pos = ambitoDeBusqueda.lastIndexOf(":");

            if (pos == -1) {
                ambitoActual = ambitoDeBusqueda;
            } else {
                ambitoActual = ambitoDeBusqueda.substring(pos + 1);
            }

            // ¿Es este el ámbito que buscamos (por el prefijo)?
            if (ambitoActual.equals(prefijo)) {
                claveBuscada = nombreIdentificador + ":" + ambitoDeBusqueda;
                elem = TablaDeSimbolos.getSimbolo(claveBuscada);

                if (elem != null) {
                    // ¡Encontrado!
                    val.sval = claveBuscada;
                    simboloEncontrado = true;
                    break; // <-- SALIR DEL LOOP
                }
            }

            // Si no lo encontramos, subimos al ámbito padre
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
        // Lógica original para variables sin prefijo
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
            yyerror("Error: Sentencia return declarada en lugar inválido");
        }
}

public String chequearTipos(String tipo1, String tipo2){

    // No permitir operaciones entre dfloat
    if(tipo1.equals("dfloat") && tipo2.equals("dfloat")){
        yyerror("Error: No se pueden realizar operaciones entre operandos de tipo dfloat.");
        return "error";
    }

    // Si alguno ya es error, propagá el error
    if(tipo1.equals("error") || tipo2.equals("error")){
        return "error";
    }

    // Tipos distintos → error
    if(!tipo1.equals(tipo2)){
        yyerror("Error: Tipos incompatibles (" + tipo1 + " y " + tipo2 + ").");
        return "error";
    }

    // Si todo OK, devolver el tipo
    return tipo1;
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
        yyerror("Error: la variable utilizada para el pasaje copia resultado no existe en el ámbito actual o se esta utilizando algo que no es una variable.");
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
//#line 1015 "Parser.java"
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
//#line 32 "Gramatica.y"
{ reportarEstructura("Fin del programa");}
break;
case 2:
//#line 33 "Gramatica.y"
{ yyerror("Error: '}' de mas al final de programa");}
break;
case 3:
//#line 34 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '{' al inicio"); }
break;
case 4:
//#line 35 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '}' al final"); }
break;
case 5:
//#line 36 "Gramatica.y"
{ yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
break;
case 6:
//#line 37 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del programa"); }
break;
case 7:
//#line 40 "Gramatica.y"
{ambito = val_peek(0).sval;}
break;
case 8:
//#line 44 "Gramatica.y"
{ reportarEstructura("declaracion de funcion");
                                                                                            /* Etiqueta de fin de función*/
                                                                                            String etiquetaFin = "fin_" + ambito; /* o derivar el nombre de la función de otra forma*/
                                                                                            ArregloTercetos.crearTerceto(etiquetaFin, "_", "_");
                                                                                            salirAmbito();
                                                                                            chequearReturn();

                                                                                          }
break;
case 9:
//#line 54 "Gramatica.y"
{
                                 declaracionDeFuncion(val_peek(1).sval, ambito, "Función");
                                 entrarAmbito(val_peek(1).sval);
                                 pilaReturns.push(val_peek(1).sval + ":" + "false");

                                 /* Crear etiqueta de inicio de función para los tercetos*/
                                 String etiquetaInicio = "ini_" + ambito ;
                                 ArregloTercetos.crearTerceto(etiquetaInicio, "_", "_");
                                }
break;
case 10:
//#line 63 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre a la función"); }
break;
case 16:
//#line 75 "Gramatica.y"
{ yyerror("Error: No se debe colocar ';' después de la declaración de función"); }
break;
case 17:
//#line 78 "Gramatica.y"
{ yyerror("Error: Falta ';' al final de la declaración o sentencia mal formada"); }
break;
case 18:
//#line 79 "Gramatica.y"
{ yyerror("Error: Falta ';' al final de la sentencia ejecutable o sentencia mal formada" ); }
break;
case 19:
//#line 80 "Gramatica.y"
{ yyerror("Error: Sentencia inválida detectada — se descarta hasta ';'"); }
break;
case 20:
//#line 81 "Gramatica.y"
{
                                                  yyerror("Error: Sentencia inválida o mal terminada antes del cierre del bloque '}'");
                               }
break;
case 21:
//#line 89 "Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 24:
//#line 96 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cr");}
break;
case 25:
//#line 97 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cv");}
break;
case 26:
//#line 98 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 27:
//#line 99 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 28:
//#line 100 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 29:
//#line 101 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 31:
//#line 107 "Gramatica.y"
{registrarReturn();

                                                          /* Crear nombre para la variable de retorno (puede ser _ret_funcion:ambito)*/
                                                          String varRetorno = "_ret_" + ambito;

                                                          /* Generar terceto para asignar el valor de retorno*/
                                                          ArregloTercetos.crearTerceto(":=", varRetorno, val_peek(2).sval);

                                                          /* Generar terceto de salto al final de la función*/
                                                          ArregloTercetos.crearTerceto("JMP", "fin_" + ambito, null);

                                             }
break;
case 32:
//#line 121 "Gramatica.y"
{tipo = "ulong";}
break;
case 33:
//#line 125 "Gramatica.y"
{
                              yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval);
                              ControlAsigMultiple.pushTipoDer(yyval.tipo);
                              yyval.sval = val_peek(0).sval;
                            }
break;
case 34:
//#line 131 "Gramatica.y"
{
                              yyval.tipo = val_peek(2).tipo;
                              String t = obtenerTipoDeSimbolo(val_peek(0).sval);
                              ControlAsigMultiple.pushTipoDer(t);
                              yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;
                            }
break;
case 35:
//#line 138 "Gramatica.y"
{
                              yyerror("Error: se esperaba ',' entre constantes");
                              yyval = val_peek(1);
                            }
break;
case 36:
//#line 145 "Gramatica.y"
{
                              String clave = chequearAmbito("", ambito, val_peek(0).sval).sval;
                              yyval.sval = clave;
                              yyval.tipo = obtenerTipoDeSimbolo(clave);
                              ControlAsigMultiple.pushTipoIzq(yyval.tipo);
                            }
break;
case 37:
//#line 152 "Gramatica.y"
{
                              String clave = chequearAmbito("", ambito, val_peek(0).sval).sval;
                              yyval.tipo = val_peek(2).tipo;
                              ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(clave));
                              yyval.sval = val_peek(2).sval + "," + clave;
                            }
break;
case 38:
//#line 159 "Gramatica.y"
{
                              String clave = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval).sval;
                              yyval.sval = clave;
                              yyval.tipo = obtenerTipoDeSimbolo(clave);
                              ControlAsigMultiple.pushTipoIzq(yyval.tipo);
                            }
break;
case 39:
//#line 166 "Gramatica.y"
{
                              String clave = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval).sval;
                              yyval.tipo = val_peek(4).tipo;
                              ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(clave));
                              yyval.sval = val_peek(4).sval + "," + clave;
                            }
break;
case 40:
//#line 173 "Gramatica.y"
{
                              yyerror("Error: se esperaba ',' entre variables");
                              yyval = val_peek(1);
                            }
break;
case 41:
//#line 178 "Gramatica.y"
{
                              yyerror("Error: se esperaba ',' entre variables");
                              yyval = val_peek(3);
                            }
break;
case 42:
//#line 184 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 43:
//#line 185 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 44:
//#line 186 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 45:
//#line 191 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 46:
//#line 192 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 47:
//#line 193 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 48:
//#line 194 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 49:
//#line 195 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 50:
//#line 196 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 51:
//#line 197 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 52:
//#line 198 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 57:
//#line 203 "Gramatica.y"
{ reportarEstructura("WHILE"); yyval = ArregloTercetos.completarBackPatchingWHILE(); }
break;
case 58:
//#line 204 "Gramatica.y"
{ reportarEstructura("WHILE"); yyval = ArregloTercetos.completarBackPatchingWHILE(); }
break;
case 59:
//#line 205 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");   }
break;
case 60:
//#line 206 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 61:
//#line 207 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 62:
//#line 209 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 63:
//#line 210 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 64:
//#line 212 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 65:
//#line 213 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 66:
//#line 214 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 67:
//#line 215 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 68:
//#line 216 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 69:
//#line 217 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 70:
//#line 218 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 71:
//#line 219 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 72:
//#line 220 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 73:
//#line 221 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 74:
//#line 222 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 75:
//#line 223 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 76:
//#line 224 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 78:
//#line 228 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIFDesapilaryCompletar("bl", null,null);}
break;
case 79:
//#line 231 "Gramatica.y"
{ ArregloTercetos.apilarTercetoInicialWHILE(); }
break;
case 80:
//#line 237 "Gramatica.y"
{ArregloTercetos.completarTercetoBackPatchingIF();}
break;
case 81:
//#line 238 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 82:
//#line 241 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 83:
//#line 242 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 84:
//#line 243 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 85:
//#line 244 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 86:
//#line 247 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 87:
//#line 248 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 88:
//#line 249 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 89:
//#line 250 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 92:
//#line 258 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 93:
//#line 259 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 94:
//#line 260 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 97:
//#line 266 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 98:
//#line 269 "Gramatica.y"
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
case 99:
//#line 281 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 100:
//#line 282 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 101:
//#line 285 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("COMP", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo); }
break;
case 102:
//#line 288 "Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                               yyval = ArregloTercetos.crearTerceto(":=", val_peek(2).sval, val_peek(0).sval);
                                                               yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo);
                                                               }
break;
case 103:
//#line 294 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 104:
//#line 295 "Gramatica.y"
{yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(2).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 105:
//#line 299 "Gramatica.y"
{
                              reportarEstructura("asignacion multiple");
                              ControlAsigMultiple.compararTipos();
                              crearTercetosAsigMultiple(val_peek(2).sval, val_peek(0).sval);
                            }
break;
case 106:
//#line 308 "Gramatica.y"
{
                             reportarEstructura("expresión lambda");
                             /* Declarar lambda (opcional en TS si solo es "inline")*/
                             contadorLambda++;
                             String nombreLambda = "_lambda" + contadorLambda;
                             String claveLambda  = nombreLambda + ":" + ambito;

                             /* \[Opcional] dejar en TS para validaciones*/
                             ElementoTablaDeSimbolos elem = new ElementoTablaDeSimbolos();
                             elem.setTipo("expresion_lambda");
                             elem.setAmbito(ambito);
                             elem.setUso("Lambda");
                             /* TablaDeSimbolos.addSimbolo(claveLambda, elem); // <- opcional*/

                             /* Registrar parámetro formal en el mismo ámbito (cv)*/
                             ParserValExt paramFormal = registrarParametroFuncion(val_peek(1).sval, "cv");

                             /* Etiqueta ini de la lambda*/
                             ArregloTercetos.crearTerceto("ini_" + claveLambda, "_", "_");

                             /* Guardar:*/
                             /*  - sval: clave de la lambda (para CALL y etiquetas)*/
                             /*  - tipo: clave del parámetro formal (para asignar y poder obtener su tipo)*/
                             yyval.sval = claveLambda;
                             yyval.tipo = paramFormal.sval; /* p.ej. "A:PROGRAMA"*/
                           }
break;
case 107:
//#line 337 "Gramatica.y"
{
                              /* fin de la declaración*/
                              ArregloTercetos.crearTerceto("fin_" + val_peek(3).sval, "_", "_");
                            }
break;
case 108:
//#line 342 "Gramatica.y"
{
                              /* Chequeo de tipos: formal vs real*/
                              String tipoFormal = obtenerTipoDeSimbolo(val_peek(7).tipo); /* $1.tipo es la clave TS del parámetro formal*/
                              chequearTipos(tipoFormal, val_peek(1).tipo);

                              /* Pasaje copia-valor: asignar real -> formal*/
                              ArregloTercetos.crearTerceto(":=", val_peek(7).tipo, val_peek(1).sval);

                              /* Llamada a la lambda*/
                              ArregloTercetos.crearTerceto("CALL", val_peek(7).sval, null);
                            }
break;
case 109:
//#line 354 "Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 110:
//#line 356 "Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 111:
//#line 358 "Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 112:
//#line 361 "Gramatica.y"
{ yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 113:
//#line 362 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); }
break;
case 114:
//#line 363 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 115:
//#line 367 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("+", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo); }
break;
case 116:
//#line 368 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("-", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo);}
break;
case 117:
//#line 369 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 118:
//#line 370 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 119:
//#line 371 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 120:
//#line 372 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 121:
//#line 373 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 122:
//#line 374 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 123:
//#line 375 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 124:
//#line 381 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("*", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo); }
break;
case 125:
//#line 382 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("/", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo);}
break;
case 126:
//#line 383 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 127:
//#line 384 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 128:
//#line 385 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 129:
//#line 386 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 130:
//#line 387 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 131:
//#line 388 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 132:
//#line 389 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 133:
//#line 400 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 134:
//#line 401 "Gramatica.y"
{yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); yyval = val_peek(0); }
break;
case 135:
//#line 402 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 136:
//#line 403 "Gramatica.y"
{
                                           /* ⚡ Generar terceto CALL usando el ID que guardó inicio_llamado*/
                                                                  yyval = ArregloTercetos.crearTerceto("CALL", val_peek(2).sval, null);

                                                                  /* ⚡ Guardar el tipo de retorno de la función*/
                                                                  yyval.tipo = obtenerTipoDeSimbolo(val_peek(2).sval);

                                                                  /* ⚡ Crear una temporal para almacenar el valor de retorno*/
                                                                  String temp = "_t" + ArregloTercetos.declararTemporal(yyval.tipo,ambito); /* nombre temporal único*/
                                                                  declaracionDeVariable(temp, yyval.tipo, ambito, "temporal");

                                                                  /* ⚡ Asignar el valor retornado (_ret_<funcion>) a la temporal*/
                                                                  String retName = "_ret_" + obtenerAmbito(val_peek(2).sval);
                                                                  ArregloTercetos.crearTerceto(":=", temp, retName);

                                                                  /* ⚡ Guardar temporal como valor semántico del factor*/
                                                                  yyval.sval = temp;

                                                                  /* ⚡ Finalizar la gestión de pila de funciones*/
                                                                  PilaDeFuncionesLlamadas.finalizarLlamada();

                                                                  /* ⚡ Realizar pasajes de copia-resultado si hay CR*/
                                                                   realizarPasajesCopiaResultado();
                                         }
break;
case 137:
//#line 427 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval);}
break;
case 138:
//#line 428 "Gramatica.y"
{
                                                  /* Esta es la acción semántica correcta:*/
                                                  /* 1. Crea un terceto para la operación TRUNC.*/
                                                  /* 2. El resultado (temporal) es el sval de este factor.*/
                                                  /* 3. El tipo es ulong.*/
                                                  yyval = ArregloTercetos.crearTerceto("TRUNC", val_peek(1).sval, null);
                                                  yyval.tipo = "ulong";
                      }
break;
case 139:
//#line 436 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 140:
//#line 437 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 141:
//#line 438 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 142:
//#line 442 "Gramatica.y"
{
                             yyval = chequearAmbito("", ambito, val_peek(1).sval);
                             PilaDeFuncionesLlamadas.iniciarLlamada(ambito+":"+val_peek(1).sval);
                           }
break;
//#line 1819 "Parser.java"
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
