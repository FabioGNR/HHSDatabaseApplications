package dbapplication.program;

import javax.swing.JDialog;
import javax.swing.JFrame;

import dbapplication.JEditField;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author omari_000
 */
public class RegisterProgramFrame extends JDialog {

    private final String INTERNSHIP_CARD_ID = "Internship";
    private final String STUDY_CARD_ID = "Study";
    private CardLayout containerLayout;

    private JTextField orgIDField, nameField;
    private JButton registerButton;
    private JRadioButton internshipButton, studyButton;
    private ButtonGroup buttonGroup;
    private JComboBox termBox, studyTypeBox, maxCreditBox;
    private JPanel containerPanel, internshipPanel, studyPanel;

    private static String[] terms = {"term 1", "term 2", "term 3", "term 4"};
    private static String[] studyType = {"Minor", "European Project Semester", "Summer school"};
    private static String[] max_credits = {"15 EC", "30 EC"};

    enum ButtonAction {
        Internship, StudyProgram
    }

    public RegisterProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createInternshipComponents();
        createStudyProgram();
        switchProgram();
    }

    private void setupFrame() {
        setSize(510, 510);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Register Program");
        setResizable(true);
    }

    private void createInternshipComponents() {

        internshipPanel = new JPanel();
        internshipPanel.setLayout(null);

        orgIDField = new JEditField("Institute code");
        nameField = new JEditField("Name");
        orgIDField.setBounds(20, 50, 100, 25);
        nameField.setBounds(20, 100, 100, 25);
        add(orgIDField);
        add(nameField);

        registerButton = new JButton("Register");
        registerButton.setBounds(350, 380, 100, 25);
        add(registerButton);

        internshipButton = new JRadioButton("Internship");
        studyButton = new JRadioButton("Study");
        internshipButton.setBounds(130, 50, 100, 25);
        studyButton.setBounds(130, 100, 100, 25);

        add(internshipButton);
        add(studyButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(studyButton);
        buttonGroup.add(internshipButton);

        termBox = new JComboBox(terms);
        termBox.setBounds(230, 50, 100, 20);
        add(termBox);

        maxCreditBox = new JComboBox(max_credits);
        maxCreditBox.setBounds(20, 150, 75, 25);
        add(maxCreditBox);

    }

    private void createStudyProgram() {

        studyPanel = new JPanel();
        studyPanel.setLayout(null);

        orgIDField = new JEditField("Institute code");
        nameField = new JEditField("Name");
        orgIDField.setBounds(20, 50, 100, 25);
        nameField.setBounds(20, 100, 100, 25);
        add(orgIDField);
        add(nameField);

        registerButton = new JButton("Register");
        registerButton.setBounds(350, 380, 100, 25);
        add(registerButton);

        internshipButton = new JRadioButton("Internship");
        studyButton = new JRadioButton("Study");
        internshipButton.setBounds(130, 50, 100, 25);
        studyButton.setBounds(130, 100, 100, 25);

        add(internshipButton);
        add(studyButton);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(studyButton);
        buttonGroup.add(internshipButton);
        studyTypeBox = new JComboBox(studyType);
        studyTypeBox.setBounds(340, 50, 125, 20);
        add(studyTypeBox);
    }

    private void switchProgram() {
        containerLayout = new CardLayout();
        containerPanel = new JPanel(containerLayout);
        containerPanel.add(internshipPanel, INTERNSHIP_CARD_ID);
        containerPanel.add(studyPanel, STUDY_CARD_ID);
        add(containerPanel);
    }

    class RegisterListener implements ActionListener {

        ButtonAction action;

        public RegisterListener(ButtonAction action) {
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name, org_id, study, type, max_credits, exPcode;

            if (internshipButton.isSelected()) {
                containerLayout.show(internshipPanel, INTERNSHIP_CARD_ID);
            } else if (studyButton.isSelected()) {
                containerLayout.show(studyPanel, STUDY_CARD_ID);
            } else {
                containerLayout.show(internshipPanel, INTERNSHIP_CARD_ID);
            }
            
            name = nameField.getText();
            if(name.isEmpty()){
                name = null;
            }
            org_id = orgIDField.getText();
            if(org_id.isEmpty()){
                org_id = null;
            }
            study = studyTypeBox.getSelectedObjects().toString();
            if(study.isEmpty()){
                 
                
            }
        }
    }
}
