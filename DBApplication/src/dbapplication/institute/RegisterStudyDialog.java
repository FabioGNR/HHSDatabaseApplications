package dbapplication.institute;

import dbapplication.JEditField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Sishi
 */
public class RegisterStudyDialog extends JDialog {

    public enum StudyType {Study}
    private final StudyType requiredType;
    
    private JTextField codeField;
    private JTextField emailField;
    private JTextField numberField;
    
    private JButton registerButton;
    
    public RegisterStudyDialog(JFrame owner, StudyType type) {
        super(owner, true);
        requiredType = type;
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(300,300);
        setTitle("Register study");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
    }

    private void createComponents() {
        codeField = new JEditField("Code");
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
    
    private class RegisterButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String code, email, nr;
            
            code = codeField.getText();
            if (code.isEmpty() || code.matches(".*\\d+.*")) {
                JOptionPane.showMessageDialog(RegisterStudyDialog.this, 
                        "Code cannot be a number", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                
            }
            email = emailField.getText();
            if (email.isEmpty() || !email.contains("@")) {
                JOptionPane.showMessageDialog(RegisterStudyDialog.this, 
                        "Email must be in format of a@b.ccc", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                
            }
            nr = numberField.getText();
            if (nr.isEmpty() || nr.matches("^\\D+$")) {
                JOptionPane.showMessageDialog(RegisterStudyDialog.this, 
                        "Phone number cannot be a letter", "Incorrect input", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int nummer = Integer.parseInt(nr);
            
            if (e.getSource() == registerButton) {
                if (code == null || email == null || nr == null) {
                    JOptionPane.showMessageDialog(RegisterStudyDialog.this, 
                            "Registering of study failed", "Error", JOptionPane.WARNING_MESSAGE);
                } 
                else {
                    int register = JOptionPane.showOptionDialog(RegisterStudyDialog.this, 
                            "Study has been registerd", "Registerd", JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    
                    if (register == JOptionPane.OK_OPTION) {
                        Study.insertStudy(code, email, nummer);
                    }
                    
                }
            }
            
        }
        
    }
    
}
