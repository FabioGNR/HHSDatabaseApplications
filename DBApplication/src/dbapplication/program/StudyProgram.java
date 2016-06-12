package dbapplication.program;

import dbapplication.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RLH
 */
public class StudyProgram extends ExProgram {

    private String org_id, studyType, study_code, maxCredit; // wat is study_org_id? and 2x org_id?

    public StudyProgram(ResultSet result) throws SQLException {
        super(result);
        org_id = result.getString("org_id");
        studyType = result.getString("type");
        study_code = result.getString("study_code");
        maxCredit = result.getString("max_credit");
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

    public static boolean insertNewStudyProgram(String name, String term,
            String org_id, String studyType, String maxCredits, String studyCode) {
        Connection connect = DBConnection.getConnection();
//        if (!ExProgram.insertExProgram(name, term)) {
//            return false;
//        }
        String sql = "INSERT INTO study_program (org_id, type, max_credits, study_code) VALUES (?,?,?,?,)";
        try {
            PreparedStatement statement = connect.prepareStatement(sql);
//            statement.setString(1, exPcode);
            statement.setString(1, org_id);
            statement.setString(2, studyType);
            statement.setString(3, maxCredits);
            statement.setString(4, studyCode);
            statement.executeUpdate();
            System.out.println("A new studyProgram was succesfully added.");
        } catch (SQLException e) {
            System.out.println("Failed to add a study program: " + e.getMessage());
            return false;
        }
        return true;
    }
}
