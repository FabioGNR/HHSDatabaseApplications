package dbapplication;

import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class DBApplication {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
 
        
        // connectie maken met database
        if(!DBConnection.connect()) {
            new JOptionPane("Connection to database failed", 
                    JOptionPane.ERROR_MESSAGE, JOptionPane.OK_OPTION)
                    .setVisible(true);
            return;
        }
      
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally{
            JFrame frame = new MainFrame(); 
        }
    }
}
    
