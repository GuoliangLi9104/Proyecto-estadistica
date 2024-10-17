package Controller;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.Collections;

/**
 *
 * @author anama
 */
public class CtrlData {

    String[] columns = {"Limites Indicados", "Limites Reales", "Puntos Medios Xi", "Frecuencia absoluta", "Frecuencia Relativa", "ACUM absoluta(-)", "ACUM Relativa(-)", "ACUM absoluta(+)", "ACUM Relativa(+)"};
    double[] data = {};
    public List<Double> list = new ArrayList<>();
    DefaultTableModel model = new DefaultTableModel();

    public CtrlData() {

    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //to calculate the Limites Indicados, the next 3 methods depends on this one
    public void getFirstColumn(JTable table, int classes) {
        Collections.sort(this.list); //to order the list
        double min = this.list.getFirst();
        double max = this.list.getLast();
        double range = max - min;
        double interval = range / classes;
        for (int i = 0; i < interval; i++) {
            double limiteInferior = min + (i * interval);
            double limiteSuperior = min + ((i + 1) * interval);
            double superior = limiteSuperior - 1;
            model.setValueAt(new String[]{String.format("%.2f-%.2f", limiteInferior, superior)}, i, 0);
            this.getSecondColumm(table, superior, limiteInferior, i);
            this.getForthColum(superior, limiteInferior, i);
        }
        table.setModel(model);
    }

    //to calculate the Limites Reales
    public void getSecondColumm(JTable table, double superior, double inferior, int i) {
        double limiteInferiorReal = inferior - 0.5;
        double limiteSuperiorReal = superior + 0.5;
        model.setValueAt(new String[]{String.format("%.2f-%.2f", limiteInferiorReal, limiteSuperiorReal)}, i, 1);
        this.getThirdColumm(limiteSuperiorReal, limiteInferiorReal, i);
    }

    //to calculate the Punto Medio
    public void getThirdColumm(double superior, double inferior, int i) {
        double point = (inferior + superior) / 2;
        model.setValueAt(point, i, 2);
    }

    //method to calculate the Frecuencia absoluta
    public void getForthColum(double superior, double inferior, int i) {
        int frecuencia = 0;
        int limiteInfInt = (int) Math.floor(inferior); // Obtener parte entera del límite inferior
        int limiteSupInt = (int) Math.floor(superior); // Obtener parte entera del límite superior
        for (Double valor : this.list) {
            int valorInt = valor.intValue(); // Obtener solo la parte entera del valor
            if (valorInt >= limiteInfInt && valorInt <= limiteSupInt) {
                frecuencia++;
            }
        }
        model.setValueAt(frecuencia, i, 3);
    }

    //method to calculate the Frecuencia Absoluta
    public void getFifthColum(JTable table, int amount) {
        double relative = amount / this.list.size() * 100;
        for (int i = 0; i < this.list.size(); i++) {
            model.setValueAt(relative, i, 4);
        }
    }
    
    
    
    
    
    
    
    
    
    
    

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //method to get Mediana
    public void getMe() {
        double me = 0;
        if (this.list.size() % 2 == 1) {
            me = this.list.get(this.list.size() / 2);
        } else {
            double medio1 = this.list.get(this.list.size() / 2 - 1);
            double medio2 = this.list.get(this.list.size() / 2);
            me = (medio1 + medio2) / 2.0;
        }
    }

    //method to get Moda
    public void getMo() {
        
    }

    //method to get Media Aritmetica (Promedio)
    public void getX(JTextField txt) {
        double x = 0;
        for (Double value : this.list) {
            x += value;
        }
        txt.setText("" + x);
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //method to add the data in the list
    public void addData(JTextField txtData) {
        this.list.add(Double.parseDouble(txtData.getText().toString()));
    }
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    //to load the columns in the table
    public void loadCandidateData(JTable table) {
        model.setRowCount(0);
        TableRowSorter<TableModel> order = new TableRowSorter<TableModel>(model);
        table.setRowSorter(order);
        for (int i = 0; i < columns.length; i++) {
            model.addColumn(columns[i]);
        }
        table.setModel(model);
    }

}
