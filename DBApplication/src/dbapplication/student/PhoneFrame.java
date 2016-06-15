package dbapplication.student;

import dbapplication.DatabaseTableModel;
import dbapplication.JEditField;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author fabio
 */
public class PhoneFrame extends JDialog {

    private JTable numbersTable;
    private PhoneNumber selectedNumber = null;
    private DatabaseTableModel<PhoneNumber> tableModel;
    private JScrollPane tablePanel;
    private JEditField numberField;
    private JCheckBox cellularBox;
    private JButton okButton, addButton, saveButton, deleteButton;

    public PhoneFrame(JFrame owner, ArrayList<PhoneNumber> numbers) {
        super(owner, true);
        setupFrame();
        createComponents();
        tableModel.setItems(numbers);
    }

    private void setupFrame() {
        setSize(510, 310);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Student phone numbers");
    }

    private void createComponents() {
        numbersTable = new JTable();
        tableModel = new DatabaseTableModel(new String[]{"Number", "Cellular"});
        numbersTable.setModel(tableModel);
        numbersTable.setPreferredScrollableViewportSize(new Dimension(300, 300));
        numbersTable.setFillsViewportHeight(true);
        numbersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        numbersTable.getSelectionModel().addListSelectionListener(new SelectionListener());
        tablePanel = new JScrollPane(numbersTable);
        tablePanel.setBounds(10, 10, 320, 230);
        add(tablePanel);

        numberField = new JEditField("Number");
        numberField.setBounds(340, 10, 130, 30);
        add(numberField);
        cellularBox = new JCheckBox("Cellular");
        cellularBox.setBounds(340, 50, 130, 30);
        add(cellularBox);

        saveButton = new JButton("Save");
        saveButton.setBounds(340, 210, 60, 30);
        saveButton.addActionListener(new SaveButtonListener());
        add(saveButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(410, 210, 80, 30);
        deleteButton.addActionListener(new DeleteButtonListener());
        add(deleteButton);

        addButton = new JButton("Add New");
        addButton.setBounds(10, 250, 80, 30);
        add(addButton);

        okButton = new JButton("OK");
        okButton.setBounds(260, 250, 70, 30);
        okButton.addActionListener(new OKButtonListener());
        add(okButton);
    }

    public ArrayList<PhoneNumber> getPhoneNumbers() {
        return tableModel.getAll();
    }

    private class DeleteButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedNumber == null) {
                return;
            }
            // show confirm dialog and confirm that the user choose "OK"
            int choice = JOptionPane.showConfirmDialog(PhoneFrame.this, "Are you sure you want to delete this number?",
                    "Delete number", JOptionPane.OK_CANCEL_OPTION);
            if (choice == JOptionPane.OK_OPTION) {
                // remove selected number from list
                tableModel.getAll().remove(selectedNumber);
                // update table
                tableModel.fireTableDataChanged();
            }
        }
    }

        private class SaveButtonListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedNumber == null) {
                    return;
                }
                String number = numberField.getText();
                boolean cellular = cellularBox.isSelected();
                selectedNumber.setNumber(number);
                selectedNumber.setCellular(cellular);
                selectedNumber.refreshCellData();
            }
        }

        private class OKButtonListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                PhoneFrame.this.dispose();
            }
        }

        private class SelectionListener implements ListSelectionListener {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = numbersTable.getSelectedRow();
                if (selectedRow < 0) {
                    selectedNumber = null;
                    numberField.setText("");
                    cellularBox.setSelected(false);
                    return; // selection cleared
                }
                selectedNumber = tableModel.get(selectedRow);
                numberField.setText(selectedNumber.getNumber());
                cellularBox.setSelected(selectedNumber.isCellular());
            }
        }
    }
