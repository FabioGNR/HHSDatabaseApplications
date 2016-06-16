package dbapplication.student;

import dbapplication.DBConnection;
import dbapplication.DatabaseTableClass;
import dbapplication.program.ExProgram;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author fabio
 */
public class Enrollment extends DatabaseTableClass {
    private int acquiredCredits;
    private int student_id;
    private Date registrationDate;
    private ExProgram program;
    private String[] cellData;
    
    public Enrollment(ResultSet set, ResultSet programSet) throws SQLException {
        acquiredCredits = set.getInt("acquired_credits");
        student_id = set.getInt("student_id");
        registrationDate = set.getDate("registration_date");
        program = new ExProgram(programSet);
        refreshCellData();
    }
    
    public Enrollment(int student_id, ExProgram program, int acquiredCredits, Date regDate) {
        this.program = program;
        this.student_id = student_id;
        this.acquiredCredits = acquiredCredits;
        registrationDate = regDate;
    }
    
    public void refreshCellData() {
        cellData = new String[] { program.getName(), acquiredCredits+"", 
            program.getMaxCredits()+"", registrationDate.toString()};
    }

    public int getAcquiredCredits() {
        return acquiredCredits;
    }

    public void setAcquiredCredits(int acquiredCredits) {
        this.acquiredCredits = acquiredCredits;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public ExProgram getProgram() {
        return program;
    }

    public void setProgram(ExProgram program) {
        this.program = program;
    }
    
    @Override
    public String getDataAt(int cell) {
        return cellData[cell];
    }
    
    public boolean save() {
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement updateStat = connection.prepareStatement(
                    "UPDATE enrollment "
                            + "SET (acquired_credits, registration_date) "
                            + "VALUES(?,?) WHERE student_id=? AND ex_program=?");
            updateStat.setInt(1, acquiredCredits);
            updateStat.setDate(2, registrationDate);
            updateStat.setInt(3, student_id);
            updateStat.setInt(4, program.getExPcode());
        }
        catch (Exception error) {
            return false;
        }
        return true;
    }
    
    public static boolean insertEnrollment(int student_id, int program, 
            int credits, Date registrationDate) {
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO enrollment (student_id, ex_program, "
                        + "acquired_credits, registration_date) "
                        + "VALUES(?,?,?,?");
            statement.setInt(1, student_id);
            statement.setInt(2, program);
            statement.setInt(3, credits);
            statement.setDate(4, registrationDate);
            statement.executeUpdate();
            statement.close();
        }
        catch(Exception error) {
            
            return false;
        }
        return true;
    }
    
    public static ArrayList<Enrollment> getStudentEnrollments(Student student) {
        Connection connection = DBConnection.getConnection();
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        try {
            PreparedStatement enrollStat = connection.prepareStatement(
                    "SELECT ex_program, acquired_credits, "
                            + "registration_date FROM enrollment "
                            + "WHERE student_id=?");
            enrollStat.setInt(1, student.getStudentid());
            ResultSet enrollSet = enrollStat.executeQuery();
            while(enrollSet.next()) {
                PreparedStatement programStat = connection.prepareStatement(
                    "SELECT name, max_credit FROM ex_program WHERE code=?");
                programStat.setInt(1, enrollSet.getInt("ex_program"));
                ResultSet programSet = programStat.executeQuery();
                enrollments.add(new Enrollment(enrollSet, programSet));
                programStat.close();
            }
            enrollStat.close();
        }
        catch (Exception error){
            error.printStackTrace();
        }
        return enrollments;
    }
    
}
