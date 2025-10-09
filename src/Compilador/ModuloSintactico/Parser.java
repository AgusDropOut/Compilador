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
//#line 24 "Parser.java"




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
public final static short ID=257;
public final static short WHILE=258;
public final static short IF=259;
public final static short ELSE=260;
public final static short ENDIF=261;
public final static short PRINT=262;
public final static short RETURN=263;
public final static short DO=264;
public final static short CTE=265;
public final static short ASIG=266;
public final static short TRUNC=267;
public final static short CR=268;
public final static short ULONG=269;
public final static short COMP=270;
public final static short CADENA=271;
public final static short FLECHA=272;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    1,    1,    3,    3,    2,    2,
    2,    2,    2,    2,    6,    5,    5,    9,    9,    9,
    9,    9,    9,   10,   11,   11,    4,   13,   13,   13,
    8,    8,    8,    8,    8,    8,    7,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    7,   16,
   16,   14,   14,   14,   14,   15,   15,   15,   15,   15,
   21,   21,   21,   22,   22,   22,   20,   17,   17,   18,
   19,   19,   19,   19,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   12,   24,   24,   24,
   24,   24,   24,   24,   24,   24,   23,   23,   23,   23,
   23,
};
final static short yylen[] = {                            2,
    4,    3,    3,    2,    0,    2,    8,    8,    2,    2,
    1,    2,    2,    2,    2,    1,    3,    3,    2,    3,
    2,    3,    2,    1,    3,    3,    1,    1,    3,    2,
    1,    3,    3,    5,    2,    4,   10,    6,    8,    5,
    9,    9,    4,    4,    1,    1,    1,    1,    6,    4,
    3,    5,    3,    5,    3,    9,    9,    8,    5,    6,
    6,    4,    3,    8,    7,    6,    7,    8,    6,    1,
    0,    3,    2,    2,    1,    2,    3,    2,    2,    3,
    1,    3,    1,    3,    3,    2,    3,    3,    5,    3,
   10,    9,    9,    8,    3,    3,    1,    3,    3,    3,
    3,    3,    3,    4,    4,    4,    4,    3,    3,    3,
    3,    3,    3,    3,    3,    1,    1,    1,    3,    4,
    2,
};
final static short yydefred[] = {                         0,
    0,    5,    0,    5,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   27,    3,    6,   11,    0,    0,
    0,    0,   48,   45,   46,   47,    2,    1,   14,    0,
    0,    0,    0,    0,    0,    0,  118,    0,    0,    0,
    0,  116,    0,    0,    0,    0,    0,    0,    0,   12,
    9,   13,   10,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  121,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   55,   73,    0,    0,    0,   70,
    0,    0,   63,    0,   53,    0,   26,   25,    0,    0,
    0,    0,    0,   28,    0,    0,    0,    0,    0,    0,
    0,  114,  110,  115,  112,    0,    0,    0,   81,  119,
   72,    0,    0,    0,    0,    0,    0,    0,    0,   50,
    0,    0,  111,  108,  113,  109,    0,    0,   62,    0,
    0,    0,   43,   44,    0,   24,    0,    0,   16,    0,
    0,   33,   36,    0,   30,    0,    0,    0,    0,   86,
    0,  120,    0,  107,  106,  105,  104,   52,    0,   79,
   76,   78,   54,    0,    0,    0,    0,    0,   59,    0,
    0,   40,   23,   21,   19,    0,    0,    0,    0,    0,
   34,   29,    0,    0,    0,   85,   84,   82,   49,   80,
   77,   69,    0,   60,    0,    0,   66,    0,   38,    0,
    0,   61,    5,   17,   22,   20,   18,    5,    0,    0,
    0,    0,   67,    0,    0,    0,    0,    0,   65,    0,
    0,    0,    0,    0,    0,    0,   94,    0,   58,    0,
   64,    0,    0,    0,   68,    0,   39,    8,    7,   93,
    0,   92,   56,   57,    0,   41,   42,   91,   37,
};
final static short yydgoto[] = {                          3,
    5,   17,   18,   19,  138,   20,  121,   22,  139,  140,
   23,   39,   95,   40,  122,   83,   24,   25,   26,   41,
  108,  109,   42,   43,
};
final static short yysindex[] = {                      -100,
  -94,    0,    0,    0,  172,  189,  206,  -14,  -16, -226,
  155,  155,   11,  580,    0,    0,    0,    0, -230,  -50,
  -48,   26,    0,    0,    0,    0,    0,    0,    0,  580,
 -204, -200,   36, -198,  -15,  580,    0,  -40,  -39,  370,
   20,    0,   29,  378,  152,    9,   40,  -12,   -5,    0,
    0,    0,    0,   19, -175, -169,   -8, -166,   63,   -9,
   76,  224,  258,    0,  605, -139,   79,  580,  580,  350,
  474,  580,  485,  297,    0,    0,  501,  517,  386,    0,
  521,   65,    0,   88,    0,   54,    0,    0, -206, -206,
 -126, -120,   96,    0,  -32,  580,  528,   51,   29,   51,
   29,    0,    0,    0,    0,   36,  -27,   69,    0,    0,
    0,  -28,   -1,   51,   29,   51,   29,   -8,  543,    0,
  -45,  354,    0,    0,    0,    0,  561,   89,    0, -127,
  396, -125,    0,    0,  -97,    0, -104,   71,    0, -210,
   73,    0,    0,  -92,    0,  -87,   -8,  297,  404,    0,
  -89,    0,  580,    0,    0,    0,    0,    0,  416,    0,
    0,    0,    0,   42,  -90,  426,  -90,  434,    0,  -91,
  442,    0,    0,    0,    0,   50, -206,  -76,  -80,   60,
    0,    0,  455,  235,  147,    0,    0,    0,    0,    0,
    0,    0,  -90,    0,  569,  132,    0,  462,    0,  581,
  133,    0,    0,    0,    0,    0,    0,    0,  235,  154,
  157,  159,    0,  -90,  470,  -90,  591,  140,    0,  -90,
  478,  -90,  214,  228,  160,  159,    0,  162,    0,  -90,
    0,  -90,  505,  -90,    0,  -90,    0,    0,    0,    0,
  164,    0,    0,    0,  -90,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    1,    0,    0,    0,  207,    0,    0,    0,   61,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  143,    0,    0,    0,    0,    0,
  314,    0,  -38,   64,    0,    0,    0,   44,   84,    0,
    0,    0,    0,    5,    0,    0,   99,   95,    0,    0,
    0,    0,    0,    0,    0,    0,  330,    0,    0,    0,
    0,    0,  100,    0,    0,    0,    0,    0,   64,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   25,    0,  115,    0,    0,  -21,   15,   32,
   49,    0,    0,    0,    0,   75,    0,    0,    0,    0,
    0,    0,    0,   66,   87,  104,  121,  275,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   64,
    0,   64,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  116,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   64,    0,   64,   64,    0,   64,
   64,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   64,    0,    0,    0,    0,   64,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   64,    0,   64,    0,    0,    0,   64,
    0,   64,    0,    0,    0,    0,    0,    0,    0,   64,
    0,   64,    0,   64,    0,   64,    0,    0,    0,    0,
    0,    0,    0,    0,   64,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    6,    0,    0,  740,  123,    0,  381,  196,   90,    0,
    0,  837,    0,  205,  715,  725,    0,    0,    0,  217,
    0,  131,  -30,   80,
};
final static int YYTABLESIZE=990;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         69,
    5,   97,   97,   70,   97,   71,   97,    6,   51,    7,
   53,  146,  155,  161,   70,   70,   71,   71,  102,  102,
   97,  102,    2,  102,   65,   47,   48,   90,    4,   31,
   66,  103,  105,   91,   70,   34,   71,  102,   55,  157,
    5,   70,   15,   71,   29,  178,  124,  126,   35,  135,
   45,   70,   58,   71,   98,   98,   59,   98,   15,   98,
   76,  136,   15,   35,   92,   35,   64,   88,   32,   55,
   77,  103,  103,   98,  103,   78,  103,   62,   60,   89,
   61,   93,   63,   32,   97,   32,   56,   31,  100,  100,
  103,  100,   62,  100,  134,   94,   70,   63,   71,   96,
  191,  102,   31,   97,   31,   99,   99,  100,   99,  152,
   99,  176,  153,  180,  177,   83,  177,  110,   83,  111,
   34,   31,   71,  132,   99,    5,   95,   95,  133,   95,
  142,   95,  168,   80,  171,   80,  143,   98,   33,   99,
  101,  144,   15,  101,  101,   95,  101,  167,  101,  115,
  117,  174,  175,  211,  103,   33,    1,   88,   51,  173,
   96,   96,  101,   96,  181,   96,  186,  187,  198,   80,
   80,  100,  203,   90,   89,  206,  207,  182,  225,   96,
  205,  228,  208,  117,  117,  117,  212,  117,   99,  117,
  216,  222,   85,  226,   36,  241,   34,  227,  234,   34,
  240,  117,  242,   34,  248,   50,    4,   52,  223,   95,
  160,   10,  141,  224,   49,   68,   44,   97,   97,   97,
   97,   97,   97,   97,   97,   97,  101,  154,   10,  150,
   72,   97,  145,   97,  102,  102,  102,  102,  102,  102,
  102,  102,  102,   96,  151,   10,   98,   35,  102,   30,
  102,   54,   67,   10,  156,   37,    5,    5,    5,    5,
   35,   35,    5,    5,   87,  117,  204,   10,   34,    5,
   98,   98,   98,   98,   98,   98,   98,   98,   98,   34,
   32,   32,   54,  188,   98,    0,   98,  103,  103,  103,
  103,  103,  103,  103,  103,  103,   16,  190,    0,   31,
   31,  103,   34,  103,  100,  100,  100,  100,  100,  100,
  100,  100,  100,   27,   87,   87,    0,   31,  100,   71,
  100,   99,   99,   99,   99,   99,   99,   99,   99,   99,
   28,  100,   35,   87,    0,   99,   10,   99,  238,   15,
   37,    0,   95,   95,   95,   95,   95,   95,   95,   95,
   95,   33,  239,   75,   88,   51,   95,    0,   95,  101,
  101,  101,  101,  101,  101,  101,  101,  101,    0,   74,
   90,   89,   75,  101,    0,  101,   96,   96,   96,   96,
   96,   96,   96,   96,   96,   21,   21,   21,   74,    0,
   96,    0,   96,   10,   34,    0,    0,   87,  117,  117,
  117,  117,  117,  117,  117,  117,  117,   33,   35,   10,
   33,   35,  117,    0,  117,   35,   37,   10,   38,   37,
   75,   38,   84,   37,   82,   10,    0,    8,    9,   11,
   12,    0,    0,   13,   14,   10,   75,    0,    0,    0,
   15,    0,    0,  184,    8,    9,   11,   12,    0,    0,
   13,   14,   74,  120,    0,   10,    0,   15,    0,  128,
    0,    8,    9,   11,   12,   10,    0,   13,   14,    8,
    9,   11,   12,   10,   15,   13,   14,    0,  163,  102,
   35,   10,   15,    8,    9,   11,   12,    0,   37,   13,
   14,   35,   74,    0,  209,    0,   15,    0,    0,   37,
   81,   10,  164,   15,    0,    0,    0,    0,  127,   10,
    0,  164,    0,  104,   35,    0,    0,   10,   34,    0,
  170,    0,   37,    0,   10,    0,    0,    0,  185,  164,
   87,   87,   87,   87,   87,   87,   87,   87,   87,  164,
  189,    0,    0,    0,   10,   34,  164,    0,  196,    0,
  193,  201,    0,    9,   11,   12,  195,    0,   13,   14,
   10,   34,    0,  164,  200,    0,    0,   10,    0,   75,
   75,   75,   75,   75,   75,   75,   75,   75,  218,  210,
    0,    0,   10,    0,  217,   74,   74,   74,   74,   74,
   74,   74,   74,   74,  230,  164,    0,    0,    0,    0,
   10,  164,  236,   21,   21,  114,   35,  119,   10,  162,
    9,   11,   12,  164,   37,   13,   14,    0,    0,    0,
   10,    0,    0,    0,   34,    0,    9,   11,   12,  245,
   10,   13,   14,   73,    9,   11,   12,   79,   80,   13,
   14,    0,    9,   11,   12,  130,   80,   13,   14,   34,
  148,  162,    9,   11,   12,    0,    0,   13,   14,  162,
    9,   11,   12,    0,    0,   13,   14,  158,    0,    0,
    0,  162,    9,   11,   12,    0,    0,   13,   14,    0,
    0,  162,    9,   11,   12,  165,    0,   13,   14,    0,
    9,   11,   12,  214,   80,   13,   14,    0,    9,   11,
   12,    0,   80,   13,   14,  220,    0,    0,    0,    0,
  162,    9,   11,   12,    0,  232,   13,   14,    9,   11,
   12,    0,   80,   13,   14,  162,    9,   11,   12,  116,
   35,   13,   14,  162,    9,   11,   12,    0,   37,   13,
   14,    9,   11,   12,    0,    0,   13,   14,    0,   32,
    0,    0,    0,    0,    0,    0,  123,   35,    0,    0,
  162,    9,   11,   12,    0,   37,   13,   14,    0,    0,
    0,    0,  125,   35,    0,    0,    0,    9,   11,   12,
    0,   37,   13,   14,    9,   11,   12,    0,    0,   13,
   14,    0,    0,    0,    0,  131,    0,    0,    0,    9,
   11,   12,    0,  129,   13,   14,    0,    0,    0,    0,
    0,  149,    0,    0,    0,    0,    0,    9,   11,   12,
    0,    0,   13,   14,    0,    9,   11,   12,  137,  137,
   13,   14,    0,  159,    0,   33,   35,    9,   11,   12,
    0,  166,   13,   14,   37,    0,   38,    9,   11,   12,
   46,    0,   13,   14,  169,    0,  172,    0,    0,    0,
  106,   35,  183,    0,    0,    0,   57,    0,    0,   37,
    0,   38,    0,    0,    0,    0,    0,    0,    0,  179,
    0,   86,    0,    0,    0,    0,    0,    0,    0,  192,
    0,  194,  197,    0,  199,  202,    0,    0,    0,    0,
    0,  107,    0,    0,  112,  113,    0,    0,  118,  215,
    0,    0,    0,    0,  221,    0,  137,  213,    0,    0,
    0,    0,  219,   32,    0,    0,    0,    0,    0,    0,
    0,  233,  147,    0,    0,    0,    0,    0,  229,    0,
  231,    0,    0,    0,  235,    0,  237,    0,   32,    0,
    0,    0,    0,    0,  243,    0,  244,    0,  246,    0,
  247,    0,    0,    0,    0,    0,    0,    0,    0,  249,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  107,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   41,   43,   43,   45,   45,    2,   59,    4,
   59,   44,   41,   59,   43,   43,   45,   45,   40,   41,
   59,   43,  123,   45,   40,  256,  257,   40,  123,   46,
   46,   62,   63,   46,   43,   45,   45,   59,   44,   41,
   40,   43,  269,   45,   59,  256,   77,   78,   44,  256,
   40,   43,  257,   45,   40,   41,  257,   43,  269,   45,
   41,  268,  269,   59,   46,   61,  265,   59,   44,   44,
   42,   40,   41,   59,   43,   47,   45,   42,   43,   40,
   45,  257,   47,   59,  123,   61,   61,   44,   40,   41,
   59,   43,   42,   45,   41,  265,   43,   47,   45,  266,
   59,  123,   59,   41,   44,   40,   41,   59,   43,   41,
   45,   41,   44,   41,   44,   41,   44,  257,   44,   41,
   45,   61,   59,   59,   59,  125,   40,   41,   41,   43,
  257,   45,  260,  261,  260,  261,  257,  123,   44,   60,
   61,   46,   59,   40,   41,   59,   43,   59,   45,   70,
   71,  256,  257,  184,  123,   61,  257,   59,   59,  257,
   40,   41,   59,   43,  257,   45,  256,  257,  260,  261,
  261,  123,  123,   59,   59,  256,  257,  265,  209,   59,
  257,  212,  123,   41,   42,   43,   40,   45,  123,   47,
   59,   59,   41,   40,   40,  226,   45,   41,   59,   45,
   41,   59,   41,   45,   41,  256,    0,  256,  203,  123,
  256,   40,   90,  208,   19,  256,   12,  256,  257,  258,
  259,  260,  261,  262,  263,  264,  123,  256,   40,  257,
  270,  270,  265,  272,  256,  257,  258,  259,  260,  261,
  262,  263,  264,  123,  272,   40,  256,  257,  270,  266,
  272,  257,   36,   40,  256,  265,  256,  257,  258,  259,
  256,  257,  262,  263,  256,  123,  177,   40,   45,  269,
  256,  257,  258,  259,  260,  261,  262,  263,  264,   45,
  256,  257,  257,  153,  270,   -1,  272,  256,  257,  258,
  259,  260,  261,  262,  263,  264,  125,  256,   -1,  256,
  257,  270,   45,  272,  256,  257,  258,  259,  260,  261,
  262,  263,  264,  125,   40,   41,   -1,  257,  270,  256,
  272,  256,  257,  258,  259,  260,  261,  262,  263,  264,
  125,  256,  257,   59,   -1,  270,   40,  272,  125,  256,
  265,   -1,  256,  257,  258,  259,  260,  261,  262,  263,
  264,  257,  125,   40,  256,  256,  270,   -1,  272,  256,
  257,  258,  259,  260,  261,  262,  263,  264,   -1,   40,
  256,  256,   59,  270,   -1,  272,  256,  257,  258,  259,
  260,  261,  262,  263,  264,    5,    6,    7,   59,   -1,
  270,   -1,  272,   40,   45,   -1,   -1,  123,  256,  257,
  258,  259,  260,  261,  262,  263,  264,  256,  257,   40,
  256,  257,  270,   -1,  272,  257,  265,   40,  267,  265,
   40,  267,  271,  265,   44,   40,   -1,  256,  257,  258,
  259,   -1,   -1,  262,  263,   40,  123,   -1,   -1,   -1,
  269,   -1,   -1,   40,  256,  257,  258,  259,   -1,   -1,
  262,  263,  123,   73,   -1,   40,   -1,  269,   -1,   79,
   -1,  256,  257,  258,  259,   40,   -1,  262,  263,  256,
  257,  258,  259,   40,  269,  262,  263,   -1,  125,  256,
  257,   40,  269,  256,  257,  258,  259,   -1,  265,  262,
  263,  257,  123,   -1,   40,   -1,  269,   -1,   -1,  265,
  123,   40,  122,  269,   -1,   -1,   -1,   -1,  123,   40,
   -1,  131,   -1,  256,  257,   -1,   -1,   40,   45,   -1,
  125,   -1,  265,   -1,   40,   -1,   -1,   -1,  125,  149,
  256,  257,  258,  259,  260,  261,  262,  263,  264,  159,
  125,   -1,   -1,   -1,   40,   45,  166,   -1,  168,   -1,
  125,  171,   -1,  257,  258,  259,  123,   -1,  262,  263,
   40,   45,   -1,  183,  123,   -1,   -1,   40,   -1,  256,
  257,  258,  259,  260,  261,  262,  263,  264,  198,  125,
   -1,   -1,   40,   -1,  123,  256,  257,  258,  259,  260,
  261,  262,  263,  264,  125,  215,   -1,   -1,   -1,   -1,
   40,  221,  125,  223,  224,  256,  257,  123,   40,  256,
  257,  258,  259,  233,  265,  262,  263,   -1,   -1,   -1,
   40,   -1,   -1,   -1,   45,   -1,  257,  258,  259,  125,
   40,  262,  263,  264,  257,  258,  259,  260,  261,  262,
  263,   -1,  257,  258,  259,  125,  261,  262,  263,   45,
  123,  256,  257,  258,  259,   -1,   -1,  262,  263,  256,
  257,  258,  259,   -1,   -1,  262,  263,  125,   -1,   -1,
   -1,  256,  257,  258,  259,   -1,   -1,  262,  263,   -1,
   -1,  256,  257,  258,  259,  125,   -1,  262,  263,   -1,
  257,  258,  259,  125,  261,  262,  263,   -1,  257,  258,
  259,   -1,  261,  262,  263,  125,   -1,   -1,   -1,   -1,
  256,  257,  258,  259,   -1,  125,  262,  263,  257,  258,
  259,   -1,  261,  262,  263,  256,  257,  258,  259,  256,
  257,  262,  263,  256,  257,  258,  259,   -1,  265,  262,
  263,  257,  258,  259,   -1,   -1,  262,  263,   -1,   10,
   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,   -1,   -1,
  256,  257,  258,  259,   -1,  265,  262,  263,   -1,   -1,
   -1,   -1,  256,  257,   -1,   -1,   -1,  257,  258,  259,
   -1,  265,  262,  263,  257,  258,  259,   -1,   -1,  262,
  263,   -1,   -1,   -1,   -1,   81,   -1,   -1,   -1,  257,
  258,  259,   -1,   79,  262,  263,   -1,   -1,   -1,   -1,
   -1,   97,   -1,   -1,   -1,   -1,   -1,  257,  258,  259,
   -1,   -1,  262,  263,   -1,  257,  258,  259,   89,   90,
  262,  263,   -1,  119,   -1,  256,  257,  257,  258,  259,
   -1,  127,  262,  263,  265,   -1,  267,  257,  258,  259,
   14,   -1,  262,  263,  130,   -1,  132,   -1,   -1,   -1,
  256,  257,  148,   -1,   -1,   -1,   30,   -1,   -1,  265,
   -1,  267,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  140,
   -1,   45,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  165,
   -1,  167,  168,   -1,  170,  171,   -1,   -1,   -1,   -1,
   -1,   65,   -1,   -1,   68,   69,   -1,   -1,   72,  195,
   -1,   -1,   -1,   -1,  200,   -1,  177,  193,   -1,   -1,
   -1,   -1,  198,  184,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  217,   96,   -1,   -1,   -1,   -1,   -1,  214,   -1,
  216,   -1,   -1,   -1,  220,   -1,  222,   -1,  209,   -1,
   -1,   -1,   -1,   -1,  230,   -1,  232,   -1,  234,   -1,
  236,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  245,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  153,
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
null,null,null,null,null,null,null,"ID","WHILE","IF","ELSE","ENDIF","PRINT",
"RETURN","DO","CTE","ASIG","TRUNC","CR","ULONG","COMP","CADENA","FLECHA",
};
final static String yyrule[] = {
"$accept : programa",
"programa : ID '{' list_sentencia '}'",
"programa : '{' list_sentencia '}'",
"programa : ID list_sentencia '}'",
"programa : ID list_sentencia",
"list_sentencia :",
"list_sentencia : list_sentencia sentencia",
"declaracion_funcion : tipo ID '(' parametros_formales ')' '{' list_sentencia '}'",
"declaracion_funcion : tipo error '(' parametros_formales ')' '{' list_sentencia '}'",
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
"list_vars : ID",
"list_vars : list_vars ',' ID",
"list_vars : ID '.' ID",
"list_vars : list_vars ',' ID '.' ID",
"list_vars : list_vars ID",
"list_vars : list_vars ID '.' ID",
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
"sentencia_ejecutable : expresion_lambda",
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
"asignacion_simple : ID ASIG expresion",
"asignacion_simple : ID '.' ID ASIG expresion",
"asignacion_multiple : list_vars '=' list_ctes",
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

//#line 208 "Gramatica.y"

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
//#line 653 "Parser.java"
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
{ reportarEstructura("declaracion de funcion"); }
break;
case 8:
//#line 31 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre a la función"); }
break;
case 12:
//#line 37 "Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 13:
//#line 38 "Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 14:
//#line 39 "Gramatica.y"
{yyerror("Error: Sentencia invalida");}
break;
case 15:
//#line 42 "Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 20:
//#line 51 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 21:
//#line 52 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 22:
//#line 53 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 23:
//#line 54 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 26:
//#line 61 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 30:
//#line 69 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre constantes"); }
break;
case 35:
//#line 77 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 36:
//#line 78 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 37:
//#line 83 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 38:
//#line 84 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 39:
//#line 85 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 40:
//#line 86 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 41:
//#line 87 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 42:
//#line 88 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 43:
//#line 89 "Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 44:
//#line 90 "Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 49:
//#line 95 "Gramatica.y"
{ reportarEstructura("WHILE"); }
break;
case 50:
//#line 96 "Gramatica.y"
{ reportarEstructura("WHILE"); }
break;
case 51:
//#line 97 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 52:
//#line 98 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 53:
//#line 99 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 54:
//#line 101 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 55:
//#line 102 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 56:
//#line 104 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 57:
//#line 105 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 58:
//#line 106 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 59:
//#line 107 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 60:
//#line 108 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 61:
//#line 109 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 62:
//#line 110 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 63:
//#line 111 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 64:
//#line 112 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 65:
//#line 113 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 66:
//#line 114 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 67:
//#line 115 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 68:
//#line 116 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 71:
//#line 123 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 73:
//#line 127 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 74:
//#line 128 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 75:
//#line 129 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 78:
//#line 137 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 79:
//#line 138 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 80:
//#line 139 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 83:
//#line 145 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 85:
//#line 149 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 86:
//#line 150 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 88:
//#line 156 "Gramatica.y"
{ reportarEstructura("asignacion simple"); }
break;
case 89:
//#line 157 "Gramatica.y"
{ reportarEstructura("asignacion simple"); }
break;
case 90:
//#line 160 "Gramatica.y"
{ reportarEstructura("asignacion multiple"); }
break;
case 91:
//#line 164 "Gramatica.y"
{ reportarEstructura("expresion lambda"); }
break;
case 92:
//#line 165 "Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 93:
//#line 166 "Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 94:
//#line 167 "Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 98:
//#line 173 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 99:
//#line 174 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 100:
//#line 175 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 101:
//#line 176 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 102:
//#line 177 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 103:
//#line 178 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 105:
//#line 180 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 106:
//#line 181 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 107:
//#line 182 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 110:
//#line 190 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 111:
//#line 191 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 112:
//#line 192 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 113:
//#line 193 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 114:
//#line 194 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 115:
//#line 195 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 121:
//#line 203 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); }
break;
//#line 1114 "Parser.java"
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
