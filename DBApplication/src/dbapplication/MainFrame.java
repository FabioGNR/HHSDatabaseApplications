package dbapplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import dbapplication.student.*;
import dbapplication.program.*;
import dbapplication.institute.*;
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
        Student, Institute, Program
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
        setSize(300, 300);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("International office Exchange students systeem"); 
    }

    private void createComponents() {
        JButton studentButton, instituteButton, programButton;

        studentButton = new JButton("Manage Students");
        instituteButton = new JButton("Manage Institutes");
        programButton = new JButton("Manage programs");

        // set buttonsize
        instituteButton.setSize(200, 50);
        programButton.setSize(200, 50);
        studentButton.setSize(200, 50);

        // set buttonLocations
        instituteButton.setLocation(50, 50);
        studentButton.setLocation(50, 110);
        programButton.setLocation(50, 170);

        // add action listeners
        instituteButton.addActionListener(new SelectionListener(new SearchInstituteFrame(this), new RegisterInstituteFrame(this)));
        studentButton.addActionListener(new SelectionListener(new SearchStudentFrame(this), new RegisterStudentFrame(this)));
        programButton.addActionListener(new SelectionListener(new SearchProgramFrame(this), new RegisterProgramFrame(this)));

        //add to Frame
        add(instituteButton);
        add(studentButton);
        add(programButton);

    }

    class SelectionListener implements ActionListener {

        private JDialog searchDialog, registerDialog;

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

    public static void MaakConnectie() {
        DBConnectie.setUsernameAndURL();
        Connection Connectie = null;
        Statement stat = null;

        try {
            Connectie = DBConnectie.getConnection();
            System.out.println("Connection Succesfull");

            //Statements: Voor het testen dat de connectie werkt
            stat = Connectie.createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM student ");

            while (result.next()) {
                System.out.println(result.getString("Name"));
                System.out.println(result.getString("Student_id"));
            }

        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());

        } finally {

            if (Connectie != null) {
                try {
                    Connectie.close();
                } catch (SQLException error) {
                }
                if (stat != null) {
                    try {
                        stat.close();
                    } catch (SQLException error) {
                    }
                }

            }
        }
    }
}
