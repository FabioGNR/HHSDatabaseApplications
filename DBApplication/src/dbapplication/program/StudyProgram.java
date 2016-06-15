package dbapplication.program;

import dbapplication.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author RLH
 */
public class StudyProgram extends ExProgram {

    private String org_id, studyType, study_code, maxCredit;

    public StudyProgram(ResultSet result) throws SQLException {
        super(result);
        org_id = result.getString("org_id");
        studyType = result.getString("type");
        study_code = result.getString("study_code");
        maxCredit = result.getString("max_credit");
    }

    public static ArrayList<ExProgram> searchStudyProgram(String filter, String conditionColumn){
        ArrayList<ExProgram> programs = new ArrayList<>();
        String sql = "SELECT EX.code, EX.name, SP.org_id, I.org_id \n"
                + "FROM ex_program EX JOIN studyProgram SP ON EX.code = SP.code " 
                + "JOIN I ON I.org_id = SP.org_id"
                + "WHERE ex_program. `" + conditionColumn + "` LIKE ?\n"
                + "ORDER BY ex_program. `name` asc";
        try{
            PreparedStatement statement = DBConnection.getConnection().prepareStatement(
                    sql);
            statement.setString(1, "%" + filter + "%");
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                programs.add(new Internship(results));
            }
            results.close();
            statement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return programs;
        }
    
    @Override
    public boolean delete() {
        Connection connect = DBConnection.getConnection();
        String sql = "DELETE FROM study_program WHERE code = ?";
        try {
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, exPcode);
            statement.executeUpdate();
            System.out.println("Study Program was succesfully deleted.");
        } catch (SQLException e) {
            System.out.println("Failed to delete: " + e.getMessage());
            return false;
        }
        return super.delete();
    }

    public static boolean insertNewStudyProgram(String name, boolean[] terms,
            String org_id, String studyType, int maxCredits, String studyCode) {
        Connection connect = DBConnection.getConnection();
        int code = ExProgram.insertExProgram(name, terms, maxCredits);
        if (code <= -1) {
            return false;
        }
        String sql = "INSERT INTO study_program (code, org_id, type, study_code) VALUES (?,?,?,?)";
        //studyCode is een name van study bijv hbo ict.
        try {
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, code);
            statement.setString(2, org_id);
            statement.setString(3, studyType);
            statement.setString(4, studyCode);
            statement.executeUpdate();
            System.out.println("A new studyProgram was succesfully added.");
        } catch (SQLException e) {
            System.out.println("Failed to add a study program: " + e.getMessage());
            return false;
        }
        return true;
    }

    public void setOrg_id(String org_id) {
        this.org_id = org_id;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public void setStudy_code(String study_code) {
        this.study_code = study_code;
    }

    public void setMaxCredit(String maxCredit) {
        this.maxCredit = maxCredit;
    }

    public String getOrg_id() {
        return org_id;
    }

    public String getStudyType() {
        return studyType;
    }

    public String getStudy_code() {
        return study_code;
    }

    public String getMaxCredit() {
        return maxCredit;
    }
}
