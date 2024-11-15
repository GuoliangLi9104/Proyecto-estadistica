/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JTable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Gandr
 */
public class Graphics {

    // Método para generar una gráfica de columnas (Bar Chart)
    public void generateBarChart(JTable table) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Leer los datos desde la tabla para crear el dataset
        for (int i = 0; i < table.getRowCount() - 1; i++) {
            String interval = (String) table.getValueAt(i, 0);  // Limites Indicados
            Number frequencyValue = (Number) table.getValueAt(i, 3);  // Frecuencia Absoluta
            Integer frequency = frequencyValue.intValue();
            dataset.addValue(frequency, "Frecuencia", interval);
        }

        // Crear la gráfica
        JFreeChart chart = ChartFactory.createBarChart(
                "Gráfico de Columnas", // Título
                "Intervalo", // Eje X
                "Frecuencia", // Eje Y
                dataset
        );

        // Mostrar la gráfica en un JFrame
        showChart(chart, "Gráfico de Columnas");
    }

    // Método para generar una gráfica de pastel (Pie Chart)
    public void generatePieChart(JTable table) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        // Leer los datos desde la tabla para crear el dataset
        for (int i = 0; i < table.getRowCount() - 1; i++) {
            String interval = (String) table.getValueAt(i, 0);  // Limites Indicados
            Double relativeFrequency = (Double) table.getValueAt(i, 4);  // Frecuencia Relativa
            dataset.setValue(interval, relativeFrequency);
        }

        // Crear la gráfica
        JFreeChart chart = ChartFactory.createPieChart(
                "Gráfico de Pastel", // Título
                dataset
        );

        // Mostrar la gráfica en un JFrame
        showChart(chart, "Gráfico de Pastel");
    }

    // Método para generar una gráfica de ojiva (Line Chart para acumulada)
    public void generateOgiveChart(JTable table) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Leer los datos acumulados desde la tabla
        for (int i = 0; i < table.getRowCount() - 1; i++) {
            String interval = (String) table.getValueAt(i, 0);  // Limites Indicados
            Number cumulativeAbsValue = (Number) table.getValueAt(i, 5);  // Acumulada Absoluta
            Integer cumulativeAbs = cumulativeAbsValue.intValue();
            dataset.addValue(cumulativeAbs, "Frecuencia Acumulada", interval);
        }

        // Crear la gráfica
        JFreeChart chart = ChartFactory.createLineChart(
                "Ojiva", // Título
                "Intervalo", // Eje X
                "Frecuencia Acumulada", // Eje Y
                dataset
        );

        // Mostrar la gráfica en un JFrame
        showChart(chart, "Ojiva");
    }

    // Método auxiliar para mostrar la gráfica en un JFrame
    private void showChart(JFreeChart chart, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

   // Método para generar gráfico de barras
public BufferedImage generateBarChartImage(JTable table) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    for (int i = 0; i < table.getRowCount(); i++) {
        String interval = (String) table.getValueAt(i, 0); // Limites Indicados
        Object frequencyObj = table.getValueAt(i, 3); // Frecuencia Absoluta

        // Verificar que 'interval' y 'frequencyObj' no sean nulos
        if (interval != null && frequencyObj != null && frequencyObj instanceof Number) {
            Number frequency = (Number) frequencyObj;
            dataset.addValue(frequency, "Frecuencia", interval);
        }
    }
    JFreeChart chart = ChartFactory.createBarChart("Gráfico de Columnas", "Intervalo", "Frecuencia", dataset);
    return chart.createBufferedImage(500, 400);
}

// Método para generar gráfico de pastel
public BufferedImage generatePieChartImage(JTable table) {
    DefaultPieDataset dataset = new DefaultPieDataset();
    for (int i = 0; i < table.getRowCount(); i++) {
        String interval = (String) table.getValueAt(i, 0); // Limites Indicados
        Object frequencyObj = table.getValueAt(i, 3); // Frecuencia Absoluta

        // Verificar que 'interval' y 'frequencyObj' no sean nulos
        if (interval != null && frequencyObj != null && frequencyObj instanceof Number) {
            Number frequency = (Number) frequencyObj;
            dataset.setValue(interval, frequency);
        }
    }
    JFreeChart chart = ChartFactory.createPieChart("Gráfico de Pastel", dataset, true, true, false);
    return chart.createBufferedImage(500, 400);
}

// Método para generar gráfico de ojiva
public BufferedImage generateOgiveChartImage(JTable table) {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    double cumulativeFrequency = 0.0;

    for (int i = 0; i < table.getRowCount(); i++) {
        String interval = (String) table.getValueAt(i, 0); // Limites Indicados
        Object frequencyObj = table.getValueAt(i, 3); // Frecuencia Absoluta

        // Verificar que 'interval' y 'frequencyObj' no sean nulos
        if (interval != null && frequencyObj != null && frequencyObj instanceof Number) {
            cumulativeFrequency += ((Number) frequencyObj).doubleValue();
            dataset.addValue(cumulativeFrequency, "Frecuencia acumulada", interval);
        }
    }
    JFreeChart chart = ChartFactory.createLineChart("Gráfico de Ojiva", "Intervalo", "Frecuencia Acumulada", dataset);
    return chart.createBufferedImage(500, 400);
}

}
