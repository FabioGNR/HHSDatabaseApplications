package dbapplication.program;

import dbapplication.JEditField;
import dbapplication.JSearchField;
import dbapplication.SearchFilter;
import dbapplication.institute.Institute;
import dbapplication.institute.SelectInstituteDialog;
import dbapplication.institute.SelectInstituteDialog.InstituteType;
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
    private JTextField instituteField, studyTypeField, studyField;
    private JButton searchButton, saveButton, deleteButton, studyTypeButton, instituteButton;
    private JComboBox internshipConditionBox, studyProgramBox, programBox;
    private final static String INTERSHIP_CARD_ID = "Internship";
    private final static String STUDYPROGRAM_CARD_ID = "Study Program";
    private String[] programType = {INTERSHIP_CARD_ID, STUDYPROGRAM_CARD_ID};
    private JTable resultTable;
    private JScrollPane resultPanel;
    private ProgramTableModel resultModel;
    private JLabel selectedProgramLabel, programCodeLabel;
    private ExProgram selectedProgram = null;
    private Internship internshipSelected = null;
    private StudyProgram studyProgramSelected = null;
    private String selectedInstitute;

    public SearchProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
//        search("", "name");
    }

    private void setupFrame() {
        setTitle("Search Program");
        setSize(800, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private void createComponents() {
        searchField = new JSearchField();
        searchField.setBounds(20, 20, 180, 30);
        add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(220, 20, 90, 30);
        searchButton.addActionListener(new SearchListener());
        add(searchButton);

        programBox = new JComboBox(programType);
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
            new SearchFilter("Study Type", "type"),
            new SearchFilter("Study Code", "study_code")
        });
        studyProgramBox.setBounds(450, 20, 125, 30);
        add(studyProgramBox);
        studyProgramBox.setVisible(false);

        resultTable = new JTable();
        resultTable.setBounds(0, 0, 555, 300);
        resultModel = new ProgramTableModel();
        resultTable.setModel(resultModel);
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

        //maak een button om org name te zoeken
        ActionListener instituteButtonListener = new SelectInstitute();
        instituteField = new JEditField("Organisation");
        instituteField.setBounds(600, 100, 100, 30);
        add(instituteField);
        instituteButton = new JButton("...");
        instituteButton.setBounds(710, 100, 40, 30);
        instituteButton.addActionListener(instituteButtonListener);
        add(instituteButton);

        //maak een combobox van
        studyTypeField = new JEditField("Study Type");
        studyTypeField.setBounds(600, 140, 100, 30);
        add(studyTypeField);
        studyTypeField.setVisible(false);
        studyTypeButton = new JButton("...");
        studyTypeButton.setBounds(710, 140, 40, 30);
        //addlistener
        add(studyTypeButton);
        studyTypeButton.setVisible(false);

        studyField = new JEditField("Study Code");
        studyField.setBounds(600, 180, 150, 30);
        add(studyField);
        studyField.setVisible(false);

        saveButton = new JButton("Save");
        saveButton.setBounds(600, 335, 75, 30);
        saveButton.addActionListener(new ProgramEditListener());
        add(saveButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(685, 335, 75, 30);
        deleteButton.addActionListener(new ProgramEditListener());
        add(deleteButton);
    }

    private void search(String filter, String conditionColumn, ExProgram.ProgramType type) {
        ArrayList<ExProgram> program;
        
        if(type == ExProgram.ProgramType.Internship){
            program = Internship.searchProgram(filter, conditionColumn);
        }else{
            program = StudyProgram.searchStudyProgram(filter, conditionColumn);
        }
        resultModel.setResults(program);
        
    }

    class ProgramEditListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedProgram == null) {
                return;
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
            selectedProgram = resultModel.getProgramAt(selectedRow);
            selectedProgramLabel.setText("Selected program: " + selectedProgram.getExPcode());
            nameField.setText(selectedProgram.getName());
//            orgIDField.setText(); 
//            studyCodeField.setText();

        }
    }

    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SearchFilter selectedFilter;
            if (e.getSource() == programBox.getItemAt(0)) {
                int selectedIndex = internshipConditionBox.getSelectedIndex();
                selectedFilter = (SearchFilter) internshipConditionBox.getItemAt(selectedIndex);
            } else {
                int selectedIndex = studyProgramBox.getSelectedIndex();
                selectedFilter = (SearchFilter) studyProgramBox.getItemAt(selectedIndex);
            }
//            search(searchField.getText(), selectedFilter.getColumnName());
        }
    }

    class SwitchProgramListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean internshipSelected = programBox.getSelectedIndex() == 0;
            internshipConditionBox.setVisible(internshipSelected);
            studyProgramBox.setVisible(!internshipSelected);
            // maakt het uit bij het updaten?
//                instituteField.setVisible(!internshipSelected);
//                instituteButton.setVisible(!internshipSelected);
            studyTypeField.setVisible(!internshipSelected);
            studyField.setVisible(!internshipSelected);
            studyTypeButton.setVisible(!internshipSelected);
        }
    }

    class SelectInstitute implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SelectInstituteDialog instituteDlg = new SelectInstituteDialog((JFrame) getOwner(), SelectInstituteDialog.InstituteType.University);
            instituteDlg.setVisible(true);

            Institute institute = instituteDlg.getSelectedInstitute();
            if (institute != null) {
                instituteField.setText(institute.getName());
                selectedInstitute = institute.getOrgid();
            }
        }

    }
}
