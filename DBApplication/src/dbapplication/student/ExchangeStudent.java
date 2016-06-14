package dbapplication.student;

import dbapplication.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author fabio
 */
public class ExchangeStudent extends Student {

    private String city, address, uniID, uniName;

    public ExchangeStudent(ResultSet result) throws SQLException {
        super(result);
        city = result.getString("city");
        address = result.getString("address");
        uniID = result.getString("uni_id");
        uniName = result.getString("uni_name");
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

    public String getUniName() {
        return uniName;
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
        if (!Student.insertNewStudent(student_id, name, gender, email)) {
            return false;
        }
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

    public static ArrayList<Student> searchStudents(String filter, String conditionColumn) {
        ArrayList<Student> students = new ArrayList<>();
        try {
            // column names can't be set dynamically with preparedstatement
            // luckily conditionColumn isn't user input
            String query = "SELECT student.`name`, gender, email, student.student_id, E.city, E.address, university as uni_id, I.`name` as uni_name \n"
                    + "FROM student JOIN exchange_student E ON student.student_id=E.student_id "
                    + "JOIN institute I ON I.org_id=E.university "
                    + "WHERE student.`" + conditionColumn + "` LIKE ?\n"
                    + "ORDER BY student.`name` asc";

            PreparedStatement stat = DBConnection.getConnection().prepareStatement(
                    query);
            stat.setString(1, "%" + filter + "%");
            ResultSet results = stat.executeQuery();
            while (results.next()) {
                students.add(new ExchangeStudent(results));
            }
            results.close();
            stat.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return students;
    }

}
