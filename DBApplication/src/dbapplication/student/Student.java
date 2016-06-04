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
public class Student {

    enum StudentType {
        Exchange, HHS
    }
    
    protected String name, studentid, gender, email;
    protected String[] cellData;

    public Student(ResultSet result) throws SQLException {
        studentid = result.getString("student_id");
        name = result.getString("name");
        gender = result.getString("gender");
        email = result.getString("email");
        cellData = new String[]{studentid, name, gender, email};
    }

    public static ArrayList<Student> searchStudents(String filter, String conditionColumn) {
        ArrayList<Student> students = new ArrayList<>();
        try {
            // column names can't be set dynamically with preparedstatement
            // luckily conditionColumn isn't user input
            PreparedStatement stat = DBConnection.getConnection().prepareStatement(
                    "SELECT * FROM student WHERE `" + conditionColumn + "` LIKE ?");
            stat.setString(1, "%" + filter + "%");
            ResultSet results = stat.executeQuery();
            while (results.next()) {
                students.add(new Student(results));
            }
            results.close();
            stat.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return students;
    }

    protected static boolean insertNewStudent(String student_id, String name, String gender, String email) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO student "
                    + "(student_id,name, gender, email) "
                    + "VALUES (?,?,?,?)");

            statement.setString(1, student_id);
            statement.setString(2, name);
            statement.setString(3, gender);
            statement.setString(4, email);
            statement.executeUpdate();
            System.out.println("Preparedstatement werkt ");
            statement.close();
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet :(");
            return false;
        }  
        return true;
    }

    public boolean delete() {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM student WHERE student_id=?");
            statement.setString(1, studentid);
            statement.executeUpdate();

            System.out.println("Preparedstatement werkt!");
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

    public String getName() {
        return name;
    }

    public String getStudentid() {
        return studentid;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

}
