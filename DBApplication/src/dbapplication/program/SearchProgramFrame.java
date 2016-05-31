package dbapplication.program;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author omari_000
 */
public class SearchProgramFrame extends JDialog {
    public SearchProgramFrame(JFrame owner) {
        super(owner, true);
        setupFrame();      
        createComponents(); 
    }

    private void setupFrame() {
        setSize(510, 150);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Search Program");
    }

    private void createComponents() { 
    }
}
