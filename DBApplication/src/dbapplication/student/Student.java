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
    protected ArrayList<Enrollment> enrollments;
    protected ArrayList<PhoneNumber> phoneNumbers;
    protected int studentid;
    protected Gender gender;
    protected String name, email;
    protected String[] cellData;

    public Student(ResultSet result, ResultSet numbersSet, 
            ResultSet enrollmentsSet) throws SQLException {
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
                phoneNumbers.add(new PhoneNumber(numbersSet));
            }
        }
        enrollments = new ArrayList<>();
        if(enrollmentsSet != null) {
            while (enrollmentsSet.next()) {
                enrollments.add(new Enrollment(enrollmentsSet, enrollmentsSet));
            }
        }
        refreshCellData();
    }

    public ArrayList<Enrollment> getEnrollments() {
        return enrollments;
    }
    
    protected static boolean insertNewStudent(int student_id, String name, 
            Gender gender, String email, ArrayList<PhoneNumber> numbers) {
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
            statement.close();
            // add phone numbers
            for(int i = 0; i < numbers.size(); i++) {
                PhoneNumber number = numbers.get(i);
                PhoneNumber.insertNumber(student_id, 
                        number.getNumber(), number.isCellular());
            }
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
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
            return false;
        }
        return true;
    }
    
    protected static ResultSet requestEnrollments(int student_id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                "SELECT student_id, ex_program, acquired_credits, "
                        + "registration_date, P.description, "
                        + "P.`code`, P.`name`, P.max_credit FROM enrollment E "
                        + "JOIN ex_program P ON P.`code` = ex_program WHERE student_id=?");
            statement.setInt(1, student_id);
            ResultSet set = statement.executeQuery();
            statement.closeOnCompletion();
            return set;
        }
        catch(Exception error) {
            error.printStackTrace();
            return null;
        }
    }
    
    protected static ResultSet requestPhoneNumbers(int student_id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT phonenr, is_cell, student_id "
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
        } catch (SQLException error) {           
            System.out.println("Error: " + error.getMessage());
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
