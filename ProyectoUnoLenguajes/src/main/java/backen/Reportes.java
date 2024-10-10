/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package backen;

import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author xavi
 */
public class Reportes {

    private Token token;
    private JTable table;
    private DefaultTableModel tableModel;

    public Reportes() {

    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public JTable reporteToken() {
        String[] columnNames = {"Token", "Expresión Regular", "Lenguaje", "Tipo"};

        // Crear el modelo de la tabla
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Llenar la tabla con los datos de los tokens
        for (Token token : token.getListaDeTokens()) {
            Object[] row = {
                token.getToken(), // Aquí va el valor del token
                token.getExpresionRegular(), // Aquí la expresión regular
                token.getLenguaje(), // Aquí el lenguaje
                token.getTipo() // Aquí el tipo de token
            };
            tableModel.addRow(row);
        }
        return table;
    }

    public JTable reporteError() {
        String[] columnNames = {"Token", "Expresión Regular", "Lenguaje", "Tipo"};

        // Crear el modelo de la tabla
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // Llenar la tabla con los datos de los tokens
        for (Token token : token.getTokenError()) {
            Object[] row = {
                token.getToken(), // Aquí va el valor del token
                token.getExpresionRegular(), // Aquí la expresión regular
                token.getLenguaje(), // Aquí el lenguaje
                token.getTipo() // Aquí el tipo de token
            };
            tableModel.addRow(row);
        }
        return table;
    }

}
