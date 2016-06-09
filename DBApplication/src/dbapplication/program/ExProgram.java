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

    enum ProgramType {
        Internship, StudyProgram
    }
    protected String name, exPcode, term;
    // of beter array van terms maken?
    // hoe wordt exPcode aangemaakt?
    protected String[] cellData;

    public ExProgram(ResultSet result) throws SQLException {
        name = result.getString("name");
        exPcode = result.getString("code");
        term = result.getString("term");
        cellData = new String[]{exPcode, name, term};
    }

    public static ArrayList<ExProgram> searchExProgram(String searchFilter, String conditionColumn) {
        ArrayList<ExProgram> program = new ArrayList<>();
        String sql = "SELECT * FROM ex_program WHERE `" + conditionColumn + "` LIKE ?";
        try {
            PreparedStatement state = DBConnection.getConnection().prepareStatement(sql);
            state.setString(1, "%" + searchFilter + "%");
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

    protected static boolean insertExProgram(String name, String term) {
        Connection connection = DBConnection.getConnection();
        String sql = "INSERT INTO ex_program (name, term) VALUES (?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, exPcode);
            statement.setString(1, name);
            statement.setString(2, term);
            statement.executeUpdate();
            System.out.println("Preparedstatement passed ");
            statement.close();
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement was not succesful");
            return false;
        }
        return true;
    }

    public boolean delete() {
        Connection connect = DBConnection.getConnection();
        String sql = "DELETE FROM ex_program WHERE code = ?";
        try {
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, exPcode);
            statement.executeUpdate();
            System.out.println("PreparedStatement was succesful");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public String getExPcode() {
        return exPcode;
    }

    public String getDataAt(int cell) {
        return cellData[cell];
    }

}
