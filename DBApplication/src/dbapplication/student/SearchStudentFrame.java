 package dbapplication.student;

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
public class SearchStudentFrame extends JDialog{
    private JTextField searchField, uniField;
    private JComboBox searchConditionCombo, searchTypeCombo, hhsStudyCombo;
    private JTable resultTable;
    private JScrollPane resultPanel;
    private StudentTableModel resultModel;
    private JButton selectFilterButton, selectUniButton;
    private Institute selectedFilterInstitute;
    private String exchangeUniID;
    
    private JEditField nameField, emailField, cityField, addressField;
    private JRadioButton genderFBox, genderMBox;
    private JButton saveButton, deleteButton, searchButton;
    
    private JLabel selectedStudentLabel;
    private Student selectedStudent = null;
    
    public SearchStudentFrame(JFrame owner) {
        super(owner, true);
        setupFrame();     
        createComponents();
    }
    
    private void setupFrame() {
        setSize(700,600);
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
        
        searchTypeCombo = new JComboBox(new Student.StudentType[] {
            Student.StudentType.Exchange,  Student.StudentType.HHS
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
        resultTable.setBounds(0, 0, 400, 500);
        resultModel = new StudentTableModel();
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
        
        hhsStudyCombo = new JComboBox(HHSStudent.LocalStudy.values());
        hhsStudyCombo.setBounds(450, 220, 130, 30);
        add(hhsStudyCombo);
        hhsStudyCombo.setVisible(false);
        
        cityField = new JEditField("City");
        cityField.setBounds(450, 220, 150, 30);
        add(cityField);
        cityField.setVisible(false);
        
        addressField = new JEditField("Address");
        addressField.setBounds(450, 260, 150, 30);
        add(addressField);
        addressField.setVisible(false);
        
        uniField = new JEditField("University");
        uniField.setBounds(450, 300, 120, 30);
        add(uniField);
        uniField.setEnabled(false);
        uniField.setVisible(false);
        
        selectUniButton = new JButton("...");
        selectUniButton.setBounds(570, 300, 30, 30);
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
            int selectedTypeIndex = searchTypeCombo.getSelectedIndex();
            Student.StudentType type = (Student.StudentType)searchTypeCombo.getItemAt(selectedTypeIndex);
            boolean exchangeSelected = type == Student.StudentType.Exchange;
            cityField.setVisible(exchangeSelected);
            addressField.setVisible(exchangeSelected);
            uniField.setVisible(exchangeSelected);
            selectUniButton.setVisible(exchangeSelected);
            hhsStudyCombo.setVisible(!exchangeSelected);        
    }
    
    private void search(String filter, String conditionColumn, Student.StudentType type) {
        ArrayList<Student> students;
        if(type == Student.StudentType.Exchange) {
            students = ExchangeStudent.searchStudents(filter, conditionColumn);
        }
        else {
            students = HHSStudent.searchStudents(filter, conditionColumn);
        }
        resultModel.setResults(students);
        selectedStudent = null;
    }
    
    class SelectTypeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            toggleFields();
            selectedStudent = null;
            resultModel.clear();
        }     
    }
    
    class SelectFilterListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SearchFilterDialog dlg = new SearchFilterDialog();
            dlg.setVisible(true);
            selectedFilterInstitute = dlg.getSelectedInstitute();
        }
        
    }
    
    class StudentEditListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(selectedStudent == null) return;
            if(e.getSource() == saveButton) {
                
            }
            else if(e.getSource() == deleteButton) {
                // show confirm dialog and confirm that the user choose "OK"
                int choice = JOptionPane.showConfirmDialog(SearchStudentFrame.this, "Are you sure you want to delete this student?", 
                    "Delete student", JOptionPane.OK_CANCEL_OPTION);
                if(choice == JOptionPane.OK_OPTION) {
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
            SelectInstituteDialog dlg = new SelectInstituteDialog((JFrame)getOwner(), SelectInstituteDialog.InstituteType.University);
            dlg.setVisible(true);
            // pauses until dialog is closed
            Institute institute = dlg.getSelectedInstitute();
            if(institute != null)
            {
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
            if(selectedRow < 0) return; // selection cleared
            selectedStudent = resultModel.getStudentAt(selectedRow);
            selectedStudentLabel.setText(
                    "Selected student: "+selectedStudent.getStudentid());
            nameField.setText(selectedStudent.getName());
            emailField.setText(selectedStudent.getEmail());
            JRadioButton correctGenderBox;
            if(selectedStudent.getGender().equals("f"))
                correctGenderBox = genderFBox;
            else 
                correctGenderBox = genderMBox;
            correctGenderBox.setSelected(true);
            int selectedTypeIndex = searchTypeCombo.getSelectedIndex();
            Student.StudentType type = (Student.StudentType)searchTypeCombo.getItemAt(selectedTypeIndex);
            if(type == Student.StudentType.Exchange) {
                cityField.setText(((ExchangeStudent)selectedStudent).getCity());
                addressField.setText(((ExchangeStudent)selectedStudent).getAddress());
                uniField.setText(((ExchangeStudent)selectedStudent).getUniName());
            }
            else {
                hhsStudyCombo.setSelectedItem(((HHSStudent)selectedStudent).getLocalStudy());
            }
        }       
    }
    
    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedTypeIndex = searchTypeCombo.getSelectedIndex();
            Student.StudentType type = (Student.StudentType)searchTypeCombo.getItemAt(selectedTypeIndex);
            int selectedConditionIndex = searchConditionCombo.getSelectedIndex();
            SearchFilter selectedFilter = (SearchFilter)searchConditionCombo.getItemAt(selectedConditionIndex);
            search(searchField.getText(), selectedFilter.getColumnName(), type);
        }      
    }
}
