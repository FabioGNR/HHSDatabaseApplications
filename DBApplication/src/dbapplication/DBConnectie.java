package dbapplication;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Macintosh
 */
public class DBConnectie {

    private static String url;
    private static String username;
    private static String password;

    public static void setUsernameAndURL() {
        url = "jdbc:mysql://meru.hhs.nl:3306/15086836";
        username = "15086836";
        password = "Loh4rirei4";

    }

    public static java.sql.Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

}
