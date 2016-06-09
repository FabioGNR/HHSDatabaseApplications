package dbapplication.institute;

import dbapplication.JEditField;
import dbapplication.JSearchField;
import dbapplication.SearchFilter;
import java.awt.Dimension;
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
 * @author sishi
 */
public class StudyFrame extends JDialog{
    private JTextField searchField;
    private JTextField codefield;
    private JTextField org_idField;
    private JTextField contactField;
    
    private JButton searchButton;
    private JButton saveButton;
    private JButton deleteButton;
    
    private JComboBox searchConditionCombo;
    private JTable resultTable;
    private JScrollPane resultPanel;
    private StudyTableModel resultModel;
    private JLabel selectedStudyLabel;
    private Study selectedStudy = null;
    
    public StudyFrame(JFrame owner) {
        super(owner, true);
        setupFrame();     
        createComponents();
        // fill JTable searching on empty filter
        search("", "code");
    }
    
    private void setupFrame() {
        setSize(700,600);
        setTitle("Study");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
    }
    
    private void createComponents() {
        StudyEditListener edit = new StudyEditListener();
        
        searchField = new JSearchField();
        searchField.setLocation(20, 20);
        searchField.setSize(180, 30);
        add(searchField);
        
        searchButton = new JButton("Search");
        searchButton.setLocation(220, 20);
        searchButton.setSize(90, 30);
        searchButton.addActionListener(new SearchListener());
        add(searchButton);
        
        searchConditionCombo = new JComboBox(new SearchFilter[]{
            new SearchFilter("Code", "code"), 
            new SearchFilter("Org ID", "org_id"), 
            new SearchFilter("Contactperson", "contactperson")});
        searchConditionCombo.setLocation(340, 20);
        searchConditionCombo.setSize(100, 30);
        add(searchConditionCombo);
        
        resultTable = new JTable();
        resultTable.setLocation(0, 0);
        resultTable.setSize(400, 500);
        resultModel = new StudyTableModel();
        resultTable.setModel(resultModel);
        resultTable.setPreferredScrollableViewportSize(new Dimension(400, 500));
        resultTable.setFillsViewportHeight(true);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(new SelectionListener());
        resultPanel = new JScrollPane(resultTable);
        resultPanel.setLocation(20, 60);
        resultPanel.setSize(420, 500);
        add(resultPanel);
        
        selectedStudyLabel = new JLabel("Selected study:");
        selectedStudyLabel.setLocation(450, 20);
        selectedStudyLabel.setSize(150, 30);
        add(selectedStudyLabel);
        
        codefield = new JEditField("Code");
        codefield.setLocation(450, 60);
        codefield.setSize(150, 30);
        add(codefield);
        
        org_idField = new JEditField("Org_ID");
        org_idField.setLocation(450, 100);
        org_idField.setSize(150, 30);
        add(org_idField);
        
        contactField = new JEditField("Contactperson");
        contactField.setLocation(450, 140);
        contactField.setSize(150, 30);
        add(contactField);
        
        saveButton = new JButton("Save");
        saveButton.setLocation(450, 490);
        saveButton.setSize(75, 30);
        saveButton.addActionListener(edit);
        add(saveButton);
        
        deleteButton = new JButton("Delete");
        deleteButton.setLocation(525, 490);
        deleteButton.setSize(75, 30);
        deleteButton.addActionListener(edit);
        add(deleteButton);
    }
    
    private void search(String filter, String conditionColumn) {
        ArrayList<Study> study = Study.searchStudy(filter, conditionColumn);
        resultModel.setResults(study);
    }
    
    class StudyEditListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(selectedStudy == null) return;
            if(e.getSource() == saveButton) {
                
            }
            else if(e.getSource() == deleteButton) {
                // show confirm dialog and confirm that the user choose "OK"
                int choice = JOptionPane.showConfirmDialog(StudyFrame.this, "Are you sure you want to delete this study?", 
                    "Delete study", JOptionPane.OK_CANCEL_OPTION);
                if(choice == JOptionPane.OK_OPTION) {
                    selectedStudy.deleteStudy();
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
            selectedStudy = resultModel.getStudyAt(selectedRow);
            selectedStudyLabel.setText("Selected study: "+selectedStudy.getCode());
            codefield.setText(selectedStudy.getCode());
            org_idField.setText(selectedStudy.getOrg_id());
            
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
