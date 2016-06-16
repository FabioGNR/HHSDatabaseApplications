package dbapplication.student;

import dbapplication.JEditField;
import dbapplication.institute.Institute;
import dbapplication.institute.SelectInstituteDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 *
 * @author omari_000
 */
public class RegisterStudentFrame extends JDialog {

    private JTextField nameField, emailField, cityField;
    private JTextField addressField,  uniField, studentidField;
    private JComboBox studyBox;
    private ArrayList<PhoneNumber> numbers = new ArrayList<>();
    private JButton addButton, selectUniButton, phoneButton;
    private int selectedInstCode = -1;
    private JRadioButton hhs_studentButton, genderFBox, genderMBox;
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
        setTitle("Register Student");
        addWindowListener(new CloseListener());
    }

    private void createComponents() {

        studentidField = new JEditField("Student ID");
        studentidField.setBounds(20, 70, 180, 30);
        add(studentidField);

        nameField = new JEditField("Name");
        nameField.setBounds(20, 120, 180, 30);
        add(nameField);
        
        genderFBox = new JRadioButton("Female");
        genderFBox.setBounds(20, 170, 75, 30);
        genderMBox = new JRadioButton("Male");
        genderMBox.setBounds(95, 170, 75, 30);
        genderMBox.setSelected(true);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(genderFBox);
        genderGroup.add(genderMBox);
        add(genderFBox);
        add(genderMBox);

        emailField = new JEditField("Email");
        emailField.setBounds(20, 220, 180, 30);
        add(emailField);
        
        phoneButton = new JButton("Phone numbers");
        phoneButton.setBounds(20, 260, 130, 30);
        phoneButton.addActionListener(new PhoneNumbersListener());
        add(phoneButton);

        cityField = new JEditField("City");
        cityField.setBounds(20, 310, 180, 30);
        add(cityField);
        cityField.setVisible(false);

        addressField = new JEditField("Address");
        addressField.setBounds(20, 360, 180, 30);
        add(addressField);
        addressField.setVisible(false);

        uniField = new JEditField("University");
        uniField.setBounds(20, 410, 180, 30);
        uniField.setEnabled(false);
        add(uniField);
        uniField.setVisible(false);
        
        selectUniButton = new JButton("...");
        selectUniButton.setBounds(200, 410, 40, 30);
        add(selectUniButton);
        selectUniButton.addActionListener(new SelectUniversityListener());
        selectUniButton.setVisible(false);

        studyBox = new JComboBox(HHSStudent.LocalStudy.values());
        studyBox.setBounds(20, 310, 180, 30);
        add(studyBox);
        studyBox.setVisible(false);

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
        hhs_studentButton.addItemListener(switchLis);      
        add(hhs_studentButton);

        ex_studentButton = new JRadioButton();
        ex_studentButton.setBounds(300, 50, 100, 30);
        ex_studentButton.setText("exchange student");
        ex_studentButton.addItemListener(switchLis);
        add(ex_studentButton);
        hhs_studentButton.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(hhs_studentButton);
        group.add(ex_studentButton);
    }
    
    class CloseListener extends WindowAdapter {
        @Override
        public void windowClosed(WindowEvent e) {
            // reset all fields
            selectedInstCode = 01;
            uniField.setText("");
            nameField.setText("");
            emailField.setText("");
            cityField.setText("");
            addressField.setText("");
            studentidField.setText("");
            numbers = new ArrayList<>();
        }  
    }
    
    class PhoneNumbersListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            PhoneFrame frame = new PhoneFrame((JFrame)RegisterStudentFrame.this.getOwner(), 
                    numbers);
            frame.setVisible(true);
            // our arrayList will be changed so we don't need to reassign it 
        }    
    }
    
    private class SelectUniversityListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SelectInstituteDialog dlg = new SelectInstituteDialog((JFrame)getOwner(), SelectInstituteDialog.InstituteType.University);
            dlg.setVisible(true);
            // pauses until dialog is closed
            Institute institute = dlg.getSelectedInstitute();
            if(institute != null)
            {
                uniField.setText(institute.getName());
                selectedInstCode = institute.getOrgid();
            }
        }     
    }
    
    private class SwitchStudentListener implements ItemListener {    
        @Override
        public void itemStateChanged(ItemEvent e) {
            boolean showExchangeFields = ex_studentButton.isSelected();
            cityField.setVisible(showExchangeFields);
            addressField.setVisible(showExchangeFields);
            studyBox.setVisible(!showExchangeFields);
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
            Student.Gender gender = genderFBox.isSelected() 
                    ? Student.Gender.Female : Student.Gender.Male;
            String email = emailField.getText();
            if (email.isEmpty() || !email.contains("@")) {
                JOptionPane.showMessageDialog(RegisterStudentFrame.this, 
                        "Email must be in format of a@b.ccc", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                return;
            }           
            String sID = studentidField.getText();
            // if student id is not 8 digits show error message
            if (!sID.matches("^\\d{8,8}$")) {
                JOptionPane.showMessageDialog(RegisterStudentFrame.this, 
                        "Student ID must consist of 8 digits", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (numbers.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterStudentFrame.this, 
                        "Student needs to have at least one phone number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int student_id = Integer.parseInt(sID);
            boolean result;
            if(hhs_studentButton.isSelected()) {
                HHSStudent.LocalStudy hhs_study = (HHSStudent.LocalStudy)studyBox.getSelectedItem();
                result = HHSStudent.insertNewHHSStudent(student_id, name, 
                        gender, email, hhs_study, numbers);
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
                if(selectedInstCode == -1) {
                    JOptionPane.showMessageDialog(RegisterStudentFrame.this, 
                        "Please select the student's university", "Missing university", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                result = ExchangeStudent.insertNewExchangeStudent(student_id, 
                        name, gender, email, city, 
                        address, selectedInstCode, numbers);
            }      
            if(!result) {
                JOptionPane.showMessageDialog(RegisterStudentFrame.this, 
                    "Registering of student failed", "Error", JOptionPane.WARNING_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(RegisterStudentFrame.this, 
                    "Registering of student succeeded", "Success", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
}
