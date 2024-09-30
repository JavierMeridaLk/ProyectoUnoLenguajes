package backen;

import java.util.ArrayList;
import java.util.List;

public class Analizador {

    private List<String> lineasHTML = new ArrayList<>();  // Para almacenar las líneas de HTML
    private List<String> lineasCSS = new ArrayList<>();   // Para almacenar las líneas de CSS
    private List<String> lineasJS = new ArrayList<>();    // Para almacenar las líneas de JS

    public Analizador() {

    }

    public void exportarHtml(String texto) {
        String[] lineas = separarPorLineas(texto);
        for (String linea : lineas) {
            System.out.println(linea);
        }
        separarTipoDeCodigo(lineas);

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
        analizadorHTML();

        analizadorCSS();

        analizadorJS();

    }

    private void mostrarLineasPorEstado() {

    }

    public void analizadorHTML() {
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
