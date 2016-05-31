package dbapplication;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author omari_000
 */
public class ChoiceListener {
    private final JDialog searchDialog, registerDialog;
    public ChoiceListener(JDialog search, JDialog register) {
        searchDialog = search;
        registerDialog = register;
    }
    
    public void SearchClicked() {
        searchDialog.setVisible(true);
        searchDialog.toFront();
    }
    
    public void RegisterClicked() {
        registerDialog.setVisible(true);     
        registerDialog.toFront();
    }
}
