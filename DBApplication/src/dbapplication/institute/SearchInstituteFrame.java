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
 * @author jordain & sishi
 */
public class SearchInstituteFrame extends JDialog {

    private  JTextField searchField;
    private  JTextField cityField;
    private  JTextField nameField;
    private  JTextField countryField;
    private  JTextField addressField;
   

    private JLabel cityLabel;
    private JLabel nameLabel;
    private JLabel countryLabel;
    private JLabel addressLabel;
    private JLabel selectedInstituteLabel;

    private JComboBox searchConditionCombo;
    private JComboBox typeCombo;
    
    private JButton searchButton;
    private JButton showButton;
    private JButton updateButton;
    private JButton deleteButton;

    private JTable resultTable;
    private JScrollPane resultPanel;
    private InstituteTableModel resultModel;
    private Institute selectedInstitute = null;
    
    public SearchInstituteFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
        search("", "name");
    }

    private void setupFrame() {
        setSize(700, 450);
        setTitle("Search Institute");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        
    }

    private void createComponents() {
        SearchListener lis = new SearchListener();
        InstituteEditListener edit = new InstituteEditListener();
        SelectStudyListener studyLis = new SelectStudyListener();

        searchField = new JSearchField();
        searchField.setLocation(20, 20);
        searchField.setSize(180, 30);
        add(searchField);

        searchButton = new JButton("Search");
        searchButton.setLocation(215, 20);
        searchButton.setSize(90, 30);
        searchButton.addActionListener(lis);
        add(searchButton);

        typeCombo = new JComboBox(Institute.InstituteType.values());
        typeCombo.setLocation(440, 20);
        typeCombo.setSize(150, 30);
        typeCombo.addActionListener(lis);
        add(typeCombo);

        searchConditionCombo = new JComboBox(new SearchFilter[]{
            new SearchFilter("Name", "name"),
            new SearchFilter("City", "city"),
            new SearchFilter("Country", "country")});
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
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(new SelectionListener());
        resultPanel = new JScrollPane(resultTable);
        resultPanel.setLocation(20, 60);
        resultPanel.setSize(400, 300);
        add(resultPanel);

        selectedInstituteLabel = new JLabel("Selected institute:");
        selectedInstituteLabel.setLocation(440, 55);
        selectedInstituteLabel.setSize(200, 30);
        add(selectedInstituteLabel);
        
        nameLabel = new JLabel("Name");
        nameLabel.setLocation(440, 85);
        nameLabel.setSize(90, 30);
        add(nameLabel);

        nameField = new JEditField("Name");
        nameField.setLocation(490, 85);
        nameField.setSize(120, 30);
        add(nameField);

        cityLabel = new JLabel("City");
        cityLabel.setLocation(440, 135);
        cityLabel.setSize(90, 30);
        add(cityLabel);

        cityField = new JEditField("City");
        cityField.setLocation(490, 135);
        cityField.setSize(120, 30);
        add(cityField);

        countryLabel = new JLabel("Country");
        countryLabel.setLocation(440, 185);
        countryLabel.setSize(90, 30);
        add(countryLabel);

        countryField = new JEditField("Country");
        countryField.setLocation(490, 185);
        countryField.setSize(120, 30);
        add(countryField);

        addressLabel = new JLabel("Address");
        addressLabel.setLocation(440, 235);
        addressLabel.setSize(90, 30);
        add(addressLabel);

        addressField = new JEditField("Address");
        addressField.setLocation(490, 235);
        addressField.setSize(120, 30);
        add(addressField);

        showButton = new JButton("Manage Studies");
        showButton.setLocation(440, 285);
        showButton.setSize(150, 30);
        add(showButton);
        showButton.addActionListener(studyLis);
        showButton.setVisible(false);

        updateButton = new JButton("Update");
        updateButton.setLocation(440, 331);
        updateButton.setSize(90, 30);
        updateButton.addActionListener(edit);
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setLocation(540, 331);
        deleteButton.setSize(90, 30);
        deleteButton.addActionListener(edit);
        add(deleteButton);

    }

    public void refresh() {
        int selectedIndex = searchConditionCombo.getSelectedIndex();
        SearchFilter selectedFilter = (SearchFilter) searchConditionCombo.getItemAt(selectedIndex);
        search(searchField.getText(), selectedFilter.getColumnName());

    }
    
    public void textRefresh() {
        nameField.setText("");
        cityField.setText("");
        countryField.setText("");
        addressField.setText("");
        
    }

    private class SelectStudyListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(selectedInstitute == null) return;
            StudyFrame dlg = new StudyFrame((JFrame) getOwner(), 
                    selectedInstitute.getOrgid());
            dlg.setVisible(true);          
        }
    }

    class InstituteEditListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String city, name, country, address;
            if (selectedInstitute == null) {
                return;
            }
            city = cityField.getText();
            if (city.isEmpty() || city.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(SearchInstituteFrame.this,
                        "city cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                city = null;
            }
            name = nameField.getText();
            if (name.isEmpty() || name.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(SearchInstituteFrame.this,
                        "name cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                name = null;
            }
            country = countryField.getText();
            if (country.isEmpty() || country.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(SearchInstituteFrame.this,
                        "country cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                country = null;
            }
            address = addressField.getText();
            if (address.isEmpty()) {
                address = null;
            }
            if (e.getSource() == updateButton) {
                if (city == null || address == null || name == null || country == null) {

                    JOptionPane.showMessageDialog(SearchInstituteFrame.this, "Update of institute failed", "Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    int update = JOptionPane.showOptionDialog(SearchInstituteFrame.this, "Institute has been updated", "Updated", JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (update == JOptionPane.OK_OPTION) {
                        selectedInstitute.updateInstitute(cityField.getText(), nameField.getText(), countryField.getText(), addressField.getText());
                        selectedInstituteLabel.setText("Selected institute: " + nameField.getText());
                        refresh();

                    }
                }
            }
            if (e.getSource() == deleteButton) {
                if (city == null || address == null || name == null || country == null) {
                    JOptionPane.showMessageDialog(SearchInstituteFrame.this, "Update of institute failed", "Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    int choice = JOptionPane.showConfirmDialog(SearchInstituteFrame.this, "Are you sure you want to delete this institute?",
                            "Delete institute", JOptionPane.OK_CANCEL_OPTION);
                    if (choice == JOptionPane.OK_OPTION) {
                        selectedInstitute.deleteInstitute();
                        selectedInstituteLabel.setText("Selected institute: ");
                        refresh();
                        textRefresh();
                    }

                }
            }
        }
    }

    private void search(String filter, String conditionColumn) {
        if ((Institute.InstituteType)typeCombo.getSelectedItem() 
                == Institute.InstituteType.University) {
            ArrayList<dbapplication.institute.Institute> institute
                    = dbapplication.institute.Institute.searchUniversity(filter, conditionColumn);
            resultModel.setResults(institute);
            showButton.setVisible(true);

        } else {
            ArrayList<dbapplication.institute.Institute> institute
                    = dbapplication.institute.Institute.searchCompany(filter, conditionColumn);
            resultModel.setResults(institute); 
            showButton.setVisible(false);
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
            selectedInstitute = resultModel.getInstituteAt(selectedRow);
            selectedInstituteLabel.setText(
                    "Selected institute: " + selectedInstitute.getName());

            nameField.setText(selectedInstitute.getName());

            cityField.setText(selectedInstitute.getCity());

            countryField.setText(selectedInstitute.getCountry());

            addressField.setText(selectedInstitute.getAddress());

        }

    }
  
}