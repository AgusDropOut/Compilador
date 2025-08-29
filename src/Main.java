import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {

        AnalizadorLexico analizadorLexico = new AnalizadorLexico(new MatrizDeTransicion());


        String content = new String(Files.readAllBytes(Paths.get("src/archivo.txt")));
        int pointer = 0;

        char c = content.charAt(pointer);
        System.out.println("Primer carácter: " + c);

        pointer++;
        c = content.charAt(pointer);
        System.out.println("Segundo carácter: " + c);

        pointer++;
        c = content.charAt(pointer);
        System.out.println("Tercer carácter: " + c);
    }
}