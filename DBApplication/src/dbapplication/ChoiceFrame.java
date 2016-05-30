package dbapplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author omari_000
 */
public class ChoiceFrame extends JFrame {
    
    private ChoiceListener choiceListener;

    public ChoiceFrame(ChoiceListener listener) {
        this.choiceListener = listener; 
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(360, 150);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Please make a choice");
        setVisible(true);
    }

    private void createComponents() {
        JButton searchButton, registerButton;

        searchButton = new JButton("Search");
        registerButton = new JButton("Register");

        // set buttonsize
        registerButton.setSize(200, 50);
        searchButton.setSize(200, 50);

        // set buttonLocations
        registerButton.setLocation(50, 50);
        searchButton.setLocation(260, 50);

        //add to Frame
        add(registerButton);
        add(searchButton);

    }
    
    class SelectionListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }

}
