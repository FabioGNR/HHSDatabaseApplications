package dbapplication.program;

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
    private JButton registerButton, instituteButton, selectStudyButton;
    private JRadioButton internshipButton, studyProgramButton;
    private ButtonGroup buttonGroup;
    private JCheckBox[] termBoxes = new JCheckBox[5];
    private JComboBox maxCreditBox, studyTypeBox;
    private String[] maxCredit = {"15 EC", "30 EC", "45 EC", "60 EC"};
    private String[] studyType = {"Minor", "European Project", "Summer School"};
    private int selectedInstitute = -1;
    private String selectedStudy = null;

    enum ButtonAction {
        Internship, StudyProgram
    }

    public RegisterProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(510, 510);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Register Program");
        setResizable(true);
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

        ActionListener openOrgButtonListener = new Selectinstitute();
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

        studyTypeBox = new JComboBox(studyType);
        studyTypeBox.setBounds(20, 250, 125, 25);
        add(studyTypeBox);
        studyTypeBox.setVisible(false);

        maxCreditBox = new JComboBox(maxCredit);
        maxCreditBox.setBounds(20, 150, 75, 25);
        add(maxCreditBox);

        for (int i = 0; i < termBoxes.length; i++) {
            termBoxes[i] = new JCheckBox("Term " + (i + 1));
            termBoxes[i].setBounds(220, 50 + (i * 25), 100, 25);
            add(termBoxes[i]);
        }

        ActionListener register = new RegisterProgram();
        registerButton = new JButton("Register");
        registerButton.setBounds(350, 380, 100, 25);
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
            SelectStudyDialog dlg = new SelectStudyDialog((JFrame) getOwner());
            Study study = dlg.getSelectedStudy();
            dlg.setVisible(true);

            if (studyProgramButton.isSelected() && study != null) {
                instituteField.setText(study.getCode());
                selectedStudy = study.getCode();
            }
        }
    }

    class RegisterProgram implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int maxCredits = 0;
            boolean[] terms = new boolean[5];
            String studyType = "";

            String name = nameField.getText();
            if (name.isEmpty()) {
                name = null;
            }
            String org_id = instituteField.getText();
            if (org_id.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterProgramFrame.this,
                        "Please choose an institute.",
                        "Institute not selected.", JOptionPane.WARNING_MESSAGE);
                org_id = null;
            }
            maxCredits = (maxCreditBox.getSelectedIndex() + 1) * 15;

            for (int i = 0; i < 5; i++) {
                terms[i] = termBoxes[i].isSelected();
            }

            if (internshipButton.isSelected()) {
                Internship.insertNewInternship(org_id, name, terms, maxCredits);
            } else {
                String study = studyField.getText();
                if (study.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterProgramFrame.this,
                            "Please choose a study.",
                            "Study not selected.", JOptionPane.WARNING_MESSAGE);
                    study = null;
                }
                studyType = (String) studyTypeBox.getSelectedItem();
                StudyProgram.insertNewStudyProgram(name, terms, org_id, studyType, maxCredits, selectedStudy);
            }
        }
    }

    class Selectinstitute implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            SelectInstituteDialog.InstituteType type = internshipButton.isSelected()
                    ? SelectInstituteDialog.InstituteType.Company
                    : SelectInstituteDialog.InstituteType.University;

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
