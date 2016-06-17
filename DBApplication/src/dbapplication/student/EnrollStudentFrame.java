package dbapplication.student;

import dbapplication.JEditField;
import dbapplication.institute.Institute;
import dbapplication.institute.SelectInstituteDialog;
import dbapplication.institute.SelectStudyDialog;
import dbapplication.institute.Study;
import dbapplication.program.ExProgram;
import dbapplication.program.SelectProgramFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Fabio
 */
public class EnrollStudentFrame extends JDialog {

    private JEditField programField, instituteField, studyField;
    private JEditField dateField;
    private JEditField creditsField;
    private JButton okButton, cancelButton;
    private JButton selectInstituteButton, selectStudyButton, selectProgramButton;
    private ExProgram selectedProgram = null;
    private Enrollment insertedEnrollment = null;
    private Institute selectedInstitute = null;
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
        instituteField = new JEditField("Institute");
        instituteField.setBounds(10, 10, 150, 30);
        instituteField.setEnabled(false);
        add(instituteField);

        selectInstituteButton = new JButton("...");
        selectInstituteButton.setBounds(160, 10, 30, 30);
        selectInstituteButton.addActionListener(new SelectInstituteListener());
        add(selectInstituteButton);

        studyField = new JEditField("Study");
        studyField.setBounds(10, 50, 150, 30);
        studyField.setEnabled(false);
        studyField.setVisible(false);
        add(studyField);

        selectStudyButton = new JButton("...");
        selectStudyButton.setBounds(160, 50, 30, 30);
        selectStudyButton.addActionListener(new SelectStudyListener());
        selectStudyButton.setVisible(false);
        add(selectStudyButton);

        programField = new JEditField("Program");
        programField.setBounds(10, 90, 150, 30);
        programField.setEnabled(false);
        add(programField);

        selectProgramButton = new JButton("...");
        selectProgramButton.setBounds(160, 90, 30, 30);
        selectProgramButton.addActionListener(new SelectProgramListener());
        add(selectProgramButton);

        dateField = new JEditField("Registration Date");
        dateField.setBounds(10, 130, 180, 30);
        add(dateField);

        creditsField = new JEditField("Acquired credits");
        creditsField.setBounds(10, 170, 180, 30);
        add(creditsField);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(10, 210, 85, 30);
        cancelButton.addActionListener(new CancelButtonListener());
        add(cancelButton);

        okButton = new JButton("OK");
        okButton.setBounds(105, 210, 85, 30);
        okButton.addActionListener(new OKButtonListener());
        add(okButton);
    }

    public Enrollment getInsertedEnrollment() {
        return insertedEnrollment;
    }

    private class SelectInstituteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SelectInstituteDialog dlg = new SelectInstituteDialog((JFrame) getOwner(), null);
            dlg.setVisible(true);
            // pauses until dialog is closed
            Institute institute = dlg.getSelectedInstitute();
            if (institute != null) {
                instituteField.setText(institute.getName());
                selectedInstitute = institute;
                studyField.setText("");
                programField.setText("");
                selectedProgram = null;
                studyField.setVisible(!institute.isBusiness());
                selectStudyButton.setVisible(!institute.isBusiness());
            }
        }
    }

    private class SelectStudyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(selectedInstitute == null) return;
            SelectStudyDialog dlg = new SelectStudyDialog(
                    (JFrame) getOwner(), selectedInstitute.getOrgid());
            dlg.setVisible(true);
            // pauses until dialog is closed
            Study study = dlg.getSelectedStudy();
            if (study != null) {
                studyField.setText(study.getCode());
                programField.setText("");
                selectedProgram = null;
            }
        }
    }

    class SelectProgramListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedInstitute == null) {
                return;
            }
            JFrame owner = (JFrame) EnrollStudentFrame.this.getOwner();
            SelectProgramFrame frame;
            if (selectedInstitute.isBusiness()) {
                frame = new SelectProgramFrame(owner,
                        selectedInstitute.getOrgid());
            } else {
                if (!studyField.getText().isEmpty()) {
                    frame = new SelectProgramFrame(owner,
                        selectedInstitute.getOrgid(), studyField.getText());
                }
                else
                    return;
            }
            frame.setVisible(true);
            ExProgram program = frame.getSelectedProgram();
            programField.setText(program == null ? "" : program.getName());
            selectedProgram = program;
        }
    }

    class OKButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedProgram == null) {
                JOptionPane.showMessageDialog(EnrollStudentFrame.this.getOwner(),
                        "Please select an exchange program",
                        "Missing program", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String sCredits = creditsField.getText();
            if (!sCredits.matches("^\\d+$")) {
                JOptionPane.showMessageDialog(EnrollStudentFrame.this.getOwner(),
                        "Acquired credits must be a number",
                        "Missing acquired credit", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int credits = Integer.parseInt(sCredits);
            if (credits > selectedProgram.getMaxCredits()) {
                JOptionPane.showMessageDialog(EnrollStudentFrame.this.getOwner(),
                        "Acquired credits must be below or equal to max credits for selected program",
                        "Acquired credits exceed max credits", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String sDate = dateField.getText();
            Date date;
            try {
                date = Date.valueOf(sDate);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(EnrollStudentFrame.this.getOwner(),
                        "Date must be in the format of yyyy-mm-dd",
                        "Incorrect registration date", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!Enrollment.insertEnrollment(student.getStudentid(),
                    selectedProgram.getCode(), credits, date)) {
                JOptionPane.showMessageDialog(EnrollStudentFrame.this.getOwner(),
                        "Failed to enroll student for program",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            insertedEnrollment = new Enrollment(student.getStudentid(),
                    selectedProgram, credits, date);
            insertedEnrollment.setExistsInDB(true);
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
