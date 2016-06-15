package dbapplication.program;

import dbapplication.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author RLH
 */
public class ExProgram {

    enum ProgramType {
        Internship, StudyProgram
    }
    protected String name, exPcode, maxCredit;
    protected String[] cellData;
    protected boolean[] terms = new boolean[5];

    public ExProgram(ResultSet result) throws SQLException {
        name = result.getString("name");
        exPcode = result.getString("code");

        cellData = new String[]{exPcode, name, maxCredit};
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

    protected static int insertExProgram(String name, boolean[] terms, String maxCredit) {
        Connection connection = DBConnection.getConnection();
        String insertExProgram = "INSERT INTO ex_program (name, max_credit) VALUES (?,?)";
        String insertTerm = "INSERT INTO ex_program_term (code, term) VALUES (?,?)";
        int code = -1;

        try {
            PreparedStatement exProgramStatement = connection.prepareStatement(insertExProgram, Statement.RETURN_GENERATED_KEYS);
            exProgramStatement.setString(1, name);
            exProgramStatement.setString(2, maxCredit);
            exProgramStatement.executeUpdate();
            System.out.println("Preparedstatement passed ");
            ResultSet set = exProgramStatement.getGeneratedKeys();
            set.next();
            code = set.getInt(1);
            exProgramStatement.close();

            for (int i = 0; i < terms.length; i++) {
                if (terms[i]) {
                    PreparedStatement termStatement = connection.prepareStatement(insertTerm);
                    termStatement.setInt(1, code);
                    termStatement.setString(2, (i + 1) + "");
                    termStatement.executeUpdate();
                    termStatement.close();
                }
            }
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement was not succesful");
        }
        return code;
    }

    public boolean update(String name, String maxCredit) {
        Connection connect = DBConnection.getConnection();
        String sql = "UPDATE ex_program SET name = ?, max_credit = ?";
        try {
            PreparedStatement updateStatement = connect.prepareStatement(sql);
            updateStatement.setString(1, name);
            updateStatement.setString(2, maxCredit);
            updateStatement.executeUpdate();
            updateStatement.close();
        } catch (SQLException e) {
            e.getMessage();
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
