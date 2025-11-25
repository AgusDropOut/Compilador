package Compilador.ModuloGC;

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

    // Variables para almacenar la referencia al mensaje de error
    private int iniPosErrorDivCero;
    private int finPosErrorDivCero;
    private int iniPosErrorRestaNegativa;
    private int finPosErrorRestaNegativa;
    private int iniPosErrorRecursion;
    private int finPosErrorRecursion;
    private String errorDivCero = "'Error: Division por cero.'";
    private String errorRestaNegativa = "'Error: Resultado de resta negativo (underflow).'";
    private String errorLlamadaInvalida = "'Error: Una función no puede llamarse a si misma'";



    // Control de Whiles (usando etiquetas manuales)
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

        iniPosErrorDivCero = registrarCadena(errorDivCero);
        finPosErrorDivCero = largoCadena.get(errorDivCero);
        iniPosErrorRestaNegativa = registrarCadena(errorRestaNegativa);
        finPosErrorRestaNegativa = largoCadena.get(errorRestaNegativa);
        iniPosErrorRecursion = registrarCadena(errorLlamadaInvalida);
        finPosErrorRecursion = largoCadena.get(errorLlamadaInvalida);

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
            // A) Hay que insertar bloques antes de procesar la instrucción?
            procesarBloque(i, codigo);
            // B) Traducir instrucción
            procesarTerceto(listaTercetos.get(i), i);

        }

        codigoWAT.append(variablesGlobales);
        codigoWAT.append(seccionDatos);
        escribirFunciones();
        codigoWAT.append(")\n");
    }

    private void procesarBloque(int i, StringBuilder codigo){

        if(AnalizadorDeBloques.tieneBloqueElseAsignado(i)){
            codigo.append(Identador.obtenerIndentacion()).append("    br ").append(1).append("\n");
            codigo.append(Identador.obtenerIndentacion()).append("    ) ;; fin block else\n");
            Identador.disminuirIndentacion();
        }

        if (AnalizadorDeBloques.tieneBloqueFinAsignado(i)) {
            for(int j = AnalizadorDeBloques.getNumeroDeFinAsignados(i); j>0; j--) {

                codigo.append(Identador.obtenerIndentacion()).append("    br ").append(0).append("\n");
                codigo.append(Identador.obtenerIndentacion()).append("    ) ;; fin block\n");
                Identador.disminuirIndentacion();

            }
        }


        if(AnalizadorDeBloques.tieneBloqueInicioAsignado(i)){

            codigo.append(Identador.obtenerIndentacion()).append("    (block").append("\n");
            Identador.aumentarIndentacion();
            if(AnalizadorDeBloques.esUnBloqueIfElse(i)){
                codigo.append(Identador.obtenerIndentacion()).append("    (block").append("\n");
                Identador.aumentarIndentacion();
            }
        }



        for (int j = 0; j < AnalizadorDeBloques.getNumeroDeFinWhileAsignados(i); j++) {
            String lblLoop = pilaWhile.pop();
            Identador.aumentarIndentacion();
            codigo.append(Identador.obtenerIndentacion()).append("    br $").append(lblLoop).append("\n");  // volver al inicio del loop
            codigo.append(Identador.obtenerIndentacion()).append("    ) ;; fin loop\n");
            codigo.append(Identador.obtenerIndentacion()).append("    ) ;; fin block while\n");

        }
    }







    private void procesarTerceto(Terceto terceto, int i) {
        String operador = terceto.getOperador();
        StringBuilder codigo = funciones.get(funcionActual);

        if (operador.startsWith("ini_")) { iniciarFuncion(operador.substring(4)); return; }
        if (operador.startsWith("fin_")) { finalizarFuncion(); return; }

        switch (operador) {
            case "WHILE_START" -> procesarWhileStart(terceto, codigo);
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
            case "LAMBDA" -> {}//Se resuelve en tiempo de compilación
        }
    }



    // --- UTILS COPIADOS (Para que compile directo) ---
    private void procesarWhileStart(Terceto t, StringBuilder codigo) {
        String lblBreak = "break_W" + contadorWhile;
        String lblLoop = "loop_W" + contadorWhile;
        contadorWhile++;
        pilaWhile.push(lblLoop);
        pilaWhileBfs.push(lblBreak);

        codigo.append(Identador.obtenerIndentacion()).append("    (block $").append(lblBreak).append("\n");
        codigo.append(Identador.obtenerIndentacion()).append("    (loop $").append(lblLoop).append("\n");
    }


    private void procesarBf(Terceto t, StringBuilder codigo, int i) {
        if(AnalizadorDeBloques.esTercetoBfWhile(i)){
            String lblBreak = pilaWhileBfs.pop();
            String op1 = obtenerValor(t.getOp1());
            ponerEnteroEnPila(op1, codigo);
            codigo.append(Identador.obtenerIndentacion()).append("    i32.eqz\n");
            codigo.append(Identador.obtenerIndentacion()).append("    br_if $").append(lblBreak).append("\n");
        } else {
            String op1 = obtenerValor(t.getOp1());
            ponerEnteroEnPila(op1, codigo);
            codigo.append(Identador.obtenerIndentacion()).append("    i32.eqz\n");
            int destino = obtenerDestino(t.getOp2());
            if (destino != -1) {
                codigo.append(Identador.obtenerIndentacion()).append("    br_if 0").append("\n");
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
            else if (nombreFunc.contains("_lambda")) codigoWAT.append(" ");
            else codigoWAT.append(" (result i32)");
            codigoWAT.append("\n").append(entrada.getValue()).append("  )\n");
        }
    }
    private void procesarComparacion(String instruccion, Terceto t, StringBuilder codigo) {
        String op1 = obtenerValor(t.getOp1());
        String op2 = obtenerValor(t.getOp2());
        ponerEnteroEnPila(op1, codigo);
        ponerEnteroEnPila(op2, codigo);
        codigo.append(Identador.obtenerIndentacion()).append("    ").append(instruccion).append("\n");
        String temp = crearTemporal();
        codigo.append(Identador.obtenerIndentacion()).append("    global.set $").append(temp).append("\n");
        t.setResultado(temp);
    }
    private void procesarSuma(Terceto terceto, StringBuilder codigo) {
        String op1 = obtenerValor(terceto.getOp1());
        String op2 = obtenerValor(terceto.getOp2());
        ponerEnteroEnPila(op1, codigo);
        ponerEnteroEnPila(op2, codigo);
        codigo.append(Identador.obtenerIndentacion()).append("    i32.add\n");
        String temp = crearTemporal();
        codigo.append(Identador.obtenerIndentacion()).append("    global.set $").append(temp).append("\n");
        terceto.setResultado(temp);
    }
    private void procesarResta(Terceto terceto, StringBuilder codigo) {
        String op1 = obtenerValor(terceto.getOp1()); // Minuendo (A)
        String op2 = obtenerValor(terceto.getOp2()); // Sustraendo (B)

        // --- 1. VALIDACIÓN DE UNDERFLOW (A < B) ---


        codigo.append(Identador.obtenerIndentacion()).append("    (block \n");
        Identador.aumentarIndentacion();
        // Cargar A y B para la comparación
        ponerEnteroEnPila(op1, codigo);
        ponerEnteroEnPila(op2, codigo);
        // Compara A < B (i32.lt_u es "less than unsigned")
        codigo.append(Identador.obtenerIndentacion()).append("    i32.lt_u ;; Verifica si A < B (Underflow)\n");
        //invertir el resultado para el br_if
        codigo.append(Identador.obtenerIndentacion()).append("    i32.eqz \n");
        codigo.append(Identador.obtenerIndentacion()).append("    br_if 0 \n");

        // Asume que posErrorRestaNegativa y lenErrorRestaNegativa están definidos.
        codigo.append(Identador.obtenerIndentacion()).append("    i32.const ").append(iniPosErrorRestaNegativa).append("\n");
        codigo.append(Identador.obtenerIndentacion()).append("    i32.const ").append(finPosErrorRestaNegativa).append("\n");
        codigo.append(Identador.obtenerIndentacion()).append("    call $alert_str ;; Muestra el mensaje de error\n");

        // Detener la ejecución
        codigo.append(Identador.obtenerIndentacion()).append("    unreachable ;; Termina el programa\n");
        Identador.disminuirIndentacion();
        codigo.append(Identador.obtenerIndentacion()).append("    )\n");



        // --- 2. EJECUCIÓN NORMAL (Si no hubo underflow) ---

        // Si llegamos aquí, A >= B. Recargamos A y B para la operación real,
        // ya que fueron consumidos por la comparación.
        ponerEnteroEnPila(op1, codigo);
        ponerEnteroEnPila(op2, codigo);

        // Ejecutar la resta
        codigo.append(Identador.obtenerIndentacion()).append("    i32.sub\n");

        // 3. Almacenar resultado
        String temp = crearTemporal();
        codigo.append(Identador.obtenerIndentacion()).append("    global.set $").append(temp).append("\n");
        terceto.setResultado(temp);
    }
    private void procesarMultiplicacion(Terceto terceto, StringBuilder codigo) {
        String op1 = obtenerValor(terceto.getOp1());
        String op2 = obtenerValor(terceto.getOp2());
        if (esFlotante(op1) || esFlotante(op2)) {
            ponerFlotanteEnPila(op1, codigo);
            ponerFlotanteEnPila(op2, codigo);
            codigo.append(Identador.obtenerIndentacion()).append("    f64.mul\n");
            String temp = crearTemporalFlotante();
            codigo.append(Identador.obtenerIndentacion()).append("    global.set $").append(temp).append("\n");
            terceto.setResultado(temp);
        } else {
            ponerEnteroEnPila(op1, codigo);
            ponerEnteroEnPila(op2, codigo);
            codigo.append(Identador.obtenerIndentacion()).append("    i32.mul\n");
            String temp = crearTemporal();
            codigo.append(Identador.obtenerIndentacion()).append("    global.set $").append(temp).append("\n");
            terceto.setResultado(temp);
        }
    }
    private void procesarDivision(Terceto terceto, StringBuilder codigo) {
            String op1 = obtenerValor(terceto.getOp1());
            String op2 = obtenerValor(terceto.getOp2());

            // 1. CARGA y VALIDACIÓN DEL DENOMINADOR (B)
            // El divisor (Op2) es el que debemos chequear.
            codigo.append(Identador.obtenerIndentacion()).append("    (block \n");
            Identador.aumentarIndentacion();
            ponerEnteroEnPila(op2, codigo);
            codigo.append(Identador.obtenerIndentacion()).append("    i32.const 0\n");
            codigo.append(Identador.obtenerIndentacion()).append("    i32.eq ;; Pila: [B == 0] (0 o 1)\n");
            codigo.append(Identador.obtenerIndentacion()).append("    i32.eqz ;;Invertir condicion\n");
            codigo.append(Identador.obtenerIndentacion()).append("    br_if 0 ;; Si B == 0, salta al IF\n");


            codigo.append(Identador.obtenerIndentacion()).append("    i32.const ").append(iniPosErrorDivCero).append("\n");
            codigo.append(Identador.obtenerIndentacion()).append("    i32.const ").append(finPosErrorDivCero).append("\n");
            codigo.append(Identador.obtenerIndentacion()).append("    call $alert_str ;; Muestra el mensaje\n");
            codigo.append(Identador.obtenerIndentacion()).append("    unreachable ;; Termina el programa\n");
            Identador.disminuirIndentacion();
            codigo.append(Identador.obtenerIndentacion()).append("    )\n");


            // --- ELSE (RAMA DE EJECUCIÓN NORMAL: B != 0) ---


            // 3. RE-CARGAR OPERANDOS y DIVIDIR
            // Los valores de Op1 y Op2 fueron consumidos por la validación, ¡debemos cargarlos de nuevo!
            ponerEnteroEnPila(op1, codigo); // Cargar A (Numerador)
            ponerEnteroEnPila(op2, codigo); // Cargar B (Denominador)
            codigo.append(Identador.obtenerIndentacion()).append("        i32.div_u ;; Ejecutar A / B\n");


            // 4. GUARDAR RESULTADO
            String temp = crearTemporal();
            codigo.append(Identador.obtenerIndentacion()).append("    global.set $").append(temp).append("\n");
            terceto.setResultado(temp);

    }
    private void procesarAsignacion(Terceto terceto, StringBuilder codigo) {
        String destino = obtenerValor(terceto.getOp1());
        String origen = obtenerValor(terceto.getOp2());
        if (destino.startsWith("_ret_")) {
            if (origen == null || origen.isEmpty()) {
                codigo.append(Identador.obtenerIndentacion()).append("    i32.const 0\n");
            } else if (esNumeroULONG(origen)) {
                String numero = origen.substring(0, origen.length() - 2);
                codigo.append(Identador.obtenerIndentacion()).append("    i32.const ").append(numero).append("\n");
            } else if (esFlotante(origen)) {
                ponerFlotanteEnPila(origen, codigo);
                codigo.append(Identador.obtenerIndentacion()).append("    i32.trunc_f64_u\n");
            } else {
                String nombreVar = asegurarVariableEntera(origen);
                codigo.append(Identador.obtenerIndentacion()).append("    global.get $").append(nombreVar).append("\n");
            }
            // Pone el candado a 0 (Función inactiva)
            String nombreLimpio = funcionActual;
            String candadoNombre = "$_lock_" + nombreLimpio;
            // --- NUEVO: Unset del candado de Recursion (Runtime) ---
            codigo.append(Identador.obtenerIndentacion()).append("    ;; Liberacion de Candado de Recursion\n");
            codigo.append(Identador.obtenerIndentacion()).append("    i32.const 0\n");
            codigo.append(Identador.obtenerIndentacion()).append("    global.set ").append(candadoNombre).append(" ;; Set Candado a 0\n");
            codigo.append(Identador.obtenerIndentacion()).append("    return\n");
            terceto.setResultado(origen);
            return;
        }
        String nombreDestino = asegurarVariableEntera(destino);
        if (esFlotante(origen)) {
            ponerFlotanteEnPila(origen, codigo);
            codigo.append(Identador.obtenerIndentacion()).append("    i32.trunc_f64_u\n");
        } else {
            ponerEnteroEnPila(origen, codigo);
        }
        codigo.append(Identador.obtenerIndentacion()).append("    global.set $").append(nombreDestino).append("\n");
        terceto.setResultado(nombreDestino);
    }
    private void procesarPrint(Terceto terceto, StringBuilder codigo) {
        String original = terceto.getOp1();
        String valor = obtenerValor(original);
        if (original != null && esCadena(original)) {
            int pos = registrarCadena(original);
            int len = largoCadena.get(original);
            codigo.append(Identador.obtenerIndentacion()).append("    i32.const ").append(pos).append("\n");
            codigo.append(Identador.obtenerIndentacion()).append("    i32.const ").append(len).append("\n");
            codigo.append(Identador.obtenerIndentacion()).append("    call $alert_str\n");
        } else {
            ponerEnteroEnPila(valor, codigo);
            codigo.append(Identador.obtenerIndentacion()).append("    call $alert_i32\n");
        }
        terceto.setResultado("");
    }
    private void procesarTrunc(Terceto terceto, StringBuilder codigo) {
        String original = terceto.getOp1();
        codigo.append(Identador.obtenerIndentacion()).append("    f64.const ").append(original).append("\n");
        codigo.append(Identador.obtenerIndentacion()).append("    i32.trunc_f64_u\n");
        String temp = crearTemporal();
        codigo.append(Identador.obtenerIndentacion()).append("    global.set $").append(temp).append("\n");
        terceto.setResultado(temp);
    }
    private void procesarLlamadaFuncion(Terceto terceto, StringBuilder codigo) {
        String funcTS = terceto.getOp1();

        // 1. Limpieza básica: quitar argumentos si vienen en el string (ej: "FUNC(arg)" -> "FUNC")
        if (funcTS != null && funcTS.contains("(")) {
            funcTS = funcTS.substring(0, funcTS.indexOf('(')).trim();
        }

        String funcWasm = limpiarNombre(funcTS);

        // 2. Reconstrucción del nombre con Scope (Manejo de anidamiento infinito)
        // Formato esperado en TS: "NOMBRE:SCOPE_RAIZ:SCOPE_PADRE:..."
        // Formato deseado en WAT: "$SCOPE_RAIZ_SCOPE_PADRE_NOMBRE"
        if (funcTS != null && funcTS.contains(":") && !funcTS.startsWith("_lambda")) {
            String[] p = funcTS.split(":");

            // Verificamos que tenga al menos Nombre y un Scope
            if (p.length >= 2) {
                StringBuilder nombreCompleto = new StringBuilder();

                // Recorremos todos los scopes (del índice 1 en adelante)
                // Esto concatena "SCOPE_RAIZ:SCOPE_PADRE:..." en el orden correcto
                for (int i = 1; i < p.length; i++) {
                    nombreCompleto.append(p[i]).append(":");
                }

                // Al final agregamos el nombre de la función (índice 0)
                nombreCompleto.append(p[0]);

                // Convertimos los ':' en '_' usando tu función auxiliar
                funcWasm = limpiarNombre(nombreCompleto.toString());
            }
        }

        // 3. Generación del Call
        codigo.append(Identador.obtenerIndentacion()).append("    call $").append(funcWasm).append("\n");

        // 4. Manejo del valor de retorno (si no es lambda/procedimiento void)
        if (funcTS != null && !funcTS.startsWith("_lambda")) {
            String temp = crearTemporal();
            // Guardamos el resultado que quedó en la pila en una temporal global
            codigo.append(Identador.obtenerIndentacion()).append("    global.set $").append(temp).append("\n");
            // Actualizamos el terceto para que quien use este resultado sepa dónde está
            terceto.setResultado(temp);
        }
    }
    private void iniciarFuncion(String nombreFunc) {
        pilaFunciones.push(funcionActual);
        String nombreLimpio = limpiarNombre(nombreFunc);
        funcionActual = nombreLimpio;
        funciones.put(nombreLimpio, new StringBuilder());
        if (nombreFunc.contains(":")) scopeActual = nombreFunc.substring(nombreFunc.indexOf(':') + 1);

        String candadoNombre = "$_lock_" + nombreLimpio;
        if (!tiposVariable.containsKey(candadoNombre)) {
            variablesGlobales.append(Identador.obtenerIndentacion()).append("  (global ").append(candadoNombre).append(" (mut i32) (i32.const 0)) ;; Candado de Recursion\n");
            tiposVariable.put(candadoNombre, "i32");
        }

        StringBuilder codigo = funciones.get(funcionActual);

        // --- NUEVO: Control y set de candado en Runtime (Al inicio del cuerpo de la funcion) ---
        codigo.append(Identador.obtenerIndentacion()).append("    ;; Bloqueo de Recursion Directa (Runtime Check)\n");
        codigo.append(Identador.obtenerIndentacion()).append("    (block\n");
        Identador.aumentarIndentacion();

        codigo.append(Identador.obtenerIndentacion()).append("    global.get ").append(candadoNombre).append("\n");
        codigo.append(Identador.obtenerIndentacion()).append("    i32.const 1\n");
        codigo.append(Identador.obtenerIndentacion()).append("    i32.ne ;; Compara candado != 1\n");
        codigo.append(Identador.obtenerIndentacion()).append("    br_if 0 ;; Si candado != 1, saltar (función inactiva, OK)\n");

        codigo.append(Identador.obtenerIndentacion()).append("    i32.const ").append(iniPosErrorRecursion).append("\n");
        codigo.append(Identador.obtenerIndentacion()).append("    i32.const ").append(finPosErrorRecursion).append("\n");
        codigo.append(Identador.obtenerIndentacion()).append("    call $alert_str ;; Muestra el error\n");
        codigo.append(Identador.obtenerIndentacion()).append("    unreachable ;; Termina el programa\n");

        Identador.disminuirIndentacion();
        codigo.append(Identador.obtenerIndentacion()).append("    )\n");

        // Pone el candado a 1 (Función activa)
        codigo.append(Identador.obtenerIndentacion()).append("    i32.const 1\n");
        codigo.append(Identador.obtenerIndentacion()).append("    global.set ").append(candadoNombre).append(" ;; Set Candado a 1\n");
    }
    private void finalizarFuncion() {

        // -------------------------------------------------------

        if (!pilaFunciones.isEmpty()) funcionActual = pilaFunciones.pop();
        else funcionActual = "main";
    }
    private void ponerEnteroEnPila(String valor, StringBuilder codigo) {
        if (valor == null || valor.isEmpty()) { codigo.append(Identador.obtenerIndentacion()).append("    i32.const 0\n"); return; }
        if (esNumeroULONG(valor)) codigo.append(Identador.obtenerIndentacion()).append("    i32.const ").append(valor.substring(0, valor.length() - 2)).append("\n");
        else codigo.append(Identador.obtenerIndentacion()).append("    global.get $").append(asegurarVariableEntera(valor)).append("\n");
    }
    private void ponerFlotanteEnPila(String valor, StringBuilder codigo) {
        if (valor == null || valor.isEmpty()) { codigo.append("    f64.const 0\n"); return; }
        if (esNumeroFlotante(valor)) {
            String valW = valor.replace(',', '.').replace('d', 'E').replace('D', 'E');
            codigo.append(Identador.obtenerIndentacion()).append("    f64.const ").append(valW).append("\n");
        } else if (esNumeroULONG(valor)) {
            codigo.append(Identador.obtenerIndentacion()).append("    i32.const ").append(valor.substring(0, valor.length() - 2)).append("\n");
            codigo.append(Identador.obtenerIndentacion()).append("    f64.convert_i32_u\n");
        } else {
            String nombreVar = limpiarNombre(valor);
            codigo.append(Identador.obtenerIndentacion()).append("    global.get $").append(nombreVar).append("\n");
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