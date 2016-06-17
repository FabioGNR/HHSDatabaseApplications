package dbapplication.program;

import dbapplication.JEditArea;
import dbapplication.JEditField;
import dbapplication.institute.Institute;
import dbapplication.institute.SelectInstituteDialog;
import dbapplication.institute.SelectStudyDialog;
import dbapplication.institute.Study;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author omari_000
 */
public class RegisterProgramFrame extends JDialog {

    private JTextField instituteField, nameField, studyField;
    private JEditArea descriptionArea;
    private JButton registerButton, instituteButton, selectStudyButton;
    private JRadioButton internshipButton, studyProgramButton;
    private ButtonGroup buttonGroup;
    private final JCheckBox[] termBoxes = new JCheckBox[ExProgram.Terms];
    private JComboBox maxCreditBox, studyTypeBox;
    private final String[] maxCredit = {"15 EC", "30 EC", "45 EC", "60 EC"};
    private int selectedInstitute = -1;
    private Study selectedStudy = null;

    enum ButtonAction {
        Internship, StudyProgram
    }

    public RegisterProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(575, 575);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Register Program");
        setResizable(true);
    }
    
    private void resetFields() {
        selectedStudy = null;
        selectedInstitute = -1;
        instituteField.setText("");
        studyField.setText("");
        descriptionArea.setText("");
        nameField.setText("");
        for(int i = 0; i < termBoxes.length; i++) {
            termBoxes[i].setSelected(false);
        }
    }

    private void createComponents() {

        ActionListener switchButton = new SwitchProgram();
        internshipButton = new JRadioButton("Internship");
        internshipButton.setBounds(375, 50, 90, 25);
        internshipButton.addActionListener(switchButton);
        add(internshipButton);
        internshipButton.setSelected(true);

        studyProgramButton = new JRadioButton("Study");
        studyProgramButton.setBounds(375, 90, 95, 25);
        studyProgramButton.addActionListener(switchButton);
        add(studyProgramButton);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(studyProgramButton);
        buttonGroup.add(internshipButton);

        nameField = new JEditField("Name");
        nameField.setBounds(20, 50, 125, 25);
        add(nameField);

        instituteField = new JEditField("Institute");
        instituteField.setBounds(20, 100, 125, 25);
        instituteField.setEnabled(false);
        add(instituteField);

        ActionListener openOrgButtonListener = new SelectInstitute();
        instituteButton = new JButton("...");
        instituteButton.setBounds(155, 100, 40, 25);
        instituteButton.addActionListener(openOrgButtonListener);
        add(instituteButton);

        studyField = new JEditField("Study");
        studyField.setBounds(20, 200, 125, 25);
        studyField.setEnabled(false);
        add(studyField);
        studyField.setVisible(false);

        ActionListener studyButtonListener = new SelectStudy();
        selectStudyButton = new JButton("...");
        selectStudyButton.setBounds(155, 200, 40, 25);
        selectStudyButton.addActionListener(studyButtonListener);
        add(selectStudyButton);
        selectStudyButton.setVisible(false);
        

        studyTypeBox = new JComboBox(StudyProgram.StudyType.values());
        studyTypeBox.setBounds(20, 250, 125, 25);
        add(studyTypeBox);
        studyTypeBox.setVisible(false);
        
        descriptionArea = new JEditArea("Write a description");
        descriptionArea.setBounds(20, 300, 350, 100);
        add(descriptionArea);

        maxCreditBox = new JComboBox(maxCredit);
        maxCreditBox.setBounds(20, 150, 75, 25);
        add(maxCreditBox);

        for (int i = 0; i < termBoxes.length; i++) {
            termBoxes[i] = new JCheckBox("Term " + (i + 1));
            termBoxes[i].setBounds(240, 50 + (i * 25), 100, 25);
            add(termBoxes[i]);
        }

        ActionListener register = new RegisterProgram();
        registerButton = new JButton("Register");
        registerButton.setBounds(400, 425, 100, 25);
        registerButton.addActionListener(register);
        add(registerButton);
    }

    class SwitchProgram implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean studyProgramSelected = internshipButton.isSelected();
            instituteField.setText("");
            selectedInstitute = -1;
            selectedStudy = null;
            studyField.setVisible(!studyProgramSelected);
            selectStudyButton.setVisible(!studyProgramSelected);
            studyTypeBox.setVisible(!studyProgramSelected);
        }
    }

    class SelectStudy implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(selectedInstitute == -1) {
                JOptionPane.showMessageDialog(RegisterProgramFrame.this, 
                        "Please select an university first.", 
                        "Select university", JOptionPane.WARNING_MESSAGE);
                return;
            }
            SelectStudyDialog dlg = new SelectStudyDialog(
                    (JFrame) getOwner(), selectedInstitute); 
            dlg.setVisible(true);
            Study study = dlg.getSelectedStudy();

            if (study != null) {
                studyField.setText(study.getCode());
                selectedStudy = study;
            }
        }
    }

    class RegisterProgram implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int maxCredit;
            boolean[] terms = new boolean[ExProgram.Terms];
            // nameField
            String name = nameField.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterProgramFrame.this,
                        "Please enter a name!",
                        "No name entered", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // instituteBox
            if (selectedInstitute == -1) {
                JOptionPane.showMessageDialog(RegisterProgramFrame.this,
                        "Please choose an institute!",
                        "Institute not selected.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // checkBoxes
            boolean anySelected = false;
            for (int i = 0; i < ExProgram.Terms; i++) {
                terms[i] = termBoxes[i].isSelected();
                if (terms[i] == true) {
                    anySelected = true;
                }
            }
            if (anySelected == false) {
                JOptionPane.showMessageDialog(RegisterProgramFrame.this,
                        "Please select at least one term!",
                        "No term was selected.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // descriptionField
            String description = descriptionArea.getText();
            if (description.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterProgramFrame.this,
                        "Please enter a description!",
                        "No description", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // creditBox
            maxCredit = (maxCreditBox.getSelectedIndex() + 1) * 15;

            boolean result;
            if (internshipButton.isSelected()) {
                // insert internship data
                result = Internship.insertNewInternship(name, terms,
                        maxCredit, description, selectedInstitute);
            } else {
                // studyField
                if (selectedStudy == null) {
                    JOptionPane.showMessageDialog(RegisterProgramFrame.this,
                            "Please choose a study!",
                            "Study not selected.", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                // studyTypeBox
                String studyType = ((StudyProgram.StudyType)studyTypeBox.getSelectedItem()).name();
                // insert studyProgram data
                result = StudyProgram.insertNewStudyProgram(name, terms, maxCredit,
                        description, selectedInstitute, studyType, 
                        selectedStudy.getCode());
            }
            if (!result) {
                JOptionPane.showMessageDialog(RegisterProgramFrame.this,
                        "Registering a program failed.", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(RegisterProgramFrame.this,
                        "Program was registered.", "Success", JOptionPane.PLAIN_MESSAGE);
                // reset fields and close dialog
                resetFields();
                setVisible(false);
            }

        }
    }

    class SelectInstitute implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            Institute.InstituteType type = internshipButton.isSelected()
                    ? Institute.InstituteType.Company
                    : Institute.InstituteType.University;

            SelectInstituteDialog instDlg = new SelectInstituteDialog((JFrame) getOwner(), type);
            instDlg.setVisible(true);
            Institute institute = instDlg.getSelectedInstitute();

            if (institute != null) {
                instituteField.setText(institute.getName());
                selectedInstitute = institute.getOrgid();
            }
        }
    }
}
