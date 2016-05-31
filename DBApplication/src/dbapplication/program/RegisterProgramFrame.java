package dbapplication.program;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author omari_000
 */
public class RegisterProgramFrame extends JDialog{
    public RegisterProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();      
        createComponents(); 
    }

    private void setupFrame() {
        setSize(510, 150);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Register Program");
    }

    private void createComponents() { 
    }
}
