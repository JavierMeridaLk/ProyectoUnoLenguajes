package backen;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorCss {

    private List<String> codigoCSS;
    private List<String> palabrasCSS;
    private final String[] ETIQUETAS = {"body", "header", "main", "nav", "aside", "div", "ul", "ol", "li", "a", "h1", "h2", "h3", "h4", "h5", "h6",
        "p", "span", "label", "textarea", "button", "section", "article", "footer"};
    private final String[] COMBINADORES = {">", "+", "~", " "};//agregar las condiciones del " "
    private final String[] REGLAS = {"color", "background-color", "backgound", "font-size", "font-weight", "font-family", "font-aling", "width", "height",
        "min-width", "min-height", "max-width", "max-height", "display", "inline", "block", "inline-block", "flex", "grid", "none", "margin", "border",
        "padding", "content", "border-color", "border-style", "border-width", "border-top", "border-bottom", "border-left", "border-right",
        "box-sizing", "border-box", "position", "static", "relative", "absolute", "sticky", "fixed", "top", "bottom", "left", "right", "z-index",
        "justify-content", "align-items", "border-radius", "auto", "float", "list-style", "text-align", "box-shadow"};
    private final String[] OTROS = {"px", "%", "rem", "em", "vw", "vh", ":hover", ":active", ":not()", ":nth-child()", "odd", "even", "::before",
        "::after", ":", ";", ",", "(", ")", "{", "}"};

    public AnalizadorCss() {

    }

    public void analizarCss(List<String> codigo, Token token) {

        separarPorPalabras(codigo);

        for (String palabra : palabrasCSS) {
            Token tokenNuevo = new Token();
            String expresion = "";
            String lenguaje = "CSS";
            String tipo = "";
            boolean agregar = true;
            if (existeEnArreglo(palabra, ETIQUETAS)) {
                expresion = palabra;
                tipo = "Etiqueta o Tipo";
                agregar = true;
            } else if (existeEnArreglo(palabra, COMBINADORES)) {
                expresion = palabra;
                tipo = "Combinadores";
                agregar = true;
            } else if (existeEnArreglo(palabra, REGLAS)) {
                expresion = palabra;
                tipo = "Reglas";
                agregar = true;
            } else if (existeEnArreglo(palabra, OTROS)) {
                expresion = palabra;
                tipo = "Otros";
                agregar = true;
            } else if (palabra.equals("*")) {
                expresion = "";
                tipo = "Universal";
                agregar = true;
            } else if (palabra.matches("\\.[a-z][a-z0-9_]*")) {
                expresion = "[a-z]+ [0-9]* (- ([a-z] | [0-9])+)*";
                tipo = "De clase"; // Aquí pones el código que deseas ejecutar si cumple la condición
                agregar = true;
            } else if (palabra.matches("#[a-z][a-z0-9_]*")) {
                expresion = "#[a-z]+ [0-9]* (- ([a-z] | [0-9])*)";
                tipo = "De id"; // Aquí pones el código que deseas ejecutar si cumple la condición
                agregar = true;
            } else if (palabra.matches("'[^']*'")) {
                tipo = "Cadena"; // Aquí pones el código que deseas ejecutar si cumple la condición
                expresion = "'+([a-z]||[0-9]||)+'";
                agregar = true;
            } else if (palabra.matches("#([a-fA-F0-9]{3}|[a-fA-F0-9]{6})")) {
                expresion = "#+([a-fA-F0-9]{3}||[a-fA-F0-9]{6})";
                tipo = "hexadecimal"; // La palabra es un color hexadecimal válido
                agregar = true;
            } else if (palabra.matches("rgba\\((\\d{1,3},\\s*,\\s*){3}(\\d(\\.\\d+)?|1|0)?\\)")) {
                expresion = "rgba+(+([0-9]{3}||[0-9]{4})+)";
                tipo = "rgba"; // Color rgba válido
                agregar = true;
            } else if (palabra.matches("[a-z]+[0-9]*(-([a-z]|[0-9])*)*")) {
                expresion = "[a-z]+ [0-9]* (- ([a-z] | [0-9])+)*";
                tipo = "Identificador";
                agregar = true;
            } else if (palabra.matches("-?\\d+")) {
                expresion = "[0-9]*";
                System.out.println("Es un número entero válido");
                agregar = true;
            } else {
                agregar = false;
                // Si no pertenece a ninguna de las categorías anteriores
                System.out.println(palabra + " es un error.");
                tokenNuevo.setToken(palabra);
                tokenNuevo.setLenguaje(lenguaje);
                token.getTokenError().add(tokenNuevo);
            }
            if (agregar) {
                tokenNuevo.setToken(palabra);
                tokenNuevo.setExpresionRegular(expresion);
                tokenNuevo.setLenguaje(lenguaje);
                tokenNuevo.setTipo(tipo);
                token.getListaDeTokens().add(tokenNuevo);
                // Agregar la palabra y su tipo al resultado (esto depende de cómo manejes el Token)
            }

        }
        //return codigoCSS;
    }

    private boolean existeEnArreglo(String palabra, String[] arreglo) {
        for (String item : arreglo) {
            if (palabra.equals(item)) {
                return true;
            }
        }
        return false;
    }

    private void separarPorPalabras(List<String> codigo) {
        palabrasCSS = new ArrayList<>();
        StringBuilder palabraCreada = new StringBuilder();
        char actual;
        String palabraNueva = "";
        String palabraTemporal = "";

        for (String linea : codigo) {
            // Quitar espacios al inicio y al final de la línea
            String lineaProcesada = linea.trim();
            if (!lineaProcesada.isEmpty()) {
                for (int i = 0; i < lineaProcesada.length(); i++) {
                    actual = lineaProcesada.charAt(i);
                    if (actual == ' ' || !haySiguientePalabra(i, lineaProcesada.length())) {
                        // Verificar si palabraNueva no está vacía antes de acceder al último carácter
                        if (!palabraNueva.isEmpty()) {
                            char ultimoCaracter = palabraNueva.charAt(palabraNueva.length() - 1);
                            if (esDelimitador(ultimoCaracter)) {
                                for (int j = 0; j < palabraNueva.length() - 1; j++) {
                                    palabraTemporal = palabraTemporal + palabraNueva.charAt(j);
                                }
                                palabrasCSS.add(palabraTemporal);
                                palabrasCSS.add(String.valueOf(ultimoCaracter));
                                System.out.println(palabraTemporal);
                                System.out.println(ultimoCaracter);
                                palabraTemporal = "";
                            } else {
                                palabrasCSS.add(palabraNueva);
                            }
                        }
                        palabraNueva = "";
                    } else {
                        palabraNueva = palabraNueva + actual;
                    }
                }
            }
        }
    }

    private boolean esDelimitador(char c) {
        return c == '(' || c == ')' || c == ';' || c == '{' || c == '}' || c == ':' || c == ',';
    }

    private boolean haySiguientePalabra(int x, int y) {

        return x + 1 != y;
    }
}
