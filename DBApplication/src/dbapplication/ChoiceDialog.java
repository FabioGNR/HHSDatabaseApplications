package dbapplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author omari_000
 */
public class ChoiceDialog extends JDialog {

    private final ChoiceListener choiceListener;

    private enum Choice {
        Search, Register
    };

    public ChoiceDialog(JFrame owner, ChoiceListener listener) {
        super(owner, true);    
        this.choiceListener = listener;
        setupFrame();      
        createComponents(); 
        setVisible(true);
    }

    private void setupFrame() {
        setSize(510, 150);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Please make a choice");
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
            // close/dispose this frame because the choice has been made
            dispose();
            // let the choice listener know what happened
            if (Choice.Search == buttonChoice) {
                choiceListener.SearchClicked();
            } else if (Choice.Register == buttonChoice) {
                choiceListener.RegisterClicked();
            }       
        }
    }
}
