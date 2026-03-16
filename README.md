# WebAssembly Compiler

This repository contains a compiler developed in Java that translates high-level source code into WebAssembly (Wasm). The system includes an integrated testing environment via a local web server to verify the execution of the compiled code.

## System Requirements

* **Java Runtime Environment (JRE) / Java Development Kit (JDK) 17 or higher.**
  * Using a modern JDK distribution (such as [Adoptium Temurin](https://adoptium.net/)) is recommended to ensure compatibility and avoid errors when passing environment arguments. A basic JRE meeting the version requirement is also sufficient.

## Execution Instructions

1. Open a terminal or command prompt.
2. Navigate to the directory containing the `Compilador.jar` executable and the test files.
3. Run the compiler by providing the path to the source code file as a parameter:

```bash
java -jar Compilador.jar <source_code_path>
```

**Usage Example:**

```bash
java -jar Compilador.jar PRUEBA_DE_FUEGO.txt
```

## Compilation Flow and Testing Environment

Upon a successful compilation, the system automates the deployment and testing process:

1. **Intermediate Code Generation:** The compiler generates an `out.wat` (WebAssembly Text Format) file in the root directory. The contents of this file are also printed to the console for inspection.
2. **Assembly:** The `.wat` code is processed internally using `wat2wasm` to generate the final executable binary.
   * *Note:* The `wat2wasm` executable is included in the directory in case manual assembly is required (by running `wat2wasm out.wat`). If an external source code generates invalid instructions, the console will display the native `wat2wasm` errors before aborting the process.
3. **Local Server:** A lightweight HTTP server is initialized, and the default web browser is automatically opened to load the WebAssembly execution environment.
4. **Standard Output (Prints):** Print instructions defined in the source code will be displayed in the browser via notifications or pop-ups using the following format: `Valor/cadena recibida --> {Valor}`.
5. **Termination:** Once testing in the browser is complete, press the `Enter` key in the terminal. This will shut down the local server and perform an automatic cleanup of the generated temporary files.

## Test Cases

The repository includes multiple use cases to validate the compiler's behavior:

* **Complex Tests (`PRUEBA_DE_FUEGO`):** Files that evaluate the performance and correctness of the compiler in advanced scenarios. C++ equivalents of these algorithms are provided so the outputs can be independently verified (e.g., using [OnlineGDB](https://www.onlinegdb.com/online_c++_compiler)).
* **Classic Algorithms:** Test implementations for standard computational problems, including iterative Fibonacci and Factorial calculations.
* **Error Validation:** Files with names containing the words `error` or `incorrecto` possess deliberate lexical, syntactic, or semantic flaws. Their purpose is to verify that the compiler correctly intercepts issues and reports the expected failures without generating final code. All other test cases are expected to compile successfully.
