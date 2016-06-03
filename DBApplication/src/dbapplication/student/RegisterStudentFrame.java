package dbapplication.student;

import dbapplication.JEditField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author omari_000
 */
public class RegisterStudentFrame extends JDialog {

    private JTextField nameField;
    private JTextField genderField;
    private JTextField emailField;
    private JTextField cityField;
    private JTextField addressField;
    private JTextField studyField;
    private JTextField uniField;
    private JTextField studentidField;

    private JButton addButton;
    private JRadioButton hhs_studentButton;
    private JRadioButton ex_studentButton;

    public RegisterStudentFrame(JFrame owner) {
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

        studentidField = new JEditField("Student ID");
        studentidField.setLocation(20, 70);
        studentidField.setSize(180, 30);
        add(studentidField);

        nameField = new JEditField("Name");
        nameField.setLocation(20, 120);
        nameField.setSize(180, 30);
        add(nameField);

        genderField = new JEditField("Gender");
        genderField.setLocation(20, 170);
        genderField.setSize(180, 30);
        add(genderField);

        emailField = new JEditField("Email");
        emailField.setLocation(20, 220);
        emailField.setSize(180, 30);
        add(emailField);

        cityField = new JEditField("City");
        cityField.setLocation(20, 270);
        cityField.setSize(180, 30);
        add(cityField);
        cityField.setVisible(false);

        addressField = new JEditField("Address");
        addressField.setLocation(20, 320);
        addressField.setSize(180, 30);
        add(addressField);
        addressField.setVisible(false);

        uniField = new JEditField("University");
        uniField.setLocation(20, 370);
        uniField.setSize(180, 30);
        add(uniField);
        uniField.setVisible(false);

        studyField = new JEditField("Study");
        studyField.setLocation(20, 270);
        studyField.setSize(180, 30);
        add(studyField);
        studyField.setVisible(false);

        // de buttons:
        RegisterStudentFrame.AddButtonListener lis = new RegisterStudentFrame.AddButtonListener();
        addButton = new JButton("Register");
        addButton.setLocation(400, 180);
        addButton.setSize(90, 60);
        addButton.addActionListener(lis);
        add(addButton);

        hhs_studentButton = new JRadioButton();
        hhs_studentButton.setLocation(300, 20);
        hhs_studentButton.setSize(100, 30);
        hhs_studentButton.setText("hhs student");
        hhs_studentButton.addActionListener(lis);
        add(hhs_studentButton);

        ex_studentButton = new JRadioButton();
        ex_studentButton.setLocation(300, 50);
        ex_studentButton.setSize(100, 30);
        ex_studentButton.setText("exchange student");
        ex_studentButton.addActionListener(lis);
        add(ex_studentButton);

        ButtonGroup group = new ButtonGroup();
        group.add(hhs_studentButton);
        group.add(ex_studentButton);

    }

    private class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            String name, gender, email, city, address, hhs_study, university, student_id;

            name = nameField.getText();
            if (name.isEmpty()) {
                name = null;
            }
            gender = genderField.getText();
            if (gender.isEmpty()) {
                gender = null;
            }
            email = emailField.getText();
            if (email.isEmpty()) {
                email = null;
            }
            city = cityField.getText();
            if (city.isEmpty()) {
                city = null;
            }
            address = addressField.getText();
            if (address.isEmpty()) {
                address = null;
            }
            hhs_study = studyField.getText();
            if (hhs_study.isEmpty()) {
                hhs_study = null;
            }

            university = uniField.getText();
            if (university.isEmpty()) {
                university = null;
            }

            student_id = studentidField.getText();
            if (student_id.isEmpty()) {
                student_id = null;
            }

            if (event.getSource() == hhs_studentButton) {

                cityField.setVisible(false);
                addressField.setVisible(false);
                studyField.setVisible(true);
                System.out.println("hhs student");
                Student.insertHHS_Student(student_id, name, gender, email, hhs_study);

            }
            if (event.getSource() == ex_studentButton) {
                studyField.setVisible(false);
                cityField.setVisible(true);
                addressField.setVisible(true);
                System.out.println("exchangestudent");
                Student.insertExchange_Student(student_id, name, gender, email, city, address, university);
            }
            
            if(event.getSource() == addButton){
                
                
            }

        }

    }
}
