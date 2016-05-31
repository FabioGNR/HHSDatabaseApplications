package dbapplication.student;

import dbapplication.JSearchField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author omari_000
 */
public class SearchStudentFrame extends JDialog{
    JTextField searchField;
    JButton searchButton;
    JComboBox searchConditionCombo;
    public SearchStudentFrame(JFrame owner) {
        super(owner, true);
        setupFrame();     
        createComponents();
    }
    
    private void setupFrame() {
        setSize(500,400);
        setTitle("Search Students");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);        
    }
    
    private void createComponents() {
        searchField = new JSearchField();
        searchField.setLocation(20, 20);
        searchField.setSize(180, 30);
        add(searchField);
        searchButton = new JButton("Search");
        searchButton.setLocation(220, 20);
        searchButton.setSize(90, 30);
        add(searchButton);
        searchConditionCombo = new JComboBox(new String[]{"Student ID", "Name", "Email"});
        searchConditionCombo.setLocation(340, 20);
        searchConditionCombo.setSize(100, 30);
        add(searchConditionCombo);
    }
}
