package dbapplication.institute;

import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author fabio
 */
public class ContactPersonsFrame extends JDialog {
    private JTable resultTable;
    private JScrollPane resultPanel;
    private ContactPersonTableModel resultModel;
    
    public ContactPersonsFrame(JFrame owner, String study, String orgID) {
        super(owner, true);
        setupFrame();     
        createComponents();
        // fill JTable searching on empty filter
        
    }
    
    private void setupFrame() {
        setSize(700,600);
        setTitle("Study");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
    }
    
    private void createComponents() {
        resultTable = new JTable();
        resultTable.setLocation(0, 0);
        resultTable.setSize(400, 500);
        resultModel = new ContactPersonTableModel();
        resultTable.setModel(resultModel);
        resultTable.setPreferredScrollableViewportSize(new Dimension(400, 500));
        resultTable.setFillsViewportHeight(true);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(new ContactPersonsFrame.SelectionListener());
        resultPanel = new JScrollPane(resultTable);
        resultPanel.setLocation(20, 60);
        resultPanel.setSize(420, 500);
        add(resultPanel);
    }

    private static class SelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
       
}
