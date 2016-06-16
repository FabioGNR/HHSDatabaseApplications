package dbapplication.student;

import dbapplication.DatabaseTableModel;
import dbapplication.JEditField;
import dbapplication.JSearchField;
import dbapplication.SearchFilter;
import dbapplication.institute.Institute;
import dbapplication.institute.SelectInstituteDialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author omari_000
 */
public class SearchStudentFrame extends JDialog {

    private JTextField searchField, uniField;
    private JComboBox searchConditionCombo, searchTypeCombo, hhsStudyCombo;
    private JTable resultTable;
    private JScrollPane resultPanel;
    private DatabaseTableModel<Student> resultModel;
    private JButton selectFilterButton, selectUniButton, phoneButton;
    private Institute selectedFilterInstitute;
    private int exchangeUniID = -1;

    private JEditField nameField, emailField, cityField, addressField;
    private JRadioButton genderFBox, genderMBox;
    private JButton saveButton, deleteButton, searchButton, enrollmentsButton;

    private JLabel selectedStudentLabel;
    private Student selectedStudent = null;

    public SearchStudentFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(700, 600);
        setTitle("Search Students");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
    }

    private void createComponents() {
        searchField = new JSearchField();
        searchField.setBounds(20, 20, 180, 30);
        add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(210, 20, 70, 30);
        searchButton.addActionListener(new SearchListener());
        add(searchButton);

        searchTypeCombo = new JComboBox(new Student.StudentType[]{
            Student.StudentType.Exchange, Student.StudentType.HHS
        });
        searchTypeCombo.setBounds(290, 20, 100, 30);
        searchTypeCombo.addActionListener(new SelectTypeListener());
        add(searchTypeCombo);

        searchConditionCombo = new JComboBox(new SearchFilter[]{
            new SearchFilter("Student ID", "student_id"),
            new SearchFilter("Name", "name"),
            new SearchFilter("Email", "email")});
        searchConditionCombo.setBounds(400, 20, 100, 30);
        add(searchConditionCombo);

        resultTable = new JTable();
        resultModel = new DatabaseTableModel<>(new String[] { "Student ID", "Name", "Gender", "Email" });
        resultTable.setModel(resultModel);
        resultTable.setPreferredScrollableViewportSize(new Dimension(400, 500));
        resultTable.setFillsViewportHeight(true);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(new SelectionListener());
        resultPanel = new JScrollPane(resultTable);
        resultPanel.setBounds(20, 60, 420, 500);
        add(resultPanel);

        selectedStudentLabel = new JLabel("Selected student:");
        selectedStudentLabel.setBounds(450, 60, 150, 30);
        add(selectedStudentLabel);

        selectFilterButton = new JButton("Select institute filter");
        selectFilterButton.setBounds(510, 20, 150, 30);
        add(selectFilterButton);
        selectFilterButton.addActionListener(new SelectFilterListener());

        nameField = new JEditField("Name");
        nameField.setBounds(450, 100, 150, 30);
        add(nameField);

        emailField = new JEditField("Email");
        emailField.setBounds(450, 140, 150, 30);
        add(emailField);

        genderFBox = new JRadioButton("Female");
        genderFBox.setBounds(450, 180, 75, 30);
        genderMBox = new JRadioButton("Male");
        genderMBox.setBounds(525, 180, 75, 30);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(genderFBox);
        genderGroup.add(genderMBox);
        add(genderFBox);
        add(genderMBox);
        
        phoneButton = new JButton("Phone numbers");
        phoneButton.setBounds(450, 220, 130, 30);
        phoneButton.addActionListener(new PhoneNumbersListener());
        add(phoneButton);

        enrollmentsButton = new JButton("Enrollments");
        enrollmentsButton.setBounds(450, 260, 130, 30);
        add(enrollmentsButton);
        
        hhsStudyCombo = new JComboBox(HHSStudent.LocalStudy.values());
        hhsStudyCombo.setBounds(450, 300, 130, 30);
        add(hhsStudyCombo);
        hhsStudyCombo.setVisible(false);

        cityField = new JEditField("City");
        cityField.setBounds(450, 300, 150, 30);
        add(cityField);
        cityField.setVisible(false);

        addressField = new JEditField("Address");
        addressField.setBounds(450, 340, 150, 30);
        add(addressField);
        addressField.setVisible(false);

        uniField = new JEditField("University");
        uniField.setBounds(450, 380, 120, 30);
        add(uniField);
        uniField.setEnabled(false);
        uniField.setVisible(false);

        selectUniButton = new JButton("...");
        selectUniButton.setBounds(570, 380, 30, 30);
        add(selectUniButton);
        selectUniButton.addActionListener(new SelectUniversityListener());
        selectUniButton.setVisible(false);

        saveButton = new JButton("Save");
        saveButton.setBounds(450, 490, 75, 30);
        saveButton.addActionListener(new StudentEditListener());
        add(saveButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(525, 490, 75, 30);
        deleteButton.addActionListener(new StudentEditListener());
        add(deleteButton);

        toggleFields();
    }

    private void toggleFields() {
        // show the right fields depending on the selected student type
        int selectedTypeIndex = searchTypeCombo.getSelectedIndex();
        Student.StudentType type = (Student.StudentType) searchTypeCombo.getItemAt(selectedTypeIndex);
        boolean exchangeSelected = type == Student.StudentType.Exchange;
        cityField.setVisible(exchangeSelected);
        addressField.setVisible(exchangeSelected);
        uniField.setVisible(exchangeSelected);
        selectUniButton.setVisible(exchangeSelected);
        hhsStudyCombo.setVisible(!exchangeSelected);
    }

    private void search(String filter, String conditionColumn, Student.StudentType type) {
        // execute search based on user input
        ArrayList<Student> students;
        if (type == Student.StudentType.Exchange) {
            students = ExchangeStudent.searchStudents(filter, conditionColumn);
        } else {
            students = HHSStudent.searchStudents(filter, conditionColumn);
        }
        resultModel.setItems(students);
        setSelectedStudent(null);
    }
    
    private void setSelectedStudent(Student student) {
        selectedStudent = student;
        if(student != null) {
            selectedStudentLabel.setText("Selected student: "+student.getStudentid());
        }
        else
            selectedStudentLabel.setText("Selected student: ");
    }
    
    private void saveSelectedStudent() {
        if(selectedStudent == null) return;
        int selectedTypeIndex = searchTypeCombo.getSelectedIndex();
        Student.StudentType type = (Student.StudentType) searchTypeCombo.getItemAt(selectedTypeIndex);
        // set new student properties
        if(type == Student.StudentType.Exchange) {
            if(exchangeUniID == -1) {
                JOptionPane.showMessageDialog(this, "Select a university", 
                        "Missing university", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ExchangeStudent student = (ExchangeStudent)selectedStudent;
            student.setAddress(addressField.getText());
            student.setCity(cityField.getText());
            student.setUniID(exchangeUniID);
            student.setUniName(uniField.getText());
        }
        else if(type == Student.StudentType.HHS) {
            HHSStudent student = (HHSStudent)selectedStudent;
            student.setLocalStudy((HHSStudent.LocalStudy)hhsStudyCombo.getSelectedItem());
        }
        selectedStudent.setEmail(emailField.getText());
        selectedStudent.setGender(genderFBox.isSelected() 
                ? Student.Gender.Female : Student.Gender.Male);
        selectedStudent.setName(nameField.getText());
        // attempt to save
        if(!selectedStudent.save()) {
            JOptionPane.showMessageDialog(this, "Error saving student", 
                    "Could not save student", JOptionPane.ERROR_MESSAGE);
        }
        // refresh result table
        selectedStudent.refreshCellData();
        resultModel.fireTableDataChanged();
    }
    
    class EnrollmentsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(selectedStudent == null) return;
            EnrollmentFrame frame = new EnrollmentFrame(
                (JFrame)SearchStudentFrame.this.getOwner(), new ArrayList<>(), 
                    selectedStudent);
            frame.setVisible(true);
        }
        
    }
    
    class PhoneNumbersListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(selectedStudent == null) return;
            PhoneFrame frame = new PhoneFrame((JFrame)SearchStudentFrame.this.getOwner(), selectedStudent.getPhoneNumbers());
            frame.setVisible(true);
            ArrayList<PhoneNumber> numbers = frame.getPhoneNumbers();
            
        }    
    }

    class SelectTypeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            toggleFields();
            setSelectedStudent(null);
            resultModel.clear();
            
        }
    }

    class SelectFilterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SearchFilterDialog dlg = new SearchFilterDialog(
                    (JFrame)SearchStudentFrame.this.getOwner());
            dlg.setVisible(true);
            selectedFilterInstitute = dlg.getSelectedInstitute();
        }
    }

    class StudentEditListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStudent == null) {
                return;
            }
            if (e.getSource() == saveButton) {
                saveSelectedStudent();
            } else if (e.getSource() == deleteButton) {
                // show confirm dialog and confirm that the user choose "OK"
                int choice = JOptionPane.showConfirmDialog(SearchStudentFrame.this, "Are you sure you want to delete this student?",
                        "Delete student", JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    selectedStudent.delete();
                    resultModel.clear();
                    selectedStudent = null;
                }
            }
        }
    }

    private class SelectUniversityListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SelectInstituteDialog dlg = new SelectInstituteDialog((JFrame) getOwner(), SelectInstituteDialog.InstituteType.University);
            dlg.setVisible(true);
            // pauses until dialog is closed
            Institute institute = dlg.getSelectedInstitute();
            if (institute != null) {
                uniField.setText(institute.getName());
                exchangeUniID = institute.getOrgid();
            }
        }
    }

    class SelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            // get the corresponding 'Student' object 
            // and update the fields to reflect it
            int selectedRow = resultTable.getSelectedRow();
            if (selectedRow < 0) {
                setSelectedStudent(null);
                return; // selection cleared
            }
            setSelectedStudent(resultModel.get(selectedRow));
            nameField.setText(selectedStudent.getName());
            emailField.setText(selectedStudent.getEmail());
            JRadioButton correctGenderBox;
            if (selectedStudent.getGender() == Student.Gender.Female) {
                correctGenderBox = genderFBox;
            } else {
                correctGenderBox = genderMBox;
            }
            correctGenderBox.setSelected(true);
            int selectedTypeIndex = searchTypeCombo.getSelectedIndex();
            Student.StudentType type = (Student.StudentType) searchTypeCombo.getItemAt(selectedTypeIndex);
            if (type == Student.StudentType.Exchange) {
                cityField.setText(((ExchangeStudent) selectedStudent).getCity());
                addressField.setText(((ExchangeStudent) selectedStudent).getAddress());
                exchangeUniID = ((ExchangeStudent) selectedStudent).getUniID();
                uniField.setText(((ExchangeStudent) selectedStudent).getUniName());
            } else {
                hhsStudyCombo.setSelectedItem(((HHSStudent) selectedStudent).getLocalStudy());
            }
        }
    }

    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedTypeIndex = searchTypeCombo.getSelectedIndex();
            Student.StudentType type = (Student.StudentType) searchTypeCombo.getItemAt(selectedTypeIndex);
            int selectedConditionIndex = searchConditionCombo.getSelectedIndex();
            SearchFilter selectedFilter = (SearchFilter) searchConditionCombo.getItemAt(selectedConditionIndex);
            search(searchField.getText(), selectedFilter.getColumnName(), type);
        }
    }
}
