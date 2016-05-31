package dbapplication.student;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author omari_000
 */
public class SearchStudentFrame extends JDialog{
    public SearchStudentFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
    }
    
    private void setupFrame() {
        setSize(500,400);
        setTitle("Search Students");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);        
    }
}
