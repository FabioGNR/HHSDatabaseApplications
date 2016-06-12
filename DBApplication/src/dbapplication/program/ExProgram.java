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
    protected String name, exPcode;
    protected String[] cellData;
    protected boolean[] terms = new boolean[5];

    public ExProgram(ResultSet result) throws SQLException {
        name = result.getString("name");
        exPcode = result.getString("code");
        cellData = new String[]{exPcode, name};
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

    protected static boolean insertExProgram(String name, boolean[] terms) {
        Connection connection = DBConnection.getConnection();
        String sql = "INSERT INTO ex_program (name) VALUES (?)";
        String sql2 = "INSERT INTO ex_program_term (code, term) VALUES (?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.executeUpdate();
            System.out.println("Preparedstatement passed ");
            ResultSet set = statement.getGeneratedKeys();
            set.next();
            int code = set.getInt(1);
            statement.close();
            for(int i = 0; i < terms.length; i++){
                if(terms[i]){
            PreparedStatement statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, code);
            statement2.setString(2, (i+1) + "");
            statement2.executeUpdate();
            statement2.close();;
                }
            }
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
