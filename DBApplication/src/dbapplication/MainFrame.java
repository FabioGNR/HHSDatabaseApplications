package dbapplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import dbapplication.institute.*;
import dbapplication.overzicht.*;
import dbapplication.student.*;
import dbapplication.program.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JDialog;

/**
 *
 * @author omari_000
 */
public class MainFrame extends JFrame {

    enum ButtonType {
        Student, Institute, Program, Overzicht
    }

    public MainFrame() throws SQLException {
        setupFrame();
        createComponents();
        MaakConnectie();
        // setVisible moet op einde 
        // anders worden components niet zichtbaar
        setVisible(true);
    }

    private void setupFrame() {
        setSize(300, 350);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("International office Exchange students systeem");
    }

    private void createComponents() {
        JButton studentButton, instituteButton, programButton, overzichtButton;

        studentButton = new JButton("Manage Students");
        instituteButton = new JButton("Manage Institutes");
        programButton = new JButton("Manage programs");
        overzichtButton = new JButton("Overzichten");

        // set buttonsize
        instituteButton.setSize(200, 50);
        programButton.setSize(200, 50);
        studentButton.setSize(200, 50);
        overzichtButton.setSize(200, 50);

        // set buttonLocations
        instituteButton.setLocation(50, 50);
        studentButton.setLocation(50, 110);
        programButton.setLocation(50, 170);
        overzichtButton.setLocation(50, 230);

        // add action listeners
        instituteButton.addActionListener(new SelectionListener(new SearchInstituteFrame(this), new RegisterInstituteFrame(this)));
        studentButton.addActionListener(new SelectionListener(new SearchStudentFrame(this), new RegisterStudentFrame(this)));
        programButton.addActionListener(new SelectionListener(new SearchProgramFrame(this), new RegisterProgramFrame(this)));
        overzichtButton.addActionListener(new OverzichtListener(new CountryOverzicht(this), new CityOverzicht(this), new StudyOverzicht(this)));

        //add to Frame
        add(instituteButton);
        add(studentButton);
        add(programButton);
        add(overzichtButton);

    }

    class SelectionListener implements ActionListener {
        // deze listener geeft mogelijke dialogs door aan een choicelistener
        // en geeft die listener door aan choiceframe
        private final JDialog searchDialog, registerDialog;

        public SelectionListener(JDialog search, JDialog register) {
            searchDialog = search;
            registerDialog = register;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            ChoiceListener choiceListener = new ChoiceListener(searchDialog, registerDialog);
            ChoiceDialog choice = new ChoiceDialog(MainFrame.this, choiceListener);
        }
    }
    
    class OverzichtListener implements ActionListener {
        
        private final JDialog countryDialog, cityDialog, studyDialog;
        
        public OverzichtListener(JDialog country, JDialog city, JDialog study) {
            countryDialog = country;
            cityDialog = city;
            studyDialog = study;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            OverzichtChoiceListener choiceListener = new OverzichtChoiceListener(countryDialog, cityDialog, studyDialog);
            OverzichtChoiceDialog Choice = new OverzichtChoiceDialog(MainFrame.this, choiceListener);
        }
    }
    
    public static void MaakConnectie() {
        Connection Connectie = null;
        Statement stat = null;

        try {
            Connectie = DBConnection.getConnection();
            System.out.println("Connection Succesfull");

            //Statements: Voor het testen dat de connectie werkt
            stat = Connectie.createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM student ");

            while (result.next()) {
                //System.out.println(result.getString("Name"));
                //System.out.println(result.getString("Student_id"));
            }

        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());

        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException error) {
                }
            }
        }
    }
}
