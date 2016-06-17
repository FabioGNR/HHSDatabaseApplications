package dbapplication.program;

import dbapplication.DatabaseTableModel;
import dbapplication.JSearchField;
import dbapplication.SearchFilter;
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
public class SelectProgramFrame extends JDialog {

    private JTextField searchField;
    private JButton searchButton, okButton, cancelButton;
    private JComboBox internshipConditionBox, studyProgramBox, programBox;
    private JTable resultTable;
    private JScrollPane resultPanel;
    private DatabaseTableModel<ExProgram> resultModel;
    private JLabel selectedProgramLabel;
    private ExProgram selectedProgram = null;
    private int instituteID;
    private String study;

    public SelectProgramFrame(JFrame owner, int company) {
        super(owner, true);
        instituteID = company;
        study = null;       
        setupFrame();
        createComponents();   
        search("", "name");
    }
    
    public SelectProgramFrame(JFrame owner, int university, String study) {
        super(owner, true);
        instituteID = university;
        this.study = study;
        setupFrame();
        createComponents();   
        search("", "name");
    }

    private void setupFrame() {
        setTitle("Select Program");
        setSize(595, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);      
    }
    
    private ExProgram.ProgramType getSelectedType() {
        return (ExProgram.ProgramType)programBox.getSelectedItem();
    }

    private void createComponents() {
        searchField = new JSearchField();
        searchField.setBounds(20, 20, 180, 30);
        add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(220, 20, 90, 30);
        searchButton.addActionListener(new SearchListener());
        add(searchButton);

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
        resultModel = new DatabaseTableModel<>(
                new String[] { "Name", "Credits" });
        resultTable.setModel(resultModel);
        resultTable.setPreferredScrollableViewportSize(new Dimension(400, 300));
        resultTable.setFillsViewportHeight(true);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(new SelectionListener());
        resultPanel = new JScrollPane(resultTable);
        resultPanel.setBounds(20, 60, 555, 300);
        add(resultPanel);

        selectedProgramLabel = new JLabel("Selected Program: ");
        selectedProgramLabel.setBounds(20, 370, 150, 30);
        add(selectedProgramLabel);
        
        ExProgram.ProgramType type;
        if(study == null) // company > internship
            type = ExProgram.ProgramType.Internship;
        else
            type = ExProgram.ProgramType.StudyProgram;
        programBox = new JComboBox(new ExProgram.ProgramType[] {type});
        programBox.setBounds(330, 20, 100, 30);
        programBox.addActionListener(new SwitchProgramListener());
        add(programBox);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(425, 370, 75, 30);
        cancelButton.addActionListener(new CancelButtonListener());
        add(cancelButton);
        
        okButton = new JButton("OK");
        okButton.setBounds(510, 370, 75, 30);
        okButton.setEnabled(false);
        okButton.addActionListener(new OKButtonListener());
        add(okButton);
    }

    public ExProgram getSelectedProgram() {
        return selectedProgram;
    }

    private void search(String filter, String conditionColumn) {
        ArrayList<ExProgram> programs, filteredPrograms = new ArrayList<>();
        ExProgram.ProgramType type = getSelectedType();
        if(type == ExProgram.ProgramType.Internship){
            programs = Internship.searchProgram(filter, conditionColumn);
            for(int i = 0; i < programs.size(); i++) {
                Internship program = (Internship)programs.get(i);
                if(program.getOrg_id() == instituteID) {
                    filteredPrograms.add(programs.get(i));
                }
            }
        }else{
            programs = StudyProgram.searchStudyProgram(filter, conditionColumn);
            for(int i = 0; i < programs.size(); i++) {
                StudyProgram program = (StudyProgram)programs.get(i);
                if(program.getOrg_id() == instituteID) {
                    if(program.getStudy_code().equals(study)) {
                        filteredPrograms.add(programs.get(i));
                    }
                }
            }
        }
        resultModel.setItems(filteredPrograms); 
    }

    class OKButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
        
    }
    
    class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedProgram = null;
            dispose();
        }
        
    }
    
    class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = resultTable.getSelectedRow();
            if (selectedRow < 0) {
                okButton.setEnabled(false);
                return;
            }
            selectedProgram = resultModel.get(selectedRow);
            selectedProgramLabel.setText("Selected program: " + selectedProgram.getName());
            okButton.setEnabled(true);
        }
    }

    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
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
    }

    class SwitchProgramListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean internshipSelected = 
                    getSelectedType() == ExProgram.ProgramType.Internship;
            internshipConditionBox.setVisible(internshipSelected);
            studyProgramBox.setVisible(!internshipSelected);
        }
    }
}
