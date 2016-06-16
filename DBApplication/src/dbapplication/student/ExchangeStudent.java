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

    private int uniID;
    private String city, address, uniName;

    public ExchangeStudent(ResultSet result, ResultSet numbers
    , ResultSet enrollmentsSet) throws SQLException {
        super(result, numbers, enrollmentsSet);
        city = result.getString("city");
        address = result.getString("address");
        uniID = result.getInt("uni_id");
        uniName = result.getString("uni_name");
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public int getUniID() {
        return uniID;
    }

    public String getUniName() {
        return uniName;
    }

    @Override
    public boolean save() {
        if (!super.save()) {
            return false;
        }
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE exchange_student SET address=?, city=?, university=? WHERE student_id=?");
            statement.setString(1, address);
            statement.setString(2, city);
            statement.setInt(3, uniID);
            statement.setInt(4, studentid);
            statement.executeUpdate();
            statement.close();
        } catch (Exception error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet :(");
            return false;
        }
        return true;
    }

    @Override
    public boolean delete() {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM exchange_student WHERE student_id=?");
            statement.setInt(1, studentid);
            statement.executeUpdate();
            statement.close();

            System.out.println("Preparedstatement werkt!");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
            return false;
        }
        return super.delete();
    }

    public static boolean insertNewExchangeStudent(int student_id, String name, Gender gender, 
            String email, String city, String address, int university, ArrayList<PhoneNumber> numbers) {
        if (!Student.insertNewStudent(student_id, name, gender, email, numbers)) {
            return false;
        }
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO exchange_student "
                    + "(student_id, city, address, university) "
                    + "VALUES (?, ?, ?, ?)");
            statement.setInt(1, student_id);
            statement.setString(2, city);
            statement.setString(3, address);
            statement.setInt(4, university);

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
                int student_id = results.getInt("student_id");
                ResultSet numbers = requestPhoneNumbers(student_id);
                ResultSet enrollments = requestEnrollments(student_id);
                students.add(new ExchangeStudent(results, numbers, enrollments));
                numbers.close();
                enrollments.close();
            }
            results.close();
            stat.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return students;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUniID(int uniID) {
        this.uniID = uniID;
    }

    public void setUniName(String uniName) {
        this.uniName = uniName;
    }

}
