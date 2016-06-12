package dbapplication.program;

import dbapplication.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author RLH
 */
public class Internship extends ExProgram {

    private String exProgram;

    public Internship(ResultSet result) throws SQLException {
        super(result);
        exProgram = result.getString("code");
    }

    public String getProgram() {
        return exProgram;
    }
    
    @Override
    public boolean delete(){
        Connection connect = DBConnection.getConnection();
        String sql = "DELETE FROM internship WHERE code = ?";
        try{
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, exPcode);
            statement.executeUpdate();
            System.out.println("PreparedStatement was succesful");
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        return super.delete();
    }
    
    // array of terms and why static?
    public static boolean insertNewInternship(String orgID, String name, boolean[] terms){
        Connection connect = DBConnection.getConnection();
        if(!ExProgram.insertExProgram(name, terms)){
            return false;
        }
        String sql = "INSERT INTO internship (org_id, name) VALUES (?,?)";
        try{
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, orgID);
            statement.setString(2, name);
            statement.executeUpdate();
            System.out.println("Inserting new Internship was succesful");
        }catch(SQLException e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        return true;
    }
}
