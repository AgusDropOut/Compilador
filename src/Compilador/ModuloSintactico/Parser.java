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
   29,
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
    2,
};
final static short yydefred[] = {                         0,
    0,    5,    0,    5,    0,    0,    0,    0,   73,    0,
    0,    0,    0,   27,    3,    6,   12,    0,    0,    0,
    0,   50,    0,   48,   49,    0,    0,    2,    1,   15,
    0,    0,  128,    0,    0,    0,    0,    0,    0,  117,
  126,    0,    0,    0,    0,    0,    0,    9,    0,    0,
   13,   10,   14,   11,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  131,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   72,   74,    0,    0,    0,
   65,   77,    0,    0,    0,   55,    0,    0,    0,   25,
    0,    0,   17,    0,   39,    0,    0,    0,   28,    0,
    0,    0,    0,   57,   81,    0,    0,    0,    0,    0,
  124,  120,  125,  122,    0,    0,    0,   89,  129,    0,
    0,    0,   76,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   64,  121,  118,  123,  119,   46,
   47,   24,    0,    0,   22,   20,    0,    0,   38,   36,
    0,   30,    0,   80,    0,   52,    0,   94,    0,  130,
    0,  116,  115,  114,  113,    0,    0,   61,   87,   84,
   86,    0,    0,    0,   43,    0,    0,    0,    5,   18,
   23,   21,   19,   34,   29,   54,    0,   56,   93,   92,
   90,    0,    0,    0,    0,   68,    0,   41,   88,   85,
    0,    0,   63,   71,    0,   62,    0,   51,    0,    0,
    0,    0,    0,    0,    0,    0,   67,    0,    0,    0,
   69,    7,    0,    0,    0,    0,   60,    0,   66,    0,
    0,    0,   70,    0,   42,    0,    0,    0,  103,   58,
   59,    0,   44,   45,    0,  102,  101,   40,  100,
};
final static short yydgoto[] = {                          3,
    5,   16,   17,   18,   91,   19,   20,  130,   50,   93,
   94,   22,   37,  100,   23,   38,  131,   80,   81,   24,
   25,   26,   59,   39,  117,  118,   27,   40,   41,   42,
};
final static short yysindex[] = {                      -109,
 -108,    0,    0,    0,  326,  403,  436,    6,    0,  167,
    3,  171,   64,    0,    0,    0,    0,   53, -195,  -59,
  -46,    0,  -16,    0,    0,  185, -149,    0,    0,    0,
   92, -144,    0,   13,  -29,  161,  -38,  -82,   95,    0,
    0,   56,  151, -128,   54, -123, -218,    0,    0,   -5,
    0,    0,    0,    0,   98, -118, -115,  161,  273,  110,
  171,   76,  224,  236,  238,    0,  202, -113,  171,  171,
 -112,  113,  241,  251,  171,    0,    0,  565,  100,  421,
    0,    0,  253,  255,  116,    0,   87,    0, -107,    0,
   37, -177,    0, -214,    0, -105, -104,  120,    0,  -35,
  121,  493, -143,    0,    0,   54,   58,   56,   58,   56,
    0,    0,    0,    0,   92,  -21,   41,    0,    0,   23,
   26,  127,    0,   58,   56,   58,   56,   54, -233,  -43,
  -63, -233,  581,  111,    0,    0,    0,    0,    0,    0,
    0,    0,   46, -218,    0,    0,  -94, -176,    0,    0,
  -92,    0,  -90,    0,  591,    0,  519,    0, -170,    0,
  171,    0,    0,    0,    0,  502,  452,    0,    0,    0,
    0, -233,  -42,  462,    0,  -75,  530,  -75,    0,    0,
    0,    0,    0,    0,    0,    0,  541,    0,    0,    0,
    0, -143,  214,  593,  129,    0,  471,    0,    0,    0,
  605,  131,    0,    0,  -75,    0,  446,    0,  225,  163,
   51,  -75,  551,  -75,  616,  145,    0,  -75,  563,  -75,
    0,    0,  165,   51,   51,  276,    0,  -75,    0,  -75,
  575,  -75,    0,  -75,    0,   51,  277,  279,    0,    0,
    0,  -75,    0,    0,  289,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    1,    0,    0,    0,  208,    0,    0,    0,    0,    0,
    0,    0,  -13,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -39,    0,    0,    0,  -41,  361,    0,
    0,  -22,    0,    0,   28,    0,    0,    0,  -10,   43,
    0,    0,    0,    0,   -9,    0,    0,    0,    0,  316,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  381,    0,    0,    0,    0,    0,    0,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    2,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   -4,    0,   45,
  477,   47,    0,    0,    0,   57,   15,   32,   49,   66,
    0,    0,    0,    0,   99,    0,    0,    0,    0,    0,
    0,    0,    0,   86,  105,  122,  141,  350,  -41,    0,
    0,  -41,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,  -41,    0,  -41,    0,  -41,    0,  -41,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,    0,  -41,    0,    0,    0,    0,    0,
    0,  -41,    0,  -41,    0,    0,    0,  -41,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,  -41,    0,  -41,
    0,  -41,    0,  -41,    0,    0,    0,    0,    0,    0,
    0,  -41,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    8,    0,    0,    0,    0,  -11,    0,  383,    0,  189,
    0,    0,  484,    0,    0,    0,  702, -100,  457,    0,
    0,    0,    0,   18,    0,  173,    0,    0,  288,   50,
};
final static int YYTABLESIZE=917;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         52,
    5,  127,  127,  127,   73,  127,   74,  127,  153,    6,
   70,    7,   54,    2,    4,  170,  200,   75,  106,  127,
  106,   73,  106,   74,   71,   76,   77,   56,  167,    8,
   31,  174,   71,   37,   35,   92,  106,   89,   96,   32,
   78,  147,   43,   60,   57,   33,   71,   31,   37,   90,
   14,   35,   67,   72,   14,  111,   32,  111,   68,  111,
   48,  172,   33,  163,   30,   73,  165,   74,   73,   49,
   74,  197,  107,  111,  107,  101,  107,  143,  145,  182,
  144,  160,  148,  127,  161,  189,   26,  146,  183,  112,
  107,  112,   47,  112,  190,   32,   73,   83,   74,   64,
  106,   16,   84,   99,   65,   53,  109,  112,  109,   46,
  109,  108,  110,    9,   10,   96,   61,   11,   12,   66,
   32,   13,  125,  127,  109,    5,  108,  141,  108,   73,
  108,   74,   92,   64,   62,   82,   63,  111,   65,   91,
   14,   88,   91,   97,  108,  104,   98,  104,   99,  104,
  105,  119,  122,  123,  107,    1,  140,  142,  132,  149,
  150,  154,  110,  104,  110,  151,  110,  166,  179,  178,
  181,  112,  184,  185,    9,   10,   76,   77,   11,   12,
  110,  105,   13,  105,   77,  105,  207,  214,  109,  220,
   44,   86,  171,    9,   10,   32,   51,   11,   12,  105,
   44,   13,  225,  232,  236,   32,   36,    4,  108,   53,
   44,   32,  169,  199,   75,   32,  127,  127,  127,  127,
  127,  127,  127,  127,   58,  127,   69,  104,  152,   32,
  127,   75,  127,  106,  106,  106,  106,  106,  106,  106,
  106,   44,  106,  158,  110,   37,   32,  106,   55,  106,
  159,   31,   97,  211,   37,   35,    5,    5,    5,   95,
   32,    5,    5,  105,  224,    5,   33,   98,   32,    5,
  111,  111,  111,  111,  111,  111,  111,  111,  162,  111,
   32,  164,   32,   26,  111,   32,  111,  107,  107,  107,
  107,  107,  107,  107,  107,   32,  107,   32,   16,   32,
   99,  107,   53,  107,  112,  112,  112,  112,  112,  112,
  112,  112,   96,  112,   33,   34,  239,  246,  112,  247,
  112,  109,  109,  109,  109,  109,  109,  109,  109,  249,
  109,  107,  180,  191,    0,  109,    0,  109,  210,   33,
   34,  108,  108,  108,  108,  108,  108,  108,  108,  223,
  108,  112,  114,    0,    0,  108,    0,  108,    0,    0,
  104,  104,  104,  104,  104,  104,  104,  104,    0,  104,
  137,  139,    0,    0,  104,    0,  104,  110,  110,  110,
  110,  110,  110,  110,  110,    0,  110,   21,   21,   21,
   95,  110,    0,  110,    0,  103,  105,  105,  105,  105,
  105,  105,  105,  105,    0,  105,   31,    0,   95,    0,
  105,    0,  105,    0,   33,   34,   31,   35,    0,   79,
   79,   85,   31,    0,   33,   34,   31,   35,    0,   14,
   33,   34,    0,   35,   33,   34,    0,   35,   83,   78,
   31,  104,    0,    0,    0,    0,    0,    0,   33,   34,
   15,   35,    0,    0,    0,    0,    0,  115,    0,    0,
    0,    0,  134,    0,    0,   33,   34,    0,   35,  171,
    9,   10,   95,    0,   11,   12,    0,    0,   13,  109,
  171,    9,   10,   79,  156,   11,   12,   33,   34,   13,
    0,  111,    0,  113,    0,   45,  124,    0,  226,   33,
   34,   33,   34,   78,   33,   34,  126,    0,  136,    0,
  138,  237,  238,  173,   33,   34,   33,   34,   33,   34,
    0,    0,    0,  245,    0,    0,   87,   28,    0,    9,
   10,    0,    0,   11,   12,  102,  135,   13,    0,  173,
    0,    0,    0,  133,  106,    0,    0,    0,    0,  195,
  116,    0,  120,  121,    0,    0,  202,    0,  128,  173,
   29,    0,    0,    0,    0,    0,    0,    0,    0,  173,
  222,    0,   83,   83,  194,  173,   83,   83,   83,  216,
   83,    8,    9,   10,  201,  168,   11,   12,  175,   21,
   13,  173,    0,  215,   14,  173,    0,    0,    0,   82,
    0,  173,    0,    0,    0,   95,   95,   95,   95,   95,
   95,   95,   95,  173,   95,  155,   79,   79,   79,   79,
   79,   79,   79,  196,  192,   79,    0,    0,  198,    0,
  203,    0,  204,    0,  206,    0,   78,   78,   78,   78,
   78,   78,   78,  188,  116,   78,    0,    0,    0,    0,
    0,    0,    0,  217,  205,    0,    0,    0,    8,    9,
   10,  221,    0,   11,   12,  208,    0,   13,  227,    0,
  229,   14,    0,    0,  233,  228,  235,    9,   10,    0,
   77,   11,   12,    0,  240,   13,  241,  234,  243,  129,
  244,    8,    9,   10,    0,    0,   11,   12,  248,  242,
   13,    8,    9,   10,   14,  176,   11,   12,    9,   10,
   13,   77,   11,   12,   14,  186,   13,  212,    9,   10,
    0,   77,   11,   12,    0,    0,   13,    9,   10,  218,
   77,   11,   12,   82,   82,   13,    0,   82,   82,   82,
  230,   82,    0,    0,    0,    0,    0,    0,    0,    9,
   10,    0,    0,   11,   12,    0,    0,   13,    9,   10,
    0,    0,   11,   12,    0,    0,   13,    0,    0,    0,
    0,    0,    0,    0,  171,    9,   10,    0,    0,   11,
   12,    0,    0,   13,    0,  171,    9,   10,    0,    0,
   11,   12,    0,    0,   13,    0,  171,    9,   10,    0,
    0,   11,   12,    0,  157,   13,  171,    9,   10,    0,
    0,   11,   12,    0,    0,   13,    0,    0,  171,    9,
   10,    9,   10,   11,   12,   11,   12,   13,    0,   13,
  171,    9,   10,    0,  177,   11,   12,    9,   10,   13,
    0,   11,   12,    0,    0,   13,    0,    9,   10,    9,
   10,   11,   12,   11,   12,   13,  187,   13,    0,    0,
    0,    9,   10,    0,    0,   11,   12,  193,    0,   13,
    0,    0,    9,   10,    0,    0,   11,   12,    0,    0,
   13,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  209,    0,  213,    0,    0,    0,    0,
    0,    0,  219,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  231,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         59,
    0,   41,   42,   43,   43,   45,   45,   47,   44,    2,
   40,    4,   59,  123,  123,   59,   59,   59,   41,   59,
   43,   43,   45,   45,   36,  259,  260,   44,  129,   40,
   44,  132,   44,   44,   44,   47,   59,  256,   44,   44,
  123,  256,   40,   26,   61,   44,   58,   61,   59,  268,
  269,   61,   40,   36,  269,   41,   61,   43,   46,   45,
  256,  125,   61,   41,   59,   43,   41,   45,   43,  265,
   45,  172,   41,   59,   43,   58,   45,   41,  256,  256,
   44,   41,   94,  123,   44,  256,   59,  265,  265,   41,
   59,   43,   40,   45,  265,   45,   43,   42,   45,   42,
  123,   59,   47,   59,   47,   59,   41,   59,   43,   46,
   45,   62,   63,  257,  258,   59,  266,  261,  262,  264,
   45,  265,   73,   74,   59,  125,   41,   41,   43,   43,
   45,   45,  144,   42,   43,   41,   45,  123,   47,   41,
  269,  265,   44,   46,   59,   41,  265,   43,  264,   45,
   41,  265,  265,   41,  123,  265,   41,  265,   59,  265,
  265,   41,   41,   59,   43,   46,   45,   41,  123,   59,
  265,  123,  265,  264,  257,  258,  259,  260,  261,  262,
   59,   41,  265,   43,  260,   45,  179,   59,  123,   59,
   40,   41,  256,  257,  258,   45,  256,  261,  262,   59,
   40,  265,   40,   59,   40,   45,   40,    0,  123,  256,
   40,   45,  256,  256,  256,   45,  256,  257,  258,  259,
  260,  261,  262,  263,   40,  265,  256,  123,  264,   45,
  270,  270,  272,  256,  257,  258,  259,  260,  261,  262,
  263,   40,  265,  265,  123,  256,   45,  270,  265,  272,
  272,  265,  266,   40,  265,  265,  256,  257,  258,  265,
  265,  261,  262,  123,   40,  265,  265,  266,   45,  269,
  256,  257,  258,  259,  260,  261,  262,  263,  256,  265,
   45,  256,   45,  256,  270,   45,  272,  256,  257,  258,
  259,  260,  261,  262,  263,   45,  265,   45,  256,   45,
  256,  270,  256,  272,  256,  257,  258,  259,  260,  261,
  262,  263,  256,  265,  264,  265,   41,   41,  270,   41,
  272,  256,  257,  258,  259,  260,  261,  262,  263,   41,
  265,  256,  144,  161,   -1,  270,   -1,  272,  125,  264,
  265,  256,  257,  258,  259,  260,  261,  262,  263,  125,
  265,   64,   65,   -1,   -1,  270,   -1,  272,   -1,   -1,
  256,  257,  258,  259,  260,  261,  262,  263,   -1,  265,
   83,   84,   -1,   -1,  270,   -1,  272,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,  265,    5,    6,    7,
   41,  270,   -1,  272,   -1,  123,  256,  257,  258,  259,
  260,  261,  262,  263,   -1,  265,  256,   -1,   59,   -1,
  270,   -1,  272,   -1,  264,  265,  256,  267,   -1,   59,
   38,  271,  256,   -1,  264,  265,  256,  267,   -1,  269,
  264,  265,   -1,  267,  264,  265,   -1,  267,  123,   59,
  256,   59,   -1,   -1,   -1,   -1,   -1,   -1,  264,  265,
  125,  267,   -1,   -1,   -1,   -1,   -1,  256,   -1,   -1,
   -1,   -1,   80,   -1,   -1,  264,  265,   -1,  267,  256,
  257,  258,  123,   -1,  261,  262,   -1,   -1,  265,  256,
  256,  257,  258,  123,  102,  261,  262,  264,  265,  265,
   -1,  256,   -1,  256,   -1,   12,  256,   -1,  211,  264,
  265,  264,  265,  123,  264,  265,  256,   -1,  256,   -1,
  256,  224,  225,  131,  264,  265,  264,  265,  264,  265,
   -1,   -1,   -1,  236,   -1,   -1,   43,  125,   -1,  257,
  258,   -1,   -1,  261,  262,  263,   80,  265,   -1,  157,
   -1,   -1,   -1,  123,   61,   -1,   -1,   -1,   -1,  167,
   67,   -1,   69,   70,   -1,   -1,  174,   -1,   75,  177,
  125,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  187,
  125,   -1,  257,  258,  123,  193,  261,  262,  263,  197,
  265,  256,  257,  258,  123,  129,  261,  262,  132,  207,
  265,  209,   -1,  123,  269,  213,   -1,   -1,   -1,  123,
   -1,  219,   -1,   -1,   -1,  256,  257,  258,  259,  260,
  261,  262,  263,  231,  265,  123,  256,  257,  258,  259,
  260,  261,  262,  167,  123,  265,   -1,   -1,  172,   -1,
  174,   -1,  176,   -1,  178,   -1,  256,  257,  258,  259,
  260,  261,  262,  125,  161,  265,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  197,  125,   -1,   -1,   -1,  256,  257,
  258,  205,   -1,  261,  262,  125,   -1,  265,  212,   -1,
  214,  269,   -1,   -1,  218,  125,  220,  257,  258,   -1,
  260,  261,  262,   -1,  228,  265,  230,  125,  232,  125,
  234,  256,  257,  258,   -1,   -1,  261,  262,  242,  125,
  265,  256,  257,  258,  269,  125,  261,  262,  257,  258,
  265,  260,  261,  262,  269,  125,  265,  125,  257,  258,
   -1,  260,  261,  262,   -1,   -1,  265,  257,  258,  125,
  260,  261,  262,  257,  258,  265,   -1,  261,  262,  263,
  125,  265,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,   -1,   -1,  261,  262,   -1,   -1,  265,  257,  258,
   -1,   -1,  261,  262,   -1,   -1,  265,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  256,  257,  258,   -1,   -1,  261,
  262,   -1,   -1,  265,   -1,  256,  257,  258,   -1,   -1,
  261,  262,   -1,   -1,  265,   -1,  256,  257,  258,   -1,
   -1,  261,  262,   -1,  103,  265,  256,  257,  258,   -1,
   -1,  261,  262,   -1,   -1,  265,   -1,   -1,  256,  257,
  258,  257,  258,  261,  262,  261,  262,  265,   -1,  265,
  256,  257,  258,   -1,  133,  261,  262,  257,  258,  265,
   -1,  261,  262,   -1,   -1,  265,   -1,  257,  258,  257,
  258,  261,  262,  261,  262,  265,  155,  265,   -1,   -1,
   -1,  257,  258,   -1,   -1,  261,  262,  166,   -1,  265,
   -1,   -1,  257,  258,   -1,   -1,  261,  262,   -1,   -1,
  265,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  192,   -1,  194,   -1,   -1,   -1,   -1,
   -1,   -1,  201,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  215,
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
"factor : ID '(' parametros_reales ')'",
"factor : '-' CTE",
};

//#line 239 "Gramatica.y"

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
//#line 796 "Parser.java"
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
//#line 74 "Gramatica.y"
{tipo = "ulong";}
break;
case 30:
//#line 79 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre constantes"); }
break;
case 31:
//#line 82 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval);}
break;
case 32:
//#line 83 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval);}
break;
case 33:
//#line 84 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); }
break;
case 34:
//#line 85 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); }
break;
case 35:
//#line 86 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 36:
//#line 87 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 37:
//#line 90 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 38:
//#line 91 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 39:
//#line 92 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 40:
//#line 97 "Gramatica.y"
{ reportarEstructura("IF"); }
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
{ reportarEstructura("PRINT"); }
break;
case 47:
//#line 104 "Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 51:
//#line 108 "Gramatica.y"
{ reportarEstructura("WHILE"); yyval = ArregloTercetos.completarBackPatchingWHILE(); }
break;
case 52:
//#line 109 "Gramatica.y"
{ reportarEstructura("WHILE"); yyval = ArregloTercetos.completarBackPatchingWHILE(); }
break;
case 53:
//#line 110 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");   }
break;
case 54:
//#line 111 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 55:
//#line 112 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 56:
//#line 114 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 57:
//#line 115 "Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 58:
//#line 117 "Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
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
case 72:
//#line 133 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIFDesapilaryCompletar("bl", null,null);}
break;
case 73:
//#line 136 "Gramatica.y"
{ ArregloTercetos.apilarTercetoInicialWHILE(); }
break;
case 74:
//#line 142 "Gramatica.y"
{ArregloTercetos.completarTercetoBackPatchingIF();}
break;
case 75:
//#line 143 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 76:
//#line 146 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 77:
//#line 147 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 78:
//#line 148 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 79:
//#line 149 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 80:
//#line 152 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 81:
//#line 153 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 82:
//#line 154 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 83:
//#line 155 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 86:
//#line 163 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 87:
//#line 164 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 88:
//#line 165 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 91:
//#line 171 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 93:
//#line 175 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 94:
//#line 176 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 95:
//#line 179 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("COMP", val_peek(2).sval, val_peek(0).sval);}
break;
case 96:
//#line 182 "Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                               yyval = ArregloTercetos.crearTerceto(":=", val_peek(2).sval, val_peek(0).sval);}
break;
case 97:
//#line 186 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval);}
break;
case 98:
//#line 187 "Gramatica.y"
{yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(2).sval);}
break;
case 99:
//#line 190 "Gramatica.y"
{ reportarEstructura("asignacion multiple"); }
break;
case 100:
//#line 194 "Gramatica.y"
{ reportarEstructura("expresion lambda"); }
break;
case 101:
//#line 195 "Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 102:
//#line 196 "Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 103:
//#line 197 "Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 104:
//#line 200 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("+", val_peek(2).sval, val_peek(0).sval);}
break;
case 105:
//#line 201 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("-", val_peek(2).sval, val_peek(0).sval);}
break;
case 106:
//#line 202 "Gramatica.y"
{yyval = val_peek(0);}
break;
case 107:
//#line 203 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 108:
//#line 204 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 109:
//#line 205 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 110:
//#line 206 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 111:
//#line 207 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 112:
//#line 208 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 114:
//#line 210 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 115:
//#line 211 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 116:
//#line 212 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 118:
//#line 219 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("*", val_peek(2).sval, val_peek(0).sval); }
break;
case 119:
//#line 220 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("/", val_peek(2).sval, val_peek(0).sval);}
break;
case 120:
//#line 221 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 121:
//#line 222 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 122:
//#line 223 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 123:
//#line 224 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 124:
//#line 225 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 125:
//#line 226 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 126:
//#line 227 "Gramatica.y"
{yyval = val_peek(0);}
break;
case 127:
//#line 230 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); }
break;
case 129:
//#line 232 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); }
break;
case 131:
//#line 234 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); }
break;
//#line 1377 "Parser.java"
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
