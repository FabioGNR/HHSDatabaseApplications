package dbapplication.institute;

import dbapplication.JEditField;
import dbapplication.JSearchField;
import dbapplication.SearchFilter;
import dbapplication.institute.InstituteTableModel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author omari_000
 */
public class SearchInstituteFrame extends JDialog {

    private JTextField searchField;
    private JTextField cityField;
    private JTextField nameField;
    private JTextField countryField;
    private JTextField addressField;
    
    private JLabel cityLabel;
    private JLabel nameLabel;
    private JLabel countryLabel;
    private JLabel addressLabel;
    
    private JButton searchButton;
    private JButton addButton;
    
    private JComboBox searchConditionCombo;
    private JComboBox showProgramsCombo;
    
    private JTable resultTable;
    private JScrollPane resultPanel;
    private InstituteTableModel resultModel;
    
    private static String[] programs = {"Building process", "Business intelligence", "Database design", "Financial accounting", "Marketing", "Mechanica", "Programming"};

    public SearchInstituteFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(1000, 500);
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
        searchButton.setLocation(215, 20);
        searchButton.setSize(90, 30);
        searchButton.addActionListener(new SearchInstituteFrame.SearchListener());
        add(searchButton);
        
        searchConditionCombo = new JComboBox(new SearchFilter[]{
            new SearchFilter("ID", "org_id"),
            new SearchFilter("City", "city"),
            new SearchFilter("Name", "name"),
            new SearchFilter("Country", "country"),
            new SearchFilter("Address", "address"),
            new SearchFilter("Business?", "is_business")
        });
        searchConditionCombo.setLocation(320, 20);
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
        
        cityLabel = new JLabel("City");
        cityLabel.setLocation(500, 70);
        cityLabel.setSize(90, 30);
        add(cityLabel);
        
        cityField = new JEditField("City");
        cityField.setLocation(550, 70);
        cityField.setSize(90, 30);
        add(cityField);
        
        nameLabel = new JLabel("Name");
        nameLabel.setLocation(500, 120);
        nameLabel.setSize(90, 30);
        add(nameLabel);
        
        nameField = new JEditField("Name");
        nameField.setLocation(550, 120);
        nameField.setSize(90, 30);
        add(nameField);
        
        countryLabel = new JLabel("Country");
        countryLabel.setLocation(500, 170);
        countryLabel.setSize(90, 30);
        add(countryLabel);
        
        countryField = new JEditField("Country");
        countryField.setLocation(550, 170);
        countryField.setSize(90, 30);
        add(countryField);
        
        addressLabel = new JLabel("Address");
        addressLabel.setLocation(500, 220);
        addressLabel.setSize(90, 30);
        add(addressLabel);
        
        addressField = new JEditField("Address");
        addressField.setLocation(550, 220);
        addressField.setSize(90, 30);
        add(addressField);
        
        addButton = new JButton("Add");
        addButton.setLocation(660, 270);
        addButton.setSize(90, 30);
        add(addButton);
        
        showProgramsCombo = new JComboBox(programs);
        showProgramsCombo.setLocation(500, 270);
        showProgramsCombo.setSize(150, 30);
        add(showProgramsCombo);
    }

    private void search(String filter, String conditionColumn) {
        ArrayList<dbapplication.institute.Institute> institute = dbapplication.institute.Institute.searchInstitute(filter, conditionColumn);
        resultModel.setResults(institute);
    }

    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int is_business;
            int selectedIndex = searchConditionCombo.getSelectedIndex();
            SearchFilter selectedFilter = (SearchFilter) searchConditionCombo.getItemAt(selectedIndex);
            search(searchField.getText(), selectedFilter.getColumnName());
            
            
             
                
            }
             
             
        }

    }

