package Controller;

import Model.Function;
import com.singularsys.jep.JepException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author anama
 */
public class Method {

    //Method to recieve the parameters, x0 and x1 are the points to graphicate, the c to recieve a max number and e the error in case there is one
    public double root(Function f, double x0, double x1, int c, double e) {
        double r = Double.NaN; //is the double of the root to calculate it. NaN helps to not be a number
        double x2 = x0; //the interesection of the X in the graphic
        int k = 0; //to count the cicles of the while
        try {
            while (Math.abs(f.value(x2)) > e && k < c) { //while the value of x2 is ibbger than e (which means we have not find the root) and bigger than K (which is the counter) and smaller than c (max number of tries)
                x2 = x0 - f.value(x0) * (x1 - x0) / (f.value(x1) - f.value(x0)); //this helps to calculate a new value to move the graphic closer to the root
                x0 = x1;
                x1 = x2;
                k++; //sum 1 more to count how many times we tried to find the root
            }
            if (k < c) { //if k is less than c, we found the root
                r = x2; //r is now the value of the root
            }
        } catch (JepException ex) {
            JOptionPane.showMessageDialog(null, "ERROR", "ERROR EN CLASE METODO PARA ENCONTRAR EL VALOR DE LA RAÍZ" , JOptionPane.ERROR_MESSAGE);
        }
        return r; //returns the value of the root
    }

}
