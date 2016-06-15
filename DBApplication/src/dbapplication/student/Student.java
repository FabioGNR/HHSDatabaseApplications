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
        refreshCellData();
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
    
    public boolean save() {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE student SET name=?, gender=?, email=? WHERE student_id=?");
            statement.setString(1, name);
            statement.setString(2, gender);
            statement.setString(3, email);
            statement.setString(4, studentid);
            statement.executeUpdate();
        } catch (Exception error) {
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
    
    public void refreshCellData() {
        cellData = new String[]{studentid, name, gender, email};
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

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    

}
