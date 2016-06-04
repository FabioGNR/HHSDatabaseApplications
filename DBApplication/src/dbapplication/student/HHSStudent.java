package dbapplication.student;

import dbapplication.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author fabio
 */
public class HHSStudent extends Student {
    private String localStudy;
    public HHSStudent(ResultSet result) throws SQLException {
        super(result);
        localStudy = result.getString("hhs_study");
    }

    public String getLocalStudy() {
        return localStudy;
    }
    
    @Override
    public boolean delete() {      
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM hhs_student WHERE student_id=?");
            statement.setString(1, studentid);
            statement.executeUpdate();

            System.out.println("Preparedstatement werkt!");
        } catch (SQLException error) { 
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
            return false;
        }
        return super.delete();
    }
    
    public static boolean insertNewHHSStudent(String student_id, String name, 
            String gender, String email, String hhs_study) {
        if(!Student.insertNewStudent(student_id, name, gender, email))
            return false;
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO hhs_student "
                    + "(student_id, hhs_study) "
                    + "VALUES (?,?)");
            statement.setString(1, student_id);
            statement.setString(2, hhs_study);

            statement.executeUpdate();
            statement.close();
            System.out.println("Preparedstatement werkt ");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet :(");
            return false;
        }
        return true;
    }
    
}
