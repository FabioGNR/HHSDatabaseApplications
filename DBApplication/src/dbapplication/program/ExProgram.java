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
public class ExProgram {
    private String name, programCode;
    private String[] cellData;

    public ExProgram(ResultSet result) throws SQLException {
        name = result.getString("name");
        programCode = result.getString("code");
        cellData = new String[]{"name", "code"};
    }

    public static ArrayList<ExProgram> searchExProgram(String filter, String conditionColumn) {
        ArrayList<ExProgram> program = new ArrayList<>();

        try {
            PreparedStatement state = DBConnection.getConnection().prepareStatement(
                    "SELECT * FROM ex_program WHERE `" + conditionColumn + "` LIKE ?");
            state.setString(1, "%" + filter + "%");
            ResultSet result = state.executeQuery();
            while (result.next()) {
                program.add(new ExProgram(result));
            }
            result.close();
            state.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return program;
    }

    public static void insertInternship(String exPcode, String org_id, String name) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO ex_program "
                    + "(code, name) "
                    + "VALUES (?,?)");

            statement.setString(1, exPcode);
            statement.setString(2, name);

            PreparedStatement statement2 = connection.prepareStatement(
                    "INSERT INTO internship "
                    + "(org_id, name) "
                    + "VALUES (?,?)");

            statement.setString(1, org_id);
            statement.setString(2, name);

            statement2.executeUpdate();
            System.out.println("Preparedstatement passed ");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement was not succesful");
        }

    }

    public static void insertStudyProgram(String exPcode, String name, String org_id, String study, String type, String max_credits) {
        Connection connection = DBConnection.getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO ex_program "
                    + "(code, name) "
                    + "VALUES (?,?)");

            statement.setString(1, exPcode);
            statement.setString(2, name);

            PreparedStatement statement2 = connection.prepareStatement(
                    "INSERT INTO study_program "
                    + "(code, name, org_id, study, type, max_credits) "
                    + "VALUES (?,?,?,?,?,?)");

            statement.setString(1, org_id);
            statement.setString(2, name);
            statement.setString(3, exPcode);
            statement.setString(4, study);
            statement.setString(3, type);
            statement.setString(6, max_credits);

            statement2.executeUpdate();
            System.out.println("Preparedstatement passed ");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement was not succesful");
        }
    }

    public String getName() {
        return name;
    }

    public String getProgramCode() {
        return programCode;
    }

    public String getDataAt(int cell) {
        return cellData[cell];
    }

}
