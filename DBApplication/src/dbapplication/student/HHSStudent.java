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
public class HHSStudent extends Student {

    public enum LocalStudy {
        ICT {
            @Override
            public String toString() {
                return "HBO-ICT";
            }
        }, CMD {
            @Override
            public String toString() {
                return "Communication Media Design";
            }
        };

        @Override
        public abstract String toString();
    }

    private LocalStudy localStudy;

    public HHSStudent(ResultSet result, ResultSet numbers, 
            ResultSet enrollmentsSet) throws SQLException {
        super(result, numbers, enrollmentsSet);
        String lStudy = result.getString("hhs_study");
        LocalStudy[] localStudies = LocalStudy.values();
        for (int i = 0; i < localStudies.length; i++) {
            if (localStudies[i].toString().equals(lStudy) ||
                    localStudies[i].name().equals(lStudy)) {
                localStudy = localStudies[i];
                break;
            }
        }
    }

    public LocalStudy getLocalStudy() {
        return localStudy;
    }

    @Override
    public boolean save() {
        if (!super.save()) {
            return false;
        }
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE hhs_student SET hhs_study=? WHERE student_id=?");
            statement.setString(1, localStudy.toString());
            statement.setInt(2, studentid);
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
                    "DELETE FROM hhs_student WHERE student_id=?");
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

    public static boolean insertNewHHSStudent(int student_id, String name,
            Gender gender, String email, LocalStudy hhs_study, ArrayList<PhoneNumber> numbers) {
        if (!Student.insertNewStudent(student_id, name, gender, email, numbers)) {
            return false;
        }
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO hhs_student "
                    + "(student_id, hhs_study) "
                    + "VALUES (?,?)");
            statement.setInt(1, student_id);
            statement.setString(2, hhs_study.toString());

            statement.executeUpdate();
            statement.close();
            System.out.println("Preparedstatement werkt ");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet :(");
            // delete student from supertype table
            try {
                PreparedStatement delete = connection.prepareStatement(
                    "DELETE FROM student WHERE student_id=?");
                delete.setString(1, student_id+"");
                delete.executeUpdate();
                delete.close();
            }
            catch(Exception ex) {

            }
            return false;
        }
        return true;
    }

    public static ArrayList<Student> searchStudents(String filter, String conditionColumn) {
        ArrayList<Student> students = new ArrayList<>();
        try {
            // column names can't be set dynamically with preparedstatement
            // luckily conditionColumn isn't user input
            String query = "SELECT `name`, gender, email, student.student_id, hhs_study \n"
                    + "FROM student JOIN \n"
                    + "hhs_student H ON student.student_id=H.student_id "
                    + "WHERE student.`" + conditionColumn + "` LIKE ? ORDER BY `name` asc";

            PreparedStatement stat = DBConnection.getConnection().prepareStatement(
                    query);
            stat.setString(1, "%" + filter + "%");
            ResultSet results = stat.executeQuery();
            while (results.next()) {
                int student_id = results.getInt("student_id");
                ResultSet numbers = requestPhoneNumbers(student_id);
                ResultSet enrollments = requestEnrollments(student_id);
                students.add(new HHSStudent(results, numbers, enrollments));
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

    public void setLocalStudy(LocalStudy localStudy) {
        this.localStudy = localStudy;
    }
}
