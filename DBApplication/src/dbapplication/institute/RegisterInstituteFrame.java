package dbapplication.institute;

import dbapplication.ChoiceDialog;
import dbapplication.ChoiceListener;
import dbapplication.DBConnection;
import dbapplication.MainFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private JTextField orgField;
    private JTextField cityField;
    private JTextField countryField;
    private JTextField addressField;

    private JTextField studyField;
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
        setSize(510, 510);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Register Institute");
    }

    private void createComponents() {
        nameField = new JTextField();
        nameField.setLocation(20, 20);
        nameField.setSize(180, 30);
        nameField.setText("Name");
        add(nameField);

        orgField = new JTextField();
        orgField.setLocation(20, 70);
        orgField.setSize(180, 30);
        orgField.setText("Org_ID");
        add(orgField);

        cityField = new JTextField();
        cityField.setLocation(20, 120);
        cityField.setSize(180, 30);
        cityField.setText("City");
        add(cityField);

        countryField = new JTextField();
        countryField.setLocation(20, 170);
        countryField.setSize(180, 30);
        countryField.setText("Country");
        add(countryField);

        addressField = new JTextField();
        addressField.setLocation(20, 220);
        addressField.setSize(180, 30);
        addressField.setText("Address");
        add(addressField);

        studyField = new JTextField();
        studyField.setLocation(20, 270);
        studyField.setSize(180, 30);
        studyField.setText("Study");
        add(studyField);

        // de buttons:
        AddButtonListener lis = new AddButtonListener();
        addButton = new JButton();
        addButton.setLocation(400, 400);
        addButton.setSize(60, 60);
        addButton.addActionListener(lis);
        addButton.setText("Add");
        add(addButton);

        isBusinessLabel = new JLabel();
        isBusinessLabel.setLocation(230, 20);
        isBusinessLabel.setSize(100, 30);
        isBusinessLabel.setText("is_business");
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
            String org_id, city, name, country, address;
            int is_business;
            org_id = orgField.getText();
            city = cityField.getText();
            name = nameField.getText();
            country = countryField.getText();
            address = addressField.getText();
            // is_business is 1 als yes is geselecteerd, anders 0
            is_business = yesBox.isSelected() ? 1 : 0;

            add2Institute(org_id, city, 
                    name, country, address, is_business);
        }

        private void add2Institute(String org_id, String city, 
                String name, String country, String address, int is_business) {
            Connection connection = DBConnection.getConnection();
            try {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO institute "
                            + "(org_id, city, name, country, address, is_business) "
                            + "VALUES (?,?,?,?,?,?)");

                statement.setString(1, org_id);
                statement.setString(2, city);
                statement.setString(3, name);
                statement.setString(4, country);
                statement.setString(5, address);
                statement.setInt(6, is_business);
                statement.executeUpdate();
                System.out.println("preparedstatement werkt");
            } catch (SQLException error) {
                System.out.println("Error: " + error.getMessage());
                System.out.println("preparedstatement werkt niet");
            }

        }
    }
}
