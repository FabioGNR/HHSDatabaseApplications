package dbapplication.student;

import dbapplication.JEditField;
import dbapplication.program.ExProgram;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Fabio
 */
public class EnrollStudentFrame extends JDialog {
    private JEditField programField;
    private JEditField dateField;
    private JEditField creditsField;
    private JButton okButton, cancelButton, selectProgramButton;
    private ExProgram selectedProgram = null;
    private Enrollment insertedEnrollment = null;
    private Student student;
    
    public EnrollStudentFrame(JFrame owner, Student student) {
        super(owner, true);
        setupFrame();
        this.student = student;
        createComponents();
    }

    private void setupFrame() {
        setSize(300, 400);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Enroll student");
    }

    private void createComponents() {
        programField = new JEditField("Program");
        programField.setBounds(10, 10, 100, 30);
        add(programField);
        
        selectProgramButton = new JButton("...");
        selectProgramButton.setBounds(110, 10, 30, 30);
        selectProgramButton.addActionListener(new SelectProgramListener());
        add(selectProgramButton);
        
        dateField = new JEditField("Registration Date");
        dateField.setBounds(10, 50, 130, 30);
        add(dateField);
        
        creditsField = new JEditField("Acquired credits");
        creditsField.setBounds(10, 90, 130, 30);
        add(creditsField);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(10, 130, 60, 30);
        cancelButton.addActionListener(new CancelButtonListener());
        add(cancelButton);
        
        okButton = new JButton("OK");
        okButton.setBounds(80, 130, 60, 30);
        okButton.addActionListener(new OKButtonListener());
        add(okButton);
    }

    public Enrollment getInsertedEnrollment() {
        return insertedEnrollment;
    }
    
    class SelectProgramListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
    class OKButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(selectedProgram == null) {
                JOptionPane.showMessageDialog(EnrollStudentFrame.this.getOwner(), 
                        "Please select an exchange program", 
                        "Missing program", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String sCredits = creditsField.getText();
            if(!sCredits.matches("^\\d+$")) {
                JOptionPane.showMessageDialog(EnrollStudentFrame.this.getOwner(), 
                        "Acquired credits must be a number", 
                        "Missing acquired credit", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int credits = Integer.parseInt(sCredits);
            if(credits > selectedProgram.getMaxCredits()) {
                JOptionPane.showMessageDialog(EnrollStudentFrame.this.getOwner(), 
                        "Acquired credits must be below or equal to max credits for selected program", 
                        "Acquired credits exceed max credits", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String sDate = dateField.getText();
            Date date;
            try {
                date = Date.valueOf(sDate);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(EnrollStudentFrame.this.getOwner(), 
                        "Date must be in the format of yyyy-mm-dd", 
                        "Incorrect registration date", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(!Enrollment.insertEnrollment(student.getStudentid(), 
                    selectedProgram.getCode(), credits, date)) {
                JOptionPane.showMessageDialog(EnrollStudentFrame.this.getOwner(), 
                        "Failed to enroll student for program", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            insertedEnrollment = new Enrollment(student.getStudentid(), 
                    selectedProgram, credits, date);
            dispose();
        }
        
    }
    
    class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
        
    }
}
