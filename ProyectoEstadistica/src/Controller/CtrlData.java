package Controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
        double lowerLimit = 0;
        double upperLimit = 0;

        for (int i = 0; i < classes; i++) {
            lowerLimit = min + (i * interval);
            upperLimit = min + ((i + 1) * interval);
            double adjustedUpper = upperLimit - 1;
            if (i == classes - 1) {
                model.setValueAt(String.format("%.2f-%.2f", lowerLimit, max), classes - 1, 0);
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
        for (int i = 0; i < table.getRowCount() - 1; i++) {
            int absFreq = (int) table.getValueAt(i, 3);
            double relativeFreq = (double) absFreq / dataList.size() * 100;;
            model.setValueAt(relativeFreq, i, 4);
        }
    }

// Calculate Cumulative Absolute and Relative Frequency (+)
    public void calculateSixthColumn(JTable table) {
        int cumulativeAbs = 0;
        double cumulativeRel = 0.0;
        for (int i = 0; i < table.getRowCount() - 1; i++) {
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
        model.setValueAt(totalFi, model.getRowCount() - 1, 3);
        model.setValueAt(totalRe, model.getRowCount() - 1, 4);
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

        if (valores.length >= 10 && this.validateNumber(txtClasses.getText())) {
            for (String valor : valores) {
                try {
                    this.dataList.add(Double.parseDouble(valor.trim()));
                } catch (NumberFormatException e) {
                    System.out.println("Valor no válido. Debe ingresar solo números: " + valor);
                }
            }
            this.processAndCalculate(txtAreaData, tblData, txtMe, txtX, txtMo, txtClasses);
        } else {
            JOptionPane.showMessageDialog(null, "La lista debe ser mayor o igual a 10 y contener solo números decimales o enteros. Además, el número de clases debe ser un entero mayor a 1.",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
            txtClasses.setText("");
            txtAreaData.setText("");
        }
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

        for (int i = 0; i < table.getRowCount() - 1; i++) {
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
public double calculateModeFromText(String textData, JTextField txtMo) {
    // Lista para almacenar las frecuencias de cada número
    List<Double> dataList = new ArrayList<>();
    Map<Double, Integer> frequencyMap = new HashMap<>();

    // Dividir la cadena por comas o espacios
    String[] dataEntries = textData.split("[,\\s]+");

    for (String entry : dataEntries) {
        entry = entry.trim();
        if (!entry.isEmpty()) {
            try {
                double value = Double.parseDouble(entry.replace(",", "."));
                dataList.add(value);
                frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Dato inválido: " + entry);
            }
        }
    }

    // Calcular la moda
    double mode = 0;
    int maxFrequency = 0;
    int modeCount = 0;
    List<Double> modes = new ArrayList<>();

    for (Map.Entry<Double, Integer> entry : frequencyMap.entrySet()) {
        int frequency = entry.getValue();
        if (frequency > maxFrequency) {
            maxFrequency = frequency;
            mode = entry.getKey();
            modes.clear();
            modes.add(mode);
            modeCount = 1;
        } else if (frequency == maxFrequency) {
            modes.add(entry.getKey());
            modeCount++;
        }
    }

    // Verificar si es bimodal o no hay moda
    if (modeCount == 1) {
        txtMo.setText(String.format("%.2f", mode)); // Mostrar la moda única en txtMo
        return mode; // Retorna la moda si es única
    } else if (modeCount == 2) {
        JOptionPane.showMessageDialog(null, "La distribución es bimodal: " + modes.get(0) + " y " + modes.get(1));
        txtMo.setText("Bimodal"); // Mostrar "Bimodal" en txtMo
    } else {
        JOptionPane.showMessageDialog(null, "No hay moda.");
        txtMo.setText("No hay moda"); // Mostrar "No hay moda" en txtMo
    }
    
    return 0;  // Si no hay una moda clara, retorna 0 o el valor que prefieras
}



  public void processAndCalculate(JTextArea txtAreaNum, JTable tblData, JTextField txtMe, JTextField txtX, JTextField txtMo, JTextField txtClasses) {
    // Limpiar la lista de datos
    dataList.clear();

    // Obtener los datos del JTextArea y procesarlos
    String[] dataEntries = txtAreaNum.getText().split("[,\\s]+");  // Dividir por comas o espacios

    for (String entry : dataEntries) {
        entry = entry.trim();  // Eliminar espacios adicionales
        if (!entry.isEmpty()) {  // Evitar entradas vacías
            try {
                // Reemplazar comas por puntos antes de convertir
                entry = entry.replace(",", ".");
                dataList.add(Double.parseDouble(entry));  // Convertir a double y añadir a la lista
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

    // Agregar "TOTAL" en la tabla
    model.setValueAt("TOTAL", model.getRowCount() - 1, 2);
    this.getTotal(); // Método para calcular los totales

    // Calcular y mostrar la media
    double mean = calculateMean(tblData);
    txtMe.setText(String.format("%.2f", mean));

    // Calcular y mostrar la mediana
    double median = calculateMedian(tblData);
    txtX.setText(String.format("%.2f", median));

    // Calcular y mostrar la moda a partir de los datos en txtAreaNum
    double mode = calculateModeFromText(txtAreaNum.getText(), txtMo);
    if (mode != 0) {
        txtMo.setText(String.format("%.2f", mode));
    }
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
        Pattern patron = Pattern.compile("^[1-9][0-9]*$");  // Asegura que el valor sea mayor a 0
        Matcher m = patron.matcher(valor);
        return m.matches();
    }

        // Método que recibe la tabla, obtiene los datos y muestra los resultados
public void mostrarResultadosDesdeTabla(JTable tabla) {
    // Obtener los valores de la tabla
    String media = obtenerValorDeTabla(tabla, "Limites Indicados");
    String mediana = obtenerValorDeTabla(tabla, "Limites Reales");
    String moda = obtenerValorDeTabla(tabla, "Puntos Medios Xi");
    String frecuenciaAbsoluta = obtenerValorDeTabla(tabla, "Frecuencia absoluta");
    String frecuenciaRelativa = obtenerValorDeTabla(tabla, "Frecuencia Relativa");
    String acumAbsolutaPos = obtenerValorDeTabla(tabla, "ACUM absoluta(+)");
    String acumRelativaPos = obtenerValorDeTabla(tabla, "ACUM Relativa(+)");
    String acumAbsolutaNeg = obtenerValorDeTabla(tabla, "ACUM absoluta(-)");
    String acumRelativaNeg = obtenerValorDeTabla(tabla, "ACUM Relativa(-)");

    // Crear el array con los cálculos
    String[] calculos = {
        "Media Aritmética: " + media,
        "Mediana: " + mediana,
        "Moda: " + moda,
        "Frecuencia Absoluta: " + frecuenciaAbsoluta,
        "Frecuencia Relativa: " + frecuenciaRelativa,
        "ACUM Absoluta (+): " + acumAbsolutaPos,
        "ACUM Relativa (+): " + acumRelativaPos,
        "ACUM Absoluta (-): " + acumAbsolutaNeg,
        "ACUM Relativa (-): " + acumRelativaNeg
    };

    // Crear el JList con los cálculos
    JList<String> listaCalculos = new JList<>(calculos);
    JScrollPane scrollPane = new JScrollPane(listaCalculos);
    scrollPane.setPreferredSize(new Dimension(250, 100));

    // Crear el panel adicional con datos detallados
    JPanel panelDetalles = new JPanel();
    panelDetalles.setLayout(new BorderLayout());
    panelDetalles.add(new JLabel("Datos Detallados"), BorderLayout.NORTH);
    JTextArea txtAreaDetalles = new JTextArea(10, 30);
    txtAreaDetalles.setText("Aquí se mostrarán los datos detallados de cada cálculo...");
    txtAreaDetalles.setEditable(false);
    panelDetalles.add(new JScrollPane(txtAreaDetalles), BorderLayout.CENTER);

    // Botón para mostrar el panel adicional
    JButton btnDetalles = new JButton("Ver Detalles");
    btnDetalles.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, panelDetalles, "Detalles de Cálculos", JOptionPane.INFORMATION_MESSAGE);
        }
    });

    // Crear el panel del resumen que incluye la lista y el botón
    JPanel panelResumen = new JPanel(new BorderLayout());
    panelResumen.add(scrollPane, BorderLayout.CENTER);
    panelResumen.add(btnDetalles, BorderLayout.SOUTH);

    // Mostrar el JOptionPane con el resumen
    JOptionPane.showMessageDialog(null, panelResumen, "Resumen de Resultados", JOptionPane.INFORMATION_MESSAGE);
}


private String obtenerValorDeTabla(JTable tabla, String estadistica) {
    // Obtener el modelo de la tabla
    TableModel model = tabla.getModel();
    
    // Lista de las estadísticas esperadas (basado en los campos que diste)
    String[] estadisticasEsperadas = {
        "Limites Indicados", "Limites Reales", "Puntos Medios Xi",
        "Frecuencia absoluta", "Frecuencia Relativa",
        "ACUM absoluta(+)", "ACUM Relativa(+)",
        "ACUM absoluta(-)", "ACUM Relativa(-)"
    };

    // Iterar sobre las filas de la tabla
    for (int i = 0; i < model.getRowCount(); i++) {
        // Obtener el valor de la primera columna (nombre de la estadística)
        Object nombreEstadisticaObj = model.getValueAt(i, 0);  // Primera columna: Nombre de la estadística
        
        // Verificar si el valor no es null y si el nombre de la estadística coincide
        if (nombreEstadisticaObj != null) {
            String nombreEstadistica = nombreEstadisticaObj.toString();
            for (String estadisticaEsperada : estadisticasEsperadas) {
                if (nombreEstadistica.equals(estadisticaEsperada)) {
                    // Si el nombre coincide, devolver el valor de la segunda columna (valor)
                    Object valorObj = model.getValueAt(i, 1);  // Segunda columna: Valor
                    return valorObj != null ? valorObj.toString() : "Valor no disponible";
                }
            }
        }
    }
    
    // Si no se encuentra la estadística en la tabla, devolver "No encontrado"
    return "No encontrado";
}


    // Método para mostrar los resultados en un JOptionPane
    private void mostrarResultados(String media, String mediana, String moda, String frecuenciaAcumuladaAbsoluta, String frecuenciaAcumuladaRelativa) {
        // Los cálculos obtenidos
        String[] calculos = {
            "Media Aritmética: " + media,
            "Mediana: " + mediana,
            "Moda: " + moda,
            "Frecuencia Acumulada Absoluta: " + frecuenciaAcumuladaAbsoluta,
            "Frecuencia Acumulada Relativa: " + frecuenciaAcumuladaRelativa
        };

        // Crear el JList con los cálculos
        JList<String> listaCalculos = new JList<>(calculos);
        JScrollPane scrollPane = new JScrollPane(listaCalculos);
        scrollPane.setPreferredSize(new Dimension(250, 100));

        // Crear el panel adicional con datos detallados
        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new BorderLayout());
        panelDetalles.add(new JLabel("Datos Detallados"), BorderLayout.NORTH);
        JTextArea txtAreaDetalles = new JTextArea(10, 30);
        txtAreaDetalles.setText("Aquí se mostrarán los datos detallados de cada cálculo...");
        txtAreaDetalles.setEditable(false);
        panelDetalles.add(new JScrollPane(txtAreaDetalles), BorderLayout.CENTER);

        // Botón para mostrar el panel adicional
        JButton btnDetalles = new JButton("Ver Detalles");
        btnDetalles.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, panelDetalles, "Detalles de Cálculos", JOptionPane.INFORMATION_MESSAGE);
        });

        // Crear el panel del resumen que incluye la lista y el botón
        JPanel panelResumen = new JPanel(new BorderLayout());
        panelResumen.add(scrollPane, BorderLayout.CENTER);
        panelResumen.add(btnDetalles, BorderLayout.SOUTH);

        // Mostrar el JOptionPane con el resumen
        JOptionPane.showMessageDialog(null, panelResumen, "Resumen de Resultados", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
