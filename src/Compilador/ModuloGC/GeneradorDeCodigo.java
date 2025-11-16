/*package Compilador.ModuloGC;

import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.ModuloLexico.ElementoTablaDeSimbolos;
import Compilador.ModuloSemantico.Terceto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GeneradorDeCodigo {
    private ArrayList<Terceto> lista_tercetos;
    private int auxs;
    private StringBuilder seccion_datos;
    private StringBuilder seccion_codigo;
    private StringBuilder assembler;
    private String nombrePrograma;
    private Set<String> declarados;
    private boolean printBufDeclarado = false;

    public GeneradorDeCodigo(ArrayList<Terceto> tercetos, String nombrePrograma) {
        this.lista_tercetos = tercetos;
        this.nombrePrograma = nombrePrograma;
        this.seccion_codigo = new StringBuilder();
        this.seccion_datos = new StringBuilder();
        this.assembler = new StringBuilder();
        this.declarados = new HashSet<>();
        this.auxs = 0;
    }

    public void generarCodigo() {
        generarEncabezadoArchivo();
        generarDatosConTS();
        for (int i = 0; i < lista_tercetos.size(); i++) {
            traducirTerceto(lista_tercetos.get(i), i);
        }
        assembler.append("\n.data\n").append(seccion_datos);
        assembler.append("\n.code\n").append(nombrePrograma).append(":\n");
        assembler.append(seccion_codigo);
        assembler.append("    invoke ExitProcess, 0\n");
        assembler.append("end ").append(nombrePrograma).append("\n");
    }

    private void generarEncabezadoArchivo() {
        assembler.append(".586\n");
        assembler.append(".model flat, stdcall\n");
        assembler.append("option casemap:none\n");
        assembler.append("include \\masm32\\include\\windows.inc\n");
        assembler.append("include \\masm32\\include\\kernel32.inc\n");
        assembler.append("include \\masm32\\include\\user32.inc\n");
        assembler.append("include \\masm32\\include\\masm32.inc\n");
        assembler.append("includelib \\masm32\\lib\\kernel32.lib\n");
        assembler.append("includelib \\masm32\\lib\\user32.lib\n");
        assembler.append("includelib \\masm32\\lib\\masm32.lib\n");
    }

    private void generarDatosConTS() {
        for (ElementoTablaDeSimbolos e : TablaDeSimbolos.getElementos()) {
            String uso = e.getUso();
            if (uso == null || "Función".equals(uso)) continue;

            String lexema = TablaDeSimbolos.getLexema(e);
            String asmName = sanitizar(lexema);
            if (declarados.contains(asmName)) continue;

            String tipo = e.getTipo();
            if (tipo == null) continue;

            String dir;
            switch (tipo.toLowerCase()) {
                case "ulong": dir = "dd"; break;
                case "double":
                case "dfloat": dir = "real8"; break;
                default: dir = "dd"; break;
            }
            seccion_datos.append(asmName).append(" ").append(dir).append(" ?\n");
            declarados.add(asmName);
        }
    }

    public void traducirTerceto(Terceto t, int idx) {
        String op = t.getOperador();
        String a1 = traducirOperando(t.getOp1());
        String a2 = traducirOperando(t.getOp2());
        String res;
        boolean eslambda = op != null && op.contains("lambda");
        String inicio = null;

        if (op.startsWith("ini_")) {
            inicio = op.substring(eslambda ? 5 : 4);
            op = eslambda ? "LAMBDA" : "FUNC_BEGIN";
        } else if (op.startsWith("fin_")) {
            op = eslambda ? "LAMBDA" : "FUNC_END";
        }

        switch (op) {
            case "+":
                res = nuevaAuxiliar();
                seccion_codigo.append("    mov eax, ").append(a1).append("\n");
                seccion_codigo.append("    add eax, ").append(a2).append("\n");
                seccion_codigo.append("    mov ").append(res).append(", eax\n");
                t.setResultado(res);
                break;
            case "-":
                res = nuevaAuxiliar();
                seccion_codigo.append("    mov eax, ").append(a1).append("\n");
                seccion_codigo.append("    sub eax, ").append(a2).append("\n");
                seccion_codigo.append("    mov ").append(res).append(", eax\n");
                t.setResultado(res);
                break;
            case "*":
                res = nuevaAuxiliar();
                seccion_codigo.append("    mov eax, ").append(a1).append("\n");
                seccion_codigo.append("    mul ").append(a2).append("\n");
                seccion_codigo.append("    mov ").append(res).append(", eax\n");
                t.setResultado(res);
                break;
            case "/":
                res = nuevaAuxiliar();
                seccion_codigo.append("    mov eax, ").append(a1).append("\n");
                seccion_codigo.append("    mov edx, 0\n");
                seccion_codigo.append("    div ").append(a2).append("\n");
                seccion_codigo.append("    mov ").append(res).append(", eax\n");
                t.setResultado(res);
                break;
            case ":=":
                String dst = asegurarVariable(a1);
                String src = a2;
                if (esMem(dst) && esMem(src)) {
                    seccion_codigo.append("    mov eax, ").append(src).append("\n");
                    seccion_codigo.append("    mov ").append(dst).append(", eax\n");
                } else {
                    seccion_codigo.append("    mov ").append(dst).append(", ").append(src).append("\n");
                }
                t.setResultado(dst);
                break;
            case "PRINT":
                // Buffer para convertir número a texto
                asegurarBufferImpresion();     // declara _buf (y _nl si ya estaba)
                // Título para el MessageBox
                asegurarTituloMessageBox();

                // Mover el operando a EAX (inmediato o memoria)
                seccion_codigo.append("    mov eax, ").append(a1).append("\n");

                // Convertir a texto y mostrar en MessageBox
                seccion_codigo.append("    invoke dwtoa, eax, addr _buf\n");
                seccion_codigo.append("    invoke MessageBox, NULL, addr _buf, addr _mbTitle, MB_OK or MB_ICONINFORMATION\n");
                break;
            case "CALL":
                seccion_codigo.append("    call ").append(a1).append("\n");
                break;
            case "JMP":
                seccion_codigo.append("    jmp ").append(a1).append("\n");
                break;
            case "FUNC_BEGIN":
                seccion_codigo.append(sanitizar(inicio)).append(":\n");
                break;
            case "FUNC_END":
                seccion_codigo.append("    ret\n\n");
                break;
            case "LAMBDA":
                //En caso del lambda, no hacemos nada ya que no es una función que pueda ser llamada desde afuera de la línea
                break;
            default:
                break;
        }
    }



    private boolean esMem(String op) {
        if (op == null) return false;
        String r = op.toLowerCase();
        if (r.equals("eax") || r.equals("ebx") || r.equals("ecx") || r.equals("edx")) return false;
        if (op.matches("\\d+")) return false; // inmediato
        return op.matches("[A-Za-z_@][A-Za-z0-9_@]*"); // etiqueta en .data
    }

    private String traducirOperando(String op) {
        if (op == null) return "";
        if (op.matches("\\d+")) {
            int i = Integer.parseInt(op);
            return lista_tercetos.get(i).getResultado();
        }
        if (op.matches("\\d+UL")) return op.substring(0, op.length() - 2);
        return sanitizar(op);
    }

    private String nuevaAuxiliar() {
        String n = "@aux" + auxs++;
        if (!declarados.contains(n)) {
            seccion_datos.append(n).append(" dd ?\n");
            declarados.add(n);
        }
        return n;
    }

    private String sanitizar(String nombre) {
        if (nombre == null) return "_";
        String limpio = nombre.replace(':', '_').replaceAll("[^A-Za-z0-9_@$]", "_");
        if (limpio.isEmpty()) limpio = "_";
        if (Character.isDigit(limpio.charAt(0))) limpio = "_" + limpio;
        return limpio;
    }

    private boolean esInmediato(String s) {
        return s != null && s.matches("\\d+(UL)?");
    }

    private void asegurarTituloMessageBox() {
        if (!declarados.contains("_mbTitle")) {
            seccion_datos.append("_mbTitle db \"Resultado\",0\n");
            declarados.add("_mbTitle");
        }
    }

    private void asegurarBufferImpresion() {
        if (!printBufDeclarado) {
            seccion_datos.append("_buf db 32 dup(0)\n");
            seccion_datos.append("_nl  db 13,10,0\n");
            declarados.add("_buf");
            declarados.add("_nl");
            printBufDeclarado = true;
        }
    }

    private String asegurarVariable(String id) {
        String n = id;
        if (!declarados.contains(n)) {
            seccion_datos.append(n).append(" dd ?\n");
            declarados.add(n);
        }
        return n;
    }

    public void imprimirAssembler() {
        System.out.println("===== CODIGO ASSEMBLER =====");
        System.out.print(assembler.toString());
        System.out.println("=============================");
    }

  public void producirArchivoMASM() {
      java.nio.file.Path destino = obtenerPathEscritorio().resolve(nombrePrograma + ".asm");
      try {
          java.nio.file.Files.writeString(
                  destino,
                  assembler.toString(),
                  java.nio.charset.StandardCharsets.ISO_8859_1
          );
          System.out.println("Archivo MASM generado en: " + destino);
      } catch (java.io.IOException e) {
          System.err.println("Error al generar archivo MASM: " + e.getMessage());
      }
  }

  private java.nio.file.Path obtenerPathEscritorio() {
      String home = System.getProperty("user.home");
      String os = System.getProperty("os.name").toLowerCase();
      java.nio.file.Path desktop = java.nio.file.Paths.get(home, "Desktop"); // Nombre físico persiste (Windows/Mac/Linux)
      if (!java.nio.file.Files.isDirectory(desktop)) {
          // Fallback si no existe
          desktop = java.nio.file.Paths.get(home);
      }
      return desktop;
  }
}*/
// java
package Compilador.ModuloGC;

import Compilador.ModuloLexico.ElementoTablaDeSimbolos;
import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.ModuloSemantico.Terceto;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

public class GeneradorDeCodigo {
    private final ArrayList<Terceto> listaTercetos;
    private final String nombrePrograma;

    private final StringBuilder assembler = new StringBuilder();
    private final StringBuilder seccionGlobals = new StringBuilder();
    private final StringBuilder seccionData = new StringBuilder();
    private final Map<String, StringBuilder> funciones = new LinkedHashMap<>();
    private final Stack<String> pilaFunciones = new Stack<>();
    private final Stack<String> pilaScopes = new Stack<>();
    private final Map<String, Boolean> funcionTieneReturn = new HashMap<>();

    private final Map<String, String> tiposGlobal = new HashMap<>(); // nombre -> i32|f64

    private final Map<String, Long> valoresEnteros = new HashMap<>();
    private final Map<String, Double> valoresFlotantes = new HashMap<>();
    private int contadorTemps = 0;

    // Pool de cadenas
    private final Map<String, Integer> offsetCadena = new HashMap<>();
    private final Map<String, Integer> longitudCadena = new HashMap<>();
    private int siguienteOffsetData = 0;

    private int contadorAuxiliares = 0;
    private String funcionActual = "main";
    private String scopeActual = null;

    // Patrones del lenguaje
    private static final Pattern P_ULONG = Pattern.compile("\\d+UL");
    private static final Pattern P_DFLOAT = Pattern.compile("[+\\-]?\\d+\\.\\d+D[+\\-]?\\d+");

    public GeneradorDeCodigo(ArrayList<Terceto> tercetos, String nombrePrograma) {
        this.listaTercetos = tercetos;
        this.nombrePrograma = nombrePrograma;
    }

    public void generarCodigo() {
        assembler.append("(module\n");
        assembler.append("  (import \"env\" \"alert_i32\" (func $alert_i32 (param i32)))\n");
        assembler.append("  (import \"env\" \"alert_str\" (func $alert_str (param i32 i32)))\n");
        assembler.append("  (memory (export \"memory\") 1)\n");

        declararVariablesGlobalesDesdeTS();

        funciones.putIfAbsent("main", new StringBuilder());
        funcionActual = "main";

        for (int i = 0; i < listaTercetos.size(); i++) {
            generarInstruccionesDesdeTerceto(listaTercetos.get(i), i);
        }

        assembler.append(seccionGlobals);
        assembler.append(seccionData);

        for (Map.Entry<String, StringBuilder> entrada : funciones.entrySet()) {
            String nombre = entrada.getKey();
            StringBuilder cuerpo = entrada.getValue();
            assembler.append("\n  (func $").append(nombre);
            if (!"main".equals(nombre)) {
                assembler.append(" (result i32)");
            } else {
                assembler.append(" (export \"main\")");
            }
            assembler.append("\n");
            assembler.append(cuerpo);
            if (!"main".equals(nombre) && !funcionTieneReturn.getOrDefault(nombre, false)) {
                assembler.append("    i32.const 0\n");
                assembler.append("    return\n");
            }
            assembler.append("  )\n");
        }

        assembler.append(")\n");
    }

    private void declararVariablesGlobalesDesdeTS() {
        for (ElementoTablaDeSimbolos e : TablaDeSimbolos.getElementos()) {
            String uso = e.getUso();
            if (uso == null || "Función".equals(uso)) continue;

            String lexema = TablaDeSimbolos.getLexema(e);
            if (lexema == null) continue;

            if (esLiteralCadena(lexema)) {
                registrarCadenaEnData(lexema);
                continue;
            }
            if (esLiteralEnteroSinSigno(lexema) || esLiteralFlotante(lexema)) continue;

            String nombreWasm = limpiarNombre(lexema);
            if (tiposGlobal.containsKey(nombreWasm)) continue;

            seccionGlobals.append("  (global $")
                    .append(nombreWasm)
                    .append(" (mut i32) (i32.const 0))\n");
            tiposGlobal.put(nombreWasm, "i32");
        }
    }

    private void generarInstruccionesDesdeTerceto(Terceto t, int idx) {
        String op = t.getOperador();
        boolean esLambda = op != null && op.contains("lambda");
        String inicioRaw = null;

        if (op.startsWith("ini_")) {
            inicioRaw = op.substring(esLambda ? 5 : 4);
            op = "FUNC_BEGIN";
        } else if (op.startsWith("fin_")) {
            op = "FUNC_END";
        }

        StringBuilder cuerpo = funciones.computeIfAbsent(funcionActual, k -> new StringBuilder());

        switch (op) {
            case "+" -> {
                String a1 = resolverOperando(t.getOp1());
                String a2 = resolverOperando(t.getOp2());
                emitirPushEntero(a1, cuerpo);
                emitirPushEntero(a2, cuerpo);
                cuerpo.append("    i32.add\n");
                String tmp = crearTempEntero();
                cuerpo.append("    global.set $").append(tmp).append("\n");
                t.setResultado(tmp);
            }
            case "-" -> {
                String a1 = resolverOperando(t.getOp1());
                String a2 = resolverOperando(t.getOp2());
                emitirPushEntero(a1, cuerpo);
                emitirPushEntero(a2, cuerpo);
                cuerpo.append("    i32.sub\n");
                String tmp = crearTempEntero();
                cuerpo.append("    global.set $").append(tmp).append("\n");
                t.setResultado(tmp);
            }
            case "*" -> {
                String a1 = resolverOperando(t.getOp1());
                String a2 = resolverOperando(t.getOp2());
                boolean usarF64 = debeUsarFlotante(a1, a2);
                if (usarF64) {
                    emitirPushFlotante(a1, cuerpo);
                    emitirPushFlotante(a2, cuerpo);
                    cuerpo.append("    f64.mul\n");
                    String aux = crearVariableTemporal("f64");
                    cuerpo.append("    global.set $").append(aux).append("\n");
                    t.setResultado(aux);
                    Double v1 = obtenerValorFlotanteConstante(a1);
                    Double v2 = obtenerValorFlotanteConstante(a2);
                    if (v1 != null && v2 != null) valoresFlotantes.put(aux, v1 * v2);
                } else {
                    emitirPushEntero(a1, cuerpo);
                    emitirPushEntero(a2, cuerpo);
                    cuerpo.append("    i32.mul\n");
                    String aux = crearVariableTemporal("i32");
                    cuerpo.append("    global.set $").append(aux).append("\n");
                    t.setResultado(aux);
                    Long v1 = obtenerValorEnteroConstante(a1);
                    Long v2 = obtenerValorEnteroConstante(a2);
                    if (v1 != null && v2 != null) valoresEnteros.put(aux, (v1 * v2) & 0xFFFFFFFFL);
                }
            }
            case "/" -> {
                String a1 = resolverOperando(t.getOp1());
                String a2 = resolverOperando(t.getOp2());

                boolean a1f = esLiteralFlotante(a1) || esGlobalFlotante(a1);
                boolean a2f = esLiteralFlotante(a2) || esGlobalFlotante(a2);

                if (!a1f && !a2f) {
                    emitirPushEntero(a1, cuerpo);
                    emitirPushEntero(a2, cuerpo);
                    cuerpo.append("    i32.div_u\n");
                    String tmp = crearTempEntero();
                    cuerpo.append("    global.set $").append(tmp).append("\n");
                    t.setResultado(tmp);
                } else {
                    emitirPushFlotante(a1, cuerpo);
                    emitirPushFlotante(a2, cuerpo);
                    cuerpo.append("    f64.div\n");
                    String tmpf = "_tf" + (contadorTemps++);
                    asegurarGlobalFlotante(tmpf);
                    cuerpo.append("    global.set $").append(limpiarNombre(tmpf)).append("\n");
                    t.setResultado(tmpf);
                }
            }
            case ":=" -> {
                String destinoId = resolverOperando(t.getOp1());
                String origen = resolverOperando(t.getOp2());
                String destino = asegurarGlobalEntero(destinoId);
                if (esGlobalFlotante(origen) || esLiteralFlotante(origen)) {
                    emitirPushFlotante(origen, cuerpo);
                    cuerpo.append("    i32.trunc_f64_u\n");
                } else {
                    emitirPushEntero(origen, cuerpo);
                }
                cuerpo.append("    global.set $").append(destino).append("\n");
                t.setResultado(destino);
                if (esLiteralEnteroSinSigno(origen)) {
                    long v = Long.parseLong(origen.substring(0, origen.length() - 2));
                    valoresEnteros.put(destino, v);
                }
            }
            case "PRINT" -> {
                String rawOp = t.getOp1();
                String a1 = resolverOperando(rawOp);

                if (rawOp != null && esLiteralCadena(rawOp)) {
                    int off = registrarCadenaEnData(rawOp);
                    int len = longitudCadena.getOrDefault(rawOp, 0);
                    cuerpo.append("    i32.const ").append(off).append("\n");
                    cuerpo.append("    i32.const ").append(len).append("\n");
                    cuerpo.append("    call $alert_str\n");
                } else {
                    emitirPushEntero(a1, cuerpo);
                    cuerpo.append("    call $alert_i32\n");
                }
                t.setResultado("");
            }
            case "TRUNC" -> {
                String a1 = resolverOperando(t.getOp1());

                if (esLiteralFlotante(a1) || esGlobalFlotante(a1)) {
                    emitirPushFlotante(a1, cuerpo);
                    cuerpo.append("    f64.abs\n");
                    cuerpo.append("    f64.trunc\n");
                    cuerpo.append("    i32.trunc_f64_u\n");
                } else {
                    emitirPushEntero(a1, cuerpo);
                }
                String tmp = crearTempEntero();
                cuerpo.append("    global.set $").append(tmp).append("\n");
                t.setResultado(tmp);
            }
            case "CALL" -> {
                String raw = t.getOp1();
                String nombreFuncion = raw;
                String mapeo = null;

                if (raw != null && raw.contains("(") && raw.endsWith(")")) {
                    int p = raw.indexOf('(');
                    nombreFuncion = raw.substring(0, p).trim();
                    mapeo = raw.substring(p + 1, raw.length() - 1).trim();
                }

                // Paso 1: setear parámetros (igual que ya haces)
                if (mapeo != null && mapeo.contains("->")) {
                    String[] pares = mapeo.split(",");
                    for (String par : pares) {
                        String s = par.trim();
                        if (s.isEmpty()) continue;
                        int flecha = s.indexOf("->");
                        if (flecha < 0) continue;
                        String arg = s.substring(0, flecha).trim();
                        String param = s.substring(flecha + 2).trim();
                        if (arg.isEmpty() || param.isEmpty()) continue;

                        String valorArg = resolverOperando(arg);

                        String lexParam = param;
                        for (ElementoTablaDeSimbolos e : TablaDeSimbolos.getElementos()) {
                            String lex = TablaDeSimbolos.getLexema(e);
                            if (lex == null) continue;
                            if (lex.startsWith(param + ":") && lex.contains(":" + nombreFuncion)) {
                                lexParam = lex;
                                break;
                            }
                        }

                        String globalParam = asegurarGlobalEntero(
                                resolverIdentificadorEnTS(lexParam)
                        );
                        emitirPushEntero(valorArg, cuerpo);
                        cuerpo.append("    global.set $")
                                .append(limpiarNombre(globalParam))
                                .append("\n");
                    }
                }

                // Paso 2: llamar a la función
                String nombreWasm = resolverNombreFuncionEnTS(nombreFuncion);
                cuerpo.append("    call $").append(nombreWasm).append("\n");

                // Paso 3: guardar el valor devuelto (en la pila) en un temporal global
                String tmp = crearTempEntero();
                cuerpo.append("    global.set $").append(tmp).append("\n");
                t.setResultado(tmp);
            }
            case "RETURN", "return" -> {
                String opRet = t.getOp1();

                if (opRet == null || opRet.isBlank()) {
                    // return sin expresión: devuelve 0
                    cuerpo.append("    i32.const 0\n");
                } else {
                    String val = resolverOperando(opRet);

                    if (esLiteralEnteroSinSigno(val)) {
                        // literal tipo 65UL
                        cuerpo.append("    i32.const ")
                                .append(val.substring(0, val.length() - 2))
                                .append("\n");
                    } else if (esLiteralFlotante(val) || esGlobalFlotante(val)) {
                        // si en el futuro devuelves flotantes, aquí iría la lógica f64
                        emitirPushFlotante(val, cuerpo);
                        cuerpo.append("    i32.trunc_f64_u\n");
                    } else {
                        // variable/global/temporal
                        cuerpo.append("    global.get $")
                                .append(limpiarNombre(val))
                                .append("\n");
                    }
                }

                funcionTieneReturn.put(funcionActual, true);
                cuerpo.append("    return\n");
                t.setResultado(opRet);
            }
            case "JMP" -> {
                String a1 = resolverOperando(t.getOp1());
                cuerpo.append("    ;; jmp ").append(a1).append("\n");
            }
            case "FUNC_BEGIN" -> {
                String fnLexTS = normalizarNombreFuncionEnTS(inicioRaw);
                String fnWasm = limpiarNombre(fnLexTS);
                pilaFunciones.push(funcionActual);
                pilaScopes.push(scopeActual == null ? "" : scopeActual);
                funcionActual = fnWasm;
                funciones.putIfAbsent(funcionActual, new StringBuilder());
                int p = fnLexTS.indexOf(':');
                scopeActual = (p > 0 && p < fnLexTS.length() - 1)
                        ? fnLexTS.substring(p + 1)
                        : "";
            }
            case "FUNC_END" -> {
                funcionActual = pilaFunciones.isEmpty() ? "main" : pilaFunciones.pop();
                scopeActual = pilaScopes.isEmpty() ? "" : pilaScopes.pop();
            }
            case "LAMBDA" -> {
                // no-op
            }
            default -> {
                // no-op
            }
        }
    }

    private boolean esEntero(String val) {
        if (val == null || val.isBlank()) return false;
        if (val.matches("\\d+")) return true;
        if (val.endsWith("UL") && val.substring(0, val.length() - 2).matches("\\d+")) return true;
        String k = limpiarNombre(val);
        return "i32".equals(tiposGlobal.get(k));
    }

    private boolean debeUsarFlotante(String a1, String a2) {
        return esLiteralFlotante(a1) || esLiteralFlotante(a2) || esGlobalFlotante(a1) || esGlobalFlotante(a2);
    }

    private String crearVariableTemporal(String tipo) {
        String nombre = "aux" + contadorAuxiliares++;
        if (!tiposGlobal.containsKey(nombre)) {
            if ("f64".equals(tipo)) {
                seccionGlobals.append("  (global $")
                        .append(nombre)
                        .append(" (mut f64) (f64.const 0))\n");
                tiposGlobal.put(nombre, "f64");
            } else {
                seccionGlobals.append("  (global $")
                        .append(nombre)
                        .append(" (mut i32) (i32.const 0))\n");
                tiposGlobal.put(nombre, "i32");
            }
        }
        return nombre;
    }

    private boolean esGlobalFlotante(String val) {
        if (val == null || val.isEmpty()) return false;
        String id = limpiarNombre(val);
        return "f64".equals(tiposGlobal.get(id));
    }

    private String crearTempEntero() {
        String id = "_t" + (contadorTemps++);
        asegurarGlobalEntero(id);
        return id;
    }

    private String asegurarGlobalEntero(String id) {
        String k = limpiarNombre(id);
        if (!tiposGlobal.containsKey(k)) {
            seccionGlobals.append("  (global $")
                    .append(k)
                    .append(" (mut i32) (i32.const 0))\n");
            tiposGlobal.put(k, "i32");
        }
        return k;
    }

    private void asegurarGlobalFlotante(String id) {
        String k = limpiarNombre(id);
        if (!tiposGlobal.containsKey(k)) {
            seccionGlobals.append("  (global $")
                    .append(k)
                    .append(" (mut f64) (f64.const 0))\n");
            tiposGlobal.put(k, "f64");
        }
    }

    private void emitirPushEntero(String val, StringBuilder cuerpo) {
        if (val == null || val.isEmpty()) {
            cuerpo.append("    i32.const 0\n");
            return;
        }
        if (esLiteralCadena(val)) {
            cuerpo.append("    i32.const 0\n");
            return;
        }
        if (esLiteralEnteroSinSigno(val)) {
            cuerpo.append("    i32.const ").append(val.substring(0, val.length() - 2)).append("\n");
            return;
        }
        if (esLiteralFlotante(val) || esGlobalFlotante(val)) {
            emitirPushFlotante(val, cuerpo);
            cuerpo.append("    i32.trunc_f64_u\n");
            return;
        }
        cuerpo.append("    global.get $").append(limpiarNombre(val)).append("\n");
    }

    private void emitirPushFlotante(String val, StringBuilder cuerpo) {
        if (val == null || val.isEmpty()) {
            cuerpo.append("    f64.const 0\n");
            return;
        }
        if (esLiteralCadena(val)) {
            cuerpo.append("    f64.const 0\n");
            return;
        }
        if (esLiteralFlotante(val)) {
            String s = val.replace('d', 'E').replace('D', 'E');
            cuerpo.append("    f64.const ").append(s).append("\n");
            return;
        }
        if (esLiteralEnteroSinSigno(val)) {
            cuerpo.append("    i32.const ").append(val.substring(0, val.length() - 2)).append("\n");
            cuerpo.append("    f64.convert_i32_u\n");
            return;
        }
        String id = limpiarNombre(val);
        cuerpo.append("    global.get $").append(id).append("\n");
        if (!esGlobalFlotante(val)) {
            cuerpo.append("    f64.convert_i32_u\n");
        }
    }

    private boolean esLiteralEnteroSinSigno(String s) {
        return s != null && P_ULONG.matcher(s.trim()).matches();
    }

    private boolean esLiteralFlotante(String s) {
        return s != null && P_DFLOAT.matcher(s.trim()).matches();
    }

    private boolean esLiteralCadena(String s) {
        return s != null && s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"");
    }

    private Double parsearLiteralFlotante(String lit) {
        return Double.parseDouble(lit.trim().replace('d', 'E').replace('D', 'E'));
    }

    private Long obtenerValorEnteroConstante(String v) {
        if (v == null) return null;
        if (esLiteralEnteroSinSigno(v)) {
            return Long.parseLong(v.substring(0, v.length() - 2));
        }
        return valoresEnteros.get(v);
    }

    private Double obtenerValorFlotanteConstante(String v) {
        if (v == null) return null;
        if (esLiteralFlotante(v)) return parsearLiteralFlotante(v);
        if (esLiteralEnteroSinSigno(v)) {
            return Double.parseDouble(v.substring(0, v.length() - 2));
        }
        if (valoresFlotantes.containsKey(v)) return valoresFlotantes.get(v);
        if (valoresEnteros.containsKey(v)) return valoresEnteros.get(v).doubleValue();
        return null;
    }

    private String resolverOperando(String op) {
        if (op == null) return "";
        if (op.matches("\\[\\d+\\]")) {
            int i = Integer.parseInt(op.substring(1, op.length() - 1));
            return listaTercetos.get(i).getResultado();
        }
        if (op.matches("\\d+")) {
            int i = Integer.parseInt(op);
            return listaTercetos.get(i).getResultado();
        }
        if (esLiteralEnteroSinSigno(op) || esLiteralFlotante(op) || esLiteralCadena(op)) {
            return op.trim();
        }
        String lex = resolverIdentificadorEnTS(op);
        return limpiarNombre(lex);
    }

    private String resolverIdentificadorEnTS(String raw) {
        if (raw == null) return "_";
        String id = raw.trim();
        if (id.isEmpty()) return "_";
        if (id.contains(":")) return id;
        if (scopeActual != null && !scopeActual.isBlank()) {
            String lex = id + ":" + scopeActual;
            if (existeLexemaEnTS(lex)) return lex;
        }
        String porNombre = buscarPorNombreEnTS(id);
        return porNombre != null ? porNombre : id;
    }

    private boolean existeLexemaEnTS(String lexema) {
        for (ElementoTablaDeSimbolos e : TablaDeSimbolos.getElementos()) {
            String lex = TablaDeSimbolos.getLexema(e);
            if (lexema.equals(lex)) return true;
        }
        return false;
    }

    private String buscarPorNombreEnTS(String nombre) {
        String fallback = null;
        for (ElementoTablaDeSimbolos e : TablaDeSimbolos.getElementos()) {
            String lex = TablaDeSimbolos.getLexema(e);
            if (lex == null) continue;
            if (lex.startsWith(nombre + ":")) {
                if (scopeActual != null && lex.endsWith(":" + scopeActual)) return lex;
                if (fallback == null) fallback = lex;
            }
        }
        return fallback;
    }

    private String resolverNombreFuncionEnTS(String raw) {
        if (raw == null) return "_";
        String r = raw.trim();
        if (r.isEmpty()) return "_";

        if (r.contains(":")) {
            String[] partes = r.split(":");
            int mainIdx = -1;
            for (int i = 0; i < partes.length; i++) {
                if ("MAIN".equals(partes[i])) {
                    mainIdx = i;
                    break;
                }
            }
            if (mainIdx >= 0) {
                List<String> lista = new ArrayList<>(Arrays.asList(partes));
                Collections.rotate(lista, -mainIdx);
                r = String.join(":", lista);
            }
        }
        return limpiarNombre(r);
    }

    private String normalizarNombreFuncionEnTS(String inicioRaw) {
        if (inicioRaw == null || inicioRaw.isBlank()) return "_";
        return inicioRaw.trim();
    }

    private String limpiarNombre(String nombre) {
        if (nombre == null) return "_";
        String limpio = nombre
                .replace(':', '_')
                .replaceAll("[^A-Za-z0-9_@\\$\\-\\.]", "_");
        if (limpio.isEmpty()) limpio = "_";
        if (Character.isDigit(limpio.charAt(0))) limpio = "_" + limpio;
        return limpio;
    }

    private int registrarCadenaEnData(String literal) {
        Integer off = offsetCadena.get(literal);
        if (off != null) return off;
        String sinComillas = literal.substring(1, literal.length() - 1);
        byte[] bytes = sinComillas.getBytes(StandardCharsets.UTF_8);
        int len = bytes.length;
        int nuevoOffset = siguienteOffsetData;
        siguienteOffsetData += len;
        seccionData.append("  (data (i32.const ")
                .append(nuevoOffset)
                .append(") \"")
                .append(escaparLiteralCadenaWasm(bytes))
                .append("\")\n");
        offsetCadena.put(literal, nuevoOffset);
        longitudCadena.put(literal, len);
        return nuevoOffset;
    }

    private String escaparLiteralCadenaWasm(byte[] bytes) {
        StringBuilder out = new StringBuilder();
        for (byte b : bytes) {
            int v = b & 0xFF;
            if (v >= 0x20 && v <= 0x7E && v != '"' && v != '\\') {
                out.append((char) v);
            } else {
                String hex = Integer.toHexString(v);
                if (hex.length() == 1) hex = "0" + hex;
                out.append("\\").append(hex);
            }
        }
        return out.toString();
    }

    public void imprimirAssembler() {
        System.out.println("===== CODIGO WAT =====");
        System.out.print(assembler.toString());
        System.out.println("======================");
    }

    public void guardarArchivoWat() {
        java.nio.file.Path destino = obtenerDirectorioSalida().resolve(nombrePrograma + ".wat");
        try {
            java.nio.file.Files.writeString(destino, assembler.toString());
            System.out.println("Archivo WAT generado en: " + destino);
        } catch (Exception e) {
            System.err.println("Error al generar archivo WAT: " + e.getMessage());
        }
    }

    private java.nio.file.Path obtenerDirectorioSalida() {
        try {
            String projectRoot = System.getProperty("user.dir");
            java.nio.file.Path pkgPath =
                    java.nio.file.Paths.get(projectRoot, "src", "Compilador", "ModuloGC");
            if (!java.nio.file.Files.isDirectory(pkgPath)) {
                java.nio.file.Files.createDirectories(pkgPath);
            }
            return pkgPath;
        } catch (Exception e) {
            return java.nio.file.Paths.get(System.getProperty("user.home"));
        }
    }
}
