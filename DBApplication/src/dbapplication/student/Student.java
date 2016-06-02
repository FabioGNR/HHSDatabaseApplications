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

    private String name, studentid, gender, email;
    private String[] cellData;

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

    public static void insertHHS_Student(String student_id, String name, String gender, String email, String hhs_study) {
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

            PreparedStatement statement2 = connection.prepareStatement(
                    "INSERT INTO hhs_student "
                    + "(student_id, hhs_study) "
                    + "VALUES (?,?)");
            statement2.setString(1, student_id);
            statement2.setString(2, hhs_study);

            statement2.executeUpdate();
            System.out.println("Preparedstatement werkt ");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet :(");
        }
    }

    public static void insertExchange_Student(String student_id, String name, String gender, String email, String city, String adress, String university) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO student "
                    + "(student_id ,name, gender, email ) "
                    + "VALUES (?,?,?,?)");
            statement.setString(1, student_id);
            statement.setString(2, name);
            statement.setString(3, gender);
            statement.setString(4, email);

            statement.executeUpdate();

            PreparedStatement statement2 = connection.prepareStatement(
                    "INSERT INTO exhange_student "
                    + "(student_id , city, adress , university) "
                    + "VALUES (?,?,?,?)");
            statement2.setString(1, student_id);
            statement2.setString(2, city);
            statement2.setString(3, adress);
            statement2.setString(4, university);

            statement2.executeUpdate();

            System.out.println("Preparedstatement werkt!");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
        }
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
