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
    2,    2,    7,    7,    7,    7,    9,    9,    8,    5,
    5,   12,   12,   12,   13,   13,   14,    6,   16,   16,
   16,   17,   17,   17,   11,   11,   11,   19,   19,   21,
   21,   21,   21,   10,   10,   10,   10,   10,   10,   10,
   20,   20,   28,   28,   25,   25,   24,   24,   24,   27,
   35,   35,   31,   29,   33,   32,   32,   30,   30,   30,
   30,   34,   34,   34,   34,   37,   37,   37,   38,   38,
   38,   36,   36,   39,   39,   39,   39,   39,   39,   39,
   39,   39,   39,   22,   41,   23,   42,   43,   26,   44,
   44,   44,   15,   15,   15,   15,   15,   15,   45,   45,
   46,   46,   46,   46,   46,   47,   47,   48,   48,   48,
   48,   48,   40,   40,   40,   40,   50,   50,   50,   50,
   49,   18,   18,
};
final static short yylen[] = {                            2,
    4,    5,    3,    3,    2,    3,    1,    7,    2,    1,
    0,    2,    2,    2,    1,    2,    1,    1,    2,    1,
    3,    3,    2,    2,    1,    0,    4,    1,    1,    3,
    2,    1,    3,    2,    1,    3,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    3,    2,    3,    2,    6,    4,    4,    4,    3,    4,
    1,    0,    1,    1,    1,    1,    0,    3,    2,    2,
    1,    3,    2,    2,    1,    1,    3,    1,    3,    2,
    2,    3,    2,    1,    1,    1,    1,    1,    1,    1,
    2,    2,    2,    3,    1,    3,    4,    0,    6,    1,
    1,    2,    3,    1,    3,    3,    3,    2,    1,    1,
    3,    1,    3,    3,    3,    1,    1,    1,    1,    3,
    2,    1,    1,    1,    3,    1,    4,    4,    4,    4,
    2,    1,    3,
};
final static short yydefred[] = {                         0,
    7,   11,    0,    0,    0,   11,    0,    0,   65,   64,
    0,    0,    0,   28,    6,    0,   15,    0,    0,   12,
    0,    0,   47,    0,    0,   45,   46,   50,   44,   48,
   49,    0,    0,    0,    0,    0,    3,   18,   17,   16,
    0,    0,    0,    0,    0,    0,    0,   13,   14,    0,
    0,   34,    0,    0,  119,    0,    0,    0,    0,    0,
    0,    0,  112,    0,  122,    0,    0,    0,    0,    0,
   98,    0,    0,   59,    0,    0,  133,    0,   25,    0,
   20,    0,   37,    0,   33,   29,    0,  109,  110,  116,
  117,  108,    0,    0,  121,    0,    0,    0,  124,   84,
   85,   86,   87,   88,   89,    0,    0,    0,    0,   83,
    0,    0,  126,  131,   42,    0,   38,   39,   40,   41,
   43,   69,    0,    0,    0,    0,   76,    0,   61,    0,
   73,    0,   52,    0,    0,    0,    2,   57,   58,   27,
   97,    0,    0,   23,    0,   36,   31,    0,    0,    0,
  115,  113,    0,    0,   68,   92,   91,   93,    0,    0,
    0,    0,   63,   66,    0,   56,  114,  111,   81,    0,
  120,    0,   72,   60,   54,   51,    0,    0,   11,   21,
   22,   30,  130,  129,  128,  127,  125,    0,   79,   77,
   53,    0,  101,  100,    0,    0,   55,  102,   99,    8,
};
final static short yydgoto[] = {                          3,
    4,    5,   17,   18,   80,   19,   20,   21,   40,   22,
   47,   81,   82,   23,   58,   87,   24,   59,  116,  117,
  118,   26,   27,   28,   29,   30,   31,  135,   32,   60,
  165,  166,   33,   67,  130,   61,  126,  127,  109,  110,
   34,   35,  136,  195,  111,   62,   94,   63,   64,   65,
};
final static short yysindex[] = {                       -98,
    0,    0,    0, -117,  166,    0,  185,  -39,    0,    0,
  -24,   41,   15,    0,    0, -183,    0,   47, -167,    0,
  -39,  -39,    0,  -12,    0,    0,    0,    0,    0,    0,
    0,   91,  113, -159,   10,  207,    0,    0,    0,    0,
  136,  224, -120, -114, -109,    0,  -20,    0,    0, -100,
  -90,    0,   21,  -72,    0,  -37,  224,  143,  153,  -54,
  158,   23,    0,  257,    0,  224,  -61,  168,  224,  175,
    0,  111,  223,    0,   61,   79,    0,  226,    0,   64,
    0, -219,    0,  -49,    0,    0,   -4,    0,    0,    0,
    0,    0,  269,  279,    0,   16,   17,  237,    0,    0,
    0,    0,    0,    0,    0,   81,  204,  153,  224,    0,
  291,  257,    0,    0,    0, -212,    0,    0,    0,    0,
    0,    0,  303,   21,  -28,   69,    0,  248,    0,  -54,
    0,  135,    0,  -39,  242,  252,    0,    0,    0,    0,
    0,  180, -109,    0,   39,    0,    0,   71,   23,   23,
    0,    0,  -36,   -8,    0,    0,    0,    0,  135,   23,
   23,   86,    0,    0,  -54,    0,    0,    0,    0,   46,
    0,  224,    0,    0,    0,    0,  -39,  -43,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   73,    0,    0,
    0,   80,    0,    0,  318,  221,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    1,    0,    0,  371,    0,    0,    0,
    0,    0,  -33,    0,    0,    0,    0,    0,  351,    0,
    0,    0,    0,    0,  -10,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  393,    0,    0,    0,    0,
    0,    0,    0,    0, -137,  -21,  -38,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   12,    0,
  190,   34,    0,    0,    0,    0,  258,  -92,    0,    0,
    0,  399,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -46,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  266,    0,    0,
    0,    0,    0,    0,    0,  309,    0,  -11,    0,    0,
    0,    0,    0,    0,    0,   -7,    0,    0,    0,    0,
    0,    0,    0,  106,    0,    0,    0,  -67,    0,    0,
    0,   66,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -137,    0,  131,    0,    0,    0,   56,   78,
    0,    0,    0,    0,    0,    0,    0,    0,  247,  101,
  123,    0,    0,    0,    0,    0,    0,    0,    0,  156,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   -7,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   33,    0,    0,    0,    6,    0,    0,   14,  -52,
    0,  259,    0,  -16,  429,    0,    0,  490,  -87,  369,
    0,   22,   24,   25,    0,    0,    0,    0,    0,    0,
    0,  217,    0,    0,    0,   43,  297,  239,    0,    0,
    0,    0,    0,    0,    5,  -51,  -58,  -32,  354,  355,
};
final static int YYTABLESIZE=686;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        132,
   11,  192,   97,  123,  184,    6,  132,  132,  132,  132,
  132,  132,   96,  132,   88,   41,   89,  134,    9,   39,
   19,   44,   35,   84,    2,  132,  132,  132,  132,  123,
   75,   50,  186,   32,   48,   49,    7,   35,   36,  148,
   11,  150,  174,  115,  118,  144,  163,  164,   51,   14,
   32,   67,  118,  118,  118,   74,  118,   93,  118,  161,
   43,  152,   90,   88,   90,   89,  104,   91,   70,   91,
  118,  118,  118,  118,  104,   68,  104,  188,  104,   92,
   42,  119,  177,  120,  121,   14,   45,  145,  107,  132,
  168,  123,  104,  104,  104,  104,  107,   46,  107,   98,
  107,  139,  123,   88,  142,   89,   69,  143,  128,  171,
  105,  123,  172,  115,  107,  107,  107,  107,  105,  140,
  105,   88,  105,   89,   94,   11,  187,   26,   93,  172,
   57,   26,   70,  106,  118,   54,  105,  105,  105,  105,
  157,  106,  156,  106,   77,  106,   78,  175,  115,   78,
   78,  119,   66,  120,  121,  103,  104,   54,   79,  106,
  106,  106,  106,  103,   13,  103,    1,  103,   75,   75,
   75,   24,   75,   86,   24,  107,   74,   88,  107,   89,
   54,  103,  103,  103,  103,   88,  119,   89,  120,  121,
  191,   95,  114,   74,   74,   74,   80,   74,  122,   80,
  105,  129,  105,  106,  104,   16,   11,   12,  131,   96,
   13,  196,   96,   96,   16,  146,   38,   19,   96,  183,
  193,   13,  132,  106,   16,  132,  132,  132,  132,  132,
  132,  132,  132,  132,   35,  137,  169,  132,  132,  132,
  132,  132,  170,   35,   83,  103,   16,  185,   67,  123,
  123,  123,   13,  123,   32,   95,   11,   11,   11,  147,
   16,   11,   11,  138,  158,   11,  141,  118,   54,   11,
  118,  118,  118,  118,  118,  118,  118,  155,  118,  153,
  154,   16,  118,  118,  118,  118,  118,   82,  173,  104,
   15,  178,  104,  104,  104,  104,  104,  104,  104,  133,
  104,   54,  179,  181,  104,  104,  104,  104,  104,   37,
  189,  107,   71,   54,  107,  107,  107,  107,  107,  107,
  107,   94,  107,   54,   94,   94,  107,  107,  107,  107,
  107,   72,  164,  105,  182,   54,  105,  105,  105,  105,
  105,  105,  105,  198,  105,  200,   53,   54,  105,  105,
  105,  105,  105,   90,   55,   13,  106,   56,  199,  106,
  106,  106,  106,  106,  106,  106,  176,  106,   53,   82,
    5,  106,  106,  106,  106,  106,   55,   13,  103,   56,
   62,  103,  103,  103,  103,  103,  103,  103,   70,  103,
   10,   53,    4,  103,  103,  103,  103,  103,    1,   55,
   13,  180,   56,   71,  197,   73,   99,   13,  162,   56,
  190,  112,  113,    0,  100,  101,  102,  103,    0,    0,
    0,    8,    9,   10,    0,    0,   11,   12,    0,    0,
   13,    9,   10,    0,   14,   11,   12,    0,    0,   13,
    8,    9,   10,    0,    0,   11,   12,    0,    0,   13,
   71,   71,    0,   14,   71,    0,    0,    0,    0,    0,
    0,    0,    8,    9,   10,    0,    0,   11,   12,   75,
   76,   13,    0,    0,    0,   14,    8,    9,   10,   53,
    0,   11,   12,    0,    0,   13,    0,   55,   13,   14,
   56,    0,  125,    0,   25,    0,   25,  132,    9,   10,
    0,    0,   11,   12,    0,    0,   13,   82,   82,   82,
    0,   82,  124,   52,    0,    0,    0,    0,   62,   62,
   55,   13,   62,   56,  149,   25,   70,   70,    0,    0,
   70,    0,   55,   13,  151,   56,    0,  159,    0,   85,
  125,    0,   55,   13,    0,   56,  160,  108,    0,   25,
    0,    0,    0,    0,   55,   13,    0,   56,  167,   25,
    0,    0,    0,    0,   90,    0,   55,   13,    0,   56,
    0,    0,   90,   90,    0,   90,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  125,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   25,
    0,    0,    0,    0,   25,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   25,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  194,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   25,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         33,
    0,   45,   40,   62,   41,  123,   40,   41,   42,   43,
   44,   45,   59,   47,   43,   40,   45,   70,   40,   59,
   59,   16,   44,   44,  123,   59,   60,   61,   62,   41,
  123,   44,   41,   44,   21,   22,    4,   59,    6,   44,
   40,   93,  130,   60,   33,  265,  259,  260,   61,  269,
   61,   59,   41,   42,   43,  123,   45,   53,   47,  111,
   46,   94,   42,   43,   42,   45,   33,   47,  123,   47,
   59,   60,   61,   62,   41,   33,   43,  165,   45,   59,
   40,   60,  135,   60,   60,  269,   40,   82,   33,  123,
  123,  150,   59,   60,   61,   62,   41,  265,   43,   57,
   45,   41,  161,   43,   41,   45,  266,   44,   66,   41,
   33,  123,   44,  130,   59,   60,   61,   62,   41,   41,
   43,   43,   45,   45,   59,  125,   41,  265,  124,   44,
   40,  269,  123,   33,  123,   45,   59,   60,   61,   62,
   60,   41,   62,   43,  265,   45,   41,  134,  165,   44,
  265,  130,   40,  130,  130,   33,  123,   45,  268,   59,
   60,   61,   62,   41,  265,   43,  265,   45,  261,  262,
  263,   41,  265,  264,   44,   33,   41,   43,  123,   45,
   45,   59,   60,   61,   62,   43,  165,   45,  165,  165,
  177,  264,   40,  261,  262,  263,   41,  265,   41,   44,
  123,  263,   60,   61,   62,   40,  261,  262,   41,  256,
  265,  179,  259,  260,   40,  265,  256,  256,  256,  256,
  264,  265,  256,  123,   40,  259,  260,  261,  262,  263,
  264,  265,  266,  267,  256,  125,  265,  271,  272,  273,
  274,  275,  271,  265,  265,  123,   40,  256,  256,  261,
  262,  263,  265,  265,  265,  266,  256,  257,  258,  264,
   40,  261,  262,   41,   61,  265,   41,  256,   45,  269,
  259,  260,  261,  262,  263,  264,  265,   41,  267,  264,
  264,   40,  271,  272,  273,  274,  275,   41,   41,  256,
  125,   40,  259,  260,  261,  262,  263,  264,  265,  125,
  267,   45,  123,  265,  271,  272,  273,  274,  275,  125,
  265,  256,  123,   45,  259,  260,  261,  262,  263,  264,
  265,  256,  267,   45,  259,  260,  271,  272,  273,  274,
  275,  125,  260,  256,  264,   45,  259,  260,  261,  262,
  263,  264,  265,  264,  267,  125,  256,   45,  271,  272,
  273,  274,  275,   45,  264,  265,  256,  267,   41,  259,
  260,  261,  262,  263,  264,  265,  125,  267,  256,  123,
    0,  271,  272,  273,  274,  275,  264,  265,  256,  267,
  123,  259,  260,  261,  262,  263,  264,  265,  123,  267,
   40,  256,    0,  271,  272,  273,  274,  275,    0,  264,
  265,  143,  267,   35,  188,  270,  264,  265,  112,  267,
  172,   58,   58,   -1,  272,  273,  274,  275,   -1,   -1,
   -1,  256,  257,  258,   -1,   -1,  261,  262,   -1,   -1,
  265,  257,  258,   -1,  269,  261,  262,   -1,   -1,  265,
  256,  257,  258,   -1,   -1,  261,  262,   -1,   -1,  265,
  261,  262,   -1,  269,  265,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,  257,  258,   -1,   -1,  261,  262,   41,
   42,  265,   -1,   -1,   -1,  269,  256,  257,  258,  256,
   -1,  261,  262,   -1,   -1,  265,   -1,  264,  265,  269,
  267,   -1,   64,   -1,    5,   -1,    7,   69,  257,  258,
   -1,   -1,  261,  262,   -1,   -1,  265,  261,  262,  263,
   -1,  265,  256,   24,   -1,   -1,   -1,   -1,  261,  262,
  264,  265,  265,  267,  256,   36,  261,  262,   -1,   -1,
  265,   -1,  264,  265,  256,  267,   -1,  109,   -1,   50,
  112,   -1,  264,  265,   -1,  267,  256,   58,   -1,   60,
   -1,   -1,   -1,   -1,  264,  265,   -1,  267,  256,   70,
   -1,   -1,   -1,   -1,  256,   -1,  264,  265,   -1,  267,
   -1,   -1,  264,  265,   -1,  267,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  172,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  130,
   -1,   -1,   -1,   -1,  135,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  165,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  178,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  196,
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
"sentencia : error punto_y_coma",
"punto_y_coma : ';'",
"punto_y_coma : error",
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
"lista_sentencias_ejecutable : lista_sentencias_ejecutable sentencia_ejecutable punto_y_coma",
"lista_sentencias_ejecutable : sentencia_ejecutable punto_y_coma",
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
"operador_expresion : '+'",
"operador_expresion : '-'",
"termino : termino operador_termino factor",
"termino : factor",
"termino : error operador_termino factor",
"termino : termino operador_termino error",
"termino : error operador_termino error",
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

//#line 404 "Gramatica.y"
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

    if (!"Variable".equals(elem.getUso())) {
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
//#line 886 "Parser.java"
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
{ yyerror("Error: sentencia invalida escrita"); }
break;
case 18:
//#line 67 "Gramatica.y"
{ yyerror("Error: falta ';' al final de la sentencia o sentencia invalida escrita"); }
break;
case 19:
//#line 71 "Gramatica.y"
{ reportarEstructura("declaracion de variable(s)"); }
break;
case 22:
//#line 78 "Gramatica.y"
{registrarParametroFuncion(val_peek(0).sval,val_peek(2).sval);}
break;
case 23:
//#line 79 "Gramatica.y"
{ yyerror("Error: Falta definir el tipo del parametro formal"); }
break;
case 24:
//#line 80 "Gramatica.y"
{ yyerror("Error: Falta definir un nombre al parametro formal"); }
break;
case 25:
//#line 83 "Gramatica.y"
{yyval.sval = "cr";}
break;
case 26:
//#line 84 "Gramatica.y"
{ yyval.sval = "cv"; }
break;
case 27:
//#line 87 "Gramatica.y"
{registrarReturn();
                                                          String varRetorno = "_ret_" + ambito;
                                                          ArregloTercetos.crearTerceto(":=", varRetorno, val_peek(1).sval);
                                                          ArregloTercetos.crearTerceto("RETURN", varRetorno, val_peek(1).sval);
                                                          ArregloTercetos.crearTerceto("JMP", "fin_" + ambito, null);
                                             }
break;
case 28:
//#line 95 "Gramatica.y"
{tipo = "ulong";}
break;
case 29:
//#line 98 "Gramatica.y"
{ yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); ControlAsigMultiple.pushTipoDer(yyval.tipo); yyval.sval = val_peek(0).sval; }
break;
case 30:
//#line 99 "Gramatica.y"
{ yyval.tipo = val_peek(2).tipo; String t = obtenerTipoDeSimbolo(val_peek(0).sval); ControlAsigMultiple.pushTipoDer(t); yyval.sval = val_peek(2).sval + "," + val_peek(0).sval; }
break;
case 31:
//#line 100 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre constantes"); yyval = val_peek(1); }
break;
case 32:
//#line 105 "Gramatica.y"
{ControlAsigMultiple.pushTipoIzq(yyval.tipo); }
break;
case 33:
//#line 106 "Gramatica.y"
{ ControlAsigMultiple.pushTipoIzq(obtenerTipoDeSimbolo(val_peek(0).sval)); yyval.sval = val_peek(2).sval + "," + val_peek(0).sval; }
break;
case 34:
//#line 107 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); yyval = val_peek(1); }
break;
case 35:
//#line 110 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 36:
//#line 111 "Gramatica.y"
{yyval = declaracionDeVariable(val_peek(0).sval, tipo, ambito, "Variable");}
break;
case 37:
//#line 112 "Gramatica.y"
{ yyerror("Error: se esperaba ',' entre variables"); }
break;
case 52:
//#line 141 "Gramatica.y"
{yyerror("Error: Cuerpo ejecutable vacio");}
break;
case 55:
//#line 148 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 56:
//#line 149 "Gramatica.y"
{ reportarEstructura("IF"); }
break;
case 57:
//#line 152 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 58:
//#line 153 "Gramatica.y"
{ reportarEstructura("PRINT"); ArregloTercetos.crearTerceto("PRINT", val_peek(1).sval, null); }
break;
case 59:
//#line 154 "Gramatica.y"
{ yyerror("Error: falta argumento dentro del print"); }
break;
case 60:
//#line 157 "Gramatica.y"
{
                            reportarEstructura("WHILE");
                            yyval = ArregloTercetos.completarBackPatchingWHILE();
                            ArregloTercetos.crearTerceto("WHILE_END", "_", "_");
                      }
break;
case 62:
//#line 166 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'do' en estructura while");}
break;
case 63:
//#line 169 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIFDesapilaryCompletar("bl", null,null);}
break;
case 64:
//#line 172 "Gramatica.y"
{ArregloTercetos.crearTerceto("IF_START", "_", "_");}
break;
case 65:
//#line 175 "Gramatica.y"
{
                                ArregloTercetos.apilarTercetoInicialWHILE();

                                ArregloTercetos.crearTerceto("WHILE_START", "_", "_");
                              }
break;
case 66:
//#line 182 "Gramatica.y"
{ArregloTercetos.completarTercetoBackPatchingIF();}
break;
case 67:
//#line 183 "Gramatica.y"
{yyerror("Error: falta palabra reservada 'endif'");}
break;
case 68:
//#line 186 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 69:
//#line 187 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(1).sval,null);}
break;
case 70:
//#line 188 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(0).sval,null);}
break;
case 71:
//#line 189 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingIF("bf", val_peek(0).sval,null);}
break;
case 72:
//#line 192 "Gramatica.y"
{ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 73:
//#line 193 "Gramatica.y"
{yyerror("Error: falta parentesis de apertura '(' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(1).sval,null);}
break;
case 74:
//#line 194 "Gramatica.y"
{yyerror("Error: falta parentesis de cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(0).sval,null);}
break;
case 75:
//#line 195 "Gramatica.y"
{yyerror("Error: faltan parentesis de apertura '(' y cierre ')' en condicion"); ArregloTercetos.crearTercetoBackPatchingWHILE("bf", val_peek(0).sval,null);}
break;
case 78:
//#line 202 "Gramatica.y"
{ yyerror("Error: Declaracion de parametro real invalida"); }
break;
case 79:
//#line 205 "Gramatica.y"
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
case 80:
//#line 245 "Gramatica.y"
{ yyerror("Error: Falta definir el nombre del parametro formal"); }
break;
case 81:
//#line 246 "Gramatica.y"
{ yyerror("Error: Falta '->' en la especificacion de parametro real"); }
break;
case 82:
//#line 249 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, val_peek(1).sval); }
break;
case 83:
//#line 250 "Gramatica.y"
{ yyerror("Error: falta operador de comparacion en la condicion"); }
break;
case 84:
//#line 253 "Gramatica.y"
{ yyval.sval = "=="; }
break;
case 85:
//#line 254 "Gramatica.y"
{ yyval.sval = ">="; }
break;
case 86:
//#line 255 "Gramatica.y"
{ yyval.sval = "<="; }
break;
case 87:
//#line 256 "Gramatica.y"
{ yyval.sval = "=!"; }
break;
case 88:
//#line 257 "Gramatica.y"
{ yyval.sval = ">"; }
break;
case 89:
//#line 258 "Gramatica.y"
{ yyval.sval = "<"; }
break;
case 90:
//#line 259 "Gramatica.y"
{yyerror("Error: Operador invalido");}
break;
case 91:
//#line 260 "Gramatica.y"
{yyerror("Error: Operador invalido");}
break;
case 92:
//#line 261 "Gramatica.y"
{yyerror("Error: Operador invalido");}
break;
case 93:
//#line 262 "Gramatica.y"
{yyerror("Error: Operador invalido");}
break;
case 94:
//#line 266 "Gramatica.y"
{ reportarEstructura("asignacion simple");
                                                               yyval = ArregloTercetos.crearTerceto(":=", val_peek(2).sval, val_peek(0).sval);
                                                               yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, ":=");
                                                               }
break;
case 95:
//#line 272 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = val_peek(0).tipo; }
break;
case 96:
//#line 276 "Gramatica.y"
{
                              reportarEstructura("asignacion multiple");
                              ControlAsigMultiple.compararTipos();
                              crearTercetosAsigMultiple(val_peek(2).sval, val_peek(0).sval);
                            }
break;
case 97:
//#line 284 "Gramatica.y"
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
case 98:
//#line 297 "Gramatica.y"
{
                              ArregloTercetos.crearTerceto("fin_" + val_peek(1).sval, "_", "_");
                            }
break;
case 99:
//#line 301 "Gramatica.y"
{
                              String tipoFormal = obtenerTipoDeSimbolo(val_peek(5).tipo);
                              chequearTipos(tipoFormal, val_peek(0).tipo, ":=");
                              ArregloTercetos.crearTerceto(":=", val_peek(5).tipo, val_peek(0).sval);
                              ArregloTercetos.crearTerceto("CALL", val_peek(5).sval, null);
                            }
break;
case 100:
//#line 310 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = val_peek(0).tipo; }
break;
case 101:
//#line 311 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); }
break;
case 102:
//#line 312 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 103:
//#line 315 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, val_peek(1).sval); }
break;
case 104:
//#line 316 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 105:
//#line 317 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 106:
//#line 318 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 107:
//#line 319 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 108:
//#line 320 "Gramatica.y"
{ yyerror("Error: error en expresion, revisar '+' '-'"); }
break;
case 109:
//#line 323 "Gramatica.y"
{ yyval.sval = "+"; }
break;
case 110:
//#line 324 "Gramatica.y"
{ yyval.sval = "-"; }
break;
case 111:
//#line 328 "Gramatica.y"
{yyval = ArregloTercetos.crearTerceto(val_peek(1).sval, val_peek(2).sval, val_peek(0).sval); yyval.tipo = chequearTipos(val_peek(2).tipo, val_peek(0).tipo, val_peek(1).sval); }
break;
case 112:
//#line 329 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo;}
break;
case 113:
//#line 330 "Gramatica.y"
{ yyerror("Error: operando a la izquierda invalido"); }
break;
case 114:
//#line 331 "Gramatica.y"
{ yyerror("Error: operando a la derecha invalido"); }
break;
case 115:
//#line 332 "Gramatica.y"
{ yyerror("Error: operandos a la izquierda y derecha invalidos"); }
break;
case 116:
//#line 335 "Gramatica.y"
{ yyval.sval = "*"; }
break;
case 117:
//#line 336 "Gramatica.y"
{ yyval.sval = "/"; }
break;
case 118:
//#line 340 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo; }
break;
case 119:
//#line 341 "Gramatica.y"
{yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); yyval = val_peek(0); }
break;
case 120:
//#line 342 "Gramatica.y"
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
case 121:
//#line 354 "Gramatica.y"
{ yyval = constanteNegativa(val_peek(0)); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval);}
break;
case 122:
//#line 355 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = "ulong"; }
break;
case 123:
//#line 359 "Gramatica.y"
{yyval = val_peek(0); yyval.tipo = val_peek(0).tipo; }
break;
case 124:
//#line 360 "Gramatica.y"
{yyval.tipo = obtenerTipoDeSimbolo(val_peek(0).sval); yyval = val_peek(0); }
break;
case 125:
//#line 361 "Gramatica.y"
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
case 126:
//#line 373 "Gramatica.y"
{ yyval = val_peek(0); yyval.tipo = "ulong"; }
break;
case 127:
//#line 376 "Gramatica.y"
{String tipo = obtenerTipoDeSimbolo(val_peek(1).sval);
                                          if(! tipo.equals("dfloat")){
                                             yyerror("Error: La funcion TRUNC solo acepta operandos de tipo dfloat.");
                                          }
                                          yyval = ArregloTercetos.crearTerceto("TRUNC", val_peek(1).sval, null);
                                          yyval.tipo = "ulong";}
break;
case 128:
//#line 383 "Gramatica.y"
{ yyerror("Error: falta ')' en la expresion TRUNC"); }
break;
case 129:
//#line 384 "Gramatica.y"
{ yyerror("Error: falta '(' en la expresion TRUNC"); }
break;
case 130:
//#line 385 "Gramatica.y"
{ yyerror("Error: faltan '(' y ')' en la expresion TRUNC"); }
break;
case 131:
//#line 391 "Gramatica.y"
{
                             /* CORRECCIÓN 1: Usar $$.sval*/
                             PilaDeFuncionesLlamadas.iniciarLlamada(yyval.sval);
                           }
break;
case 132:
//#line 397 "Gramatica.y"
{ yyval = chequearAmbito("", ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
case 133:
//#line 398 "Gramatica.y"
{ yyval = chequearAmbito(val_peek(2).sval, ambito, val_peek(0).sval); yyval.tipo = obtenerTipoDeSimbolo(yyval.sval); }
break;
//#line 1573 "Parser.java"
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
