 package dbapplication.student;

import dbapplication.JEditField;
import dbapplication.JSearchField;
import dbapplication.SearchFilter;
import dbapplication.institute.Institute;
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
    private JTextField searchField;
    private JComboBox searchConditionCombo;
    private JTable resultTable;
    private JScrollPane resultPanel;
    private StudentTableModel resultModel;
    private JButton selectFilterButton;
    private Institute selectedFilterInstitute;
    
    private JEditField nameField, emailField;
    private JRadioButton genderFBox, genderMBox;
    private JButton saveButton, deleteButton, searchButton;
    
    private JLabel selectedStudentLabel;
    private Student selectedStudent = null;
    
    public SearchStudentFrame(JFrame owner) {
        super(owner, true);
        setupFrame();     
        createComponents();
        // fill JTable searching on empty filter
        search("", "name");
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
        searchButton.setBounds(220, 20, 90, 30);
        searchButton.addActionListener(new SearchListener());
        add(searchButton);
        
        searchConditionCombo = new JComboBox(new SearchFilter[]{
            new SearchFilter("Student ID", "student_id"), 
            new SearchFilter("Name", "name"), 
            new SearchFilter("Email", "email")});
        searchConditionCombo.setBounds(340, 20, 100, 30);
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
        selectFilterButton.setBounds(450, 20, 150, 30);
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
        
        saveButton = new JButton("Save");
        saveButton.setBounds(450, 490, 75, 30);
        saveButton.addActionListener(new StudentEditListener());
        add(saveButton);
        
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(525, 490, 75, 30);
        deleteButton.addActionListener(new StudentEditListener());
        add(deleteButton);
    }
    
    private void search(String filter, String conditionColumn) {
        ArrayList<Student> students = Student.searchStudents(filter, conditionColumn);
        resultModel.setResults(students);
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
                }
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
        }       
    }
    
    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = searchConditionCombo.getSelectedIndex();
            SearchFilter selectedFilter = (SearchFilter)searchConditionCombo.getItemAt(selectedIndex);
            search(searchField.getText(), selectedFilter.getColumnName());
        }      
    }
}
