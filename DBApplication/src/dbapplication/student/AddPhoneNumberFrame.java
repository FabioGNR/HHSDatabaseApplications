package dbapplication.student;

import dbapplication.JEditField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author fabio
 */
public class AddPhoneNumberFrame extends JDialog {
    private JEditField numberField;
    private JCheckBox cellularBox;
    private JButton okButton, cancelButton;
    private PhoneNumber newPhoneNumber = null;
    
    
    public AddPhoneNumberFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(300, 300);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Add phone number");
    }

    private void createComponents() {
        numberField = new JEditField("Phone number");
        numberField.setBounds(50, 50, 200, 30);
        add(numberField);
        cellularBox = new JCheckBox("Cellular");
        cellularBox.setBounds(50, 100, 200, 30);
        add(cellularBox);
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(50, 200, 95, 30);
        cancelButton.addActionListener(new CancelButtonListener());
        add(cancelButton);
        okButton = new JButton("OK");
        okButton.setBounds(155, 200, 95, 30);
        okButton.addActionListener(new OKButtonListener());
        add(okButton);
    }

    public PhoneNumber getNewPhoneNumber() {
        return newPhoneNumber;
    }
    
    
    class OKButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String number = numberField.getText();
            if(number.isEmpty()) {
                JOptionPane.showMessageDialog(AddPhoneNumberFrame.this, 
                        "Please enter a phone number", "Missing number", JOptionPane.WARNING_MESSAGE);
                return;
            }   
            newPhoneNumber = new PhoneNumber(number, cellularBox.isSelected());
            AddPhoneNumberFrame.this.dispose();
        }      
    }
    
    class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            newPhoneNumber = null;
            AddPhoneNumberFrame.this.dispose();
        }
    }
    
}
