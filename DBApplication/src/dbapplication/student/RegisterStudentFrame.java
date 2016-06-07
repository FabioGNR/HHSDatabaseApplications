package dbapplication.student;

import dbapplication.JEditField;
import dbapplication.institute.Institute;
import dbapplication.institute.SelectInstituteDialog;

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

    private JTextField nameField, genderField, emailField, cityField;
    private JTextField addressField, studyField, uniField, studentidField;

    private JButton addButton, selectUniButton;
    private String selectedInstCode = "";
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
        studentidField.setBounds(20, 70, 180, 30);
        studentidField.setLocation(20, 70);
        studentidField.setSize(180, 30);
        add(studentidField);

        nameField = new JEditField("Name");
        nameField.setBounds(20, 120, 180, 30);
        add(nameField);

        genderField = new JEditField("Gender");
        genderField.setBounds(20, 170, 180, 30);
        add(genderField);

        emailField = new JEditField("Email");
        emailField.setBounds(20, 220, 180, 30);
        add(emailField);

        cityField = new JEditField("City");
        cityField.setBounds(20, 270, 180, 30);
        add(cityField);
        cityField.setVisible(false);

        addressField = new JEditField("Address");
        addressField.setBounds(20, 320, 180, 30);
        add(addressField);
        addressField.setVisible(false);

        uniField = new JEditField("University");
        uniField.setBounds(20, 370, 180, 30);
        uniField.setEnabled(false);
        add(uniField);
        uniField.setVisible(false);
        
        selectUniButton = new JButton("...");
        selectUniButton.setBounds(200, 370, 40, 30);
        add(selectUniButton);
        selectUniButton.addActionListener(new SelectUniversityListener());
        selectUniButton.setVisible(false);

        studyField = new JEditField("Study");
        studyField.setBounds(20, 270, 180, 30);
        add(studyField);
        studyField.setVisible(false);

        // de buttons:
        AddButtonListener addLis = new AddButtonListener();
        addButton = new JButton("Register");
        addButton.setBounds(400, 180, 90, 60);
        addButton.addActionListener(addLis);
        add(addButton);

        SwitchStudentListener switchLis = new SwitchStudentListener();
        hhs_studentButton = new JRadioButton();
        hhs_studentButton.setBounds(300, 20, 100, 30);
        hhs_studentButton.setText("hhs student");
        hhs_studentButton.addActionListener(switchLis);
        add(hhs_studentButton);

        ex_studentButton = new JRadioButton();
        ex_studentButton.setBounds(300, 50, 100, 30);
        ex_studentButton.setText("exchange student");
        ex_studentButton.addActionListener(switchLis);
        add(ex_studentButton);

        ButtonGroup group = new ButtonGroup();
        group.add(hhs_studentButton);
        group.add(ex_studentButton);
    }
    
    private class SelectUniversityListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SelectInstituteDialog dlg = new SelectInstituteDialog((JFrame)getOwner(), SelectInstituteDialog.InstituteType.University);
            dlg.setVisible(true);
            // pauses until dialog is closed
            Institute institute = dlg.getSelectedInstitute();
            if(institute != null)
                uniField.setText(institute.getName());
        }     
    }
    
    private class SwitchStudentListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean showExchangeFields = ex_studentButton.isSelected();
            cityField.setVisible(showExchangeFields);
            addressField.setVisible(showExchangeFields);
            studyField.setVisible(!showExchangeFields);
            uniField.setVisible(showExchangeFields);
            selectUniButton.setVisible(showExchangeFields);
        }       
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {    
            String name = nameField.getText();
            if (name.isEmpty()) {
                name = null;
            }
            String gender = genderField.getText();
            if (gender.isEmpty()) {
                gender = null;
            }
            String email = emailField.getText();
            if (email.isEmpty()) {
                email = null;
            }
            String student_id = studentidField.getText();
            if (student_id.isEmpty()) {
                student_id = null;
            }
            if(hhs_studentButton.isSelected()) {
                String hhs_study = studyField.getText();
                if (hhs_study.isEmpty()) {
                    hhs_study = null;
                }
                HHSStudent.insertNewHHSStudent(student_id, name, gender, email, hhs_study);
            }
            else {               
                String city = cityField.getText();
                if (city.isEmpty()) {
                    city = null;
                }
                String address = addressField.getText();
                if (address.isEmpty()) {
                    address = null;
                }
                String university = uniField.getText();
                if (university.isEmpty()) {
                    university = null;
                }
                ExchangeStudent.insertNewExchangeStudent(student_id, name, gender, email, city, address, university);
            }      
        }
    }
}
