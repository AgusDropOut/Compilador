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

import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.ModuloLexico.ElementoTablaDeSimbolos;
import Compilador.ModuloSemantico.Terceto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class GeneradorDeCodigo {
    private final ArrayList<Terceto> lista_tercetos;
    private int auxs;
    private final StringBuilder assembler;        // Módulo WAT final
    private final StringBuilder seccion_globals;  // Globals
    private final StringBuilder seccion_data;     // Data (cadenas)
    private final Map<String, StringBuilder> funciones; // nombre -> cuerpo
    private String funcionActual;                 // "main" por defecto
    private final String nombrePrograma;
    private final Set<String> declarados;         // globals declarados

    // Pool de cadenas: literal -> offset y longitudes
    private final Map<String, Integer> strOffset = new HashMap<>();
    private final Map<String, Integer> strLength = new HashMap<>();
    private int dataOffset = 0;

    public GeneradorDeCodigo(ArrayList<Terceto> tercetos, String nombrePrograma) {
        this.lista_tercetos = tercetos;
        this.nombrePrograma = nombrePrograma;
        this.assembler = new StringBuilder();
        this.seccion_globals = new StringBuilder();
        this.seccion_data = new StringBuilder();
        this.funciones = new LinkedHashMap<>();
        this.declarados = new HashSet<>();
        this.funcionActual = "main";
        this.auxs = 0;
    }

    public void generarCodigo() {
        // Encabezado e imports
        assembler.append("(module\n");
        assembler.append("  (import \"env\" \"alert_i32\" (func $alert_i32 (param i32)))\n");
        assembler.append("  (import \"env\" \"alert_str\" (func $alert_str (param i32 i32)))\n");
        // Memoria exportada para cadenas
        assembler.append("  (memory (export \"memory\") 1)\n");

        // Declarar variables como globals mutables
        generarGlobalsConTS();

        // Asegurar función main
        funciones.putIfAbsent("main", new StringBuilder());
        funcionActual = "main";

        // Traducir tercetos
        for (int i = 0; i < lista_tercetos.size(); i++) {
            traducirTerceto(lista_tercetos.get(i), i);
        }

        // Emitir globals y data de cadenas
        assembler.append(seccion_globals);
        assembler.append(seccion_data);

        // Emitir funciones (exporta main)
        for (Map.Entry<String, StringBuilder> e : funciones.entrySet()) {
            String nombre = e.getKey();
            StringBuilder cuerpo = e.getValue();
            assembler.append("\n  (func $").append(nombre);
            if ("main".equals(nombre)) {
                assembler.append(" (export \"main\")");
            }
            assembler.append("\n");
            assembler.append(cuerpo);
            assembler.append("  )\n");
        }

        // Cierre
        assembler.append(")\n");
    }

    private void generarGlobalsConTS() {
        for (ElementoTablaDeSimbolos e : TablaDeSimbolos.getElementos()) {
            String uso = e.getUso();
            if (uso == null || "Función".equals(uso)) continue;

            String lexema = TablaDeSimbolos.getLexema(e);
            // Si el lexema es un literal de cadena, lo internamos como data, no como global
            if (esCadenaLiteral(lexema)) {
                internarCadena(lexema);
                continue;
            }

            String wasmName = sanitizar(lexema);
            if (declarados.contains(wasmName)) continue;

            seccion_globals.append("  (global $").append(wasmName).append(" (mut i32) (i32.const 0))\n");
            declarados.add(wasmName);
        }
    }

    private void traducirTerceto(Terceto t, int idx) {
        String op = t.getOperador();
        String a1 = traducirOperando(t.getOp1());
        String a2 = traducirOperando(t.getOp2());
        boolean eslambda = op != null && op.contains("lambda");
        String inicio = null;

        if (op.startsWith("ini_")) {
            inicio = op.substring(eslambda ? 5 : 4);
            op = eslambda ? "LAMBDA" : "FUNC_BEGIN";
        } else if (op.startsWith("fin_")) {
            op = eslambda ? "LAMBDA" : "FUNC_END";
        }

        StringBuilder body = funciones.computeIfAbsent(funcionActual, k -> new StringBuilder());

        switch (op) {
            case "+" -> {
                String res = nuevaAuxiliar();
                emitPush(a1, body);
                emitPush(a2, body);
                body.append("    i32.add\n");
                body.append("    global.set $").append(res).append("\n");
                t.setResultado(res);
            }
            case "-" -> {
                String res = nuevaAuxiliar();
                emitPush(a1, body);
                emitPush(a2, body);
                body.append("    i32.sub\n");
                body.append("    global.set $").append(res).append("\n");
                t.setResultado(res);
            }
            case "*" -> {
                String res = nuevaAuxiliar();
                emitPush(a1, body);
                emitPush(a2, body);
                body.append("    i32.mul\n");
                body.append("    global.set $").append(res).append("\n");
                t.setResultado(res);
            }
            case "/" -> {
                String res = nuevaAuxiliar();
                emitPush(a1, body);
                emitPush(a2, body);
                body.append("    i32.div_u\n");
                body.append("    global.set $").append(res).append("\n");
                t.setResultado(res);
            }
            case ":=" -> {
                String dst = asegurarGlobal(a1);
                emitPush(a2, body);
                body.append("    global.set $").append(dst).append("\n");
                t.setResultado(dst);
            }
            case "PRINT" -> {
                if (esCadenaLiteral(a1)) {
                    int off = internarCadena(a1);
                    int len = strLength.get(a1);
                    body.append("    i32.const ").append(off).append("\n");
                    body.append("    i32.const ").append(len).append("\n");
                    body.append("    call $alert_str\n");
                } else {
                    emitPush(a1, body);
                    body.append("    call $alert_i32\n");
                }
            }
            case "CALL" -> body.append("    call $").append(sanitizar(a1)).append("\n");
            case "JMP" -> body.append("    ;; jmp ").append(a1).append(" (no soportado en WASM MVP)\n");
            case "FUNC_BEGIN" -> {
                funcionActual = sanitizar(inicio);
                funciones.putIfAbsent(funcionActual, new StringBuilder());
            }
            case "FUNC_END" -> funcionActual = "main";
            case "LAMBDA" -> { /* no-op */ }
            default -> { /* no-op */ }
        }
    }

    private void emitPush(String val, StringBuilder body) {
        if (val == null || val.isEmpty()) {
            body.append("    i32.const 0\n");
        } else if (esInmediato(val)) {
            String num = val.endsWith("UL") ? val.substring(0, val.length() - 2) : val;
            body.append("    i32.const ").append(num).append("\n");
        } else if (esCadenaLiteral(val)) {
            // Uso directo en expresiones no está soportado; solo en PRINT (se degrada a 0)
            body.append("    i32.const 0\n");
        } else {
            body.append("    global.get $").append(sanitizar(val)).append("\n");
        }
    }

    private String nuevaAuxiliar() {
        String n = "aux" + auxs++;
        if (!declarados.contains(n)) {
            seccion_globals.append("  (global $").append(n).append(" (mut i32) (i32.const 0))\n");
            declarados.add(n);
        }
        return n;
    }

    private String asegurarGlobal(String id) {
        String n = sanitizar(id);
        if (!declarados.contains(n)) {
            seccion_globals.append("  (global $").append(n).append(" (mut i32) (i32.const 0))\n");
            declarados.add(n);
        }
        return n;
    }

    private String traducirOperando(String op) {
        if (op == null) return "";
        // Referencia a resultado de otro terceto: un número sin comillas
        if (op.matches("\\d+")) {
            int i = Integer.parseInt(op);
            return lista_tercetos.get(i).getResultado();
        }
        // Inmediato sin signo
        if (op.matches("\\d+UL")) return op;
        // Literal de cadena: preservar comillas para que PRINT lo detecte
        if (esCadenaLiteral(op)) return op;
        // Identificador normal
        return sanitizar(op);
    }

    private String sanitizar(String nombre) {
        if (nombre == null) return "_";
        String limpio = nombre.replace(':', '_').replaceAll("[^A-Za-z0-9_@\\$\\-\\.]", "_");
        if (limpio.isEmpty()) limpio = "_";
        if (Character.isDigit(limpio.charAt(0))) limpio = "_" + limpio;
        return limpio;
    }

    private boolean esInmediato(String s) {
        return s != null && s.matches("\\d+(UL)?");
    }

    private boolean esCadenaLiteral(String s) {
        return s != null && s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"");
    }

    // Interna una cadena literal (incluye comillas en 'literal')
    private int internarCadena(String literal) {
        if (!esCadenaLiteral(literal)) return 0;
        Integer off = strOffset.get(literal);
        if (off != null) return off;

        String sinComillas = literal.substring(1, literal.length() - 1);
        byte[] bytes = sinComillas.getBytes(StandardCharsets.UTF_8);
        int len = bytes.length;

        int nuevoOffset = dataOffset;
        dataOffset += len; // sin terminador nulo; usamos longitud explícita

        String escaped = escapeWatBytes(bytes);
        seccion_data.append("  (data (i32.const ")
                .append(nuevoOffset)
                .append(") \"")
                .append(escaped)
                .append("\")\n");

        strOffset.put(literal, nuevoOffset);
        strLength.put(literal, len);
        return nuevoOffset;
    }

    // Escapa bytes a literal WAT: ASCII imprimible salvo \" y \\; el resto como \hh
    private String escapeWatBytes(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            int v = b & 0xFF;
            if (v == 0x22 || v == 0x5C) { // " o \
                sb.append('\\').append((char) v);
            } else if (v >= 0x20 && v <= 0x7E) {
                sb.append((char) v);
            } else {
                sb.append('\\');
                String hx = Integer.toHexString(v);
                if (hx.length() == 1) sb.append('0');
                sb.append(hx);
            }
        }
        return sb.toString();
    }

    public void imprimirAssembler() {
        System.out.println("===== CODIGO WAT =====");
        System.out.print(assembler.toString());
        System.out.println("======================");
    }

    public void producirArchivoWAT() {
        java.nio.file.Path destino = obtenerPath().resolve(nombrePrograma + ".wat");
        try {
            java.nio.file.Files.writeString(
                    destino,
                    assembler.toString(),
                    java.nio.charset.StandardCharsets.UTF_8
            );
            System.out.println("Archivo WAT generado en: " + destino);
        } catch (java.io.IOException e) {
            System.err.println("Error al generar archivo WAT: " + e.getMessage());
        }

    }

    private java.nio.file.Path obtenerPath() {
        try {
            // Intentar desde el directorio de trabajo del proyecto
            String projectRoot = System.getProperty("user.dir");
            java.nio.file.Path pkgPath = java.nio.file.Paths.get(projectRoot, "src", "Compilador", "ModuloGC");

            // Crear si no existe
            if (!java.nio.file.Files.isDirectory(pkgPath)) {
                java.nio.file.Files.createDirectories(pkgPath);
            }
            return pkgPath;
        } catch (Exception e) {
            // Fallback: intentar devolver el directorio de trabajo si algo falla
            try {
                java.nio.file.Path wd = java.nio.file.Paths.get(System.getProperty("user.dir"));
                if (java.nio.file.Files.isDirectory(wd)) return wd;
            } catch (Exception ignored) {}

            // Último recurso: home del usuario
            return java.nio.file.Paths.get(System.getProperty("user.home"));
        }
    }



}

