package dbapplication.student;

import dbapplication.DBConnection;
import dbapplication.DatabaseTableClass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author fabio
 */
public class Student extends DatabaseTableClass {

    public enum StudentType {
        Exchange, HHS
    }
    
    public enum Gender {
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
    
    protected ArrayList<PhoneNumber> phoneNumbers;
    protected int studentid;
    protected Gender gender;
    protected String name, email;
    protected String[] cellData;

    public Student(ResultSet result, ResultSet numbersSet) throws SQLException {
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
        phoneNumbers = new ArrayList<>();
        if(numbersSet != null) {
            while(numbersSet.next()) {
                phoneNumbers.add(new PhoneNumber(
                        numbersSet.getString("phonenr"), 
                        numbersSet.getBoolean("is_cell")));
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
    
    protected static ResultSet requestPhoneNumbers(int student_id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT phonenr, is_cell "
                    + "FROM student_phone WHERE student_id=?");
            statement.setInt(1, student_id);
            statement.closeOnCompletion();
            return statement.executeQuery();
        }
        catch (Exception error) {
            return null;
        }
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

    @Override
    public String getDataAt(int cell) {
        return cellData[cell];
    }

    public String getName() {
        return name;
    }
    
    public ArrayList<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
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
