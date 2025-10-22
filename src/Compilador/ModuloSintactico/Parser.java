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
   14,    9,    9,    9,    9,    9,    9,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,   17,
   17,   15,   15,   15,   15,   16,   16,   16,   16,   16,
   21,   21,   21,   22,   22,   22,   20,   18,   18,   19,
   23,   23,   23,   23,   13,   13,   13,   13,   13,   13,
   13,   13,   13,   13,   13,   13,   13,   13,   25,   25,
   25,   25,   25,   25,   25,   25,   25,   24,   24,   24,
   24,   24,
};
final static short yylen[] = {                            2,
    4,    3,    3,    2,    0,    2,    7,    2,    2,    2,
    2,    1,    2,    2,    2,    2,    1,    3,    3,    2,
    3,    2,    3,    2,    1,    3,    3,    1,    1,    3,
    2,    1,    3,    3,    5,    2,    4,   10,    6,    8,
    5,    9,    9,    4,    4,    1,    1,    1,    6,    4,
    3,    5,    3,    5,    3,    9,    9,    8,    5,    6,
    6,    4,    3,    8,    7,    6,    7,    8,    6,    1,
    0,    3,    2,    2,    1,    2,    3,    2,    2,    3,
    1,    3,    1,    3,    3,    2,    3,    3,    5,    3,
   10,    9,    9,    8,    3,    3,    1,    3,    3,    3,
    3,    3,    3,    4,    4,    4,    4,    1,    3,    3,
    3,    3,    3,    3,    3,    3,    1,    1,    1,    3,
    4,    2,
};
final static short yydefred[] = {                         0,
    0,    5,    0,    5,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   28,    3,    6,   12,    0,    0,    0,
    0,    0,   48,   46,   47,    2,    1,   15,    0,    0,
  119,    0,    0,    0,    0,    0,    0,  108,  117,    0,
    0,    0,    0,    0,    0,    0,    0,    9,    0,    0,
   13,   10,   14,   11,    0,    0,    0,    0,    0,    0,
    0,  122,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   55,   73,    0,    0,    0,   70,    0,
    0,   63,    0,   53,    0,   27,   26,    0,    0,    0,
   25,    0,    0,   17,    0,    0,    0,    0,   29,    0,
    0,    0,    0,    0,  115,  111,  116,  113,    0,    0,
    0,   81,  120,    0,    0,    0,   72,    0,    0,    0,
    0,    0,    0,   50,    0,    0,  112,  109,  114,  110,
    0,    0,   62,    0,    0,    0,   44,   45,    0,   24,
    0,    0,   22,   20,    0,    0,   34,   37,    0,   31,
    0,   86,    0,  121,    0,  107,  106,  105,  104,    0,
   52,    0,   79,   76,   78,   54,    0,    0,    0,    0,
    0,   59,    0,    0,   41,    0,    5,   18,   23,   21,
   19,   35,   30,   85,   84,   82,    0,    0,   49,   80,
   77,   69,    0,   60,    0,    0,   66,    0,   39,    0,
    0,   61,    0,    0,    0,    0,   67,    0,    0,    0,
    0,    0,   65,    0,    0,    0,    7,    0,    0,    0,
    0,   58,    0,   64,    0,    0,    0,   68,    0,   40,
    0,    0,    0,   94,   56,   57,    0,   42,   43,    0,
   93,   92,   38,   91,
};
final static short yydgoto[] = {                          3,
    5,   16,   17,   18,   92,   19,   20,  125,   22,   94,
   95,   23,   35,  100,   36,  126,   82,   24,   25,   37,
  111,  112,   38,   39,   40,
};
final static short yysindex[] = {                      -109,
 -108,    0,    0,    0,  -82,  329,  386,   -8,  167,  167,
   38,  171,  -37,    0,    0,    0,    0,   42, -229,  -59,
  -46,  -16,    0,    0,    0,    0,    0,    0,   55, -169,
    0,  -15,  -29,  161,  -38,  425,   63,    0,    0,   -1,
  460,  151, -157,   40,  171, -141, -230,    0,   91,  -12,
    0,    0,    0,    0,   98, -118, -115,  197,  236,  238,
  241,    0,  185, -114,  171,  171, -113,  112,  254,  256,
  171,  507, -143,    0,    0,  258,  268,  366,    0,  578,
   95,    0,  116,    0,   87,    0,    0,   43, -107, -105,
    0,   35, -212,    0, -227, -104, -103,  120,    0,    3,
   12,   -1,   12,   -1,    0,    0,    0,    0,   55,  -21,
   69,    0,    0,   23,   26,  117,    0,   12,   -1,   12,
   -1,   43,  587,    0,  -43,  -63,    0,    0,    0,    0,
  589,  109,    0, -125,  520, -120,    0,    0,  171,    0,
   48, -230,    0,    0,  -96, -195,    0,    0,  -95,    0,
  -91,    0, -184,    0,  171,    0,    0,    0,    0,  509,
    0,  533,    0,    0,    0,    0,  -42,  -83,  543,  -83,
  470,    0, -117,  479,    0,   43,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0, -143,  207,    0,    0,
    0,    0,  -83,    0,  599,  119,    0,  489,    0,  601,
  131,    0,  409,  225,  163,   51,    0,  -83,  555,  -83,
  613,  145,    0,  -83,  566,  -83,    0,  165,   51,   51,
  227,    0,  -83,    0,  -83,  576,  -83,    0,  -83,    0,
   51,  257,  289,    0,    0,    0,  -83,    0,    0,  292,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    1,    0,    0,    0,  208,    0,    0,    0,    0,    0,
    0,    0,  -11,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -39,    0,    0,    0,    0,  282,    0,    0,  -22,
  -41,    0,    0,    0,    0,    0,    0,    0,  -10,   28,
    0,    0,    0,    0,   -4,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  317,    0,    0,
    0,   44,    0,    0,    0,    0,    0,  -41,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   61,   -9,    0,
    0,    0,    0,    0,    0,    0,    0,    4,    0,   62,
   15,   32,   49,   66,    0,    0,    0,    0,   92,    0,
    0,    0,    0,    0,    0,    0,    0,   86,  105,  122,
  141,  296,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -41,    0,  -41,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -41,    0,  -41,
  -41,    0,  -41,  -41,    0,   64,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -41,    0,    0,    0,    0,  -41,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -41,    0,  -41,
    0,    0,    0,  -41,    0,  -41,    0,    0,    0,    0,
    0,    0,  -41,    0,  -41,    0,  -41,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,  -41,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    8,    0,    0,    0,    0,   46,    0,  437,  315,  193,
    0,    0,  550,    0,  330,  695,  486,    0,    0,  305,
    0,  198,    0,  398,   47,
};
final static int YYTABLESIZE=906;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         52,
    5,  118,  118,  118,   69,  118,   70,  118,   46,    6,
   66,    7,   54,    2,    4,  164,  191,   71,   97,  118,
   97,   69,   97,   70,   63,   90,   48,   56,  145,    8,
   64,   56,   32,   32,   34,   49,   97,   91,   14,   36,
   76,   14,   15,  143,   57,   77,  151,   33,   32,   32,
   28,   34,  144,   60,   36,  102,   36,  102,   61,  102,
  180,  166,   33,  157,   33,   69,  159,   70,   69,  181,
   70,  184,   98,  102,   98,  141,   98,   42,  142,   67,
  185,   47,   69,  118,   70,   69,   16,   70,   67,  103,
   98,  103,   93,  103,   62,   30,   60,   58,   87,   59,
   97,   61,   51,   75,  102,  104,  100,  103,  100,  154,
  100,   14,  155,    9,   10,  119,  121,   11,   12,   88,
   90,   13,   89,   89,  100,    5,   99,  138,   99,   69,
   99,   70,   83,  171,   79,   83,   96,  102,  174,   79,
  146,  198,   79,   97,   99,   95,   98,   95,   99,   95,
  113,  116,  117,  136,   98,    1,  137,  160,  139,  140,
  147,  148,  101,   95,  101,  149,  101,  170,  179,  182,
  177,  103,  183,    8,    9,   10,   79,  210,   11,   12,
  101,   96,   13,   96,  203,   96,   14,   93,  100,  216,
   43,   84,  165,    9,   10,   30,   51,   11,   12,   96,
   43,   13,  220,  227,  231,   30,   34,    4,   99,   53,
   43,   30,  163,  190,   71,   30,  118,  118,  118,  118,
  118,  118,  118,  118,   43,  118,   65,   95,   45,   30,
  118,   71,  118,   97,   97,   97,   97,   97,   97,   97,
   97,   30,   97,  152,  101,   32,  206,   97,   55,   97,
  153,   36,   55,   32,   32,   34,    5,    5,    5,   33,
   36,    5,    5,   96,  219,    5,  150,  234,   33,    5,
  102,  102,  102,  102,  102,  102,  102,  102,  156,  102,
   30,  158,   30,   16,  102,   30,  102,   98,   98,   98,
   98,   98,   98,   98,   98,   86,   98,  241,   30,   51,
   30,   98,   30,   98,  103,  103,  103,  103,  103,  103,
  103,  103,   30,  103,   31,   32,   88,   90,  103,   89,
  103,  100,  100,  100,  100,  100,  100,  100,  100,  242,
  100,  205,  244,   50,  178,  100,   87,  100,   68,   41,
   75,   99,   99,   99,   99,   99,   99,   99,   99,  218,
   99,    0,  186,    0,   87,   99,    0,   99,    0,    0,
   95,   95,   95,   95,   95,   95,   95,   95,    0,   95,
    0,    0,    0,    0,   95,   74,   95,  101,  101,  101,
  101,  101,  101,  101,  101,    0,  101,    0,    0,    0,
    0,  101,    0,  101,    0,    0,   96,   96,   96,   96,
   96,   96,   96,   96,   75,   96,   29,    0,    0,    0,
   96,    0,   96,    0,   31,   32,   29,   33,   87,    0,
    0,   83,   29,    0,   31,   32,   29,   33,    0,   14,
   31,   32,    0,   33,   31,   32,    0,   33,    0,   74,
  109,   21,   21,   21,    0,    0,    0,    0,   31,   32,
    0,   33,  101,   26,    0,    0,    0,  106,  108,    0,
   31,   32,  165,    9,   10,    0,    0,   11,   12,    0,
    0,   13,   74,  128,  130,    0,    0,   81,    0,    0,
  165,    9,   10,    0,    0,   11,   12,    0,  131,   13,
    0,  103,    0,  105,    0,    0,  107,    0,    0,   31,
   32,   31,   32,    0,   31,   32,    0,    0,  124,  118,
   27,  120,    0,  127,  132,    0,    0,   31,   32,   31,
   32,   31,   32,  129,    0,    0,    0,    0,    0,    0,
    0,   31,   32,  217,    0,    0,    0,   75,   75,   75,
   75,   75,   75,   75,   75,    0,   75,   73,    0,    0,
    0,   87,   87,   87,   87,   87,   87,   87,   87,    0,
   87,   44,  167,  133,    0,    0,    0,    0,    0,    0,
    0,  167,   74,   74,   74,   74,   74,   74,   74,   74,
    0,   74,   80,    0,    8,    9,   10,    0,    0,   11,
   12,   85,  195,   13,   88,    0,    0,   14,  167,    0,
    0,  200,    0,  221,    0,  167,    0,  196,    0,    0,
  201,  211,  110,    0,  114,  115,  232,  233,    0,  172,
  122,  175,    9,   10,  167,   79,   11,   12,  240,  123,
   13,  187,    0,    0,  212,    0,    0,    0,    0,   21,
  167,    8,    9,   10,  173,  167,   11,   12,    0,    0,
   13,  167,    0,  192,   14,  194,  197,  189,  199,  202,
    0,    0,  167,    0,    8,    9,   10,  193,    0,   11,
   12,    0,    0,   13,    0,    0,    0,   14,  207,  223,
    0,    9,   10,  213,    0,   11,   12,   72,  176,   13,
  229,    0,    0,  222,    0,  224,    0,    0,    0,  228,
  237,  230,  134,    0,  110,    0,    0,    0,  235,    0,
  236,  161,  238,  168,  239,    0,    9,   10,   78,   79,
   11,   12,  243,  208,   13,  214,    9,   10,    0,   79,
   11,   12,    0,    0,   13,    9,   10,  225,   79,   11,
   12,    0,    0,   13,    0,    9,   10,    0,   79,   11,
   12,    0,    0,   13,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    9,   10,    9,   10,   11,   12,   11,
   12,   13,    0,   13,  135,  165,    9,   10,    0,    0,
   11,   12,    0,    0,   13,    0,    0,    0,  165,    9,
   10,    0,    0,   11,   12,    0,    0,   13,  165,    9,
   10,    0,    0,   11,   12,    0,    0,   13,    0,    0,
  165,    9,   10,    0,    0,   11,   12,  162,    0,   13,
    0,  165,    9,   10,    0,  169,   11,   12,    0,    0,
   13,  165,    9,   10,    9,   10,   11,   12,   11,   12,
   13,    0,   13,    9,   10,    9,   10,   11,   12,   11,
   12,   13,    0,   13,  188,    9,   10,    9,   10,   11,
   12,   11,   12,   13,    0,   13,    0,    0,    0,    9,
   10,    0,    0,   11,   12,    0,    0,   13,    0,    0,
    0,  204,    0,    0,    0,    0,    0,    0,    0,  209,
    0,    0,    0,    0,  215,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  226,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         59,
    0,   41,   42,   43,   43,   45,   45,   47,   46,    2,
   40,    4,   59,  123,  123,   59,   59,   59,   41,   59,
   43,   43,   45,   45,   40,  256,  256,   44,  256,   40,
   46,   44,   44,   44,   44,  265,   59,  268,  269,   44,
   42,  269,  125,  256,   61,   47,   44,   44,   59,   61,
   59,   61,  265,   42,   59,   41,   61,   43,   47,   45,
  256,  125,   59,   41,   61,   43,   41,   45,   43,  265,
   45,  256,   41,   59,   43,   41,   45,   40,   44,   34,
  265,   40,   43,  123,   45,   43,   59,   45,   43,   41,
   59,   43,   47,   45,  264,   45,   42,   43,   59,   45,
  123,   47,   59,   41,   58,   59,   41,   59,   43,   41,
   45,  269,   44,  257,  258,   69,   70,  261,  262,   59,
   59,  265,   59,  265,   59,  125,   41,   41,   43,   43,
   45,   45,   41,  259,  260,   44,   46,  123,  259,  260,
   95,  259,  260,   46,   59,   41,  265,   43,  264,   45,
  265,  265,   41,   59,  123,  265,   41,   41,  266,  265,
  265,  265,   41,   59,   43,   46,   45,   59,  265,  265,
  123,  123,  264,  256,  257,  258,  260,   59,  261,  262,
   59,   41,  265,   43,  177,   45,  269,  142,  123,   59,
   40,   41,  256,  257,  258,   45,  256,  261,  262,   59,
   40,  265,   40,   59,   40,   45,   40,    0,  123,  256,
   40,   45,  256,  256,  256,   45,  256,  257,  258,  259,
  260,  261,  262,  263,   40,  265,  256,  123,  266,   45,
  270,  270,  272,  256,  257,  258,  259,  260,  261,  262,
  263,   45,  265,  265,  123,  256,   40,  270,  265,  272,
  272,  256,  265,  265,  265,  265,  256,  257,  258,  256,
  265,  261,  262,  123,   40,  265,  264,   41,  265,  269,
  256,  257,  258,  259,  260,  261,  262,  263,  256,  265,
   45,  256,   45,  256,  270,   45,  272,  256,  257,  258,
  259,  260,  261,  262,  263,  256,  265,   41,   45,  256,
   45,  270,   45,  272,  256,  257,  258,  259,  260,  261,
  262,  263,   45,  265,  264,  265,  256,  256,  270,  256,
  272,  256,  257,  258,  259,  260,  261,  262,  263,   41,
  265,  125,   41,   19,  142,  270,   41,  272,   34,   10,
   59,  256,  257,  258,  259,  260,  261,  262,  263,  125,
  265,   -1,  155,   -1,   59,  270,   -1,  272,   -1,   -1,
  256,  257,  258,  259,  260,  261,  262,  263,   -1,  265,
   -1,   -1,   -1,   -1,  270,   59,  272,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,  265,   -1,   -1,   -1,
   -1,  270,   -1,  272,   -1,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  123,  265,  256,   -1,   -1,   -1,
  270,   -1,  272,   -1,  264,  265,  256,  267,  123,   -1,
   -1,  271,  256,   -1,  264,  265,  256,  267,   -1,  269,
  264,  265,   -1,  267,  264,  265,   -1,  267,   -1,  123,
  256,    5,    6,    7,   -1,   -1,   -1,   -1,  264,  265,
   -1,  267,  256,  125,   -1,   -1,   -1,   60,   61,   -1,
  264,  265,  256,  257,  258,   -1,   -1,  261,  262,   -1,
   -1,  265,   36,   76,   77,   -1,   -1,   41,   -1,   -1,
  256,  257,  258,   -1,   -1,  261,  262,   -1,  123,  265,
   -1,  256,   -1,  256,   -1,   -1,  256,   -1,   -1,  264,
  265,  264,  265,   -1,  264,  265,   -1,   -1,   72,  256,
  125,  256,   -1,  256,   78,   -1,   -1,  264,  265,  264,
  265,  264,  265,  256,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  264,  265,  125,   -1,   -1,   -1,  256,  257,  258,
  259,  260,  261,  262,  263,   -1,  265,  123,   -1,   -1,
   -1,  256,  257,  258,  259,  260,  261,  262,  263,   -1,
  265,   12,  126,   78,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  135,  256,  257,  258,  259,  260,  261,  262,  263,
   -1,  265,  123,   -1,  256,  257,  258,   -1,   -1,  261,
  262,   42,  123,  265,   45,   -1,   -1,  269,  162,   -1,
   -1,  123,   -1,  206,   -1,  169,   -1,  171,   -1,   -1,
  174,  123,   63,   -1,   65,   66,  219,  220,   -1,  134,
   71,  136,  257,  258,  188,  260,  261,  262,  231,  123,
  265,  123,   -1,   -1,  198,   -1,   -1,   -1,   -1,  203,
  204,  256,  257,  258,  125,  209,  261,  262,   -1,   -1,
  265,  215,   -1,  168,  269,  170,  171,  125,  173,  174,
   -1,   -1,  226,   -1,  256,  257,  258,  125,   -1,  261,
  262,   -1,   -1,  265,   -1,   -1,   -1,  269,  193,  125,
   -1,  257,  258,  198,   -1,  261,  262,  263,  139,  265,
  125,   -1,   -1,  208,   -1,  210,   -1,   -1,   -1,  214,
  125,  216,  125,   -1,  155,   -1,   -1,   -1,  223,   -1,
  225,  125,  227,  125,  229,   -1,  257,  258,  259,  260,
  261,  262,  237,  125,  265,  125,  257,  258,   -1,  260,
  261,  262,   -1,   -1,  265,  257,  258,  125,  260,  261,
  262,   -1,   -1,  265,   -1,  257,  258,   -1,  260,  261,
  262,   -1,   -1,  265,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  257,  258,  257,  258,  261,  262,  261,
  262,  265,   -1,  265,   80,  256,  257,  258,   -1,   -1,
  261,  262,   -1,   -1,  265,   -1,   -1,   -1,  256,  257,
  258,   -1,   -1,  261,  262,   -1,   -1,  265,  256,  257,
  258,   -1,   -1,  261,  262,   -1,   -1,  265,   -1,   -1,
  256,  257,  258,   -1,   -1,  261,  262,  123,   -1,  265,
   -1,  256,  257,  258,   -1,  131,  261,  262,   -1,   -1,
  265,  256,  257,  258,  257,  258,  261,  262,  261,  262,
  265,   -1,  265,  257,  258,  257,  258,  261,  262,  261,
  262,  265,   -1,  265,  160,  257,  258,  257,  258,  261,
  262,  261,  262,  265,   -1,  265,   -1,   -1,   -1,  257,
  258,   -1,   -1,  261,  262,   -1,   -1,  265,   -1,   -1,
   -1,  187,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  195,
   -1,   -1,   -1,   -1,  200,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  211,
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

//#line 212 ".\Gramatica.y"

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
        yyerror("Error: Redeclaracion de variable " + token);
        nuevoParserVal.sval = token;
    }
    return nuevoParserVal;
}

public ParserVal chequearAmbito(String prefijo, String ambitoReal, String nombreIdentificador) {
    ElementoTablaDeSimbolos elem = null;
    String claveBuscada = null;
    String ambitoDeBusqueda =  ambito; //copia de la variable global ambito
    boolean simboloEncontrado = false;
    String ambitoActual = "";
    ParserVal val = new ParserVal();
    val.sval = nombreIdentificador; //Si todo sale mal el parsel val se queda con un valor mal pero por lo menos no se rompe la compilacion
  if(!prefijo.isEmpty()){
    while (!simboloEncontrado && !ambitoDeBusqueda.isEmpty()){
         int pos = ambitoDeBusqueda.lastIndexOf(":");
         System.out.println("ambito: " + ambitoDeBusqueda);
           if (pos == -1) {
              ambitoActual = ambitoDeBusqueda;
          } else {
              ambitoActual = ambitoDeBusqueda.substring(pos + 1);
          }
         if(ambitoActual.equals(prefijo)){
             claveBuscada = nombreIdentificador + ":" + ambitoDeBusqueda;
             elem = TablaDeSimbolos.getSimbolo(claveBuscada);
             simboloEncontrado = true;
             if(elem == null){
                 yyerror("El símbolo '" + nombreIdentificador +
                    "' no se encuentra en el ámbito del prefijo '" + prefijo + "'.");
             } else {
                 val.sval = claveBuscada;
             }
         }
        ambitoDeBusqueda = ambitoDeBusqueda.substring(0, pos);
    }
  }else {
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
//#line 712 "Parser.java"
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
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 33:
//#line 76 ".\Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 34:
//#line 77 ".\Gramatica.y"
{}
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
//#line 85 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 39:
//#line 86 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 40:
//#line 87 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 41:
//#line 88 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 42:
//#line 89 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 43:
//#line 90 ".\Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 44:
//#line 91 ".\Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 45:
//#line 92 ".\Gramatica.y"
{ reportarEstructura("PRINT"); }
break;
case 49:
//#line 96 ".\Gramatica.y"
{ reportarEstructura("WHILE"); }
break;
case 50:
//#line 97 ".\Gramatica.y"
{ reportarEstructura("WHILE"); }
break;
case 51:
//#line 98 ".\Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 52:
//#line 99 ".\Gramatica.y"
{ yyerror("Error: falta cuerpo del WHILE");  }
break;
case 53:
//#line 100 ".\Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 54:
//#line 102 ".\Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 55:
//#line 103 ".\Gramatica.y"
{ yyerror("Error: falta palabra reservada DO");  }
break;
case 56:
//#line 105 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 57:
//#line 106 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 58:
//#line 107 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 59:
//#line 108 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 60:
//#line 109 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 61:
//#line 110 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 62:
//#line 111 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 63:
//#line 112 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 64:
//#line 113 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 65:
//#line 114 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 66:
//#line 115 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 67:
//#line 116 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 68:
//#line 117 ".\Gramatica.y"
{yyerror("Error: Falta contenido en bloque then/else");}
break;
case 71:
//#line 124 ".\Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 73:
//#line 128 ".\Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion");}
break;
case 74:
//#line 129 ".\Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion");}
break;
case 75:
//#line 130 ".\Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion");}
break;
case 78:
//#line 138 ".\Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 79:
//#line 139 ".\Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 80:
//#line 140 ".\Gramatica.y"
{ yyerror("Sentencia no reconocida, se esperaba ';'"); }
break;
case 83:
//#line 146 ".\Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 85:
//#line 150 ".\Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 86:
//#line 151 ".\Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 88:
//#line 157 ".\Gramatica.y"
{ reportarEstructura("asignacion simple");
                                            yyval = chequearAmbito("", ambito, val_peek(2).sval);}
break;
case 89:
//#line 159 ".\Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                   yyval = chequearAmbito(val_peek(4).sval, ambito, val_peek(2).sval); }
break;
case 90:
//#line 163 ".\Gramatica.y"
{ reportarEstructura("asignacion multiple"); }
break;
case 91:
//#line 167 ".\Gramatica.y"
{ reportarEstructura("expresion lambda"); }
break;
case 92:
//#line 168 ".\Gramatica.y"
{ yyerror("Error: falta '{' en la expresion lambda"); }
break;
case 93:
//#line 169 ".\Gramatica.y"
{ yyerror("Error: falta '}' en la expresion lambda"); }
break;
case 94:
//#line 170 ".\Gramatica.y"
{ yyerror("Error: falta '{' y '}' en la expresion lambda"); }
break;
case 98:
//#line 176 ".\Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 99:
//#line 177 ".\Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 100:
//#line 178 ".\Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 101:
//#line 179 ".\Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 102:
//#line 180 ".\Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 103:
//#line 181 ".\Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 105:
//#line 183 ".\Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 106:
//#line 184 ".\Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 107:
//#line 185 ".\Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 111:
//#line 194 ".\Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 112:
//#line 195 ".\Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 113:
//#line 196 ".\Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 114:
//#line 197 ".\Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 115:
//#line 198 ".\Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 116:
//#line 199 ".\Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 122:
//#line 207 ".\Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); }
break;
//#line 1200 "Parser.java"
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
