package dbapplication;

import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class DBApplication {

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     */
    public static void main(String[] args) throws SQLException {
 
        
        // De onderstaande zorgt voor problemen op mijn Mac(jordain), UB zo laten tot dat we een oplossingen kunnen vinden
        
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally{
            JFrame frame = new MainFrame(); 
        }
    }
}
    
