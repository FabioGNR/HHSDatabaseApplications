package dbapplication.student;

import dbapplication.DBConnection;
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
    public Student(ResultSet result) throws SQLException
    {
        studentid = result.getString("student_id");
        name = result.getString("name");
        gender = result.getString("gender");
        email = result.getString("email");
        cellData = new String[] {studentid, name, gender, email};
    }
    
    public static ArrayList<Student> searchStudents(String filter, String conditionColumn) {
        ArrayList<Student> students = new ArrayList<>();
        try {
            // column names can't be set dynamically with preparedstatement
            // luckily conditionColumn isn't user input
            PreparedStatement stat = DBConnection.getConnection().prepareStatement(
                    "SELECT * FROM student WHERE `"+conditionColumn+"` LIKE ?");
            stat.setString(1, "%"+filter+"%");
            ResultSet results = stat.executeQuery();
            while(results.next()) {
                students.add(new Student(results));
            }
            results.close();
            stat.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return students;
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
