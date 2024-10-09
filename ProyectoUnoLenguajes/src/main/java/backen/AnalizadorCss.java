package backen;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorCss {

    private List<String> codigoCSS;
    private List<String> palabrasCSS;
    private List<String> palabras;
    private final String[] ETIQUETAS = {"body", "header", "main", "nav", "aside", "div", "ul", "ol", "li", "a", "h1", "h2", "h3", "h4", "h5", "h6",
        "p", "span", "label", "textarea", "button", "section", "article", "footer"};
    private final String[] COMBINADORES = {">", "+", "~", " "};
    private final String[] REGLAS = {"color", "background-color", "backgound", "font-size", "font-weight", "font-family", "font-aling", "width", "height",
        "min-width", "min-height", "max-width", "max-height", "display", "inline", "block", "inline-block", "flex", "grid", "none", "margin", "border",
        "padding", "content", "border-color", "border-style", "border-width", "border-top", "border-bottom", "border-left", "border-right",
        "box-sizing", "border-box", "position", "static", "relative", "absolute", "sticky", "fixed", "top", "bottom", "left", "right", "z-index",
        "justify-content", "align-items", "border-radius", "auto", "float", "list-style", "text-align", "box-shadow"};
    private final String[] OTROS = {"px", "%", "rem", "em", "vw", "vh", ":hover", ":active", ":not()", ":nth-child()", "odd", "even", "::before",
        "::after", ":", ";", ",", "(", ")", "{", "}"};

    public AnalizadorCss() {
        palabrasCSS = new ArrayList<>();
        palabras = new ArrayList<>();
    }

    public void analizarCss(List<String> codigo, Token token) {
        separarPorPalabras(codigo);
        validarPalabras();

        for (String palabra : palabrasCSS) {
            Token tokenNuevo = new Token();
            String expresion = "";
            String lenguaje = "CSS";
            String tipo = "";
            boolean agregar = true;
            // Verificar las diferentes categorías
            if (existeEnArreglo(palabra, ETIQUETAS)) {
                expresion = palabra;
                tipo = "Etiqueta o Tipo";
            } else if (existeEnArreglo(palabra, COMBINADORES)) {
                expresion = palabra;
                tipo = "Combinadores";
            } else if (existeEnArreglo(palabra, REGLAS)) {
                expresion = palabra;
                tipo = "Reglas";
            } else if (existeEnArreglo(palabra, OTROS)) {
                expresion = palabra;
                tipo = "Otros";
            } else if (palabra.equals("*")) {
                expresion = "";
                tipo = "Universal";
            } else if (esClase(palabra)) {
                expresion = "Clase";
                tipo = "De clase";
            } else if (esId(palabra)) {
                expresion = "Id";
                tipo = "De id";
            } else if (esCadena(palabra)) {
                tipo = "Cadena";
                expresion = "Cadena encontrada";
            } else if (esColorHexadecimal(palabra)) {
                expresion = "Color hexadecimal";
                tipo = "hexadecimal";
            } else if (esRgba(palabra)) {
                expresion = "Color rgba";
                tipo = "rgba";
            } else if (esIdentificador(palabra)) {
                expresion = "Identificador";
                tipo = "Identificador";
            } else if (esEntero(palabra)) {
                expresion = "Número entero";
                tipo = "Entero";
            } else {
                // Si es un error
                agregar = false;
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
                // Agregar nuevo token al listado
            }
        }
    }

    private boolean existeEnArreglo(String palabra, String[] arreglo) {
        for (String item : arreglo) {
            if (palabra.equals(item)) {
                return true;
            }
        }
        return false;
    }
// Métodos auxiliares para validar cada tipo de palabra
    private boolean esClase(String palabra) {
        return palabra.length() > 1 && palabra.startsWith(".") && esNombreValido(palabra.substring(1));
    }

    private boolean esId(String palabra) {
        return palabra.length() > 1 && palabra.startsWith("#") && esNombreValido(palabra.substring(1));
    }

    private boolean esCadena(String palabra) {
        return palabra.length() > 1 && palabra.startsWith("'") && palabra.endsWith("'");
    }

    private boolean esColorHexadecimal(String palabra) {
        if (palabra.length() != 7 && palabra.length() != 4) {
            return false; // Longitud debe ser 7 (#xxxxxx) o 4 (#xxx)
        }
        if (palabra.charAt(0) != '#') {
            return false; // Debe comenzar con #
        }
        // Validar caracteres hexadecimales
        for (int i = 1; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            if (!isHexadecimal(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean esRgba(String palabra) {
        if (!palabra.startsWith("rgba(") || !palabra.endsWith(")")) {
            return false;
        }
        String contenido = palabra.substring(5, palabra.length() - 1);
        String[] valores = contenido.split(",");
        if (valores.length != 4) {
            return false; // Debe haber 4 valores
        }
        for (String valor : valores) {
            if (!esNumero(valor.trim()) && !valor.trim().equals("1") && !valor.trim().equals("0")) {
                return false; // No es un número válido
            }
        }
        return true;
    }

    private boolean esIdentificador(String palabra) {
        // Un identificador comienza con una letra y puede contener letras, números y guiones
        return !palabra.isEmpty() && Character.isLetter(palabra.charAt(0)) && esNombreValido(palabra);
    }

    private boolean esEntero(String palabra) {
        for (char c : palabra.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return !palabra.isEmpty();
    }

    private boolean esNombreValido(String nombre) {
        // Comprueba si el nombre es válido (letras, números, y guiones bajos)
        for (char c : nombre.toCharArray()) {
            if (!(Character.isLetter(c) || Character.isDigit(c) || c == '_')) {
                return false;
            }
        }
        return true;
    }

    private boolean isHexadecimal(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    private boolean esNumero(String numero) {
        for (char c : numero.toCharArray()) {
            if (!Character.isDigit(c) && c != '.') {
                return false; // Permitir punto decimal
            }
        }
        return true;
    }

    public void validarPalabras() {
        // Lista de caracteres especiales para separar
        char[] separadores = {'>', '+', '~', ':', ';', ',', '(', ')', '*'};
        // Recorremos cada palabra de la lista original
        for (String palabra : palabras) {
            StringBuilder palabraActual = new StringBuilder();
            boolean dentroDeComillas = false; // Booleano para indicar que estamos en un comentario
            
            for (int i = 0; i < palabra.length(); i++) {
                char caracter = palabra.charAt(i);
                // Si estamos dentro de comillas, acumulamos caracteres hasta cerrarlas
                if (dentroDeComillas) {
                    palabraActual.append(caracter);
                    if (caracter == '\'') {
                        dentroDeComillas = false; // Cerramos comillas
                        palabrasCSS.add(palabraActual.toString());
                        palabraActual.setLength(0); // Limpiamos la palabra actual
                    }
                } else if (caracter == '\'') {
                    // Si encontramos una comilla de apertura
                    if (palabraActual.length() > 0) {
                        palabrasCSS.add(palabraActual.toString()); // Añadir lo que esté acumulado
                        palabraActual.setLength(0); // Limpiamos la palabra actual
                    }
                    palabraActual.append(caracter);
                    dentroDeComillas = true; 
                } else if (esSeparador(caracter, separadores)) {
                    // Si es un separador, añadir la palabra actual y el separador
                    if (palabraActual.length() > 0) {
                        palabrasCSS.add(palabraActual.toString()); // Añadir la palabra formada
                        palabraActual.setLength(0); // Limpiamos la palabra actual
                    }
                    palabrasCSS.add(Character.toString(caracter)); // Añadimos el separador
                } else {
                    palabraActual.append(caracter);
                }
            }
            // Añadir la última palabra si quedó algo pendiente
            if (palabraActual.length() > 0) {
                palabrasCSS.add(palabraActual.toString());
            }
        }
    }
// Método auxiliar que verifica si el carácter es un separador
    private boolean esSeparador(char caracter, char[] separadores) {
        for (char caracterIngresado : separadores) {
            if (caracter == caracterIngresado) {
                return true;
            }
        }
        return false;
    }

    private void separarPorPalabras(List<String> codigo) {

        for (String lineaInicial : codigo) {
            String lineaProcesada = lineaInicial.trim();
            StringBuilder palabraActual = new StringBuilder();
            // Recorrer cada carácter en la línea
            for (int i = 0; i < lineaProcesada.length(); i++) {
                char caracter = lineaProcesada.charAt(i);
                if (caracter == ' ' || caracter == '\n') {
                    if (palabraActual.length() > 0) {
                        palabras.add(palabraActual.toString()); // Añadir la palabra
                        palabraActual.setLength(0); // Limpiar la palabra actual
                    }
                } else {
                    // Continuamos formando la palabra temporal
                    palabraActual.append(caracter);
                }
            }
            // Añadir la última palabra al final de la línea si no está vacía
            if (palabraActual.length() > 0) {
                palabras.add(palabraActual.toString());
            }
        }
    }
}
