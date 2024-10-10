package fronted;

import java.awt.BorderLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class reportesError extends javax.swing.JDialog {

    private JTable tabla1;

    public reportesError(java.awt.Frame parent, boolean modal) {
        super(parent, modal);  // Establecer el padre y si es modal
        initComponents();
        this.setSize(900, 700);  // Ajustar tamaño específico
        this.setLocationRelativeTo(null);  // Centrar el diálogo en pantalla
        this.setTitle("REPORTES TOKEN");  // Título de la ventana
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Ubuntu Sans", 0, 24)); // NOI18N
        jLabel1.setText("Reporte de Token");

        jButton1.setText("ExportarHTML");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 571, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(347, 347, 347)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 349, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(8, 8, 8)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Verificar si la tabla está cargada
        if (tabla1 != null) {
            exportarTablaAHTML(tabla1);
        } else {
            JOptionPane.showMessageDialog(this, "No hay tabla para exportar", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jButton1ActionPerformed
    public void exportarTablaAHTML(JTable table) {

        File carpeta = new File("Reportes HTML");

        // Crear la carpeta si no existe
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        try {
            String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre del Archivo HTML", "Nombre", JOptionPane.QUESTION_MESSAGE);
            File archivo = new File(carpeta, nombre + ".html");
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
            // Escribir la cabecera del HTML
            writer.write("<html>\n<head>\n<title>Reporte de Error</title>\n</head>\n<body>\n");
            writer.write("<h2>Reporte de Tokens</h2>\n");
            writer.write("<table border='1' cellpadding='5' cellspacing='0'>\n");

            // Escribir los títulos de las columnas
            writer.write("<tr>\n");
            TableModel model = table.getModel();
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.write("<th>" + model.getColumnName(i) + "</th>");
            }
            writer.write("</tr>\n");

            // Escribir los datos de las filas
            for (int i = 0; i < model.getRowCount(); i++) {
                writer.write("<tr>\n");
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.write("<td>" + model.getValueAt(i, j).toString() + "</td>");
                }
                writer.write("</tr>\n");
            }

            // Cerrar la tabla y el HTML
            writer.write("</table>\n</body>\n</html>");
            writer.flush();
            JOptionPane.showMessageDialog(this, "Exportación a HTML completada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al exportar la tabla a HTML", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void subirTabla(JTable tabla) {
        tabla1 = tabla;
        JScrollPane scrollPane = new JScrollPane(tabla);
        jPanel1.removeAll();
        jPanel1.setLayout(new BorderLayout());  // Asegura que el JScrollPane ocupe todo el espacio
        jPanel1.add(scrollPane, BorderLayout.CENTER);
        jPanel1.revalidate();
        jPanel1.repaint();
        this.pack();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
