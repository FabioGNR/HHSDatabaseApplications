package dbapplication.institute;

import dbapplication.JEditField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author jordain & sishi
 */
public class RegisterInstituteFrame extends JDialog {

    private JTextField nameField;
    private JTextField cityField;
    private JTextField countryField;
    private JTextField addressField;

    private JButton registerButton;

    private JButton showButton;
    RegisterStudyDialog s = null;
    private Study study;
    private static JComboBox Studiesbox;

    private JRadioButton yesRadio;
    private JRadioButton noRadio;
    private JLabel isBusinessLabel;
    int org_idreturn;

    public int getOrg_idreturn() {
        return org_idreturn;
    }

    private String selectedStudy;

    public RegisterInstituteFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(510, 300);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Register Institute");
    }

    private void createComponents() {
        SwitchInstituteListener switchLis = new SwitchInstituteListener();
        RegisterStudyListener studyLis = new RegisterStudyListener();

        nameField = new JEditField("Name");
        nameField.setLocation(20, 20);
        nameField.setSize(180, 30);
        add(nameField);

        cityField = new JEditField("City");
        cityField.setLocation(20, 70);
        cityField.setSize(180, 30);
        add(cityField);

        countryField = new JEditField("Country");
        countryField.setLocation(20, 120);
        countryField.setSize(180, 30);
        add(countryField);

        addressField = new JEditField("Address");
        addressField.setLocation(20, 170);
        addressField.setSize(180, 30);
        add(addressField);

        // de buttons:
        AddButtonListener lis = new AddButtonListener();
        registerButton = new JButton("Register");
        registerButton.setLocation(400, 180);
        registerButton.setSize(90, 60);
        registerButton.addActionListener(lis);
        add(registerButton);

        isBusinessLabel = new JLabel("Company?");
        isBusinessLabel.setLocation(230, 20);
        isBusinessLabel.setSize(100, 30);
        add(isBusinessLabel);

        yesRadio = new JRadioButton("Yes");
        yesRadio.setLocation(300, 20);
        yesRadio.setSize(100, 30);
        yesRadio.addActionListener(switchLis);
        add(yesRadio);

        noRadio = new JRadioButton("No");
        noRadio.setLocation(300, 50);
        noRadio.setSize(100, 30);
        noRadio.addActionListener(switchLis);
        add(noRadio);

        ButtonGroup group = new ButtonGroup();
        group.add(yesRadio);
        group.add(noRadio);

        showButton = new JButton("Add Studies");
        showButton.setLocation(180, 220);
        showButton.setSize(120, 30);
        add(showButton);
        showButton.addActionListener(studyLis);
        showButton.setVisible(false);

        Studiesbox = new JComboBox();
        Studiesbox.setLocation(20, 220);
        Studiesbox.setSize(150, 30);
        Studiesbox.setVisible(false);

        add(Studiesbox);

    }

    public static void addStudiesbox(String text) {
        Studiesbox.addItem(text);
    }

    private class RegisterStudyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            RegisterStudyDialog dlg = new RegisterStudyDialog((JFrame) getOwner(), RegisterStudyDialog.StudyType.Study);
            dlg.setVisible(true);

        }
    }

    private class SwitchInstituteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (yesRadio.isSelected()) {
                showButton.setVisible(false);
                Studiesbox.setVisible(false);
            }
            else {
               showButton.setVisible(true);
                Studiesbox.setVisible(true); 
    }
        }
    }

    public void clearField() {
        cityField.setText("");
        nameField.setText("");
        countryField.setText("");
        addressField.setText("");
    }

    private class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            String city, name, country, address;
            int is_business;
            city = cityField.getText();
            if (city.isEmpty() || city.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(RegisterInstituteFrame.this,
                        "city cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                city = null;
            }
            name = nameField.getText();
            if (name.isEmpty() || name.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(RegisterInstituteFrame.this,
                        "name cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                name = null;
            }
            country = countryField.getText();
            if (country.isEmpty() || country.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(RegisterInstituteFrame.this,
                        "country cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                country = null;
            }
            address = addressField.getText();
            if (address.isEmpty()) {

                address = null;
            }

            if (event.getSource() == registerButton) {
                if (city == null || address == null || name == null || country == null) {
                    JOptionPane.showMessageDialog(RegisterInstituteFrame.this, "Registering of institute failed", "Error", JOptionPane.WARNING_MESSAGE);
                    
                } else {
                    int register = JOptionPane.showOptionDialog(RegisterInstituteFrame.this, "Institute has been Registerd", "Registerd", JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (register == JOptionPane.OK_OPTION) {

                        is_business = yesRadio.isSelected() ? 0 : 1;
                        org_idreturn = Institute.insertInstitute(city, name, country, address, is_business);

                        for (int i = 0; i < RegisterStudyDialog.codearray.size(); i++) {
                            Study.insertStudy(RegisterStudyDialog.codearray.get(i), RegisterStudyDialog.emailarray.get(i), RegisterStudyDialog.numberarray.get(i), org_idreturn);
                        }
                        RegisterStudyDialog.codearray.clear();
                        clearField();
                        setVisible(false);
                        dispose();

                    }
                }
            }
        }
    }
}
