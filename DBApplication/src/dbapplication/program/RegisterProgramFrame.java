package dbapplication.program;

import javax.swing.JDialog;
import javax.swing.JFrame;

import dbapplication.JEditField;
import static dbapplication.program.RegisterProgramFrame.ButtonAction.Internship;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author omari_000
 */
public class RegisterProgramFrame extends JDialog {

    private final String INTERNSHIP_CARD_ID = "Internship";
    private final String STUDY_CARD_ID = "Study";
    private CardLayout cardContainer;

    private JTextField orgIDField, nameField, addressField, cityField, countryField, studyField;
    private JButton registerButton;
    private JRadioButton internshipButton, studyButton;
    private JRadioButton term1, term2, term3, term4, term5;
    private JRadioButton minor, eps, summerSchool;
    private ButtonGroup buttonGroup;
    private JPanel containerPanel, internshipPanel, studyPanel;
    private JComboBox studyBox;

    enum ButtonAction {
        Internship, StudyProgram
    }

    public RegisterProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        addContainerPanel();
//        createInternshipComponents();
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

        addressField = new JEditField("Address");
        addressField.setBounds(20, 150, 100, 25);
        add(addressField);

        cityField = new JEditField("City");
        cityField.setBounds(20, 200, 100, 25);
        add(cityField);

        countryField = new JEditField("Country");
        countryField.setBounds(20, 250, 100, 25);
        add(countryField);

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

        term1 = new JRadioButton("term 1");
        term1.setBounds(300, 50, 100, 25);
        add(term1);

        term2 = new JRadioButton("term 2");
        term2.setBounds(300, 75, 100, 25);
        add(term2);

        term3 = new JRadioButton("term 3");
        term3.setBounds(300, 100, 100, 25);
        add(term3);

        term4 = new JRadioButton("term 4");
        term4.setBounds(300, 125, 100, 25);
        add(term4);

        term5 = new JRadioButton("term 5");
        term5.setBounds(300, 150, 100, 25);
        add(term5);
    }

    private void createStudyProgram() {

        createInternshipComponents();

        minor = new JRadioButton("Minor");
        minor.setBounds(225, 50, 100, 25);
        add(minor);

        eps = new JRadioButton("European Project Semester");
        eps.setBounds(225, 100, 100, 25);
        add(eps);

        summerSchool = new JRadioButton("Summer School");
        summerSchool.setBounds(225, 150, 100, 25);
        add(summerSchool);

    }

    private void addContainerPanel() {

        internshipPanel = new JPanel();
        internshipPanel.setLayout(null);
        studyPanel = new JPanel();
        studyPanel.setLayout(null);

        cardContainer = new CardLayout();
        containerPanel = new JPanel(cardContainer);
        containerPanel.add(internshipPanel, INTERNSHIP_CARD_ID);
        containerPanel.add(studyPanel, STUDY_CARD_ID);
        add(containerPanel);
    }

    class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String name, org_id, address, city, country, study, type, max_credits, exPcode;

            if (internshipButton.isSelected()) {
                cardContainer.show(containerPanel, INTERNSHIP_CARD_ID);
            } else if (studyButton.isSelected()) {
                cardContainer.show(containerPanel, STUDY_CARD_ID);
            } else {
                cardContainer.show(containerPanel, INTERNSHIP_CARD_ID);
            }

            if (e.getSource() == registerButton) {
                name = nameField.getText();
                if (name.isEmpty()) {
                    name = null;
                }
                org_id = orgIDField.getText();
                if (org_id.isEmpty()) {
                    org_id = null;
                }
                address = addressField.getText();
                if (address.isEmpty()) {
                    address = null;
                }
                city = cityField.getText();
                if (city.isEmpty()) {
                    city = null;
                }
                country = countryField.getText();
                if (country.isEmpty()) {
                    country = null;
                }
                study = studyField.getText();
                if (study.isEmpty()) {
                    study = null;
                }

            }
        }
    }
}
