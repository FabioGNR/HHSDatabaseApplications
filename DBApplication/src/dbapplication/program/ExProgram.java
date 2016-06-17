package dbapplication.program;

import dbapplication.DBConnection;
import dbapplication.DatabaseTableClass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author RLH
 */
public class ExProgram extends DatabaseTableClass {

    public static final int Terms = 5;

    public enum ProgramType {
        Internship, StudyProgram {
            @Override
            public String toString() {
                return "Study Program";
            }
        }
    }
    protected String name, description, instituteName;
    protected int maxCredits, code;
    protected String[] cellData;
    protected boolean[] existingTerms;
    protected boolean[] terms = new boolean[Terms];

    public ExProgram(ResultSet result, ResultSet termSet) throws SQLException {
        name = result.getString("name");
        code = result.getInt("code");
        maxCredits = Integer.parseInt(result.getString("max_credit"));
        try {
            instituteName = result.getString("inst_name");
        } catch (Exception e) {
            instituteName = null;
        }
        description = result.getString("description");
        if (termSet != null) {
            while (termSet.next()) {
                // convert 1-5 to index ( 0-4 )
                int term = Integer.parseInt(termSet.getString("term")) - 1;
                terms[term] = true;
            }
        }
        existingTerms = terms.clone();
        refreshCellData();
    }

    public ExProgram(ResultSet result) throws SQLException {
        this(result, null);
    }

    public void refreshCellData() {
        cellData = new String[]{name, maxCredits + " ECS", description};
    }
    
    protected static int insertExProgram(String name, boolean[] terms, int maxCredits, String description) {
        Connection connection = DBConnection.getConnection();
        String insertExProgram = "INSERT INTO ex_program (name, max_credit, description) VALUES (?,?,?)";
        String insertTerm = "INSERT INTO ex_program_term (code, term) VALUES (?,?)";
        int code = -1;

        try {
            PreparedStatement exProgramStatement = connection.prepareStatement(
                    insertExProgram, Statement.RETURN_GENERATED_KEYS);
            exProgramStatement.setString(1, name);
            exProgramStatement.setString(2, maxCredits + "");
            exProgramStatement.setString(3, description);
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
            System.out.println("Insert ExProgram was not succesful");
        }
        return code;
    }

    protected static ResultSet requestTerms(int code) {
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement stat = connection.prepareStatement(
                    "SELECT code, term FROM ex_program_term WHERE code=?");
            stat.setInt(1, code);
            ResultSet set = stat.executeQuery();
            stat.closeOnCompletion();
            return set;
        } catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }

    public boolean save() {
        Connection connect = DBConnection.getConnection();
        String sql = "UPDATE ex_program SET name=?, description=?, max_credit=? WHERE code = ?";
        try {
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setString(3, maxCredits+"");
            statement.setInt(4, code);
            statement.executeUpdate();
            System.out.println("Updated ExProgram");
            saveTerms();
            existingTerms = terms.clone();
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("Failed updating ExProgram");
            return false;
        }
        return true;
    }

    private boolean saveTerms() {
        boolean success = true;
        Connection connect = DBConnection.getConnection();
        // save and add enrollments
        for (int i = 0; i < terms.length; i++) {
            int termCode = i+1;
            boolean newState = terms[i];
            boolean oldState = existingTerms[i];
            try {
                PreparedStatement stat = null;
                if (!oldState && newState) {
                    stat = connect.prepareStatement(
                            "INSERT INTO ex_program_term SET code=?, term=?");
                    stat.setInt(1, code);
                    stat.setString(2, termCode+"");
                } else if (oldState && !newState) {
                    stat = connect.prepareStatement(
                            "DELETE FROM ex_program_term WHERE code=? AND term=?");
                    stat.setInt(1, code);
                    stat.setString(2, termCode+"");                    
                }
                if(stat != null) {
                    stat.executeUpdate();
                    stat.close();
                }
            } catch (Exception error) {
                success = false;
            }
        }
        return success;
    }

    public boolean delete() {
        Connection connect = DBConnection.getConnection();
        String sql = "DELETE FROM ex_program WHERE code = ?";
        try {
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, code);
            statement.executeUpdate();
            System.out.println("Deleted ExProgram");
        } catch (SQLException error) {
            System.out.println("Error: " + error.getMessage());
            System.out.println("Failed deleting ExProgram");
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getDataAt(int cell) {
        return cellData[cell];
    }

    public int getMaxCredits() {
        return maxCredits;
    }

    public boolean[] getTerms() {
        return terms;
    }

    public void setTerms(boolean[] terms) {
        this.terms = terms;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxCredit(int maxCredit) {
        this.maxCredits = maxCredit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstituteName() {
        return instituteName;
    }
}
