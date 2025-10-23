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
import java.util.Stack;
//#line 26 "Parser.java"




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
public final static short FLECHA=272;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    3,    4,    4,    2,
    2,    2,    2,    2,    2,    7,    5,    5,   10,   10,
   10,   10,   10,   10,   11,   12,   12,    6,   14,   14,
   14,   15,   15,   15,   15,   15,   15,    9,    9,    9,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,   18,   18,   16,   16,   16,   16,   17,   17,
   17,   17,   17,   22,   22,   22,   23,   23,   23,   21,
   19,   24,   24,   20,   25,   25,   25,   25,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   27,   27,   27,   27,   27,   27,   27,   27,
   27,   26,   26,   26,   26,   26,
};
final static short yylen[] = {                            2,
    4,    3,    3,    2,    0,    2,    7,    2,    2,    2,
    2,    1,    2,    2,    2,    2,    1,    3,    3,    2,
    3,    2,    3,    2,    1,    3,    3,    1,    1,    3,
    2,    1,    3,    3,    5,    2,    4,    1,    3,    2,
   10,    6,    8,    5,    9,    9,    4,    4,    1,    1,
    1,    6,    4,    3,    5,    3,    5,    3,    9,    9,
    8,    5,    6,    6,    4,    3,    8,    7,    6,    7,
    8,    6,    1,    0,    3,    2,    2,    1,    2,    3,
    2,    2,    3,    1,    3,    1,    3,    3,    2,    3,
    3,    1,    3,    3,   10,    9,    9,    8,    3,    3,
    1,    3,    3,    3,    3,    3,    3,    4,    4,    4,
    4,    1,    3,    3,    3,    3,    3,    3,    3,    3,
    1,    1,    1,    3,    4,    2,
};
final static short yydefred[] = {                         0,
    0,    5,    0,    5,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   28,    3,    6,   12,    0,    0,    0,
    0,   51,    0,   49,   50,    0,    2,    1,   15,    0,
    0,  123,    0,    0,    0,    0,    0,    0,  112,  121,
    0,    0,    0,    0,    0,    0,    0,    9,    0,    0,
   13,   10,   14,   11,    0,    0,    0,    0,    0,    0,
    0,    0,  126,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   58,   76,    0,    0,    0,   73,
    0,    0,   66,    0,   56,    0,   27,   26,    0,    0,
   25,    0,    0,   17,    0,   40,    0,    0,    0,   29,
    0,    0,    0,    0,    0,    0,  119,  115,  120,  117,
    0,    0,    0,   84,  124,    0,    0,    0,   75,    0,
    0,    0,    0,    0,    0,   53,    0,    0,  116,  113,
  118,  114,    0,    0,   65,    0,    0,    0,   47,   48,
   24,    0,    0,   22,   20,    0,    0,   39,   37,    0,
   31,    0,   89,    0,  125,    0,  111,  110,  109,  108,
    0,   55,    0,   82,   79,   81,   57,    0,    0,    0,
    0,    0,   62,    0,    0,   44,    5,   18,   23,   21,
   19,   35,   30,   88,   87,   85,    0,    0,   52,   83,
   80,   72,    0,   63,    0,    0,   69,    0,   42,    0,
    0,   64,    0,    0,    0,    0,   70,    0,    0,    0,
    0,    0,   68,    0,    0,    0,    7,    0,    0,    0,
    0,   61,    0,   67,    0,    0,    0,   71,    0,   43,
    0,    0,    0,   98,   59,   60,    0,   45,   46,    0,
   97,   96,   41,   95,
};
final static short yydgoto[] = {                          3,
    5,   16,   17,   18,   92,   19,   20,  127,   50,   94,
   95,   22,   36,  101,   23,   37,  128,   83,   24,   25,
   38,  113,  114,   26,   39,   40,   41,
};
final static short yysindex[] = {                      -109,
 -108,    0,    0,    0,  -82,  268,  320,  -34,  167,  167,
   -8,  171,    5,    0,    0,    0,    0,   -4, -212,  -59,
  -46,    0,  -16,    0,    0, -219,    0,    0,    0,   97,
 -188,    0,   19,  -29,  161,  -38,  439,   41,    0,    0,
   56,  425,  151, -148,   40, -155, -214,    0,    0,   -5,
    0,    0,    0,    0,   95, -122, -117,  171,  197,  224,
  236,  238,    0,  185, -114,  171,  171, -105,  118,  241,
  253,  171,  497, -143,    0,    0,  255,  258,  456,    0,
  462,  102,    0,  121,    0,   87,    0,    0,    0,  -99,
    0,   76, -195,    0, -229,    0,  -97,  -96,  125,    0,
  -35,   50,   58,   56,   58,   56,    0,    0,    0,    0,
   97,  -21,   92,    0,    0,   23,   26,  132,    0,   58,
   56,   58,   56,   50,  551,    0,  -43,  -63,    0,    0,
    0,    0,  569,  119,    0, -125,  509, -106,    0,    0,
    0,   54, -214,    0,    0,  -77, -177,    0,    0,  -75,
    0,  -61,    0, -159,    0,  171,    0,    0,    0,    0,
  499,    0,  519,    0,    0,    0,    0,  -42,  -56,  529,
  -56,  468,    0, -102,  478,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -143,  207,    0,    0,
    0,    0,  -56,    0,  578,  146,    0,  488,    0,  580,
  149,    0,  341,  225,  221,   51,    0,  -56,  539,  -56,
  590,  259,    0,  -56,  549,  -56,    0,  277,   51,   51,
  279,    0,  -56,    0,  -56,  563,  -56,    0,  -56,    0,
   51,  289,  292,    0,    0,    0,  -56,    0,    0,  293,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    1,    0,    0,    0,  335,    0,    0,    0,    0,    0,
    0,    0,  -13,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -39,    0,    0,    0,    0,  282,    0,    0,
  -22,  -41,    0,    0,    0,    0,    0,    0,  -10,   28,
    0,    0,    0,    0,  -11,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  310,    0,
    0,    0,   43,    0,    0,    0,    0,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    2,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   -9,    0,
   45,   57,   15,   32,   49,   66,    0,    0,    0,    0,
  108,    0,    0,    0,    0,    0,    0,    0,    0,   86,
  105,  122,  141,  296,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -41,    0,  -41,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
  -41,  -41,    0,  -41,  -41,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -41,    0,    0,    0,    0,  -41,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -41,    0,  -41,
    0,    0,    0,  -41,    0,  -41,    0,    0,    0,    0,
    0,    0,  -41,    0,  -41,    0,  -41,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    8,    0,    0,    0,    0,   -6,    0,  437,    0,  196,
    0,    0,   14,    0,    0,  330,  721,  452,    0,    0,
  317,    0,  198,    0,    0,  398,   53,
};
final static int YYTABLESIZE=932;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         52,
    5,  122,  122,  122,   70,  122,   71,  122,  152,    6,
   67,    7,   54,    2,    4,  165,  191,   74,  101,  122,
  101,   70,  101,   71,   29,   45,  146,   56,   68,    8,
   32,   43,   36,   38,   33,   47,  101,   68,   97,   14,
   93,   90,   15,   48,   57,   34,   58,   32,   38,   36,
   46,   33,   49,   91,   14,  106,   86,  106,   64,  106,
  144,  167,   34,  158,   65,   70,  160,   71,   70,  145,
   71,  102,  102,  106,  102,   63,  102,  112,  180,  116,
  117,   76,   70,  122,   71,  124,   16,  181,  147,  107,
  102,  107,   70,  107,   71,   31,  184,   77,   88,   61,
  101,   54,   78,   94,   62,  185,  104,  107,  104,   89,
  104,  104,  106,    9,   10,   91,  142,   11,   12,  143,
   14,   13,  121,  123,  104,    5,  103,  140,  103,   70,
  103,   71,  155,  172,   80,  156,   93,  106,   61,   59,
   98,   60,   99,   62,  103,   99,  100,   99,   86,   99,
  115,   86,  175,   80,  102,    1,  198,   80,  119,  118,
  138,  139,  105,   99,  105,  141,  105,  148,  149,  112,
  150,  107,  161,    8,    9,   10,  177,  171,   11,   12,
  105,  100,   13,  100,  203,  100,   14,  179,  104,  182,
   44,   85,  166,    9,   10,   31,   51,   11,   12,  100,
   44,   13,  183,   80,  210,   31,   35,  216,  103,   53,
   44,   31,  164,  190,   74,   31,  122,  122,  122,  122,
  122,  122,  122,  122,   44,  122,   66,   99,  151,   31,
  122,   72,  122,  101,  101,  101,  101,  101,  101,  101,
  101,   31,  101,  153,  105,   38,  206,  101,   55,  101,
  154,   32,   92,   36,   38,   33,    5,    5,    5,   96,
  220,    5,    5,  100,  219,    5,   34,   93,   31,    5,
  106,  106,  106,  106,  106,  106,  106,  106,  157,  106,
   31,  159,   31,   16,  106,   31,  106,  102,  102,  102,
  102,  102,  102,  102,  102,   87,  102,   31,   54,   31,
   94,  102,   31,  102,  107,  107,  107,  107,  107,  107,
  107,  107,   91,  107,   32,   33,  231,  227,  107,  234,
  107,  104,  104,  104,  104,  104,  104,  104,  104,  241,
  104,  205,  242,  244,    4,  104,   90,  104,  178,   42,
   78,  103,  103,  103,  103,  103,  103,  103,  103,  218,
  103,   69,    0,  186,   90,  103,    0,  103,    0,    0,
   99,   99,   99,   99,   99,   99,   99,   99,   77,   99,
    0,    0,    0,    0,   99,    0,   99,  105,  105,  105,
  105,  105,  105,  105,  105,    0,  105,    0,    0,    0,
    0,  105,   27,  105,    0,    0,  100,  100,  100,  100,
  100,  100,  100,  100,   78,  100,   30,    0,    0,    0,
  100,    0,  100,    0,   32,   33,   30,   34,   90,    0,
    0,   84,   30,    0,   32,   33,   30,   34,    0,   14,
   32,   33,   77,   34,   32,   33,    0,   34,    0,    0,
  111,   21,   21,   21,   28,    0,    0,    0,   32,   33,
    0,   34,  103,    0,    0,    0,    0,    0,  108,  110,
   32,   33,  166,    9,   10,  217,    0,   11,   12,    0,
    0,   13,    0,   75,  130,  132,    0,    0,   82,  105,
  166,    9,   10,    0,    0,   11,   12,   32,   33,   13,
    0,  107,    0,  109,    0,    0,  120,    0,    0,   32,
   33,   32,   33,    0,   32,   33,    0,    0,  122,  126,
  129,    0,    0,  131,    0,  134,   32,   33,   32,   33,
    0,   32,   33,    8,    9,   10,    0,    0,   11,   12,
  135,    0,   13,    0,    0,    0,   14,   78,   78,   78,
   78,   78,   78,   78,   78,    0,   78,   81,    0,    0,
    0,   90,   90,   90,   90,   90,   90,   90,   90,    0,
   90,   74,    0,    0,  168,   77,   77,   77,   77,   77,
   77,   77,   77,  168,   77,    8,    9,   10,  133,    0,
   11,   12,    0,    0,   13,    0,  136,  173,   14,  176,
  195,    0,    0,    0,    0,    0,    8,    9,   10,  168,
  200,   11,   12,  221,    0,   13,  168,    0,  196,   14,
  211,  201,    0,    0,    0,    0,  232,  233,    0,  125,
  192,  187,  194,  197,  168,  199,  202,    0,  240,    0,
    0,    0,    0,  174,  212,    0,    0,    0,    0,   21,
  168,    0,    0,  189,  207,  168,    0,    0,    0,  213,
    0,  168,    0,  193,    0,    0,    0,    0,    0,  222,
    0,  224,  168,  223,    0,  228,    0,  230,    0,    0,
    0,    0,    0,  229,  235,  162,  236,    0,  238,    0,
  239,    9,   10,   79,   80,   11,   12,  237,  243,   13,
    0,    0,    0,  169,    0,    9,   10,    0,    0,   11,
   12,   73,  208,   13,  214,    0,    0,    0,    0,    0,
    0,    0,    9,   10,  225,   80,   11,   12,    9,   10,
   13,    0,   11,   12,    9,   10,   13,   80,   11,   12,
    0,    0,   13,    0,    9,   10,    0,   80,   11,   12,
    0,    0,   13,    0,    9,   10,    0,   80,   11,   12,
    0,    0,   13,    9,   10,    9,   10,   11,   12,   11,
   12,   13,    0,   13,  166,    9,   10,    0,    0,   11,
   12,    0,    0,   13,  166,    9,   10,    0,    0,   11,
   12,    0,    0,   13,  166,    9,   10,    0,    0,   11,
   12,    0,    0,   13,  166,    9,   10,    0,    0,   11,
   12,  137,    0,   13,  166,    9,   10,    9,   10,   11,
   12,   11,   12,   13,    0,   13,    0,    0,  166,    9,
   10,    0,    0,   11,   12,    9,   10,   13,    0,   11,
   12,    0,    0,   13,    9,   10,    9,   10,   11,   12,
   11,   12,   13,    0,   13,  163,    9,   10,    0,    0,
   11,   12,    0,  170,   13,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  188,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  204,    0,    0,
    0,    0,    0,    0,    0,  209,    0,    0,    0,    0,
  215,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  226,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         59,
    0,   41,   42,   43,   43,   45,   45,   47,   44,    2,
   40,    4,   59,  123,  123,   59,   59,   59,   41,   59,
   43,   43,   45,   45,   59,   12,  256,   44,   35,   40,
   44,   40,   44,   44,   44,   40,   59,   44,   44,  269,
   47,  256,  125,  256,   61,   44,  266,   61,   59,   61,
   46,   61,  265,  268,  269,   41,   43,   43,   40,   45,
  256,  125,   61,   41,   46,   43,   41,   45,   43,  265,
   45,   58,   41,   59,   43,  264,   45,   64,  256,   66,
   67,   41,   43,  123,   45,   72,   59,  265,   95,   41,
   59,   43,   43,   45,   45,   45,  256,   42,   59,   42,
  123,   59,   47,   59,   47,  265,   41,   59,   43,  265,
   45,   59,   60,  257,  258,   59,   41,  261,  262,   44,
  269,  265,   70,   71,   59,  125,   41,   41,   43,   43,
   45,   45,   41,  259,  260,   44,  143,  123,   42,   43,
   46,   45,  265,   47,   59,   41,  264,   43,   41,   45,
  265,   44,  259,  260,  123,  265,  259,  260,   41,  265,
   59,   41,   41,   59,   43,  265,   45,  265,  265,  156,
   46,  123,   41,  256,  257,  258,  123,   59,  261,  262,
   59,   41,  265,   43,  177,   45,  269,  265,  123,  265,
   40,   41,  256,  257,  258,   45,  256,  261,  262,   59,
   40,  265,  264,  260,   59,   45,   40,   59,  123,  256,
   40,   45,  256,  256,  256,   45,  256,  257,  258,  259,
  260,  261,  262,  263,   40,  265,  256,  123,  264,   45,
  270,  270,  272,  256,  257,  258,  259,  260,  261,  262,
  263,   45,  265,  265,  123,  256,   40,  270,  265,  272,
  272,  265,  266,  265,  265,  265,  256,  257,  258,  265,
   40,  261,  262,  123,   40,  265,  265,  266,   45,  269,
  256,  257,  258,  259,  260,  261,  262,  263,  256,  265,
   45,  256,   45,  256,  270,   45,  272,  256,  257,  258,
  259,  260,  261,  262,  263,  256,  265,   45,  256,   45,
  256,  270,   45,  272,  256,  257,  258,  259,  260,  261,
  262,  263,  256,  265,  264,  265,   40,   59,  270,   41,
  272,  256,  257,  258,  259,  260,  261,  262,  263,   41,
  265,  125,   41,   41,    0,  270,   41,  272,  143,   10,
   59,  256,  257,  258,  259,  260,  261,  262,  263,  125,
  265,   35,   -1,  156,   59,  270,   -1,  272,   -1,   -1,
  256,  257,  258,  259,  260,  261,  262,  263,   59,  265,
   -1,   -1,   -1,   -1,  270,   -1,  272,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,  265,   -1,   -1,   -1,
   -1,  270,  125,  272,   -1,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  123,  265,  256,   -1,   -1,   -1,
  270,   -1,  272,   -1,  264,  265,  256,  267,  123,   -1,
   -1,  271,  256,   -1,  264,  265,  256,  267,   -1,  269,
  264,  265,  123,  267,  264,  265,   -1,  267,   -1,   -1,
  256,    5,    6,    7,  125,   -1,   -1,   -1,  264,  265,
   -1,  267,  256,   -1,   -1,   -1,   -1,   -1,   61,   62,
  264,  265,  256,  257,  258,  125,   -1,  261,  262,   -1,
   -1,  265,   -1,   37,   77,   78,   -1,   -1,   42,  256,
  256,  257,  258,   -1,   -1,  261,  262,  264,  265,  265,
   -1,  256,   -1,  256,   -1,   -1,  256,   -1,   -1,  264,
  265,  264,  265,   -1,  264,  265,   -1,   -1,  256,   73,
  256,   -1,   -1,  256,   -1,   79,  264,  265,  264,  265,
   -1,  264,  265,  256,  257,  258,   -1,   -1,  261,  262,
   79,   -1,  265,   -1,   -1,   -1,  269,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,  265,  123,   -1,   -1,
   -1,  256,  257,  258,  259,  260,  261,  262,  263,   -1,
  265,  123,   -1,   -1,  128,  256,  257,  258,  259,  260,
  261,  262,  263,  137,  265,  256,  257,  258,  123,   -1,
  261,  262,   -1,   -1,  265,   -1,  125,  136,  269,  138,
  123,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,  163,
  123,  261,  262,  206,   -1,  265,  170,   -1,  172,  269,
  123,  175,   -1,   -1,   -1,   -1,  219,  220,   -1,  123,
  169,  123,  171,  172,  188,  174,  175,   -1,  231,   -1,
   -1,   -1,   -1,  125,  198,   -1,   -1,   -1,   -1,  203,
  204,   -1,   -1,  125,  193,  209,   -1,   -1,   -1,  198,
   -1,  215,   -1,  125,   -1,   -1,   -1,   -1,   -1,  208,
   -1,  210,  226,  125,   -1,  214,   -1,  216,   -1,   -1,
   -1,   -1,   -1,  125,  223,  125,  225,   -1,  227,   -1,
  229,  257,  258,  259,  260,  261,  262,  125,  237,  265,
   -1,   -1,   -1,  125,   -1,  257,  258,   -1,   -1,  261,
  262,  263,  125,  265,  125,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  257,  258,  125,  260,  261,  262,  257,  258,
  265,   -1,  261,  262,  257,  258,  265,  260,  261,  262,
   -1,   -1,  265,   -1,  257,  258,   -1,  260,  261,  262,
   -1,   -1,  265,   -1,  257,  258,   -1,  260,  261,  262,
   -1,   -1,  265,  257,  258,  257,  258,  261,  262,  261,
  262,  265,   -1,  265,  256,  257,  258,   -1,   -1,  261,
  262,   -1,   -1,  265,  256,  257,  258,   -1,   -1,  261,
  262,   -1,   -1,  265,  256,  257,  258,   -1,   -1,  261,
  262,   -1,   -1,  265,  256,  257,  258,   -1,   -1,  261,
  262,   81,   -1,  265,  256,  257,  258,  257,  258,  261,
  262,  261,  262,  265,   -1,  265,   -1,   -1,  256,  257,
  258,   -1,   -1,  261,  262,  257,  258,  265,   -1,  261,
  262,   -1,   -1,  265,  257,  258,  257,  258,  261,  262,
  261,  262,  265,   -1,  265,  125,  257,  258,   -1,   -1,
  261,  262,   -1,  133,  265,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  161,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  187,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  195,   -1,   -1,   -1,   -1,
  200,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  211,
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
"sentencia_return : RETURN expresion ';'",
"sentencia_return : RETURN expresion error",
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
"sentencia_ejecutable : IF condicion_if_while '{' bloque_ejecutable '}' ELSE '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : IF condicion_if_while '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : IF condicion_if_while sentencia_ejecutable ';' ELSE sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : IF condicion_if_while sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : IF condicion_if_while '{' bloque_ejecutable '}' ELSE sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : IF condicion_if_while sentencia_ejecutable ';' ELSE '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : PRINT '(' CADENA ')'",
"sentencia_ejecutable : PRINT '(' expresion ')'",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_return",
"sentencia_ejecutable : WHILE condicion_if_while DO '{' bloque_ejecutable '}'",
"sentencia_ejecutable : WHILE condicion_if_while DO sentencia_ejecutable",
"sentencia_ejecutable : WHILE condicion_if_while DO",
"sentencia_ejecutable : WHILE condicion_if_while DO '{' '}'",
"sentencia_ejecutable : PRINT '(' ')'",
"sentencia_ejecutable : WHILE condicion_if_while '{' bloque_ejecutable '}'",
"sentencia_ejecutable : WHILE condicion_if_while sentencia_ejecutable",
"sentencia_ejecutable : IF condicion_if_while '{' '}' ELSE '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : IF condicion_if_while '{' bloque_ejecutable '}' ELSE '{' '}' end_if",
"sentencia_ejecutable : IF condicion_if_while '{' '}' ELSE '{' '}' end_if",
"sentencia_ejecutable : IF condicion_if_while '{' '}' end_if",
"sentencia_ejecutable : IF condicion_if_while ELSE sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : IF condicion_if_while sentencia_ejecutable ';' ELSE end_if",
"sentencia_ejecutable : IF condicion_if_while ELSE end_if",
"sentencia_ejecutable : IF condicion_if_while end_if",
"sentencia_ejecutable : IF condicion_if_while '{' '}' ELSE sentencia_ejecutable ';' end_if",
"sentencia_ejecutable : IF condicion_if_while '{' bloque_ejecutable '}' ELSE end_if",
"sentencia_ejecutable : IF condicion_if_while '{' '}' ELSE end_if",
"sentencia_ejecutable : IF condicion_if_while ELSE '{' bloque_ejecutable '}' end_if",
"sentencia_ejecutable : IF condicion_if_while sentencia_ejecutable ';' ELSE '{' '}' end_if",
"sentencia_ejecutable : IF condicion_if_while ELSE '{' '}' end_if",
"end_if : ENDIF",
"end_if :",
"condicion_if_while : '(' condicion ')'",
"condicion_if_while : condicion ')'",
"condicion_if_while : '(' condicion",
"condicion_if_while : condicion",
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
"factor : ID '(' parametros_reales ')'",
"factor : '-' CTE",
};

//#line 227 "Gramatica.y"

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

public ParserVal constanteNegativa(ParserVal clave) {
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

    ParserVal val = new ParserVal();
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
public ParserVal declaracionDeVariable(String token, String tipo, String ambito, String uso){
    String nombreNuevo = token + ":" + ambito;
    ParserVal nuevoParserVal = new ParserVal();
    if (!TablaDeSimbolos.estaSimbolo(nombreNuevo)) {
        ElementoTablaDeSimbolos nuevoElem = new ElementoTablaDeSimbolos();
        nuevoElem.setTipo(tipo);
        nuevoElem.setAmbito(ambito);
        nuevoElem.setUso(uso);
        TablaDeSimbolos.addSimbolo(nombreNuevo, nuevoElem);
        nuevoParserVal.sval = nombreNuevo;
    } else {
        yyerror("Error: Redeclaracion de variable " + token + " en el ambito " + ambito);
        nuevoParserVal.sval = token;
    }
    return nuevoParserVal;
}

public ParserVal chequearAmbito(String prefijo, String ambitoReal, String nombreIdentificador) {
    ElementoTablaDeSimbolos elem = null;
    String claveBuscada = null;
    String ambitoDeBusqueda = ambito; // copia de la variable global ambito
    boolean simboloEncontrado = false;
    String ambitoActual = "";
    ParserVal val = new ParserVal();
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

public void declaracionDeFuncion(String token, String tipoRetorno, String ambito, String uso) {
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
    nuevoElem.setTipo(tipoRetorno);
    nuevoElem.setAmbito(ambito);
    nuevoElem.setUso("Función");
    TablaDeSimbolos.addSimbolo(token, nuevoElem);
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
//#line 793 "Parser.java"
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
//#line 20 "Gramatica.y"
{ }
break;
case 2:
//#line 21 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre al programa"); }
break;
case 3:
//#line 22 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '{' al inicio"); }
break;
case 4:
//#line 23 "Gramatica.y"
{ yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
break;
case 7:
//#line 30 "Gramatica.y"
{ reportarEstructura("declaracion de funcion");
                                                                                            salirAmbito();
                                                                                            chequearReturn();
                                                                                          }
break;
case 8:
//#line 36 "Gramatica.y"
{entrarAmbito(val_peek(0).sval);
                                 System.out.println(ambito);
                                 declaracionDeFuncion(val_peek(0).sval, val_peek(1).sval, ambito, "Función");
                                 pilaReturns.push(val_peek(0).sval + ":" + "false");
                                }
break;
case 9:
//#line 41 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre a la función"); }
break;
case 13:
//#line 48 "Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 14:
//#line 49 "Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 15:
//#line 50 "Gramatica.y"
{yyerror("Error: Sentencia invalida");}
break;
case 16:
//#line 53 "Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 21:
//#line 62 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 22:
//#line 63 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 23:
//#line 64 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 24:
//#line 65 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 26:
//#line 71 "Gramatica.y"
{registrarReturn();}
break;
case 27:
//#line 72 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 28:
//#line 75 "Gramatica.y"
{tipo = "ulong";}
break;
case 31:
//#line 80 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre constantes"); }
break;
case 32:
//#line 83 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval);}
break;
case 33:
//#line 84 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval);}
break;
case 34:
//#line 85 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); }
break;
case 35:
//#line 86 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); }
break;
case 36:
//#line 87 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 37:
//#line 88 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 38:
//#line 91 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 39:
//#line 92 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 40:
//#line 93 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 41:
//#line 98 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 42:
//#line 99 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 43:
//#line 100 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 44:
//#line 101 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 45:
//#line 102 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 46:
//#line 103 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 47:
//#line 104 "Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 48:
//#line 105 "Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 52:
//#line 109 "Gramatica.y"
{ reportarEstructura("WHILE"); }
break;
case 53:
//#line 110 "Gramatica.y"
{ reportarEstructura("WHILE"); }
break;
case 54:
//#line 111 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 55:
//#line 112 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 56:
//#line 113 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 57:
//#line 115 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 58:
//#line 116 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 59:
//#line 118 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 60:
//#line 119 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 61:
//#line 120 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 62:
//#line 121 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 63:
//#line 122 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 64:
//#line 123 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 65:
//#line 124 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 66:
//#line 125 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 67:
//#line 126 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 68:
//#line 127 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 69:
//#line 128 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 70:
//#line 129 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 71:
//#line 130 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 74:
//#line 137 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 76:
//#line 141 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 77:
//#line 142 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 78:
//#line 143 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 81:
//#line 151 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 82:
//#line 152 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 83:
//#line 153 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 86:
//#line 159 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 88:
//#line 163 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 89:
//#line 164 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 91:
//#line 170 "Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                               yyval = ArregloTercetos.crearTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval);}
break;
case 92:
//#line 174 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval);}
break;
case 93:
//#line 175 "Gramatica.y"
{yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(2).sval);}
break;
case 94:
//#line 178 "Gramatica.y"
{ reportarEstructura("asignacion multiple"); }
break;
case 95:
//#line 182 "Gramatica.y"
{ reportarEstructura("expresion lambda"); }
break;
case 96:
//#line 183 "Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 97:
//#line 184 "Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 98:
//#line 185 "Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 99:
//#line 188 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("+", val_peek(2).sval, val_peek(0).sval);}
break;
case 100:
//#line 189 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("-", val_peek(2).sval, val_peek(0).sval);}
break;
case 101:
//#line 190 "Gramatica.y"
{yyval = val_peek(0);}
break;
case 102:
//#line 191 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 103:
//#line 192 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 104:
//#line 193 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 105:
//#line 194 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 106:
//#line 195 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 107:
//#line 196 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 109:
//#line 198 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 110:
//#line 199 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 111:
//#line 200 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 113:
//#line 207 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("*", val_peek(2).sval, val_peek(0).sval); }
break;
case 114:
//#line 208 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("/", val_peek(2).sval, val_peek(0).sval);}
break;
case 115:
//#line 209 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 116:
//#line 210 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 117:
//#line 211 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 118:
//#line 212 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 119:
//#line 213 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 120:
//#line 214 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 121:
//#line 215 "Gramatica.y"
{yyval = val_peek(0);}
break;
case 122:
//#line 218 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); }
break;
case 124:
//#line 220 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); }
break;
case 126:
//#line 222 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); }
break;
//#line 1342 "Parser.java"
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
