package dbapplication;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author omari_000
 */
public class ChoiceListener {
    Class searchFrame, registerFrame;
    public ChoiceListener(Class search, Class register) {
        searchFrame = search;
        registerFrame = register;
    }
    
    public void SearchClicked() {
        try {
            searchFrame.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void RegisterClicked() {
        try {
            registerFrame.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
    }
}
