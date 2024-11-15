/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Gandr
 */
public class Pdf {

    public void generarPDF(String filePath, JTable table, String mean, String median, String mode,
            BufferedImage barChartImage, BufferedImage pieChartImage, BufferedImage ogiveChartImage) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Fuentes
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Título del documento
            document.add(new Paragraph("Reporte de Datos Estadísticos", titleFont));
            document.add(new Paragraph("\n"));

            // Tabla de datos
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            for (int i = 0; i < table.getColumnCount(); i++) {
                PdfPCell cell = new PdfPCell(new Paragraph(table.getColumnName(i), normalFont));
                pdfTable.addCell(cell);
            }
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Object value = table.getValueAt(row, col);
                    pdfTable.addCell(new Paragraph(value != null ? value.toString() : "", normalFont));
                }
            }
            document.add(pdfTable);

            // Estadísticas
            document.add(new Paragraph("Estadísticas Calculadas:", titleFont));
            document.add(new Paragraph("Media: " + mean, normalFont));
            document.add(new Paragraph("Mediana: " + median, normalFont));
            document.add(new Paragraph("Moda: " + mode, normalFont));

            // Agregar gráficos al PDF
            addChartToPDF(document, barChartImage, "Gráfico de Columnas");
            addChartToPDF(document, pieChartImage, "Gráfico de Pastel");
            addChartToPDF(document, ogiveChartImage, "Ojiva");

            document.close();
            JOptionPane.showMessageDialog(null, "PDF generado exitosamente en " + filePath);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al generar el PDF: " + e.getMessage());
        }
    }

    private void addChartToPDF(Document document, BufferedImage chartImage, String title) throws DocumentException, IOException {
        document.add(new Paragraph("\n" + title, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(chartImage, "png", baos);
        Image pdfImage = Image.getInstance(baos.toByteArray());
        pdfImage.scaleToFit(500, 400);
        document.add(pdfImage);
    }

}
