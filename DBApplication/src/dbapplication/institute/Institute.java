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
public class Institute {
    private String org_id, city, name, country, address, is_business;
    private String[] cellData;
    
    public Institute(ResultSet result) throws SQLException
    {
        org_id = result.getString("org_id");
        city = result.getString("city");
        name = result.getString("name");
        country = result.getString("country");
        address = result.getString("address");
        is_business = result.getString("is_business");
        cellData = new String[] {org_id, city, name, country, address, is_business};
    }
    
    public static ArrayList<Institute> searchInstitute(String filter, String conditionColumn) {
        ArrayList<Institute> institute = new ArrayList<>();
        try {
            // column names can't be set dynamically with preparedstatement
            // luckily conditionColumn isn't user input
            PreparedStatement stat = DBConnection.getConnection().prepareStatement(
                    "SELECT * FROM institute WHERE `"+conditionColumn+"` LIKE ?");
            stat.setString(1, "%"+filter+"%");
            ResultSet results = stat.executeQuery();
            while(results.next()) {
                institute.add(new Institute(results));
            }
            results.close();
            stat.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return institute;
    }
    
    public static void insertInstitute(String city, 
                String name, String country, String address, int is_business) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO institute "
                        + "(city, name, country, address, is_business) "
                        + "VALUES (?,?,?,?,?)");
            statement.setString(1, city);
            statement.setString(2, name);
            statement.setString(3, country);
            statement.setString(4, address);
            statement.setInt(5, is_business);
            statement.executeUpdate();
            statement.close();
            System.out.println("preparedstatement werkt");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
        }
    }
    public boolean updateInstitute(String city, 
                String name, String country, String address){
        
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE institute SET city = ?, name = ?, country = ?, address = ? WHERE org_id = ?");
            
            statement.setString(1, city);
            statement.setString(2, name);
            statement.setString(3, country);
            statement.setString(4, address);
            statement.setString(5, org_id);
            statement.executeUpdate();
            
            
            System.out.println("preparedstatement werkt");
        } catch (SQLException error) {
            
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
            return false;
        }
        return true;
    }
    
    
    public boolean deleteInstitute() {
        
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM institute WHERE name = ?");
            
            statement.setString(1, name);
            statement.executeUpdate();
            
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

    public String getOrgid() {
        return org_id;
    }
    
    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }
    
    public String getCountry() {
        return country;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getIsbusiness() {
        return is_business;
    }

    
    
}