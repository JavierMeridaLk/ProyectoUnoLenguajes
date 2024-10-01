package backen;

import java.util.ArrayList;
import java.util.List;

public class Analizador {

    private List<String> lineasHTML = new ArrayList<>();  // Para almacenar las líneas de HTML
    private List<String> lineasCSS = new ArrayList<>();   // Para almacenar las líneas de CSS
    private List<String> lineasJS = new ArrayList<>();    // Para almacenar las líneas de JS

    private List<String> codigoHTML = new ArrayList<>();  // Para almacenar las líneas de HTML
    private List<String> codigoCSS = new ArrayList<>();   // Para almacenar las líneas de CSS
    private List<String> codigoJS = new ArrayList<>();    // Para almacenar las líneas de JS

    private List<String> codigoOptimizado = new ArrayList<>();
     private List<Token> tokenOptimizacion = new ArrayList<>();

    public Analizador() {

    }

    public void exportarHtml(String texto) {
        String[] lineas = separarPorLineas(texto);
        for (String linea : lineas) {
            System.out.println(linea);
        }
        separarTipoDeCodigo(lineas);

    }

    public List<String> optimizarCodigo(String texto) {
        Token token = new Token();
        boolean agregar;
        int contadorDeEspaciosVacios = 0;
        String[] lineas = separarPorLineas(texto);

        for (int i = 0; i < lineas.length; i++) {

            if (lineas[i].isEmpty()) {
                contadorDeEspaciosVacios++;
            } else {
                contadorDeEspaciosVacios = 0;
            }
            
            if (contadorDeEspaciosVacios > 0 || tieneComentario(lineas[i])) {
                token.getTokenOptimizacion().add(token);
                agregar = false;
            } else {
                agregar = true;
            }

            if (agregar) {
                codigoOptimizado.add(lineas[i]);
            }
        }

        return codigoOptimizado;
    }

    public boolean tieneComentario(String linea) {
        for (int j = 0; j < linea.length() - 1; j++) { // Cambia < linea.length() por < linea.length() - 1
            if (linea.charAt(j) == '/' && linea.charAt(j + 1) == '/') {
                return true;
            }
        }
        return false;
    }

    public String[] separarPorLineas(String texto) {
        String[] lineas = texto.split("\n");
        // Separa el texto en líneas usando el salto de línea como delimitador
        return texto.split("\n");
    }

    public void separarTipoDeCodigo(String[] lineas) {
        String estadoActual = ""; // Estado actual (html, css, js)

        for (int fila = 0; fila < lineas.length; fila++) {
            String linea = lineas[fila].trim(); // Elimina espacios en blanco al principio y al final de la línea

            // Verifica si se ha encontrado un cambio de estado
            if (linea.equals(">>[html]")) {
                estadoActual = "html";
            } else if (linea.equals(">>[css]")) {
                estadoActual = "css";
            } else if (linea.equals(">>[js]")) {
                estadoActual = "js";
            } else {
                // Dependiendo del estado actual, agrega la línea al ArrayList correspondiente
                switch (estadoActual) {
                    case "html":
                        lineasHTML.add(linea);
                        break;
                    case "css":
                        lineasCSS.add(linea);
                        break;
                    case "js":
                        lineasJS.add(linea);
                        break;
                }
            }
        }

        analizadorCSS();

        analizadorJS();

        analizadorHTML(codigoCSS, codigoJS);

    }

    private void mostrarLineasPorEstado() {

    }

    public void analizadorHTML(List<String> codigoCSS, List<String> codigoJS) {
        System.out.println("-------------------");
        System.out.println("HTML Lines:");
        System.out.println("-------------------");
        for (String linea : lineasHTML) {
            System.out.println(linea);
        }
    }

    public void analizadorCSS() {
        System.out.println("-------------------");

        System.out.println("CSS Lines:");
        System.out.println("-------------------");
        for (String linea : lineasCSS) {
            System.out.println(linea);
        }
    }

    public void analizadorJS() {
        System.out.println("-------------------");
        System.out.println("JS Lines:");
        System.out.println("-------------------");
        for (String linea : lineasJS) {
            System.out.println(linea);
        }

    }

}
