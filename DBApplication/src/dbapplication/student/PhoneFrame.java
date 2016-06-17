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
    private PhoneNumber[] existingNumbers;
    private DatabaseTableModel<PhoneNumber> tableModel;
    private JScrollPane tablePanel;
    private JEditField numberField;
    private JCheckBox cellularBox;
    private JButton okButton, cancelButton;
    private JButton addButton, saveButton, deleteButton;
    private Student student;

    public PhoneFrame(JFrame owner, ArrayList<PhoneNumber> numbers, Student student) {
        super(owner, true);
        setupFrame();
        createComponents();
        this.student = student;
        if(numbers == null) {
            numbers = new ArrayList<>();
        }
        tableModel.setItems(numbers);
        existingNumbers = numbers.toArray(new PhoneNumber[numbers.size()]);
    }

    public PhoneFrame(JFrame owner, ArrayList<PhoneNumber> numbers) {
        this(owner, numbers, null);
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
        addButton.addActionListener(new AddNumberListener());
        add(addButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(180, 250, 70, 30);
        cancelButton.addActionListener(new CancelListener());
        add(cancelButton);
        
        okButton = new JButton("OK");
        okButton.setBounds(260, 250, 70, 30);
        okButton.addActionListener(new OKButtonListener());
        add(okButton);
    }

    public ArrayList<PhoneNumber> getPhoneNumbers() {
        return tableModel.getAll();
    }
    
    private class AddNumberListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            AddPhoneNumberFrame frame = new AddPhoneNumberFrame((JFrame)PhoneFrame.this.getOwner());
            frame.setVisible(true);
            PhoneNumber newNumber = frame.getNewPhoneNumber();
            if(newNumber != null) {
                tableModel.getAll().add(newNumber);
                tableModel.fireTableDataChanged();
                // select newest row
                numbersTable.setRowSelectionInterval(
                        tableModel.getRowCount()-1, tableModel.getRowCount()-1);
            }
        }
        
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
            tableModel.fireTableDataChanged();
        }
    }

    private class CancelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // reset to old list
            ArrayList<PhoneNumber> newList = tableModel.getAll();
            newList.clear();
            for(int i = 0; i < existingNumbers.length; i++) {
                newList.add(existingNumbers[i]);
            }
            PhoneFrame.this.dispose();
        }        
    }
    
    private class OKButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(student != null) { // update our student
                ArrayList<PhoneNumber> numbers = tableModel.getAll();
                if(numbers.isEmpty()) {
                    JOptionPane.showMessageDialog(PhoneFrame.this, 
                            "Student must have at least one phone number",
                            "Missing phone number", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // save and add enrollments
                for(int i = 0; i < numbers.size();i ++) {
                    PhoneNumber number = numbers.get(i);
                    if(!number.existsInDB()) {
                        // insert new
                        PhoneNumber.insertNumber(student.getStudentid(), 
                                number.getNumber(), number.isCellular());
                        number.setExistsInDB(true);
                    }
                    else if(number.needsUpdate()) {
                        number.save();
                    }              
                }
                // remove enrollments by comparing to new list
                for(int i = 0; i < existingNumbers.length; i++) {
                    PhoneNumber number = existingNumbers[i];
                    if(!numbers.contains(number)) {
                        number.delete();
                    }           
                }
            }
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
