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






//#line 2 ".\Gramatica.y"
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
    0,    0,    0,    0,    1,    1,    3,    4,    4,    2,
    2,    2,    2,    2,    2,    7,    5,    5,   10,   10,
   10,   10,   10,   10,   11,   12,   12,    6,   14,   14,
   14,   15,   15,   15,   15,   15,   15,    9,    9,    9,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,   18,   18,   16,   16,   16,   16,   17,   17,
   17,   17,   17,   22,   22,   22,   23,   23,   23,   21,
   19,   19,   20,   24,   24,   24,   24,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   26,   26,   26,   26,   26,   26,   26,   26,   26,
   25,   25,   25,   25,   25,
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
    3,    5,    3,   10,    9,    9,    8,    3,    3,    1,
    3,    3,    3,    3,    3,    3,    4,    4,    4,    4,
    1,    3,    3,    3,    3,    3,    3,    3,    3,    1,
    1,    1,    3,    4,    2,
};
final static short yydefred[] = {                         0,
    0,    5,    0,    5,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   28,    3,    6,   12,    0,    0,    0,
    0,   51,    0,   49,   50,    2,    1,   15,    0,    0,
  122,    0,    0,    0,    0,    0,    0,  111,  120,    0,
    0,    0,    0,    0,    0,    0,    0,    9,    0,    0,
   13,   10,   14,   11,    0,    0,    0,    0,    0,    0,
    0,  125,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   58,   76,    0,    0,    0,   73,    0,
    0,   66,    0,   56,    0,   27,   26,    0,    0,    0,
   25,    0,    0,   17,    0,   40,    0,    0,    0,   29,
    0,    0,    0,    0,    0,  118,  114,  119,  116,    0,
    0,    0,   84,  123,    0,    0,    0,   75,    0,    0,
    0,    0,    0,    0,   53,    0,    0,  115,  112,  117,
  113,    0,    0,   65,    0,    0,    0,   47,   48,    0,
   24,    0,    0,   22,   20,    0,    0,   39,   37,    0,
   31,    0,   89,    0,  124,    0,  110,  109,  108,  107,
    0,   55,    0,   82,   79,   81,   57,    0,    0,    0,
    0,    0,   62,    0,    0,   44,    0,    5,   18,   23,
   21,   19,   35,   30,   88,   87,   85,    0,    0,   52,
   83,   80,   72,    0,   63,    0,    0,   69,    0,   42,
    0,    0,   64,    0,    0,    0,    0,   70,    0,    0,
    0,    0,    0,   68,    0,    0,    0,    7,    0,    0,
    0,    0,   61,    0,   67,    0,    0,    0,   71,    0,
   43,    0,    0,    0,   97,   59,   60,    0,   45,   46,
    0,   96,   95,   41,   94,
};
final static short yydgoto[] = {                          3,
    5,   16,   17,   18,   92,   19,   20,  126,   50,   94,
   95,   22,   35,  101,   23,   36,  127,   82,   24,   25,
   37,  112,  113,   38,   39,   40,
};
final static short yysindex[] = {                      -113,
 -108,    0,    0,    0,  -82,  268,  359,  -34,  167,  167,
   11,  171,  -37,    0,    0,    0,    0,   21, -229,  -59,
  -46,    0,  -16,    0,    0,    0,    0,    0,   55, -223,
    0,   19,  -29,  161,  -38,  440,   29,    0,    0,   34,
  413,  151, -190,   40,  171, -170, -230,    0,    0,  -12,
    0,    0,    0,    0,   60, -148, -154,  197,  215,  236,
  238,    0,  185, -144,  171,  171, -137,   93,  241,  254,
  171,  478, -143,    0,    0,  256,  258,  449,    0,  270,
   77,    0,  103,    0,   37,    0,    0,   94, -115, -118,
    0,   45, -212,    0, -227,    0, -116, -112,  108,    0,
    3,   46,   34,   46,   34,    0,    0,    0,    0,   55,
  -21,   89,    0,    0,   23,   26,  115,    0,   46,   34,
   46,   34,   94,  425,    0,  -43,  -63,    0,    0,    0,
    0,  564,   98,    0, -155,  341, -119,    0,    0,  171,
    0,   35, -230,    0,    0, -106, -210,    0,    0, -105,
    0, -103,    0, -202,    0,  171,    0,    0,    0,    0,
  499,    0,  509,    0,    0,    0,    0,  -42,  -98,  520,
  -98,  455,    0, -117,  461,    0,   94,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -143,  207,    0,
    0,    0,    0,  -98,    0,  566,  107,    0,  472,    0,
  575,  109,    0,  423,  225,  129,   51,    0,  -98,  530,
  -98,  599,  111,    0,  -98,  541,  -98,    0,  131,   51,
   51,  132,    0,  -98,    0,  -98,  551,  -98,    0,  -98,
    0,   51,  136,  137,    0,    0,    0,  -98,    0,    0,
  144,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    1,    0,    0,    0,  190,    0,    0,    0,    0,    0,
    0,    0,  -13,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -39,    0,    0,    0,    0,  282,    0,    0,  -22,
  -41,    0,    0,    0,    0,    0,    0,    0,  -10,   13,
    0,    0,    0,    0,  -11,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  331,    0,    0,
    0,   28,    0,    0,    0,    0,    0,  -41,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   44,   -9,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   -4,    0,
   57,   15,   32,   49,   66,    0,    0,    0,    0,   91,
    0,    0,    0,    0,    0,    0,    0,    0,   86,  105,
  122,  141,  296,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -41,    0,  -41,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
  -41,  -41,    0,  -41,  -41,    0,   61,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -41,    0,    0,    0,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
  -41,    0,    0,    0,  -41,    0,  -41,    0,    0,    0,
    0,    0,    0,  -41,    0,  -41,    0,  -41,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,  -41,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   10,    0,    0,    0,    0,  715,    0,  437,    0,   62,
    0,    0,  504,    0,    0,  193,  710,  439,    0,    0,
  170,    0,   52,    0,  398,   54,
};
final static int YYTABLESIZE=922;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         52,
    5,  121,  121,  121,   69,  121,   70,  121,   46,    2,
   66,    6,   54,    7,    4,  165,  192,   74,  100,  121,
  100,   69,  100,   70,   28,   90,   48,   56,  146,    8,
   32,   97,   36,   38,   34,   49,  100,   91,   14,   33,
   62,   14,   15,  144,   57,  181,  152,   32,   38,   36,
   42,   34,  145,  185,  182,  105,   33,  105,   63,  105,
   47,  167,  186,  158,   64,   69,  160,   70,   69,   75,
   70,   16,  101,  105,  101,   76,  101,  139,   14,   69,
   77,   70,   69,  121,   70,  142,   54,   60,  143,  106,
  101,  106,   61,  106,   89,   30,   60,   58,   87,   59,
  100,   61,   91,  172,   79,   98,  103,  106,  103,  100,
  103,  103,  105,    9,   10,   93,   99,   11,   12,   92,
  114,   13,  120,  122,  103,    5,  102,  117,  102,  155,
  102,   86,  156,  118,   86,  137,   69,  105,   70,  175,
   79,  199,   79,  138,  102,   98,  141,   98,  148,   98,
  140,    1,  149,  150,  101,  161,  171,  178,  180,  183,
  184,   79,  104,   98,  104,  211,  104,  217,  221,  228,
  232,  106,  235,    8,    9,   10,  242,  243,   11,   12,
  104,   99,   13,   99,  245,   99,   14,  204,  103,    4,
   43,   84,  166,    9,   10,   30,   51,   11,   12,   99,
   43,   13,   41,   68,  179,   30,   34,  187,  102,   53,
   43,   30,  164,  191,   74,   30,  121,  121,  121,  121,
  121,  121,  121,  121,   43,  121,   65,   98,   45,   30,
  121,   71,  121,  100,  100,  100,  100,  100,  100,  100,
  100,   30,  100,  153,  104,   38,  207,  100,   55,  100,
  154,   32,   96,   36,   38,   34,    5,    5,    5,   30,
   33,    5,    5,   99,  220,    5,  151,    0,   16,    5,
  105,  105,  105,  105,  105,  105,  105,  105,  157,  105,
   30,  159,   30,   54,  105,   30,  105,  101,  101,  101,
  101,  101,  101,  101,  101,   86,  101,    0,   30,   91,
   30,  101,   30,  101,  106,  106,  106,  106,  106,  106,
  106,  106,   93,  106,   31,   32,   92,    0,  106,    0,
  106,  103,  103,  103,  103,  103,  103,  103,  103,    0,
  103,  206,    0,    0,    0,  103,   90,  103,    0,    0,
   78,  102,  102,  102,  102,  102,  102,  102,  102,  219,
  102,    0,    0,    0,   90,  102,    0,  102,    0,    0,
   98,   98,   98,   98,   98,   98,   98,   98,    0,   98,
    0,    0,    0,    0,   98,    0,   98,  104,  104,  104,
  104,  104,  104,  104,  104,    0,  104,    0,    0,   77,
    0,  104,   26,  104,  135,    0,   99,   99,   99,   99,
   99,   99,   99,   99,   78,   99,   29,    0,    0,    0,
   99,    0,   99,    0,   31,   32,   29,   33,   90,    0,
    0,   83,   29,    0,   31,   32,   29,   33,    0,   14,
   31,   32,    0,   33,   31,   32,    0,   33,    0,    0,
  110,   21,   21,   21,    0,    0,    0,    0,   31,   32,
    0,   33,  102,   77,    0,    0,    0,  107,  109,    0,
   31,   32,  166,    9,   10,  174,    0,   11,   12,    0,
  104,   13,   74,  129,  131,    0,    0,   81,   31,   32,
  166,    9,   10,   27,    0,   11,   12,    0,    0,   13,
    0,  106,    0,  108,    0,    0,  119,    0,    0,   31,
   32,   31,   32,    0,   31,   32,    0,    0,  125,  121,
    0,  128,    0,  130,  133,   44,  134,   31,   32,   31,
   32,   31,   32,    8,    9,   10,    9,   10,   11,   12,
   11,   12,   13,    0,   13,   80,   14,   78,   78,   78,
   78,   78,   78,   78,   78,   85,   78,  218,   88,  162,
    0,   90,   90,   90,   90,   90,   90,   90,   90,    0,
   90,    0,   73,  168,    0,    0,  111,    0,  115,  116,
    0,  132,  168,  173,  123,  176,    0,  196,    0,    0,
    0,    0,    0,  201,    0,    0,   77,   77,   77,   77,
   77,   77,   77,   77,  212,   77,  166,    9,   10,  168,
  124,   11,   12,    0,  222,   13,  168,  193,  197,  195,
  198,  202,  200,  203,    8,    9,   10,  233,  234,   11,
   12,  188,    0,   13,    0,  168,    0,   14,    0,  241,
    0,    0,  208,  190,    0,  213,    0,  214,    0,    0,
   21,  168,    0,  177,  194,    0,  168,  223,    0,  225,
    0,    0,  168,  229,  224,  231,    0,    0,    0,  111,
    0,    0,  236,  168,  237,  230,  239,    0,  240,    9,
   10,   78,   79,   11,   12,  238,  244,   13,    8,    9,
   10,    9,   10,   11,   12,   11,   12,   13,  169,   13,
  209,   14,    0,    0,    0,    0,    9,   10,    0,  215,
   11,   12,   72,    0,   13,    9,   10,    0,   79,   11,
   12,    9,   10,   13,   79,   11,   12,    9,   10,   13,
   79,   11,   12,  226,    0,   13,    0,    0,    9,   10,
    0,   79,   11,   12,    9,   10,   13,    0,   11,   12,
    0,    0,   13,    0,    0,    0,    0,    0,   67,    0,
    0,    0,    0,    0,    0,    9,   10,   67,    0,   11,
   12,   93,    0,   13,  166,    9,   10,    0,    0,   11,
   12,    0,    0,   13,    0,  166,    9,   10,    0,    0,
   11,   12,    0,    0,   13,  166,    9,   10,    0,  136,
   11,   12,    0,    0,   13,    0,  166,    9,   10,    0,
    0,   11,   12,    0,    0,   13,  166,    9,   10,  147,
    0,   11,   12,    0,    0,   13,    0,    0,    0,    0,
    9,   10,    9,   10,   11,   12,   11,   12,   13,    0,
   13,    9,   10,  163,    0,   11,   12,    0,    0,   13,
    0,  170,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    9,   10,   93,    0,   11,
   12,    0,    0,   13,    0,    0,    0,    0,    0,    0,
  189,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  205,    0,    0,
    0,    0,    0,    0,    0,  210,    0,    0,    0,    0,
  216,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  227,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         59,
    0,   41,   42,   43,   43,   45,   45,   47,   46,  123,
   40,    2,   59,    4,  123,   59,   59,   59,   41,   59,
   43,   43,   45,   45,   59,  256,  256,   44,  256,   40,
   44,   44,   44,   44,   44,  265,   59,  268,  269,   44,
  264,  269,  125,  256,   61,  256,   44,   61,   59,   61,
   40,   61,  265,  256,  265,   41,   61,   43,   40,   45,
   40,  125,  265,   41,   46,   43,   41,   45,   43,   41,
   45,   59,   41,   59,   43,   42,   45,   41,  269,   43,
   47,   45,   43,  123,   45,   41,   59,   42,   44,   41,
   59,   43,   47,   45,  265,   45,   42,   43,   59,   45,
  123,   47,   59,  259,  260,   46,   41,   59,   43,  264,
   45,   58,   59,  257,  258,   59,  265,  261,  262,   59,
  265,  265,   69,   70,   59,  125,   41,  265,   43,   41,
   45,   41,   44,   41,   44,   59,   43,  123,   45,  259,
  260,  259,  260,   41,   59,   41,  265,   43,  265,   45,
  266,  265,  265,   46,  123,   41,   59,  123,  265,  265,
  264,  260,   41,   59,   43,   59,   45,   59,   40,   59,
   40,  123,   41,  256,  257,  258,   41,   41,  261,  262,
   59,   41,  265,   43,   41,   45,  269,  178,  123,    0,
   40,   41,  256,  257,  258,   45,  256,  261,  262,   59,
   40,  265,   10,   34,  143,   45,   40,  156,  123,  256,
   40,   45,  256,  256,  256,   45,  256,  257,  258,  259,
  260,  261,  262,  263,   40,  265,  256,  123,  266,   45,
  270,  270,  272,  256,  257,  258,  259,  260,  261,  262,
  263,   45,  265,  265,  123,  256,   40,  270,  265,  272,
  272,  265,  265,  265,  265,  265,  256,  257,  258,   45,
  265,  261,  262,  123,   40,  265,  264,   -1,  256,  269,
  256,  257,  258,  259,  260,  261,  262,  263,  256,  265,
   45,  256,   45,  256,  270,   45,  272,  256,  257,  258,
  259,  260,  261,  262,  263,  256,  265,   -1,   45,  256,
   45,  270,   45,  272,  256,  257,  258,  259,  260,  261,
  262,  263,  256,  265,  264,  265,  256,   -1,  270,   -1,
  272,  256,  257,  258,  259,  260,  261,  262,  263,   -1,
  265,  125,   -1,   -1,   -1,  270,   41,  272,   -1,   -1,
   59,  256,  257,  258,  259,  260,  261,  262,  263,  125,
  265,   -1,   -1,   -1,   59,  270,   -1,  272,   -1,   -1,
  256,  257,  258,  259,  260,  261,  262,  263,   -1,  265,
   -1,   -1,   -1,   -1,  270,   -1,  272,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,  265,   -1,   -1,   59,
   -1,  270,  125,  272,  125,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  123,  265,  256,   -1,   -1,   -1,
  270,   -1,  272,   -1,  264,  265,  256,  267,  123,   -1,
   -1,  271,  256,   -1,  264,  265,  256,  267,   -1,  269,
  264,  265,   -1,  267,  264,  265,   -1,  267,   -1,   -1,
  256,    5,    6,    7,   -1,   -1,   -1,   -1,  264,  265,
   -1,  267,  256,  123,   -1,   -1,   -1,   60,   61,   -1,
  264,  265,  256,  257,  258,  125,   -1,  261,  262,   -1,
  256,  265,   36,   76,   77,   -1,   -1,   41,  264,  265,
  256,  257,  258,  125,   -1,  261,  262,   -1,   -1,  265,
   -1,  256,   -1,  256,   -1,   -1,  256,   -1,   -1,  264,
  265,  264,  265,   -1,  264,  265,   -1,   -1,   72,  256,
   -1,  256,   -1,  256,   78,   12,   78,  264,  265,  264,
  265,  264,  265,  256,  257,  258,  257,  258,  261,  262,
  261,  262,  265,   -1,  265,  123,  269,  256,  257,  258,
  259,  260,  261,  262,  263,   42,  265,  125,   45,  125,
   -1,  256,  257,  258,  259,  260,  261,  262,  263,   -1,
  265,   -1,  123,  127,   -1,   -1,   63,   -1,   65,   66,
   -1,  123,  136,  135,   71,  137,   -1,  123,   -1,   -1,
   -1,   -1,   -1,  123,   -1,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  123,  265,  256,  257,  258,  163,
  123,  261,  262,   -1,  207,  265,  170,  169,  172,  171,
  172,  175,  174,  175,  256,  257,  258,  220,  221,  261,
  262,  123,   -1,  265,   -1,  189,   -1,  269,   -1,  232,
   -1,   -1,  194,  125,   -1,  199,   -1,  199,   -1,   -1,
  204,  205,   -1,  140,  125,   -1,  210,  209,   -1,  211,
   -1,   -1,  216,  215,  125,  217,   -1,   -1,   -1,  156,
   -1,   -1,  224,  227,  226,  125,  228,   -1,  230,  257,
  258,  259,  260,  261,  262,  125,  238,  265,  256,  257,
  258,  257,  258,  261,  262,  261,  262,  265,  125,  265,
  125,  269,   -1,   -1,   -1,   -1,  257,  258,   -1,  125,
  261,  262,  263,   -1,  265,  257,  258,   -1,  260,  261,
  262,  257,  258,  265,  260,  261,  262,  257,  258,  265,
  260,  261,  262,  125,   -1,  265,   -1,   -1,  257,  258,
   -1,  260,  261,  262,  257,  258,  265,   -1,  261,  262,
   -1,   -1,  265,   -1,   -1,   -1,   -1,   -1,   34,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,   43,   -1,  261,
  262,   47,   -1,  265,  256,  257,  258,   -1,   -1,  261,
  262,   -1,   -1,  265,   -1,  256,  257,  258,   -1,   -1,
  261,  262,   -1,   -1,  265,  256,  257,  258,   -1,   80,
  261,  262,   -1,   -1,  265,   -1,  256,  257,  258,   -1,
   -1,  261,  262,   -1,   -1,  265,  256,  257,  258,   95,
   -1,  261,  262,   -1,   -1,  265,   -1,   -1,   -1,   -1,
  257,  258,  257,  258,  261,  262,  261,  262,  265,   -1,
  265,  257,  258,  124,   -1,  261,  262,   -1,   -1,  265,
   -1,  132,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,  143,   -1,  261,
  262,   -1,   -1,  265,   -1,   -1,   -1,   -1,   -1,   -1,
  161,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  188,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  196,   -1,   -1,   -1,   -1,
  201,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  212,
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
"asignacion_simple : ID ASIG expresion",
"asignacion_simple : ID '.' ID ASIG expresion",
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

//#line 217 ".\Gramatica.y"

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
//#line 731 "Parser.java"
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
//#line 18 ".\Gramatica.y"
{ }
break;
case 2:
//#line 19 ".\Gramatica.y"
{ yyerror("Error: Falta definir un nombre al programa"); }
break;
case 3:
//#line 20 ".\Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '{' al inicio"); }
break;
case 4:
//#line 21 ".\Gramatica.y"
{ yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
break;
case 7:
//#line 28 ".\Gramatica.y"
{ reportarEstructura("declaracion de funcion");
                                                                                            salirAmbito();}
break;
case 8:
//#line 32 ".\Gramatica.y"
{entrarAmbito(val_peek(0).sval); System.out.println(ambito);}
break;
case 9:
//#line 33 ".\Gramatica.y"
{ yyerror("Error: Falta definir un nombre a la función"); }
break;
case 13:
//#line 40 ".\Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 14:
//#line 41 ".\Gramatica.y"
{ yyerror("Error: Sentencia no reconocida, se esperaba ';'"); }
break;
case 15:
//#line 42 ".\Gramatica.y"
{yyerror("Error: Sentencia invalida");}
break;
case 16:
//#line 45 ".\Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 21:
//#line 54 ".\Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 22:
//#line 55 ".\Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 23:
//#line 56 ".\Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 24:
//#line 57 ".\Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 27:
//#line 64 ".\Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 28:
//#line 67 ".\Gramatica.y"
{tipo = "ulong";}
break;
case 31:
//#line 72 ".\Gramatica.y"
{ yyerror("Error: se esperaba ',' entre constantes"); }
break;
case 32:
//#line 75 ".\Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval);}
break;
case 33:
//#line 76 ".\Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval);}
break;
case 34:
//#line 77 ".\Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); }
break;
case 35:
//#line 78 ".\Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); }
break;
case 36:
//#line 79 ".\Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 37:
//#line 80 ".\Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 38:
//#line 83 ".\Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 39:
//#line 84 ".\Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 40:
//#line 85 ".\Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 41:
//#line 90 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 42:
//#line 91 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 43:
//#line 92 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 44:
//#line 93 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 45:
//#line 94 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 46:
//#line 95 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 47:
//#line 96 ".\Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 48:
//#line 97 ".\Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 52:
//#line 101 ".\Gramatica.y"
{ reportarEstructura("WHILE"); }
break;
case 53:
//#line 102 ".\Gramatica.y"
{ reportarEstructura("WHILE"); }
break;
case 54:
//#line 103 ".\Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 55:
//#line 104 ".\Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 56:
//#line 105 ".\Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 57:
//#line 107 ".\Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 58:
//#line 108 ".\Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 59:
//#line 110 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 60:
//#line 111 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 61:
//#line 112 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 62:
//#line 113 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 63:
//#line 114 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 64:
//#line 115 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 65:
//#line 116 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 66:
//#line 117 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 67:
//#line 118 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 68:
//#line 119 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 69:
//#line 120 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 70:
//#line 121 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 71:
//#line 122 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 74:
//#line 129 ".\Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 76:
//#line 133 ".\Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 77:
//#line 134 ".\Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 78:
//#line 135 ".\Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 81:
//#line 143 ".\Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 82:
//#line 144 ".\Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 83:
//#line 145 ".\Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 86:
//#line 151 ".\Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 88:
//#line 155 ".\Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 89:
//#line 156 ".\Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 91:
//#line 162 ".\Gramatica.y"
{ reportarEstructura("asignacion simple");
                                            yyval = chequearAmbito("", ambito, val_peek(2).sval);}
break;
case 92:
//#line 164 ".\Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                   yyval = chequearAmbito(val_peek(4).sval, ambito, val_peek(2).sval); }
break;
case 93:
//#line 168 ".\Gramatica.y"
{ reportarEstructura("asignacion multiple"); }
break;
case 94:
//#line 172 ".\Gramatica.y"
{ reportarEstructura("expresion lambda"); }
break;
case 95:
//#line 173 ".\Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 96:
//#line 174 ".\Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 97:
//#line 175 ".\Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 101:
//#line 181 ".\Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 102:
//#line 182 ".\Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 103:
//#line 183 ".\Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 104:
//#line 184 ".\Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 105:
//#line 185 ".\Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 106:
//#line 186 ".\Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 108:
//#line 188 ".\Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 109:
//#line 189 ".\Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 110:
//#line 190 ".\Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 114:
//#line 199 ".\Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 115:
//#line 200 ".\Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 116:
//#line 201 ".\Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 117:
//#line 202 ".\Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 118:
//#line 203 ".\Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 119:
//#line 204 ".\Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 121:
//#line 208 ".\Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); }
break;
case 123:
//#line 210 ".\Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); }
break;
case 125:
//#line 212 ".\Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); }
break;
//#line 1243 "Parser.java"
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
