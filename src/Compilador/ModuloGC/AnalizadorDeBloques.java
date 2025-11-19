
package Compilador.ModuloGC;

import Compilador.ModuloSemantico.Terceto;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AnalizadorDeBloques {
    private static List<Bloque> bloques = new ArrayList<>();
    private static Stack<Boolean> esBfWhile = new Stack<>();
    private static List<Integer> tercetosbfWhile = new Stack<>();
    private static Stack<Integer> ifStartStack = new Stack<>(); // new: track IF_START indices

    public static void analizarBloques(List<Terceto> tercetos) {
        // limpiar estado si se vuelve a llamar
        bloques.clear();
        esBfWhile.clear();
        tercetosbfWhile.clear();

        ifStartStack.clear();

        for (int i = 0; i < tercetos.size(); i++) {
            Terceto t = tercetos.get(i);

            if (t.getOperador().equals("IF_START")) {
                // push the IF_START index so the next bf can match it directly
                ifStartStack.push(i);
            } else if (t.getOperador().equals("WHILE_START")) {
                esBfWhile.push(true);
            } else if (t.getOperador().equals("bf")) {
                if (!esBfWhile()) { // NO es while, entonces es if
                    Bloque b = new Bloque();
                    // prefer stack-matched IF_START; fallback to original backward scan
                    int inicioIf;
                    if (!ifStartStack.isEmpty()) {
                        inicioIf = ifStartStack.pop();
                    } else {
                        inicioIf = getInicioIf(i, tercetos);
                    }
                    System.out.println("indice bf de IF: " + inicioIf);
                    b.setInicio(inicioIf);
                    //if-else
                    if(tercetos.get(Integer.parseInt(t.getOp2())-1).getOperador().equals("bl")) {
                        b.setEsIfElse(true);
                        b.setFin(Integer.parseInt(tercetos.get(Integer.parseInt(t.getOp2())-1).getOp2()));
                        b.setInicioElse(Integer.parseInt(t.getOp2()));
                    } else { //if solitario
                        b.setEsIfElse(false);
                        b.setFin(Integer.parseInt(t.getOp2()));
                        b.setInicioElse(-1); // no tiene else
                    }
                    bloques.add(b);
                } else {
                    tercetosbfWhile.add(i);
                    esBfWhile.pop();
                }
            } else if (t.getOperador().equals("bl")) {

            } else if (t.getOperador().equals("WHILE_END")) {
                if (!esBfWhile.isEmpty()) esBfWhile.pop();
            }
        }


    }



    public static void imprimirBloques() {
        for (Bloque b : bloques) {
            System.out.println("Bloque IF desde terceto " + b.getInicio() +
                    " hasta terceto " + b.getFin() +
                    " con ELSE en terceto " + b.getInicioElse()
                    + ". Es IF-ELSE: " + b.esIfElse());
        }
    }

    public static boolean esTercetoBfWhile(int indice) {
        return tercetosbfWhile.contains(indice);
    }

    private static boolean esBfWhile() {
        return !esBfWhile.isEmpty() && esBfWhile.peek();
    }
    private static int getInicioIf(int indice, List<Terceto> tercetos) {
        int tercetoInicio = 0;
        for (int i = indice; i >= 0 ; i--) {
            Terceto t = tercetos.get(i);
            if (t.getOperador().equals("IF_START")) {
                tercetoInicio = i;
                break;
            }
        }
        return tercetoInicio;
    }

    public static boolean tieneBloqueInicioAsignado(int indice) {
        for (Bloque b : bloques) {
            if (b.getInicio() == indice) {
                return true;
            }
        }
        return false;
    }
    public static boolean tieneBloqueFinAsignado(int indice) {
        for (Bloque b : bloques) {
            if (b.getFin() == indice) {
                return true;
            }
        }
        return false;
    }
    public static int getNumeroDeFinAsignados(int indice) {
        int contador = 0;
        for (Bloque b : bloques) {
            if (b.getFin() == indice) {
                contador ++;
            }
        }
        return contador;
    }
    public static boolean tieneBloqueElseAsignado(int indice) {
        for (Bloque b : bloques) {
            if (b.getInicioElse() == indice) {
                return true;
            }
        }
        return false;
    }
    public static boolean esUnBloqueIfElse(int indice) {
        for (Bloque b : bloques) {
            // Solo retornamos true si el bloque empieza exactamente en este índice
            // y además fue detectado como if-else.
            if (b.getInicio() == indice && b.esIfElse()) {
                return true;
            }
        }
        return false;
    }
}
