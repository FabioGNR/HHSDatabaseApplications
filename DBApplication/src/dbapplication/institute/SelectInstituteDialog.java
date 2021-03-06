package dbapplication.institute;

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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author jordain & sishi
 */
public class SelectInstituteDialog extends JDialog{
    private final Institute.InstituteType requiredType;
    private JTextField searchField;
    private JButton searchButton, cancelButton, okButton;
    private JComboBox searchConditionCombo;
    private JComboBox typeCombo;
    private JLabel selectedInstituteLabel;

    private JTable resultTable;
    private JScrollPane resultPanel;
    private InstituteTableModel resultModel;

    private Institute selectedInstitute = null;
    
    public SelectInstituteDialog(Frame owner, Institute.InstituteType type) {
        super(owner, true);
        requiredType = type;
        setupFrame();
        createComponents();
        // fill JTable searching on empty filter
        search("", "name");
    }
    
    private void setupFrame() {
        setSize(550, 510);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Select Institute");
    }
    
    private void createComponents () {
        searchField = new JSearchField();
        searchField.setLocation(20, 20);
        searchField.setSize(180, 30);
        add(searchField);

        searchButton = new JButton("Search");
        searchButton.setLocation(215, 20);
        searchButton.setSize(90, 30);
        searchButton.addActionListener(new SearchListener());
        add(searchButton);

        searchConditionCombo = new JComboBox(new SearchFilter[]{
            new SearchFilter("Name", "name"),
            new SearchFilter("City", "city"),
            new SearchFilter("Country", "country")
        });
        searchConditionCombo.setLocation(320, 20);
        searchConditionCombo.setSize(100, 30);
        add(searchConditionCombo);     
        Institute.InstituteType[] types;
        if(requiredType == null)
            types = Institute.InstituteType.values();
        else
            types = new Institute.InstituteType[] {requiredType};
        typeCombo = new JComboBox(types);
        typeCombo.setLocation(430, 20);
        typeCombo.setSize(100, 30);
        typeCombo.addActionListener(new SwitchTypeListener());
        add(typeCombo);
        
        resultTable = new JTable();
        resultTable.setLocation(0, 0);
        resultTable.setSize(440, 300);
        resultModel = new InstituteTableModel();
        resultTable.setModel(resultModel);
        resultTable.setPreferredScrollableViewportSize(new Dimension(400, 300));
        resultTable.setFillsViewportHeight(true);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(new SelectionListener());
        resultPanel = new JScrollPane(resultTable);
        resultPanel.setLocation(20, 60);
        resultPanel.setSize(400, 300);
        add(resultPanel);
        
        CloseDialogListener closeLis = new CloseDialogListener();
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(270, 390, 70, 30);
        cancelButton.addActionListener(closeLis);
        add(cancelButton);
        okButton = new JButton("OK");
        okButton.setBounds(350, 390, 70, 30);
        okButton.addActionListener(closeLis);
        add(okButton);
        okButton.setEnabled(false);
        
        selectedInstituteLabel = new JLabel("Selected institute:");
        selectedInstituteLabel.setLocation(20, 365);
        selectedInstituteLabel.setSize(250, 30);
        add(selectedInstituteLabel); 
    }

    public Institute getSelectedInstitute() {
        return selectedInstitute;
    }
    
    private void search(String filter, String conditionColumn) {
        if ((Institute.InstituteType)typeCombo.getSelectedItem() 
                == Institute.InstituteType.University) {
            ArrayList<dbapplication.institute.Institute> institute
                    = dbapplication.institute.Institute.searchUniversity(filter, conditionColumn);
            resultModel.setResults(institute);

        } else {
            ArrayList<dbapplication.institute.Institute> institute
                    = dbapplication.institute.Institute.searchCompany(filter, conditionColumn);
            resultModel.setResults(institute); 
        }
    }
    
    class CloseDialogListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == cancelButton) {
                selectedInstitute = null;
            }
            dispose();
        }      
    }
    
    class SwitchTypeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = searchConditionCombo.getSelectedIndex();
            SearchFilter selectedFilter = (SearchFilter) searchConditionCombo.getItemAt(selectedIndex);
            search(searchField.getText(), selectedFilter.getColumnName());
        }
        
    }

    class SearchListener implements ActionListener {

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
            Institute inst = resultModel.getInstituteAt(selectedRow);
            // if the selected institute matches the required type
            if(requiredType == null 
                    || (requiredType == Institute.InstituteType.Company && inst.isBusiness())
                    || (requiredType == Institute.InstituteType.University && !inst.isBusiness())) {
                selectedInstitute = inst;
                selectedInstituteLabel.setText(
                        "Selected institute: " + selectedInstitute.getName());
                okButton.setEnabled(true);
            }
        }
    }
    
}
