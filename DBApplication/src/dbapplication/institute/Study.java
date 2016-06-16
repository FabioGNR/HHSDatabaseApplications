package dbapplication.institute;

import dbapplication.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author jordain & sishi
 */
public class Study {

    private String code, email, phonenr;
    private String[] cellData;

    public Study(ResultSet result) throws SQLException {
        code = result.getString("code");
        email = result.getString("email");
        phonenr = result.getString("phonenr");

        cellData = new String[]{code, email, phonenr};
    }

    public static ArrayList<Study> searchStudy(String filter, 
            String conditionColumn, int institute) {
        ArrayList<Study> study = new ArrayList<>();
        try {
            // column names can't be set dynamically with preparedstatement
            // luckily conditionColumn isn't user input
            PreparedStatement stat = DBConnection.getConnection().prepareStatement(
                    "SELECT `code`, org_id, email, phonenr FROM study WHERE `" + conditionColumn + "` LIKE ?"
                            + " AND org_id=?");

            stat.setString(1, "%" + filter + "%");
            stat.setInt(2, institute);
            ResultSet results = stat.executeQuery();
            while (results.next()) {
                study.add(new Study(results));
            }
            stat.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return study;
    }

    public static void insertStudy(String code,
            String email, String phonenr , int org_id) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO study "
                    + "(code, email, phonenr ,org_id) "
                    + "VALUES (?,?,?,?)");
            statement.setString(1, code);
            statement.setString(2, email);
            statement.setString(3, phonenr);
             statement.setInt(4, org_id);

            statement.executeUpdate();
            statement.close();
            System.out.println("preparedstatement werkt");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
        }
    }

    public boolean updateStudy(String code,
            String email, String phonenr) {

        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE study SET code = ?, email = ?, phonenr = ? WHERE code = ?");

            statement.setString(1, code);
            statement.setString(2, email);
            statement.setString(3, phonenr);
            statement.setString(4, code);
            statement.executeUpdate();
            statement.close();

            System.out.println("Preparedstatement werkt");
        } catch (SQLException error) {

            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
            return false;
        }
        return true;
    }

    public boolean deleteStudy() {

        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM study WHERE code = ?");

            statement.setString(1, code);
            statement.executeUpdate();
            statement.close();

            System.out.println("preparedstatement werkt");
        } catch (SQLException error) {

            System.out.println("Error: " + error.getMessage());
            System.out.println("preparedstatement werkt niet");
            return false;
        }
        return true;

    }

    public String getDataAt(int cell) {
        return cellData[cell];
    }

    public String getCode() {
        return code;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phonenr;
    }

}
