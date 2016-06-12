package dbapplication.program;

import dbapplication.JEditField;
import dbapplication.JSearchField;
import dbapplication.SearchFilter;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
    private JTextField orgIDField, studyTypeField, studyCodeField;
    private JButton searchButton, saveButton, deleteButton;
    private JComboBox internshipConditionBox, studyProgramBox, programBox;
    private final static String INTERSHIP_CARD_ID = "Internship";
    private final static String STUDYPROGRAM_CARD_ID = "Study Program";
    private String[] programType = {INTERSHIP_CARD_ID, STUDYPROGRAM_CARD_ID};
    private JTable resultTable;
    private JScrollPane resultPanel;
    private ProgramTableModel resultModel;
    private JLabel selectedProgramLabel, programCodeLabel;
    private ExProgram selectedProgram = null;

    public SearchProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
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

        ActionListener switchProgram = new SwitchProgramListener();
        programBox = new JComboBox(programType);
        programBox.setBounds(330, 20, 100, 30);
        programBox.addActionListener(switchProgram);
        add(programBox);

        internshipConditionBox = new JComboBox(new SearchFilter[]{
            new SearchFilter("Program Code", "code"),
            new SearchFilter("Name", "name")});
        internshipConditionBox.setBounds(450, 20, 125, 30);
        add(internshipConditionBox);
        internshipConditionBox.setVisible(true);

        studyProgramBox = new JComboBox(new SearchFilter[]{
            new SearchFilter("Program Code", "code"),
            new SearchFilter("Org_ID", "org_id"),
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

        orgIDField = new JEditField("Organisation ID");
        orgIDField.setBounds(600, 100, 150, 30);
        add(orgIDField);
        orgIDField.setVisible(false);

        studyTypeField = new JEditField("Study Type");
        studyTypeField.setBounds(600, 140, 150, 30);
        add(studyTypeField);
        studyTypeField.setVisible(false);

        studyCodeField = new JEditField("Study Code");
        studyCodeField.setBounds(600, 180, 150, 30);
        add(studyCodeField);
        studyCodeField.setVisible(false);

        saveButton = new JButton("Save");
        saveButton.setBounds(600, 525, 75, 30);
        saveButton.addActionListener(new ProgramEditListener());

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(675, 525, 75, 30);
        deleteButton.addActionListener(new ProgramEditListener());

    }

    private void search(String filter, String conditionColumn) {
        ArrayList<ExProgram> program = ExProgram.searchExProgram(filter, conditionColumn);
        resultModel.setResults(program);
    }

    class ProgramEditListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedProgram == null) {
                return;
            }
            if (e.getSource() == saveButton) {
                // saving data not done
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

        }
    }

    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == programBox.getItemAt(0)) {
                int selectedIndex = internshipConditionBox.getSelectedIndex();
                SearchFilter selectedFilter = (SearchFilter) internshipConditionBox.getItemAt(selectedIndex);
                search(searchField.getText(), selectedFilter.getColumnName());
            } else {
                int selectedIndex = studyProgramBox.getSelectedIndex();
                SearchFilter selectedfFilter = (SearchFilter) studyProgramBox.getItemAt(selectedIndex);
                search(searchField.getText(), selectedfFilter.getColumnName());
            }
        }
    }

    class SwitchProgramListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (programBox.getSelectedIndex() == 0) {
                internshipConditionBox.setVisible(true);
                studyProgramBox.setVisible(false);
                orgIDField.setVisible(false);
                studyTypeField.setVisible(false);
                studyCodeField.setVisible(false);
            } else {
                internshipConditionBox.setVisible(false);
                studyProgramBox.setVisible(true);
                orgIDField.setVisible(true);
                studyTypeField.setVisible(true);
                studyCodeField.setVisible(true);
            }
        }

    }

}
