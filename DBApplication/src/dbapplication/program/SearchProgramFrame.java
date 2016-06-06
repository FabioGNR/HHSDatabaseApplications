package dbapplication.program;

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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


/**
 *
 * @author omari_000
 */
public class SearchProgramFrame extends JDialog {
    
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox conditionBox;
    private JTable resultTable;
    private JScrollPane resultPanel;
    private ProgramTableModel resultModel;            
    
    public SearchProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();      
        createComponents(); 
    }

    private void setupFrame() {
        setTitle("Search Program");
        setSize(500, 400);
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
    
    private void search(String filter, String conditionColumn){
        ArrayList<ExProgram> program = ExProgram.searchExProgram(filter, conditionColumn);
        resultModel.setResults(program);
    }
    
    class SearchListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = conditionBox.getSelectedIndex();
            SearchFilter selectedFilter = (SearchFilter)conditionBox.getItemAt(selectedIndex);
            search(searchField.getText(), selectedFilter.getColumnName());
        }
        
    }
}
