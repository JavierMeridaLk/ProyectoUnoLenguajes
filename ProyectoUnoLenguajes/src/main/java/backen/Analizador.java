package backen;

import fronted.reportesError;
import fronted.reportesToken;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Analizador {

    private List<String> lineasHTML = new ArrayList<>();  // Para almacenar las líneas de HTML
    private List<String> lineasCSS = new ArrayList<>();   // Para almacenar las líneas de CSS
    private List<String> lineasJS = new ArrayList<>();    // Para almacenar las líneas de JS

    private List<String> codigoHTML = new ArrayList<>();  // Para almacenar las líneas de HTML
    private List<String> codigoCSS = new ArrayList<>();   // Para almacenar las líneas de CSS
    private List<String> codigoJS = new ArrayList<>();    // Para almacenar las líneas de JS

    private List<String> codigoOptimizado = new ArrayList<>();
    private List<Token> tokenOptimizacion = new ArrayList<>();
    
    private reportesToken reporteToken;
    private reportesError reportesError;

    public Analizador(reportesToken reporteToken, reportesError reportesError) {
    this.reporteToken=reporteToken;
    this.reportesError=reportesError;
    
    }

    public void exportarHtml(String texto) {
        Token token = new Token();
        Reportes reporte = new Reportes();
        String[] lineas = separarPorLineas(texto);
        separarTipoDeCodigo(lineas, token);
        crearHtml();
        reporte.setToken(token);
        reporteToken.subirTabla(reporte.reporteToken());
        reportesError.subirTabla(reporte.reporteError());
        
        

    }

    public void crearHtml() {
        File carpeta = new File("Archivos HTML");

        // Crear la carpeta si no existe
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        try {
            String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre del Archivo HTML", "Nombre", JOptionPane.QUESTION_MESSAGE);
            File archivo = new File(carpeta, nombre + ".html");

            
            String css = "";
            String js = "";
            String htmlBody="";
            
            for (String linea : lineasCSS) {
                css= css+linea+"\n";
            }
            for (String linea : lineasJS) {
                js= js+linea+"\n";
            }
            for (String linea : codigoHTML) {
                htmlBody= htmlBody+linea+"\n";
            }
            
            // Crear el código HTML final
            String codigoHtmlFinal =""
                    + "<!DOCTYPE html>"+"\n"
                    +"<html lang=\"es\">" +"\n"
                    +"  <head>" +"\n"
                    +"    <meta charset=\"UTF-8\">" +"\n"
                    +"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +"\n"
                    +"    <title> "+ nombre +" </title>" +"\n"
                    +"    <style>" +"\n"
                    +"      "+css+""+"\n"
                    +"    </style>" +"\n"
                    +"    <script>" +"\n"
                    +"      "+js+"" +"\n"
                    +"    </script>" +"\n"
                    +"  </head>" +"\n"
                    +"  <body>" +"\n"
                    +"    "+htmlBody+"" +"\n"
                    +"  </body>" +"\n"
                    +"</html>"  +"\n"      ;
            // Guardar el archivo
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(codigoHtmlFinal);
            }

            JOptionPane.showMessageDialog(null, "Archivo HTML guardado en la carpeta: " + carpeta.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al guardar el archivo HTML", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

    public void separarTipoDeCodigo(String[] lineas, Token token) {
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
        AnalizadorCss analizadorCss = new AnalizadorCss();
        analizadorCss.analizarCss(lineasCSS, token);

        AnalizadorJs analizadorJs = new AnalizadorJs();
        analizadorJs.analizarJs(lineasJS, token);

        AnalizadorHtml analizadorHtml = new AnalizadorHtml();
        codigoHTML = analizadorHtml.analizarHtml(token, lineasHTML);
    }
}
