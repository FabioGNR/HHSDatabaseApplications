package dbapplication.program;

import javax.swing.JDialog;
import javax.swing.JFrame;

import dbapplication.JEditField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author omari_000
 */
public class RegisterProgramFrame extends JDialog {

    private JTextField orgIDField, nameField, studyCode;
    private JButton registerButton, orgIdButton;
    private JRadioButton internshipButton, studyButton;
    private JRadioButton minor, eps, summerSchool;
    private ButtonGroup buttonGroup;
    private JCheckBox[] termBoxes = new JCheckBox[5];
    private JComboBox maxCreditBox;
    private String[] maxCredit = {"", "15 EC", "30 EC"};

    enum ButtonAction {
        Internship, StudyProgram
    }

    public RegisterProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createInternshipComponents();
    }

    private void setupFrame() {
        setSize(510, 510);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Register Program");
        setResizable(true);
    }

    private void createInternshipComponents() {

        nameField = new JEditField("Name");
        nameField.setBounds(20, 50, 100, 25);
        add(nameField);

        orgIDField = new JEditField("Institute code");
        orgIDField.setBounds(20, 100, 100, 25);
        add(orgIDField);

        //add a button to select org
        orgIdButton = new JButton("...");
        orgIdButton.setBounds(130, 100, 40, 25);
        add(orgIdButton);

        maxCreditBox = new JComboBox(maxCredit);
        maxCreditBox.setBounds(20, 150, 75, 25);
        add(maxCreditBox);

        for (int i = 0; i < termBoxes.length; i++) {
            termBoxes[i] = new JCheckBox("Term " + (i + 1));
            termBoxes[i].setBounds(400, 50 + (i * 25), 100, 25);
            add(termBoxes[i]);
        }

//        ActionListener registerListener = new 
        registerButton = new JButton("Register");
        registerButton.setBounds(350, 380, 100, 25);
//        registerButton.addActionListener();
        add(registerButton);

        buttonGroup = new ButtonGroup();
        buttonGroup.add(studyButton);
        buttonGroup.add(internshipButton);
        ActionListener switchButton = new SwitchProgram();
        internshipButton = new JRadioButton("Internship");
        internshipButton.setBounds(130, 50, 95, 25);
        internshipButton.addActionListener(switchButton);
        add(internshipButton);
        internshipButton.setSelected(true);

        studyButton = new JRadioButton("Study");
        studyButton.setBounds(130, 75, 95, 25);
        studyButton.addActionListener(switchButton);
        add(studyButton);

        //listener toevoegen
        //studyCode ontbreekt in database 
        
        
        // studyType
        minor = new JRadioButton("Minor");
        minor.setBounds(225, 50, 170, 25);
        minor.setVisible(false);
        add(minor);

        eps = new JRadioButton("European Project Semester");
        eps.setBounds(225, 100, 170, 25);
        eps.setVisible(false);
        add(eps);

        summerSchool = new JRadioButton("Summer School");
        summerSchool.setBounds(225, 150, 170, 25);
        summerSchool.setVisible(false);
        add(summerSchool);
    }

    class SwitchProgram implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == internshipButton) {
                boolean internshipSelected = internshipButton.isSelected();
                eps.setVisible(!internshipSelected);
                summerSchool.setVisible(!internshipSelected);
                minor.setVisible(!internshipSelected);
            } else {
                boolean studyProgramSelected = studyButton.isSelected();
                eps.setVisible(studyProgramSelected);
                summerSchool.setVisible(studyProgramSelected);
                minor.setVisible(studyProgramSelected);
            }
        }
    }

    class RegisterProgram implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name, org_id, term, maxCredit, studyType, studyCode;
            name = nameField.getText();
            if (name.isEmpty()) {
                name = null;
            }
            org_id = orgIDField.getText();
            if (org_id.isEmpty()) {
                org_id = null;
            }
            
            
            if(internshipButton.isSelected()){
//                Internship.insertNewInternship(org_id, name, term);
            }
        }

    }
}
