package dbapplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import dbapplication.student.*;
import dbapplication.program.*;
import dbapplication.institute.*;
import javax.swing.JDialog;

/**
 *
 * @author omari_000
 */
public class MainFrame extends JFrame {
    
    enum ButtonType{
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
        
        // add action listeners
        instituteButton.addActionListener(new SelectionListener(new SearchInstituteFrame(this), new RegisterInstituteFrame(this)));
        studentButton.addActionListener(new SelectionListener(new SearchStudentFrame(this), new RegisterStudentFrame(this)));
        programButton.addActionListener(new SelectionListener(new SearchProgramFrame(this), new RegisterProgramFrame(this)));
        
        //add to Frame
        add(instituteButton); 
        add(studentButton);
        add(programButton);

    }
    
    
    
    class SelectionListener implements ActionListener{
        private JDialog searchDialog, registerDialog;
        public SelectionListener(JDialog search, JDialog register) {
            searchDialog = search;
            registerDialog = register;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            ChoiceListener choiceListener = new ChoiceListener(searchDialog, registerDialog);
            ChoiceDialog choice = new ChoiceDialog(MainFrame.this, choiceListener);
        }   
    }

}
