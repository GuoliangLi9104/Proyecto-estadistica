package Model;

import com.singularsys.jep.JepException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.nfunk.jep.JEP;

/**
 *
 * @author anama
 */
public class Function {

    JEP j = new JEP();

    //this method recieves the function and then, added to the J
    public Function(String def) {
        try {
            j.addVariable("x", 0);
        } catch (JepException ex) {
            JOptionPane.showMessageDialog(null, "ERROR", j.getErrorInfo(), JOptionPane.ERROR_MESSAGE);
        }
        j.addStandardConstants();
        j.addStandardFunctions();
        j.parseExpression(def);
        if (j.hasError()) {
            JOptionPane.showMessageDialog(null, "ERROR", j.getErrorInfo(), JOptionPane.ERROR_MESSAGE);
        }
    }

    //method to return a doble when validate the X value
    public double value(double x) throws JepException {
        double r;
        j.addVariable("x", x);
        r = j.getValue();
        if (j.hasError()) {
            JOptionPane.showMessageDialog(null, "ERROR", j.getErrorInfo(), JOptionPane.ERROR_MESSAGE);
            return r;
        }
        return r; //return the result
    }

}
