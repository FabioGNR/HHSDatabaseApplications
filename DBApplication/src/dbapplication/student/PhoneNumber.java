package dbapplication.student;

import dbapplication.DBConnection;
import dbapplication.DatabaseTableClass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author fabio
 */
public class PhoneNumber extends DatabaseTableClass {

    private String number, existingNumber;
    private int student_id = -1;
    private boolean isCellular, existsInDB, needsUpdate;
    private String[] cellData;

    public PhoneNumber(ResultSet set) throws SQLException {
        this.number = set.getString("phonenr");
        existingNumber = number;
        this.isCellular = set.getBoolean("is_cell");
        student_id = set.getInt("student_id");
        existsInDB = true;
        needsUpdate = false;
        refreshCellData();
    }

    public PhoneNumber(String number, boolean cellular) {
        this.number = number;
        this.isCellular = cellular;
        refreshCellData();
        needsUpdate = false;
        existsInDB = false;
    }

    public static boolean insertNumber(int student_id, String phonenr, boolean is_cellular) {
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement phoneStat = connection.prepareStatement(
                    "INSERT INTO student_phone "
                    + "(student_id,phonenr, is_cell) "
                    + "VALUES (?,?,?)");
            phoneStat.setInt(1, student_id);
            phoneStat.setString(2, phonenr);
            phoneStat.setInt(3, is_cellular ? 1 : 0);
            phoneStat.executeUpdate();
            phoneStat.close();
            return true;
        }
        catch(Exception error) {
            return false;
        }
    }

    public void refreshCellData() {
        cellData = new String[]{number, isCellular ? "Yes" : "No"};
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
        needsUpdate = true;
    }

    public boolean isCellular() {
        return isCellular;
    }

    public void setCellular(boolean isCellular) {
        this.isCellular = isCellular;
        needsUpdate = true;
    }

    @Override
    public String getDataAt(int cell) {
        return cellData[cell];
    }

    boolean existsInDB() {
        return existsInDB;
    }

    void setExistsInDB(boolean b) {
        existsInDB = b;
    }

    boolean needsUpdate() {
        return needsUpdate;
    }

    public boolean delete() {
        if(!existsInDB) return false;
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement deleteStat = connection.prepareStatement(
                    "DELETE FROM student_phone "
                            + "WHERE student_id=? AND phonenr=?");
            deleteStat.setInt(1, student_id);
            // use existing program code instead of potentially updated program
            deleteStat.setString(2, number);
            deleteStat.executeUpdate();
            deleteStat.close();
        }
        catch (Exception error) {
            return false;
        }
        return true;        
    }
    
    public boolean save() {
        if(!existsInDB) return false;
        try {
            Connection connection = DBConnection.getConnection();
            PreparedStatement updateStat = connection.prepareStatement(
                    "UPDATE student_phone "
                            + "SET phonenr=?, is_cell=? "
                            + "WHERE student_id=? AND phonenr=?");
            updateStat.setString(1, number);
            updateStat.setInt(2, isCellular ? 1 : 0);
            updateStat.setInt(3, student_id);
            updateStat.setString(4, existingNumber);
            needsUpdate = false;
            existingNumber = number;
            updateStat.executeUpdate();
            updateStat.close();
        }
        catch (Exception error) {
            System.out.println(error.getMessage());
            return false;
        }
        return true;
    }

}
