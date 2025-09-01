public class PalabrasReservadas {
    public static String[] palabrasReservadas = new String[]{"while","if"};

    public static boolean existe(String palabra) {
        boolean existe = false;
        for (int i = 0; i < palabrasReservadas.length; i++) {
            if (palabra.equals(palabrasReservadas[i])) {
                existe = true;
                break;
            }
        }
        return existe;
    }
}
