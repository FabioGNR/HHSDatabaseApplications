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
    private String code, email; 
    private int phone_number;
    private String[] cellData;
    
    public Study(ResultSet result) throws SQLException
    {
        code = result.getString("code");
        email = result.getString("email");
        phone_number = result.getInt("phone number");
        
        refreshCellData();
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
                String email, int phone_number) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO study "
                        + "(code, email, phone number) "
                        + "VALUES (?,?,?)");
            statement.setString(1, code);
            statement.setString(2, email);
            statement.setInt(3, phone_number);
           
            statement.executeUpdate();
            statement.close();
            System.out.println("preparedstatement werkt");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
        }
    }
    public boolean updateStudy(String code, 
                String email, int phone_number){
        
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE study SET code = ?, email = ?, phone number = ? WHERE code = ?");
            
            statement.setString(1, code);
            statement.setString(2, email);
            statement.setInt(3, phone_number);   
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
    
    public void refreshCellData() {
        String number = String.format("^\\d+$", phone_number);
        cellData = new String[] {code, email, number};
    }
    
   
    
    public String getDataAt(int cell) {
        return cellData[cell];
    }

    public String getCode() {
        return code;
    }
    
    public String getEmail() {
        return email;
    }

    public int getPhone_number() {
        return phone_number;
    }
    
    
    }

    
    
