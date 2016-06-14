package dbapplication.overzicht;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Sishi
 */
public class CityOverzicht extends JDialog{
    
    public CityOverzicht(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(400, 400);
        setTitle("City overzicht");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
    }

    private void createComponents() {
    }
    
    
}
