package backen;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorHtml {

    private List<String> palabrasHtml;
    private List<String> palabras;
    private List<String> codigoHTML;
    private final String[] TIPO_DE_CIERRE_ESP = {"<principal/>", "<encabezado/>", "<navegacion/>", "<apartado/>", "<listaordenada/>", "<listadesordenada/>", "<itemlista/>", "<anclaje/>", "<contenedor/>",
        "<seccion/>", "<articulo/>", "<titulo1/>", "<titulo2/>", "<titulo3/>", "<titulo4/>", "<titulo5/>", "<titulo6/>", "<parrafo/>", "<span/>", "<entrada/>",
        "<formulario/>", "<label/>", "<area/>", "<boton/>", "<piepagina/>"};
    private final String[] TIPO_DE_APERTURA_ESP = {"<principal", "<encabezado", "<navegacion", "<apartado", "<listaordenada", "<listadesordenada", "<itemlista",
        "<anclaje", "<contenedor", "<seccion", "<articulo", "<parrafo", "<span", "<formulario", "<label", "<boton", "<piepagina", "<titulo1", "<titulo2",
        "<titulo3", "<titulo4", "<titulo5", "<titulo6", "<entrada", "<area"};

    public AnalizadorHtml() {
        palabrasHtml = new ArrayList<>();
        palabras = new ArrayList<>();
        codigoHTML = new ArrayList<>();
    }

    public List<String> analizarHtml(Token token, List<String> codigo) {
        separarPalabras(codigo);
        separarCaracteres();
        validarPalabras(token);
        traducirPalabras();
        return codigoHTML;
    }

    public void traducirPalabras() {
        for (String palabra : palabras) {
            String palabraTemporal = "";
            String palabraInicio = "";
            String restoPalabra = "";

            if (palabra.contains(" ")) {
                // Si hay un espacio, separamos en dos partes
                palabraInicio = palabra.substring(0, palabra.indexOf(" "));
                restoPalabra = palabra.substring(palabra.indexOf(" "));
            } else {
                // Si no hay espacio, verificamos si termina en '>', entonces solo asignamos las partes
                if (palabra.endsWith(">")) {
                    palabraInicio = palabra.substring(0, palabra.length() - 1);
                    restoPalabra = ">";
                } else {
                    palabraInicio = palabra;
                    restoPalabra = "";
                }
            }
            if (existeEnArreglo(palabra, TIPO_DE_CIERRE_ESP)) {
                switch (palabra) {
                    case "<principal/>":
                        codigoHTML.add("</main>");
                        break;
                    case "<encabezado/>":
                        codigoHTML.add("</header>");
                        break;
                    case "<navegacion/>":
                        codigoHTML.add("</nav>");
                        break;
                    case "<apartado/>":
                        codigoHTML.add("</aside>");
                        break;
                    case "<contenedor/>":
                        codigoHTML.add("</div>");
                        break;
                    case "<listaordenada/>":
                        codigoHTML.add("</ul>");
                        break;
                    case "<listadesordenada/>":
                        codigoHTML.add("</ol>");
                        break;
                    case "<itemlista/>":
                        codigoHTML.add("</li>");
                        break;
                    case "<anclaje/>":
                        codigoHTML.add("</a>");
                        break;
                    case "<seccion/>":
                        codigoHTML.add("</section>");
                        break;
                    case "<articulo/>":
                        codigoHTML.add("</article>");
                        break;
                    case "<titulo1/>":
                        codigoHTML.add("</h1>");
                        break;
                    case "<titulo2/>":
                        codigoHTML.add("</h2>");
                        break;
                    case "<titulo3/>":
                        codigoHTML.add("</h3>");
                        break;
                    case "<titulo4/>":
                        codigoHTML.add("</h4>");
                        break;
                    case "<titulo5/>":
                        codigoHTML.add("</h5>");
                        break;
                    case "<titulo6/>":
                        codigoHTML.add("</h6>");
                        break;
                    case "<parrafo/>":
                        codigoHTML.add("</p>");
                        break;
                    case "<span/>":
                        codigoHTML.add("</span>");
                        break;
                    case "<entrada/>":
                        codigoHTML.add("</input>");
                        break;
                    case "<formulario/>":
                        codigoHTML.add("</form>");
                        break;
                    case "<label/>":
                        codigoHTML.add("</label>");
                        break;
                    case "<area/>":
                        codigoHTML.add("</textarea>");
                        break;
                    case "<boton/>":
                        codigoHTML.add("</button>");
                        break;
                    case "<piepagina/>":
                        codigoHTML.add("</footer>");
                        break;
                }
            } else if (palabra.charAt(0) == '/' && palabra.charAt(1) == '/') {
                String comentario = "<!-- ";
                for (int i = 2; i < palabra.length(); i++) {
                    comentario = comentario + palabra.charAt(i);
                }
                comentario = comentario + " -->";
                codigoHTML.add(comentario);
            } else if (existeEnArreglo(palabraInicio, TIPO_DE_APERTURA_ESP)) {
                switch (palabraInicio) {
                    case "<principal":
                        palabraTemporal = "<main" + restoPalabra;
                        break;
                    case "<encabezado":
                        palabraTemporal = "<header" + restoPalabra;
                        break;
                    case "<navegacion":
                        palabraTemporal = "<nav" + restoPalabra;
                        break;
                    case "<apartado":
                        palabraTemporal = "<aside" + restoPalabra;
                        break;
                    case "<contenedor":
                        palabraTemporal = "<div" + restoPalabra;
                        break;
                    case "<listaordenada":
                        palabraTemporal = "<ul" + restoPalabra;
                        break;
                    case "<listadesordenada":
                        palabraTemporal = "<ol" + restoPalabra;
                        break;
                    case "<itemlista":
                        palabraTemporal = "<li" + restoPalabra;
                        break;
                    case "<anclaje":
                        palabraTemporal = "<a" + restoPalabra;
                        break;
                    case "<seccion":
                        palabraTemporal = "<section" + restoPalabra;
                        break;
                    case "<articulo":
                        palabraTemporal = "<article" + restoPalabra;
                        break;
                    case "<titulo1":
                        palabraTemporal = "<h1" + restoPalabra;
                        break;
                    case "<titulo2":
                        palabraTemporal = "<h2" + restoPalabra;
                        break;
                    case "<titulo3":
                        palabraTemporal = "<h3" + restoPalabra;
                        break;
                    case "<titulo4":
                        palabraTemporal = "<h4" + restoPalabra;
                        break;
                    case "<titulo5":
                        palabraTemporal = "<h5" + restoPalabra;
                        break;
                    case "<titulo6":
                        palabraTemporal = "<h6" + restoPalabra;
                        break;
                    case "<parrafo":
                        palabraTemporal = "<p" + restoPalabra;
                        break;
                    case "<span":
                        palabraTemporal = "<span" + restoPalabra;
                        break;
                    case "<entrada":
                        palabraTemporal = "<input" + restoPalabra;
                        break;
                    case "<formulario":
                        palabraTemporal = "<form" + restoPalabra;
                        break;
                    case "<label":
                        palabraTemporal = "<label" + restoPalabra;
                        break;
                    case "<area":
                        palabraTemporal = "<textarea" + restoPalabra;
                        break;
                    case "<boton":
                        palabraTemporal = "<button" + restoPalabra;
                        break;
                    case "<piepagina":
                        palabraTemporal = "<footer" + restoPalabra;
                        break;

                }
                codigoHTML.add(palabraTemporal); // Añadir la traducción con restoPalabra
            } else {
                codigoHTML.add(palabra);
            }
            
            
        }
        System.out.println("---------------------------");
        System.out.println("Palabras traducidas");
        for (String string : codigoHTML) {
            System.out.println(string);
        }
        System.out.println("----------------------------");
    }

    private boolean existeEnArreglo(String palabra, String[] arreglo) {
        for (String item : arreglo) {
            if (palabra.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public void separarPalabras(List<String> codigo) {
        // Asumimos que la lista palabras es local
        for (String lineaInicial : codigo) {
            String lineaProcesada = lineaInicial.trim();
            // Verificar si la línea contiene un comentario //
            int indiceComentario = lineaProcesada.indexOf("//");
            if (indiceComentario != -1) {
                // Si se encuentra el //, separar la parte antes del comentario y agregarla como palabras
                String antesDelComentario = lineaProcesada.substring(0, indiceComentario).trim();
                if (!antesDelComentario.isEmpty()) {
                    // Procesar las etiquetas HTML sin dividir el contenido dentro
                    procesarLinea(antesDelComentario, palabras);
                }
                // Agregar el comentario completo como una sola palabra
                palabras.add(lineaProcesada.substring(indiceComentario).trim());
            } else {
                // Si no hay comentario, procesar normalmente solo las etiquetas HTML y el texto
                procesarLinea(lineaProcesada, palabras);
            }
        }
        // Mostrar las palabras separadas
        for (String palabra : palabras) {
            System.out.println(palabra);
        }
    }

// Método auxiliar para procesar etiquetas HTML y texto sin dividir el contenido dentro
    private void procesarLinea(String linea, List<String> palabras) {
        StringBuilder palabraActual = new StringBuilder();
        boolean dentroDeEtiqueta = false;
        for (int i = 0; i < linea.length(); i++) {
            char c = linea.charAt(i);
            if (c == '<') {
                // Si estamos construyendo una palabra fuera de una etiqueta, agregarla a la lista
                if (palabraActual.length() > 0 && !dentroDeEtiqueta) {
                    palabras.add(palabraActual.toString().trim());
                    palabraActual.setLength(0);
                }
                dentroDeEtiqueta = true;
                palabraActual.append(c); // Comienza a construir la etiqueta
            } else if (c == '>') {
                palabraActual.append(c); // Cierra la etiqueta
                palabras.add(palabraActual.toString().trim()); // Agregar la etiqueta completa
                palabraActual.setLength(0); // Reiniciar el StringBuilder
                dentroDeEtiqueta = false;
            } else {
                palabraActual.append(c);
            }
        }
        // Si queda alguna palabra después de la última etiqueta, agregarla
        if (palabraActual.length() > 0) {
            palabras.add(palabraActual.toString().trim());
        }

    }

    public void separarCaracteres() {
        palabrasHtml.clear(); // Limpiar la lista antes de comenzar a agregar las palabras separadas
        boolean dentroDeCadena = false;
        boolean dentroDeComentario = false;
        String palabraTemporal = "";
        // Lista de palabras reservadas que deben separarse
        String[] palabrasReservadas = {"class", "=", "href", "onClick", "id", "style", "type", "placeholder", "required", "name"};
        for (String palabra : palabras) {
            for (int i = 0; i < palabra.length(); i++) {
                char caracterActual = palabra.charAt(i);
                // Verificar si estamos en un comentario
                if (!dentroDeCadena && !dentroDeComentario && i < palabra.length() - 1 && palabra.charAt(i) == '/' && palabra.charAt(i + 1) == '/') {
                    dentroDeComentario = true;
                    palabraTemporal = "//";
                    i++; // Saltar el segundo '/'
                    continue;
                }
                // Si estamos dentro de un comentario, añadimos todo hasta el final de la línea
                if (dentroDeComentario) {
                    palabraTemporal += caracterActual;
                    if (caracterActual == '\n' || i == palabra.length() - 1) {
                        palabrasHtml.add(palabraTemporal.trim());
                        palabraTemporal = "";
                        dentroDeComentario = false;
                    }
                    continue;
                }
                // Verificar si estamos dentro de una cadena
                if (caracterActual == '"' && !dentroDeCadena) {
                    dentroDeCadena = true;
                    palabraTemporal += caracterActual;
                    continue;
                } else if (caracterActual == '"' && dentroDeCadena) {
                    dentroDeCadena = false;
                    palabraTemporal += caracterActual;
                    palabrasHtml.add(palabraTemporal.trim());
                    palabraTemporal = "";
                    continue;
                }
                // Si estamos dentro de una cadena, seguimos añadiendo los caracteres
                if (dentroDeCadena) {
                    palabraTemporal += caracterActual;
                    continue;
                }
                // Separar por espacio o salto de línea
                if (caracterActual == ' ' || caracterActual == '\n') {
                    if (!palabraTemporal.isEmpty()) {
                        palabrasHtml.add(palabraTemporal.trim());
                        palabraTemporal = "";
                    }
                    continue;
                }
                // Separar por los caracteres '<' y '>'
                if (caracterActual == '<' || caracterActual == '>') {
                    if (!palabraTemporal.isEmpty()) {
                        palabrasHtml.add(palabraTemporal.trim());
                        palabraTemporal = "";
                    }
                    palabrasHtml.add(String.valueOf(caracterActual));
                    continue;
                }
                // Verificar si la palabra temporal es una palabra reservada
                if (!palabraTemporal.isEmpty() && esPalabraReservada(palabraTemporal, palabrasReservadas)) {
                    palabrasHtml.add(palabraTemporal.trim());
                    palabraTemporal = "";
                }
                // Si el carácter actual es '=', separarlo como palabra reservada
                if (caracterActual == '=') {
                    if (!palabraTemporal.isEmpty()) {
                        palabrasHtml.add(palabraTemporal.trim());
                        palabraTemporal = "";
                    }
                    palabrasHtml.add("=");
                    continue;
                }
                // Si no es un caso especial, añadir el carácter a la palabra temporal
                palabraTemporal += caracterActual;
            }
            // Añadir la última palabra al final del bucle si no está vacía
            if (!palabraTemporal.isEmpty()) {
                palabrasHtml.add(palabraTemporal.trim());
                palabraTemporal = "";
            }
        }
    }

// Método auxiliar para verificar si la palabra es una palabra reservada
    private boolean esPalabraReservada(String palabra, String[] palabrasReservadas) {
        for (String reservada : palabrasReservadas) {
            if (palabra.equals(reservada)) {
                return true;
            }
        }
        return false;
    }

    public void validarPalabras(Token token) {
        for (String palabra : palabrasHtml) {
            Token tokenNuevo = new Token();
            String tokenTxt = "";
            String tipo = "";
            String lenguaje = "HTML";
            String expresionRegular = "";
            boolean agregar = true;
            // Verificar etiquetas de apertura y cierre
            switch (palabra) {
                case "<":
                    tokenTxt = "<";
                    tipo = "Signo de apertura";
                    expresionRegular = tokenTxt;
                    break;
                case ">":
                    tokenTxt = ">";
                    tipo = "Signo de cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "principal":
                    tokenTxt = "<principal>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "encabezado":
                    tokenTxt = "<encabezado>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "navegacion":
                    tokenTxt = "<navegacion>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "apartado":
                    tokenTxt = "<apartado>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "listaordenada":
                    tokenTxt = "<listaordenada>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "listadesordenada":
                    tokenTxt = "<listadesordenada>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "itemlista":
                    tokenTxt = "<itemlista>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "anclaje":
                    tokenTxt = "<anclaje>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "contenedor":
                    tokenTxt = "<contenedor>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "seccion":
                    tokenTxt = "<seccion>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "articulo":
                    tokenTxt = "<articulo>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "parrafo":
                    tokenTxt = "<parrafo>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "span":
                    tokenTxt = "<span>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "formulario":
                    tokenTxt = "<formulario>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "label":
                    tokenTxt = "<label>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "boton":
                    tokenTxt = "<boton>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "piepagina":
                    tokenTxt = "<piepagina>";
                    tipo = "Apertura";
                    expresionRegular = tokenTxt;
                    break;
                case "principal/":
                    tokenTxt = "<principal/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "encabezado/":
                    tokenTxt = "<encabezado/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "navegacion/":
                    tokenTxt = "<navegacion/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "apartado/":
                    tokenTxt = "<apartado/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "listaordenada/":
                    tokenTxt = "<listaordenada/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "listadesordenada/":
                    tokenTxt = "<listadesordenada/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "itemlista/":
                    tokenTxt = "<itemlista/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "anclaje/":
                    tokenTxt = "<anclaje/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "contenedor/":
                    tokenTxt = "<contenedor/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "seccion/":
                    tokenTxt = "<seccion/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "articulo/":
                    tokenTxt = "<articulo/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "parrafo/":
                    tokenTxt = "<parrafo/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "span/":
                    tokenTxt = "<span/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "formulario/":
                    tokenTxt = "<formulario/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "label/":
                    tokenTxt = "<label/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "boton/":
                    tokenTxt = "<boton/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                case "piepagina/":
                    tokenTxt = "<piepagina/>";
                    tipo = "Cierre";
                    expresionRegular = tokenTxt;
                    break;
                // Verificar etiquetas de una línea
                case "titulo1":
                case "titulo2":
                case "titulo3":
                case "titulo4":
                case "titulo5":
                case "titulo6":
                    tokenTxt = "<h" + (palabra.charAt(6) - '0') + ">";
                    tipo = "Una Línea";
                    expresionRegular = tokenTxt;
                    break;
                // Verificar palabras reservadas
                case "class":
                case "=":
                case "href":
                case "onClick":
                case "id":
                case "style":
                case "type":
                case "placeholder":
                case "required":
                case "name":
                    tokenTxt = palabra;
                    tipo = "Palabra Reservada";
                    expresionRegular = palabra;
                    break;
                // Verificar cadenas
                default:
                    if (palabra.startsWith("\"") && palabra.endsWith("\"")) {
                        tokenTxt = palabra;
                        tipo = "Cadena";
                        expresionRegular = "\"+([a-zZ-A] || [0-9] )*+\"";
                    } else {
                        // Considerar como texto
                        tokenTxt = palabra;
                        tipo = "Texto";
                        expresionRegular = "Cualquier conjunto de caracteres";
                    }
                    break;
            }
            // Asignar propiedades al token
            tokenNuevo.setToken(tokenTxt);
            tokenNuevo.setTipo(tipo);
            tokenNuevo.setLenguaje(lenguaje);
            tokenNuevo.setExpresionRegular(expresionRegular);
            // Verificar si se debe agregar el token a una lista o realizar alguna acción
            if (agregar) {
                token.getListaDeTokens().add(tokenNuevo);
            }
        }
    }
}
