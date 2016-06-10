package dbapplication.student;

import dbapplication.JEditField;
import dbapplication.institute.Institute;
import dbapplication.institute.SelectInstituteDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author fabio
 * Enables user to select the institute and program(s) and then search on students
 */
public class SearchFilterDialog extends JDialog {
    private Institute selectedInstitute = null;
    
    private JTextField selectedInstituteLabel;
    private JButton selectInstituteButton, cancelButton, okButton;
    
    public SearchFilterDialog(JFrame owner) {
        super(owner, true);
        setupFrame();     
        createComponents();
    }

    SearchFilterDialog() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void setupFrame() {
        setSize(700,600);
        setTitle("Select institute and program(s)");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(null);
    }

    private void createComponents() {
        selectedInstituteLabel = new JEditField("Institute");
        selectedInstituteLabel.setEnabled(false);
        selectedInstituteLabel.setBounds(20, 20, 200, 30);
        add(selectedInstituteLabel);
        selectInstituteButton = new JButton("...");
        selectInstituteButton.setBounds(220, 20, 70, 30);
        add(selectInstituteButton);
        CloseDialogListener closeLis = new CloseDialogListener();
        okButton = new JButton("OK");
        okButton.setBounds(610, 550, 70, 30);
        okButton.addActionListener(closeLis);
        add(okButton);
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(520, 550, 70, 30);
        cancelButton.addActionListener(closeLis);
        add(cancelButton);
    }

    public Institute getSelectedInstitute() {
        return selectedInstitute;
    }
    
    class CloseDialogListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == cancelButton) {
                selectedInstitute = null;
            }
            dispose();
        }
        
    }    
    
    private class SelectInstituteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SelectInstituteDialog dlg = new SelectInstituteDialog((JFrame)getOwner(), null);
            dlg.setVisible(true);
            // pauses until dialog is closed
            Institute institute = dlg.getSelectedInstitute();
            if(institute != null) {
                selectedInstituteLabel.setText(institute.getName());
                selectedInstitute = institute;
            }
        }     
    }
}
