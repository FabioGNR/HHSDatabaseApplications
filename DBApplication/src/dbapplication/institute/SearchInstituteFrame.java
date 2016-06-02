package dbapplication.institute;

import dbapplication.JSearchField;
import dbapplication.SearchFilter;
import dbapplication.institute.InstituteTableModel;
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
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumnModel;
/**
 *
 * @author omari_000
 */
public class SearchInstituteFrame extends JDialog{
    private JTextField searchField;
    private JButton searchButton;
    private JComboBox searchConditionCombo;
    private JTable resultTable;
    private JScrollPane resultPanel;
    private InstituteTableModel resultModel;
    
    public SearchInstituteFrame(JFrame owner) {
        super(owner, true);
        setupFrame();     
        createComponents();
    }
    
    private void setupFrame() {
        setSize(500,400);
        setTitle("Search Institute");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);        
    }
    
    private void createComponents() {
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
            new SearchFilter("ID", "org_id"), 
            new SearchFilter("City", "city"), 
            new SearchFilter("Name", "name"), 
            new SearchFilter("Country", "country"), 
            new SearchFilter("Address", "address"), 
            new SearchFilter("Business?", "is_business")
        });
        searchConditionCombo.setLocation(340, 20);
        searchConditionCombo.setSize(100, 30);
        add(searchConditionCombo);
        resultTable = new JTable();
        resultTable.setLocation(0, 0);
        resultTable.setSize(400, 300);
        resultModel = new InstituteTableModel();
        resultTable.setModel(resultModel);
        resultTable.setPreferredScrollableViewportSize(new Dimension(400, 300));
        resultTable.setFillsViewportHeight(true);
        resultPanel = new JScrollPane(resultTable);
        resultPanel.setLocation(20, 60);
        resultPanel.setSize(400, 300);
        add(resultPanel);
    }
    
    private void search(String filter, String conditionColumn) {
        ArrayList<dbapplication.institute.Institute> institute = dbapplication.institute.Institute.searchInstitute(filter, conditionColumn);
        resultModel.setResults(institute);
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
