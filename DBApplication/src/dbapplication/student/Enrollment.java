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
    private int existingProgam;
    private boolean existsInDB, needsUpdate = false;
    
    public Enrollment(ResultSet set, ResultSet programSet) throws SQLException {
        acquiredCredits = set.getInt("acquired_credits");
        student_id = set.getInt("student_id");
        registrationDate = set.getDate("registration_date");
        program = new ExProgram(programSet);
        existingProgam = program.getCode();
        refreshCellData();
        existsInDB = true;
    }
    
    public Enrollment(int student_id, ExProgram program, int acquiredCredits, Date regDate) {
        this.program = program;
        existingProgam = program.getCode();
        this.student_id = student_id;
        this.acquiredCredits = acquiredCredits;
        registrationDate = regDate;
        existsInDB = false;
    }
    
    public void refreshCellData() {
        cellData = new String[] { program.getName(), acquiredCredits+"", 
            program.getMaxCredits()+"", 
            acquiredCredits >= program.getMaxCredits() ? "Yes" : "No", 
            registrationDate.toString()};
    }

    public int getAcquiredCredits() {
        return acquiredCredits;
    }

    public void setAcquiredCredits(int acquiredCredits) {
        this.acquiredCredits = acquiredCredits;
        needsUpdate = true;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
        needsUpdate = true;
    }

    public ExProgram getProgram() {
        return program;
    }

    public void setProgram(ExProgram program) {
        this.program = program;
        needsUpdate = true;
    }
    
    @Override
    public String getDataAt(int cell) {
        return cellData[cell];
    }

    public boolean existsInDB() {
        return existsInDB;
    }

    public void setExistsInDB(boolean existsInDB) {
        this.existsInDB = existsInDB;
    }

    public boolean needsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        this.needsUpdate = needsUpdate;
    }
    
    public boolean delete() {
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement deleteStat = connection.prepareStatement(
                    "DELETE FROM enrollment "
                            + "WHERE student_id=? AND ex_program=?");
            deleteStat.setInt(1, student_id);
            // use existing program code instead of potentially updated program
            deleteStat.setInt(2, existingProgam);
            deleteStat.executeUpdate();
            deleteStat.close();
        }
        catch (Exception error) {
            return false;
        }
        return true;        
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
            updateStat.setInt(4, program.getCode());
            needsUpdate = false;
            updateStat.executeUpdate();
            updateStat.close();
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
