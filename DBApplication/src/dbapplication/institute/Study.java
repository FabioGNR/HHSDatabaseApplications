package dbapplication.institute;
import dbapplication.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/** 
 *
 * @author Sishi
 */
public class Study {
    private String code, org_id, contactperson;
    private String[] cellData;
    
    public Study(ResultSet result) throws SQLException
    {
        code = result.getString("code");
        org_id = result.getString("org_id");
        contactperson = result.getString("contactperson");
        
        cellData = new String[] {contactperson, org_id};
    }
    
    public static ArrayList<Study> searchStudy(String filter, String conditionColumn) {
        ArrayList<Study> study = new ArrayList<>();
        try {
            // column names can't be set dynamically with preparedstatement
            // luckily conditionColumn isn't user input
            PreparedStatement stat = DBConnection.getConnection().prepareStatement(
                    "SELECT * FROM study WHERE `"+conditionColumn+"` LIKE ?");
            
            stat.setString(1, "%"+filter+"%");
            ResultSet results = stat.executeQuery();
            while(results.next()) {
                study.add(new Study(results));
            }
            results.close();
            stat.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return study;
    }
    
    public static void insertStudy(String code, 
                String org_id, String contactperson) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO study "
                        + "(code, org_id, contactperson) "
                        + "VALUES (?,?,?)");
            statement.setString(1, code);
            statement.setString(2, org_id);
            statement.setString(3, contactperson);
           
            statement.executeUpdate();
            statement.close();
            System.out.println("preparedstatement werkt");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
        }
    }
    public boolean updateStudy(String code, 
                String org_id, String contactperson){
        
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE study SET code = ?, org_id = ?, contactperson = ? WHERE code = ?");
            
            statement.setString(1, code);
            statement.setString(2, org_id);
            statement.setString(3, contactperson);   
            statement.setString(4, code);
            statement.executeUpdate();
            statement.close();
            
            
            System.out.println("Preparedstatement werkt");
        } catch (SQLException error) {
            
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
            return false;
        }
        return true;
    }
    
    
    public boolean deleteStudy() {
        
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM study WHERE code = ?");
            
            statement.setString(1, code);
            statement.executeUpdate();
            statement.close();
            
            System.out.println("preparedstatement werkt");
        } catch (SQLException error) {
            
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
            return false;
        }
        return true;
        
    }
    
   
    
    public String getDataAt(int cell) {
        return cellData[cell];
    }

    public String getCode() {
        return code;
    }
    
    public String getOrg_id() {
        return org_id;
    }

    public String getContactperson() {
        return contactperson;
    }
    
    
    }

    
    
