package dbapplication.institute;

import dbapplication.JEditField;
import dbapplication.JSearchField;
import dbapplication.SearchFilter;
import dbapplication.program.SelectStudyDialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
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
    private JTextField studyField;

    private JLabel cityLabel;
    private JLabel nameLabel;
    private JLabel countryLabel;
    private JLabel addressLabel;
    private JLabel selectedInstituteLabel;
    private JLabel isBusinessLabel;

    private JButton searchButton;
    private JButton showButton;
    private JButton updateButton;
    private JButton deleteButton;

    private JComboBox searchConditionCombo;

    private JRadioButton yesRadio;
    private JRadioButton noRadio;

    private JTable resultTable;
    private JScrollPane resultPanel;
    private InstituteTableModel resultModel;

    private String selectedStudy;
    private Institute selectedInstitute = null;

    public SearchInstituteFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
        search("", "name");
    }

    private void setupFrame() {
        setSize(700, 480);
        setTitle("Search Institute");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);

    }

    private void createComponents() {
        SearchListener lis = new SearchListener();
        InstituteEditListener edit = new InstituteEditListener();
        SelectStudyListener studyLis = new SelectStudyListener();
        SwitchInstituteListener switchLis = new SwitchInstituteListener();

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

        selectedInstituteLabel = new JLabel("Selected institute:");
        selectedInstituteLabel.setLocation(440, 20);
        selectedInstituteLabel.setSize(200, 30);
        add(selectedInstituteLabel);

        cityLabel = new JLabel("City");
        cityLabel.setLocation(440, 70);
        cityLabel.setSize(90, 30);
        add(cityLabel);

        cityField = new JEditField("City");
        cityField.setLocation(490, 70);
        cityField.setSize(120, 30);
        add(cityField);

        nameLabel = new JLabel("Name");
        nameLabel.setLocation(440, 120);
        nameLabel.setSize(90, 30);
        add(nameLabel);

        nameField = new JEditField("Name");
        nameField.setLocation(490, 120);
        nameField.setSize(120, 30);
        add(nameField);

        countryLabel = new JLabel("Country");
        countryLabel.setLocation(440, 170);
        countryLabel.setSize(90, 30);
        add(countryLabel);

        countryField = new JEditField("Country");
        countryField.setLocation(490, 170);
        countryField.setSize(120, 30);
        add(countryField);

        addressLabel = new JLabel("Address");
        addressLabel.setLocation(440, 220);
        addressLabel.setSize(90, 30);
        add(addressLabel);

        addressField = new JEditField("Address");
        addressField.setLocation(490, 220);
        addressField.setSize(120, 30);
        add(addressField);

        isBusinessLabel = new JLabel("Business?");
        isBusinessLabel.setLocation(440, 270);
        isBusinessLabel.setSize(100, 30);
        add(isBusinessLabel);

        yesRadio = new JRadioButton("Yes");
        yesRadio.setLocation(510, 270);
        yesRadio.setSize(50, 30);
        yesRadio.addActionListener(switchLis);
        add(yesRadio);

        noRadio = new JRadioButton("No");
        noRadio.setLocation(560, 270);
        noRadio.setSize(50, 30);
        noRadio.addActionListener(switchLis);
        add(noRadio);

        ButtonGroup group = new ButtonGroup();
        group.add(yesRadio);
        group.add(noRadio);

        studyField = new JEditField("Studies");
        studyField.setLocation(440, 320);
        studyField.setSize(70, 30);
        studyField.setEnabled(false);
        add(studyField);
        studyField.setVisible(false);

        showButton = new JButton("Show studies");
        showButton.setLocation(520, 320);
        showButton.setSize(120, 30);
        add(showButton);
        showButton.addActionListener(studyLis);
        showButton.setVisible(false);

        updateButton = new JButton("Update");
        updateButton.setLocation(440, 370);
        updateButton.setSize(90, 30);
        updateButton.addActionListener(edit);

        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setLocation(540, 370);
        deleteButton.setSize(90, 30);
        deleteButton.addActionListener(edit);

        add(deleteButton);

    }

    private class SelectStudyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            SelectStudyDialog dlg = new SelectStudyDialog((JFrame) getOwner(), SelectStudyDialog.ProgramType.studyProgram);
            dlg.setVisible(true);
            // pauses until dialog is closed
            Study study = dlg.getSelectedStudy();
            if (study != null) {
                //uniField.setText(getName());
                selectedStudy = study.getCode();
            }
        }
    }

    private class SwitchInstituteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean showExchangeFields = noRadio.isSelected();
            studyField.setVisible(showExchangeFields);
            showButton.setVisible(showExchangeFields);
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
            if (address.isEmpty() || address.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(SearchInstituteFrame.this,
                        "addres cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
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
                        //refresh
                        int selectedIndex = searchConditionCombo.getSelectedIndex();
                        SearchFilter selectedFilter = (SearchFilter) searchConditionCombo.getItemAt(selectedIndex);
                        search(searchField.getText(), selectedFilter.getColumnName());
                    }
                }
            }
            if (e.getSource() == deleteButton) {
                // show confirm dialog and confirm that the user choose "OK"
                if (city == null || address == null || name == null || country == null) {

                    JOptionPane.showMessageDialog(SearchInstituteFrame.this, "Update of institute failed", "Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    int choice = JOptionPane.showConfirmDialog(SearchInstituteFrame.this, "Are you sure you want to delete this institute?",
                            "Delete institute", JOptionPane.OK_CANCEL_OPTION);
                    if (choice == JOptionPane.OK_OPTION) {
                        selectedInstitute.deleteInstitute();
                        //refresh
                        int selectedIndex = searchConditionCombo.getSelectedIndex();
                        SearchFilter selectedFilter = (SearchFilter) searchConditionCombo.getItemAt(selectedIndex);
                        search(searchField.getText(), selectedFilter.getColumnName());

                    }

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
                    "Selected institute: " + selectedInstitute.getName());

            nameField.setText(selectedInstitute.getName());

            cityField.setText(selectedInstitute.getCity());

            countryField.setText(selectedInstitute.getCountry());

            addressField.setText(selectedInstitute.getAddress());

        }
    }

}
