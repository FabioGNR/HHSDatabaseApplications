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
public class ExchangeStudent extends Student{
    private String city, address, uniID;
    public ExchangeStudent(ResultSet result) throws SQLException {
        super(result);
        city = result.getString("city");
        address = result.getString("address");
        uniID = result.getString("university");       
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getUniID() {
        return uniID;
    }

    @Override
    public boolean delete() {      
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM exchange_student WHERE student_id=?");
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
    
    public static boolean insertNewExchangeStudent(String student_id, String name, 
            String gender, String email, String city, String address, String university) {
        if(!Student.insertNewStudent(student_id, name, gender, email))
            return false;
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO exchange_student "
                    + "(student_id, city, address, university) "
                    + "VALUES (?,?, ?, ?, ?)"); //een vraagteken te veel
            statement.setString(1, student_id);
            statement.setString(2, city);
            statement.setString(3, address);
            statement.setString(4, university);

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
