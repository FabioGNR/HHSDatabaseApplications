package dbapplication.institute;

import dbapplication.JEditField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author jordain & sishi
 */
public class RegisterStudyDialog extends JDialog {

    private JTextField codeField;
    private JTextField emailField;
    private JTextField numberField;

    private Study newStudy;
    
    private JButton registerButton;

    public RegisterStudyDialog(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(300, 300);
        setTitle("Register study");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
    }

    private void createComponents() {
        codeField = new JEditField("Name");
        codeField.setLocation(20, 20);
        codeField.setSize(150, 30);
        add(codeField);

        emailField = new JEditField("Contactperson e-mail");
        emailField.setLocation(20, 70);
        emailField.setSize(150, 30);
        add(emailField);

        numberField = new JEditField("Contactperson number");
        numberField.setLocation(20, 120);
        numberField.setSize(150, 30);
        add(numberField);

        registerButton = new JButton("Register");
        registerButton.setLocation(20, 170);
        registerButton.setSize(90, 30);
        registerButton.addActionListener(new RegisterButtonListener());
        add(registerButton);
    }

    public Study getNewStudy() {
        return newStudy;
    }

    private class RegisterButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String code, email, nr;

            code = codeField.getText();
            if (code.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterStudyDialog.this,
                        "Code cannot be a Empty", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                 return;
            }
             if (code.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(RegisterStudyDialog.this,
                        "Code cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
               
                 return;
             }
            email = emailField.getText();
            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(RegisterStudyDialog.this,
                        "Email must be in format of a@b.ccc", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterStudyDialog.this,
                        "Email cannot be empty", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            nr = numberField.getText();
            if (nr.isEmpty()) {
                JOptionPane.showMessageDialog(RegisterStudyDialog.this,
                        "Phone number must be entered", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                return;
            }
             if (!nr.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(RegisterStudyDialog.this,
                        "Number cannot have letters", "Incorrect input", JOptionPane.WARNING_MESSAGE);
               
                 return;
             }
            newStudy = new Study(code, email, nr);
            dispose();
        }
    }
}
