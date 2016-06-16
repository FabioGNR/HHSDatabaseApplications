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
    private Date registrationDate;
    private ExProgram program;
    private String[] cellData;
    
    public Enrollment(ResultSet set, ResultSet programSet) throws SQLException {
        acquiredCredits = set.getInt("acquired_credits");
        registrationDate = set.getDate("registration_date");
        program = new ExProgram(programSet);
        refreshCellData();
    }
    
    public void refreshCellData() {
        cellData = new String[] { program.getName(), acquiredCredits+"", 
            program.getMaxCredits()+"", registrationDate.toString()};
    }
    
    @Override
    public String getDataAt(int cell) {
        return cellData[cell];
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
