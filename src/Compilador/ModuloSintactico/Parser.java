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
    2,    2,    7,    7,    7,    7,    7,    9,    9,   11,
   11,    8,    5,    5,   13,   13,   13,   14,   14,   15,
    6,   17,   17,   17,   18,   18,   18,   12,   12,   12,
   20,   20,   22,   22,   22,   22,   10,   10,   10,   10,
   10,   10,   10,   21,   21,   21,   29,   29,   26,   26,
   25,   25,   25,   28,   36,   36,   32,   30,   34,   33,
   33,   31,   31,   31,   31,   35,   35,   35,   35,   38,
   38,   38,   39,   39,   39,   37,   37,   40,   40,   40,
   40,   40,   40,   40,   40,   40,   40,   23,   42,   24,
   43,   44,   27,   45,   45,   45,   16,   16,   16,   16,
   16,   16,   47,   47,   47,   47,   47,   46,   46,   48,
   48,   49,   49,   49,   49,   49,   41,   41,   41,   41,
   51,   51,   51,   51,   50,   19,   19,
};
final static short yylen[] = {                            2,
    4,    5,    3,    3,    2,    3,    1,    7,    2,    1,
    0,    2,    2,    2,    1,    2,    2,    1,    2,    1,
    2,    2,    1,    3,    3,    2,    2,    1,    0,    4,
    1,    1,    3,    2,    1,    3,    2,    1,    3,    2,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    1,    3,    2,    3,    3,    2,    6,    4,
    4,    4,    3,    4,    1,    0,    1,    1,    1,    1,
    0,    3,    2,    2,    1,    3,    2,    2,    1,    1,
    3,    1,    3,    2,    2,    3,    2,    1,    1,    1,
    1,    1,    1,    1,    2,    2,    2,    3,    1,    3,
    4,    0,    6,    1,    1,    2,    3,    1,    3,    3,
    3,    2,    3,    1,    3,    3,    3,    1,    1,    1,
    1,    1,    1,    3,    2,    1,    1,    1,    3,    1,
    4,    4,    4,    4,    2,    1,    3,
};
final static short yydefred[] = {                         0,
    7,   11,    0,    0,    0,   11,    0,    0,   69,   68,
    0,    0,    0,   31,    6,    0,    0,    0,    0,   12,
    0,    0,   50,    0,    0,   48,   49,   53,   47,   51,
   52,    0,    0,    0,    0,    0,    3,   17,    0,    0,
    0,    0,   16,    0,    0,    0,    0,   18,   13,   14,
    0,    0,   37,    0,    0,  123,    0,    0,    0,    0,
    0,    0,    0,  114,    0,  126,    0,    0,    0,    0,
    0,  102,    0,    0,   63,    0,    0,  137,    0,   28,
    0,   23,    0,   40,    0,   19,   36,   32,    0,  118,
  119,  120,  121,  112,    0,    0,  125,    0,    0,    0,
  128,   88,   89,   90,   91,   92,   93,    0,    0,    0,
    0,   87,    0,    0,  130,  135,   45,    0,   41,   42,
   43,   44,   46,   73,    0,    0,    0,    0,   80,    0,
   65,    0,   77,    0,    0,   55,    0,    0,    0,    2,
   61,   62,   30,  101,    0,    0,   26,    0,   39,   34,
    0,    0,    0,  117,  115,    0,    0,   72,   96,   95,
   97,    0,    0,    0,    0,   67,   70,    0,   60,  116,
  113,   85,    0,  124,    0,   76,   64,   56,    0,   20,
   58,   54,    0,    0,   11,   24,   25,   33,  134,  133,
  132,  131,  129,    0,   83,   81,   21,   57,    0,  105,
  104,    0,    0,   59,  106,  103,    8,
};
final static short yydgoto[] = {                          3,
    4,    5,   17,   18,   81,   19,   20,   21,   49,   22,
  181,   46,   82,   83,   23,   59,   89,   24,   60,  118,
  119,  120,   26,   27,   28,   29,   30,   31,  138,   32,
   61,  168,  169,   33,   68,  132,   62,  128,  129,  111,
  112,   34,   35,  139,  202,  113,   63,   96,   64,   65,
   66,
};
final static short yysindex[] = {                      -101,
    0,    0,    0, -105,  185,    0,  200,   -6,    0,    0,
    8,   59,   88,    0,    0, -131,   83,  108, -112,    0,
  -49,  -49,    0,  -36,    0,    0,    0,    0,    0,    0,
    0,   34,  115, -107,   48,  220,    0,    0,  -15,  -45,
  -89,  -87,    0,  -85,    0,  -23,  120,    0,    0,    0,
  -84,  -76,    0,  441,  -74,    0,  -33,  -45,  315,  162,
  -69,  164,   -4,    0,  169,    0,  -45,  -51,  215,  -45,
  248,    0,  136,  261,    0,   69,   96,    0,  281,    0,
   32,    0, -203,    0,   61,    0,    0,    0,  -38,    0,
    0,    0,    0,    0,  208,  275,    0,   92,  102,  306,
    0,    0,    0,    0,    0,    0,    0,   -1,  307,  162,
  -45,    0,  289,  169,    0,    0,    0, -113,    0,    0,
    0,    0,    0,    0,  293,  441,  -41,   50,    0,  328,
    0,  -69,    0,   79,  245,    0,  -35,  187,  332,    0,
    0,    0,    0,    0,  251,  -85,    0,  118,    0,    0,
  127,   -4,   -4,    0,    0,   -5,   -2,    0,    0,    0,
    0,   79,   -4,   -4,   52,    0,    0,  -69,    0,    0,
    0,    0,  129,    0,  -45,    0,    0,    0,  268,    0,
    0,    0,  -35,   27,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  145,    0,    0,    0,    0,  137,    0,
    0,  366,  234,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    1,    0,    0,  408,    0,    0,    0,
    0,    0,  -28,    0,    0,    0,   11,    0,  371,    0,
    0,    0,    0,    0,   71,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  414,    0,    0,    0,    0,
    0,    0,    0, -179,   58,    9,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   22,
    0,  -67,   44,    0,    0,    0,    0,  148, -100,    0,
    0,    0,  422,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -56,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  198,
    0,    0,    0,    0,    0,    0,    0,  299,    0,  141,
    0,    0,    0,    0,    0,    0,    0,   19,    0,    0,
    0,    0,    0,    0,    0,   56,    0,    0,    0,  -88,
    0,    0,    0,  -50,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -179,    0,   99,    0,    0,
    0,   68,   90,    0,    0,    0,    0,    0,    0,    0,
    0,  272,  125,  156,    0,    0,    0,    0,    0,    0,
    0,    0,  128,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   19,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   23,    0,    0,    0,   24,    0,    0,  402,  -13,
  243,    0,  286,    0,  -24,    5,    0,    0,  514,  -80,
  400,    0,  -14,  -12,  -11,    0,    0,    0,    0,    0,
    0,    0,  244,    0,    0,    0,   13,  323,  264,    0,
    0,    0,    0,    0,    0,  -34,  -53,  214,  -68,  381,
  392,
};
final static int YYTABLESIZE=717;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         55,
   11,   90,  100,   91,  136,  151,   99,   51,   98,   48,
   15,  136,  136,  136,  136,  136,  136,    6,  136,   95,
   85,    2,   79,  180,   52,   75,    7,  155,   36,   55,
  136,  136,  136,  136,   78,  190,  117,   92,  192,   42,
   11,  153,   93,   76,   77,   69,  121,   39,  122,  123,
   15,  177,   38,   71,  122,   75,  171,  137,  160,  164,
  159,  147,  122,  122,  122,   14,  122,   22,  122,  127,
  100,  199,  145,   58,  134,  146,  108,   71,   55,  130,
  122,  122,  122,  122,  108,   29,  108,  194,  108,   29,
  174,   95,  193,  175,  136,  175,   82,    9,   40,   82,
  111,   38,  108,  108,  108,  108,  148,  117,  111,  142,
  111,   90,  111,   91,   35,  162,   38,  121,  127,  122,
  123,   90,  109,   91,  183,   11,  111,  111,  111,  111,
  109,   35,  109,   41,  109,   15,  143,   14,   90,   27,
   91,   43,   27,  117,  122,  166,  167,   44,  109,  109,
  109,  109,   45,  121,   67,  122,  123,  110,   70,   55,
   79,   79,   79,    1,   79,  110,  108,  110,   84,  110,
   71,   84,   78,   78,   78,   78,   78,   79,   86,  127,
   13,  127,   80,  110,  110,  110,  110,   88,  107,   97,
  111,   11,   12,   75,   75,   13,  107,   75,  107,  100,
  107,  116,  100,  100,  124,   98,   47,  203,   98,   98,
   54,  131,  109,   55,  107,  107,  107,  107,   56,   13,
  179,   57,   98,  172,   16,  150,   16,  136,   13,  173,
  136,  136,  136,  136,  136,  136,  136,  136,  136,   16,
   54,   84,  136,  136,  136,  136,  136,  110,   56,   13,
  189,   57,   55,  191,   74,  133,   11,   11,   11,   16,
  140,   11,   11,  127,   22,   11,   15,   15,   15,   11,
   66,   15,   15,   16,   71,   15,  125,  122,  107,   15,
  122,  122,  122,  122,  122,  122,  122,   16,  122,   54,
  200,   13,  122,  122,  122,  122,  122,   56,   13,  108,
   57,  141,  108,  108,  108,  108,  108,  108,  108,   15,
  108,  182,   86,   38,  108,  108,  108,  108,  108,   55,
   74,  144,   38,  111,   37,  149,  111,  111,  111,  111,
  111,  111,  111,   55,  111,   35,   99,   55,  111,  111,
  111,  111,  111,   94,   73,  109,  158,  109,  109,  109,
  109,  109,  109,  109,  109,  156,  109,   90,  207,   91,
  109,  109,  109,  109,  109,  157,  125,  161,  176,  178,
   54,  184,  136,  185,  107,  108,  106,  125,   56,   13,
  110,   57,  187,  110,  110,  110,  110,  110,  110,  110,
  188,  110,  197,  195,   86,  110,  110,  110,  110,  110,
  205,  127,  127,  127,  167,  127,  206,    5,   66,   66,
   10,  107,   66,    4,  107,  107,  107,  107,  107,  107,
  107,    1,  107,   50,  126,  198,  107,  107,  107,  107,
  107,  186,   56,   13,   72,   57,  165,  204,  196,  114,
    8,    9,   10,    9,   10,   11,   12,   11,   12,   13,
  115,   13,    0,   14,    0,    8,    9,   10,   74,   74,
   11,   12,   74,  152,   13,    0,    0,    0,   14,    0,
    0,   56,   13,    0,   57,    8,    9,   10,    0,    0,
   11,   12,   92,   90,   13,   91,    0,   93,   14,    8,
    9,   10,    0,    0,   11,   12,    0,    0,   13,   94,
    0,    0,   14,  135,    9,   10,    0,    0,   11,   12,
    0,    0,   13,    0,    0,    0,    0,    0,   25,    0,
   25,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  154,    0,   86,   86,   86,    0,   86,   53,   56,   13,
    0,   57,    0,    0,  163,    0,    0,    0,  170,   25,
    0,    0,   56,   13,   94,   57,   56,   13,    0,   57,
    0,    0,   94,   94,   87,   94,    0,    0,    0,    0,
    0,    0,  110,    0,   25,    0,    0,    0,  101,   13,
    0,   57,    0,    0,   25,    0,  102,  103,  104,  105,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   25,    0,    0,    0,    0,
    0,   25,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   25,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  201,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   25,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         45,
    0,   43,   59,   45,   33,   44,   40,   44,   59,   59,
    0,   40,   41,   42,   43,   44,   45,  123,   47,   54,
   44,  123,  123,   59,   61,   41,    4,   96,    6,   45,
   59,   60,   61,   62,  123,   41,   61,   42,   41,   16,
   40,   95,   47,   39,   40,   33,   61,   40,   61,   61,
   40,  132,   59,  123,   33,  123,  125,   71,   60,  113,
   62,  265,   41,   42,   43,  269,   45,   59,   47,   65,
   58,   45,   41,   40,   70,   44,   33,   59,   45,   67,
   59,   60,   61,   62,   41,  265,   43,  168,   45,  269,
   41,  126,   41,   44,  123,   44,   41,   40,   40,   44,
   33,   44,   59,   60,   61,   62,   83,  132,   41,   41,
   43,   43,   45,   45,   44,  111,   59,  132,  114,  132,
  132,   43,   33,   45,  138,  125,   59,   60,   61,   62,
   41,   61,   43,   46,   45,  125,   41,  269,   43,   41,
   45,   59,   44,  168,  123,  259,  260,   40,   59,   60,
   61,   62,  265,  168,   40,  168,  168,   33,  266,   45,
  261,  262,  263,  265,  265,   41,  123,   43,   41,   45,
  123,   44,  261,  262,  263,  265,  265,  265,   59,  175,
  265,   41,  268,   59,   60,   61,   62,  264,   33,  264,
  123,  261,  262,  261,  262,  265,   41,  265,   43,  256,
   45,   40,  259,  260,   41,  256,  256,  185,  259,  260,
  256,  263,  123,   45,   59,   60,   61,   62,  264,  265,
  256,  267,  256,  265,   40,  264,   40,  256,  265,  271,
  259,  260,  261,  262,  263,  264,  265,  266,  267,   40,
  256,  265,  271,  272,  273,  274,  275,  123,  264,  265,
  256,  267,   45,  256,  270,   41,  256,  257,  258,   40,
  125,  261,  262,  123,  256,  265,  256,  257,  258,  269,
  123,  261,  262,   40,  256,  265,   63,  256,  123,  269,
  259,  260,  261,  262,  263,  264,  265,   40,  267,  256,
  264,  265,  271,  272,  273,  274,  275,  264,  265,  256,
  267,   41,  259,  260,  261,  262,  263,  264,  265,  125,
  267,  125,   41,  256,  271,  272,  273,  274,  275,   45,
  123,   41,  265,  256,  125,  265,  259,  260,  261,  262,
  263,  264,  265,   45,  267,  265,  266,   45,  271,  272,
  273,  274,  275,   45,  125,  256,   41,   33,  259,  260,
  261,  262,  263,  264,  265,  264,  267,   43,  125,   45,
  271,  272,  273,  274,  275,  264,  153,   61,   41,  125,
  256,   40,  125,  123,   60,   61,   62,  164,  264,  265,
  256,  267,  265,  259,  260,  261,  262,  263,  264,  265,
  264,  267,  125,  265,  123,  271,  272,  273,  274,  275,
  264,  261,  262,  263,  260,  265,   41,    0,  261,  262,
   40,  256,  265,    0,  259,  260,  261,  262,  263,  264,
  265,    0,  267,   22,  256,  183,  271,  272,  273,  274,
  275,  146,  264,  265,   35,  267,  114,  194,  175,   59,
  256,  257,  258,  257,  258,  261,  262,  261,  262,  265,
   59,  265,   -1,  269,   -1,  256,  257,  258,  261,  262,
  261,  262,  265,  256,  265,   -1,   -1,   -1,  269,   -1,
   -1,  264,  265,   -1,  267,  256,  257,  258,   -1,   -1,
  261,  262,   42,   43,  265,   45,   -1,   47,  269,  256,
  257,  258,   -1,   -1,  261,  262,   -1,   -1,  265,   59,
   -1,   -1,  269,  256,  257,  258,   -1,   -1,  261,  262,
   -1,   -1,  265,   -1,   -1,   -1,   -1,   -1,    5,   -1,
    7,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,   -1,  261,  262,  263,   -1,  265,   24,  264,  265,
   -1,  267,   -1,   -1,  256,   -1,   -1,   -1,  256,   36,
   -1,   -1,  264,  265,  256,  267,  264,  265,   -1,  267,
   -1,   -1,  264,  265,   51,  267,   -1,   -1,   -1,   -1,
   -1,   -1,   59,   -1,   61,   -1,   -1,   -1,  264,  265,
   -1,  267,   -1,   -1,   71,   -1,  272,  273,  274,  275,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  132,   -1,   -1,   -1,   -1,
   -1,  138,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  168,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  184,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  203,
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
"declaracion_funcion : header_funcion '(' parametros_formales ')' '{' list_sentencia '}'",
"header_funcion : tipo ID",
"header_funcion : tipo",
"list_sentencia :",
"list_sentencia : list_sentencia sentencia",
"sentencia : sentencia_declarativa punto_y_coma",
"sentencia : sentencia_ejecutable punto_y_coma",
"sentencia : declaracion_funcion",
"sentencia : declaracion_funcion ';'",
"sentencia : error ';'",
"punto_y_coma : ';'",
"punto_y_coma : error ';'",
"punto_y_coma_cuerpo : ';'",
"punto_y_coma_cuerpo : error '}'",
"sentencia_declarativa : tipo list_vars",
"parametros_formales : parametro_formal",
"parametros_formales : parametros_formales ',' parametro_formal",
"parametro_formal : semantica tipo ID",
"parametro_formal : semantica ID",
"parametro_formal : semantica tipo",
"semantica : CR",
"semantica :",
"sentencia_return : RETURN '(' expresion ')'",
"tipo : ULONG",
"list_ctes : CTE",
"list_ctes : list_ctes ',' CTE",
"list_ctes : list_ctes CTE",
"list_vars_mix : identificador",
"list_vars_mix : list_vars_mix ',' identificador",
"list_vars_mix : list_vars_mix identificador",
"list_vars : ID",
"list_vars : list_vars ',' ID",
"list_vars : list_vars ID",
"cuerpo_ejecutable : bloque_ejecutable",
"cuerpo_ejecutable : sentencia_ejecutable_simple",
"sentencia_ejecutable_simple : asignacion_simple",
"sentencia_ejecutable_simple : asignacion_multiple",
"sentencia_ejecutable_simple : sentencia_return",
"sentencia_ejecutable_simple : sentencia_print",
"sentencia_ejecutable : sentencia_if",
"sentencia_ejecutable : asignacion_simple",
"sentencia_ejecutable : asignacion_multiple",
"sentencia_ejecutable : sentencia_return",
"sentencia_ejecutable : expresion_lambda",
"sentencia_ejecutable : sentencia_while",
"sentencia_ejecutable : sentencia_print",
"bloque_ejecutable : '{' lista_sentencias_ejecutable '}'",
"bloque_ejecutable : '{' '}'",
"bloque_ejecutable : '{' error '}'",
"lista_sentencias_ejecutable : lista_sentencias_ejecutable sentencia_ejecutable punto_y_coma_cuerpo",
"lista_sentencias_ejecutable : sentencia_ejecutable punto_y_coma_cuerpo",
"sentencia_if : if condicion_if cuerpo_ejecutable else cuerpo_ejecutable end_if",
"sentencia_if : if condicion_if cuerpo_ejecutable end_if",
"sentencia_print : PRINT '(' CADENA ')'",
"sentencia_print : PRINT '(' expresion ')'",
"sentencia_print : PRINT '(' ')'",
"sentencia_while : while condicion_while do cuerpo_ejecutable",
"do : DO",
"do :",
"else : ELSE",
"if : IF",
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
"parametros_reales : parametro_real",
"parametros_reales : parametros_reales ',' parametro_real",
"parametros_reales : error",
"parametro_real : expresion FLECHA ID",
"parametro_real : expresion FLECHA",
"parametro_real : expresion ID",
"condicion : expresion comp expresion",
"condicion : expresion factor_simple",
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
"var_asignacion_simple : identificador",
"asignacion_multiple : list_vars_mix '=' list_ctes",
"header_lambda : '(' tipo ID ')'",
"$$1 :",
"expresion_lambda : header_lambda bloque_ejecutable $$1 '(' factor_lambda ')'",
"factor_lambda : identificador",
"factor_lambda : CTE",
"factor_lambda : '-' CTE",
"expresion : expresion operador_expresion termino",
"expresion : termino",
"expresion : error operador_expresion termino",
"expresion : expresion operador_expresion error",
"expresion : error operador_expresion error",
"expresion : error ';'",
"termino : termino operador_termino factor",
"termino : factor",
"termino : error operador_termino factor",
"termino : termino operador_termino error",
"termino : error operador_termino error",
"operador_expresion : '+'",
"operador_expresion : '-'",
"operador_termino : '*'",
"operador_termino : '/'",
"factor : identificador",
"factor : CTE",
"factor : inicio_llamado parametros_reales ')'",
"factor : '-' CTE",
"factor : trunc",
"factor_simple : identificador",
"factor_simple : CTE",
"factor_simple : inicio_llamado parametros_reales ')'",
"factor_simple : trunc",
"trunc : TRUNC '(' CTE ')'",
"trunc : TRUNC '(' CTE error",
"trunc : TRUNC error CTE ')'",
"trunc : TRUNC error CTE error",
"inicio_llamado : identificador '('",
"identificador : ID",
"identificador : ID '.' ID",
};

//#line 428 "Gramatica.y"
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

    if (!"Variable".equals(elem.getUso()) && !"parametro".equals(elem.getUso())) {
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
//#line 901 "Parser.java"
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
//#line 27 "Gramatica.y"
{ reportarEstructura("Fin del programa");}
break;
case 2:
//#line 28 "Gramatica.y"
{ yyerror("Error: '}' de mas al final de programa");}
break;
case 3:
//#line 29 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '{' al inicio"); }
break;
case 4:
//#line 30 "Gramatica.y"
{ yyerror("Error: Falta delimitador del programa '}' al final"); }
break;
case 5:
//#line 31 "Gramatica.y"
{ yyerror("Error: Falta delimitadores del programa '{' al inicio y '}' al final"); }
break;
case 6:
//#line 32 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del programa"); }
break;
case 7:
//#line 35 "Gramatica.y"
{ambito = val_peek(0).sval; prog_principal = val_peek(0).sval;}
break;
case 8:
//#line 38 "Gramatica.y"
{ reportarEstructura("declaracion de funcion");
                                                                                            String etiquetaFin = "fin_" + ambito;
                                                                                            ArregloTercetos.crearTerceto(etiquetaFin, "_", "_");
                                                                                            salirAmbito();
                                                                                            chequearReturn();
                                                                                          }
break;
case 9:
//#line 46 "Gramatica.y"
{
                                 declaracionDeFuncion(val_peek(0).sval, ambito, "Función");
                                 entrarAmbito(val_peek(0).sval);
                                 pilaReturns.push(val_peek(0).sval + ":" + "false");
                                 String etiquetaInicio = "ini_" + ambito ;
                                 ArregloTercetos.crearTerceto(etiquetaInicio, "_", "_");
                                }
break;
case 10:
//#line 53 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre a la función"); }
break;
case 16:
//#line 63 "Gramatica.y"
{ yyerror("Error: ';' de mas despues de declaracion de funcion"); }
break;
case 17:
//#line 64 "Gramatica.y"
{ yyerror("Error: sentencia invalida escrita"); }
break;
case 19:
//#line 69 "Gramatica.y"
{ yyerror("Error: falta ';' al final de la sentencia o sentencia invalida escrita"); }
break;
case 21:
//#line 73 "Gramatica.y"
{ yyerror("Error: falta ';' al final de la sentencia o sentencia invalida escrita"); }
break;
case 22:
//#line 77 "Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 25:
//#line 84 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,val_peek(2).sval);}
break;
case 26:
//#line 85 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 27:
//#line 86 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre al parametro formal"); }
break;
case 28:
//#line 89 "Gramatica.y"
{yyval.sval = "cr";}
break;
case 29:
//#line 90 "Gramatica.y"
{ yyval.sval = "cv"; }
break;
case 30:
//#line 93 "Gramatica.y"
{registrarReturn();
                                                          String varRetorno = "_ret_" + ambito;
                                                          ArregloTercetos.crearTerceto(":=", varRetorno, val_peek(1).sval);
                                                          ArregloTercetos.crearTerceto("RETURN", varRetorno, val_peek(1).sval);
                                                          ArregloTercetos.crearTerceto("JMP", "fin_" + ambito, null);
                                             }
break;
case 31:
//#line 101 "Gramatica.y"
{tipo = "ulong";}
break;
case 32:
//#line 104 "Gramatica.y"
{ yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); ControlAsigMultiple.pushTipoDer(yyval.tipo); yyval.sval = val_peek(0).sval; }
break;
case 33:
//#line 105 "Gramatica.y"
{ yyval.tipo = val_peek(2).tipo; String t = obtenerTipoDeSimbolo(val_peek(0).sval); ControlAsigMultiple.pushTipoDer(t); yyval.sval = val_peek(2).sval + "," + val_peek(0).sval; }
break;
case 34:
//#line 106 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre constantes"); yyval = val_peek(1); }
break;
case 35:
//#line 111 "Gramatica.y"
{ControlAsigMultiple.pushTipoIzq(yyval.tipo); }
break;
case 36:
//#line 112 "Gramatica.y"
{ ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(val_peek(0).sval)); yyval.sval = val_peek(2).sval + "," + val_peek(0).sval; }
break;
case 37:
//#line 113 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); yyval = val_peek(1); }
break;
case 38:
//#line 116 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 39:
//#line 117 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 40:
//#line 118 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 55:
//#line 147 "Gramatica.y"
{yyerror("Error: Cuerpo ejecutable vacio");}
break;
case 56:
//#line 148 "Gramatica.y"
{yyerror("Error: sentencia invalida dentro del cuerpo ejecutable");}
break;
case 59:
//#line 155 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 60:
//#line 156 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 61:
//#line 159 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 62:
//#line 160 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 63:
//#line 161 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 64:
//#line 164 "Gramatica.y"
{
                            reportarEstructura("WHILE");
                            yyval = ArregloTercetos.completarBackPatchingWHILE();
                            ArregloTercetos.crearTerceto("WHILE_END", "_", "_");
                      }
break;
case 66:
//#line 173 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'do' en estructura while");}
break;
case 67:
//#line 176 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIFDesapilaryCompletar("bl", null,null);}
break;
case 68:
//#line 179 "Gramatica.y"
{ArregloTercetos.crearTerceto("IF_START", "_", "_");}
break;
case 69:
//#line 182 "Gramatica.y"
{
                                ArregloTercetos.apilarTercetoInicialWHILE();

                                ArregloTercetos.crearTerceto("WHILE_START", "_", "_");
                              }
break;
case 70:
//#line 189 "Gramatica.y"
{ArregloTercetos.completarTercetoBackPatchingIF();}
break;
case 71:
//#line 190 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 72:
//#line 193 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 73:
//#line 194 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 74:
//#line 195 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(0).sval,null);}
break;
case 75:
//#line 196 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(0).sval,null);}
break;
case 76:
//#line 199 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 77:
//#line 200 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 78:
//#line 201 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(0).sval,null);}
break;
case 79:
//#line 202 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(0).sval,null);}
break;
case 82:
//#line 209 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 83:
//#line 212 "Gramatica.y"
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
case 84:
//#line 252 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 85:
//#line 253 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 86:
//#line 256 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, val_peek(1).sval); }
break;
case 87:
//#line 257 "Gramatica.y"
{ yyerror("Error: falta operador de comparacion en la condicion"); }
break;
case 88:
//#line 260 "Gramatica.y"
{ yyval.sval = "=="; }
break;
case 89:
//#line 261 "Gramatica.y"
{ yyval.sval = ">="; }
break;
case 90:
//#line 262 "Gramatica.y"
{ yyval.sval = "<="; }
break;
case 91:
//#line 263 "Gramatica.y"
{ yyval.sval = "=!"; }
break;
case 92:
//#line 264 "Gramatica.y"
{ yyval.sval = ">"; }
break;
case 93:
//#line 265 "Gramatica.y"
{ yyval.sval = "<"; }
break;
case 94:
//#line 266 "Gramatica.y"
{yyerror("Error: Operador invalido");}
break;
case 95:
//#line 267 "Gramatica.y"
{yyerror("Error: Operador invalido");}
break;
case 96:
//#line 268 "Gramatica.y"
{yyerror("Error: Operador invalido");}
break;
case 97:
//#line 269 "Gramatica.y"
{yyerror("Error: Operador invalido");}
break;
case 98:
//#line 273 "Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                               yyval = ArregloTercetos.crearTerceto(":=", val_peek(2).sval, val_peek(0).sval);
                                                               yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, ":=");
                                                               }
break;
case 99:
//#line 279 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = val_peek(0).tipo; }
break;
case 100:
//#line 283 "Gramatica.y"
{
                              reportarEstructura("asignacion multiple");
                              ControlAsigMultiple.compararTipos();
                              crearTercetosAsigMultiple(val_peek(2).sval, val_peek(0).sval);
                            }
break;
case 101:
//#line 291 "Gramatica.y"
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
case 102:
//#line 304 "Gramatica.y"
{
                              ArregloTercetos.crearTerceto("fin_" + val_peek(1).sval, "_", "_");
                            }
break;
case 103:
//#line 308 "Gramatica.y"
{
                              String tipoFormal = obtenerTipoDeSimbolo(val_peek(5).tipo);
                              chequearTipos(tipoFormal, val_peek(0).tipo, ":=");
                              ArregloTercetos.crearTerceto(":=", val_peek(5).tipo, val_peek(0).sval);
                              ArregloTercetos.crearTerceto("CALL", val_peek(5).sval, null);
                            }
break;
case 104:
//#line 317 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = val_peek(0).tipo; }
break;
case 105:
//#line 318 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); }
break;
case 106:
//#line 319 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 107:
//#line 322 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, val_peek(1).sval);
                        System.out.println("$1: "+ val_peek(2).sval + " $2: " + val_peek(1).sval + " $3: " + val_peek(0).sval + " -> $$: " + yyval.sval);}
break;
case 108:
//#line 324 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 109:
//#line 325 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 110:
//#line 326 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 111:
//#line 327 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 112:
//#line 328 "Gramatica.y"
{ yyerror("Error: error en expresion, revisar '+' '-'"); }
break;
case 113:
//#line 334 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, val_peek(1).sval); }
break;
case 114:
//#line 335 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 115:
//#line 336 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 116:
//#line 337 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 117:
//#line 338 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 118:
//#line 343 "Gramatica.y"
{
                          yyval = new ParserValExt(); /* Nuevo objeto*/
                          yyval.sval = "+";
                        }
break;
case 119:
//#line 347 "Gramatica.y"
{
                          yyval = new ParserValExt(); /* Nuevo objeto*/
                          yyval.sval = "-";
                        }
break;
case 120:
//#line 353 "Gramatica.y"
{
                          yyval = new ParserValExt();
                          yyval.sval = "*";
                        }
break;
case 121:
//#line 357 "Gramatica.y"
{
                          yyval = new ParserValExt();
                          yyval.sval = "/";
                        }
break;
case 122:
//#line 364 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo; }
break;
case 123:
//#line 365 "Gramatica.y"
{yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); yyval = val_peek(0); }
break;
case 124:
//#line 366 "Gramatica.y"
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
case 125:
//#line 378 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval);}
break;
case 126:
//#line 379 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = "ulong"; }
break;
case 127:
//#line 383 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo; }
break;
case 128:
//#line 384 "Gramatica.y"
{yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); yyval.sval = val_peek(0).sval; }
break;
case 129:
//#line 385 "Gramatica.y"
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
case 130:
//#line 397 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = "ulong"; }
break;
case 131:
//#line 400 "Gramatica.y"
{String tipo = obtenerTipoDeSimbolo(val_peek(1).sval);
                                          if(! tipo.equals("dfloat")){
                                             yyerror("Error: La funcion TRUNC solo acepta operandos de tipo dfloat.");
                                          }
                                          yyval = ArregloTercetos.crearTerceto("TRUNC", val_peek(1).sval, null);
                                          yyval.tipo = "ulong";}
break;
case 132:
//#line 407 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 133:
//#line 408 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 134:
//#line 409 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 135:
//#line 415 "Gramatica.y"
{
                             /* CORRECCIÓN 1: Usar $$.sval*/
                             PilaDeFuncionesLlamadas.iniciarLlamada(yyval.sval);
                           }
break;
case 136:
//#line 421 "Gramatica.y"
{ yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 137:
//#line 422 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
//#line 1613 "Parser.java"
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
