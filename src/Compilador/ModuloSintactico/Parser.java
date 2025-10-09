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
    0,    0,    0,    0,    1,    1,    3,    3,    2,    2,
    2,    2,    2,    2,    6,    5,    5,    9,    9,    9,
    9,    9,    9,   10,   11,   11,    4,   13,   13,   13,
    8,    8,    8,    8,    8,    8,    7,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,   16,   16,
   14,   14,   14,   14,   15,   15,   15,   15,   15,   20,
   20,   20,   21,   21,   21,   19,   17,   17,   18,   22,
   22,   22,   22,   12,   12,   12,   12,   12,   12,   12,
   12,   12,   12,   12,   12,   12,   24,   24,   24,   24,
   24,   24,   24,   24,   24,   23,   23,   23,   23,   23,
   23,
};
final static short yylen[] = {                            2,
    4,    3,    3,    2,    0,    2,    8,    8,    2,    2,
    1,    2,    2,    2,    2,    1,    3,    3,    2,    3,
    2,    3,    2,    1,    3,    3,    1,    1,    3,    2,
    1,    3,    3,    5,    2,    4,   10,    6,    8,    5,
    9,    9,    4,    4,    1,    1,    1,    6,    4,    3,
    5,    3,    5,    3,    9,    9,    8,    5,    6,    6,
    4,    3,    8,    7,    6,    7,    8,    6,    1,    0,
    3,    2,    2,    1,    2,    3,    2,    2,    3,    1,
    3,    1,    3,    3,    2,    3,    3,    5,    3,   10,
    9,    9,    8,    3,    3,    1,    3,    3,    3,    3,
    3,    3,    4,    4,    4,    4,    3,    3,    3,    3,
    3,    3,    3,    3,    1,    1,    1,    3,    4,    2,
    1,
};
final static short yydefred[] = {                         0,
    0,    5,    0,    5,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   27,    3,    6,   11,    0,    0,    0,
    0,   47,   45,   46,    2,    1,   14,    0,    0,  117,
    0,    0,    0,    0,    0,    0,  121,  115,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   12,    9,
   13,   10,    0,    0,    0,    0,    0,    0,    0,  120,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   54,   72,    0,    0,    0,   69,    0,    0,   62,
    0,   52,    0,   26,   25,    0,    0,    0,    0,    0,
    0,    0,   28,    0,    0,    0,    0,    0,  113,  109,
  114,  111,    0,    0,    0,   80,  118,    0,    0,    0,
   71,    0,    0,    0,    0,    0,    0,   49,    0,    0,
  110,  107,  112,  108,    0,    0,   61,    0,    0,    0,
   43,   44,    0,    0,   24,    0,    0,   16,    0,    0,
   33,   36,    0,   30,    0,   85,    0,  119,    0,  106,
  105,  104,  103,    0,   51,    0,   78,   75,   77,   53,
    0,    0,    0,    0,    0,   58,    0,    0,   40,    0,
   23,   21,   19,    0,    0,    0,    0,    0,   34,   29,
   84,   83,   81,    0,    0,   48,   79,   76,   68,    0,
   59,    0,    0,   65,    0,   38,    0,    0,   60,    5,
   17,   22,   20,   18,    5,    0,    0,    0,   66,    0,
    0,    0,    0,    0,   64,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   57,    0,   63,    0,    0,    0,
   67,    0,   39,    8,    7,    0,    0,    0,   93,   55,
   56,    0,   41,   42,    0,   92,   91,   37,   90,
};
final static short yydgoto[] = {                          3,
    5,   16,   17,   18,  137,   19,  119,   21,  138,  139,
   22,   34,   94,   35,  120,   80,   23,   24,   36,  105,
  106,   37,   38,   39,
};
final static short yysindex[] = {                      -113,
  -89,    0,    0,    0,  295,  417,  453,    9,  167,  167,
    2,  171,  -37,    0,    0,    0,    0, -212,  -59,  -42,
  -16,    0,    0,    0,    0,    0,    0,   53, -184,    0,
   19,  -29,  161,  -38,  462,   47,    0,    0,    5,  505,
  151, -176,   60,  171, -168,   64,   30,   18,    0,    0,
    0,    0,   70, -159, -141,  -10,   -4,  239,  241,    0,
  185, -137,  171,  171, -135,   83,  256,  258,  171,  537,
  553,    0,    0,  273,  275,  482,    0,  304,   75,    0,
   91,    0,   26,    0,    0,  -31, -125, -243, -243, -123,
 -122,  101,    0,   35,   39,    5,   39,    5,    0,    0,
    0,    0,   53,  -21,   58,    0,    0,  -14,   44,  108,
    0,   39,    5,   39,    5,  -31,  494,    0,  -41,  -87,
    0,    0,    0,    0,  606,   92,    0, -244,  364, -177,
    0,    0,  171, -112,    0, -202,   69,    0, -223,   71,
    0,    0, -111,    0, -106,    0, -201,    0,  171,    0,
    0,    0,    0,  539,    0,  551,    0,    0,    0,    0,
   13, -101,  563, -101,  514,    0, -138,  520,    0,  -31,
    0,    0,    0,   37, -243, -104, -199,   43,    0,    0,
    0,    0,    0,  553,  207,    0,    0,    0,    0, -101,
    0,  620,  103,    0,  526,    0,  633,  109,    0,    0,
    0,    0,    0,    0,    0,  225,  133,    3,    0, -101,
  573, -101,  635,  117,    0, -101,  583, -101,  472,  492,
  137,    3,    3,  138,    0, -101,    0, -101,  596, -101,
    0, -101,    0,    0,    0,    3,  139,  142,    0,    0,
    0, -101,    0,    0,  144,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    1,    0,    0,    0,  187,    0,    0,    0,    0,    0,
    0,    0,  -12,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -39,    0,    0,    0,    0,  331,    0,    0,  -22,   61,
    0,    0,    0,    0,    0,    0,  154,   77,    0,    0,
    0,    0,   74,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  353,    0,    0,    0,   81,
    0,    0,    0,    0,    0,   61,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   85,   17,    0,    0,    0,
    0,  149,    0,   97,   15,   32,   49,   66,    0,    0,
    0,    0,   73,    0,    0,    0,    0,    0,    0,    0,
    0,   86,  105,  122,  141,  314,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   61,    0,   61,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   61,    0,   61,   61,    0,   61,   61,    0,   98,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   61,
    0,    0,    0,    0,   61,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   61,
    0,   61,    0,    0,    0,   61,    0,   61,    0,    0,
    0,    0,    0,    0,    0,   61,    0,   61,    0,   61,
    0,   61,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   61,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  135,    0,    0,  428,   99,    0,  439,  172,   20,    0,
    0,  843,    0,  184,  725,  471,    0,    0,  166,    0,
   54,    0,  419,  -17,
};
final static int YYTABLESIZE=992;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         50,
    5,  116,  116,  116,   67,  116,   68,  116,   45,    2,
   64,   67,  134,   68,  165,   77,   52,  158,   96,  116,
   96,   67,   96,   68,  135,   14,  151,   54,   67,   42,
   68,   31,  176,    4,   29,   42,   96,  160,   96,   98,
   29,   41,   42,   46,   55,   14,   74,   29,   31,  113,
  115,   75,   47,  172,  181,  101,  203,  101,   61,  101,
   33,   54,  173,  182,   62,  204,  132,   27,   67,   89,
   68,  188,   97,  101,   97,   90,   97,   33,  145,   60,
   58,  168,   77,  116,  153,   59,   67,   73,   68,  102,
   97,  102,   14,  102,   58,   56,   87,   57,  148,   59,
   96,  149,   67,   88,   68,   92,   99,  102,   99,  174,
   99,  178,  175,   82,  175,   91,   82,   35,   85,   70,
  195,   77,   93,  111,   99,    5,   98,  107,   98,  110,
   98,  131,   35,  130,   35,   15,    6,  101,    7,   50,
  133,  141,  142,   87,   98,   94,  143,   94,  154,   94,
  164,    1,  171,  179,   97,   89,   88,  180,   77,  200,
  202,  212,  100,   94,  100,  205,  100,  218,  159,    9,
   10,  102,  223,   11,   12,  230,  236,   13,  239,  246,
  100,   95,  247,   95,  249,   95,    4,  140,   99,   48,
   42,   82,   32,   40,  201,   29,   49,   31,   66,   95,
   42,    0,  183,    0,    0,   29,   33,   32,   98,   32,
   42,   29,   31,   51,  157,   29,  116,  116,  116,  116,
  116,  116,  116,  116,   42,  116,   63,   94,   44,   29,
  116,   69,  116,   96,   96,   96,   96,   96,   96,   96,
   96,  150,   96,  146,  100,   95,  208,   96,   53,   96,
  147,   97,   31,   30,   31,    0,    5,    5,    5,   30,
   31,    5,    5,   95,  222,    5,   30,   31,  187,    5,
  101,  101,  101,  101,  101,  101,  101,  101,   42,  101,
   42,   33,   53,   29,  101,   29,  101,   97,   97,   97,
   97,   97,   97,   97,   97,   42,   97,   42,  144,  152,
   29,   97,   29,   97,  102,  102,  102,  102,  102,  102,
  102,  102,   42,  102,   42,   84,   70,   29,  102,   29,
  102,   99,   99,   99,   99,   99,   99,   99,   99,   35,
   99,  207,   15,    0,  219,   99,   50,   99,   35,  220,
   87,   98,   98,   98,   98,   98,   98,   98,   98,  221,
   98,    0,   89,   88,   86,   98,    0,   98,    0,    0,
   94,   94,   94,   94,   94,   94,   94,   94,    0,   94,
    0,    0,   86,    0,   94,    0,   94,  100,  100,  100,
  100,  100,  100,  100,  100,    0,  100,    0,    0,   74,
    0,  100,    0,  100,    0,    0,   95,   95,   95,   95,
   95,   95,   95,   95,   32,   95,   28,    0,    0,   31,
   95,   73,   95,   32,   30,   31,   28,   32,   31,   15,
    0,   81,   28,    0,   30,   31,   28,   32,  128,   14,
   30,   31,    0,   32,   30,   31,   86,   32,    0,    0,
  103,    0,    0,   20,   20,   20,    0,    0,   30,   31,
    0,   32,    0,   74,    0,    0,    0,    0,    0,    0,
   65,    0,  159,    9,   10,    0,    0,   11,   12,   65,
    0,   13,    0,   72,    0,   73,  100,  102,   79,    0,
  159,    9,   10,    0,    0,   11,   12,    0,  167,   13,
    0,    0,  122,  124,   99,    0,  101,    0,    0,    0,
    0,    0,   30,   31,   30,   31,    0,    0,  118,    0,
    0,  112,    0,  114,  126,  136,  136,    0,    0,   30,
   31,   30,   31,    0,    0,    0,    0,    0,  121,    0,
  123,    0,    0,    0,    0,    0,   30,   31,   30,   31,
    0,   25,    0,    0,    0,    0,  127,    0,    0,    0,
    8,    9,   10,    0,    0,   11,   12,    0,  161,   13,
    9,   10,    0,   14,   11,   12,  177,  161,   13,   86,
   86,   86,   86,   86,   86,   86,   86,   26,   86,    0,
    0,    0,    0,    0,   71,    0,   74,   74,   74,   74,
   74,   74,   74,   74,  161,   74,  234,    0,  166,    0,
  169,  161,  136,  193,  125,    0,  198,    0,   73,   73,
   73,   73,   73,   73,   73,   73,  235,   73,  155,  159,
    9,   10,    0,  161,   11,   12,  224,   78,   13,    0,
    0,    0,  189,  214,  191,  194,  192,  196,  199,    0,
  237,  238,  197,    0,  161,    0,    0,    0,  213,  161,
    0,    0,    0,    0,  245,  161,    0,   20,   20,  117,
  209,  184,    0,    0,    0,  215,    0,  161,    0,    0,
    0,    0,    8,    9,   10,  186,    0,   11,   12,    0,
  225,   13,  227,    0,    0,   14,  231,  190,  233,    0,
    0,    0,    0,    0,    0,    0,  240,  226,  241,    0,
  243,    0,  244,    0,    0,    0,    0,  232,    8,    9,
   10,    0,  248,   11,   12,    0,    0,   13,    9,   10,
  242,   14,   11,   12,   70,    0,   13,    8,    9,   10,
  162,    0,   11,   12,    0,    0,   13,    0,    9,   10,
   14,   77,   11,   12,  210,    0,   13,    8,    9,   10,
    9,   10,   11,   12,   11,   12,   13,  216,   13,  228,
   14,    9,   10,   76,   77,   11,   12,    0,    0,   13,
    9,   10,    0,   77,   11,   12,    9,   10,   13,   77,
   11,   12,    9,   10,   13,   77,   11,   12,    0,    0,
   13,    0,    0,    9,   10,    9,   10,   11,   12,   11,
   12,   13,  129,   13,    0,    0,  159,    9,   10,    9,
   10,   11,   12,   11,   12,   13,    0,   13,  159,    9,
   10,    0,    0,   11,   12,    0,    0,   13,  159,    9,
   10,    0,    0,   11,   12,    0,    0,   13,  159,    9,
   10,  156,    0,   11,   12,    0,    0,   13,    0,  163,
    0,  159,    9,   10,   43,    0,   11,   12,    0,    0,
   13,    0,    9,   10,    0,    0,   11,   12,    0,    0,
   13,    0,    0,    0,    0,    0,    9,   10,  185,    0,
   11,   12,    0,   83,   13,    0,   86,    0,    0,    9,
   10,    9,   10,   11,   12,   11,   12,   13,    0,   13,
    0,    0,    0,  104,    0,  108,  109,    0,  206,    0,
    0,  116,    0,    0,    0,    0,  211,    0,    0,    0,
    0,  217,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  229,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  170,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  104,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         59,
    0,   41,   42,   43,   43,   45,   45,   47,   46,  123,
   40,   43,  256,   45,  259,  260,   59,   59,   41,   59,
   43,   43,   45,   45,  268,  269,   41,   44,   43,   40,
   45,   44,  256,  123,   45,   40,   59,  125,   56,   57,
   45,   40,   40,  256,   61,  269,   42,   45,   61,   67,
   68,   47,  265,  256,  256,   41,  256,   43,   40,   45,
   44,   44,  265,  265,   46,  265,   41,   59,   43,   40,
   45,   59,   41,   59,   43,   46,   45,   61,   44,  264,
   42,  259,  260,  123,   41,   47,   43,   41,   45,   41,
   59,   43,  269,   45,   42,   43,  265,   45,   41,   47,
  123,   44,   43,   40,   45,  265,   41,   59,   43,   41,
   45,   41,   44,   41,   44,   46,   44,   44,   59,   59,
  259,  260,  264,   41,   59,  125,   41,  265,   43,  265,
   45,   41,   59,   59,   61,   59,    2,  123,    4,   59,
  266,  265,  265,   59,   59,   41,   46,   43,   41,   45,
   59,  265,  265,  265,  123,   59,   59,  264,  260,  123,
  265,   59,   41,   59,   43,  123,   45,   59,  256,  257,
  258,  123,   40,  261,  262,   59,   40,  265,   41,   41,
   59,   41,   41,   43,   41,   45,    0,   89,  123,   18,
   40,   41,   44,   10,  175,   45,  256,   44,   33,   59,
   40,   -1,  149,   -1,   -1,   45,   40,   59,  123,   61,
   40,   45,   59,  256,  256,   45,  256,  257,  258,  259,
  260,  261,  262,  263,   40,  265,  256,  123,  266,   45,
  270,  270,  272,  256,  257,  258,  259,  260,  261,  262,
  263,  256,  265,  265,  123,  256,   40,  270,  265,  272,
  272,  256,  265,  264,  265,   -1,  256,  257,  258,  264,
  265,  261,  262,  123,   40,  265,  264,  265,  256,  269,
  256,  257,  258,  259,  260,  261,  262,  263,   40,  265,
   40,  265,  265,   45,  270,   45,  272,  256,  257,  258,
  259,  260,  261,  262,  263,   40,  265,   40,  264,  256,
   45,  270,   45,  272,  256,  257,  258,  259,  260,  261,
  262,  263,   40,  265,   40,  256,  256,   45,  270,   45,
  272,  256,  257,  258,  259,  260,  261,  262,  263,  256,
  265,  125,  256,   -1,  200,  270,  256,  272,  265,  205,
  256,  256,  257,  258,  259,  260,  261,  262,  263,  125,
  265,   -1,  256,  256,   41,  270,   -1,  272,   -1,   -1,
  256,  257,  258,  259,  260,  261,  262,  263,   -1,  265,
   -1,   -1,   59,   -1,  270,   -1,  272,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,  265,   -1,   -1,   59,
   -1,  270,   -1,  272,   -1,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  256,  265,  256,   -1,   -1,  256,
  270,   59,  272,  265,  264,  265,  256,  267,  265,  125,
   -1,  271,  256,   -1,  264,  265,  256,  267,  125,  269,
  264,  265,   -1,  267,  264,  265,  123,  267,   -1,   -1,
  256,   -1,   -1,    5,    6,    7,   -1,   -1,  264,  265,
   -1,  267,   -1,  123,   -1,   -1,   -1,   -1,   -1,   -1,
   33,   -1,  256,  257,  258,   -1,   -1,  261,  262,   42,
   -1,  265,   -1,   35,   -1,  123,   58,   59,   40,   -1,
  256,  257,  258,   -1,   -1,  261,  262,   -1,  125,  265,
   -1,   -1,   74,   75,  256,   -1,  256,   -1,   -1,   -1,
   -1,   -1,  264,  265,  264,  265,   -1,   -1,   70,   -1,
   -1,  256,   -1,  256,   76,   88,   89,   -1,   -1,  264,
  265,  264,  265,   -1,   -1,   -1,   -1,   -1,  256,   -1,
  256,   -1,   -1,   -1,   -1,   -1,  264,  265,  264,  265,
   -1,  125,   -1,   -1,   -1,   -1,   76,   -1,   -1,   -1,
  256,  257,  258,   -1,   -1,  261,  262,   -1,  120,  265,
  257,  258,   -1,  269,  261,  262,  139,  129,  265,  256,
  257,  258,  259,  260,  261,  262,  263,  125,  265,   -1,
   -1,   -1,   -1,   -1,  123,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  156,  265,  125,   -1,  128,   -1,
  130,  163,  175,  165,  123,   -1,  168,   -1,  256,  257,
  258,  259,  260,  261,  262,  263,  125,  265,  125,  256,
  257,  258,   -1,  185,  261,  262,  208,  123,  265,   -1,
   -1,   -1,  162,  195,  164,  165,  123,  167,  168,   -1,
  222,  223,  123,   -1,  206,   -1,   -1,   -1,  123,  211,
   -1,   -1,   -1,   -1,  236,  217,   -1,  219,  220,  123,
  190,  123,   -1,   -1,   -1,  195,   -1,  229,   -1,   -1,
   -1,   -1,  256,  257,  258,  125,   -1,  261,  262,   -1,
  210,  265,  212,   -1,   -1,  269,  216,  125,  218,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  226,  125,  228,   -1,
  230,   -1,  232,   -1,   -1,   -1,   -1,  125,  256,  257,
  258,   -1,  242,  261,  262,   -1,   -1,  265,  257,  258,
  125,  269,  261,  262,  263,   -1,  265,  256,  257,  258,
  125,   -1,  261,  262,   -1,   -1,  265,   -1,  257,  258,
  269,  260,  261,  262,  125,   -1,  265,  256,  257,  258,
  257,  258,  261,  262,  261,  262,  265,  125,  265,  125,
  269,  257,  258,  259,  260,  261,  262,   -1,   -1,  265,
  257,  258,   -1,  260,  261,  262,  257,  258,  265,  260,
  261,  262,  257,  258,  265,  260,  261,  262,   -1,   -1,
  265,   -1,   -1,  257,  258,  257,  258,  261,  262,  261,
  262,  265,   78,  265,   -1,   -1,  256,  257,  258,  257,
  258,  261,  262,  261,  262,  265,   -1,  265,  256,  257,
  258,   -1,   -1,  261,  262,   -1,   -1,  265,  256,  257,
  258,   -1,   -1,  261,  262,   -1,   -1,  265,  256,  257,
  258,  117,   -1,  261,  262,   -1,   -1,  265,   -1,  125,
   -1,  256,  257,  258,   12,   -1,  261,  262,   -1,   -1,
  265,   -1,  257,  258,   -1,   -1,  261,  262,   -1,   -1,
  265,   -1,   -1,   -1,   -1,   -1,  257,  258,  154,   -1,
  261,  262,   -1,   41,  265,   -1,   44,   -1,   -1,  257,
  258,  257,  258,  261,  262,  261,  262,  265,   -1,  265,
   -1,   -1,   -1,   61,   -1,   63,   64,   -1,  184,   -1,
   -1,   69,   -1,   -1,   -1,   -1,  192,   -1,   -1,   -1,
   -1,  197,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  213,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  133,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  149,
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
"factor : expresion_lambda",
};

//#line 206 "Gramatica.y"

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
//#line 655 "Parser.java"
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
//#line 19 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre al programa"); }
break;
case 3:
//#line 20 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '{' al inicio"); }
break;
case 4:
//#line 21 "Gramatica.y"
{ yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
break;
case 7:
//#line 28 "Gramatica.y"
{ reportarEstructura("declaracion de funcion"); }
break;
case 8:
//#line 29 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre a la función"); }
break;
case 12:
//#line 35 "Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 13:
//#line 36 "Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 14:
//#line 37 "Gramatica.y"
{yyerror("Error: Sentencia invalida");}
break;
case 15:
//#line 40 "Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 20:
//#line 49 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 21:
//#line 50 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 22:
//#line 51 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 23:
//#line 52 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 26:
//#line 59 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 30:
//#line 67 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre constantes"); }
break;
case 35:
//#line 75 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 36:
//#line 76 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 37:
//#line 81 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 38:
//#line 82 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 39:
//#line 83 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 40:
//#line 84 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 41:
//#line 85 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 42:
//#line 86 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 43:
//#line 87 "Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 44:
//#line 88 "Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 48:
//#line 92 "Gramatica.y"
{ reportarEstructura("WHILE"); }
break;
case 49:
//#line 93 "Gramatica.y"
{ reportarEstructura("WHILE"); }
break;
case 50:
//#line 94 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 51:
//#line 95 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 52:
//#line 96 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 53:
//#line 98 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 54:
//#line 99 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 55:
//#line 101 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 56:
//#line 102 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 57:
//#line 103 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 58:
//#line 104 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 59:
//#line 105 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 60:
//#line 106 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 61:
//#line 107 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 62:
//#line 108 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 63:
//#line 109 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 64:
//#line 110 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 65:
//#line 111 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 66:
//#line 112 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 67:
//#line 113 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 70:
//#line 120 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 72:
//#line 124 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 73:
//#line 125 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 74:
//#line 126 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 77:
//#line 134 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 78:
//#line 135 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 79:
//#line 136 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 82:
//#line 142 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 84:
//#line 146 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 85:
//#line 147 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 87:
//#line 153 "Gramatica.y"
{ reportarEstructura("asignacion simple"); }
break;
case 88:
//#line 154 "Gramatica.y"
{ reportarEstructura("asignacion simple"); }
break;
case 89:
//#line 157 "Gramatica.y"
{ reportarEstructura("asignacion multiple"); }
break;
case 90:
//#line 161 "Gramatica.y"
{ reportarEstructura("expresion lambda"); }
break;
case 91:
//#line 162 "Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 92:
//#line 163 "Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 93:
//#line 164 "Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 97:
//#line 170 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 98:
//#line 171 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 99:
//#line 172 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 100:
//#line 173 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 101:
//#line 174 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 102:
//#line 175 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 104:
//#line 177 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 105:
//#line 178 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 106:
//#line 179 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 109:
//#line 187 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 110:
//#line 188 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 111:
//#line 189 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 112:
//#line 190 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 113:
//#line 191 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 114:
//#line 192 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 120:
//#line 200 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); }
break;
//#line 1116 "Parser.java"
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
