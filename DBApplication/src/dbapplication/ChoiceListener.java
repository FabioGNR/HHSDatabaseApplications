package dbapplication;

import javax.swing.JDialog;

/**
 *
 * @author omari_000
 */
public class ChoiceListener {
    // deze listener krijgt twee dialogs, en laat de juiste zien op basis
    // van de keuze die gemaakt wordt
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
