package dbapplication.institute;

import dbapplication.JEditField;
//import dbapplication.program.SelectProgramDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 *
 * @author omari_000
 */
public class RegisterInstituteFrame extends JDialog {

    private JTextField nameField;
    private JTextField cityField;
    private JTextField countryField;
    private JTextField addressField;
    private JTextField programField;

    private JButton registerButton;
    private JButton addButton;
    private JButton selectProgramButton;
    
    private JRadioButton yesRadio;
    private JRadioButton noRadio;
    private JLabel isBusinessLabel;
    
    private static String[] programs = {"Building process", "Business intelligence", "Database design", "Financial accounting", "Marketing", "Mechanica", "Programming"};

    public RegisterInstituteFrame(JFrame owner) {
        super(owner, true);
        setupFrame();
        createComponents();
    }

    private void setupFrame() {
        setSize(510, 300);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("Register Institute");
    }

    private void createComponents() {
        SwitchInstituteListener switchLis = new SwitchInstituteListener();
        
        nameField = new JEditField("Name");
        nameField.setLocation(20, 20);
        nameField.setSize(180, 30);
        add(nameField);

        cityField = new JEditField("City");
        cityField.setLocation(20, 70);
        cityField.setSize(180, 30);
        add(cityField);

        countryField = new JEditField("Country");
        countryField.setLocation(20, 120);
        countryField.setSize(180, 30);
        add(countryField);

        addressField = new JEditField("Address");
        addressField.setLocation(20, 170);
        addressField.setSize(180, 30);
        add(addressField);

        // de buttons:
        AddButtonListener lis = new AddButtonListener();
        registerButton = new JButton("Register");
        registerButton.setLocation(400, 180);
        registerButton.setSize(90, 60);
        registerButton.addActionListener(lis);
        add(registerButton);

        isBusinessLabel = new JLabel();
        isBusinessLabel.setLocation(230, 20);
        isBusinessLabel.setSize(100, 30);
        isBusinessLabel.setText("Business?");
        add(isBusinessLabel);

        yesRadio = new JRadioButton();
        yesRadio.setLocation(300, 20);
        yesRadio.setSize(100, 30);
        yesRadio.setText("Yes");
        yesRadio.addActionListener(switchLis);
        add(yesRadio);

        noRadio = new JRadioButton();
        noRadio.setLocation(300, 50);
        noRadio.setSize(100, 30);
        noRadio.setText("No");
        noRadio.addActionListener(switchLis);
        add(noRadio);

        ButtonGroup group = new ButtonGroup();
        group.add(yesRadio);
        group.add(noRadio);
        
        programField = new JEditField("Programs");
        programField.setLocation(20, 220);
        programField.setSize(100, 30);
        //programField.setEnabled(false);
        add(programField);
        //programField.setVisible(false);
        
        selectProgramButton = new JButton("...");
        selectProgramButton.setLocation(150, 220);
        selectProgramButton.setSize(100, 30);
        add(selectProgramButton);
        //selectProgramButton.addActionListener(new SelectProgramListener());
       // selectProgramButton.setVisible(false); 

    }
    
    /*private class SelectProgramListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
         
            SelectProgramDialog dlg = new SelectProgramDialog( (JFrame)getOwner(), SelectProgramDialog.ProgramType.Internship);
            dlg.setVisible(true);
        }
            SelectInstituteDialog dlg = new SelectInstituteDialog((JFrame)getOwner(), SelectInstituteDialog.InstituteType.University);
            dlg.setVisible(true);
            // pauses until dialog is closed
            Institute institute = dlg.getSelectedInstitute();
            if(institute != null)
            {
                uniField.setText(institute.getName());
                selectedInstCode = institute.getOrgid();
            }
        }
    }*/    
    
    private class SwitchInstituteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean showExchangeFields = noRadio.isSelected();
            programField.setVisible(showExchangeFields);
        }       
    }
    

    private class AddButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            String city, name, country, address;
            int is_business;
            city = cityField.getText();
            if (city.isEmpty()) {
                city = null;
            }
            name = nameField.getText();
            if (name.isEmpty()) {
                name = null;
            }
            country = countryField.getText();
            if (country.isEmpty()) {
                country = null;
            }
            address = addressField.getText();
            if (address.isEmpty()) {
                address = null;
            }
            if (event.getSource()==addButton){
                
            }
            // is_business is 1 als yes is geselecteerd, anders 0
            is_business = yesRadio.isSelected() ? 1 : 0;

            Institute.insertInstitute(city, name, country, address, is_business);
        }
        
        
    }
}
