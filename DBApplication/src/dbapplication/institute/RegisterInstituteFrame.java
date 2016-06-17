package dbapplication.institute;

import dbapplication.JEditField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
    
    String boxList[] = { "Company", "University" };
    private JComboBox box;
    private static JComboBox studiesBox;

    int org_idreturn;

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
        
        box = new JComboBox(boxList);
        box.setLocation(250, 20);
        box.setSize(150, 30);
        box.addActionListener(switchLis);
        add(box);

        showButton = new JButton("Add Studies");
        showButton.setLocation(180, 220);
        showButton.setSize(120, 30);
        add(showButton);
        showButton.addActionListener(studyLis);
        showButton.setVisible(false);

        studiesBox = new JComboBox();
        studiesBox.setLocation(20, 220);
        studiesBox.setSize(150, 30);
        studiesBox.setVisible(false);

        add(studiesBox);

    }

    private class RegisterStudyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            RegisterStudyDialog dlg = new RegisterStudyDialog((JFrame) getOwner());
            dlg.setVisible(true);
            Study newStudy = dlg.getNewStudy();
            if(newStudy != null) {
                studiesBox.addItem(newStudy);
            }
        }
    }

    private class SwitchInstituteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (box.getSelectedIndex() == 0) {
                showButton.setVisible(false);
                studiesBox.setVisible(false);
            }
            else {
               showButton.setVisible(true);
               studiesBox.setVisible(true); 
    }
        }
    }

    public void clearField() {
        cityField.setText("");
        nameField.setText("");
        countryField.setText("");
        addressField.setText("");
        box.setSelectedIndex(0);
        studiesBox.removeAllItems();
    }

    private class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            String city, name, country, address;
            int is_business;
            city = cityField.getText();
            if (city.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterInstituteFrame.this,
                        "city cannot be a Empty", "Incorrect input", JOptionPane.WARNING_MESSAGE);
             
            }
            if (city.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(RegisterInstituteFrame.this,
                        "city cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                city = null;
            }
            name = nameField.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterInstituteFrame.this,
                        "name cannot be a Empty", "Incorrect input", JOptionPane.WARNING_MESSAGE);
             
            }
            if (name.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(RegisterInstituteFrame.this,
                        "name cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                name = null;
            }
            country = countryField.getText();
            if (country.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterInstituteFrame.this,
                        "country cannot be a Empty", "Incorrect input", JOptionPane.WARNING_MESSAGE);
              
            }
            if (country.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(RegisterInstituteFrame.this,
                        "country cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                country = null;
            }
            address = addressField.getText();
            if (address.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterInstituteFrame.this,
                        "Adress cannot be a Empty", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                address = null;
            }

            if (event.getSource() == registerButton) {
                if (city == null || address == null || name == null || country == null) {
                    JOptionPane.showMessageDialog(RegisterInstituteFrame.this, "Registering of institute failed", "Error", JOptionPane.WARNING_MESSAGE);
                    
                } else {
                    int register = JOptionPane.showOptionDialog(RegisterInstituteFrame.this, "Institute has been Registerd", "Registerd", JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (register == JOptionPane.OK_OPTION) {
                        
                        if (box.getSelectedIndex() == 0) {
                            is_business = 1;
                        }else 
                            is_business = 0;
                        
                        int org_id = Institute.insertInstitute(city, name, country, address, is_business);

                        for(int i = 0; i < studiesBox.getItemCount(); i++) {
                            Study study = (Study)studiesBox.getItemAt(i);
                            Study.insertStudy(study.getCode(), 
                                    study.getEmail(), study.getPhoneNumber(), 
                                    org_id);
                        }
                        clearField();
                        setVisible(false);
                        dispose();

                    }
                }
            }
        }
    }

        public int getOrg_idreturn() {
        return org_idreturn;
    }
}
