package Compilador.ModuloGC;

public class Identador {
    private static int nivelIndentacion = 0;
    private static final String ESPACIOS_POR_NIVEL = "    "; // 4 espacios por nivel

    public static void aumentarIndentacion() {
        nivelIndentacion++;
    }

    public static void disminuirIndentacion() {
        if (nivelIndentacion > 0) {
            nivelIndentacion--;
        }
    }

    public static String obtenerIndentacion() {
        StringBuilder indentacion = new StringBuilder();
        for (int i = 0; i < nivelIndentacion; i++) {
            indentacion.append(ESPACIOS_POR_NIVEL);
        }
        return indentacion.toString();
    }
}
