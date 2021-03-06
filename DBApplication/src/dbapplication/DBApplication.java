package dbapplication;

import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class DBApplication {

    /*
        SE-2: Groep 3
            Omar Mohammad 08068593
            Sishi Hu 15049825
            Jordain Gijsbertha 15074595
            Raymond Lee 11106298
            Fabio Waljaard 15086836
     */

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {

        // connectie maken met database en error laten zien als t faalt
        if (!DBConnection.connect()) {
            new JOptionPane("Connection to database failed",
                    JOptionPane.ERROR_MESSAGE, JOptionPane.OK_OPTION)
                    .setVisible(true);
            return;
        }

        // het thema proberen in te stellen en dan de MainFrame laten zien
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            JFrame frame = new MainFrame();
        }
    }
}
