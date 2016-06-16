package dbapplication.program;

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
public class SearchProgramFrame extends JDialog {

    private JTextField searchField, nameField;
    private JTextField instituteField, studyField;
    private JButton searchButton, saveButton, deleteButton;
    private JButton instituteButton, studyCodeButton;
    private JComboBox internshipConditionBox, studyProgramBox;
    private JComboBox programBox, maxCreditBox, studyTypeBox;
    private String[] studyType = {"Minor", "EPS", "Summer School"};
    private JTable resultTable;
    private JScrollPane resultPanel;
    private DatabaseTableModel<ExProgram> tableModel;
    private JLabel selectedProgramLabel, programCodeLabel;
    private ExProgram selectedProgram = null;
    private Internship internshipSelected = null;
    private StudyProgram studyProgramSelected = null;
    private String selectedInstitute;
    private int internshipID = -1;

    public SearchProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
        search("", "name");
    }

    private void setupFrame() {
        setTitle("Search Program");
        setSize(800, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
    
    @Override
    public void setVisible(boolean state) {
        super.setVisible(state);
        // reset fields here
        if(state)
            search("", "name");
    }

    private void createComponents() {
        searchField = new JSearchField();
        searchField.setBounds(20, 20, 180, 30);
        add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(220, 20, 90, 30);
        searchButton.addActionListener(new SearchListener());
        add(searchButton);

        programBox = new JComboBox(ExProgram.ProgramType.values());
        programBox.setBounds(330, 20, 100, 30);
        programBox.addActionListener(new SwitchProgramListener());
        add(programBox);

        internshipConditionBox = new JComboBox(new SearchFilter[]{
            new SearchFilter("Name", "name")});
        //misschien zoeken op credits}
        internshipConditionBox.setBounds(450, 20, 125, 30);
        add(internshipConditionBox);
        internshipConditionBox.setVisible(true);

        studyProgramBox = new JComboBox(new SearchFilter[]{
            new SearchFilter("Name", "name")
        });
        studyProgramBox.setBounds(450, 20, 125, 30);
        add(studyProgramBox);
        studyProgramBox.setVisible(false);

        resultTable = new JTable();
        resultTable.setBounds(0, 0, 555, 300);
        tableModel = new DatabaseTableModel<>(
                new String[]{"Name", "Credits"});

        resultTable.setModel(tableModel);
        resultTable.setPreferredScrollableViewportSize(new Dimension(400, 300));
        resultTable.setFillsViewportHeight(true);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(new SelectionListener());
        resultPanel = new JScrollPane(resultTable);
        resultPanel.setBounds(20, 60, 555, 300);
        add(resultPanel);

        selectedProgramLabel = new JLabel("Selected Program: ");
        selectedProgramLabel.setBounds(600, 20, 150, 30);
        add(selectedProgramLabel);

        nameField = new JEditField("Name");
        nameField.setBounds(600, 60, 150, 30);
        add(nameField);

        ActionListener instituteButtonListener = new SelectInstitute();
        instituteField = new JEditField("Organisation");
        instituteField.setBounds(600, 100, 100, 30);
        add(instituteField);
        instituteButton = new JButton("...");
        instituteButton.setBounds(710, 100, 40, 30);
        instituteButton.addActionListener(instituteButtonListener);
        add(instituteButton);

        studyTypeBox = new JComboBox(studyType);
        studyTypeBox.setBounds(600, 140, 150, 30);
        add(studyTypeBox);
        studyTypeBox.setVisible(false);

        studyField = new JEditField("Study Name");
        studyField.setBounds(600, 180, 100, 30);
        add(studyField);
        studyField.setVisible(false);

        // add listener
        studyCodeButton = new JButton("...");
        studyCodeButton.setBounds(710, 180, 40, 30);
        add(studyCodeButton);
        studyCodeButton.setVisible(false);

        saveButton = new JButton("Save");
        saveButton.setBounds(600, 335, 75, 30);
        saveButton.addActionListener(new saveChanges());
        add(saveButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(685, 335, 75, 30);
        deleteButton.addActionListener(new saveChanges());
        add(deleteButton);
    }
    
    private ExProgram.ProgramType getSelectedType() {
        return (ExProgram.ProgramType)programBox.getSelectedItem();
    }

    private void search(String filter, String conditionColumn) {
        ArrayList<ExProgram> program;
        ExProgram.ProgramType type = getSelectedType();
        if (type == ExProgram.ProgramType.Internship) {
            program = Internship.searchProgram(filter, conditionColumn);
        } else {
            program = StudyProgram.searchStudyProgram(filter, conditionColumn);
        }
        tableModel.setItems(program);
    }

    class saveChanges implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedProgram == null) {
                return;
            }
            ExProgram.ProgramType type = getSelectedType();
            if (type == ExProgram.ProgramType.Internship) {
                if (internshipID == -1) {
                    JOptionPane.showMessageDialog(SearchProgramFrame.this, "Please select an internship!",
                            "Internship missing", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // set the fields
                Internship program = (Internship) selectedProgram;
                program.setName(nameField.getName());
                program.setMaxCredit(maxCreditBox.getSelectedIndex());

            }

            if (e.getSource() == saveButton) {
                int save = JOptionPane.showConfirmDialog(SearchProgramFrame.this, "Save Program?",
                        "Save", JOptionPane.OK_CANCEL_OPTION);
                if (save == JOptionPane.OK_OPTION) {
                    // save depending on internship or study program
                    // make an update button first
                }
            } else if (e.getSource() == deleteButton) {
                int choice = JOptionPane.showConfirmDialog(SearchProgramFrame.this,
                        "Are you sure you want to delete this program?",
                        "Delete program", JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    selectedProgram.delete();
                }
            }
        }
    }

    class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = resultTable.getSelectedRow();
            if (selectedRow < 0) {
                return;
            }
            selectedProgram = tableModel.get(selectedRow);
            selectedProgramLabel.setText("Selected program: " + selectedProgram.getName());
            nameField.setText(selectedProgram.getName());
//            orgIDField.setText(); 
//            studyCodeField.setText();

        }
    }
    
    private void searchOnFilter() {
            SearchFilter selectedFilter;
            if (getSelectedType() == ExProgram.ProgramType.Internship) {
                int selectedIndex = internshipConditionBox.getSelectedIndex();
                selectedFilter = (SearchFilter) internshipConditionBox.getItemAt(selectedIndex);
            } else {
                int selectedIndex = studyProgramBox.getSelectedIndex();
                selectedFilter = (SearchFilter) studyProgramBox.getItemAt(selectedIndex);
            }
            search(searchField.getText(), selectedFilter.getColumnName());        
    }

    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            searchOnFilter();
        }
    }

    class SwitchProgramListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean internshipSelected = 
                    getSelectedType() == ExProgram.ProgramType.Internship;
            internshipConditionBox.setVisible(internshipSelected);
            studyProgramBox.setVisible(!internshipSelected);
            // maakt het uit bij het updaten?
//                instituteField.setVisible(!internshipSelected);
//                instituteButton.setVisible(!internshipSelected);
            studyTypeBox.setVisible(!internshipSelected);
            studyField.setVisible(!internshipSelected);
            studyCodeButton.setVisible(!internshipSelected);
            searchOnFilter();
        }
    }

    class SelectInstitute implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SelectInstituteDialog instituteDlg = new SelectInstituteDialog((JFrame) getOwner(), Institute.InstituteType.University);
            instituteDlg.setVisible(true);

            Institute institute = instituteDlg.getSelectedInstitute();
            if (institute != null) {
                instituteField.setText(institute.getName());
                selectedInstitute = institute.getOrgid() + "";
            }
        }

    }
}
