package dbapplication.overzicht;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Sishi
 */
public class CountryOverzicht extends JDialog {
    
    public CountryOverzicht(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(400, 400);
        setTitle("Country overzicht");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
    }

    private void createComponents() {
        
    }
    
}
