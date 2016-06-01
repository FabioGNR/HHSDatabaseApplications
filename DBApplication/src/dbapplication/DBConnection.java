package dbapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Macintosh
 */
public class DBConnection {

    private static Connection connection;
    private static final String url = "jdbc:mysql://meru.hhs.nl:3306/15086836";
    private static final String username = "15086836";
    private static final String password = "Loh4rirei4";

    public static boolean connect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static void close() {
        try {
            connection.close();
        } 
        catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public static java.sql.Connection getConnection() {
        return connection;
    }
}
