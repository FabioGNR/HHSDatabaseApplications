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
    
    enum Gender {
        Female {
            @Override
            public String getValue() {
                return "f";
            }
        }, Male {
            @Override
            public String getValue() {
                return "m";
            }
        };
        
        public abstract String getValue();
    }
    
    protected int studentid;
    protected Gender gender;
    protected String name, email;
    protected String[] cellData;

    public Student(ResultSet result) throws SQLException {
        studentid = result.getInt("student_id");
        name = result.getString("name");
        email = result.getString("email");
        // cast gender string to enum
        String sGender = result.getString("gender");
        Gender[] genders = Gender.values();
        for (int i = 0; i < genders.length; i++) {
            if (genders[i].getValue().equals(sGender)) {
                gender = genders[i];
                break;
            }
        } 
        refreshCellData();
    }
    
    protected static boolean insertNewStudent(int student_id, String name, Gender gender, String email) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO student "
                    + "(student_id,name, gender, email) "
                    + "VALUES (?,?,?,?)");

            statement.setInt(1, student_id);
            statement.setString(2, name);
            statement.setString(3, gender.getValue());
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
            statement.setString(2, gender.getValue());
            statement.setString(3, email);
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

    public boolean delete() {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM student WHERE student_id=?");
            statement.setInt(1, studentid);
            statement.executeUpdate();
            statement.close();
            System.out.println("Preparedstatement werkt!");
        } catch (SQLException error) {
            
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
            return false;
        }      
        return true;
    }
    
    public void refreshCellData() {
        String ID = String.format("%08d", studentid);
        cellData = new String[]{ID, name, gender.toString(), email};
    }

    public String getDataAt(int cell) {
        return cellData[cell];
    }

    public String getName() {
        return name;
    }

    public int getStudentid() {
        return studentid;
    }

    public Gender getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStudentid(int studentid) {
        this.studentid = studentid;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    

}
