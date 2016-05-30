package dbapplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import dbapplication.student.*;
import dbapplication.program.*;
import dbapplication.institute.*;

/**
 *
 * @author omari_000
 */
public class MainFrame extends JFrame {
    
     enum  ButtonType{
            Student, Institute, Program
        }

    public MainFrame() {

        setupFrame();
        createComponents();

    }

    private void setupFrame() {
        setSize(300, 300);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("International office Exchange students systeem");
        setVisible(true);
    }

    private void createComponents() {
        JButton studentButton, instituteButton, programButton;

        studentButton = new JButton("Manage Students");
        instituteButton = new JButton("Manage Institutes");
        programButton = new JButton("Manage programs");

        // set buttonsize
        instituteButton.setSize(200, 50);
        programButton.setSize(200, 50);
        studentButton.setSize(200, 50);

        // set buttonLocations
        instituteButton.setLocation(50, 50);
        studentButton.setLocation(50, 110);
        programButton.setLocation(50, 170);
        
        //add to Frame
        add(instituteButton); 
        add(studentButton);
        add(programButton);

    }
    
    
    
    class SelectionListener implements ActionListener{
        ButtonType buttonType;
        public SelectionListener(ButtonType type) {
            buttonType = type;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            ChoiceListener choiceListener;
            switch(buttonType) {
                case Student:
                    choiceListener = new ChoiceListener(SearchStudentFrame.class, RegisterStudentFrame.class);
                    
            }
            ChoiceFrame frame = new ChoiceFrame(choiceListener);
        }   
    }

}
