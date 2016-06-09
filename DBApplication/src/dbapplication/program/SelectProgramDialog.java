package dbapplication.program;

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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Sishi
 */
/*public class SelectProgramDialog extends JDialog {
    
    public enum ProgramType { Internship, studyProgram }
    private final ProgramType requiredType;
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox conditionBox;
    private JTable resultTable;
    private JScrollPane resultPanel;
    private ProgramTableModel resultModel; 
    private ExProgram selectedExProgram = null;
    
    public SelectProgramDialog (Frame owner, ProgramType type) {
        super(owner, true);
        requiredType = type;
        setupFrame();
        createComponents();
        // fill JTable searching on empty filter
        search("", "name");
    }
    
    private void setupFrame() {
        setSize(510, 510);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Select Program");
    }
    
    private void createComponents() {
        searchField = new JSearchField();
        searchField.setBounds(20, 20, 180, 30);
        add(searchField);
        
        searchButton = new JButton("Search");
        searchButton.setBounds(220, 20, 90, 30);
        searchButton.addActionListener(new SearchProgramFrame.SearchListener());
        add(searchButton);
        
        conditionBox = new JComboBox(new SearchFilter[]{
            new SearchFilter("ExProCode", "code"),
            new SearchFilter("Name", "name")});
        conditionBox.setBounds(340, 20, 100, 30);
        add(conditionBox);
        
        resultTable = new JTable();
        resultTable.setBounds(0, 0, 400, 300);
        resultModel = new ProgramTableModel();
        resultTable.setModel(resultModel);
        resultTable.setPreferredScrollableViewportSize(new Dimension(400, 300));
        resultTable.setFillsViewportHeight(true);
        resultPanel = new JScrollPane(resultTable);
        resultPanel.setBounds(20, 60, 400, 300);
        add(resultPanel);
    }
    
    public ExProgram getSelectedProgram() {
        return selectedExProgram;
    }
    
    private void search(String filter, String conditionColumn) {
        ArrayList<dbapplication.program.ExProgram> program = dbapplication.program.ExProgram.searchExProgram(filter, conditionColumn);
        resultModel.setResults(program);
    }
    
    class CloseDialogListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == cancelButton) {
                selectedExProgram = null;
            }
            dispose();
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
            ExProgram inst = resultModel.getExProgramAt(selectedRow);
            // if the selected institute matches the required type
            if(requiredType == null || (requiredType == SelectProgramDialog.ProgramType.Company && inst.getIsbusiness().equals("1"))
                    || (requiredType == SelectProgramDialog.ProgramType.University && inst.getIsbusiness().equals("0"))) {
                selectedExProgram = inst;
                selectedExProgramLabel.setText(
                        "Selected program: " + selectedExProgram.getName());
            }
        }
    }
}
*/