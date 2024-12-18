package View;

import Controller.CtrlData;
import Controller.Graphics;
import Controller.Pdf;
import java.awt.image.BufferedImage;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author anama
 */
public class Design extends javax.swing.JFrame {

    CtrlData controller = new CtrlData();  // Controller instance
    Graphics grafic = new Graphics();
    Pdf genPdf = new Pdf();
    Info info = new Info();

    /**
     * Creates new form Design
     */
    public Design() {
        initComponents();
        btnPrint.setVisible(false); // Ocultar el botón de imprimir al inicio
        this.setLocationRelativeTo(null);  // Center the window
        this.setResizable(false);  // Disable resizing
        this.loadTable();  // Load initial table data
    }

    // Load the table with initial structure
    private void loadTable() {
        this.controller.loadTableData(this.tblData);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblGraphicTitle = new javax.swing.JLabel();
        lblEnterDataTitle = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtClasses = new javax.swing.JTextField();
        btnCalculate = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        pnlBottons = new javax.swing.JPanel();
        btnColumns = new javax.swing.JButton();
        btnOjiba = new javax.swing.JButton();
        btnPie = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtData = new javax.swing.JTextArea();
        pnlResults = new javax.swing.JPanel();
        lblMoTitle = new javax.swing.JLabel();
        txtMo = new javax.swing.JTextField();
        lblMoTitle1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblData = new javax.swing.JTable();
        txtMe = new javax.swing.JTextField();
        lblMoTitle2 = new javax.swing.JLabel();
        txtX = new javax.swing.JTextField();
        btnInfo = new javax.swing.JButton();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblGraphicTitle.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblGraphicTitle.setText("Graficadora estadística");
        jPanel1.add(lblGraphicTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 210, 54));

        lblEnterDataTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblEnterDataTitle.setText("Ingrese la lista separada por coma");
        jPanel1.add(lblEnterDataTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 250, 30));

        btnCancel.setBackground(new java.awt.Color(255, 51, 51));
        btnCancel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancel.setText("Cancelar");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jPanel1.add(btnCancel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 90, 40));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Clases");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, -1, -1));
        jPanel1.add(txtClasses, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 90, 190, 50));

        btnCalculate.setBackground(new java.awt.Color(0, 204, 0));
        btnCalculate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCalculate.setText("Calcular");
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });
        jPanel1.add(btnCalculate, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 250, 90, 40));

        btnPrint.setBackground(new java.awt.Color(0, 204, 255));
        btnPrint.setText("Imprimir Pdf");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jPanel1.add(btnPrint, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 250, 110, 40));

        pnlBottons.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Graficos"));
        pnlBottons.setOpaque(false);
        pnlBottons.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnColumns.setBackground(new java.awt.Color(153, 255, 255));
        btnColumns.setText("Colmnas");
        btnColumns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColumnsActionPerformed(evt);
            }
        });
        pnlBottons.add(btnColumns, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, 90, 40));

        btnOjiba.setBackground(new java.awt.Color(51, 255, 255));
        btnOjiba.setText("Ojiba");
        btnOjiba.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOjibaActionPerformed(evt);
            }
        });
        pnlBottons.add(btnOjiba, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 90, 40));

        btnPie.setBackground(new java.awt.Color(51, 255, 255));
        btnPie.setText("Pie");
        btnPie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPieActionPerformed(evt);
            }
        });
        pnlBottons.add(btnPie, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 90, 40));

        jPanel1.add(pnlBottons, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 20, 190, 270));

        txtData.setColumns(20);
        txtData.setRows(5);
        jScrollPane2.setViewportView(txtData);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 340, 50));

        pnlResults.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Resultados"));
        pnlResults.setOpaque(false);
        pnlResults.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblMoTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMoTitle.setText("Mo");
        pnlResults.add(lblMoTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 34, 30));

        txtMo.setEnabled(false);
        pnlResults.add(txtMo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 56, 44));

        lblMoTitle1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMoTitle1.setText("Me");
        pnlResults.add(lblMoTitle1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 34, 30));

        tblData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tblData.setEnabled(false);
        tblData.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(tblData);

        pnlResults.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 19, 970, 230));

        txtMe.setEnabled(false);
        pnlResults.add(txtMe, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 270, 56, 44));

        lblMoTitle2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMoTitle2.setText("X");
        pnlResults.add(lblMoTitle2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 280, 34, 30));

        txtX.setEnabled(false);
        pnlResults.add(txtX, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 270, 56, 44));

        btnInfo.setBackground(new java.awt.Color(51, 51, 255));
        btnInfo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnInfo.setForeground(new java.awt.Color(255, 255, 255));
        btnInfo.setText("Guia");
        btnInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoActionPerformed(evt);
            }
        });
        pnlResults.add(btnInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 290, 90, 40));

        jPanel1.add(pnlResults, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 990, 340));

        lblBackground.setForeground(new java.awt.Color(255, 255, 255));
        lblBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Diseño sin título.png"))); // NOI18N
        jPanel1.add(lblBackground, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 670));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed

        // Lógica de cálculo
        this.controller.addNumbers(txtClasses, txtData, tblData, txtMe, txtX, txtMo);

        // Mostrar el botón de imprimir
        btnPrint.setVisible(true);


    }//GEN-LAST:event_btnCalculateActionPerformed


    private void btnColumnsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColumnsActionPerformed
        grafic.generateBarChart(tblData);
    }//GEN-LAST:event_btnColumnsActionPerformed

    private void btnOjibaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOjibaActionPerformed
        grafic.generateOgiveChart(tblData);
    }//GEN-LAST:event_btnOjibaActionPerformed

    private void btnPieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPieActionPerformed
        grafic.generatePieChart(tblData);
    }//GEN-LAST:event_btnPieActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed

        // Limpiar los campos de texto
        this.txtClasses.setText("");
        this.txtData.setText("");
        this.txtMe.setText("");
        this.txtX.setText("");
        this.txtMo.setText("");

        // Recargar las columnas de la tabla
        controller.loadTableData(tblData);

        // Limpiar la lista de datos
        controller.dataList.clear();

        btnPrint.setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfoActionPerformed
        Info infoWindow = new Info(); // Crear una nueva instancia de Info cada vez que se presiona el botón

        infoWindow.setVisible(true); // Mostrar la ventana Info
    }//GEN-LAST:event_btnInfoActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Crear un selector de archivos
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar PDF");

        // Mostrar diálogo para elegir ubicación y nombre del archivo
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Asegurarse de que el archivo tenga la extensión .pdf
            if (!filePath.endsWith(".pdf")) {
                filePath += ".pdf";
            }

            // Obtener datos de la tabla y valores de estadísticas
            JTable table = tblData;
            String mean = this.txtMe.getText(); // Usa el texto de txtMe para la media
            String median = this.txtX.getText(); // Usa el texto de txtX para la mediana
            String mode = this.txtMo.getText(); // Usa el texto de txtMo para la moda

            // Generar imágenes de los gráficos
            BufferedImage barChartImage = grafic.generateBarChartImage(table);
            BufferedImage pieChartImage = grafic.generatePieChartImage(table);
            BufferedImage ogiveChartImage = grafic.generateOgiveChartImage(table);

            // Llamar al controlador para generar el PDF con gráficos
            genPdf.generarPDF(filePath, table, mean, median, mode, barChartImage, pieChartImage, ogiveChartImage);

            // Confirmación al usuario
            JOptionPane.showMessageDialog(null, "PDF generado exitosamente en " + filePath);
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalculate;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnColumns;
    private javax.swing.JButton btnInfo;
    private javax.swing.JButton btnOjiba;
    private javax.swing.JButton btnPie;
    private javax.swing.JButton btnPrint;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblEnterDataTitle;
    private javax.swing.JLabel lblGraphicTitle;
    private javax.swing.JLabel lblMoTitle;
    private javax.swing.JLabel lblMoTitle1;
    private javax.swing.JLabel lblMoTitle2;
    private javax.swing.JPanel pnlBottons;
    private javax.swing.JPanel pnlResults;
    private javax.swing.JTable tblData;
    private javax.swing.JTextField txtClasses;
    private javax.swing.JTextArea txtData;
    private javax.swing.JTextField txtMe;
    private javax.swing.JTextField txtMo;
    private javax.swing.JTextField txtX;
    // End of variables declaration//GEN-END:variables
}
