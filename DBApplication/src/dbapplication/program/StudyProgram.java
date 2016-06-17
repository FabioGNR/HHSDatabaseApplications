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
    public enum StudyType{
        Minor, EPS, SummerSchool{
            @Override
            public String toString(){
                return "Summer School";
            }
        }
    }

    private int org_id;
    private StudyType type;
    private String study_code;

    public StudyProgram(ResultSet result, ResultSet termSet) throws SQLException {
        super(result, termSet);
        org_id = result.getInt("org_id");
        String studyType = result.getString("type");
        StudyType[] studyTypes = StudyType.values();
        for(int i = 0; i < studyTypes.length; i++){
            if(studyTypes[i].name().equals(studyType)){
                type = studyTypes[i];
                break;
            }
        }
        study_code = result.getString("study_code");
    }

    public static ArrayList<ExProgram> searchStudyProgram(String filter, String conditionColumn) {
        ArrayList<ExProgram> programs = new ArrayList<>();
        String sql = "SELECT EX.code, EX.max_credit, SP.type, description, "
                + "SP.study_code, EX.name, SP.org_id, I.`name` as inst_name \n"
                + "FROM ex_program EX JOIN study_program SP ON EX.code = SP.code "
                + "JOIN institute I ON I.org_id = SP.org_id "
                + "WHERE EX.`" + conditionColumn + "` LIKE ?\n"
                + "ORDER BY EX.`name` asc";
        try {
            PreparedStatement statement = DBConnection.getConnection().prepareStatement(
                    sql);
            statement.setString(1, "%" + filter + "%");
            ResultSet results = statement.executeQuery();
            while (results.next()) {
                ResultSet termSet = requestTerms(results.getInt("code"));
                programs.add(new StudyProgram(results, termSet));
                termSet.close();
            }
            statement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Search studyProgram failed.");
        }
        return programs;
    }

    @Override
    public boolean save() {
        if (!super.save()) {
            return false;
        }
        Connection connection = DBConnection.getConnection();
        String sql = "UPDATE study_program SET org_id = ?, type = ?, study_code = ? WHERE code = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, org_id);
            statement.setString(2, type.name());
            statement.setString(3, study_code);
            statement.setInt(4, code);
            statement.executeUpdate();
            statement.close();
        } catch (Exception error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("Updating studyProgram failed.");
            return false;
        }
        return true;
    }

    @Override
    public boolean delete() {
        Connection connect = DBConnection.getConnection();
        String sql = "DELETE FROM study_program WHERE code = ?";
        try {
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, code);
            statement.executeUpdate();
            System.out.println("Study Program was succesfully deleted.");
        } catch (SQLException e) {
            System.out.println("Failed to delete studyProgram. " + "\n" + e.getMessage());
            return false;
        }
        return super.delete();
    }

    public static boolean insertNewStudyProgram(String name, boolean[] terms,
            int maxCredit, String description, int org_id, String type, String studyCode) {
        Connection connect = DBConnection.getConnection();
        int code = ExProgram.insertExProgram(name, terms, maxCredit, description);
        if (code <= -1) {
            return false;
        }
        String sql = "INSERT INTO study_program (code, org_id, type, study_code) VALUES (?,?,?,?)";
        try {
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, code);
            statement.setInt(2, org_id);
            statement.setString(3, type);
            statement.setString(4, studyCode);
            statement.executeUpdate();
            System.out.println("A new studyProgram was succesfully added.");
        } catch (SQLException e) {
            System.out.println("Failed to add a study program \n " + e.getMessage());
            return false;
        }
        return true;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public void setStudyType(StudyType type) {
        this.type = type;
    }

    public void setStudy_code(String study_code) {
        this.study_code = study_code;
    }

    public int getOrg_id() {
        return org_id;
    }

    public StudyType getStudyType() {
        return type;
    }

    public String getStudy_code() {
        return study_code;
    }

}
