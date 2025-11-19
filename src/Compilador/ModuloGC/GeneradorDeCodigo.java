package Compilador.ModuloGC;

import Compilador.ModuloLexico.AnalizadorLexico;
import Compilador.ModuloLexico.ElementoTablaDeSimbolos;
import Compilador.ModuloLexico.TablaDeSimbolos;
import Compilador.ModuloSemantico.Terceto;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class GeneradorDeCodigo {
    private final ArrayList<Terceto> listaTercetos;
    private final String nombrePrograma;

    // Salida
    private final StringBuilder codigoWAT = new StringBuilder();
    private final StringBuilder variablesGlobales = new StringBuilder();
    private final StringBuilder seccionDatos = new StringBuilder();
    private final Map<String, StringBuilder> funciones = new LinkedHashMap<>();
    private final Stack<String> pilaFunciones = new Stack<>();
    private String funcionActual = "main";
    private String scopeActual = null;



    // Control de Whiles (usando tus etiquetas manuales)
    private Stack<String> pilaWhile = new Stack<>();
    private int contadorWhile = 0;
    private Stack<String>pilaWhileBfs = new Stack<>();

    private int profundidadDeBloques = 0;

    // Utils
    private final Map<String, String> tiposVariable = new HashMap<>();
    private final Map<String, Integer> posicionCadena = new HashMap<>();
    private final Map<String, Integer> largoCadena = new HashMap<>();
    private int siguientePosicionMemoria = 0;
    private int contadorTemporales = 0;

    public GeneradorDeCodigo(ArrayList<Terceto> tercetos, String nombrePrograma) {
        this.listaTercetos = tercetos;
        this.nombrePrograma = nombrePrograma;
    }

    public void generarCodigo() {
        // 1. PASO PREVIO: Detectar estructuras IF/ELSE mirando los bf/bl
        AnalizadorDeBloques.analizarBloques(listaTercetos);
        AnalizadorDeBloques.imprimirBloques();

        // 2. Encabezado
        codigoWAT.append("(module\n");
        codigoWAT.append("  (import \"env\" \"alert_i32\" (func $alert_i32 (param i32)))\n");
        codigoWAT.append("  (import \"env\" \"alert_str\" (func $alert_str (param i32 i32)))\n");
        codigoWAT.append("  (memory (export \"memory\") 1)\n");

        declararVariablesGlobales();
        funciones.put("main", new StringBuilder());
        funcionActual = "main";

        // 3. BUCLE PRINCIPAL
        for (int i = 0; i < listaTercetos.size(); i++) {
            StringBuilder codigo = funciones.get(funcionActual);
            System.out.println(i + ": " + listaTercetos.get(i).toString() + "Es un bloque if else? " + AnalizadorDeBloques.esUnBloqueIfElse(i));
            if (AnalizadorDeBloques.tieneBloqueFinAsignado(i)) {
                for(int j = AnalizadorDeBloques.getNumeroDeFinAsignados(i); j>0; j--) {

                        codigo.append("br ").append(0);
                        codigo.append(") ;; fin block\n");

                }
            }
            if(AnalizadorDeBloques.tieneBloqueInicioAsignado(i)){
                codigo.append("(block").append("\n");
                if(AnalizadorDeBloques.esUnBloqueIfElse(i)){
                    codigo.append("(block").append("\n");
                }
            }

            if(AnalizadorDeBloques.tieneBloqueElseAsignado(i)){
                codigo.append("br ").append(1);
                codigo.append(") ;; fin block else\n");
            }

            // B) Traducir instrucción
            procesarTerceto(listaTercetos.get(i), i);




        }

        codigoWAT.append(variablesGlobales);
        codigoWAT.append(seccionDatos);
        escribirFunciones();
        codigoWAT.append(")\n");
    }





    private void procesarTerceto(Terceto terceto, int i) {
        String operador = terceto.getOperador();
        StringBuilder codigo = funciones.get(funcionActual);

        if (operador.startsWith("ini_")) { iniciarFuncion(operador.substring(4)); return; }
        if (operador.startsWith("fin_")) { finalizarFuncion(); return; }

        switch (operador) {
            case "WHILE_START" -> procesarWhileStart(terceto, codigo);
            case "WHILE_END" -> procesarWhileEnd(terceto, codigo);
            case "bf" -> procesarBf(terceto, codigo, i);
            case "bl", "JMP" -> procesarSaltoIncondicional(terceto, codigo, i);
            // ... Operaciones comunes ...
            case ">"  -> procesarComparacion("i32.gt_u", terceto, codigo);
            case "<"  -> procesarComparacion("i32.lt_u", terceto, codigo);
            case "==" -> procesarComparacion("i32.eq",   terceto, codigo);
            case ">=" -> procesarComparacion("i32.ge_u", terceto, codigo);
            case "<=" -> procesarComparacion("i32.le_u", terceto, codigo);
            case "=!" -> procesarComparacion("i32.ne",   terceto, codigo);
            case "+"     -> procesarSuma(terceto, codigo);
            case "-"     -> procesarResta(terceto, codigo);
            case "*"     -> procesarMultiplicacion(terceto, codigo);
            case "/"     -> procesarDivision(terceto, codigo);
            case ":="    -> procesarAsignacion(terceto, codigo);
            case "PRINT" -> procesarPrint(terceto, codigo);
            case "TRUNC" -> procesarTrunc(terceto, codigo);
            case "CALL"  -> procesarLlamadaFuncion(terceto, codigo);
            case "LAMBDA" -> {}
        }
    }



    // --- UTILS COPIADOS (Para que compile directo) ---
    private void procesarWhileStart(Terceto t, StringBuilder codigo) {
        String lblBreak = "break_W" + contadorWhile;
        String lblLoop = "loop_W" + contadorWhile;
        contadorWhile++;
        pilaWhile.push(lblLoop);
        pilaWhileBfs.push(lblBreak);

        codigo.append("    (block $").append(lblBreak).append("\n");
        codigo.append("    (loop $").append(lblLoop).append("\n");
    }
    private void procesarWhileEnd(Terceto t, StringBuilder codigo) {
        String lblLoop = pilaWhile.pop(); // o guardarlo en otra pila si lo necesitas

        codigo.append("    br $").append(lblLoop).append("\n");  // 🔁 volver al inicio del loop
        codigo.append("    ) ;; fin loop\n");
        codigo.append("    ) ;; fin block while\n");

    }

    private void procesarBf(Terceto t, StringBuilder codigo, int i) {
        if(AnalizadorDeBloques.esTercetoBfWhile(i)){
            String lblBreak = pilaWhileBfs.pop();
            String op1 = obtenerValor(t.getOp1());
            ponerEnteroEnPila(op1, codigo);
            codigo.append("    i32.eqz\n");
            codigo.append("    br_if $").append(lblBreak).append("\n");
        } else {
            String op1 = obtenerValor(t.getOp1());
            ponerEnteroEnPila(op1, codigo);
            codigo.append("    i32.eqz\n");
            int destino = obtenerDestino(t.getOp2());
            if (destino != -1) {
                codigo.append("    br_if ").append(profundidadDeBloques).append("\n");
            }
        }
    }
    private void procesarSaltoIncondicional(Terceto t, StringBuilder codigo, int i) {

    }

    private void escribirFunciones() {
        for (Map.Entry<String, StringBuilder> entrada : funciones.entrySet()) {
            String nombreFunc = entrada.getKey();
            codigoWAT.append("\n  (func $").append(nombreFunc);
            if (nombreFunc.equals("main")) codigoWAT.append(" (export \"main\")");
            else codigoWAT.append(" (result i32)");
            codigoWAT.append("\n").append(entrada.getValue()).append("  )\n");
        }
    }
    private void procesarComparacion(String instruccion, Terceto t, StringBuilder codigo) {
        String op1 = obtenerValor(t.getOp1());
        String op2 = obtenerValor(t.getOp2());
        ponerEnteroEnPila(op1, codigo);
        ponerEnteroEnPila(op2, codigo);
        codigo.append("    ").append(instruccion).append("\n");
        String temp = crearTemporal();
        codigo.append("    global.set $").append(temp).append("\n");
        t.setResultado(temp);
    }
    private void procesarSuma(Terceto terceto, StringBuilder codigo) {
        String op1 = obtenerValor(terceto.getOp1());
        String op2 = obtenerValor(terceto.getOp2());
        ponerEnteroEnPila(op1, codigo);
        ponerEnteroEnPila(op2, codigo);
        codigo.append("    i32.add\n");
        String temp = crearTemporal();
        codigo.append("    global.set $").append(temp).append("\n");
        terceto.setResultado(temp);
    }
    private void procesarResta(Terceto terceto, StringBuilder codigo) {
        String op1 = obtenerValor(terceto.getOp1());
        String op2 = obtenerValor(terceto.getOp2());
        ponerEnteroEnPila(op1, codigo);
        ponerEnteroEnPila(op2, codigo);
        codigo.append("    i32.sub\n");
        String temp = crearTemporal();
        codigo.append("    global.set $").append(temp).append("\n");
        terceto.setResultado(temp);
    }
    private void procesarMultiplicacion(Terceto terceto, StringBuilder codigo) {
        String op1 = obtenerValor(terceto.getOp1());
        String op2 = obtenerValor(terceto.getOp2());
        if (esFlotante(op1) || esFlotante(op2)) {
            ponerFlotanteEnPila(op1, codigo);
            ponerFlotanteEnPila(op2, codigo);
            codigo.append("    f64.mul\n");
            String temp = crearTemporalFlotante();
            codigo.append("    global.set $").append(temp).append("\n");
            terceto.setResultado(temp);
        } else {
            ponerEnteroEnPila(op1, codigo);
            ponerEnteroEnPila(op2, codigo);
            codigo.append("    i32.mul\n");
            String temp = crearTemporal();
            codigo.append("    global.set $").append(temp).append("\n");
            terceto.setResultado(temp);
        }
    }
    private void procesarDivision(Terceto terceto, StringBuilder codigo) {
        String op1 = obtenerValor(terceto.getOp1());
        String op2 = obtenerValor(terceto.getOp2());
        if (esFlotante(op1) || esFlotante(op2)) {
            ponerFlotanteEnPila(op1, codigo);
            ponerFlotanteEnPila(op2, codigo);
            codigo.append("    f64.div\n");
            String temp = crearTemporalFlotante();
            codigo.append("    global.set $").append(temp).append("\n");
            terceto.setResultado(temp);
        } else {
            ponerEnteroEnPila(op1, codigo);
            ponerEnteroEnPila(op2, codigo);
            codigo.append("    i32.div_u\n");
            String temp = crearTemporal();
            codigo.append("    global.set $").append(temp).append("\n");
            terceto.setResultado(temp);
        }
    }
    private void procesarAsignacion(Terceto terceto, StringBuilder codigo) {
        String destino = obtenerValor(terceto.getOp1());
        String origen = obtenerValor(terceto.getOp2());
        if (destino.startsWith("_ret_")) {
            if (origen == null || origen.isEmpty()) {
                codigo.append("    i32.const 0\n");
            } else if (esNumeroULONG(origen)) {
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
    private void procesarPrint(Terceto terceto, StringBuilder codigo) {
        String original = terceto.getOp1();
        String valor = obtenerValor(original);
        if (original != null && esCadena(original)) {
            int pos = registrarCadena(original);
            int len = largoCadena.get(original);
            codigo.append("    i32.const ").append(pos).append("\n");
            codigo.append("    i32.const ").append(len).append("\n");
            codigo.append("    call $alert_str\n");
        } else {
            ponerEnteroEnPila(valor, codigo);
            codigo.append("    call $alert_i32\n");
        }
        terceto.setResultado("");
    }
    private void procesarTrunc(Terceto terceto, StringBuilder codigo) {
        String original = terceto.getOp1();
        codigo.append("    f64.const ").append(original).append("\n");
        codigo.append("    i32.trunc_f64_u\n");
        String temp = crearTemporal();
        codigo.append("    global.set $").append(temp).append("\n");
        terceto.setResultado(temp);
    }
    private void procesarLlamadaFuncion(Terceto terceto, StringBuilder codigo) {
        String funcTS = terceto.getOp1();
        if (funcTS != null && funcTS.contains("(")) funcTS = funcTS.substring(0, funcTS.indexOf('(')).trim();
        String funcWasm = limpiarNombre(funcTS);
        if (funcTS != null && funcTS.contains(":")) {
            String[] p = funcTS.split(":");
            if (p.length >= 2) funcWasm = limpiarNombre(p[1] + ":" + p[0]);
        }
        codigo.append("    call $").append(funcWasm).append("\n");
        String temp = crearTemporal();
        codigo.append("    global.set $").append(temp).append("\n");
        terceto.setResultado(temp);
    }
    private void iniciarFuncion(String nombreFunc) {
        pilaFunciones.push(funcionActual);
        String nombreLimpio = limpiarNombre(nombreFunc);
        funcionActual = nombreLimpio;
        funciones.put(nombreLimpio, new StringBuilder());
        if (nombreFunc.contains(":")) scopeActual = nombreFunc.substring(nombreFunc.indexOf(':') + 1);
    }
    private void finalizarFuncion() {
        if (!pilaFunciones.isEmpty()) funcionActual = pilaFunciones.pop();
        else funcionActual = "main";
    }
    private void ponerEnteroEnPila(String valor, StringBuilder codigo) {
        if (valor == null || valor.isEmpty()) { codigo.append("    i32.const 0\n"); return; }
        if (esNumeroULONG(valor)) codigo.append("    i32.const ").append(valor.substring(0, valor.length() - 2)).append("\n");
        else codigo.append("    global.get $").append(asegurarVariableEntera(valor)).append("\n");
    }
    private void ponerFlotanteEnPila(String valor, StringBuilder codigo) {
        if (valor == null || valor.isEmpty()) { codigo.append("    f64.const 0\n"); return; }
        if (esNumeroFlotante(valor)) {
            String valW = valor.replace(',', '.').replace('d', 'E').replace('D', 'E');
            codigo.append("    f64.const ").append(valW).append("\n");
        } else if (esNumeroULONG(valor)) {
            codigo.append("    i32.const ").append(valor.substring(0, valor.length() - 2)).append("\n");
            codigo.append("    f64.convert_i32_u\n");
        } else {
            String nombreVar = limpiarNombre(valor);
            codigo.append("    global.get $").append(nombreVar).append("\n");
            if (!tiposVariable.getOrDefault(nombreVar, "i32").equals("f64")) codigo.append("    f64.convert_i32_u\n");
        }
    }
    private String obtenerValor(String operando) {
        if (operando == null) return "";
        if (operando.startsWith("[") && operando.endsWith("]")) {
            int idx = Integer.parseInt(operando.substring(1, operando.length() - 1));
            return listaTercetos.get(idx).getResultado();
        }
        if (operando.matches("\\d+")) {
            int idx = Integer.parseInt(operando);
            if (idx < listaTercetos.size()) return listaTercetos.get(idx).getResultado();
        }
        if (esNumeroULONG(operando) || esNumeroFlotante(operando) || esCadena(operando)) return operando.trim();
        return buscarEnTablaSimbolos(operando);
    }
    private int obtenerDestino(String op) {
        if (op == null) return -1;
        if (op.startsWith("[") && op.endsWith("]")) {
            try { return Integer.parseInt(op.substring(1, op.length() - 1)); } catch(Exception e) { return -1; }
        }
        try { return Integer.parseInt(op); } catch(Exception e) { return -1; }
    }
    private String buscarEnTablaSimbolos(String nombre) {
        if (nombre == null || nombre.isEmpty()) return "_";
        nombre = nombre.trim();
        if (nombre.contains(":")) return nombre;
        if (scopeActual != null && !scopeActual.isEmpty()) {
            String conScope = nombre + ":" + scopeActual;
            if (existeEnTablaSimbolos(conScope)) return conScope;
        }
        for (ElementoTablaDeSimbolos elem : TablaDeSimbolos.getElementos()) {
            String lex = TablaDeSimbolos.getLexema(elem);
            if (lex != null && lex.startsWith(nombre + ":")) return lex;
        }
        return nombre;
    }
    private boolean existeEnTablaSimbolos(String lexema) {
        for (ElementoTablaDeSimbolos elem : TablaDeSimbolos.getElementos()) {
            if (lexema.equals(TablaDeSimbolos.getLexema(elem))) return true;
        }
        return false;
    }
    private void declararVariablesGlobales() {
        for (ElementoTablaDeSimbolos elemento : TablaDeSimbolos.getElementos()) {
            String uso = elemento.getUso();
            if (uso == null || uso.equals("Función")) continue;
            String lexema = TablaDeSimbolos.getLexema(elemento);
            if (lexema == null) continue;
            if (esCadena(lexema)) { registrarCadena(lexema); continue; }
            if (esNumeroULONG(lexema) || esNumeroFlotante(lexema)) continue;
            String nombreLimpio = limpiarNombre(lexema);
            if (!tiposVariable.containsKey(nombreLimpio)) {
                variablesGlobales.append("  (global $").append(nombreLimpio).append(" (mut i32) (i32.const 0))\n");
                tiposVariable.put(nombreLimpio, "i32");
            }
        }
    }
    private String asegurarVariableEntera(String nombre) {
        String nombreLimpio = limpiarNombre(nombre);
        if (!tiposVariable.containsKey(nombreLimpio)) {
            variablesGlobales.append("  (global $").append(nombreLimpio).append(" (mut i32) (i32.const 0))\n");
            tiposVariable.put(nombreLimpio, "i32");
        }
        return nombreLimpio;
    }
    private String crearTemporal() {
        String nombre = "_t" + contadorTemporales++;
        return asegurarVariableEntera(nombre);
    }
    private String crearTemporalFlotante() {
        String nombre = "_tf" + contadorTemporales++;
        String nombreLimpio = limpiarNombre(nombre);
        if (!tiposVariable.containsKey(nombreLimpio)) {
            variablesGlobales.append("  (global $").append(nombreLimpio).append(" (mut f64) (f64.const 0))\n");
            tiposVariable.put(nombreLimpio, "f64");
        }
        return nombre;
    }
    private int registrarCadena(String cadena) {
        if (posicionCadena.containsKey(cadena)) return posicionCadena.get(cadena);
        String sinComillas = cadena.substring(1, cadena.length() - 1);
        byte[] bytes = sinComillas.getBytes(StandardCharsets.UTF_8);
        int largo = bytes.length;
        int posicion = siguientePosicionMemoria;
        siguientePosicionMemoria += largo;
        seccionDatos.append("  (data (i32.const ").append(posicion).append(") \"");
        for (byte b : bytes) {
            int valor = b & 0xFF;
            if (valor >= 32 && valor <= 126 && valor != '"' && valor != '\\') seccionDatos.append((char) valor);
            else {
                String hex = Integer.toHexString(valor);
                if (hex.length() == 1) hex = "0" + hex;
                seccionDatos.append("\\").append(hex);
            }
        }
        seccionDatos.append("\")\n");
        posicionCadena.put(cadena, posicion);
        largoCadena.put(cadena, largo);
        return posicion;
    }
    private String limpiarNombre(String nombre) {
        if (nombre == null) return "_";
        String limpio = nombre.replace(':', '_').replace(',', '_').replace('.', '_').replace('-', '_').replace('+', '_');
        limpio = limpio.replaceAll("[^A-Za-z0-9_]", "_");
        if (limpio.isEmpty()) limpio = "_";
        if (Character.isDigit(limpio.charAt(0))) limpio = "_" + limpio;
        return limpio;
    }
    private boolean esNumeroULONG(String valor) { return valor != null && valor.trim().matches("\\d+UL"); }
    private boolean esNumeroFlotante(String valor) { return valor != null && valor.trim().matches("[+\\-]?\\d+[,.]\\d+[dDeE][+\\-]?\\d+"); }
    private boolean esCadena(String valor) { return valor != null && valor.startsWith("\"") && valor.endsWith("\""); }
    private boolean esFlotante(String valor) {
        if (esNumeroFlotante(valor)) return true;
        String nombreLimpio = limpiarNombre(valor);
        return tiposVariable.getOrDefault(nombreLimpio, "i32").equals("f64");
    }
    public void imprimirAssembler() {
        System.out.println("===== CODIGO WAT =====");
        System.out.print(codigoWAT.toString());
        System.out.println("======================");
    }
    public void guardarArchivoWat() {
        try {
            String rutaProyecto = System.getProperty("user.dir");
            java.nio.file.Path carpeta = java.nio.file.Paths.get(rutaProyecto, "src", "Compilador", "ModuloGC");
            if (!java.nio.file.Files.isDirectory(carpeta)) java.nio.file.Files.createDirectories(carpeta);
            java.nio.file.Path archivo = carpeta.resolve(nombrePrograma + ".wat");
            java.nio.file.Files.writeString(archivo, codigoWAT.toString());
            System.out.println("Archivo WAT generado en: " + archivo);
        } catch (Exception e) {
            System.err.println("Error al generar archivo WAT: " + e.getMessage());
        }
    }
}