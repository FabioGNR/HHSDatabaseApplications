package dbapplication.program;

import dbapplication.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author RLH
 */
public class Internship extends ExProgram {

    private String exProgram, org_id; //moeten maxCredit en name erbij? en term?

    public Internship(ResultSet result) throws SQLException {
        super(result);
        exProgram = result.getString("code");
        org_id = result.getString("org_id");
    }
    
    public static ArrayList<ExProgram> searchStudents(String filter, String conditionColumn){
        ArrayList<ExProgram> programs = new ArrayList<>();
        String sql = "SELECT EX.code, EX.name, I.org_id, IN.org_id \n"
                + "FROM ex_program EX JOIN internship I ON EX.code = I.code " 
                + "JOIN institute IN ON I.org_id = IN.org_id"
                + "WHERE ex_program. `" + conditionColumn + "` LIKE ?\n"
                + "ORDER BY ex_program. `name` asc";
        try{
            PreparedStatement statement = DBConnection.getConnection().prepareStatement(
                    sql);
            statement.setString(1, "%" + filter + "%");
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                programs.add(new Internship(results));
            }
            results.close();
            statement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return programs;
        }
    

//    @Override
//    public boolean update(){
//        
//    }
    
    @Override
    public boolean delete() {
        Connection connect = DBConnection.getConnection();
        String sql = "DELETE FROM internship WHERE code = ?";
        try {
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, exPcode);
            statement.executeUpdate();
            System.out.println("PreparedStatement was succesful");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        return super.delete();
    }

    public static boolean insertNewInternship(String orgID, String name, boolean[] terms, String maxCredit) {
        Connection connect = DBConnection.getConnection();
        int code = ExProgram.insertExProgram(name, terms, maxCredit);
        if (code <= -1) {
            return false;
        }
        String sql = "INSERT INTO internship (code, org_id) VALUES (?,?)";
        try {
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, code);
            statement.setString(2, orgID);
            statement.executeUpdate();
            System.out.println("Inserting new Internship was succesful");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        return true;
    }

    public String getProgram() {
        return exProgram;
    }

    public String getOrg_id() {
        return org_id;
    }
}
