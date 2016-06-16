package dbapplication.student;

import dbapplication.DatabaseTableModel;
import dbapplication.JEditField;
import dbapplication.program.ExProgram;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
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
 * @author Fabio
 */
public class EnrollmentFrame extends JDialog {
    private JTable enrollmentsTable;
    private Enrollment selectedEnrollment = null;
    private DatabaseTableModel<Enrollment> tableModel;
    private JScrollPane tablePanel;
    private JEditField dateField, creditsField, programField;
    private JButton okButton, addButton, saveButton, deleteButton;
    private Student student;
    
    public EnrollmentFrame (JFrame owner, 
            ArrayList<Enrollment> enrollments, Student student) {
        super(owner, true);
        setupFrame();
        createComponents();
        this.student = student;
        tableModel.setItems(enrollments);
    }

    private void setupFrame() {
        setSize(510, 310);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Student enrollments");
    }

    private void createComponents() {
        enrollmentsTable = new JTable();
        tableModel = new DatabaseTableModel(new String[]{"Program", 
            "Acquired ECS", "Max ECS", "Registration Date"});
        enrollmentsTable.setModel(tableModel);
        enrollmentsTable.setPreferredScrollableViewportSize(new Dimension(300, 300));
        enrollmentsTable.setFillsViewportHeight(true);
        enrollmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        enrollmentsTable.getSelectionModel().addListSelectionListener(new SelectionListener());
        tablePanel = new JScrollPane(enrollmentsTable);
        tablePanel.setBounds(10, 10, 320, 230);
        add(tablePanel);

        programField = new JEditField("Program");
        programField.setBounds(340, 10, 130, 30);
        programField.setEditable(false);
        add(programField);
        dateField = new JEditField("Registration Date");
        dateField.setBounds(340, 50, 130, 30);
        add(dateField);
        creditsField = new JEditField("Acquired Credits");
        creditsField.setBounds(340, 90, 130, 30);
        add(creditsField);

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
        addButton.addActionListener(new EnrollStudentListener());
        add(addButton);

        okButton = new JButton("OK");
        okButton.setBounds(260, 250, 70, 30);
        okButton.addActionListener(new OKButtonListener());
        add(okButton);
    }
    
public ArrayList<Enrollment> getEnrollments() {
        return tableModel.getAll();
    }
    
    private class EnrollStudentListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            EnrollStudentFrame frame = new EnrollStudentFrame(
                    (JFrame)EnrollmentFrame.this.getOwner(), student);
            frame.setVisible(true);
            Enrollment newEnrollment = frame.getInsertedEnrollment();
            if(newEnrollment != null) {
                tableModel.getAll().add(newEnrollment);
                tableModel.fireTableDataChanged();
                // select newest row
                enrollmentsTable.setRowSelectionInterval(
                        tableModel.getRowCount()-1, tableModel.getRowCount()-1);
            }
        }
        
    }

    private class DeleteButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedEnrollment == null) {
                return;
            }
            // show confirm dialog and confirm that the user choose "OK"
            int choice = JOptionPane.showConfirmDialog(EnrollmentFrame.this, 
                    "Are you sure you want to delete this enrollment?",
                    "Delete enrollment", JOptionPane.OK_CANCEL_OPTION);
            if (choice == JOptionPane.OK_OPTION) {
                // remove selected number from list
                tableModel.getAll().remove(selectedEnrollment);
                // update table
                tableModel.fireTableDataChanged();
            }
        }
    }

    private class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedEnrollment == null) {
                return;
            }
            ExProgram selectedProgram = selectedEnrollment.getProgram();
            String sCredits = creditsField.getText();
            if(!sCredits.matches("^\\d+$")) {
                JOptionPane.showMessageDialog(EnrollmentFrame.this.getOwner(), 
                        "Acquired credits must be a number", 
                        "Missing acquired credit", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int credits = Integer.parseInt(sCredits);
            if(credits > selectedProgram.getMaxCredits()) {
                JOptionPane.showMessageDialog(EnrollmentFrame.this.getOwner(), 
                        "Acquired credits must be below or equal to max credits for selected program", 
                        "Acquired credits exceed max credits", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String sDate = dateField.getText();
            Date date;
            try {
                date = Date.valueOf(sDate);
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(EnrollmentFrame.this.getOwner(), 
                        "Date must be in the format of yyyy-mm-dd", 
                        "Incorrect registration date", JOptionPane.WARNING_MESSAGE);
                return;
            }
            selectedEnrollment.setRegistrationDate(date);
            selectedEnrollment.setAcquiredCredits(credits);
            if(!selectedEnrollment.save()) {
                JOptionPane.showMessageDialog(EnrollmentFrame.this.getOwner(), 
                        "Failed to save enrollment", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            selectedEnrollment.refreshCellData();
        }
    }

    private class OKButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            EnrollmentFrame.this.dispose();
        }
    }

    private class SelectionListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow = enrollmentsTable.getSelectedRow();
            if (selectedRow < 0) {
                selectedEnrollment = null;
                creditsField.setText("");
                dateField.setText("");
                programField.setText("");
                return; // selection cleared
            }
            selectedEnrollment = tableModel.get(selectedRow);
            creditsField.setText(selectedEnrollment.getAcquiredCredits()+"");
            dateField.setText(selectedEnrollment.getRegistrationDate().toString());
            programField.setText(selectedEnrollment.getProgram().getName());
        }
    }
}
