package Controller;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        "ACUM absoluta(+)", "ACUM Relativa(+)",
        "ACUM absoluta(-)", "ACUM Relativa(-)"};

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
        model.addRow(new Object[]{null, null, null, null}); //Add a row to add a Total row
        double lowerLimit=0;
        double upperLimit=0;

        for (int i = 0; i < classes; i++) {
           lowerLimit = min + (i * interval);
           upperLimit = min + ((i + 1) * interval);
            double adjustedUpper = upperLimit - 1;
            if (i==classes-1){
            model.setValueAt(String.format("%.2f-%.2f", lowerLimit, max), classes-1, 0);
            calculateSecondColumn(table, max, lowerLimit, i);
            calculateFourthColumn(max, lowerLimit, i);
            return;
            }
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

        for (Double value : dataList) {
            if (value >= lower && value <= upper) {
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
        for (int i = 0; i < table.getRowCount()-1; i++) {
            int absFreq = (int) table.getValueAt(i, 3);
            double relativeFreq = (double) absFreq / dataList.size() * 100;;
            model.setValueAt(relativeFreq, i, 4);
        }
    }

// Calculate Cumulative Absolute and Relative Frequency (+)
    public void calculateSixthColumn(JTable table) {
        int cumulativeAbs = 0;
        double cumulativeRel = 0.0;
        for (int i = 0; i < table.getRowCount()-1; i++) {
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

// Calculate Cumulative Absolute and Relative Frequency (-)
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
    
    //Method to add totals at the end of the table
    private void getTotal() {
        double totalFi = 0;
        double totalRe = 0;

        for (int i = 0; i < model.getRowCount() - 1; i++) {
            totalFi += Double.parseDouble(model.getValueAt(i, 3).toString());
            totalRe += Double.parseDouble(model.getValueAt(i, 4).toString());
        }
        model.setValueAt(totalFi, model.getRowCount()-1, 3);
        model.setValueAt(totalRe, model.getRowCount()-1, 4);
    }

    // ------------------ AUXILIARY METHODS --------------------------------
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

    // Add a numbers to the list and update the text area
    public void addNumbers(JTextField txtClasses, JTextArea txtAreaData, JTable tblData, JTextField txtMe, JTextField txtX, JTextField txtMo) {
        String texto = txtAreaData.getText();
        String[] valores = texto.split(",");

        if (valores.length > 10 && this.validateNumber(txtClasses.getText())) {//to validate the amount of numbers
            for (String valor : valores) {
                try {
                    this.dataList.add(Double.parseDouble(valor.trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Valor no válido debe ingresar solo números: " + valor);
                }
            }
            this.processAndCalculate(txtAreaData, tblData, txtMe, txtX, txtMo, txtClasses);
        }
        JOptionPane.showMessageDialog(null, "La lista debe ser mayor a 10 y solo numeros decimales (.) y enteros\nLas clases no deben ir vacias, solo entero mayor a 1",
                "ERROR", JOptionPane.ERROR_MESSAGE);
        txtClasses.setText("");
        txtAreaData.setText("");
        return;
    }

// Método para calcular la Media Aritmética
    public double calculateMean(JTable table) {
        double sumOfProducts = 0;
        int totalFrequency = 0;

        for (int i = 0; i < table.getRowCount() - 1; i++) {
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

        for (int i = 0; i < table.getRowCount()-1; i++) {
            Integer frequency = (Integer) table.getValueAt(i, 3);
            if (frequency != null) {
                totalFrequency += frequency;
            }
        }

        double halfTotalFrequency = totalFrequency / 2.0;
        int cumulativeFrequency = 0;
        double median = 0;

        for (int i = 0; i < table.getRowCount() - 1; i++) {
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

        for (int i = 0; i < table.getRowCount() - 1; i++) {
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

    public void processAndCalculate(JTextArea txtAreaNum, JTable tblData, JTextField txtMe, JTextField txtX, JTextField txtMo, JTextField txtClasses) {
        // Obtener los datos del JTextArea
        String[] dataEntries = txtAreaNum.getText().split(" ");

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
        calculateFirstColumn(tblData, Integer.parseInt(txtClasses.getText()));
        calculateFifthColumn(tblData);  // Frecuencia relativa
        calculateSixthColumn(tblData);  // Acumulada (-)
        calculateSeventhColumn(tblData);  // Acumulada (+)

        //TO ADD TOTAL VIEW IN THE TABLE
        model.setValueAt("TOTAL", model.getRowCount() - 1, 2);
        this.getTotal(); //method to calcule the totals

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
        for (int i = 0; i < table.getRowCount() - 1; i++) {
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
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    //METODO PARA VALIDAR EL TXTCLASSES
    public boolean validateNumber(String valor) {
        Pattern patron = Pattern.compile("[1-9]*");
        Matcher m = patron.matcher(valor);
        return m.matches();
    }
}
