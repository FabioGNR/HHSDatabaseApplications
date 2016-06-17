package dbapplication.program;

import dbapplication.DatabaseTableModel;
import dbapplication.JEditArea;
import dbapplication.JEditField;
import dbapplication.JSearchField;
import dbapplication.SearchFilter;
import dbapplication.institute.SelectStudyDialog;
import dbapplication.institute.Study;
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
    private JTextArea descriptionAreaField;
    private JButton searchButton, saveButton, deleteButton;
    private JButton studyCodeButton;
    private JComboBox internshipConditionBox, studyProgramBox;
    private JComboBox programBox, maxCreditBox, studyTypeBox;
    private final String[] maxCredits = {"15 ECS", "30 ECS", "45 ECS", "60 ECS"};
    private final JCheckBox[] termBoxes = new JCheckBox[ExProgram.Terms];
    private JTable resultTable;
    private JScrollPane resultPanel;
    private DatabaseTableModel<ExProgram> tableModel;
    private JLabel selectedProgramLabel, instituteLabel;
    private ExProgram selectedProgram = null;

    public SearchProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
        search("", "name");
    }

    private void setupFrame() {
        setTitle("Search Program");
        setSize(1200, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    @Override
    public void setVisible(boolean state) {
        super.setVisible(state);
        // reset fields here
        if (state) {
            search("", "name");
        }
    }
    
    private void resetFields() {
        nameField.setText("");
        studyField.setText("");
        instituteField.setText("");
        descriptionAreaField.setText("");
        for(int i = 0; i < termBoxes.length; i++) {
            termBoxes[i].setSelected(false);
        }
    }

    private void createComponents() {
        //searchfield
        searchField = new JSearchField();
        searchField.setBounds(20, 20, 180, 30);
        add(searchField);

        //searchButton
        searchButton = new JButton("Search");
        searchButton.setBounds(220, 20, 90, 30);
        searchButton.addActionListener(new SearchListener());
        add(searchButton);

        //programBox
        programBox = new JComboBox(ExProgram.ProgramType.values());
        programBox.setBounds(330, 20, 100, 30);
        programBox.addActionListener(new SwitchProgramListener());
        add(programBox);

        //internshipConditionBox
        internshipConditionBox = new JComboBox(new SearchFilter[]{
            new SearchFilter("Name", "name")});
        internshipConditionBox.setBounds(450, 20, 125, 30);
        add(internshipConditionBox);
        internshipConditionBox.setVisible(true);

        //studyProgramBox
        studyProgramBox = new JComboBox(new SearchFilter[]{
            new SearchFilter("Name", "name")
        });
        studyProgramBox.setBounds(450, 20, 125, 30);
        add(studyProgramBox);
        studyProgramBox.setVisible(false);

        //resultTable
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
        resultPanel.setBounds(20, 60, 555, 350);
        add(resultPanel);

        //selectedprogramLabel
        selectedProgramLabel = new JLabel("Selected Program: ");
        selectedProgramLabel.setBounds(600, 20, 250, 30);
        add(selectedProgramLabel);

        //nameField
        nameField = new JEditField("Name");
        nameField.setBounds(600, 60, 220, 30);
        add(nameField);

        //maxCreditBox
        maxCreditBox = new JComboBox(maxCredits);
        maxCreditBox.setBounds(600, 100, 100, 30);
        add(maxCreditBox);

        instituteLabel = new JLabel("Institute: ");
        instituteLabel.setBounds(600, 140, 60, 30);
        add(instituteLabel);

        //instituteField
        instituteField = new JEditField("");
        instituteField.setBounds(660, 140, 150, 30);
        instituteField.setEnabled(false);
        add(instituteField);

        //studyTypeBox
        studyTypeBox = new JComboBox(StudyProgram.StudyType.values());
        studyTypeBox.setBounds(600, 180, 150, 30);
        add(studyTypeBox);
        studyTypeBox.setVisible(false);

        studyField = new JEditField("Study Name");
        studyField.setBounds(600, 220, 100, 30);
        studyField.setEnabled(false);
        add(studyField);
        studyField.setVisible(false);

        // add listener
        studyCodeButton = new JButton("...");
        studyCodeButton.setBounds(710, 220, 40, 30);
        studyCodeButton.addActionListener(new SelectStudyListener());
        add(studyCodeButton);
        studyCodeButton.setVisible(false);

        descriptionAreaField = new JEditArea("Write a description.");
        descriptionAreaField.setBounds(850, 40, 300, 200);
        add(descriptionAreaField);
        
        // term boxes
        for(int i = 0; i < ExProgram.Terms; i++) {
            termBoxes[i] = new JCheckBox("Term "+(i+1));
            termBoxes[i].setBounds(850, 250+(i*30), 130, 30);
            add(termBoxes[i]);
        }

        saveButton = new JButton("Save");
        saveButton.setBounds(600, 350, 75, 30);
        saveButton.addActionListener(new SaveListener());
        add(saveButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(685, 350, 75, 30);
        deleteButton.addActionListener(new DeleteListener());
        add(deleteButton);
    }

    private ExProgram.ProgramType getSelectedType() {
        return (ExProgram.ProgramType) programBox.getSelectedItem();
    }
    
    private int getSelectedProgramInstitute() {
        ExProgram.ProgramType type = getSelectedType();
        if(type == ExProgram.ProgramType.Internship) {
            return ((Internship)selectedProgram).getOrg_id();
        } else {
            return ((StudyProgram)selectedProgram).getOrg_id();
        }
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
        resetFields();
    }

    private void setSelectedProgram(ExProgram program) {
        selectedProgram = program;
        if (program != null) {
            selectedProgramLabel.setText("Selected Program: " + program.getName());
        } else {
            resetFields();
            selectedProgramLabel.setText("Selected Program: ");
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

    class SelectStudyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(selectedProgram == null) return;
            if(getSelectedType() != ExProgram.ProgramType.StudyProgram) return;
            SelectStudyDialog dlg = new SelectStudyDialog(
                    (JFrame) getOwner(), getSelectedProgramInstitute());
            dlg.setVisible(true);
            // pauses until dialog is closed
            Study study = dlg.getSelectedStudy();
            if (study != null) {
                studyField.setText(study.getCode());
            }
        }    
    }

    class DeleteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedProgram == null) {
                JOptionPane.showMessageDialog(SearchProgramFrame.this, "Please select a program!",
                        "Program not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int choice = JOptionPane.showConfirmDialog(SearchProgramFrame.this,
                    "Are you sure you want to delete this program?",
                    "Delete program", JOptionPane.OK_CANCEL_OPTION);
            if (choice == JOptionPane.OK_OPTION) {
                selectedProgram.delete();
                setSelectedProgram(null);
                tableModel.fireTableDataChanged();
            }
        }
    }

    class SaveListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedProgram == null) {
                JOptionPane.showMessageDialog(SearchProgramFrame.this, "Please select a program!",
                        "Program not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
            ExProgram.ProgramType type = getSelectedType();
            if (type == ExProgram.ProgramType.StudyProgram) {
                // set study attributes
                StudyProgram studyProgram = (StudyProgram) selectedProgram;
                studyProgram.setStudy_code(studyField.getText());
                studyProgram.setStudyType((StudyProgram.StudyType) studyTypeBox.getSelectedItem());
            }
            boolean[] terms = new boolean[ExProgram.Terms];
            for(int i = 0; i < terms.length; i++) {
                terms[i] = termBoxes[i].isSelected();
            }
            selectedProgram.setTerms(terms);
            selectedProgram.setName(nameField.getText());
            selectedProgram.setDescription(descriptionAreaField.getText());
            int credits = (maxCreditBox.getSelectedIndex() + 1) * 15;
            selectedProgram.setMaxCredit(credits);
            if (!selectedProgram.save()) {
                JOptionPane.showMessageDialog(SearchProgramFrame.this, "Saving program failed!",
                        "Error saving program", JOptionPane.ERROR_MESSAGE);
            } else {
                selectedProgram.refreshCellData();
                tableModel.fireTableDataChanged();
            }
        }
    }

    class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = resultTable.getSelectedRow();
            if (selectedRow < 0) {
                setSelectedProgram(null);
                return;
            }
            setSelectedProgram(tableModel.get(selectedRow));
            nameField.setText(selectedProgram.getName());
            //maxCredit
            int itemIndex = (selectedProgram.getMaxCredits() / 15) - 1;
            itemIndex = Math.min(itemIndex, maxCreditBox.getItemCount() - 1);
            maxCreditBox.setSelectedIndex(itemIndex);
            //instituteField
            instituteField.setText(selectedProgram.getInstituteName());
            // description
            descriptionAreaField.setText(selectedProgram.getDescription());
            // study type & study
            if (getSelectedType() == ExProgram.ProgramType.StudyProgram) {
                StudyProgram studyProgram = (StudyProgram) selectedProgram;
                studyTypeBox.setSelectedItem(studyProgram.getStudyType());
                //studyCode  
                studyField.setText(studyProgram.getStudy_code());
            }
            // set term boxes
            for(int i = 0; i < ExProgram.Terms; i++) {
                termBoxes[i].setSelected(selectedProgram.getTerms()[i]);
            }
        }
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
            boolean internshipSelected = getSelectedType() == ExProgram.ProgramType.Internship;
            internshipConditionBox.setVisible(internshipSelected);
            studyProgramBox.setVisible(!internshipSelected);
            studyTypeBox.setVisible(!internshipSelected);
            studyField.setVisible(!internshipSelected);
            studyCodeButton.setVisible(!internshipSelected);
            searchOnFilter();
        }
    }

}
