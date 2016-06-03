package dbapplication.institute;

import dbapplication.JEditField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JFrame;
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

    private JButton addButton;
    private JCheckBox yesBox;
    private JCheckBox noBox;
    private JLabel isBusinessLabel;

    public RegisterInstituteFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(510, 290);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Register Institute");
    }

    private void createComponents() {
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
        addButton = new JButton("Register");
        addButton.setLocation(400, 180);
        addButton.setSize(90, 60);
        addButton.addActionListener(lis);
        add(addButton);

        isBusinessLabel = new JLabel();
        isBusinessLabel.setLocation(230, 20);
        isBusinessLabel.setSize(100, 30);
        isBusinessLabel.setText("Business?");
        add(isBusinessLabel);

        yesBox = new JCheckBox();
        yesBox.setLocation(300, 20);
        yesBox.setSize(100, 30);
        yesBox.setText("Yes");
        add(yesBox);

        noBox = new JCheckBox();
        noBox.setLocation(300, 50);
        noBox.setSize(100, 30);
        noBox.setText("No");
        add(noBox);

        ButtonGroup group = new ButtonGroup();
        group.add(yesBox);
        group.add(noBox);

    }

    private class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            String city, name, country, address;
            int is_business;
            city = cityField.getText();
            if (city.isEmpty()) {
                city = null;
            }
            name = nameField.getText();
            if (name.isEmpty()) {
                name = null;
            }
            country = countryField.getText();
            if (country.isEmpty()) {
                country = null;
            }
            address = addressField.getText();
            if (address.isEmpty()) {
                address = null;
            }
            // is_business is 1 als yes is geselecteerd, anders 0
            is_business = yesBox.isSelected() ? 1 : 0;

            Institute.insertInstitute(city, name, country, address, is_business);
        }
        
        
    }
}
