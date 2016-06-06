package dbapplication.program;

import javax.swing.JDialog;
import javax.swing.JFrame;

import dbapplication.JEditField;
import static dbapplication.program.RegisterProgramFrame.ButtonAction.Internship;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author omari_000
 */
public class RegisterProgramFrame extends JDialog {

    private JTextField orgIDField, nameField, studyField;
    private JButton registerButton;
    private JRadioButton internshipButton, studyButton;
    private JRadioButton term1, term2, term3, term4, term5;
    private JRadioButton minor, eps, summerSchool;
    private ButtonGroup buttonGroup;
    private JCheckBox[] termBoxes = new JCheckBox[6];

    enum ButtonAction {
        Internship, StudyProgram
    }

    public RegisterProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createInternshipComponents();
        createStudyProgram();
    }

    private void setupFrame() {
        setSize(510, 510);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Register Program");
        setResizable(true);
    }

    private void createInternshipComponents() {

        orgIDField = new JEditField("Institute code");
        orgIDField.setBounds(20, 50, 100, 25);
        add(orgIDField);

        nameField = new JEditField("Name");
        nameField.setBounds(20, 100, 100, 25);
        add(nameField);

//        ActionListener registerListener = new 
        registerButton = new JButton("Register");
        registerButton.setBounds(350, 380, 100, 25);
//        registerButton.addActionListener();
        add(registerButton);

        ActionListener switchButton = new ButtonListener();
        internshipButton = new JRadioButton("Internship");
        internshipButton.setBounds(130, 50, 95, 25);
        internshipButton.addActionListener(switchButton);
        add(internshipButton);
        internshipButton.setSelected(true);

        studyButton = new JRadioButton("Study");
        studyButton.setBounds(130, 75, 95, 25);
        studyButton.addActionListener(switchButton);
        add(studyButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(studyButton);
        buttonGroup.add(internshipButton);
        for (int i = 0; i < termBoxes.length; i++) {
            termBoxes[i] = new JCheckBox("Term "+(i+1));
            termBoxes[i].setBounds(400, 50+(i*25), 100, 25);
            add(termBoxes[i]);
        }
    }

    private void createStudyProgram() {
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

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name, org_id,study;
            if (e.getSource() == registerButton) {
                name = nameField.getText();
                if (name.isEmpty()) {
                    name = null;
                }
                org_id = orgIDField.getText();
                if (org_id.isEmpty()) {
                    org_id = null;
                }
                study = studyField.getText();
                if (study.isEmpty()) {
                    study = null;
                }
            }
            else {
                boolean studyProgramSelected = studyButton.isSelected();
                eps.setVisible(studyProgramSelected);
                summerSchool.setVisible(studyProgramSelected);
                minor.setVisible(studyProgramSelected);
            }
        }
    }
}
