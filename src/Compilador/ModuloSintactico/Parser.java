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
public final static short COMP=271;
public final static short CADENA=272;
public final static short FLECHA=273;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    3,    4,    4,    2,
    2,    2,    2,    2,    2,    7,    5,    5,   10,   10,
   10,   10,   10,   10,   11,   12,    6,   14,   14,   14,
   15,   15,   15,   15,   15,   15,    9,    9,    9,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,   18,   23,   19,   19,   16,   16,   16,   16,
   24,   24,   24,   24,   17,   17,   17,   17,   17,   26,
   26,   26,   27,   27,   27,   25,   20,   28,   28,   21,
   29,   30,   22,   22,   22,   22,   31,   31,   31,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   32,   32,   32,   32,   32,   32,   32,   32,
   32,   33,   33,   33,   33,   33,   34,
};
final static short yylen[] = {                            2,
    4,    3,    3,    2,    0,    2,    7,    2,    2,    2,
    2,    1,    2,    2,    2,    2,    1,    3,    3,    2,
    3,    2,    3,    2,    1,    2,    1,    1,    3,    2,
    1,    3,    3,    5,    2,    4,    1,    3,    2,   10,
    6,    8,    5,    9,    9,    4,    4,    1,    1,    1,
    1,    6,    4,    3,    5,    3,    5,    3,    9,    9,
    8,    5,    6,    6,    4,    3,    8,    7,    6,    7,
    8,    6,    1,    1,    1,    0,    3,    2,    2,    1,
    3,    2,    2,    1,    2,    3,    2,    2,    3,    1,
    3,    1,    3,    3,    2,    3,    3,    1,    3,    3,
    4,    0,    8,    6,    6,    5,    1,    1,    2,    3,
    3,    1,    3,    3,    3,    3,    3,    3,    4,    4,
    4,    4,    3,    3,    3,    3,    3,    3,    3,    3,
    1,    1,    1,    3,    3,    2,    2,
};
final static short yydefred[] = {                         0,
    0,    5,    0,    5,    0,    0,    0,    0,    0,   74,
    0,    0,    0,    0,   27,    3,    6,   12,    0,    0,
    0,    0,   50,    0,   48,   49,   51,    0,    0,    0,
    2,    1,   15,    0,    0,    0,    0,  133,    0,    0,
    0,    0,    0,    0,  131,    0,    0,    0,    0,    0,
    9,    0,    0,   13,   10,   14,   11,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  136,    0,  137,    0,    0,    0,    0,    0,
    0,   73,   75,    0,    0,    0,   66,   78,    0,    0,
    0,    0,    0,   90,    0,   56,    0,    0,    0,   25,
    0,    0,   17,    0,   39,    0,    0,    0,   28,    0,
    0,    0,    0,   58,   82,    0,    0,   88,   85,   87,
    0,    0,    0,  101,    0,    0,    0,    0,  129,  125,
  130,  127,   77,  134,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   65,  126,  123,  128,
  124,   95,    0,  135,    0,   46,   47,   24,    0,    0,
   22,   20,    0,    0,   38,   36,    0,   30,    0,   81,
    0,   53,    0,    0,  102,    0,    0,   89,   86,  122,
  121,  120,  119,    0,   62,    0,    0,   43,    0,    0,
    0,   94,   93,   91,    5,   18,   23,   21,   19,   34,
   29,   55,    0,   57,    0,    0,  106,    0,    0,    0,
   69,    0,   41,    0,    0,   64,   72,    0,   63,    0,
   52,  104,    0,  105,    0,    0,    0,    0,    0,   68,
    0,    0,    0,   70,    7,    0,  108,  107,    0,   61,
    0,   67,    0,    0,    0,   71,    0,   42,  109,  103,
   59,   60,    0,   44,   45,   40,
};
final static short yydgoto[] = {                          3,
    5,   17,   18,   19,  101,   20,   21,   66,   53,  103,
  104,   23,   41,  110,   24,   42,   67,   86,   87,   25,
   26,   27,   28,   62,   43,   93,   94,   29,   30,  206,
  239,   44,   45,   46,
};
final static short yysindex[] = {                       -98,
 -117,    0,    0,    0,  173,  186,  222,  -30, -216,    0,
   71,   22,  120,   21,    0,    0,    0,    0,   31, -221,
  -52,  -50,    0,   -8,    0,    0,    0,   93, -187,  228,
    0,    0,    0, -178,  108, -168,  120,    0,   45,  -35,
  -23,  376,   72,   57,    0,  605,  162,   44, -151, -212,
    0,    0,  -44,    0,    0,    0,    0,   78, -134, -131,
  120,  366,   96,  120,  537,  -48,  256,  105,   29,  326,
  561,  610,    0,  107,    0,  -97,  120,  120,  613,  626,
  120,    0,    0,  527,  114,  385,    0,    0,  633,  637,
  108,  -29,   49,    0,  134,    0,   67,    0,  -89,    0,
   87, -218,    0, -230,    0,  -88,  -87,  136,    0,  -26,
  139,  539,  537,    0,    0,   44,  398,    0,    0,    0,
  588,  143,  -40,    0,   59,   57,   59,   57,    0,    0,
    0,    0,    0,    0,   32,   53,   59,   57,   59,   57,
   44,  -94,  407,  -94,  550,  127,    0,    0,    0,    0,
    0,    0, -214,    0,  120,    0,    0,    0,   65, -212,
    0,    0,  -77, -200,    0,    0,  -73,    0,  -70,    0,
  562,    0,  418,  588,    0,  111,  120,    0,    0,    0,
    0,    0,    0,  431,    0,  -94,  442,    0,  -64,  453,
  -64,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  462,    0,  116,  158,    0,  117,  571,  140,
    0,  471,    0,  573,  152,    0,    0,  -64,    0,  247,
    0,    0,    4,    0,  -64,  480,  -64,  601,  153,    0,
  -64,  489,  -64,    0,    0,  -63,    0,    0,  174,    0,
  -64,    0,  -64,  502,  -64,    0,  -64,    0,    0,    0,
    0,    0,  -64,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    1,    0,    0,    0,  214,    0,    0,    0,    0,    0,
    0,    0,    0,  -14,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  337,    0,
    0,  -38,  310,  -28,    0,    0,    0,  -36,    0,    0,
    0,  -16,  -32,    0,    0,    0,    0,    7,    0,    0,
    0,    0,  508,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  360,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -38,    0,    0,    0,    0,
   98,    0,    0,    0,    0,    0,    0,  -11,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   11,    0,   10,
  517,   19,    0,    0,    0,   33,    0,    0,    0,    0,
    0,    0,    0,    0,   20,   41,   62,   82,    0,    0,
    0,    0,    0,    0,    0,    0,  104,  131,  151,  206,
  275,  -38,    0,  -38,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -38,    0,  -38,  -38,    0,  -38,    0,
  -38,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -38,    0,    0,    0,    0,    0,  -38,    0,    0,
    0,    0,    0,    0,  -38,    0,  -38,    0,    0,    0,
  -38,    0,  -38,    0,    0,    0,    0,    0,    0,    0,
  -38,    0,  -38,    0,  -38,    0,  -38,    0,    0,    0,
    0,    0,  -38,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    6,    0,    0,    0,    0,  741,    0,   -3,    0,  101,
    0,    0,  867,    0,    0,    0,  820, -110,  752,    0,
    0,    0,    0,    0,    9,    0,   64,    0,    0,    0,
    0,   50,   46,    0,
};
final static int YYTABLESIZE=1048;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        106,
    5,   22,   22,   22,   78,    4,   55,    6,   57,    7,
  119,  112,  112,   79,  112,   80,  112,  169,  179,   79,
   76,   80,   26,    8,    2,  163,   16,   37,   33,   31,
  112,  184,   33,  187,   51,   59,   63,  161,   85,   15,
    5,  192,   37,   99,   52,   74,   31,  162,  236,   33,
   35,  193,   60,   15,   32,  198,  100,   15,  114,  117,
  117,   47,  117,  123,  117,  199,   49,   35,  100,  111,
   50,   32,  181,   36,   79,  212,   80,   54,  117,   64,
  113,  113,  146,  113,   75,  113,   79,   68,   80,  154,
   76,   97,  155,  183,  112,   79,   73,   80,   89,  113,
   71,  118,  118,   90,  118,   72,  118,  157,  172,   79,
   37,   80,   88,  123,   98,   36,  130,  132,  126,  128,
  118,  115,  115,  107,  115,    5,  115,  159,  138,  140,
  160,  108,   61,  109,  149,  151,  115,   36,   92,  123,
  115,   92,  117,  114,  114,  124,  114,  133,  114,   71,
   69,  207,   70,   79,   72,   80,  222,  224,   79,   79,
   80,   80,  114,  113,   36,   82,   83,    1,  134,  123,
  110,  110,  144,  110,  156,  110,  158,  165,  166,  170,
  210,  167,  177,  215,  118,  191,  123,  195,  197,  110,
  116,  116,  200,  116,  201,  116,   83,  223,  227,  123,
  220,  249,   96,   54,  115,   56,   36,  118,  229,  116,
  233,  245,    9,    4,  250,  178,   22,   76,  194,   26,
   77,  105,  123,   16,    0,    9,  114,  112,  123,  112,
  112,  112,  112,  112,  112,  112,  152,  112,  168,   37,
  123,    0,  112,  153,  112,  111,  111,   81,  111,   37,
  111,   31,   98,  110,   33,   99,    5,   58,    5,    5,
  196,    9,    5,    5,  111,  100,    5,    9,  237,  238,
    5,    0,   35,  116,   54,  117,   32,  117,  117,  117,
  117,  117,  117,  117,  125,  117,    9,  180,   97,    0,
  117,    0,  117,   38,   39,  121,  113,   16,  113,  113,
  113,  113,  113,  113,  113,    0,  113,    0,  182,    0,
   31,  113,    0,  113,   96,   96,    0,  118,    0,  118,
  118,  118,  118,  118,  118,  118,   35,  118,  111,    0,
    0,    0,  118,   96,  118,   38,   39,  115,   40,  115,
  115,  115,  115,  115,  115,  115,   32,  115,   35,   80,
   65,    0,  115,    0,  115,    0,    0,   38,   39,  114,
   40,  114,  114,  114,  114,  114,  114,  114,   80,  114,
   36,  235,    0,    0,  114,   35,  114,  132,  132,  132,
  122,  132,    0,  132,   38,   39,  110,   40,  110,  110,
  110,  110,  110,  110,  110,  132,  110,   96,    0,   79,
    0,  110,    0,  110,    0,    9,  116,    0,  116,  116,
  116,  116,  116,  116,  116,    9,  116,   35,   79,    0,
    0,  116,    0,  116,    9,    0,   38,   39,    8,   40,
   10,   11,   80,   95,   12,   13,    0,  174,   14,    0,
    0,    8,   15,   10,   11,    0,    9,   12,   13,    0,
    0,   14,    0,    0,    0,   15,    0,    9,    0,  132,
    0,  111,    0,  111,  111,  111,  111,  111,  111,  111,
    9,  111,    0,    0,    0,    0,  111,    8,  111,   10,
   11,    9,   79,   12,   13,   10,   11,   14,  113,   12,
   13,   15,    9,   14,    0,    0,    0,    0,   84,    0,
    0,    9,    8,    0,   10,   11,    0,  145,   12,   13,
    9,  120,   14,   10,   11,    0,   15,   12,   13,    9,
    0,   14,  175,    0,    0,    0,    0,    0,    9,    0,
   96,  186,   96,   96,   96,   96,   96,   96,   96,    0,
   96,    9,  204,    0,    0,    0,    0,   84,    0,    0,
    0,    0,    0,  209,    0,    0,   83,    0,    0,    0,
    0,    0,    0,    0,  214,   80,    9,   80,   80,   80,
   80,   80,   80,    0,    0,   80,    9,  218,    9,    0,
    0,  127,    0,    0,    0,    0,  221,    0,    0,    9,
   38,   39,  132,  228,  132,  132,  132,  132,  132,  132,
  132,    9,  132,    0,  241,   36,    0,  132,    0,  132,
    9,    0,    9,  247,    0,   79,    0,   79,   79,   79,
   79,   79,   79,   10,   11,   79,  253,   12,   13,  112,
   84,   14,   36,   10,   11,   82,   83,   12,   13,   83,
    9,   14,   10,   11,    0,   83,   12,   13,    0,   36,
   14,  142,    0,  120,   36,   10,   11,   36,    0,   12,
   13,  171,  120,   14,   10,   11,    0,    0,   12,   13,
   36,    0,   14,  120,  189,   10,   11,   36,    0,   12,
   13,   36,    0,   14,    0,    0,  202,    0,   10,   11,
    0,   83,   12,   13,    0,  225,   14,  231,    0,   10,
   11,    0,   83,   12,   13,    0,    0,   14,  120,    0,
   10,   11,    0,    0,   12,   13,    0,  120,   14,   10,
   11,    0,    0,   12,   13,  243,    0,   14,   10,   11,
    0,   83,   12,   13,    0,  120,   14,   10,   11,    0,
    0,   12,   13,    0,  120,   14,   10,   11,    0,   34,
   12,   13,    0,    0,   14,    0,    0,  120,    0,   10,
   11,    0,    0,   12,   13,   84,   84,   14,    0,   84,
   84,   84,    0,   84,   83,   83,    0,    0,   83,   83,
   83,    0,   83,    0,   10,   11,    0,    0,   12,   13,
  102,    0,   14,    0,   10,   11,   10,   11,   12,   13,
   12,   13,   14,    0,   14,    0,    0,   10,   11,    0,
    0,   12,   13,    0,    0,   14,  129,    0,    0,   10,
   11,    0,    0,   12,   13,   38,   39,   14,   10,   11,
   10,   11,   12,   13,   12,   13,   14,  147,   14,    0,
    0,    0,    0,   35,  164,    0,    0,    0,    0,    0,
    0,    0,   38,   39,    0,   40,    0,   15,   10,   11,
   91,   34,   12,   13,    0,  131,   14,    0,  137,   38,
   39,    0,   40,    0,   38,   39,    0,   38,   39,   48,
    0,  139,    0,    0,  117,    0,    0,    0,  148,    0,
   38,   39,  150,  185,    0,  188,    0,   38,   39,    0,
  102,   38,   39,  143,    0,    0,    0,    0,    0,    0,
    0,    0,   92,   97,   34,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  116,    0,  173,    0,    0,  211,    0,  213,  216,    0,
  217,    0,  219,  135,  136,    0,    0,  141,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  230,  190,    0,    0,    0,    0,  234,
    0,    0,    0,    0,    0,    0,  240,    0,  242,    0,
    0,    0,  246,    0,  248,    0,    0,  176,    0,    0,
  203,    0,  251,    0,  252,    0,  254,    0,  255,    0,
    0,    0,    0,    0,  256,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   92,    0,    0,    0,    0,    0,    0,  226,    0,
    0,    0,    0,  232,    0,    0,    0,    0,    0,    0,
  205,    0,    0,  208,    0,    0,    0,  244,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,    5,    6,    7,   40,  123,   59,    2,   59,    4,
   59,   40,   41,   43,   43,   45,   45,   44,   59,   43,
   59,   45,   59,   40,  123,  256,   59,   44,   59,   44,
   59,  142,   44,  144,  256,   44,   28,  256,   42,  270,
   40,  256,   59,  256,  266,   37,   61,  266,   45,   61,
   44,  266,   61,  270,   44,  256,  269,  270,   62,   40,
   41,   40,   43,   67,   45,  266,   46,   61,   59,   61,
   40,   61,   41,   45,   43,  186,   45,   59,   59,  267,
   40,   41,   86,   43,   40,   45,   43,  266,   45,   41,
   46,   59,   44,   41,  123,   43,  265,   45,   42,   59,
   42,   40,   41,   47,   43,   47,   45,   41,  112,   43,
   40,   45,   41,  117,  266,   45,   71,   72,   69,   70,
   59,   40,   41,   46,   43,  125,   45,   41,   79,   80,
   44,  266,   40,  265,   89,   90,   41,   45,   41,  143,
   59,   44,  123,   40,   41,   41,   43,   41,   45,   42,
   43,   41,   45,   43,   47,   45,   41,   41,   43,   43,
   45,   45,   59,  123,   45,  260,  261,  266,  266,  173,
   40,   41,   59,   43,   41,   45,  266,  266,  266,   41,
  184,   46,   40,  187,  123,   59,  190,  123,  266,   59,
   40,   41,  266,   43,  265,   45,  261,   40,   59,  203,
  195,  265,   41,  256,  123,  256,   45,  256,  212,   59,
   59,   59,   40,    0,   41,  256,  220,  256,  155,  256,
  256,  266,  226,  256,   -1,   40,  123,  256,  232,  258,
  259,  260,  261,  262,  263,  264,  266,  266,  265,  256,
  244,   -1,  271,  273,  273,   40,   41,  271,   43,  266,
   45,  266,  267,  123,  266,  267,  256,  266,  258,  259,
  160,   40,  262,  263,   59,  256,  266,   40,  265,  266,
  270,   -1,  266,  123,  256,  256,  266,  258,  259,  260,
  261,  262,  263,  264,  256,  266,   40,  256,  256,   -1,
  271,   -1,  273,  265,  266,   40,  256,  125,  258,  259,
  260,  261,  262,  263,  264,   -1,  266,   -1,  256,   -1,
  125,  271,   -1,  273,   40,   41,   -1,  256,   -1,  258,
  259,  260,  261,  262,  263,  264,  256,  266,  123,   -1,
   -1,   -1,  271,   59,  273,  265,  266,  256,  268,  258,
  259,  260,  261,  262,  263,  264,  125,  266,  256,   40,
  123,   -1,  271,   -1,  273,   -1,   -1,  265,  266,  256,
  268,  258,  259,  260,  261,  262,  263,  264,   59,  266,
   45,  125,   -1,   -1,  271,  256,  273,   41,   42,   43,
  125,   45,   -1,   47,  265,  266,  256,  268,  258,  259,
  260,  261,  262,  263,  264,   59,  266,  123,   -1,   40,
   -1,  271,   -1,  273,   -1,   40,  256,   -1,  258,  259,
  260,  261,  262,  263,  264,   40,  266,  256,   59,   -1,
   -1,  271,   -1,  273,   40,   -1,  265,  266,  256,  268,
  258,  259,  123,  272,  262,  263,   -1,   40,  266,   -1,
   -1,  256,  270,  258,  259,   -1,   40,  262,  263,   -1,
   -1,  266,   -1,   -1,   -1,  270,   -1,   40,   -1,  123,
   -1,  256,   -1,  258,  259,  260,  261,  262,  263,  264,
   40,  266,   -1,   -1,   -1,   -1,  271,  256,  273,  258,
  259,   40,  123,  262,  263,  258,  259,  266,  123,  262,
  263,  270,   40,  266,   -1,   -1,   -1,   -1,  123,   -1,
   -1,   40,  256,   -1,  258,  259,   -1,  123,  262,  263,
   40,  256,  266,  258,  259,   -1,  270,  262,  263,   40,
   -1,  266,  125,   -1,   -1,   -1,   -1,   -1,   40,   -1,
  256,  125,  258,  259,  260,  261,  262,  263,  264,   -1,
  266,   40,  125,   -1,   -1,   -1,   -1,   40,   -1,   -1,
   -1,   -1,   -1,  123,   -1,   -1,   40,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  123,  256,   40,  258,  259,  260,
  261,  262,  263,   -1,   -1,  266,   40,  125,   40,   -1,
   -1,  256,   -1,   -1,   -1,   -1,  125,   -1,   -1,   40,
  265,  266,  256,  123,  258,  259,  260,  261,  262,  263,
  264,   40,  266,   -1,  125,   45,   -1,  271,   -1,  273,
   40,   -1,   40,  125,   -1,  256,   -1,  258,  259,  260,
  261,  262,  263,  258,  259,  266,  125,  262,  263,  264,
  123,  266,   45,  258,  259,  260,  261,  262,  263,  123,
   40,  266,  258,  259,   -1,  261,  262,  263,   -1,   45,
  266,  125,   -1,  256,   45,  258,  259,   45,   -1,  262,
  263,  123,  256,  266,  258,  259,   -1,   -1,  262,  263,
   45,   -1,  266,  256,  125,  258,  259,   45,   -1,  262,
  263,   45,   -1,  266,   -1,   -1,  125,   -1,  258,  259,
   -1,  261,  262,  263,   -1,  125,  266,  125,   -1,  258,
  259,   -1,  261,  262,  263,   -1,   -1,  266,  256,   -1,
  258,  259,   -1,   -1,  262,  263,   -1,  256,  266,  258,
  259,   -1,   -1,  262,  263,  125,   -1,  266,  258,  259,
   -1,  261,  262,  263,   -1,  256,  266,  258,  259,   -1,
   -1,  262,  263,   -1,  256,  266,  258,  259,   -1,    9,
  262,  263,   -1,   -1,  266,   -1,   -1,  256,   -1,  258,
  259,   -1,   -1,  262,  263,  258,  259,  266,   -1,  262,
  263,  264,   -1,  266,  258,  259,   -1,   -1,  262,  263,
  264,   -1,  266,   -1,  258,  259,   -1,   -1,  262,  263,
   50,   -1,  266,   -1,  258,  259,  258,  259,  262,  263,
  262,  263,  266,   -1,  266,   -1,   -1,  258,  259,   -1,
   -1,  262,  263,   -1,   -1,  266,  256,   -1,   -1,  258,
  259,   -1,   -1,  262,  263,  265,  266,  266,  258,  259,
  258,  259,  262,  263,  262,  263,  266,   86,  266,   -1,
   -1,   -1,   -1,  256,  104,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  265,  266,   -1,  268,   -1,  270,  258,  259,
  256,  121,  262,  263,   -1,  256,  266,   -1,  256,  265,
  266,   -1,  268,   -1,  265,  266,   -1,  265,  266,   13,
   -1,  256,   -1,   -1,   65,   -1,   -1,   -1,  256,   -1,
  265,  266,  256,  142,   -1,  144,   -1,  265,  266,   -1,
  160,  265,  266,   84,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   46,   47,  174,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   64,   -1,  113,   -1,   -1,  184,   -1,  186,  187,   -1,
  189,   -1,  191,   77,   78,   -1,   -1,   81,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  212,  145,   -1,   -1,   -1,   -1,  218,
   -1,   -1,   -1,   -1,   -1,   -1,  225,   -1,  227,   -1,
   -1,   -1,  231,   -1,  233,   -1,   -1,  121,   -1,   -1,
  171,   -1,  241,   -1,  243,   -1,  245,   -1,  247,   -1,
   -1,   -1,   -1,   -1,  253,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  155,   -1,   -1,   -1,   -1,   -1,   -1,  209,   -1,
   -1,   -1,   -1,  214,   -1,   -1,   -1,   -1,   -1,   -1,
  174,   -1,   -1,  177,   -1,   -1,   -1,  228,
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
"expresion : TRUNC '(' expresion ')'",
"expresion : TRUNC '(' expresion error",
"expresion : TRUNC error expresion ')'",
"expresion : TRUNC error expresion error",
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
"inicio_llamado : ID '('",
};

//#line 413 "Gramatica.y"
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
        if (erroresEmitidos.add(clave)) { //Si todavía no se mostró el error en esa límea, lo mostramos
           System.err.println("Error de sintaxis en línea " + linea + ": " + s);
        }
        //Caso contrario, no hacemos nada
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
            yyerror("Error: Sentencia return declarada en lugar inválido");
        }
}

//Función para el chequeo de tipos de variables y constantes
public String chequearTipos(String tipo1, String tipo2){
    if (!tipo1.equals(tipo2)){
        yyerror("Error: Tipos incompatibles (" + tipo1 + " y " + tipo2 +").");
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


//#line 978 "Parser.java"
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
{ }
break;
case 2:
//#line 30 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre al programa"); }
break;
case 3:
//#line 31 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '{' al inicio"); }
break;
case 4:
//#line 32 "Gramatica.y"
{ yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
break;
case 7:
//#line 39 "Gramatica.y"
{ reportarEstructura("declaracion de funcion");
                                                                                            /* Etiqueta de fin de función*/
                                                                                            String etiquetaFin = "fin_" + ambito; /* o derivar el nombre de la función de otra forma*/
                                                                                            ArregloTercetos.crearTerceto(etiquetaFin, "_", "_");
                                                                                            salirAmbito();
                                                                                            chequearReturn();

                                                                                          }
break;
case 8:
//#line 49 "Gramatica.y"
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
//#line 58 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre a la función"); }
break;
case 13:
//#line 65 "Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 14:
//#line 66 "Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 15:
//#line 67 "Gramatica.y"
{yyerror("Error: Sentencia invalida");}
break;
case 16:
//#line 70 "Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 19:
//#line 77 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cr");}
break;
case 20:
//#line 78 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,"cv");}
break;
case 21:
//#line 79 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 22:
//#line 80 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 23:
//#line 81 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 24:
//#line 82 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 26:
//#line 88 "Gramatica.y"
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
//#line 102 "Gramatica.y"
{tipo = "ulong";}
break;
case 28:
//#line 106 "Gramatica.y"
{
                              yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval);
                              ControlAsigMultiple.pushTipoDer(yyval.tipo);
                              yyval.sval = val_peek(0).sval;
                            }
break;
case 29:
//#line 112 "Gramatica.y"
{
                              yyval.tipo = val_peek(2).tipo;
                              String t = obtenerTipoDeSimbolo(val_peek(0).sval);
                              ControlAsigMultiple.pushTipoDer(t);
                              yyval.sval = val_peek(2).sval + "," + val_peek(0).sval;
                            }
break;
case 30:
//#line 119 "Gramatica.y"
{
                              yyerror("Error: se esperaba ',' entre constantes");
                              yyval = val_peek(1);
                            }
break;
case 31:
//#line 126 "Gramatica.y"
{
                              String clave = chequearAmbito("", ambito, val_peek(0).sval).sval;
                              yyval.sval = clave;
                              yyval.tipo = obtenerTipoDeSimbolo(clave);
                              ControlAsigMultiple.pushTipoIzq(yyval.tipo);
                            }
break;
case 32:
//#line 133 "Gramatica.y"
{
                              String clave = chequearAmbito("", ambito, val_peek(0).sval).sval;
                              yyval.tipo = val_peek(2).tipo;
                              ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(clave));
                              yyval.sval = val_peek(2).sval + "," + clave;
                            }
break;
case 33:
//#line 140 "Gramatica.y"
{
                              String clave = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval).sval;
                              yyval.sval = clave;
                              yyval.tipo = obtenerTipoDeSimbolo(clave);
                              ControlAsigMultiple.pushTipoIzq(yyval.tipo);
                            }
break;
case 34:
//#line 147 "Gramatica.y"
{
                              String clave = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval).sval;
                              yyval.tipo = val_peek(4).tipo;
                              ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(clave));
                              yyval.sval = val_peek(4).sval + "," + clave;
                            }
break;
case 35:
//#line 154 "Gramatica.y"
{
                              yyerror("Error: se esperaba ',' entre variables");
                              yyval = val_peek(1);
                            }
break;
case 36:
//#line 159 "Gramatica.y"
{
                              yyerror("Error: se esperaba ',' entre variables");
                              yyval = val_peek(3);
                            }
break;
case 37:
//#line 165 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 38:
//#line 166 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 39:
//#line 167 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 40:
//#line 172 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 41:
//#line 173 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 42:
//#line 174 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 43:
//#line 175 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 44:
//#line 176 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 45:
//#line 177 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 46:
//#line 178 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 47:
//#line 179 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 52:
//#line 184 "Gramatica.y"
{ reportarEstructura("WHILE"); yyval = ArregloTercetos.completarBackPatchingWHILE(); }
break;
case 53:
//#line 185 "Gramatica.y"
{ reportarEstructura("WHILE"); yyval = ArregloTercetos.completarBackPatchingWHILE(); }
break;
case 54:
//#line 186 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");   }
break;
case 55:
//#line 187 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 56:
//#line 188 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 57:
//#line 190 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 58:
//#line 191 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 59:
//#line 193 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 60:
//#line 194 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 61:
//#line 195 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 62:
//#line 196 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 63:
//#line 197 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 64:
//#line 198 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 65:
//#line 199 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 66:
//#line 200 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 67:
//#line 201 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 68:
//#line 202 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 69:
//#line 203 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 70:
//#line 204 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 71:
//#line 205 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 73:
//#line 209 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIFDesapilaryCompletar("bl", null,null);}
break;
case 74:
//#line 212 "Gramatica.y"
{ ArregloTercetos.apilarTercetoInicialWHILE(); }
break;
case 75:
//#line 218 "Gramatica.y"
{ArregloTercetos.completarTercetoBackPatchingIF();}
break;
case 76:
//#line 219 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 77:
//#line 222 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 78:
//#line 223 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 79:
//#line 224 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 80:
//#line 225 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 81:
//#line 228 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 82:
//#line 229 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 83:
//#line 230 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 84:
//#line 231 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 87:
//#line 239 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 88:
//#line 240 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 89:
//#line 241 "Gramatica.y"
{ yyerror("Sentencia inválida en bloque ejecutable"); }
break;
case 92:
//#line 247 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 93:
//#line 250 "Gramatica.y"
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
case 94:
//#line 262 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 95:
//#line 263 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 96:
//#line 266 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("COMP", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo); }
break;
case 97:
//#line 269 "Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                               yyval = ArregloTercetos.crearTerceto(":=", val_peek(2).sval, val_peek(0).sval);
                                                               yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo);
                                                               }
break;
case 98:
//#line 275 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 99:
//#line 276 "Gramatica.y"
{yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(2).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 100:
//#line 280 "Gramatica.y"
{
                              reportarEstructura("asignacion multiple");
                              ControlAsigMultiple.compararTipos();
                              crearTercetosAsigMultiple(val_peek(2).sval, val_peek(0).sval);
                            }
break;
case 101:
//#line 289 "Gramatica.y"
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
case 102:
//#line 318 "Gramatica.y"
{
                              /* fin de la declaración*/
                              ArregloTercetos.crearTerceto("fin_" + val_peek(3).sval, "_", "_");
                            }
break;
case 103:
//#line 323 "Gramatica.y"
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
case 104:
//#line 335 "Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 105:
//#line 337 "Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 106:
//#line 339 "Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 107:
//#line 342 "Gramatica.y"
{ yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 108:
//#line 343 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); }
break;
case 109:
//#line 344 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 110:
//#line 348 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("+", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo); }
break;
case 111:
//#line 349 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("-", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo);}
break;
case 112:
//#line 350 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 113:
//#line 351 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 114:
//#line 352 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 115:
//#line 353 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 116:
//#line 354 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 117:
//#line 355 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 118:
//#line 356 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 119:
//#line 357 "Gramatica.y"
{yyval.tipo = "ulong";}
break;
case 120:
//#line 358 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 121:
//#line 359 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 122:
//#line 360 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 123:
//#line 366 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("*", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo); }
break;
case 124:
//#line 367 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("/", val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo);}
break;
case 125:
//#line 368 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 126:
//#line 369 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 127:
//#line 370 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 128:
//#line 371 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 129:
//#line 372 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 130:
//#line 373 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 131:
//#line 374 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 132:
//#line 377 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 133:
//#line 378 "Gramatica.y"
{yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); yyval = val_peek(0); }
break;
case 134:
//#line 379 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 135:
//#line 380 "Gramatica.y"
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
case 136:
//#line 404 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval);}
break;
case 137:
//#line 407 "Gramatica.y"
{
                             yyval = chequearAmbito("", ambito, val_peek(1).sval);
                             PilaDeFuncionesLlamadas.iniciarLlamada(ambito+":"+val_peek(1).sval);
                           }
break;
//#line 1753 "Parser.java"
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
