package dbapplication.institute;

import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author omari_000
 */
public class SearchInstituteFrame extends JDialog {
      public SearchInstituteFrame(JFrame owner) {
        super(owner, true);
        setupFrame();      
        createComponents(); 
    }

    private void setupFrame() {
        setSize(510, 150);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Search Institute");
    }

    private void createComponents() { 
    }
}
