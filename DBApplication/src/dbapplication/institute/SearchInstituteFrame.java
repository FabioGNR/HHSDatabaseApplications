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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
    private JLabel selectedInstituteLabel;

    private JButton searchButton;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    private JComboBox searchConditionCombo;
    private JComboBox showProgramsCombo;

    private JTable resultTable;
    private JScrollPane resultPanel;
    private InstituteTableModel resultModel;

    private Institute selectedInstitute = null;
    private static String[] programs = {"Building process", "Business intelligence", "Database design", "Financial accounting", "Marketing", "Mechanica", "Programming"};

    public SearchInstituteFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
        search("", "name");
    }

    private void setupFrame() {
        setSize(750, 450);
        setTitle("Search Institute");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);

    }

    private void createComponents() {
        SearchListener lis = new SearchListener();
        InstituteEditListener edit = new InstituteEditListener();

        searchField = new JSearchField();
        searchField.setLocation(20, 20);
        searchField.setSize(180, 30);
        add(searchField);

        searchButton = new JButton("Search");
        searchButton.setLocation(215, 20);
        searchButton.setSize(90, 30);
        searchButton.addActionListener(lis);
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
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(new SelectionListener());
        resultPanel = new JScrollPane(resultTable);
        resultPanel.setLocation(20, 60);
        resultPanel.setSize(400, 300);
        add(resultPanel);

        cityLabel = new JLabel("City");
        cityLabel.setLocation(440, 120);
        cityLabel.setSize(90, 30);
        add(cityLabel);

        cityField = new JEditField("City");
        cityField.setLocation(490, 120);
        cityField.setSize(90, 30);
        add(cityField);

        nameLabel = new JLabel("Name");
        nameLabel.setLocation(440, 170);
        nameLabel.setSize(90, 30);
        add(nameLabel);

        nameField = new JEditField("Name");
        nameField.setLocation(490, 170);
        nameField.setSize(90, 30);
        add(nameField);

        countryLabel = new JLabel("Country");
        countryLabel.setLocation(440, 220);
        countryLabel.setSize(90, 30);
        add(countryLabel);

        countryField = new JEditField("Country");
        countryField.setLocation(490, 220);
        countryField.setSize(90, 30);
        add(countryField);

        addressLabel = new JLabel("Address");
        addressLabel.setLocation(440, 270);
        addressLabel.setSize(90, 30);
        add(addressLabel);

        addressField = new JEditField("Address");
        addressField.setLocation(490, 270);
        addressField.setSize(90, 30);
        add(addressField);

        addButton = new JButton("Add");
        addButton.setLocation(600, 320);
        addButton.setSize(90, 30);
        add(addButton);

        showProgramsCombo = new JComboBox(programs);
        showProgramsCombo.setLocation(440, 320);
        showProgramsCombo.setSize(150, 30);
        add(showProgramsCombo);

        updateButton = new JButton("Update");
        updateButton.setLocation(440, 20);
        updateButton.setSize(90, 30);
        updateButton.addActionListener(edit);
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setLocation(540, 20);
        deleteButton.setSize(90, 30);
        deleteButton.addActionListener(edit);
        add(deleteButton);

        selectedInstituteLabel = new JLabel("Selected institute:");
        selectedInstituteLabel.setLocation(440, 70);
        selectedInstituteLabel.setSize(150, 30);
        add(selectedInstituteLabel);

    }

    class InstituteEditListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedInstitute == null) {
                return;
            }
            if (e.getSource() == updateButton) {
                int update = JOptionPane.showOptionDialog(SearchInstituteFrame.this, "Institute has been updated", "Updated", JOptionPane.PLAIN_MESSAGE, 
                         JOptionPane.INFORMATION_MESSAGE, null, null, null);
                if (update == JOptionPane.OK_OPTION) {
                selectedInstitute.updateInstitute(); 
                }
            }
                if (e.getSource() == deleteButton) {
                // show confirm dialog and confirm that the user choose "OK"
                int choice = JOptionPane.showConfirmDialog(SearchInstituteFrame.this, "Are you sure you want to delete this institute?",
                        "Delete institute", JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    selectedInstitute.deleteInstitute();
                    
                }
            }
        }
    }
        
    
    
    private void search(String filter, String conditionColumn) {
        ArrayList<dbapplication.institute.Institute> institute = dbapplication.institute.Institute.searchInstitute(filter, conditionColumn);
        resultModel.setResults(institute);
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
                    "Selected institute: " + selectedInstitute.getOrgid());

            nameField.setText(selectedInstitute.getName());

            cityField.setText(selectedInstitute.getCity());

            countryField.setText(selectedInstitute.getCountry());

            addressField.setText(selectedInstitute.getAddress());

        }
    }

}
