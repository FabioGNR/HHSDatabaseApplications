package dbapplication.institute;

import dbapplication.JEditField;
import dbapplication.JSearchField;
import dbapplication.SearchFilter;
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
public class SelectStudyDialog extends JDialog {

    private JTextField searchField;

    private JButton cancelButton;
    private JButton searchButton;
    private JButton okButton;

    private JLabel selectedStudyLabel;

    private JComboBox searchConditionCombo;
    private JTable resultTable;
    private JScrollPane resultPanel;

    private StudyTableModel resultModel;
    private Study selectedStudy = null;

    public SelectStudyDialog(Frame owner) {
        super(owner, true);
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
        }

    }

}
