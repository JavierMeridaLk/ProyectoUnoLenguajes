package backen;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorJs {

    private List<String> palabrasJS;
    private List<String> palabras;

    public AnalizadorJs() {
        palabras = new ArrayList<>();
        palabrasJS = new ArrayList<>();
    }

    public void separarPalabras(List<String> codigo) {
        for (String lineaInicial : codigo) {
            String lineaProcesada = lineaInicial.trim();

            // Verificar si la línea contiene un comentario //
            int indiceComentario = lineaProcesada.indexOf("//");
            if (indiceComentario != -1) {
                // Si se encuentra el //, separar la parte antes del comentario y agregarla como palabras
                String antesDelComentario = lineaProcesada.substring(0, indiceComentario).trim();
                if (!antesDelComentario.isEmpty()) {
                    String[] palabrasEnLinea = antesDelComentario.split("\\s+");
                    for (String palabra : palabrasEnLinea) {
                        if (!palabra.isEmpty()) {
                            palabras.add(palabra);
                        }
                    }
                }
                // Agregar el comentario completo como una sola palabra
                palabras.add(lineaProcesada.substring(indiceComentario).trim());
            } else {
                // Si no hay comentario, procesar normalmente
                String[] palabrasEnLinea = lineaProcesada.split("\\s+");
                for (String palabra : palabrasEnLinea) {
                    if (!palabra.isEmpty()) {
                        palabras.add(palabra);
                    }
                }
            }
        }

        // Mostrar las palabras separadas
        for (String palabra : palabras) {
            System.out.println(palabra);
        }
    }

    public void verificarPalabras() {
        // Lista de símbolos de un solo carácter a verificar
        char[] simbolos = {
            '(', ')', '[', ']', '{', '}', '+', '-', '*', '/', '=', ';', ',', '.', ':', '!', '<', '>', '&', '|'
        };

        // Recorre cada palabra
        for (String palabra : palabras) {
            StringBuilder palabraTemporal = new StringBuilder(); // Para almacenar la palabra temporalmente
            boolean dentroDeCadena = false; // Indicador para saber si estamos dentro de una cadena
            char delimitadorCadena = 0; // Almacena el tipo de delimitador de la cadena

            // Recorre cada carácter de la palabra
            for (int i = 0; i < palabra.length(); i++) {
                char actual = palabra.charAt(i);
                boolean esDobleSimbolo = false;

                // Verificar si se trata del inicio o fin de una cadena
                if (!dentroDeCadena && (actual == '"' || actual == '\'' || actual == '`')) {
                    // Si no estamos dentro de una cadena, comenzamos una
                    dentroDeCadena = true;
                    delimitadorCadena = actual;
                    palabraTemporal.append(actual); // Agregar el delimitador inicial a la palabra
                    continue;
                } else if (dentroDeCadena) {
                    // Si estamos dentro de una cadena, seguir añadiendo hasta encontrar el delimitador de cierre
                    palabraTemporal.append(actual);
                    if (actual == delimitadorCadena) {
                        dentroDeCadena = false; // Cierra la cadena
                        palabrasJS.add(palabraTemporal.toString());
                        palabraTemporal.setLength(0); // Reiniciar la palabra temporal
                    }
                    continue;
                }

                // Verificar si se trata de un comentario de línea con //
                if (actual == '/' && i + 1 < palabra.length() && palabra.charAt(i + 1) == '/') {
                    // Si había una palabra temporal, agregarla a la lista
                    if (palabraTemporal.length() > 0) {
                        palabrasJS.add(palabraTemporal.toString());
                        palabraTemporal.setLength(0); // Reiniciar la palabra temporal
                    }

                    // Agregar el comentario completo como una palabra separada
                    palabrasJS.add(palabra.substring(i)); // Tomar todo lo que resta de la palabra como un comentario

                    break; // Salir del ciclo porque el resto de la palabra es el comentario
                }

                // Verificar si se trata de un operador de dos caracteres
                if (i + 1 < palabra.length()) {
                    String dosCaracteres = palabra.substring(i, i + 2);

                    // Verificar si es alguno de los operadores de dos caracteres
                    if (dosCaracteres.equals("||") || dosCaracteres.equals("&&") || dosCaracteres.equals("==")
                            || dosCaracteres.equals("!=") || dosCaracteres.equals("<=") || dosCaracteres.equals(">=")
                            || dosCaracteres.equals("++") || dosCaracteres.equals("--")) {

                        // Si había una palabra temporal, agregarla a la lista
                        if (palabraTemporal.length() > 0) {
                            palabrasJS.add(palabraTemporal.toString());
                            palabraTemporal.setLength(0); // Reiniciar la palabra temporal
                        }
                        // Agregar el operador de dos caracteres como una palabra separada
                        palabrasJS.add(dosCaracteres);

                        // Saltar el siguiente carácter porque ya se procesaron dos caracteres
                        i++;
                        esDobleSimbolo = true;
                    }
                }

                // Si no es un operador de dos caracteres, verificar si es un símbolo de un carácter
                if (!esDobleSimbolo && esSimbolo(actual, simbolos)) {
                    // Si había una palabra temporal, agregarla a la lista
                    if (palabraTemporal.length() > 0) {
                        palabrasJS.add(palabraTemporal.toString());
                        palabraTemporal.setLength(0); // Reiniciar la palabra temporal
                    }
                    // Agregar el símbolo como una palabra separada
                    palabrasJS.add(String.valueOf(actual));
                } else if (!esDobleSimbolo) {
                    // Si no es un símbolo, agregarlo a la palabra temporal
                    palabraTemporal.append(actual);
                }
            }

            // Si queda alguna palabra sin agregar al final del ciclo, agregarla
            if (palabraTemporal.length() > 0) {
                palabrasJS.add(palabraTemporal.toString());
            }
        }

        // Mostrar las palabras separadas
        System.out.println("-------------------------------");
        System.out.println("Palabras de JS inicio:");
        for (String palabraSeparada : palabrasJS) {
            System.out.println(palabraSeparada);
        }
        System.out.println("Palabras de JS fin");
        System.out.println("-------------------------------");
    }

// Método auxiliar para verificar si un carácter es uno de los símbolos
    private boolean esSimbolo(char c, char[] simbolos) {
        for (char simbolo : simbolos) {
            // Verificar si el carácter es igual a uno de los símbolos
            if (c == simbolo) {
                return true;
            }
        }
        return false;
    }

    public void analizarJs(List<String> codigo, Token token) {
        separarPalabras(codigo);
        verificarPalabras();

        for (String palabra : palabrasJS) {
            String tokenTxt = "";
            String tipo = "";
            String lenguaje = "Javascript";
            String expresionRegular = "";
            boolean agregar = false;
            Token tokenNuevo = new Token();

            try {
                Integer.parseInt(palabra);
                tokenTxt = palabra;
                tipo = "Entero";
                expresionRegular = "[0-9]*";
                agregar = true;
            } catch (NumberFormatException e1) {
                // Si no es un entero, verificar si es un decimal
                try {
                    Double.parseDouble(palabra);
                    tokenTxt = palabra;
                    tipo = "Decimal";
                    expresionRegular = "([0-9]+.+[0-9])*";
                    agregar = true;
                } catch (NumberFormatException e2) {

                    if (palabra.equals("+")) {
                        tokenTxt = palabra;
                        tipo = "Suma";
                        expresionRegular = "+";
                        agregar = true;
                    } else if (palabra.equals("-")) {
                        tokenTxt = palabra;
                        tipo = "Resta";
                        expresionRegular = "-";
                        agregar = true;
                    } else if (palabra.equals("*")) {
                        tokenTxt = palabra;
                        tipo = "Multiplicacion";
                        expresionRegular = "*";
                        agregar = true;
                    } else if (palabra.equals("/")) {
                        tokenTxt = palabra;
                        tipo = "División";
                        expresionRegular = "/";
                        agregar = true;
                    } else if (palabra.equals("==")) {
                        tokenTxt = palabra;
                        tipo = "Igual";
                        expresionRegular = "==";
                        agregar = true;
                    } else if (palabra.equals("<")) {
                        tokenTxt = palabra;
                        tipo = "Menor que";
                        expresionRegular = "<";
                        agregar = true;
                    } else if (palabra.equals(">")) {
                        tokenTxt = palabra;
                        tipo = "Mayor que";
                        expresionRegular = ">";
                        agregar = true;
                    } else if (palabra.equals("<=")) {
                        tokenTxt = palabra;
                        tipo = "Menor o igual que";
                        expresionRegular = "<=";
                        agregar = true;
                    } else if (palabra.equals(">=")) {
                        tokenTxt = palabra;
                        tipo = "Mayor o igual que";
                        expresionRegular = ">=";
                        agregar = true;
                    } else if (palabra.equals("!=")) {
                        tokenTxt = palabra;
                        tipo = "Diferente";
                        expresionRegular = "!=";
                        agregar = true;
                    } else if (palabra.equals("||")) {
                        tokenTxt = palabra;
                        tipo = "Or";
                        expresionRegular = "||";
                        agregar = true;
                    } else if (palabra.equals("&&")) {
                        tokenTxt = palabra;
                        tipo = "And";
                        expresionRegular = "&&";
                        agregar = true;
                    } else if (palabra.equals("!")) {
                        tokenTxt = palabra;
                        tipo = "Not";
                        expresionRegular = "!";
                        agregar = true;
                    } else if (palabra.equals("++")) {
                        tokenTxt = palabra;
                        tipo = "Incremento";
                        expresionRegular = "++";
                        agregar = true;
                    } else if (palabra.equals("--")) {
                        tokenTxt = palabra;
                        tipo = "Decremento";
                        expresionRegular = "--";
                        agregar = true;
                    } else if (palabra.equals("function")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "function";
                        agregar = true;
                    } else if (palabra.equals("const")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "const";
                        agregar = true;
                    } else if (palabra.equals("let")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "let";
                        agregar = true;
                    } else if (palabra.equals("document")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "document";
                        agregar = true;
                    } else if (palabra.equals("event")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "event";
                        agregar = true;
                    } else if (palabra.equals("alert")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "alert";
                        agregar = true;
                    } else if (palabra.equals("for")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "for";
                        agregar = true;
                    } else if (palabra.equals("while")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "while";
                        agregar = true;
                    } else if (palabra.equals("if")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "if";
                        agregar = true;
                    } else if (palabra.equals("else")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "else";
                        agregar = true;
                    } else if (palabra.equals("return")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "return";
                        agregar = true;
                    } else if (palabra.equals("console.log")) {
                        tokenTxt = palabra;
                        tipo = "Palabra Reservada";
                        expresionRegular = "console.log";
                        agregar = true;
                    } else if (palabra.equals("null")) {
                        tokenTxt = palabra;
                        tipo = "Aritmeticos";
                        expresionRegular = "null";
                        agregar = true;
                    } else if (palabra.equals("(")) {
                        tokenTxt = palabra;
                        tipo = "Paréntesis";
                        expresionRegular = "(";
                        agregar = true;
                    } else if (palabra.equals(")")) {
                        tokenTxt = palabra;
                        tipo = "Paréntesis";
                        expresionRegular = ")";
                        agregar = true;
                    } else if (palabra.equals("[")) {
                        tokenTxt = palabra;
                        tipo = "Corchetes";
                        expresionRegular = "[";
                        agregar = true;
                    } else if (palabra.equals("]")) {
                        tokenTxt = palabra;
                        tipo = "Corchetes";
                        expresionRegular = "]";
                        agregar = true;
                    } else if (palabra.equals("{")) {
                        tokenTxt = palabra;
                        tipo = "Llaves";
                        expresionRegular = "{";
                        agregar = true;
                    } else if (palabra.equals("}")) {
                        tokenTxt = palabra;
                        tipo = "Llaves";
                        expresionRegular = "}";
                        agregar = true;
                    } else if (palabra.equals("=")) {
                        tokenTxt = palabra;
                        tipo = "Asignación";
                        expresionRegular = "=";
                        agregar = true;
                    } else if (palabra.equals(";")) {
                        tokenTxt = palabra;
                        tipo = "Punto y coma";
                        expresionRegular = ";";
                        agregar = true;
                    } else if (palabra.equals(",")) {
                        tokenTxt = palabra;
                        tipo = "Coma";
                        expresionRegular = ",";
                        agregar = true;
                    } else if (palabra.equals(".")) {
                        tokenTxt = palabra;
                        tipo = "Punto";
                        expresionRegular = ".";
                        agregar = true;
                    } else if (palabra.equals(":")) {
                        tokenTxt = palabra;
                        tipo = "Dos puntos";
                        expresionRegular = ":";
                        agregar = true;
                    } else if (palabra.equals("true")) {
                        tokenTxt = palabra;
                        tipo = "Booleano";
                        expresionRegular = "true";
                        agregar = true;
                    } else if (palabra.equals("false")) {
                        tokenTxt = palabra;
                        tipo = "Booleano";
                        expresionRegular = "false";
                        agregar = true;
                    } else if ((palabra.charAt(0) == '"' && palabra.charAt(palabra.length() - 1) == '"')
                            || (palabra.charAt(0) == '\'' && palabra.charAt(palabra.length() - 1) == '\'')
                            || (palabra.charAt(0) == '`' && palabra.charAt(palabra.length() - 1) == '`')) {//cadenas
                        tokenTxt = palabra;
                        tipo = "Cadena";
                        expresionRegular = "('||\"||`)+[a-zA-z]+('||\"||`)";
                        agregar = true;
                    } else if (verificaIdentificador(palabra)) {//identificador
                        tokenTxt = palabra;
                        tipo = "Identificador";
                        expresionRegular = "[a-zA-Z]+([a-zA-Z] || [0-9] || [ _ ])*";
                        agregar = true;
                    } else if (palabra.charAt(0) == '/' && palabra.charAt(1) == '/') {//comentario
                        tokenTxt = palabra;
                        tipo = "Comentario";
                        expresionRegular = "//cualquier caracter";
                        agregar = true;
                    }else {
                        agregar = false;
                        // Si no pertenece a ninguna de las categorías anteriores
                        System.out.println(palabra + " es un error.");
                        tokenNuevo.setToken(tokenTxt);
                        tokenNuevo.setLenguaje(lenguaje);
                        token.getTokenError().add(tokenNuevo);
                    }
                }
            }
            if (agregar) {
                tokenNuevo.setToken(tokenTxt);
                tokenNuevo.setExpresionRegular(expresionRegular);
                tokenNuevo.setLenguaje(lenguaje);
                tokenNuevo.setTipo(tipo);
                token.getListaDeTokens().add(tokenNuevo);
                // Agregar la palabra y su tipo al resultado (esto depende de cómo manejes el Token)
            }
        }
    }

    public boolean verificaIdentificador(String palabra) {
        // La palabra no puede estar vacía
        if (palabra == null || palabra.length() == 0) {
            return false;
        }
        // Verificar que el primer carácter sea una letra
        if (!Character.isLetter(palabra.charAt(0))) {
            return false;
        }
        // Verificar que los siguientes caracteres sean letras, dígitos o '_'
        for (int i = 1; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return false;
            }
        }
        // Si todo cumple, la cadena es válida
        return true;
    }
}
