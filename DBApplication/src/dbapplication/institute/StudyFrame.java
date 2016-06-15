package dbapplication.institute;

import dbapplication.JEditField;
import dbapplication.JSearchField;
import dbapplication.SearchFilter;
import dbapplication.institute.RegisterStudyDialog;
import dbapplication.institute.Study;
import dbapplication.institute.StudyTableModel;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Sishi
 */
public class StudyFrame extends JDialog {

    public enum ProgramType {
        Internship, studyProgram
    }
    private final ProgramType requiredType;

    private JTextField searchField;

    private JButton cancelButton;
    private JButton searchButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton okButton;
    private JButton addButton;

    private JLabel codeLabel;
    private JLabel org_idLabel;
    private JLabel contactpersonLabel;
    private JLabel selectedStudyLabel;

    private JTextField org_idField;
    private JTextField codeField;
    private JTextField contactpersonField;

    private JComboBox searchConditionCombo;
    private JTable resultTable;
    private JScrollPane resultPanel;

    private StudyTableModel resultModel;
    private Study selectedStudy = null;

    public StudyFrame(Frame owner, ProgramType type) {
        super(owner, true);
        requiredType = type;
        setupFrame();
        createComponents();
        // fill JTable searching on empty filter
        search("", "code");
    }

    private void setupFrame() {
        setSize(700, 500);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Select Study");
    }

    private void createComponents() {
        SelectionListener select = new SelectionListener();
        searchListener search = new searchListener();
        CloseDialogListener close = new CloseDialogListener();
        RegisterStudyListener register = new RegisterStudyListener();

        searchField = new JSearchField();
        searchField.setBounds(20, 20, 180, 30);
        add(searchField);

        searchButton = new JButton("Search");
        searchButton.setBounds(220, 20, 90, 30);
        searchButton.addActionListener(search);
        add(searchButton);

        searchConditionCombo = new JComboBox(new SearchFilter[]{
            new SearchFilter("Code", "code"),
            new SearchFilter("Org_ID", "org_id"),
            new SearchFilter("Contactperson", "contactperson")
        });
        searchConditionCombo.setBounds(320, 20, 100, 30);
        add(searchConditionCombo);

        selectedStudyLabel = new JLabel("Selected study:");
        selectedStudyLabel.setLocation(440, 20);
        selectedStudyLabel.setSize(200, 30);
        add(selectedStudyLabel);

        codeLabel = new JLabel("code");
        codeLabel.setLocation(440, 70);
        codeLabel.setSize(90, 30);
        add(codeLabel);

        codeField = new JEditField("code");
        codeField.setLocation(490, 70);
        codeField.setSize(120, 30);
        add(codeField);

        org_idLabel = new JLabel("org_id");
        org_idLabel.setLocation(440, 120);
        org_idLabel.setSize(90, 30);
        add(org_idLabel);

        org_idField = new JEditField("org_id");
        org_idField.setLocation(490, 120);
        org_idField.setSize(120, 30);
        add(org_idField);

        contactpersonLabel = new JLabel("Contact");
        contactpersonLabel.setLocation(440, 170);
        contactpersonLabel.setSize(90, 30);
        add(contactpersonLabel);

        contactpersonField = new JEditField("contactperson");
        contactpersonField.setLocation(490, 170);
        contactpersonField.setSize(120, 30);
        add(contactpersonField);

        //table
        resultTable = new JTable();
        resultTable.setBounds(0, 0, 400, 300);
        resultModel = new StudyTableModel();
        resultTable.setModel(resultModel);
        resultTable.setPreferredScrollableViewportSize(new Dimension(400, 300));
        resultTable.setFillsViewportHeight(true);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(select);
        resultPanel = new JScrollPane(resultTable);
        resultPanel.setBounds(20, 60, 400, 300);
        add(resultPanel);

        //de buttons
        StudyEditListener edit = new StudyEditListener();

        cancelButton = new JButton("Cancel");
        cancelButton.setLocation(260, 400);
        cancelButton.setSize(70, 30);
        cancelButton.addActionListener(close);
        add(cancelButton);

        okButton = new JButton("OK");
        okButton.setLocation(350, 400);
        okButton.setSize(70, 30);
        okButton.addActionListener(close);
        add(okButton);

        updateButton = new JButton("Update");
        updateButton.setLocation(440, 370);
        updateButton.setSize(90, 30);
        updateButton.addActionListener(edit);
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setLocation(540, 370);
        deleteButton.setSize(90, 30);
        deleteButton.addActionListener(edit);
        add(deleteButton);
        
        addButton = new JButton("Add");
        addButton.setLocation(490, 400);
        addButton.setSize(90, 30);
        addButton.addActionListener(register);
        add(addButton);

    }

    public Study getSelectedStudy() {
        return selectedStudy;
    }

    private void search(String filter, String conditionColumn) {
        ArrayList<dbapplication.institute.Study> study = dbapplication.institute.Study.searchStudy(filter, conditionColumn);
        resultModel.setResults(study);
    }

    class CloseDialogListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == cancelButton) {
                selectedStudy = null;
            }
            dispose();
        }

    }

    class searchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int selectedIndex = searchConditionCombo.getSelectedIndex();
            SearchFilter selectedFilter = (SearchFilter) searchConditionCombo.getItemAt(selectedIndex);
            search(searchField.getText(), selectedFilter.getColumnName());

        }
    }

    class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {

            // get the corresponding 'Institute' object 
            // and update the fields to reflect it
            int selectedRow = resultTable.getSelectedRow();
            if (selectedRow < 0) {
                return; // selection cleared
            }
            selectedStudy = resultModel.getStudyAt(selectedRow);
            selectedStudyLabel.setText(
                    "Selected study: " + selectedStudy.getCode());

            contactpersonField.setText(selectedStudy.getContactperson());

            codeField.setText(selectedStudy.getCode());

            org_idField.setText(selectedStudy.getOrg_id());

        }

    }

    class StudyEditListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedStudy == null) {
                return;
            }
            if (e.getSource() == updateButton) {
                int update = JOptionPane.showOptionDialog(StudyFrame.this, "Study has been updated", "Updated", JOptionPane.PLAIN_MESSAGE,
                        JOptionPane.INFORMATION_MESSAGE, null, null, null);
                if (update == JOptionPane.OK_OPTION) {
                    selectedStudy.updateStudy(codeField.getText(), org_idField.getText(), contactpersonField.getText());

                    int selectedIndex = searchConditionCombo.getSelectedIndex();
                    SearchFilter selectedFilter = (SearchFilter) searchConditionCombo.getItemAt(selectedIndex);
                    search(searchField.getText(), selectedFilter.getColumnName());
                }
            }
            if (e.getSource() == deleteButton) {
                // show confirm dialog and confirm that the user choose "OK"
                int choice = JOptionPane.showConfirmDialog(StudyFrame.this, "Are you sure you want to delete this Study?",
                        "Delete Study", JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    selectedStudy.deleteStudy();
                    
                    int selectedIndex = searchConditionCombo.getSelectedIndex();
                    SearchFilter selectedFilter = (SearchFilter) searchConditionCombo.getItemAt(selectedIndex);
                    search(searchField.getText(), selectedFilter.getColumnName());
                }
            }
        }
    }
        
        private class RegisterStudyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            RegisterStudyDialog rsf = new RegisterStudyDialog((JFrame)getOwner(), RegisterStudyDialog.StudyType.Study);
            rsf.setVisible(true);
    }
        }
    }
