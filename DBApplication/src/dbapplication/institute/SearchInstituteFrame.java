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
    private JTextField org_idField;
    private JTextField cityField;
    private JTextField nameField;
    private JTextField countryField;
    private JTextField addressField;
    
    private JLabel org_idLabel;
    private JLabel cityLabel;
    private JLabel nameLabel;
    private JLabel countryLabel;
    private JLabel addressLabel;
    private JLabel isBusinessLabel;
    private JLabel companyLabel;
    private JLabel uniLabel;
    
    private JCheckBox yesBox;
    private JCheckBox noBox;
    
    private JButton searchButton;
    private JButton companyButton;
    private JButton uniButton;
    
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
        
        org_idLabel = new JLabel("Org_id");
        org_idLabel.setLocation(500, 20);
        org_idLabel.setSize(90, 30);
        add(org_idLabel);
        
        org_idField = new JEditField("Org_id");
        org_idField.setLocation(550, 20);
        org_idField.setSize(90, 30);
        add(org_idField);
        
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
        
        isBusinessLabel = new JLabel();
        isBusinessLabel.setLocation(500, 270);
        isBusinessLabel.setSize(100, 30);
        isBusinessLabel.setText("Business?");
        add(isBusinessLabel);

        yesBox = new JCheckBox();
        yesBox.setLocation(560, 270);
        yesBox.setSize(50, 30);
        yesBox.setText("Yes");
        add(yesBox);

        noBox = new JCheckBox();
        noBox.setLocation(610, 270);
        noBox.setSize(50, 30);
        noBox.setText("No");
        add(noBox);

        ButtonGroup group = new ButtonGroup();
        group.add(yesBox);
        group.add(noBox);
        
        companyLabel = new JLabel("Company");
        companyLabel.setLocation(500, 320);
        companyLabel.setSize(90, 30);
        add(companyLabel);
        
        uniLabel = new JLabel("University");
        uniLabel.setLocation(500, 370);
        uniLabel.setSize(90, 30);
        add(uniLabel);
        
        companyButton = new JButton("Choose");
        companyButton.setLocation(560, 320);
        companyButton.setSize(90, 30);
        add(companyButton);
        
        uniButton = new JButton("Choose");
        uniButton.setLocation(560, 370);
        uniButton.setSize(90, 30);
        add(uniButton);
    }

    private void search(String filter, String conditionColumn) {
        ArrayList<dbapplication.institute.Institute> institute = dbapplication.institute.Institute.searchInstitute(filter, conditionColumn);
        resultModel.setResults(institute);
    }

    class SearchListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = searchConditionCombo.getSelectedIndex();
            SearchFilter selectedFilter = (SearchFilter) searchConditionCombo.getItemAt(selectedIndex);
            search(searchField.getText(), selectedFilter.getColumnName());
        }

    }
}
