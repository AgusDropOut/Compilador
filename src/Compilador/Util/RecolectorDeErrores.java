package Compilador.Util;

import java.util.ArrayList;
import java.util.List;

public class RecolectorDeErrores {
    private static final List<String> errores = new ArrayList<>();
    private static final List<String> warnings = new ArrayList<>();

    private static final String YELLOW = "\u001B[33m";
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";

    /** Agrega un error con su número de línea evitando duplicados en la misma línea. */
    public static void agregarError(String mensaje, int linea) {
        String entrada = "Línea " + linea + ": " + mensaje;

        // Evitar duplicado exacto
        if (!errores.contains(entrada)) {
            errores.add(entrada);
        }
    }

    /** Agrega warning evitando duplicados en la misma línea. */
    public static void agregarWarning(String mensaje, int linea) {
        String entrada = "Línea " + linea + ": " + mensaje;

        if (!warnings.contains(entrada)) {
            warnings.add(entrada);
        }
    }

    public static void imprimirMensajes() {
        if (errores.isEmpty() && warnings.isEmpty()) {
            System.out.println("✅ Sin errores ni advertencias.");
            return;
        }

        if (!warnings.isEmpty()) {
            System.out.println("=== Advertencias ===");
            for (String w : warnings)
                System.out.println(YELLOW + w + RESET);
        }

        if (!errores.isEmpty()) {
            System.out.println("\n=== Errores ===");
            for (String e : errores)
                System.out.println(RED + e + RESET);
        }

        System.out.println("====================");
    }

    public boolean hayErrores() { return !errores.isEmpty(); }

    public void limpiar() {
        errores.clear();
        warnings.clear();
    }
}
