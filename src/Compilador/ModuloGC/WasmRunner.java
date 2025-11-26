package Compilador.ModuloGC;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

public class WasmRunner {

    public static void ejecutar(Path rutaWatGenerado) {
        HttpServer server = null;
        Path tempDir = null;

        try {
            System.out.println("--- INICIANDO WASM RUNNER (MODO SINGLE-JAR) ---");

            // 1. CREAR ENTORNO TEMPORAL (Sandbox)
            tempDir = Files.createTempDirectory("compilador_runtime_");
            System.out.println("Entorno temporal creado en: " + tempDir);

            // 2. EXTRAER RECURSOS DEL JAR
            // Extraemos el .exe y el .html desde adentro del JAR hacia la carpeta temporal
            Path toolExe = extraerRecurso("/tools/wat2wasm.exe", tempDir.resolve("wat2wasm.exe"));
            Path indexHtml = extraerRecurso("/templates/index.html", tempDir.resolve("index.html"));

            // Copiamos el .wat que generó tu compilador a la carpeta temporal
            Path watEnTemp = tempDir.resolve("programita.wat");
            Files.copy(rutaWatGenerado, watEnTemp, StandardCopyOption.REPLACE_EXISTING);

            // Definimos donde va a quedar el wasm
            Path wasmOutput = tempDir.resolve("programita.wasm");

            // 3. COMPILAR (WAT -> WASM)
            System.out.println("1. Compilando WAT a WASM...");

            // Le damos permisos de ejecución al exe extraído (importante en algunos sistemas)
            toolExe.toFile().setExecutable(true);

            ProcessBuilder pb = new ProcessBuilder(
                    toolExe.toAbsolutePath().toString(),
                    watEnTemp.toAbsolutePath().toString(),
                    "-o",
                    wasmOutput.toAbsolutePath().toString()
            );
            pb.inheritIO();
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.err.println("Error: Falló la compilación de wat2wasm.");
                return; // No borramos directorio para que puedas debuguear si falla
            }

            // 4. LEVANTAR SERVIDOR
            // Usamos puerto 0 para que el sistema asigne uno libre automáticamente
            server = SimpleFileServer.createFileServer(
                    new InetSocketAddress(0), // <--- CAMBIO CLAVE: 0 en vez de 8080
                    tempDir,
                    SimpleFileServer.OutputLevel.NONE
            );
            server.start();

            // Obtenemos qué puerto nos regaló el sistema
            int port = server.getAddress().getPort();

            // Construimos la URL con ese puerto
            String url = "http://localhost:" + port + "/index.html";

            System.out.println("2. Servidor listo en: " + url);

            // 5. ABRIR NAVEGADOR
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                System.out.println("Abre manualmente: " + url);
            }

            // 6. ESPERA ACTIVA
            System.out.println("\n[ENTER] para cerrar el programa y limpiar archivos...");
            new Scanner(System.in).nextLine();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) server.stop(0);
            if (tempDir != null) borrarDirectorio(tempDir);
            System.out.println("Limpieza completada. Adiós.");
        }
    }

    /**
     * Saca un archivo de dentro del JAR y lo copia al disco temporal.
     */
    private static Path extraerRecurso(String rutaInterna, Path rutaDestino) throws IOException {
        // La ruta interna debe empezar con /
        try (InputStream is = WasmRunner.class.getResourceAsStream(rutaInterna)) {
            if (is == null) {
                throw new IOException("Recurso no encontrado dentro del JAR: " + rutaInterna);
            }
            Files.copy(is, rutaDestino, StandardCopyOption.REPLACE_EXISTING);
        }
        return rutaDestino;
    }

    private static void borrarDirectorio(Path path) {
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            // Ignorar errores de borrado en temporales
        }
    }
}