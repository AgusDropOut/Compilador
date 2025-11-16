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

/**
 * Generador de código WebAssembly (WAT) a partir de tercetos.
 * Toma la representación intermedia producida por el compilador y
 * arma el código ejecutable final en formato WAT.
 */

public class GeneradorDeCodigo {
    // Lista de tercetos (código intermedio) a traducir
    private final ArrayList<Terceto> listaTercetos;
    private final String nombrePrograma;

    // Construcción del código WAT final
    private final StringBuilder codigoWAT = new StringBuilder();
    private final StringBuilder variablesGlobales = new StringBuilder();
    private final StringBuilder seccionDatos = new StringBuilder();

    // Almacena el código de cada función (nombre -> código)
    private final Map<String, StringBuilder> funciones = new LinkedHashMap<>();

    // Control de contexto durante generación
    private final Stack<String> pilaFunciones = new Stack<>();
    private String funcionActual = "main";
    private String scopeActual = null;

    // Registro de tipos de variables (nombre -> "i32" o "f64")
    private final Map<String, String> tiposVariable = new HashMap<>();

    // Gestión de cadenas de texto
    private final Map<String, Integer> posicionCadena = new HashMap<>();
    private final Map<String, Integer> largoCadena = new HashMap<>();
    private int siguientePosicionMemoria = 0;

    // Contadores para nombres únicos
    private int contadorTemporales = 0;

    public GeneradorDeCodigo(ArrayList<Terceto> tercetos, String nombrePrograma) {
        this.listaTercetos = tercetos;
        this.nombrePrograma = nombrePrograma;
    }

    /**
     * Punto de entrada principal: arma el módulo WAT completo.
     * - Escribe los imports y memoria.
     * - Declara todas las variables globales.
     * - Traduce cada terceto.
     * - Ensambla todas las funciones en el orden correcto.
     */

    public void generarCodigo() {
        // 1. Encabezado del módulo WAT
        codigoWAT.append("(module\n");
        codigoWAT.append("  (import \"env\" \"alert_i32\" (func $alert_i32 (param i32)))\n");
        codigoWAT.append("  (import \"env\" \"alert_str\" (func $alert_str (param i32 i32)))\n");
        codigoWAT.append("  (memory (export \"memory\") 1)\n");

        // 2. Declarar todas las variables globales
        declararVariablesGlobales();

        // 3. Inicializar función main
        funciones.put("main", new StringBuilder());
        funcionActual = "main";

        // 4. Procesar cada terceto
        for (int i = 0; i < listaTercetos.size(); i++) {
            procesarTerceto(listaTercetos.get(i));
        }

        // 5. Ensamblar todo el código
        codigoWAT.append(variablesGlobales);
        codigoWAT.append(seccionDatos);

        // 6. Escribir todas las funciones
        for (Map.Entry<String, StringBuilder> entrada : funciones.entrySet()) {
            String nombreFunc = entrada.getKey();
            StringBuilder codigoFunc = entrada.getValue();

            codigoWAT.append("\n  (func $").append(nombreFunc);

            // Main no retorna valor, otras funciones sí
            if (nombreFunc.equals("main")) {
                codigoWAT.append(" (export \"main\")");
            } else {
                codigoWAT.append(" (result i32)");
            }

            codigoWAT.append("\n");
            codigoWAT.append(codigoFunc);
            codigoWAT.append("  )\n");
        }

        codigoWAT.append(")\n");
    }

    /**
     * Recorre la tabla de símbolos y declara todas las variables globales
     * que realmente pertenecen al programa (se saltean literales y cadenas).
     * Cada variable se declara como global mutable y queda disponible para cualquier función.
     */

    private void declararVariablesGlobales() {
        for (ElementoTablaDeSimbolos elemento : TablaDeSimbolos.getElementos()) {
            String uso = elemento.getUso();

            // Ignorar funciones
            if (uso == null || uso.equals("Función")) {
                continue;
            }

            String lexema = TablaDeSimbolos.getLexema(elemento);
            if (lexema == null) {
                continue;
            }

            // Las cadenas van a la sección de datos
            if (esCadena(lexema)) {
                registrarCadena(lexema);
                continue;
            }

            // Los literales no necesitan declaración
            if (esNumeroULONG(lexema) || esNumeroFlotante(lexema)) {
                continue;
            }

            // Declarar variable global
            String nombreLimpio = limpiarNombre(lexema);
            if (!tiposVariable.containsKey(nombreLimpio)) {
                variablesGlobales.append("  (global $")
                        .append(nombreLimpio)
                        .append(" (mut i32) (i32.const 0))\n");
                tiposVariable.put(nombreLimpio, "i32");
            }
        }
    }

    /**
     * Traduce un terceto según su operador.
     * También detecta instrucciones especiales como inicio y fin de función.
     */

    private void procesarTerceto(Terceto terceto) {
        String operador = terceto.getOperador();

        // Detectar inicio/fin de función
        if (operador.startsWith("ini_")) {
            String nombreFunc = operador.substring(4);
            iniciarFuncion(nombreFunc);
            return;
        }

        if (operador.startsWith("fin_")) {
            finalizarFuncion();
            return;
        }

        // Obtener el código de la función actual
        StringBuilder codigo = funciones.get(funcionActual);

        // Procesar según el operador
        switch (operador) {
            case "+" -> procesarSuma(terceto, codigo);
            case "-" -> procesarResta(terceto, codigo);
            case "*" -> procesarMultiplicacion(terceto, codigo);
            case "/" -> procesarDivision(terceto, codigo);
            case ":=" -> procesarAsignacion(terceto, codigo);
            case "PRINT" -> procesarPrint(terceto, codigo);
            case "TRUNC" -> procesarTrunc(terceto, codigo);
            case "CALL" -> procesarLlamadaFuncion(terceto, codigo);
            case "JMP" -> codigo.append("    ;; jmp\n");
            case "LAMBDA" -> { /* no hacer nada */ }
        }
    }

    /**
     * Traduce una operación de suma
     * Carga los operandos en la pila, ejecuta la instrucción WAT correspondiente
     * y guarda el resultado en un temporal global.
     */

    private void procesarSuma(Terceto terceto, StringBuilder codigo) {
        String operando1 = obtenerValor(terceto.getOp1());
        String operando2 = obtenerValor(terceto.getOp2());

        // Poner ambos operandos en la pila
        ponerEnteroEnPila(operando1, codigo);
        ponerEnteroEnPila(operando2, codigo);

        // Sumar
        codigo.append("    i32.add\n");

        // Guardar resultado en temporal
        String temporal = crearTemporal();
        codigo.append("    global.set $").append(temporal).append("\n");
        terceto.setResultado(temporal);
    }

    /**
     * Traduce una operación de resta
     * Carga los operandos en la pila, ejecuta la instrucción WAT correspondiente
     * y guarda el resultado en un temporal global.
     */

    private void procesarResta(Terceto terceto, StringBuilder codigo) {
        String operando1 = obtenerValor(terceto.getOp1());
        String operando2 = obtenerValor(terceto.getOp2());

        ponerEnteroEnPila(operando1, codigo);
        ponerEnteroEnPila(operando2, codigo);
        codigo.append("    i32.sub\n");

        String temporal = crearTemporal();
        codigo.append("    global.set $").append(temporal).append("\n");
        terceto.setResultado(temporal);
    }

    /**
     * Traduce una operación de múltiplicación.
     * Carga los operandos en la pila, ejecuta la instrucción WAT correspondiente
     * y guarda el resultado en un temporal global.
     */

    private void procesarMultiplicacion(Terceto terceto, StringBuilder codigo) {
        String operando1 = obtenerValor(terceto.getOp1());
        String operando2 = obtenerValor(terceto.getOp2());

        // Si alguno es flotante, usar multiplicación flotante
        if (esFlotante(operando1) || esFlotante(operando2)) {
            ponerFlotanteEnPila(operando1, codigo);
            ponerFlotanteEnPila(operando2, codigo);
            codigo.append("    f64.mul\n");

            String temporal = crearTemporalFlotante();
            codigo.append("    global.set $").append(temporal).append("\n");
            terceto.setResultado(temporal);
        } else {
            ponerEnteroEnPila(operando1, codigo);
            ponerEnteroEnPila(operando2, codigo);
            codigo.append("    i32.mul\n");

            String temporal = crearTemporal();
            codigo.append("    global.set $").append(temporal).append("\n");
            terceto.setResultado(temporal);
        }
    }

    /**
     * Traduce una operación de división
     * Carga los operandos en la pila, ejecuta la instrucción WAT correspondiente
     * y guarda el resultado en un temporal global.
     */

    private void procesarDivision(Terceto terceto, StringBuilder codigo) {
        String operando1 = obtenerValor(terceto.getOp1());
        String operando2 = obtenerValor(terceto.getOp2());

        if (esFlotante(operando1) || esFlotante(operando2)) {
            ponerFlotanteEnPila(operando1, codigo);
            ponerFlotanteEnPila(operando2, codigo);
            codigo.append("    f64.div\n");

            String temporal = crearTemporalFlotante();
            codigo.append("    global.set $").append(temporal).append("\n");
            terceto.setResultado(temporal);
        } else {
            ponerEnteroEnPila(operando1, codigo);
            ponerEnteroEnPila(operando2, codigo);
            codigo.append("    i32.div_u\n");

            String temporal = crearTemporal();
            codigo.append("    global.set $").append(temporal).append("\n");
            terceto.setResultado(temporal);
        }
    }

    /**
     * Traduce una asignación.
     * Si la asignación va al valor de retorno de una función, se emite un return.
     * En cualquier otro caso simplemente se actualiza la variable destino.
     */

    private void procesarAsignacion(Terceto terceto, StringBuilder codigo) {
        String destino = obtenerValor(terceto.getOp1());
        String origen = obtenerValor(terceto.getOp2());

        // Si es asignación a variable de retorno, hacer return
        if (destino.startsWith("_ret_")) {
            if (origen == null || origen.isEmpty()) {
                codigo.append("    i32.const 0\n");
            } else if (esNumeroULONG(origen)) {
                // Quitar el sufijo "UL"
                String numero = origen.substring(0, origen.length() - 2);
                codigo.append("    i32.const ").append(numero).append("\n");
            } else if (esFlotante(origen)) {
                ponerFlotanteEnPila(origen, codigo);
                codigo.append("    i32.trunc_f64_u\n");
            } else {
                String nombreVar = asegurarVariableEntera(origen);
                codigo.append("    global.get $").append(nombreVar).append("\n");
            }
            codigo.append("    return\n");
            terceto.setResultado(origen);
            return;
        }

        // Asignación normal
        String nombreDestino = asegurarVariableEntera(destino);

        if (esFlotante(origen)) {
            ponerFlotanteEnPila(origen, codigo);
            codigo.append("    i32.trunc_f64_u\n");
        } else {
            ponerEnteroEnPila(origen, codigo);
        }

        codigo.append("    global.set $").append(nombreDestino).append("\n");
        terceto.setResultado(nombreDestino);
    }

    /**
     * Traduce la instrucción PRINT.
     * Si el operando es una cadena, se usa la función alert_str.
     * Si es un número, se usa la función alert_i32.
     */

    private void procesarPrint(Terceto terceto, StringBuilder codigo) {
        String operandoOriginal = terceto.getOp1();
        String operando = obtenerValor(operandoOriginal);

        // Si es una cadena, usar alert_str
        if (operandoOriginal != null && esCadena(operandoOriginal)) {
            int posicion = registrarCadena(operandoOriginal);
            int largo = largoCadena.get(operandoOriginal);

            codigo.append("    i32.const ").append(posicion).append("\n");
            codigo.append("    i32.const ").append(largo).append("\n");
            codigo.append("    call $alert_str\n");
        } else {
            // Imprimir número
            ponerEnteroEnPila(operando, codigo);
            codigo.append("    call $alert_i32\n");
        }

        terceto.setResultado("");
    }

    /**
     * Implementa la operación TRUNC.
     * Si el valor es un literal flotante, se calcula el truncado en tiempo de compilación.
     * Si es una variable, se convierte a f64, se trunca y finalmente se pasa a entero sin signo.
     */

    private void procesarTrunc(Terceto terceto, StringBuilder codigo) {
        String operandoOriginal = terceto.getOp1();
        double valor = parsearFlotante(operandoOriginal);
        long truncado = (long) Math.abs(Math.floor(valor));
        codigo.append("    i32.const ").append(truncado).append("\n");
        String temporal = crearTemporal();
        codigo.append("    global.set $").append(temporal).append("\n");
        terceto.setResultado(temporal);
    }

    /**
     * Traduce una llamada a función.
     * WebAssembly no acepta dos puntos en los nombres (“F:MAIN”), por lo que
     * se reconstruye el nombre quitando esos caracteres y reemplazándolos por guiones bajos.
     * Después de llamar, el resultado se guarda en un temporal global.
     */

    private void procesarLlamadaFuncion(Terceto terceto, StringBuilder codigo) {
        // op1 viene de los tercetos como "F:MAIN" o "G:MAIN:F"
        String nombreFuncionTS = terceto.getOp1(); // p.ej. "F:MAIN"

        // Si en algún momento op1 viniera con parámetros "F:MAIN(...)", los recortamos
        if (nombreFuncionTS != null && nombreFuncionTS.contains("(")) {
            int posPar = nombreFuncionTS.indexOf('(');
            nombreFuncionTS = nombreFuncionTS.substring(0, posPar).trim();
        }

        // Normalizar al mismo formato que usás al crear las funciones desde "ini_*"
        String nombreFuncionWasm = limpiarNombre(nombreFuncionTS);


        if (nombreFuncionTS != null && nombreFuncionTS.contains(":")) {
            // Reordenar "F:MAIN" -> "MAIN:F", "G:MAIN:F" -> "MAIN:F:G"
            String[] partes = nombreFuncionTS.split(":");
            if (partes.length == 2) {          // F:MAIN
                nombreFuncionTS = partes[1] + ":" + partes[0];       // MAIN:F
            } else if (partes.length == 3) {   // G:MAIN:F
                nombreFuncionTS = partes[1] + ":" + partes[2] + ":" + partes[0]; // MAIN:F:G
            }
            nombreFuncionWasm = limpiarNombre(nombreFuncionTS); // MAIN:F -> MAIN_F, MAIN:F:G -> MAIN_F_G
        }

        // Generar la llamada
        codigo.append("    call $").append(nombreFuncionWasm).append("\n");

        // Guardar el resultado en un temporal global
        String temporal = crearTemporal();
        codigo.append("    global.set $").append(temporal).append("\n");
        terceto.setResultado(temporal);
    }

    /**
     * Registra el comienzo de una función nueva.
     * Se crea un bloque de código separado y se cambia el contexto actual.
     * Si el nombre viene con “A:B”, se lo deja en un formato simple
     * reemplazando los “:” por guiones bajos para que WebAssembly lo acepte.
     */

    private void iniciarFuncion(String nombreFunc) {
        pilaFunciones.push(funcionActual);

        // Normalizar el nombre de función
        String nombreLimpio = limpiarNombre(nombreFunc);
        funcionActual = nombreLimpio;
        funciones.put(nombreLimpio, new StringBuilder());

        // Actualizar scope actual: partir del nombre "original", pero si tu TS
        // guarda la función con ":" usa ese formato de forma consistente.
        if (nombreFunc.contains(":")) {
            int pos = nombreFunc.indexOf(':');
            scopeActual = nombreFunc.substring(pos + 1);
        } else {
            // opcional: si tu compilador está usando "_" en vez de ":", puedes deshacer el cambio
            scopeActual = nombreFunc;
        }
    }

    /**
     * Indica el cierre de la función actual y vuelve al ámbito anterior.
     */

    private void finalizarFuncion() {
        if (!pilaFunciones.isEmpty()) {
            funcionActual = pilaFunciones.pop();
        } else {
            funcionActual = "main";
        }
    }

    /**
     * Coloca un entero en la pila.
     * Si viene como literal “123UL” se le quita el sufijo.
     * Si es una variable, se obtiene desde su global correspondiente.
     */

    private void ponerEnteroEnPila(String valor, StringBuilder codigo) {
        if (valor == null || valor.isEmpty()) {
            codigo.append("    i32.const 0\n");
            return;
        }

        if (esNumeroULONG(valor)) {
            // Quitar sufijo "UL" y usar el número
            String numero = valor.substring(0, valor.length() - 2);
            codigo.append("    i32.const ").append(numero).append("\n");
        } else {
            // Es una variable
            String nombreVar = asegurarVariableEntera(valor);
            codigo.append("    global.get $").append(nombreVar).append("\n");
        }
    }

    /**
     * Coloca un valor flotante en la pila.
     * Convierte literales con formato del lenguaje (1,5d+00) al formato que espera WAT.
     * Si es una variable entera, también la convierte a f64.
     */

    private void ponerFlotanteEnPila(String valor, StringBuilder codigo) {
        if (valor == null || valor.isEmpty()) {
            codigo.append("    f64.const 0\n");
            return;
        }

        if (esNumeroFlotante(valor)) {
            // Convertir formato del lenguaje a formato WAT
            String numeroWAT = valor.replace(',', '.')
                    .replace('d', 'E')
                    .replace('D', 'E');
            codigo.append("    f64.const ").append(numeroWAT).append("\n");
        } else if (esNumeroULONG(valor)) {
            // Convertir entero a flotante
            String numero = valor.substring(0, valor.length() - 2);
            codigo.append("    i32.const ").append(numero).append("\n");
            codigo.append("    f64.convert_i32_u\n");
        } else {
            // Es una variable
            String nombreVar = limpiarNombre(valor);
            codigo.append("    global.get $").append(nombreVar).append("\n");

            // Si no es flotante, convertir
            if (!tiposVariable.getOrDefault(nombreVar, "i32").equals("f64")) {
                codigo.append("    f64.convert_i32_u\n");
            }
        }
    }

    /**
     * Resuelve un operando.
     * Puede ser:
     * - un literal,
     * - una referencia a terceto,
     * - una variable con o sin scope,
     * - o una cadena.
     * Devuelve el nombre definitivo tal cual tiene que usarse en WAT.
     */

    private String obtenerValor(String operando) {
        if (operando == null) {
            return "";
        }

        // Si es referencia a terceto [N] o N
        if (operando.startsWith("[") && operando.endsWith("]")) {
            int indice = Integer.parseInt(operando.substring(1, operando.length() - 1));
            return listaTercetos.get(indice).getResultado();
        }

        if (operando.matches("\\d+") && !operando.contains("UL")) {
            int indice = Integer.parseInt(operando);
            if (indice < listaTercetos.size()) {
                return listaTercetos.get(indice).getResultado();
            }
        }

        // Si es literal o variable, devolverlo tal cual
        if (esNumeroULONG(operando) || esNumeroFlotante(operando) || esCadena(operando)) {
            return operando.trim();
        }

        // Es un identificador, buscar en tabla de símbolos
        return buscarEnTablaSimbolos(operando);
    }

    /**
     * Busca un símbolo considerando el scope actual.
     * Si el nombre no tenía scope, se intenta completar con el ámbito que esté activo.
     */

    private String buscarEnTablaSimbolos(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return "_";
        }

        nombre = nombre.trim();

        // Si ya tiene scope (contiene ":"), devolverlo
        if (nombre.contains(":")) {
            return nombre;
        }

        // Buscar con scope actual
        if (scopeActual != null && !scopeActual.isEmpty()) {
            String conScope = nombre + ":" + scopeActual;
            if (existeEnTablaSimbolos(conScope)) {
                return conScope;
            }
        }

        // Buscar sin scope
        for (ElementoTablaDeSimbolos elem : TablaDeSimbolos.getElementos()) {
            String lexema = TablaDeSimbolos.getLexema(elem);
            if (lexema != null && lexema.startsWith(nombre + ":")) {
                return lexema;
            }
        }

        return nombre;
    }

    /**
     * Verifica si un lexema existe en la tabla de símbolos
     */
    private boolean existeEnTablaSimbolos(String lexema) {
        for (ElementoTablaDeSimbolos elem : TablaDeSimbolos.getElementos()) {
            String lex = TablaDeSimbolos.getLexema(elem);
            if (lexema.equals(lex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Crea variables auxiliares globales para guardar resultados de operaciones.
     * Los nombres se generan automáticamente (_t0, _t1, etc).
     */
    private String crearTemporal() {
        String nombre = "_t" + contadorTemporales++;
        asegurarVariableEntera(nombre);
        return nombre;
    }

    /**
     * Crea variables auxiliares globales para guardar resultados de operaciones.
     * Los nombres se generan automáticamente (_t0, _t1, etc).
     */

    private String crearTemporalFlotante() {
        String nombre = "_tf" + contadorTemporales++;
        String nombreLimpio = limpiarNombre(nombre);

        if (!tiposVariable.containsKey(nombreLimpio)) {
            variablesGlobales.append("  (global $")
                    .append(nombreLimpio)
                    .append(" (mut f64) (f64.const 0))\n");
            tiposVariable.put(nombreLimpio, "f64");
        }

        return nombre;
    }

    /**
     * Asegura que una variable entera exista en la sección de globales.
     * Si no está declarada, la agrega.
     */
    private String asegurarVariableEntera(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return "_";
        }

        String nombreLimpio = limpiarNombre(nombre);

        if (!tiposVariable.containsKey(nombreLimpio)) {
            variablesGlobales.append("  (global $")
                    .append(nombreLimpio)
                    .append(" (mut i32) (i32.const 0))\n");
            tiposVariable.put(nombreLimpio, "i32");
        }

        return nombreLimpio;
    }

    /**
     * Guarda una cadena literal en la memoria.
     * Se almacena byte a byte y se recuerdan su posición y longitud.
     */

    private int registrarCadena(String cadena) {
        // Si ya está registrada, devolver su posición
        if (posicionCadena.containsKey(cadena)) {
            return posicionCadena.get(cadena);
        }

        // Quitar comillas
        String sinComillas = cadena.substring(1, cadena.length() - 1);
        byte[] bytes = sinComillas.getBytes(StandardCharsets.UTF_8);
        int largo = bytes.length;

        // Asignar posición en memoria
        int posicion = siguientePosicionMemoria;
        siguientePosicionMemoria += largo;

        // Generar código de datos
        seccionDatos.append("  (data (i32.const ")
                .append(posicion)
                .append(") \"");

        // Recorre cada byte de la cadena y lo escribe en el bloque (data):
        // - si es un caracter ASCII imprimible, se copia tal cual,
        // - si es un caracter especial o no imprimible, se escribe como \xx en hexadecimal.
        // De esta forma, se asegura la validez de la cadena en el formato WAT.
        for (byte b : bytes) {
            int valor = b & 0xFF;
            if (valor >= 32 && valor <= 126 && valor != '"' && valor != '\\') {
                seccionDatos.append((char) valor);
            } else {
                String hex = Integer.toHexString(valor);
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                seccionDatos.append("\\").append(hex);
            }
        }

        seccionDatos.append("\")\n");

        // Guardar información
        posicionCadena.put(cadena, posicion);
        largoCadena.put(cadena, largo);

        return posicion;
    }

    /**
     * Adapta un nombre de variable o función para que sea válido dentro de WAT.
     * Se reemplazan caracteres como “:” por “_” y se filtran otros que no acepta WebAssembly.
     */
    private String limpiarNombre(String nombre) {
        if (nombre == null) {
            return "_";
        }

        // Reemplazar caracteres no válidos
        String limpio = nombre.replace(':', '_')
                .replace(',', '_')
                .replace('.', '_')
                .replace('-', '_')
                .replace('+', '_');

        // Quitar cualquier otro carácter raro
        limpio = limpio.replaceAll("[^A-Za-z0-9_]", "_");

        if (limpio.isEmpty()) {
            limpio = "_";
        }

        // Si empieza con número, agregar guión bajo
        if (Character.isDigit(limpio.charAt(0))) {
            limpio = "_" + limpio;
        }

        return limpio;
    }

    /**
     * Verifica si un valor es un número ULONG (ej: "123UL")
     */
    private boolean esNumeroULONG(String valor) {
        if (valor == null) {
            return false;
        }
        return valor.trim().matches("\\d+UL");
    }

    /**
     * Verifica si un valor es un número flotante (ej: "1.5d+00")
     */
    private boolean esNumeroFlotante(String valor) {
        if (valor == null) {
            return false;
        }
        // Acepta formatos como: 1.5d+00, -7,887800e+00
        return valor.trim().matches("[+\\-]?\\d+[,.]\\d+[dDeE][+\\-]?\\d+");
    }

    /**
     * Verifica si un valor es una cadena (ej: "Hola")
     */
    private boolean esCadena(String valor) {
        if (valor == null || valor.length() < 2) {
            return false;
        }
        return valor.startsWith("\"") && valor.endsWith("\"");
    }

    /**
     * Verifica si un valor es de tipo flotante
     */
    private boolean esFlotante(String valor) {
        if (esNumeroFlotante(valor)) {
            return true;
        }

        String nombreLimpio = limpiarNombre(valor);
        return tiposVariable.getOrDefault(nombreLimpio, "i32").equals("f64");
    }

    /**
     * Convierte un literal flotante a número
     */
    private double parsearFlotante(String literal) {
        String normalizado = literal.trim()
                .replace(',', '.')
                .replace('d', 'E')
                .replace('D', 'E');
        return Double.parseDouble(normalizado);
    }

    /**
     * Imprime el código WAT generado en consola
     */
    public void imprimirAssembler() {
        System.out.println("===== CODIGO WAT =====");
        System.out.print(codigoWAT.toString());
        System.out.println("======================");
    }

    /**
     * Guarda el código WAT en un archivo
     */
    public void guardarArchivoWat() {
        try {
            String rutaProyecto = System.getProperty("user.dir");
            java.nio.file.Path carpeta = java.nio.file.Paths.get(
                    rutaProyecto, "src", "Compilador", "ModuloGC"
            );

            if (!java.nio.file.Files.isDirectory(carpeta)) {
                java.nio.file.Files.createDirectories(carpeta);
            }

            java.nio.file.Path archivo = carpeta.resolve(nombrePrograma + ".wat");
            java.nio.file.Files.writeString(archivo, codigoWAT.toString());

            System.out.println("Archivo WAT generado en: " + archivo);
        } catch (Exception e) {
            System.err.println("Error al generar archivo WAT: " + e.getMessage());
        }
    }
}
