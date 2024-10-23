package Controller;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Collections;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.JFrame;

public class CtrlData {

    String[] columns = {
        "Limites Indicados", "Limites Reales", "Puntos Medios Xi",
        "Frecuencia absoluta", "Frecuencia Relativa",
        "ACUM absoluta(-)", "ACUM Relativa(-)",
        "ACUM absoluta(+)", "ACUM Relativa(+)"};

    public List<Double> dataList = new ArrayList<>();
    DefaultTableModel model = new DefaultTableModel();

    public CtrlData() {
    }

    // ------------------ CALCULATION METHODS ------------------------------
    public void calculateFirstColumn(JTable table, int classes) {
        if (dataList.isEmpty()) {
            JOptionPane.showMessageDialog(null, "The list is empty. Please add data before proceeding.");
            return;
        }

        Collections.sort(dataList);  // Sort the list
        double min = dataList.get(0);  // Get minimum value
        double max = dataList.get(dataList.size() - 1);  // Get maximum value
        double range = max - min;
        double interval = range / classes;

        model.setRowCount(classes);  // Set the number of rows based on classes

        for (int i = 0; i < classes; i++) {
            double lowerLimit = min + (i * interval);
            double upperLimit = min + ((i + 1) * interval);
            double adjustedUpper = upperLimit - 1;

            model.setValueAt(String.format("%.2f-%.2f", lowerLimit, adjustedUpper), i, 0);
            calculateSecondColumn(table, adjustedUpper, lowerLimit, i);
            calculateFourthColumn(adjustedUpper, lowerLimit, i);
        }
        table.setModel(model);
    }

    // Calculate Real Limits
    public void calculateSecondColumn(JTable table, double upper, double lower, int i) {
        double realLowerLimit = lower - 0.5;
        double realUpperLimit = upper + 0.5;
        model.setValueAt(String.format("%.2f-%.2f", realLowerLimit, realUpperLimit), i, 1);
        calculateThirdColumn(realUpperLimit, realLowerLimit, i);
    }

    // Calculate Midpoint
    public void calculateThirdColumn(double upper, double lower, int i) {
        double midpoint = (lower + upper) / 2;
        model.setValueAt(midpoint, i, 2);
    }

    // Calculate Absolute Frequency
    public void calculateFourthColumn(double upper, double lower, int i) {
        int frequency = 0;
        int lowerInt = (int) Math.floor(lower);
        int upperInt = (int) Math.floor(upper);

        for (Double value : dataList) {
            int intValue = value.intValue();
            if (intValue >= lowerInt && intValue <= upperInt) {
                frequency++;
            }
        }
        model.setValueAt(frequency, i, 3);
    }

    // Calculate Relative Frequency
    public void calculateFifthColumn(JTable table) {
        if (dataList.isEmpty()) {
            return;  // Prevent division by zero
        }
        for (int i = 0; i < table.getRowCount(); i++) {
            int absFreq = (int) table.getValueAt(i, 3);
            double relativeFreq = (double) absFreq / dataList.size() * 100;
            model.setValueAt(relativeFreq, i, 4);
        }
    }

// Calculate Cumulative Absolute and Relative Frequency (-)
    public void calculateSixthColumn(JTable table) {
        int cumulativeAbs = 0;
        double cumulativeRel = 0.0;

        for (int i = 0; i < table.getRowCount(); i++) {
            // Asegurar que no sean nulos
            Integer absFreq = (Integer) table.getValueAt(i, 3);  // Frecuencia absoluta
            if (absFreq == null) {
                absFreq = 0;
            }

            Double relFreq = (Double) table.getValueAt(i, 4);  // Frecuencia relativa
            if (relFreq == null) {
                relFreq = 0.0;
            }

            // Continuar los cálculos
            cumulativeAbs += absFreq;
            cumulativeRel += relFreq;

            model.setValueAt(cumulativeAbs, i, 5); // Columna 5: Acumulada absoluta (-)
            model.setValueAt(cumulativeRel, i, 6); // Columna 6: Acumulada relativa (-)
        }
    }

// Calculate Cumulative Absolute and Relative Frequency (+)
    public void calculateSeventhColumn(JTable table) {
        int cumulativeAbs = 0;
        double cumulativeRel = 0.0;

        for (int i = table.getRowCount() - 1; i >= 0; i--) {
            // Asegurar que no sean nulos
            Integer absFreq = (Integer) table.getValueAt(i, 3);
            if (absFreq == null) {
                absFreq = 0;
            }

            Double relFreq = (Double) table.getValueAt(i, 4);
            if (relFreq == null) {
                relFreq = 0.0;
            }

            // Continuar los cálculos
            cumulativeAbs += absFreq;
            cumulativeRel += relFreq;

            model.setValueAt(cumulativeAbs, i, 7); // Columna 7: Acumulada absoluta (+)
            model.setValueAt(cumulativeRel, i, 8); // Columna 8: Acumulada relativa (+)
        }
    }

    // ------------------ AUXILIARY METHODS --------------------------------
    // Add data to the list
    public void addData(JTextField txtData) {
        try {
            String input = txtData.getText().trim().replace(",", ".");  // Reemplaza coma por punto
            double value = Double.parseDouble(input);  // Convierte a double
            dataList.add(value);  // Añade a la lista
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number.");
        }
    }

    // Load table columns
    public void loadTableData(JTable table) {
        model.setRowCount(0);
        model.setColumnCount(0); // Limpia las columnas antes de añadirlas
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        for (String column : columns) {
            model.addColumn(column);
        }
        table.setModel(model);
    }

    // Remove the last data entry
    public void removeLastData(JTextArea txtAreaData) {
        if (!dataList.isEmpty()) {
            dataList.remove(dataList.size() - 1);
            updateTextArea(txtAreaData);
        } else {
            JOptionPane.showMessageDialog(null, "The list is empty. No data to remove.");
        }
    }

    // Update the text area with the current list
    private void updateTextArea(JTextArea txtAreaData) {
        StringBuilder content = new StringBuilder();
        for (Double number : dataList) {
            content.append(number).append(" ");
        }
        txtAreaData.setText(content.toString());
    }

    // Remove specific number from the list
    public void removeSpecificData(JTextArea txtAreaData) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter the number to remove:"));
        JTextField txtNumber = new JTextField(10);
        panel.add(txtNumber);

        int result = JOptionPane.showConfirmDialog(null, panel, "Remove Number", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double number = Double.parseDouble(txtNumber.getText());
                if (dataList.contains(number)) {
                    dataList.removeIf(n -> n.equals(number));
                    updateTextArea(txtAreaData);
                    JOptionPane.showMessageDialog(null, "All occurrences of the number " + number + " have been removed.");
                } else {
                    JOptionPane.showMessageDialog(null, "Number not found in the list.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.");
            }
        }
    }

    // Add a number multiple times to the list and update the text area
    public void addNumbersMultipleTimes(JTextField txtNumber, JTextField txtTimes, JTextArea txtAreaData) {
        try {
            double number = Double.parseDouble(txtNumber.getText());
            int times = Integer.parseInt(txtTimes.getText());

            for (int i = 0; i < times; i++) {
                dataList.add(number);
            }
            updateTextArea(txtAreaData);
            txtNumber.setText("");
            txtTimes.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers.");
        }
    }
// Método para calcular la Media Aritmética

    public double calculateMean(JTable table) {
        double sumOfProducts = 0;
        int totalFrequency = 0;

        for (int i = 0; i < table.getRowCount(); i++) {
            Double midpoint = (Double) table.getValueAt(i, 2);  // Puntos Medios Xi
            Integer frequency = (Integer) table.getValueAt(i, 3);  // Frecuencia Absoluta

            if (midpoint != null && frequency != null) {
                sumOfProducts += midpoint * frequency;
                totalFrequency += frequency;
            }
        }

        // Devuelve la media aritmética
        return totalFrequency != 0 ? sumOfProducts / totalFrequency : 0;
    }

// Método para calcular la Mediana
    public double calculateMedian(JTable table) {
        int totalFrequency = 0;

        for (int i = 0; i < table.getRowCount(); i++) {
            Integer frequency = (Integer) table.getValueAt(i, 3);
            if (frequency != null) {
                totalFrequency += frequency;
            }
        }

        double halfTotalFrequency = totalFrequency / 2.0;
        int cumulativeFrequency = 0;
        double median = 0;

        for (int i = 0; i < table.getRowCount(); i++) {
            Integer frequency = (Integer) table.getValueAt(i, 3);
            if (frequency != null) {
                cumulativeFrequency += frequency;

                if (cumulativeFrequency >= halfTotalFrequency) {
                    // Asegurar el formato correcto de los límites
                    Double lowerLimit = Double.valueOf(table.getValueAt(i, 0).toString().split("-")[0].replace(",", "."));
                    Double intervalWidth = Double.valueOf(table.getValueAt(i, 1).toString().split("-")[1].replace(",", ".")) - lowerLimit;
                    int previousCumulative = cumulativeFrequency - frequency;

                    // Fórmula para la mediana
                    median = lowerLimit + (((halfTotalFrequency - previousCumulative) / frequency) * intervalWidth);
                    break;
                }
            }
        }
        return median;
    }

// Método para calcular la Moda
    public double calculateMode(JTable table) {
        int maxFrequency = 0;
        double mode = 0;

        for (int i = 0; i < table.getRowCount(); i++) {
            Integer frequency = (Integer) table.getValueAt(i, 3);
            if (frequency != null && frequency > maxFrequency) {
                maxFrequency = frequency;

                // Asegurar que el formato sea el correcto
                String lowerLimitStr = table.getValueAt(i, 0).toString().split("-")[0].replace(",", ".");
                String upperLimitStr = table.getValueAt(i, 1).toString().split("-")[1].replace(",", ".");

                Double lowerLimit = Double.valueOf(lowerLimitStr);
                Double upperLimit = Double.valueOf(upperLimitStr);
                Double intervalWidth = upperLimit - lowerLimit;

                Integer prevFrequency = i > 0 ? (Integer) table.getValueAt(i - 1, 3) : 0;
                Integer nextFrequency = i < table.getRowCount() - 1 ? (Integer) table.getValueAt(i + 1, 3) : 0;

                // Fórmula para la moda
                mode = lowerLimit + ((frequency - prevFrequency) / ((frequency - prevFrequency) + (frequency - nextFrequency))) * intervalWidth;
            }
        }
        return mode;
    }

    public void processAndCalculate(JTextArea txtAreaNum, JTable tblData, JTextField txtMe, JTextField txtX, JTextField txtMo) {
        // Obtener los datos del JTextArea
        String[] dataEntries = txtAreaNum.getText().split(" ");

        // Limpiar la lista existente y añadir los nuevos datos
        dataList.clear();
        for (String entry : dataEntries) {
            entry = entry.trim();  // Eliminar espacios
            if (!entry.isEmpty()) {  // Evitar entradas vacías
                try {
                    // Reemplazar comas por puntos antes de convertir
                    entry = entry.replace(",", ".");
                    dataList.add(Double.parseDouble(entry));  // Convertir a double y añadir
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Dato inválido: " + entry);
                }
            }
        }

        // Cargar las columnas de la tabla y realizar los cálculos
        loadTableData(tblData);
        calculateFirstColumn(tblData, 5);  // Ejemplo: 5 clases
        calculateFifthColumn(tblData);  // Frecuencia relativa
        calculateSixthColumn(tblData);  // Acumulada (-)
        calculateSeventhColumn(tblData);  // Acumulada (+)

        // Calcular y mostrar la media
        double mean = calculateMean(tblData);
        txtMe.setText(String.format("%.2f", mean));

        // Calcular y mostrar la mediana
        double median = calculateMedian(tblData);
        txtX.setText(String.format("%.2f", median));

        // Calcular y mostrar la moda
        double mode = calculateMode(tblData);
        txtMo.setText(String.format("%.2f", mode));
    }
// Método para generar una gráfica de columnas (Bar Chart)

    public void generateBarChart(JTable table) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Leer los datos desde la tabla para crear el dataset
        for (int i = 0; i < table.getRowCount(); i++) {
            String interval = (String) table.getValueAt(i, 0);  // Limites Indicados
            Integer frequency = (Integer) table.getValueAt(i, 3);  // Frecuencia Absoluta
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
        for (int i = 0; i < table.getRowCount(); i++) {
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
        for (int i = 0; i < table.getRowCount(); i++) {
            String interval = (String) table.getValueAt(i, 0);  // Limites Indicados
            Integer cumulativeAbs = (Integer) table.getValueAt(i, 5);  // Acumulada Absoluta (-)
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
        frame.setVisible(true);
    }
}
