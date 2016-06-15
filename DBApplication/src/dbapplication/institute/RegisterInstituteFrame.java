package dbapplication.institute;

import dbapplication.JEditField;
import dbapplication.program.SelectStudyDialog;
//import dbapplication.program.SelectStudyDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author omari_000
 */
public class RegisterInstituteFrame extends JDialog {

    private JTextField nameField;
    private JTextField cityField;
    private JTextField countryField;
    private JTextField addressField;

    private JButton registerButton;

    private JButton showButton;

    private JRadioButton yesRadio;
    private JRadioButton noRadio;
    private JLabel isBusinessLabel;

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

        isBusinessLabel = new JLabel();
        isBusinessLabel.setLocation(230, 20);
        isBusinessLabel.setSize(100, 30);
        isBusinessLabel.setText("Business?");
        add(isBusinessLabel);

        yesRadio = new JRadioButton();
        yesRadio.setLocation(300, 20);
        yesRadio.setSize(100, 30);
        yesRadio.setText("Yes");
        yesRadio.addActionListener(switchLis);
        add(yesRadio);

        noRadio = new JRadioButton();
        noRadio.setLocation(300, 50);
        noRadio.setSize(100, 30);
        noRadio.setText("No");
        noRadio.addActionListener(switchLis);
        add(noRadio);

        ButtonGroup group = new ButtonGroup();
        group.add(yesRadio);
        group.add(noRadio);
        
        showButton = new JButton("Show studies");
        showButton.setLocation(20, 220);
        showButton.setSize(150, 30);
        add(showButton);
        showButton.addActionListener(studyLis);
        showButton.setVisible(false);

    }

    private class RegisterStudyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            SelectStudyDialog dlg = new SelectStudyDialog((JFrame) getOwner(), SelectStudyDialog.ProgramType.studyProgram);
            dlg.setVisible(true);
            // pauses until dialog is closed
            Study study = dlg.getSelectedStudy();
            if (study != null) {
                //uniField.setText(getName());
                selectedStudy = study.getCode();

            }
        }
    }

    private class SwitchInstituteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean showExchangeFields = noRadio.isSelected();
            showButton.setVisible(showExchangeFields);
        }
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
            if (address.isEmpty() || address.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(RegisterInstituteFrame.this,
                        "addres cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                address = null;
            }

            if (event.getSource() == registerButton) {
                if (city == null || address == null || name == null || country == null) {
                    JOptionPane.showMessageDialog(RegisterInstituteFrame.this, "Registering of institute failed", "Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    int register = JOptionPane.showOptionDialog(RegisterInstituteFrame.this, "Institute has been Registerd", "Registerd", JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (register == JOptionPane.OK_OPTION) {

                        is_business = yesRadio.isSelected() ? 1 : 0;
                        Institute.insertInstitute(city, name, country, address, is_business);

                        JOptionPane.showMessageDialog(null, "BANANAAAAAAAAA");
                    }

                    // is_business is 1 als yes is geselecteerd, anders 0
                }

            }
        }
    }
}
