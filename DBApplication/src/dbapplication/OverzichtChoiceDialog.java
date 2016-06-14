package dbapplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Sishi
 */
public class OverzichtChoiceDialog extends JDialog{
    
    private final OverzichtChoiceListener choiceListener;
    
    private enum Choice {
        Country, City, Study;
    }
    
    public OverzichtChoiceDialog(JFrame owner, OverzichtChoiceListener listener) {
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
        JButton countryButton, cityButton, studyButton;

        countryButton = new JButton("Country");
        countryButton.setLocation(75, 50);
        countryButton.setSize(100, 50);
        add(countryButton);
        
        cityButton = new JButton("City");
        cityButton.setLocation(200, 50);
        cityButton.setSize(100, 50);
        add(cityButton);
        
        studyButton = new JButton("Study");
        studyButton.setLocation(325, 50);
        studyButton.setSize(100, 50);
        add(studyButton);

        // add selection listeners
        countryButton.addActionListener(new SelectionListener(Choice.Country));
        cityButton.addActionListener(new SelectionListener(Choice.City));
        studyButton.addActionListener(new SelectionListener(Choice.Study));

    }
    
    class SelectionListener implements ActionListener {
        
        private Choice buttonChoice;
        
        public SelectionListener(Choice choice) {
            buttonChoice = choice;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            dispose();
            
            if(Choice.Country == buttonChoice) {
                choiceListener.CountryClicked();
            } 
            else if (Choice.City == buttonChoice) {
                choiceListener.CityClicked();
            }
            else if (Choice.Study == buttonChoice) {
                choiceListener.StudyClicked();
            }
        }
        
    }
    
}
