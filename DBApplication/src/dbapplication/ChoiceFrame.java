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
    private enum Choice { Search, Register };

    public ChoiceFrame(ChoiceListener listener) {
        this.choiceListener = listener; 
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(510, 150);
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
        registerButton.setLocation(260, 50);
        searchButton.setLocation(50, 50);
        
        // add selection listeners
        searchButton.addActionListener(new SelectionListener(Choice.Search));
        registerButton.addActionListener(new SelectionListener(Choice.Register));

        //add to Frame
        add(registerButton);
        add(searchButton);

    }
    
    class SelectionListener implements ActionListener {
        private Choice buttonChoice;
        public SelectionListener(Choice choice) {
            buttonChoice = choice;
        }
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            // let the choice listener know what happened
            if(Choice.Search == buttonChoice) {
                choiceListener.SearchClicked();
            }
            else if (Choice.Register == buttonChoice) {
                choiceListener.RegisterClicked();
            }
            // close/dispose this frame because the choice has been made
            dispose();
        }
    }
}
