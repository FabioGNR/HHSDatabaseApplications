package dbapplication.institute;

import dbapplication.DBConnection;
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
            PreparedStatement stat = DBConnection.getConnection().prepareStatement(
                    "SELECT * FROM institute WHERE ? LIKE ?");
            stat.setString(1, conditionColumn);
            stat.setString(2, "%"+filter+"%");
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