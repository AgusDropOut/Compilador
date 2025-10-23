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
    8,   18,   19,   19,   16,   16,   16,   16,   22,   22,
   22,   22,   17,   17,   17,   17,   17,   24,   24,   24,
   25,   25,   25,   23,   20,   26,   26,   21,   27,   27,
   27,   27,   13,   13,   13,   13,   13,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   29,   29,   29,   29,
   29,   29,   29,   29,   29,   28,   28,   28,   28,   28,
};
final static short yylen[] = {                            2,
    4,    3,    3,    2,    0,    2,    7,    2,    2,    2,
    2,    1,    2,    2,    2,    2,    1,    3,    3,    2,
    3,    2,    3,    2,    1,    2,    1,    1,    3,    2,
    1,    3,    3,    5,    2,    4,    1,    3,    2,   10,
    6,    8,    5,    9,    9,    4,    4,    1,    1,    1,
    6,    4,    3,    5,    3,    5,    3,    9,    9,    8,
    5,    6,    6,    4,    3,    8,    7,    6,    7,    8,
    6,    1,    1,    0,    3,    2,    2,    1,    3,    2,
    2,    1,    2,    3,    2,    2,    3,    1,    3,    1,
    3,    3,    2,    3,    3,    1,    3,    3,   10,    9,
    9,    8,    3,    3,    1,    3,    3,    3,    3,    3,
    3,    4,    4,    4,    4,    1,    3,    3,    3,    3,
    3,    3,    3,    3,    1,    1,    1,    3,    4,    2,
};
final static short yydefred[] = {                         0,
    0,    5,    0,    5,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   27,    3,    6,   12,    0,    0,    0,
    0,   50,    0,   48,   49,    0,    2,    1,   15,    0,
    0,  127,    0,    0,    0,    0,    0,    0,  116,  125,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    9,
    0,    0,   13,   10,   14,   11,    0,    0,    0,    0,
    0,    0,    0,    0,  130,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   57,   80,    0,    0,
    0,   72,   73,    0,    0,    0,   65,   76,    0,   55,
    0,    0,    0,   25,    0,    0,   17,    0,   39,    0,
    0,    0,   28,    0,    0,    0,    0,    0,    0,  123,
  119,  124,  121,    0,    0,    0,   88,  128,    0,    0,
    0,   79,    0,    0,    0,    0,    0,    0,   52,    0,
    0,  120,  117,  122,  118,   75,    0,    0,    0,    0,
    0,   64,   46,   47,   24,    0,    0,   22,   20,    0,
    0,   38,   36,    0,   30,    0,   93,    0,  129,    0,
  115,  114,  113,  112,    0,   54,    0,   86,   83,   85,
   56,    0,    0,   61,    0,    0,   43,    0,    0,    0,
    5,   18,   23,   21,   19,   34,   29,   92,   91,   89,
    0,    0,   51,   87,   84,    0,    0,   68,    0,   41,
    0,    0,   63,   71,    0,   62,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   67,    0,    0,    0,   69,
    7,    0,    0,    0,    0,   60,    0,   66,    0,    0,
    0,   70,    0,   42,    0,    0,    0,  102,   58,   59,
    0,   44,   45,    0,  101,  100,   40,   99,
};
final static short yydgoto[] = {                          3,
    5,   16,   17,   18,   95,   19,   20,  130,   52,   97,
   98,   22,   36,  104,   23,   43,  131,   86,   87,   24,
   25,   37,   38,  116,  117,   26,   39,   40,   41,
};
final static short yysindex[] = {                      -113,
 -108,    0,    0,    0,  -82,  268,  387,  -33,  167,  171,
    7,  185,    5,    0,    0,    0,    0,   25, -229,  -59,
  -46,    0,  -16,    0,    0, -211,    0,    0,    0,   90,
 -205,    0,   39,  -29,  161,  -38,  458,   71,    0,    0,
   34,  161,  452,   83,  151, -183,   35, -148, -186,    0,
    0,   -5,    0,    0,    0,    0,   82, -135, -130,  185,
   76,  236,  238,  241,    0,  202, -129,  185,  185, -123,
  103,  251,  253,  185,  540, -143,    0,    0,  255,  258,
  106,    0,    0,  270,   92,  474,    0,    0,  108,    0,
   52,    0, -112,    0,   69, -224,    0, -231,    0, -111,
 -109,  111,    0,  -35,   35,   56,   34,   56,   34,    0,
    0,    0,    0,   90,  -21,   79,    0,    0,   23,   26,
  117,    0,   56,   34,   56,   34,   35,  424,    0,  -43,
  -63,    0,    0,    0,    0,    0, -154,  552, -154,  582,
  100,    0,    0,    0,    0,   37, -186,    0,    0, -104,
 -223,    0,    0, -103,    0,  -98,    0, -212,    0,  185,
    0,    0,    0,    0,  542,    0,  562,    0,    0,    0,
    0,  -42,  480,    0, -154,  486,    0,  -92,  573,  -92,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -143,  214,    0,    0,    0,  635,  110,    0,  497,    0,
  641,  112,    0,    0,  -92,    0,  439,  225,  130,   51,
  -92,  597,  -92,  647,  114,    0,  -92,  608,  -92,    0,
    0,  138,   51,   51,  136,    0,  -92,    0,  -92,  624,
  -92,    0,  -92,    0,   51,  144,  147,    0,    0,    0,
  -92,    0,    0,  149,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    1,    0,    0,    0,  203,    0,    0,    0,    0,    0,
    0,    0,  -13,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -39,    0,    0,    0,    0,  517,    0,    0,
  -22,    0,  -41,  331,    0,    0,   13,    0,    0,    0,
  -10,   28,    0,    0,    0,    0,   -9,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  527,    0,    0,    0,   43,    0,    0,    0,    0,    0,
  370,    0,    0,    0,    0,  -41,    0,    0,    0,    0,
    0,    2,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   -4,    0,   45,   57,   15,   32,   49,   66,    0,
    0,    0,    0,   99,    0,    0,    0,    0,    0,    0,
    0,    0,   86,  105,  122,  141,  296,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -41,    0,  -41,  -41,    0,  -41,    0,  -41,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
    0,    0,    0,    0,  -41,    0,    0,    0,    0,    0,
  -41,    0,  -41,    0,    0,    0,  -41,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,  -41,    0,
  -41,    0,  -41,    0,    0,    0,    0,    0,    0,    0,
  -41,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
  137,    0,    0,    0,    0,  825,    0,  403,    0,   58,
    0,    0,  593,    0,    0,    0,  748, -125,  461,    0,
    0,    0,   19,    0,   44,    0,    0,  384,   27,
};
final static int YYTABLESIZE=972;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
    5,  126,  126,  126,   72,  126,   73,  126,  156,    2,
   69,  173,   56,  176,    4,  169,  195,   74,  105,  126,
  105,   72,  105,   73,  150,   29,   50,   58,   44,    8,
   31,  148,  184,   37,   35,   51,  105,   14,  100,   32,
  149,  185,   15,  188,   59,   33,   45,   31,   37,  199,
   48,   35,  189,   71,   60,  110,   32,  110,   65,  110,
   81,  171,   33,  162,   49,   72,  164,   73,   72,   93,
   73,   26,  106,  110,  106,   79,  106,   72,   66,   73,
   80,   94,   14,  126,   67,   14,   16,  107,  109,  111,
  106,  111,  144,  111,   72,   31,   73,   63,  124,  126,
  105,   53,   64,   98,   82,   83,  108,  111,  108,  146,
  108,   78,  147,    9,   10,   95,   92,   11,   12,  159,
   31,   13,  160,   88,  108,    5,  107,  101,  107,  102,
  107,   63,   61,  103,   62,  118,   64,  110,    6,   90,
    7,  121,   90,  122,  107,  103,  136,  103,  143,  103,
  139,    1,  145,  152,  106,  153,  154,  165,  180,  181,
  183,  186,  109,  103,  109,  187,  109,   83,  213,  224,
  219,  111,  231,    8,    9,   10,  238,  235,   11,   12,
  109,  104,   13,  104,  245,  104,   14,  246,  108,  248,
   46,   90,  170,    9,   10,   31,   53,   11,   12,  104,
   46,   13,    4,  190,  182,   31,   35,    0,  107,   55,
   42,   31,  168,  194,   74,   31,  126,  126,  126,  126,
  126,  126,  126,  126,   46,  126,   68,  103,  155,   31,
  126,   74,  126,  105,  105,  105,  105,  105,  105,  105,
  105,   46,  105,  157,  109,   37,   31,  105,   57,  105,
  158,   31,   96,  210,   37,   35,    5,    5,    5,   99,
   32,    5,    5,  104,  223,    5,   33,   97,   26,    5,
  110,  110,  110,  110,  110,  110,  110,  110,  161,  110,
   31,  163,   31,   16,  110,   31,  110,  106,  106,  106,
  106,  106,  106,  106,  106,   31,  106,   31,   53,   31,
   98,  106,   31,  106,  111,  111,  111,  111,  111,  111,
  111,  111,   95,  111,   32,   33,    0,  207,  111,    0,
  111,  108,  108,  108,  108,  108,  108,  108,  108,    0,
  108,  106,    0,    0,    0,  108,   94,  108,  209,   32,
   33,  107,  107,  107,  107,  107,  107,  107,  107,  222,
  107,    0,    0,    0,   94,  107,    0,  107,    0,    0,
  103,  103,  103,  103,  103,  103,  103,  103,    0,  103,
    0,    0,    0,    0,  103,    0,  103,  109,  109,  109,
  109,  109,  109,  109,  109,    0,  109,    0,    0,   78,
    0,  109,   27,  109,  137,    0,  104,  104,  104,  104,
  104,  104,  104,  104,    0,  104,   30,   21,   21,   21,
  104,    0,  104,    0,   32,   33,   30,   34,   94,    0,
    0,   89,   30,    0,   32,   33,   30,   34,   77,   14,
   32,   33,    0,   34,   32,   33,    0,   34,    0,   77,
   30,    0,    0,    0,    0,   85,  111,  113,   32,   33,
    0,   34,    0,   78,    0,    0,    0,  114,    0,    0,
    0,    0,  133,  135,    0,   32,   33,    0,   34,  170,
    9,   10,    0,    0,   11,   12,    0,  129,   13,    0,
  170,    9,   10,    0,    0,   11,   12,    0,  141,   13,
    0,  108,   77,  110,    0,    0,  112,    0,    0,   32,
   33,   32,   33,    0,   32,   33,  123,    0,  125,    0,
  132,   28,    0,  134,   32,   33,   32,   33,   32,   33,
    0,   32,   33,    8,    9,   10,    9,   10,   11,   12,
   11,   12,   13,  172,   13,    0,   14,    0,    0,    0,
  172,    0,    0,    0,    0,    0,  142,    0,  166,    0,
    0,   94,   94,   94,   94,   94,   94,   94,   94,    0,
   94,    0,    0,  221,    0,    0,    0,    0,    0,  172,
    0,    0,    0,    0,   84,  197,    0,    0,  202,    0,
   76,  172,    0,    0,    0,    0,   78,   78,   78,   78,
   78,   78,   78,  225,  172,   78,  140,  174,    0,  177,
    0,  215,  196,    0,   47,    0,  236,  237,  201,   21,
  172,    0,    0,    0,  172,    0,    0,    0,  244,  214,
  172,    0,    0,    0,    0,   77,   77,   77,   77,   77,
   77,   77,  172,  198,   77,  200,  203,   91,  204,   82,
  206,    0,    8,    9,   10,    0,    0,   11,   12,   81,
    0,   13,  105,    0,    0,   14,    0,    0,  115,  216,
  119,  120,  128,    0,  191,  220,  127,    0,    0,    0,
    0,  226,    0,  228,    0,    0,  175,  232,    0,  234,
    9,   10,    0,    0,   11,   12,  193,  239,   13,  240,
    0,  242,    0,  243,    8,    9,   10,  205,    0,   11,
   12,  247,    0,   13,    0,    0,  178,   14,    9,   10,
   82,   83,   11,   12,    9,   10,   13,    0,   11,   12,
   75,  227,   13,    0,    0,    0,    0,    0,    0,    0,
    9,   10,  233,   83,   11,   12,    9,   10,   13,   83,
   11,   12,    9,   10,   13,   83,   11,   12,  241,    0,
   13,    0,  115,    9,   10,    0,   83,   11,   12,  211,
    0,   13,    0,    0,    0,  217,    0,    0,    0,    0,
    0,  229,    0,   82,   82,    0,    0,   82,   82,   82,
    0,   82,    0,   81,   81,    0,    0,   81,   81,   81,
    0,   81,    0,    0,    0,    0,    9,   10,    9,   10,
   11,   12,   11,   12,   13,    0,   13,  170,    9,   10,
    0,    0,   11,   12,    0,    0,   13,  170,    9,   10,
    0,    0,   11,   12,    0,    0,   13,    0,  170,    9,
   10,  138,    0,   11,   12,    0,    0,   13,    9,   10,
    0,    0,   11,   12,    0,    0,   13,    0,    0,    0,
    0,    0,  170,    9,   10,    0,    0,   11,   12,   70,
    0,   13,    0,  170,    9,   10,   70,    0,   11,   12,
   70,    0,   13,   96,    0,  167,    0,    0,    0,  170,
    9,   10,    0,    0,   11,   12,    0,  179,   13,    0,
    0,    9,   10,    0,    0,   11,   12,    9,   10,   13,
    0,   11,   12,    9,   10,   13,    0,   11,   12,    0,
    0,   13,  192,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  151,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  208,    0,
    0,    0,    0,  212,    0,    0,    0,    0,  218,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  230,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   96,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         59,
    0,   41,   42,   43,   43,   45,   45,   47,   44,  123,
   40,  137,   59,  139,  123,   59,   59,   59,   41,   59,
   43,   43,   45,   45,  256,   59,  256,   44,   10,   40,
   44,  256,  256,   44,   44,  265,   59,  269,   44,   44,
  265,  265,  125,  256,   61,   44,   40,   61,   59,  175,
   46,   61,  265,   35,  266,   41,   61,   43,  264,   45,
   42,  125,   61,   41,   40,   43,   41,   45,   43,  256,
   45,   59,   41,   59,   43,   42,   45,   43,   40,   45,
   47,  268,  269,  123,   46,  269,   59,   61,   62,   41,
   59,   43,   41,   45,   43,   45,   45,   42,   72,   73,
  123,   59,   47,   59,  259,  260,   41,   59,   43,   41,
   45,   41,   44,  257,  258,   59,  265,  261,  262,   41,
   45,  265,   44,   41,   59,  125,   41,   46,   43,  265,
   45,   42,   43,  264,   45,  265,   47,  123,    2,   41,
    4,  265,   44,   41,   59,   41,   41,   43,   41,   45,
   59,  265,  265,  265,  123,  265,   46,   41,   59,  123,
  265,  265,   41,   59,   43,  264,   45,  260,   59,   40,
   59,  123,   59,  256,  257,  258,   41,   40,  261,  262,
   59,   41,  265,   43,   41,   45,  269,   41,  123,   41,
   40,   41,  256,  257,  258,   45,  256,  261,  262,   59,
   40,  265,    0,  160,  147,   45,   40,   -1,  123,  256,
   40,   45,  256,  256,  256,   45,  256,  257,  258,  259,
  260,  261,  262,  263,   40,  265,  256,  123,  264,   45,
  270,  270,  272,  256,  257,  258,  259,  260,  261,  262,
  263,   40,  265,  265,  123,  256,   45,  270,  265,  272,
  272,  265,  266,   40,  265,  265,  256,  257,  258,  265,
  265,  261,  262,  123,   40,  265,  265,  266,  256,  269,
  256,  257,  258,  259,  260,  261,  262,  263,  256,  265,
   45,  256,   45,  256,  270,   45,  272,  256,  257,  258,
  259,  260,  261,  262,  263,   45,  265,   45,  256,   45,
  256,  270,   45,  272,  256,  257,  258,  259,  260,  261,
  262,  263,  256,  265,  264,  265,   -1,  181,  270,   -1,
  272,  256,  257,  258,  259,  260,  261,  262,  263,   -1,
  265,  256,   -1,   -1,   -1,  270,   41,  272,  125,  264,
  265,  256,  257,  258,  259,  260,  261,  262,  263,  125,
  265,   -1,   -1,   -1,   59,  270,   -1,  272,   -1,   -1,
  256,  257,  258,  259,  260,  261,  262,  263,   -1,  265,
   -1,   -1,   -1,   -1,  270,   -1,  272,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,  265,   -1,   -1,   59,
   -1,  270,  125,  272,  125,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,   -1,  265,  256,    5,    6,    7,
  270,   -1,  272,   -1,  264,  265,  256,  267,  123,   -1,
   -1,  271,  256,   -1,  264,  265,  256,  267,   59,  269,
  264,  265,   -1,  267,  264,  265,   -1,  267,   -1,   37,
  256,   -1,   -1,   -1,   -1,   43,   63,   64,  264,  265,
   -1,  267,   -1,  123,   -1,   -1,   -1,  256,   -1,   -1,
   -1,   -1,   79,   80,   -1,  264,  265,   -1,  267,  256,
  257,  258,   -1,   -1,  261,  262,   -1,   75,  265,   -1,
  256,  257,  258,   -1,   -1,  261,  262,   -1,   86,  265,
   -1,  256,  123,  256,   -1,   -1,  256,   -1,   -1,  264,
  265,  264,  265,   -1,  264,  265,  256,   -1,  256,   -1,
  256,  125,   -1,  256,  264,  265,  264,  265,  264,  265,
   -1,  264,  265,  256,  257,  258,  257,  258,  261,  262,
  261,  262,  265,  131,  265,   -1,  269,   -1,   -1,   -1,
  138,   -1,   -1,   -1,   -1,   -1,   86,   -1,  125,   -1,
   -1,  256,  257,  258,  259,  260,  261,  262,  263,   -1,
  265,   -1,   -1,  125,   -1,   -1,   -1,   -1,   -1,  167,
   -1,   -1,   -1,   -1,  123,  173,   -1,   -1,  176,   -1,
  123,  179,   -1,   -1,   -1,   -1,  256,  257,  258,  259,
  260,  261,  262,  210,  192,  265,  123,  137,   -1,  139,
   -1,  199,  123,   -1,   12,   -1,  223,  224,  123,  207,
  208,   -1,   -1,   -1,  212,   -1,   -1,   -1,  235,  123,
  218,   -1,   -1,   -1,   -1,  256,  257,  258,  259,  260,
  261,  262,  230,  173,  265,  175,  176,   45,  178,  123,
  180,   -1,  256,  257,  258,   -1,   -1,  261,  262,  123,
   -1,  265,   60,   -1,   -1,  269,   -1,   -1,   66,  199,
   68,   69,  123,   -1,  123,  205,   74,   -1,   -1,   -1,
   -1,  211,   -1,  213,   -1,   -1,  125,  217,   -1,  219,
  257,  258,   -1,   -1,  261,  262,  125,  227,  265,  229,
   -1,  231,   -1,  233,  256,  257,  258,  125,   -1,  261,
  262,  241,   -1,  265,   -1,   -1,  125,  269,  257,  258,
  259,  260,  261,  262,  257,  258,  265,   -1,  261,  262,
  263,  125,  265,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  257,  258,  125,  260,  261,  262,  257,  258,  265,  260,
  261,  262,  257,  258,  265,  260,  261,  262,  125,   -1,
  265,   -1,  160,  257,  258,   -1,  260,  261,  262,  125,
   -1,  265,   -1,   -1,   -1,  125,   -1,   -1,   -1,   -1,
   -1,  125,   -1,  257,  258,   -1,   -1,  261,  262,  263,
   -1,  265,   -1,  257,  258,   -1,   -1,  261,  262,  263,
   -1,  265,   -1,   -1,   -1,   -1,  257,  258,  257,  258,
  261,  262,  261,  262,  265,   -1,  265,  256,  257,  258,
   -1,   -1,  261,  262,   -1,   -1,  265,  256,  257,  258,
   -1,   -1,  261,  262,   -1,   -1,  265,   -1,  256,  257,
  258,   84,   -1,  261,  262,   -1,   -1,  265,  257,  258,
   -1,   -1,  261,  262,   -1,   -1,  265,   -1,   -1,   -1,
   -1,   -1,  256,  257,  258,   -1,   -1,  261,  262,   35,
   -1,  265,   -1,  256,  257,  258,   42,   -1,  261,  262,
   46,   -1,  265,   49,   -1,  128,   -1,   -1,   -1,  256,
  257,  258,   -1,   -1,  261,  262,   -1,  140,  265,   -1,
   -1,  257,  258,   -1,   -1,  261,  262,  257,  258,  265,
   -1,  261,  262,  257,  258,  265,   -1,  261,  262,   -1,
   -1,  265,  165,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   98,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  191,   -1,
   -1,   -1,   -1,  196,   -1,   -1,   -1,   -1,  201,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  214,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  147,
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
"sentencia_ejecutable : WHILE condicion_while DO '{' bloque_ejecutable '}'",
"sentencia_ejecutable : WHILE condicion_while DO sentencia_ejecutable",
"sentencia_ejecutable : WHILE condicion_while DO",
"sentencia_ejecutable : WHILE condicion_while DO '{' '}'",
"sentencia_ejecutable : PRINT '(' ')'",
"sentencia_ejecutable : WHILE condicion_while '{' bloque_ejecutable '}'",
"sentencia_ejecutable : WHILE condicion_while sentencia_ejecutable",
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

//#line 236 "Gramatica.y"

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
//#line 805 "Parser.java"
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
{ reportarEstructura("WHILE"); }
break;
case 52:
//#line 109 "Gramatica.y"
{ reportarEstructura("WHILE"); }
break;
case 53:
//#line 110 "Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
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
//#line 139 "Gramatica.y"
{ArregloTercetos.completarTercetoBackPatchingIF();}
break;
case 74:
//#line 140 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 75:
//#line 143 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 76:
//#line 144 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 77:
//#line 145 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 78:
//#line 146 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 80:
//#line 150 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 81:
//#line 151 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 82:
//#line 152 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 85:
//#line 160 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 86:
//#line 161 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 87:
//#line 162 "Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 90:
//#line 168 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 92:
//#line 172 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 93:
//#line 173 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 94:
//#line 176 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("COMP", val_peek(2).sval, val_peek(0).sval);}
break;
case 95:
//#line 179 "Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                               yyval = ArregloTercetos.crearTerceto(":=", val_peek(2).sval, val_peek(0).sval);}
break;
case 96:
//#line 183 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval);}
break;
case 97:
//#line 184 "Gramatica.y"
{yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(2).sval);}
break;
case 98:
//#line 187 "Gramatica.y"
{ reportarEstructura("asignacion multiple"); }
break;
case 99:
//#line 191 "Gramatica.y"
{ reportarEstructura("expresion lambda"); }
break;
case 100:
//#line 192 "Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 101:
//#line 193 "Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 102:
//#line 194 "Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 103:
//#line 197 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("+", val_peek(2).sval, val_peek(0).sval);}
break;
case 104:
//#line 198 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("-", val_peek(2).sval, val_peek(0).sval);}
break;
case 105:
//#line 199 "Gramatica.y"
{yyval = val_peek(0);}
break;
case 106:
//#line 200 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 107:
//#line 201 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 108:
//#line 202 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 109:
//#line 203 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 110:
//#line 204 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 111:
//#line 205 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 113:
//#line 207 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 114:
//#line 208 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 115:
//#line 209 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 117:
//#line 216 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("*", val_peek(2).sval, val_peek(0).sval); }
break;
case 118:
//#line 217 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto("/", val_peek(2).sval, val_peek(0).sval);}
break;
case 119:
//#line 218 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 120:
//#line 219 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 121:
//#line 220 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 122:
//#line 221 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 123:
//#line 222 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 124:
//#line 223 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 125:
//#line 224 "Gramatica.y"
{yyval = val_peek(0);}
break;
case 126:
//#line 227 "Gramatica.y"
{yyval = chequearAmbito("", ambito, val_peek(0).sval); }
break;
case 128:
//#line 229 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); }
break;
case 130:
//#line 231 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); }
break;
//#line 1378 "Parser.java"
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
