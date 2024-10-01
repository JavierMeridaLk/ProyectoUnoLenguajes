package backen;

import java.util.ArrayList;
import java.util.List;

public class Token {

    private String token;// texto ingresado
    private String tipo;//tipo de token
    private String fila;
    private String columna;
    private String lenguaje;
    private String expresionRegular;//lo que acepta
    
    private List<Token> Token = new ArrayList<>();
    private List<Token> tokenOptimizacion = new ArrayList<>();

    //Entrada    
    private final String[] ENTRADA_HTML = {
        "principal", "encabezado", "navegacion", "apartado", "listaordenada",
        "listadesordenada", "itemlista", "anclaje", "contenedor", "seccion",
        "articulo", "titulo", "parrafo", "span", "entrada", "formulario",
        "label", "area", "boton", "piepagina"
    };

    //Traduccion
    private final String[] TRADUCCION_HTML = {
        "main", "header", "nav", "aside", "ul", "ol", "li", "a", "div",
        "section", "article", "h#", "p", "span", "input", "form",
        "label", "textarea", "button", "footer"
    };

    //Tipo
    private final String[] TIPO_DE_TOKEN_HTML = {
        "Apertura", "Cierre", "Una línea",};

    public Token() {

    }
    

    public void crearTokenHTML(Token token) {
        
        
            
        

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getColumna() {
        return columna;
    }

    public void setColumna(String columna) {
        this.columna = columna;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public String getExpresionRegular() {
        return expresionRegular;
    }

    public void setExpresionRegular(String expresionRegular) {
        this.expresionRegular = expresionRegular;
    }

    public List<Token> getTokenOptimizacion() {
        return tokenOptimizacion;
    }

    public void setTokenOptimizacion(List<Token> tokenOptimizacion) {
        this.tokenOptimizacion = tokenOptimizacion;
    }

}
