package dbapplication.institute;

import dbapplication.JEditField;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author Sishi
 */
public class RegisterStudyDialog extends JDialog {

    public enum StudyType {Study}
    private final StudyType requiredType;
    private Institute registerStudy = null;
    
    private JTextField codeField;
    
    private JButton registerButton;
    
    public RegisterStudyDialog(JFrame owner, StudyType type) {
        super(owner, true);
        requiredType = type;
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(400,400);
        setTitle("Register study");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
    }

    private void createComponents() {
        codeField = new JEditField("Code");
        codeField.setLocation(20, 20);
        codeField.setSize(90, 30);
        add(codeField);
        
        registerButton = new JButton("Register");
        registerButton.setLocation(50, 50);
        registerButton.setSize(90, 30);
        add(registerButton);
    }
    
}
